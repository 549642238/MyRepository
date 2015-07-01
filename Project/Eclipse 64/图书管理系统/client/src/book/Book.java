//书的数据结构
package book;

import java.io.Serializable;

public class Book implements Serializable{
	public String Bname = null;
	public String ISBN = null;
	public String Bclass = null;
	public String Bwriter = null;
	public String Bpublisher = null;
	public int Bquantity = 0;
	public Book(String Name,String Isbn,String Class,String Writer,String Publisher,int Quantity){
		Bname = Name;
		ISBN = Isbn;
		Bclass = Class;
		Bwriter = Writer;
		Bpublisher = Publisher;
		Bquantity = Quantity;
	}
}
