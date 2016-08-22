#include "ik_solver.h"

extern MathFunction mathFunction;												// 引用数学方法类，机器人的方法中会调用数学方法

//ForwardKinematics：已知关节角度求各个杆件位姿,递归函数按照从BODY到各个child关节的顺序依次计算每个子关节对应连杆的位姿
void IKSolver::forwardKinematics(int j){
	if (j == NONE)
		return;
	if (j != 1){
		int i = myRobot->uLINK[j].mother;
		for(int m=0;m<3;m++){
			myRobot->uLINK[j].p[m] = myRobot->uLINK[i].R[m][0] * myRobot->uLINK[j].b[0] + myRobot->uLINK[i].R[m][1] * myRobot->uLINK[j].b[1] + myRobot->uLINK[i].R[m][2] * myRobot->uLINK[j].b[2] + myRobot->uLINK[i].p[m];
		}
		double** R = mathFunction.rodrigues(myRobot->uLINK[j].a,myRobot->uLINK[j].q);
		for(int m=0;m<3;m++){
			for(int n=0;n<3;n++){
				myRobot->uLINK[j].R[m][n] = myRobot->uLINK[i].R[m][0]*R[0][n] + myRobot->uLINK[i].R[m][1]*R[1][n] + myRobot->uLINK[i].R[m][2]*R[2][n];
			}
		}
		for(int i=0;i<3;i++){
			free(R[i]);
		}
		free(R);
	}
	forwardKinematics(myRobot->uLINK[j].sister);
	forwardKinematics(myRobot->uLINK[j].child);
}

//ForwardKinematics Child：已知关节角度求各个杆件位姿,递归函数按照从i到i所在的关节链的各个child关节的顺序依次计算每个子关节对应连杆的位姿
void IKSolver::forwardKinematicsChild(int j){
	if (j == NONE)
		return;
	if (j != 1){
		int i = myRobot->uLINK[j].mother;
		for(int m=0;m<3;m++){
			myRobot->uLINK[j].p[m] = myRobot->uLINK[i].R[m][0] * myRobot->uLINK[j].b[0] + myRobot->uLINK[i].R[m][1] * myRobot->uLINK[j].b[1] + myRobot->uLINK[i].R[m][2] * myRobot->uLINK[j].b[2] + myRobot->uLINK[i].p[m];
		}
		double** R = mathFunction.rodrigues(myRobot->uLINK[j].a,myRobot->uLINK[j].q);
		for(int m=0;m<3;m++){
			for(int n=0;n<3;n++){
				myRobot->uLINK[j].R[m][n] = myRobot->uLINK[i].R[m][0]*R[0][n] + myRobot->uLINK[i].R[m][1]*R[1][n] + myRobot->uLINK[i].R[m][2]*R[2][n];
			}
		}
		for(int i=0;i<3;i++){
			free(R[i]);
		}
		free(R);
	}
	forwardKinematicsChild(myRobot->uLINK[j].child);
}

//计算雅克比矩阵
double** IKSolver::calcJacobian(int* idx,int jsize){
	double** J = (double**)malloc(sizeof(double*)*DIMENSION);
	double target[DIMENSION];
	for(int i=0;i<DIMENSION;i++){
		target[i] = myRobot->uLINK[idx[jsize]].p[i];
		J[i] = (double*)malloc(sizeof(double)*jsize);
	}
	for(int i=0;i<DIMENSION;i++){
		for(int j=0;j<jsize;j++){
			J[i][j] = 0;
		}
	}
	for(int i=0;i<jsize;i++){
		int j = idx[i];
		double a[DIMENSION] = {0,0,0};
		for(int k=0;k<DIMENSION;k++){
			a[k] = 0;
			for(int m = 0;m<DIMENSION;m++){
				a[k] += myRobot->uLINK[j].R[k][m] * myRobot->uLINK[j].a[m];
			}
		}
		double c[DIMENSION];
		for(int k=0;k<DIMENSION;k++){
			c[k] = target[k] - myRobot->uLINK[j].p[k];
		}
		for(int k=0;k<DIMENSION;k++){
			J[k][i] = (mathFunction.cross(a,c))[k];
		}
	}
	return J;
}

//利用DLS算法的IK算法，可以避免奇异矩阵带来的巨大关节角配置，已知目标关节位置，求到关节to的关节配置
double IKSolver::inverseKinematics(int to,Joint Target){
	double lambda = 0.03;														// LM算法的阻尼系数初值
	double v1 = 10;																// 放大因子
	double v2 = 0.1;															// 缩小因子
	double res;																	// 误差大小(理论)
	int* idx;																	// 从母杆到目标杆的链
	int number;																	// 关节链长度
	double** J;																	// 雅克比矩阵肯定是3*number的，表示n个关节的3维坐标
	double** Jt;																// 雅克比矩阵的转置
	if(to == RLEG_J4 || to == LLEG_J4){											// 如果目标关节在足部，和target位置相关的有4个关节
		number = NUMBER_OF_LOWERBODY_JOINTS;
	}else{
		number = NUMBER_OF_UPPERBODY_JOINTS;
	}
	double* H1 = (double*)malloc(sizeof(double)*number);						// 对角权重矩阵的对角值，WLN算法
	double* H2 = (double*)malloc(sizeof(double)*number);						// 对角权重矩阵的对角值，WLN算法
	double* W = (double*)malloc(sizeof(double)*number);							// WLN的权重矩阵W是一个对角阵
	double** H = (double**)malloc(sizeof(double*)*DIMENSION);					// (拟)海塞矩阵
	double** Jw = (double**)malloc(sizeof(double*)*DIMENSION);					// 矩阵J和W^-1成绩
	double** Wj = (double**)malloc(sizeof(double*)*number);						// 矩阵W^-1和J'的乘积
	double** Ji = (double**)malloc(sizeof(double*)*number);						// DLS最终雅克比矩阵的广义逆
	double** HI = (double**)malloc(sizeof(double*)*DIMENSION);					// DLS矩阵H+lamda*I
	double** iHI = (double**)malloc(sizeof(double*)*DIMENSION);					// DLS矩阵H+lamda*I的逆
	double* t = (double*)malloc(sizeof(double)*(DIMENSION*DIMENSION));			// 临时矩阵，按行存放求逆前的矩阵元素
	double* r = (double*)malloc(sizeof(double)*(DIMENSION*DIMENSION));			// 临时矩阵，按行存放求逆后的矩阵元素
	double* dq = (double*)malloc(sizeof(double)*number);						// 角度增量
	double* err;																// target和uLINK[idx[end]]的位置偏差
	double e;																	// target和uLINK[idx[end]]的位置偏差大小
	int updateJ = 1;															// 根据一次迭代的误差变化决定是否重新更新关节配置
	for(int i=0;i<DIMENSION;i++){
		H[i] = (double*)malloc(sizeof(double)*DIMENSION);
		Jw[i] = (double*)malloc(sizeof(double)*number);
		HI[i] = (double*)malloc(sizeof(double)*DIMENSION);
		iHI[i] = (double*)malloc(sizeof(double)*DIMENSION);
	}
	/* 参数to可取:RLEG_J4、LLEG_J4、RARM_J2、RARM_J4、LARM_J2、LARM_J4、LEG_J4、REG_J4 */
	idx = myRobot->findRoute(to);												// 找到从BODY到达关节to所在连杆的母连杆的所有关节
	if(idx == NULL){															// 一般情况是目标关节to输入不正确
		printf("请输入正确的目的关节链\n");
		exit(0);
	}
	forwardKinematicsChild(idx[0]);
	for(int i=0;i<number;i++){
		Wj[i] = (double*)malloc(sizeof(double)*DIMENSION);
		Ji[i] = (double*)malloc(sizeof(double)*DIMENSION);
		int index = idx[i];
		H1[i] = pow(myRobot->uLINK[index].maxq-myRobot->uLINK[index].minq,2)*(2*myRobot->uLINK[index].q-myRobot->uLINK[index].minq-myRobot->uLINK[index].maxq)/(4*pow(myRobot->uLINK[index].maxq-myRobot->uLINK[index].q,2)*pow(myRobot->uLINK[index].q-myRobot->uLINK[index].minq,2));
	}
	for(int i=0;i<MAX_LOOP_TIME;i++){
		if(updateJ == 1){
			for(int x=0;x<number;x++){
				int index = idx[x];
				H2[x] = pow(myRobot->uLINK[index].maxq-myRobot->uLINK[index].minq,2)*(2*myRobot->uLINK[index].q-myRobot->uLINK[index].minq-myRobot->uLINK[index].maxq)/(4*pow(myRobot->uLINK[index].maxq-myRobot->uLINK[index].q,2)*pow(myRobot->uLINK[index].q-myRobot->uLINK[index].minq,2));
				if(fabs(H2[x])-fabs(H1[x])>0){
					W[x] = 1.0/(1+fabs(H2[x]));
				}else{
					W[x] = 1;
				}
			}
			J = calcJacobian(idx,number);
			err = mathFunction.calcVWerr(Target,myRobot->uLINK[to]);							// 当前关节位姿目标关节位姿的偏差
			for(int x=0;x<DIMENSION;x++){
				for(int y=0;y<number;y++){
					Jw[x][y] = J[x][y]*W[y];
				}
			}
			Jt = mathFunction.transposition(J,DIMENSION,number);	
			for(int x=0;x<DIMENSION;x++){
				free(J[x]);
				for(int y=0;y<DIMENSION;y++){
					H[x][y] = 0;
					for(int z=0;z<number;z++){
						H[x][y] += Jw[x][z]*Jt[z][y];
					}
				}
			}
			free(J);
			if(i==0){
				e = mathFunction.val2(err);
			}
		}
		if(e < IK_ERR){															// 理论偏差精度为0.001m
			break;
		}
		for(int x=0;x<number;x++){
			for(int y=0;y<DIMENSION;y++){
				Wj[x][y] = W[x]*Jt[x][y];
			}
		}
		for(int x = 0;x<DIMENSION;x++){
			for(int y=0;y<DIMENSION;y++){
				if(x==y){
					HI[x][y] = H[x][y]+lambda;
				}else{
					HI[x][y] = H[x][y];
				}
			}
		}
		// 可能一行的最后一个元素和下一行的第一个元素不连续，所以这里一定要将二维矩阵按顺序放到数组里
		for(int x = 0;x<DIMENSION;x++){
			for(int y = 0;y<DIMENSION;y++){
				t[DIMENSION*x+y] = HI[x][y];
			}
		}
		funcinv(t,r);
		for(int x =0;x < DIMENSION;x++){
			for(int y=0; y< DIMENSION;y++){
				iHI[x][y] = r[x * DIMENSION + y];
			}
		}
		for(int x=0;x<number;x++){
			for(int y=0;y<DIMENSION;y++){
				Ji[x][y] = 0;
				for(int z=0;z<DIMENSION;z++){
					Ji[x][y] += Wj[x][z]*iHI[z][y];
				}
			}
		}
		for(int x=0;x<number;x++){
			dq[x] = 0;
			for(int y=0;y<DIMENSION;y++){
				dq[x] += Ji[x][y]*err[y];
			}
		}
		for(int x=0;x<number;x++){
			int index = idx[x];
			myRobot->uLINK[index].q += dq[x];
		}
		forwardKinematicsChild(idx[0]);
		free(err);
		err = mathFunction.calcVWerr(Target,myRobot->uLINK[to]);
		if(mathFunction.val2(err) < e){
			free(err);
			lambda = lambda*v2;
			e = mathFunction.val2(err);
			for(int x=0;x<number;x++){
				H1[x] = H2[x];
			}
			updateJ = 1;
			for(int x=0;x<number;x++){
				free(Jt[x]);
			}
			free(Jt);
		}else{
			free(err);
			for(int x=0;x<number;x++){
				int index = idx[x];
				myRobot->uLINK[index].q = myRobot->uLINK[index].q - dq[x];
			}
			forwardKinematicsChild(idx[0]);
			updateJ = 0;
			lambda = lambda*v1;
			err = mathFunction.calcVWerr(Target,myRobot->uLINK[to]);
		}
	}
	// 限制关节角度防止越界
	for(int x = 0;x<number;x++){
		int index = idx[x];
		if(myRobot->uLINK[index].q > myRobot->uLINK[index].maxq){
			myRobot->uLINK[index].q = myRobot->uLINK[index].maxq;
		}
		if(myRobot->uLINK[index].q < myRobot->uLINK[index].minq){
			myRobot->uLINK[index].q = myRobot->uLINK[index].minq;
		}
	}
	forwardKinematicsChild(idx[0]);
	err = mathFunction.calcVWerr(Target,myRobot->uLINK[to]);
	res = mathFunction.val2(err);
	free(idx);
	for(int i=0;i<DIMENSION;i++){
		free(H[i]);
		free(Jw[i]);
		free(HI[i]);
		free(iHI[i]);
	}
	free(H);
	free(Jw);
	free(HI);
	free(iHI);
	for(int i=0;i<number;i++){
		free(Wj[i]);
		free(Ji[i]);
	}
	free(Wj);
	free(Ji);
	free(H1);
	free(H2);
	free(W);
	free(t);
	free(r);
	free(dq);
	return res;
}

//对上半身使用IK求解关节角度配置
double IKSolver::ikUpperBody(vect RSE,vect REH,vect LSE,vect LEH){
	Joint RHand,LHand;																	// 目标关节状态
	double result,result1,result2,result3,result4;										// 理论IK配置误差
	RHand.p[0] = myRobot->uLINK[RARM_J1].p[0] + RSE.x*SE_LENGTH;
	RHand.p[1] = myRobot->uLINK[RARM_J1].p[1] + RSE.y*SE_LENGTH;
	RHand.p[2] = myRobot->uLINK[RARM_J1].p[2] + RSE.z*SE_LENGTH;
	result1 = inverseKinematics(RARM_J2,RHand);
	LHand.p[0] = myRobot->uLINK[LARM_J1].p[0] + LSE.x*SE_LENGTH;
	LHand.p[1] = myRobot->uLINK[LARM_J1].p[1] + LSE.y*SE_LENGTH;
	LHand.p[2] = myRobot->uLINK[LARM_J1].p[2] + LSE.z*SE_LENGTH;
	result2 = inverseKinematics(LARM_J2,LHand);
	RHand.p[0] = myRobot->uLINK[RARM_J3].p[0] + REH.x*ELBOWtoWRIST;
	RHand.p[1] = myRobot->uLINK[RARM_J3].p[1] + REH.y*ELBOWtoWRIST;
	RHand.p[2] = myRobot->uLINK[RARM_J3].p[2] + REH.z*ELBOWtoWRIST;
	result3 = inverseKinematics(RARM_J4,RHand);
	LHand.p[0] = myRobot->uLINK[LARM_J3].p[0] + LEH.x*ELBOWtoWRIST;
	LHand.p[1] = myRobot->uLINK[LARM_J3].p[1] + LEH.y*ELBOWtoWRIST;
	LHand.p[2] = myRobot->uLINK[LARM_J3].p[2] + LEH.z*ELBOWtoWRIST;
	result4 = inverseKinematics(LARM_J4,LHand);
	result = result1 + result2 + result3 + result4;
	return result;
}

//对下半身使用IK求解关节角度配置(single support)
double IKSolver::ikLowerBody(vect RHK,vect RKA,vect LHK,vect LKA){
	Joint RFoot,LFoot;															// 目标关节状态
	double result,result1,result2,result3,result4;								// 理论IK配置误差
	RFoot.p[0] = myRobot->uLINK[RLEG_J2].p[0] + RHK.x*LEGtoKNEE;
	RFoot.p[1] = myRobot->uLINK[RLEG_J2].p[1] + RHK.y*LEGtoKNEE;
	RFoot.p[2] = myRobot->uLINK[RLEG_J2].p[2] + RHK.z*LEGtoKNEE;
	result1 = inverseKinematics(RLEG_J3,RFoot);
	LFoot.p[0] = myRobot->uLINK[LLEG_J2].p[0] + LHK.x*LEGtoKNEE;
	LFoot.p[1] = myRobot->uLINK[LLEG_J2].p[1] + LHK.y*LEGtoKNEE;
	LFoot.p[2] = myRobot->uLINK[LLEG_J2].p[2] + LHK.z*LEGtoKNEE;
	result2 = inverseKinematics(LLEG_J3,LFoot);
	RFoot.p[0] = myRobot->uLINK[RLEG_J3].p[0] + RKA.x*KNEEtoFOOT;
	RFoot.p[1] = myRobot->uLINK[RLEG_J3].p[1] + RKA.y*KNEEtoFOOT;
	RFoot.p[2] = myRobot->uLINK[RLEG_J3].p[2] + RKA.z*KNEEtoFOOT;
	result3 = inverseKinematics(RLEG_J4,RFoot);
	LFoot.p[0] = myRobot->uLINK[LLEG_J3].p[0] + LKA.x*KNEEtoFOOT;
	LFoot.p[1] = myRobot->uLINK[LLEG_J3].p[1] + LKA.y*KNEEtoFOOT;
	LFoot.p[2] = myRobot->uLINK[LLEG_J3].p[2] + LKA.z*KNEEtoFOOT;
	result4 = inverseKinematics(LLEG_J4,LFoot);
	result = result1 + result2 + result3 + result4;
	return result;
}

//Selt Collision Avoidance
int IKSolver::selfCollisionAvoidance(){
	int res = 1;
	Joint LHand,RHand;
	for(int m=0;m<3;m++){														// Torso坐标系下双手的位置
		LHand.p[m] = myRobot->uLINK[LARM_J4].R[m][0] * WRISTtoHAND + myRobot->uLINK[LARM_J4].R[m][1] * 0 + myRobot->uLINK[LARM_J4].R[m][2] * 0 + myRobot->uLINK[LARM_J4].p[m];
		RHand.p[m] = myRobot->uLINK[RARM_J4].R[m][0] * WRISTtoHAND + myRobot->uLINK[RARM_J4].R[m][1] * 0 + myRobot->uLINK[RARM_J4].R[m][2] * 0 + myRobot->uLINK[RARM_J4].p[m];
	}
	if(LHand.p[0]<=COLLISION_TORSO_FRONT && LHand.p[0]>=COLLISION_TORSO_BACK && LHand.p[1]<=COLLISION_TORSO_LEFT && LHand.p[2]<=COLLISION_TORSO_UP && LHand.p[2]>=COLLISION_TORSO_BOTTOM){
		res = 0;
	}
	if(LHand.p[0]<=myRobot->uLINK[LLEG_J0].p[0]+COLLISION_HIP_FRONT && LHand.p[0]>=myRobot->uLINK[LLEG_J0].p[0]+COLLISION_HIP_BACK && LHand.p[1]<=myRobot->uLINK[LLEG_J0].p[1]+COLLISION_HIP_SIDES && LHand.p[2]<=myRobot->uLINK[LLEG_J0].p[2]+COLLISION_HIP_UP && LHand.p[2]>=myRobot->uLINK[LLEG_J0].p[2]+COLLISION_HIP_BOTTOM){
		res = 0;
	}
	if(RHand.p[0]<=COLLISION_TORSO_FRONT && RHand.p[0]>=COLLISION_TORSO_BACK && RHand.p[1]>=COLLISION_TORSO_RIGHT && RHand.p[2]<=COLLISION_TORSO_UP && RHand.p[2]>=COLLISION_TORSO_BOTTOM){
		res = 0;
	}
	if(RHand.p[0]<=myRobot->uLINK[RLEG_J0].p[0]+COLLISION_HIP_FRONT && RHand.p[0]>=myRobot->uLINK[RLEG_J0].p[0]+COLLISION_HIP_BACK && RHand.p[1]>=myRobot->uLINK[RLEG_J0].p[1]-COLLISION_HIP_SIDES && RHand.p[2]<=myRobot->uLINK[RLEG_J0].p[2]+COLLISION_HIP_UP && RHand.p[2]>=myRobot->uLINK[RLEG_J0].p[2]+COLLISION_HIP_BOTTOM){
		res = 0;
	}
	if(!res){																	// 自碰撞调整
		myRobot->uLINK[RARM_J0].q += -10*ToRad;
		myRobot->uLINK[RARM_J1].q += -10*ToRad;
		myRobot->uLINK[RARM_J3].q += -10*ToRad;
		myRobot->uLINK[LARM_J0].q += -10*ToRad;
		myRobot->uLINK[LARM_J1].q += 10*ToRad;
		myRobot->uLINK[LARM_J3].q += 10*ToRad;
		if(myRobot->uLINK[RARM_J0].q<myRobot->uLINK[RARM_J0].minq){
			myRobot->uLINK[RARM_J0].q = myRobot->uLINK[RARM_J0].minq;
		}
		if(myRobot->uLINK[RARM_J1].q<myRobot->uLINK[RARM_J1].minq){
			myRobot->uLINK[RARM_J1].q = myRobot->uLINK[RARM_J1].minq;
		}
		if(myRobot->uLINK[RARM_J3].q<myRobot->uLINK[RARM_J3].minq){
			myRobot->uLINK[RARM_J3].q = myRobot->uLINK[RARM_J3].minq;
		}
		if(myRobot->uLINK[LARM_J0].q<myRobot->uLINK[LARM_J0].minq){
			myRobot->uLINK[LARM_J0].q = myRobot->uLINK[LARM_J0].minq;
		}
		if(myRobot->uLINK[LARM_J1].q>myRobot->uLINK[LARM_J1].maxq){
			myRobot->uLINK[LARM_J1].q = myRobot->uLINK[LARM_J1].maxq;
		}
		if(myRobot->uLINK[LARM_J3].q>myRobot->uLINK[LARM_J3].maxq){
			myRobot->uLINK[LARM_J3].q = myRobot->uLINK[LARM_J3].maxq;
		}
		forwardKinematicsChild(RARM_J0);
		forwardKinematicsChild(LARM_J0);
		res = 1;
		if(LHand.p[0]<=COLLISION_TORSO_FRONT && LHand.p[0]>=COLLISION_TORSO_BACK && LHand.p[1]<=COLLISION_TORSO_LEFT && LHand.p[2]<=COLLISION_TORSO_UP && LHand.p[2]>=COLLISION_TORSO_BOTTOM){
			res = 0;
		}
		if(LHand.p[0]<=myRobot->uLINK[LLEG_J0].p[0]+COLLISION_HIP_FRONT && LHand.p[0]>=myRobot->uLINK[LLEG_J0].p[0]+COLLISION_HIP_BACK && LHand.p[1]<=myRobot->uLINK[LLEG_J0].p[1]+COLLISION_HIP_SIDES && LHand.p[2]<=myRobot->uLINK[LLEG_J0].p[2]+COLLISION_HIP_UP && LHand.p[2]>=myRobot->uLINK[LLEG_J0].p[2]+COLLISION_HIP_BOTTOM){
			res = 0;
		}
		if(RHand.p[0]<=COLLISION_TORSO_FRONT && RHand.p[0]>=COLLISION_TORSO_BACK && RHand.p[1]>=COLLISION_TORSO_RIGHT && RHand.p[2]<=COLLISION_TORSO_UP && RHand.p[2]>=COLLISION_TORSO_BOTTOM){
			res = 0;
		}
		if(RHand.p[0]<=myRobot->uLINK[RLEG_J0].p[0]+COLLISION_HIP_FRONT && RHand.p[0]>=myRobot->uLINK[RLEG_J0].p[0]+COLLISION_HIP_BACK && RHand.p[1]>=myRobot->uLINK[RLEG_J0].p[1]-COLLISION_HIP_SIDES && RHand.p[2]<=myRobot->uLINK[RLEG_J0].p[2]+COLLISION_HIP_UP && RHand.p[2]>=myRobot->uLINK[RLEG_J0].p[2]+COLLISION_HIP_BOTTOM){
			res = 0;
		}
	}
	return res;
}
