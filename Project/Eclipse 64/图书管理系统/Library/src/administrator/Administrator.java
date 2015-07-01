//管理员的数据结构
package administrator;

import java.io.Serializable;

public class Administrator implements Serializable{
	public String Anumber = null;
    public String Apasswd = null;
    public String Aname = null;
    public Administrator(String number,String passwd,String name){
    	Anumber = number;
    	Apasswd = passwd;
    	Aname = name;
    }
}