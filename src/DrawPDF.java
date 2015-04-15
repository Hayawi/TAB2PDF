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
	private static float[] previousNoteX = new float[6];
	private static int[] previousNoteY = new int[6];
	private static int[] previousNoteLine = new int[6];
	
	private DrawPDF () {
		
	}
	
	public static boolean writePDF(Tablature tab) throws IOException{
		Document document = new Document(PageSize.A4);
		
		try {
			FileOutputStream output = new FileOutputStream(tab.getOutputPath());
			PdfWriter writer = PdfWriter.getInstance(document, output);
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			drawHeader(cb, tab.getTitle(), tab.getSubtitle(), tab.getTitleColor(), tab.getSubtitleColor());
			ArrayList<Measure> measures = tab.getMeasures();
			spacing = tab.getSpacing();
			fontColor = tab.getFontColor();
			
			processTablature(cb, measures, document);

			document.close(); 
			output.close();
			writer.close();
//			} catch (FileNotFoundException e) {		
//			if (e.toString().contains("The process cannot access the file because it is being used by another process"))
//		    JOptionPane.showMessageDialog(null, "Please close the file before converting.", "Error",
//		                                    JOptionPane.ERROR_MESSAGE);
//			else if (e.toString().contains("Access is denied")) {
//			    JOptionPane.showMessageDialog(null, "Cannot output file to this directory, please select another directory.", "Error",
//                        JOptionPane.ERROR_MESSAGE);
//			}
//			else if (e.toString().contains("The system cannot find the path specified")) {
//			    JOptionPane.showMessageDialog(null, "The output directory does not exist.", "Error",
//                        JOptionPane.ERROR_MESSAGE);
//			}
//			else {
//			    JOptionPane.showMessageDialog(null, e, "Error",
//                        JOptionPane.ERROR_MESSAGE);
//			}
			} catch (DocumentException e) {
			    JOptionPane.showMessageDialog(null, e, "Error",
                        JOptionPane.ERROR_MESSAGE);
		}
		return true;
	}

	public static ByteArrayOutputStream writePDFInMemory(Tablature tab)
			throws IOException, DocumentException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
//		try {
			PdfWriter writer = PdfWriter.getInstance(document, output);
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			drawHeader(cb, tab.getTitle(), tab.getSubtitle(), tab.getTitleColor(), tab.getSubtitleColor());
			ArrayList<Measure> measures = tab.getMeasures();
			spacing = tab.getSpacing();
			fontColor = tab.getFontColor();
			processTablature(cb, measures, document);
			
			document.close(); 
			output.close();
			writer.close();
//		} catch (FileNotFoundException e) {
//		    JOptionPane.showMessageDialog(null, e, "Error",JOptionPane.ERROR_MESSAGE);
//			e.printStackTrace();
//		} catch (DocumentException e) {
//		    JOptionPane.showMessageDialog(null, e, "Error",JOptionPane.ERROR_MESSAGE);
//		}
		return output;
	}
	
	private static void drawHeader(PdfContentByte cb, String title, String subtitle, BaseColor titleColor, BaseColor subtitleColor) throws DocumentException, IOException {
		cb.beginText();
		BaseFont bf = BaseFont.createFont();
		Font titleFont = new Font(bf, 32, 0, titleColor);
		Font subtitleFont = new Font(bf, 14, 0, subtitleColor);
		Chunk c = new Chunk(title, titleFont);
		Phrase phrase = new Phrase(c);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, phrase, 300, 800, 0);
		c = new Chunk(subtitle, subtitleFont);
		phrase = new Phrase(c);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, phrase, 300, 780, 0);
		cb.endText();

		
	}
	
	private static void processTablature(PdfContentByte cb, ArrayList<Measure> measures, Document document) throws DocumentException, IOException {
		float pageLocationX = STARTMARGIN;
		float remainingSpace = 550f;
		float measureLength = 0;
		float pageLocationY = 750;
		int pageLine = 0;
		cb.setLineWidth(0.2F);
		drawHorizontalLines(cb, pageLocationY);
		
		for (Measure measure: measures) {
			measureLength = measure.getLength() * spacing;
			if (measureLength < remainingSpace)
				remainingSpace = remainingSpace - measureLength;
			else {
				pageLine++;
				remainingSpace = LINEWIDTH - measureLength;
				pageLocationY = pageLocationY - (2 * STAFFHEIGHT);
				if (pageLine == 9) {
					newPage(document);
					cb.setLineWidth(.5F);
					pageLocationY = 810;
					pageLine = 0;
				}
				pageLocationX = STARTMARGIN;
				drawHorizontalLines(cb, pageLocationY);
			}
			processMeasure(cb, measure, pageLocationX, pageLocationY);
			pageLocationX += measureLength;
		}
	}
	
	private static void processMeasure(PdfContentByte cb, Measure measure, float pageLocationX, float pageLocationY) throws DocumentException, IOException {
		int currentLine = 0;
		for (ArrayList<String> tokens : measure.getTokens()) {
			draw(cb, tokens, pageLocationX, pageLocationY, currentLine); 
			currentLine++;
		}
	}

	private static void draw(PdfContentByte cb, ArrayList<String> tokens, float horizontalShift, float pageLocationY, int currentLine) throws DocumentException, IOException {
		boolean firstVerticalLine = true;
		boolean hold = false;
		String previousToken = "";
		String letter = "";
		
		float verticalShift = pageLocationY - (HEIGHTSPACING * currentLine);

		int index = 0;
		
		for (String s : tokens) {
			System.out.print(s);
			if (s.equals("|")) {
				drawVerticalBars(cb, horizontalShift, pageLocationY);
				if (firstVerticalLine) {
					horizontalShift += spacing;
					firstVerticalLine = false;
				}
			}
			else if (s.equals("||")) {
				if (previousToken.equals("*")) {
					drawVerticalBars(cb, horizontalShift - spacing/2, pageLocationY);
					drawThickVerticalBars(cb, horizontalShift, pageLocationY);
				}
				else
					drawVerticalBars(cb, horizontalShift, pageLocationY);
				if (firstVerticalLine) {
					horizontalShift += spacing; ;
					firstVerticalLine = false;
				}
			}
			else if (s.equals("|||")) {
				for (int i = 0; i < s.length(); i++) {
					drawVerticalBars(cb, horizontalShift, pageLocationY);
					horizontalShift += spacing;
				}
			}
			else if (s.contains("-"))
				horizontalShift += (s.length() * spacing);
			else if (s.length() == 2) {
				if (s.contains("|")) {
					if (firstVerticalLine) 	
						horizontalShift += spacing;
					else
						drawText(cb, "Repeat " + s.substring(1) + " times", horizontalShift - 6 * spacing, pageLocationY + HEIGHTSPACING * 1.5F);
					firstVerticalLine = false;
				}
				else {
					drawText(cb, s, horizontalShift, verticalShift);
					horizontalShift += (2 * spacing);
				}
			}
			else if (s.length() == 1) {
				if (s.contains("*")) {
					drawCircle(cb, horizontalShift, verticalShift);					
					if (previousToken.equals("||")) {
						drawVerticalBars(cb, horizontalShift - spacing/2, pageLocationY);
						drawThickVerticalBars(cb, horizontalShift - spacing, pageLocationY);
					}
				}
				else if (s.contains("s")) {
					drawSlash(cb, horizontalShift, verticalShift);
				}
				else if (s.contains("h") || s.contains("p")) {
					hold = true;
					letter = s;
				}
				else if (index == 0 || index == tokens.size())
					;
				else
				{
					drawText(cb, s, horizontalShift, verticalShift);
				}
				horizontalShift += spacing;
			}
			else if (s.length() == 3) {
				drawText(cb, s.substring(1, 2), horizontalShift, verticalShift);
				drawDiamond(cb, horizontalShift + spacing - 1, verticalShift);
				horizontalShift += (3 * spacing);
			}
			else if (s.length() == 4) {
				drawText(cb, s.substring(1, 3), horizontalShift, verticalShift);
				drawDiamond(cb, horizontalShift + spacing + 1 , verticalShift);
				horizontalShift += (4 * spacing);
			}

			if (hold && Pattern.matches("[0-9]+", s)) {
				drawHold(cb, letter, previousNoteX[currentLine] - spacing, (previousNoteY[currentLine]) + 4F, horizontalShift - spacing, verticalShift + 4F);
				hold = false;
			}  

			if (!s.contains("-")) 
				previousToken = s;
			if (Pattern.matches("[0-9]+", s)) { 
				previousNoteX[currentLine] = horizontalShift;
				previousNoteY[currentLine] = (int)verticalShift;
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
	
	private static void drawThickVerticalBars(PdfContentByte cb, float xShift, float yStart) {
		cb.setLineWidth(2F);
		cb.moveTo(xShift, yStart);
		cb.lineTo(xShift, yStart - STAFFHEIGHT);
		cb.stroke();
		cb.setLineWidth(0.2F);
	}
	
	private static void drawText(PdfContentByte cb, String string, float x, float y) throws DocumentException, IOException {
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
