#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <cstring>
using namespace std;

char* myStrtok(char* s, const char* split){
	int len = 0;
	static char* res;
	if(s!=nullptr){
		res = s;
	}
	if(*res == 0){
		return nullptr;
	}
	bool find = false;
	char* ret;
	while(len==0 || find){
		find = true;
		for(const char* c=split;*c!=0;c++){
			if(*c == *res){
				*res = 0;
				find = false;
				break;
			}
		}
		if(find){
			if(len==0){
				ret = res;
			}
			len++;
		}
		++res;
		if(*res==0){
			break;
		}
	}
	if(len==0){
		return nullptr;
	}
	return ret;
}

int main(){
	char s[] = "a,1,2a3,a";
	const char* split = "a,";
	char* temp = myStrtok(s, split);
	while(temp!=nullptr){
		cout<<temp<<endl;
		temp = myStrtok(nullptr, split);
	}
	return 0;
}
