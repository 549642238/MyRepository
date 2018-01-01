#include <iostream>
#include <list>
#include <limits>
using namespace std;

bool compare(const int a, const int b){
	return a>b;
}

int main(){
	unsigned int N, cost=0;
	cin>>N;
	unsigned int* L = new unsigned int[N];
	for(int i=0;i<N;i++){
		cin>>L[i];
	}
	while(N > 1){
		unsigned int min1 = numeric_limits<unsigned int>::max();
		unsigned int min2 = numeric_limits<unsigned int>::max();
		unsigned int minPos1=0, minPos2=0;
		for(int i=0;i<N;i++){
			if(L[i] < min1){
				min2 = min1;
				min1 = L[i];
				minPos2 = minPos1;
				minPos1 = i;
			}else if(L[i] < min2){
				min2 = L[i];
				minPos2 = i;
			}
		}
		cost += min1+min2;
		L[minPos1] = min1+min2;
		L[minPos2] = L[N-1];
		--N;
	}
	cout<<cost<<endl;
	delete[] L;
	return 0;
}
