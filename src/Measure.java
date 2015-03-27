import java.util.ArrayList;

public class Measure extends Object {
	ArrayList<String> measure;
	ArrayList<ArrayList<String>> tokens;
	private int length;
	
	public Measure(ArrayList<String> measure) {
		this.measure = measure;
		tokens = new ArrayList<ArrayList<String>>();
		for (String s: measure) {
			this.tokens.add(ParseFile.parse(s));
		}
		this.length = getMeasureLength(tokens);
	}
	
	public int getLength() {
		return this.length;
	}
	
	public ArrayList<ArrayList<String>> getTokens() {
		return tokens;
	}
 
	public static int getMeasureLength(ArrayList<ArrayList<String>> tokens) {
		int staffLength = -1;
		ArrayList<String> line = tokens.get(2);
		for (String s : line) {
			if (s.contains("|")) {
				staffLength += 1;
			}
			else {
				staffLength += s.length();
			}
		}
		return staffLength;
	}
}
