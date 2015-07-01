/** Run Log.java connected with Connect.java,Display.java,
 *Client.java*/ 
package myqq.log;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

import myqq.connect.Connect;
import myqq.display.Display;
import myqq.record.Record;
import myqq.server.Server;


public class Log{
	protected static JFrame f1=new JFrame("MyQQ Log");
	public static void run(){
		Display.run(f1,new LogFace(),300,140);
		new Server();
	} 
}
class LogFace extends JApplet{
	private static Connection con=null;
	private static ResultSet rs=null; 
	private static Statement statement=null; 
	private JButton b1=new JButton("Log");
	private JButton b2=new JButton("Exit");
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
				String number=t1.getText();
				String passwd=t2.getText();
				try{
					statement = con.createStatement();
        			String sql = "select * from people where number="+number;	
        			rs = statement.executeQuery(sql);	
        			if(rs.next()==false){
        				JOptionPane.showMessageDialog(null,"number not exist!","Log Failed",JOptionPane.ERROR_MESSAGE);
        			}else{
        				/*If log success,show a Message,then pass the number to MyQQ Pane,and hide the log window,then display MyQQ Pane*/
        				if(passwd.equals(rs.getString("passwd"))){
        					JOptionPane.showMessageDialog(null,"Welcome to MyQQ!","Log Success",JOptionPane.CLOSED_OPTION);
        					Record.addUser(number);
        					Log.f1.dispose();
        				}else{
        					JOptionPane.showMessageDialog(null,"passwd is wrong!","Log Failed",JOptionPane.ERROR_MESSAGE);
        				}
        			}
				}catch(Exception ep){
					System.out.println("Log error1");
				}
		//		Connect.disConnect();
			}
		});
		b1.setBackground(Color.GREEN);
		b1.setBorder(new LineBorder(Color.RED));
		b1.setToolTipText("Log in MyQQ");
		c.add(b1);
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
        			con.close();
				}catch(Exception ep){
					System.out.println("Log error2");
				}
				Log.f1.dispose();
			}
		});
		b2.setBackground(Color.RED);
		b2.setBorder(new LineBorder(Color.BLUE));
		b2.setToolTipText("Exit MyQQ");
		c.add(b2);
	}
}