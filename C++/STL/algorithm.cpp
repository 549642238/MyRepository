// Basic operations on algorithm
#include <algorithm>
#include <iostream>
#include <utility>
#include <vector>
#include <numeric>
#include <iterator>

using namespace std;

bool compare(const int a, const int b){
	return a>b;
}

bool compare2(const int a){
	return a == 5;
}

int main(){
	vector<int> v(10, 2);
	for(int i=0;i<v.size();++i){
		v[i] = i+1;
	}
	cout<<"vector = 1,2...10"<<endl;
	int sum = accumulate(v.begin(), v.end(), 0);
	cout<<"sum = "<<sum<<endl;
	cout<<"Find 1 = "<<(find(v.begin(), v.end(), 1)!=v.end())<<endl;			// Invoke operator== in vector elements
	cout<<"Find 11 = "<<(find(v.begin(), v.end(), 11)!=v.end())<<endl;
	vector<int> v2;
	for(int i=0;i<10;++i){
		v2.push_back(i);
	}
	vector<int>::iterator it = v.begin();
	while((it=find_first_of(it, v.end(), v2.begin(), v2.end()))!=v.end()){		// Invoke operator== in vector elements
		cout<<"Find "<<*it<<endl;
		++it;
	}
	cout<<"Fill 1"<<endl;
	fill(v.begin(), v.begin() + (v.end()-v.begin())/2, 1);
	for(vector<int>::iterator it=v.begin();it!=v.end();++it){
		cout<<*it<<" ";
	}
	cout<<endl;
	cout<<"Fill_n 11 3"<<endl;
	fill_n(back_inserter(v), 11, 3);
	for(vector<int>::iterator it=v.begin();it!=v.end();++it){
		cout<<*it<<" ";
	}
	cout<<endl;
	cout<<"Replace 3 with 5"<<endl;
	replace(v.begin(), v.end(), 3, 5);
	for(vector<int>::iterator it=v.begin();it!=v.end();++it){
		cout<<*it<<" ";
	}
	cout<<endl;
	vector<int>::iterator unique_it = unique(v.begin(), v.end());
	cout<<"Delete repeated ele"<<endl;
	v.erase(unique_it, v.end());
	for(vector<int>::iterator it=v.begin();it!=v.end();++it){
		cout<<*it<<" ";
	}
	cout<<endl;
	cout<<"Sort desc"<<endl;
	sort(v.begin(), v.end(), compare);			// Invoke operator< in vector elements
	for(vector<int>::iterator it=v.begin();it!=v.end();++it){
		cout<<*it<<" ";
	}
	cout<<endl;
	cout<<"Count_if"<<endl;
	cout<<"There are "<<count_if(v.begin(), v.end(), compare2)<<" 5"<<endl;
	cout<<"Reverse print"<<endl;
	for(vector<int>::reverse_iterator it=v.rbegin();it!=v.rend();++it){
		cout<<*it<<" ";
	}
	cout<<endl;
	cout<<"Reverse"<<endl;
	reverse(v.begin(), v.end());
	for(vector<int>::iterator it=v.begin();it!=v.end();++it){
		cout<<*it<<" ";
	}
	cout<<endl;
	return 0;
}
