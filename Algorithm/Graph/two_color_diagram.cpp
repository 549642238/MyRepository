// 二色图判断
// 给定一个具有n个顶点的图。要给图上每个顶点染色，并且要使相邻的顶点颜色不同。问是否能最多用2中颜色进行染色？题目保证没有重边和自环 1<=n<=1000
#include <iostream>
#include <vector>
using namespace std;
#if 0
int main(){
	int N, E;
	cin>>N>>E;
	vector<vector<int> > Graph(N, vector<int>(N,0));
	for(int i=0;i<E;++i){
		int start, to;
		cin>>start>>to;
		Graph[start][to] = 1;
		Graph[to][start] = 1;
	}
	vector<char> color(N,'w');
	for(int i=0;i<N;++i){
		if(color[i] == 'w'){
			color[i] = 'r';
		}
		for(int j=0;j<N;++j){
			if(Graph[i][j] == 1){
				if(color[j] == 'w'){
					color[j] = ((color[i]=='r')?'b':'r');
				}else{
					if(color[i] == color[j]){
						cout<<"No"<<endl;
						return 0;
					}
				}
			}
		}
	}
	cout<<"Yes"<<endl;
	return 0;
}
#endif

int main(){
	int N, E;
	cin>>N>>E;
	vector<vector<int> > Graph(N);
	for(int i=0;i<E;++i){
		int start, to;
		cin>>start>>to;
		Graph[start].push_back(to);
	}
	vector<char> color(N, 'w');
	for(int i=0;i<N;++i){
		if(color[i] == 'w'){
			color[i] = 'r';
		}
		for(int j=0;j<Graph[i].size();++j){
			if(color[Graph[i][j]] == 'w'){
				color[Graph[i][j]] = color[i]=='r'?'b':'r';
			}else{
				if(color[Graph[i][j]] == color[i]){
					cout<<"No"<<endl;
					return 0;
				}
			}
		}
	}
	cout<<"Yes"<<endl;
	return 0;
}
