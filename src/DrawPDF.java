import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

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
import com.itextpdf.text.pdf.PdfPatternPainter;
import com.itextpdf.text.pdf.PdfWriter;

public class DrawPDF {
	
	private final static int STAFFHEIGHT = 35;
	private final static int HEIGHTSPACING = 7;
	private final static int STARTMARGIN = 30;
	private final static float LINEWIDTH = 550f;
	
	private static float spacing = 5f;
	private static int fontSize = 8;
	private static BaseColor fontColor = BaseColor.BLACK;
	private static String[] previousNote = new String[6];
	private static float[] previousNoteX = new float[6];
	private static int[] previousNoteY = new int[6];
	private static int[] previousNoteLine = new int[6];
	
	private DrawPDF () {
		
	}
	
	public static void writePDF(Tablature tab) throws IOException, DocumentException{
		Document document = new Document();
		
			FileOutputStream output = new FileOutputStream(tab.getOutputPath());
			PdfWriter writer = PdfWriter.getInstance(document, output);
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			drawHeader(cb, tab.getTitle(), tab.getSubtitle(), 
					tab.getTitleFontSize(), tab.getSubtitleFontSize(), tab.getTitleColor(), tab.getSubtitleColor());
			ArrayList<Measure> measures = tab.getMeasures();
			spacing = tab.getSpacing();
			fontColor = tab.getFontColor();
			
			processTablature(cb, measures, tab.numberMeasures(), document);

			document.close(); 
			output.close();
			writer.close();
	}

	public static ByteArrayOutputStream writePDFInMemory(Tablature tab)
			throws IOException, DocumentException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
			PdfWriter writer = PdfWriter.getInstance(document, output);
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			drawHeader(cb, tab.getTitle(), tab.getSubtitle(), 
					tab.getTitleFontSize(), tab.getSubtitleFontSize(), tab.getTitleColor(), tab.getSubtitleColor());
			ArrayList<Measure> measures = tab.getMeasures();
			spacing = tab.getSpacing();
			fontColor = tab.getFontColor();
			processTablature(cb, measures, tab.numberMeasures(), document);
			
			document.close(); 
			output.close();
			writer.close();

		return output;
	}
	
	private static void drawHeader(PdfContentByte cb, String title, String subtitle, 
			int titleFontSize, int subtitleFontSize, BaseColor titleColor, BaseColor subtitleColor) throws DocumentException, IOException {
		cb.beginText();
		BaseFont bf = BaseFont.createFont();
		Font titleFont = new Font(bf, titleFontSize, 0, titleColor);
		Font subtitleFont = new Font(bf, subtitleFontSize, 0, subtitleColor);
		Chunk c = new Chunk(title, titleFont);
		Phrase phrase = new Phrase(c);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, phrase, 300, 780, 0);
		c = new Chunk(subtitle, subtitleFont);
		phrase = new Phrase(c);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, phrase, 300, 760, 0);
		cb.endText();

		
	}
	
	private static void processTablature(PdfContentByte cb, ArrayList<Measure> measures, boolean numberedMeasures, Document document) throws DocumentException, IOException {
		float pageLocationX = STARTMARGIN;
		float remainingSpace = 550F;
		float measureLength = 0;
		float pageLocationY = 725F;
		int pageLine = 0;
		int measureIndex = 1;
		cb.setLineWidth(0.2F);
		drawHorizontalLines(cb, pageLocationY);
		
		for (Measure measure: measures) {
			measureLength = measure.getLength() * spacing;
			if (measure.rightRepeat()) {
				measureLength += 10F + spacing;
			}
			if (measure.leftRepeat()) {
				measureLength += 15F;
			}
			if (measure.endLine()) {
				measureLength += 12F;
			}
			if (measureLength < remainingSpace)
				remainingSpace = remainingSpace - measureLength;
			else {
				pageLine++;
				remainingSpace = LINEWIDTH - measureLength;
				pageLocationY = pageLocationY - (2 * STAFFHEIGHT);
				if (pageLine == 9) {
					newPage(document);
					cb.setLineWidth(.2F);
					pageLocationY = 800;
					pageLine = 0;
				}
				pageLocationX = STARTMARGIN;
				drawHorizontalLines(cb, pageLocationY);
			}
			processMeasure(cb, measure, pageLine, pageLocationX, pageLocationY);
			if (numberedMeasures) {
				drawText(cb, measureIndex + "", pageLocationX, pageLocationY + 5, 5);
				measureIndex++;
			}
			pageLocationX += measureLength;
		}
	}
	
	private static void processMeasure(PdfContentByte cb, Measure measure, int pageLine, float pageLocationX, float pageLocationY) throws DocumentException, IOException {
		int currentLine = 0;
		for (ArrayList<String> tokens : measure.getTokens()) {
			draw(cb, tokens, pageLocationX, pageLocationY, currentLine, pageLine, measure.leftRepeat(), measure.rightRepeat(), measure.endLine(), measure.numberOfRepeats()); 
			currentLine++;
		}
	}

	private static void draw(PdfContentByte cb, ArrayList<String> tokens, float horizontalShift, float pageLocationY, 
			int currentLine, int pageLine, boolean leftRepeat, boolean rightRepeat, boolean endLine, int numberOfRepeats) throws DocumentException, IOException {
		boolean firstVerticalLine = true;
		boolean hold = false;
		String letter = "";
		float verticalShift = pageLocationY - (HEIGHTSPACING * currentLine);
		int index = 0; 
		
		for (String s : tokens) {
			System.out.print(s);
			if (index == 0 && leftRepeat) {
				drawLeftRepeatBar(cb, horizontalShift, pageLocationY);
				horizontalShift += 15F;
				firstVerticalLine = false;
			}
			if (s.equals("|")) {
				drawVerticalBars(cb, horizontalShift, pageLocationY);
				if (firstVerticalLine) {
					horizontalShift += spacing;
					firstVerticalLine = false;
				}
			}
			else if (s.equals("||")) {
				drawVerticalBars(cb, horizontalShift, pageLocationY);
				if (firstVerticalLine) {
					horizontalShift += spacing; ;
					firstVerticalLine = false;
				}
			}  
			else if (s.equals("|||")) {
				for (int i = 0; i < s.length(); i++) {
					drawVerticalBars(cb, horizontalShift, pageLocationY);
					horizontalShift += 4F;
				}
			}
			else if (s.contains("-"))
				horizontalShift += ((float)(s.length()) * spacing);
			else if (s.length() == 2) {
				if (s.contains("|")) {
					if (firstVerticalLine) 	
						horizontalShift += spacing;
					firstVerticalLine = false;
				}
				else {
					drawText(cb, s, horizontalShift, verticalShift, fontSize);
					horizontalShift += 2F * spacing;
				}
			}
			else if (s.length() == 1) {
				if (s.contains("s")) {
					drawSlash(cb, horizontalShift, verticalShift);
				}
				else if (s.contains("h") || s.contains("p")) {
					hold = true;
					letter = s;
				}
				else if (index == 0 || index == tokens.size() || s.contains("*"))
					;
				else
				{
					drawText(cb, s, horizontalShift, verticalShift, fontSize);

				}
				horizontalShift += spacing;
			}
			else if (s.length() == 3) {
				drawText(cb, s.substring(1, 2), horizontalShift, verticalShift, fontSize);
				drawDiamond(cb, horizontalShift + spacing - 1, verticalShift);

				horizontalShift += (3F * spacing);

			}
			else if (s.length() == 4) {
				drawText(cb, s.substring(1, 3), horizontalShift, verticalShift, fontSize);
				drawDiamond(cb, horizontalShift + spacing + 1 , verticalShift);
				horizontalShift += (4F * spacing);

			}
			if (index == tokens.size() - 1 && rightRepeat) {
				drawRightRepeatBar(cb, horizontalShift, pageLocationY);
				if (numberOfRepeats > 1) {
					drawText(cb, "Repeat " + numberOfRepeats + " times", horizontalShift - 6 * spacing, pageLocationY + HEIGHTSPACING * 1.5F, fontSize);
				}
			}
			if (endLine && index == tokens.size() - 1) {
				for (int i = 0; i < 3; i++) {
					drawVerticalBars(cb, horizontalShift, pageLocationY);
					horizontalShift += 4F;
				}
			}

			if (hold && Pattern.matches("[0-9]+", s)) {
				if (previousNoteLine[currentLine] != pageLine) {
					drawHold(cb, letter, previousNoteX[currentLine] - previousNote[currentLine].length() * spacing, (previousNoteY[currentLine]) + 4F, 575, (previousNoteY[currentLine]) + 4F);
					drawHold(cb, letter, 5, verticalShift + 4F, horizontalShift - s.length() * spacing, verticalShift + 4F);
				}
				else {
					drawHold(cb, letter, previousNoteX[currentLine] - previousNote[currentLine].length() * spacing, (previousNoteY[currentLine]) + 4F, horizontalShift - s.length() * spacing, verticalShift + 4F);
				}
				hold = false;
			}  
			if (Pattern.matches("[0-9]+", s)) { 
				previousNote[currentLine] = s;
				previousNoteX[currentLine] = horizontalShift;
				previousNoteY[currentLine] = (int)verticalShift;
				previousNoteLine[currentLine] = pageLine;
				}
			index++; 
		}
		System.out.println();
	}
	

	private static void drawHorizontalLines(PdfContentByte cb, float pageLocationY) {
		int x1 = 0;
		int x2 = 1000;
		int linesPerBar = 6;
		
		for (int i = 0; i < linesPerBar ; i++) {
		cb.moveTo(x1, pageLocationY - HEIGHTSPACING * i);
		cb.lineTo(x2, pageLocationY - HEIGHTSPACING * i);
		}
		cb.stroke();
	}
	
	private static void drawVerticalBars(PdfContentByte cb, float xShift, float yStart) {
		cb.moveTo(xShift, yStart);
		cb.lineTo(xShift, yStart - STAFFHEIGHT);
		cb.stroke();
	}
	
	private static void drawLeftRepeatBar (PdfContentByte cb, float xShift, float yStart) {
		cb.setLineWidth(2F);
		cb.moveTo(xShift, yStart);
		cb.lineTo(xShift, yStart - STAFFHEIGHT);
		cb.stroke();
		drawCircle(cb, xShift + 5F, yStart - HEIGHTSPACING * 2);
		drawCircle(cb, xShift + 5F, yStart - HEIGHTSPACING * 3);
		drawVerticalBars(cb, xShift + 2.5F, yStart);
		cb.setLineWidth(0.2F);
	}
	
	private static void drawRightRepeatBar (PdfContentByte cb, float xShift, float yStart) {
		cb.setLineWidth(2F);
		cb.moveTo(xShift + 10F, yStart);
		cb.lineTo(xShift + 10F, yStart - STAFFHEIGHT);
		cb.stroke();
		drawCircle(cb, xShift + 5F, yStart - HEIGHTSPACING * 2);
		drawCircle(cb, xShift + 5F, yStart - HEIGHTSPACING * 3);
		drawVerticalBars(cb, xShift + 7.5f, yStart);
		cb.setLineWidth(0.2F);
	}
	
	private static void drawText(PdfContentByte cb, String string, float x, float y, int fontSize) throws DocumentException, IOException {
		BaseFont bf = BaseFont.createFont();
		Font font = new Font(bf, fontSize, 0, fontColor);
		Chunk c = new Chunk(string, font);
		c.setBackground(BaseColor.WHITE);
		Phrase phrase = new Phrase(c);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, phrase, x, y - 3, 0);
	}
	
	
	private static void drawSlash(PdfContentByte cb, float x, float y) {
		float size = 2.5f;
		cb.moveTo(x - size, y - size);
		cb.lineTo(x + size, y + size);
		cb.stroke();
	}

	private static void drawHold(PdfContentByte cb, String letter, float xStart, float yStart, float xEnd, float yEnd) throws DocumentException, IOException {
		float x1 = xStart;
		float x2 = ((xStart + xEnd) / 2);
		float x3 = xEnd;
		cb.moveTo(x1, yStart);
		cb.curveTo(x2, yStart + 3, x3, yEnd);
		cb.stroke();
		cb.beginText();
		BaseFont bf = BaseFont.createFont();
		cb.setFontAndSize(bf, 4);
        cb.showTextAligned(Element.ALIGN_CENTER, letter, x2, yStart + 2F, 0);
		cb.endText();
	}
	
	private static void drawDiamond(PdfContentByte cb, float x, float y) {
		cb.setColorFill(BaseColor.WHITE);
		cb.moveTo(x, y);
		cb.lineTo(x + 2, y + 2);
		cb.lineTo(x + 4, y);
		cb.lineTo(x + 2, y - 2);
		cb.closePathFillStroke();
		cb.setColorFill(BaseColor.BLACK);
	}
	
	private static void newPage(Document document) {
		document.newPage();
	}

	private static void drawCircle(PdfContentByte cb, float x, float y) {
		cb.setColorFill(BaseColor.BLACK);
		cb.setLineWidth(1.0F);
		cb.circle(x, y, 1.5F);
		cb.fill();
		cb.setLineWidth(0.2F);
	}
	
	

}
