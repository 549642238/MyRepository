/*客户端登录开始界面
 * @author 			czh
 * @complete_time	2015/6/4
 * @take_time		4days
 * @long			1813Lines
 * @Explaination	1)登录library服务器，可以通过选择身份来确定登录后的界面
 * 					2)不提供账号注册，账号注册由管理员完成，针对本校的学生
 * 					3)登录界面不连接服务器，登陆后根据身份会生成不同用户(User、Admin),
 * 					用户连接服务器，服务器连接数据库判断账户密码等正确性决定是否显示功能界面
 * 					4)需要注意,登录需要给不同用户传递连接服务器的参数，如端口号和地址
 * @start			客户端开始程序
 */
package log;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import user.User;
import administrator.Administrator;
import display.Display;

public class Log extends JApplet{
	private final String ipv4 = "localhost";				//服务端ip
	private final int port = 8899;							//服务端端口
	private JFrame frame = new JFrame("Library Login");
	private final String[] id = {"User","Administrator"};
	private JComboBox<String> cb = new JComboBox<String>(id);
	private JButton b1=new JButton("Log");
	private JButton b2=new JButton("Exit");
	private JTextField t1=new JTextField(20);
	private JTextField t2=new JTextField(20);
	private JLabel L1=new JLabel("Number");
	private JLabel L2=new JLabel("Passwd");
	private JButton b0 = new JButton("picture");
	public JFrame toFrame(){
		return frame;
	}
	public static void main(String[] arg){
		Log log = new Log();
		Display.run(log.toFrame(), log, 400, 500);
	}
	public void init(){
		Container c = getContentPane();
		c.setLayout(null);
		c.setBackground(Color.PINK);
		b0.setIcon(new ImageIcon("pictures&sounds\\a.jpg"));
		b0.setBounds(2, 2, 390, 300);
		b0.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b0.setToolTipText("Welcome to Xidian library");
		c.add(b0);
		L1.setBackground(Color.GREEN);
		L1.setOpaque(true);
		L1.setBorder(new LineBorder(Color.BLUE));
		L1.setToolTipText("Number");
		L1.setBounds(20, 310, 80, 25);
		c.add(L1);
		L2.setBackground(Color.GREEN);
		L2.setOpaque(true);
		L2.setBorder(new LineBorder(Color.BLUE));
		L2.setToolTipText("Password");
		L2.setBounds(20, 340, 80, 25);
		c.add(L2);
		t1.setBackground(Color.CYAN);
		t1.setToolTipText("Input your account number");
		t1.setBorder(new LineBorder(Color.RED));
		t1.setBounds(110, 310, 150, 25);
		c.add(t1);
		t2.setBackground(Color.CYAN);
		t2.setToolTipText("Input your password");
		t2.setBorder(new LineBorder(Color.RED));
		t2.setBounds(110, 340, 150, 25);
		c.add(t2);
		cb.setBackground(Color.ORANGE);
		cb.setBorder(new LineBorder(Color.BLUE));
		cb.setToolTipText("Please choose a identify to log");
		cb.setBounds(270, 310, 120, 25);
		c.add(cb);
		b1.setBackground(Color.GREEN);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b1.setBounds(100, 420, 50, 25);
		b1.setToolTipText("Click to log");
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(cb.getSelectedItem().toString().equals("User")){
					new User(t1.getText().trim(),t2.getText().trim(),frame,ipv4,port);
				}else{
					new Administrator(t1.getText().trim(),t2.getText().trim(),frame,ipv4,port);
				}
			}
		});
		c.add(b1);
		b2.setBackground(Color.RED);
		b2.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.ORANGE)));
		b2.setBounds(200, 420, 50, 25);
		b2.setToolTipText("Click to Exit");
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(null,"You Exit the System,bye!","Exit",JOptionPane.CLOSED_OPTION);
				frame.dispose();
			}
		});
		c.add(b2);
	}
}
