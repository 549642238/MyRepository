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
		start();
	}
	public void run(){
		while(true){
			if(Communication.currentCount<Communication.count){
				if(this.isAlive()){
					System.out.println("alive");
				}
				ta.append(Communication.s);
				Communication.currentCount++;
				while(Communication.currentCount!=Communication.count){
					System.out.println("wait...");
					yield();
				}
				Communication.s="";
			}
		}
	}
}