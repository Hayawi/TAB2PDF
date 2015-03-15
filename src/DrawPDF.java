import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class DrawPDF {

	static String TITLE = "";
	static String SUBTITLE = "";
	static String SPACING;

	private final static int STAFFHEIGHT = 35;
	private final static int HEIGHTSPACING = 7;
	private final static int STARTMARGIN = 30;
	private final static float LINEWIDTH = 550f;

	private static float horizontalShift = 0;
	private static float pageLocationX = 0;
	private static int pageLocationY = 750;
	private static int currentLine = 0;
	private static float spacing = 4.4f;
	private static float remainingLineSpace = LINEWIDTH;
	private static int pageLine = 0;
	private static PdfContentByte cb;
	private static int fontSize = 8;
	private static Document document = new Document(PageSize.A4);
	private static float previousNoteX = 0;
	private static int previousNoteY = 0;
	private static int previousNoteLine = 0;

	public static void main(String[] args) throws DocumentException,
			IOException {
		Tablature tab = new Tablature(
				"C:/Users/JH/Documents/CSE2311/Testproj/src/packag/moonlightsonata.txt",
				"C:/New folder/test2.pdf");
		tab.draw();

	}

	@SuppressWarnings("unchecked")
	public static ByteArrayOutputStream writePDFInMemory(Tablature tab)
			throws IOException {

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
			PdfWriter writer = PdfWriter.getInstance(document, output);

			document.open();
			cb = writer.getDirectContent();
			drawHeader(tab.getTitle(), tab.getSubtitle());
			ArrayList<Measure> staves = tab.getMeasures();
			spacing = tab.getSpacing();

			drawHorizontalLines(cb, pageLocationY); // FIX THIS SHIT

			for (Measure measure : staves)
				processMeasure(measure);

			document.close(); // no need to close PDFWriter?
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	public static void writePDF(Tablature tab) throws IOException {

		FileOutputStream output = new FileOutputStream(tab.getOutputPath());

		try {
			PdfWriter writer = PdfWriter.getInstance(document, output);

			document.open();
			cb = writer.getDirectContent();
			drawHeader(tab.getTitle(), tab.getSubtitle());
			ArrayList<Measure> staves = tab.getMeasures();
			spacing = tab.getSpacing();

			drawHorizontalLines(cb, pageLocationY); // FIX THIS SHIT

			for (Measure measure : staves)
				processMeasure(measure);

			document.close(); // no need to close PDFWriter?
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void drawHeader(String title, String subtitle)
			throws DocumentException, IOException {
		cb.beginText();
		BaseFont bf = BaseFont.createFont();
		cb.setFontAndSize(bf, 32);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, title, 300, 800, 0);
		cb.setFontAndSize(bf, 14);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, subtitle, 300, 780, 0);
		cb.endText();
	}

	public static void processMeasure(Measure measure)
			throws DocumentException, IOException {
		float barLength = 0;
		boolean firstLine = true;

		for (ArrayList<String> tokens : measure.getTokens()) {
			if (firstLine) {
				barLength = measure.getLength() * spacing; // get the length of
															// the measure
				if (barLength < remainingLineSpace) {
					remainingLineSpace = remainingLineSpace - barLength;
				} else { // won't fit on the current line, so reset.
					pageLine++;
					remainingLineSpace = LINEWIDTH - barLength;
					pageLocationY = pageLocationY - (2 * STAFFHEIGHT);
					if (pageLine == 9) {
						newPage();
						cb.setLineWidth(.5F);
						pageLocationY = 810;
					}
					pageLocationX = 0;
					drawHorizontalLines(cb, pageLocationY);
				}
			}
			horizontalShift = pageLocationX; // reset the shift to the beginning
												// of the measure for the next
												// line.
			draw(tokens, firstLine, cb);
			firstLine = false;
			currentLine++;
		}
		pageLocationX += barLength;
		currentLine = 0;
	}

	public static void draw(ArrayList<String> tokens,
			boolean firstLineOfMeasure, PdfContentByte cb)
			throws DocumentException, IOException {
		/*
		 * when we reach a |, we'll probably end up drawing it 5 times so let's
		 * just keep the y co-ordinate at the same spot. (fix later) when we
		 * reach a * we'll draw an extra line when we reach the end of a line,
		 * and it ends with a -, for simplicity we'll just not draw anything and
		 * fix after. while we draw, we'll increment spacing. before every draw,
		 * we'll take the length of the string in the queue, and multiply its
		 * length by the spacing, and add it to the currentShift.
		 */
		boolean firstVerticalLine = true;
		String previousToken = "";
		String previousNote = "";

		boolean hold = false;
		int index = 0;

		for (String s : tokens) {

			if (s.equals("|")) {
				cb.setLineWidth(0.5F);
				drawVerticalBars(cb, pageLocationY, horizontalShift);
				if (firstVerticalLine) {
					horizontalShift += spacing;
					firstVerticalLine = false;
				}
			} else if (s.equals("||")) {
				if (previousToken.equals("*")) {
					cb.setLineWidth(0.5F);
					drawVerticalBars(cb, pageLocationY, horizontalShift
							- spacing / 2);
					cb.setLineWidth(2F);
				} else
					cb.setLineWidth(0.5F);
				drawVerticalBars(cb, pageLocationY, horizontalShift);
				if (firstVerticalLine) {
					horizontalShift += spacing;
					;
					firstVerticalLine = false;
				}

			} else if (s.equals("|||")) {
				cb.setLineWidth(0.5F);
				for (int i = 0; i < s.length(); i++) {
					drawVerticalBars(cb, pageLocationY, horizontalShift);
					horizontalShift += spacing;
				}
			} else if (s.contains("-"))
				horizontalShift += (s.length() * spacing);
			else if (s.length() == 2) {
				if (s.contains("|")) {
					if (firstVerticalLine)
						horizontalShift += spacing;
					else
						drawRepeat("Repeat " + s.substring(1) + " times");
					firstVerticalLine = false;
				} else {
					drawText(s);
					horizontalShift += (2 * spacing);
				}
			} else if (s.length() == 1) {
				if (s.contains("*")) {
					drawCircle(pageLocationY, horizontalShift);
					if (previousToken.equals("||")) {
						cb.setLineWidth(0.5F);
						drawVerticalBars(cb, pageLocationY, horizontalShift
								- spacing / 2);
						cb.setLineWidth(2F);
						drawVerticalBars(cb, pageLocationY, horizontalShift
								- spacing);
					}
				} else if (s.contains("s")) {
					drawSlash(pageLocationY, horizontalShift);
				} else if (s.contains("h")) {
					hold = true;
				} else if (index == 0 || index == tokens.size())
					;
				else {
					drawText(s);
				}
				horizontalShift += spacing;
			} else if (s.length() == 3) {
				drawText(s.substring(1, 2));
				drawDiamond(horizontalShift + spacing - 1, pageLocationY);
				horizontalShift += (3 * spacing);
			} else if (s.length() == 4) {
				drawText(s.substring(1, 3));
				drawDiamond(horizontalShift + spacing + 1, pageLocationY);
				horizontalShift += (4 * spacing);
			}

			/*
			 * if (hold && Pattern.matches("[0-9]+", s)) { drawHold(s,
			 * previousNoteX, previousNoteY, horizontalShift, pageLocationY);
			 * System.out.println(previousNoteX + " " + previousNoteY);
			 * System.out.println(horizontalShift + " " + pageLocationY);
			 * System.out.println(previousNote + " " + s); hold = false; }
			 */

			if (!s.contains("-"))
				previousToken = s;
			if (Pattern.matches("[0-9]+", s)) { // record location of last note
												// -- only works when same line
												// of the staff.
				previousNote = s;
				previousNoteX = horizontalShift;
				previousNoteY = pageLocationY;
				previousNoteLine = pageLine;
			}
			// System.out.println(currentShift);
			index++; // FIX THIS SHIT LATER
		}

	}

	public static void drawHorizontalLines(PdfContentByte cb, int yStart) {
		cb.setLineWidth((float) 0.5);
		// horizontal lines
		int x1 = 0; // left edge of page
		int x2 = 1000; // right edge of page
		int linesPerBar = 6;

		for (int i = 0; i < linesPerBar; i++) {
			cb.moveTo(x1, pageLocationY - HEIGHTSPACING * i);
			cb.lineTo(x2, pageLocationY - HEIGHTSPACING * i);
		}
		cb.stroke();
	}

	// need to rework parameters
	public static void drawVerticalBars(PdfContentByte cb, float yStart,
			float xShift) {
		cb.moveTo(STARTMARGIN + xShift, yStart);
		cb.lineTo(STARTMARGIN + xShift, yStart - STAFFHEIGHT);
		cb.stroke();
	}

	// add colour option, only really needs to
	// cb.setColour(tabObject.getColour());
	// pageLocationY - 2 needs to have 2 changed to a variable; 2 as an
	// arbitrary number to test shift
	// font style could be changed in the future; easy to change, need to refer
	// to BaseFont API.
	public static void drawText(String string) throws DocumentException,
			IOException {

		BaseFont bf = BaseFont.createFont();
		Font font = new Font(bf, fontSize);
		Chunk c = new Chunk(string, font);
		c.setBackground(BaseColor.WHITE);
		Phrase phrase = new Phrase(c);

		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, phrase,
				STARTMARGIN + horizontalShift, (pageLocationY - 3)
						- HEIGHTSPACING * currentLine, 0);
	}

	public static void drawRepeat(String string) throws DocumentException,
			IOException { // get rid of this shit

		BaseFont bf = BaseFont.createFont();
		Font font = new Font(bf, fontSize);
		Chunk c = new Chunk(string, font);
		c.setBackground(BaseColor.WHITE);
		Phrase phrase = new Phrase(c);

		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, phrase,
				STARTMARGIN + horizontalShift - 6 * spacing, pageLocationY
						+ HEIGHTSPACING, 0);
	}

	private static void drawSlash(float yStart, float xShift) {
		float x = STARTMARGIN + xShift;
		float y = yStart - currentLine * HEIGHTSPACING;
		float size = spacing / 2;
		cb.setLineWidth(0.5F);
		cb.moveTo(x - size, y - size);
		cb.lineTo(x + size, y + size);
		cb.stroke();
	}

	public static void drawHold(String letter, float xStart, float yStart,
			float xEnd, float yEnd) {
		float x1 = STARTMARGIN + xStart;
		float x2 = STARTMARGIN + ((xStart + xEnd) / 2);
		float x3 = STARTMARGIN + xEnd;
		cb.moveTo(x1, yStart);

		cb.curveTo(x2, yStart + 3, x3, yEnd);
		cb.stroke();
	}

	public static void drawDiamond(float xStart, float yStart) {
		cb.setColorFill(BaseColor.WHITE);
		cb.setLineWidth(0.5F);
		float x = STARTMARGIN + xStart;
		float y = yStart - currentLine * HEIGHTSPACING;
		cb.moveTo(x, y);
		cb.lineTo(x + 2, y + 2);
		cb.lineTo(x + 4, y);
		cb.lineTo(x + 2, y - 2);
		cb.closePathFillStroke();
	}

	public static void newPage() {
		pageLine = 0;
		document.newPage();
	}

	private static void drawCircle(float yStart, float xShift) {
		cb.setLineWidth(2F);
		cb.circle((float) (STARTMARGIN + xShift), (float) yStart - currentLine
				* HEIGHTSPACING, 1.0F);
		cb.stroke();
	}

}