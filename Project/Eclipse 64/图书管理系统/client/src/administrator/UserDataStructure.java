//一个与User数据属性相同的数据结构(相当于镜像),不直接用User是因为如果调用new User()会生成一个真实的User(包括界面),而有时候不必这么做(只需要借用一下数据结构就好)
package administrator;

import java.io.Serializable;

public class UserDataStructure implements Serializable{
	protected String Unumber = null;
	protected String Upasswd = null;
	protected String Uname = null;
	protected String Uschool = null;
	protected String Udepartment = null;
	protected int Uborrow = 0;
	public UserDataStructure(String number,String passwd,String name,String school,String department,int b){
		Unumber = number;
		Upasswd = passwd;
		Uname = name;
		Uschool = school;
		Udepartment = department;
		Uborrow = b;
	}
}
