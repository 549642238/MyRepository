/*Server [controller]�ṩ������߼�������������client�����󽫴��������ظ�client������ά�ַ���˽�������
 * @author 			czh
 * @complete_time	2015/6/4
 * @take_time		4days
 * @long			1321Lines
 * @Explaination	1)library�ķ�����,��User��Administrator���Ӳ�Ϊ���ṩ���ݿ�ȷ���
 * 					3)�ṩ�������ӡ����е�¼�������ƹ���
 * 					4)���Է������ݿ�(User��Administratorֻ��ͨ����������ӷ������ݿ�)
 * 					5)���ɷ��������棬ά������client�嵥
 * 					6)ά��Server����,���Խ��û���������Ϣ֪ͨ��ʾ����,����ServerGUI.addClient(),ServerGUI.removeClient()
 * 					7)���к�User��Administratorͨ�ŵ�����Э��
 * @Module			1)�����ŵ�ģ�飺��������client��socket���󣬽���socket���ӣ������µķ����߳�
 * 					2)��¼ģ�飺ÿ��ͨ���߳�Ҫ�Ե�¼�����ж�,����log()��check()
 * 					3)����ģ�飺ÿ��ͨ���߳�Ҫ������client������ͨ����ѯ���ݿ�������Ӧ
 * @start			��������ʼ����
 */
package server;

import java.util.*;
import java.io.*;
import java.net.*;

import connect.Connect;
import display.Display;
import user.User;
import administrator.Administrator;
import book.Book;
import record.Record;

public class Server{
	protected static ServerGUI sg = null;
	protected static int N = 100;							//ϵͳ����ͬʱ�����������
	public static void main(String[] arg){
		System.out.println("Xidian Library Server Start:\nStart Time:"+new Date().toString());
		sg = new ServerGUI();
		Display.run(sg.toFrame(), sg, 400, 600);
	}
	private static ArrayList<ThreadServerHandler> l = new ArrayList<ThreadServerHandler> ();		//�������ӵ�client���߳�
	public static ArrayList<ThreadServerHandler> toL(){
		return l;
	}
	public Server(int port,int n){
		N = n;
		ServerSocket server = null;
		try{
			server = new ServerSocket(port);
			label:
			while(true){
				try{
					Socket client = server.accept();
					ThreadServerHandler t=new ThreadServerHandler(client);
				}catch(Exception e){
					System.out.println("Server error0 -> Server start failed(1) -> Probably the port has been used or occupied!");
					e.printStackTrace();
					break;
				}
			}
		}catch(Exception e){
			System.out.println("Server error1 -> Server start failed(2)! -> Unpredictable error happens,please retry again");
			e.printStackTrace();
		}finally{
			l.clear();
			try{
				server.close();
			}catch(Exception e){
				System.out.println("Server error1 -> Server start failed(3)! -> Server close failed");
				e.printStackTrace();
			}
		}
	}
}
class ThreadServerHandler extends Thread implements Runnable{
	private final String P0 = "OK";					//���͸�client����Ϣ����Э��
	private final String P00 = "NO";				//���͸�client����Ϣ�ܾ�Э��
	private final String P1 = "Log";				//�յ�����User�ĵ�¼����Э��
	private final String P2 = "Exit";				//�յ�����client���˳�����Э��
	private final String P3 = "Modify";				//�յ�����User�޸ĸ�����Ϣ��Э��
	private final String P4 = "Record";				//�յ�����User��ѯ����¼��Э��
	private final String P5 = "SearchBook";			//�յ�����User��ѯͼ���Э��
	private final String P6 = "BorrowBook";			//�յ�����User���ͼ���Э��
	private final String P7 = "LogA";				//�յ�����Administrator�ĵ�¼����Э��
	private final String P8 = "AdminSearchBook";	//�յ�����Administrator�Ĳ�ѯͼ��Э��
	private final String P9 = "InsertBook";			//�յ�����Administrator�����ͼ��Э��
	private final String P10 = "ModifyBook";		//�յ�����Administrator���޸�ͼ��Э��
	private final String P11 = "DeleteBook";		//�յ�����Administrator��ɾ��ͼ��Э��
	private final String P12 = "AdminSearchUser";	//�յ�����Administrator�Ĳ�ѯ�û�Э��
	private final String P13 = "InsertUser";		//�յ�����Administrator������û�Э��
	private final String P14 = "ModifyUser";		//�յ�����Administrator���޸��û�Э��
	private final String P15 = "DeleteUser";		//�յ�����Administrator��ɾ���û�Э��
	private final String P16 = "BackBook";			//�յ�����Administrator�Ĺ黹ͼ��Э��
	private final String P17 = "FetchInformation";	//�յ�����User��ѯ������Ϣ��Э��
	private final String P18 = "SelectUserRecord";	//�յ�����Administrator�Ĳ�ѯ�û�����¼Э��
	private final String P19 = "DeleteUserRecord";	//�յ�����Administrator��ɾ���û�����¼Э��
	private final String PE = "EOF";				//������Ϣ������Э��
	private Connect con = new Connect();			//���߳������ݿ�����ӣ�ÿ����client��ͨ���̶߳�����һ������������
	private Socket incoming;						//��client��ͨ��socket
	private PrintWriter writer;						
	private BufferedReader br;
	private String no = new String("");				//��Ҫ������ʾÿ����client��ͨ���̣߳���ͬ��no���������벻ͬ��client��ͨ���߳�
	public String toNo(){
		return no;
	}
	public ThreadServerHandler(Socket in){
		incoming = in;
		try{
			writer = new PrintWriter(new OutputStreamWriter(incoming.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
			con.connect(); 							//�������ݿ⣬�൱��һ�����ݿ��ͨ���߳�
		}catch(Exception e){
			System.out.println("Server error2 -> Communication Thread Initialize failed");
			e.printStackTrace();
		}
		start();
	}
	public void writeUser(String number,String name,String passwd,String school,String department,int borrow){
		try{
			writer.write(number+"\n");
			writer.write(name+"\n");
			writer.write(passwd+"\n");
			writer.write(school+"\n");
			writer.write(department+"\n");
			writer.write(borrow+"\n");
			writer.flush();
		}catch(Exception e){
			System.out.println("Server error3 -> Write User failed -> Suggest restarting client");
			e.printStackTrace();
		}
	}
	public static synchronized boolean check(String number,Thread t){	/*	
																			����¼�û��Ƿ��Ѿ����ߣ���������δ���ߵ��û���ӵ�������̼߳����У�
																			���������뱣֤�����ԣ�֮����Ҫ��static����Ϊϣ�������߳�ִ���������
																			ʱ���⣬����������̣߳������ͬ����ͬһ���˺ţ�ͬʱ�жϲ����ߣ���ô��
																			��ͬһ��client�������,��Thread��������Ϊ��̬�������޷�ʹ��this
																		*/
		boolean b = false;
		for(int i=0;i<Server.toL().size();i++){
			if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(number)){
				b = true;
				break;
			}
		}
		if(!b && Server.toL().size()<Server.N){				//�û������߶��ҵ�ǰ��������������δ��
			Server.toL().add((ThreadServerHandler)t);
		}
		return b;
	}
	public void log(){										//��¼�ж�
		try{
			String s = br.readLine();
			if(s.equals(P1)){
				no = br.readLine();
				String passwd = br.readLine();
				if(!con.selectUser(no)){					//������ݿ��ѯ���˺Ų�����
					writer.write("E1"+"\n");
					writer.flush();
					try{
						sleep(3000);						//��֤���㹻��ʱ���ڹر�ǰ�ÿͻ����յ���Ϣ
						br.close();
						writer.close();
						incoming.close();					//��¼ʧ�ܻ��Զ��ر�����ͨ��ͨ�������ر�ͨ���߳�,���ݿ�����
						con.disConnect();
						this.stop();						//�ر��߳�
					}catch(Exception e){
						System.out.println("Server error4 -> User Log message passing failed(1)!");
						e.printStackTrace();
					}
				}else if(!con.toUser().Upasswd.equals(passwd)){			//�˺Ŵ��ڵ��������
					writer.write("E2"+"\n");
					writer.flush();
					try{
						sleep(3000);
						br.close();
						writer.close();
						incoming.close();
						con.disConnect();
						this.stop();
					}catch(Exception e){
						System.out.println("Server error4 -> User Log message passing failed(2)!");
						e.printStackTrace();
					}
				}
				boolean b = check(no,this);					//����¼�û��Ƿ��Ѿ�����
				if(!b){
					writer.write(P0+"\n");
					writer.flush();
					con.selectUser(no);
					User u = con.toUser();
					writeUser(u.Unumber,u.Uname,u.Upasswd,u.Uschool,u.Udepartment,u.Uborrow);
					Server.sg.addClient(u.Unumber,u.Uname,"User");
				}else{
					writer.write("E3"+"\n");
					writer.flush();
					try{
						sleep(3000);
						br.close();
						writer.close();
						incoming.close();
						con.disConnect();
						this.stop();
					}catch(Exception e){
						System.out.println("Server error4 -> User Log message passing failed(3)!");
						e.printStackTrace();
					}
				}
			}else if(s.equals(P7)){
				no = br.readLine();
				String passwd = br.readLine();
				if(!con.selectAdmin(no)){				//����˺Ų�����
					writer.write("E1"+"\n");
					writer.flush();
					try{
						sleep(3000);
						br.close();
						writer.close();
						incoming.close();					//��¼ʧ�ܻ��Զ��ر�����ͨ��ͨ�������ر�ͨ���߳�
						con.disConnect();
						this.stop();						//�ر��߳�
					}catch(Exception e){
						System.out.println("Server error4 -> Admin Log message passing failed(a)!");
						e.printStackTrace();
					}
				}else if(!con.toAdmin().Apasswd.equals(passwd)){			//�˺Ŵ��ڵ��������
					writer.write("E2"+"\n");
					writer.flush();
					try{
						sleep(3000);
						br.close();
						writer.close();
						incoming.close();
						con.disConnect();
						this.stop();
					}catch(Exception e){
						System.out.println("Server error4 -> Admin Log message passing failed(b)!");
						e.printStackTrace();
					}
				}
				boolean b = check(no,this);					//����¼�û��Ƿ��Ѿ�����
				if(!b){
					writer.write(P0+"\n");
					writer.flush();
					con.selectAdmin(no);
					Administrator a = con.toAdmin();
					writer.write(a.Anumber+"\n");
					writer.write(a.Aname+"\n");
					writer.write(a.Apasswd+"\n");
					writer.flush();
					Server.sg.addClient(a.Anumber,a.Aname,"Admin");
				}else{
					writer.write("E3"+"\n");
					writer.flush();
					try{
						sleep(3000);
						br.close();
						writer.close();
						incoming.close();
						con.disConnect();
						this.stop();
					}catch(Exception e){
						System.out.println("Server error4 -> Admin Log message passing failed(c)!");
						e.printStackTrace();
					}
				}
			}else{
				throw new Exception("Protocol Error");
			}
		}catch(Exception e){
			System.out.println("Server error4 -> Log failed,Probably protocol failed,suggest restart client");
			e.printStackTrace();
		}
	}
	public void run(){
		try{
			log();
			f();
		}catch(Exception e){								//����ͻ����쳣�˳�����Ƿ��˳��������д���Ӧ���ڷ����Ĩȥ��Ӧclient�˺�
			int location = 0;
			for(int i=0;i<Server.toL().size();i++){
				if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(no)){
					location = i;
					break;
				}
			}
			Server.toL().remove(location);					//�Ƴ����������̼߳����ж�Ӧ���̣߳���¼��Ӧclient��δ�������б���
			Server.sg.removeClient(no);
			try{
				br.close();
				writer.close();
				incoming.close();
				con.disConnect();
			}catch(Exception ee){
				System.out.println("Server error5 -> Running Error -> Closed Error");
				ee.printStackTrace();
			}
			System.out.println("Server error6 -> Running Error�����п�����client�Ƿ��˳�����ʱ��������");
			e.printStackTrace();
		}
	}
	public void f() throws Exception{
		while(true){
			String accept =  br.readLine();
			if(accept.equals(P2)){								//�������յ�����client���˳��ź�
				String s = br.readLine();
				writer.write("Copy"+"\n"); 						/*
																	�����client���͵��źŲ������壬
																	ֻ��Ϊ����ʱʹ���������յ�Exit��Ϣ��
																	����client�˳������û�У��ܿ���client
																	������Ϣ��server�˻�û�յ���ǰ�͹ر��˶�д
																	ͨ���������쳣
																*/
				int location = 0;
				for(int i=0;i<Server.toL().size();i++){
					if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s)){
						location = i;
						break;
					}
				}
				Server.sg.removeClient(s);
				Server.toL().remove(location);					//�Ƴ����������̼߳����ж�Ӧ���̣߳���¼��Ӧclient��δ�������б���
				br.close();
				writer.close();
				incoming.close();
				con.disConnect();
				break;											//�˳������ź�ģʽ������connection�߳�
			}else if(accept.equals(P3)){						//User���¸�����Ϣ
				String number = br.readLine();
				String name = br.readLine();
				String passwd = br.readLine();
				if(con.updateUser(number, name, passwd)){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P4)){						//User�鿴��������¼
				String number = br.readLine();
				ArrayList<Record> list = new ArrayList<Record>();
				if(con.selectRecord(number)){
					list = con.toRecord();
				}
				if(list.isEmpty()){
					writer.write(P00+"\n");
					writer.flush();
				}else{
					writer.write(P0+"\n");
					writer.flush();
					for(int i=0;i<list.size();i++){
						writer.write(((Record)(list.get(i))).name + "\n");
						writer.write(((Record)(list.get(i))).ISBN + "\n");
						writer.write(((Record)(list.get(i))).date + "\n");
						writer.write(((Record)(list.get(i))).state + "\n");
						writer.flush();
					}
					writer.write(PE+"\n");
					writer.flush();
				}
			}else if(accept.equals(P5)){						//User��ѯ�鼮
				String name = br.readLine();
				String Writer = br.readLine();
				String type = br.readLine(); 
				ArrayList<Book> list = new ArrayList<Book>();
				if(Writer.equals("") && type.equals("All") && con.selectBook_accordingName(name)){
					list = con.toBook();
				}else if(name.equals("") && type.equals("All") && con.selectBook_accordingWriter(Writer)){
					list = con.toBook();
				}else if(name.equals("") && Writer.equals("") && con.selectBook_accordingClass(type)){
					list = con.toBook();
				}else if(!name.equals("") && !Writer.equals("") && type.equals("All") && con.selectBook_accordingNameandWriter(name,Writer)){
					list = con.toBook();
				}else if(!name.equals("") && Writer.equals("") && !type.equals("All") && con.selectBook_accordingNameandClass(name,type)){
					list = con.toBook();
				}else if(name.equals("") && !Writer.equals("") && !type.equals("All") && con.selectBook_accordingWriterandClass(Writer,type)){
					list = con.toBook();
				}else if(!name.equals("") && !Writer.equals("") && !type.equals("All") && con.selectBook_accordingNameWriterandClass(name,Writer,type)){
					list = con.toBook();
				}
				if(list.isEmpty()){
					writer.write(P00+"\n");
					writer.flush();
				}else{
					writer.write(P0+"\n");
					writer.flush();
					for(int i=0;i<list.size();i++){
						writer.write(((Book)(list.get(i))).Bname + "\n");
						writer.write(((Book)(list.get(i))).ISBN + "\n");
						writer.write(((Book)(list.get(i))).Bwriter + "\n");
						writer.write(((Book)(list.get(i))).Bclass + "\n");
						writer.write(((Book)(list.get(i))).Bpublisher + "\n");
						writer.write(((Book)(list.get(i))).Bquantity + "\n");
						writer.flush();
					}
					writer.write(PE+"\n");
					writer.flush();
				}
			}else if(accept.equals(P6)){						//User����
				String number = br.readLine();
				String ISBN = br.readLine();
				String name = br.readLine();
				if(con.borrow(number, ISBN, name)){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P8)){						//Admin��ѯ�鼮
				String ISBN = br.readLine();
				if(con.selectBook_accordingISBN(ISBN)){
					Book b = (Book)(con.toBook().get(0));
					writer.write(P0+"\n");
					writer.write(b.Bname+"\n");
					writer.write(b.Bclass+"\n");
					writer.write(b.Bwriter+"\n");
					writer.write(b.Bpublisher+"\n");
					writer.write(b.Bquantity+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P9)){						//Admin����鼮
				if(con.insertBook(br.readLine(), br.readLine(), br.readLine(), br.readLine(), br.readLine(), new Integer(br.readLine()).intValue())){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P10)){						//Admin�޸��鼮
				if(con.updateBook(br.readLine(), br.readLine(), br.readLine(), br.readLine(), br.readLine(), new Integer(br.readLine()).intValue())){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P11)){						//Adminɾ���鼮
				if(con.deleteBook(br.readLine())){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P12)){						//Admin��ѯ�û�
				String Number = br.readLine();
				if(con.selectUser(Number)){
					User u = (User)(con.toUser());
					writer.write(P0+"\n");
					writer.write(u.Upasswd+"\n");
					writer.write(u.Uname+"\n");
					writer.write(u.Uschool+"\n");
					writer.write(u.Udepartment+"\n");
					writer.write(u.Uborrow+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P13)){						//Admin����û�
				if(con.insertUser(br.readLine(), br.readLine(), br.readLine(), br.readLine(), br.readLine(), new Integer(br.readLine()).intValue())){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P14)){						//Admin�޸��û���Ϣ
				if(con.updateUserFromAdmin(br.readLine(), br.readLine(), br.readLine(), new Integer(br.readLine()).intValue())){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P15)){						//Adminɾ���û�
				boolean exist = false;
				String s = br.readLine();
				for(int i=0;i<Server.toL().size();i++){
					if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(s)){
						writer.write(P00+"\n");
						writer.flush();
						exist = true;
						break;
					}
				}
				if(!exist){
					if(con.deleteUser(s)){
						writer.write(P0+"\n");
						writer.flush();
					}else{
						writer.write(P00+"\n");
						writer.flush();
					}
				}
			}else if(accept.equals(P16)){						//Admin����ȷ���û��黹�鼮�����ϵͳ���飬�����¼�����ݿ����
				if(con.back(br.readLine(),br.readLine())){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else if(accept.equals(P17)){						//User��ѯ����������Ϣ
				if(con.selectUser(br.readLine())){
					User u = con.toUser();
					writeUser(u.Unumber,u.Uname,u.Upasswd,u.Uschool,u.Udepartment,u.Uborrow);
				}
			}else if(accept.equals(P18)){
				String number = br.readLine();
				ArrayList<Record> list = new ArrayList<Record>();
				if(con.selectRecord(number)){
					list = con.toRecord();
				}
				if(list.isEmpty()){
					writer.write(P00+"\n");
					writer.flush();
				}else{
					writer.write(P0+"\n");
					writer.flush();
					for(int i=0;i<list.size();i++){
						writer.write(((Record)(list.get(i))).name + "\n");
						writer.write(((Record)(list.get(i))).ISBN + "\n");
						writer.write(((Record)(list.get(i))).date + "\n");
						writer.write(((Record)(list.get(i))).state + "\n");
						writer.flush();
					}
					writer.write(PE+"\n");
					writer.flush();
				}
			}else if(accept.equals(P19)){
				String Number = br.readLine();
				String date = br.readLine();
				if(con.deleteRecord(Number,date)){
					writer.write(P0+"\n");
					writer.flush();
				}else{
					writer.write(P00+"\n");
					writer.flush();
				}
			}else{												//����Э��֮����������δʶ���Э�鵽��
				System.out.println("Serious Error Happen -> Unrecoginsed Protocol");
				int location = 0;
				for(int i=0;i<Server.toL().size();i++){
					if(((ThreadServerHandler)(Server.toL().get(i))).toNo().equals(no)){
						location = i;
						break;
					}
				}
				Server.toL().remove(location);					//�Ƴ����������̼߳����ж�Ӧ���̣߳���¼��Ӧclient��δ�������б���
				try{
					br.close();
					writer.close();
					incoming.close();
					con.disConnect();
				}catch(Exception ee){
					System.out.println("Server error7 -> Running Error -> Unrecognized protocol -> Closed Error");
					ee.printStackTrace();
				}
			}
		}
	}
}