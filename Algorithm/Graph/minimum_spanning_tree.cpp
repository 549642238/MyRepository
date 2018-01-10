// Minimum spanning tree
#include <iostream>
#include <algorithm>
#include <queue>
#include <vector>
#include <unordered_set>
using namespace std;

struct Node{
	int start, to, weight;
	Node(int a, int b, int c):start(a), to(b), weight(c){}
	friend bool operator>(const Node& a, const Node& b){
		return a.weight>b.weight;
	}
	friend bool operator<(const Node& a, const Node& b){
		return a.weight<b.weight;
	}
};

int main(){
	int N, E;
	cin>>N>>E;
	priority_queue<Node, vector<Node>, greater<Node> > Graph;
	for(int i=0;i<E;++i){
		int start, to, weight;
		cin>>start>>to>>weight;
		Graph.push(Node(start, to, weight));
	}
	unordered_set<int> V;
	for(int i=0;i<N;++i){
		V.insert(i);
	}
	int cost = 0;
	while(!V.empty() && !Graph.empty()){
		Node temp = Graph.top();
		Graph.pop();
		if(V.find(temp.start) != V.end() || V.find(temp.to) != V.end()){
			cost += temp.weight;
			V.erase(temp.start);
			V.erase(temp.to);
		}
	}
	cout<<cost<<endl;
	return 0;
}
