package com.example.xidian_dining;
/*
 * ע�����˳��������¼���棬�ɹ����ֱ�ӽ���������
 */
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;

public class Register2 extends Activity{
	private String regEmail = "";
	private String regPhone = "";
	private String regBirth = "";
	private final static int DATE_DIALOG = 0;
	private Button dateButton;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register2);
        final Button nextButton = (Button)findViewById(R.id.register_button3);
        final Button exitButton = (Button)findViewById(R.id.register_button4);
        final EditText emailText = (EditText)findViewById(R.id.register_editText9);
        final EditText phoneText = (EditText)findViewById(R.id.register_editText10);
        final TextView resText = (TextView)findViewById(R.id.register_textView13);
        dateButton = (Button)findViewById(R.id.register_date_button);
        dateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });
        exitButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		resText.setText("��ת...");
        		shift();
        	}
        });
        nextButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		Connection.email = emailText.getText().toString().trim();
        		Connection.phone = phoneText.getText().toString().trim();
        		Connection.birth = dateButton.getText().toString().trim();
        		if(regBirth.equals("Date")){
        			regBirth = "";
        		}
        		new Thread(){
        			public void run(){
        				Connection.writer.write("register\n");				//ͨ��Э��[client��server��]������ע����Ϣ�����������������������ݿ����
        				Connection.writer.write(Connection.number+"\n");
        				Connection.writer.write(Connection.passwd+"\n");
        				Connection.writer.write(Connection.name+"\n");
        				Connection.writer.write(Connection.grade+"\n");
        				Connection.writer.write(Connection.sex+"\n");
        				Connection.writer.write(Connection.email+"\n");
        				Connection.writer.write(Connection.phone+"\n");
        				Connection.writer.write(Connection.birth+"\n");
        				Connection.writer.flush();
        			}
        		}.start();
        		resText.setText("����ע��...");
        	/*	try{
        			Thread.sleep(1000);
        		}catch(Exception e){
        			System.out.println("Register2 error2 -> register failed");
        			e.printStackTrace();
        		}*/
        		boolean find = true;
    			int time=0;
    			while(Connection.waiting3 == true){
    				try{
    					Thread.sleep(100);
    					time++;
    					if(time>50){
    						find = false;
            				resText.setText("����������ӳ�ʱ�����Գ�����ת������������µ�¼");
    						break;
    					}
    				}catch(Exception e){
    					System.out.println("Pane Message recv timeout error");
    					e.printStackTrace();
    				}
    			}
    			Connection.waiting3 = true;
    			if(find){
    				if(Connection.result.equals("OK")){
            			resText.setText("ע��ɹ�");
            			shift2();
            		}else if(Connection.result.equals("alreadyused")){
            			resText.setText("�˺��Ѵ���");
            		}else{
            			resText.setText("����������ʧ�ܣ�������");
            		}
    			}
        		Connection.number = "";
        		Connection.passwd = "";
        		Connection. name = "";
        		Connection.sex = "";
        		Connection.email = "";
        		Connection.grade = "";
        		Connection.phone = "";
        		Connection.birth = "";
        		Connection.registertime = "";
        	}
        });
	}
	protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        Calendar c = Calendar.getInstance();
            dialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                    	String mmm = month+1+"";
                    	String dd = dayOfMonth+"";
                    	if(month+1<10){
                    		int mm = month+1;
                    		mmm = "0"+mm;
                    	}
                    	if(dayOfMonth<10){
                    		dd = "0"+dayOfMonth; 
                    	}
                        dateButton.setText(""+year+mmm+dd);
                    }
                }, 
                c.get(Calendar.YEAR), // �������
                c.get(Calendar.MONTH), // �����·�
                c.get(Calendar.DAY_OF_MONTH) // ��������
            );
        return dialog;
    }
	//�[activity]ת�ƣ��� MainActivity activity[��¼����]����MainActivity.java
	private void shift(){
		Connection.number = "";
		Connection.passwd = "";
		Connection.name = "";
		Connection.sex = "";
		Connection.email = "";
		Connection.grade = "";
		Connection.phone = "";
		Connection.birth = "";
		Connection.registertime = "";
    	Intent intent = new Intent();   
        /* ָ��intentҪ�������� */
        intent.setClass(Register2.this, MainActivity.class);
        /* �رյ�ǰ��Activity */
        Register2.this.finish();
        /* ����һ���µ�Activity */
        Register2.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
	//�[activity]ת�ƣ��� Pane activity[������]����Pane.java
	private void shift2(){
		try{
			Connection.writer.write("login\n");
			Connection.writer.write(Connection.number+"\n");
			Connection.writer.write(Connection.passwd+"\n");
			Connection.writer.flush();
		}catch(Exception e){
			System.out.println("Register2 Error -> shift2 error");
			e.printStackTrace();
		}
		Connection.number = "";
		Connection.passwd = "";
		Connection.name = "";
		Connection.sex = "";
		Connection.email = "";
		Connection.grade = "";
		Connection.phone = "";
		Connection.birth = "";
		Connection.registertime = "";
    	Intent intent = new Intent();   
        /* ָ��intentҪ�������� */
        intent.setClass(Register2.this, Pane.class);
        /* �رյ�ǰ��Activity */
        Register2.this.finish();
        /* ����һ���µ�Activity */
        Register2.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
}
