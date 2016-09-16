package com.example.nao_controller;

import java.io.*;
import java.net.Socket;

public class Connection {
	public static Socket client;				//�������ͨ�ŵ�����
	public static BufferedReader br;			//��������
	public static PrintWriter writer;			//д������
	public static boolean waiting = true;		//���ӵȴ�
	public static boolean link = false;			//���ӽ��
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
