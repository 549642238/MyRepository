/** An assistant thread only to help avoid two different cliens
 *	send message at the same time*/ 
package myqq.thread2;

import myqq.observer.Observer;

public class MyThread2 extends Thread{
	public MyThread2(){
		start();
	}
	public synchronized void f(){
		while(Observer.bb);
		Observer.setState();
		Observer.bb = true;
	}
	public void run(){
		f();
	}
}