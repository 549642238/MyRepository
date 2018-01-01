// Combination for C(k,n)
#include <iostream>
using namespace std;

void DFS(int* list, int k , int n, int num, int level, char* path){
	if(level == n){
		if(num == k){
			for(int i=0;i<n;i++){
				if(path[i] == '1'){
					cout<<list[i]<<" ";
				}
			}
			cout<<endl;
		}
	}else{
		if(num<=k){			// Branch and cut
			path[level] = '0';
			DFS(list, k, n, num, level+1, path);
			path[level] = '1';
			DFS(list, k, n, num+1, level+1, path);
		}
	}
}

void combination(int* list, int k, int n){
	char path[n] ;
	DFS(list, k, n, 0, 0, path);
}

int main(){
	int a[] = {1,2,3,4,5,6,7};
	combination(a,3,7);
	return 0;
}
