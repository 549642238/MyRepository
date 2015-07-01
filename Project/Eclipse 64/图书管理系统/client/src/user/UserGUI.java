/*User ����ʾ���棬������ʾ������Ϣ��ͼ���ѯ���
 * Function:	1)����APanel
 * 				2)����BPanel
 * 				3)�˳�ϵͳ
 */
package user;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import display.Display;

public class UserGUI extends JApplet{
	private User user = null;
	private JFrame frame = new JFrame("XiDian Library");
	private APanel panela = null;
	private BPanel panelb = null;
	private JButton b = new JButton("Exit");
	public UserGUI(User u){
		user = u;
		panela = new APanel(user);
		panelb = new BPanel(user);
	}
	public JFrame toFrame(){
		return frame;
	}
	public void fresh(User u){
		user = u;
		panela.fresh(u);
	}
	public void init(){
		Container c = getContentPane();
		c.setLayout(null);
		c.setBackground(Color.CYAN);
		c.add(panela);
		c.add(panelb);
		b.setBackground(Color.RED);
		b.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b.setBounds(600, 540, 70, 25);
		b.setToolTipText("Exit the library");
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				user.exit();
			}
		});
		c.add(b);
	}
	public static void main(String[] arg){
		Display.run(new JFrame("XiDian Library"), new UserGUI(new User("13121175","123",null,"loaclhost",8899)), 700, 600);
	}
}