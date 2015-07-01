/** A bridge connect Client and MyThread,avoid forming a circle*/  
package myqq.observer;

import javax.media.*;
import java.io.*;

public class Observer{
	private static String 	s1 = new String(),
							s2 = new String(),
							s3 = new String();
	private static boolean b = false;
	public static boolean toState(){
		return b;
	}
	public static void setState(){
		if(b){
			b = false;
		}else{
			b=true;
		}
	}
	public static void setS1(String s){
		s1 = s;
	}
	public static void setS2(String s){
		s2 = s;
	}
	public static void setS3(String s){
		s3 = s;
	}
	public static String toS1(){
		return s1;
	}
	public static String toS2(){
		return s2;
	}
	public static String toS3(){
		return s3;
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
	public static void main(String[] arg){
		play();
	}
}