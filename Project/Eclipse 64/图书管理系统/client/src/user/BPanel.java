/*User 的显示界面，用于显示图书信息
 * Function:	1)触发显示查询书籍事件,显示查询结果界面,调用User.search()[控制器]
 * 				2)触发显示租借书籍事件,显示租借结果提示,调用User.borrow()[控制器]
 */
package user;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import record.Record;
import display.Display;
import book.Book;

public class BPanel extends JPanel{
	private final int N =100;										//定义最多显示100条书籍信息
	private final String[] type = {"All","Science","Literature","Military"};
	private final String[] colnames = {"No","Book_Name","ISBN","Writer","Type","Publisher","Quantity"};			//呈现图书的信息选项
	private ArrayList<Book> list = new ArrayList<Book>();
	private DefaultTableModel model = null;
	private JTable table = null;
	private JScrollPane scrollpane = null;
	private JLabel l0 = new JLabel("Searching Book");
	private JLabel l1 = new JLabel("Book Name");
	private JLabel l2 = new JLabel("Book Writer");
	private JLabel l3 = new JLabel("Book Type");
	private JTextField t = new JTextField(30);
	private JTextField t1 = new JTextField(20);
	private JTextField t2 = new JTextField(20);
	private JComboBox jb = new JComboBox(type);
	private JButton b1 = new JButton("Search");
	private JButton b2 = new JButton("Borrow");
	private User user = null;
	public BPanel(User u){
		user = u;
		this.setLayout(null);
		this.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		this.setBackground(Color.YELLOW);
		this.setBounds(250, 5, 735, 530);
		l0.setBackground(Color.GREEN);
		l0.setBorder(new LineBorder(Color.BLUE));
		l0.setToolTipText("Find the book you want");
		l0.setOpaque(true);
		l0.setBounds(0, 0, 735, 20);
		this.add(l0);
		l1.setToolTipText("Book's name:");
		l1.setBorder(new LineBorder(Color.RED));
		l1.setBackground(Color.ORANGE);
		l1.setOpaque(true);
		l1.setBounds(10, 50, 75, 25);
		this.add(l1);
		t1.setToolTipText("Input the book's name:");
		t1.setBackground(Color.PINK);
		t1.setBorder(new LineBorder(Color.BLUE));
		t1.setBounds(85, 50, 155, 25);
		this.add(t1);
		l2.setToolTipText("Book's writer:");
		l2.setBorder(new LineBorder(Color.RED));
		l2.setBackground(Color.ORANGE);
		l2.setOpaque(true);
		l2.setBounds(250, 50, 80, 25);
		this.add(l2);
		t2.setToolTipText("Input the book's writer:");
		t2.setBackground(Color.PINK);
		t2.setBorder(new LineBorder(Color.BLUE));
		t2.setBounds(330, 50, 100, 25);
		this.add(t2);
		l3.setToolTipText("Book's type:");
		l3.setBorder(new LineBorder(Color.RED));
		l3.setBackground(Color.ORANGE);
		l3.setOpaque(true);
		l3.setBounds(440, 50, 70, 25);
		this.add(l3);
		jb.setBackground(Color.PINK);
		jb.setBorder(new LineBorder(Color.BLUE));
		jb.setToolTipText("Choose the book's class:");
		jb.setBounds(510, 50, 90, 25);
		this.add(jb);
		b1.setBackground(Color.GREEN);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b1.setToolTipText("Click to Search");
		b1.setBounds(650, 50, 70, 25);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				model = new DefaultTableModel(colnames, 100);
				table.setModel(model);
				if(t1.getText().equals("") && t2.getText().equals("") && jb.getSelectedItem().equals("All")){
					JOptionPane.showMessageDialog(null,"You input nothing!","Search Failed",JOptionPane.ERROR_MESSAGE);
				}else{
					list = user.search(t1.getText(),t2.getText(),jb.getSelectedItem().toString());
					t.setText("There are "+list.size()+" tips results");
					table.getColumnModel().getColumn(0).setPreferredWidth(10);
					table.getColumnModel().getColumn(1).setPreferredWidth(150);
					table.getColumnModel().getColumn(2).setPreferredWidth(40);
					table.getColumnModel().getColumn(3).setPreferredWidth(50);
					table.getColumnModel().getColumn(4).setPreferredWidth(40);
					table.getColumnModel().getColumn(5).setPreferredWidth(100);
					table.getColumnModel().getColumn(6).setPreferredWidth(10);
					for(int i=0;i<list.size();i++){
						table.setValueAt(new String((i+1)+""), i, 0);
						table.setValueAt(((Book)(list.get(i))).Bname, i, 1);
						table.setValueAt(((Book)(list.get(i))).ISBN, i, 2);
						table.setValueAt(((Book)(list.get(i))).Bclass, i, 3);
						table.setValueAt(((Book)(list.get(i))).Bwriter,i, 4);
						table.setValueAt(((Book)(list.get(i))).Bpublisher,i, 5);
						table.setValueAt(((Book)(list.get(i))).Bquantity, i, 6);
					}
				}
			}
		});
		this.add(b1);
		b2.setBackground(Color.MAGENTA);
		b2.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b2.setToolTipText("Click to Borrow the selected book");
		b2.setBounds(350, 460, 70, 25);
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(list.isEmpty() || table.getSelectedRowCount() == 0){
					JOptionPane.showMessageDialog(null,"You select nothing!","Borrow Failed",JOptionPane.ERROR_MESSAGE);
				}else{
					int i = table.getSelectedRow();
					if(i >= list.size() || i<0){
						JOptionPane.showMessageDialog(null,"You choose none book!","Borrow Failed",JOptionPane.ERROR_MESSAGE);
					}else if(user.borrow(((Book)list.get(i)).ISBN, ((Book)list.get(i)).Bname)){
						JOptionPane.showMessageDialog(null,"You have borrowed the book,Please go to fetch it!","Borrow Success",JOptionPane.CLOSED_OPTION);
					}else{
						JOptionPane.showMessageDialog(null,"Your max borrow number is reached\n or the book left 0 or you have borrowed thie book!","Borrow Failed",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		this.add(b2);
		model = new DefaultTableModel(colnames, N);
		table = new JTable(model);
		scrollpane = new JScrollPane(table);
		scrollpane.setBounds(10, 80, 715, 375);
		scrollpane.setBackground(Color.YELLOW);
		scrollpane.setBorder(new LineBorder(Color.BLACK));
		scrollpane.setToolTipText("Book Information");
		this.add(scrollpane);
		t.setBackground(Color.GREEN);
		t.setBorder(new LineBorder(Color.RED));
		t.setToolTipText("About Books Searching");
		t.setEditable(false);
		t.setBounds(3, 490, 730 ,30);
		this.add(t);
		table.setBackground(Color.CYAN);
		table.setBorder(new LineBorder(Color.RED));
		table.setToolTipText("Books' Information");
		table.setEnabled(true);
		table.setVisible(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);				//只允许选中一行
	}
}