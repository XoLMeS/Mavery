import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.print.DocFlavor.INPUT_STREAM;

import tools.*;

public class Mavery {

	static Bag<Bag> brain;
	static ArrayList<String> words;
	static ArrayList<Word> spec_words;
	static ArrayList<String> new_words;

	static ArrayList<String> recieved;
	static ArrayList<String> answered;

	
	static Thread timer;
	public static long f;
	private static long pause = 60;
	
	private static String interlocutor_name = "Stranger";

	public static void main(String[] args) throws IOException {

		
		timer = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				f = System.currentTimeMillis()/1000;
				while(true){
					if(System.currentTimeMillis()/1000-f>pause){
						Mavery.ask();
						f = System.currentTimeMillis()/1000;
					}
				}
			}
		});
		
		
		brain = new Bag<Bag>("brain");
		words = new ArrayList<String>();
		spec_words = new ArrayList<Word>();

		recieved = new ArrayList<String>();
		answered = new ArrayList<String>();

		System.out.println("Loading words...");
		loadSpecWords();
		BuferedReader reader = null;

		try {
			reader = new BuferedReader("brain.txt");
			String read = null;
			do {
				read = reader.getNextString();
				if (read != null) {
					words.add(read);
					brain.add(new Bag<Bag>(read));
					for (Bag b : brain) {
						if (b.title.equals(read)) {
							b.add(new Bag<Point>("connections"));
							break;
						}
					}
				} else {
					break;
				}
			} while (true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		reader.closeThread();

		System.out.println("Done");
		System.out.println("Building the brain...");
		try {
			reader = new BuferedReader("brain_connections.txt");
			String read = null;
			do {
				read = reader.getNextString();
				if (read != null) {

					if (read.startsWith("/")) {
						read = read.substring(1);
						String[] s = read.split(" ");
						addConnection(words.get(Integer.valueOf(s[0])),
								Integer.valueOf(s[1]), Integer.valueOf(s[2]));
					}

				} else {
					break;
				}
			} while (true);
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader.closeThread();
		System.out.println("Done");

		XThread t = new XThread();
		t.start();
		System.out.println("Mavery is alive");
		System.out
				.println("******************************************************");
		timer.run();
	}

	public static void listen(String input) {
		f = System.currentTimeMillis()/1000;
		// say("I've recieved '" + input +"'");
		Random rand = new Random();
		String str = input.toLowerCase();
		recieved.add(str);
		String answer = "";
		boolean found = false;
		// GREETING
		if (interlocutor_name.equals("Stranger")
				&& (str.contains("i am ") || str.contains("i`m "))) {
			int name_begins = 0;
			if (str.contains("i am")) {
				name_begins = str.indexOf("am ") + 3;
			} else {
				name_begins = str.indexOf("i`m ") + 4;
			}
			int name_ends = name_begins;
			while (str.charAt(name_ends) != 46 || str.charAt(name_ends) != 32) {
				name_ends++;
				if (name_ends == str.length()) {
					break;
				}
			}
			interlocutor_name = str.substring(name_begins, name_ends);

			for (Bag b : brain) {
				if (b.title.equals(interlocutor_name)) {
					say("Oh, " + interlocutor_name + ". How are you?");
					found = true;
					break;
				}
			}
			if (!found) {
				brain.add(new Bag<Bag>(interlocutor_name));
				for (Bag b : brain) {
					if (b.title.equals(interlocutor_name)) {
						b.add(new Bag<Point>("connections"));
						break;
					}
				}
				words.add(interlocutor_name);
				addConnection("i", 13, words.indexOf(interlocutor_name));
				addConnection(interlocutor_name, 11, words.indexOf(interlocutor_name));
				addConnection(interlocutor_name, 11, 13);
				addConnection(interlocutor_name, words.indexOf(interlocutor_name),9);
				say("Nice to meet you " + interlocutor_name + ". My name is "
						+ getInfo("i", "name") + ".");
			}

		} else if (!interlocutor_name.equals("Stranger")
				&& (str.contains("i am ") || str.contains("i`m "))) {
			say("I know who you are, " + interlocutor_name);
		}

		if (str.startsWith("hello") || str.contains("hello ")
				|| str.startsWith("hi") || str.contains("hi ")) {
			say("Hello, " + interlocutor_name);
		}

		// PARTYHARD
		ArrayList<String> nouns = new ArrayList<String>();
		ArrayList<String> verbs = new ArrayList<String>();
		new_words = new ArrayList<String>();
		String[] sentence = str.split(" ");
		String where_to_look = "";
		String what_to_look = "";
		String what_to_look2 = "";
		int x_to_look = -1;
		int y_to_look = -1;
		for (String s : sentence) {
			int i = inBrain(s);
			if (i != -1) {
				String result = partOfSpeech(s, i);

				switch (result) {
				case "noun":
					nouns.add(s);
					break;
				case "verb":
					verbs.add(s);
					break;
				case "unknown":
					new_words.add(s);
					break;
				}
			} else {

				new_words.add(s);
			}
		}

		

		

		if (str.contains(" you") || str.contains("you ")
				|| str.contains("your ") || str.contains(" your")) {
			where_to_look = "i";
		}
		
		if (str.contains("my ") || str.contains(" my")) {
			where_to_look = interlocutor_name;
		}
		if (str.contains("do you ") || str.contains("are you ")) {

			for (int i = 2; i < sentence.length; i++) {
				if (verbs.contains(sentence[i])) {
					x_to_look = words.indexOf(sentence[i]);
					
					what_to_look2 = sentence[i];
				}

				if (nouns.contains(sentence[i])) {
					y_to_look = words.indexOf(sentence[i]);
					
					what_to_look = sentence[i];
					break;
				}
				if (!words.contains(sentence[i])) {
					say("Sorry, but I do not know word '" + sentence[i] + "'");
					what_to_look = sentence[i];
				}
			}
			if (x_to_look != -1) {
				if (str.contains(" me") || str.contains("me ")) {
					what_to_look = interlocutor_name;
					y_to_look = words.indexOf(interlocutor_name);
				}
				if (remember(where_to_look, x_to_look, y_to_look)) {
					say("Yeah, I " + what_to_look2 + " " + what_to_look + ".");
				}

				else if (inBrain(what_to_look) == -1) {
					say("No, explain what is '" + what_to_look + "'.");
				} else {
					say("No, i do not " + what_to_look2 + " " + what_to_look
							+ ".");
				}
			}
		}

	}

	private static void say(String msg) {
		System.out.println("Mavery: " + msg);
	}

	private static void addConnection(String where, int num_of_word,
			int num_of_word2) {
		for (Bag<Bag> b : brain) {
			if (b.title.equals(where)) {
				for (Bag b2 : b) {
					if (b2.title.equals("connections")) {
						//System.out.println("Added new connectins: " + where + " " + num_of_word + " <-> " + num_of_word2);
						b2.add(new Point(num_of_word, num_of_word2));
						break;
					}
				}
			}
		}
	}

	// NEEDS OPTIMIZETION!!!
	private static String partOfSpeech(String word, int index) {
		for (Bag<Bag> b : brain) {
			if (b.title.equals(word)) {
				for (Bag<Point> b2 : b) {
					if (b2.title.equals("connections")) {
						for (Point p : b2) {

							if (p.x == index) {

								switch (p.y) {
								case 9:
									return "noun";
								case 10:
									return "verb";
								default:
									return "unknown";
								}
							}
						}
					}
				}
			}
		}
		return "unknown";

	}

	private static int inBrain(String word) {
		if (words.contains(word)) {
			return words.indexOf(word);
		} else
			return -1;
	}

	private static String getInfo(String where, String what) {
		int num_where = inBrain(where);
		int num_what = inBrain(what);

		if (num_where != -1 && num_what != -1) {
			for (Bag<Bag> b : brain) {

				if (b.title.equals(where)) {

					for (Bag<Point> b2 : b) {
						if (b2.title.equals("connections")) {
							for (Point p : b2) {
								if (p.x == num_what) {
								
									if(partOfSpeech(words.get(p.y), p.y)=="noun"){
									
									return words.get(p.y);
									}
									
								}
								if (p.y == num_what) {
								
									if(partOfSpeech(words.get(p.x), p.x)=="noun"){
										
										return words.get(p.x);
									}
								}

							}
						}
					}
				}
			}
		}
		return "nothing";
	}

	private static boolean remember(String where, int x, int y) {
		
		for (Bag<Bag> b : brain) {
			if (b.title.equals(where)) {
				for (Bag<Point> b2 : b) {
				
					if (b2.title.equals("connections")) {
						for (Point p : b2) {
							
							if (p.x == x && p.y == y) {
								return true;
							}
							if (p.x == y && p.y == x) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private static void loadSpecWords() {
		spec_words.add(new Word("what", "noun"));
	}

	public static void ask() {
		Random rand = new Random();
		if (interlocutor_name.equals("Stranger")) {
			say("What is your name?");
		} else {
			String questing = "";
			
			switch (rand.nextInt(3)) {
			case 0:
				say("...");
				break;
			default:
				say("...");
				break;
			}
			
		}
	}
}
