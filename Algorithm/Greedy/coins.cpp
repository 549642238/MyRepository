// 有1元，5元，10元，50元，100元，500元的硬币各C1，C5，C10，C50，C100，C500枚，现在要用这些硬币来支付A元，最少需要多少枚硬币？ 硬币的数目小于10^9，需要支付的钱数小于10^9
#include <iostream>
using namespace std;

unsigned int tryIt(unsigned int& A, unsigned int current, unsigned int C){
	unsigned number = A/current;
	if(number > C){
		number = C;
	}
	A -= number*current;
	return number;
}

int main(){
	unsigned int A;
	unsigned int C1, C5, C10, C50, C100, C500;
	cin>>A>>C1>>C5>>C10>>C50>>C100>>C500;
	C500 = tryIt(A, 500, C500);
	C100 = tryIt(A, 100, C100);
	C50 = tryIt(A, 50, C50);
	C10 = tryIt(A, 10, C10);
	C5 = tryIt(A, 5, C5);
	C1 = tryIt(A, 1, C1);
	cout<<C500<<" "<<C100<<" "<<C50<<" "<<C10<<" "<<C5<<" "<<C1<<endl;
	return 0;
}
