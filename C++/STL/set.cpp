// Basic operations on set
#include <algorithm>
#include <iostream>
#include <utility>
#include <string>
#include <set>

using namespace std;

int main(){
	// Init
	cout<<"----------- Init -----------"<<endl;
	set<int> s1;
	set<int> s2(s1.begin(), s1.end());
	set<int> s(s2);
	// Insert
	cout<<"----------- Insert -----------"<<endl;
	s.insert(1);
	for(int i=0;i<10;++i){
		s.insert(i);
	}
	pair<set<int>::iterator, bool> insertRes = s.insert(7);
	if(insertRes.second == false){
		cout<<"Insert 7 failed"<<endl;
	}
	// Delete
	cout<<"----------- Delete -----------"<<endl;
	cout<<"Find 7 and delete it"<<endl;
	set<int>::iterator findRes = s.find(7);
	if(findRes != s.end()){
		s.erase(findRes);
	}
	cout<<"Count 7 = "<<s.count(7)<<endl;
	cout<<"Erase "<<s.erase(4)<<" 4"<<endl;
	// Print
	cout<<"----------- Print -----------"<<endl;
	for(set<int>::const_iterator it=s.begin();it!=s.end();++it){
		cout<<*it<<endl;
	}
	return 0;
}
