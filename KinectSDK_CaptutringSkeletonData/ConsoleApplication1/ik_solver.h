/*
	Name:ik_solver.h
	Data:2016.3.18 19:13
	Author:czh
	Description:�ṩIK��FK�㷨���ڴ˻����Ͽ���ʵ������ײ����
	Dependence:robot.h
	Interface:�����ṩforwardKinematics��forwardKinematicsChild��inverseKinematics��ikUpperBody��ikLowerBody��selfCollisionAvoidance�ӿ�
*/

#ifndef _IK_SOLVER_H_
#define _IK_SOLVER_H_

#include "funcpinv.h"
#include "funcinv.h"
#include "robot.h"

class IKSolver{
private:
	Robot* myRobot;
public:
	IKSolver(Robot* aRobot){
		myRobot = aRobot;
	}
	void forwardKinematics(int j);
	void forwardKinematicsChild(int j);
	double** calcJacobian(int* idx,int jsize);
	double inverseKinematics(int to,Joint Target);
	double ikUpperBody(vect RSE,vect REH,vect LSE,vect LEH);
	double ikLowerBody(vect RHK,vect RKA,vect LHK,vect LKA);
	int selfCollisionAvoidance();
};

#endif