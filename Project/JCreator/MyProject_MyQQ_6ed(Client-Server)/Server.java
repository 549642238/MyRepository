/** Server:A new client thread created as long as an client log
 *	Can receive messages from client,then server process the messages and
 *	return messages to right clients
 *	Notion:Run Server before client Log*/
 //Complitately C/S model
 /*	Problems:
  *	Log problem:If log twice is works,then it will be wrong if log again
  *	Fresh problem:If click exit button quickly ,the room state mybe not right
  *	sometiomes log can make room state wrong*/
package myqq.server;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import myqq.connect.Connect;

public class Server{
	public static void main(String[] arg){
		System.out.println("MyQQ Server start\t"+new Date());
		new Server(8899);
	}
	private static ArrayList<ThreadServerHandler> l = new ArrayList<ThreadServerHandler> ();
	private static ArrayList<Client> list = new ArrayList<Client> ();
	private static String temp;
	private static boolean lock = false;
	public static ArrayList<Client> toList(){
		return list;
	}
	public static void addClient(){
		try{
			ServerSocket server = new ServerSocket(8189);
			Socket client = server.accept();
			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			Writer writer = new OutputStreamWriter(client.getOutputStream());
			temp = br.readLine();
			boolean b = true;
			for(int i=0;i<Server.toList().size();i++){
				if(((Client)(Server.toList().get(i))).toNo().equals(temp)){
					b = false;
				}
			}
			if(!b){
				writer.write("No"+"\n");
				writer.flush();
				br.readLine();
				JOptionPane.showMessageDialog(null,"Your account is online!","Log Failed",JOptionPane.ERROR_MESSAGE);
			}else{
				writer.write("Yes"+"\n");
				writer.flush();
				list.add(new Client(temp));
				br.readLine();
				lock = true;
			}
			br.close();
			writer.close();
			client.close();
			server.close();
		}catch(Exception ee){
			System.out.println("Server error1");
			ee.printStackTrace();
		}
	}
	public Server(int port){
		try{
			ServerSocket server = new ServerSocket(port);
			while(true){
				try{
					addClient();
					Socket client = server.accept();
					ThreadServerHandler t=new ThreadServerHandler(client,temp);
					while(!lock){
						Thread.yield();			//(Client)c must be initialized first,or it will be null
					}
					l.add(t);
					lock = false;
					Socket ss = new Socket("localhost",8989);
					BufferedReader br = new BufferedReader(new InputStreamReader(ss.getInputStream()));
					Writer writer = new OutputStreamWriter(ss.getOutputStream());
					writer.write("Run\n");
					writer.flush();
					br.readLine();
					br.readLine();
					writer.write("close");
					t.welcome();
				}catch(Exception ee){
					System.out.println("Server error2");
					ee.printStackTrace();
					break;
				}
			}
		}catch(Exception e){
			System.out.println("Server error3");
			e.printStackTrace();
		}finally{
			Connect.disConnect();
		}
	}
	public static ArrayList<ThreadServerHandler> toL(){
		return l;
	}
}
class ThreadServerHandler extends Thread implements Runnable{
	private final String S1 = "addText";
	private final String S2 = "setText";
	private final String S3 = "Server";
	private Socket incoming;
	private String no = new String();
	private String s1 = new String();
	private String s2 = new String();
	private String s3 = new String();
	public String toNo(){
		return no;
	}
	public void write(String s0,String s1,String s2){
		try{
			Writer writer = new OutputStreamWriter(incoming.getOutputStream());
			writer.write(s0+"\n");
			writer.write(s1+"\n");
			writer.write(s2+"eof\n");
			writer.flush();
		}catch(Exception ee){
			System.out.println("Server error write");
			ee.printStackTrace();
		}
	}
	public void welcome(){
		String s = new String();
		Connect.connect();
		Connect.select(no);
		String name = Connect.toName();
		for(int j=0;j<Server.toL().size();j++){
			((ThreadServerHandler)(Server.toL().get(j))).write(S1,"System",name+" enter into the room");
			Connect.select(((ThreadServerHandler)(Server.toL().get(j))).toNo());
			s+=Connect.toName()+"\t"+Connect.toNumber()+"\n";
		}
		for(int j=0;j<Server.toL().size();j++){
			((ThreadServerHandler)(Server.toL().get(j))).write(S2,"Current Online Number"+Server.toL().size(),s);
		}
	}
	public ThreadServerHandler(Socket in,String n){
		incoming = in;
		no = n;
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
				BufferedReader br = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String temp;
				int index;
				s1 = br.readLine();
				s2 = br.readLine();
				while ((temp=br.readLine()) != null) {
            		if ((index = temp.indexOf("eof")) != -1) {//遇到eof时就结束接收  
            			sb.append(temp.substring(0, index));  
           		    	break;  
           			}  
           			sb.append(temp);  
         		}
         		s3 = sb.toString();
         		String name = new String();
         		Connect.connect();
				Connect.select(s1);
				name = Connect.toName();
         		if(s3.equals("Exit_MyQQ")){
         			String sss = new String();
         			for(int i=0;i<Server.toL().size();i++){
         				if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s1)){
         					Server.toL().remove(i);
         					break;
         				}
         			}
         			for(int i=0;i<Server.toList().size();i++){
         				if(((Client)(Server.toList().get(i))).toNo().equals(s1)){
         					Server.toList().remove(i);
         					break;
         				}
         			}
         			for(int i=0;i<Server.toL().size();i++){
         				Connect.connect();
						Connect.select(((ThreadServerHandler)(Server.toL().get(i))).toNo());
						sss+=Connect.toName()+"\t"+Connect.toNumber()+"\n";
         				((ThreadServerHandler)(Server.toL().get(i))).write(S1,"System",name+" left the room");
         			}
					for(int j=0;j<Server.toL().size();j++){
						((ThreadServerHandler)(Server.toL().get(j))).write(S2,"Current Online Number"+Server.toList().size(),sss);
					}
         			incoming.close();
         			break;
         		}else{
         			if(s2.equals("")){
         				for(int i=0;i<Server.toL().size();i++){
         					((ThreadServerHandler)(Server.toL().get(i))).write(S1,name,s3);
						}
         			}else{
         				if(s1.equals(s2)){
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
								if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s2)){
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
         		}
			}catch(Exception e){
				System.out.println("Server error00");
				e.printStackTrace();
				break;
			}	
		}
	}
}
class Client{
	private String number = null;
	public String toNo(){
		return number;
	}
	public Client(String no){
		number = no;
	}
}