// Basic operations on priority_queue
#include <algorithm>
#include <iostream>
#include <utility>
#include <string>
#include <queue>

using namespace std;

int main(){
	// Init
	cout<<"----------- Init -----------"<<endl;
	priority_queue<int, vector<int>, greater<int> > q;		// Little heap
	// Insert
	cout<<"----------- Insert -----------"<<endl;
	for(int i=0;i<10;++i){
		q.push(i+1);
	}
	// Delete
	q.pop();
	// Print
	cout<<"----------- Print -----------"<<endl;
	while(!q.empty()){
		cout<<q.top()<<" ";
		q.pop();
	}
	cout<<endl;
	return 0;
}
