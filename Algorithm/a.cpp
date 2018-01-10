#include <iostream>
#include <vector>
#include <queue>
using namespace std;

	const int INF = 100000000;
    struct Node{
        int to, cost;
        friend bool operator<(const Node& a, const Node& b){
            return a.cost<b.cost;
        }
        friend bool operator>(const Node& a, const Node& b){
            return a.cost>b.cost;
        }
        Node(int b,int c):to(b),cost(c){}
    };
int networkDelayTime(vector<vector<int> >& times, int N, int K) {
		vector<int> dis(N+1,INF);
        priority_queue<Node, vector<Node>, greater<Node> > d;
        d.push(Node(K,0));
        vector<vector<pair<int,int> > > edge(N+1);
        for(int i=0;i<times.size();++i){
            edge[times[i][0]].push_back(make_pair(times[i][1], times[i][2]));
        }
		while(!d.empty()){
            Node temp = d.top();
            d.pop();
            dis[temp.to] = temp.cost;
			for(int i=0;i<edge[temp.to].size();++i){
                if(dis[edge[temp.to][i].first] > (dis[temp.to]+edge[temp.to][i].second)){
					cout<<edge[temp.to][i].first<<" "<<dis[temp.to]+edge[temp.to][i].second<<endl;
					d.push(Node(edge[temp.to][i].first,dis[temp.to]+edge[temp.to][i].second));
				}
			}
        }
        int time = -1;
        for(int i=1;i<=N;++i){
            if(dis[i] == INF){
                return -1;
            }
            if(dis[i]>time){
                time = dis[i];
            }
        }
        return time;
    }
int main(){
	vector<vector<int> > times;
	times.push_back({1,2,1});
	times.push_back({2,3,2});
	times.push_back({1,3,2});
	cout<<networkDelayTime(times,3,1)<<endl;
	return 0;
}
