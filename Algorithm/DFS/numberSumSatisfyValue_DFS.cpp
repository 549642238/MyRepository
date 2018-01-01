// 给定n个数，计算是否存在其中的一组数字和等于m
#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <utility>
#include <memory>
#include <limits>
#include <algorithm>
#include <string>
#include <unordered_map>
#include <sys/time.h>
using namespace std;

int* node, *a, *solution;
int n, sig = 0, sum = 0, k;

void DFS(int level){
	if(sum == k){
		sig = 1;
		for(int i=0;i<n;i++){
			solution[i] = node[i];
		}
	}
	if(!sig && level < n){
		node[level] = 1;
		sum += a[level];
		DFS(level+1);
		node[level] = 0;
		sum -= a[level];
		DFS(level+1);
	}
}

int main(){
	cin>>n>>k;
	a = new int[n];
	node = new int[n];
	solution = new int[n];
	for(int i=0;i<n;i++){
		cin>>a[i];
		node[i] = 0;
		solution[i] = 0;
	}
	timeval start, end;
	gettimeofday(&start, NULL);
	DFS(0);
	if(sig){
		cout<<"Yes"<<endl;
		for(int i=0;i<n;i++){
			if(solution[i] == 1){
				cout<<a[i]<<" ";
			}
		}
		cout<<endl;
	}else{
		cout<<"No"<<endl;
	}
	gettimeofday(&end, NULL);
	cout<<(end.tv_sec-start.tv_sec)+1.0*(end.tv_usec-start.tv_usec)/1000000<<endl;
	delete[] a;
	delete[] node;
	return 0;
}
