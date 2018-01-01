#include <iostream>
#include <deque>
#include <string>
using namespace std;

int main(){
	unsigned lineNumber = 0;
	unsigned int N;
	cin>>N;
	deque<char> S;
	for(int i=0;i<N;i++){
		char c;
		cin>>c;
		S.push_back(c);
	}
	while(!S.empty()){
		if(lineNumber == 80){
			cout<<endl;
			lineNumber = 0;
		}
		if(S.front() < S.back()){
			cout<<S.front();
			S.pop_front();
			lineNumber ++;
		}else if(S.front() > S.back()){
			cout<<S.back();
			S.pop_back();
			lineNumber ++;
		}else{
			int len = S.size();
			if(len > 1){
				int sig = 0;
				for(int i=0;i<len/2;i++){
					if(S[i] ==  S[len-i-1]){
					}else if(S[i] < S[len-1-i]){
						sig = 0;
						break;
					}else{
						sig = 1;
						break;
					}
				}
				if(!sig){
					cout<<S.front();
					S.pop_front();
					lineNumber ++;
				}else{
					cout<<S.back();
					S.pop_back();
					lineNumber ++;
				}
			}else{
				cout<<S.front();
				S.pop_front();
				lineNumber++;
			}
		}
	}
	return 0;
}
