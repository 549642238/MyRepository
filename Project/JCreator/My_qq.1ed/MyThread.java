package MYQQ.mythread;

import MYQQ.myqq.MyQQ;
import MYQQ.communication.Communication;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 

public class MyThread extends Thread{
	private JTextArea ta=new JTextArea();
	public MyThread(JTextArea t){
		ta=t;
		try{
			sleep(300);
		}catch(Exception e){
			System.out.println("mythread error0");
		}
		start();
	}
	public synchronized void f(){
		if(Communication.currentCount<Communication.count){
			ta.append(Communication.s);
			Communication.currentCount++;
			if(Communication.currentCount==Communication.count){
				Communication.s="";
			}
			try{
				sleep(100);
			}catch(Exception e){
				System.out.println("mythread error1");
			}
		}
	} 
	public void run(){
		while(true){
			f();
			yield();
		}
	}
}