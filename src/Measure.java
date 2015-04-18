import java.util.ArrayList;

public class Measure extends Object {
	
	ArrayList<String> measure;
	ArrayList<ArrayList<String>> tokens;
	private int length;
	private boolean leftRepeatBarLine;
	private boolean rightRepeatBarLine;
	private boolean endLine;
	private int numberOfRepeats;
	
	public Measure(ArrayList<String> measure) {
		this.measure = measure;
		this.rightRepeatBarLine = false;
		this.leftRepeatBarLine = false;
		this.endLine = false;
		this.numberOfRepeats = 0;
		tokens = new ArrayList<ArrayList<String>>();
		for (String s: measure) {
			this.tokens.add(ParseFile.parse(s));
		}
		repeatCheck(tokens);
		endCheck(tokens);
		this.length = getMeasureLength(tokens);
	}
	
	public boolean rightRepeat() {
		return this.rightRepeatBarLine;
	}
	
	public boolean leftRepeat() {
		return this.leftRepeatBarLine;
	}
	
	public boolean endLine() {
		return this.endLine;
	}
	
	public int numberOfRepeats() {
		return this.numberOfRepeats;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public ArrayList<ArrayList<String>> getTokens() {
		return tokens;
	}
	
	private void repeatCheck(ArrayList<ArrayList<String>> tokens) {
			ArrayList<String> thirdLine = tokens.get(2);
			ArrayList<String> fourthLine = tokens.get(3);

			if (thirdLine.get(1).contains("*") && fourthLine.get(1).contains("*") && 
					thirdLine.get(0).contains("||") && fourthLine.get(0).contains("||")) {
				for (ArrayList<String> lines : tokens) {
					lines.remove(0);
					if (lines.get(0).contains("-") && lines.get(0).length() > 1) {
						lines.set(0, lines.get(0).substring(1));
					}
					else {
						lines.remove(0);
					}
				}
				leftRepeatBarLine = true;
			}
			if (thirdLine.get(thirdLine.size() - 2).contains("*") && fourthLine.get(fourthLine.size() - 2).contains("*") && 
					thirdLine.get(thirdLine.size() - 1).contains("||") && fourthLine.get(fourthLine.size() - 1).contains("||")) {
				for (ArrayList<String> lines : tokens) {
					if (lines.get(lines.size() - 1).contains("|") && lines.get(lines.size() - 1).length() > 1) {
						if (Character.isDigit(lines.get(lines.size() - 1).charAt(1)))
							numberOfRepeats = Integer.parseInt(lines.get(lines.size() - 1).substring(1));
					}
					lines.remove(lines.size() - 1);
					if (lines.get(lines.size() - 1).contains("-") && lines.get(lines.size() - 1).length() > 1) {
						lines.set(lines.size() - 1, lines.get(lines.size() - 1).substring(1));
					}
					else {
						lines.remove(lines.size() - 1);
					}
				}
				rightRepeatBarLine = true;
			}
	}
	
	private void endCheck (ArrayList<ArrayList<String>> tokens) {
		boolean exists = true;
		for (ArrayList<String> lines : tokens) {
			if (!lines.get(lines.size() - 1).contains("|") | !lines.get(lines.size() - 2).contains("*")){
				exists = false;
				break;
			}
		}
		if (exists){
			for (ArrayList<String> lines : tokens) {
				
					lines.remove(lines.size() - 1);
					lines.remove(lines.size() - 1);
			}
			endLine = true;
		}
	}

	private int getMeasureLength(ArrayList<ArrayList<String>> tokens) {
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
