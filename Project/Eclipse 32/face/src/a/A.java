package a;

import java.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

import javax.media.*;

import display.Display;
public class A {
	public static void main(String[] arg){
		Display.run(Face.toFrame(), new Face(), 650, 620);
	}
}
class Face extends JApplet{
	private static JFrame frame = new JFrame("Poker Face");
	private JLabel l = new JLabel("Poker Face   Lady-gaga");
	private JButton music =  new JButton("Music Off");
	private JButton b = new JButton("Exit");
	
	private boolean bm = false;
	
	public static JFrame toFrame(){
		return frame;
	}
	public void init(){
		Container c = getContentPane();
		c.setBackground(Color.PINK);
		c.setLayout(null);					//不使用布局管理器就可以任意设置component位置
		Sound.play();
		l.setBackground(Color.ORANGE);
		l.setBorder(new LineBorder(Color.BLUE));
		l.setToolTipText("Information table");
		l.setBounds(240,10,140,20);
		l.setOpaque(true);
		c.add(l);
		c.add(new p1());
		c.add(new p2());
		b.setBackground(Color.RED);
		b.setBorder(new LineBorder(Color.YELLOW));
		b.setBounds(290,550,40,20);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.dispose();
				Sound.suspend();
				JOptionPane.showMessageDialog(null,"Goodbye!","Exit",JOptionPane.CLOSED_OPTION);
				System.exit(0);
			}
		});
		b.setToolTipText("Click here to exit!");
		c.add(b);
		music.setBackground(Color.GRAY);
		music.setBorder(new LineBorder(Color.BLUE));
		music.setToolTipText("Click to Turn off the BackgroundMusic");
		music.setBounds(500, 550, 70, 20);
		music.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(bm){
					Sound.con();
					music.setText("Music Off");
					music.setToolTipText("Click to Turn off the BackgroundMusic");
					music.setBackground(Color.GRAY);
					bm = false;
				}else{
					Sound.suspend();
					music.setText("Music On");
					music.setToolTipText("Click to Turn on the BackgroundMusic");
					music.setBackground(Color.GREEN);
					bm = true;
				}
			}
		});
		c.add(music);
	}
}
class p1 extends JPanel{
	public p1(){
		this.setBackground(Color.CYAN);
		this.setBounds(10, 40, 300, 500);
		this.setBorder(new LineBorder(Color.BLUE));
		this.setLayout(null);
		this.add(new p11());
		this.add(new p12());
	}
	private class p11 extends JPanel{
		private JButton b = new JButton("Button1-1");
		private JLabel l = new JLabel("Label1-1");
		public p11(){
			this.setBackground(Color.GREEN);
			this.setBounds(10, 10, 280, 300);
			this.setBorder(new LineBorder(Color.RED));
			this.setLayout(null);
			this.add(b);
			this.add(l);
			b.setBounds(10, 100, 100, 30);
			b.setBackground(Color.BLUE);
			b.setBorder(new LineBorder(Color.RED));
			b.setToolTipText("Button 1-1");
			l.setBounds(170, 150, 60, 20);
			l.setBackground(Color.LIGHT_GRAY);
			l.setBorder(new LineBorder(Color.RED));
			l.setOpaque(true);
		}
	}
	private class p12 extends JPanel{
		private JButton b = new JButton("Button1-2");
		private JLabel l = new JLabel("Label1-2");
		public p12(){
			this.setBackground(Color.LIGHT_GRAY);
			this.setBounds(10, 320, 280, 170);
			this.setBorder(new LineBorder(Color.RED));
			this.setLayout(null);
			this.add(b);
			this.add(l);
			b.setBounds(10, 100, 100, 30);
			b.setBackground(Color.GREEN);
			b.setBorder(new LineBorder(Color.RED));
			b.setToolTipText("Button 1-2");
			l.setBounds(170, 140, 60, 20);
			l.setBackground(Color.ORANGE);
			l.setBorder(new LineBorder(Color.RED));
			l.setOpaque(true);
		}
	}
}
class p2 extends JPanel{
	private JButton b = new JButton("Button 2");
	private JLabel l = new JLabel("Label2");
	public p2(){
		this.setBackground(Color.YELLOW);
		this.setBounds(320, 40, 300, 500);
		this.setBorder(new LineBorder(Color.BLUE));
		this.setLayout(null);
		this.add(b);
		this.add(l);
		b.setBounds(10, 200, 100, 30);
		b.setBackground(Color.PINK);
		b.setBorder(new LineBorder(Color.RED));
		b.setToolTipText("Button 2");
		l.setBounds(170, 150, 60, 20);
		l.setBackground(Color.CYAN);
		l.setBorder(new LineBorder(Color.RED));
		l.setOpaque(true);
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
	public static void exit(){
		player.close();
	}
	public static void suspend(){
		player.stop();
	}
	public static void con(){
		player.start();
	}
}