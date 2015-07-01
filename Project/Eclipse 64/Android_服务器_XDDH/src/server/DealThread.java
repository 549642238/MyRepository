package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import factory.DaoFactory;
import vo.*;

public class DealThread implements Runnable {
    private Socket cc;
    BufferedReader in;
    PrintWriter out;
    User user = null;

    DealThread(Socket cc) {
        this.cc = cc;
        try {
            this.in = new BufferedReader( new InputStreamReader(cc.getInputStream()) );
            this.out = new PrintWriter(cc.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        // TODO Auto-generated method stub
        try {
            while (true) {
                String stream = in.readLine();
                System.out.println("Stream:" + stream);    
                switch (stream) {
                case ("login"):// 发现登陆消息类型
                    login(in.readLine(),in.readLine());break;
                case ("logout"):
                    logout(user.getName());break;
                case ("register"):
                    register(in.readLine(),in.readLine(),in.readLine());break;
                case ("modify"):
                    User user = new User();
                    user.setId(in.readLine());
                    user.setPasswd(in.readLine());
                    user.setName(in.readLine());
                    user.setSex(in.readLine());
                    user.setEmail(in.readLine());
                    user.setPhone(in.readLine());
                    user.setGrade(in.readLine());
                    user.setBirth(in.readLine());
                    modifyUser(user);break;
                case ("dimsearch")://利用窗口搜索或者利用 一定菜名搜索返回有关菜的品论
                    dimSearch(in.readLine(),in.readLine(),in.readLine(),in.readLine());
                case ("remark"):
                    sendRecommend();break;
                case ("like"):
                case ("unlike"):
                case ("build"):
                    sendBuilds();break;
                case ("layer"):
                    sendLayers(in.readLine());break;
                case ("window"):
                    sendWindows(in.readLine(),in.readLine());break;
                case ("tucao"):
                    TuCao(in.readLine(),in.readLine(),in.readLine(),in.readLine(),in.readLine(),in.readLine(),Integer.parseInt(in.readLine()));
                    break;
                case ("recipe"):
                    sendRecipe(in.readLine(),in.readLine(),in.readLine());break;
                case("searchremarkbydish")://只利用菜名搜索
                    dishSearchbydishname(in.readLine());break;
                }//switch

            }//while

        } catch (Exception e) {
            e.printStackTrace();
        }//try
    }

    private void login(String id, String passwd) throws Exception{
        out.println("login");
        if (DaoFactory.getUserInstance().findByUserId(id) == null) {//检查是否有该用户
            out.println("nouser");// 1
        } else {
            if ((this.user = DaoFactory.getUserInstance().findByUserIdandPasswd(id, passwd)) == null) {//核实用户身份
              out.println("passwderror");// 2
            } else {
                out.println("loginsucceed");//3
                sendProfile(user);
                //sendRecommend();
                }
           }
     }
    
    private void logout(String id) throws Exception{//用户正常退出 相关处理
        out.println("logout");
        this.in.close();
        this.out.close();
        this.cc.close();
        out.println("logoutsucceed");
     }
    private void register(String id,String passwd,String name)throws Exception{
        if(DaoFactory.getUserInstance().findByUserId(id) != null){
            out.println("register");
            out.println("alreadyuseed");
        }else{
            User user  = new User();
            user.setId(id);
            user.setPasswd(passwd);
            user.setName(name);
            user.setGrade(in.readLine());
            user.setSex(in.readLine());            
            user.setEmail(in.readLine());
            user.setPhone(in.readLine());
            user.setBirth(in.readLine());
            if(DaoFactory.getUserInstance().doCreate(user)){
                out.println("register");
                out.println("succeed");
            }else{
                out.println("register");
                out.println("failure");
            }
            
        }
    }
    private void sendProfile(User user){
        out.println(user.getId());
        out.println(user.getName());
        out.println(user.getGrade());
        out.println(user.getBirth());
        out.println(user.getSex());
        out.println(user.getEmail());
        out.println(user.getPhone());
    }
    private void modifyUser(User user){
        try {
            if(DaoFactory.getUserInstance().modifyUser(user)){
                out.println("modify");
                out.println("succeed");
                out.println("login");
                out.println("loginsucceed");
                sendProfile(user);
            }else{
                out.println("modify");
                out.println("failure");
            }//if
        } catch (Exception e) {
            e.printStackTrace();
        }//try
    }
    private void sendRecommend(){
            out.println("remark");
        try{
            ArrayList<Remark> remarks = new ArrayList<Remark>();
            remarks = DaoFactory.getRemarkInstance().findRemark();
            for(Remark remark: remarks){
                out.println(remark.getBuildName() + " " + remark.getLayer() + " " + remark.getWindowName() +" "+ remark.getDishName());
                out.println(remark.getUserName() +":"+ remark.getContent());
                out.println(remark.getDishScore());
                //out.println(remark.getTimeId());
                }
            out.println("end");
        }catch(Exception e){
            e.printStackTrace();
        }//try
    }//sendRecommend
    
    public void sendBuilds(){
        out.println("build");
        try{
            ArrayList<String> builds = new ArrayList<String>();
            builds = DaoFactory.getWindowInstance().findBuildName();
            for(String build: builds){
                    out.println(build);
            }//for
            out.println("end");
        }catch(Exception e){
            e.printStackTrace();
        }//try
        
    }//sendBuilds
    public void sendLayers(String build){
       
        out.println("layer");
        try{
            ArrayList<String> layers = new ArrayList<String>();
            layers = DaoFactory.getWindowInstance().findLayers(build);
            for(String layer:layers){
                    out.println(layer);
            }//for
            out.println("end");
        }catch(Exception e){
            e.printStackTrace();
        }//try
        
    }
    private void sendWindows(String build,String layer){
        out.println("window");
        try{
            ArrayList<String> windows = new ArrayList<String>();
            windows = DaoFactory.getWindowInstance().findWindowsbyBuildandLayer(build,layer);
            for(String window: windows){
                    out.println(window);
            }//for
            out.println("end");
        }catch(Exception e){
            e.printStackTrace();
        }//try
    }
    private void sendRecipe(String build,String layer,String windowname){
        out.println("recipe");
        try{
            ArrayList<String> recipes = new ArrayList<String>();
            Window window = DaoFactory.getWindowInstance().findWindowbyBuildLayerandWindowname(build, layer, windowname);
            recipes = DaoFactory.getRecipeInstance().findDishsbyWindowid(window);
            for(String recipe: recipes){
                    out.println(recipe);
            }//for
            out.println("end");
        }catch(Exception e){
            e.printStackTrace();
        }//try
    }
   private void TuCao(String username, String buildname,String layer, String windowname, String dishname, String content,int dishscore){
       out.println("tucao");
       try{
          if(DaoFactory.getRemarkInstance().insertRemark(username, buildname, layer, windowname, dishname, content, dishscore)){
              out.println("succeed");
          }else{
              out.println("failure");
          }//if
              
       }catch(Exception e){
           e.printStackTrace();
       }//try
   }
   private void dishSearchbydishname(String dishname){
       out.println("searchremarkbydish");
       try{
           ArrayList<Remark> remarks = new ArrayList<Remark>();
           remarks = DaoFactory.getRemarkInstance().findRemarkbyDishName(dishname);
           for(Remark remark: remarks){
               out.println(remark.getBuildName() + " " + remark.getLayer() + " " + remark.getWindowName() +" "+ remark.getDishName());
               out.println(remark.getUserName() +":"+ remark.getContent());
               out.println(remark.getDishScore());
               //out.println(remark.getTimeId());
               }
           out.println("end");
       }catch(Exception e){
           e.printStackTrace();
       }//try
   }
   

   private void dishRemarkSearch(String buildname,String layer,String windowname, String dishname){
       out.println("dishremarksearch");
       System.out.println("in dishreamrk search");
       try{
           ArrayList<Remark> remarks = new ArrayList<Remark>();
           remarks = DaoFactory.getRemarkInstance().findRemarkbyBLWD(buildname, layer, windowname, dishname);
           for(Remark remark: remarks){
               out.println(remark.getBuildName() + " " + remark.getLayer() + " " + remark.getWindowName() +" "+ remark.getDishName());
               out.println(remark.getUserName() +":"+ remark.getContent());
               out.println(remark.getDishScore());
               //out.println(remark.getTimeId());
               System.out.println("reamrk:"+remark.getDishName());
               }
           out.println("end");
       }catch(Exception e){
           e.printStackTrace();
       }//try
   }//dishRemarkSearch
   
   private void windowscoreSearch(String buildname,String layer,String windowname){
       out.println("windowscoresearch");
       try{
           System.out.println("in  windowscoresearch");
           Window window = new Window();
           window = DaoFactory.getWindowInstance().findWindowbyBuildLayerandWindowname(buildname, layer, windowname);
           out.println(window.getWindowScore());//发送窗口 分数
//           ArrayList<String> recipes = new ArrayList<String>();
//           recipes = DaoFactory.getRecipeInstance().findDishsbyWindowid(window);
//           for(String recipe: recipes){
//               out.println(recipe);
//           }//for
//           out.println("end");
           
       }catch(Exception e){
           e.printStackTrace();
       }//try
   }//windowscoreSearch
   
   private void dimSearch(String buildname,String layer,String windowname, String dishname){
       System.out.println(buildname+layer+windowname+"**"+dishname+"**");
       try{
           if(dishname.equals("")){
               windowscoreSearch(buildname,layer,windowname);
           }else{
               dishRemarkSearch(buildname,layer,windowname,dishname);
           }//if
           
       }catch(Exception e){
           e.printStackTrace();
       }//try
       
   }//dimSearch
   
}
