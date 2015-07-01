package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int port = 2000;
    public static void main(String [] args){
        try{
            ServerSocket ss = new ServerSocket(port);
            while (true){
               // System.out.println("create server socket succed");
                    Socket cs = ss.accept(); 
                    Thread t = new Thread(new DealThread(cs));
                    t.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        }
}


