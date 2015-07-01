/*服务器启动后的显示界面
 * @Explaination	显示在线用户列表,显示时间,开启服务器
 */
package server;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import display.Display;

public class ServerGUI extends JApplet{
	private final int N = 100;												//系统允许同时在线最大人数
	private static final int port = 8899;									//服务器端口号
	private MyThread t = null;												//服务器服务线程
	private TimeThread t2 = null;											//时间显示线程
	private LinkedList<Client> list = new LinkedList<Client>();				//当前在线列表
	private final String[] colnames = {"No","Number","Name","ID"};
	private JFrame frame = new JFrame("Xidian Library Server");
	private DefaultTableModel model = null;
	private JTable table = null;
	private JScrollPane scrollpane = null;
	private JLabel l1 = new JLabel("");
	private JLabel l2 = new JLabel("");
	private JButton b1 = new JButton("Close");
	public JFrame toFrame(){
		return frame;
	}
	public void init(){
		Container c = getContentPane();
		c.setLayout(null);
		c.setBackground(Color.GREEN);
		model = new DefaultTableModel(colnames, N);
		table = new JTable(model);
		scrollpane = new JScrollPane(table);
		scrollpane.setBounds(10, 70, 380, 450);
		scrollpane.setBackground(Color.YELLOW);
		scrollpane.setBorder(new LineBorder(Color.BLACK));
		scrollpane.setToolTipText("Online Clients' Information");
		table.setBackground(Color.CYAN);
		table.setBorder(new LineBorder(Color.RED));
		table.setToolTipText("Online Clients' Information");
		table.setEnabled(false);
		table.setVisible(true);
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(40);
		c.add(scrollpane);
		Date date = new Date();
		l1.setText(date.toLocaleString());
		l1.setBackground(Color.PINK);
		l1.setBorder(new LineBorder(Color.RED));
		l1.setOpaque(true);
		l1.setBounds(50, 20, 120, 25);
		l1.setToolTipText("Today is:");
		c.add(l1);
		c.add(scrollpane);
		b1.setBackground(Color.RED);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLACK)));
		b1.setToolTipText("Attentiion:Click tp close library server,all clients will die");
		b1.setBounds(160, 530, 60, 25);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(null,"The Xidian library server closed!","Server Close",JOptionPane.CLOSED_OPTION);
				frame.dispose();
				System.exit(0);
			}
		});
		c.add(b1);
		l2.setText(list.size()+"");
		l2.setBackground(Color.ORANGE);
		l2.setBorder(new LineBorder(Color.RED));
		l2.setOpaque(true);
		l2.setBounds(200, 20, 60, 25);
		l2.setToolTipText("Current online number:");
		c.add(l2);
		t = new MyThread(port,N);
		t2 = new TimeThread(l1);
	}
	public void addClient(String Number,String Name,String ID){
		table.setValueAt(list.size()+1, list.size(), 0);
		table.setValueAt(Number, list.size(), 1);
		table.setValueAt(Name, list.size(), 2);
		table.setValueAt(ID, list.size(), 3);
		list.add(new Client(Number,Name,ID));
		l2.setText(list.size()+"");
	}
	public void removeClient(String Number){
		int location = 0;
		for(int i=0;i<list.size();i++){
			if(((Client)(list.get(i))).Number.equals(Number)){
				location = i;
				break;
			}
		}
		list.remove(location);
		model = new DefaultTableModel(colnames, N);							//清空在线列表table	
		table.setModel(model);
		for(int i=0;i<list.size();i++){
			table.setValueAt(i+1, i, 0);
			table.setValueAt(((Client)(list.get(i))).Number, i, 1);
			table.setValueAt(((Client)(list.get(i))).Name, i, 2);
			table.setValueAt(((Client)(list.get(i))).ID, i, 3);
		}
		l2.setText(list.size()+"");
	}
}
class Client{
	protected String Number;
	protected String Name;
	protected String ID;
	public Client(String number,String name,String id){
		Number = number;
		Name = name;
		ID = id;
	}
}
class MyThread extends Thread{
	private int port = 0;
	private int N = 100;
	public MyThread(int p,int n){
		port =  p;
		N = n;
		start();
	}
	public void run(){
		new Server(port,N);
	}
}
class TimeThread extends Thread{
	private JLabel label = null; 
	public TimeThread(JLabel l){
		label = l;
		start();
	}
	public void run(){
		try{
			while(true){
				label.setText(new Date().toLocaleString());
				sleep(1000);
			}
		}catch(Exception e){
			System.out.println("SerevrGUI error0 -> Timer wrong -> Suggest restarting server");
			e.printStackTrace();
		}
	}
}
