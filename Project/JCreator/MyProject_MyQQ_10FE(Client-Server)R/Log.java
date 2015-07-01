/**  Entire project enter into Log.java,Log provide a method to open log face
 *	@Author:czh;
 *	@Para:none;
 *	@Time:2015/4/9 19:29;
 *	@Attention:Support more than one online,but the number of online clients can't
 *	 beyond 50,otherwise the System may react slowly
 *	@Composition:
 *	 1)Client_Side:
 *		a.Client:	Client has data about a client's information,function,and GUI compositions
 *		b.Display:	A plug-in unit support showing CLient_GUI
 *		c.Log:		Supporting a method to connect to server,also support funtion:
 *						i)Creating a new account
 *						ii)Deleting an existing account
 *						iii)If a client log success,the log_gui exit automatically
 *		d.Connect:	Connect to far database
 **/
package myqq.log;

import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

import myqq.connect.Connect;
import myqq.client.Client;
import myqq.display.Display;


public class Log{
	protected static JFrame f1=new JFrame("MyQQ Log");
	public static void Create(String no){
		new Client(no);
	}
	public static void main(String[] args){
		Display.run(f1,new LogFace(),300,140);
	} 
}
class LogFace extends JApplet{
	private static Connection con=null;
	private static ResultSet rs=null; 
	private static Statement statement=null;
	protected static JFrame apply = new JFrame("Apply");
	protected static JFrame unregist = new JFrame("UnRegister");
	private JButton b1=new JButton("Log");
	private JButton b2=new JButton("Exit");
	private JButton b3 = new JButton("Apply");
	private JButton b4 = new JButton("UnRegiste");
	private JTextField t1=new JTextField(20);
	private JTextField t2=new JTextField(20);
	private JLabel L1=new JLabel("Number");
	private JLabel L2=new JLabel("Passwd");
	private final String ipv4 = "localhost";		//server ip
	public void init(){
	   	Container c=getContentPane();
	   	c.setLayout(new FlowLayout());
	   	L1.setBorder(new LineBorder(Color.ORANGE));
	   	c.add(L1);
	   	t1.setToolTipText("Input log number");
	   	c.add(t1);
	   	L2.setBorder(new LineBorder(Color.ORANGE));
	   	c.add(L2);
	   	t2.setToolTipText("Input log passwd");
	   	c.add(t2);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				con=Connect.connect();
				String number=t1.getText().trim();
				String passwd=t2.getText().trim();
				try{
					statement = con.createStatement();
        			String sql = "select * from people where number="+number;	
        			rs = statement.executeQuery(sql);	
        			if(rs.next()==false){
        				JOptionPane.showMessageDialog(null,"number not exist!","Log Failed",JOptionPane.ERROR_MESSAGE);
        			}else{
        				/*If log success,show a Message,then pass the number to MyQQ Pane,and hide the log window,then display MyQQ Pane*/
        				if(passwd.equals(rs.getString("passwd"))){
        					try{
        						Socket toServer = new Socket(ipv4,8189);		/*	
																						之所以再连接一个端口是为了告诉服务器是谁上线了，如果只要
																						密码账户正确就直接连接服务器，那服务器在它连接上之前如何去
																						判断这个账户是否已经在线，二次建立端口就可以在主端口连接前
																						判断账户是否在线不在线就不让它连接主端口（8899）
																				*/
								Writer writer = new OutputStreamWriter(toServer.getOutputStream());
								writer.write(number+"\n");
								writer.flush();
								BufferedReader br = new BufferedReader(new InputStreamReader(toServer.getInputStream()));
								String s = br.readLine();
								writer.write("Copy\n");
								writer.flush();
								Client cl = null;
								if(s.equals("Yes")){
									Log.f1.dispose();								//登陆成功后Log框自动退出
									cl = new Client(number);
									cl.Client_G();
								}else{
									JOptionPane.showMessageDialog(null,"Your account is online!","Log Failed",JOptionPane.ERROR_MESSAGE);
								}
																					/*
																						前面已经判断过s是否等于"Yes",这里再一次判断理由如下：
																						你发现两次判断之间有一条write的语句，翻看server代码
																						就会发现不论是server向客户端写入了"Yes"还是"No"它都要
																						等客户端写一个应答回复它，如果Client收到"No",好，两个
																						if语句都不执行，直接写"Copy"退出Log界面并受到来自Serer
																						的弹框提示"账户已在线"
																					*/
        					}catch(Exception ee){
        						System.out.println("Log error 0");
        						ee.printStackTrace();
        					}
        				}else{
        					JOptionPane.showMessageDialog(null,"passwd is wrong!","Log Failed",JOptionPane.ERROR_MESSAGE);
        				}
        			}
				}catch(Exception ep){
					System.out.println("Log error1");
				}
			}
		});
		b1.setBackground(Color.GREEN);
		b1.setBorder(new LineBorder(Color.RED));
		b1.setToolTipText("Log in MyQQ");
		c.add(b1);
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Log.f1.dispose();
			}
		});
		b2.setBackground(Color.RED);
		b2.setBorder(new LineBorder(Color.BLUE));
		b2.setToolTipText("Exit MyQQ");
		c.add(b2);
		b3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Display.run(apply,new Apply(),300,400);
			}
		});
		b3.setBackground(Color.ORANGE);
		b3.setBorder(new LineBorder(Color.BLACK));
		b3.setToolTipText("Apply a new account");
		c.add(b3);
		b4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Display.run(unregist,new UnRegister(),300,400);
			}
		});
		b4.setBackground(Color.YELLOW);
		b4.setBorder(new LineBorder(Color.WHITE));
		b4.setToolTipText("Delete an exist account");
		c.add(b4);
	}
}
class Apply extends JApplet{
	private JTextArea t0=new JTextArea(1,30);
	private JLabel L1=new JLabel("number");
	private JLabel L2=new JLabel("passwd");
	private JLabel L3=new JLabel("name");
	private JLabel L4=new JLabel("sex");
	private JLabel L5=new JLabel("teleph");
	private JTextField t1=new JTextField(20);
	private JTextField t2=new JTextField(20);
	private JTextField t3=new JTextField(20);
	private JTextField t4=new JTextField(20);
	private JTextField t5=new JTextField(20);
	private JButton b=new JButton("Register");
	private JButton b1=new JButton("Close");
	public void init(){
		Container c=getContentPane();
		c.setLayout(new FlowLayout());
		c.setBackground(Color.PINK);
		t0.setEditable(false);
		t0.setText("\t    Personal Information");
		t0.setBackground(Color.CYAN);
		t0.setBorder(new LineBorder(Color.BLACK));
		c.add(t0);
		L1.setBackground(Color.BLUE);
		L1.setBorder(new LineBorder(Color.GREEN));
		c.add(L1);
		t1.setBackground(Color.YELLOW);
		t1.setBorder(new LineBorder(Color.GRAY));
		c.add(t1);
		L2.setBackground(Color.BLUE);
		L2.setBorder(new LineBorder(Color.GREEN));
		c.add(L2);
		t2.setBackground(Color.YELLOW);
		t2.setBorder(new LineBorder(Color.GRAY));	
		c.add(t2);
		L3.setBackground(Color.BLUE);
		L3.setBorder(new LineBorder(Color.GREEN));
		c.add(L3);
		t3.setBackground(Color.YELLOW);
		t3.setBorder(new LineBorder(Color.GRAY));
		c.add(t3);
		L4.setBackground(Color.BLUE);
		L4.setBorder(new LineBorder(Color.GREEN));
		c.add(L4);
		t4.setBackground(Color.YELLOW);
		t4.setBorder(new LineBorder(Color.GRAY));
		c.add(t4);
		L5.setBackground(Color.BLUE);
		L5.setBorder(new LineBorder(Color.GREEN));
		c.add(L5);
		t5.setBackground(Color.YELLOW);
		t5.setBorder(new LineBorder(Color.GRAY));
		c.add(t5);
		b.setToolTipText("Submit information");
		b.setBackground(Color.BLUE);
		b.setSize(15,10);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(t1.getText().trim().equals("") || t2.getText().trim().equals("") || t3.getText().trim().equals("") || t4.getText().trim().equals("") || t5.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null,"Cannot be null!","Fail",JOptionPane.CLOSED_OPTION);
					LogFace.apply.dispose();
				}else{
					Connect.connect();
					Connect.select(t1.getText().trim());
					if(Connect.toNumber()==null){
					Connect.insert(t1.getText().trim(),t2.getText().trim(),t3.getText().trim(),t4.getText().trim(),t5.getText().trim());
					JOptionPane.showMessageDialog(null,"Apply Success!","A new user born",JOptionPane.CLOSED_OPTION);
					LogFace.apply.dispose();
					}else{
						JOptionPane.showMessageDialog(null,"Account has been registered!","Regist fail",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		c.add(b);
		b1.setToolTipText("Exit this dialog");
		b1.setBackground(Color.RED);
		b1.setSize(15,10);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				LogFace.apply.dispose();
			}
		});
		c.add(b1);
	}
}
class UnRegister extends JApplet{
	private JLabel L1=new JLabel("number");
	private JLabel L2=new JLabel("passwd");
	private JTextField t1=new JTextField(20);
	private JTextField t2=new JTextField(20);
	private JButton b=new JButton("UnRegister");
	private JButton b1=new JButton("Close");
	public void init(){
		Container c=getContentPane();
		c.setLayout(new FlowLayout());
		c.setBackground(Color.CYAN);
		L1.setBackground(Color.BLUE);
		L1.setBorder(new LineBorder(Color.GREEN));
		c.add(L1);
		t1.setBackground(Color.YELLOW);
		t1.setBorder(new LineBorder(Color.GRAY));
		c.add(t1);
		L2.setBackground(Color.BLUE);
		L2.setBorder(new LineBorder(Color.GREEN));
		c.add(L2);
		t2.setBackground(Color.YELLOW);
		t2.setBorder(new LineBorder(Color.GRAY));	
		c.add(t2);
		b.setToolTipText("Submit information");
		b.setBackground(Color.RED);
		b.setSize(15,10);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Connect.connect();
				Connect.select(t1.getText());
				if(Connect.toNumber()==null){
					JOptionPane.showMessageDialog(null,"Account not exist!","Failed",JOptionPane.ERROR_MESSAGE);
				}else if(Connect.toPasswd().equals(t2.getText())!=true){
					JOptionPane.showMessageDialog(null,"Passwd wrong!","Failed",JOptionPane.ERROR_MESSAGE);
				}else{
					Connect.delete(t1.getText());
					JOptionPane.showMessageDialog(null,"UnRegister finished!","Success",JOptionPane.CLOSED_OPTION);
				}
			}
		});
		c.add(b);
		b1.setToolTipText("Submit information");
		b1.setBackground(Color.GREEN);
		b1.setSize(15,10);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				LogFace.unregist.dispose();
			}
		});
		c.add(b1);
	}
}