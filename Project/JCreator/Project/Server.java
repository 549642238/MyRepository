/** Only a message passer,aimed to pass message from Store to Record*/ 
package myqq.server;

import myqq.record.Record;

public class Server{
	public static void f(String s1,String s2,String s3){
		Record.messagePassToClient(s1,s2,s3);
	}
}