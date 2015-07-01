/**	If different clients send a message at the same time,the Thread can 
 *	make them in different time be sent to all users*/
package myqq.thread;

import myqq.store.Store;

public class MyThread extends Thread{
	private String s1 = new String(),
	s2 = new String(),
	s3 = new String();
	public MyThread(String a,String b,String c){
		s1 = a;
		s2 = b;
		s3 = c;
		start();
	}
	public synchronized void f(){
		while(Store.toState());
		Store.changeState();
		Store.comeS(s1,s2,s3);
		Store.changeState();
	}
	public void run(){
		f();
	}
}