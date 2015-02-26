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

	/** Specifies that a particular pattern has been match, but no 
	 * drawing procedure for the pattern has been implemented yet.*/
	private static final String NOT_IMPLEMENTED = " NOT IMPLEMENTED!";

	// stave storage and reorganization
	private static String[] staves;	  // stores all the seperate staves, each as a single string.
	private static String[] bigStave; // stores all measures on one big stave
	static String[] staveBuffer; // stores a tab ready to be placed
	private static ArrayList<String[]> organizedStaves = new ArrayList<String[]>();
	private static String[] consecRemStaves; // stave with consecutive vertical
												// removed.
	private static String[] lines; // for split lines method

	// page attributes // probably will be move into Tablature object upon
	// refactoring
	// along with several methods
	private static float spacing = 5f;
	private static int bodyWidth = 560; // width of body in pixels.
	private static int margin = 40;
	private static int topOfPage = 733; // the y position to start drawing stuff
			// on each page.
	private static float lineWidth = 0.3f;
	private static int defaultFontSize = 8;

	// details
	// used in the algorithm for checking if the stave under construction will
	// fit on the page.
	private static float staveWidth = 0f;
	private static int maxVertBars = 3;
	private static float vertBarSpacing = 4f;
	private static BaseFont bf;
	private static PdfContentByte canvas;
	private static float barSpacing = 7f;
	private static Document document;

	/**
	 * 
	 * @return The current spacing being used.
	 * 
	 */
	public static float getSpacing() {
		return spacing;
	}

	/**
	 * Returns the current font size used for printing the notes.
	 */
	public static int getDefaultFontSize() {
		return defaultFontSize;
	}

	/**
	 * Used for setting the spacing between the notes. The spacing 
	 * has a maximum setting of 80 since anything more than that would not
	 * allow a measure to have 5 spaces between vertical bars. 
	 */
	public static void setSpacing(float spacing) {
		if(spacing > 80f){
			spacing = 80f;
		}
		Creater.spacing = spacing;
	}

	/**
	 * Sets the font size to be used for printing notes. The default is 8.
	 * @param defaultFontSize
	 */
	public static void setDefaultFontSize(int defaultFontSize) {
		Creater.defaultFontSize = defaultFontSize;
	}

	/** The method which is called to produce a new PDF tablature. It calls all
	 * the methods necessary to create a new PDF. It should only be called when 
	 * creating a new PDF and not when modifying an exiting one for performance 
	 * reasons.
	 * 
	 * @param document A reference to the iText pdf document.
	 * @param contentByte The canvas which is being drawn to.
	 * @param body The tablature of notes, measures, staves and musical symbols.
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void createTab(Document document, PdfContentByte contentByte,
			String body) throws DocumentException, IOException {
		Creater.document = document;
		String bodySpacesRemoved = body.replaceAll(" ", "-");
		makeBigStave(bodySpacesRemoved);
		canvas = contentByte;
		drawTablature();
	}

	/** drawTablature is responsible for drawing the actual PDF
	 * It is called the first time a PDF is created and for all subsequent 
	 * modifications to the PDF. It has the precondition that the staves 
	 * have already been concatenated into one large stave to be partitioned
	 * into smaller staves which will fit within the width of the page.
	 * 
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void drawTablature() throws DocumentException, IOException {
		
		reorganizeStaves(); // takes the big stave and partitions it taking into
							// account spacing.
		bf = BaseFont.createFont();
		int repeatDetectLineNum = 3;
		String token = "";
		float xPosTok = 0f; // pos of char relative to end of token.
		// for drawing diamond
		float diamondSpace = 6f;
		float diamondSideLength = 3f;
		float diamondWidth = diamondSideLength * 1.4142f;

		float yTune = -2.5f;// tunes the vertical position of the digits drawn.
		canvas.setLineWidth(lineWidth);
		int xPos = margin, yPos = topOfPage, fontWidth = 5;

		/*
		 * This beefy triple for loop carries out all the drawing by iterating
		 * along each line parsing tokens 5 characters at a time and matching
		 * them to specific kinds of symbols given by the rugular expressions
		 * within the if statements.
		 */
		int staveCount = 0;
		for (String[] stave : getOrganizedStaves()) { // new stave

			staveCount++;
			// starts a new page after every 9 staves.
			if (staveCount % 10 == 0) {
				document.newPage();
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
					
					/* FOR DRAWING MUSICAL SYMBOLS/DIGITS */
					// single digit volume swell
					if (token.matches(".[-*]<[0-9]>")) {
						xPosTok = calcPosRelToEnd(1, xPos);
						singleDigitLineSurround(xPosTok, yPos, fontWidth);
						String text = token.substring(3, 4);
						drawText(yTune, xPosTok, yPos, text, defaultFontSize);
						drawDiamond(xPosTok, yPos, diamondSpace, diamondWidth);
						// System.out.println(token);
					}
					// double digit volume swell
					else if (token.matches(".<[0-9]{2}>")) {
						xPosTok = calcPosRelToEnd(3, xPos);
						twoDigitLineSurround(xPosTok, yPos, fontWidth);
						String text = token.substring(2, 4);
						drawText(yTune, xPosTok, yPos, text, defaultFontSize);
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
						drawText(yTune, xPosTok, yPos, text, defaultFontSize);
						// System.out.println(token + NOT_IMPLEMENTED);
					}
					// single digit
					else if (token.matches(".[\\|\\*-][0-9][\\*\\|-].")) {
						xPosTok = calcPosRelToEnd(3, xPos);
						singleDigitLineSurround(xPosTok, yPos, fontWidth);
						String text = token.substring(2, 3);
						drawText(yTune, xPosTok, yPos, text, defaultFontSize);
						// System.out.println(token);
					} else if (token.matches(".[0-9][h][0-9].")) {
						// TODO: first digit is drawn twice because it's
						// position is different within the
						// token.
						curveAndLetter(token, yTune, xPos, yPos, fontWidth, "h");
					}
					// draw pull off, across bar,
					else if (token.matches("[0-9][-][\\|][p][0-9]")) {
						// TODO: write a better regex
						// 3-|p0
						drawPullOff(token, yTune, xPos, yPos, fontWidth, "p");
						// System.out.println(" " + token + NOT_IMPLEMENTED);
					}

					/* FOR DRAWING BARS */
					// repeat start
					if (token.matches("\\|\\|\\*..")
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
						// hammer with single digits
					}
					// first vertical bar is a special case
					else if (token.matches("\\|[\\*-]...") && i == 3 && j < 5) {
						xPosTok = calcPosRelToEnd(5, xPos);
						drawThinVertBar(xPosTok, yPos);
						// System.out.println( " " +token + NOT_IMPLEMENTED);
					}
					// first vertical bar(s) is a special case
					else if (token.matches("\\|{2}[\\*-]..") && i == 3 && j < 5) {
						xPosTok = calcPosRelToEnd(5, xPos);
						drawThinVertBar(xPosTok, yPos);
						drawThinVertBar(xPosTok + vertBarSpacing, yPos);
						// System.out.println( " " +token + NOT_IMPLEMENTED);
					}
					// vertical bar middle case
					else if (token.matches(".[-]\\|[-].") && i == 3 && j > 4) {
						xPosTok = calcPosRelToEnd(3, xPos);
						drawThinVertBar(xPosTok, yPos);
						// System.out.println( " " +token + NOT_IMPLEMENTED);
					}
					// vertical bar end of line case
					else if (token.matches("...[-\\|]\\|") && i == 3
							&& j == stave[i].length() - 1) {
						xPosTok = calcPosRelToEnd(1, xPos);
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
					// draw "repeat x number of times" embedded in bars.
					else if (i == 0 && token.matches("....[0-9]")
							&& stave[1].charAt(j) == '|') {
						String num = token.substring(4, 5);
						xPosTok = calcPosRelToEnd(8, xPos);
						// System.out.print("start" + num + "end"); // debug
						String text = "Repeat " + num + " times";
						drawText(10, xPosTok, yPos, text, defaultFontSize);
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

	/** Draws three vertical bars.
	 * @param xPosTok The position of the first bar relative 
	 * to the last character in the token, multiplied by the line 
	 * spacing.
	 * @param yPos The vertical position of the bar.
	 */
	private static void drawTripleThinBars(float xPosTok, int yPos) {
		
		drawThinVertBar(xPosTok, yPos);
		drawThinVertBar(xPosTok + vertBarSpacing, yPos);
		drawThinVertBar(xPosTok + vertBarSpacing * 2, yPos);
	}

	/**
	 * @param token The token to be parsed, in version 1.0 this is 5 character long.
	 * @param yTune Used to tune the vertical position relative to the original y position.
	 * @param xPos The horizontal position of the symbol to be drawn.
	 * @param yPos The original vertical position of the symbol to be drawn.
	 * @param fontWidth The width of the character.  Not exactly the actually width,
	 * there are no units associated with this number currently.  It is for tweaking
	 * the position where the bar line stops being drawn, and then starts being drawn
	 * again after the symbol is drawn.
	 */
	private static void curveAndLetter(String token, float yTune, int xPos,
			int yPos, int fontWidth, String letter) {
		float xPosTok;

		// (".[0-9][h][0-9].")

		// draw first digit
		xPosTok = calcPosRelToEnd(4, xPos);
		singleDigitLineSurround(xPosTok, yPos, fontWidth);
		String text = token.substring(1, 2);
		drawText(yTune, xPosTok, yPos, text, defaultFontSize);

		// draw curve
		canvas.moveTo(xPosTok, yPos + barSpacing / 1.5f);
		xPosTok = calcPosRelToEnd(3, xPos);
		canvas.curveTo(xPosTok, yPos + barSpacing * 1.25f,
				calcPosRelToEnd(1, xPos), yPos + barSpacing / 1.5f);
		canvas.stroke();

		// draw little letter above curve
		int fontSize = 5;
		int xTune = 3;
		xPosTok = calcPosRelToEnd(3, xPos);
		drawText(-yTune * 3, xPosTok + xTune, yPos, letter, fontSize);
		canvas.moveTo(xPosTok, yPos);

		// move pencil to just after first char.
		xPosTok = calcPosRelToEnd(4, xPos);
		canvas.moveTo(xPosTok + fontWidth / 2, yPos);

		// draw last digit
		xPosTok = calcPosRelToEnd(1, xPos);
		singleDigitLineSurround(xPosTok, yPos, fontWidth);
		text = token.substring(3, 4);
		drawText(yTune, xPosTok, yPos, text, defaultFontSize);
	}

	/**
	 * @param token The token to be parsed, in version 1.0 this is 5 character long.
	 * @param yTune Used to tune the vertical position relative to the original y position.
	 * @param xPos The horizontal position of the symbol to be drawn.
	 * @param yPos The original vertical position of the symbol to be drawn.
	 * @param fontWidth The width of the character.  Not exactly the actually width,
	 * there are no units associated with this number currently.  It is for tweaking
	 * the position where the bar line stops being drawn, and then starts being drawn
	 * again after the symbol is drawn.
	 */
	private static void drawPullOff(String token, float yTune, int xPos,
			int yPos, int fontWidth, String letter) {
		float xPosTok;

		// (".[0-9][h][0-9].")

		// draw first digit
		xPosTok = calcPosRelToEnd(5, xPos);
		singleDigitLineSurround(xPosTok, yPos, fontWidth);
		String text = token.substring(0, 1);
		drawText(yTune, xPosTok, yPos, text, defaultFontSize);

		// draw curve
		canvas.moveTo(xPosTok, yPos + barSpacing / 1.5f);
		xPosTok = calcPosRelToEnd(3, xPos);
		canvas.curveTo(xPosTok, yPos + barSpacing * 1.25f,
				calcPosRelToEnd(1, xPos), yPos + barSpacing / 1.5f);
		canvas.stroke();

		// draw little letter above curve
		int fontSize = 5;
		int xTune = 3;
		xPosTok = calcPosRelToEnd(3, xPos);
		drawText(-yTune * 3, xPosTok + xTune, yPos, letter, fontSize);
		canvas.moveTo(xPosTok, yPos);

		// move pencil to just after first char.
		xPosTok = calcPosRelToEnd(5, xPos);
		canvas.moveTo(xPosTok + fontWidth / 2, yPos);

		// draw last digit
		xPosTok = calcPosRelToEnd(0, xPos);
		singleDigitLineSurround(xPosTok, yPos, fontWidth);
		text = token.substring(4, 5);
		drawText(yTune, xPosTok, yPos, text, defaultFontSize);
	}

	/** Finds the position relative to the last character of a token,
	 * taking line spacing into account.
	 * @param posFromEnd The number of characters between the last character
	 * and the character whose position we want to find. 
	 * @param xPos The position of the last character of the token.
	 * @return The horizontal position of the character whose position
	 * relative to the end of the token is scaled by the spacing for 
	 * document.
	 */
	private static float calcPosRelToEnd(int posFromEnd, int xPos) {
		return xPos - spacing * posFromEnd;
	}

	/** Draws the left margin of the document. Has the precondition 
	 * that the x Position argument is equal to the margin width.
	 * 
	 * @param xPos The current horizontal position of the pencil.
	 * @param yPos The current vertical position of the pencil.
	 */
	private static void drawLeftMargin(int xPos, int yPos) {
		canvas.lineTo(xPos - margin, yPos);
		canvas.stroke();
		canvas.moveTo(xPos, yPos);
	}

	/** Draw the lagato slide, which is a digit followed by a 
	 * slash through the barline, followed by another digit.
	 * @param token The 5 character token (as of version 1.0)
	 * @param yTune  Tweaks the vertical position with which the symbols are drawn.
	 * @param xPosTok  The position relative to the end of the token
	 * @param yPos The y position of the pencil
	 * @param fontWidth The width of the character.  Not exactly the actually width,
	 * there are no units associated with this number currently.  It is for tweaking
	 * the position where the bar line stops being drawn, and then starts being drawn
	 * again after the symbol is drawn.
	 */
	private static void drawLagatoSlide(String token, float yTune,
			float xPosTok, int yPos, int fontWidth) {
		singleDigitLineSurround(xPosTok, yPos, fontWidth);

		// draw first digit
		String text = token.substring(2, 3);
		drawText(yTune, xPosTok, yPos, text, defaultFontSize);

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

	/** Helper method for the legato slide methods.  It draws the slash which 
	 * goes through the barline.
	 * @param xPosTok The position of th eslash relative to the end of the token.
	 * @param yPos The vertical position of the pencil.
	 */
	private static void drawSlash(float xPosTok, int yPos) {
		canvas.lineTo(xPosTok + spacing, yPos);
		canvas.stroke();
		canvas.moveTo(xPosTok - 1.5f + spacing, yPos - 1.5f);
		canvas.lineTo(xPosTok + 1.5f + spacing, yPos + 1.5f);
		canvas.stroke();
		canvas.moveTo(xPosTok + spacing, yPos);
	}

	/** Draws the diamond shape for volume swells. 
	 * @param xPosTok The position of the diamond relative to the end of the token.
	 * @param yPos The vertical position of the pencil.
	 * @param diamondSpace the length of the line between the digit to be drawn 
	 * and the left edge of the the diamond to be drawn. Increasing this value moves the 
	 * diamond to the right, while decreasin it moves the diamond closer to the left. 
	 * @param diamondWidth The length of the diamond going to the left side to the right side
	 *  it is unit-less and is meant for tweaking the overall size of the diamond.
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

	/**
	 * Draws text to the specified position.
	 * 
	 * @param yTune
	 *            For fine tuning the vertical position of the text relative to
	 *            the pencil coordinates
	 * @param xPos
	 *            The pencils current horizontal coordinates
	 * @param yPos
	 *            The pencils current vertical coordinates
	 * @param text
	 *            The text to be written
	 * @param fontSize
	 *            The desired font size.  Default is 8
	 */
	private static void drawText(float yTune, float xPos, int yPos,
			String text, int fontSize) {
		canvas.beginText();
		canvas.setFontAndSize(bf, fontSize);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, text, xPos, yPos
				+ yTune, 0);
		canvas.endText();
	}

	/**
	 * Used for drawing barlines around double digits.
	 * @param xPosTok The position of the first digit relative to the second one.
	 * @param yPos The vertical position of the pencil.
	 * @param fontWidth The width of the character.  Not exactly the actually width,
	 * there are no units associated with this number currently.  It is for tweaking
	 * the position where the bar line stops being drawn, and then starts being drawn
	 * again after the symbol is drawn.
	 */
	private static void twoDigitLineSurround(float xPosTok, int yPos,
			int fontWidth) {
		canvas.lineTo(xPosTok - fontWidth, yPos);
		canvas.stroke();
		canvas.moveTo(xPosTok + fontWidth, yPos);
	}

	/**
	 * Draws dots at the specified x and y coordinates.
	 * @param offset The offset moves the dots to the right for positive values 
	 * and to the left for negative values. 
	 * @param xPos the x position of the pencil.
	 * @param y the y position of the pencil.
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
	 * @param side  specifies whether it is a begin or end repeat symbol,
	 *  0 means begin and 1 means end.
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
			symOffset = 1.8f * spacing;
			// offset is set to align end offset with
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
	 * Draws a thick vertical bar at the specified x and y coordinates.
	 * @param x is horizontal position
	 * @param y is the vertical position of the third bar.
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
		canvas.setLineWidth(lineWidth);
	}

	/** Draws a thin vertcal bar at the specified coordinates.
	 * @param x is horizontal position of the bar.
	 * @param y is the vertical position of the third bar.
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

	/*
	 * ALL BELOW IS FOR ARRANGING THE STAVES
	 */
	/**
	 * Returns the big stave. 
	 * Precondition: The big stave has been created 
	 * successfully.
	 */
	public static String[] getBigStave() {
		return bigStave;
	}

	/**
	 * Makes one big stave with all measures on it.
	 * @param body all staves, measures and digits. 
	 */
	public static boolean makeBigStave(String body) {
		// String[] evenNewerStaves = removeWhiteSpace(newStaves);
		String[] staves = seperateStaves(body);
		/*// debugging
		 * for(int i = 0; i < staves.length; i++) { for(int j = 0; j <
		 * staves[i].length(); j++){ System.out.print(staves[i].charAt(j)); }
		 * System.out.println(); }
		 */
		String[] newStaves = removeConsecutiveBars(staves);
		/*  // debugging
		 * for(int i = 0; i < newStaves.length; i++) { for(int j = 0; j
		 * < newStaves[i].length(); j++){
		 * System.out.print(newStaves[i].charAt(j)); } }
		 */
		bigStave = concatStaves(newStaves);
		return true;
	}

	/**
	 * Separates all the staves into seperate strings.
	 * @param s The whole body which contains all staves, measures and symbols.
	 * Precondition: All staves are separated by two or more consecutive
	 * new line characters. 
	 * Post: Returns an array of Strings where each array is a stave.
	 */
	public static String[] seperateStaves(String s) {

		// note it also has to handle the Windows version of new line character
		// as well as unix.
		staves = s.split("(\n|\r\n){2,}");
		return staves;
	}

	/**
	 * Takes a array of staves and removes consecutive vertical bars, so that the 
	 * big stave is a valid musical stave.   For example:
	 *  |-----|----|----|-----|
	 *  
	 *  instead of...
	 *  |-----|----||----|-----| when after conctenating staves together.
	 *
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
					lines[j] = lines[j].substring(2); // remove the first two chars
				if (numOfLastBarsOfLastStave == 1
						&& countBeginBars(lines[j]) == 2)
					lines[j] = lines[j].substring(1); // remove the first char
				if (numOfLastBarsOfLastStave == 2
						&& countBeginBars(lines[j]) == 1)
					lines[j] = lines[j].substring(1); // remove the first char
				if (numOfLastBarsOfLastStave == 3)
					;
				// do nothing , no measures after, end of song.
			}
			consecRemStaves[i] = buildStave(lines);
			numOfLastBarsOfLastStave = countEndBars(staves[i]);
		}
		return consecRemStaves;
	}

	/**
	 * @return The list of staves partitioned in way that
	 * they will fit on the page.
	 */
	public static ArrayList<String[]> getOrganizedStaves() {

		return organizedStaves;
	}

	/**
	 * Concatenates all the staves into one long stave, which is returned as an
	 * array strings, each string is a separate line of the stave. 
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
			// lines[i] += "\n";  // no need to use newline character, the array index is enough to reference distinct lines.
		}
		return lines;
	}

	/**
	 * Takes the big Stave and partitions it into 1 or more staves such that
	 * each stave contains the maximum number of measures, while each stave
	 * remains with the boundary of the body width.
	 * 
	 * Preconditon: The big stave must be initialized, 
	 * 	- the first measure has no more than 3 vertical bars at the start.
	 */
	public static boolean reorganizeStaves() {
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
					i = temp - 1; // decreases spacing and tries again from same
									// point
					continue; // TODO: add more fineness to spacing changes
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

	/** If end of the big stave has been reached
	 *  but the current set of measures is still in bounds
	 * then make a new stave containing all remaining measures.
	 */ 
	private static void makeLastStave(int lastIndex, int i) {
		if (i + 1 >= bigStave[0].length()) {
			cutStave(lastIndex, bigStave[0].length());
			organizedStaves.add(staveBuffer);
		}
	}

	/**
	 * Returns the index of the previous measure. If no previous measure is found or the
	 * current measure is to large to fit within bounds then -1 is returned. More
	 * specifically returns the index of the last vertical bar at the end of the
	 * preceding measure.
	 * 
	 * @param firstBarCurMeas The current index of the bigStave, (should be the first vertical
	 * bar of the current measure.
	 * 
	 * @param lastBarLastMeas The index of the last vertical bar of the preceding measure. 
	 */
	public static int indexOfLastMeasure(int lastBarLastMeas, int firstBarCurMeas) {
		int index = 0;
		for (int i = firstBarCurMeas - maxVertBars; i >= lastBarLastMeas; i--) {
			if (bigStave[3].charAt(i) == '|') {
				index = i;
				break;
			}
		}
		// checks to see if the current measure is in bounds.
		if (false == isInBounds(index, firstBarCurMeas)) {
			return -1;
		}
		return index;
	}

	/** Checks to see if the space between the two indices is within the bounds
	 * of the page.
	 * 
	 * @param lastIndex The index of the last vertical bar of the last measure, or
	 * in the case of being start of the song, the vertical bar at the begging of 
	 * the song.
	 * @param curIndex The first vertical bar of the current measure. 
	 * @return True if the space between the two indices is less than 
	 * desired width of the body.  False otherwise.
	 */
	public static boolean isInBounds(int lastIndex, int curIndex) {
		return (curIndex - (lastIndex - maxVertBars)) * spacing < bodyWidth;
	}

	/** Copies a partition of the big stave into a a stave buffer, to be copied into 
	 * the final list of organized staves. 
	 * @param lastIndex The beginning index where the cut starts.
	 * @param curIndex The last part where the cut ends.
	 * 
	 * Pre-condition: bigStave must be initialized, lastindex must be less than curindex
	 * and both must be natural numbers. 
	 * 
	 * Post: Sets the staveBuffer to a copy of the partition between the 
	 * start and end indices.
	 */
	public static void cutStave(int lastIndex, int curIndex) {
		staveBuffer = new String[6];
		Arrays.fill(staveBuffer, "");
		for (int i = 0; i < bigStave.length; i++) {
			staveBuffer[i] = bigStave[i].substring(lastIndex, curIndex);
		}

	}

	/**
	 * @return The repeat number embedded in in double vertical bars, if
	 * no number is embedded then returns -1.
	 * 
	 * for example:
	 * 
	 *  |2
	 *  ||
	 *  ||
	 *  ||
	 *  
	 *  will return 2.
     *
  	 *  ||
	 *  ||
	 *  ||
	 *  ||
	 *  
	 *	will return -1
	 *
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

	/**
	 * Counts the number of consecutive vertical bars counting backwards from
	 * the end of the last line.
	 * Pre-condition: All vertical bars are located within the
	 * last 5 indices of the line and all measures have a length greater than
	 * 5.
	 * 
	 * @return Returns the number of ending vertical bars.
	 */
	public static int countEndBars(String line) {
		int j = line.length() - 1;
		int count = 0;

		for (int i = 0; i < 5 && (j - i) >= 0; i++) {
			if (line.charAt(j - i) == '|')
				count++;
		}
		return count;
	}

	// TODO:  Change so that it counts vertical bars on the second line instead of the first. 
	// the embedded repeated number will interfere if bars are counted on the first line.
	/**
	 * Counts the number of consecutive vertical bars counting from the start of
	 * the first line. 
	 * Pre-conditions: All vertical bars are located within the first 5
	 * indices of the line. All measure have a length greater than 5.
	 * @return the number of vertical bars at the start of the first line.
	 */
	public static int countBeginBars(String line) {
		int count = 0;
		for (int i = 0; i < 5; i++) {
			if (line.charAt(i) == '|')
				count++;
		}
		return count;
	}

	/**
	 * Takes an array of lines and builds a stave from it. 
	 * Pre-condition: All lines are the same length.
	 */
	private static String buildStave(String[] lines) {
		String stave = "";   // TODO:  local variable should not be returned!  value can be lost!
		for (int i = 0; i < lines.length; i++) {
			stave += lines[i] + '\n';
		}
		return stave;
	}

	/**
	 * Splits a stave into separate lines.
	 *  Pre-condition: Each line is ended with \n
	 *  Post: An array of lines for the given stave.
	 */
	private static String[] splitLines(String stave) {
		lines = stave.split("(\n)");
		return lines;
	}

	/** 
	 * Returns the i'th line of the given string where 0 is the first line. If
	 * there there is no i'th line null is returned.
	 * @param i The line to be removed.
	 * @param s A string of lines.
	 */
	public static String getLine(int i, String s) {
		String[] lines = s.split("(\n|\r\n)"); 
		if (lines.length <= i)
			return "null";
		else
			return lines[i];// TODO: returns a local variable! not good.

	}

	/**
	 * Counts the number of lines in the string.
	 * @param s The string with lines in it.
	 * @return The number of lines within the string.
	 */
	public static int countLines(String s) {

		int count = 1;
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == '\n')
				count++;

		return count; // TODO: returns a local variable! not good.
	}

	/**
	 * Draws a stylish header.
	 * @param canvas Used to draw objects to the PDF.
	 * @param header A array of strings the, first being the title of the song and
	 * the second being the composer.
	 */
	public static boolean drawHeader(PdfContentByte canvas, String[] header)
			throws DocumentException, IOException {
		canvas.beginText();
		BaseFont bf = BaseFont.createFont();
		canvas.setFontAndSize(bf, 32);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, header[0], 275,
				780, 0);
		canvas.setFontAndSize(bf, 14);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, header[1], 275,
				760, 0);
		canvas.endText();
		return true;
	}

}
