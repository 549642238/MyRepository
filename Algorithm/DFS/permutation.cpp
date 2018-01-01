// Permutation for A(n,n)
#include <iostream>
using namespace std;

void permutation(int* list, int start, int end){
	if(start > end){
		for(int i=0;i<=end;i++){
			cout<<list[i]<<" ";
		}
		cout<<endl;
	}else{
		for(int i=start;i<=end;i++){
			int temp = list[i];
			list[i] = list[start];
			list[start] = temp;
			permutation(list, start+1, end);
			temp = list[i];
			list[i] = list[start];
			list[start] = temp;
		}
	}
}

int main(){
	int a[] = {1,2,3};
	permutation(a,0,2);
	return 0;
}
