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
 /** ������֧�֣�
  *		1)MySQL����������ݿ��������myqq������ݿ⣬�������ű�people����client��Ϣ
  *		2)JMF����(��)Ƶ������������������Ϣ������ʾ��
  *		3)���ݿ����Ӱ�jar��JDBC
  **/
 /** �ó���Ĳ���֮����	
  *		1)����㣺��Ӧ��Щ������Ҳ�����Ż������룬��������ѭ���ǣ��������Բ���ѭ���жϸ���client�Ƿ�����(�Ƿ���client������)���Թ�contains��������ʧ�ܸ��գ�
  *		2)��β��㣺���ݿ����Զ�����ӣ�����û����ѭ�ͻ���ͨ���������������ݿ⣬���ǿͻ��˿���ֱ�����ӵ����ݿ⣬������̫��ȫ��������ѧʶ̫ǳд��������ķ���������
  *		3)���в��㣺������װB�����ܲ�������̨�������ܳ�����ɹ��ܣ������˶�����ͨ�Ų��죬Socketֻ���ö˿������Ͳ����ˣ���֮��ֻ��һ̨���ԡ���������˵���˶����ᰡ��
  *		4)���в��㣺�ͻ����쳣�رշ���������Ϊ�����ߣ�������쳣�ǿͻ���С�������ǵ�Exit��ť����Ϊ�涨�ľ��ǵ�Exit�˳����Ǹ�С��汾�Ͳ���GUI�ڲ��Ķ���
  **/
 /** �ջ�
  *		1)��ѧΪ���ã���ǰ�������仰�����ڡ������Զ����������д�������֪��String��(StringBuffer��StringBuilder)ȷʵ����������(Wonderful)������I/O��ô�ã���InputStream����Reader?I/Oʹ���쳣����ȵȡ�����������Զ����ӿ����ĸ����
  *		2)�㵽�����ʲô��static����д����ǰ����Ϊ�Ҷ�static��д������ҷ��ֵ�����̫ɵ
  *		3)����Ҫ��ʼ���գ���new��Socket,�ڴ浱Ȼ�������գ�������㲻�أ��ߺߡ����������ͣ���������ݿ����ӣ���������Ƥƨ�����ˣ��ߺߡ������´�����ʱ���㶪���ݣ�������ˣ���������ˣ�������ʼ���գ�ǧ���ƭ�Լ�
  *		4)ע�ͺ���Ҫ�����Ǵ�д��������֪���һ���1week��ɣ��м���ü��β�֪����������ô���£�ֱ�������ٿ��������û��ע�͵��벻����������˵�㳭Ϯ��Ϊ����Ͳ�������������ԩ�ģ�������write on my own
  *		5)�㵽�׶԰��˽���٣�ֻ����д��������֪����������5�������ǲ������м�������̫��(300-400��)�����ÿ��.java100�����Ҿ͹��ˣ���ע��ʹ�ñ�ʱ����Ȩ�޵Ŀ���
  *		6)�ͻ����߼�����ͽ������ֿ�д��һ�ֺܺõ�ѡ�񣬷θ�֮�ԣ���������һ��ȴ��ʮ��֮�ѣ�����֮����ʱ��϶���ĺܸߣ�ż��û�취�����Ƿֿ��������ɡ�����
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
	private static ArrayList<ThreadServerHandler> l = new ArrayList<ThreadServerHandler> ();		//�������ӵ�client���߳�
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
																	д���������ҪС�ĵ�ϸ�ڣ����һ��Ҫ�ڶ˿��߳���ֻ����һ��writer,һ��reader
																	Ϊ�Σ�����ǰû��ע�⣬��ģ����new�˺ü���reader��writer���ÿ�����˽����˳�
																	������ҪΪÿ��online client���·�����Ϣʱ���֣���ʱ����clientż�����ַ�����
																	Ϣ����ȷ��û���»�Ϊ�յ�BUG����˼ڤ��1�������������⼸�������reader��writer
																	�С��ó���Է��䶯̬��Ϣ�ĸ��²���������1����ÿ��online clientд��Ϣ����������
																	��̬д����Ϣ��(Ҳ���ڼ�¼�����¼)	2����ÿ��online clientд��Ϣ����������
																	�ﻹ��˭�м�����д�뷿����Ϣ��¼��	������ÿ��online client��Ҫ������Ի��õ�
																	ѭ�������˱Ƚ��٣�online client���ڸ��ݷ�������ʾ������1����ʱ�������Ѿ���ɲ�
																	��һ������ѭ������Ȼ����в������ѭ��������������ѭ��2��ĳ��online client
																	����������1���������������client�᲻��ѵڶ����Ŀͻ���������Ϣû���յ���ʧ��
																	���ᣬ��Ϊ�ͻ���Ҳ���̶߳����Ǹ�ѭ�����������һ�����������ѭ��ȥreadline()��Ȼ
																	�����ڶ�����������������Ϣ�����ǣ�����и�ģ���reader�ڸ�client����һ��ʱ�϶���
																	��⵽����������buffer��ȴreadline�ˣ���ô��Ȼ������������Ρ�������reader��writer
																	ͳһ������������徻�ˣ����Ҵ���ɶ���Ҳ�ã����ֶ���Ϊ�أ�
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
		try{																//�����ͻ���ͬʱ����ͬһ��Client�������ͬһ��Thread��write()���Σ�����Ҫʹ��synchronized
			writer.write(s0+"\n");
			writer.write(s1+"\n");
			writer.write(s2+"eof\n");
			writer.flush();
		}catch(Exception ee){
			System.out.println("Server error write");
			ee.printStackTrace();
		}
	}
	public synchronized void welcome(){										//�������ͻ���ͬʱ��½��ֹ������Ϣ��ʾ������Ҫ��ԭ�Ӳ�����������Ϣ��ʾ����ָ������·��䶯̬��������ƥ�䣬˳�򲻱ص��ģ�˭�Ƚ���˭��Welcome��������ԭ�Ӳ����ж��Ⱥ�
		String s = new String();
		Connect.connect();
		Connect.select(no);
		String name = Connect.toName();
		for(int j=0;j<Server.toL().size();j++){
			((ThreadServerHandler)(Server.toL().get(j))).write(S1,"System",name+" enter into the room");			//����Ǹ��·��䶯̬�ĵ�һ��
			Connect.select(((ThreadServerHandler)(Server.toL().get(j))).toNo());
			s+=Connect.toName()+"\t"+Connect.toNumber()+"\n";
		}
		for(int j=0;j<Server.toL().size();j++){
			((ThreadServerHandler)(Server.toL().get(j))).write(S2,"Current Online Number"+Server.toL().size(),s);	//����Ǹ��·��䶯̬�ĵڶ���
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
																	Server������Ϣ����Ҫԭ�ӣ�ÿ��������CLient��Ӧ����
																	�˶�����һ��Socket����������������Ҫͬʱ��ȡ���
																	CLient��������Ϣ�������߳�ֻ�轫��ӦSocketִ��readline()
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
         					Server.toL().remove(i);				//���·��䶯̬ǰһ��Ҫ����remove���������ͣ���Ȼ����(�߳�)
         					break;
         				}
         			}
         			for(int i=0;i<Server.toL().size();i++){
         				Connect.connect();
						Connect.select(((ThreadServerHandler)(Server.toL().get(i))).toNo());
						String name = Connect.toName();
						sss+=Connect.toName()+"\t"+Connect.toNumber()+"\n";
         				((ThreadServerHandler)(Server.toL().get(i))).write(S1,"System",name+" left the room");		//����Ǹ��·��䶯̬�ĵ�һ��
         			}
					for(int j=0;j<Server.toL().size();j++){
						((ThreadServerHandler)(Server.toL().get(j))).write(S2,"Current Online Number"+Server.toL().size(),sss);	//����Ǹ��·��䶯̬�ĵڶ���
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
            			if ((index = temp.indexOf("eof")) != -1) {	//����eofʱ�ͽ�������  
            				sb.append(temp.substring(0, index));  
           			    	break;  
           				}  
           				sb.append(temp);  
         			}
         			s3 = sb.toString();
         			Connect.select(s1);
         			String name = Connect.toName();
					if(s2.equals("")){					//Ĭ��Ⱥ����ֻҪ�㲻ָ�����Ͷ���
         				for(int i=0;i<Server.toL().size();i++){
         					((ThreadServerHandler)(Server.toL().get(i))).write(S1,name,s3);			
						}
         			}else{								//˽��
         				if(s1.equals(s2)){				//�ж���Ϣ����Ŀ�ķ��������Լ�
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
								if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s2)){			//�жϷ��Ͷ���һ��Ҫ���ߣ�ǰ���Ѿ��жϹ����ܷ����Լ���
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