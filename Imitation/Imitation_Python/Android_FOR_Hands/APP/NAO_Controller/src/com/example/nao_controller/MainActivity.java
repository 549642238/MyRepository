package com.example.nao_controller;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;

import java.io.*;
import java.net.Socket;

import com.example.nao_controller.MainActivity;
import com.example.nao_controller.Pane;
import com.example.nao_controller.R;

@SuppressLint("ValidFragment") public class MainActivity extends ActionBarActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
        	//跳转至fragment_main.xml，跳过activity_main.xml
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(this))
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    //活动[activity]转移，至Pane activity[主界面]，见Pane.java
    private void shift(){
    	Intent intent = new Intent();   
        /* 指定intent要启动的类 */
        intent.setClass(MainActivity.this, Pane.class);
        /* 关闭当前的Activity */
        MainActivity.this.finish();
        /* 启动一个新的Activity */
        MainActivity.this.startService(intent);
        startActivity(intent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
    	private MainActivity ma = null;
    	private Handler handler;
        public PlaceholderFragment(MainActivity m) {
        	ma = m;
        }
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final Button connect = (Button)rootView.findViewById(R.id.fragment_button1);
            final EditText ip = (EditText)rootView.findViewById(R.id.fragment_editText1);
            final TextView result = (TextView)rootView.findViewById(R.id.fragment_textView1);
            handler = new Handler(){
            	@Override
            	public void handleMessage(Message msg){
            		if(msg.what == 0x001){
            			result.setText("Cannot connect to "+ip.getText().toString().trim()+",make sure the ip is right");
            		}else if(msg.what == 0x002){
            			result.setText("Connection success");
            			ma.shift();
            		}
            	}
            };
            connect.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		final String s = ip.getText().toString().trim();
            		new Thread(){
        				public void run(){
        					Connection.connect(s);
        				}
        			}.start();
            		int time = 0;
            		while(Connection.waiting){
            			try{
        					Thread.sleep(500);
        					time++;
        					if(time>=10){
        						Message msg = new Message();
                				msg.what = 0x001;
                				msg.obj = "N";
                				handler.sendMessage(msg);
                				System.out.println("----------Time Out"+s+"==============================================");
        						break;
        					}
        				}catch(Exception e){
        					e.printStackTrace();
        				}
            		}
            		if (Connection.link){
            			Message msg = new Message();
        				msg.what = 0x002;
        				msg.obj = "Y";
        				handler.sendMessage(msg);
            		}else{
            			Message msg = new Message();
        				msg.what = 0x001;
        				msg.obj = "N";
        				handler.sendMessage(msg);
            		}
            	}
            });
            return rootView;
        }
        
    }

}
