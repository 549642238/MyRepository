#include "ik_solver.h"

extern MathFunction mathFunction;												// ������ѧ�����࣬�����˵ķ����л������ѧ����

//ForwardKinematics����֪�ؽڽǶ�������˼�λ��,�ݹ麯�����մ�BODY������child�ؽڵ�˳�����μ���ÿ���ӹؽڶ�Ӧ���˵�λ��
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

//ForwardKinematics Child����֪�ؽڽǶ�������˼�λ��,�ݹ麯�����մ�i��i���ڵĹؽ����ĸ���child�ؽڵ�˳�����μ���ÿ���ӹؽڶ�Ӧ���˵�λ��
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

//�����ſ˱Ⱦ���
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

//����DLS�㷨��IK�㷨�����Ա��������������ľ޴�ؽڽ����ã���֪Ŀ��ؽ�λ�ã��󵽹ؽ�to�Ĺؽ�����
double IKSolver::inverseKinematics(int to,Joint Target){
	double lambda = 0.03;														// LM�㷨������ϵ����ֵ
	double v1 = 10;																// �Ŵ�����
	double v2 = 0.1;															// ��С����
	double res;																	// ����С(����)
	int* idx;																	// ��ĸ�˵�Ŀ��˵���
	int number;																	// �ؽ�������
	double** J;																	// �ſ˱Ⱦ���϶���3*number�ģ���ʾn���ؽڵ�3ά����
	double** Jt;																// �ſ˱Ⱦ����ת��
	if(to == RLEG_J4 || to == LLEG_J4){											// ���Ŀ��ؽ����㲿����targetλ����ص���4���ؽ�
		number = NUMBER_OF_LOWERBODY_JOINTS;
	}else{
		number = NUMBER_OF_UPPERBODY_JOINTS;
	}
	double* H1 = (double*)malloc(sizeof(double)*number);						// �Խ�Ȩ�ؾ���ĶԽ�ֵ��WLN�㷨
	double* H2 = (double*)malloc(sizeof(double)*number);						// �Խ�Ȩ�ؾ���ĶԽ�ֵ��WLN�㷨
	double* W = (double*)malloc(sizeof(double)*number);							// WLN��Ȩ�ؾ���W��һ���Խ���
	double** H = (double**)malloc(sizeof(double*)*DIMENSION);					// (��)��������
	double** Jw = (double**)malloc(sizeof(double*)*DIMENSION);					// ����J��W^-1�ɼ�
	double** Wj = (double**)malloc(sizeof(double*)*number);						// ����W^-1��J'�ĳ˻�
	double** Ji = (double**)malloc(sizeof(double*)*number);						// DLS�����ſ˱Ⱦ���Ĺ�����
	double** HI = (double**)malloc(sizeof(double*)*DIMENSION);					// DLS����H+lamda*I
	double** iHI = (double**)malloc(sizeof(double*)*DIMENSION);					// DLS����H+lamda*I����
	double* t = (double*)malloc(sizeof(double)*(DIMENSION*DIMENSION));			// ��ʱ���󣬰��д������ǰ�ľ���Ԫ��
	double* r = (double*)malloc(sizeof(double)*(DIMENSION*DIMENSION));			// ��ʱ���󣬰��д�������ľ���Ԫ��
	double* dq = (double*)malloc(sizeof(double)*number);						// �Ƕ�����
	double* err;																// target��uLINK[idx[end]]��λ��ƫ��
	double e;																	// target��uLINK[idx[end]]��λ��ƫ���С
	int updateJ = 1;															// ����һ�ε��������仯�����Ƿ����¸��¹ؽ�����
	for(int i=0;i<DIMENSION;i++){
		H[i] = (double*)malloc(sizeof(double)*DIMENSION);
		Jw[i] = (double*)malloc(sizeof(double)*number);
		HI[i] = (double*)malloc(sizeof(double)*DIMENSION);
		iHI[i] = (double*)malloc(sizeof(double)*DIMENSION);
	}
	/* ����to��ȡ:RLEG_J4��LLEG_J4��RARM_J2��RARM_J4��LARM_J2��LARM_J4��LEG_J4��REG_J4 */
	idx = myRobot->findRoute(to);												// �ҵ���BODY����ؽ�to�������˵�ĸ���˵����йؽ�
	if(idx == NULL){															// һ�������Ŀ��ؽ�to���벻��ȷ
		printf("��������ȷ��Ŀ�Ĺؽ���\n");
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
			err = mathFunction.calcVWerr(Target,myRobot->uLINK[to]);							// ��ǰ�ؽ�λ��Ŀ��ؽ�λ�˵�ƫ��
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
		if(e < IK_ERR){															// ����ƫ���Ϊ0.001m
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
		// ����һ�е����һ��Ԫ�غ���һ�еĵ�һ��Ԫ�ز���������������һ��Ҫ����ά����˳��ŵ�������
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
	// ���ƹؽڽǶȷ�ֹԽ��
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

//���ϰ���ʹ��IK���ؽڽǶ�����
double IKSolver::ikUpperBody(vect RSE,vect REH,vect LSE,vect LEH){
	Joint RHand,LHand;																	// Ŀ��ؽ�״̬
	double result,result1,result2,result3,result4;										// ����IK�������
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

//���°���ʹ��IK���ؽڽǶ�����(single support)
double IKSolver::ikLowerBody(vect RHK,vect RKA,vect LHK,vect LKA){
	Joint RFoot,LFoot;															// Ŀ��ؽ�״̬
	double result,result1,result2,result3,result4;								// ����IK�������
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
	for(int m=0;m<3;m++){														// Torso����ϵ��˫�ֵ�λ��
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
	if(!res){																	// ����ײ����
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
