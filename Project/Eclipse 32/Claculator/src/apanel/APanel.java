package apanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.border.*;

import sound.Sound;
import controller.Controller;

public class APanel extends JPanel{
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
