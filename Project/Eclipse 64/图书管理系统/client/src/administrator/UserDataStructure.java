//һ����User����������ͬ�����ݽṹ(�൱�ھ���),��ֱ����User����Ϊ�������new User()������һ����ʵ��User(��������),����ʱ�򲻱���ô��(ֻ��Ҫ����һ�����ݽṹ�ͺ�)
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
