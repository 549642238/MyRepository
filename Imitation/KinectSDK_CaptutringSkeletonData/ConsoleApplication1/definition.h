/*
	Name:definition.h
	Data:2015.11.11 16:46
	Author:czh
	Description:�ļ������˻�����NAO�����˳��ȡ��ؽ���š����ٶȷ�Χ�Լ������õ��ĳ���PI�����������������ֵ�ȣ���Joint���ݽṹ
	��ʾ��ÿ���ؽڵĲ���(���ơ�������˱�š�ĸ���˱�š������˱�š��ھֲ�����ϵ�µ�λ��b���ڸ�������ϵ[WORLD\TORSO]�µ�λ��p
	����ת����)���ھֲ�����ϵ�µ�ת�᷽��a���ؽڽǶ�q.
	Dependence:��
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

//����
#define PI 3.14159265357
#define ToRad PI/180
#define G -9.8																	// �������ٶ�m/s^2

//�������,���浥λ��Ӧ����Kinect����ϵ�µĳ���,������ʵ����
#define MIN_UP_MOTION 0.040														// ������������ֵ��С����ֵ������Ϊ���¶�������,m
#define MAX_UP_MOTION 0.400														// ������������ֵ��������ֵ������Ϊ���¶�������,m
#define MIN_LOW_MOTION 0.200													// ������������ֵ��С����ֵ������Ϊ���¶�������,m
#define MAX_LOW_MOTION 0.300													// ������������ֵ��������ֵ������Ϊ���¶�������,m
#define MIN_FOOT_HEIGHT_DIFF 0.025												// վ�������л�������֧�ŵ���С˫�ŵĸ߶Ȳ�ֵ,m
#define MAX_FOOT_SUPPORT_X 0.010												// Actor����֧�Ŷ���ο�ȵ�һ��(�;����Actor�й�),m
#define MIN_DOUBLE_SUPPORT_TORSO_HEIGHT_RATIO 0.07								// double support����СTorso�߶ȱ仯���ȣ��;���Actor�й�
#define LEFT_SUPPORT_AREA_LEFT 0.0299											// ���վ��֧�Ŷ������߷�Χ(Ankle Frame),m
#define LEFT_SUPPORT_AREA_RIGHT 0.0231											// ���վ��֧�Ŷ�����ұ߷�Χ(Ankle Frame),m
#define LEFT_SUPPORT_AREA_UP 0.07025											// ���վ��֧�Ŷ�����ϱ߷�Χ(Ankle Frame),m
#define LEFT_SUPPORT_AREA_BOTTOM 0.03025										// ���վ��֧�Ŷ�����±߷�Χ(Ankle Frame),m
#define RIGHT_SUPPORT_AREA_LEFT 0.0231											// �ҽ�վ��֧�Ŷ������߷�Χ(Ankle Frame),m
#define RIGHT_SUPPORT_AREA_RIGHT 0.0299											// �ҽ�վ��֧�Ŷ�����ұ߷�Χ(Ankle Frame),m
#define RIGHT_SUPPORT_AREA_UP 0.07025											// �ҽ�վ��֧�Ŷ�����ϱ߷�Χ(Ankle Frame),m
#define RIGHT_SUPPORT_AREA_BOTTOM 0.03025										// �ҽ�վ��֧�Ŷ�����±߷�Χ(Ankle Frame),m
#define COEFFICIENT 0.3															// ��֫�ؽ����ƽ��ٶ�/�ؽ������ٶȣ����ﲻ��Ҫ��֫��ӦΪ��֫�ؽ�����������ʽ���ã�����Ҫ����ִ��ʱ��
#define ANKLE_PHYSICAL_ERR 3													// ����NAO�Ļ�е�����������ɵ�AnkleRoll����

//�������
#define JOINT_RECORD_ACCOUNT 13													// Kinect�ɼ����Ĺؼ��ؽ���Ŀ
#define PERIOD 10																// ���㶯��ִ��ʱ��ʱ�ĵ�λ�ۼ�ʱ��
#define MAX_FRAME 10000															// �ɼ������֡����Kinectÿ��ɼ�30֡��5min��ɼ�30��60��5 = 9000֡
#define SINGLE_SUPPORT_DELAY 500												// ����վ���������

//����ؽڽǶȷ�Χ
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

//����ؽ��ٶȷ�Χ
#define MAX_LShoulderPitch 8.26													// �ؽ�LShoulderPitch�������ٶ�rad/s
#define MAX_LShoulderRoll 7.19													// �ؽ�LShoulderRoll�������ٶ�rad/s
#define MAX_LElbowYaw 8.26														// �ؽ�LElbowYaw�������ٶ�rad/s
#define MAX_LElbowRoll 7.19														// �ؽ�LElbowRoll�������ٶ�rad/s
#define MAX_RShoulderPitch 8.26													// �ؽ�RShoulderPitch�������ٶ�rad/s
#define MAX_RShoulderRoll 7.19													// �ؽ�RShoulderRoll�������ٶ�rad/s
#define MAX_RElbowYaw 8.26														// �ؽ�RElbowYaw�������ٶ�rad/s
#define MAX_RElbowRoll 7.19														// �ؽ�RElbowRoll�������ٶ�rad/s
#define MAX_RHipYawPitch 4.16													// �ؽ�RHipYawPitch�������ٶ�rad/s
#define MAX_RHipRoll 4.16														// �ؽ�RHipRoll�������ٶ�rad/s
#define MAX_RHipPitch 6.40														// �ؽ�RHipPitch�������ٶ�rad/s
#define MAX_RKneePitch 6.40														// �ؽ�RKneePitch�������ٶ�rad/s
#define MAX_RAnklePitch 6.40													// �ؽ�RAnklePitch�������ٶ�rad/s
#define MAX_RAnkleRoll 4.16														// �ؽ�RAnkleRoll�������ٶ�rad/s
#define MAX_LHipYawPitch 4.16													// �ؽ�LHipYawPitch�������ٶ�rad/s
#define MAX_LHipRoll 4.16														// �ؽ�LHipRoll�������ٶ�rad/s
#define MAX_LHipPitch 6.40														// �ؽ�LHipPitch�������ٶ�rad/s
#define MAX_LKneePitch 6.40														// �ؽ�LKneePitch�������ٶ�rad/s
#define MAX_LAnklePitch 6.40													// �ؽ�LAnklePitch�������ٶ�rad/s
#define MAX_LAnkleRoll 4.16														// �ؽ�LAnkleRoll�������ٶ�rad/s

//����ؽ����
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
#define MAX_JOINT 25															// �����ŵĹؽڱ�� HEAD_J1 = 25

//����˼����� ��λ��m
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
#define SE_LENGTH sqrt(pow(SHOULDERtoELBOWX,2)+pow(SHOULDERtoELBOWY,2))			// SHoulder-Elbow����

//����IK���㳣��
#define MAX_LOOP_TIME 15														// ����InverseKinematics����������
#define IK_ERR 0.001															// ����InverseKinematics�����������ֵ(��λ:m)
#define DIMENSION 3																// ��������ά�ȣ�Ҳ��InverseKinematics���ſ˱�����
#define NUMBER_OF_UPPERBODY_JOINTS 2											// ����IK������֫�ؽ����Լ�Hip-Knee�ؽ����Ĺؽڸ���
#define NUMBER_OF_LOWERBODY_JOINTS 1											// ����IK������֫�ؽ�Knee-Ankle���Ĺؽڸ���

//����ײ������(����Torso��Hip����)���ֲ�����ϵ��,��λ��m
#define COLLISION_TORSO_UP 0.1265												// Torso�ϲ���Χ
#define COLLISION_TORSO_BOTTOM -0.05											// Torso�²���Χ
#define COLLISION_TORSO_FRONT 0.04												// Torsoǰ�淶Χ
#define COLLISION_TORSO_BACK -0.04												// Torso���淶Χ
#define COLLISION_TORSO_LEFT 0.05												// Torso��෶Χ
#define COLLISION_TORSO_RIGHT -0.05												// Torso�Ҳ෶Χ
#define COLLISION_HIP_UP 0.035													// Hip�ϲ���Χ
#define COLLISION_HIP_BOTTOM -0.02												// Hip�²���Χ
#define COLLISION_HIP_FRONT 0.05												// Hipǰ�淶Χ
#define COLLISION_HIP_BACK -0.05												// Hip���淶Χ
#define COLLISION_HIP_SIDES 0.04												// Hip��෶Χ

//��ά�ռ�����
struct vect{
	double x,y,z;
};

//�ؽ����ݽṹ
struct Joint{
	char name[40];																// �ؽ�����
	int sister,child,mother;													// ������ˡ��������ˡ�ĸ����
	double** R;																	// ��ת����
	double b[3],p[3],a[3];														// �ֲ�����ϵ���ꡢ����(TORSO��WORLD)���ֲ�����ϵת��
	double q,maxq,minq;															// �ؽڽǶȡ����Ƕȡ���С�Ƕ�
	double m;																	// �ؽ�����
	double c[3];																// �ؽ�������ĸ��������ϵ�µ�λ��
};

#endif // !_DEFINITION_H_
