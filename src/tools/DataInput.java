package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class DataInput {

	public static Long getLong() throws IOException{
		String s = getString();
		Long value = Long.valueOf(s);
		return value;
	}
	
	public static char getChar() throws IOException{
		String s = getString();
		return s.charAt(0);
	}
	
	public static Integer getInt(){
		int p = 0;
		String s = "";
		try {
			s = getString();
			if(s.equals("")){p = p + 1;}
			for(int i = 0; i < s.length();i++){
				if(s.charAt(i)<48 | s.charAt(i)>57){
					p = p + 1;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(p!=0){return -1;}
		else{
		Integer value = Integer.valueOf(s);
		return value;}
		
	}
	
	public static String getString() throws IOException{
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String s = br.readLine();
		return s;
	}
	
}