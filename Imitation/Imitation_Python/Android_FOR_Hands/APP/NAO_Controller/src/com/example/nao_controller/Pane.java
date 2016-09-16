package com.example.nao_controller;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.nao_controller.Connection;
import com.example.nao_controller.R;

public class Pane extends Activity{
	public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
        	new AlertDialog.Builder(this).setTitle("Exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        		   public void onClick(DialogInterface dialog, int which){
        			   new Thread(){
        	        		public void run(){		
        	        			try {
        							System.exit(0);
        						} catch (Exception e) {
        							e.printStackTrace();
        						}
        	        		}
        	        	}.start();
        		   }
        	}).setNegativeButton("Cancle",null).show();
        }
        return true;
    }
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pane);
        final Button lo = (Button)findViewById(R.id.pane_button1);
    	final Button lc = (Button)findViewById(R.id.pane_button2);
    	final Button ro = (Button)findViewById(R.id.pane_button3);
    	final Button rc = (Button)findViewById(R.id.pane_button4);
    	lo.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		new Thread(){
                	public void run(){
                		Connection.writer.write("1,1.0,0\n");
                		Connection.writer.flush();
                	}
                }.start();
        	}
    	});
    	lc.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		new Thread(){
                	public void run(){
                		Connection.writer.write("1,0.3,0\n");
                		Connection.writer.flush();
                	}
                }.start();
        	}
    	});
    	ro.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		new Thread(){
                	public void run(){
                		Connection.writer.write("2,1.0,0\n");
                		Connection.writer.flush();
                	}
                }.start();
        	}
    	});
    	rc.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		new Thread(){
                	public void run(){
                		Connection.writer.write("2,0.3,0\n");
                		Connection.writer.flush();
                	}
                }.start();
        	}
    	});
	}
}
