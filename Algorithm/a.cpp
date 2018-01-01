// 有一个长为n的数列a0a1,...,an-1，请求出这个序列中最长的上升子序列的长度。上升子序列指对于任意的i<j，满足ai<aj
// 1<=n<=1000
// 0<=ai<=100000
// d[i] = max{1, d[j]+1(0<=j<i && a[j]<a[i])}
#include <iostream>
using namespace std;

int main(){
	int n, a[1000], d[1001];
	cin>>n;
	for(int i=0;i<n;++i){
		cin>>a[i];
	}
	d[0] = 1;
	int len = 1;
	for(int i=1;i<n;++i){
		int max = 1;
		for(int j=0;j<i;++j){
			if(a[j]<a[i] && max<(d[j]+1)){
				max = d[j]+1;
			}
		}
		d[i] = max;
		if(d[i] > len){
			len = d[i];
		}
	}
	cout<<len<<endl;
	return 0;
}
