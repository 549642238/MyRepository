/*
	Name:zmp.h
	Data:2016.3.18 19:18
	Author:czh
	Description:�ṩZMP��CoM�ļ��㷽��,���ƻ�������double support��single support����ģʽ�µ�ƽ�⡣����������ƽ�����ж�ZMP�Ƿ���
	֧�Ŷ�����ڲ������Ƿ����ùؽڽǶ�
	Dependence:ik_solver.h
	Interface:�����ṩcalcCoM()��calcM()��balance()�ӿ�
*/

#ifndef _ZMP_H_
#define _ZMP_H_

#include "ik_solver.h"

class ZMP{
private:
	Robot* myRobot;
	IKSolver* myIKSolver;
public:
	ZMP(Robot* aRobot,IKSolver* aIKSolver){
		myRobot = aRobot;
		myIKSolver = aIKSolver;
	}
	double calcM(int j);
	double* calcMC(int j);
	double* calcCoM(const double M);
	void getAnkleAngle(vect AC,double& change_ankle_roll,double& change_ankle_pitch);
	int doubleSupportCheck(const double M);
	void balance(int supportMode,int& sig,const double M,int no);
};

#endif