/** Some comments:
 *	@Server:A new client thread created as long as an client log successful
 *	1.Can receive messages from client,then server process the messages and
 *	return messages to right clients
 *	2.Can tell all online users the room's state(who leaves,who comes in,how many online clients)
 *	3.Can exam bugs(one account can be loged twice at the same time)
 *	4.Can connect more than one clients
 *	5.Can know who is online,because server has a mirror of clients' information(only a part information of each client limits to the memory space)
 *	@Notion:Run Server before client Log
 *	@Fine:Complitately C/S model
 **/
 /** 第三方支持：
  *		1)MySQL，服务端数据库里必须有myqq这个数据库，里面有张表people存有client信息
  *		2)JMF，音(视)频播放器，用来播放消息到来提示音
  *		3)数据库连接包jar，JDBC
  **/
 /** 该程序的不足之处：	
  *		1)最大不足：响应有些慢，我也尽量优化过代码，尤其是在循环那，本来可以不用循环判断给定client是否在线(是否在client镜像中)，试过contains函数，以失败告终！
  *		2)其次不足：数据库可以远程连接，但并没有遵循客户端通过服务器访问数据库，而是客户端可以直接连接到数据库，这样不太安全，但本人学识太浅写不出正规的服务器啊！
  *		3)还有不足：本来想装B试试能不能在两台机子上跑程序完成功能，但本人对网络通信不熟，Socket只会用端口其他就不会了，加之俺只有一台电脑。。。哎，说多了都是泪啊！
  *		4)略有不足：客户端异常关闭服务器仍认为他在线，这里的异常是客户点小红叉而不是点Exit按钮，因为规定的就是点Exit退出，那个小红叉本就不是GUI内部的东东
  **/
 /** 收获：
  *		1)所学为所用，以前不理解这句话，现在。。。略懂，这个程序写完你真的知道String类(StringBuffer、StringBuilder)确实如书上所述(Wonderful)，还有I/O怎么用，用InputStream还是Reader?I/O使用异常处理等等。你做过的永远比你从看到的更深刻
  *		2)你到底理解什么是static了吗，写程序前我认为我懂static，写程序后我发现当初我太傻
  *		3)事情要有始有终，你new了Socket,内存当然会帮你回收，但如果你不关，哼哼。。。不解释，你打开了数据库连接，你用完拍皮屁股走人，哼哼。。。下次用它时咒你丢数据，程序如此，做人亦如此，万事有始有终，千万别骗自己
  *		4)注释很重要，我是从写这个程序得知，我花了1week完成，有几天好几次不知道这块代码怎么回事，直到现在再看这个程序没有注释的想不起来，别人说你抄袭因为你解释不来，哎。。。冤哪，这真是write on my own
  *		5)你到底对包了解多少，只有你写完大程序后才知道，我用了5个但还是不够，有几个代码太长(300-400行)，最好每个.java100行左右就够了，但注意使用保时访问权限的控制
  *		6)客户端逻辑程序和界面程序分开写是一种很好的选择，肺腑之言，但做到这一点却又十分之难，它们之间有时耦合度真的很高，偶真没办法把他们分开，尽量吧。。。
  **/
 
package server;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import connect.Connect;

public class Server{
	private static final String ipv4 = "10.170.63.22";
	public static void main(String[] arg){
		System.out.println("MyQQ Server start\t"+new Date());
		new Server(8899);
	}
	private static ArrayList<ThreadServerHandler> l = new ArrayList<ThreadServerHandler> ();		//保留连接到client的线程
	private static String temp;
	public static ArrayList<ThreadServerHandler> toL(){
		return l;
	}
	public Server(int port){
		try{
			byte[] by = new byte[4];
			int j=0;
			for(int i=0;i<ipv4.length();i++){
				String temp = new String("");
				while(i<ipv4.length() && ipv4.charAt(i)!='.'){
					temp += ipv4.charAt(i);
					i++;
				}
				by[j] = (byte)((new Integer(temp)).intValue());
				System.out.println((int)by[j]);
				j++;
			}
			ServerSocket server = new ServerSocket(port,0,InetAddress.getByAddress(by));
			Connect.connect();
			label:
			while(true){
				try{
					Socket client = server.accept();
					ThreadServerHandler t=new ThreadServerHandler(client);
				}catch(Exception ee){
					System.out.println("Server error2 -> Server start failed(1)!");
					ee.printStackTrace();
					break;
				}
			}
		}catch(Exception e){
			System.out.println("Server error3 -> Server start failed(2)!");
			e.printStackTrace();
		}finally{
			Connect.disConnect();
		}
	}
}
class ThreadServerHandler extends Thread implements Runnable{
	private final String S1 = "AddText";
	private final String S2 = "SetText";
	private final String S3 = "Server";
	private Socket incoming;								/*	
																	写这个代码最要小心的细节：这个一定要在端口线程中只能有一个writer,一个reader
																	为何？我先前没有注意，在模块中new了好几个reader、writer结果每当有人进入退出
																	房间需要为每个online client更新房间信息时发现，有时个别client偶尔出现房间信
																	息不正确或没更新或为空的BUG，苦思冥想1晚后问题出现在这几个多余的reader、writer
																	中。该程序对房间动态信息的更新采用两步：1、给每个online client写信息让他将房间
																	动态写入信息框(也用于记录聊天记录)	2、给每个online client写信息让他将房间
																	里还有谁有几个人写入房间信息记录框	这两步每个online client都要完成所以会用到
																	循环假如人比较少，online client正在根据服务器提示做步骤1，这时服务器已经完成步
																	骤一的所有循环他当然会进行步骤二的循环假设服务器完成循环2后，某个online client
																	正在做步骤1，那这个不争气的client会不会把第二步的客户发来的信息没有收到或丢失，
																	不会，因为客户端也有线程而且是个循环当他做完第一步会进行下轮循环去readline()依然
																	读到第二步服务器发来的信息。可是，如果有个模块的reader在该client读第一步时肯定会
																	检测到本不是他的buffer他却readline了，那么自然会出现上述情形。将所有reader、writer
																	统一后整个世界就清净了，而且代码可读性也好，何乐而不为呢？
															*/
	private Writer writer;									
	private BufferedReader br;
	private String no = new String();
	private String s1 = new String();
	private String s2 = new String();
	private String s3 = new String();
	public String toNo(){
		return no;
	}
	public void write(String s0,String s1,String s2){
		try{																//当两客户端同时发给同一个Client，会调用同一个Thread中write()两次，不需要使用synchronized
			writer.write(s0+"\n");
			writer.write(s1+"\n");
			writer.write(s2+"eof\n");
			writer.flush();
		}catch(Exception ee){
			System.out.println("Server error write");
			ee.printStackTrace();
		}
	}
	public synchronized void welcome(){										//当两个客户端同时登陆防止房间信息显示错误需要用原子操作，房间信息显示错误指下面更新房间动态的两步不匹配，顺序不必担心，谁先进来谁先Welcome，上面有原子操作判断先后
		String s = new String();
		Connect.connect();
		Connect.select(no);
		String name = Connect.toName();
		for(int j=0;j<Server.toL().size();j++){
			((ThreadServerHandler)(Server.toL().get(j))).write(S1,"System",name+" enter into the room");			//这就是更新房间动态的第一步
			Connect.select(((ThreadServerHandler)(Server.toL().get(j))).toNo());
			s+=Connect.toName()+"\t"+Connect.toNumber()+"\n";
		}
		for(int j=0;j<Server.toL().size();j++){
			((ThreadServerHandler)(Server.toL().get(j))).write(S2,"Current Online Number"+Server.toL().size(),s);	//这就是更新房间动态的第二步
		}
	}
	public ThreadServerHandler(Socket in){
		incoming = in;
		try{
			writer = new OutputStreamWriter(incoming.getOutputStream());
			br = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
		}catch(Exception e){
			System.out.println("Server error3");
			e.printStackTrace();
		}
		start();
	}
	public void run(){
		try{
			f();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void f() throws Exception{
		while(true){
			if(this.isAlive()){
				System.out.println("Communication");
			}
			try{
				StringBuilder sb = new StringBuilder();
				String temp;
				int index;
				s1 = br.readLine();								/*	
																	Server接受消息不需要原子，每个单独的CLient对应服务
																	端都会有一个Socket监听，如果服务端需要同时读取多个
																	CLient发来的消息，所有线程只需将对应Socket执行readline()
																*/
				if(s1.equals("Log")){							//Client Log info
					boolean sig = false;						
					s2 = br.readLine();							//Client number
					no = s2;
					for(int i=0;i<Server.toL().size();i++){
						if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s2)){
							writer.write("No\n");
							writer.flush();
							sig = true;
							break;
						}
					}
					if(sig){
						br.close();
						writer.close();
						incoming.close();
						break;
					}else{
						Server.toL().add(this);
						writer.write("Yes\n");
						writer.flush();
						welcome();
					}
				}else if(s1.equals("Exit")){					//Client Exit info
					s1 = br.readLine();							//Client number
					String sss = new String();
         			for(int i=0;i<Server.toL().size();i++){
         				if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s1)){
         					Server.toL().remove(i);				//更新房间动态前一定要将他remove掉，不解释，不然出错(线程)
         					break;
         				}
         			}
         			for(int i=0;i<Server.toL().size();i++){
         				Connect.connect();
						Connect.select(((ThreadServerHandler)(Server.toL().get(i))).toNo());
						String name = Connect.toName();
						sss+=Connect.toName()+"\t"+Connect.toNumber()+"\n";
         				((ThreadServerHandler)(Server.toL().get(i))).write(S1,"System",name+" left the room");		//这就是更新房间动态的第一步
         			}
					for(int j=0;j<Server.toL().size();j++){
						((ThreadServerHandler)(Server.toL().get(j))).write(S2,"Current Online Number"+Server.toL().size(),sss);	//这就是更新房间动态的第二步
					}
					System.out.println(s1+" unlink success");
					br.close();
					writer.close();
         			incoming.close();
         			break;
				}else if(s1.equals("Send")){						//Send Message info
					s1 = br.readLine();								//Source
					s2 = br.readLine();								//Destination
					while ((temp=br.readLine()) != null) {			//Message
            			if ((index = temp.indexOf("eof")) != -1) {	//遇到eof时就结束接收  
            				sb.append(temp.substring(0, index));  
           			    	break;  
           				}  
           				sb.append(temp);  
         			}
         			s3 = sb.toString();
         			Connect.select(s1);
         			String name = Connect.toName();
					if(s2.equals("")){					//默认群发，只要你不指定发送对象
         				for(int i=0;i<Server.toL().size();i++){
         					((ThreadServerHandler)(Server.toL().get(i))).write(S1,name,s3);			
						}
         			}else{								//私聊
         				if(s1.equals(s2)){				//判断信息发送目的方不能是自己
         					String s =new String();
							s= "Cannot Pass message:\n  1)The number not exist\n  2)He(She) is offline\n  3)You cannot pass message to yourselef";
							for(int i=0;i<Server.toL().size();i++){
        						if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s1)){
       								((ThreadServerHandler)(Server.toL().get(i))).write(S1,"System",s);
								}
							}
         				}else{
         					boolean pan = false;
         					for(int i=0;i<Server.toL().size();i++){
								if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s2)){			//判断发送对象一定要在线，前面已经判断过不能发给自己了
									pan = true;
								}
							}
							if(pan){
								for(int i=0;i<Server.toL().size();i++){
         							if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s1)){
         								((ThreadServerHandler)(Server.toL().get(i))).write(S1,name,s3);
									}
									if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s2)){
										((ThreadServerHandler)(Server.toL().get(i))).write(S1,name,s3);
									}
								}
							}else{
								String s =new String();
								s= "Cannot Pass message:\n  1)The number not exist\n  2)He(She) is offline\n  3)You cannot pass message to yourselef";
								for(int i=0;i<Server.toL().size();i++){
         							if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s1)){
         								((ThreadServerHandler)(Server.toL().get(i))).write(S1,"System",s);
									}
								}
							}
         				}
         			}
				}else{
					System.out.println("Message loss");
				}
			}catch(Exception e){
				System.out.println("Server error00 -> Server Thread run failed!");
				e.printStackTrace();
				break;
			}	
		}
	}
}