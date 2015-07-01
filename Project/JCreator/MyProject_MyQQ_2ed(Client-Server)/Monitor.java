/** Function:Apply a new account;
 *			Delete a exist account
 *			Close all clients*/
package myqq.monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import javax.swing.border.*;

import myqq.display.Display;
import myqq.connect.Connect;

public class Monitor extends JApplet{
	private JButton b1=new JButton("Close Server");
	private JButton b2=new JButton("Application");
	private JButton b3=new JButton("UnRegister");
	protected static JFrame apply=new JFrame("Application");
	protected static JFrame unRegister=new JFrame("UnRegister");
	public void init(){
		Container c=getContentPane();
		c.setLayout(new FlowLayout());
		b2.setToolTipText("Apply a new count");
		b2.setBackground(Color.BLUE);
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Display.run(apply,new Apply(),300,400);
			}
		});
		c.add(b2);
		b3.setToolTipText("Delete a user");
		b3.setBackground(Color.ORANGE);
		b3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Display.run(unRegister,new UnRegister(),300,400);
			}
		});
		c.add(b3);
		b1.setToolTipText("Close all users' terminals");
		b1.setBackground(Color.RED);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		c.add(b1);
	}
	public static void main(String[] arg){
		Display.run(new JFrame("Server"),new Monitor(),400,300);
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
				if(t1.getText().equals("") || t2.getText().equals("") || t3.getText().equals("") || t4.getText().equals("") || t5.getText().equals("")){
					JOptionPane.showMessageDialog(null,"Cannot be null!","Fail",JOptionPane.CLOSED_OPTION);
					Monitor.apply.dispose();
				}else{
					Connect.connect();
					Connect.select(t1.getText());
					if(Connect.toNumber()==null){
					Connect.insert(t1.getText(),t2.getText(),t3.getText(),t4.getText(),t5.getText());
					JOptionPane.showMessageDialog(null,"Apply Success!","A new user born",JOptionPane.CLOSED_OPTION);
					Monitor.apply.dispose();
					}else{
						JOptionPane.showMessageDialog(null,"Account has been registered!","Regist fail",JOptionPane.ERROR_MESSAGE);
					}
					Connect.disConnect();
				}
			}
		});
		c.add(b);
		b1.setToolTipText("Exit this dialog");
		b1.setBackground(Color.RED);
		b1.setSize(15,10);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Monitor.apply.dispose();
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
				Connect.disConnect();
			}
		});
		c.add(b);
		b1.setToolTipText("Submit information");
		b1.setBackground(Color.GREEN);
		b1.setSize(15,10);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Monitor.unRegister.dispose();
			}
		});
		c.add(b1);
	}
}