// LCS: Longest common subsequence
// d[i][j] = max{d[i-1][j], d[i][j-1], d[i-1][j-1]+1} d[0][i]=0, d[i][0]=0
// d[len1][len2]
#include <iostream>
#include <string>
using namespace std;

int main(){
	string s1, s2;
	cin>>s1>>s2;
	int len1 = s1.size(), len2 = s2.size();
	int** d = new int*[len1+1];
	for(int i=0;i<=len1;++i){
		d[i] = new int[len2+1];
	}
	for(int i=0;i<=len1;++i){
		d[i][0] = 0;
	}
	for(int i=0;i<=len2;++i){
		d[0][i] = 0;
	}
	for(int i=1;i<=len1;++i){
		for(int j=1;j<=len2;++j){
			int a,b,c;
			if(s1[i-1] == s2[j-1]){
				a = d[i-1][j-1] + 1;
			}else{
				a = d[i-1][j-1];
			}
			b = d[i-1][j];
			c = d[i][j-1];
			d[i][j] = a>b?(a>c?a:c):(b>c?b:c);
		}
	}
	cout<<d[len1][len2]<<endl;
	for(int i=0;i<=len1;++i){
		delete[] d[i];
	}
	delete[] d;
	return 0;
}
