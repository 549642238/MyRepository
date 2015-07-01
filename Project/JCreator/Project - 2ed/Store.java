/** Main function:comeS() used to pass message from Client to Server,
 *the state used to automic operation*/
package myqq.store;

import javax.media.*;
import java.io.*;

import myqq.server.Server;

public class Store{
	private static boolean state = false;
	public static boolean toState(){
		return state;
	}
	public static void changeState(){
		if(state){
			state = false;
		}else{
			state = true;
		}
	}
	public static void comeS(String a,String b,String c){
		Server.f(a,b,c);
	}
	public static void play(){
		File f1 = new File("a.wav"); 
		Player player=null;
		try{
			player = Manager.createRealizedPlayer(f1.toURI().toURL());
		}catch(Exception e){
			System.out.println("Error");
			System.exit(0);
		}
		player.prefetch();
		player.start();
	}
}