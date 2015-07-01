package apanel;

import java.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.border.*;

import sound.Sound;
import controller.Controller;

public class APanel extends JPanel{
	private ArrayList<JButton> bl = new ArrayList<JButton>();
	private ArrayList<JButton> bm = new ArrayList<JButton>();
	private boolean condition = true;
	private MyThread t = null;
	private MyThreada tt = null;
	private JButton b0 = new JButton("0");
	private JButton b1 = new JButton("1");
	private JButton b2 = new JButton("2");
	private JButton b3 = new JButton("3");
	private JButton b4 = new JButton("4");
	private JButton b5 = new JButton("5");
	private JButton b6 = new JButton("6");
	private JButton b7 = new JButton("7");
	private JButton b8 = new JButton("8");
	private JButton b9 = new JButton("9");
	private JButton bd = new JButton(".");
	private JButton bp = new JButton("¦Ð");
	private class MyThread extends Thread{
		private ArrayList<JButton> list = new ArrayList<JButton>();
		public MyThread(ArrayList<JButton> l){
			super();
			list = l;
			start();
		}
		public void run(){
			try{
				while(condition){
					int i=0;
					for(;i<list.size();i++){
						if(i==0 || i==1 || i==2){
							((JButton)list.get(i)).setBackground(Color.YELLOW);
						}else{
							((JButton)list.get(i)).setBackground(Color.RED);
						}
					}
					sleep(1500);
					for(i=0;i<list.size();i++){
						((JButton)list.get(i)).setBackground(Color.LIGHT_GRAY);
					}
					sleep(1500);
				}
			}catch(Exception e){
			//	System.out.println("Close Shining Successfully!");
			}
		}
	}
	private class MyThreada extends Thread{
		private ArrayList<JButton> list = new ArrayList<JButton>();
		public MyThreada(ArrayList<JButton> l){
			super();
			list = l;
			start();
		}
		public void run(){
			try{
				sleep(1500);
				while(condition){
					int i=0;
					for(;i<list.size();i++){
						((JButton)list.get(i)).setBackground(Color.GREEN);
					}
					sleep(1000);
					for(i=0;i<list.size();i++){
						((JButton)list.get(i)).setBackground(Color.LIGHT_GRAY);
					}
					sleep(1000);
				}
			}catch(Exception e){
			//	System.out.println("Close Shining Successfully!");
			}
		}
	}
	public void stopShining(){
		try{
			condition = false;
			t.interrupt();
			tt.interrupt();
			for(int i=0;i<bl.size();i++){
				((JButton)bl.get(i)).setBackground(Color.LIGHT_GRAY);
			}
			for(int i=0;i<bm.size();i++){
				((JButton)bm.get(i)).setBackground(Color.LIGHT_GRAY);
			}
		}catch(Exception e){
			System.out.println("Thread ERROR");
			e.printStackTrace();
		}
	}
	public void conShining(){
		try{
			condition = true;
			t = new MyThread(bl);
			tt = new MyThreada(bm);
		}catch(Exception e){
			System.out.println("Thread ERROR");
			e.printStackTrace();
		}
	}
	public APanel(){
		this.setBounds(5, 130, 290, 200);
		this.setBackground(Color.CYAN);
		this.setBorder(new LineBorder(Color.RED));
		this.setLayout(new GridLayout(4,3,3,5));
		this.add(b7);
		this.add(b8);
		this.add(b9);
		this.add(b4);
		this.add(b5);
		this.add(b6);
		this.add(b1);
		this.add(b2);
		this.add(b3);
		this.add(b0);
		this.add(bd);
		this.add(bp);
		bl.add(b0);
		bl.add(b2);
		bl.add(bp);
		bl.add(b7);
		bl.add(b5);
		bl.add(b9);
		bm.add(b1);
		bm.add(b4);
		bm.add(b8);
		bm.add(b6);
		bm.add(b3);
		bm.add(bd);
		t = new MyThread(bl);
		tt = new MyThreada(bm);
		this.setToolTipText("Number Button Panel:fill number to screen when clicked");
		b0.setBackground(Color.LIGHT_GRAY);
		b0.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b0.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(0);
			}
		});
		b1.setBackground(Color.LIGHT_GRAY);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(1);
			}
		});
		b2.setBackground(Color.LIGHT_GRAY);
		b2.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(2);
			}
		});
		b3.setBackground(Color.LIGHT_GRAY);
		b3.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(3);
			}
		});
		b4.setBackground(Color.LIGHT_GRAY);
		b4.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(4);
			}
		});
		b5.setBackground(Color.LIGHT_GRAY);
		b5.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b5.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(5);
			}
		});
		b6.setBackground(Color.LIGHT_GRAY);
		b6.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b6.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(6);
			}
		});
		b7.setBackground(Color.LIGHT_GRAY);
		b7.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b7.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(7);
			}
		});
		b8.setBackground(Color.LIGHT_GRAY);
		b8.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b8.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(8);
			}
		});
		b9.setBackground(Color.LIGHT_GRAY);
		b9.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		b9.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(9);
			}
		});
		bd.setBackground(Color.LIGHT_GRAY);
		bd.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		bd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(10);
			}
		});
		bp.setBackground(Color.LIGHT_GRAY);
		bp.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED)));
		bp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(11);
			}
		});
	}
	private void ring(){
		if(Sound.toB()){
			Sound.ring();
		}
	}
}
