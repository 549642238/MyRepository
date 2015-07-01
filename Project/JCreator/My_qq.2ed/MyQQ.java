/** MyQQ's main window*/
package MYQQ.myqq;

import MYQQ.connect.Connect;
import MYQQ.display.Display;
import MYQQ.mythread.MyThread;
import MYQQ.communication.Communication;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.basic.*;
import javax.swing.border.*;
import java.io.*;

public class MyQQ extends JApplet{
	protected static JFrame f2=null;
	protected static String no=null;
	protected String noTemp=null;
	private boolean b=true;
	private Icon picture=new ImageIcon("1.jpg");
	private JTextArea ta1=new JTextArea(20,20);
	private JTextArea ta2=new JTextArea(2,25);
	private JButton pb=new JButton(picture);
	private JButton b1=new JButton("Send");
	private JButton b2=new JButton("Clear");
	private JButton b3=new JButton("OnLine");
	private JTextField t=new JTextField(10);
	private MyThread my=new MyThread(ta1);
	public static void pass(String s){
		no=s;
	}
	public void init(){
		Communication.currentCount++;
		Communication.count++;
		noTemp=no;
		Connect.connect();
		Container c=getContentPane();
		c.setLayout(new FlowLayout());
		pb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				f2=new JFrame("MyQQ");
				Connect.connect();
				Connect.select(noTemp);
				Display.run(f2,new Self_Information(),300,400);
			}
		});
		pb.setBorder(new LineBorder(Color.ORANGE));
		pb.setToolTipText("Self information");
		c.add(pb);
		Connect.select(no);
		t.setText(no);
		t.setBorder(new LineBorder(Color.BLUE));
		t.setToolTipText("You can call me:");
		t.setBackground(Color.PINK);
		t.setEditable(false);
		c.add(t);
		b3.setBackground(Color.GREEN);
		b3.setBorder(new LineBorder(Color.BLUE));
		b3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(b==true){
					b3.setBackground(Color.GRAY);
					b3.setText("OffLine");
					my.stop();
					Communication.count--;
					Communication.currentCount--;
					b=false;
				}else{
					b3.setBackground(Color.GREEN);
					b3.setText("OnLine");
					Communication.currentCount++;
					Communication.count++;
					my=new MyThread(ta1);
					b=true;
				}
			}
		});
		c.add(b3);
		ta1.setBackground(Color.LIGHT_GRAY);
		ta1.setBorder(new LineBorder(Color.RED));
		ta1.setToolTipText("Talking Records");
		ta1.setEditable(false);
		c.add(new JScrollPane(ta1,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		ta2.setBackground(Color.PINK);
		ta2.setBorder(new LineBorder(Color.BLUE));
		c.add(new JScrollPane(ta2,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		b1.setBackground(Color.GREEN);
		b1.setBorder(new LineBorder(Color.YELLOW));
		b1.setToolTipText("Send message");
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Connect.select(noTemp);
				if(b==true){
					Communication.s+=Connect.toName()+" :"+ta2.getText()+"\n";
					Communication.currentCount=0;
				}	
				ta2.setText("");
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
	}
	public static void main(String[] arg){
		Display.run(new JFrame("MyQQ"),new MyQQ(),300,140);
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
				Connect.update(MyQQ.no,"telephone",ss);
	//			System.out.println("111");
				JOptionPane.showMessageDialog(null,"Save Success!","Save",JOptionPane.CLOSED_OPTION);
				Connect.select(MyQQ.no);
				MyQQ.f2.dispose();
			}
		});
		c.add(b);
		b1.setBackground(Color.RED);
		b1.setSize(15,10);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				MyQQ.f2.dispose();
			}
		});
		c.add(b1);
	}
}