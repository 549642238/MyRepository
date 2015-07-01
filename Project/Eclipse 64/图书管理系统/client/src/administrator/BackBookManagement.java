/*Administrator 的显示界面，用于归还书籍
 * Function:	1)显示归还信息，调用admin.back()[控制器]
 * 				3)退出系统
 */
package administrator;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import administrator.Administrator;
import display.Display;

public class BackBookManagement extends JApplet{
	private Administrator admin = null;
	private JFrame frame = new JFrame("BookBacking System");
	private JLabel l1 = new JLabel("Book_ISBN");
	private JLabel l2 = new JLabel("User_Number");
	private JTextField t1 = new JTextField(20);
	private JTextField t2 = new JTextField(20);
	private JButton b1 = new JButton("Return");
	private JButton b2 = new JButton("Exit");
	public BackBookManagement(Administrator a){
		admin = a;
	}
	public JFrame toFrame(){
		return frame;
	}
	public void init(){
		Container c = getContentPane();
		c.setLayout(null);
		c.setBackground(Color.PINK);
		l1.setBackground(Color.YELLOW);
		l1.setOpaque(true);
		l1.setBorder(new LineBorder(Color.RED));
		l1.setToolTipText("Book ISBN:");
		l1.setBounds(50, 30, 100, 25);
		c.add(l1);
		t1.setBackground(Color.CYAN);
		t1.setBorder(new LineBorder(Color.BLUE));
		t1.setBounds(200, 30, 200, 25);
		t1.setToolTipText("Input the book's ISBN");
		c.add(t1);
		l2.setBackground(Color.YELLOW);
		l2.setOpaque(true);
		l2.setBorder(new LineBorder(Color.RED));
		l2.setToolTipText("User Number:");
		l2.setBounds(50, 100, 100, 25);
		c.add(l2);
		t2.setBackground(Color.CYAN);
		t2.setBorder(new LineBorder(Color.BLUE));
		t2.setBounds(200, 100, 200, 25);
		t2.setToolTipText("Input the user's Number");
		c.add(t2);
		b1.setBackground(Color.GREEN);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b1.setToolTipText("Click to return the book");
		b1.setBounds(150, 200, 80, 25);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(admin.back(t1.getText().trim(),t2.getText().trim())){
					JOptionPane.showMessageDialog(null,"You have return the book!","Back Success",JOptionPane.CLOSED_OPTION);
				}else{
					JOptionPane.showMessageDialog(null,"1)Input illegal(Input noting or else)\n2)The user or book not exists\n3)The book has been returned!","Back Failed",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		c.add(b1);
		b2.setBackground(Color.RED);
		b2.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLACK)));
		b2.setToolTipText("Click to Exit this page");
		b2.setBounds(270, 200, 80, 25);
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.dispose();
			}
		});
		c.add(b2);
	}
	public static void main(String[] arg){
		BackBookManagement bbm = new BackBookManagement(null);
		Display.run(bbm.toFrame(), bbm, 500, 400);
	}
}
