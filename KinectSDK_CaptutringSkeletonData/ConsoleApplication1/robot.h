/*
	Name:robot.h
	Data:2016.3.18 19:12
	Author:czh
	Description:����һ������Ļ����ˣ����������˵����˽ṹ����������֮��Ĺ�ϵ
	Dependence:math_function.h
	Interface:�����ṩinitialization��findRoute��referencePose��wakeUp��print�ӿ�
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