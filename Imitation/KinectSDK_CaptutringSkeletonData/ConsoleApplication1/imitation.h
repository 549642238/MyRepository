/*
	Name:imitation.h
	Data:2016.3.18 19:20
	Author:czh
	Description:模仿的采集和处理模块主要实现，使用多线程并行两个模块，同时处理模块也采用并行模式
	Dependence:robot.h
	Interface:对外提供模仿功能
*/

#ifndef _CAPTURING_H_
#define _CAPTURING_H_

#include <WINSOCK2.H>
#include <windows.h>  
#include <time.h>
#include <NuiApi.h>
#include <opencv2/opencv.hpp>
#include "zmp.h"
#include "log.h"

using namespace cv;

//进行模仿，一边采集处理数据，一边讲数据传输到Server端控制NAO做动作，需要实现开启NAO和Server，没有错误发生返回0
int imitate(char* ip,int port);

#endif