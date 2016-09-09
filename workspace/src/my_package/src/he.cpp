#include <iostream>
#include "hello.h"
using namespace std;

int main(){
	Hello* hello = new Hello();
	char temp[20];
	cout<<"Input words:"<<endl;
	cin>>temp;
	hello->hi(temp);
	return 0;
}
