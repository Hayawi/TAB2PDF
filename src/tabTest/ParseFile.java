package tabTest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.IOUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;



public class ParseFile {
	
	
	
	private static String TITLE = "";
	private static String AUTHOR = "";
	private static String SPACING;

	private final static int barHeight = 35;
	private final static int heightSpacing = 7;
	private final static int startMargin = 30;
	
	
	private static int currentShift = 0;
	private static int relativeShift = 0;
	private static int pageLocationX = 0;
	private static int pageLocationY = 750;
	private static int currentLine = 0;
	private static int spacing = 5;
	private static int remainingLineSpace = 525;
	private static int pageLine = 0;
	private static PdfContentByte cb;
	private static int fontSize = 7;
	
	
	public static void main(String[] args) throws DocumentException, IOException {
			
			String file = openFile("C:/Users/Darren/CSE2311/GuitarTablatureProject/src/test/moonlightsonata.txt");
			processFile(file);
			
	}
	
	public static String openFile(String filename) throws IOException{
		  FileInputStream inputStream = new FileInputStream(filename);
		  String everything;
		    try {
		        everything = IOUtils.toString(inputStream);
		    } finally {
		        inputStream.close();
		    }
		    return everything;
	}
		
	public void createPDF(String filename) throws DocumentException, IOException {
		
		Document document = new Document();
		
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		
		document.open();
		
		document.add(new Paragraph("Hello World!"));
		
		document.close();
	}
	
	public static void processFile(String file) throws DocumentException, IOException {
		char newLine = '\n';
		TITLE = file.substring(file.indexOf('=') + 1, file.indexOf(newLine) - 1);
		file = file.substring(file.indexOf(newLine) + 1);
		AUTHOR = file.substring(file.indexOf('=') + 1, file.indexOf(newLine) - 1);
		file = file.substring(file.indexOf(newLine) + 1);
		SPACING = file.substring(file.indexOf('=') + 1, file.indexOf(newLine)); 
		file = file.substring(file.indexOf('|'));
		drawHeader();
		ArrayList<Queue> bars = barSort(file);
		
		// scan through arraylist
		for (Queue<String> q : bars) {
		// as we finish scanning through a queue, we will set the firstLine value to true.
			boolean firstLine = true;
			//keep track of the length of the bar;
			int lineLength = 0;
			
			// scan through queue
			for (String s : q) {
				// if it's the first line, we need to peek at the line, and record the length
				// if the length is greater than the remaining space, we make a new line.
				// if the length is less than the remaining space, we subtract the length from the remaining space.
				
				if (firstLine) {
					firstLine = false;
					lineLength = q.peek().length() * spacing;
					if (lineLength <= remainingLineSpace) {
						remainingLineSpace = remainingLineSpace - lineLength; 
					}
					else {
						System.out.println(pageLine);
						// since the length of the bar is greater than the remaining lineSpace
						// we reset the remainingLineSpace
						// we increment pageLine (which keeps track of which line we're on
						// we also need to reset the pageLocationX to equal the startMargin
						// also need to draw a new horizontal line
						pageLine++;
						remainingLineSpace = 525;
						pageLocationY = pageLocationY - (2 * barHeight);
						pageLocationX = 0;
						drawHorzBars(cb, pageLocationY);
					}
				}
				
				draw(parse(s), cb); 
				currentLine++; // after we finish drawing a line, we increment to the next line on the bar
				
				
				// since parsing will return an arrayList of that entire bar
				// since we have the entire queue, we can draw it bar by bar now.
				// we'll pass a queue to parse 
				// we reset the shift.
				currentShift = pageLocationX;        
			}
			pageLocationX += lineLength;

			// reset back to the top
			currentLine = 0;
			// we peek into the next bar and the length of the string * spacing is greater than the remaining space, we make a new line.

		}


	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList barSort(String string) {
		// Queue<String> stringQueue = new Queue<String>();
		String bar = "";
		String line = "";
		
		//a counter to keep track of how many bars we have, and what bar we're on
		int amountOfBars = 0;
		int barIndex = 0;
		// we store the index of the newline character and the index of the vertical line for easy use
		int indexOfNewLine = 0;
		int indexOfVerticalLine;
		// we need to keep track of what number of the 6 lines per bar we're on
		int lineIndex = 0;
		// we need to keep track if we're on the first line or not (we set this value to true when we finish with a set of bars
		boolean firstLine = true;
		
		// to keep track of the queues with an index
		ArrayList<Queue<String>> bars = new ArrayList<Queue<String>>();
		
		//until we're done with the entire string (while the index of the new line character is 0 or more, or the length of the string is greater than 0
		while (indexOfNewLine != -1 && string.length() > 0) {
			//we should most likely deal with the entire string in this method
			//so we take the string (without header), we then substring everything up to the new line
			// we run that new line through to a loop which processes each bar, and takes the substring of everything up to a vertical line
			// we repeat this six times
			
			// six iterations of the for loop.
			for (int i = 0; i < 6; i ++) { 
				indexOfNewLine = string.indexOf('\n');
				indexOfVerticalLine = string.indexOf('|');
				line = string.substring(0, indexOfNewLine); // this takes the entire line
				string = string.substring(indexOfNewLine + 1); // this takes everything after the line
				// we repeat this until we're done with the entire string
				// we use the empty string to know when we're finished.

				while (line.length() > 1) {
				// we have to ensure that when we substring the last bar in the line, that we also take the last bar.

					// general substring up to the next vertical bar
					// how do we ensure that we get the last set of vertical lines?
					// there's a high likelihood that the length of the index will be 1 - 3 less than the length of the string(line)
					// have to deal with lines better, causing an infinite loop
					// conditions, we start with a |, so we just substring everything when we hit the first instance of | 2 characters after.
					// so basically |---- and ||---- are the only combinations
					// so we take the substring of 0 to indexOf('|') two characters after the beginning.
					// the condition is that line.indexOf('|'); exists.
					if (line.indexOf('|', 2) > 0) {
						bar = line.substring(0, line.indexOf('|', 2)); // so this gets the index of the first vertical line past character 2
						line = line.substring(line.indexOf('|', 2)); // we then remove the substring we just stored

					}
					
					if (line.indexOf('|', 3) < 0) {
						bar = bar + line;
						bar = bar.substring(0, bar.length() - 1);
						line = "";
					}
					
				// if this is the first line, we make a queue in the arraylist for each substring, and add to the amount of bars
				// if it's not then, we just continue putting strings into the queue, and cycle through based on the amount of bars on the first line
					if (firstLine) {
						bars.add(new LinkedList<String>());
						amountOfBars++;
						bars.get(barIndex).add(bar);
						barIndex++;

					}
					else {
						bars.get(barIndex).add(bar);
						barIndex++;
					}





				}
				
				// after we're finished with that line, we reset the bar index. (basically amount of bars subtract barIndex)
				barIndex = barIndex - amountOfBars; // basically we know the size of the array (total number of bars recorded)
												   // we know the number of bars in the set
												   // so given our index matches the size, we want it to go back the amount of bars
												   // so so if we have 3 bars, and our size is 7, we subtract 3 from the index or size, doesn't matter
												   // at the end of every iteration it'll be index = size
												   // so our index is 7, we want to go to index 4, since we have 0, 1, 2, 3, 4, 5, 6					
				firstLine = false;
			}
			
			string = string.substring(string.indexOf('\n') + 1);
			if (string.indexOf('\n') <= 1) {
				while (string.indexOf('\n') >= 0 && string.indexOf('\n') <= 1)
				string = string.substring(string.indexOf('\n') + 1);
			}
			// after we're done with 6 lines, we reset firstline to true and
			//  we need to set barIndex to the amount of bars, since we're moving on to a new set of bars
			firstLine = true;
			barIndex = bars.size(); // so now we have barIndex equal to the size. so since we're at size 4, and index 4, when we add one to the size, it'll be size 5, index 4, and again we increment.
			amountOfBars = 0; // reset this value for the set
		}
		
		return bars;
		// this finds the count of the next | (if it is at index 1, we take the index of everything after?
			// check indexOf method, high likelihood of an indexOf that checks after a certain index, so we will check after index 1
		// we also need to make sure that the index is 2 less than the end. if it is, we don't bother with the substring.
		// we then queue the substring of everything up to that |
		// 
		//queue this string from 0 to |
		// increment barCounter
		
	}
	
	public static ArrayList<String> parse(String string) {
		
		ArrayList<String> token = new ArrayList<String>();
		String hyphen = "";
		
		for (int i = 0; i < string.length(); i++) {
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
					hyphen = hyphen + "-";
					i++;
					if (i == string.length())
						break;
				}
				token.add(hyphen);
				hyphen = "";
				i--;
			}
			else if ((string.charAt(i) >= '0' && (string.charAt(i) <= '9'))) {
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
			else if (string.charAt(i) == 'p')
				token.add("p");
		}
		
		return token;
	}	

	public static void writePDF(String filename, String contents) throws IOException{
		Document document = new Document();

		
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
		
			document.open();
			// write the text
		
			document.add(new Paragraph(contents));
			cb = writer.getDirectContent();
			
			// Adds some lines just for TESTING
			drawHorzBars(cb, 800);
			drawVerticalBars(cb, pageLocationY, currentShift);
			String file = ParseFile.openFile("C:/Users/Darren/CSE2311/GuitarTablatureProject/src/test/moonlightsonata.txt");
			ParseFile.processFile(file);
			
			document.close(); // no need to close PDFWriter?
			} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void drawHeader() throws DocumentException, IOException {
		cb.beginText();
		BaseFont bf = BaseFont.createFont();
		cb.setFontAndSize(bf, 32);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, TITLE, 300, 800, 0);
		cb.setFontAndSize(bf, 14);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, AUTHOR, 300, 780, 0);
		cb.endText();
		
	}
	
	public static void draw(ArrayList<String> bar, PdfContentByte cb) throws DocumentException, IOException {
		/* considering we are dealing with this by a bar by bar approach:
		 * we need to keep track of several variables
		 * the starting margin, which is the margin we start at, at the start of every line
		 * the current line we're on.
		 * 
		 * 
		 * when we reach a |, we'll probably end up drawing it 5 times so let's just keep the y co-ordinate at the same spot. (fix later)
		 * when we reach a * we'll draw an extra line
		 * when we reach the end of a line, and it ends with a -, for simplicity we'll just not draw anything and fix after.
		 * while we draw, we'll increment spacing. before every draw, we'll take the length of the string in the queue, and multiply its
		 * length by the spacing, and add it to the currentShift.
		 */
		
		for (String s : bar) {
			if (s.equals("|")) {
				cb.setLineWidth(0.5F);
				drawVerticalBars(cb, pageLocationY, currentShift);
				currentShift += spacing;
			}
			else if (s.equals("||")) {
				cb.setLineWidth(2F);
				drawVerticalBars(cb, pageLocationY, currentShift);
				currentShift += spacing * 2;
			}
			else if (s.contains("-"))
				currentShift += (s.length() * spacing);
			else if (s.length() == 2) {
				currentShift += 2 * spacing;
			}
			else if (s.length() == 1) {
				if (s.contains("*")) {
					drawCircle(pageLocationY, currentShift);
				}
				else
				{
					drawNumber(s);
				}
				
				currentShift += spacing;
			}
			
		}
	}
	
	// set to arbitrary thicknesses for testing
	public static void drawHorzBars(PdfContentByte cb,int yStart) {
		cb.setLineWidth((float) 0.5);
		// horizontal lines
		int x1 = 0;
		int x2 = 1000;
		int linesPerBar = 6;
		
		for (int i = 0; i < linesPerBar ; i++) {
		cb.moveTo(x1, pageLocationY - heightSpacing * i);
		cb.lineTo(x2, pageLocationY - heightSpacing * i);
		}
		cb.stroke();
		
	}
	
	// need to rework parameters
	public static void drawVerticalBars(PdfContentByte cb, int yStart, int xShift) {
		cb.moveTo(startMargin + xShift, yStart);
		cb.lineTo(startMargin + xShift, yStart - barHeight);
		cb.stroke();
	}
	
	// add colour option, only really needs to cb.setColour(tabObject.getColour());
	// pageLocationY - 2 needs to have 2 changed to a variable; 2 as an arbitrary number to test shift
	// font style could be changed in the future; easy to change, need to refer to BaseFont API.
	public static void drawNumber(String number) throws DocumentException, IOException {
		cb.beginText();
		BaseFont bf = BaseFont.createFont();
		cb.setFontAndSize(bf, fontSize);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, number, startMargin + currentShift, (pageLocationY - 2) - heightSpacing * currentLine, 0);
		cb.endText();
		
	}

	private static void drawCircle(int yStart, int xShift) {
		cb.circle((float)(startMargin + xShift), (float)yStart - currentLine*heightSpacing , 1.0F);
	}
	
	

}
