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
	private static LinkedList<Client> l = new LinkedList();
	private static Client c = null;
	public static void addUser(String number){
		c = new Client(number);
		l.add(((Client)c));
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