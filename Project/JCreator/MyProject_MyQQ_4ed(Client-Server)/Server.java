/** Server:A new client thread created as long as an client log
 *	Can receive messages from client,then server process the messages and
 *	return messages to right clients
 *	Notion:Run Server before client Log*/
package myqq.server;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import myqq.client.Client;
import myqq.connect.Connect;

public class Server{
	public static void main(String[] arg){
		System.out.println("MyQQ Server start\t"+new Date());
		new Server(8899);
	}
	private static ArrayList<Client> list = new ArrayList<Client> ();
	private static ArrayList<ThreadServerHandler> l = new ArrayList<ThreadServerHandler> ();
	private static String temp;
	private static Client c;
	private static boolean lock = false;
	public static void addClient(){
		try{
			ServerSocket server = new ServerSocket(8189);
			Socket client = server.accept();
			System.out.println("haah");
			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			temp = br.readLine();
			br.close();
			client.close();
			server.close();
		}catch(Exception e){
			System.out.println("Server error1");
			e.printStackTrace();
		}
		boolean b = true;
		for(int i=0;i<list.size();i++){
			if(((Client)list.get(i)).toNo().equals(temp)){
				JOptionPane.showMessageDialog(null,"Your account is online!","Log Failed",JOptionPane.ERROR_MESSAGE);
				b = false;
				break;
			}
		}
		if(b){
			c = new Client(temp);
			lock = true;
		}
	}
	public Server(int port){
		try{
			ServerSocket server = new ServerSocket(port);
			while(true){
				addClient();
				Socket client = server.accept();
				ThreadServerHandler t=new ThreadServerHandler(client,temp);
				while(!lock){
					Thread.yield();			//(Client)c must be initialized first,or it will be null
				}
				list.add(c);
				l.add(t);
				lock = false;
				t.welcome();
			}
		}catch(Exception e){
			System.out.println("Server error2");
			e.printStackTrace();
		}finally{
			Connect.disConnect();
		}
	}
	public static ArrayList<Client> toList(){
		return list;
	}
	public static ArrayList<ThreadServerHandler> toL(){
		return l;
	}
}
class ThreadServerHandler extends Thread implements Runnable{
	private Socket incoming;
	private String no = new String();
	private String s1 = new String();
	private String s2 = new String();
	private String s3 = new String();
	public String toNo(){
		return no;
	}
	public void write(String s1,String s2){
		try{
			Writer writer = new OutputStreamWriter(incoming.getOutputStream());
			writer.write(s1+"\n");
			writer.write(s2+"eof\n");
			writer.flush();
		}catch(Exception ee){
			System.out.println("Server error0");
			ee.printStackTrace();
		}
	}
	public void welcome(){
		String s = new String();
		Connect.connect();
		Connect.select(no);
		String name = Connect.toName();
		for(int j=0;j<Server.toL().size();j++){
			((ThreadServerHandler)(Server.toL().get(j))).write("System",name+" enter into the room");
			Connect.connect();
			Connect.select(((ThreadServerHandler)(Server.toL().get(j))).toNo());
			s+=Connect.toName()+"\t"+Connect.toNumber()+"\n";
		}
		for(int j=0;j<Server.toList().size();j++){
			((Client)(Server.toList().get(j))).receive();
		}
		for(int j=0;j<Server.toL().size();j++){
			((ThreadServerHandler)(Server.toL().get(j))).write("Current Online Number"+Server.toList().size(),s);
		}
		for(int j=0;j<Server.toList().size();j++){
			((Client)(Server.toList().get(j))).clientChange();
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
         			String s = new String();
         			for(int i=0;i<Server.toL().size();i++){
         				if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s1)){
         					Server.toL().remove(i);
         					break;
         				}
         			}
         			for(int i=0;i<Server.toList().size();i++){
         				if(((Client)(Server.toList().get(i))).toNo().equals(s1)){
         					((Client)(Server.toList().get(i))).disappear();
         					Server.toList().remove(i);
         					break;
         				}
         			}
         			for(int i=0;i<Server.toL().size();i++){
         				Connect.connect();
						Connect.select(((ThreadServerHandler)(Server.toL().get(i))).toNo());
						s+=Connect.toName()+"\t"+Connect.toNumber()+"\n";
         				((ThreadServerHandler)(Server.toL().get(i))).write("System",name+" left the room");
         			}
         			for(int i=0;i<Server.toList().size();i++){
						((Client)(Server.toList().get(i))).receive();
					}
					for(int j=0;j<Server.toL().size();j++){
						((ThreadServerHandler)(Server.toL().get(j))).write("Current Online Number"+Server.toList().size(),s);
					}
					for(int i=0;i<Server.toList().size();i++){
						((Client)(Server.toList().get(i))).clientChange();
					}
         			incoming.close();
         			break;
         		}else{
         			if(s2.equals("")){
         				for(int i=0;i<Server.toL().size();i++){
         					((ThreadServerHandler)(Server.toL().get(i))).write(name,s3);
						}
						for(int j=0;j<Server.toList().size();j++){
							((Client)(Server.toList().get(j))).receive();
						}
         			}else{
         				if(s1.equals(s2)){
         					String s =new String();
							s= "Cannot Pass message:\n  1)The number not exist\n  2)He(She) is offline\n  3)You cannot pass message to yourselef";
							for(int i=0;i<Server.toL().size();i++){
        						if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s1)){
       								((ThreadServerHandler)(Server.toL().get(i))).write("System",s);
								}
							}
							for(int i=0;i<Server.toList().size();i++){
								if(((Client)(Server.toList().get(i))).toNo().equals(s1)){
									((Client)(Server.toList().get(i))).receive();
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
         								((ThreadServerHandler)(Server.toL().get(i))).write(name,s3);
									}
									if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s2)){
										((ThreadServerHandler)(Server.toL().get(i))).write(name,s3);
									}
								}
								for(int i=0;i<Server.toList().size();i++){
									if(((Client)(Server.toList().get(i))).toNo().equals(s1)){
										((Client)(Server.toList().get(i))).receive();
									}
									if(((Client)(Server.toList().get(i))).toNo().equals(s2)){
										((Client)(Server.toList().get(i))).receive();
									}
								}
							}else{
								String s =new String();
								s= "Cannot Pass message:\n  1)The number not exist\n  2)He(She) is offline\n  3)You cannot pass message to yourselef";
								for(int i=0;i<Server.toL().size();i++){
         							if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s1)){
         								((ThreadServerHandler)(Server.toL().get(i))).write("System",s);
									}
								}
								for(int i=0;i<Server.toList().size();i++){
									if(((Client)(Server.toList().get(i))).toNo().equals(s1)){
										((Client)(Server.toList().get(i))).receive();
									}
								}
							}
         				}
         			}
         		}
			}catch(Exception e){
				e.printStackTrace();
			}	
		}
	}
}