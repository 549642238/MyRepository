package bpanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.border.*;

import controller.Controller;
import sound.Sound;

public class BPanel extends JPanel{
	private ArrayList<JButton> Buttonlist = new ArrayList<JButton>();
	private boolean condition = true;
	private MyThread t = null;
	private JButton b1 = new JButton("DEL");
	private JButton b2 = new JButton("AC");
	private JButton b3 = new JButton("¡Á");
	private JButton b4 = new JButton("¡Â");
	private JButton b5 = new JButton("+");
	private JButton b6 = new JButton("-");
	private JButton b7 = new JButton("Ans");
	private JButton b8 = new JButton("=");
	private class MyThread extends Thread{
		private ArrayList list = new ArrayList();
		public MyThread(ArrayList l){
			super();
			list = l;
			start();
		}
		public void run(){
			try{
				while(condition){
					int i=0;
					for(;i<list.size();i++){
						sleep(1500);
						if(i==0){
							((JButton)list.get(list.size()-1)).setBackground(Color.LIGHT_GRAY);
						}else{
							((JButton)list.get(i-1)).setBackground(Color.LIGHT_GRAY);
						}
						((JButton)list.get(i)).setBackground(Color.MAGENTA);
					}
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
			for(int i=0;i<Buttonlist.size();i++){
				((JButton)Buttonlist.get(i)).setBackground(Color.LIGHT_GRAY);
			}
		}catch(Exception e){
			System.out.println("Thread ERROR");
			e.printStackTrace();
		}
	}
	public void conShining(){
		try{
			condition = true;
			t = new MyThread(Buttonlist);
		}catch(Exception e){
			System.out.println("Thread ERROR");
			e.printStackTrace();
		}
	}
	public BPanel(){
		this.setBounds(305, 130, 190, 200);
		this.setBackground(Color.PINK);
		this.setBorder(new LineBorder(Color.RED));
		this.setLayout(new GridLayout(4,2,3,5));
		this.add(b1);
		this.add(b2);
		this.add(b3);
		this.add(b4);
		this.add(b5);
		this.add(b6);
		this.add(b7);
		this.add(b8);
		Buttonlist.add(b3);
		Buttonlist.add(b4);
		Buttonlist.add(b5);
		Buttonlist.add(b6);
		t = new MyThread(Buttonlist);
		this.setToolTipText("Operation Button Panel:Each operator matches a kind of operation");
		b1.setBackground(Color.YELLOW);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(12);
			}
		});
		b2.setBackground(Color.GREEN);
		b2.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(13);
			}
		});
		b3.setBackground(Color.LIGHT_GRAY);
		b3.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(14);
			}
		});
		b4.setBackground(Color.LIGHT_GRAY);
		b4.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(15);
			}
		});
		b5.setBackground(Color.LIGHT_GRAY);
		b5.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b5.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(16);
			}
		});
		b6.setBackground(Color.LIGHT_GRAY);
		b6.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b6.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(17);
			}
		});
		b7.setBackground(Color.ORANGE);
		b7.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b7.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(18);
			}
		});
		b8.setBackground(Color.GREEN);
		b8.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b8.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(19);
			}
		});
	}
	public void ring(){
		if(Sound.toB()){
			Sound.ring();
		}
	}
}
