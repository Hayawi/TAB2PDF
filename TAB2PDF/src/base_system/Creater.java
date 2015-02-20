package base_system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;

public class Creater {

	private static final String NOT_IMPLEMENTED = " NOT IMPLEMENTED!";

	// stave storage and reorganization 
	private static String[] bigStave; // stores all measures on one big stave
	private static String[] staveBuffer; // stores a tab ready to be placed
	private static ArrayList<String[]> organizedStaves = new ArrayList<String[]>(); 
	private static String[] consecRemStaves; // stave with consecutive vertical bars removed
	private static String[] stavsNoWhtSpace ; // staves with single spaces removed.
	private static String[] lines; // for split lines method
	
	// page attributes // probably will be move into Tablature object upon refactoring
	// along with several methods
	private static float spacing = 12f;
	private static int bodyWidth = 560; // width of body in pixels.
	private static int margin = 40;
	private static Document doc;
	private static int topOfPage = 733;
	private static float lineWidth = 0.3f;
	private static int defaultFontSize = 9;

	// details
	private static float staveWidth = 0f;
	private static int maxVertBars = 3;
	private static BaseFont bf;
	private static PdfContentByte canvas;
	private static float barSpacing = 7f;

	/**
	 * 
	 * @param doc 
	 * @param contentByte
	 *            Used to draw to the cnavas and position the pencil
	 * @param body
	 *            The tablature of notes, measures, staves and musical symbols.
	 * @param doc
	 *            y to the document to be drawn.
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void createTab(Document document, PdfContentByte contentByte, String body) throws DocumentException, IOException {
		doc = document;
		makeBigStave(body);		
		canvas = contentByte;
		drawTablature();
	}

	public static void drawTablature() throws DocumentException,IOException {
		
		reorganizeStaves(); // takes the big stave and partitions it taking into account spacing.
		bf = BaseFont.createFont();
		int repeatDetectLineNum = 3;
		String token = "";
		float xPosTok = 0f; // pos of char relative to end of token.
		// for drawing diamond
		float diamondSpace = 6f;
		float diamondSideLength = 3f;
		float diamondWidth = diamondSideLength * 1.4142f;

		float yTune = -2.5f;// tunes the  vertical position of the digits drawn.
		canvas.setLineWidth(lineWidth);
		int xPos = margin,
			yPos = topOfPage,
			fontWidth = 5;

		/*
		 * This beefy triple for loop carries out all the drawing
		 * by iterating along each line parsing tokens 5 characters at a time
		 * and matching them to specific kinds of symbols given by the rugular
		 * expressions within the if statements.
		 */
		int staveCount = 0;
		for (String[] stave : getOrganizedStaves()) { // new stave
			
			staveCount++;  
			// starts a new page after every 9 staves.
			if(staveCount % 10 == 0) 
			{
				doc.newPage();
				canvas.setLineWidth(lineWidth);
				yPos = topOfPage;
			}
			canvas.moveTo(xPos, yPos);
			for (int i = 0; i < stave.length; i++) { // new line
				// draw line for left margin
				drawLeftMargin(xPos, yPos);

				for (int j = 0; j < stave[i].length(); j++) { // per char

					System.out.print(stave[i].charAt(j)); // debug
					
					// special case for the start of the string
					if (j == 4) {
						token = stave[i].substring(0, 5);
						// System.out.println(token);
					}
					// all tokens are 5 chars in length
					else if (j > 4) {
						token = stave[i].substring(j - 4, j + 1);
					} else
						token = "";
					/*
					 * This matches tokens along the line and draws the
					 * corresponding symbols
					 */
					
					// single digit volume swell
					if (token.matches(".[-*]<[0-9]>")) {
						xPosTok = calcPosRelToEnd(1, xPos);
						singleDigitLineSurround(xPosTok, yPos, fontWidth);
						String text = token.substring(3, 4);
						drawText(yTune, xPosTok, yPos, text,defaultFontSize);
						drawDiamond(xPosTok, yPos, diamondSpace, diamondWidth);
						// System.out.println(token);
					}
					// double digit volume swell
					else if (token.matches(".<[0-9]{2}>")) {
						xPosTok = calcPosRelToEnd(3, xPos);
						twoDigitLineSurround(xPosTok, yPos, fontWidth);
						String text = token.substring(2, 4);
						drawText(yTune, xPosTok, yPos, text,defaultFontSize);
						drawDiamond(xPosTok, yPos, diamondSpace, diamondWidth);
						// System.out.println(token);
					}
					// single digit lagato slide
					else if (token.matches("..[0-9][s][0-9]")) {
						xPosTok = calcPosRelToEnd(3, xPos);
						drawLagatoSlide(token, yTune, xPosTok, yPos, fontWidth);
						// System.out.println(token);
					}
					// left double digit lagato slide
					else if (token.matches(".[0-9]{2}s[0-9]")) {
						System.out.println(token + NOT_IMPLEMENTED);
					}
					// right double digit lagato slide
					else if (token.matches(".[0-9]s[0-9]{2}")) {
						System.out.println(token + NOT_IMPLEMENTED);
					}
					// both double digit lagato slide
					else if (token.matches("[0-9]{2}s[0-9]{2}")) {
						System.out.println(token + NOT_IMPLEMENTED);
					}
					// double digit
					else if (token.matches("..[*-][0-9]{2}")) {
						xPosTok = calcPosRelToEnd(2, xPos);
						twoDigitLineSurround(xPosTok, yPos, fontWidth);
						String text = token.substring(3);
						drawText(yTune, xPosTok, yPos, text,defaultFontSize);
						// System.out.println(token + NOT_IMPLEMENTED);
					}
					// single digit
					else if (token.matches("..[*-][0-9][*-]")) {
						xPosTok = calcPosRelToEnd(2, xPos);
						singleDigitLineSurround(xPosTok, yPos, fontWidth);
						String text = token.substring(3, 4);
						drawText(yTune, xPosTok, yPos, text, defaultFontSize);
						// System.out.println(token);
					}
					// repeat start
					else if (token.matches("\\|\\|\\*..")
							&& i == repeatDetectLineNum) {
						xPosTok = calcPosRelToEnd(5, xPos);
						drawBeginRepeatSymbol(xPosTok, yPos, 0);
						// System.out.println(token + NOT_IMPLEMENTED);
					}
					// repeat end
					else if (token.matches("--\\*\\|\\|")
							&& i == repeatDetectLineNum) {

						xPosTok = calcPosRelToEnd(4, xPos);
						drawBeginRepeatSymbol(xPosTok, yPos, 1);
						// System.out.println( " " +token + NOT_IMPLEMENTED);
					} else if (token.matches("..[0-9][h][0-9]")) {

						hammerTime(barSpacing, token, yTune, xPos, yPos,
								fontWidth);
					}
					// first vertical bar is a special case
					else if (token.matches("\\|[\\*-]...") && i == 3 && j < 5) {
						xPosTok = calcPosRelToEnd(5, xPos);
						drawThinVertBar(xPosTok, yPos);
						// System.out.println( " " +token + NOT_IMPLEMENTED);
					}
					// vertical bar middle case
					else if (token.matches(".[-]\\|[-].") && i == 3 && j > 4) {
						xPosTok = calcPosRelToEnd(3, xPos);
						drawThinVertBar(xPosTok, yPos);
						// System.out.println( " " +token + NOT_IMPLEMENTED);
					}
					// vertical bar end of line case
					else if (token.matches("...[-]\\|") && i == 3
							&& j == stave[i].length() - 1) {
						xPosTok = calcPosRelToEnd(0, xPos);
						drawThinVertBar(xPosTok, yPos);
						// System.out.println( " " +token + NOT_IMPLEMENTED);
					}
					// triple vertical bars specify the end of the song.
					else if (token.matches(".[-]\\|{3}") && i == 3
							&& j == stave[i].length() - 1) {
						xPosTok = calcPosRelToEnd(3, xPos);
						drawTripleThinBars(xPosTok, yPos);
						// System.out.println( " " +token + NOT_IMPLEMENTED);
					}						
					// increase pencil position by spacing.
					xPos += spacing;
				}
				// draw line for right margin
				canvas.lineTo(bodyWidth + margin, yPos);
				canvas.stroke();

				// reset positions for next bar
				xPos = margin; // move back to left margin
				yPos -= barSpacing; // moe down one bar level
				canvas.moveTo(xPos, yPos); // move pencil here
				System.out.println(); // for debugging
			} // end line for loop
			// make space for next stave

				yPos -= barSpacing * 4.2; // spacing between staves
			
			System.out.println(); // for debugging
		} // end stave for loop
	} // end drawTablature method

	/**
	 * @param xPosTok
	 * @param yPos
	 */
	private static void drawTripleThinBars(float xPosTok, int yPos) {
		float vertBarSpacing = 4f;
		drawThinVertBar(xPosTok, yPos);
		drawThinVertBar(xPosTok + vertBarSpacing, yPos);
		drawThinVertBar(xPosTok + vertBarSpacing * 2, yPos);
	}

	/**
	 * @param barSpacing
	 * @param token
	 * @param yTune
	 * @param xPos
	 * @param yPos
	 * @param fontWidth
	 */
	private static void hammerTime(float barSpacing, String token, float yTune,
			int xPos, int yPos, int fontWidth) {
		float xPosTok;
		// draw first digit
		xPosTok = calcPosRelToEnd(3, xPos);
		singleDigitLineSurround(xPosTok, yPos, fontWidth);
		String text = token.substring(2, 3);
		drawText(yTune, xPosTok, yPos, text,defaultFontSize);

		// draw curve
		canvas.moveTo(xPosTok, yPos + barSpacing/1.5f);
		xPosTok = calcPosRelToEnd(2, xPos);
		canvas.curveTo(xPosTok, yPos + barSpacing*1.25f ,
				calcPosRelToEnd(0, xPos), yPos + barSpacing/1.5f);
		canvas.stroke();
		
		// draw little h for hammer
		text = "h";
		int fontSize = 5;
		int xTune = 3;
		xPosTok = calcPosRelToEnd(2, xPos);
		drawText(-yTune*3, xPosTok+xTune, yPos, text,fontSize);
		canvas.moveTo(xPosTok, yPos);
		
		// move pencil to just after first char.
		xPosTok = calcPosRelToEnd(3, xPos);
		canvas.moveTo(xPosTok + fontWidth / 2, yPos);

		// draw last digit
		xPosTok = calcPosRelToEnd(0, xPos);
		singleDigitLineSurround(xPosTok, yPos, fontWidth);
		text = token.substring(4, 5);
		drawText(yTune, xPosTok, yPos, text,defaultFontSize);
	}

	/**
	 * @param indexFromEnd
	 * @param xPos
	 * @return
	 */
	private static float calcPosRelToEnd(int indexFromEnd, int xPos) {
		return xPos - spacing * indexFromEnd;
	}

	/**
	 * @param xPos
	 * @param yPos
	 */
	private static void drawLeftMargin(int xPos, int yPos) {
		canvas.lineTo(xPos - margin, yPos);
		canvas.stroke();
		canvas.moveTo(xPos, yPos);
	}

	/**
	 * @param token The 5 character token
	 * @param yTune Tweaks the vertical position
	 * @param xPosTok The position relative to the end of the token
	 * @param yPos The y position
	 * @param fontWidth The font width
	 */
	private static void drawLagatoSlide(String token, float yTune,
			float xPosTok, int yPos, int fontWidth) {
		singleDigitLineSurround(xPosTok, yPos, fontWidth);

		// draw first digit
		String text = token.substring(2, 3);
		drawText(yTune, xPosTok, yPos, text,defaultFontSize);

		// draw slash
		drawSlash(xPosTok, yPos);

		// draw line from first digit through slash to second digit.
		canvas.lineTo(xPosTok + spacing * 2 - fontWidth / 2, yPos);
		canvas.stroke();
		canvas.moveTo(xPosTok + spacing * 2 + fontWidth / 2, yPos);

		// draw second digit
		text = token.substring(4, 5);
		canvas.beginText();
		canvas.setFontAndSize(bf, 9);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, text, xPosTok
				+ spacing * 2, yPos + yTune, 0);
		canvas.endText();
	}

	/**
	 * @param xPosTok
	 * @param yPos
	 */
	private static void drawSlash(float xPosTok, int yPos) {
		canvas.lineTo(xPosTok + spacing, yPos);
		canvas.stroke();
		canvas.moveTo(xPosTok - 1.5f + spacing, yPos - 1.5f);
		canvas.lineTo(xPosTok + 1.5f + spacing, yPos + 1.5f);
		canvas.stroke();
		canvas.moveTo(xPosTok + spacing, yPos);
	}

	/**
	 * @param xPosTok
	 * @param yPos
	 * @param diamondSpace
	 * @param diamondWidth
	 */
	private static void drawDiamond(float xPosTok, int yPos,
			float diamondSpace, float diamondWidth) {
		// draw line upto beggining of diamond
		canvas.lineTo(xPosTok + diamondSpace, yPos);
		canvas.stroke();
		// draw diamond

		// draw top left side
		canvas.moveTo(xPosTok + diamondSpace, yPos);
		canvas.lineTo(xPosTok + diamondSpace + diamondWidth / 2, yPos
				+ diamondWidth / 2);
		canvas.stroke();

		// draw top right side
		canvas.moveTo(xPosTok + diamondSpace + diamondWidth / 2, yPos
				+ diamondWidth / 2);
		canvas.lineTo(xPosTok + diamondSpace + diamondWidth, yPos);
		canvas.stroke();

		// draw bottom right side
		canvas.moveTo(xPosTok + diamondSpace + diamondWidth, yPos);
		canvas.lineTo(xPosTok + diamondSpace + diamondWidth / 2, yPos
				- diamondWidth / 2);
		canvas.stroke();

		// draw bottom left side
		canvas.moveTo(xPosTok + diamondSpace + diamondWidth / 2, yPos
				- diamondWidth / 2);
		canvas.lineTo(xPosTok + diamondSpace, yPos);
		canvas.stroke();

		// set position after diamond
		canvas.moveTo(xPosTok + diamondSpace + diamondWidth, yPos);
	}

	private static void singleDigitLineSurround(float xPosTok, int yPos,
			int fontWidth) {
		canvas.lineTo(xPosTok - fontWidth / 2, yPos);
		canvas.stroke();
		canvas.moveTo(xPosTok + fontWidth / 2, yPos);
	}

	private static void drawText(float yTune, float xPos, int yPos, String text, int fontSize) {
		canvas.beginText();
		canvas.setFontAndSize(bf, fontSize);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, text, xPos, yPos
				+ yTune, 0);
		canvas.endText();
	}

	private static void twoDigitLineSurround(float xPosTok, int yPos,
			int fontWidth) {
		canvas.lineTo(xPosTok - fontWidth, yPos);
		canvas.stroke();
		canvas.moveTo(xPosTok + fontWidth, yPos);
	}

	/**
	 * Draws dots at the specified x and y coordinates.
	 * 
	 */
	public static void drawCircles(float xPos, float y, float offset) {
		float x = xPos + offset; // adjust relative pencil position
		canvas.saveState();
		canvas.setColorStroke(new GrayColor(0.0f));
		canvas.setColorFill(new GrayColor(0.0f));
		canvas.circle(x, y, 1.3f);
		canvas.fillStroke();
		canvas.restoreState();
		canvas.saveState();
		canvas.circle(x, y + barSpacing, 1.3f);
		canvas.fillStroke();
		canvas.restoreState();

		// retore positon.
		canvas.moveTo(xPos, y);

	}

	/**
	 * Draws a thick vertical bar at the given x and y position.
	 * 
	 * @param cicleOffset
	 *            the spacing between the bar and the dots
	 * @param side
	 *            specifies weather it is a begin or end repeat symbol 0 means
	 *            begin and 1 means end.
	 */
	public static void drawBeginRepeatSymbol(float x, float y, int side) {
		float barSpacing = 0f, // spacing between thick and thing bar
		circleOffset = 0f, // spacing between circle and bars
		symOffset = 0f; // fine tuning for symbol positioning.

		if (side == 0) {
			barSpacing = 2.7f;
			circleOffset = 5f;
		} else if (side == 1) {
			barSpacing = -2.7f;
			circleOffset = -5f;
			symOffset = 2f * spacing; // offset is set to align end offset with
										// begin offset when
			// they are back to back, its a scalar multiple of spacing to ensure
			// that
			// they remain aligned even after a spacing change.
		}
		drawThickVertBar(x + symOffset, y);
		drawThinVertBar(x + barSpacing + symOffset, y);
		drawCircles(x + symOffset, y, circleOffset);
	}

	/**
	 * @param x
	 *            is horz position
	 * @param y
	 *            is the vertical position of the third bar.
	 */
	private static void drawThickVertBar(float x, float y) {
		// draw line upto bar
		canvas.lineTo(x, y);
		canvas.stroke();

		canvas.moveTo(x, y + barSpacing * 3);
		canvas.lineTo(x, y - barSpacing * 2);
		canvas.setLineWidth(2.4f);
		canvas.stroke();
		canvas.moveTo(x, y);
		canvas.setLineWidth(0.3f);
	}

	/**
	 * @param x
	 *            is horz position
	 * @param y
	 *            is the vertical position of the third bar.
	 */
	private static void drawThinVertBar(float x, float y) {
		// draw line upto bar
		canvas.lineTo(x, y);
		canvas.stroke();

		// draw bar
		canvas.moveTo(x, y + barSpacing * 3);
		canvas.lineTo(x, y - barSpacing * 2);
		canvas.stroke();

		// set position back
		canvas.moveTo(x, y);
	}

/******************************************************************************
 * ****************************************************************************	
 * ****************************************************************************
 * ********************ALL BELOW IS FOR ARRANGING THE STAVES*******************
 */
	/*
	 * Returns the big stave. Precondition: makeBigStave has be executed
	 * successfully.
	 */
	public static String[] getBigStave() {
		return bigStave;
	}

	/*
	 * Makes one big stave with all measures on it.
	 */
	public static boolean makeBigStave(String body) {
		String[] staves = seperateStaves(body);		
		/*// debug
		for(int i = 0; i < staves.length; i++)
		{
			for(int j = 0; j < staves[i].length(); j++){
				System.out.print(staves[i].charAt(j));
			}
			System.out.println();
		}
		*/
		String[] newStaves = removeConsecutiveBars(staves);
		/*  //debug
		for(int i = 0; i < newStaves.length; i++)
		{
			for(int j = 0; j < newStaves[i].length(); j++){
				System.out.print(newStaves[i].charAt(j));
			}
		}
		*/
		//String[] evenNewerStaves = removeWhiteSpace(newStaves);
		bigStave = concatStaves(newStaves);
		return true;
	}

	/** TODO
	 * Removes all whitespace from the bigStave.
	 * @param newStaves
	 * @return
	 */
	private static String[] removeWhiteSpace(String[] newStaves) {
		
		stavsNoWhtSpace = new String[newStaves.length];
		for (int i = 1; i < newStaves.length; i++) {
			String[] lines = splitLines(newStaves[i]);
			
			for (int j = 0; j < lines.length; j++) {
				String[] splits = lines[j].split("\\s+");  // 1 or more white space chars
				
			}
			//newStavesNoWhiteSpace[i] = buildStave(lines);
		}
		return null;
	}

	/**
	 * Separates all the staves into they're own respective strings.
	 * 
	 * Precondition: is that all staves are separated by two or more consecutive new
	 * line characters. 
	 * 
	 * Post: Returns an array of Strings. Each array is a
	 * stave.
	 * 
	 */
	public static String[] seperateStaves(String s) {
		
		// note it also has to handle the Windows version of new line character as well as unix.
		String[] staves = s.split("(\n|\r\n){2,}");
		
		return staves;
	}

	/*
	 * Takes a string of staves and removes consecutive vertical bars. The first
	 * stave does not have it's first vertical bar removed. All bars afterwards
	 * have they're first vertical bars removed.
	 * 
	 * Programmer: An array list was not used because the number of staves
	 * remains the same. The needed array size is known beforehand, so thereis
	 * no need for the list length to be dynamic.
	 */
	public static String[] removeConsecutiveBars(String[] staves) {
		 consecRemStaves = new String[staves.length];

		consecRemStaves[0] = staves[0]; // doesn't change the first stave.
		int numOfLastBarsOfLastStave = countEndBars(staves[0]);
		for (int i = 1; i < staves.length; i++) {
			String[] lines = splitLines(staves[i]);
			for (int j = 0; j < lines.length; j++) {
				if (numOfLastBarsOfLastStave == 1
						&& countBeginBars(lines[j]) == 1)
					lines[j] = lines[j].substring(1); // remove the first char
				if (numOfLastBarsOfLastStave == 2
						&& countBeginBars(lines[j]) == 2)
					lines[j] = lines[j].substring(2); // remove the first two
														// chars
				if (numOfLastBarsOfLastStave == 1
						&& countBeginBars(lines[j]) == 2)
					lines[j] = lines[j].substring(1); // remove the first char
				if (numOfLastBarsOfLastStave == 2
						&& countBeginBars(lines[j]) == 1)
					lines[j] = lines[j].substring(1); // remove the first char
				if (numOfLastBarsOfLastStave == 3)
					;
				// do nothing.
			}
			consecRemStaves[i] = buildStave(lines);
			numOfLastBarsOfLastStave = countEndBars(staves[i]);
		}
		return consecRemStaves;
	}

	public static ArrayList<String[]> getOrganizedStaves() {

		return organizedStaves;
	}

	/*
	 * concatenates all the staves into one long stave, which is returned as an
	 * array of lines. Each line ends with the new line character.
	 */
	public static String[] concatStaves(String[] staves) {

		
		String[] lines = new String[6];
		int numLines = lines.length;
		Arrays.fill(lines, ""); // too prevent stings being inialized to "null"
		for (int i = 0; i < numLines; i++) {
			for (int j = 0; j < staves.length; j++) {
				String line = getLine(i, staves[j]);
				lines[i] += line;
				if (line == null)
					System.out.print("null string in stave Concat method");
			}
			// lines[i] += "\n";
		}
		return lines;
	}

	/*
	 * Takes the big Stave and partitions it into 1 or more staves such that
	 * each stave contains the maxumum number of measures while each stave
	 * remains with the boundry of the body width.
	 * 
	 * Pre: - bigStave must be initialized, - the first measure has no more than
	 * 3 vertical bars at the start. Post: The measures allocated to staves in
	 * way such that each stave will contain the maximum number of measures
	 * while staying within the page boundries.
	 */
	public static boolean reorganizeStaves() {
		boolean measureBiggerThanBody = false;
		int lastIndex = 0;
		for (int i = maxVertBars; i < bigStave[1].length(); i++) {
			if (bigStave[1].charAt(i) == '|') {
				staveWidth = (i - lastIndex) * spacing;
			}
			// check if current measure is within bounds.
			if (staveWidth > bodyWidth) {
				int temp = i;
				i = indexOfLastMeasure(lastIndex, i);
				if (i == -1) {
					spacing -= 0.5f; // measure is too large to fit in bounds
					i = temp - 1; // decreases spacing and tries again from same point
					continue; // TODO: redo the whole thing with new spacing.
				}
				cutStave(lastIndex, i + 1);
				if (bigStave[2].charAt(i + 1) == '*')
					lastIndex = i - 1;
				else
					lastIndex = i;
				staveWidth = 0;
				organizedStaves.add(staveBuffer);
			}
			makeLastStave(lastIndex, i);
		}
		return false;
	}

	// the end of the big stave is reached
	// but the current set of measures is still in bounds
	// then make a new stave containing all remaining measures.
	private static void makeLastStave(int lastIndex, int i) {
		if (i + 1 >= bigStave[0].length()) {
			cutStave(lastIndex, bigStave[0].length());
			organizedStaves.add(staveBuffer);
		}
	}

	/*
	 * returns the index of the last measure. If no last measure is found or the
	 * measure is to large to fit within bounds then -1 is returned. More
	 * specifically returns the index of the last vertical bar at the end of the
	 * last measure.
	 * @param j The current index of the bigStave, (should be the first vertical bar
	 * of the current measure.
	 * @param lastIndex The in
	 * 
	 * Pre: 
	 */
	public static int indexOfLastMeasure(int lastIndex, int j) {
		int index = 0;
		for (int i = j - maxVertBars; i >= lastIndex; i--) {
			if (bigStave[3].charAt(i) == '|') {
				index = i;
				break;
			}

		}
		// now check that the space between the last index and the current one is 
		// lest the the body width.
		if (false == isInBounds(index, j)) {
			return -1;
		}
		return index;
	}

	public static boolean isInBounds(int lastIndex, int curIndex) {
		return (curIndex - (lastIndex - maxVertBars)) * spacing < bodyWidth;
	}

	/*
	 * Pre: bigStave must be initialized, lastindex must be less than curindex
	 * and both must be naturalk numbers. Post: Returns the former portion of
	 * the stave which does fit on the current stave being drawn.
	 */
	public static void cutStave(int lastIndex, int curIndex) {
		staveBuffer = new String[6];
		Arrays.fill(staveBuffer, "");
		for (int i = 0; i < bigStave.length; i++) {
			staveBuffer[i] = bigStave[i].substring(lastIndex, curIndex);
		}

	}

	public static String[] getStaveBuffer() {
		return staveBuffer;
	}

	/*
	 * Post: returns the repeat number embedded in in double vertical bars, if no
	 * number is embedded then returns -1.
	 */
	public static int getRepeatNum(String line1, String line2) {
		int numBars1 = countEndBars(line1);
		int numBars2 = countEndBars(line2);
		if (numBars1 != numBars2) {
			return Character.getNumericValue(line1.charAt(line1.length() - 1));
		} else {
			return -1;
		}
	}

	/*
	 * counts the number of consecutive vertical bars counting backwards from
	 * the end of the last line. Pre: All vertical bars are located within the
	 * last 5 indices of the line. and all measure have a length greater than
	 * 10. Post: Returns the number of ending vertical bars.
	 */
	public static int countEndBars(String line) {
		int j = line.length() - 1;
		int count = 0;
		
		for (int i = 0; i < 5 && (j-i) >= 0; i++) {
			if (line.charAt(j - i) == '|')
				count++;
		}
		return count;
	}

	/*
	 * counts the number of consecutive vertical bars counting from the start of
	 * the first line. Pre: All vertical bars are located within the first 5
	 * indices of the line. and all measure have a length greater than 10. Post:
	 * Returns the number of beggining vertical bars.
	 */
	public static int countBeginBars(String line) {
		int count = 0;
		for (int i = 0; i < 5; i++) {
			if (line.charAt(i) == '|')
				count++;
		}
		return count;
	}

	/*
	 * Takes an array of lines and builds a stave from it. Pre: all lines are
	 * the same length.
	 */
	private static String buildStave(String[] lines) {
		String stave = "";
		for (int i = 0; i < lines.length; i++) {
			stave += lines[i] + '\n';
		}
		return stave;
	}

	/*
	 * Splits a stave into it's lines. Pre: Each line is ended with \n Post: An
	 * array of lines for the given stave.
	 */
	private static String[] splitLines(String stave) {
		lines = stave.split("(\n)");
		return lines;
	}

	/*
	 * returns the ith line of the given string where 0 is the first line. If
	 * there there is no ith line null is returned.
	 */
	public static String getLine(int i, String s) {
		String[] lines = s.split("(\n|\r\n)");
		if (lines.length <= i)
			return "null";
		else
			return lines[i];

	}

	/*
	 * Counts the number of lines in a string.
	 */
	public static int countLines(String s) {

		int count = 1;
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == '\n')
				count++;

		return count;
	}

	/*
	 * Draws a stylish header.
	 */
	public static boolean drawHeader(PdfContentByte canvas, String[] header)
			throws DocumentException, IOException {
		canvas.beginText();
		BaseFont bf = BaseFont.createFont();
		canvas.setFontAndSize(bf, 32);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, header[0], 275, 780, 0);
		canvas.setFontAndSize(bf, 14);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, header[1], 275, 760, 0);
		canvas.endText();
		return true;
	}

}
