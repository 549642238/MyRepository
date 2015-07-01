package calculator;

import java.util.*;
import java.io.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.border.*;

import sound.Sound;
import display.Display;
import apanel.APanel;
import bpanel.BPanel;
import cpanel.CPanel;

public class Calculator extends JApplet{
	private static JFrame frame = new JFrame("酷炫计算器");
	private APanel panela = new APanel();
	private BPanel panelb = new BPanel();
	private CPanel panelc = new CPanel();
	
	private JButton b = new JButton("Exit");
	private static JTextArea  ta = new JTextArea(20,10);
	private static JTextField t = new JTextField(100);
	private JLabel l = new JLabel("       Ans = ");
	private JButton info = new JButton("About");
	public void init(){
		JLabel k = new JLabel("ds");
		Container c = getContentPane();
		c.setLayout(null); 								//不使用布局管理器
		c.setBackground(Color.MAGENTA);
		c.add(panela);
		c.add(panelb);
		c.add(panelc);
		ta.setBackground(Color.CYAN);
		ta.setBorder(new LineBorder(Color.BLUE));
		ta.setEditable(false);
		ta.setToolTipText("Screen:Display information of input and system message");
		ta.setText("\n\t\tCASIO Made in XiDian");
		ta.setBounds(5, 5, 490, 45);
		c.add(ta);
		info.setBackground(Color.YELLOW);
		info.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		info.setToolTipText("Editon Information");
		info.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(null,"Made in Xidian,Author\n CZH, version 1.0,\nFinished in 2015/5/22","Edition Information",JOptionPane.CLOSED_OPTION);
			}
		});
		info.setBounds(5, 55, 70, 25);
		c.add(info);
		l.setBackground(Color.GREEN);
		l.setBorder(new LineBorder(Color.BLUE));
		l.setOpaque(true);
		l.setBounds(80, 55, 75, 25);
		l.setToolTipText("The result = ");
		c.add(l);
		t.setBackground(Color.PINK);
		t.setBorder(new LineBorder(Color.BLUE));
		t.setToolTipText("Display the result");
		t.setText("0.0");
		t.setEditable(false);
		t.setBounds(160, 55, 335, 25);
		c.add(t);
		b.setBackground(Color.RED);
		b.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.BLUE)));
		b.setToolTipText("Exit KuXuan Calculator");
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(Sound.toB()){
					Sound.ring();
				}
				JOptionPane.showMessageDialog(null,"Goodbye!","Exit",JOptionPane.CLOSED_OPTION);
				Sound.exit();
				frame.dispose();
				System.exit(0);
			}
		});
		b.setBounds(402, 85, 90, 40);
		c.add(b);
	}
	public static String toInput(){
		return ta.getText();
	}
	public static void setInput(String s){
		ta.setText(s);
	}
	public static void setOutput(String s){
		t.setText(s);
	}
	public static void main(String[] arg){
		Display.run(frame, new Calculator(), 510, 370);
	}
}