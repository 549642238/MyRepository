/**  Entire project enter into Log.java,Log provide a method to open log face
 *	@Author:czh;
 *	@Para:none;
 *	@Edition:3.11 (FED)P：	1、比第10版代码简化，改变了客户端上线检查机制(从Log改到Client，只使用一个Socket端口就OK)
 *							2、将客户端和服务器通讯协议做了明确规定，不像第10版那样模糊
 *							3、服务器代码更加清晰，移除了Client镜像，占用内存更小	
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
package log;

import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

import connect.Connect;
import client.Client;
import display.Display;


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
								Log.f1.dispose();								//登陆成功后Log框自动退出
								new Client(number);
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