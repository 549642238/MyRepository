/** Main function:connect to mySQL,select and update data.
 *Store Client Pane's information,if myQQ Pane needs return these messages*/
package myqq.connect;

import java.sql.*;

public class Connect{
	private static Connection con=null;
	private static ResultSet rs=null; 
	private static Statement statement=null;
	private static String number=null;
    private static String passwd=null;
    private static String name = null;
    private static String sex = null;
    private static String telephone = null;
    public static String toNumber(){
    	return number;
    }
    public static String toPasswd(){
    	return passwd;
    }
    public static String toName(){
    	return name;
    }
    public static String toSex(){
    	return sex;
    }
    public static String toTele(){
    	return telephone;
    }
	public static Connection connect(){
		number = passwd = name = sex =telephone = null;
		String Driver="com.mysql.jdbc.Driver";      
	   	String URL="jdbc:mysql://localhost:3306/MyQQ";     
	    String Username="root";     
	   	String Password="12345";    
	   	try{
	   		Class.forName(Driver).newInstance();  
	    	con=DriverManager.getConnection(URL,Username,Password);
	   	}catch(Exception e){
	   		System.out.println("Connect error0");
	   	}
	   	return con;
	}
	public static void select(String no){
		try{
			statement = con.createStatement();
        	String sql = "select * from people where number="+no;	
        	rs = statement.executeQuery(sql);
		}catch(Exception e){
			System.out.println("Connect error1");
		}
        try{
        	if(rs.next()){
        		number=no;
        		passwd = rs.getString("passwd");
            	name = rs.getString("name");
            	sex=rs.getString("sex");
            	telephone=rs.getString("telephone");
        	}else{
        		System.out.println("Connect error2-You can ignore that");
        	}
        }catch(Exception e){
        	System.out.println("Connect error3");
        }
        try{
        	rs.close();
        }catch(Exception e){
        	System.out.println("rs close error");
        }
	}
	public static void update(String no,String x,String r){
		String sql="update people set "+x+"="+r+" where number="+no;
		try{
			statement = con.createStatement();
			statement.execute(sql);
		}catch(Exception e){
			System.out.println("Connect error4");
		}
	}
	public static void insert(String no,String p,String n,String s,String t){
		String sql="insert into people(number,passwd,name,sex,telephone) values('"+no+"','"+p+"','"+n+"','"+s+"','"+t+"')";
		try{
			statement = con.createStatement();
			statement.execute(sql);
		}catch(Exception e){
			System.out.println("Connect error5");
		}
	}
	public static void delete(String no){
		String sql="delete from people where number="+no;
		try{
			statement = con.createStatement();
			statement.execute(sql);
		}catch(Exception e){
			System.out.println("Connect error6");
		}
	}
	public static void disConnect(){
		Connection con=null;
		rs=null; 
		statement=null;
		number=null;
    	passwd=null;
    	name = null;
    	sex = null;
    	telephone = null;
		try{
			con.close();
		}catch(Exception e){
			System.out.println("Connect error7");
		}	
	}
	public static void main(String[] arg){
		connect();
		select("13121175");
	//	update("13121175","name","dad");
	}
}