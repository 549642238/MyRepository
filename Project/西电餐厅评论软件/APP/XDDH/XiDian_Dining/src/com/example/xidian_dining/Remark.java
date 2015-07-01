package com.example.xidian_dining;
/*
 * 评论活动，显示所有最新的 50条评论
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Remark extends Activity{
	private String reid = "";
	public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
        	new Thread(){
        		public void run(){
        			try {
        				Connection.pan2 = false;						//心跳线程终止循环
        				Connection.heartBeaten.interrupt();				//关闭心跳线程
        				Connection.pan = false;
        				Connection.listenThread.interrupt();
						Connection.writer.write("logout\n");
						Connection.writer.flush();
						Connection.br.close();
						Connection.writer.close();
						Connection.client.close();
						System.exit(0);
					} catch (IOException e) {
						System.out.println("Exit Error");
						e.printStackTrace();
					}
        		}
        	}.start();
        }
        return true;
    }
	public void mybuttonlistener1(View v){
 		reid = "";
 		final ListView lv = (ListView)findViewById(R.id.remark_listView1);
 		SimpleAdapter adapter = (SimpleAdapter)(lv.getAdapter());
 		final Button tbtn = (Button) v;
 		for (int i = 0; i < lv.getChildCount(); i++) {
 		     LinearLayout layout = (LinearLayout)lv.getChildAt(i);
 		     if(tbtn == (Button)layout.findViewById(R.id.simple_button1)){
 		    	 final TextView idText = (TextView)layout.findViewById(R.id.simple_textView4);
 		    	 reid = idText.getText().toString();
 		    	 break;
 		     }
 		}
 		new Thread(){
 			public void run(){
 				Connection.writer.write("zan\n");			//通信协议[client发server收]，表示客户端有人对某条评论点赞
 				Connection.writer.write(reid+"\n");
 				Connection.writer.write(Connection.user.getId()+"\n");
 				Connection.writer.flush();
 			}
 		}.start();
 		while(Connection.waiting12 == true);
        Connection.waiting12 = true;
        if(Connection.result.equals("succeed")){
     		String content = tbtn.getText().toString().trim();
     		Pattern p = Pattern.compile("[0-9][0-9]*");
            Matcher m = p.matcher(content);
            m.find();
            int a = (new Integer(m.group())).intValue();
            a++;
            ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
            for(int i=0;i<Connection.remark.size();i++){
            	Map<String,Object> item = new HashMap<String,Object>();
            	if(reid.equals(Connection.remark.get(i).getTimeId())){
            		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"层"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                	String sc = ""+(Connection.remark.get(i).getDishScore());
                	if(sc.equals("0.0")){
                		sc = "无";
                	}
                	item.put("l3", "评分:"+sc);
                	item.put("l4", "赞 "+a);
                	Connection.remark.get(i).setZanCount(a);
                	item.put("l5", "踩 "+(Connection.remark.get(i).getCaiCount()));
                	item.put("l6", Connection.remark.get(i).getTimeId());
                	items.add(item);
            	}else{
            		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"层"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                	String sc = ""+(Connection.remark.get(i).getDishScore());
                	if(sc.equals("0.0")){
                		sc = "无";
                	}
                	item.put("l3", "评分:"+sc);
                	item.put("l4", "赞 "+(Connection.remark.get(i).getZanCount()));
                	item.put("l5", "踩 "+(Connection.remark.get(i).getCaiCount()));
                	item.put("l6", Connection.remark.get(i).getTimeId());
                	items.add(item);
            	}
            }
            adapter = new SimpleAdapter(this,items,R.layout.simple_item,new String[]{"l1","l2","l3","l4","l5","l6"},new int[]{R.id.simple_textView1,R.id.simple_textView2,R.id.simple_textView3,R.id.simple_button1,R.id.simple_button2,R.id.simple_textView4});
            //tbtn.setText("赞 "+a);
            lv.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }else if(Connection.result.equals("repeat")){
     		String content = tbtn.getText().toString().trim();
     		Pattern p = Pattern.compile("[0-9][0-9]*");
            Matcher m = p.matcher(content);
            m.find();
            int a = (new Integer(m.group())).intValue();
            a--;
            ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
            for(int i=0;i<Connection.remark.size();i++){
            	Map<String,Object> item = new HashMap<String,Object>();
            	if(reid.equals(Connection.remark.get(i).getTimeId())){
            		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"层"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                	String sc = ""+(Connection.remark.get(i).getDishScore());
                	if(sc.equals("0.0")){
                		sc = "无";
                	}
                	item.put("l3", "评分:"+sc);
                	Connection.remark.get(i).setZanCount(a);
                	item.put("l4", "赞 "+a);
                	item.put("l5", "踩 "+(Connection.remark.get(i).getCaiCount()));
                	item.put("l6", Connection.remark.get(i).getTimeId());
                	items.add(item);
            	}else{
            		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"层"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                	String sc = ""+(Connection.remark.get(i).getDishScore());
                	if(sc.equals("0.0")){
                		sc = "无";
                	}
                	item.put("l3", "评分:"+sc);
                	item.put("l4", "赞 "+(Connection.remark.get(i).getZanCount()));
                	item.put("l5", "踩 "+(Connection.remark.get(i).getCaiCount()));
                	item.put("l6", Connection.remark.get(i).getTimeId());
                	items.add(item);
            	}
            }
            adapter = new SimpleAdapter(this,items,R.layout.simple_item,new String[]{"l1","l2","l3","l4","l5","l6"},new int[]{R.id.simple_textView1,R.id.simple_textView2,R.id.simple_textView3,R.id.simple_button1,R.id.simple_button2,R.id.simple_textView4});
            lv.setAdapter(adapter);
            //tbtn.setText("赞 "+a);
            //adapter.notifyDataSetChanged();
        }
    }
 	public void mybuttonlistener2(View v){
 		reid = "";
 		final ListView lv = (ListView)findViewById(R.id.remark_listView1);
 		SimpleAdapter adapter = (SimpleAdapter)(lv.getAdapter());
 		Button tbtn = (Button) v;
 		for (int i = 0; i < lv.getChildCount(); i++) {
 		     LinearLayout layout = (LinearLayout)lv.getChildAt(i);
 		     if(tbtn == (Button)layout.findViewById(R.id.simple_button2)){
 		    	 final TextView idText = (TextView)layout.findViewById(R.id.simple_textView4);
 		    	 reid = idText.getText().toString();
 		    	 break;
 		     }
 		}
 		new Thread(){
 			public void run(){
 				Connection.writer.write("cai\n");				//通信协议[client发server收]，表示客户端有人对某条评论点踩
 				Connection.writer.write(reid+"\n");
 				Connection.writer.write(Connection.user.getId()+"\n");
 				Connection.writer.flush();
 			}
 		}.start();
 		while(Connection.waiting13 == true);
        Connection.waiting13 = true;
        if(Connection.result.equals("succeed")){
     		String content = tbtn.getText().toString().trim();
     		Pattern p = Pattern.compile("[0-9][0-9]*");
            Matcher m = p.matcher(content);
            m.find();
            int a = (new Integer(m.group())).intValue();
            a++;
            ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
            for(int i=0;i<Connection.remark.size();i++){
            	Map<String,Object> item = new HashMap<String,Object>();
            	if(reid.equals(Connection.remark.get(i).getTimeId())){
            		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"层"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                	String sc = ""+(Connection.remark.get(i).getDishScore());
                	if(sc.equals("0.0")){
                		sc = "无";
                	}
                	item.put("l3", "评分:"+sc);
                	item.put("l4", "赞 "+(Connection.remark.get(i).getZanCount()));
                	Connection.remark.get(i).setCaiCount(a);
                	item.put("l5", "踩 "+a);
                	item.put("l6", Connection.remark.get(i).getTimeId());
                	items.add(item);
            	}else{
            		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"层"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                	String sc = ""+(Connection.remark.get(i).getDishScore());
                	if(sc.equals("0.0")){
                		sc = "无";
                	}
                	item.put("l3", "评分:"+sc);
                	item.put("l4", "赞 "+(Connection.remark.get(i).getZanCount()));
                	item.put("l5", "踩 "+(Connection.remark.get(i).getCaiCount()));
                	item.put("l6", Connection.remark.get(i).getTimeId());
                	items.add(item);
            	}
            }
            adapter = new SimpleAdapter(this,items,R.layout.simple_item,new String[]{"l1","l2","l3","l4","l5","l6"},new int[]{R.id.simple_textView1,R.id.simple_textView2,R.id.simple_textView3,R.id.simple_button1,R.id.simple_button2,R.id.simple_textView4});
            //tbtn.setText("赞 "+a);
            lv.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }else if(Connection.result.equals("repeat")){
     		String content = tbtn.getText().toString().trim();
     		Pattern p = Pattern.compile("[0-9][0-9]*");
            Matcher m = p.matcher(content);
            m.find();
            int a = (new Integer(m.group())).intValue();
            a--;
            ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
            for(int i=0;i<Connection.remark.size();i++){
            	Map<String,Object> item = new HashMap<String,Object>();
            	if(reid.equals(Connection.remark.get(i).getTimeId())){
            		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"层"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                	String sc = ""+(Connection.remark.get(i).getDishScore());
                	if(sc.equals("0.0")){
                		sc = "无";
                	}
                	item.put("l3", "评分:"+sc);
                	item.put("l4", "赞 "+(Connection.remark.get(i).getZanCount()));
                	Connection.remark.get(i).setCaiCount(a);
                	item.put("l5", "踩 "+a);
                	item.put("l6", Connection.remark.get(i).getTimeId());
                	items.add(item);
            	}else{
            		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"层"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                	String sc = ""+(Connection.remark.get(i).getDishScore());
                	if(sc.equals("0.0")){
                		sc = "无";
                	}
                	item.put("l3", "评分:"+sc);
                	item.put("l4", "赞 "+(Connection.remark.get(i).getZanCount()));
                	item.put("l5", "踩 "+(Connection.remark.get(i).getCaiCount()));
                	item.put("l6", Connection.remark.get(i).getTimeId());
                	items.add(item);
            	}
            }
            adapter = new SimpleAdapter(this,items,R.layout.simple_item,new String[]{"l1","l2","l3","l4","l5","l6"},new int[]{R.id.simple_textView1,R.id.simple_textView2,R.id.simple_textView3,R.id.simple_button1,R.id.simple_button2,R.id.simple_textView4});
            //tbtn.setText("赞 "+a);
            lv.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }
    }
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remark);
        new Thread(){
        	public void run(){
        		Connection.writer.write("remark\n");
        		Connection.writer.flush();
        	}
        }.start();
        final Button exitButton = (Button)findViewById(R.id.remark_button1);
        exitButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		shift();
        	}
        });
        final Button upButton = (Button)findViewById(R.id.simple_button1);
        final Button downButton = (Button)findViewById(R.id.simple_button2);
        while(Connection.waiting4 == true);
        Connection.waiting4 = true;
        ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
        final ListView list = (ListView)findViewById(R.id.remark_listView1);
        for(int i=0;i<Connection.remark.size();i++){
        	Map<String,Object> item = new HashMap<String,Object>();
        	item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"层"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
        	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
        	String sc = ""+(Connection.remark.get(i).getDishScore());
        	if(sc.equals("0.0")){
        		sc = "无";
        	}
        	item.put("l3", "评分:"+sc);
        	item.put("l4", "赞 "+(Connection.remark.get(i).getZanCount()));
        	item.put("l5", "踩 "+(Connection.remark.get(i).getCaiCount()));
        	item.put("l6", Connection.remark.get(i).getTimeId());
        	items.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,items,R.layout.simple_item,new String[]{"l1","l2","l3","l4","l5","l6"},new int[]{R.id.simple_textView1,R.id.simple_textView2,R.id.simple_textView3,R.id.simple_button1,R.id.simple_button2,R.id.simple_textView4});
        list.setAdapter(adapter);
	}
	//活动[activity]转移，至Pane activity[主界面]，见Pane.java
	private void shift(){
    	Intent intent = new Intent();   
        /* 指定intent要启动的类 */
        intent.setClass(Remark.this, Pane.class);
        /* 关闭当前的Activity */
        Remark.this.finish();
        /* 启动一个新的Activity */
        Remark.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
}
