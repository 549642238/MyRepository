#include <iostream>
#include <algorithm>
using namespace std;

int main(){
	unsigned int N, R;
	unsigned int X[1001];
	while(cin>>R>>N){
		if(N == -1 && R == -1){
			return 0;
		}else{
			for(int i=0;i<N;i++){
				cin>>X[i];
			}
			sort(X, X+N);
			unsigned int sigNumber = 0;
			for(int i=0;i<N;){
				unsigned int lastPos = X[i];
				while(i<N && X[i]-lastPos<=R){
					++i;
				}
				++sigNumber;
				lastPos = X[i-1];
				while(i<N && X[i]-lastPos<=R){
					++i;
				}
			}
			cout<<sigNumber<<endl;
		}
	}
	return 0;
}
