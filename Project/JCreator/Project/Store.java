/** Main function:comeS() used to pass message from Client to Server,
 *the state used to automic operation*/
package myqq.store;

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
}