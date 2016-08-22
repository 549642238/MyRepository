/*
	Name:log.h
	Data:2016.3.18 19:18
	Author:czh
	Description:�ṩ��־��¼���ܣ����ڼ�¼ϵͳ���̲ɼ������ݺ�����Ľ���Լ�ϵͳ״̬
	Interface:�����ṩ�򿪡�д�롢�ر��ļ�
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