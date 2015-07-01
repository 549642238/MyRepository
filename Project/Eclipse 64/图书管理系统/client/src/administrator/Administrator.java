/*Administrator ���߼������[controller]�����ݽ�����¼�����(Button��)�����¼���ִ��,���ںͷ����������Ӵ����¼��������������ظ�����
 * Tips:	1)ÿ������Ա��¼�����һ���ͷ�����ͨ�ŵ�socket,�����������������Ϣ����������������֪��Admin�ͷ�����ͨ�ŵ�Э��
 * 			2)Administrator.java���߼�����ģ��(������Ϊ�ǿ�����)��������ݹ���Ա�ڽ���Ĵ����¼����п��ƣ�ѡ���Ӧ���߼�����ģ��ִ�в���������ظ�����
 * 			3)Administrator.javaҲ��һ�����ڼ�¼Administrator��Ϣ�����ݽṹ,�������Կ��Ա������๲���������ݷ���
 * Module:	1)searchBook():��ѯ�鼮
 * 			2)insertBook():����鼮
 * 			3)updateBook():�����鼮
 * 			4)deleteBook():ɾ���鼮
 * 			5)searchUser():��ѯ�û�
 * 			6)insertUser():����û�
 * 			7)updateUser():�����û�
 * 			8)deleteUser():ɾ���û�
 * 			9)selectUserRecord():��ѯ�û�����¼
 * 			10)deleteRecord():ɾ���û�����¼
 * 			11)back():�黹ͼ��
 * 			12)exit():�˳�ģ�飬ֻ��Ҫ�ر�һЩ������������ӡ��رս��沢֪ͨ����������
 */
package administrator;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import record.Record;
import book.Book;
import display.Display;

public class Administrator implements Serializable{
	protected String Anumber = null;
    protected String Apasswd = null;
    protected String Aname = null;
    private AdministratorGUI face = null;
    private String ipv4 = "localhost";							//������IP��ַ
	private int port = 8899;									//�������˿ں�
	private final String pro0 = "OK";							//���ͣ����գ�Server����Ϣ����Э��
	private final String pro00 = "NO";							//���ͣ����գ�Server����Ϣ�ܾ�Э��
	private final String pro1 = "LogA";							//ͨ��Э�飬��¼����
	private final String pro2 = "Exit";							//ͨ��Э�飬�˳�����
	private final String pro3 = "AdminSearchBook";				//ͨ��Э�飬����ͼ������
	private final String pro4 = "InsertBook";					//ͨ��Э�飬���ͼ������
	private final String pro5 = "ModifyBook";					//ͨ��Э�飬�޸�ͼ������
	private final String pro6 = "DeleteBook";					//ͨ��Э�飬ɾ��ͼ������
	private final String pro7 = "AdminSearchUser";				//ͨ��Э�飬�����û�����
	private final String pro8 = "InsertUser";					//ͨ��Э�飬����û�����
	private final String pro9 = "ModifyUser";					//ͨ��Э�飬�޸��û�����
	private final String pro10 = "DeleteUser";					//ͨ��Э�飬ɾ���û�����
	private final String pro11 = "BackBook";					//ͨ��Э�飬�黹ͼ������
	private final String pro12 = "SelectUserRecord";			//ͨ��Э�飬��ѯ�û�����¼����
	private final String pro13 = "DeleteUserRecord";			//ͨ��Э�飬ɾ���û�����¼����
	private final String proe = "EOF";							//ͨ��Э�飬��ʾ��Ϣ����
	private BufferedReader br = null;
	private PrintWriter writer = null;
	private Socket client = null;
    public Administrator(String number,String passwd,JFrame frame,String ip,int p){
    	/*
		 * ��ʼ�����̻������ӷ�������ͨ����ݡ��˻���������֤�������µ�Administrator���棬�����ӷ����������ĸ�����Ϣ����Administrator���ݽṹ
		 * ����˻������롢��ݻ��˻������ߴ��������ܵ����Է������Ĳ�ͬ��ʾ
		 */
    	Anumber = number;
    	Apasswd = passwd;
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
    		if(s.equals(pro0)){
    			Anumber = br.readLine();
    			Aname = br.readLine();
    			Apasswd = br.readLine();
    			frame.dispose();
    			face = new AdministratorGUI(this);
    			JOptionPane.showMessageDialog(null,"Welcome to Xidian library!","Log Success",JOptionPane.CLOSED_OPTION);
    			Display.run(face.toFrame(), face, 500, 500);
    		}else{
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
    		System.out.println("Administrator error0 -> Socket Connect failed");
    	}
    }
    public Book searchBook(String ISBN){
    	try{
			writer.write(pro3+"\n");
			writer.write(ISBN+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return null;									//���ݿ��в��޴���
			}else{
				return new Book(br.readLine(),ISBN,br.readLine(),br.readLine(),br.readLine(),new Integer(br.readLine()).intValue());
			}
		}catch(Exception e){
			System.out.println("Administrator error1 -> searchBook failed");
			e.printStackTrace();
		}
    	return null;
    }
    public boolean insertBook(String ISBN,String Name,String Class,String Writer,String Publisher,String Quantity){
    	try{
    		int q = new Integer(Quantity).intValue();
    		if(ISBN.equals("") || Name.equals("") || Writer.equals("") || Publisher.equals("") || !(q>=1 && q<=100)){
    			return false;
    		}
			writer.write(pro4+"\n");
			writer.write(ISBN+"\n");
			writer.write(Name+"\n");
			writer.write(Class+"\n");
			writer.write(Writer+"\n");
			writer.write(Publisher+"\n");
			writer.write(q+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//���ݿ����ʧ�ܣ��ܿ��ܱ��ISBN�Ѵ���
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error2 -> insertBook failed");
			e.printStackTrace();
		}
    	return false;
    }
    public boolean updateBook(String ISBN,String Name,String Class,String Writer,String Publisher,String Quantity){
    	try{
    		int q = new Integer(Quantity).intValue();
    		if(Name.equals("") || Writer.equals("") || Publisher.equals("") || !(q>=1 && q<=100)){
    			return false;
    		}
			writer.write(pro5+"\n");
			writer.write(ISBN+"\n");
			writer.write(Name+"\n");
			writer.write(Class+"\n");
			writer.write(Writer+"\n");
			writer.write(Publisher+"\n");
			writer.write(q+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//���ݿ��޸�ʧ�ܣ��ܿ������ݿⷢ�����ش���
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error3 -> updateBook failed");
			e.printStackTrace();
		}
    	return false;
    }
    public boolean deleteBook(String ISBN){
    	if(ISBN.equals("")){
    		return false;
    	}
    	try{
			writer.write(pro6+"\n");
			writer.write(ISBN+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//���ݿ��޸�ʧ�ܣ��ܿ������ݿ�û�и���
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error4 -> deleteBook failed");
			e.printStackTrace();
		}
    	return false;
    }
    public UserDataStructure searchUser(String Number){
    	try{
			writer.write(pro7+"\n");
			writer.write(Number+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return null;									//���ݿ��в��޴���
			}else{
				return new UserDataStructure(Number,br.readLine(),br.readLine(),br.readLine(),br.readLine(),new Integer(br.readLine()).intValue());
			}
		}catch(Exception e){
			System.out.println("Administrator error5 -> searchUser failed");
			e.printStackTrace();
		}
    	return null;
    }
    public boolean insertUser(String Number,String Name,String Passwd,String School,String Department,String Borrow){
    	try{
    		int q = new Integer(Borrow).intValue();
    		if(Number.equals("") || Name.equals("") || Passwd.equals("") || School.equals("") || !(q>=0 && q<=3)){
    			return false;
    		}
			writer.write(pro8+"\n");
			writer.write(Number+"\n");
			writer.write(Passwd+"\n");
			writer.write(Name+"\n");
			writer.write(School+"\n");
			writer.write(Department+"\n");
			writer.write(q+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//���ݿ����ʧ�ܣ��ܿ��ܱ��Number�Ѵ���
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error6 -> insertUser failed");
			e.printStackTrace();
		}
    	return false;
    }
    public boolean updateUser(String Number,String School,String Department,String Borrow){
    	try{
    		int q = new Integer(Borrow).intValue();
    		if(Number.equals("") || School.equals("") || Department.equals("") || !(q>=0 && q<=3)){
    			return false;
    		}
			writer.write(pro9+"\n");
			writer.write(Number+"\n");
			writer.write(School+"\n");
			writer.write(Department+"\n");
			writer.write(q+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//���ݿ��޸�ʧ�ܣ��ܿ������ݿⷢ�����ش���
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error7 -> updateUser failed");
			e.printStackTrace();
		}
    	return false;
    }
    public boolean deleteUser(String Number){
    	if(Number.equals("")){
    		return false;
    	}
    	try{
			writer.write(pro10+"\n");
			writer.write(Number+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//���ݿ�ɾ��ʧ�ܣ��ܿ������ݿ�û�и��û�
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error8 -> deleteUser failed");
			e.printStackTrace();
		}
    	return false;
    }
    public ArrayList<Record> selectUserRecord(UserDataStructure ud){			//��ѯ����¼ģ��(��Ҫʱ�����������),�������������������������¼��Ϣ
		try{
			writer.write(pro12+"\n");
			writer.write(ud.Unumber+"\n");
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
			System.out.println("Administrator error9 -> selectUserRecord failed");
			e.printStackTrace();
		}
		return null;
	}
    public boolean deleteRecord(String number,String date){
    	try{
			writer.write(pro13+"\n");
			writer.write(number+"\n");
			writer.write(date+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro0)){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("Administrator error10 -> selectUserRecord failed");
			e.printStackTrace();
		}
    	return false;
    }
    public boolean back(String ISBN,String Number){
    	if(ISBN.equals("") || Number.equals("")){
    		return false;
    	}
    	try{
			writer.write(pro11+"\n");
			writer.write(Number+"\n");
			writer.write(ISBN+"\n");
			writer.flush();
			String s = br.readLine();
			if(s.equals(pro00)){
				return false;									//���ݿ��ѯʧ�ܣ��ܿ������ݿ�û�и�����û�
			}else{
				return true;
			}
		}catch(Exception e){
			System.out.println("Administrator error11 -> Back book failed");
			e.printStackTrace();
		}
    	return false;
    }
    public void exit(){
    	try{
			writer.write(pro2+"\n");
			writer.write(Anumber+"\n");
			writer.flush();
			br.readLine();
			br.close();
			writer.close();
			client.close();
			JOptionPane.showMessageDialog(null,"Goodbye!","Exit",JOptionPane.CLOSED_OPTION);
			face.toFrame().dispose();
			System.exit(0);
		}catch(Exception e){
			System.out.println("Administrator error12 -> Exit Error");
			e.printStackTrace();
		}
    }
}