import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
	
	public static ArrayList<Measure> sortMeasure(String body) {
		String line = "";
		int indexOfNewLine = 0;
		int indexOfVerticalLine = 0;
		int lineLength = 0;
		String checkString = "";
		Pattern pattern = Pattern.compile("\\|?(\\||(\n[0-9]))(-|\\*)");
		
		ArrayList<String> blockOfMeasures = new ArrayList<String>();
		ArrayList<Measure> measures = new ArrayList<Measure>();
		
		// forgot why body length > 0 is used, check again later
		while (indexOfNewLine != -1 && body.length() > 0) {
			for (int i = 0; i < 6; i ++) { 
				Matcher matcher = pattern.matcher(body);
				if (matcher.find()) {
					indexOfVerticalLine = matcher.start();
					
				indexOfNewLine = body.indexOf('\n', indexOfVerticalLine + 1);
				int check2 = body.indexOf('\n', indexOfNewLine + 1);
				checkString = body.substring(indexOfNewLine, check2).trim();
				Matcher check = pattern.matcher(checkString);
				if (!check.find() && i != 5) {
					blockOfMeasures.clear();
					break;
				}
				line = body.substring(indexOfVerticalLine, indexOfNewLine).trim(); 
				System.out.println(line);
				if (line.lastIndexOf("| ") > 0) 
					line = line.substring(0, line.lastIndexOf("| ") + 2).trim(); 
				body = body.substring(indexOfNewLine + 1); 
				blockOfMeasures.add(line);
				}
			}
			if (unevenBlockLengthCheck(blockOfMeasures)) {
				String message = "";
				for (String s : blockOfMeasures) {
					message = message + s + '\n';
				}
				throw new InvalidMeasureException("This measure is formatted incorrectly.");
			}
			if (blockOfMeasures.size() > 0)
				measures.addAll(convertToMeasures(blockOfMeasures));
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
			else if (string.charAt(i) == ' ') {
				i++;
				while (string.charAt(i) == ' ') {
					hyphen = hyphen + "-";
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
			else {
				if (string.charAt(i) != '>' && string.charAt(i) != '<')
				token.add("-");
			}
		}
		return token;
	}
	
	public static ArrayList<Measure> convertToMeasures(ArrayList<String> block) {
		Pattern separator = Pattern.compile("(\\||^[0-9])([0-9]|\\|)?\\|?");
		Matcher matcher = separator.matcher(block.get(2));
		ArrayList<String> measure = new ArrayList<String>();
		ArrayList<Measure> newMeasures = new ArrayList<Measure>();
		int indexOfBeginningStaff = 0;
		matcher.find();
		while (matcher.find()) {
			for (String s : block) {
				measure.add(s.substring(indexOfBeginningStaff, matcher.end()));
			}
			newMeasures.add(new Measure(measure));
			measure.clear();
			indexOfBeginningStaff = matcher.start();
		}
		return newMeasures;
	}
	
	public static boolean unevenBlockLengthCheck(ArrayList<String> blockOfMeasures) {
		HashSet<Integer> lengths = new HashSet<Integer>();
		for (String s : blockOfMeasures) {
			lengths.add(s.length());
		}
		if (lengths.size() > 1)
			return true;
		return false;
	}
	/*
	public static ArrayList<String> reconstruct(ArrayList<String> block) {
		StringBuilder string = new StringBuilder();
		ArrayList<ArrayList<String>> tokens = new ArrayList<ArrayList<String>>();
		ArrayList<String> processedBlock = new ArrayList<String>();
		for (String line : block) {
			tokens.add(parse(line));
		}
		for (ArrayList<String> line : tokens) {
			for (String token : line) {
				string.append(token);
			}
			processedBlock.add(string.toString());
		}
		return null; 
	}
	*/
}
