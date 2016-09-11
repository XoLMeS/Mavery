

import java.io.IOException;

import tools.DataInput;

public class XThread extends Thread{
	public String input;
	
	public void run(){
		
	
		//System.out.println("Thread is live");
		
		while(true){
			try {
				input = DataInput.getString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(input.equals("exit")){
				this.stop();
			}
			else {
				Mavery.listen(input);
				
			}
		}
	}
}
