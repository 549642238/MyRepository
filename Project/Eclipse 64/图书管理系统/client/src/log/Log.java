/*�ͻ��˵�¼��ʼ����
 * @author 			czh
 * @complete_time	2015/6/4
 * @take_time		4days
 * @long			1813Lines
 * @Explaination	1)��¼library������������ͨ��ѡ�������ȷ����¼��Ľ���
 * 					2)���ṩ�˺�ע�ᣬ�˺�ע���ɹ���Ա��ɣ���Ա�У��ѧ��
 * 					3)��¼���治���ӷ���������½�������ݻ����ɲ�ͬ�û�(User��Admin),
 * 					�û����ӷ��������������������ݿ��ж��˻��������ȷ�Ծ����Ƿ���ʾ���ܽ���
 * 					4)��Ҫע��,��¼��Ҫ����ͬ�û��������ӷ������Ĳ�������˿ںź͵�ַ
 * @start			�ͻ��˿�ʼ����
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
	private final String ipv4 = "localhost";				//�����ip
	private final int port = 8899;							//����˶˿�
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
