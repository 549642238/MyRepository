/*Administrator 的显示界面，用于管理User
 * Function:	1)显示查询的用户信息,调用admin.searchUser()[控制器]
 * 				2)键入添加的用户信息,调用admin.insertUser()[控制器]
 * 				3)键入用户的修改信息,调用admin.updateUser()[控制器]
 * 				4)根据输入的Number删除用户,调用admin.deleteUser()[控制器]
 */
package administrator;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import administrator.Administrator;
import display.Display;

public class UserManagement extends JApplet{
	private Administrator admin = null;
	private JFrame frame = new JFrame("User Management");
	private JTextField t1 = new JTextField(20);
	private JTextField t2 = new JTextField(20);
	private JTextField t3 = new JTextField(20);
	private JTextField t4 = new JTextField(20);
	private JTextField t5 = new JTextField(20);
	private JTextField t6 = new JTextField(20);
	private JLabel l1 = new JLabel("Number");
	private JLabel l2 = new JLabel("Name");
	private JLabel l3 = new JLabel("Password");
	private JLabel l4 = new JLabel("School");
	private JLabel l5 = new JLabel("Department");
	private JLabel l6 = new JLabel("Borrow");
	private JButton b0 = new JButton("Record");
	private JButton b1 = new JButton("Search");
	private JButton b2 = new JButton("Insert");
	private JButton b3 = new JButton("Modify");
	private JButton b4 = new JButton("Delete");
	private JButton b5 = new JButton("Exit");
	public JFrame toFrame(){
		return frame;
	}
	public static void main(String[] arg){
		UserManagement um = new UserManagement(null);
		Display.run(um.toFrame(), um, 400, 500);
	}
	public UserManagement(Administrator a){
		admin = a;
	}
	public void init(){
		Container c = getContentPane();
		c.setLayout(null);
		c.setBackground(Color.YELLOW);
		b0.setBackground(Color.MAGENTA);
		b0.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b0.setToolTipText("Click to Search the user");
		b0.setBounds(165, 50, 70, 25);
		b0.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				UserDataStructure ud = null;
				String s = t1.getText();
				if(s.equals("")){
					JOptionPane.showMessageDialog(null,"Please Input user's number!","Search Failed",JOptionPane.ERROR_MESSAGE);
				}else{
					ud = admin.searchUser(s);
					if(ud == null){
						JOptionPane.showMessageDialog(null,"The user not exists!","Search Failed",JOptionPane.ERROR_MESSAGE);
					}else{
						RecordPane r = new RecordPane(ud,admin);
						Display.run(r.toFrame(), r, 600, 500);
					}
				}
			}
		});
		c.add(b0);
		l1.setBackground(Color.PINK);
		l1.setBorder(new LineBorder(Color.BLUE));
		l1.setOpaque(true);
		l1.setBounds(70, 10, 80, 25);
		l1.setToolTipText("User Number:");
		c.add(l1);
		t1.setBackground(Color.LIGHT_GRAY);
		t1.setBounds(150, 10, 100, 25);
		t1.setBorder(new LineBorder(Color.RED));
		t1.setToolTipText("The user's number(You mustn't change it)");
		c.add(t1);
		b1.setBackground(Color.GREEN);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b1.setToolTipText("Click to Search the user");
		b1.setBounds(300, 10, 70, 25);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String s = t1.getText();
				if(s.equals("")){
					JOptionPane.showMessageDialog(null,"You have no Input!","Search Failed",JOptionPane.ERROR_MESSAGE);
				}else{
					UserDataStructure user = admin.searchUser(s);
					if(user == null){
						JOptionPane.showMessageDialog(null,"The user not exists!","Search Failed",JOptionPane.ERROR_MESSAGE);
					}else{
						t2.setText(user.Uname);
						t3.setText(user.Upasswd);
						t4.setText(user.Uschool);
						t5.setText(user.Udepartment);
						t6.setText(user.Uborrow+"");
					}
				}
			}
		});
		c.add(b1);
		l2.setBackground(Color.PINK);
		l2.setBorder(new LineBorder(Color.BLUE));
		l2.setOpaque(true);
		l2.setBounds(30, 100, 80, 25);
		l2.setToolTipText("User Name:");
		c.add(l2);
		t2.setBackground(Color.LIGHT_GRAY);
		t2.setBounds(160, 100, 200, 25);
		t2.setBorder(new LineBorder(Color.RED));
		t2.setToolTipText("The User's name(Cannot change)");
		c.add(t2);
		l3.setBackground(Color.PINK);
		l3.setBorder(new LineBorder(Color.BLUE));
		l3.setOpaque(true);
		l3.setBounds(30, 150, 80, 25);
		l3.setToolTipText("User Password:");
		c.add(l3);
		t3.setBackground(Color.LIGHT_GRAY);
		t3.setBounds(160, 150, 200, 25);
		t3.setBorder(new LineBorder(Color.RED));
		c.add(t3);
		l4.setBackground(Color.PINK);
		l4.setBorder(new LineBorder(Color.BLUE));
		l4.setOpaque(true);
		l4.setBounds(30, 200, 80, 25);
		l4.setToolTipText("User School:");
		c.add(l4);
		t4.setBackground(Color.CYAN);
		t4.setBounds(160, 200, 200, 25);
		t4.setBorder(new LineBorder(Color.RED));
		t4.setToolTipText("Input the user's school");
		c.add(t4);
		l5.setBackground(Color.PINK);
		l5.setBorder(new LineBorder(Color.BLUE));
		l5.setOpaque(true);
		l5.setBounds(30, 250, 80, 25);
		l5.setToolTipText("User Department:");
		c.add(l5);
		t5.setBackground(Color.CYAN);
		t5.setBounds(160, 250, 200, 25);
		t5.setBorder(new LineBorder(Color.RED));
		t5.setToolTipText("Input the user's department");
		c.add(t5);
		l6.setBackground(Color.PINK);
		l6.setBorder(new LineBorder(Color.BLUE));
		l6.setOpaque(true);
		l6.setBounds(30, 300, 80, 25);
		l6.setToolTipText("User Borrow Number:");
		c.add(l6);
		t6.setBackground(Color.CYAN);
		t6.setBounds(160, 300, 200, 25);
		t6.setBorder(new LineBorder(Color.RED));
		t6.setToolTipText("Input the user's borrow number,you must input a number below 3(include)");
		c.add(t6);
		b2.setBackground(Color.GREEN);
		b2.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b2.setToolTipText("Add a new book into the library");
		b2.setBounds(100, 350, 70, 25);
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(admin.insertUser(t1.getText().trim(),t2.getText().trim(),t3.getText().toString().trim(),t4.getText().trim(),t5.getText().trim(),t6.getText().trim())){
					JOptionPane.showMessageDialog(null,"A new User has been added into library!","Add Success",JOptionPane.CLOSED_OPTION);
				}else{
					JOptionPane.showMessageDialog(null,"1)The user's number has been existed!\n2)The user's information is not complete!\n3)Input Wrong(The Input is too long、Input form is illegal or else)!","Add Failed",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		c.add(b2);
		b3.setBackground(Color.ORANGE);
		b3.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b3.setToolTipText("Modify an existing user in the library,Cannot Modify Number,or you will failed");
		b3.setBounds(200, 350, 70, 25);
		b3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(admin.updateUser(t1.getText().trim(),t4.getText().trim(),t5.getText().trim(),t6.getText().trim())){
					JOptionPane.showMessageDialog(null,"The user is updated OK!","Modify Success",JOptionPane.CLOSED_OPTION);
				}else{
					JOptionPane.showMessageDialog(null,"1)Input Illegal\n2)Number cannot be changed\n3)The book not exist!","Modify Failed",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		c.add(b3);
		b4.setBackground(Color.PINK);
		b4.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b4.setToolTipText("Delete an existing user in the library");
		b4.setBounds(100, 400, 70, 25);
		b4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(admin.deleteUser(t1.getText().trim())){
					JOptionPane.showMessageDialog(null,"The user is deleted OK!","Delete Success",JOptionPane.CLOSED_OPTION);
				}else{
					JOptionPane.showMessageDialog(null,"1)The user not exist\n2)The user not return book\n3)The user is online!","Delete Failed",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		c.add(b4);
		b5.setBackground(Color.RED);
		b5.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b5.setToolTipText("Exit this page,Please pay attention that you have saved the changes");
		b5.setBounds(200, 400, 70, 25);
		b5.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.dispose();
			}
		});
		c.add(b5);
	}
}
