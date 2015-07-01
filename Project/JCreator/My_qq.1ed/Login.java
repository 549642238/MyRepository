/** Entire project enter into Login.java,Login provide a method to open log face
 *@author:czh;
 *@para:none;
 *@time:2015/3/12 22:06;
 *@attention:The speed of Type can't be very quick,or you'll see message overdisplay*/ 
package MYQQ.login;

import MYQQ.log.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import MYQQ.display.Display;

public class Login{
	protected static JFrame frame=new JFrame("Log Machine"); 
	public static void main(String[] arg){
		Display.run(frame,new LoginFace(),300,250);
	} 
}
class LoginFace extends JApplet{
	private JButton b=new JButton("Start");
	private JButton b1=new JButton("Close");
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
		ta.setText("            Welcome to MyQQ Login");
		ta.setBackground(Color.YELLOW);
		ta.setEditable(false);
		c.add(ta);
		c.setBackground(Color.ORANGE);
	}
} 