// 有n中不同大小的数字ai，每种各mi个。判断是否可以从这些数字之中选出若干使他们的大小恰好为K.
//限制条件
//1<=n<=100
//1<=ai,mi<=100000
//1<=k<=100000
//d[i+1][j] = {d[i][j-k*a[i]]==1, ∃0<=k<=mi}, d[0][0]=1, d[0][j]=0
//d[n][K]
#include <iostream>
#include <limits>
using namespace std;

int main(){
	int n,K;
	cin>>n;
	int a[100], m[100], **d = new int*[n+1];
	for(int i=0;i<n;++i){
		cin>>a[i]>>m[i];
	}
	cin>>K;
	for(int i=0;i<=n;++i){
		d[i] = new int[K+1];
	}
	d[0][0] = 1;
	for(int i=0;i<n;++i){
		for(int j=0;j<=K;++j){
			int k=0, sig = 0;
			while(k<=m[i] && j>=k*a[i]){
				if(d[i][j-k*a[i]] == 1){
					sig = 1;
					break;
				}
				++k;
			}
			if(sig == 1){
				d[i+1][j] = 1;
			}else{
				d[i+1][j] = 0;
			}
		}
	}
	cout<<d[n][K]<<endl;
	for(int i=0;i<=n;++i){
		delete[] d[i];
	}
	delete[] d;
	return 0;
}
