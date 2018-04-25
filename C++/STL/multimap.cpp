// Basic operations on multimap
#include <algorithm>
#include <iostream>
#include <utility>
#include <string>
#include <map>

using namespace std;

int main(){
	// Init
	cout<<"----------- Init -----------"<<endl;
	multimap<string, int> m1;
	multimap<string, int> m2(m1.begin(), m1.end());
	multimap<string, int> m(m2);
	// Insert
	cout<<"----------- Insert -----------"<<endl;
	pair<string, int> p = make_pair("123", 123);
	m.insert(p);
	m.insert(make_pair("456", 456));
	cout<<"Reinsert 456"<<endl;
	m.insert(make_pair("456", 456));
	m.insert(make_pair("789", 789));
	m.insert(make_pair("789", 798));
	// Delete
	cout<<"----------- Delete -----------"<<endl;
	cout<<"Count 789 = "<<m.count("789")<<endl;
	cout<<"Erase "<<m.erase("789")<<" 789"<<endl;
	// Find
	cout<<"----------- Find -----------"<<endl;
	cout<<"Find 456"<<endl;
	pair<multimap<string,int>::iterator, multimap<string,int>::iterator> findRes = m.equal_range("456");
	while(findRes.first != findRes.second){
		cout<<findRes.first->first<<" "<<findRes.first->second<<endl;
		++findRes.first;
	}
	// Print
	cout<<"----------- Print -----------"<<endl;
	for(multimap<string,int>::iterator it=m.begin();it!=m.end();++it){
		cout<<it->first<<" "<<it->second<<endl;
	}
	return 0;
}
