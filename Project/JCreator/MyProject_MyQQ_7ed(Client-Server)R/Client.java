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
	private static ArrayList<Client> list = new ArrayList<Client> ();
	private String number = null;
	private JApplet cg = null;
	private JFrame jj = null;
	public static ArrayList<Client> toList(){
		return list;
	}
	public String toNo(){
		return number;
	}
	public void disappear(){
		jj.dispose();
	}
	public Client(String no){
		number = no;
	}
	public void Client_G(){
		cg = new Client_GUI(number);
		jj = new JFrame("MyQQ-Online");
//		Display.run(jj,new Client_GUI(number),300,700);
		Display.run(jj,cg,300,700);
	}
	public void run(){
		((Client_GUI)cg).run();
	}
	public static void main(String[] arg){
		Client c =new Client("13121175");
		c.Client_G();
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
	public Client_GUI(String number){
		JOptionPane.showMessageDialog(null,"Welcome to MyQQ!","Log Success",JOptionPane.CLOSED_OPTION);
		try{
			client = new Socket("127.0.0.1",8899);
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new OutputStreamWriter(client.getOutputStream());
		}catch(Exception e){
			System.out.println("Client error1");
			e.printStackTrace();
		}
		no=number;
	}
	public void run(){
		myth = new MyThread();
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
			public void actionPerformed(ActionEvent e){
				try{
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
		b3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					myth.stop();	//not safe					
																	/*
																		一定要把线程关掉，不然即使界面消失线程依旧运行，继续运行会使其他Client线程出错
																		对于把myth指向null是一种很不好的习惯，但依照百度完全让这个线程销毁只有如此
																	*/
					myth = null;
					writer.write(no+"\n");
					writer.write("offline"+"\n");
					writer.write("Exit_MyQQ"+"eof\n");
					writer.flush();
					for(int i=0;i<Client.toList().size();i++){
						if(((Client)(Client.toList().get(i))).toNo().equals(no)){
							((Client)(Client.toList().get(i))).disappear();
							Client.toList().remove(i);
							break;
						}
					}
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
		MyThread(){
			super();
			start();
		}
		public void run(){
			while(true){
				System.out.println("Communication\'");
				try{
					StringBuilder sb = new StringBuilder();
					String s0 = br.readLine();
					if(s0.equals("addText")){				//更新房间信息的步骤一，也可以用来就收来自其他client的消息
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
					}else if(s0.equals("setText")){			//更新房间信息的步骤二
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
					}
				}catch(Exception e){
					System.out.println("Client error2");
					e.printStackTrace();
					break;
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