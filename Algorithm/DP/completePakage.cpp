// Complete package
// 一个背包总容量为W，现在有n个物品，第i个 物品体积为weight[i]，价值为value[i]，每个物品都有无限多件，现在往背包里面装东西，怎么装能使背包的内物品价值最大
// d[i+1][j] = max{d[i][j], d[i+1][j-w[i]]+v[i](w[i]<=j)} d[0][i]=0
// d[n][W]
#include <iostream>
#include <string>
using namespace std;

int d[100][100];

int main(){
	int n,W;
	cin>>n;
	int w[100], v[100];
	for(int i=0;i<n;++i){
		cin>>w[i]>>v[i];
	}
	cin>>W;
	for(int i=0;i<n;++i){
		for(int j=0;j<=W;++j){
			int k=0, val = -1;
			if(j>=w[i]){	//
				d[i+1][j] = d[i][j]>(d[i+1][j-w[i]]+v[i])?d[i][j]:(d[i+1][j-w[i]]+v[i]);
			}else{
				d[i+1][j] = d[i][j];
			}				// Conversion
		/*	while(j-k*w[i] >= 0){
				if(d[i][j-k*w[i]]+k*v[i] > val){
					val = d[i][j-k*w[i]]+k*v[i];
				}
				++k;
			}
			d[i+1][j] = val;
		*/
		}
	}
	cout<<d[n][W]<<endl;
	return 0;
}
