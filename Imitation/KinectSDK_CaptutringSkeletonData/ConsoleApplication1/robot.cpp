#include "robot.h"

MathFunction mathFunction;													// 引用数学方法类，机器人的方法中会调用数学方法

/*
	关节的初始化模块，确定各个关节之间的关系【sister、child、NONE】，初始化关节的关节轴矢量a和相对位置b，同时给每个关节分配ID和name
    可以先暂时令所有关节角度初始为0，在测试前可以配置个别关节的角度
    所有关节的关系：(左边是child，右边是sister，没有用0表示)
                                                BODY(1)
                                             /           \
                                      RLEG_J0(2)          0
                                    [RHipYawPitch]
                                 /                  \
                            RLEG_J1(3)            LLEG_J0(8)      
                            [RHipRoll]          [LHipYawPitch]
                            /        \          /            \
                       RLEG_J2(4)     0   LLEG_J1(9)         RARM_J0(14)
                      [RHipPitch]         [LHipRoll]        [RShouldPitch]
                       /        \        /         \       /              \
                  RLEG_J3(5)     0   LLEG_J2(10)    0  RARM_J1(15)        LARM_J0(19) 
                 [RKneePitch]        [LHipPitch]      [RShouldRoll]      [LShouldPitch]
                  /        \         /         \        /        \      /                \
              RLEG_J4(6)    0    LLEG_J3(11)    0   RARM_J2(16)   0  LARM_J1(20)      HEAD_J0(24)
            [RAnklePitch]        [LKneePitch]       [RElbowYaw]     [LShouldRoll]      [HeadYaw]
             /        \          /        \         /        \       /        \        /       \
         RLEG_J5(7)    0     LLEG_J4(12)   0    RARM_J3(17)   0  LARM_J2(21)   0   HEAD_J1(25)  0
        [RAnkleRoll]        [LAnklePitch]      [RElbowRoll]      [LElbowYaw]       [HeadPitch]
         /        \          /        \         /        \        /        \       /        \
        0          0     LLEG_J5(13)   0    RARM_J4(18)   0   LARM_J3(22)   0     0          0
      	                [LAnkleRoll]        [RWristYaw]      [LElbowRoll]
                         /       \          /        \        /        \
                        0         0        0          0   LARM_J4(23)   0
                                                          [LWristYaw]
                                                          /        \
                                                         0          0
*/
void Robot::initialization(){
	//BODY
	strcpy(uLINK[BODY].name,"BODY");
	uLINK[BODY].child = RLEG_J0;
	uLINK[BODY].sister = NONE;
	uLINK[BODY].mother = NONE;
	uLINK[BODY].b[0] = uLINK[BODY].b[1] = uLINK[BODY].b[2] = 0;
	uLINK[BODY].p[0] = uLINK[BODY].p[1] = uLINK[BODY].p[2] = 0;
	uLINK[BODY].a[0] = uLINK[BODY].a[1] = 0;
	uLINK[BODY].a[2] = 1;
	uLINK[BODY].q = 0*ToRad;
	uLINK[BODY].maxq = 0*ToRad;
	uLINK[BODY].minq = 0*ToRad;
	uLINK[BODY].m = 1.0496;
	uLINK[BODY].c[0] = -0.00413;
	uLINK[BODY].c[1] = 0;
	uLINK[BODY].c[2] = 0.04342;
	//RLEG
	strcpy(uLINK[RLEG_J0].name ,"RLEG_J0[RHipYawPitch]");           
	uLINK[RLEG_J0].child = RLEG_J1;
	uLINK[RLEG_J0].sister = LLEG_J0;
	uLINK[RLEG_J0].mother = BODY;
	uLINK[RLEG_J0].b[0] = 0;
	uLINK[RLEG_J0].b[1] = -BODYtoLEGY;
	uLINK[RLEG_J0].b[2] = -BODYtoLEGZ;        
	uLINK[RLEG_J0].p[0] = uLINK[RLEG_J0].p[1] = uLINK[RLEG_J0].p[2] = 0;                           
	uLINK[RLEG_J0].a[0] = 0;
	uLINK[RLEG_J0].a[1] = pow(2,0.5)/2;
	uLINK[RLEG_J0].a[2] = pow(2,0.5)/2;                          
	uLINK[RLEG_J0].q = 0*ToRad;
	uLINK[RLEG_J0].maxq = 42.44*ToRad;
	uLINK[RLEG_J0].minq = -65.62*ToRad;
	uLINK[RLEG_J0].m = 0.06981;
	uLINK[RLEG_J0].c[0] = -0.00781;
	uLINK[RLEG_J0].c[1] = 0.01114;
	uLINK[RLEG_J0].c[2] = 0.02661;
	strcpy(uLINK[RLEG_J1].name,"RLEG_J1[RHipRoll]");
	uLINK[RLEG_J1].child = RLEG_J2;
	uLINK[RLEG_J1].sister = NONE;
	uLINK[RLEG_J1].mother = RLEG_J0;
	uLINK[RLEG_J1].b[0] = uLINK[RLEG_J1].b[1] = uLINK[RLEG_J1].b[2] = 0;    // BODY和RLEG的相对位置[杆件在母连杆坐标系相对于母连杆的位置]
	uLINK[RLEG_J1].p[0] = uLINK[RLEG_J1].p[1] = uLINK[RLEG_J1].p[2] = 0;    // 杆件在世界坐标系中的位置
	uLINK[RLEG_J1].a[0] = 1;
	uLINK[RLEG_J1].a[1] = 0;
	uLINK[RLEG_J1].a[2] = 0;												// 连杆关节轴矢量，在母连杆坐标系中
	uLINK[RLEG_J1].q = 0*ToRad;												// 关节角
	uLINK[RLEG_J1].maxq = 21.74*ToRad;										// 最大角度
	uLINK[RLEG_J1].minq = -45.29*ToRad;										// 最小角度
	uLINK[RLEG_J1].m = 0.14053;												// 连杆质量
	uLINK[RLEG_J1].c[0] = -0.01549;											// 连杆质心
	uLINK[RLEG_J1].c[1] = -0.00029;
	uLINK[RLEG_J1].c[2] = -0.00515;
	strcpy(uLINK[RLEG_J2].name,"RLEG_J2[RHipPitch]");
	uLINK[RLEG_J2].child = RLEG_J3;
	uLINK[RLEG_J2].sister = NONE;
	uLINK[RLEG_J2].mother = RLEG_J1;
	uLINK[RLEG_J2].b[0] = uLINK[RLEG_J2].b[1] = uLINK[RLEG_J2].b[2] = 0;
	uLINK[RLEG_J2].p[0] = uLINK[RLEG_J2].p[1] = uLINK[RLEG_J2].p[2] = 0;
	uLINK[RLEG_J2].a[0] = 0;
	uLINK[RLEG_J2].a[1] = 1;
	uLINK[RLEG_J2].a[2] = 0;
	uLINK[RLEG_J2].q = 0*ToRad;
	uLINK[RLEG_J2].maxq = 27.73*ToRad;
	uLINK[RLEG_J2].minq = -88.00*ToRad;
	uLINK[RLEG_J2].m = 0.38968;
	uLINK[RLEG_J2].c[0] = 0.00138;
	uLINK[RLEG_J2].c[1] = -0.00221;
	uLINK[RLEG_J2].c[2] = -0.05373;
	strcpy(uLINK[RLEG_J3].name,"RLEG_J3[RKneePitch]");
	uLINK[RLEG_J3].child = RLEG_J4;
	uLINK[RLEG_J3].sister = NONE;
	uLINK[RLEG_J3].mother = RLEG_J2;
	uLINK[RLEG_J3].b[0] = uLINK[RLEG_J3].b[1] = 0;
	uLINK[RLEG_J3].b[2] = -LEGtoKNEE;
	uLINK[RLEG_J3].p[0] = uLINK[RLEG_J3].p[1] = uLINK[RLEG_J3].p[2] = 0;
	uLINK[RLEG_J3].a[0] = 0;
	uLINK[RLEG_J3].a[1] = 1;
	uLINK[RLEG_J3].a[2] = 0;
	uLINK[RLEG_J3].q = 0*ToRad;
	uLINK[RLEG_J3].maxq = 121.47*ToRad;
	uLINK[RLEG_J3].minq = -5.90*ToRad;
	uLINK[RLEG_J3].m = 0.30142;
	uLINK[RLEG_J3].c[0] = 0.00453;
	uLINK[RLEG_J3].c[1] = -0.00225;
	uLINK[RLEG_J3].c[2] = -0.04936;
	strcpy(uLINK[RLEG_J4].name ,"RLEG_J4[RAnklePitch]");
	uLINK[RLEG_J4].child = RLEG_J5;
	uLINK[RLEG_J4].sister = NONE;
	uLINK[RLEG_J4].mother = RLEG_J3;
	uLINK[RLEG_J4].b[0] = 0;
	uLINK[RLEG_J4].b[1] = 0;
	uLINK[RLEG_J4].b[2] = -KNEEtoFOOT;
	uLINK[RLEG_J4].p[0] = uLINK[RLEG_J4].p[1] = uLINK[RLEG_J4].p[2] = 0;
	uLINK[RLEG_J4].a[0] = 0;
	uLINK[RLEG_J4].a[1] = 1;
	uLINK[RLEG_J4].a[2] = 0;
	uLINK[RLEG_J4].q = 0*ToRad;
	uLINK[RLEG_J4].maxq = 53.40*ToRad;
	uLINK[RLEG_J4].minq = -67.97*ToRad;
	uLINK[RLEG_J4].m = 0.13416;
	uLINK[RLEG_J4].c[0] = 0.00045;
	uLINK[RLEG_J4].c[1] = -0.00029;
	uLINK[RLEG_J4].c[2] = 0.00685;
	strcpy(uLINK[RLEG_J5].name,"RLEG_J5[RAnkleRoll]");
	uLINK[RLEG_J5].child = NONE;
	uLINK[RLEG_J5].sister = NONE;
	uLINK[RLEG_J5].mother = RLEG_J4;
	uLINK[RLEG_J5].b[0] = uLINK[RLEG_J5].b[1] = uLINK[RLEG_J5].b[2] = 0;
	uLINK[RLEG_J5].p[0] = uLINK[RLEG_J5].p[1] = uLINK[RLEG_J5].p[2] = 0;
	uLINK[RLEG_J5].a[0] = 1;
	uLINK[RLEG_J5].a[1] = 0;
	uLINK[RLEG_J5].a[2] = 0;
	uLINK[RLEG_J5].q = 0*ToRad;
	uLINK[RLEG_J5].maxq = 22.80*ToRad;
	uLINK[RLEG_J5].minq = -44.06*ToRad;
	uLINK[RLEG_J5].m = 0.17184;
	uLINK[RLEG_J5].c[0] = 0.02542;
	uLINK[RLEG_J5].c[1] = -0.0033;
	uLINK[RLEG_J5].c[2] = -0.03239;
	//LLEG
	strcpy(uLINK[LLEG_J0].name,"LLEG_J0[LHipYawPitch]");           
	uLINK[LLEG_J0].child = LLEG_J1;
	uLINK[LLEG_J0].sister = RARM_J0;
	uLINK[LLEG_J0].mother = BODY;
	uLINK[LLEG_J0].b[0] = 0;
	uLINK[LLEG_J0].b[1] = BODYtoLEGY;
	uLINK[LLEG_J0].b[2] = -BODYtoLEGZ;        
	uLINK[LLEG_J0].p[0] = uLINK[LLEG_J0].p[1] = uLINK[LLEG_J0].p[2] = 0;                           
	uLINK[LLEG_J0].a[0] = 0;
	uLINK[LLEG_J0].a[1] = pow(2,0.5)/2;
	uLINK[LLEG_J0].a[2] = -pow(2,0.5)/2;                          
	uLINK[LLEG_J0].q = 0*ToRad;
	uLINK[LLEG_J0].maxq = 42.44*ToRad;
	uLINK[LLEG_J0].minq = -65.62*ToRad;
	uLINK[LLEG_J0].m = 0.06981;
	uLINK[LLEG_J0].c[0] = -0.00781;
	uLINK[LLEG_J0].c[1] = -0.01114;
	uLINK[LLEG_J0].c[2] = 0.02661;
	strcpy(uLINK[LLEG_J1].name ,"LLEG_J1[LHipRoll]");
	uLINK[LLEG_J1].child = LLEG_J2;
	uLINK[LLEG_J1].sister = NONE;
	uLINK[LLEG_J1].mother = LLEG_J0;
	uLINK[LLEG_J1].b[0] = 0;
	uLINK[LLEG_J1].b[1] = 0;
	uLINK[LLEG_J1].b[2] = 0;
	uLINK[LLEG_J1].p[0] = uLINK[LLEG_J1].p[1] = uLINK[LLEG_J1].p[2] = 0;
	uLINK[LLEG_J1].a[0] = 1;
	uLINK[LLEG_J1].a[1] = 0;
	uLINK[LLEG_J1].a[2] = 0;
	uLINK[LLEG_J1].q = 0*ToRad;
	uLINK[LLEG_J1].maxq = 45.29*ToRad;
	uLINK[LLEG_J1].minq = -21.74*ToRad;
	uLINK[LLEG_J1].m = 0.14053;
	uLINK[LLEG_J1].c[0] = -0.01549;
	uLINK[LLEG_J1].c[1] = 0.00029;
	uLINK[LLEG_J1].c[2] = -0.00515;
	strcpy(uLINK[LLEG_J2].name,"LLEG_J2[LHipPitch]");
	uLINK[LLEG_J2].child = LLEG_J3;
	uLINK[LLEG_J2].sister = NONE;
	uLINK[LLEG_J2].mother = LLEG_J1;
	uLINK[LLEG_J2].b[0] = uLINK[LLEG_J2].b[1] = uLINK[LLEG_J2].b[2] = 0;
	uLINK[LLEG_J2].p[0] = uLINK[LLEG_J2].p[1] = uLINK[LLEG_J2].p[2] = 0;
	uLINK[LLEG_J2].a[0] = 0;
	uLINK[LLEG_J2].a[1] = 1;
	uLINK[LLEG_J2].a[2] = 0;
	uLINK[LLEG_J2].q = 0*ToRad;
	uLINK[LLEG_J2].maxq = 27.73*ToRad;
	uLINK[LLEG_J2].minq = -88.00*ToRad;
	uLINK[LLEG_J2].m = 0.38968;
	uLINK[LLEG_J2].c[0] = 0.00138;
	uLINK[LLEG_J2].c[1] = 0.00221;
	uLINK[LLEG_J2].c[2] = -0.05373;
	strcpy(uLINK[LLEG_J3].name,"LLEG_J3[LKneePitch]");
	uLINK[LLEG_J3].child = LLEG_J4;
	uLINK[LLEG_J3].sister = NONE;
	uLINK[LLEG_J3].mother = LLEG_J2;
	uLINK[LLEG_J3].b[0] = uLINK[LLEG_J3].b[1] = 0;
	uLINK[LLEG_J3].b[2] = -LEGtoKNEE;
	uLINK[LLEG_J3].p[0] = uLINK[LLEG_J3].p[1] = uLINK[LLEG_J3].p[2] = 0;
	uLINK[LLEG_J3].a[0] = 0;
	uLINK[LLEG_J3].a[1] = 1;
	uLINK[LLEG_J3].a[2] = 0;
	uLINK[LLEG_J3].q = 0*ToRad;
	uLINK[LLEG_J3].maxq = 121.04*ToRad;
	uLINK[LLEG_J3].minq = -5.29*ToRad;
	uLINK[LLEG_J3].m = 0.30142;
	uLINK[LLEG_J3].c[0] = 0.00453;
	uLINK[LLEG_J3].c[1] = 0.00225;
	uLINK[LLEG_J3].c[2] = -0.04936;
	strcpy(uLINK[LLEG_J4].name,"LLEG_J4[LAnklePitch]");
	uLINK[LLEG_J4].child = LLEG_J5;
	uLINK[LLEG_J4].sister = NONE;
	uLINK[LLEG_J4].mother = LLEG_J3;
	uLINK[LLEG_J4].b[0] = uLINK[LLEG_J4].b[1] = 0;
	uLINK[LLEG_J4].b[2] = -KNEEtoFOOT;
	uLINK[LLEG_J4].p[0] = uLINK[LLEG_J4].p[1] = uLINK[LLEG_J4].p[2] = 0;
	uLINK[LLEG_J4].a[0] = 0;
	uLINK[LLEG_J4].a[1] = 1;
	uLINK[LLEG_J4].a[2] = 0;
	uLINK[LLEG_J4].q = 0*ToRad;
	uLINK[LLEG_J4].maxq = 52.86*ToRad;
	uLINK[LLEG_J4].minq = -68.15*ToRad;
	uLINK[LLEG_J4].m = 0.13416;
	uLINK[LLEG_J4].c[0] = 0.00045;
	uLINK[LLEG_J4].c[1] = 0.00029;
	uLINK[LLEG_J4].c[2] = 0.00685;
	strcpy(uLINK[LLEG_J5].name,"LLEG_J5[LAnkleRoll]");
	uLINK[LLEG_J5].child = NONE;
	uLINK[LLEG_J5].sister = NONE;
	uLINK[LLEG_J5].mother = LLEG_J4;
	uLINK[LLEG_J5].b[0] = uLINK[LLEG_J5].b[1] = uLINK[LLEG_J5].b[2] = 0;
	uLINK[LLEG_J5].p[0] = uLINK[LLEG_J5].p[1] = uLINK[LLEG_J5].p[2] = 0;
	uLINK[LLEG_J5].a[0] = 1;
	uLINK[LLEG_J5].a[1] = 0;
	uLINK[LLEG_J5].a[2] = 0;
	uLINK[LLEG_J5].q = 0*ToRad;
	uLINK[LLEG_J5].maxq = 44.06*ToRad;
	uLINK[LLEG_J5].minq = -22.79*ToRad;
	uLINK[LLEG_J5].m = 0.17184;
	uLINK[LLEG_J5].c[0] = 0.02542;
	uLINK[LLEG_J5].c[1] = 0.0033;
	uLINK[LLEG_J5].c[2] = -0.03239;
	//RARM
	strcpy(uLINK[RARM_J0].name,"RARM_J0[RShoulderPitch]");
	uLINK[RARM_J0].child = RARM_J1;
	uLINK[RARM_J0].sister = LARM_J0;
	uLINK[RARM_J0].mother = BODY;
	uLINK[RARM_J0].b[0] = 0;
	uLINK[RARM_J0].b[1] =  -BODYtoSHOULDERY;
	uLINK[RARM_J0].b[2] = BODYtoSHOULDERZ;
	uLINK[RARM_J0].p[0] = uLINK[RARM_J0].p[1] = uLINK[RARM_J0].p[2] = 0;
	uLINK[RARM_J0].a[0] = 0;
	uLINK[RARM_J0].a[1] = 1;
	uLINK[RARM_J0].a[2] = 0;
	uLINK[RARM_J0].q = 0*ToRad;
	uLINK[RARM_J0].maxq = 119.5*ToRad;
	uLINK[RARM_J0].minq = -119.5*ToRad;
	uLINK[RARM_J0].m = 0.09304;
	uLINK[RARM_J0].c[0] = -0.00165;
	uLINK[RARM_J0].c[1] = 0.02663;
	uLINK[RARM_J0].c[2] = 0.00014;
	strcpy(uLINK[RARM_J1].name,"RARM_J1[RShoulderRoll]");
	uLINK[RARM_J1].child = RARM_J2;
	uLINK[RARM_J1].sister = NONE;
	uLINK[RARM_J1].mother = RARM_J0;
	uLINK[RARM_J1].b[0] = uLINK[RARM_J1].b[1] = uLINK[RARM_J1].b[2] = 0;
	uLINK[RARM_J1].p[0] = uLINK[RARM_J1].p[1] = uLINK[RARM_J1].p[2] = 0;
	uLINK[RARM_J1].a[0] = uLINK[RARM_J1].a[1] = 0;
	uLINK[RARM_J1].a[2] = 1; 
	uLINK[RARM_J1].q = 0*ToRad;
	uLINK[RARM_J1].maxq = 18*ToRad;
	uLINK[RARM_J1].minq = -76*ToRad;
	uLINK[RARM_J1].m = 0.15777;
	uLINK[RARM_J1].c[0] = 0.02455;
	uLINK[RARM_J1].c[1] = -0.00563;
	uLINK[RARM_J1].c[2] = 0.0033;
	strcpy(uLINK[RARM_J2].name,"RARM_J2[RElbowYaw]");
	uLINK[RARM_J2].child = RARM_J3;
	uLINK[RARM_J2].sister = NONE;
	uLINK[RARM_J2].mother = RARM_J1;
	uLINK[RARM_J2].b[0] = SHOULDERtoELBOWX;
	uLINK[RARM_J2].b[1] = -SHOULDERtoELBOWY;
	uLINK[RARM_J2].b[2] = 0;
	uLINK[RARM_J2].p[0] = uLINK[RARM_J2].p[1] = uLINK[RARM_J2].p[2] = 0;
	uLINK[RARM_J2].a[0] = 1;
	uLINK[RARM_J2].a[1] = 0;
	uLINK[RARM_J2].a[2] = 0;
	uLINK[RARM_J2].q = 0*ToRad;
	uLINK[RARM_J2].maxq = 119.5*ToRad;
	uLINK[RARM_J2].minq = -119.5*ToRad;
	uLINK[RARM_J2].m = 0.06483;
	uLINK[RARM_J2].c[0] = -0.02744;
	uLINK[RARM_J2].c[1] = 0;
	uLINK[RARM_J2].c[2] = -0.00014;
	strcpy(uLINK[RARM_J3].name,"RARM_J3[RElbowRoll]");
	uLINK[RARM_J3].child = RARM_J4;
	uLINK[RARM_J3].sister = NONE;
	uLINK[RARM_J3].mother = RARM_J2;
	uLINK[RARM_J3].b[0] = uLINK[RARM_J3].b[1] = uLINK[RARM_J3].b[2] = 0;
	uLINK[RARM_J3].p[0] = uLINK[RARM_J3].p[1] = uLINK[RARM_J3].p[2] = 0;
	uLINK[RARM_J3].a[0] = uLINK[RARM_J3].a[1] = 0;
	uLINK[RARM_J3].a[2] = 1;
	uLINK[RARM_J3].q = 0*ToRad;
	uLINK[RARM_J3].maxq = 88.5*ToRad;
	uLINK[RARM_J3].minq = 2*ToRad;
	uLINK[RARM_J3].m = 0.07761;
	uLINK[RARM_J3].c[0] = 0.02556;
	uLINK[RARM_J3].c[1] = -0.00281;
	uLINK[RARM_J3].c[2] = 0.00076;
	strcpy(uLINK[RARM_J4].name,"RARM_J4[RWristYaw]");
	uLINK[RARM_J4].child = NONE;
	uLINK[RARM_J4].sister = NONE;
	uLINK[RARM_J4].mother = RARM_J3;
	uLINK[RARM_J4].b[0] = ELBOWtoWRIST;
	uLINK[RARM_J4].b[1] = 0;
	uLINK[RARM_J4].b[2] = 0;
	uLINK[RARM_J4].p[0] = uLINK[RARM_J4].p[1] = uLINK[RARM_J4].p[2] = 0;
	uLINK[RARM_J4].a[0] = 1;
	uLINK[RARM_J4].a[1] = 0;
	uLINK[RARM_J4].a[2] = 0;
	uLINK[RARM_J4].q = 0*ToRad;
	uLINK[RARM_J4].maxq = 104.5*ToRad;
	uLINK[RARM_J4].minq = -104.5*ToRad;
	uLINK[RARM_J4].m = 0.18533;
	uLINK[RARM_J4].c[0] = 0.03434;
	uLINK[RARM_J4].c[1] = 0.00088;
	uLINK[RARM_J4].c[2] = 0.00308;
	//LARM
	strcpy(uLINK[LARM_J0].name,"LARM_J0[LShoulderPitch]");
	uLINK[LARM_J0].child = LARM_J1;
	uLINK[LARM_J0].sister = HEAD_J0;
	uLINK[LARM_J0].mother = BODY;
	uLINK[LARM_J0].b[0] = 0;
	uLINK[LARM_J0].b[1] = BODYtoSHOULDERY;
	uLINK[LARM_J0].b[2] = BODYtoSHOULDERZ;
	uLINK[LARM_J0].p[0] = uLINK[LARM_J0].p[1] = uLINK[LARM_J0].p[2] = 0;
	uLINK[LARM_J0].a[0] = 0;
	uLINK[LARM_J0].a[1] = 1;
	uLINK[LARM_J0].a[2] = 0;
	uLINK[LARM_J0].q = 0*ToRad;
	uLINK[LARM_J0].maxq = 119.5*ToRad;
	uLINK[LARM_J0].minq = -119.5*ToRad;
	uLINK[LARM_J0].m = 0.09304;
	uLINK[LARM_J0].c[0] = -0.00165;
	uLINK[LARM_J0].c[1] = -0.02663;
	uLINK[LARM_J0].c[2] = 0.00014;
	strcpy(uLINK[LARM_J1].name,"LARM_J1[LShoulderRoll]");
	uLINK[LARM_J1].child = LARM_J2;
	uLINK[LARM_J1].sister = NONE;
	uLINK[LARM_J1].mother = LARM_J0;
	uLINK[LARM_J1].b[0] = uLINK[LARM_J1].b[1] = uLINK[LARM_J1].b[2] = 0;
	uLINK[LARM_J1].p[0] = uLINK[LARM_J1].p[1] = uLINK[LARM_J1].p[2] = 0;
	uLINK[LARM_J1].a[0] = uLINK[LARM_J1].a[1] =0;
	uLINK[LARM_J1].a[2] = 1;
	uLINK[LARM_J1].q = 0*ToRad;
	uLINK[LARM_J1].maxq = 76*ToRad;
	uLINK[LARM_J1].minq = -18*ToRad;
	uLINK[LARM_J1].m = 0.15777;
	uLINK[LARM_J1].c[0] = 0.02455;
	uLINK[LARM_J1].c[1] = 0.00563;
	uLINK[LARM_J1].c[2] = 0.0033;
	strcpy(uLINK[LARM_J2].name,"LARM_J2[LElbowYaw]");
	uLINK[LARM_J2].child = LARM_J3;
	uLINK[LARM_J2].sister = NONE;
	uLINK[LARM_J2].mother = LARM_J1;
	uLINK[LARM_J2].b[0] = SHOULDERtoELBOWX;
	uLINK[LARM_J2].b[1] = SHOULDERtoELBOWY;
	uLINK[LARM_J2].b[2] = 0;
	uLINK[LARM_J2].p[0] = uLINK[LARM_J2].p[1] = uLINK[LARM_J2].p[2] = 0;
	uLINK[LARM_J2].a[0] = 1;
	uLINK[LARM_J2].a[1] = 0;
	uLINK[LARM_J2].a[2] = 0;
	uLINK[LARM_J2].q = 0*ToRad;
	uLINK[LARM_J2].maxq = 119.5*ToRad;
	uLINK[LARM_J2].minq = -119.5*ToRad;
	uLINK[LARM_J2].m = 0.06483;
	uLINK[LARM_J2].c[0] = -0.02744;
	uLINK[LARM_J2].c[1] = 0;
	uLINK[LARM_J2].c[2] = -0.00014;
	strcpy(uLINK[LARM_J3].name,"LARM_J3[LElbowRoll]");
	uLINK[LARM_J3].child = LARM_J4;
	uLINK[LARM_J3].sister = NONE;
	uLINK[LARM_J3].mother = LARM_J2;
	uLINK[LARM_J3].b[0] = uLINK[LARM_J3].b[1] = uLINK[LARM_J3].b[2] = 0;
	uLINK[LARM_J3].p[0] = uLINK[LARM_J3].p[1] = uLINK[LARM_J3].p[2] = 0;
	uLINK[LARM_J3].a[0] = 0;
	uLINK[LARM_J3].a[1] = 0;
	uLINK[LARM_J3].a[2] = 1;
	uLINK[LARM_J3].q = 0*ToRad;
	uLINK[LARM_J3].maxq = -2*ToRad;
	uLINK[LARM_J3].minq =  -88.5*ToRad;
	uLINK[LARM_J3].m = 0.07761;
	uLINK[LARM_J3].c[0] = 0.02556;
	uLINK[LARM_J3].c[1] = 0.00281;
	uLINK[LARM_J3].c[2] = 0.00076;
	strcpy(uLINK[LARM_J4].name,"LARM_J4[LWristYaw]");
	uLINK[LARM_J4].child = NONE;
	uLINK[LARM_J4].sister = NONE;
	uLINK[LARM_J4].mother = LARM_J3;
	uLINK[LARM_J4].b[0] = ELBOWtoWRIST;
	uLINK[LARM_J4].b[1] = 0;
	uLINK[LARM_J4].b[2] = 0;
	uLINK[LARM_J4].p[0] = uLINK[LARM_J4].p[1] = uLINK[LARM_J4].p[2] = 0;
	uLINK[LARM_J4].a[0] = 1;
	uLINK[LARM_J4].a[1] = 0;
	uLINK[LARM_J4].a[2] = 0;
	uLINK[LARM_J4].q = 0*ToRad;
	uLINK[LARM_J4].maxq = 104.5*ToRad;
	uLINK[LARM_J4].minq =  -104.5*ToRad;
	uLINK[LARM_J4].m = 0.18533;
	uLINK[LARM_J4].c[0] = 0.03434;
	uLINK[LARM_J4].c[1] = -0.00088;
	uLINK[LARM_J4].c[2] = 0.00308;
	//HEAD
	strcpy(uLINK[HEAD_J0].name,"HEAD_J0[HeadYaw]");
	uLINK[HEAD_J0].child = HEAD_J1;
	uLINK[HEAD_J0].sister = NONE;
	uLINK[HEAD_J0].mother = BODY;
	uLINK[HEAD_J0].b[0] = uLINK[HEAD_J0].b[1] = 0;
	uLINK[HEAD_J0].b[2] = BODYtoHEAD;
	uLINK[HEAD_J0].p[0] = uLINK[HEAD_J0].p[1] = uLINK[HEAD_J0].p[2] = 0;
	uLINK[HEAD_J0].a[0] = 0;
	uLINK[HEAD_J0].a[1] = 0;
	uLINK[HEAD_J0].a[2] = 1;
	uLINK[HEAD_J0].q = 0*ToRad;
	uLINK[HEAD_J0].maxq = 119.5*ToRad;
	uLINK[HEAD_J0].minq =  -119.5*ToRad;
	uLINK[HEAD_J0].m = 0.07842;
	uLINK[HEAD_J0].c[0] = -0.00001;
	uLINK[HEAD_J0].c[1] = 0;
	uLINK[HEAD_J0].c[2] = -0.02742;
	strcpy(uLINK[HEAD_J1].name,"HEAD_J1[HeadPitch]");
	uLINK[HEAD_J1].child = NONE;
	uLINK[HEAD_J1].sister = NONE;
	uLINK[HEAD_J1].mother = HEAD_J0;
	uLINK[HEAD_J1].b[0] = uLINK[HEAD_J1].b[1] = uLINK[HEAD_J1].b[2] = 0;
	uLINK[HEAD_J1].p[0] = uLINK[HEAD_J1].p[1] = uLINK[HEAD_J1].p[2] = 0;
	uLINK[HEAD_J1].a[0] = 0;
	uLINK[HEAD_J1].a[1] = 1;
	uLINK[HEAD_J1].a[2] = 0;
	uLINK[HEAD_J1].q = 0*ToRad;
	uLINK[HEAD_J1].maxq = 29.5*ToRad;
	uLINK[HEAD_J1].minq =  -38.5*ToRad;
	uLINK[HEAD_J1].m = 0.60533;
	uLINK[HEAD_J1].c[0] = -0.00112;
	uLINK[HEAD_J1].c[1] = 0;
	uLINK[HEAD_J1].c[2] = 0.05258;
	double temp[3] = {1,1,1};
	//初始化所有关节的旋转矩阵
	for (int i = 1;i<=MAX_JOINT;i++){
		uLINK[i].R = (double**)malloc(sizeof(double*)*3);
		for(int j=0;j<3;j++){
			uLINK[i].R[j] = (double*)malloc(sizeof(double)*3);
		}
		uLINK[i].R = mathFunction.rodrigues(temp,0*ToRad);						//初始化所有连杆的姿态
	}
}

//递归打印各个关节的位姿
void Robot::print(int j){
	if (j == 0) 
		return;
	printf("============j = %d: %s============\n",j,uLINK[j].name);
	printf("位置:[%0.10lf,%0.10lf,%0.10lf]\n",uLINK[j].p[0],uLINK[j].p[1],uLINK[j].p[2]);
	printf("姿态:\n");
	for(int m=0;m<3;m++){
		printf("%0.10lf %0.10lf %0.10lf\n",uLINK[j].R[m][0],uLINK[j].R[m][1],uLINK[j].R[m][2]);
	}
	printf("------------关节%d打印结束---------\n\n",j);
	print(uLINK[j].child);
	print(uLINK[j].sister);
}

//找到连杆序号为to的母连杆所有关节序号(包括to)
int* Robot::findRoute(int to){
	int* idx;
	switch(to){
		case RARM_J2:{
			idx = (int*)malloc(sizeof(int)*3);
			idx[0] = RARM_J0;
			idx[1] = RARM_J1;
			idx[2] = RARM_J2;
			break;
		}
		case RARM_J4:{
			idx = (int*)malloc(sizeof(int)*3);
			idx[0] = RARM_J2;
			idx[1] = RARM_J3;
			idx[2] = RARM_J4;
			break;
		}
		case LARM_J2:{
			idx = (int*)malloc(sizeof(int)*3);
			idx[0] = LARM_J0;
			idx[1] = LARM_J1;
			idx[2] = LARM_J2;
			break;
		}
		case LARM_J4:{
			idx = (int*)malloc(sizeof(int)*3);
			idx[0] = LARM_J2;
			idx[1] = LARM_J3;
			idx[2] = LARM_J4;
			break;
		}
		case RLEG_J3:{
			idx = (int*)malloc(sizeof(int)*3);
			idx[0] = RLEG_J1;
			idx[1] = RLEG_J2;
			idx[2] = RLEG_J3;
			break;
		}
		case RLEG_J4:{
			idx = (int*)malloc(sizeof(int)*2);
			idx[0] = RLEG_J3;
			idx[1] = RLEG_J4;
			break;
		}
		case LLEG_J3:{
			idx = (int*)malloc(sizeof(int)*3);
			idx[0] = LLEG_J1;
			idx[1] = LLEG_J2;
			idx[2] = LLEG_J3;
			break;
		}
		case LLEG_J4:{
			idx = (int*)malloc(sizeof(int)*2);
			idx[0] = LLEG_J3;
			idx[1] = LLEG_J4;
			break;
		}
		default:{
			printf("Error Configuration\n");
			idx = NULL;
		}
	}
	return idx;
}

//参考姿态
void Robot::referencePose(){
	uLINK[BODY].p[0] = uLINK[BODY].p[1] = uLINK[BODY].p[2] = 0;
	uLINK[BODY].R = mathFunction.rpy2rot(0,0,0);
	uLINK[HEAD_J0].q = 0.0*ToRad;
	uLINK[HEAD_J1].q = -9.74028269513*ToRad;
	uLINK[RARM_J0].q = 82.7212822826*ToRad;
	uLINK[RARM_J1].q = -12.8386648474*ToRad;
	uLINK[RARM_J2].q = 68.8745557842*ToRad;
	uLINK[RARM_J3].q = 23.8800183348*ToRad;
	uLINK[RARM_J4].q = 5.7295772273*ToRad;
	uLINK[LARM_J0].q = 82.7212822826*ToRad;
	uLINK[LARM_J1].q = 12.8386648474*ToRad;
	uLINK[LARM_J2].q = -68.8745557842*ToRad;
	uLINK[LARM_J3].q = -23.8800183348*ToRad;
	uLINK[LARM_J4].q = 5.72957893484*ToRad;
	uLINK[RLEG_J0].q = -9.74028269513*ToRad;
	uLINK[RLEG_J1].q = -5.72957808107*ToRad;
	uLINK[RLEG_J2].q = 7.44845112119*ToRad;
	uLINK[RLEG_J3].q = -5.15662040103*ToRad;
	uLINK[RLEG_J4].q = 5.15662040103*ToRad;
	uLINK[RLEG_J5].q = 7.44845112119*ToRad;
	uLINK[LLEG_J0].q = -9.74028269513*ToRad;
	uLINK[LLEG_J1].q = 5.72957808107*ToRad;
	uLINK[LLEG_J2].q = 7.44845112119*ToRad;
	uLINK[LLEG_J3].q = -5.15662040103*ToRad;
	uLINK[LLEG_J4].q = 5.15662040103*ToRad;
	uLINK[LLEG_J5].q = -7.44845112119*ToRad;
}

//初始化NAO开机wakeUp后的状态
void Robot::wakeUp(){
	initialization();
	referencePose();
}
