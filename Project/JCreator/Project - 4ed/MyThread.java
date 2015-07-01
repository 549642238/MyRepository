/**	If different clients send a message at the same time,the Thread can 
 *	make them in different time be sent to all users*/
package myqq.thread;

import myqq.observer.Observer;
import myqq.record.Record;
import myqq.thread2.MyThread2;

public class MyThread extends Thread{
	public MyThread(){
		start();
	}
	public synchronized void f(){
		Record.messagePassToClient(Observer.toS1(),Observer.toS2(),Observer.toS3());
	}
	public void run(){
		while(true){
			if(this.isAlive()){
				//System.out.println("Alive");
			}
			if(Observer.toState()){
				f();
				Observer.setState();
				Observer.bb=false;
			}
		}
	}
}