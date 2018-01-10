#include <iostream>
#include <algorithm>
#include <vector>
#include <utility>
#include <queue>
using namespace std;

int main(){
	int N, L, P;
	cin>>N;
	vector<pair<int,int> > station;
	for(int i=0;i<N;++i){
		int A,B;
		cin>>A>>B;
		station.push_back(make_pair<int,int>(A,B));
	}
	cin>>L>>P;
	for(int i=0;i<N;++i){
		station[i].first = L - station[i].first;
	}
	priority_queue<int> currentGasStation;
	int currentPos = P, lastPos = 0, res = 0;
	for(vector<pair<int,int> >::iterator it=station.begin(); it!=station.end();){
		if(it->first<=P){
			currentGasStation.push(it->second);
			station.erase(it);
		}else{
			++it;
		}
	}
	while(currentPos < L){
		if(currentGasStation.empty()){
			res = -1;
			break;
		}
		lastPos = currentPos;
		currentPos += currentGasStation.top();
		currentGasStation.pop();
		for(vector<pair<int,int> >::iterator it=station.begin();it!=station.end();){
			if(it->first>lastPos && it->first<=currentPos){
				currentGasStation.push(it->second);
				station.erase(it);
			}else{
				++it;
			}
		}
		++res;
	}
	cout<<res<<endl;
	return 0;
}
