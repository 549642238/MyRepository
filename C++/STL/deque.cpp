// Basic operations on deque
#include <algorithm>
#include <iostream>
#include <utility>
#include <string>
#include <deque>

using namespace std;

int main(){
	// Init
	cout<<"----------- Init -----------"<<endl;
	deque<int> dq1;
	deque<int> dq2(dq1.begin(), dq1.end());
	deque<int> dq3(dq2);
	deque<int> dq(3, 2);
	// Insert
	cout<<"----------- Insert -----------"<<endl;
	for(int i=0;i<10;++i){
		dq.push_back(i+1);
	}
	dq.push_front(-3);
	// Find
	cout<<"----------- Find -----------"<<endl;
	cout<<"Random access 4th ele"<<endl;
	cout<<dq[3]<<endl;
	cout<<"Find 4"<<endl;
	deque<int>::iterator findRes = find(dq.begin(), dq.end(), 4);
	deque<int>::iterator insertRes = dq.insert(findRes, 666);
	cout<<"Insert 666 before 4"<<endl;
	// Delete
	cout<<"----------- Delete -----------"<<endl;
	cout<<"Delete ele next 666"<<endl;
	deque<int>::iterator nextEle = dq.erase(++insertRes);
	cout<<"Next ele is "<<*nextEle<<endl;
	cout<<"Delete first ele"<<endl;
	dq.pop_front();
	// Print
	cout<<"----------- Print -----------"<<endl;
	for(deque<int>::iterator it=dq.begin();it!=dq.end();++it){
		cout<<*it<<" ";
	}
	cout<<endl;
	return 0;
}
