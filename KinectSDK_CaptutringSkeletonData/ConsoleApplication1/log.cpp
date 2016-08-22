#include "log.h"

void Log::jointRecord(char* content){
	fprintf(joint,content);
}

void Log::jointFlush(){
	fflush(joint);
}

void Log::jointClose(){
	fclose(joint);
}

void Log::positionRecord(char* content){
	fprintf(position,content);
}

void Log::positionFlush(){
	fflush(position);
}

void Log::positionClose(){
	fclose(position);
}

void Log::errRecord(char* content){
	fprintf(err,content);
}

void Log::errFlush(){
	fflush(err);
}

void Log::errClose(){
	fclose(err);
}
