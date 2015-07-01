/** Entire project enter into Login.java,Login provide a method to open log face
 *@author:czh;
 *@para:none;
 *@time:2015/4/5 16:35;
 *@attention:The speed of Type can't be very quick,or you'll see message overdisplay*/ 
package myqq.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

import myqq.log.Log;
import myqq.display.Display;
import myqq.monitor.Monitor;
import myqq.server.Server;

public class Login{
	private static final int port = 8899;
	protected static JFrame frame=new JFrame("Log Machine"); 
	protected static JFrame frame2=new JFrame("Monitor");
	public static void main(String[] arg){
		Display.run(frame,new LoginFace(),300,200);
		new Server(8899);
	} 
}
class LoginFace extends JApplet{
	private JButton b=new JButton("Start");
	private JButton b1=new JButton("Close");
	private JButton b2=new JButton("Server");
	private JTextArea ta=new JTextArea(3,20);
	public void init(){
		Container c=getContentPane();
		c.setLayout(new FlowLayout());
		b.setBackground(Color.GREEN);
		b.setBorder(new LineBorder(Color.BLUE));
		b.setToolTipText("Open LogFace");
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Log.run();
			}
		});
		c.add(b);
		b1.setBackground(Color.RED);
		b1.setBorder(new LineBorder(Color.BLUE));
		b1.setToolTipText("Exit System");
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Login.frame.dispose();
			}
		});
		c.add(b1);
		b2.setBackground(Color.BLUE);
		b2.setBorder(new LineBorder(Color.BLUE));
		b2.setToolTipText("Open Server");
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Display.run(Login.frame2,new Monitor(),200,200);
			}
		});
		c.add(b2);
		ta.setText("            Welcome to MyQQ Login");
		ta.setBackground(Color.YELLOW);
		ta.setEditable(false);
		c.add(ta);
		c.setBackground(Color.ORANGE);
	}
} 