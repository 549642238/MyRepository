#include <iostream>
using namespace std;

template<typename T>
class Ptr{
private:
	T* data;
	int* count;
public:
	Ptr(T* t);
	Ptr(const Ptr&);
	Ptr<T>& operator=(const Ptr&);
	T* operator->();
	~Ptr();
};

template<typename T>
Ptr<T>::Ptr(T* t):data(t), count(new int(1)){
}
template<typename T>
Ptr<T>::Ptr(const Ptr& ptr):data(ptr.data), count(ptr.count){
	++(*count);
}
template<typename T>
Ptr<T>& Ptr<T>::operator=(const Ptr& ptr){
	if(--(*count) == 0){
		delete count;
		delete data;
	}
	data = ptr.data;
	count = ptr.count;
	++(*count);
	return *this;
}
template<typename T>
T* Ptr<T>::operator->(){
	return data;
}
template<typename T>
Ptr<T>::~Ptr(){
	if(--(*count) == 0){
		delete count;
		delete data;
	}
}

int main(){
	Ptr<int> pa(new int(5));
	Ptr<int> pb = Ptr<int>(new int(4));
	pa = pb;
	cout<<"---------------"<<endl;
	int* same = new int(3);
	Ptr<int> p1(same);
	Ptr<int> p2(same);				// 对于shared_ptr来讲，这样调用也是错误的。智能指针的一般初始化方式最好是smart_ptr<T> p(new T);
	return 0;
}
