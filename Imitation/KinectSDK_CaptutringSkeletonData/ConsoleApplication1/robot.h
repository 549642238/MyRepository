/*
	Name:robot.h
	Data:2016.3.18 19:12
	Author:czh
	Description:定义一个具体的机器人，包括机器人的连杆结构参数和连杆之间的关系
	Dependence:math_function.h
	Interface:对外提供initialization、findRoute、referencePose、wakeUp、print接口
*/

#ifndef _ROBOT_H_
#define _ROBOT_H_

#include "math_function.h"

class Robot{
private:
	char name[20];
public:
	Joint uLINK[MAX_JOINT+1];
	Robot(const char* aname){
		strcpy(name,aname);
	}
	void initialization();
	int* findRoute(int to);
	void referencePose();
	void wakeUp();
	void print(int j);
};

#endif