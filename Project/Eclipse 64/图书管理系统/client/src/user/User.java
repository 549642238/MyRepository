/*User ���߼������[controller]�����ݽ�����¼�����(Button��)�����¼���ִ��,���ںͷ����������Ӵ����¼��������������ظ�����
 * Tips:	1)ÿ���û���¼�����һ���ͷ�����ͨ�ŵ�socket,�����������������Ϣ����������������֪��User�ͷ�����ͨ�ŵ�Э��
 * 			2)User.java���߼�����ģ��(������Ϊ�ǿ�����)����������û��ڽ���Ĵ����¼����п��ƣ�ѡ���Ӧ���߼�����ģ��ִ�в���������ظ�����
 * 			3)User.javaҲ��һ�����ڼ�¼User��Ϣ�����ݽṹ,�������Կ��Ա������๲���������ݷ���
 * Module:	1)modify():�޸ĸ�����Ϣ
 * 			2)record():��ѯ��������¼
 * 			3)search():����������ѯͼ����Ϣ
 * 			4)borrow():���ͼ�飬��ͨ�������������ݿⷢ������������ɹ�����������Ϣ�б�
 * 			5)freshInformation():���¸�����Ϣģ��
 * 			6)exit():�˳�ģ�飬ֻ��Ҫ�ر�һЩ������������ӡ��رս��沢֪ͨ����������
 */
package user;

import java.util.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

import display.Display;
import record.Record;
import book.Book;

public class User implements Serializable{
	private String ipv4 = "localhost";							//������IP��ַ
	private int port = 8899;									//�������˿ں�
	private final String pro0 = "OK";							//���ͣ����գ�Server����Ϣ����Э��
	private final String pro00 = "NO";							//���ͣ����գ�Server����Ϣ�ܾ�Э��
	private final String pro1 = "Log";							//ͨ��Э�飬��¼����
	private final String pro2 = "Exit";							//ͨ��Э�飬�˳�����
	private final String pro3 = "Modify";						//ͨ��Э�飬�޸�����
	private final String pro4 = "Record";						//ͨ��Э�飬�鿴����¼����
	private final String pro5 = "SearchBook";					//ͨ��Э�飬��ѯͼ������
	private final String pro6 = "BorrowBook";					//ͨ��Э�飬���ͼ������
	private final String pro7 = "FetchInformation";				//ͨ��Э�飬ˢ�¸���ҳ������
	private final String proe = "EOF";							//ͨ��Э�飬��ʾ��Ϣ����
	private BufferedReader br = null;
	private PrintWriter writer = null;
	private UserGUI face = null;
	private Socket client = null;
	protected String Unumber = null;
	protected String Upasswd = null;
	protected String Uname = null;
	protected String Uschool = null;
	protected String Udepartment = null;
	protected int Uborrow = 0;
	public int toUborrow(){
		return Uborrow;
	}
	public static void main(String[] arg){
		new User("13121175","123",new JFrame("Log"),"localhost",8899);
	}
	public User(String number,String passwd,JFrame frame,String ip,int p){
		/*
		 * ��ʼ�����̻������ӷ�������ͨ����ݡ��˻���������֤�������µ�User���棬�����ӷ����������ĸ�����Ϣ����User���ݽṹ
		 * ����˻������롢��ݻ��˻������ߴ��������ܵ����Է������Ĳ�ͬ��ʾ
		 */
		try{
			ipv4 = ip;
			port = p;
			client = new Socket(ipv4,port);
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
			writer.write(pro1+"\n");
			writer.write(number+"\n");
			writer.write(passwd+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro0)){									//�յ���������¼�ɹ�����ʾ
				Unumber = br.readLine();
				Uname = br.readLine();
				Upasswd = br.readLine();
				Uschool = br.readLine();
				Udepartment = br.readLine();
				Uborrow = new Integer(br.readLine()).intValue();
				frame.dispose();								//��½�ɹ����¼������Զ���ʧ
				face = new UserGUI(this);
				JOptionPane.showMessageDialog(null,"Welcome to Xidian library!","Log Success",JOptionPane.CLOSED_OPTION);
				Display.run(face.toFrame(), face, 1000, 600);
			}else{												//�˻���������ѵ�¼����������¼ʧ��
				if(s.equals("E1")){
					JOptionPane.showMessageDialog(null,"Your accout not exists!","Log failed",JOptionPane.ERROR_MESSAGE);
				}else if(s.equals("E2")){
					JOptionPane.showMessageDialog(null,"Your password is wrong!","Log failed",JOptionPane.ERROR_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(null,"Your account has been online or the server is full!","Log failed",JOptionPane.ERROR_MESSAGE);
				}
				br.close();
				writer.close();
				client.close();
			}
		}catch(Exception e){
			System.out.println("User error0 - Socket Connect failed");
			e.printStackTrace();
		}
	}
	public void modify(User u){							//�޸ĸ�����Ϣģ��(��Ҫʱ�����������),���޸ĺ�ĸ�����Ϣ����������,��������������ݿ�
		try{
			writer.write(pro3+"\n");
			writer.write(u.Unumber+"\n");
			writer.write(u.Uname+"\n");
			writer.write(u.Upasswd+"\n");
			writer.flush();
			String result = br.readLine();
			if(result.equals(pro0)){
				JOptionPane.showMessageDialog(null,"Your Information has been Changed successful!","Modify Success",JOptionPane.CLOSED_OPTION);
				Uname = u.Uname;
				Upasswd = u.Upasswd;
				face.fresh(this);
			}else{
				JOptionPane.showMessageDialog(null,"Your Information not Changed!","Modify Failed",JOptionPane.ERROR_MESSAGE);
			}
		}catch(Exception e){
			System.out.println("User error1 - Modify Error");
			e.printStackTrace();
		}
	}
	public ArrayList<Record> record(User u){			//��ѯ����¼ģ��(��Ҫʱ�����������),�������������������������¼��Ϣ
		try{
			writer.write(pro4+"\n");
			writer.write(u.Unumber+"\n");
			writer.flush();
			String s = br.readLine();
			ArrayList<Record> list = new ArrayList<Record>();
			if(s.equals(pro0)){
				String name = new String("");
				while(!(name = br.readLine()).equals(proe)){
					list.add(new Record(name,br.readLine(),br.readLine(),new Integer(br.readLine()).intValue()));
				}
			}
			return list;
		}catch(Exception e){
			System.out.println("User error2 - Record Error");
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<Book> search(String name,String Writer,String type){		//��ѯͼ��ģ��(��Ҫʱ�����������),��������������������ز�ѯͼ����Ϣ
		ArrayList<Book> list = new ArrayList<Book>();
		try{
			writer.write(pro5+"\n");
			writer.write(name+"\n");
			writer.write(Writer+"\n");
			writer.write(type+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro0)){
				String bname = new String("");
				while(!(bname = br.readLine()).equals(proe)){
					list.add(new Book(bname,br.readLine(),br.readLine(),br.readLine(),br.readLine(),new Integer(br.readLine()).intValue()));
				}
			}
			return list;
		}catch(Exception e){
			System.out.println("User error3 - Search Error");
			e.printStackTrace();
		}
		return null;
	}
	public boolean borrow(String ISBN,String name){								//���ͼ��ģ��(��Ҫʱ�����������),������������������󣬷������������ݿ���ݷ��ؽ������������
		try{
			writer.write(pro6+"\n");
			writer.write(Unumber+"\n");
			writer.write(ISBN+"\n");
			writer.write(name+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro0)){
				Uborrow++;
				face.fresh(this);
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("User error4 - Borrow Error");
			e.printStackTrace();
		}
		return false;
	}
	public void freshInformation(){									//ˢ�¸�����Ϣģ��(��Ҫʱ�����������),�����������������᷵���������ݿ��ŵĸ�����Ϣ			
		try{
			writer.write(pro7+"\n");
			writer.write(Unumber+"\n");
			writer.flush();
			Unumber = br.readLine();
			Uname = br.readLine();
			Upasswd = br.readLine();
			Uschool = br.readLine();
			Udepartment = br.readLine();
			Uborrow = new Integer(br.readLine()).intValue();
			face.fresh(this);
		}catch(Exception e){
			System.out.println("User error5 - FreshInformation Error");
			e.printStackTrace();
		}
	}
	public void exit(){												//User�˳�
		try{
			writer.write(pro2+"\n");
			writer.write(Unumber+"\n");
			writer.flush();
			br.readLine();
			br.close();
			writer.close();
			client.close();
			JOptionPane.showMessageDialog(null,"Goodbye!","Exit",JOptionPane.CLOSED_OPTION);
			face.toFrame().dispose();
			System.exit(0);
		}catch(Exception e){
			System.out.println("User error6 - Exit Error");
			e.printStackTrace();
		}
	}
}