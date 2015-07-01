/*Administrator 的逻辑处理层[controller]，根据界面的事件触发(Button等)控制事件的执行,基于和服务器的连接处理事件并将处理结果返回给界面
 * Tips:	1)每个管理员登录后会有一个和服务器通信的socket,负责与服务器进行信息交换，所以它必须知道Admin和服务器通信的协议
 * 			2)Administrator.java是逻辑处理模块(可以认为是控制器)，它会根据管理员在界面的触发事件进行控制，选择对应的逻辑处理模块执行并将结果返回给界面
 * 			3)Administrator.java也是一个用于记录Administrator信息的数据结构,它的属性可以被包内类共享，方便数据访问
 * Module:	1)searchBook():查询书籍
 * 			2)insertBook():添加书籍
 * 			3)updateBook():更新书籍
 * 			4)deleteBook():删除书籍
 * 			5)searchUser():查询用户
 * 			6)insertUser():添加用户
 * 			7)updateUser():更新用户
 * 			8)deleteUser():删除用户
 * 			9)selectUserRecord():查询用户租借记录
 * 			10)deleteRecord():删除用户租借记录
 * 			11)back():归还图书
 * 			12)exit():退出模块，只需要关闭一些与服务器的连接、关闭界面并通知服务器即可
 */
package administrator;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import record.Record;
import book.Book;
import display.Display;

public class Administrator implements Serializable{
	protected String Anumber = null;
    protected String Apasswd = null;
    protected String Aname = null;
    private AdministratorGUI face = null;
    private String ipv4 = "localhost";							//服务器IP地址
	private int port = 8899;									//服务器端口号
	private final String pro0 = "OK";							//发送（接收）Server的消息允许协议
	private final String pro00 = "NO";							//发送（接收）Server的消息拒绝协议
	private final String pro1 = "LogA";							//通信协议，登录命令
	private final String pro2 = "Exit";							//通信协议，退出命令
	private final String pro3 = "AdminSearchBook";				//通信协议，搜索图书命令
	private final String pro4 = "InsertBook";					//通信协议，添加图书命令
	private final String pro5 = "ModifyBook";					//通信协议，修改图书命令
	private final String pro6 = "DeleteBook";					//通信协议，删除图书命令
	private final String pro7 = "AdminSearchUser";				//通信协议，搜索用户命令
	private final String pro8 = "InsertUser";					//通信协议，添加用户命令
	private final String pro9 = "ModifyUser";					//通信协议，修改用户命令
	private final String pro10 = "DeleteUser";					//通信协议，删除用户命令
	private final String pro11 = "BackBook";					//通信协议，归还图书命令
	private final String pro12 = "SelectUserRecord";			//通信协议，查询用户租借记录命令
	private final String pro13 = "DeleteUserRecord";			//通信协议，删除用户租借记录命令
	private final String proe = "EOF";							//通信协议，表示信息结束
	private BufferedReader br = null;
	private PrintWriter writer = null;
	private Socket client = null;
    public Administrator(String number,String passwd,JFrame frame,String ip,int p){
    	/*
		 * 初始化过程会先连接服务器，通过身份、账户、密码验证后生成新的Administrator界面，并将从服务器发来的个人信息填入Administrator数据结构
		 * 如果账户、密码、身份或账户已在线错误发生会受到来自服务器的不同提示
		 */
    	Anumber = number;
    	Apasswd = passwd;
    	try{
    		ipv4 = ip;
    		port = p;
    		client = new Socket(ipv4,port);
    		br = new BufferedReader(new InputStreamReader(client.getInputStream()));
    		writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
    		writer.write(pro1+"\n");
    		writer.write(number+"\n");
    		writer.write(passwd+"\n");
    		writer.flush();
    		String s = br.readLine();
    		if(s.equals(pro0)){
    			Anumber = br.readLine();
    			Aname = br.readLine();
    			Apasswd = br.readLine();
    			frame.dispose();
    			face = new AdministratorGUI(this);
    			JOptionPane.showMessageDialog(null,"Welcome to Xidian library!","Log Success",JOptionPane.CLOSED_OPTION);
    			Display.run(face.toFrame(), face, 500, 500);
    		}else{
    			if(s.equals("E1")){
					JOptionPane.showMessageDialog(null,"Your accout not exists!","Log failed",JOptionPane.ERROR_MESSAGE);
				}else if(s.equals("E2")){
					JOptionPane.showMessageDialog(null,"Your password is wrong!","Log failed",JOptionPane.ERROR_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(null,"Your account has been online or the server is full!","Log failed",JOptionPane.ERROR_MESSAGE);
				}
				br.close();
				writer.close();
				client.close();
    		}
    	}catch(Exception e){
    		System.out.println("Administrator error0 -> Socket Connect failed");
    	}
    }
    public Book searchBook(String ISBN){
    	try{
			writer.write(pro3+"\n");
			writer.write(ISBN+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return null;									//数据库中查无此书
			}else{
				return new Book(br.readLine(),ISBN,br.readLine(),br.readLine(),br.readLine(),new Integer(br.readLine()).intValue());
			}
		}catch(Exception e){
			System.out.println("Administrator error1 -> searchBook failed");
			e.printStackTrace();
		}
    	return null;
    }
    public boolean insertBook(String ISBN,String Name,String Class,String Writer,String Publisher,String Quantity){
    	try{
    		int q = new Integer(Quantity).intValue();
    		if(ISBN.equals("") || Name.equals("") || Writer.equals("") || Publisher.equals("") || !(q>=1 && q<=100)){
    			return false;
    		}
			writer.write(pro4+"\n");
			writer.write(ISBN+"\n");
			writer.write(Name+"\n");
			writer.write(Class+"\n");
			writer.write(Writer+"\n");
			writer.write(Publisher+"\n");
			writer.write(q+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//数据库添加失败，很可能编号ISBN已存在
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error2 -> insertBook failed");
			e.printStackTrace();
		}
    	return false;
    }
    public boolean updateBook(String ISBN,String Name,String Class,String Writer,String Publisher,String Quantity){
    	try{
    		int q = new Integer(Quantity).intValue();
    		if(Name.equals("") || Writer.equals("") || Publisher.equals("") || !(q>=1 && q<=100)){
    			return false;
    		}
			writer.write(pro5+"\n");
			writer.write(ISBN+"\n");
			writer.write(Name+"\n");
			writer.write(Class+"\n");
			writer.write(Writer+"\n");
			writer.write(Publisher+"\n");
			writer.write(q+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//数据库修改失败，很可能数据库发生严重错误
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error3 -> updateBook failed");
			e.printStackTrace();
		}
    	return false;
    }
    public boolean deleteBook(String ISBN){
    	if(ISBN.equals("")){
    		return false;
    	}
    	try{
			writer.write(pro6+"\n");
			writer.write(ISBN+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//数据库修改失败，很可能数据库没有该书
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error4 -> deleteBook failed");
			e.printStackTrace();
		}
    	return false;
    }
    public UserDataStructure searchUser(String Number){
    	try{
			writer.write(pro7+"\n");
			writer.write(Number+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return null;									//数据库中查无此书
			}else{
				return new UserDataStructure(Number,br.readLine(),br.readLine(),br.readLine(),br.readLine(),new Integer(br.readLine()).intValue());
			}
		}catch(Exception e){
			System.out.println("Administrator error5 -> searchUser failed");
			e.printStackTrace();
		}
    	return null;
    }
    public boolean insertUser(String Number,String Name,String Passwd,String School,String Department,String Borrow){
    	try{
    		int q = new Integer(Borrow).intValue();
    		if(Number.equals("") || Name.equals("") || Passwd.equals("") || School.equals("") || !(q>=0 && q<=3)){
    			return false;
    		}
			writer.write(pro8+"\n");
			writer.write(Number+"\n");
			writer.write(Passwd+"\n");
			writer.write(Name+"\n");
			writer.write(School+"\n");
			writer.write(Department+"\n");
			writer.write(q+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//数据库添加失败，很可能编号Number已存在
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error6 -> insertUser failed");
			e.printStackTrace();
		}
    	return false;
    }
    public boolean updateUser(String Number,String School,String Department,String Borrow){
    	try{
    		int q = new Integer(Borrow).intValue();
    		if(Number.equals("") || School.equals("") || Department.equals("") || !(q>=0 && q<=3)){
    			return false;
    		}
			writer.write(pro9+"\n");
			writer.write(Number+"\n");
			writer.write(School+"\n");
			writer.write(Department+"\n");
			writer.write(q+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//数据库修改失败，很可能数据库发生严重错误
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error7 -> updateUser failed");
			e.printStackTrace();
		}
    	return false;
    }
    public boolean deleteUser(String Number){
    	if(Number.equals("")){
    		return false;
    	}
    	try{
			writer.write(pro10+"\n");
			writer.write(Number+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//数据库删除失败，很可能数据库没有该用户
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error8 -> deleteUser failed");
			e.printStackTrace();
		}
    	return false;
    }
    public ArrayList<Record> selectUserRecord(UserDataStructure ud){			//查询租借记录模块(需要时控制器会调用),请求服务器，服务器返回租借记录信息
		try{
			writer.write(pro12+"\n");
			writer.write(ud.Unumber+"\n");
			writer.flush();
			String s = br.readLine();
			ArrayList<Record> list = new ArrayList<Record>();
			if(s.equals(pro0)){
				String name = new String("");
				while(!(name = br.readLine()).equals(proe)){
					list.add(new Record(name,br.readLine(),br.readLine(),new Integer(br.readLine()).intValue()));
				}
			}
			return list;
		}catch(Exception e){
			System.out.println("Administrator error9 -> selectUserRecord failed");
			e.printStackTrace();
		}
		return null;
	}
    public boolean deleteRecord(String number,String date){
    	try{
			writer.write(pro13+"\n");
			writer.write(number+"\n");
			writer.write(date+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro0)){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("Administrator error10 -> selectUserRecord failed");
			e.printStackTrace();
		}
    	return false;
    }
    public boolean back(String ISBN,String Number){
    	if(ISBN.equals("") || Number.equals("")){
    		return false;
    	}
    	try{
			writer.write(pro11+"\n");
			writer.write(Number+"\n");
			writer.write(ISBN+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//数据库查询失败，很可能数据库没有该书或用户
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error11 -> Back book failed");
			e.printStackTrace();
		}
    	return false;
    }
    public void exit(){
    	try{
			writer.write(pro2+"\n");
			writer.write(Anumber+"\n");
			writer.flush();
			br.readLine();
			br.close();
			writer.close();
			client.close();
			JOptionPane.showMessageDialog(null,"Goodbye!","Exit",JOptionPane.CLOSED_OPTION);
			face.toFrame().dispose();
			System.exit(0);
		}catch(Exception e){
			System.out.println("Administrator error12 -> Exit Error");
			e.printStackTrace();
		}
    }
}