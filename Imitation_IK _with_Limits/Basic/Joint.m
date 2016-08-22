%{
    关节的初始化模块，确定各个关节之间的关系【sister、child、NONE】，初始化关节的关节轴矢量a和相对位置b，同时给每个关节分配ID和name。
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
%}
%body
    BODY = 1; NONE = 0;
%RLEG
    RLEG_J0 = 2; RLEG_J1 = 3; RLEG_J2 = 4; RLEG_J3 = 5; RLEG_J4 = 6; RLEG_J5 = 7;
%LLEG
    LLEG_J0 = 8; LLEG_J1 = 9; LLEG_J2 = 10; LLEG_J3 = 11; LLEG_J4 = 12; LLEG_J5 = 13;
%RARM
    RARM_J0 = 14; RARM_J1 = 15; RARM_J2 = 16; RARM_J3 = 17; RARM_J4 = 18;
%LARM
    LARM_J0 = 19; LARM_J1 = 20; LARM_J2 = 21; LARM_J3 = 22; LARM_J4 = 23;
%HEAD
    HEAD_J0 = 24; HEAD_J1 = 25;
%杆件长度
    BODYtoHEAD = 0.12650; 
    BODYtoLEGY = 0.050; BODYtoLEGZ = 0.085; LEGtoKNEE = 0.100; KNEEtoFOOT = 0.1029; FOOTtoGROUND = 0.04519; 
    BODYtoSHOULDERY = 0.098; BODYtoSHOULDERZ = 0.100; SHOULDERtoELBOWX = 0.105; SHOULDERtoELBOWY = 0.015; ELBOWtoWRIST = 0.05595; WRISTtoHAND = 0.05755;
%常数
    ToRad = pi/180; global G; G = -9.8; global MAX_JOINT; MAX_JOINT = HEAD_J1;
%确定关节关系
global uLINK
%BODY
uLINK(BODY).name = 'BODY';
uLINK(BODY).child = RLEG_J0;
uLINK(BODY).sister = NONE;
uLINK(BODY).mother = NONE;
uLINK(BODY).b = [0,0,0]';
uLINK(BODY).p = [0,0,0]';
uLINK(BODY).a = [0,0,1]';
uLINK(BODY).q = 0*ToRad;
uLINK(BODY).maxq = 0*ToRad;
uLINK(BODY).minq = 0*ToRad;
uLINK(BODY).m = 1.0496;
uLINK(BODY).c = [-0.00413,0,0.04342]';
%RLEG
uLINK(RLEG_J0).name = 'RLEG_J0[RHipYawPitch]';           
uLINK(RLEG_J0).child = RLEG_J1;
uLINK(RLEG_J0).sister = LLEG_J0;
uLINK(RLEG_J0).mother = BODY;
uLINK(RLEG_J0).b = [0,-BODYtoLEGY,-BODYtoLEGZ]';        
uLINK(RLEG_J0).p = [0,0,0]';                           
uLINK(RLEG_J0).a = [0,(2^0.5)/2,(2^0.5)/2]';                          
uLINK(RLEG_J0).q = 0*ToRad;
uLINK(RLEG_J0).maxq = 42.44*ToRad;
uLINK(RLEG_J0).minq = -65.62*ToRad;
uLINK(RLEG_J0).m = 0.06981;  
uLINK(RLEG_J0).c = [-0.00781,0.01114,0.02661]'; 
uLINK(RLEG_J1).name = 'RLEG_J1[RHipRoll]';             
uLINK(RLEG_J1).child = RLEG_J2;
uLINK(RLEG_J1).sister = NONE;
uLINK(RLEG_J1).mother = RLEG_J0;
uLINK(RLEG_J1).b = [0,0,0]';                            %BODY和RLEG的相对位置[杆件在母连杆坐标系相对于母连杆的位置]
uLINK(RLEG_J1).p = [0,0,0]';                            %杆件在世界坐标系中的位置
uLINK(RLEG_J1).a = [1,0,0]';                            %连杆关节轴矢量，在母连杆坐标系中
uLINK(RLEG_J1).q = 0*ToRad;                             %关节角
uLINK(RLEG_J1).maxq = 21.74*ToRad;
uLINK(RLEG_J1).minq = -45.29*ToRad;
uLINK(RLEG_J1).m = 0.14053;                             %连杆质量
uLINK(RLEG_J1).c = [-0.01549,-0.00029,-0.00515]';       %连杆质心
uLINK(RLEG_J2).name = 'RLEG_J2[RHipPitch]';
uLINK(RLEG_J2).child = RLEG_J3;
uLINK(RLEG_J2).sister = NONE;
uLINK(RLEG_J2).mother = RLEG_J1;
uLINK(RLEG_J2).b = [0,0,0]';
uLINK(RLEG_J2).p = [0,0,0]';
uLINK(RLEG_J2).a = [0,1,0]';
uLINK(RLEG_J2).q = 0*ToRad;
uLINK(RLEG_J2).maxq = 27.73*ToRad;
uLINK(RLEG_J2).minq = -88.00*ToRad;
uLINK(RLEG_J2).m = 0.38968; 
uLINK(RLEG_J2).c = [0.00138,-0.00221,-0.05373]';
uLINK(RLEG_J3).name = 'RLEG_J3[RKneePitch]';
uLINK(RLEG_J3).child = RLEG_J4;
uLINK(RLEG_J3).sister = NONE;
uLINK(RLEG_J3).mother = RLEG_J2;
uLINK(RLEG_J3).b = [0,0,-LEGtoKNEE]';
uLINK(RLEG_J3).p = [0,0,0]';
uLINK(RLEG_J3).a = [0,1,0]';
uLINK(RLEG_J3).q = 0*ToRad;
uLINK(RLEG_J3).maxq = 121.47*ToRad;
uLINK(RLEG_J3).minq = -5.90*ToRad;
uLINK(RLEG_J3).m = 0.30142; 
uLINK(RLEG_J3).c = [0.00453,-0.00225,-0.04936]';
uLINK(RLEG_J4).name = 'RLEG_J4[RAnklePitch]';
uLINK(RLEG_J4).child = RLEG_J5;
uLINK(RLEG_J4).sister = NONE;
uLINK(RLEG_J4).mother = RLEG_J3;
uLINK(RLEG_J4).b = [0,0,-KNEEtoFOOT]';
uLINK(RLEG_J4).p = [0,0,0]';
uLINK(RLEG_J4).a = [0,1,0]';
uLINK(RLEG_J4).q = 0*ToRad;
uLINK(RLEG_J4).maxq = 53.40*ToRad;
uLINK(RLEG_J4).minq = -67.97*ToRad;
uLINK(RLEG_J4).m = 0.13416; 
uLINK(RLEG_J4).c = [0.00045,-0.00029,0.00685]';
uLINK(RLEG_J5).name = 'RLEG_J5[RAnkleRoll]';
uLINK(RLEG_J5).child = NONE;
uLINK(RLEG_J5).sister = NONE;
uLINK(RLEG_J5).mother = RLEG_J4;
uLINK(RLEG_J5).b = [0,0,0]';
uLINK(RLEG_J5).p = [0,0,0]';
uLINK(RLEG_J5).a = [1,0,0]';
uLINK(RLEG_J5).q = 0*ToRad;
uLINK(RLEG_J5).maxq = 22.80*ToRad;
uLINK(RLEG_J5).minq = -44.06*ToRad;
uLINK(RLEG_J5).m = 0.17184; 
uLINK(RLEG_J5).c = [0.02542,-0.0033,-0.03239]';
%LLEG
uLINK(LLEG_J0).name = 'LLEG_J0[LHipYawPitch]';           
uLINK(LLEG_J0).child = LLEG_J1;
uLINK(LLEG_J0).sister = RARM_J0;
uLINK(LLEG_J0).mother = BODY;
uLINK(LLEG_J0).b = [0,BODYtoLEGY,-BODYtoLEGZ]';        
uLINK(LLEG_J0).p = [0,0,0]';                           
uLINK(LLEG_J0).a = [0,(2^0.5)/2,-(2^0.5)/2]';                          
uLINK(LLEG_J0).q = 0*ToRad;
uLINK(LLEG_J0).maxq = 42.44*ToRad;
uLINK(LLEG_J0).minq = -65.62*ToRad;
uLINK(LLEG_J0).m = 0.06981;
uLINK(LLEG_J0).c = [-0.00781,-0.01114,0.02661]';
uLINK(LLEG_J1).name = 'LLEG_J1[LHipRoll]';
uLINK(LLEG_J1).child = LLEG_J2;
uLINK(LLEG_J1).sister = NONE;
uLINK(LLEG_J1).mother = LLEG_J0;
uLINK(LLEG_J1).b = [0,0,0]';
uLINK(LLEG_J1).p = [0,0,0]';
uLINK(LLEG_J1).a = [1,0,0]';
uLINK(LLEG_J1).q = 0*ToRad;
uLINK(LLEG_J1).maxq = 45.29*ToRad;
uLINK(LLEG_J1).minq = -21.74*ToRad;
uLINK(LLEG_J1).m = 0.14053;
uLINK(LLEG_J1).c = [-0.01549,0.00029,-0.00515]';
uLINK(LLEG_J2).name = 'LLEG_J2[LHipPitch]';
uLINK(LLEG_J2).child = LLEG_J3;
uLINK(LLEG_J2).sister = NONE;
uLINK(LLEG_J2).mother = LLEG_J1;
uLINK(LLEG_J2).b = [0,0,0]';
uLINK(LLEG_J2).p = [0,0,0]';
uLINK(LLEG_J2).a = [0,1,0]';
uLINK(LLEG_J2).q = 0*ToRad;
uLINK(LLEG_J2).maxq = 27.73*ToRad;
uLINK(LLEG_J2).minq = -88.00*ToRad;
uLINK(LLEG_J2).m = 0.38968;
uLINK(LLEG_J2).c = [0.00138,0.00221,-0.05373]';
uLINK(LLEG_J3).name = 'LLEG_J3[LKneePitch]';
uLINK(LLEG_J3).child = LLEG_J4;
uLINK(LLEG_J3).sister = NONE;
uLINK(LLEG_J3).mother = LLEG_J2;
uLINK(LLEG_J3).b = [0,0,-LEGtoKNEE]';
uLINK(LLEG_J3).p = [0,0,0]';
uLINK(LLEG_J3).a = [0,1,0]';
uLINK(LLEG_J3).q = 0*ToRad;
uLINK(LLEG_J3).maxq = 121.04*ToRad;
uLINK(LLEG_J3).minq = -5.29*ToRad;
uLINK(LLEG_J3).m = 0.30142;
uLINK(LLEG_J3).c = [0.00453,0.00225,-0.04936]';
uLINK(LLEG_J4).name = 'LLEG_J4[LAnklePitch]';
uLINK(LLEG_J4).child = LLEG_J5;
uLINK(LLEG_J4).sister = NONE;
uLINK(LLEG_J4).mother = LLEG_J3;
uLINK(LLEG_J4).b = [0,0,-KNEEtoFOOT]';
uLINK(LLEG_J4).p = [0,0,0]';
uLINK(LLEG_J4).a = [0,1,0]';
uLINK(LLEG_J4).q = 0*ToRad;
uLINK(LLEG_J4).maxq = 52.86*ToRad;
uLINK(LLEG_J4).minq = -68.15*ToRad;
uLINK(LLEG_J4).m = 0.13416;
uLINK(LLEG_J4).c = [0.00045,0.00029,0.00685]';
uLINK(LLEG_J5).name = 'LLEG_J5[LAnkleRoll]';
uLINK(LLEG_J5).child = NONE;
uLINK(LLEG_J5).sister = NONE;
uLINK(LLEG_J5).mother = LLEG_J4;
uLINK(LLEG_J5).b = [0,0,0]';
uLINK(LLEG_J5).p = [0,0,0]';
uLINK(LLEG_J5).a = [1,0,0]';
uLINK(LLEG_J5).q = 0*ToRad;
uLINK(LLEG_J5).maxq = 44.06*ToRad;
uLINK(LLEG_J5).minq = -22.79*ToRad;
uLINK(LLEG_J5).m = 0.17184;
uLINK(LLEG_J5).c = [0.02542,0.0033,-0.03239]';
%RARM
uLINK(RARM_J0).name = 'RARM_J0[RShoulderPitch]';
uLINK(RARM_J0).child = RARM_J1;
uLINK(RARM_J0).sister = LARM_J0;
uLINK(RARM_J0).mother = BODY;
uLINK(RARM_J0).b = [0,-BODYtoSHOULDERY,BODYtoSHOULDERZ]';
uLINK(RARM_J0).p = [0,0,0]';
uLINK(RARM_J0).a = [0,1,0]';
uLINK(RARM_J0).q = 0*ToRad;
uLINK(RARM_J0).minq = -119.5*ToRad;
uLINK(RARM_J0).maxq = 119.5*ToRad;
uLINK(RARM_J0).m = 0.09304;
uLINK(RARM_J0).c = [-0.00165,0.02663,0.00014]';
uLINK(RARM_J1).name = 'RARM_J1[RShoulderRoll]';
uLINK(RARM_J1).child = RARM_J2;
uLINK(RARM_J1).sister = NONE;
uLINK(RARM_J1).mother = RARM_J0;
uLINK(RARM_J1).b = [0,0,0]';
uLINK(RARM_J1).p = [0,0,0]';
uLINK(RARM_J1).a = [0,0,1]';
uLINK(RARM_J1).q = 0*ToRad;
uLINK(RARM_J1).minq = -76*ToRad;
uLINK(RARM_J1).maxq = 18*ToRad;
uLINK(RARM_J1).m = 0.15777;
uLINK(RARM_J1).c = [0.02455,-0.00563,0.0033]';
uLINK(RARM_J2).name = 'RARM_J2[RElbowYaw]';
uLINK(RARM_J2).child = RARM_J3;
uLINK(RARM_J2).sister = NONE;
uLINK(RARM_J2).mother = RARM_J1;
uLINK(RARM_J2).b = [SHOULDERtoELBOWX,-SHOULDERtoELBOWY,0]';
uLINK(RARM_J2).p = [0,0,0]';
uLINK(RARM_J2).a = [1,0,0]';
uLINK(RARM_J2).q = 0*ToRad;
uLINK(RARM_J2).minq = -119.5*ToRad;
uLINK(RARM_J2).maxq = 119.5*ToRad;
uLINK(RARM_J2).m = 0.06483;
uLINK(RARM_J2).c = [-0.02744,0,-0.00014]';
uLINK(RARM_J3).name = 'RARM_J3[RElbowRoll]';
uLINK(RARM_J3).child = RARM_J4;
uLINK(RARM_J3).sister = NONE;
uLINK(RARM_J3).mother = RARM_J2;
uLINK(RARM_J3).b = [0,0,0]';
uLINK(RARM_J3).p = [0,0,0]';
uLINK(RARM_J3).a = [0,0,1]';
uLINK(RARM_J3).q = 0*ToRad;
uLINK(RARM_J3).minq = 2*ToRad;
uLINK(RARM_J3).maxq = 88.5*ToRad;
uLINK(RARM_J3).m = 0.07761;
uLINK(RARM_J3).c = [0.02556,-0.00281,0.00076]';
uLINK(RARM_J4).name = 'RARM_J4[RWristYaw]';
uLINK(RARM_J4).child = NONE;
uLINK(RARM_J4).sister = NONE;
uLINK(RARM_J4).mother = RARM_J3;
uLINK(RARM_J4).b = [ELBOWtoWRIST,0,0]';
uLINK(RARM_J4).p = [0,0,0]';
uLINK(RARM_J4).a = [1,0,0]';
uLINK(RARM_J4).q = 0*ToRad;
uLINK(RARM_J4).minq = -104.5*ToRad;
uLINK(RARM_J4).maxq = 104.5*ToRad;
uLINK(RARM_J4).m = 0.18533;
uLINK(RARM_J4).c = [0.03434,0.00088,0.00308]';
%LARM
uLINK(LARM_J0).name = 'LARM_J0[LShoulderPitch]';
uLINK(LARM_J0).child = LARM_J1;
uLINK(LARM_J0).sister = HEAD_J0;
uLINK(LARM_J0).mother = BODY;
uLINK(LARM_J0).b = [0,BODYtoSHOULDERY,BODYtoSHOULDERZ]';
uLINK(LARM_J0).p = [0,0,0]';
uLINK(LARM_J0).a = [0,1,0]';
uLINK(LARM_J0).q = 0*ToRad;
uLINK(LARM_J0).minq = -119.5*ToRad;
uLINK(LARM_J0).maxq = 119.5*ToRad;
uLINK(LARM_J0).m = 0.09304;
uLINK(LARM_J0).c = [-0.00165,-0.02663,0.00014]';
uLINK(LARM_J1).name = 'LARM_J1[LShoulderRoll]';
uLINK(LARM_J1).child = LARM_J2;
uLINK(LARM_J1).sister = NONE;
uLINK(LARM_J1).mother = LARM_J0;
uLINK(LARM_J1).b = [0,0,0]';
uLINK(LARM_J1).p = [0,0,0]';
uLINK(LARM_J1).a = [0,0,1]';
uLINK(LARM_J1).q = 0*ToRad;
uLINK(LARM_J1).minq = -18*ToRad;
uLINK(LARM_J1).maxq = 76*ToRad;
uLINK(LARM_J1).m = 0.15777;
uLINK(LARM_J1).c = [0.02455,0.00563,0.0033]';
uLINK(LARM_J2).name = 'LARM_J2[LElbowYaw]';
uLINK(LARM_J2).child = LARM_J3;
uLINK(LARM_J2).sister = NONE;
uLINK(LARM_J2).mother = LARM_J1;
uLINK(LARM_J2).b = [SHOULDERtoELBOWX,SHOULDERtoELBOWY,0]';
uLINK(LARM_J2).p = [0,0,0]';
uLINK(LARM_J2).a = [1,0,0]';
uLINK(LARM_J2).q = 0*ToRad;
uLINK(LARM_J2).minq = -119.5*ToRad;
uLINK(LARM_J2).maxq = 119.5*ToRad;
uLINK(LARM_J2).m = 0.06483;
uLINK(LARM_J2).c = [-0.02744,0,-0.00014]';
uLINK(LARM_J3).name = 'LARM_J3[LElbowRoll]';
uLINK(LARM_J3).child = LARM_J4;
uLINK(LARM_J3).sister = NONE;
uLINK(LARM_J3).mother = LARM_J2;
uLINK(LARM_J3).b = [0,0,0]';
uLINK(LARM_J3).p = [0,0,0]';
uLINK(LARM_J3).a = [0,0,1]';
uLINK(LARM_J3).q = 0*ToRad;
uLINK(LARM_J3).minq = -88.5*ToRad;
uLINK(LARM_J3).maxq = -2*ToRad;
uLINK(LARM_J3).m = 0.07761;
uLINK(LARM_J3).c = [0.02556,0.00281,0.00076]';
uLINK(LARM_J4).name = 'LARM_J4[LWristYaw]';
uLINK(LARM_J4).child = NONE;
uLINK(LARM_J4).sister = NONE;
uLINK(LARM_J4).mother = LARM_J3;
uLINK(LARM_J4).b = [ELBOWtoWRIST,0,0]';
uLINK(LARM_J4).p = [0,0,0]';
uLINK(LARM_J4).a = [1,0,0]';
uLINK(LARM_J4).q = 0*ToRad;
uLINK(LARM_J4).minq = -104.5*ToRad;
uLINK(LARM_J4).maxq = 104.5*ToRad;
uLINK(LARM_J4).m = 0.18533;
uLINK(LARM_J4).c = [0.03434,-0.00088,0.00308]';
%HEAD
uLINK(HEAD_J0).name = 'HEAD_J0[HeadYaw]';
uLINK(HEAD_J0).child = HEAD_J1;
uLINK(HEAD_J0).sister = NONE;
uLINK(HEAD_J0).mother = BODY;
uLINK(HEAD_J0).b = [0,0,BODYtoHEAD]';
uLINK(HEAD_J0).p = [0,0,0]';
uLINK(HEAD_J0).a = [0,0,1]';
uLINK(HEAD_J0).q = 0*ToRad;
uLINK(HEAD_J0).minq = -119.5*ToRad;
uLINK(HEAD_J0).maxq = 119.5*ToRad;
uLINK(HEAD_J0).m = 0.07842;
uLINK(HEAD_J0).c = [-1e-05,0,-0.02742]';
uLINK(HEAD_J1).name = 'HEAD_J1[HeadPitch]';
uLINK(HEAD_J1).child = NONE;
uLINK(HEAD_J1).sister = NONE;
uLINK(HEAD_J1).mother = HEAD_J0;
uLINK(HEAD_J1).b = [0,0,0]';
uLINK(HEAD_J1).p = [0,0,0]';
uLINK(HEAD_J1).a = [0,1,0]';
uLINK(HEAD_J1).q = 0*ToRad;
uLINK(HEAD_J1).minq = -38.5*ToRad;
uLINK(HEAD_J1).maxq = 29.5*ToRad;
uLINK(HEAD_J1).m = 0.60533;
uLINK(HEAD_J1).c = [-0.00112,0,0.05258]';
for number =1:MAX_JOINT
    uLINK(number).R = Rodrigues([1,1,1],0*ToRad);               %初始化所有连杆的姿态
end