package packag;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.itextpdf.text.DocumentException;

public class ParseFile {
	
	public static String openFile(String filePath) throws IOException{
		  FileInputStream inputStream = new FileInputStream(filePath);
		  String file;
		    try {
		        file = IOUtils.toString(inputStream);
		    } finally {
		        inputStream.close();
		    }
		    return file;
	}
	
	public static ArrayList staffSort(String body) {
		// Queue<String> stringQueue = new Queue<String>();
		String bar = "";
		String line = "";
		
		//a counter to keep track of how many bars we have, and what bar we're on
		int numberOfStaves = 0;
		int indexOfStaff = 0;
		
		// we store the index of the newline character and the index of the vertical line for easy use
		int indexOfNewLine = 0;
		int indexOfVerticalLine = 0;
		int indexOfLastVerticalLine = 0;
		Pattern pattern = Pattern.compile("\\|?(\\||(\n[0-9]))(-|\\*)");

		// we need to keep track of what number of the 6 lines per bar we're on
		int lineIndex = 0;
		
		// we need to keep track if we're on the first line or not (we set this value to true when we finish with a set of bars
		boolean firstLine = true;
		
		// to keep track of the queues with an index
		ArrayList<Queue<ArrayList<String>>> bars = new ArrayList<Queue<ArrayList<String>>>();
		ArrayList<String> blockOfMeasures = new ArrayList<String>();
		ArrayList<Measure> measures = new ArrayList<Measure>();
		
		//until we're done with the entire string (while the index of the new line character is 0 or more, or the length of the string is greater than 0
		while (indexOfNewLine != -1 && body.length() > 0) {


			for (int i = 0; i < 6; i ++) { 
				Matcher matcher = pattern.matcher(body);
				if (matcher.find()) {
					indexOfVerticalLine = matcher.start();					
				}
				indexOfNewLine = body.indexOf('\n', indexOfVerticalLine + 1);
				line = body.substring(indexOfVerticalLine, indexOfNewLine).trim(); // this takes the entire line
				
				
				if (line.lastIndexOf("| ") > 0) 
					line = line.substring(0, line.lastIndexOf("| ") + 1).trim(); // quick and dirty fix

				body = body.substring(indexOfNewLine + 1); 
				
				
	/*			int index = 0;
				matchSeparators.find();
				while (matchSeparators.find()) {
					bar = line.substring(index, matchSeparators.end());
					index = matchSeparators.start();
					System.out.println(bar);

					ArrayList<String> parsedBar = parse(bar);
					System.out.println(firstLine);
					if (firstLine) {
						bars.add(new LinkedList<ArrayList<String>>());
						numberOfStaves++;
						bars.get(indexOfStaff).add(parsedBar);
						indexOfStaff++;
					}
					else {
						bars.get(indexOfStaff).add(parsedBar);
						indexOfStaff++;
					} 
				} */
				
				blockOfMeasures.add(line);
				indexOfStaff = indexOfStaff - numberOfStaves; 				

			}
			measures.addAll(something(blockOfMeasures));
			blockOfMeasures.clear();

			
			body = body.substring(body.indexOf('\n') + 1);
			if (body.indexOf('\n') <= 1) { // something idk check again later
				while (body.indexOf('\n') >= 0 && body.indexOf('\n') <= 1)
				body = body.substring(body.indexOf('\n') + 1);
			}
		}
		return measures;
	}
	public static ArrayList<String> parse(String string) {
		
		ArrayList<String> token = new ArrayList<String>();
		String hyphen = "";
		Pattern isDigit = Pattern.compile("^[0-9]");
		
		for (int i = 0; i < string.length(); i++) {
			Matcher matcher = isDigit.matcher(string);
			if (string.charAt(i) == '|') {
				if ((i + 1 < string.length()) && (i + 2 < string.length()) && string.charAt(i+1) == '|' && string.charAt(i+2) == '|') {
					token.add("|||");
					i = i + 2;
				}
				else if ((i + 1 < string.length()) && string.charAt(i+1) == '|') {
					token.add("||");
					i++;
				}
				else if ((i + 1 < string.length()) && (i + 2 < string.length()) && string.charAt(i+1) == '|' && string.charAt(i+2) == '|') {
					token.add("|||");
					i = i + 2;
				}
				else if ((i + 1 < string.length()) && (string.charAt(i+1) >= '0' && (string.charAt(i+1) <= '9'))) {
					token.add("|" + string.charAt(i+1));
					i++;
				}
				else 
					token.add("|");
			}
			else if (string.charAt(i) == '-') {
				while (string.charAt(i) == '-') {
					hyphen = hyphen + "-"; // use stringbuilder or something for this later
					i++;
					if (i == string.length())
						break;
				}
				token.add(hyphen);
				hyphen = "";
				i--;
			}
			else if ((string.charAt(i) >= '0' && (string.charAt(i) <= '9')) && i > 0) { 
				//check the second digit only if index + 1 is not equal to the length of the string
				if (i + 1 < string.length())
					// if the second digit is a number, we will treat it as a two-digit number.
					if (string.charAt(i + 1) >= '0' && (string.charAt(i + 1) <= '9')) {
						if (string.charAt(i-1) == '<') {
							token.add(string.substring(i-1, i+3));
							i = i + 2;
							}
							else {
							token.add(string.substring(i, i+2));
							i = i + 1;
							}
					}
				// if the character ahead is not a digit, we check if the previous character is a angle bracket
				else if (string.charAt(i-1) == '<') {
				token.add(string.substring(i-1,i+2));
				i = i + 1;
				}
				// if not we just add the number itself.
				else {
				token.add("" + string.charAt(i));
				}
			}
			else if (string.charAt(i) == 's') 
				token.add("s");
			else if (string.charAt(i) == '*')
				token.add("*");
			else if (string.charAt(i) == 'h')
				token.add("h");
			else if (string.charAt(i) == 'p')
				token.add("p");
			else if (matcher.find()) {
				token.add("" + string.charAt(i));
			}
		}
		return token;
	}
	public static ArrayList<Measure> something(ArrayList<String> block) {
		Pattern separator = Pattern.compile("(\\||^[0-9])([0-9]|\\|)?");
		Matcher matcher = separator.matcher(block.get(2));
		ArrayList<String> measures = new ArrayList<String>();
		ArrayList<Measure> newMeasures = new ArrayList<Measure>();
		int index = 0;
		
		matcher.find();
		while (matcher.find()) {
			for (String s : block) {
				measures.add(s.substring(index, matcher.end()));
			}
			newMeasures.add(new Measure(measures));
			measures.clear();
			index = matcher.start();
		}
		
		return newMeasures;
	}
	
	public static ArrayList<String> reconstruct(ArrayList<String> tokens) {
		return null; // parse, reconstruct. then pass to something() if we have two tokens that are adjacent numbers, insert a space. blah blah blah do shit, only call this if
					 // the lengths of all 6 lines of the measure are not the same.
	}
}
