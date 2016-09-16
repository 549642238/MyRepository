/*
	Name:zmp.h
	Data:2016.3.18 19:18
	Author:czh
	Description:提供ZMP、CoM的计算方法,控制机器人在double support和single support两种模式下的平衡。并根据最后的平衡结果判断ZMP是否在
	支撑多边形内并决定是否配置关节角度
	Dependence:ik_solver.h
	Interface:对外提供calcCoM()、calcM()、balance()接口
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