package output;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class CreatePDF {
	
	public static boolean writePDF(String filename, String contents){
		Document document = new Document();
		try {
			PdfWriter.getInstance(document,
					new FileOutputStream(filename));
			
			document.open();
			document.add(new Paragraph(contents));
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

}
