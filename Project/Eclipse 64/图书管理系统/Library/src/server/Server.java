/*Server [controller]提供服务端逻辑处理，接收来自client的请求将处理结果返回给client，并且维持服务端界面运行
 * @author 			czh
 * @complete_time	2015/6/4
 * @take_time		4days
 * @long			1321Lines
 * @Explaination	1)library的服务器,供User、Administrator连接并为其提供数据库等服务
 * 					3)提供多人连接、具有登录人数限制功能
 * 					4)可以访问数据库(User、Administrator只能通过服务器间接访问数据库)
 * 					5)生成服务器界面，维护在线client清单
 * 					6)维护Server界面,可以将用户上下线信息通知显示界面,调用ServerGUI.addClient(),ServerGUI.removeClient()
 * 					7)具有和User、Administrator通信的所有协议
 * @Module			1)监听信道模块：处理来自client的socket请求，建立socket连接，生成新的服务线程
 * 					2)登录模块：每个通信线程要对登录进行判断,调用log()、check()
 * 					3)服务模块：每个通信线程要对来自client的请求通过查询数据库作出响应
 * @start			服务器开始程序
 */
package server;

import java.util.*;
import java.io.*;
import java.net.*;

import connect.Connect;
import display.Display;
import user.User;
import administrator.Administrator;
import book.Book;
import record.Record;

public class Server{
	protected static ServerGUI sg = null;
	protected static int N = 100;							//系统允许同时在线最大人数
	public static void main(String[] arg){
		System.out.println("Xidian Library Server Start:\nStart Time:"+new Date().toString());
		sg = new ServerGUI();
		Display.run(sg.toFrame(), sg, 400, 600);
	}
	private static ArrayList<ThreadServerHandler> l = new ArrayList<ThreadServerHandler> ();		//保留连接到client的线程
	public static ArrayList<ThreadServerHandler> toL(){
		return l;
	}
	public Server(int port,int n){
		N = n;
		ServerSocket server = null;
		try{
			server = new ServerSocket(port);
			label:
			while(true){
				try{
					Socket client = server.accept();
					ThreadServerHandler t=new ThreadServerHandler(client);
				}catch(Exception e){
					System.out.println("Server error0 -> Server start failed(1) -> Probably the port has been used or occupied!");
					e.printStackTrace();
					break;
				}
			}
		}catch(Exception e){
			System.out.println("Server error1 -> Server start failed(2)! -> Unpredictable error happens,please retry again");
			e.printStackTrace();
		}finally{
			l.clear();
			try{
				server.close();
			}catch(Exception e){
				System.out.println("Server error1 -> Server start failed(3)! -> Server close failed");
				e.printStackTrace();
			}
		}
	}
}
class ThreadServerHandler extends Thread implements Runnable{
	private final String P0 = "OK";					//发送给client的消息允许协议
	private final String P00 = "NO";				//发送给client的消息拒绝协议
	private final String P1 = "Log";				//收到来自User的登录请求协议
	private final String P2 = "Exit";				//收到来自client的退出请求协议
	private final String P3 = "Modify";				//收到来自User修改个人信息的协议
	private final String P4 = "Record";				//收到来自User查询租借记录的协议
	private final String P5 = "SearchBook";			//收到来自User查询图书的协议
	private final String P6 = "BorrowBook";			//收到来自User租借图书的协议
	private final String P7 = "LogA";				//收到来自Administrator的登录请求协议
	private final String P8 = "AdminSearchBook";	//收到来自Administrator的查询图书协议
	private final String P9 = "InsertBook";			//收到来自Administrator的添加图书协议
	private final String P10 = "ModifyBook";		//收到来自Administrator的修改图书协议
	private final String P11 = "DeleteBook";		//收到来自Administrator的删除图书协议
	private final String P12 = "AdminSearchUser";	//收到来自Administrator的查询用户协议
	private final String P13 = "InsertUser";		//收到来自Administrator的添加用户协议
	private final String P14 = "ModifyUser";		//收到来自Administrator的修改用户协议
	private final String P15 = "DeleteUser";		//收到来自Administrator的删除用户协议
	private final String P16 = "BackBook";			//收到来自Administrator的归还图书协议
	private final String P17 = "FetchInformation";	//收到来自User查询个人信息的协议
	private final String P18 = "SelectUserRecord";	//收到来自Administrator的查询用户租借记录协议
	private final String P19 = "DeleteUserRecord";	//收到来自Administrator的删除用户租借记录协议
	private final String PE = "EOF";				//发送信息结束的协议
	private Connect con = new Connect();			//该线程与数据库的连接，每个与client的通信线程都会配一个这样的连接
	private Socket incoming;						//与client的通信socket
	private PrintWriter writer;						
	private BufferedReader br;
	private String no = new String("");				//主要用来标示每个与client的通信线程，不同的no用于区分与不同的client的通信线程
	public String toNo(){
		return no;
	}
	public ThreadServerHandler(Socket in){
		incoming = in;
		try{
			writer = new PrintWriter(new OutputStreamWriter(incoming.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
			con.connect(); 							//连接数据库，相当于一个数据库的通信线程
		}catch(Exception e){
			System.out.println("Server error2 -> Communication Thread Initialize failed");
			e.printStackTrace();
		}
		start();
	}
	public void writeUser(String number,String name,String passwd,String school,String department,int borrow){
		try{
			writer.write(number+"\n");
			writer.write(name+"\n");
			writer.write(passwd+"\n");
			writer.write(school+"\n");
			writer.write(department+"\n");
			writer.write(borrow+"\n");
			writer.flush();
		}catch(Exception e){
			System.out.println("Server error3 -> Write User failed -> Suggest restarting client");
			e.printStackTrace();
		}
	}
	public static synchronized boolean check(String number,Thread t){	/*	
																			检查登录用户是否已经在线，并决定将未在线的用户添加到服务端线程集合中，
																			这两步必须保证完整性，之所以要用static是因为希望所有线程执行这个函数
																			时互斥，如果有两个线程（编号相同，是同一个账号）同时判断不在线，那么会
																			将同一个client添加两次,用Thread传参是因为静态函数内无法使用this
																		*/
		boolean b = false;
		for(int i=0;i<Server.toL().size();i++){
			if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(number)){
				b = true;
				break;
			}
		}
		if(!b && Server.toL().size()<Server.N){				//用户不在线而且当前服务器在线人数未满
			Server.toL().add((ThreadServerHandler)t);
		}
		return b;
	}
	public void log(){										//登录判断
		try{
			String s = br.readLine();
			if(s.equals(P1)){
				no = br.readLine();
				String passwd = br.readLine();
				if(!con.selectUser(no)){					//如果数据库查询该账号不存在
					writer.write("E1"+"\n");
					writer.flush();
					try{
						sleep(3000);						//保证有足够的时间在关闭前让客户端收到信息
						br.close();
						writer.close();
						incoming.close();					//登录失败会自动关闭所有通信通道，并关闭通信线程,数据库连接
						con.disConnect();
						this.stop();						//关闭线程
					}catch(Exception e){
						System.out.println("Server error4 -> User Log message passing failed(1)!");
						e.printStackTrace();
					}
				}else if(!con.toUser().Upasswd.equals(passwd)){			//账号存在但密码错误
					writer.write("E2"+"\n");
					writer.flush();
					try{
						sleep(3000);
						br.close();
						writer.close();
						incoming.close();
						con.disConnect();
						this.stop();
					}catch(Exception e){
						System.out.println("Server error4 -> User Log message passing failed(2)!");
						e.printStackTrace();
					}
				}
				boolean b = check(no,this);					//检查登录用户是否已经在线
				if(!b){
					writer.write(P0+"\n");
					writer.flush();
					con.selectUser(no);
					User u = con.toUser();
					writeUser(u.Unumber,u.Uname,u.Upasswd,u.Uschool,u.Udepartment,u.Uborrow);
					Server.sg.addClient(u.Unumber,u.Uname,"User");
				}else{
					writer.write("E3"+"\n");
					writer.flush();
					try{
						sleep(3000);
						br.close();
						writer.close();
						incoming.close();
						con.disConnect();
						this.stop();
					}catch(Exception e){
						System.out.println("Server error4 -> User Log message passing failed(3)!");
						e.printStackTrace();
					}
				}
			}else if(s.equals(P7)){
				no = br.readLine();
				String passwd = br.readLine();
				if(!con.selectAdmin(no)){				//如果账号不存在
					writer.write("E1"+"\n");
					writer.flush();
					try{
						sleep(3000);
						br.close();
						writer.close();
						incoming.close();					//登录失败会自动关闭所有通信通道，并关闭通信线程
						con.disConnect();
						this.stop();						//关闭线程
					}catch(Exception e){
						System.out.println("Server error4 -> Admin Log message passing failed(a)!");
						e.printStackTrace();
					}
				}else if(!con.toAdmin().Apasswd.equals(passwd)){			//账号存在但密码错误
					writer.write("E2"+"\n");
					writer.flush();
					try{
						sleep(3000);
						br.close();
						writer.close();
						incoming.close();
						con.disConnect();
						this.stop();
					}catch(Exception e){
						System.out.println("Server error4 -> Admin Log message passing failed(b)!");
						e.printStackTrace();
					}
				}
				boolean b = check(no,this);					//检查登录用户是否已经在线
				if(!b){
					writer.write(P0+"\n");
					writer.flush();
					con.selectAdmin(no);
					Administrator a = con.toAdmin();
					writer.write(a.Anumber+"\n");
					writer.write(a.Aname+"\n");
					writer.write(a.Apasswd+"\n");
					writer.flush();
					Server.sg.addClient(a.Anumber,a.Aname,"Admin");
				}else{
					writer.write("E3"+"\n");
					writer.flush();
					try{
						sleep(3000);
						br.close();
						writer.close();
						incoming.close();
						con.disConnect();
						this.stop();
					}catch(Exception e){
						System.out.println("Server error4 -> Admin Log message passing failed(c)!");
						e.printStackTrace();
					}
				}
			}else{
				throw new Exception("Protocol Error");
			}
		}catch(Exception e){
			System.out.println("Server error4 -> Log failed,Probably protocol failed,suggest restart client");
			e.printStackTrace();
		}
	}
	public void run(){
		try{
			log();
			f();
		}catch(Exception e){								//如果客户端异常退出（如非法退出）或运行错误应当在服务端抹去对应client账号
			int location = 0;
			for(int i=0;i<Server.toL().size();i++){
				if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(no)){
					location = i;
					break;
				}
			}
			Server.toL().remove(location);					//移除服务器端线程集合中对应的线程，记录对应client并未在在线列表中
			Server.sg.removeClient(no);
			try{
				br.close();
				writer.close();
				incoming.close();
				con.disConnect();
			}catch(Exception ee){
				System.out.println("Server error5 -> Running Error -> Closed Error");
				ee.printStackTrace();
			}
			System.out.println("Server error6 -> Running Error（很有可能是client非法退出，暂时忽略它）");
			e.printStackTrace();
		}
	}
	public void f() throws Exception{
		while(true){
			String accept =  br.readLine();
			if(accept.equals(P2)){								//服务器收到来自client的退出信号
				String s = br.readLine();
				writer.write("Copy"+"\n"); 						/*
																	这个向client发送的信号并无意义，
																	只是为了延时使服务器先收到Exit消息，
																	再让client退出，如果没有，很可能client
																	发出消息在server端还没收到以前就关闭了读写
																	通道，出现异常
																*/
				int location = 0;
				for(int i=0;i<Server.toL().size();i++){
					if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s)){
						location = i;
						break;
					}
				}
				Server.sg.removeClient(s);
				Server.toL().remove(location);					//移除服务器端线程集合中对应的线程，记录对应client并未在在线列表中
				br.close();
				writer.close();
				incoming.close();
				con.disConnect();
				break;											//退出监听信号模式，结束connection线程
			}else if(accept.equals(P3)){						//User更新个人信息
				String number = br.readLine();
				String name = br.readLine();
				String passwd = br.readLine();
				if(con.updateUser(number, name, passwd)){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P4)){						//User查看个人租借记录
				String number = br.readLine();
				ArrayList<Record> list = new ArrayList<Record>();
				if(con.selectRecord(number)){
					list = con.toRecord();
				}
				if(list.isEmpty()){
					writer.write(P00+"\n");
					writer.flush();
				}else{
					writer.write(P0+"\n");
					writer.flush();
					for(int i=0;i<list.size();i++){
						writer.write(((Record)(list.get(i))).name + "\n");
						writer.write(((Record)(list.get(i))).ISBN + "\n");
						writer.write(((Record)(list.get(i))).date + "\n");
						writer.write(((Record)(list.get(i))).state + "\n");
						writer.flush();
					}
					writer.write(PE+"\n");
					writer.flush();
				}
			}else if(accept.equals(P5)){						//User查询书籍
				String name = br.readLine();
				String Writer = br.readLine();
				String type = br.readLine(); 
				ArrayList<Book> list = new ArrayList<Book>();
				if(Writer.equals("") && type.equals("All") && con.selectBook_accordingName(name)){
					list = con.toBook();
				}else if(name.equals("") && type.equals("All") && con.selectBook_accordingWriter(Writer)){
					list = con.toBook();
				}else if(name.equals("") && Writer.equals("") && con.selectBook_accordingClass(type)){
					list = con.toBook();
				}else if(!name.equals("") && !Writer.equals("") && type.equals("All") && con.selectBook_accordingNameandWriter(name,Writer)){
					list = con.toBook();
				}else if(!name.equals("") && Writer.equals("") && !type.equals("All") && con.selectBook_accordingNameandClass(name,type)){
					list = con.toBook();
				}else if(name.equals("") && !Writer.equals("") && !type.equals("All") && con.selectBook_accordingWriterandClass(Writer,type)){
					list = con.toBook();
				}else if(!name.equals("") && !Writer.equals("") && !type.equals("All") && con.selectBook_accordingNameWriterandClass(name,Writer,type)){
					list = con.toBook();
				}
				if(list.isEmpty()){
					writer.write(P00+"\n");
					writer.flush();
				}else{
					writer.write(P0+"\n");
					writer.flush();
					for(int i=0;i<list.size();i++){
						writer.write(((Book)(list.get(i))).Bname + "\n");
						writer.write(((Book)(list.get(i))).ISBN + "\n");
						writer.write(((Book)(list.get(i))).Bwriter + "\n");
						writer.write(((Book)(list.get(i))).Bclass + "\n");
						writer.write(((Book)(list.get(i))).Bpublisher + "\n");
						writer.write(((Book)(list.get(i))).Bquantity + "\n");
						writer.flush();
					}
					writer.write(PE+"\n");
					writer.flush();
				}
			}else if(accept.equals(P6)){						//User借书
				String number = br.readLine();
				String ISBN = br.readLine();
				String name = br.readLine();
				if(con.borrow(number, ISBN, name)){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P8)){						//Admin查询书籍
				String ISBN = br.readLine();
				if(con.selectBook_accordingISBN(ISBN)){
					Book b = (Book)(con.toBook().get(0));
					writer.write(P0+"\n");
					writer.write(b.Bname+"\n");
					writer.write(b.Bclass+"\n");
					writer.write(b.Bwriter+"\n");
					writer.write(b.Bpublisher+"\n");
					writer.write(b.Bquantity+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P9)){						//Admin添加书籍
				if(con.insertBook(br.readLine(), br.readLine(), br.readLine(), br.readLine(), br.readLine(), new Integer(br.readLine()).intValue())){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P10)){						//Admin修改书籍
				if(con.updateBook(br.readLine(), br.readLine(), br.readLine(), br.readLine(), br.readLine(), new Integer(br.readLine()).intValue())){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P11)){						//Admin删除书籍
				if(con.deleteBook(br.readLine())){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P12)){						//Admin查询用户
				String Number = br.readLine();
				if(con.selectUser(Number)){
					User u = (User)(con.toUser());
					writer.write(P0+"\n");
					writer.write(u.Upasswd+"\n");
					writer.write(u.Uname+"\n");
					writer.write(u.Uschool+"\n");
					writer.write(u.Udepartment+"\n");
					writer.write(u.Uborrow+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P13)){						//Admin添加用户
				if(con.insertUser(br.readLine(), br.readLine(), br.readLine(), br.readLine(), br.readLine(), new Integer(br.readLine()).intValue())){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P14)){						//Admin修改用户信息
				if(con.updateUserFromAdmin(br.readLine(), br.readLine(), br.readLine(), new Integer(br.readLine()).intValue())){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P15)){						//Admin删除用户
				boolean exist = false;
				String s = br.readLine();
				for(int i=0;i<Server.toL().size();i++){
					if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s)){
						writer.write(P00+"\n");
						writer.flush();
						exist = true;
						break;
					}
				}
				if(!exist){
					if(con.deleteUser(s)){
						writer.write(P0+"\n");
						writer.flush();
					}else{
						writer.write(P00+"\n");
						writer.flush();
					}
				}
			}else if(accept.equals(P16)){						//Admin当面确认用户归还书籍后进行系统还书，清除记录由数据库完成
				if(con.back(br.readLine(),br.readLine())){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P17)){						//User查询个人最新信息
				if(con.selectUser(br.readLine())){
					User u = con.toUser();
					writeUser(u.Unumber,u.Uname,u.Upasswd,u.Uschool,u.Udepartment,u.Uborrow);
				}
			}else if(accept.equals(P18)){
				String number = br.readLine();
				ArrayList<Record> list = new ArrayList<Record>();
				if(con.selectRecord(number)){
					list = con.toRecord();
				}
				if(list.isEmpty()){
					writer.write(P00+"\n");
					writer.flush();
				}else{
					writer.write(P0+"\n");
					writer.flush();
					for(int i=0;i<list.size();i++){
						writer.write(((Record)(list.get(i))).name + "\n");
						writer.write(((Record)(list.get(i))).ISBN + "\n");
						writer.write(((Record)(list.get(i))).date + "\n");
						writer.write(((Record)(list.get(i))).state + "\n");
						writer.flush();
					}
					writer.write(PE+"\n");
					writer.flush();
				}
			}else if(accept.equals(P19)){
				String Number = br.readLine();
				String date = br.readLine();
				if(con.deleteRecord(Number,date)){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else{												//以上协议之外的情况，有未识别的协议到达
				System.out.println("Serious Error Happen -> Unrecoginsed Protocol");
				int location = 0;
				for(int i=0;i<Server.toL().size();i++){
					if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(no)){
						location = i;
						break;
					}
				}
				Server.toL().remove(location);					//移除服务器端线程集合中对应的线程，记录对应client并未在在线列表中
				try{
					br.close();
					writer.close();
					incoming.close();
					con.disConnect();
				}catch(Exception ee){
					System.out.println("Server error7 -> Running Error -> Unrecognized protocol -> Closed Error");
					ee.printStackTrace();
				}
			}
		}
	}
}