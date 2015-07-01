package controller;

import calculator.Calculator;

public class Controller {
	private static double Ans = 0;
	private static String display = new String("");
	private static int record = 0;
	public static void addInput(int i){
		if(record==19){
			display = "";
		}
		record = i;
		switch(i){
		case 0:display+="0";	Calculator.setInput(display);	break;
		case 1:display+="1";	Calculator.setInput(display);	break;
		case 2:display+="2";	Calculator.setInput(display);	break;
		case 3:display+="3";	Calculator.setInput(display);	break;
		case 4:display+="4";	Calculator.setInput(display);	break;
		case 5:display+="5";	Calculator.setInput(display);	break;
		case 6:display+="6";	Calculator.setInput(display);	break;
		case 7:display+="7";	Calculator.setInput(display);	break;
		case 8:display+="8";	Calculator.setInput(display);	break;
		case 9:display+="9";	Calculator.setInput(display);	break;
		case 10:display+="."; 	Calculator.setInput(display);	break;
		case 11:display+="π"; 	Calculator.setInput(display);	break;
		case 12:if(display.length()==1){
			display = "";
		}else if(!display.equals("")){
			display = display.substring(0, display.length()-1);
		}
		Calculator.setInput(display);
		break;
		case 13:display = "";	Ans = 0; Calculator.setOutput(Ans+"");	Calculator.setInput(display);	break;
		case 14:display += "×";	Calculator.setInput(display);	break;
		case 15:display += "÷";	Calculator.setInput(display);	break;
		case 16:display += "+";	Calculator.setInput(display);	break;
		case 17:display += "-";	Calculator.setInput(display);	break;
		case 18:display += "R";	Calculator.setInput(display);	break;
		case 19:int temp = ALU.syntax(display);
		if(temp == 0){
			Ans = ALU.toRes();
			Calculator.setOutput(Ans+"");
		}else if(temp == 1){
			Calculator.setInput("Syntax ERROR");
		}else{
			Calculator.setInput("Math ERROR");
		}
		break;
		case 20:display += "^";	Calculator.setInput(display);	break;
		case 21:display += "√";	Calculator.setInput(display);	break;
		default:System.out.println("Error On Calculator(Controller)!");		System.exit(0);
		}
	}
}
class ALU{
	private final static int N = 100;							//假设最大输入表达式操作数个数为100
	private static enum State{INIT,Numa,Numb,Numc,Numd,Nume;}
	private static State current = State.INIT;
//	private static State last = State.INIT;
	private static int error = 0;
	private static double res = 0;
	private static double[] number = new double[N];
	private static char[] op = new char[N];
	public static double toRes(){
		return res;
	}
	public static int syntax(String s){			//语法鉴别函数
		int Numc_Counter = 1;					//记录状态Numc下小数点后的位数
		boolean Numd_state = false;
		current = State.INIT;
	//	last = State.INIT;
		error = 0;
		int nun = 0,opn = 0;
		label:
		for(int i=0;i<s.length();i++){
	//		last = current;
			switch(current){
			case INIT:{
				switch(s.charAt(i)){
				case '0':{
					current = State.INIT;
					number[nun] = 0;
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
					number[nun] = (new Double(s.charAt(i)+"")).doubleValue();
					break;
				}
				case '.':{
					current = State.Numb;
					number[nun] = 0;
					break;
				}
				case 'π':{
					current = State.Nume;
					number[nun] = Math.PI;
					nun++;
					break;
				}
				case 'R':{
					current = State.Nume;
					number[nun] = res;
					nun++;
					break;
				}
				case '+':
				case '-':
				case '×':
				case '÷':
				case '^':
				case '√':{
					Calculator.setInput("0"+s);
					current = State.Numd;
					op[opn] = s.charAt(i);
					opn++;
					number[nun] = 0;
					nun++;
					break;
				}
				default:System.out.println("Error On Calculator(ALU)!");
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
					number[nun] = number[nun]*10 + (new Double(s.charAt(i)+"")).doubleValue();
					break;
				}
				case '.':{
					current = State.Numb;
					break;
				}
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
					current = State.Numd;
					op[opn] = s.charAt(i);
					opn++;
					nun++;
					break;
				}
				default:System.out.println("Error On Calculator(ALU)!");
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
					number[nun] += (new Double(s.charAt(i)+"")).doubleValue()/10;
					break;
				}
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
				default:System.out.println("Error On Calculator(ALU)!");
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
					number[nun] += (new Double(s.charAt(i)+"")).doubleValue()/temp;
					Numc_Counter++;
					break;
				}
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
					op[opn] = s.charAt(i);
					opn++;
					nun++;
					break;
				}
				default:System.out.println("Error On Calculator(ALU)!");
				}
			}break;
			case Numd:{
				switch(s.charAt(i)){
				case '0':{
					Numd_state = true;
					current = State.Numd;
					number[nun] = 0;
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
					number[nun] = (new Double(s.charAt(i)+"")).doubleValue();
					break;
				}
				case '.':{
					current = State.Numb;
					number[nun] = 0;
					break;
				}
				case 'R':{
					current = State.Nume;
					number[nun] = res;
					nun++;
					break;
				}
				case 'π':{
					current = State.Nume;
					number[nun] = Math.PI;
					nun++;
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
						nun++;
					}else{
						error = 1;
					}
					break label;
				}
				default:System.out.println("Error On Calculator(ALU)!");
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
					op[opn] = s.charAt(i);
					opn++;
					current = State.Numd;
					break;
				}
				default:System.out.println("Error On Calculator(ALU)!");	System.exit(0);
				}
			}break;
			default:System.out.println("Error On Calculator(ALU)!");	System.exit(0);
			}
		}
		if(current == State.Numd && Numd_state == false){
			error = 1;
		}
		if(current != State.Nume || current != State.INIT){
			nun++;
		}
		if(error==0){
			if(calculate(number,op,nun,opn)){		//如果语法正确就计算结果
				error = 2;
			}
		}
		return error;
	}
	public static boolean calculate(double[] number,char[] op,int nun,int opn){	//计算表达式结果
//		for(int i=0;i<opn;i++){
//			System.out.print(number[i]+" "+op[i]+" ");
//		}
//		System.out.print(" "+number[opn]);
		for(int i=0;i<opn;i++){
			switch(op[i]){
			case '^':{
				number[i] = Math.pow(number[i], number[i+1]);
				if(Double.isNaN(number[i]) || Double.isInfinite(number[i])){
					return true;
				}
				for(int j=i;j<opn-1;j++){
					if(j<nun-2){
						number[j+1] = number[j+2];
					}
					op[j] = op[j+1];
				}
				--i;
				nun--;
				opn--;
			}break;
			case '√':{
				number[i] = Math.pow(number[i+1], 1.0/number[i]);
				if(Double.isNaN(number[i]) || Double.isInfinite(number[i])){
					return true;
				}
				for(int j=i;j<opn-1;j++){
					if(j<nun-2){
						number[j+1] = number[j+2];
					}
					op[j] = op[j+1];
				}
				--i;
				nun--;
				opn--;
			}break;
			default:break;
			}
		}
		for(int i=0;i<opn;i++){
			switch(op[i]){
			case '÷':{
				number[i] = number[i] / number[i+1];
				if(Double.isNaN(number[i]) || Double.isInfinite(number[i])){
					return true;
				}
				for(int j=i;j<opn-1;j++){
					if(j<nun-2){
						number[j+1] = number[j+2];
					}
					op[j] = op[j+1];
				}
				--i;
				nun--;
				opn--;
			}break;
			case '×':{
				number[i] = number[i] * number[i+1];
				if(Double.isNaN(number[i]) || Double.isInfinite(number[i])){
					return true;
				}
				for(int j=i;j<opn-1;j++){
					if(j<nun-2){
						number[j+1] = number[j+2];
					}
					op[j] = op[j+1];
				}
				--i;
				nun--;
				opn--;
			}break;
			default:break;
			}
		}
		for(int i=0;i<opn;i++){
			switch(op[i]){
			case '+':{
				number[i] = number[i] + number[i+1];
				if(Double.isNaN(number[i]) || Double.isInfinite(number[i])){
					return true;
				}
				for(int j=i;j<opn-1;j++){
					if(j<nun-2){
						number[j+1] = number[j+2];
					}
					op[j] = op[j+1];
				}
				--i;
				nun--;
				opn--;
			}break;
			case '-':{
				number[i] = number[i] - number[i+1];
				if(Double.isNaN(number[i]) || Double.isInfinite(number[i])){
					return true;
				}
				for(int j=i;j<opn-1;j++){
					if(j<nun-2){
						number[j+1] = number[j+2];
					}
					op[j] = op[j+1];
				}
				--i;
				nun--;
				opn--;
			}break;
			default:return true;
			}
		}
		res = number[0];
		return false;
	}
}
