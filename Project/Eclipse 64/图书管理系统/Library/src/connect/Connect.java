/*数据库核心程序,整个图书管理系统的最底层数据操作
 *  Main function:connect to mySQL,select、update and delete data.
 *  通过编辑数据库功能可以扩展、修改服务器的功能，进而给用户、管理员不同的功能体验
 */

package connect;

import java.sql.*;
import java.util.*;
import java.util.Date;
import administrator.Administrator;
import book.Book;
import user.User;
import record.Record;

public class Connect{
	private static final int MAX = 3;					//租借图书最大数目
	private Connection con = null;
	private ResultSet rs = null; 
	private Statement statement = null;
    private User user = null;
    private ArrayList<Book> book = new ArrayList<Book>();
    private ArrayList<Record> record = new ArrayList<Record>();
    private Administrator admin = null;
    private final String ipv4 = "localhost";		//database ip
    public ArrayList<Book> toBook(){
    	return book;
    }
    public ArrayList<Record> toRecord(){
    	return record;
    }
    public User toUser(){
    	return user;
    }
    public Administrator toAdmin(){
    	return admin;
    }
	public void connect(){
		String Driver="com.mysql.jdbc.Driver";      
	   	String URL="jdbc:mysql://"+ipv4+":3306/library";     
	    String Username="root";     
	   	String Password="12345";  
	   	try{
	   		Class.forName(Driver).newInstance();  
	    	con=DriverManager.getConnection(URL,Username,Password);
	    	statement = con.createStatement();
	   	}catch(Exception e){
	   		e.printStackTrace();
	   		System.out.println("Connect error0 - Connect Database failed!");
	   	}
	}
	public boolean selectUser(String no){								//用户登录需要判断
		try{
        	String sql = "select * from user where number='"+no+"'";	
        	rs = statement.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error1 - selectUser failed(1)");
		}
        try{
        	if(rs.next()){
        		user = new User(rs.getString("number"),rs.getString("password"),rs.getString("name"),rs.getString("school"),rs.getString("department"),rs.getInt("borrow"));
        		return true;
        	}else{
        		System.out.println("Unrecognized client(use User ID) try to enter into library");
        	}
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("Connect error1 - selectUser failed(2)");
        }
        return false;
	}
	public boolean selectAdmin(String no){								//管理员登录需要判断
		try{
        	String sql = "select * from administrator where number='"+no+"'";	
        	rs = statement.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error2 - selectAdmin failed(1)");
		}
        try{
        	if(rs.next()){
        		admin = new Administrator(rs.getString("number"),rs.getString("password"),rs.getString("name"));
        		return true;
        	}else{
        		System.out.println("Unrecognized client(use Admin ID) try to enter into library");
        	}
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("Connect error2 - selectAdmin failed(2)");
        }
        return false;
	}
	public boolean selectBook_accordingISBN(String ISBN){				//查询书根据书ISBN，管理员会调用
		try{
        	String sql = "select * from book where ISBN = '"+ISBN+"'";	
        	rs = statement.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error3 - selectBook_accordingName failed(1)");
		}
        try{
        	book.clear();
        	if(rs.next()){
        		book.add(new Book(rs.getString("name"),rs.getString("ISBN"),rs.getString("class"),rs.getString("writer"),rs.getString("publisher"),rs.getInt("quantity")));
        		return true;
        	}else{
        		return false;
        	}
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("Connect error3 - selectBook_accordingName failed(2)");
        }
        return false;
	}
	public boolean selectBook_accordingName(String name){				//查询书根据书名，模糊搜索，只要包含输入关键字的都被认为候选项
		try{
        	String sql = "select * from book where name like '%"+name+"%'";	
        	rs = statement.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error4 - selectBook_accordingName failed(1)");
		}
        try{
        	boolean b = false;
        	book.clear();
        	while(rs.next()){
        		b = true;
        		book.add(new Book(rs.getString("name"),rs.getString("ISBN"),rs.getString("class"),rs.getString("writer"),rs.getString("publisher"),rs.getInt("quantity")));
        	}
        	return b;
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("Connect error4 - selectBook_accordingName failed(2)");
        }
        return false;
	}
	public boolean selectBook_accordingClass(String Class){				//查询书根据类型
		try{
        	String sql = "select * from book where class='"+Class+"'";	
        	rs = statement.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error5 - selectBook_accordingClass failed(1)");
		}
        try{
        	boolean b = false;
        	book.clear();
        	while(rs.next()){
        		b = true;
        		book.add(new Book(rs.getString("name"),rs.getString("ISBN"),rs.getString("class"),rs.getString("writer"),rs.getString("publisher"),rs.getInt("quantity")));
        	}
        	return b;
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("Connect error5 - selectBook_accordingClass failed(2)");
        }
        return false;
	}
	public boolean selectBook_accordingWriter(String writer){				//查询书根据作者，模糊搜索，只要包含输入关键字的都被认为候选项
		try{
			String sql = "select * from book where writer like '%"+writer+"%'";
        	rs = statement.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error6 - selectBook_accordingWriter failed(1)");
		}
        try{
        	boolean b = false;
        	book.clear();
        	while(rs.next()){
        		b = true;
        		book.add(new Book(rs.getString("name"),rs.getString("ISBN"),rs.getString("class"),rs.getString("writer"),rs.getString("publisher"),rs.getInt("quantity")));
        	}
        	return b;
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("Connect error6 - selectBook_accordingWriter failed(2)");
        }
        return false;
	}
	public boolean selectBook_accordingNameandWriter(String name,String writer){				//查询书根据名字、作者，模糊搜索，只要包含输入关键字的都被认为候选项
		try{
			String sql = "select * from book where writer like '%"+writer+"%' and name like '%"+name+"%'";
        	rs = statement.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error7 - selectBook_accordingNameandWriter failed(1)");
		}
        try{
        	boolean b = false;
        	book.clear();
        	while(rs.next()){
        		b = true;
        		book.add(new Book(rs.getString("name"),rs.getString("ISBN"),rs.getString("class"),rs.getString("writer"),rs.getString("publisher"),rs.getInt("quantity")));
        	}
        	return b;
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("Connect error7 - selectBook_accordingNameandWriter failed(2)");
        }
        return false;
	}
	public boolean selectBook_accordingNameandClass(String name,String Class){				//查询书根据名字、类型，模糊搜索，只要包含输入关键字的都被认为候选项
		try{
			String sql = "select * from book where class = '"+Class+"' and name like '%"+name+"%'";
        	rs = statement.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error8 - selectBook_accordingNameandClass failed(1)");
		}
        try{
        	boolean b = false;
        	book.clear();
        	while(rs.next()){
        		b = true;
        		book.add(new Book(rs.getString("name"),rs.getString("ISBN"),rs.getString("class"),rs.getString("writer"),rs.getString("publisher"),rs.getInt("quantity")));
        	}
        	return b;
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("Connect error8 - selectBook_accordingNameandClass failed(2)");
        }
        return false;
	}
	public boolean selectBook_accordingWriterandClass(String writer,String Class){				//查询书根据作者、类型，模糊搜索，只要包含输入关键字的都被认为候选项
		try{
			String sql = "select * from book where writer like '%"+writer+"%' and class = '"+Class+"'";
        	rs = statement.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error9 - selectBook_accordingWriterandClass failed(1)");
		}
        try{
        	boolean b = false;
        	book.clear();
        	while(rs.next()){
        		b = true;
        		book.add(new Book(rs.getString("name"),rs.getString("ISBN"),rs.getString("class"),rs.getString("writer"),rs.getString("publisher"),rs.getInt("quantity")));
        	}
        	return b;
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("Connect error9 - selectBook_accordingWriterandClass failed(2)");
        }
        return false;
	}
	public boolean selectBook_accordingNameWriterandClass(String name,String writer,String Class){				//查询书根据名字、作者、类型，模糊搜索，只要包含输入关键字的都被认为候选项
		try{
			String sql = "select * from book where name like '%"+name+"%' and writer like '%"+writer+"%' and class ='"+Class+"'";
        	rs = statement.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error10 - selectBook_accordingNameWriterandClass failed(1)");
		}
        try{
        	boolean b = false;
        	book.clear();
        	while(rs.next()){
        		b = true;
        		book.add(new Book(rs.getString("name"),rs.getString("ISBN"),rs.getString("class"),rs.getString("writer"),rs.getString("publisher"),rs.getInt("quantity")));
        	}
        	return b;
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("Connect error10 - selectBook_accordingNameWriterandClass failed(2)");
        }
        return false;
	}
	public boolean selectRecord(String number){
		try{
			String sql = "select * from record"+number;
        	rs = statement.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error11 - selectRecord failed(1)");
		}
        try{
        	boolean b = false;
        	record.clear();
        	while(rs.next()){
        		b = true;
        		record.add(new Record(rs.getString("name"),rs.getString("ISBN"),rs.getString("date"),rs.getInt("state")));
        	}
        	if(record.size() >= 90){										//每个User的借书记录会自动清除，每当记录条数达到90时就会清除
        		Iterator I = record.iterator();
        		while(I.hasNext() && ((Record)I.next()).state==0){
        			I.remove();
        		}
        	}
        	return b;
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("Connect error11 - selectRecord failed(2)");
        }
        return false;
	}
	public boolean updateBook(String ISBN,String name,String Class,String writer,String publisher,int quantity){
		return exeUpdateBook(ISBN,name,Class,writer,publisher,quantity,rs,statement);
	}
	public synchronized static boolean exeUpdateBook(String ISBN,String name,String Class,String writer,String publisher,int quantity,ResultSet rrs,Statement st){				//管理员负责修改书籍信息,修改操作必须保证原子性
		String sql="update book set name = '"+name+"',class = '"+Class+"',writer = '"+writer+"',publisher = '"+publisher+"',quantity = "+quantity+" where ISBN = '"+ISBN+"'";
		String sql1="select * from book where ISBN='"+ISBN+"'";
		try{
			rrs = st.executeQuery(sql1);
			if(!(rrs.next())){
				return false;
			}
			st.execute(sql);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error12 - updateBook failed -> You may ingonre that,it probably caused by client's illegal operations");
			return false;
		}
	}
	public boolean updateUser(String number,String name,String passwd){
		return exeUpdateUser(number,name,passwd,rs,statement);
	}
	public static boolean exeUpdateUser(String number,String name,String passwd,ResultSet rrs,Statement st){						//用户可以修改自己的名字密码,原子操作，防止管理员同时修改造成信息混乱
		String sql="update user set name = '"+name+"',password = '"+passwd+ "' where number = '"+number+"'";
		String sql1="select * from user where number='"+number+"'";
		try{
			rrs = st.executeQuery(sql1);
			if(!(rrs.next())){
				return false;
			}
			st.execute(sql);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error13 - updateUser failed -> You may ingonre that,it probably caused by client's illegal operations");
			return false;
		}
	}
	public boolean updateUserFromAdmin(String number,String school,String department,int borrow){
		return exeUpdateUserFromAdmin(number,school,department,borrow,rs,statement);
	}
	public static boolean exeUpdateUserFromAdmin(String number,String school,String department,int borrow,ResultSet rrs,Statement st){				//管理员可以修改用户的学校、学院，必须保证原子操作
		String sql="update user set school = '"+school+"',department = '"+department+"',borrow = '"+borrow+ "' where number = '"+number+"'";
		String sql1="select * from user where number='"+number+"'";
		try{
			rrs = st.executeQuery(sql1);
			if(!(rrs.next())){
				return false;
			}
			st.execute(sql);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error14 - updateUserFromAdmin failed -> You may ingonre that,it probably caused by client's illegal operations");
			return false;
		}
	}
	public boolean insertBook(String ISBN,String name,String Class,String writer,String publisher,int quantity){
		return exeInsertBook(ISBN,name,Class,writer,publisher,quantity,rs,statement);
	}
	public static boolean exeInsertBook(String ISBN,String name,String Class,String writer,String publisher,int quantity,ResultSet rrs,Statement st){			//管理员可以添加书籍，保证原子操作，防止两个管理员同时加入同一本书
		String sql="insert into book(Name,ISBN,Class,Writer,Publisher,quantity) values('"+name+"','"+ISBN+"','"+Class+"','"+writer+"','"+publisher+"',"+quantity+")";
		String sql1="select * from book where ISBN='"+ISBN+"'";
		try{
			rrs = st.executeQuery(sql1);
			if(rrs.next()){
				return false;
			}
			st.execute(sql);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error15 - insertBook failed -> You may ingonre that,it probably caused by client's illegal operations");
			return false;
		}
	}
	public boolean insertUser(String number,String passwd,String name,String school,String department,int borrow){
		return exeInsertUser(number,passwd,name,school,department,borrow,rs,statement);
	}
	public static boolean exeInsertUser(String number,String passwd,String name,String school,String department,int borrow,ResultSet rrs,Statement st){			//管理员可以添加用户，保证原子操作，防止两个管理员同时加入同一个用户
		String sql = "insert into user(number,password,name,school,department,borrow) values('"+number+"','"+passwd+"','"+name+"','"+school+"','"+department+"',"+borrow+")";
		String sqla = "create table record"+number+"(name varchar(30),ISBN varchar(10),date varchar(30) primary key,state int(1))"; 
		String sql1="select * from user where number='"+number+"'";
		try{
			rrs = st.executeQuery(sql1);
			if(rrs.next()){
				return false;
			}
			st.execute(sql);
			st.execute(sqla);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error16 - insertUser failed -> You may ingonre that,it probably caused by client's illegal operations");
			return false;
		}
	}
	public boolean deleteBook(String ISBN){
		return exeDeleteBook(ISBN,rs,statement);
	}
	public static boolean exeDeleteBook(String ISBN,ResultSet rrs,Statement st){				//管理员可以删除书籍,原子操作，防止两个管理员同时删除同一本书两次
		String sql1="select * from book where ISBN='"+ISBN+"'";
		String sql="delete from book where ISBN='"+ISBN+"'";
		try{
			rrs = st.executeQuery(sql1);
			if(!rrs.next()){
				return false;
			}
			st.execute(sql);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error17 - deleteBook failed -> You may ingonre that,it probably caused by client's illegal operations");
			return false;
		}
	}
	public boolean deleteUser(String number){
		return exeDeleteUser(number,rs,statement);
	}
	public static boolean exeDeleteUser(String number,ResultSet rrs,Statement st){			//管理员可以删除用户,原子操作，防止两个管理员同时删除同一用户两次
		String sql1="select * from user where number='"+number+"'";
		String sql="delete from user where number="+number;
		String sqla = "select* from user where number = "+number;
		String sqlb = "drop table record"+number;
		try{
			rrs = st.executeQuery(sql1);
			if(!(rrs.next())){
				return false;
			}
			rrs = st.executeQuery(sqla);
			if(!rrs.next()){
				return false;
			}else{
				int borrow = rrs.getInt("borrow");
				if(borrow > 0){
					return false;
				}
			}
			st.execute(sqlb);
			st.execute(sql);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error18 - deleteUser failed -> You may ingonre that,it probably caused by client's illegal operations");
			return false;
		}
	}
	public boolean borrow(String number,String ISBN,String name){
		return exeBorrow(number,ISBN,name,rs,statement);
	}
	public static synchronized boolean exeBorrow(String number,String ISBN,String name,ResultSet rrs,Statement st){		//用户借书，原子操作，应对两个用户同时抢一本书的情况
		String sqla = "select * from book where ISBN = '"+ISBN+"'";
		String sqlb = "select * from user where number = '"+number+"'";
		String sqlc = "select * from record"+number+" where ISBN = '"+ISBN+"'";
		try{
			rrs = st.executeQuery(sqla);
			if(!(rrs.next())){
				return false;
			}
			int quantity = rrs.getInt("quantity");
			rrs = st.executeQuery(sqlb);
			if(!(rrs.next())){
				return false;
			}
			int borrow = rrs.getInt("borrow");
			if(quantity==0 || borrow == MAX){
				return false;
			}
			rrs = st.executeQuery(sqlc);
			if(rrs.next()){
				int state = rrs.getInt("state");
				if(state == 1){
					return false;
				}
			}
			quantity--;
			borrow++;
			String sql="update user set borrow = "+borrow+" where number='"+number+"'";
			String sql2="update book set quantity = "+quantity+" where ISBN='"+ISBN+"'";
			String sql3="insert into record"+number+"(name,ISBN,date,state) values('"+name+"','"+ISBN+"','"+new Date().toString()+"',"+1+")";
			st.execute(sql);
			st.execute(sql2);
			st.execute(sql3);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error19 - borrow failed");
			return false;
		}
	}
	public boolean back(String number,String ISBN){
		return exeBack(number,ISBN,rs,statement);
	}
	public static synchronized boolean exeBack(String number,String ISBN,ResultSet rrs,Statement st){			//用户还书,可以是管理员操作也可以是机器自动扫描,原子操作，应对两个用户同时还一本书的情况
		String sqla = "select* from book where ISBN = '"+ISBN+"'";
		String sqlb = "select* from user where number = '"+number+"'";
		String sqlc = "select * from record"+number+" where ISBN = '"+ISBN+"'";
		try{
			rrs = st.executeQuery(sqla);
			if(!rrs.next()){
				return false;
			}
			int quantity = rrs.getInt("quantity");
			rrs = st.executeQuery(sqlb);
			if(!(rrs.next())){
				return false;
			}
			int borrow = rrs.getInt("borrow");
			rrs = st.executeQuery(sqlc);
			if(!(rrs.next())){
				return false;
			}
			int state = rrs.getInt("state");
			if(state == 0){
				return false;
			}
			quantity++;
			borrow--;
			String sql="update user set borrow = "+borrow+" where number='"+number+"'";
			String sql2="update book set quantity = "+quantity+" where ISBN='"+ISBN+"'";
			String sql3="update record"+number+" set state = "+0+" where ISBN='"+ISBN+"'";
			st.execute(sql);
			st.execute(sql2);
			st.execute(sql3);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error20 - back failed");
			return false;
		}
	}
	public boolean deleteRecord(String number,String date){
		return exeDeleteRecord(number,date,rs,statement);
	}
	public static synchronized boolean exeDeleteRecord(String Number,String date,ResultSet rrs,Statement st){		//删除记录,原子操作，应对两个管理员同时删除同一用户的同一记录的情况
		String sqla = "delete from record"+Number+" where date = '"+date+"'";
		try{
			st.execute(sqla);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error21 - deleteRecord failed");
		}
		return false;
	}
	public void disConnect(){								//关闭数据库连接
		try{
			rs.close();
			statement.close();
			con.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connect error22 - disConnect failed -> Probably caused by none database opened,you may ignore that");
		}	
	}
	public static void main(String[] arg){
	/*	Connect con = new Connect();
		con.connect();
		con.selectAdmin("XIdian");
		Administrator a = con.toAdmin();
		System.out.println(a.Anumber+" "+a.Apasswd+" "+a.Aname);
		boolean b =con.back("13121175","MA0031");
		System.out.println(b);
		con.selectUser("13121175");
		User u = con.toUser();
		System.out.println(u.Unumber+" "+u.Uname+" "+u.Upasswd+" "+u.Uschool+" "+u.Udepartment+" "+u.Uborrow);
		con.selectBook_accordingClass("Military");
		ArrayList<Book> list = new ArrayList<Book>();
		list = con.toBook();
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i).Bname+" "+list.get(i).Bwriter+" "+list.get(i).ISBN+" "+list.get(i).Bclass+" "+list.get(i).Bpublisher+" "+list.get(i).Bquantity);
		}
		con.disConnect();*/
	}
}