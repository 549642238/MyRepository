// Basic operations on list
#include <algorithm>
#include <iostream>
#include <utility>
#include <string>
#include <list>

using namespace std;

int main(){
	// Init
	cout<<"----------- Init -----------"<<endl;
	list<int> l1;
	list<int> l2(l1.begin(), l1.end());
	list<int> l3(l2);
	list<int> l(3, 2);
	// Insert
	cout<<"----------- Insert -----------"<<endl;
	for(int i=0;i<10;++i){
		l.push_back(i+1);
	}
	l.insert(l.begin(), -1);
	l.insert(l.begin(), 3, -2);
	l.push_front(-3);
	for(list<int>::iterator it=l.begin();it!=l.end();++it){
		cout<<*it<<" ";
	}
	cout<<endl;
	// Find
	cout<<"----------- Find -----------"<<endl;
	cout<<"Find 4"<<endl;
	list<int>::iterator findRes = find(l.begin(), l.end(), 4);
	list<int>::iterator insertRes = l.insert(findRes, 666);
	cout<<"Insert 666 before 4"<<endl;
	// Delete
	cout<<"----------- Delete -----------"<<endl;
	cout<<"Delete ele next 666"<<endl;
	list<int>::iterator nextEle = l.erase(++insertRes);
	cout<<"Next ele is "<<*nextEle<<endl;
	cout<<"Delete first ele"<<endl;
	l.pop_front();
	// Print
	cout<<"----------- Print -----------"<<endl;
	for(list<int>::iterator it=l.begin();it!=l.end();++it){
		cout<<*it<<" ";
	}
	cout<<endl;
	return 0;
}
