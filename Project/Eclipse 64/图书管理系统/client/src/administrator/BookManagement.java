/*Administrator 的显示界面，用于管理书籍
 * Function:	1)显示查询的书籍信息,调用admin.searchBook()[控制器]
 * 				2)键入添加的书籍信息,调用admin.insertBook()[控制器]
 * 				3)键入书籍的修改信息,调用admin.updateBook()[控制器]
 * 				4)根据输入的ISBN删除书籍,调用admin.deleteBook()[控制器]
 */
package administrator;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import administrator.Administrator;
import book.Book;
import display.Display;

public class BookManagement extends JApplet{
	private final String[] type = {"Science","Literature","Military"};
	private Administrator admin = null;
	private JFrame frame = new JFrame("Book Management");
	private JTextField t1 = new JTextField(20);
	private JTextField t2 = new JTextField(20);
	private JComboBox<String> t3 = new JComboBox<String>(type);
	private JTextField t4 = new JTextField(20);
	private JTextField t5 = new JTextField(20);
	private JTextField t6 = new JTextField(20);
	private JLabel l1 = new JLabel("ISBN");
	private JLabel l2 = new JLabel("Name");
	private JLabel l3 = new JLabel("Class");
	private JLabel l4 = new JLabel("Writer");
	private JLabel l5 = new JLabel("Publisher");
	private JLabel l6 = new JLabel("Quantity");
	private JButton b1 = new JButton("Search");
	private JButton b2 = new JButton("Insert");
	private JButton b3 = new JButton("Modify");
	private JButton b4 = new JButton("Delete");
	private JButton b5 = new JButton("Exit");
	public JFrame toFrame(){
		return frame;
	}
	public static void main(String[] arg){
		BookManagement bm = new BookManagement(null);
		Display.run(bm.toFrame(), bm, 400, 500);
	}
	public BookManagement(Administrator a){
		admin = a;
	}
	public void init(){
		Container c = getContentPane();
		c.setLayout(null);
		c.setBackground(Color.MAGENTA);
		l1.setBackground(Color.PINK);
		l1.setBorder(new LineBorder(Color.BLUE));
		l1.setOpaque(true);
		l1.setBounds(70, 10, 60, 25);
		l1.setToolTipText("Book ISBN:");
		c.add(l1);
		t1.setBackground(Color.CYAN);
		t1.setBounds(150, 10, 100, 25);
		t1.setBorder(new LineBorder(Color.RED));
		t1.setToolTipText("Input the book's ISBN");
		c.add(t1);
		b1.setBackground(Color.GREEN);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b1.setToolTipText("Click to Search the book");
		b1.setBounds(300, 10, 70, 25);
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String s = t1.getText();
				if(s.equals("")){
					JOptionPane.showMessageDialog(null,"You have no Input!","Search Failed",JOptionPane.ERROR_MESSAGE);
				}else{
					Book book = admin.searchBook(s);
					if(book == null){
						JOptionPane.showMessageDialog(null,"The book not exists!","Search Failed",JOptionPane.ERROR_MESSAGE);
					}else{
						t2.setText(book.Bname);
						t3.setSelectedItem(book.Bclass);
						t4.setText(book.Bwriter);
						t5.setText(book.Bpublisher);
						t6.setText(book.Bquantity+"");
					}
				}
			}
		});
		c.add(b1);
		l2.setBackground(Color.PINK);
		l2.setBorder(new LineBorder(Color.BLUE));
		l2.setOpaque(true);
		l2.setBounds(30, 100, 60, 25);
		l2.setToolTipText("Book name:");
		c.add(l2);
		t2.setBackground(Color.CYAN);
		t2.setBounds(160, 100, 200, 25);
		t2.setBorder(new LineBorder(Color.RED));
		t2.setToolTipText("Input the book's name");
		c.add(t2);
		l3.setBackground(Color.PINK);
		l3.setBorder(new LineBorder(Color.BLUE));
		l3.setOpaque(true);
		l3.setBounds(30, 150, 60, 25);
		l3.setToolTipText("Book Class:");
		c.add(l3);
		t3.setBackground(Color.CYAN);
		t3.setBounds(160, 150, 200, 25);
		t3.setBorder(new LineBorder(Color.RED));
		t3.setToolTipText("Select the book's Class");
		c.add(t3);
		l4.setBackground(Color.PINK);
		l4.setBorder(new LineBorder(Color.BLUE));
		l4.setOpaque(true);
		l4.setBounds(30, 200, 60, 25);
		l4.setToolTipText("Book Writer:");
		c.add(l4);
		t4.setBackground(Color.CYAN);
		t4.setBounds(160, 200, 200, 25);
		t4.setBorder(new LineBorder(Color.RED));
		t4.setToolTipText("Input the book's writer");
		c.add(t4);
		l5.setBackground(Color.PINK);
		l5.setBorder(new LineBorder(Color.BLUE));
		l5.setOpaque(true);
		l5.setBounds(30, 250, 60, 25);
		l5.setToolTipText("Book Publisher:");
		c.add(l5);
		t5.setBackground(Color.CYAN);
		t5.setBounds(160, 250, 200, 25);
		t5.setBorder(new LineBorder(Color.RED));
		t5.setToolTipText("Input the book's publisher");
		c.add(t5);
		l6.setBackground(Color.PINK);
		l6.setBorder(new LineBorder(Color.BLUE));
		l6.setOpaque(true);
		l6.setBounds(30, 300, 60, 25);
		l6.setToolTipText("Book Quantity:");
		c.add(l6);
		t6.setBackground(Color.CYAN);
		t6.setBounds(160, 300, 200, 25);
		t6.setBorder(new LineBorder(Color.RED));
		t6.setToolTipText("Input the book's quantity,you must input a number below 100");
		c.add(t6);
		b2.setBackground(Color.GREEN);
		b2.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b2.setToolTipText("Add a new book into the library");
		b2.setBounds(100, 350, 70, 25);
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(admin.insertBook(t1.getText().trim(),t2.getText().trim(),t3.getSelectedItem().toString().trim(),t4.getText().trim(),t5.getText().trim(),t6.getText().trim())){
					JOptionPane.showMessageDialog(null,"A new book has been added into library!","Add Success",JOptionPane.CLOSED_OPTION);
				}else{
					JOptionPane.showMessageDialog(null,"1)The book's ISBN has been existed!\n2)The book's information is not complete!\n3)Input Wrong(The Input is too long、Input form is illegal or else)!","Add Failed",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		c.add(b2);
		b3.setBackground(Color.ORANGE);
		b3.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b3.setToolTipText("Modify an existing book in the library,Cannot Modify ISBN,or you will failed");
		b3.setBounds(200, 350, 70, 25);
		b3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(admin.updateBook(t1.getText().trim(),t2.getText().trim(),t3.getSelectedItem().toString().trim(),t4.getText().trim(),t5.getText().trim(),t6.getText().trim())){
					JOptionPane.showMessageDialog(null,"The book is updated OK!","Modify Success",JOptionPane.CLOSED_OPTION);
				}else{
					JOptionPane.showMessageDialog(null,"1)Input Illegal\n2)ISBN cannot be changed\n3)The book not exist!","Modify Failed",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		c.add(b3);
		b4.setBackground(Color.YELLOW);
		b4.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b4.setToolTipText("Delete an existing book in the library");
		b4.setBounds(100, 400, 70, 25);
		b4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(admin.deleteBook(t1.getText().trim())){
					JOptionPane.showMessageDialog(null,"The book is deleted OK!","Delete Success",JOptionPane.CLOSED_OPTION);
				}else{
					JOptionPane.showMessageDialog(null,"The book not exist!","Delete Failed",JOptionPane.ERROR_MESSAGE);
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
