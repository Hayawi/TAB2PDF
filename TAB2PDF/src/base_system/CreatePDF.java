package base_system;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class CreatePDF {
	/*
	 * Writes the actual pdf file.
	 */
	public static boolean writePDF(String filename, Tablature tab) throws IOException{
		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(filename));

			document.open();
			PdfContentByte cb = writer.getDirectContent();

			String[] header = tab.getHeader();
			CreatePDF.drawHeader(cb, header);	
			
			int startY = 730;
			int space = 70;
			CreatePDF.drawStaff(cb, startY);
			CreatePDF.drawStaff(cb, startY - space);
			CreatePDF.drawStaff(cb, startY - space*2);
			CreatePDF.drawStaff(cb, startY - space*3);
			
			document.close(); // no need to close PDFWriter?
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/*
	 * Draws a stylish header.
	 */
	public static boolean drawHeader(PdfContentByte cb, String[] header) throws DocumentException, IOException {
		cb.beginText();
		BaseFont bf = BaseFont.createFont();
        cb.setFontAndSize(bf, 32);   
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, header[0], 275, 800, 0);
        cb.setFontAndSize(bf, 14); 
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, header[1], 275, 780, 0);
        cb.endText();
	return true;	
	}
	
	/*
	 * Draws a 6 bar music staff, positioned with the upper left corner at 
	 * a height of argument yStart in pixels from the bottom of the page.
	 * 
	 * The music staff spans the entire width of the page.
	 */
	static boolean drawStaff(PdfContentByte cb,int yStart) {
		cb.setLineWidth((float) 0.5);
		
		// horizontal lines
		int x1 = 0;
		int x2 = 1000;
		int barSpacing = 7;
		
		int frameWidth = 127;
		int frameIndent = 40;
		
		cb.moveTo(x1,yStart);
		cb.lineTo(x2,yStart);
		cb.moveTo(x1,yStart - barSpacing);
		cb.lineTo(x2,yStart - barSpacing);
		cb.moveTo(x1,yStart - barSpacing*2);
		cb.lineTo(x2,yStart - barSpacing*2);
		cb.moveTo(x1,yStart - barSpacing*3);
		cb.lineTo(x2,yStart - barSpacing*3);
		cb.moveTo(x1,yStart - barSpacing*4);
		cb.lineTo(x2,yStart - barSpacing*4);
		cb.moveTo(x1,yStart - barSpacing*5);
		cb.lineTo(x2,yStart - barSpacing*5);
		
		//vertical lines
		cb.moveTo(frameIndent , yStart);
		cb.lineTo(frameIndent,	yStart - 5*barSpacing);
		cb.moveTo(frameIndent + frameWidth, yStart);
		cb.lineTo(frameIndent + frameWidth,	yStart - 5*barSpacing);
		cb.moveTo(frameIndent + frameWidth*2, yStart);
		cb.lineTo(frameIndent + frameWidth*2,	yStart - 5*barSpacing);
		cb.moveTo(frameIndent + frameWidth*3, yStart);
		cb.lineTo(frameIndent + frameWidth*3,	yStart - 5*barSpacing);
		cb.moveTo(frameIndent + frameWidth*4, yStart);
		cb.lineTo(frameIndent + frameWidth*4,	yStart - 5*barSpacing);
		
		
		cb.stroke();
		
		return true;
	}




}
