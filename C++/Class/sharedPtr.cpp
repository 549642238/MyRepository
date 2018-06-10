// Implementation of smart shared pointer
#include <iostream>
using namespace std;

template<class T>
class Ptr;

template<class T>
class CPtr{
private:
	friend class Ptr<T>;
	T* ptr;
	int use;
	CPtr(T* p):ptr(p), use(1){
		cout<<"Construct CPtr"<<endl;
	}
	~CPtr(){
		cout<<"Destruct CPtr"<<endl;
		delete ptr;
	}
};

template<class T>
class Ptr{
private:
	CPtr<T>* cp;
public:
	explicit Ptr(T* t):cp(new CPtr<T>(t)){
		cout<<"Construct Ptr(T*)"<<endl;
	}
	Ptr(const Ptr& ptr):cp(ptr.cp){
		++((ptr.cp)->use);
		cout<<"Construct Ptr(Ptr)"<<endl;
	}
	Ptr& operator=(const Ptr& ptr){
		if(ptr.cp != cp){
			if(--(cp->use) == 0){
				delete cp;
			}
			cp = ptr.cp;
		}
		++((ptr.cp)->use);
	}
	~Ptr(){
		if(--(cp->use) == 0){
			delete cp;
		}
		cout<<"Destruct Ptr"<<endl;
	}
};

int main(){
	/*
	Ptr<int> pa(new int(4));
	Ptr<int> pb = Ptr<int>(new int(2));
	pb = pa;
	cout<<"------------"<<endl;
	*/
	int* same = new int(3);
	Ptr<int> p1(same);
	Ptr<int> p2(same);			// 对于shared_ptr来讲，这样调用也是错误的。智能指针的一般初始化方式最好是smart_ptr<T> p(new T);
	return 0;
}
