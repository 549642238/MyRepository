package MYQQ.monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import MYQQ.display.Display;
import MYQQ.communication.Communication;

public class Monitor extends JApplet{
	private JTextArea ta=new JTextArea(4,40);
	private JButton b=new JButton("Refresh");
	private JButton b1=new JButton("Close Server");
	public void init(){
		Container c=getContentPane();
		c.setLayout(new FlowLayout());
		ta.setBackground(Color.PINK);
		c.add(new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		b.setBackground(Color.GREEN);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ta.setText("\t	Online number: "+Communication.count+"\n\t	Current number"+Communication.currentCount);
				validate();	
			}
		});
		c.add(b);
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