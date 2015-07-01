package com.example.porject;

import java.util.ArrayList;

import com.example.mycalculator.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	private static PlaceholderFragment pf = new PlaceholderFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, pf)
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
    public static void setInput(String s){
    	pf.setT1(s);
    }
    public static void setOutput(String s){
    	pf.setT2(s);
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
    	private boolean condition = true;
    	private boolean shine = true;
    	private Button b0 = null;			//1
    	private Button b1 = null;			//0
    	private Button b2 = null;			//2
    	private Button b3 = null;			//3
    	private Button b4 = null;			//4
    	private Button b5 = null;			//5
    	private Button b6 = null;			//6
    	private Button b7 = null;			//7
    	private Button b8 = null;			//8
    	private Button b9 = null;			//9
    	private Button b10 = null;			//.
    	private Button b11 = null;			//¦Ð
    	private Button b12 = null;			//DEL
    	private Button b13 = null;			//AC
    	private Button b14 = null;			//¡Á
    	private Button b15 = null;			//¡Â
    	private Button b16 = null;			//+
    	private Button b17 = null;			//-
    	private Button b18 = null;			//Ans
    	private Button b19 = null;			//=
    	private Button b20 = null;			//Pow
    	private Button b21 = null;			//Sqrt
    	private Button b22 = null;			//(
    	private Button b23 = null;			//)
    	private Button b24 = null;			//Exit
    	private Button b25 = null;			//Shine
    	private TextView t1 = null;			//Input
    	private TextView t2 = null;			//Output
    	private ArrayList<Button> la = new ArrayList<Button>();
        private ArrayList<Button> lb = new ArrayList<Button>();
        private MyThread th = null;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.calui, container, false);
            b0 = (Button)rootView.findViewById(R.id.button1);
            b1 = (Button)rootView.findViewById(R.id.button2);
            b2 = (Button)rootView.findViewById(R.id.button3);
            b3 = (Button)rootView.findViewById(R.id.button4);
            b4 = (Button)rootView.findViewById(R.id.button5);
            b5 = (Button)rootView.findViewById(R.id.button6);
            b6 = (Button)rootView.findViewById(R.id.button7);
            b7 = (Button)rootView.findViewById(R.id.button8);
            b8 = (Button)rootView.findViewById(R.id.button9);
            b9 = (Button)rootView.findViewById(R.id.button10);
            b10 = (Button)rootView.findViewById(R.id.button11);
            b11 = (Button)rootView.findViewById(R.id.button12);
            b12 = (Button)rootView.findViewById(R.id.button13);
            b13 = (Button)rootView.findViewById(R.id.button14);
            b14 = (Button)rootView.findViewById(R.id.button15);
            b15 = (Button)rootView.findViewById(R.id.button16);
            b16 = (Button)rootView.findViewById(R.id.button17);
            b17 = (Button)rootView.findViewById(R.id.button18);
            b18 = (Button)rootView.findViewById(R.id.button19);
            b19 = (Button)rootView.findViewById(R.id.button20);
            b20 = (Button)rootView.findViewById(R.id.button21);
            b21 = (Button)rootView.findViewById(R.id.button22);
            b22 = (Button)rootView.findViewById(R.id.button23);
            b23 = (Button)rootView.findViewById(R.id.button24);
            b24 = (Button)rootView.findViewById(R.id.button25);
            b25 = (Button)rootView.findViewById(R.id.button26);
            t1 = (TextView)rootView.findViewById(R.id.textview1);
            t2 = (TextView)rootView.findViewById(R.id.textview2);
            la.add(b1);
            la.add(b2);
            la.add(b11);
            la.add(b5);
            la.add(b7);
            la.add(b9);
            lb.add(b0);
            lb.add(b10);
            lb.add(b3);
            lb.add(b4);
            lb.add(b6);
            lb.add(b8);
            container.setBackgroundColor(Color.GREEN);
            b0.setTextColor(Color.RED);
            b0.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(0);
            	}
            });
            b1.setTextColor(Color.RED);
            b1.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(1);
            	}
            });
            b2.setTextColor(Color.RED);
            b2.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(2);
            	}
            });
            b3.setTextColor(Color.RED);
            b3.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(3);
            	}
            });
            b4.setTextColor(Color.RED);
            b4.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(4);
            	}
            });
            b5.setTextColor(Color.RED);
            b5.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(5);
            	}
            });
            b6.setTextColor(Color.RED);
            b6.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(6);
            	}
            });
            b7.setTextColor(Color.RED);
            b7.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(7);
            	}
            });
            b8.setTextColor(Color.RED);
            b8.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(8);
            	}
            });
            b9.setTextColor(Color.RED);
            b9.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(9);
            	}
            });
            b10.setTextColor(Color.RED);
            b10.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(10);
            	}
            });
            b11.setTextColor(Color.RED);
            b11.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(11);
            	}
            });
            b12.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(12);
            	}
            });
            b13.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(13);
            	}
            });
            b14.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(14);
            	}
            });
            b15.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(15);
            	}
            });
            b16.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(16);
            	}
            });
            b17.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(17);
            	}
            });
            b18.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(18);
            	}
            });
            b19.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(19);
            	}
            });
            b20.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(20);
            	}
            });
            b21.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(21);
            	}
            });
            b22.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(22);
            	}
            });
            b23.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(23);
            	}
            });
            b24.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(24);
            	}
            });
            b24.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		System.exit(0);
            	}
            });
            b25.setTextColor(Color.BLUE);
            b25.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		Controller.addInput(25);
            	}
            });
            b25.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		if(shine){
            			shine = false;
            			b25.setTextColor(Color.RED);
            			stopShine();
            		}else{
            			shine = true;
            			b25.setTextColor(Color.BLUE);
            			shine();
            		}
            	}
            });
            t1.setBackgroundColor(Color.CYAN);
            t1.setTextColor(Color.BLUE);
            t1.setText("\t    CASIO Made in XiDian");
            t2.setTextColor(Color.RED);
            t2.setBackgroundColor(Color.YELLOW);
            t2.setText("Output");
            th = new MyThread();
            return rootView;
        }
        public void setT1(String s){
        	t1.setText(s);
        }
        public void setT2(String s){
        	t2.setText(s);
        }
        private void shine(){
        	condition = true;
        	th = new MyThread();
        }
        private void stopShine(){
        	condition = false;
        	th.interrupt();
        	for(int i=0;i<la.size();i++){
        		((Button)la.get(i)).setTextColor(Color.RED);
        	}
        	for(int i=0;i<lb.size();i++){
        		((Button)lb.get(i)).setTextColor(Color.RED);
        	}
        }
        private class LongTimeTask extends AsyncTask<String, Void , String>{
            @Override  
            protected String doInBackground(String... params){  
                try{  
                    Thread.sleep(1000);  
                }  
                catch (InterruptedException e)  
                {  
                 //   e.printStackTrace();  
                }  
                return params[0];  
            }  
      
            @Override  
            protected void onPostExecute(String result){
            	for(int i=0;i<la.size();i++){
            		if(i==0 || i==1 || i==2){
            			((Button)la.get(i)).setTextColor(Color.YELLOW);
            		}else{
            			((Button)la.get(i)).setTextColor(Color.CYAN);
            		}
            	}
            }  
              
        }
        private class LongTimeTask2 extends AsyncTask<String, Void , String>{
            @Override  
            protected String doInBackground(String... params){  
                try{  
                    Thread.sleep(1500);  
                }  
                catch (InterruptedException e)  
                {  
                   // e.printStackTrace();  
                }  
                return params[0];  
            }  
      
            @Override  
            protected void onPostExecute(String result){
            	for(int i=0;i<lb.size();i++){
            			((Button)lb.get(i)).setTextColor(Color.MAGENTA);
            	}
            }  
              
        }
        private class LongTimeTask3 extends AsyncTask<String, Void , String>{
            @Override  
            protected String doInBackground(String... params){  
                try{  
                    Thread.sleep(1000);  
                }  
                catch (InterruptedException e)  
                {  
                 //   e.printStackTrace();  
                }  
                return params[0];  
            }  
      
            @Override  
            protected void onPostExecute(String result){
            	for(int i=0;i<la.size();i++){
            			((Button)la.get(i)).setTextColor(Color.LTGRAY);
            	}
            }  
              
        }
        private class LongTimeTask4 extends AsyncTask<String, Void , String>{
            @Override  
            protected String doInBackground(String... params){  
                try{  
                    Thread.sleep(1000);  
                }  
                catch (InterruptedException e)  
                {  
                //    e.printStackTrace();  
                }  
                return params[0];  
            }  
      
            @Override  
            protected void onPostExecute(String result){
            	for(int i=0;i<lb.size();i++){
            			((Button)lb.get(i)).setTextColor(Color.LTGRAY);
            	}
            }  
              
        }
        private class MyThread extends Thread{
        	public MyThread(){
        		start();
        	}
        	public void run(){
        		while(condition){
        			try{
        				new LongTimeTask().execute("Light1");
        				new LongTimeTask2().execute("Light2");
        				new LongTimeTask3().execute("Off1");
        				new LongTimeTask4().execute("Off2");
        				Thread.sleep(8000);
        			}catch(Exception e){
        			}
        		}
        	}
        }
    }

}
