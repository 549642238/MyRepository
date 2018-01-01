// 01 package
#include <iostream>
#include <string>
using namespace std;
/* DFS
int maxW = 0;

void DFS(int* weight, int* value, int n, int W, int currentV, int currentW, int level){
	if(level == n){
		if(currentV > maxW){
			maxW = currentV;
		}
	}else{
		if(weight[level]+currentW <= W){
			DFS(weight, value, n, W, value[level]+currentV, weight[level]+currentW, level+1);
		}
		DFS(weight, value, n, W, currentV, currentW, level+1);
	}
}

int main(){
	int w[100], v[100];
	int n,W;
	cin>>n;
	for(int i=0;i<n;i++){
		cin>>w[i]>>v[i];
	}
	cin>>W;
	DFS(w,v,n,W,0,0,0);
	cout<<maxW<<endl;
	return 0;
}
*/

// d[i+1][j] = max{d[i][j], d[i][j-w[i]]+v[i](j>=w[i])}, d[0][i] = 0
// d[n][W]
int w[100], v[100], n, W;
int d[100][100];

void DP(){
	for(int a=0;a<n;++a){
		for(int b=0;b<=W;++b){
			if(w[a] > b){
				d[a+1][b] = d[a][b];
			}else{
				d[a+1][b] = d[a][b]>(d[a][b-w[a]]+v[a])?d[a][b]:(d[a][b-w[a]]+v[a]);
			}
		}
	}
}

int main(){
	cin>>n;
	for(int i=0;i<n;i++){
		cin>>w[i]>>v[i];
	}
	cin>>W;
	DP();
	for(int i=0;i<=n;i++){
		for(int j=0;j<=W;++j){
			cout<<d[i][j]<<" ";
		}
		cout<<endl;
	}
	cout<<d[n][W]<<endl;
	return 0;
}
