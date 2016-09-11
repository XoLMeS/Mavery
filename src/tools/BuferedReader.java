package tools;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BuferedReader {
	
	private FileReader fr;
	private BufferedReader br;
	
	private boolean threadOpened = false;
	private String filename;
	
	public BuferedReader(String filename) throws IOException {
		this.filename = filename;
		openThread(filename);
	}

	public String getNextString() throws IOException {
		if (!threadOpened) {
			openThread(filename);
			threadOpened = !threadOpened;
		}
		String s;
		s = br.readLine();
		if (s != null) {
			return s;
		} else
			return null;
	}

	private void openThread(String filename) throws FileNotFoundException {
		fr = new FileReader(filename);
		br = new BufferedReader(fr);
	}
	
	public void closeThread() throws IOException{
		fr.close();
		br.close();
	}
	
	public boolean threadIsOpened(){
		return threadOpened;
	}


}
	
