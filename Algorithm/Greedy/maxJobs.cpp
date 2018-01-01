// 有n项工作，每项工作分别在Si时间开始，然后在Ti时间结束。对于每项工作，你都可以选择参与或者不参与。如果你选择了参与，那么你必须自始至终都全程参与。此外，参与的时间段不能重叠。（即使是开始的瞬间和结束的瞬间的重叠也是不允许的）时间不限，你的目的是参与尽可能多的工作，那么最多能参与多少项工作呢？
#include <iostream>
#include <algorithm>
using namespace std;

struct Job{
	unsigned int start, end;
};

bool compare(Job a, Job b){
	if(a.end == b.end){
		return a.start>b.start;
	}else{
		return a.end<b.end;
	}
}

int main(){
	unsigned int N;
	cin>>N;
	Job* jobs = new Job[N];
	for(int i=0;i<N;i++){
		cin>>jobs[i].start;
	}
	for(int i=0;i<N;i++){
		cin>>jobs[i].end;
	}
	sort(jobs, jobs+N, compare);
	unsigned int work = 0;
	unsigned int end = 0;
	for(int i=0;i<N;i++){
		if(jobs[i].start >= end){
			work ++;
			end = jobs[i].end;
		}
	}
	cout<<work<<endl;
	delete[] jobs;
	return 0;
}
