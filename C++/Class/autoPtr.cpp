// Realization of smart pointer
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
		if(--(cp->use) == 0){
			delete cp;
		}
		cp = ptr.cp;
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
	Ptr<int> pa(new int(4));
	Ptr<int> pb = Ptr<int>(new int(2));
	pb = pa;
	cout<<"------------"<<endl;
	return 0;
}
