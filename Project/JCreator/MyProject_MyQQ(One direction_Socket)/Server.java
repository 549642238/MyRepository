/** Server:A new client thread created as long as an client log
 *	Can receive messages from client,then store the messages in 
 *	a static pool,waitng for dest_client to prefetch*/
package myqq.server;

import java.util.*;
import java.io.*;
import java.net.*;

import myqq.observer.Observer;

class ThreadServerHandler extends Thread implements Runnable{
	private Socket incoming;
	public ThreadServerHandler(Socket in){
		incoming = in;
		start();
	}
	public void run(){
		try{
			f();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void f() throws Exception{
		while(true){
			if(this.isAlive()){
				System.out.println("Communication");
			}
			try{
				BufferedReader br = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String temp;
				int index;
				Observer.setS1(br.readLine());
				Observer.setS2(br.readLine());
				while ((temp=br.readLine()) != null) {
            		if ((index = temp.indexOf("eof")) != -1) {//遇到eof时就结束接收  
            			sb.append(temp.substring(0, index));  
           		    	break;  
           			}  
           			sb.append(temp);  
         		}
         		Observer.setS3(sb.toString());
         		Observer.setState();
         		if(sb.toString().equals("Exit_MyQQ")){
         			incoming.close();
         			break;
         		}
			}catch(Exception e){
				e.printStackTrace();
			}
		}	
	}
}
public class Server{
	public Server(int port){
		try{
			ServerSocket server = new ServerSocket(port);
			while(true){
				Socket client = server.accept();
				ThreadServerHandler t=new ThreadServerHandler(client);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}