//User����鼮�ļ�¼�����ݽṹ
package record;

import java.io.Serializable;

public class Record implements Serializable{
	public String name = null;
	public String ISBN = null;
	public String date = null;
	public int state = 0;
	public Record(String Name,String Isbn,String d,int s){
		name = Name;
		ISBN = Isbn;
		date = d;
		state = s;
	}
}