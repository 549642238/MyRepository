//用于显示界面，多个界面显示会调用该类
package display;

import javax.swing.*;

public class Display{
	public static void run(JFrame frame,JApplet applet,int width,int height){
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width,height);
		frame.getContentPane().add(applet);
		applet.init();
		applet.start();
		frame.setLocation(230, 80);
		frame.setResizable(false); 				//设置窗口不能改变大小
		frame.setVisible(true);
	}
}