import tools.Bag;

public class Mavery {

	public static void main(String[] args){
		
		XThread t = new XThread();
		t.start();
		
		Bag<Bag> bag = new Bag<Bag>();
	}
	
	public static void listen(String input){
		System.out.println("I've recieved '" + input +"'");
	}
}
