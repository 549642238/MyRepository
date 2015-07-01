package display;

import javax.swing.*;

public class Display{
	public static void run(JFrame frame,JApplet applet,int width,int height){
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width,height);
		frame.getContentPane().add(applet);
		applet.init();
		applet.start();
		frame.setLocation(400, 120);
		frame.setResizable(false); 				//���ô��ڲ��ܸı��С
		frame.setVisible(true);
	}
}