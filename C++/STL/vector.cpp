// Basic operations on vector
#include <algorithm>
#include <iostream>
#include <utility>
#include <string>
#include <vector>

using namespace std;

int main(){
	// Init
	cout<<"----------- Init -----------"<<endl;
	vector<int> v1;
	vector<int> v2(v1.begin(), v1.end());
	vector<int> v3(v2);
	vector<int> v(10, 2);
	// Insert
	cout<<"----------- Insert -----------"<<endl;
	for(int i=0;i<10;++i){
		v.push_back(i+1);
	}
	vector<int>::iterator insertRes = v.insert(v.begin(), -1);
	cout<<"Insert ele "<<*insertRes<<endl;
	v.insert(v.begin(), 3, -2);
	// Find
	cout<<"----------- Find -----------"<<endl;
	cout<<"Find 4"<<endl;
	vector<int>::iterator findRes = find(v.begin(), v.end(), 4);
	cout<<"Random access 4th ele"<<endl;
	cout<<v[3]<<endl;
	// Delete
	cout<<"----------- Delete -----------"<<endl;
	cout<<"Delete 4"<<endl;
	vector<int>::iterator nextEle = v.erase(findRes);
	cout<<"Next ele is "<<*nextEle<<endl;
	cout<<"Delete last ele"<<endl;
	v.pop_back();
	// Print
	cout<<"----------- Print -----------"<<endl;
	for(vector<int>::iterator it=v.begin();it!=v.end();++it){
		cout<<*it<<" ";
	}
	cout<<endl;
	return 0;
}
