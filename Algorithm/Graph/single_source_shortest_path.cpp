#include <iostream>
#include <vector>
#include <limits>
#include <queue>
using namespace std;

/*
// Bellman-Ford
int main(){
	const int INF = 1000000000;
	int N, E;
	cin>>N>>E;
	vector<vector<pair<int,int> > > Graph(N);
	for(int i=0;i<E;++i){
		int start, to, weight;
		cin>>start>>to>>weight;
		Graph[to].push_back(make_pair<int,int>(start, weight));
	}
	vector<int> d(N, INF);
	int start = 0;
	d[start] = 0;
	for(int i=0;i<N;++i){
		if(i != start){
			for(int j=0;j<Graph[i].size();++j){
				if(d[Graph[i][j].first]+Graph[i][j].second<d[i]){
					d[i] = d[Graph[i][j].first]+Graph[i][j].second;
				}
			}
		}
	}
	for(int i=0;i<N;++i){
		cout<<i<<" = "<<d[i]<<endl;
	}
	return 0;
}
*/

// Dijkstra O(ElogV)

int main(){
	const int INF = 1000000000;
	int N,E;
	cin>>N>>E;
	vector<vector<pair<int,int> > > Graph(N);
	priority_queue<pair<int,int> > d;
	vector<int> dis(N, INF);
	for(int i=0;i<E;++i){
		int start, to, weight;
		cin>>start>>to>>weight;
		Graph[start].push_back(make_pair(weight, to));
		Graph[to].push_back(make_pair(weight, start));
	}
	int start = 0;
	d.push(make_pair(0,start));
	while(!d.empty()){
		pair<int,int> neighborNode = d.top();
		d.pop();
		dis[neighborNode.second] = (neighborNode.first<dis[neighborNode.second]?neighborNode.first:dis[neighborNode.second]);
		for(int i=0;i<Graph[neighborNode.second].size();++i){
			if(Graph[neighborNode.second][i].first+dis[neighborNode.second] < dis[Graph[neighborNode.second][i].second]){
				d.push(make_pair(Graph[neighborNode.second][i].first+dis[neighborNode.second], Graph[neighborNode.second][i].second));
			}
		}
	}
	for(int i=0;i<N;++i){
		cout<<i<<" = "<<dis[i]<<endl;
	}
	return 0;
}
