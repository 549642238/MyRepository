#include "zmp.h"

extern MathFunction mathFunction;														// 引用数学方法类，机器人的方法中会调用数学方法

//计算机器人质量
double ZMP::calcM(int j){
	double m;
	if (j==0){
		m = 0; 
	}else{
		m = myRobot->uLINK[j].m + calcM(myRobot->uLINK[j].sister) + calcM(myRobot->uLINK[j].child);
	}
	return m;
}

//计算机器人质量×质心(局部坐标系下)的和
double* ZMP::calcMC(int j){
	double* mc = (double*)malloc(sizeof(double)*3);
	double* temp = (double*)malloc(sizeof(double)*3);
	if (j==0){
		mc[0] = mc[1] = mc[2] = 0; 
	}else{
		for(int i=0;i<3;i++){
			temp[i] = myRobot->uLINK[j].R[i][0]*myRobot->uLINK[j].c[0]+myRobot->uLINK[j].R[i][1]*myRobot->uLINK[j].c[1]+myRobot->uLINK[j].R[i][2]*myRobot->uLINK[j].c[2];
		}
		for(int i=0;i<3;i++){
			mc[i] = myRobot->uLINK[j].m * (myRobot->uLINK[j].p[i]+temp[i]);
		}
		double* sister = calcMC(myRobot->uLINK[j].sister);
		double* child = calcMC(myRobot->uLINK[j].child);
		for(int i=0;i<3;i++){
			mc[i] += sister[i]+child[i];
		}
	}
	free(temp);
	return mc;
}

//计算机器人质心
double* ZMP::calcCoM(const double M){
	double* com = (double*)malloc(sizeof(double)*3);
	double* MC = calcMC(1);
	for(int i=0;i<3;i++){
		com[i] = MC[i]/M;
	}
	free(MC);
	return com;
}

//Ankle Strategy:在单脚支撑下将支撑脚与COM的向量和(0,0,1)平行得到的支撑脚Ankle配置
void ZMP::getAnkleAngle(vect AC,double& change_ankle_roll,double& change_ankle_pitch){
	vect n1;
	n1.x = 0;
	n1.y = AC.z;
	n1.z = -AC.y;
	change_ankle_roll = -(AC.y/fabs(AC.y))*(acos((n1.x*0+n1.y*1+n1.z*0)/(1.0*sqrt(pow(n1.x,2)+pow(n1.y,2)+pow(n1.z,2)))));
	vect start;
	start.x = 0;
	start.y = (AC.y/fabs(AC.y))*sin(fabs(change_ankle_roll));
	start.z = cos(fabs(change_ankle_roll));
	change_ankle_pitch = (AC.x/fabs(AC.x))*acos((start.x*AC.x + start.y*AC.y + start.z*AC.z)/(sqrt(pow(start.x,2)+pow(start.y,2)+pow(start.z,2))*sqrt(pow(AC.x,2)+pow(AC.y,2)+pow(AC.z,2))));
}

//计算double support的支撑多边形，检查double support是否平衡
int ZMP::doubleSupportCheck(const double M){
	int result = 0;
	double ankleCoM[3];
	double ankleCoMLA[3];
	double* com = calcCoM(M);
	double rankle[3];
	double rankleLA[3];
	double** R = mathFunction.transposition(myRobot->uLINK[LLEG_J5].R,3,3);
	for(int i=0;i<3;i++){
		ankleCoM[i] = com[i] - myRobot->uLINK[LLEG_J5].p[i];							// TORSO坐标系下的Vankle-com
		rankle[i] = myRobot->uLINK[RLEG_J5].p[i] - myRobot->uLINK[LLEG_J5].p[i];		// TORSO坐标系下的Vlankle-rankle
	}
	for(int i=0;i<3;i++){
		ankleCoMLA[i] = R[i][0]*ankleCoM[0] + R[i][1]*ankleCoM[1] + R[i][2]*ankleCoM[2];
		rankleLA[i] = R[i][0]*rankle[0] + R[i][1]*rankle[1] + R[i][2]*rankle[2];
	}
	double v1[2] = {LEFT_SUPPORT_AREA_LEFT,LEFT_SUPPORT_AREA_UP};
	double v2[2] = {-LEFT_SUPPORT_AREA_RIGHT,LEFT_SUPPORT_AREA_UP};
	double v3[2] = {-LEFT_SUPPORT_AREA_RIGHT,-LEFT_SUPPORT_AREA_BOTTOM};
	double v4[2] = {LEFT_SUPPORT_AREA_LEFT,-LEFT_SUPPORT_AREA_BOTTOM};
	double v5[2] = {rankleLA[1]+RIGHT_SUPPORT_AREA_LEFT,rankleLA[0]+RIGHT_SUPPORT_AREA_UP};
	double v6[2] = {rankleLA[1]-RIGHT_SUPPORT_AREA_RIGHT,rankleLA[0]+RIGHT_SUPPORT_AREA_UP};
	double v7[2] = {rankleLA[1]+RIGHT_SUPPORT_AREA_LEFT,rankleLA[0]-RIGHT_SUPPORT_AREA_BOTTOM};
	double v8[2] = {rankleLA[1]-RIGHT_SUPPORT_AREA_RIGHT,rankleLA[0]-RIGHT_SUPPORT_AREA_BOTTOM};
	if(rankleLA[0]<0){																	// 左脚在前
		if(ankleCoMLA[1]<=v2[0] && ankleCoMLA[1]>=v5[0]){
			double a1 = (v2[1] - v6[1])/(v2[0] - v6[0]);
			double a2 = (v4[1] - v8[1])/(v4[0] - v8[0]);
			double b1 = v2[1] - a1*v2[0];
			double b2 = v4[1] - a2*v4[0];
			if(ankleCoMLA[0]<=a1*ankleCoMLA[1]+b1 && ankleCoMLA[0]>=a2*ankleCoMLA[1]+b2){
				result = 1;
			}else{
				result = 0;
			}
		}else if(ankleCoMLA[1]<=v1[0] && ankleCoMLA[1]>v2[0]){
			double a2 = (v4[1] - v8[1])/(v4[0] - v8[0]);
			double b2 = v4[1] - a2*v4[0];
			if(ankleCoMLA[0]>=a2*ankleCoMLA[1]+b2 && ankleCoMLA[0]<=v1[1]){
				result = 1;
			}else{
				result = 0;
			}
		}else if(ankleCoMLA[1]<v5[0] && ankleCoMLA[1]>=v6[0]){
			double a1 = (v2[1] - v6[1])/(v2[0] - v6[0]);
			double b1 = v2[1] - a1*v2[0];
			if(ankleCoMLA[0]<=a1*ankleCoMLA[1]+b1 && ankleCoMLA[0]>=v7[1]){
				result = 1;
			}else{
				result = 0;
			}
		}else{
			result = 0;
		}
	}else{																				// 右脚在前或两脚平齐
		if(ankleCoMLA[1]<=v2[0] && ankleCoMLA[1]>=v5[0]){
			double a1 = (v1[1] - v5[1])/(v1[0] - v5[0]);
			double a2 = (v3[1] - v7[1])/(v3[0] - v7[0]);
			double b1 = v1[1] - a1*v1[0];
			double b2 = v3[1] - a2*v3[0];
			if(ankleCoMLA[0]<=a1*ankleCoMLA[1]+b1 && ankleCoMLA[0]>=a2*ankleCoMLA[1]+b2){
				result = 1;
			}else{
				result = 0;
			}
		}else if(ankleCoMLA[1]<=v1[0] && ankleCoMLA[1]>v2[0]){
			double a1 = (v1[1] - v5[1])/(v1[0] - v5[0]);
			double b1 = v1[1] - a1*v1[0];
			if(ankleCoMLA[0]<=a1*ankleCoMLA[1]+b1 && ankleCoMLA[0]>=v3[1]){
				result = 1;
			}else{
				result = 0;
			}
		}else if(ankleCoMLA[1]<v5[0] && ankleCoMLA[1]>=v6[0]){
			double a2 = (v3[1] - v7[1])/(v3[0] - v7[0]);
			double b2 = v3[1] - a2*v3[0];
			if(ankleCoMLA[0]>=a2*ankleCoMLA[1]+b2 && ankleCoMLA[0]<=v5[1]){
				result = 1;
			}else{
				result = 0;
			}
		}else{
			result = 0;
		}
	}
	free(com);
	for(int i=0;i<3;i++){
		free(R[i]);
	}
	free(R);
	return result;
}

//平衡控制
void ZMP::balance(int supportMode,int& sig,const double M,int no){
	if(supportMode == 1){																// 左脚支撑平衡控制
		sig = 0;
		double ankleCoM[3];
		double ankleCoMLA[3];
		double* com = calcCoM(M);
		double** R = mathFunction.transposition(myRobot->uLINK[LLEG_J5].R,3,3);
		for(int i=0;i<3;i++){
			ankleCoM[i] = com[i] - myRobot->uLINK[LLEG_J5].p[i];						// TORSO坐标系下的Vankle-com
		}
		for(int i=0;i<3;i++){
			ankleCoMLA[i] = R[i][0]*ankleCoM[0] + R[i][1]*ankleCoM[1] + R[i][2]*ankleCoM[2];
		}
		if(ankleCoMLA[0]<=LEFT_SUPPORT_AREA_UP && ankleCoMLA[0]>=-LEFT_SUPPORT_AREA_BOTTOM && ankleCoMLA[1]<=LEFT_SUPPORT_AREA_LEFT && ankleCoMLA[1]>=-LEFT_SUPPORT_AREA_RIGHT){
			sig = 1;
			free(com);
			for(int i=0;i<3;i++){
				free(R[i]);
			}
			free(R);
		}else{
			free(com);
			for(int i=0;i<3;i++){
				free(R[i]);
			}
			free(R);
			for(int i=0;i<MAX_LOOP_TIME;i++){
				vect AC;
				AC.x = ankleCoMLA[0];
				AC.y = ankleCoMLA[1];
				AC.z = ankleCoMLA[2];
				double change_lankle_roll,change_lankle_pitch;							// lankle关节的增量
				getAnkleAngle(AC,change_lankle_roll,change_lankle_pitch);
				myRobot->uLINK[LLEG_J4].q += change_lankle_pitch;
				myRobot->uLINK[LLEG_J5].q += change_lankle_roll;
				if(myRobot->uLINK[LLEG_J4].q > myRobot->uLINK[LLEG_J4].maxq){
					myRobot->uLINK[LLEG_J4].q = myRobot->uLINK[LLEG_J4].maxq;
				}
				if(myRobot->uLINK[LLEG_J4].q < myRobot->uLINK[LLEG_J4].minq){
					myRobot->uLINK[LLEG_J4].q = myRobot->uLINK[LLEG_J4].minq;
				}
				if(myRobot->uLINK[LLEG_J5].q > myRobot->uLINK[LLEG_J5].maxq){
					myRobot->uLINK[LLEG_J5].q = myRobot->uLINK[LLEG_J5].maxq;
				}
				if(myRobot->uLINK[LLEG_J5].q < myRobot->uLINK[LLEG_J5].minq){
					myRobot->uLINK[LLEG_J5].q = myRobot->uLINK[LLEG_J5].minq;
				}
				myIKSolver->forwardKinematicsChild(LLEG_J4);
				/*预测校正后的COM是否落于支撑多边形内*/
				com = calcCoM(M);
				R = mathFunction.transposition(myRobot->uLINK[LLEG_J5].R,3,3);
				for(int i=0;i<3;i++){
					ankleCoM[i] = com[i] - myRobot->uLINK[LLEG_J5].p[i];				// TORSO坐标系下的Vankle-com
				}
				for(int i=0;i<2;i++){
					ankleCoMLA[i] = R[i][0]*ankleCoM[0] + R[i][1]*ankleCoM[1] + R[i][2]*ankleCoM[2];
				}
				if(ankleCoMLA[0]<=LEFT_SUPPORT_AREA_UP && ankleCoMLA[0]>=-LEFT_SUPPORT_AREA_BOTTOM && ankleCoMLA[1]<=LEFT_SUPPORT_AREA_LEFT && ankleCoMLA[1]>=-LEFT_SUPPORT_AREA_RIGHT){
					sig = 1;
					double RR[3][3];
					for(int x=0;x<3;x++){
						for(int y=0;y<3;y++){
							RR[x][y] = R[x][0]*myRobot->uLINK[RLEG_J5].R[0][y]+R[x][1]*myRobot->uLINK[RLEG_J5].R[1][y]+R[x][2]*myRobot->uLINK[RLEG_J5].R[2][y];
						}
					}
					double rz = atan(RR[1][0]/RR[0][0]);
					double rx = atan((RR[0][2]*sin(rz)-RR[1][2]*cos(rz))/(RR[1][1]*cos(rz)-RR[0][1]*sin(rz)));
					double ry = atan(-RR[2][0]/(RR[0][0]*cos(rz)+RR[1][0]*sin(rz)));
					myRobot->uLINK[RLEG_J4].q += -ry;									// 保证free foot和地面平行
					myRobot->uLINK[RLEG_J5].q += -rx;									// 保证free foot和地面平行
					if(myRobot->uLINK[RLEG_J4].q > myRobot->uLINK[RLEG_J4].maxq){
						myRobot->uLINK[RLEG_J4].q = myRobot->uLINK[RLEG_J4].maxq;
					}
					if(myRobot->uLINK[RLEG_J4].q < myRobot->uLINK[RLEG_J4].minq){
						myRobot->uLINK[RLEG_J4].q = myRobot->uLINK[RLEG_J4].minq;
					}
					if(myRobot->uLINK[RLEG_J5].q > myRobot->uLINK[RLEG_J5].maxq){
						myRobot->uLINK[RLEG_J5].q = myRobot->uLINK[RLEG_J5].maxq;
					}
					if(myRobot->uLINK[RLEG_J5].q < myRobot->uLINK[RLEG_J5].minq){
						myRobot->uLINK[RLEG_J5].q = myRobot->uLINK[RLEG_J5].minq;
					}
					myIKSolver->forwardKinematicsChild(RLEG_J4);
					break;
				}
				free(com);
				for(int i=0;i<3;i++){
					free(R[i]);
				}
				free(R);
			}
		}
	}else if(supportMode == 2){															// 右脚支撑平衡控制
		sig = 0;
		double ankleCoM[3];
		double ankleCoMRA[3];
		double* com = calcCoM(M);
		double** R = mathFunction.transposition(myRobot->uLINK[RLEG_J5].R,3,3);
		for(int i=0;i<3;i++){
			ankleCoM[i] = com[i] - myRobot->uLINK[RLEG_J5].p[i];						// TORSO坐标系下的Vankle-com
		}
		for(int i=0;i<3;i++){
			ankleCoMRA[i] = R[i][0]*ankleCoM[0] + R[i][1]*ankleCoM[1] + R[i][2]*ankleCoM[2];
		}
		if(ankleCoMRA[0]<=RIGHT_SUPPORT_AREA_UP && ankleCoMRA[0]>=-RIGHT_SUPPORT_AREA_BOTTOM && ankleCoMRA[1]<=RIGHT_SUPPORT_AREA_LEFT && ankleCoMRA[1]>=-RIGHT_SUPPORT_AREA_RIGHT){
			sig = 1;
			free(com);
			for(int i=0;i<3;i++){
				free(R[i]);
			}
			free(R);
		}else{
			free(com);
			for(int i=0;i<3;i++){
				free(R[i]);
			}
			free(R);
			for(int i=0;i<MAX_LOOP_TIME;i++){
				vect AC;
				AC.x = ankleCoMRA[0];
				AC.y = ankleCoMRA[1];
				AC.z = ankleCoMRA[2];
				//FILE* fp = fopen("balance.txt","a+");
				//fprintf(fp,"校正前:%lf,%lf,%lf\n",AC.x,AC.y,AC.z);
				//fflush(fp);
				double change_rankle_roll,change_rankle_pitch;							// ankle关节的增量
				getAnkleAngle(AC,change_rankle_roll,change_rankle_pitch);
				myRobot->uLINK[RLEG_J4].q += change_rankle_pitch;
				myRobot->uLINK[RLEG_J5].q += change_rankle_roll;
				if(myRobot->uLINK[RLEG_J4].q > myRobot->uLINK[RLEG_J4].maxq){
					myRobot->uLINK[RLEG_J4].q = myRobot->uLINK[RLEG_J4].maxq;
				}
				if(myRobot->uLINK[RLEG_J4].q < myRobot->uLINK[RLEG_J4].minq){
					myRobot->uLINK[RLEG_J4].q = myRobot->uLINK[RLEG_J4].minq;
				}
				if(myRobot->uLINK[RLEG_J5].q > myRobot->uLINK[RLEG_J5].maxq){
					myRobot->uLINK[RLEG_J5].q = myRobot->uLINK[RLEG_J5].maxq;
				}
				if(myRobot->uLINK[RLEG_J5].q < myRobot->uLINK[RLEG_J5].minq){
					myRobot->uLINK[RLEG_J5].q = myRobot->uLINK[RLEG_J5].minq;
				}
				myIKSolver->forwardKinematicsChild(RLEG_J4);
				/*预测校正后的COM是否落于支撑多边形内*/
				com = calcCoM(M);
				R = mathFunction.transposition(myRobot->uLINK[RLEG_J5].R,3,3);
				for(int i=0;i<3;i++){
					ankleCoM[i] = com[i] - myRobot->uLINK[RLEG_J5].p[i];				// TORSO坐标系下的Vankle-com
				}
				for(int i=0;i<2;i++){
					ankleCoMRA[i] = R[i][0]*ankleCoM[0] + R[i][1]*ankleCoM[1] + R[i][2]*ankleCoM[2];
				}
				//fprintf(fp,"校正后:%lf,%lf,%lf\n\n\n",ankleCoMLA[0],ankleCoMLA[1],ankleCoMLA[2]);
				//fflush(fp);
				if(ankleCoMRA[0]<=RIGHT_SUPPORT_AREA_UP && ankleCoMRA[0]>=-RIGHT_SUPPORT_AREA_BOTTOM && ankleCoMRA[1]<=RIGHT_SUPPORT_AREA_LEFT && ankleCoMRA[1]>=-RIGHT_SUPPORT_AREA_RIGHT){
					sig = 1;
					double RL[3][3];
					for(int x=0;x<3;x++){
						for(int y=0;y<3;y++){
							RL[x][y] = R[x][0]*myRobot->uLINK[LLEG_J5].R[0][y]+R[x][1]*myRobot->uLINK[LLEG_J5].R[1][y]+R[x][2]*myRobot->uLINK[LLEG_J5].R[2][y];
						}
					}
					double lz = atan(RL[1][0]/RL[0][0]);
					double lx = atan((RL[0][2]*sin(lz)-RL[1][2]*cos(lz))/(RL[1][1]*cos(lz)-RL[0][1]*sin(lz)));
					double ly = atan(-RL[2][0]/(RL[0][0]*cos(lz)+RL[1][0]*sin(lz)));
					myRobot->uLINK[LLEG_J4].q += -ly;									// 保证free foot和地面平行
					myRobot->uLINK[LLEG_J5].q += -lx;									// 保证free foot和地面平行
					if(myRobot->uLINK[LLEG_J4].q > myRobot->uLINK[LLEG_J4].maxq){
						myRobot->uLINK[LLEG_J4].q = myRobot->uLINK[LLEG_J4].maxq;
					}
					if(myRobot->uLINK[LLEG_J4].q < myRobot->uLINK[LLEG_J4].minq){
						myRobot->uLINK[LLEG_J4].q = myRobot->uLINK[LLEG_J4].minq;
					}
					if(myRobot->uLINK[LLEG_J5].q > myRobot->uLINK[LLEG_J5].maxq){
						myRobot->uLINK[LLEG_J5].q = myRobot->uLINK[LLEG_J5].maxq;
					}
					if(myRobot->uLINK[LLEG_J5].q < myRobot->uLINK[LLEG_J5].minq){
						myRobot->uLINK[LLEG_J5].q = myRobot->uLINK[LLEG_J5].minq;
					}
					myIKSolver->forwardKinematicsChild(LLEG_J4);
					break;
				}
				free(com);
				for(int i=0;i<3;i++){
					free(R[i]);
				}
				free(R);
			}
		}
	}else{																				// 双脚支撑平衡控制
		sig = 0;
		/*
			AnkleRoll处理，使脚面和地面全接触，在双脚对称的前提下，Body在世界坐标系下绕roll轴的旋度应该是0，只要令LAnkle和RAnkle在
			Body坐标系下位姿的roll旋转角度为0即可
		*/
		double lz,lx;
		if(myRobot->uLINK[LLEG_J5].R[0][0]!=0){
			lz = atan(myRobot->uLINK[LLEG_J5].R[1][0]/myRobot->uLINK[LLEG_J5].R[0][0]);
		}else{
			lz = myRobot->uLINK[LLEG_J5].R[1][0]/fabs(myRobot->uLINK[LLEG_J5].R[1][0])*90*ToRad;
		}
		double b = myRobot->uLINK[LLEG_J5].R[1][1]*cos(lz)-myRobot->uLINK[LLEG_J5].R[0][1]*sin(lz);
		if(b!=0){
			lx = atan((myRobot->uLINK[LLEG_J5].R[0][2]*sin(lz)-myRobot->uLINK[LLEG_J5].R[1][2]*cos(lz))/b);
		}else{
			double temp = (myRobot->uLINK[LLEG_J5].R[0][2]*sin(lz)-myRobot->uLINK[LLEG_J5].R[1][2]*cos(lz));
			lx = temp/fabs(temp)*90*ToRad;
		}
		double rz,rx;
		if(myRobot->uLINK[RLEG_J5].R[0][0]!=0){
			rz = atan(myRobot->uLINK[RLEG_J5].R[1][0]/myRobot->uLINK[RLEG_J5].R[0][0]);
		}else{
			rz = myRobot->uLINK[RLEG_J5].R[1][0]/fabs(myRobot->uLINK[RLEG_J5].R[1][0])*90*ToRad;
		}
		b = myRobot->uLINK[RLEG_J5].R[1][1]*cos(rz)-myRobot->uLINK[RLEG_J5].R[0][1]*sin(rz);
		if(b!=0){
			rx = atan((myRobot->uLINK[RLEG_J5].R[0][2]*sin(rz)-myRobot->uLINK[RLEG_J5].R[1][2]*cos(rz))/b);
		}else{
			double temp = (myRobot->uLINK[RLEG_J5].R[0][2]*sin(rz)-myRobot->uLINK[RLEG_J5].R[1][2]*cos(rz));
			rx = temp/fabs(temp)*90*ToRad;
		}
		myRobot->uLINK[LLEG_J5].q += -(lx+ANKLE_PHYSICAL_ERR*ToRad);					// 机械误差，测量所得
		myRobot->uLINK[RLEG_J5].q += -(rx-ANKLE_PHYSICAL_ERR*ToRad);
/*		if(doubleSupportCheck(M)){
			sig = 1;
		}else{		*/																	// 调节AnklePitch，使Ankle-hip与pitch-yaw平行，这个过程hip位置不像COM随位姿改变而变动，可以精确使用空间向量法求解
			double lankleHip[3];
			double lankleHipLA[3];
			double rankleHip[3];
			double rankleHipRA[3];
			double** RL = mathFunction.transposition(myRobot->uLINK[LLEG_J5].R,3,3);
			double** RR = mathFunction.transposition(myRobot->uLINK[RLEG_J5].R,3,3);
			for(int i=0;i<3;i++){
				lankleHip[i] = myRobot->uLINK[LLEG_J2].p[i] - myRobot->uLINK[LLEG_J5].p[i];
				rankleHip[i] = myRobot->uLINK[RLEG_J2].p[i] - myRobot->uLINK[RLEG_J5].p[i];
			}
			for(int i=0;i<3;i++){
				lankleHipLA[i] = RL[i][0]*lankleHip[0] + RL[i][1]*lankleHip[1] + RL[i][2]*lankleHip[2];
				rankleHipRA[i] = RR[i][0]*rankleHip[0] + RR[i][1]*rankleHip[1] + RR[i][2]*rankleHip[2];
			}
			vect start;
			start.x = 0;
			start.y = (lankleHipLA[1]/fabs(lankleHipLA[1]))*sin(fabs(myRobot->uLINK[LLEG_J5].q));
			start.z = cos(fabs(myRobot->uLINK[LLEG_J5].q));
			double change_lankle_pitch = (lankleHipLA[0]/fabs(lankleHipLA[0]))*acos((start.x*lankleHipLA[0] + start.y*lankleHipLA[1] + start.z*lankleHipLA[2])/(sqrt(pow(start.x,2)+pow(start.y,2)+pow(start.z,2))*sqrt(pow(lankleHipLA[0],2)+pow(lankleHipLA[1],2)+pow(lankleHipLA[2],2))));
			start.x = 0;
			start.y = (rankleHipRA[1]/fabs(rankleHipRA[1]))*sin(fabs(myRobot->uLINK[RLEG_J5].q));
			start.z = cos(fabs(myRobot->uLINK[RLEG_J5].q));
			double change_rankle_pitch = (rankleHipRA[0]/fabs(rankleHipRA[0]))*acos((start.x*rankleHipRA[0] + start.y*rankleHipRA[1] + start.z*rankleHipRA[2])/(sqrt(pow(start.x,2)+pow(start.y,2)+pow(start.z,2))*sqrt(pow(rankleHipRA[0],2)+pow(rankleHipRA[1],2)+pow(rankleHipRA[2],2))));
			myRobot->uLINK[LLEG_J4].q += change_lankle_pitch;
			myRobot->uLINK[RLEG_J4].q += change_rankle_pitch;
			myIKSolver->forwardKinematicsChild(LLEG_J4);
			myIKSolver->forwardKinematicsChild(RLEG_J4);
/*			if(doubleSupportCheck(M)){
				sig = 1;
			}else{				*/														// Hip Strategy
				for(int i=0;i<3;i++){
					free(RL[i]);
					free(RR[i]);
				}
				free(RL);
				free(RR);
				RL = mathFunction.transposition(myRobot->uLINK[LLEG_J5].R,3,3);
				RR = mathFunction.transposition(myRobot->uLINK[RLEG_J5].R,3,3);
				for(int i=0;i<3;i++){
					lankleHip[i] = myRobot->uLINK[LLEG_J2].p[i] - myRobot->uLINK[LLEG_J5].p[i];
					rankleHip[i] = myRobot->uLINK[RLEG_J2].p[i] - myRobot->uLINK[RLEG_J5].p[i];
				}
				for(int i=0;i<3;i++){
					lankleHipLA[i] = RL[i][0]*lankleHip[0] + RL[i][1]*lankleHip[1] + RL[i][2]*lankleHip[2];
					rankleHipRA[i] = RR[i][0]*rankleHip[0] + RR[i][1]*rankleHip[1] + RR[i][2]*rankleHip[2];
				}
				double* com = calcCoM(M);
				double lhipCoM[3];														
				double lHipCoMLA[3];													// LAnkleRoll坐标系下的LHip-CoM向量
				double rhipCoM[3];														
				double rHipCoMRA[3];													// RAnkleRoll坐标系下的RHip-CoM向量
				for(int i=0;i<3;i++){
					lhipCoM[i] = com[i] - myRobot->uLINK[LLEG_J5].p[i];
					rhipCoM[i] = com[i] - myRobot->uLINK[RLEG_J5].p[i];
				}
				for(int i=0;i<3;i++){
					lHipCoMLA[i] = RL[i][0]*lhipCoM[0] + RL[i][1]*lhipCoM[1] + RL[i][2]*lhipCoM[2];
					rHipCoMRA[i] = RR[i][0]*rhipCoM[0] + RR[i][1]*rhipCoM[1] + RR[i][2]*rhipCoM[2];
				}
				vect n1,n2,temp1,temp2;
				n1.x = 1;
				n1.y = 0;
				n1.z = 0;
				temp1.x = lHipCoMLA[0];
				temp1.y = lHipCoMLA[1];
				temp1.z = lHipCoMLA[2];
				temp2.x = 0;
				temp2.y = -(lankleHipLA[1]/fabs(lankleHipLA[1]))*cos(fabs(myRobot->uLINK[LLEG_J5].q));
				temp2.z = sin(fabs(myRobot->uLINK[LLEG_J5].q));
				n2.x = fabs(temp1.y*temp2.z-temp1.z*temp2.y);
				n2.y = temp1.z*temp2.x-temp1.x*temp2.z;
				n2.z = temp1.x*temp2.y-temp1.y*temp2.x;
				double change_lhip_pitch = (lHipCoMLA[0]/fabs(lHipCoMLA[0]))*acos((n1.x*n2.x+n1.y*n2.y+n1.z*n2.z)/(sqrt(pow(n1.x,2)+pow(n1.y,2)+pow(n1.z,2))*sqrt(pow(n2.x,2)+pow(n2.y,2)+pow(n2.z,2))));
				temp1.x = rHipCoMRA[0];
				temp1.y = rHipCoMRA[1];
				temp1.z = rHipCoMRA[2];
				temp2.x = 0;
				temp2.y = -(rankleHipRA[1]/fabs(rankleHipRA[1]))*cos(fabs(myRobot->uLINK[RLEG_J5].q));
				temp2.z = sin(fabs(myRobot->uLINK[RLEG_J5].q));
				n2.x = fabs(temp1.y*temp2.z-temp1.z*temp2.y);
				n2.y = temp1.z*temp2.x-temp1.x*temp2.z;
				n2.z = temp1.x*temp2.y-temp1.y*temp2.x;
				double change_rhip_pitch = (rHipCoMRA[0]/fabs(rHipCoMRA[0]))*acos((n1.x*n2.x+n1.y*n2.y+n1.z*n2.z)/(sqrt(pow(n1.x,2)+pow(n1.y,2)+pow(n1.z,2))*sqrt(pow(n2.x,2)+pow(n2.y,2)+pow(n2.z,2))));
				myRobot->uLINK[LLEG_J2].q += change_lhip_pitch;
				myRobot->uLINK[RLEG_J2].q += change_rhip_pitch;
				myIKSolver->forwardKinematicsChild(LLEG_J2);
				myIKSolver->forwardKinematicsChild(RLEG_J2);
				if(doubleSupportCheck(M)){
					sig = 1;
				}
				/*
				FILE* fp = fopen("balance.txt","a+");
				fprintf(fp,"%d %0.5lf,%0.5lf %0.5lf,%0.5lf\n",no,uLINK[LLEG_J2].q,uLINK[RLEG_J2].q,change_lhip_pitch,change_rhip_pitch);
				fflush(fp);
				fclose(fp);
				*/
				free(com);
	//		}
			for(int i=0;i<3;i++){
				free(RL[i]);
				free(RR[i]);
			}
			free(RL);
			free(RR);
	//	}
	}
}
