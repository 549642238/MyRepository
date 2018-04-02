// POSIX C
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <unistd.h>
#include <signal.h>
#include <errno.h>
#include <fcntl.h>
#include <dirent.h>

// ANSI C
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <ctime>

// C++ lib
#include <iostream>
#include <limits>
#include <fstream>
#include <vector>
using namespace std;

int main(){
	int N;
	cin>>N;
	if(N == 0){
		cout<<0<<endl;
		return 0;
	}
	vector<int> A(N);
	vector<int> B(N);
	for(int i=0;i<N;++i){
		cin>>A[i];
	}
	for(int i=0;i<N;++i){
		cin>>B[i];
	}
	vector<vector<int> > d(N+1, vector<int>(N+1, 0));
	d[0][0] = 0;
	for(int i=1;i<=N;++i){
		if(i%2 == 0){
			int sum = 0;
			for(int j=0;j<i;j+=2){
				sum += A[j]*A[j+1];
			}
			d[i][0] = sum;
		}else{
			d[i][0] = numeric_limits<int>::max();
		}
	}
	for(int i=1;i<=N;++i){
		if(i%2 == 0){
			int sum = 0;
			for(int j=0;j<i;j+=2){
				sum += B[j]*B[j+1];
			}
			d[0][i] = sum;
		}else{
			d[0][i] = numeric_limits<int>::max();
		}
	}
	d[1][1] = A[0]*B[0];
	for(int i=2;i<=N;++i){
		if(i%2 == 1){
			int min = numeric_limits<int>::max();
			if(d[i-1][0]!=numeric_limits<int>::max()){
				min = min<(d[i-1][0]+A[i-1]*B[0])?min:(d[i-1][0]+A[i-1]*B[0]);
			}
			if(d[i-2][1]!=numeric_limits<int>::max()){
				min = min<(d[i-2][1]+A[i-1]*A[i-2])?min:(d[i-2][1]+A[i-1]*A[i-2]);
			}
			d[i][1] = min;
		}else{
			d[i][1] = numeric_limits<int>::max();
		}
	}
	for(int i=2;i<=N;++i){
		if(i%2 == 1){
			int min = numeric_limits<int>::max();
			if(d[0][i-1]!=numeric_limits<int>::max()){
				min = min<(d[0][i-1]+A[0]*B[i-1])?min:(d[0][i-1]+A[0]*B[i-1]);
			}
			if(d[1][i-2]!=numeric_limits<int>::max()){
				min = min<(d[1][i-2]+B[i-1]*B[i-2])?min:(d[1][i-2]+B[i-1]*B[i-2]);
			}
			d[1][i] = min;
		}else{
			d[1][i] = numeric_limits<int>::max();
		}
	}
	for(int i=2;i<=N;++i){
		for(int j=2;j<=N;++j){
			int min = numeric_limits<int>::max();
			if(d[i-1][j-1]!=numeric_limits<int>::max()){
				min = d[i-1][j-1]+A[i-1]*B[j-1];
			}
			if(d[i-2][j]!=numeric_limits<int>::max()){
				min = min<(d[i-2][j]+A[i-2]*A[i-1])?min:(d[i-2][j]+A[i-2]*A[i-1]);
			}
			if(d[i][j-2]!=numeric_limits<int>::max()){
				min = min<(d[i][j-2]+B[j-2]*B[j-1])?min:(d[i][j-2]+B[j-2]*B[j-1]);
			}
			d[i][j] = min;
		}
	}
	/*
	for(int i=0;i<=N;++i){
		for(int j=0;j<=N;++j){
			cout<<d[i][j]<<" ";
		}
		cout<<endl;
	}
	*/
	cout<<d[N][N]<<endl;
	return 0;
}
