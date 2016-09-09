package tools;

import java.util.ArrayList;
import java.util.Iterator;

public class Bag<T> implements Iterable{

	private ArrayList<T> array;
	
	public Bag(){
		array = new ArrayList<T>();
	}
	
	public void add(T item){
		array.add(item);
	}
	
	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
