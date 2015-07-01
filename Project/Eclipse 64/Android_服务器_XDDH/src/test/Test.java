package test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import factory.DaoFactory;
import vo.*;
public class Test {
    public static void main(String args[])throws Exception{
//      User user = new User();
//        user.setId("13121143");
//        user.setName("鏉庣尞娑�");
//        user.setPasswd("admin");
//        //user.setGrade(3);
//        user.setSex("鐢�");
//        user.setBirth("19930826");
//        //user.setEmail("shineneo1@yahoo.com");
//        user.setPhone("18629608829");
//       DaoFactory.getUserInstance().doCreate(user);
//       User user2 = DaoFactory.getUserInstance().findByUserId("13121143");
//       System.out.println(user2.getBirth()+user2.getRegisterTime());
        try{
            Socket soc = new Socket("localhost",2000);
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(),true);
            //login;
            out.println("login\n" + "231\n" + "admin"); 
            //login 涔嬪悗鎺ュ彈淇℃伅
            User user = new User();
            while(true){
                String stream = in.readLine();
                switch(stream){
                case ("login"):
                    stream = in.readLine();
                        if(stream.equals("loginsucced")){
                            //鏍规嵁鏈嶅姟鍣ㄤ俊鎭紝寰楀埌鐢ㄦ埛淇℃伅
                            user.setId(in.readLine());
                            user.setName(in.readLine());
                            user.setGrade(in.readLine());
                            user.setBirth(in.readLine());
                            user.setSex(in.readLine());
                            user.setEmail(in.readLine());
                            user.setPhone(in.readLine());
                            printUserInfo(user);
                            //鏍规嵁鏈嶅姟鍣ㄤ俊鎭紝寰楀埌鎺ㄨ崘鑿滆氨
                            printRecommend(in);
                            
                         }else if(stream.equals("nouser")){
                                    System.out.println("login :No user");
                         }else if(stream.equals("passwderror")){
                             System.out.println("login :passwd error");
                                 }
                    
                      }//switch
            }//while
        }catch(Exception e){
                    e.printStackTrace();
        }//catch
    }
    public static void printUserInfo(User user){
        System.out.println("Id:" + user.getId());
        //System.out.println("Passwd:" + user.getPasswd());
        System.out.println("Name:" + user.getName());
        System.out.println("Grade:" + user.getGrade());
        System.out.println("Birth:" + user.getBirth());
        System.out.println("Sex:" + user.getSex());
        System.out.println("Email:" + user.getEmail());
        System.out.println("Phone:" + user.getPhone());
       // System.out.println("Register Time:" + user.getRegisterTime());
    }
    public static void printRecommend(BufferedReader in)throws Exception{
        String stream =  in.readLine();
        while(!stream.equals("!@#$%^&*()")){
            System.out.println(stream);
            stream = in.readLine();
        }
    }
}
