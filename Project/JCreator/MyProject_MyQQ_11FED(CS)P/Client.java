/** This module depart into two parts,one is Client logical achievement,
 * 	another is Client gui achievement.*/
package myqq.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.media.*;

import myqq.connect.Connect;
import myqq.display.Display;

public class Client{
	private JApplet cg = null;
	private JFrame jj = null;
	public void disappear(){
		jj.dispose();
	}
	public Client(String no){
		cg = new Client_GUI(no);
		jj = new JFrame("MyQQ-Online");
		Display.run(jj,cg,300,700);
	}
	public static void main(String[] arg){
		Client c =new Client("13121175");
	}
}
class Client_GUI extends JApplet{
	private MyThread myth;
	private Socket client;
	private BufferedReader br = null;
	private Writer writer = null;
	protected static JFrame f = null;
	private String no = null;
	private Icon picture = new ImageIcon("1.jpg");
	protected JTextArea ta1 = new JTextArea(20,20);
	private JTextArea ta2 = new JTextArea(2,21);
	private JTextArea ta3 = new JTextArea(2,20);
	private JTextField t1 = new JTextField(10);
	private JButton pb = new JButton(picture);
	private JButton b1 = new JButton("Send");
	private JButton b2 = new JButton("Clear");
	private JButton b3 = new JButton("Exit");
	private JLabel l = new JLabel("Destination"); 
	private JTextField t =new JTextField(10);
	private final String ipv4 = "localhost";		//server ip
	public Client_GUI(String number){
		try{
			no=number;
			client = new Socket(ipv4,8899);
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));						/*
																												这里直接就连接上服务器了，还没有创建好线程，那
																												这时服务器发来消息没有线程去读一会线程创建好了
																												还能读到吗？能，这是I/O规定的，服务端写入buffer
																												一直会在缓冲区存这直到你线程创建好再来读取数据不
																												会丢失
																											*/
			writer = new OutputStreamWriter(client.getOutputStream());
			writer.write("Log\n");																			//给服务器发上线请求
			writer.write(no+"\n");		
			writer.flush();																					//清除缓冲区，此时读端可以直接读，并不需要等缓冲区满后(写端才能写入流并发送到读端)
			if(br.readLine().equals("No")){																	//如果收到服务器发来的拒绝说明账户已在线，不能重复登录
				JOptionPane.showMessageDialog(null,"Your account is online!","Log Failed",JOptionPane.ERROR_MESSAGE);
				br.close();
				writer.close();
				client.close();	
				System.exit(0);
			}
		}catch(Exception e){
			System.out.println("Client error1");
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null,"Welcome to MyQQ!","Log Success",JOptionPane.CLOSED_OPTION);
		myth = new MyThread();				/*
												一定要有一个独立的线程，如果你没有独立线程而只是单独写一个run的循环函数
												则初始化Client_GUI时界面会空白一片，因为你只有一个线程去运行run没有多余
												的线程进行界面显示工作
											*/
	}
	public void init(){
		Connect.connect();
		Connect.select(no);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		pb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Connect.connect();
				Connect.select(no);
				f = new JFrame("MyQQ-Online");
				Display.run(f,new Self_Information(no),300,400);
			}
		});
		pb.setBorder(new LineBorder(Color.ORANGE));
		pb.setToolTipText("Self information");
		c.add(pb);
		t.setText(Connect.toName());
		t.setBorder(new LineBorder(Color.BLUE));
		t.setToolTipText("MyQQ name is:");
		t.setBackground(Color.PINK);
		t.setEditable(false);
		c.add(t);
		ta1.setBackground(Color.LIGHT_GRAY);
		ta1.setBorder(new LineBorder(Color.RED));
		ta1.setToolTipText("Talking Records");
		ta1.setEditable(false);
		c.add(new JScrollPane(ta1,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		ta2.setBackground(Color.PINK);
		ta2.setBorder(new LineBorder(Color.BLUE));
		c.add(new JScrollPane(ta2,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		ta3.setEditable(false);
		ta3.setToolTipText("Current online people");
		ta3.setBackground(Color.GREEN);
		ta3.setBorder(new LineBorder(Color.BLUE));
		c.add(new JScrollPane(ta3,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		t1.setBorder(new LineBorder(Color.GREEN));
		t1.setToolTipText("Input the number of people you want send to,if you want send to everyone just no filling");
		t1.setBackground(Color.CYAN);
		c.add(t1);
		l.setBackground(Color.YELLOW);
		l.setBorder(new LineBorder(Color.BLUE));
		l.setToolTipText("Dest people");
		c.add(l);
		b1.setBackground(Color.GREEN);
		b1.setBorder(new LineBorder(Color.YELLOW));
		b1.setToolTipText("Send message");
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){			/*	
																	两个客户端同时发不需要原子操作(synchronized)
																	，由于每个客户端对应服务端一个线程中的(Socket)，
																	即使俩两个Client同时发服务端暂时不处理这些消息
																	等到轮到对应线程调度时也会处理以前Client发来的
																	消息而不会遗漏或者和其他Client发来的消息搞混
																*/
				try{
					writer.write("Send\n");
					writer.write(no+"\n");
					writer.write(t1.getText()+"\n");
					writer.write(ta2.getText()+"eof\n");
					writer.flush();
				}catch(Exception ee){
					System.out.println("Client error5");
					ee.printStackTrace();
				}
				ta2.setText("");
				t1.setText("");
			}
		});
		c.add(b1);
		b2.setBackground(Color.CYAN);
		b2.setBorder(new LineBorder(Color.YELLOW));
		b2.setToolTipText("Clear up screen");
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ta1.setText("");
			}
		});
		c.add(b2);
		b3.setBackground(Color.RED);
		b3.setBorder(new LineBorder(Color.YELLOW));
		b3.setToolTipText("Exit myqq");
		b3.addActionListener(new ActionListener(){					//客户端要退出qq
			public void actionPerformed(ActionEvent e){
				try{
					myth.stopMe();									//关闭线程
																	/*
																		一定要把线程关掉，不然即使界面消失线程依旧运行，继续运行会使其他Client线程出错
																		对于把myth指向null是一种很不好的习惯，但依照百度完全让这个线程销毁只有如此
																	*/
					myth = null;									//ignore
					writer.write("Exit\n");
					writer.write(no+"\n");
					writer.flush();									
					br.close();
					writer.close();
					client.close();
					System.exit(0);
				}catch(Exception ee){
					System.out.println("Client error6");
					ee.printStackTrace();
				}
			}
		});
		c.add(b3);
	}
	class MyThread extends Thread{
		private boolean stop = false;
		MyThread(){
			super();
			start();
		}
		public void stopMe(){
			stop = true;
			try{
				interrupt();
			}catch(Exception eee){
				//do nothing,maybe the thread is blocked when it exits
			}
		}
		public void run(){
			while(!stop){
				System.out.println("Communication\'");
				try{
					StringBuilder sb = new StringBuilder();
					String s0 = br.readLine();
					if(s0.equals("AddText")){				//更新房间信息的步骤一，也可以用来就收来自其他client的消息
						Sound.play();
						String s1 = new String();
						String s2 = new String();
						try{
							String temp;
							int index;
							s1 = br.readLine();
							while ((temp=br.readLine()) != null) {
       				 	    	if ((index = temp.indexOf("eof")) != -1) {//遇到eof时就结束接收  
       				    	 		sb.append(temp.substring(0, index));  
        				   		   	break;  
        				   		}  
           						sb.append(temp+"\n");  
         					}
         					s2 = sb.toString();
						}catch(Exception e){
							System.out.println("Client error3");
							e.printStackTrace();
						}
						ta1.append(s1+": "+s2+"\n");
					}else if(s0.equals("SetText")){			//更新房间信息的步骤二
						String s1 = new String();
						String s2 = new String();
						try{
							String temp;
							int index;
							s1 = br.readLine();
							while ((temp=br.readLine()) != null) {
            					if ((index = temp.indexOf("eof")) != -1) {//遇到eof时就结束接收  
            						sb.append(temp.substring(0, index));  
           						   	break;  
           						}  
           						sb.append(temp+"\n");  
         					}
         					s2 = sb.toString();
						}catch(Exception e){
							System.out.println("Client error4");
							e.printStackTrace();
						}
						ta3.setText(s1+"\n"+s2);
					}else{
						System.out.println("Message loss");
					}
				}catch(Exception e){
					//do nothing,这个异常是调用s0产生的，就在s0阻塞语句(s0 = br.readLine())后的一句产生异常是因为interrupt会使在阻塞态的线程产生异常 
				}
			}
		}
	}
}
/*Click picture you will see self information,besides you can edit telephone and save it*/
class Self_Information extends JApplet{
	private JTextArea t0=new JTextArea(1,30);
	private JLabel L1=new JLabel("number");
	private JLabel L2=new JLabel("name");
	private JLabel L3=new JLabel("sex");
	private JLabel L4=new JLabel("teleph");
	private JTextField t1=new JTextField(20);
	private JTextField t2=new JTextField(20);
	private JTextField t3=new JTextField(20);
	private JTextField t4=new JTextField(20);
	private JButton b=new JButton("Save");
	private JButton b1=new JButton("Close");
	private String no=null;
	Self_Information(String number){
		no=number;
	}
	public void init(){
		Container c=getContentPane();
		c.setLayout(new FlowLayout());
		c.setBackground(Color.PINK);
		t0.setText("\t    Personal Information");
		t0.setEditable(false);
		t0.setBackground(Color.CYAN);
		t0.setBorder(new LineBorder(Color.BLACK));
		c.add(t0);
		L1.setBackground(Color.BLUE);
		L1.setBorder(new LineBorder(Color.GREEN));
		c.add(L1);
		t1.setEditable(false);
		t1.setBackground(Color.YELLOW);
		t1.setBorder(new LineBorder(Color.GRAY));
		t1.setText(Connect.toNumber());
		c.add(t1);
		L2.setBackground(Color.BLUE);
		L2.setBorder(new LineBorder(Color.GREEN));
		c.add(L2);
		t2.setBackground(Color.YELLOW);
		t2.setBorder(new LineBorder(Color.GRAY));
		t2.setText(Connect.toName());
		t2.setEditable(false);		
		c.add(t2);
		L3.setBackground(Color.BLUE);
		L3.setBorder(new LineBorder(Color.GREEN));
		c.add(L3);
		t3.setBackground(Color.YELLOW);
		t3.setBorder(new LineBorder(Color.GRAY));
		t3.setText(Connect.toSex());
		t3.setEditable(false);	
		c.add(t3);
		L4.setBackground(Color.BLUE);
		L4.setBorder(new LineBorder(Color.GREEN));
		c.add(L4);
		t4.setBackground(Color.YELLOW);
		t4.setBorder(new LineBorder(Color.GRAY));
		t4.setText(Connect.toTele());
		c.add(t4);
		b.setBackground(Color.GREEN);
		b.setSize(15,10);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String ss=t2.getText();
	//			Connect.update(MyQQ.no,"name",ss);
	//			System.out.println("111");
	//			ss=t3.getText();
	//			Connect.update(MyQQ.no,"sex",ss);
	//			System.out.println("111");
				ss=t4.getText();
				Connect.update(no,"telephone",ss);
	//			System.out.println("111");
				JOptionPane.showMessageDialog(null,"Save Success!","Save",JOptionPane.CLOSED_OPTION);
				Connect.select(no);
				Client_GUI.f.dispose();
			}
		});
		c.add(b);
		b1.setBackground(Color.RED);
		b1.setSize(15,10);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Client_GUI.f.dispose();
			}
		});
		c.add(b1);
	}
}
class Sound{
	public static void play(){
		File f1 = new File("a.wav"); 
		Player player=null;
		try{
			player = Manager.createRealizedPlayer(f1.toURI().toURL());
		}catch(Exception e){
			System.out.println("Error");
			System.exit(0);
		}
		player.prefetch();
		player.start();
	}
}