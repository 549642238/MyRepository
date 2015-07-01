package com.example.porject;

import com.example.porject.MainActivity;
import java.util.*;

public class Controller {
	private static double Ans = 0;
	private static String display = new String("");
	public static void addInput(int i){
		switch(i){
		case 0:display+="0";	MainActivity.setInput(display);	break;
		case 1:display+="1";	MainActivity.setInput(display);	break;
		case 2:display+="2";	MainActivity.setInput(display);	break;
		case 3:display+="3";	MainActivity.setInput(display);	break;
		case 4:display+="4";	MainActivity.setInput(display);	break;
		case 5:display+="5";	MainActivity.setInput(display);	break;
		case 6:display+="6";	MainActivity.setInput(display);	break;
		case 7:display+="7";	MainActivity.setInput(display);	break;
		case 8:display+="8";	MainActivity.setInput(display);	break;
		case 9:display+="9";	MainActivity.setInput(display);	break;
		case 10:display+="."; 	MainActivity.setInput(display);	break;
		case 11:display+="π"; 	MainActivity.setInput(display);	break;
		case 12:if(display.length()==1){
			display = "";
		}else if(!display.equals("")){
			display = display.substring(0, display.length()-1);
		}
		MainActivity.setInput(display);
		break;
		case 13:display = "";	Ans = 0; MainActivity.setOutput(Ans+"");	MainActivity.setInput(display);	break;
		case 14:display += "×";	MainActivity.setInput(display);	break;
		case 15:display += "÷";	MainActivity.setInput(display);	break;
		case 16:display += "+";	MainActivity.setInput(display);	break;
		case 17:display += "-";	MainActivity.setInput(display);	break;
		case 18:display += "R";	MainActivity.setInput(display);	break;
		case 19:int temp = ALU.syntax(display);
		display = "";
		if(temp == 0){
			Ans = ALU.toRes();
			MainActivity.setOutput(Ans+"");
		}else if(temp == 1){
			MainActivity.setInput("Syntax ERROR");
		}else{
			MainActivity.setInput("Math ERROR");
		}
		break;
		case 20:display += "^";	MainActivity.setInput(display);	break;
		case 21:display += "√";	MainActivity.setInput(display);	break;
		case 22:display += "(";	MainActivity.setInput(display);	break;
		case 23:display += ")";	MainActivity.setInput(display);	break;
		default:System.out.println("Error On Calculator(Controller)!");		System.exit(0);
		}
	}
}
class ALU{
	private final static int N = 100;							//假设最大输入表达式操作数个数为100
	private static enum State{INIT,Numa,Numb,Numc,Numd,Nume;}
	private static State current = State.INIT;
	private static int error = 0;
	private static double res = 0;
	private static ArrayList<element> list = new ArrayList<element>();	//用来存放后缀表达式
	private static LinkedList oplist = new LinkedList();		//存放操作符的栈，栈顶优先级最高
	private static double number;								//记录当前识别的操作数
	private static char op;										//记录当前识别的操作符
	public static double toRes(){
		return res;
	}
	public static int syntax(String s){			//语法鉴别函数
		int Numc_Counter = 1;					//记录状态Numc下小数点后的位数
		boolean Numd_state = false;				//初始化
		current = State.INIT;
		error = 0;
		list.clear();
		oplist.clear();
		if(s.equals("")){
			error = 1;
			return 1;
		}
		label:
		for(int i=0;i<s.length();i++){
			switch(current){
			case INIT:{
				switch(s.charAt(i)){
				case '0':{
					current = State.INIT;
					number = 0;
					break;
				}
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':{
					current = State.Numa;
					number = (new Double(s.charAt(i)+"")).doubleValue();
					break;
				}
				case '.':{
					current = State.Numb;
					number = 0;
					break;
				}
				case 'π':{
					current = State.Nume;
					number = Math.PI;
					list.add(new element(number,'#'));
					break;
				}
				case 'R':{
					current = State.Nume;
					number = res;
					list.add(new element(number,'#'));
					break;
				}
				case '(':{
					current = State.Numd;
					oplistAdd(s.charAt(i));
					break;
				}
				case ')':{
					error = 1;
					break label;
				}
				case '+':
				case '-':
				case '×':
				case '÷':
				case '^':
				case '√':{
					MainActivity.setInput("R"+s);
					current = State.Numd;
					op = s.charAt(i);
					number = res;
					list.add(new element(number,'#'));
					oplistAdd(op);
					break;
				}
				default:System.out.println("Error On Calculator(ALU[语法分析器])!");
				}
			}break;
			case Numa:{
				switch(s.charAt(i)){
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':{
					current = State.Numa;
					number = number*10 + (new Double(s.charAt(i)+"")).doubleValue();
					break;
				}
				case '.':{
					current = State.Numb;
					break;
				}
				case '(':
				case 'R':
				case 'π':{
					error = 1;
					break label;
				}
				case ')':{
					current = State.Nume;
					list.add(new element(number,'#'));
					oplistAdd(s.charAt(i));
					break;
				}
				case '+':
				case '-':
				case '×':
				case '÷':
				case '^':
				case '√':{
					current = State.Numd;
					op = s.charAt(i);
					list.add(new element(number,'#'));
					oplistAdd(op);
					break;
				}
				default:System.out.println("Error On Calculator(ALU[语法分析器])!");
				}
			}break;
			case Numb:{
				switch(s.charAt(i)){
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':{
					current = State.Numc;
					number += (new Double(s.charAt(i)+"")).doubleValue()/10;
					break;
				}
				case '(':
				case ')':
				case '+':
				case '-':
				case '×':
				case '÷':
				case '^':
				case '√':
				case '.':
				case 'R':
				case 'π':{
					error = 1;
					break label;
				}
				default:System.out.println("Error On Calculator(ALU[语法分析器])!");
				}
			}break;
			case Numc:{
				int temp = 10;
				switch(s.charAt(i)){
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':{
					current = State.Numc;
					for(int k=0;k<Numc_Counter;k++){
						temp *= 10;
					}
					number += (new Double(s.charAt(i)+"")).doubleValue()/temp;
					Numc_Counter++;
					break;
				}
				case '(':
				case ')':
				case '.':
				case 'R':
				case 'π':{
					error = 1;
					break label;
				}
				case '+':
				case '-':
				case '×':
				case '÷':
				case '^':
				case '√':{
					Numc_Counter = 1;
					current = State.Numd;
					list.add(new element(number,'#'));
					op = s.charAt(i);
					oplistAdd(op);
					break;
				}
				default:System.out.println("Error On Calculator(ALU[语法分析器])!");
				}
			}break;
			case Numd:{
				switch(s.charAt(i)){
				case '0':{
					Numd_state = true;
					current = State.Numd;
					number = 0;
					break;
				}
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':{
					current = State.Numa;
					number = (new Double(s.charAt(i)+"")).doubleValue();
					break;
				}
				case '.':{
					current = State.Numb;
					number = 0;
					break;
				}
				case 'R':{
					current = State.Nume;
					number= res;
					list.add(new element(number,'#'));
					break;
				}
				case 'π':{
					current = State.Nume;
					number = Math.PI;
					list.add(new element(number,'#'));
					break;
				}
				case ')':{
					error = 1;
					break label;
				}
				case '(':{
					if(Numd_state==true){
						error = 1;
					}else{
						current = State.Numd;
						oplistAdd(s.charAt(i));
					}
					break;
				}
				case '+':
				case '-':
				case '×':
				case '÷':
				case '^':
				case '√':{
					if(Numd_state==true){
						current = State.Numd;
						list.add(new element(number,'#'));
						oplistAdd(s.charAt(i));
					}else{
						error = 1;
					}
					break label;
				}
				default:System.out.println("Error On Calculator(ALU[语法分析器])!");
				}
			}break;
			case Nume:{
				switch(s.charAt(i)){
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case '.':
				case 'R':
				case 'π':
				case '(':{
					error = 1;
					break label;
				}
				case ')':{
					current = State.Nume;
					oplistAdd(s.charAt(i));
					break;
				}
				case '+':
				case '-':
				case '×':
				case '÷':
				case '^':
				case '√':{
					op = s.charAt(i);
					oplistAdd(op);
					current = State.Numd;
					break;
				}
				default:System.out.println("Error On Calculator(ALU[语法分析器])!");	System.exit(0);
				}
			}break;
			default:System.out.println("Error On Calculator(ALU[语法分析器])!");	System.exit(0);
			}
		}
		if(current == State.Numd && Numd_state == false/* || !match(oplist)*/){		//表达式最后不能以操作符结尾，括号要匹配
			error = 1;
		}
		if(current == State.Numa || current == State.Numb || current == State.Numc || current == State.Numd || current == State.INIT){				
			list.add(new element(number,'#'));
		}
		while(true){
			if(oplist.isEmpty()){
				break;
			}else{
				char c = '0';
				if((c= ((Character)oplist.removeFirst()).charValue()) != '('){
					list.add(new element(0,c));
				}
			}
		}
		if(error==0){
	/*		for(int i=0;i<list.size();i++){
				if(((element)(list.get(i))).toC() != '#'){
					System.out.print(" "+((element)(list.get(i))).toC());
				}else{
					System.out.print(((element)(list.get(i))).toD()+" ");
				}
			}*/
			if(calculate(list)){		//如果语法正确就计算结果
				error = 2;
			}
		}
		return error;
	}
	public static void oplistAdd(char c){
		switch(c){
		case '(':	oplist.addFirst(c);	break;
		case ')':{
			while(true){
				if(!oplist.isEmpty()){
					char temp = ((Character)oplist.removeFirst()).charValue();
					if(temp == '('){
						break;
					}
					list.add(new element(0,temp));
				}
			}
			break;
		}
		case '^':
		case '√':{
			while(true){
				if(oplist.isEmpty() || ((Character)(oplist.getFirst())).charValue()=='(' || 
				((Character)(oplist.getFirst())).charValue()=='+' || ((Character)(oplist.getFirst())).charValue()=='-' || 
				((Character)(oplist.getFirst())).charValue()=='×' || ((Character)(oplist.getFirst())).charValue()=='÷'){
					oplist.addFirst(c);
					break;
				}else{
					list.add(new element(0,((Character)oplist.removeFirst()).charValue()));
				}
			}
			break;
		}
		case '×':
		case '÷':{
			while(true){
				if(oplist.isEmpty() || ((Character)(oplist.getFirst())).charValue()=='(' || 
				((Character)(oplist.getFirst())).charValue()=='+' || ((Character)(oplist.getFirst())).charValue()=='-' ){
					oplist.addFirst(c);
					break;
				}else{
					list.add(new element(0,((Character)oplist.removeFirst()).charValue()));
				}
			}
			break;
		}
		case '+':
		case '-':{
			while(true){
				if(oplist.isEmpty() || ((Character)(oplist.getFirst())).charValue()=='('){
					oplist.addFirst(c);
					break;
				}else{
					list.add(new element(0,((Character)oplist.removeFirst()).charValue()));
				}
			}
			break;
		}
		default:System.out.println("Error On Calculator(ALU[语法分析器])!"); System.exit(0);
		}
	}
	public static boolean match(LinkedList l){
		int left = 0, right = 0;
		for(int i=0;i<l.size();i++){
			if(((Character)(l.get(i))).charValue() == '('){
				left++;
			}else if(((Character)(l.get(i))).charValue() == ')'){
				right++;
			}
		}
		return left == right;
	}
	public static boolean calculate(ArrayList l){	//计算表达式结果
		LinkedList stack = new LinkedList();
		for(int i=0;i<l.size();i++){
			if(((element)l.get(i)).toC() == '#'){
				stack.addFirst(new element(((element)l.get(i)).toD(),'#'));
			}else{
				double a = ((element)(stack.removeFirst())).toD();
				double b = ((element)(stack.removeFirst())).toD();
				switch(((element)l.get(i)).toC()){
				case '^':{
					double temp = Math.pow(b,a);
					if(Double.isInfinite(temp) || Double.isNaN(temp)){
						return true;
					}else{
						stack.addFirst(new element(temp,'#'));
					}
					break;
				}
				case '√':{
					double temp = Math.pow(a,1.0/b);
					if(Double.isInfinite(temp) || Double.isNaN(temp)){
						return true;
					}else{
						stack.addFirst(new element(temp,'#'));
					}
					break;
				}
				case '×':{
					double temp = b * a;
					if(Double.isInfinite(temp) || Double.isNaN(temp)){
						return true;
					}else{
						stack.addFirst(new element(temp,'#'));
					}
					break;
				}
				case '÷':{
					double temp = b/a;
					if(Double.isInfinite(temp) || Double.isNaN(temp)){
						return true;
					}else{
						stack.addFirst(new element(temp,'#'));
					}
					break;
				}
				case '+':{
					double temp = b + a;
					if(Double.isInfinite(temp) || Double.isNaN(temp)){
						return true;
					}else{
						stack.addFirst(new element(temp,'#'));
					}
					break;
				}
				case '-':{
					double temp = b - a;
					if(Double.isInfinite(temp) || Double.isNaN(temp)){
						return true;
					}else{
						stack.addFirst(new element(temp,'#'));
					}
					break;
				}
				}
			}
		}
		res = ((element)stack.getFirst()).toD();
		return false;
	}
}
class element{
	private double d = 0;
	private char c = '#';
	public char toC(){
		return c;
	}
	public double toD(){
		return d;
	}
	public element(double dd,char cc){
		d = dd;
		c = cc;
	}
}
