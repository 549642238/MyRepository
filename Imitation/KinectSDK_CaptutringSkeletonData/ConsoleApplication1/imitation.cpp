#include "imitation.h"

extern MathFunction mathFunction;															// 引用数学方法类，机器人的方法中会调用数学方法

Robot* myRobot = new Robot("NAO");															// 创建一个机器人
IKSolver* myIKSolver = new IKSolver(myRobot);												// 创建IKSolver
ZMP* myZMP = new ZMP(myRobot,myIKSolver);													// 创建平衡控制器
SOCKET clientSocket;																		// 与配置模块通信
Log* myLog = new Log();																		// 记录采集和控制数据
double** lastJoint = (double**)malloc(sizeof(double*)*JOINT_RECORD_ACCOUNT);				// 上一帧的关节数据
double last_RShoulderRoll,last_RShoulderPitch,last_RElbowRoll,last_RElbowYaw;				// 上一帧右臂关节配置
double last_LShoulderRoll,last_LShoulderPitch,last_LElbowRoll,last_LElbowYaw;				// 上一帧左臂关节配置
double last_LHipRoll,last_LHipPitch,last_LKneePitch,last_LAnkleRoll,last_LAnklePitch;		// 上一帧左腿关节配置
double last_RHipRoll,last_RHipPitch,last_RKneePitch,last_RAnkleRoll,last_RAnklePitch;		// 上一帧右腿关节配置
double Mass;																				// 机器人质量，平衡控制需要
double result_up,result_low;																// IK计算误差(上肢和下肢)
int calculating = 1;																		// 处理线程运行状态，1表示正在处理数据(即有数据可处理),0表示当前没有数据可处理

typedef struct IKUPPara{																	// IKUpperBody线程传参
	vect RSE,REH,LSE,LEH;
}uParam,*sParam;

struct Frame{																				// 将采集针记录在结构体中(放于内存)
	int no;																					// 帧序
	double location[15][3];																	// 每一个关节位置
	int supportMode;																		// 支撑模式
	int supportOrder;																		// 支撑指令
	int time;																				// 动作执行的理想时间
	int doubleSupportConfig;																// 是否配置double support下肢关节
};
Frame* writeFrame,*readFrame;																// 记录帧的内存位置，读取帧的内存位置

//通过传入关节点的位置，把骨骼画出来  
void drawSkeleton(Mat &image, CvPoint pointSet[], int whichone){   
    CvScalar color;   
    switch(whichone) //跟踪不同的人显示不同的颜色   
    {   
    case 0:   
        color = cvScalar(255);   
        break;   
    case 1:   
        color = cvScalar(0,255);   
        break;   
    case 2:   
        color = cvScalar(0, 0, 255);   
        break;   
    case 3:   
        color = cvScalar(255, 255, 0);   
        break;   
    case 4:   
        color = cvScalar(255, 0, 255);   
        break;   
    case 5:   
        color = cvScalar(0, 255, 255);   
        break;   
    }   
   
    if((pointSet[NUI_SKELETON_POSITION_HEAD].x!=0 || pointSet[NUI_SKELETON_POSITION_HEAD].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_SHOULDER_CENTER].x!=0 || pointSet[NUI_SKELETON_POSITION_SHOULDER_CENTER].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_HEAD], pointSet[NUI_SKELETON_POSITION_SHOULDER_CENTER], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_SHOULDER_CENTER].x!=0 || pointSet[NUI_SKELETON_POSITION_SHOULDER_CENTER].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_SPINE].x!=0 || pointSet[NUI_SKELETON_POSITION_SPINE].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_SHOULDER_CENTER], pointSet[NUI_SKELETON_POSITION_SPINE], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_SPINE].x!=0 || pointSet[NUI_SKELETON_POSITION_SPINE].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_HIP_CENTER].x!=0 || pointSet[NUI_SKELETON_POSITION_HIP_CENTER].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_SPINE], pointSet[NUI_SKELETON_POSITION_HIP_CENTER], color, 2);   
   
    //左上肢   
    if((pointSet[NUI_SKELETON_POSITION_SHOULDER_CENTER].x!=0 || pointSet[NUI_SKELETON_POSITION_SHOULDER_CENTER].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_SHOULDER_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_SHOULDER_LEFT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_SHOULDER_CENTER], pointSet[NUI_SKELETON_POSITION_SHOULDER_LEFT], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_SHOULDER_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_SHOULDER_LEFT].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_ELBOW_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_ELBOW_LEFT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_SHOULDER_LEFT], pointSet[NUI_SKELETON_POSITION_ELBOW_LEFT], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_ELBOW_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_ELBOW_LEFT].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_WRIST_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_WRIST_LEFT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_ELBOW_LEFT], pointSet[NUI_SKELETON_POSITION_WRIST_LEFT], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_WRIST_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_WRIST_LEFT].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_HAND_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_HAND_LEFT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_WRIST_LEFT], pointSet[NUI_SKELETON_POSITION_HAND_LEFT], color, 2);   
   
    //右上肢   
    if((pointSet[NUI_SKELETON_POSITION_SHOULDER_CENTER].x!=0 || pointSet[NUI_SKELETON_POSITION_SHOULDER_CENTER].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_SHOULDER_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_SHOULDER_RIGHT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_SHOULDER_CENTER], pointSet[NUI_SKELETON_POSITION_SHOULDER_RIGHT], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_SHOULDER_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_SHOULDER_RIGHT].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_ELBOW_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_ELBOW_RIGHT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_SHOULDER_RIGHT], pointSet[NUI_SKELETON_POSITION_ELBOW_RIGHT], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_ELBOW_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_ELBOW_RIGHT].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_WRIST_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_WRIST_RIGHT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_ELBOW_RIGHT], pointSet[NUI_SKELETON_POSITION_WRIST_RIGHT], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_WRIST_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_WRIST_RIGHT].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_HAND_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_HAND_RIGHT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_WRIST_RIGHT], pointSet[NUI_SKELETON_POSITION_HAND_RIGHT], color, 2);   
   
    //左下肢   
    if((pointSet[NUI_SKELETON_POSITION_HIP_CENTER].x!=0 || pointSet[NUI_SKELETON_POSITION_HIP_CENTER].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_HIP_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_HIP_LEFT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_HIP_CENTER], pointSet[NUI_SKELETON_POSITION_HIP_LEFT], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_HIP_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_HIP_LEFT].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_KNEE_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_KNEE_LEFT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_HIP_LEFT], pointSet[NUI_SKELETON_POSITION_KNEE_LEFT], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_KNEE_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_KNEE_LEFT].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_ANKLE_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_ANKLE_LEFT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_KNEE_LEFT], pointSet[NUI_SKELETON_POSITION_ANKLE_LEFT], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_ANKLE_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_ANKLE_LEFT].y!=0) &&    
        (pointSet[NUI_SKELETON_POSITION_FOOT_LEFT].x!=0 || pointSet[NUI_SKELETON_POSITION_FOOT_LEFT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_ANKLE_LEFT], pointSet[NUI_SKELETON_POSITION_FOOT_LEFT], color, 2);   
   
    //右下肢   
    if((pointSet[NUI_SKELETON_POSITION_HIP_CENTER].x!=0 || pointSet[NUI_SKELETON_POSITION_HIP_CENTER].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_HIP_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_HIP_RIGHT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_HIP_CENTER], pointSet[NUI_SKELETON_POSITION_HIP_RIGHT], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_HIP_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_HIP_RIGHT].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_KNEE_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_KNEE_RIGHT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_HIP_RIGHT], pointSet[NUI_SKELETON_POSITION_KNEE_RIGHT],color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_KNEE_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_KNEE_RIGHT].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_ANKLE_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_ANKLE_RIGHT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_KNEE_RIGHT], pointSet[NUI_SKELETON_POSITION_ANKLE_RIGHT], color, 2);   
    if((pointSet[NUI_SKELETON_POSITION_ANKLE_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_ANKLE_RIGHT].y!=0) &&   
        (pointSet[NUI_SKELETON_POSITION_FOOT_RIGHT].x!=0 || pointSet[NUI_SKELETON_POSITION_FOOT_RIGHT].y!=0))   
        line(image, pointSet[NUI_SKELETON_POSITION_ANKLE_RIGHT], pointSet[NUI_SKELETON_POSITION_FOOT_RIGHT], color, 2);   
}

//WORLD坐标系下的坐标装换成TORSO坐标系下的点
void posInTorso(double* pos,double* torso,double** R){
	double temp[3];
	int i = 0,j;
	double sum;
	for(i=0;i<3;i++){
		temp[i] = pos[i] - torso[i];
	}
	for(i=0;i<3;i++){
		sum = 0;
		for(j=0;j<3;j++){
			sum += R[i][j] * temp[j];
		}
		pos[i] = sum;
	}
}

//模仿者TORSO坐标系转换成机器人的TORSO坐标系
void rotate(double* pos){
	double temp[3];
	temp[0] = -pos[2];
	temp[1] = -pos[0];
	temp[2] = pos[1];
	for(int i=0;i<3;i++){
		pos[i] = temp[i];
	}
}

//求躯干高度
double maxHeight(double h1,double h2){
	if(h1>=h2){
		return h1;
	}else{
		return h2;
	}
}

//关节速度调整，应对人的动作关节速度大于机器人最大关节速度的情况
int speedAdjustment(double time,double LShoulderPitch,double LShoulderRoll,double LElbowRoll,double LElbowYaw,double RShoulderPitch,double RShoulderRoll,double RElbowRoll,double RElbowYaw,double LHipRoll,double LHipPitch,double LKneePitch,double LAnklePitch,double LAnkleRoll,double RHipRoll,double RHipPitch,double RKneePitch,double RAnklePitch,double RAnkleRoll){
	int add = 0;
	while(((LShoulderPitch*PI/180)/(1.0*(time+add)/1000)) > MAX_LShoulderPitch*COEFFICIENT){
		add += PERIOD;
	}
	while(((LShoulderRoll*PI/180)/(1.0*(time+add)/1000)) > MAX_LShoulderRoll*COEFFICIENT){
		add += PERIOD;
	}
	while(((LElbowRoll*PI/180)/(1.0*(time+add)/1000)) > MAX_LElbowRoll*COEFFICIENT){
		add += PERIOD;
	}
	while(((LElbowYaw*PI/180)/(1.0*(time+add)/1000)) > MAX_LElbowYaw*COEFFICIENT){
		add += PERIOD;
	}
	while(((RShoulderPitch*PI/180)/(1.0*(time+add)/1000)) > MAX_RShoulderPitch*COEFFICIENT){
		add += PERIOD;
	}
	while(((RShoulderRoll*PI/180)/(1.0*(time+add)/1000)) > MAX_RShoulderRoll*COEFFICIENT){
		add += PERIOD;
	}
	while(((RElbowRoll*PI/180)/(1.0*(time+add)/1000)) > MAX_RElbowRoll*COEFFICIENT){
		add += PERIOD;
	}
	while(((RElbowYaw*PI/180)/(1.0*(time+add)/1000)) > MAX_RElbowYaw*COEFFICIENT){
		add += PERIOD;
	}
	return add;
}

//上肢IK处理线程(子线程)
DWORD WINAPI upIK(PVOID pParam){  
	sParam sparam;  
	sparam = (sParam)pParam;
	result_up = myIKSolver->ikUpperBody(sparam->RSE,sparam->REH,sparam->LSE,sparam->LEH);	// 传入方向向量，并重定位解决
	return 0;
}

//对每一帧数据处理(子线程)
DWORD WINAPI calculateJoints(LPVOID lpParameter){
	char sendBuf[100];																		// 发送缓冲区
	char buffer[100];																		// 写入文件的内容
	int supportMode = 0;																	// 当前机器人的支撑模式,0 for double support,1 for left support and 2 for right support
	int supportOrder = 0;																	// 支撑指令，0 for none,1 for double->left,2 for double->right,3 for left->double,4 for right->double
	int Time = 0;																			// 机器人本次指令执行时间
	double jointPos[JOINT_RECORD_ACCOUNT][3];												// 本帧的关节数据
	int doubleSupportConfig = 0;															// 本帧double support下肢关节是否配置
	sprintf(buffer,"=============== 关节配置记录文件 ==================\nMass(NAO) = %0.6lf\n\n",Mass);
	myLog->jointRecord(buffer);
	myLog->jointFlush();
	while(true){
		try{
			if(readFrame < writeFrame){
				clock_t time1 = clock();													// 一次数据处理的开始时刻
				calculating = 1;
				int add = 0;																// 因为机器人关节速度限制需要延长的命令发送时间间隔，自然影响采样频率
				int period = 0;																// 纯采样时间(一帧)，理想动作执行时间
				int no = readFrame->no;
				sprintf(buffer,"\n\n\n--------------------- 第%d帧的关节配置:------------------------\n",no);
				myLog->jointRecord(buffer);
				myLog->jointFlush();
				for(int i=0;i<JOINT_RECORD_ACCOUNT;i++){
					jointPos[i][0] = readFrame->location[i][0];
					jointPos[i][1] = readFrame->location[i][1];
					jointPos[i][2] = readFrame->location[i][2];
				}
				supportMode = readFrame->supportMode;
				supportOrder = readFrame->supportOrder;
				period = readFrame->time;
				doubleSupportConfig = readFrame->doubleSupportConfig;
				++readFrame;
				int sign = 1;															// 根据平衡控制结果决定是否传输命令帧
				result_up = -1;
				result_low = -1;
				cout<<"动作变化，正在传输模仿帧............."<<endl;
				double RShoulderRoll,RShoulderPitch,RElbowRoll,RElbowYaw;
				double LShoulderRoll,LShoulderPitch,LElbowRoll,LElbowYaw;
				double RHipRoll,RHipPitch,RKneePitch,RAnklePitch,RAnkleRoll;
				double LHipRoll,LHipPitch,LKneePitch,LAnklePitch,LAnkleRoll;
				vect RSE,REH,LSE,LEH,RHK,RKA,LHK,LKA;
				//RShoulder-RElbow
				RSE.x = jointPos[4][0] - jointPos[2][0];
				RSE.y = jointPos[4][1] - jointPos[2][1];
				RSE.z = jointPos[4][2] - jointPos[2][2];
				//RElbow-RHand
				REH.x = jointPos[6][0] - jointPos[4][0];
				REH.y = jointPos[6][1] - jointPos[4][1];
				REH.z = jointPos[6][2] - jointPos[4][2];
				//LShoulder-LElbow
				LSE.x = jointPos[3][0] - jointPos[1][0];
				LSE.y = jointPos[3][1] - jointPos[1][1];
				LSE.z = jointPos[3][2] - jointPos[1][2];
				//LElbow-LHand
				LEH.x = jointPos[5][0] - jointPos[3][0];
				LEH.y = jointPos[5][1] - jointPos[3][1];
				LEH.z = jointPos[5][2] - jointPos[3][2];
				//RHip-RKnee
				RHK.x = jointPos[10][0] - jointPos[8][0];
				RHK.y = jointPos[10][1] - jointPos[8][1];
				RHK.z = jointPos[10][2] - jointPos[8][2];
				//RKnee-RAnkle
				RKA.x = jointPos[12][0] - jointPos[10][0];
				RKA.y = jointPos[12][1] - jointPos[10][1];
				RKA.z = jointPos[12][2] - jointPos[10][2];
				//LHip-LKnee
				LHK.x = jointPos[9][0] - jointPos[7][0];
				LHK.y = jointPos[9][1] - jointPos[7][1];
				LHK.z = jointPos[9][2] - jointPos[7][2];
				//LKnee-LAnkle
				LKA.x = jointPos[11][0] - jointPos[9][0];
				LKA.y = jointPos[11][1] - jointPos[9][1];
				LKA.z = jointPos[11][2] - jointPos[9][2];
				double vRSE = mathFunction.val(RSE);
				double vREH = mathFunction.val(REH);
				double vLSE = mathFunction.val(LSE);
				double vLEH = mathFunction.val(LEH);
				double vRHK = mathFunction.val(RHK);
				double vRKA = mathFunction.val(RKA);
				double vLHK = mathFunction.val(LHK);
				double vLKA = mathFunction.val(LKA);
				RSE.x = RSE.x/vRSE;
				RSE.y = RSE.y/vRSE;
				RSE.z = RSE.z/vRSE;
				REH.x = REH.x/vREH;
				REH.y = REH.y/vREH;
				REH.z = REH.z/vREH;
				LSE.x = LSE.x/vLSE;
				LSE.y = LSE.y/vLSE;
				LSE.z = LSE.z/vLSE;
				LEH.x = LEH.x/vLEH;
				LEH.y = LEH.y/vLEH;
				LEH.z = LEH.z/vLEH;
				RHK.x = RHK.x/vRHK;
				RHK.y = RHK.y/vRHK;
				RHK.z = RHK.z/vRHK;
				RKA.x = RKA.x/vRKA;
				RKA.y = RKA.y/vRKA;
				RKA.z = RKA.z/vRKA;
				LHK.x = LHK.x/vLHK;
				LHK.y = LHK.y/vLHK;
				LHK.z = LHK.z/vLHK;
				LKA.x = LKA.x/vLKA;
				LKA.y = LKA.y/vLKA;
				LKA.z = LKA.z/vLKA;
				sprintf(buffer,"\n各个关节链向量:\n");
				myLog->jointRecord(buffer);
				sprintf(buffer,"RSE = [%0.10lf,%0.10lf,%0.10lf]';\n",RSE.x,RSE.y,RSE.z);
				myLog->jointRecord(buffer);
				sprintf(buffer,"REH = [%0.10lf,%0.10lf,%0.10lf]';\n",REH.x,REH.y,REH.z);
				myLog->jointRecord(buffer);
				sprintf(buffer,"LSE = [%0.10lf,%0.10lf,%0.10lf]';\n",LSE.x,LSE.y,LSE.z);
				myLog->jointRecord(buffer);
				sprintf(buffer,"LEH = [%0.10lf,%0.10lf,%0.10lf]';\n",LEH.x,LEH.y,LEH.z);
				myLog->jointRecord(buffer);
				sprintf(buffer,"RHK = [%0.10lf,%0.10lf,%0.10lf]';\n",RHK.x,RHK.y,RHK.z);
				myLog->jointRecord(buffer);
				sprintf(buffer,"RKA = [%0.10lf,%0.10lf,%0.10lf]';\n",RKA.x,RKA.y,RKA.z);
				myLog->jointRecord(buffer);
				sprintf(buffer,"LHK = [%0.10lf,%0.10lf,%0.10lf]';\n",LHK.x,LHK.y,LHK.z);
				myLog->jointRecord(buffer);
				sprintf(buffer,"LKA = [%0.10lf,%0.10lf,%0.10lf]';\n",LKA.x,LKA.y,LKA.z);
				myLog->jointRecord(buffer);
				myLog->jointFlush();
				myRobot->referencePose();												// 参考姿态,DLS算法可考虑省略该步
				myIKSolver->forwardKinematics(1);
				HANDLE hThrd = NULL;
				IKUPPara sparam;  
				IKUPPara *p;  
				sparam.RSE = RSE; 
				sparam.REH = REH; 
				sparam.LSE = LSE;
				sparam.LEH = LEH;
				p = &sparam;  
				hThrd = (HANDLE)CreateThread(NULL,  0,  upIK,  p,  0,  NULL);  
				if(supportMode == 0){
					if(doubleSupportConfig){
						/* Kinect采集存在双腿数据不对称问题，为了保证double support模仿效果滤掉所有不对称数据，和double support
						数据采集类似，以左腿为参考数据，同时考虑Kinect采集错误，不应该存在RHK.y>0 */
						if(LHK.y < 0){
							LHK.y = -LHK.y;
						}
						RHK = LHK;
						RKA = LKA;
						RHK.y = -LHK.y;
						RKA.y = -LKA.y;
						sprintf(buffer,"\n对下肢向量修正后:\n");
						myLog->jointRecord(buffer);
						sprintf(buffer,"RHK = [%0.10lf,%0.10lf,%0.10lf]';\n",RHK.x,RHK.y,RHK.z);
						myLog->jointRecord(buffer);
						sprintf(buffer,"RKA = [%0.10lf,%0.10lf,%0.10lf]';\n",RKA.x,RKA.y,RKA.z);
						myLog->jointRecord(buffer);
						sprintf(buffer,"LHK = [%0.10lf,%0.10lf,%0.10lf]';\n",LHK.x,LHK.y,LHK.z);
						myLog->jointRecord(buffer);
						sprintf(buffer,"LKA = [%0.10lf,%0.10lf,%0.10lf]';\n",LKA.x,LKA.y,LKA.z);
						myLog->jointRecord(buffer);
						myLog->jointFlush();
						result_low = myIKSolver->ikLowerBody(RHK,RKA,LHK,LKA);			// 传入方向向量，并重定位解决
					}else{
						result_low = 0;
					}
				}else{
					result_low = myIKSolver->ikLowerBody(RHK,RKA,LHK,LKA);				// 传入方向向量，并重定位解决
				}
				while(result_low == -1 || result_up == -1);								// 等待IK解完计算才能计算平衡
				int collision = 1;														// 自碰撞分析标志
				if(sign){
					collision = myIKSolver->selfCollisionAvoidance();					// 自碰撞分析
				}
				myZMP->balance(supportMode,sign,Mass,no);								// 平衡控制,在IK计算之后
				sprintf(buffer,"\n支撑模式以及平衡结果:\n");
				myLog->jointRecord(buffer);
				sprintf(buffer,"supportMode = %d,sign = %d,doubleSupportConfig = %d\n",supportMode,sign,doubleSupportConfig);
				myLog->jointRecord(buffer);
				sprintf(buffer,"\n自碰撞分析结果:\n");
				myLog->jointRecord(buffer);
				sprintf(buffer,"Collision = %d\n",collision);
				myLog->jointRecord(buffer);
				myLog->jointFlush();
				sign = collision;
				if(sign){
					/*
					// 理想关节位置记录
					double PosLElbowx,PosLElbowy,PosLElbowz,PosRElbowx,PosRElbowy,PosRElbowz,PosLWristx,PosLWristy,PosLWristz,PosRWristx,PosRWristy,PosRWristz;
					double PosLKneex,PosLKneey,PosLKneez,PosRKneex,PosRKneey,PosRKneez,PosLAnklex,PosLAnkley,PosLAnklez,PosRAnklex,PosRAnkley,PosRAnklez;
					PosLElbowx = uLINK[LARM_J1].p[0] + LSE.x*SE_LENGTH;
					PosLElbowy = uLINK[LARM_J1].p[1] + LSE.y*SE_LENGTH;
					PosLElbowz = uLINK[LARM_J1].p[2] + LSE.z*SE_LENGTH;
					PosRElbowx = uLINK[RARM_J1].p[0] + RSE.x*SE_LENGTH;
					PosRElbowy = uLINK[RARM_J1].p[1] + RSE.y*SE_LENGTH;
					PosRElbowz = uLINK[RARM_J1].p[2] + RSE.z*SE_LENGTH;
					PosLWristx = uLINK[LARM_J1].p[0] + LSE.x*SE_LENGTH + LEH.x*(ELBOWtoWRIST+WRISTtoHAND);
					PosLWristy = uLINK[LARM_J1].p[1] + LSE.y*SE_LENGTH + LEH.y*(ELBOWtoWRIST+WRISTtoHAND);
					PosLWristz = uLINK[LARM_J1].p[2] + LSE.z*SE_LENGTH + LEH.z*(ELBOWtoWRIST+WRISTtoHAND);
					PosRWristx = uLINK[RARM_J1].p[0] + RSE.x*SE_LENGTH + REH.x*(ELBOWtoWRIST+WRISTtoHAND);
					PosRWristy = uLINK[RARM_J1].p[1] + RSE.y*SE_LENGTH + REH.y*(ELBOWtoWRIST+WRISTtoHAND);
					PosRWristz = uLINK[RARM_J1].p[2] + RSE.z*SE_LENGTH + REH.z*(ELBOWtoWRIST+WRISTtoHAND);
					PosLKneex = uLINK[LLEG_J2].p[0] + LHK.x*LEGtoKNEE;
					PosLKneey = uLINK[LLEG_J2].p[1] + LHK.y*LEGtoKNEE;
					PosLKneez = uLINK[LLEG_J2].p[2] + LHK.z*LEGtoKNEE;
					PosRKneex = uLINK[RLEG_J2].p[0] + RHK.x*LEGtoKNEE;
					PosRKneey = uLINK[RLEG_J2].p[1] + RHK.y*LEGtoKNEE;
					PosRKneez = uLINK[RLEG_J2].p[2] + RHK.z*LEGtoKNEE;
					PosLAnklex = uLINK[LLEG_J2].p[0] + LHK.x*LEGtoKNEE + LKA.x*KNEEtoFOOT;
					PosLAnkley = uLINK[LLEG_J2].p[1] + LHK.y*LEGtoKNEE + LKA.y*KNEEtoFOOT;
					PosLAnklez = uLINK[LLEG_J2].p[2] + LHK.z*LEGtoKNEE + LKA.z*KNEEtoFOOT;
					PosRAnklex = uLINK[RLEG_J2].p[0] + RHK.x*LEGtoKNEE + RKA.x*KNEEtoFOOT;
					PosRAnkley = uLINK[RLEG_J2].p[1] + RHK.y*LEGtoKNEE + RKA.y*KNEEtoFOOT;
					PosRAnklez = uLINK[RLEG_J2].p[2] + RHK.z*LEGtoKNEE + RKA.z*KNEEtoFOOT;
					sprintf(buffer,"%d %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf %0.6lf\n",no,PosLElbowx,PosLElbowy,PosLElbowz,PosRElbowx,PosRElbowy,PosRElbowz,PosLWristx,PosLWristy,PosLWristz,PosRWristx,PosRWristy,PosRWristz,PosLKneex,PosLKneey,PosLKneez,PosRKneex,PosRKneey,PosRKneez,PosLAnklex,PosLAnkley,PosLAnklez,PosRAnklex,PosRAnkley,PosRAnklez);
					myLog->errRecord(buffer);
					myLog->errFlush();													// 记录理论配置位置
					*/
					double result = result_up + result_low;
					RShoulderPitch = myRobot->uLINK[RARM_J0].q/(ToRad);
					RShoulderRoll = myRobot->uLINK[RARM_J1].q/(ToRad);
					RElbowYaw = myRobot->uLINK[RARM_J2].q/(ToRad);
					RElbowRoll = myRobot->uLINK[RARM_J3].q/(ToRad);
					LShoulderPitch = myRobot->uLINK[LARM_J0].q/(ToRad);
					LShoulderRoll = myRobot->uLINK[LARM_J1].q/(ToRad);
					LElbowYaw = myRobot->uLINK[LARM_J2].q/(ToRad);
					LElbowRoll = myRobot->uLINK[LARM_J3].q/(ToRad);
					RHipRoll = myRobot->uLINK[RLEG_J1].q/(ToRad);
					RHipPitch = myRobot->uLINK[RLEG_J2].q/(ToRad);
					RKneePitch = myRobot->uLINK[RLEG_J3].q/(ToRad);
					RAnklePitch = myRobot->uLINK[RLEG_J4].q/(ToRad);
					RAnkleRoll = myRobot->uLINK[RLEG_J5].q/(ToRad);
					LHipRoll = myRobot->uLINK[LLEG_J1].q/(ToRad);
					LHipPitch = myRobot->uLINK[LLEG_J2].q/(ToRad);
					LKneePitch = myRobot->uLINK[LLEG_J3].q/(ToRad);
					LAnklePitch = myRobot->uLINK[LLEG_J4].q/(ToRad);
					LAnkleRoll = myRobot->uLINK[LLEG_J5].q/(ToRad);
					//根据上一帧的关节角度和本帧的角度计算角速度，从而计算机器人完成这一动作的时间，一般和采样时间相同
					add = speedAdjustment(period,fabs(LShoulderPitch - last_LShoulderPitch),fabs(LShoulderRoll - last_LShoulderRoll),fabs(LElbowRoll - last_LElbowRoll),fabs(LElbowYaw - last_LElbowYaw),fabs(RShoulderPitch - last_RShoulderPitch),fabs(RShoulderRoll - last_RShoulderRoll),fabs(RElbowRoll - last_RElbowRoll),fabs(RElbowYaw - last_RElbowYaw),fabs(LHipRoll - last_LHipRoll),fabs(LHipPitch - last_LHipPitch),fabs(LKneePitch - last_LKneePitch),fabs(LAnklePitch - last_LAnklePitch),fabs(LAnkleRoll - last_LAnkleRoll),fabs(RHipRoll - last_RHipRoll),fabs(RHipPitch - last_RHipPitch),fabs(RKneePitch - last_RKneePitch),fabs(RAnklePitch - last_RAnklePitch),fabs(RAnkleRoll - last_RAnkleRoll));
					//如果动作幅度大于阈值需要保存这一帧的关节角度配置，以便下一大于阈值帧的角速度计算使用
					last_LShoulderPitch = LShoulderPitch;
					last_LShoulderRoll = LShoulderRoll;
					last_LElbowRoll = LElbowRoll;
					last_LElbowYaw = LElbowYaw;
					last_RShoulderPitch = RShoulderPitch;
					last_RShoulderRoll = RShoulderRoll;
					last_RElbowRoll = RElbowRoll;
					last_RElbowYaw = RElbowYaw;
					last_RHipRoll = RHipRoll;
					last_RHipPitch = RHipPitch;
					last_RKneePitch = RKneePitch;
					last_RAnklePitch = RAnklePitch;
					last_RAnkleRoll = RAnkleRoll;
					last_LHipRoll = LHipRoll;
					last_LHipPitch = LHipPitch;
					last_LKneePitch = LKneePitch;
					last_LAnklePitch = LAnklePitch;
					last_LAnkleRoll = LAnkleRoll;
					Time = period + add;												// 每秒采样30帧，采样一帧的时间+运动限制外加时间
					if(Time > 2000){
						Time = 2000;
					}
					clock_t time2 = clock();											// 一次数据处理的结束时刻
					if(result < 0.01){
						sprintf(buffer,"\n--------- IK配置误差小于1cm，err = %0.10lf 关节配置如下 ---------\n",result);
						myLog->jointRecord(buffer);
					}else{
						sprintf(buffer,"\n--------- IK配置误差大于1cm，err = %0.10lf 关节配置如下 ---------\n",result);
						myLog->jointRecord(buffer);
					}
					sprintf(buffer,"RShoulderPitch = %0.3lf\n",RShoulderPitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RShoulderRoll = %0.3lf\n",RShoulderRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RElbowYaw = %0.3lf\n",RElbowYaw);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RElbowRoll = %0.3lf\n",RElbowRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LShoulderPitch = %0.3lf\n",LShoulderPitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LShoulderRoll = %0.3lf\n",LShoulderRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LElbowYaw = %0.3lf\n",LElbowYaw);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LElbowRoll = %0.3lf\n",LElbowRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RHipRoll = %0.3lf\n",RHipRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RHipPitch = %0.3lf\n",RHipPitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RKneePitch = %0.3lf\n",RKneePitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RAnklePitch = %0.3lf\n",RAnklePitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RAnkleRoll = %0.3lf\n",RAnkleRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LHipRoll = %0.3lf\n",LHipRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LHipPitch = %0.3lf\n",LHipPitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LKneePitch = %0.3lf\n",LKneePitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LAnklePitch = %0.3lf\n",LAnklePitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LAnkleRoll = %0.3lf\n",LAnkleRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"Calculating time = %0.3lfs\n",1.0*(time2 - time1)/1000);
					myLog->jointRecord(buffer);
					sprintf(buffer,"Executing time = %0.3lfs\n",1.0*(Time)/1000);
					myLog->jointRecord(buffer);
					myLog->jointFlush();
					sprintf(sendBuf,"%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%0.3lf,%d,%d,%d,Frame%d,Calculating time = %d(ms),0.0\n",RShoulderPitch,RShoulderRoll,RElbowRoll,RElbowYaw,LShoulderPitch,LShoulderRoll,LElbowRoll,LElbowYaw,RHipRoll,RHipPitch,RKneePitch,RAnklePitch,RAnkleRoll,LHipRoll,LHipPitch,LKneePitch,LAnklePitch,LAnkleRoll,1.0*(Time)/1000,supportMode,supportOrder,doubleSupportConfig,no,time2-time1);
					int num = send(clientSocket,sendBuf,strlen(sendBuf)+1,0);
					if(num != strlen(sendBuf) + 1){
						printf("Connection to server terminated\n");
						int status = closesocket(clientSocket);  
						if (status == SOCKET_ERROR)  
							printf("ERROR: close client socket unsuccessful\n");  
						status=WSACleanup();  
						if (status == SOCKET_ERROR)  
							printf("ERROR: WSACleanup unsuccessful\n");  
						return -1;  
					}
					cout<<"动作传输完毕========================"<<endl;
				}else{
					RShoulderPitch = myRobot->uLINK[RARM_J0].q/(ToRad);
					RShoulderRoll = myRobot->uLINK[RARM_J1].q/(ToRad);
					RElbowYaw = myRobot->uLINK[RARM_J2].q/(ToRad);
					RElbowRoll = myRobot->uLINK[RARM_J3].q/(ToRad);
					LShoulderPitch = myRobot->uLINK[LARM_J0].q/(ToRad);
					LShoulderRoll = myRobot->uLINK[LARM_J1].q/(ToRad);
					LElbowYaw = myRobot->uLINK[LARM_J2].q/(ToRad);
					LElbowRoll = myRobot->uLINK[LARM_J3].q/(ToRad);
					RHipRoll = myRobot->uLINK[RLEG_J1].q/(ToRad);
					RHipPitch = myRobot->uLINK[RLEG_J2].q/(ToRad);
					RKneePitch = myRobot->uLINK[RLEG_J3].q/(ToRad);
					RAnklePitch = myRobot->uLINK[RLEG_J4].q/(ToRad);
					RAnkleRoll = myRobot->uLINK[RLEG_J5].q/(ToRad);
					LHipRoll = myRobot->uLINK[LLEG_J1].q/(ToRad);
					LHipPitch = myRobot->uLINK[LLEG_J2].q/(ToRad);
					LKneePitch = myRobot->uLINK[LLEG_J3].q/(ToRad);
					LAnklePitch = myRobot->uLINK[LLEG_J4].q/(ToRad);
					LAnkleRoll = myRobot->uLINK[LLEG_J5].q/(ToRad);
					sprintf(buffer,"平衡配置失败，略去本帧\n");
					myLog->jointRecord(buffer);
					sprintf(buffer,"RShoulderPitch = %0.3lf\n",RShoulderPitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RShoulderRoll = %0.3lf\n",RShoulderRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RElbowYaw = %0.3lf\n",RElbowYaw);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RElbowRoll = %0.3lf\n",RElbowRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LShoulderPitch = %0.3lf\n",LShoulderPitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LShoulderRoll = %0.3lf\n",LShoulderRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LElbowYaw = %0.3lf\n",LElbowYaw);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LElbowRoll = %0.3lf\n",LElbowRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RHipRoll = %0.3lf\n",RHipRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RHipPitch = %0.3lf\n",RHipPitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RKneePitch = %0.3lf\n",RKneePitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RAnklePitch = %0.3lf\n",RAnklePitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"RAnkleRoll = %0.3lf\n",RAnkleRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LHipRoll = %0.3lf\n",LHipRoll);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LHipPitch = %0.3lf\n",LHipPitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LKneePitch = %0.3lf\n",LKneePitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LAnklePitch = %0.3lf\n",LAnklePitch);
					myLog->jointRecord(buffer);
					sprintf(buffer,"LAnkleRoll = %0.3lf\n",LAnkleRoll);
					myLog->jointRecord(buffer);
					myLog->jointFlush();
				}
			}else{
				calculating = 0;
			}
		}catch(...){
			continue;
		}
	}
	//myLog->errClose();
	myLog->jointClose();
	calculating = 0;
	return 0;
}

//模仿函数，采集样帧，没有错误发生返回0
int imitate(char* ip,int port){
	// ----------------------------------------------------------------------------------   Init0:机器人初始化
	myRobot->wakeUp();
	myIKSolver->forwardKinematics(1);
	Mass = myZMP->calcM(1);
	// ----------------------------------------------------------------------------------   Init0:机器人初始化

	// ----------------------------------------------------------------------------------   Init1:套接字初始化
	int err;																				// 套接字连接错误返回
	WORD versionRequired;
	WSADATA wsaData;
	const char* server_ip = ip;																// 服务器IP
	const int server_port = port;															// 服务器端口
	versionRequired=MAKEWORD(1,1);
	err=WSAStartup(versionRequired,&wsaData);												// 协议库的版本信息
	if (!err){
		printf("客户端嵌套字已经打开!\n");
	}
	else{
		printf("客户端的嵌套字打开失败!\n");
		return -1;
	}
	clientSocket=socket(AF_INET,SOCK_STREAM,0);
	SOCKADDR_IN serversock_in;
	serversock_in.sin_addr.S_un.S_addr=inet_addr(server_ip);
	serversock_in.sin_family=AF_INET;
	serversock_in.sin_port=htons(server_port);
	int status = connect(clientSocket,(SOCKADDR*)&serversock_in,sizeof(SOCKADDR));
	if (status == SOCKET_ERROR){  
		cout<< "ERROR: connect to server unsuccessful" << endl;
		status=closesocket(clientSocket);  
		if (status == SOCKET_ERROR)  
			cout<< "ERROR: close client socket unsuccessful"<< endl;  
		status=WSACleanup();  
		if (status == SOCKET_ERROR)  
			cout<< "ERROR: WSACleanup unsuccessful"<< endl;  
		return -1;  
	}else{
		cout<<"服务器连接成功"<<endl;
	}
	// ----------------------------------------------------------------------------------   Init1:套接字初始化

	// ----------------------------------------------------------------------------------   Init2:采集设备初始化
	NUI_SKELETON_FRAME skeletonFrame = {0};													// 骨骼帧的定义 
	Mat skeletonImage;																		// 用图片显示捕获到的动作
	IplImage pImg;																			// 保存显示图像
    skeletonImage.create(240, 320, CV_8UC3);  
    CvPoint skeletonPoint[NUI_SKELETON_COUNT][NUI_SKELETON_POSITION_COUNT]={cvPoint(0,0)}; 
    HRESULT hr = NuiInitialize(NUI_INITIALIZE_FLAG_USES_SKELETON);							// 初始化NUI(需要Kinect)，注意这里是USES_SKELETON  
    if (FAILED(hr)){   
        cout<<"NuiInitialize failed"<<endl;   
        return hr;   
    }  
    HANDLE skeletonEvent = CreateEvent( NULL, TRUE, FALSE, NULL );							// 定义骨骼信号事件句柄 
    hr = NuiSkeletonTrackingEnable( skeletonEvent, 0 );										// 打开骨骼跟踪事件  
    if( FAILED( hr ) ){   
        cout<<"Could not open color image stream video"<<endl;   
        NuiShutdown();   
        return hr;   
    }  
	cvNamedWindow("Image",CV_WINDOW_AUTOSIZE);												// 显示图片的窗口
	// ----------------------------------------------------------------------------------   Init2:采集设备初始化

	// ----------------------------------------------------------------------------------   Init3:数据采集变量有关
	writeFrame = (Frame*)malloc(sizeof(struct Frame)*MAX_FRAME);							// 申请记录帧的内存空间
	readFrame = writeFrame;																	// 读取帧指向帧内存空间开头
	int UserID = 0;																			// 被检测到的用户ID
	int current_frame=0;																	// 记录帧
	const int min_frame = 100;																// 最小开始记录帧
	char image_name[100]={0};																// 保存图片文件名
	double** jointPos = (double**)malloc(sizeof(double*)*JOINT_RECORD_ACCOUNT);				// 本帧的关节数据
	for(int i=0;i<JOINT_RECORD_ACCOUNT;i++){
		lastJoint[i] = (double*)malloc(sizeof(double)*3);
		jointPos[i] = (double*)malloc(sizeof(double)*3);
	}
	double TORSO[3],SPINE[3];																// TORSO和SPINE的WORLD(Kinect)坐标
	double** TORSO_R;																		// TORSO在Kinect坐标系下的旋转矩阵
	int supportMode = 0;																	// 当前机器人的支撑模式,0 for double support,1 for left support and 2 for right support
	int supportOrder = 0;																	// 支撑指令，0 for none,1 for double->left,2 for double->right,3 for left->double,4 for right->double
	int doubleSupportConfig = 0;															// 本帧double support下肢关节是否配置
	double torsoHeight;																		// 正常double support下Body高度
	double currentTorsoHeight;																// 本帧Body高度
	double lastTorsoHeight;																	// 上一帧Body高度
	char* buffer = (char*)malloc(sizeof(char)*100);											// 写入文件的内容
	clock_t time1;																			// 采集一帧的开始时间
	HANDLE hThread_1 = CreateThread(NULL, 0, calculateJoints, NULL, 0, NULL);				// 创建数据处理线程
	sprintf(buffer,"-------------------- 原始采集数据 --------------------\n\n");
	myLog->positionRecord(buffer);
	myLog->positionFlush();
	// ----------------------------------------------------------------------------------   Init3:数据采集变量有关

	while( true ){
		if(current_frame >= min_frame){
			time1 = clock();
		}
		bool bFoundSkeleton = false;														// Kinect是否检测到人
		if (WaitForSingleObject(skeletonEvent, INFINITE)==0){   
            hr = NuiSkeletonGetNextFrame( 0, &skeletonFrame);								// 从刚才打开数据流的流句柄中得到该帧数据，读取到的数据地址存于skeletonFrame  
            if (SUCCEEDED(hr)){																// NUI_SKELETON_COUNT是跟踪到的最大人数  
                for( int i = 0 ; i < NUI_SKELETON_COUNT ; i++ ){   
                    NUI_SKELETON_TRACKING_STATE trackingState = skeletonFrame.SkeletonData[i].eTrackingState;
                    if( trackingState == NUI_SKELETON_TRACKED ){							// Kinect最多检测六个人，但只能跟踪两个人的骨骼，再检查每个“人”（有可能是空，不是人）
                        bFoundSkeleton = true;
						UserID = i;
						cout<<"================== User Detected =================="<<endl;
						break;
                    }   
                }   
            }
            if( !bFoundSkeleton ){
				cout<<"Detecting User .................................."<<endl;
                continue;   
            }
			//const NUI_TRANSFORM_SMOOTH_PARAMETERS VerySmoothParams = {0.7f, 0.3f, 1.0f, 1.0f, 1.0f};		// Very smooth, but with a lot of latency.Filters out large jitters.
			const NUI_TRANSFORM_SMOOTH_PARAMETERS SomewhatLatentParams = {0.5f, 0.1f, 0.5f, 0.1f, 0.1f};	// Smoothed with some latency.Filters out medium jitters.
            NuiTransformSmooth(&skeletonFrame, &SomewhatLatentParams);										// 平滑骨骼帧，消除抖动
            skeletonImage.setTo(0);      
			if( skeletonFrame.SkeletonData[UserID].eTrackingState == NUI_SKELETON_TRACKED && skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_SHOULDER_CENTER] != NUI_SKELETON_POSITION_NOT_TRACKED){     //断定是否是一个正确骨骼的条件：骨骼被跟踪到并且肩部中心（颈部位置）必须跟踪到
				cout<<"-------------------- Skeleton Tracked --------------------"<<endl;
				++current_frame;
				float fx, fy;     
				/*
					拿到所有跟踪到的关节点的坐标，并转换为我们的深度空间的坐标，因为我们是在深度图像中  
					把这些关节点标记出来的  
					NUI_SKELETON_POSITION_COUNT为跟踪到的一个骨骼的关节点的数目，为20  
				*/
				for (int j = 0; j < NUI_SKELETON_POSITION_COUNT; j++){     
					NuiTransformSkeletonToDepthImage(skeletonFrame.SkeletonData[UserID].SkeletonPositions[j], &fx, &fy );     
					skeletonPoint[UserID][j].x = (int)fx;     
					skeletonPoint[UserID][j].y = (int)fy;     
				}
				if(skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_SPINE]==NUI_SKELETON_POSITION_NOT_TRACKED || 
					skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_SHOULDER_LEFT]==NUI_SKELETON_POSITION_NOT_TRACKED ||
					skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_SHOULDER_RIGHT]==NUI_SKELETON_POSITION_NOT_TRACKED ||
					skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_ELBOW_LEFT]==NUI_SKELETON_POSITION_NOT_TRACKED ||
					skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_ELBOW_RIGHT]==NUI_SKELETON_POSITION_NOT_TRACKED ||
					skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_WRIST_LEFT]==NUI_SKELETON_POSITION_NOT_TRACKED ||
					skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_WRIST_RIGHT]==NUI_SKELETON_POSITION_NOT_TRACKED ||
					skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_HIP_LEFT]==NUI_SKELETON_POSITION_NOT_TRACKED ||
					skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_HIP_RIGHT]==NUI_SKELETON_POSITION_NOT_TRACKED ||
					skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_KNEE_LEFT]==NUI_SKELETON_POSITION_NOT_TRACKED ||
					skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_KNEE_RIGHT]==NUI_SKELETON_POSITION_NOT_TRACKED ||
					skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_ANKLE_LEFT]==NUI_SKELETON_POSITION_NOT_TRACKED ||
					skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[NUI_SKELETON_POSITION_ANKLE_RIGHT]==NUI_SKELETON_POSITION_NOT_TRACKED){
						cout<<"骨骼数据丢失!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"<<endl;
						--current_frame;
						continue;
				}
				for (int j=0; j<NUI_SKELETON_POSITION_COUNT ; j++){     
					if (skeletonFrame.SkeletonData[UserID].eSkeletonPositionTrackingState[j] != NUI_SKELETON_POSITION_NOT_TRACKED){		//跟踪点一用有三种状态：1没有被跟踪到，2跟踪到，3根据跟踪到的估计到                                     
						circle(skeletonImage,skeletonPoint[UserID][j], 3, cvScalar(0, 255, 255), 1, 8, 0);
					}
				}
				drawSkeleton(skeletonImage, skeletonPoint[UserID], UserID);
				pImg = (IplImage)skeletonImage;
				if(current_frame == min_frame){
					SPINE[0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SPINE].x;
					SPINE[1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SPINE].y;
					SPINE[2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SPINE].z;
					TORSO[0] = lastJoint[0][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_CENTER].x;
					TORSO[1] = lastJoint[0][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_CENTER].y;
					TORSO[2] = lastJoint[0][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_CENTER].z;
					lastJoint[1][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_LEFT].x;
					lastJoint[1][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_LEFT].y;
					lastJoint[1][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_LEFT].z;
					lastJoint[2][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_RIGHT].x;
					lastJoint[2][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_RIGHT].y;
					lastJoint[2][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_RIGHT].z;
					lastJoint[3][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ELBOW_LEFT].x;
					lastJoint[3][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ELBOW_LEFT].y;
					lastJoint[3][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ELBOW_LEFT].z;
					lastJoint[4][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ELBOW_RIGHT].x;
					lastJoint[4][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ELBOW_RIGHT].y;
					lastJoint[4][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ELBOW_RIGHT].z;
					lastJoint[5][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_WRIST_LEFT].x;
					lastJoint[5][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_WRIST_LEFT].y;
					lastJoint[5][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_WRIST_LEFT].z;
					lastJoint[6][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_WRIST_RIGHT].x;
					lastJoint[6][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_WRIST_RIGHT].y;
					lastJoint[6][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_WRIST_RIGHT].z;
					lastJoint[7][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_HIP_LEFT].x;
					lastJoint[7][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_HIP_LEFT].y;
					lastJoint[7][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_HIP_LEFT].z;
					lastJoint[8][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_HIP_RIGHT].x;
					lastJoint[8][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_HIP_RIGHT].y;
					lastJoint[8][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_HIP_RIGHT].z;
					lastJoint[9][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_KNEE_LEFT].x;
					lastJoint[9][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_KNEE_LEFT].y;
					lastJoint[9][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_KNEE_LEFT].z;
					lastJoint[10][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_KNEE_RIGHT].x;
					lastJoint[10][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_KNEE_RIGHT].y;
					lastJoint[10][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_KNEE_RIGHT].z;
					lastJoint[11][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_LEFT].x;
					lastJoint[11][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_LEFT].y;
					lastJoint[11][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_LEFT].z;
					lastJoint[12][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_RIGHT].x;
					lastJoint[12][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_RIGHT].y;
					lastJoint[12][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_RIGHT].z;
					lastTorsoHeight = maxHeight(fabs(lastJoint[0][1]-lastJoint[11][1]),fabs(lastJoint[0][1]-lastJoint[12][1]));
					torsoHeight = lastTorsoHeight;
					sprintf(buffer,"Torso Height = %0.5lfm\n\n\n",torsoHeight);
					myLog->positionRecord(buffer);
					myLog->positionFlush();
					double rx = -((TORSO[2]-SPINE[2])/fabs(TORSO[2]-SPINE[2]))*acos(fabs(TORSO[1]-SPINE[1])/(1.0*sqrt(pow(TORSO[1]-SPINE[1],2)+pow(TORSO[2]-SPINE[2],2))));
					double rz = ((TORSO[0]-SPINE[0])/fabs(TORSO[0]-SPINE[0]))*acos(fabs(TORSO[1]-SPINE[1])/(1.0*sqrt(pow(TORSO[0]-SPINE[0],2)+pow(TORSO[1]-SPINE[1],2))));
					TORSO_R = mathFunction.rpy2rot(rx,0,rz);								// 计算TORSO在Kinect坐标系下的旋转矩阵
					for(int i=0;i<JOINT_RECORD_ACCOUNT;i++){
						posInTorso(lastJoint[i],TORSO,TORSO_R);								// 转换成NAO Torso坐标系下的坐标
						rotate(lastJoint[i]);
					}
					for(int i=0;i<3;i++){
						free(TORSO_R[i]);
					}
					free(TORSO_R);
					vect RSE,REH,LSE,LEH,RHK,RKA,LHK,LKA;
					//RShoulder-RElbow
					RSE.x = lastJoint[4][0] - lastJoint[2][0];
					RSE.y = lastJoint[4][1] - lastJoint[2][1];
					RSE.z = lastJoint[4][2] - lastJoint[2][2];
					//RElbow-RHand
					REH.x = lastJoint[6][0] - lastJoint[4][0];
					REH.y = lastJoint[6][1] - lastJoint[4][1];
					REH.z = lastJoint[6][2] - lastJoint[4][2];
					//LShoulder-LElbow
					LSE.x = lastJoint[3][0] - lastJoint[1][0];
					LSE.y = lastJoint[3][1] - lastJoint[1][1];
					LSE.z = lastJoint[3][2] - lastJoint[1][2];
					//LElbow-LHand
					LEH.x = lastJoint[5][0] - lastJoint[3][0];
					LEH.y = lastJoint[5][1] - lastJoint[3][1];
					LEH.z = lastJoint[5][2] - lastJoint[3][2];
					//RHip-RKnee
					RHK.x = lastJoint[10][0] - lastJoint[8][0];
					RHK.y = lastJoint[10][1] - lastJoint[8][1];
					RHK.z = lastJoint[10][2] - lastJoint[8][2];
					//RKnee-RAnkle
					RKA.x = lastJoint[12][0] - lastJoint[10][0];
					RKA.y = lastJoint[12][1] - lastJoint[10][1];
					RKA.z = lastJoint[12][2] - lastJoint[10][2];
					//LHip-LKnee
					LHK.x = lastJoint[9][0] - lastJoint[7][0];
					LHK.y = lastJoint[9][1] - lastJoint[7][1];
					LHK.z = lastJoint[9][2] - lastJoint[7][2];
					//LKnee-LAnkle
					LKA.x = lastJoint[11][0] - lastJoint[9][0];
					LKA.y = lastJoint[11][1] - lastJoint[9][1];
					LKA.z = lastJoint[11][2] - lastJoint[9][2];
					double vRSE = mathFunction.val(RSE);
					double vREH = mathFunction.val(REH);
					double vLSE = mathFunction.val(LSE);
					double vLEH = mathFunction.val(LEH);
					double vRHK = mathFunction.val(RHK);
					double vRKA = mathFunction.val(RKA);
					double vLHK = mathFunction.val(LHK);
					double vLKA = mathFunction.val(LKA);
					RSE.x = RSE.x/vRSE;
					RSE.y = RSE.y/vRSE;
					RSE.z = RSE.z/vRSE;
					REH.x = REH.x/vREH;
					REH.y = REH.y/vREH;
					REH.z = REH.z/vREH;
					LSE.x = LSE.x/vLSE;
					LSE.y = LSE.y/vLSE;
					LSE.z = LSE.z/vLSE;
					LEH.x = LEH.x/vLEH;
					LEH.y = LEH.y/vLEH;
					LEH.z = LEH.z/vLEH;
					RHK.x = RHK.x/vRHK;
					RHK.y = RHK.y/vRHK;
					RHK.z = RHK.z/vRHK;
					RKA.x = RKA.x/vRKA;
					RKA.y = RKA.y/vRKA;
					RKA.z = RKA.z/vRKA;
					LHK.x = LHK.x/vLHK;
					LHK.y = LHK.y/vLHK;
					LHK.z = LHK.z/vLHK;
					LKA.x = LKA.x/vLKA;
					LKA.y = LKA.y/vLKA;
					LKA.z = LKA.z/vLKA;
					int sign = 0;															// 用来表示平衡处理结果
					myIKSolver->ikUpperBody(RSE,REH,LSE,LEH);								// 传入方向向量，并重定位解决
					myIKSolver->ikLowerBody(RHK,RKA,LHK,LKA);								// 传入方向向量，并重定位解决
					myZMP->balance(0,sign,Mass,current_frame-min_frame);
					//保存第min帧的关节角度配置
					last_LShoulderPitch = myRobot->uLINK[LARM_J0].q/(ToRad);
					last_LShoulderRoll = myRobot->uLINK[LARM_J1].q/(ToRad);
					last_LElbowYaw = myRobot->uLINK[LARM_J2].q/(ToRad);
					last_LElbowRoll = myRobot->uLINK[LARM_J3].q/(ToRad);
					last_RShoulderPitch = myRobot->uLINK[RARM_J0].q/(ToRad);
					last_RShoulderRoll = myRobot->uLINK[RARM_J1].q/(ToRad);
					last_RElbowYaw = myRobot->uLINK[RARM_J2].q/(ToRad);
					last_RElbowRoll = myRobot->uLINK[RARM_J3].q/(ToRad);
					last_RHipRoll = myRobot->uLINK[RLEG_J1].q/(ToRad);
					last_RHipPitch = myRobot->uLINK[RLEG_J2].q/(ToRad);
					last_RKneePitch = myRobot->uLINK[RLEG_J3].q/(ToRad);
					last_RAnklePitch = myRobot->uLINK[RLEG_J4].q/(ToRad);
					last_RAnkleRoll = myRobot->uLINK[RLEG_J5].q/(ToRad);
					last_LHipRoll = myRobot->uLINK[LLEG_J1].q/(ToRad);
					last_LHipPitch = myRobot->uLINK[LLEG_J2].q/(ToRad);
					last_LKneePitch = myRobot->uLINK[LLEG_J3].q/(ToRad);
					last_LAnklePitch = myRobot->uLINK[LLEG_J4].q/(ToRad);
					last_LAnkleRoll = myRobot->uLINK[LLEG_J5].q/(ToRad);
				}//if(current_frame == min_frame)
				if(current_frame>min_frame){												// 从第min_frame帧后开始记录3D坐标
					if(skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_LEFT].x>0 || 
						skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_RIGHT].x<0){		// 过滤坏帧
						--current_frame;
						continue;
					}
					SPINE[0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SPINE].x;
					SPINE[1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SPINE].y;
					SPINE[2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SPINE].z;
					TORSO[0] = jointPos[0][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_CENTER].x;
					TORSO[1] = jointPos[0][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_CENTER].y;
					TORSO[2] = jointPos[0][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_CENTER].z;
					jointPos[1][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_LEFT].x;
					jointPos[1][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_LEFT].y;
					jointPos[1][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_LEFT].z;
					jointPos[2][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_RIGHT].x;
					jointPos[2][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_RIGHT].y;
					jointPos[2][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_SHOULDER_RIGHT].z;
					jointPos[3][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ELBOW_LEFT].x;
					jointPos[3][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ELBOW_LEFT].y;
					jointPos[3][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ELBOW_LEFT].z;
					jointPos[4][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ELBOW_RIGHT].x;
					jointPos[4][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ELBOW_RIGHT].y;
					jointPos[4][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ELBOW_RIGHT].z;
					jointPos[5][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_WRIST_LEFT].x;
					jointPos[5][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_WRIST_LEFT].y;
					jointPos[5][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_WRIST_LEFT].z;
					jointPos[6][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_WRIST_RIGHT].x;
					jointPos[6][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_WRIST_RIGHT].y;
					jointPos[6][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_WRIST_RIGHT].z;
					jointPos[7][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_HIP_LEFT].x;
					jointPos[7][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_HIP_LEFT].y;
					jointPos[7][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_HIP_LEFT].z;
					jointPos[8][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_HIP_RIGHT].x;
					jointPos[8][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_HIP_RIGHT].y;
					jointPos[8][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_HIP_RIGHT].z;
					jointPos[9][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_KNEE_LEFT].x;
					jointPos[9][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_KNEE_LEFT].y;
					jointPos[9][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_KNEE_LEFT].z;
					jointPos[10][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_KNEE_RIGHT].x;
					jointPos[10][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_KNEE_RIGHT].y;
					jointPos[10][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_KNEE_RIGHT].z;
					jointPos[11][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_LEFT].x;
					jointPos[11][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_LEFT].y;
					jointPos[11][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_LEFT].z;
					jointPos[12][0] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_RIGHT].x;
					jointPos[12][1] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_RIGHT].y;
					jointPos[12][2] = skeletonFrame.SkeletonData[UserID].SkeletonPositions[NUI_SKELETON_POSITION_ANKLE_RIGHT].z;
					currentTorsoHeight = maxHeight(fabs(jointPos[0][1]-jointPos[11][1]),fabs(jointPos[0][1]-jointPos[12][1]));
					sprintf(buffer,"\n\n第%d帧骨骼数据：\n",current_frame-min_frame);
					myLog->positionRecord(buffer);
					/* 
						站立状态的切换判断应该基于世界坐标系(Kinect)，BODY≈COM，当COM.x>RFoot.x时，重心移至右脚，同时右脚高
						度比左脚大于一定阈值时，站立状态切位右脚。当COM.x<LFoot.x时，重心移至左脚，同时左脚高度比右脚大于一
						定阈值时，站立状态切位左脚。 
					*/
					supportOrder = 0;
					if(supportMode == 0){													// 双脚支撑
						if(jointPos[0][0] >= jointPos[12][0]-MAX_FOOT_SUPPORT_X && jointPos[11][1]-jointPos[12][1] >= MIN_FOOT_HEIGHT_DIFF){
							supportMode = 2;
							supportOrder = 2;
						}else if(jointPos[0][0] >= jointPos[12][0]-MAX_FOOT_SUPPORT_X){
							sprintf(buffer,"切换状态，略去本帧\n");
							myLog->positionRecord(buffer);
							continue;
						}
						if(jointPos[0][0] <= jointPos[11][0]+MAX_FOOT_SUPPORT_X && jointPos[12][1]-jointPos[11][1] >= MIN_FOOT_HEIGHT_DIFF){
							supportMode = 1;
							supportOrder = 1;
						}else if(jointPos[0][0] <= jointPos[11][0]+MAX_FOOT_SUPPORT_X){
							sprintf(buffer,"切换状态，略去本帧\n");
							myLog->positionRecord(buffer);
							continue;
						}
					}else if(supportMode == 1){												// 左脚支撑
						if(jointPos[0][0] > jointPos[11][0]+MAX_FOOT_SUPPORT_X && jointPos[12][1]-jointPos[11][1] < MIN_FOOT_HEIGHT_DIFF){
							supportMode = 0;
							supportOrder = 3;
						}
					}else{																	// 右脚支撑
						if(jointPos[0][0] < jointPos[12][0]-MAX_FOOT_SUPPORT_X && jointPos[11][1]-jointPos[12][1] < MIN_FOOT_HEIGHT_DIFF){
							supportMode = 0;
							supportOrder = 4;
						}
					}//if(supportMode == 0)
					sprintf(buffer,"\n");
					myLog->positionRecord(buffer);
					double rx = -((TORSO[2]-SPINE[2])/fabs(TORSO[2]-SPINE[2]))*acos(fabs(TORSO[1]-SPINE[1])/(1.0*sqrt(pow(TORSO[1]-SPINE[1],2)+pow(TORSO[2]-SPINE[2],2))));
					double rz = ((TORSO[0]-SPINE[0])/fabs(TORSO[0]-SPINE[0]))*acos(fabs(TORSO[1]-SPINE[1])/(1.0*sqrt(pow(TORSO[0]-SPINE[0],2)+pow(TORSO[1]-SPINE[1],2))));
					TORSO_R = mathFunction.rpy2rot(rx,0,rz);
					for(int i=0;i<JOINT_RECORD_ACCOUNT;i++){
						sprintf(buffer,"Joint %d 's configuration: Position.x = %.10f,Position.y = %.10f,Position.z = %.10f\n",i,jointPos[i][0],jointPos[i][1],jointPos[i][2]);
						myLog->positionRecord(buffer);
						posInTorso(jointPos[i],TORSO,TORSO_R);								// 转换成NAO Torso坐标系下的坐标
						rotate(jointPos[i]);
					}
					for(int i=0;i<3;i++){
						free(TORSO_R[i]);
					}
					free(TORSO_R);
					sprintf(buffer,"\n");
					myLog->positionRecord(buffer);
					cout<<"\n ----------------- record skeleton data -----------------"<<endl;
					cout<<"第"<<current_frame - min_frame<<"帧骨骼数据"<<endl;
					sprintf(buffer,"supportMode = %d, supportOrder = %d\n",supportMode,supportOrder);
					myLog->positionRecord(buffer);
					cout<<"record posture picture"<<endl;
					sprintf(image_name, ".\\recordPictures\\%s_%d%s", "Posture", current_frame-min_frame, ".png");//保存的图片名
					cvSaveImage(image_name,&pImg);											// 保存图像
					clock_t time2 = clock();												// 一次采集的结束时间
					sprintf(buffer,"currentTorsoHeight = %0.5lfm\n",currentTorsoHeight);
					myLog->positionRecord(buffer);
					sprintf(buffer,"采集时间 = %d (也即这一帧动作的理想执行时间)\n",time2-time1);
					myLog->positionRecord(buffer);
					myLog->positionFlush();
					int sig = 0;
					//判断上身关节有无动作
					int upBody[] = {1,2,3,4,5,6};
					for(int i=0;i<6;i++){
						double upLink = sqrt(pow(fabs(jointPos[upBody[i]][0] - lastJoint[upBody[i]][0]),2)+pow(fabs(jointPos[upBody[i]][1] - lastJoint[upBody[i]][1]),2)+pow(fabs(jointPos[upBody[i]][2] - lastJoint[upBody[i]][2]),2));
						if(upLink > MIN_UP_MOTION){
							sig = 1;
							break;
						}
					}
					//判断下身关节有无动作
					int lowBody[] = {7,8,9,10,11,12};
					for(int i=0;i<6;i++){
						double lowerLink = sqrt(pow(fabs(jointPos[lowBody[i]][0] - lastJoint[lowBody[i]][0]),2)+pow(fabs(jointPos[lowBody[i]][1] - lastJoint[lowBody[i]][1]),2)+pow(fabs(jointPos[lowBody[i]][2] - lastJoint[lowBody[i]][2]),2));
						if(lowerLink > MIN_LOW_MOTION){
							sig = 1;
							break;
						}
					}
					if(supportMode == 0){
						if(fabs(currentTorsoHeight-lastTorsoHeight)/torsoHeight >= MIN_DOUBLE_SUPPORT_TORSO_HEIGHT_RATIO){
							doubleSupportConfig = 1;
							lastTorsoHeight = currentTorsoHeight;
						}else{
							doubleSupportConfig = 0;
						}
					}else{
						doubleSupportConfig = 0;
					}
					//动作幅度大于阈值或者下肢支撑状态发生变化
					if(sig || supportOrder || doubleSupportConfig){
						writeFrame->no = current_frame-min_frame;
						for(int i=0;i<JOINT_RECORD_ACCOUNT;i++){
							writeFrame->location[i][0] = jointPos[i][0];
							writeFrame->location[i][1] = jointPos[i][1];
							writeFrame->location[i][2] = jointPos[i][2];
						}
						writeFrame->supportMode = supportMode;
						writeFrame->supportOrder = supportOrder;
						writeFrame->doubleSupportConfig = doubleSupportConfig;
						writeFrame->time = time2 - time1;
						++writeFrame;
						//如果动作幅度大于阈值最近一帧变成当前帧
						for(int i=0;i<JOINT_RECORD_ACCOUNT;i++){
							lastJoint[i][0] = jointPos[i][0];
							lastJoint[i][1] = jointPos[i][1];
							lastJoint[i][2] = jointPos[i][2];
						}
					}
					if(current_frame - min_frame >= MAX_FRAME){
						cout<<"采集帧数达到最大帧数，等待处理线程结束... ... ..."<<endl;
						goto end;
					}
					if(supportMode){														// 为了保证动作延时，single support采集速度要放慢
						Sleep(SINGLE_SUPPORT_DELAY);
					}
				}//if(current_frame>min_frame)
			}else{
				cout<<"SkeletonTracking............................"<<endl;
			}//if( skeletonFrame.SkeletonData[UserID].eTrackingState == NUI_SKELETON_TRACKED...)
			cvShowImage("Image", &pImg);													// 显示图像   
		}else{   
            cout<<"Buffer length of received texture is bogus\r\n"<<endl;   
        }//if(WaitForSingleObject(skeletonEvent, INFINITE)==0)
		if( cv::waitKey( 1) == 'q' ){
			goto end;
		}
	}//while(true)
end:
	while(calculating);
	cout<<"处理线程结束，采集系统关闭."<<endl;
	CloseHandle(hThread_1);																	// 关闭数据处理子线程
	cout<<"数据处理系统关闭."<<endl;
	myLog->positionClose();																	// 关闭关节记录文件
	cout<<"记录文件关闭."<<endl;
	cv::destroyWindow("IMG");																// 先销毁User跟踪器
	closesocket(clientSocket);																// 关闭Socket
	cout<<"Socket关闭."<<endl;
	return 0;
}
