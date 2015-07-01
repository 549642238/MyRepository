/*User 的逻辑处理层[controller]，根据界面的事件触发(Button等)控制事件的执行,基于和服务器的连接处理事件并将处理结果返回给界面
 * Tips:	1)每个用户登录后会有一个和服务器通信的socket,负责与服务器进行信息交换，所以它必须知道User和服务器通信的协议
 * 			2)User.java是逻辑处理模块(可以认为是控制器)，它会根据用户在界面的触发事件进行控制，选择对应的逻辑处理模块执行并将结果返回给界面
 * 			3)User.java也是一个用于记录User信息的数据结构,它的属性可以被包内类共享，方便数据访问
 * Module:	1)modify():修改个人信息
 * 			2)record():查询个人租借记录
 * 			3)search():根据条件查询图书信息
 * 			4)borrow():租借图书，会通过服务器向数据库发送请求，如果租借成功会更新租借信息列表
 * 			5)freshInformation():更新个人信息模块
 * 			6)exit():退出模块，只需要关闭一些与服务器的连接、关闭界面并通知服务器即可
 */
package user;

import java.util.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

import display.Display;
import record.Record;
import book.Book;

public class User implements Serializable{
	private String ipv4 = "localhost";							//服务器IP地址
	private int port = 8899;									//服务器端口号
	private final String pro0 = "OK";							//发送（接收）Server的消息允许协议
	private final String pro00 = "NO";							//发送（接收）Server的消息拒绝协议
	private final String pro1 = "Log";							//通信协议，登录命令
	private final String pro2 = "Exit";							//通信协议，退出命令
	private final String pro3 = "Modify";						//通信协议，修改命令
	private final String pro4 = "Record";						//通信协议，查看租借记录命令
	private final String pro5 = "SearchBook";					//通信协议，查询图书命令
	private final String pro6 = "BorrowBook";					//通信协议，租借图书命令
	private final String pro7 = "FetchInformation";				//通信协议，刷新个人页面命令
	private final String proe = "EOF";							//通信协议，表示信息结束
	private BufferedReader br = null;
	private PrintWriter writer = null;
	private UserGUI face = null;
	private Socket client = null;
	protected String Unumber = null;
	protected String Upasswd = null;
	protected String Uname = null;
	protected String Uschool = null;
	protected String Udepartment = null;
	protected int Uborrow = 0;
	public int toUborrow(){
		return Uborrow;
	}
	public static void main(String[] arg){
		new User("13121175","123",new JFrame("Log"),"localhost",8899);
	}
	public User(String number,String passwd,JFrame frame,String ip,int p){
		/*
		 * 初始化过程会先连接服务器，通过身份、账户、密码验证后生成新的User界面，并将从服务器发来的个人信息填入User数据结构
		 * 如果账户、密码、身份或账户已在线错误发生会受到来自服务器的不同提示
		 */
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
			if(s.equals(pro0)){									//收到服务器登录成功的提示
				Unumber = br.readLine();
				Uname = br.readLine();
				Upasswd = br.readLine();
				Uschool = br.readLine();
				Udepartment = br.readLine();
				Uborrow = new Integer(br.readLine()).intValue();
				frame.dispose();								//登陆成功后登录界面会自动消失
				face = new UserGUI(this);
				JOptionPane.showMessageDialog(null,"Welcome to Xidian library!","Log Success",JOptionPane.CLOSED_OPTION);
				Display.run(face.toFrame(), face, 1000, 600);
			}else{												//账户、密码或已登录错误发生、登录失败
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
			System.out.println("User error0 - Socket Connect failed");
			e.printStackTrace();
		}
	}
	public void modify(User u){							//修改个人信息模块(需要时控制器会调用),将修改后的个人信息发给服务器,服务器会更新数据库
		try{
			writer.write(pro3+"\n");
			writer.write(u.Unumber+"\n");
			writer.write(u.Uname+"\n");
			writer.write(u.Upasswd+"\n");
			writer.flush();
			String result = br.readLine();
			if(result.equals(pro0)){
				JOptionPane.showMessageDialog(null,"Your Information has been Changed successful!","Modify Success",JOptionPane.CLOSED_OPTION);
				Uname = u.Uname;
				Upasswd = u.Upasswd;
				face.fresh(this);
			}else{
				JOptionPane.showMessageDialog(null,"Your Information not Changed!","Modify Failed",JOptionPane.ERROR_MESSAGE);
			}
		}catch(Exception e){
			System.out.println("User error1 - Modify Error");
			e.printStackTrace();
		}
	}
	public ArrayList<Record> record(User u){			//查询租借记录模块(需要时控制器会调用),请求服务器，服务器返回租借记录信息
		try{
			writer.write(pro4+"\n");
			writer.write(u.Unumber+"\n");
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
			System.out.println("User error2 - Record Error");
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<Book> search(String name,String Writer,String type){		//查询图书模块(需要时控制器会调用),请求服务器，服务器返回查询图书信息
		ArrayList<Book> list = new ArrayList<Book>();
		try{
			writer.write(pro5+"\n");
			writer.write(name+"\n");
			writer.write(Writer+"\n");
			writer.write(type+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro0)){
				String bname = new String("");
				while(!(bname = br.readLine()).equals(proe)){
					list.add(new Book(bname,br.readLine(),br.readLine(),br.readLine(),br.readLine(),new Integer(br.readLine()).intValue()));
				}
			}
			return list;
		}catch(Exception e){
			System.out.println("User error3 - Search Error");
			e.printStackTrace();
		}
		return null;
	}
	public boolean borrow(String ISBN,String name){								//租借图书模块(需要时控制器会调用),向服务器发出借书请求，服务器调用数据库根据返回结果决定借书结果
		try{
			writer.write(pro6+"\n");
			writer.write(Unumber+"\n");
			writer.write(ISBN+"\n");
			writer.write(name+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro0)){
				Uborrow++;
				face.fresh(this);
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("User error4 - Borrow Error");
			e.printStackTrace();
		}
		return false;
	}
	public void freshInformation(){									//刷新个人信息模块(需要时控制器会调用),请求服务器后服务器会返回最新数据库存放的个人信息			
		try{
			writer.write(pro7+"\n");
			writer.write(Unumber+"\n");
			writer.flush();
			Unumber = br.readLine();
			Uname = br.readLine();
			Upasswd = br.readLine();
			Uschool = br.readLine();
			Udepartment = br.readLine();
			Uborrow = new Integer(br.readLine()).intValue();
			face.fresh(this);
		}catch(Exception e){
			System.out.println("User error5 - FreshInformation Error");
			e.printStackTrace();
		}
	}
	public void exit(){												//User退出
		try{
			writer.write(pro2+"\n");
			writer.write(Unumber+"\n");
			writer.flush();
			br.readLine();
			br.close();
			writer.close();
			client.close();
			JOptionPane.showMessageDialog(null,"Goodbye!","Exit",JOptionPane.CLOSED_OPTION);
			face.toFrame().dispose();
			System.exit(0);
		}catch(Exception e){
			System.out.println("User error6 - Exit Error");
			e.printStackTrace();
		}
	}
}