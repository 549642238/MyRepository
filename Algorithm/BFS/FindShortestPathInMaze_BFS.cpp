//给定一个大小为N*M的迷宫，迷宫由通道和墙壁组成，每一步可以向邻接的上下左右四格的通道移动。请求出从起点到终点的最小部署，本题假设从起点一定可以移动到终点  
//input  
//  N=10,M=10  
//#S######.#  
//......#..#  
//.#.##.##.#  
//.#........  
//##.##.####  
//....#....#  
//.#######.#  
//....#.....  
//.####.###.  
//....#...G#  
//output  
//22
#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <queue>
using namespace std;

struct Node{
	int i,j,level;
};
char** pic;
int N,M,step = 0;
queue<Node> path;
int sig = 0;

void BFS(){
	while(!path.empty() && !sig){
		Node first = path.front();
		path.pop();
		if(pic[first.i][first.j] == 'G'){
			step = first.level;
			sig = 1;
		}else{
			pic[first.i][first.j] = '#';
			if(first.i-1>=0 && (pic[first.i-1][first.j]=='.' || pic[first.i-1][first.j]=='G')){
				Node node;
				node.i = first.i-1;
				node.j = first.j;
				node.level = first.level+1;
				path.push(node);
			}
			if(first.j-1>=0 && (pic[first.i][first.j-1]=='.' || pic[first.i][first.j-1]=='G')){
				Node node;
				node.i = first.i;
				node.j = first.j-1;
				node.level = first.level+1;
				path.push(node);
			}
			if(first.i+1<N && (pic[first.i+1][first.j]=='.' || pic[first.i+1][first.j]=='G')){
				Node node;
				node.i = first.i+1;
				node.j = first.j;
				node.level = first.level+1;
				path.push(node);
			}
			if(first.j+1<M && (pic[first.i][first.j+1]=='.' || pic[first.i][first.j+1]=='G')){
				Node node;
				node.i = first.i;
				node.j = first.j+1;
				node.level = first.level+1;
				path.push(node);
			}
		}
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
	for(int i=0;i<N && !sig;i++){
		for(int j=0;j<M && !sig;j++){
			if(pic[i][j] == 'S'){
				Node node;
				node.i = i;
				node.j = j;
				node.level = 0;
				path.push(node);
				BFS();
			}
		}
	}
	cout<<step<<endl;
	for(int i=0;i<N;i++){
		delete[] pic[i];
	}
	delete[] pic;
	return 0;
}
