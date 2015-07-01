/*User 的显示界面，用于显示个人信息
 * Function:	1)触发显示租借记录事件,生成记录显示界面,调用RecordPane界面
 * 				2)触发修改个人信息事件,生成信息修改界面,调用Modify界面
 * 				3)触发刷新事件,将数据结构User用数据库最新数据更新,调用User.freshInformation()[控制器]
 */
package user;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import display.Display;

public class APanel extends JPanel{
	private JLabel l0 = new JLabel("Self_Information");
	private JLabel l1 = new JLabel("Name");
	private JLabel l2 = new JLabel("Number");
	private JLabel l3 = new JLabel("School");
	private JLabel l4 = new JLabel("Department");
	private JLabel l5 = new JLabel("Functions");
	private JTextField t1 = new JTextField(50);
	private JTextField t2 = new JTextField(50);
	private JTextField t3 = new JTextField(50);
	private JTextField t4 = new JTextField(50);
	private JButton b1 = new JButton("Record");
	private JButton b2 = new JButton("Modify");
	private JButton b3 = new JButton("Fresh");
	private User user = null;
	public void fresh(User u){
		user = u;
		t1.setText(user.Uname);
		t2.setText(user.Unumber);
		t3.setText(user.Uschool);
		t4.setText(user.Udepartment);
	}
	public APanel(User u){
		user = u;
		this.setBounds(5, 5, 235, 565);
		this.setBackground(Color.PINK);
		this.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		this.setLayout(null);
		l0.setBounds(60, 20, 100, 20);
		l0.setBackground(Color.MAGENTA);
		l0.setBorder(new LineBorder(Color.RED));
		l0.setOpaque(true);
		l0.setToolTipText("Self_Information");
		this.add(l0);
		l1.setBounds(10, 80, 40, 20);
		l1.setBackground(Color.GREEN);
		l1.setBorder(new LineBorder(Color.BLUE));
		l1.setOpaque(true);
		l1.setToolTipText("User's Name");
		this.add(l1);
		l2.setBounds(10, 130, 50, 20);
		l2.setBackground(Color.GREEN);
		l2.setBorder(new LineBorder(Color.BLUE));
		l2.setOpaque(true);
		l2.setToolTipText("User's Number");
		this.add(l2);
		l3.setBounds(10, 180, 50, 20);
		l3.setBackground(Color.GREEN);
		l3.setBorder(new LineBorder(Color.BLUE));
		l3.setOpaque(true);
		l3.setToolTipText("User's School");
		this.add(l3);
		l4.setBounds(10, 230, 70, 20);
		l4.setBackground(Color.GREEN);
		l4.setBorder(new LineBorder(Color.BLUE));
		l4.setOpaque(true);
		l4.setToolTipText("User's Department");
		this.add(l4);
		l5.setBounds(80, 300, 60, 20);
		l5.setBackground(Color.ORANGE);
		l5.setBorder(new LineBorder(Color.MAGENTA));
		l5.setOpaque(true);
		l5.setToolTipText("Functions");
		this.add(l5);
		t1.setBackground(Color.YELLOW);
		t1.setBorder(new LineBorder(Color.RED));
		t1.setBounds(100, 80, 100, 20);
		t1.setText(user.Uname);
		t1.setEditable(false);
		t1.setToolTipText("My name is");
		this.add(t1);
		t2.setBackground(Color.YELLOW);
		t2.setBorder(new LineBorder(Color.RED));
		t2.setBounds(100, 130, 100, 20);
		t2.setText(user.Unumber);
		t2.setEditable(false);
		t2.setToolTipText("My number is");
		this.add(t2);
		t3.setBackground(Color.YELLOW);
		t3.setBorder(new LineBorder(Color.RED));
		t3.setBounds(100, 180, 100, 20);
		t3.setText(user.Uschool);
		t3.setEditable(false);
		t3.setToolTipText("My school is");
		this.add(t3);
		t4.setBackground(Color.YELLOW);
		t4.setBorder(new LineBorder(Color.RED));
		t4.setBounds(100, 230, 100, 20);
		t4.setText(user.Udepartment);
		t4.setEditable(false);
		t4.setToolTipText("My department is");
		this.add(t4);
		b1.setToolTipText("Look For Records About Borrowing");
		b1.setBackground(Color.CYAN);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b1.setBounds(85, 350, 50, 20);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				RecordPane r = new RecordPane(user);
				Display.run(r.toFrame(), r, 600, 500);
			}
		});
		this.add(b1);
		b2.setToolTipText("Modify Self_Information");
		b2.setBackground(Color.MAGENTA);
		b2.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b2.setBounds(85, 400, 50, 20);
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Modify m = new Modify(user);
				Display.run(m.toFrame(), m, 300, 470);
			}
		});
		this.add(b2);
		b3.setBackground(Color.GREEN);
		b3.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b3.setBounds(85, 450, 50, 20);
		b3.setToolTipText("Fresh self Information");
		b3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				user.freshInformation();
			}
		});
		this.add(b3);
	}
}
