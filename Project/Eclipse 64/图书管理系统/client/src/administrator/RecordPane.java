/*User 的显示界面，用于显示租借记录
 * Function:	显示User租借记录,调用user.record()[控制器]
 */
package administrator;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.table.*;
import javax.swing.*;
import javax.swing.border.*;

import book.Book;
import display.Display;
import record.Record;

public class RecordPane extends JApplet{
	private final int N =100;										//定义最多查看100条记录
	private DefaultTableModel model = null;
	private JTable table = null;
	private JScrollPane scrollpane = null;
	private JFrame frame = new JFrame("Records");
	private JTextField t = new JTextField(30);
	private JLabel l0 = new JLabel("Borrow = ");
	private JButton b = new JButton("Exit");
	private JButton b1 = new JButton("Delete");
	private UserDataStructure user = null;
	private Administrator admin = null;
	private ArrayList<Record> list = new ArrayList<Record>();		//存放租借记录
	public JFrame toFrame(){
		return frame;
	}
	public RecordPane(UserDataStructure u,Administrator a){
		user = u;
		admin = a;
		list = admin.selectUserRecord(user);
	}
	public void init(){
		Container c = getContentPane();
		c.setBackground(Color.GREEN);
		c.setLayout(null);
		b.setBounds(300, 440, 50, 20);
		b.setBackground(Color.RED);
		b.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b.setToolTipText("Exit this panel");
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.dispose();
			}
		});
		c.add(b);
		b1.setBounds(200, 440, 50, 20);
		b1.setBackground(Color.ORANGE);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b1.setToolTipText("Delete this record tip");
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(table.getSelectedRowCount() == 0 || list.isEmpty()){
					JOptionPane.showMessageDialog(null,"You select nothing!","Delete Failed",JOptionPane.ERROR_MESSAGE);
				}else{
					int i = table.getSelectedRow();
					if(i >= list.size() || i<0){
						JOptionPane.showMessageDialog(null,"You choose none record!","Delete Failed",JOptionPane.ERROR_MESSAGE);
					}else if(admin.deleteRecord(user.Unumber,((Record)list.get(i)).date)){
						JOptionPane.showMessageDialog(null,"You have deleted the record!","Delete Success",JOptionPane.CLOSED_OPTION);
					}else{
						JOptionPane.showMessageDialog(null,"Unkonwn Error Happens!","Delete Failed",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		c.add(b1);
		l0.setBackground(Color.PINK);
		l0.setBorder(new LineBorder(Color.RED));
		l0.setBounds(280,30,80,20);
		l0.setText("Borrow = "+user.Uborrow);
		l0.setToolTipText("Current Borrow Number:");
		l0.setOpaque(true);
		c.add(l0);
		t.setBackground(Color.YELLOW);
		t.setBorder(new LineBorder(Color.RED));
		t.setToolTipText("About Records Existing");
		t.setEditable(false);
		t.setBounds(0, 400, 600, 25);
		t.setText("There are "+list.size()+" tips records");
		c.add(t);
		String[] colnames = {"No","Book_Name","ISBN","Date","State"};
		model = new DefaultTableModel(colnames, N);
		table = new JTable(model);
		scrollpane = new JScrollPane(table);
		scrollpane.setBounds(10, 70, 580, 320);
		scrollpane.setBackground(Color.YELLOW);
		scrollpane.setBorder(new LineBorder(Color.BLACK));
		scrollpane.setToolTipText("Borrowing Records");
		c.add(scrollpane);
		if(list == null || list.isEmpty()){
			t.setText("There is no records");
		}
		table.setBackground(Color.CYAN);
		table.setBorder(new LineBorder(Color.RED));
		table.setToolTipText("Borrowing Records");
		table.setEnabled(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);				//只允许选中一行
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(40);
		table.getColumnModel().getColumn(3).setPreferredWidth(200);
		table.getColumnModel().getColumn(4).setPreferredWidth(20);
		for(int i=0;i<list.size();i++){
			table.setValueAt(new String((i+1)+""), i, 0);
			table.setValueAt(((Record)(list.get(i))).name, i, 1);
			table.setValueAt(((Record)(list.get(i))).ISBN, i, 2);
			table.setValueAt(((Record)(list.get(i))).date, i, 3);
			table.setValueAt(((Record)(list.get(i))).state,i, 4);
		}
		table.setVisible(true);
	}
}
