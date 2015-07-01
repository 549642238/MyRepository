package paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Paint extends JPanel {
	public void paintComponent(Graphics g) {
		super.paintComponent(g);  
		this.setBackground(Color.PINK);
		this.setBounds(20, 40, 350, 300);
	    g.setColor(Color.ORANGE);
	    int i=0;  
	    int xCenter = getWidth() / 2;  
	    int yCenter = getHeight() / 2;  
	    int radius = (int)(Math.min(getWidth(), getHeight()) * 0.4);  
	 
	    int x = xCenter - radius;  
	    int y = yCenter - radius;  
	 
	    //使用while循环画弧形  
	    while(i<360){  
	        g.fillArc(x, y, 2 * radius, 2 * radius, i, 30);  
	        i+=90;  
	    }  
	  }  
} 
