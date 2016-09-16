/*
	Name:log.h
	Data:2016.3.18 19:18
	Author:czh
	Description:提供日志记录功能，用于记录系统过程采集的数据和运算的结果以及系统状态
	Interface:对外提供打开、写入、关闭文件
*/

#ifndef _LOG_H_
#define _LOG_H_

#include <stdio.h>
#include <stdlib.h>

class Log{
private:
	FILE* joint,*position,*err;
public:
	Log(){
		joint = fopen(".\\LogFiles\\joint.txt","w");
		position = fopen(".\\LogFiles\\position.txt","w");
		err =  fopen(".\\LogFiles\\err.txt","w");
	}
	void jointRecord(char* content);
	void positionRecord(char* content);
	void errRecord(char* content);
	void jointFlush();
	void positionFlush();
	void errFlush();
	void jointClose();
	void positionClose();
	void errClose();
};

#endif