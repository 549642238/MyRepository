// Basic operations on map
#include <algorithm>
#include <iostream>
#include <utility>
#include <string>
#include <map>

using namespace std;

int main(){
	// Init
	cout<<"----------- Init -----------"<<endl;
	map<string, int> m1;
	map<string, int> m2(m1.begin(), m1.end());
	map<string, int> m(m2);
	// Insert
	cout<<"----------- Insert -----------"<<endl;
	pair<string, int> p = make_pair("123", 123);
	m.insert(p);
	m.insert(make_pair("456", 456));
	/*
	*	Step 1. Find key "789", result is not found
	*	Step 2. Insert pair("789", 0);
	*	Step 3. Assgin m["789"] = 789
	*/
	m["789"] = 789;
	pair<map<string,int>::iterator, bool> insertRes = m.insert(make_pair("456", 456));
	if(insertRes.second == false){
		cout<<"Insert 456 failed"<<endl;
		++(insertRes.first->second);
	}
	// Delete
	cout<<"----------- Delete -----------"<<endl;
	cout<<"Find 789 and delete it"<<endl;
	map<string, int>::iterator findRes = m.find("789");
	if(findRes != m.end()){
		m.erase(findRes);
	}
	cout<<"Count 789 = "<<m.count("789")<<endl;
	cout<<"Add 789"<<endl;
	if(m["789"] == 0){
		cout<<"Erase "<<m.erase("789")<<" 789"<<endl;
	}
	// Print
	cout<<"----------- Print -----------"<<endl;
	for(map<string,int>::iterator it=m.begin();it!=m.end();++it){
		cout<<it->first<<" "<<it->second<<endl;
	}
	return 0;
}
