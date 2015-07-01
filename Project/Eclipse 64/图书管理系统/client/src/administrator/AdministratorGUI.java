/*Administrator 的显示界面，用于显示个人信息、功能选项
 * Function:	1)生成APanel,APanel是功能选项界面
 * 				3)退出系统
 */
package administrator;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import administrator.Administrator;
import display.Display;

public class AdministratorGUI extends JApplet {
	private JFrame frame = new JFrame("Xidian Library");
	private APanel panela = null;
	private JLabel l1 = new JLabel("Number");
	private JLabel l2 = new JLabel("Name");
	private JTextField t1 = new JTextField(10);
	private JTextField t2 = new JTextField(10);
	private JTextArea ta = new JTextArea(30,3);
	private JButton b1 = new JButton("Exit");
	private Administrator admin = null;
	public JFrame toFrame(){
		return frame;
	}
	public AdministratorGUI(Administrator a){
		admin = a;
	}
	public void init(){
		Container c = getContentPane();
		c.setLayout(null);
		c.setBackground(Color.CYAN);
		l1.setBackground(Color.ORANGE);
		l1.setBorder(new LineBorder(Color.RED));
		l1.setOpaque(true);
		l1.setToolTipText("Administrator Number");
		l1.setBounds(20, 10, 60, 25);
		c.add(l1);
		t1.setBackground(Color.PINK);
		t1.setBorder(new LineBorder(Color.BLUE));
		t1.setToolTipText("Account Number is:");
		t1.setBounds(80, 10, 100, 25);
		t1.setText(admin.Anumber);
		t1.setEditable(false);
		c.add(t1);
		l2.setBackground(Color.ORANGE);
		l2.setBorder(new LineBorder(Color.RED));
		l2.setOpaque(true);
		l2.setToolTipText("Administrator Name");
		l2.setBounds(20, 40, 60, 25);
		c.add(l2);
		t2.setBackground(Color.PINK);
		t2.setBorder(new LineBorder(Color.BLUE));
		t2.setToolTipText("My Name is:");
		t2.setBounds(80, 40, 100, 25);
		t2.setText(admin.Aname);
		t2.setEditable(false);
		c.add(t2);
		ta.setBackground(Color.ORANGE);
		ta.setEditable(false);
		ta.setText("Today is:\n\t"+new Date()+"\n");
		ta.setBorder(new LineBorder(Color.RED));
		ta.setToolTipText("Time");
		JScrollPane sp = new JScrollPane(ta);
		sp.setBounds(200, 10, 270, 60);
		c.add(sp);
		b1.setBackground(Color.RED);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b1.setToolTipText("Exit Library");
		b1.setBounds(230, 440, 60, 25);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				admin.exit();
			}
		});
		c.add(b1);
		panela = new APanel(admin);
		c.add(panela);
	}
}
class APanel extends JPanel{
	private JButton b1 = new JButton("图书管理");
	private JButton b2 = new JButton("用户管理");
	private JButton b3 = new JButton("还书服务");
	private Administrator admin = null;
	public APanel(Administrator a){
		admin = a;
		this.setBackground(Color.YELLOW);
		this.setLayout(new GridLayout(3,1,20,30));
		this.setBounds(20, 90, 460, 300);
		this.add(b1);
		this.add(b2);
		this.add(b3);
		b1.setBackground(Color.ORANGE);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b1.setToolTipText("添加、修改、删除图书");
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BookManagement bm = new BookManagement(admin);
				Display.run(bm.toFrame(), bm, 400, 500);
			}
		});
		b2.setBackground(Color.RED);
		b2.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b2.setToolTipText("添加、修改、删除用户(User)");
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				UserManagement um = new UserManagement(admin);
				Display.run(um.toFrame(), um, 400, 500);
			}
		});
		b3.setBackground(Color.GREEN);
		b3.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b3.setToolTipText("归还图书");
		b3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BackBookManagement bbm = new BackBookManagement(admin);
				Display.run(bbm.toFrame(), bbm, 500, 400);
			}
		});
	}
}