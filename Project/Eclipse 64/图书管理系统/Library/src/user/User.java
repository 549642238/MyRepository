//用户的数据结构
package user;

import java.io.Serializable;

public class User implements Serializable{
	public String Unumber = null;
	public String Upasswd = null;
	public String Uname = null;
	public String Uschool = null;
	public String Udepartment = null;
	public int Uborrow = 0;
	public User(String number,String passwd,String name,String school,String department,int borrow){
		Unumber = number;
		Upasswd = passwd;
		Uname = name;
		Uschool = school;
		Udepartment = department;
		Uborrow = borrow;
	}
}