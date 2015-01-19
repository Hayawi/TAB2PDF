package output;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class CreatePDF {
	
	public static boolean writePDF(String filename, String contents){
		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(filename));
			
			document.open();
			
			// write the text
			document.add(new Paragraph(contents));
			
			PdfContentByte cb = writer.getDirectContent();
			
			// Adds some lines just for TESTING
			CreatePDF.drawHorzBars(cb,800);
			
			
			
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

	private static void drawHorzBars(PdfContentByte cb,int yStart) {
		cb.setLineWidth((float) 0.5);
		
		// horizontal lines
		int x1 = 0;
		int x2 = 1000;
		int barSpacing = 7;
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
		cb.stroke();
		
	}


}
