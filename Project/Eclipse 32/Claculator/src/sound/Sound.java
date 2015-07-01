package sound;

import java.io.*;

import javax.media.*;

public class Sound{
	private static Player player1;
	private static Player player2;
	private static boolean b = true;
	public static void set(){
		b = !b;
	}
	public static boolean toB(){
		return b;
	}
	public static void play(){
		File f1 = new File("sound\\a.mp3"); 
		try{
			player1 = Manager.createRealizedPlayer(f1.toURI().toURL());
		}catch(Exception e){
			System.out.println("Error");
			System.exit(0);
		}
		player1.prefetch();
		player1.start();
	}
	public static void ring(){
		File f1 = new File("sound\\b.wav"); 
		try{
			player2 = Manager.createRealizedPlayer(f1.toURI().toURL());
		}catch(Exception e){
			System.out.println("Error");
			System.exit(0);
		}
		player2.prefetch();
		player2.start();
	}
	public static void exit(){
		player1.close();
	}
	public static void suspend(){
		player1.stop();
	}
	public static void con(){
		player1.start();
	}
}