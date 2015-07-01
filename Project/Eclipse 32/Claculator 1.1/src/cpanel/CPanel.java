package cpanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.border.*;

import controller.Controller;
import sound.Sound;

public class CPanel extends JPanel{
	private JButton b1 = new JButton("Pow");
	private JButton b2 = new JButton("Sqrt");
	private JButton b3 = new JButton("(");
	private JButton b4 = new JButton(")");
	
	private boolean b = false; 
	
	public CPanel(){
		this.setBounds(5, 85, 390, 40);
		this.setBackground(Color.GREEN);
		this.setBorder(new LineBorder(Color.RED));
		this.setLayout(new GridLayout(1,4,3,5));
		this.add(b1);
		this.add(b2);
		this.add(b3);
		this.add(b4);
		this.setToolTipText("Function Button Panel:Including functions and methods");
		b1.setBackground(Color.CYAN);
		b1.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLACK)));
		b1.setToolTipText("Input a pow b = a^b");
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(20);
			}
		});
		b2.setBackground(Color.CYAN);
		b2.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLACK)));
		b2.setToolTipText("Input a sqrt b = a¡Ìb");
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(21);
			}
		});
		b3.setBackground(Color.PINK);
		b3.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b3.setToolTipText("Left bracket  [(]");
		b3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(22);
			}
		});
		b4.setBackground(Color.PINK);
		b4.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b4.setToolTipText("right bracket  [)]");
		b4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ring();
				Controller.addInput(23);
			}
		});
	}
	public void ring(){
		if(Sound.toB()){
			Sound.ring();
		}
	}
}