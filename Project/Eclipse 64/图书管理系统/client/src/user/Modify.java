/*User 的个人信息修改界面，用于显示、修改个人信息
 * Function:	用界面的形式显示个人详细信息,调用User.modify()[控制器]
 */
package user;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import user.User;

public class Modify extends JApplet{
	private JFrame frame = new JFrame("Self_Information");
	private JButton b = new JButton("Exit");
	private JButton b2 = new JButton("Save");
	private JLabel l0 = new JLabel("Self_Information");
	private JLabel l1 = new JLabel("Name");
	private JLabel l2 = new JLabel("Number");
	private JLabel l3 = new JLabel("Password");
	private JLabel l4 = new JLabel("School");
	private JLabel l5 = new JLabel("Department");
	private JLabel l6 = new JLabel("Borrow");
	private JTextField t1 = new JTextField(50);
	private JTextField t2 = new JTextField(50);
	private JTextField t3 = new JTextField(50);
	private JTextField t4 = new JTextField(50);
	private JTextField t5 = new JTextField(50);
	private JTextField t6 = new JTextField(50);
	private User user = null;
	public JFrame toFrame(){
		return frame;
	}
	public Modify(User u){
		user = u;
	}
	public void init(){
		Container c = getContentPane();
		c.setBackground(Color.MAGENTA);
		c.setLayout(null);
		b.setBounds(150, 370, 60, 30);
		b.setBackground(Color.RED);
		b.setToolTipText("Exit this page");
		b.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.dispose();
			}
		});
		c.add(b);
		b2.setBounds(80, 370, 60, 30);
		b2.setBackground(Color.GREEN);
		b2.setToolTipText("Save your changes");
		b2.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				user.Uname = t1.getText().trim();
				user.Upasswd = t3.getText().trim();
				user.modify(user);
			}
		});
		c.add(b2);
		l0.setBounds(100, 20, 100, 25);
		l0.setBackground(Color.YELLOW);
		l0.setBorder(new LineBorder(Color.BLUE));
		l0.setOpaque(true);
		l0.setToolTipText("Self_Information");
		this.add(l0);
		l1.setBounds(20, 70, 70, 25);
		l1.setBackground(Color.GREEN);
		l1.setBorder(new LineBorder(Color.BLUE));
		l1.setOpaque(true);
		l1.setToolTipText("User's Name");
		this.add(l1);
		l2.setBounds(20, 120, 70, 25);
		l2.setBackground(Color.GREEN);
		l2.setBorder(new LineBorder(Color.BLUE));
		l2.setOpaque(true);
		l2.setToolTipText("User's Number");
		this.add(l2);
		l3.setBounds(20, 170, 70, 25);
		l3.setBackground(Color.GREEN);
		l3.setBorder(new LineBorder(Color.BLUE));
		l3.setOpaque(true);
		l3.setToolTipText("User's Password");
		this.add(l3);
		l4.setBounds(20, 220, 70, 25);
		l4.setBackground(Color.GREEN);
		l4.setBorder(new LineBorder(Color.BLUE));
		l4.setOpaque(true);
		l4.setToolTipText("User's School");
		this.add(l4);
		l5.setBounds(20, 270, 70, 25);
		l5.setBackground(Color.GREEN);
		l5.setBorder(new LineBorder(Color.BLUE));
		l5.setOpaque(true);
		l5.setToolTipText("User's Department");
		this.add(l5);
		l6.setBounds(20, 320, 70, 25);
		l6.setBackground(Color.GREEN);
		l6.setBorder(new LineBorder(Color.BLUE));
		l6.setOpaque(true);
		l6.setToolTipText("User's borrow number");
		this.add(l6);
		t1.setBackground(Color.ORANGE);
		t1.setBorder(new LineBorder(Color.RED));
		t1.setBounds(150, 70, 100, 25);
		t1.setText(user.Uname);
		t1.setToolTipText("My name is(Can be motified)");
		this.add(t1);
		t2.setBackground(Color.PINK);
		t2.setBorder(new LineBorder(Color.RED));
		t2.setBounds(150, 120, 100, 25);
		t2.setText(user.Unumber);
		t2.setEditable(false);
		t2.setToolTipText("My number is");
		this.add(t2);
		t3.setBackground(Color.ORANGE);
		t3.setBorder(new LineBorder(Color.RED));
		t3.setBounds(150, 170, 100, 25);
		t3.setText(user.Upasswd);
		t3.setToolTipText("My password is(Can be motified)");
		this.add(t3);
		t4.setBackground(Color.PINK);
		t4.setBorder(new LineBorder(Color.RED));
		t4.setBounds(150, 220, 100, 25);
		t4.setText(user.Uschool);
		t4.setEditable(false);
		t4.setToolTipText("My school is");
		this.add(t4);
		t5.setBackground(Color.PINK);
		t5.setBorder(new LineBorder(Color.RED));
		t5.setBounds(150, 270, 100, 25);
		t5.setText(user.Udepartment);
		t5.setEditable(false);
		t5.setToolTipText("My department is");
		this.add(t5);
		t6.setBackground(Color.PINK);
		t6.setBorder(new LineBorder(Color.RED));
		t6.setBounds(150, 320, 100, 25);
		t6.setText(user.Uborrow+"");
		t6.setEditable(false);
		t6.setToolTipText("My borrow number is");
		this.add(t6);
	}
}
