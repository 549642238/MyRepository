/*
	Name:imitation.h
	Data:2016.3.18 19:20
	Author:czh
	Description:ģ�µĲɼ��ʹ���ģ����Ҫʵ�֣�ʹ�ö��̲߳�������ģ�飬ͬʱ����ģ��Ҳ���ò���ģʽ
	Dependence:robot.h
	Interface:�����ṩģ�¹���
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

//����ģ�£�һ�߲ɼ��������ݣ�һ�߽����ݴ��䵽Server�˿���NAO����������Ҫʵ�ֿ���NAO��Server��û�д���������0
int imitate(char* ip,int port);

#endif