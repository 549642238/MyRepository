/** Record store all objects that includes all online clients,
 *	if a client send message,it pass message from Server to Client.*/
package myqq.record;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import myqq.client.Client;
import myqq.connect.Connect;

public class Record{
	private static LinkedList<Client> l = new LinkedList<Client> ();
	private static Client c = null;
	private static String name2 = new String();
	public static void addUser(String number){
		boolean b = true;
		for(int i=0;i<l.size();i++){
			if(number.equals(((Client)l.get(i)).toNo())){
				b = false;
			}
		}
		if(b){
			JOptionPane.showMessageDialog(null,"Welcome to MyQQ!","Log Success",JOptionPane.CLOSED_OPTION);
			c = new Client(number);
			Connect.connect();
			Connect.select(number);
			String name = Connect.toName();
			name2+=name+"\t"+number+"\n";
			l.add(((Client)c));
			for(int i=0;i<l.size();i++){
				c = (Client)l.get(i);
				c.receive(name+" enter into the room\n");
				c.addClient(name2);
			}
		}else{
			JOptionPane.showMessageDialog(null,"Your account is online!","Log Failed",JOptionPane.ERROR_MESSAGE);
		}
	}
/*	public static void deleteUser(String number){
		for(int i=0;i<l.size();i++){
			if((((Client)l.get(i)).toNo()).equals(number)){
				l.remove(i);
			}
		}
	}*/
	public static void messagePassToClient(String noFrom,String m,String noTo){
		String s = "";
		if(noTo.equals("")){
			Connect.connect();
			Connect.select(noFrom);
			s = Connect.toName()+":"+m+"\n";
			for(int i=0;i<l.size();i++){
				c = (Client)l.get(i);
				c.receive(s);
			}
	//		Connect.disConnect();
		}else{
			Connect.connect();
			Connect.select(noTo);
			if(Connect.toNumber() == null){
				for(int i=0;i<l.size();i++){
					if((((Client)l.get(i)).toNo()).equals(noFrom)){
						c = (Client)l.get(i);
						c.receive("The number not exist!\n");
					}
				}
		//		Connect.disConnect();
			}else{
				Connect.connect();
				Connect.select(noFrom);
				s = Connect.toName()+":"+m+"\n";
				for(int i=0;i<l.size();i++){
					if((((Client)l.get(i)).toNo()).equals(noFrom)){
						c = (Client)l.get(i);
						c.receive(s);
					}else if((((Client)l.get(i)).toNo()).equals(noTo)){
						c= (Client)l.get(i);
						c.receive(s);
					}
				}
		//		Connect.disConnect();
			}
		}
	}
}