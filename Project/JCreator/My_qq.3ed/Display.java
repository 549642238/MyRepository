/** Create a frame and display it,so you can operate frame outside*/
package MYQQ.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 

public class Display{
	public static void run(JFrame frame,JApplet applet,int width,int height){
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width,height);
		frame.getContentPane().add(applet);
		applet.init();
		applet.start();
		frame.setVisible(true);
	}
}