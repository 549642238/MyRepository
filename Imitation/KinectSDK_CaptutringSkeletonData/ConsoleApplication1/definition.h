/*
	Name:definition.h
	Data:2015.11.11 16:46
	Author:czh
	Description:文件定义了机器人NAO的连杆长度、关节序号、角速度范围以及可能用到的常量PI、采样间隔、动作阈值等，用Joint数据结构
	表示了每个关节的参数(名称、姊妹连杆标号、母连杆标号、子连杆标号、在局部坐标系下的位置b、在给定坐标系[WORLD\TORSO]下的位置p
	和旋转矩阵)、在局部坐标系下的转轴方向a、关节角度q.
	Dependence:无
*/
#ifndef _DEFINITION_H_
#define _DEFINITION_H_

#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <string.h>
#include <assert.h>

using namespace std;

//常数
#define PI 3.14159265357
#define ToRad PI/180
#define G -9.8																	// 重力加速度m/s^2

//控制相关,下面单位对应的是Kinect坐标系下的长度,并非真实长度
#define MIN_UP_MOTION 0.040														// 上身动作幅度阈值，小于阈值不会认为有新动作发生,m
#define MAX_UP_MOTION 0.400														// 上身动作幅度阈值，超过阈值不会认为有新动作发生,m
#define MIN_LOW_MOTION 0.200													// 下身动作幅度阈值，小于阈值不会认为有新动作发生,m
#define MAX_LOW_MOTION 0.300													// 上身动作幅度阈值，大于阈值不会认为有新动作发生,m
#define MIN_FOOT_HEIGHT_DIFF 0.025												// 站立姿势切换到单脚支撑的最小双脚的高度差值,m
#define MAX_FOOT_SUPPORT_X 0.010												// Actor单脚支撑多边形宽度的一半(和具体的Actor有关),m
#define MIN_DOUBLE_SUPPORT_TORSO_HEIGHT_RATIO 0.07								// double support下最小Torso高度变化幅度，和具体Actor有关
#define LEFT_SUPPORT_AREA_LEFT 0.0299											// 左脚站立支撑多边形左边范围(Ankle Frame),m
#define LEFT_SUPPORT_AREA_RIGHT 0.0231											// 左脚站立支撑多边形右边范围(Ankle Frame),m
#define LEFT_SUPPORT_AREA_UP 0.07025											// 左脚站立支撑多边形上边范围(Ankle Frame),m
#define LEFT_SUPPORT_AREA_BOTTOM 0.03025										// 左脚站立支撑多边形下边范围(Ankle Frame),m
#define RIGHT_SUPPORT_AREA_LEFT 0.0231											// 右脚站立支撑多边形左边范围(Ankle Frame),m
#define RIGHT_SUPPORT_AREA_RIGHT 0.0299											// 右脚站立支撑多边形右边范围(Ankle Frame),m
#define RIGHT_SUPPORT_AREA_UP 0.07025											// 右脚站立支撑多边形上边范围(Ankle Frame),m
#define RIGHT_SUPPORT_AREA_BOTTOM 0.03025										// 右脚站立支撑多边形下边范围(Ankle Frame),m
#define COEFFICIENT 0.3															// 上肢关节限制角速度/关节最大角速度，这里不需要下肢，应为下肢关节配置是阻塞式调用，不需要计算执行时间
#define ANKLE_PHYSICAL_ERR 3													// 由于NAO的机械、重力误差造成的AnkleRoll调整

//采样相关
#define JOINT_RECORD_ACCOUNT 13													// Kinect采集到的关键关节数目
#define PERIOD 10																// 计算动作执行时间时的单位累加时间
#define MAX_FRAME 10000															// 采集的最大帧数，Kinect每秒采集30帧，5min后采集30×60×5 = 9000帧
#define SINGLE_SUPPORT_DELAY 500												// 单腿站立采样间隔

//定义关节角度范围
#define LShoulderPitch_MAX 119.5
#define LShoulderPitch_MIN -119.5
#define LShoulderRoll_MAX 76
#define LShoulderRoll_MIN -18
#define LElbowYaw_MAX 119.5
#define LElbowYaw_MIN -119.5
#define LElbowRoll_MAX -2
#define LElbowRoll_MIN -88.5
#define LWristYaw_MAX 104.5
#define LWristYaw_MIN -104.5
#define RShoulderPitch_MAX 119.5
#define RShoulderPitch_MIN -119.5
#define RShoulderRoll_MAX 18
#define RShoulderRoll_MIN -76
#define RElbowYaw_MAX 119.5
#define RElbowYaw_MIN -119.5
#define RElbowRoll_MAX 88.5
#define RElbowRoll_MIN 2
#define RWristYaw_MAX 104.5
#define RWristYaw_MIN -104.5
#define LHipYawPitch_MAX 42.44
#define LHipYawPitch_MIN -65.62
#define LHipRoll_MAX 45.29
#define LHipRoll_MIN -21.74
#define LHipPitch_MAX 27.73
#define LHipPitch_MIN -88
#define LKneePitch_MAX 121.04
#define LKneePitch_MIN -5.29
#define LAnklePitch_MAX 52.86
#define LAnklePitch_MIN -68.15
#define LAnkleRoll_MAX 44.06
#define LAnkleRoll_MIN -22.79
#define RHipYawPitch_MAX 42.44
#define RHipYawPitch_MIN -65.62
#define RHipRoll_MAX 21.74
#define RHipRoll_MIN -45.29
#define RHipPitch_MAX 27.73
#define RHipPitch_MIN -88
#define RKneePitch_MAX 121.47
#define RKneePitch_MIN -5.90
#define RAnklePitch_MAX 53.40
#define RAnklePitch_MIN -67.97
#define RAnkleRoll_MAX 22.80
#define RAnkleRoll_MIN -44.06

//定义关节速度范围
#define MAX_LShoulderPitch 8.26													// 关节LShoulderPitch的最大角速度rad/s
#define MAX_LShoulderRoll 7.19													// 关节LShoulderRoll的最大角速度rad/s
#define MAX_LElbowYaw 8.26														// 关节LElbowYaw的最大角速度rad/s
#define MAX_LElbowRoll 7.19														// 关节LElbowRoll的最大角速度rad/s
#define MAX_RShoulderPitch 8.26													// 关节RShoulderPitch的最大角速度rad/s
#define MAX_RShoulderRoll 7.19													// 关节RShoulderRoll的最大角速度rad/s
#define MAX_RElbowYaw 8.26														// 关节RElbowYaw的最大角速度rad/s
#define MAX_RElbowRoll 7.19														// 关节RElbowRoll的最大角速度rad/s
#define MAX_RHipYawPitch 4.16													// 关节RHipYawPitch的最大角速度rad/s
#define MAX_RHipRoll 4.16														// 关节RHipRoll的最大角速度rad/s
#define MAX_RHipPitch 6.40														// 关节RHipPitch的最大角速度rad/s
#define MAX_RKneePitch 6.40														// 关节RKneePitch的最大角速度rad/s
#define MAX_RAnklePitch 6.40													// 关节RAnklePitch的最大角速度rad/s
#define MAX_RAnkleRoll 4.16														// 关节RAnkleRoll的最大角速度rad/s
#define MAX_LHipYawPitch 4.16													// 关节LHipYawPitch的最大角速度rad/s
#define MAX_LHipRoll 4.16														// 关节LHipRoll的最大角速度rad/s
#define MAX_LHipPitch 6.40														// 关节LHipPitch的最大角速度rad/s
#define MAX_LKneePitch 6.40														// 关节LKneePitch的最大角速度rad/s
#define MAX_LAnklePitch 6.40													// 关节LAnklePitch的最大角速度rad/s
#define MAX_LAnkleRoll 4.16														// 关节LAnkleRoll的最大角速度rad/s

//定义关节序号
#define NONE 0
#define BODY 1
#define RLEG_J0 2
#define RLEG_J1 3
#define RLEG_J2 4
#define RLEG_J3 5
#define RLEG_J4 6
#define RLEG_J5 7
#define LLEG_J0 8
#define LLEG_J1 9
#define LLEG_J2 10
#define LLEG_J3 11
#define LLEG_J4 12
#define LLEG_J5 13
#define RARM_J0 14
#define RARM_J1 15
#define RARM_J2 16
#define RARM_J3 17
#define RARM_J4 18
#define LARM_J0 19
#define LARM_J1 20
#define LARM_J2 21
#define LARM_J3 22
#define LARM_J4 23
#define HEAD_J0 24
#define HEAD_J1 25
#define MAX_JOINT 25															// 最大序号的关节标号 HEAD_J1 = 25

//定义杆件长度 单位：m
#define BODYtoHEAD 0.12650 
#define BODYtoLEGY 0.050
#define BODYtoLEGZ 0.085
#define LEGtoKNEE 0.100
#define KNEEtoFOOT 0.1029
#define FOOTtoGROUND 0.04519
#define BODYtoSHOULDERY 0.098
#define BODYtoSHOULDERZ 0.100
#define SHOULDERtoELBOWX 0.105
#define SHOULDERtoELBOWY 0.015
#define ELBOWtoWRIST 0.05595
#define WRISTtoHAND 0.05755
#define SE_LENGTH sqrt(pow(SHOULDERtoELBOWX,2)+pow(SHOULDERtoELBOWY,2))			// SHoulder-Elbow长度

//定义IK计算常量
#define MAX_LOOP_TIME 15														// 定义InverseKinematics最大迭代次数
#define IK_ERR 0.001															// 定义InverseKinematics容忍理论误差值(单位:m)
#define DIMENSION 3																// 定义坐标维度，也即InverseKinematics的雅克比行数
#define NUMBER_OF_UPPERBODY_JOINTS 2											// 定义IK求解的上肢关节链以及Hip-Knee关节链的关节个数
#define NUMBER_OF_LOWERBODY_JOINTS 1											// 定义IK求解的下肢关节Knee-Ankle链的关节个数

//自碰撞区域定义(包括Torso、Hip区域)，局部坐标系下,单位：m
#define COLLISION_TORSO_UP 0.1265												// Torso上部范围
#define COLLISION_TORSO_BOTTOM -0.05											// Torso下部范围
#define COLLISION_TORSO_FRONT 0.04												// Torso前面范围
#define COLLISION_TORSO_BACK -0.04												// Torso后面范围
#define COLLISION_TORSO_LEFT 0.05												// Torso左侧范围
#define COLLISION_TORSO_RIGHT -0.05												// Torso右侧范围
#define COLLISION_HIP_UP 0.035													// Hip上部范围
#define COLLISION_HIP_BOTTOM -0.02												// Hip下部范围
#define COLLISION_HIP_FRONT 0.05												// Hip前面范围
#define COLLISION_HIP_BACK -0.05												// Hip后面范围
#define COLLISION_HIP_SIDES 0.04												// Hip外侧范围

//三维空间向量
struct vect{
	double x,y,z;
};

//关节数据结构
struct Joint{
	char name[40];																// 关节名称
	int sister,child,mother;													// 姊妹连杆、孩子连杆、母连杆
	double** R;																	// 旋转矩阵
	double b[3],p[3],a[3];														// 局部坐标系坐标、坐标(TORSO、WORLD)、局部坐标系转轴
	double q,maxq,minq;															// 关节角度、最大角度、最小角度
	double m;																	// 关节质量
	double c[3];																// 关节质心在母连杆坐标系下的位置
};

#endif // !_DEFINITION_H_
