// 查找二维矩阵所有联通的W区域
#include <iostream>
#include <cstdio>
#include <cstdlib>
using namespace std;

int N,M,number=0;
char** pic;

void DFS(int i, int j){
	pic[i][j] = 'C';
	if(i-1 >= 0 && j-1>=0 && pic[i-1][j-1]=='W'){
		DFS(i-1, j-1);
	}
	if(i-1 >= 0 && pic[i-1][j]=='W'){
		DFS(i-1, j);
	}
	if(i-1 >= 0 && j+1<M && pic[i-1][j+1]=='W'){
		DFS(i-1, j+1);
	}
	if(j+1<M && pic[i][j+1]=='W'){
		DFS(i, j+1);
	}
	if(i+1 < N && j+1<M && pic[i+1][j+1]=='W'){
		DFS(i+1, j+1);
	}
	if(i+1 < N && pic[i+1][j]=='W'){
		DFS(i+1, j);
	}
	if(i+1 < N && j-1>= 0 && pic[i+1][j-1]=='W'){
		DFS(i+1, j-1);
	}
	if(j-1>= 0 && pic[i][j-1]=='W'){
		DFS(i, j-1);
	}
}

int main(){
	cin>>N>>M;
	pic = new char*[N];
	for(int i=0;i<N;i++){
		pic[i] = new char[M];
	}
	for(int i=0;i<N;i++){
		for(int j=0;j<M;j++){
			cin>>pic[i][j];
		}
	}
	for(int i=0;i<N;i++){
		for(int j=0;j<M;j++){
			if(pic[i][j] == 'W'){
				number ++;
				DFS(i,j);
			}
		}
	}
	cout<<number<<endl;
	for(int i=0;i<N;i++){
		delete[] pic[i];
	}
	delete[] pic;
	return 0;
}
