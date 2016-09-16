package com.example.nao_controller;

import java.io.*;
import java.net.Socket;

public class Connection {
	public static Socket client;				//与服务器通信的连接
	public static BufferedReader br;			//读服务器
	public static PrintWriter writer;			//写服务器
	public static boolean waiting = true;		//连接等待
	public static boolean link = false;			//连接结果
	public static void connect(String s){
		try{
			client = new Socket(s,8888);
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
			link = true;
			waiting = false;
		}catch(Exception e){
			link = false;
			waiting = false;
			e.printStackTrace();
		}
	}
}
