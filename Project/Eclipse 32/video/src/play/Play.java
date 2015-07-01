package play;

import java.io.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.border.*;
import javax.media.*;

import display.Display;
import paint.Paint;

public class Play {
	public static void main(String[] arg){
		Display.run(new JFrame("MyPlayer"), new Face(), 400, 500);
	}
}
class Face extends JApplet{
	private JButton b = new JButton("Music Off");
	private boolean boo = false;
	public void init(){
		Container c = getContentPane();
		c.setBackground(Color.CYAN);
		c.setLayout(new FlowLayout());
		c.add(new Paint());
		c.add(b);
		b.setBounds(100, 400, 50, 30);
		b.setBackground(Color.GRAY);
		b.setBorder(new LineBorder(Color.BLUE));
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!boo){
					Sound.suspend();
					b.setText("Music On");
					b.setBackground(Color.GREEN);
					boo = true;
				}else{
					Sound.con();
					b.setText("Music Off");
					b.setBackground(Color.GRAY);
					boo = false;
				}
			}
		});
		Sound.play();
	}
}
class Sound{
	private static Player player;
	public static void play(){
		File f1 = new File("sound\\a.mp3"); 
		try{
			player = Manager.createRealizedPlayer(f1.toURI().toURL());
		}catch(Exception e){
			System.out.println("Error");
			System.exit(0);
		}
		player.prefetch();
		player.start();
	}
	public static void suspend(){
		player.stop();
	}
	public static void con(){
		player.start();
	}
}