package base_system;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFCreater extends Creater{

	public boolean writePDF(String filename, Tablature tab) throws IOException {
		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(filename));
	
			document.open();
			PdfContentByte cb = writer.getDirectContent();
	
			if(tab.hasHeader()){
				String[] header = tab.getHeader();
				Creater.drawHeader(cb, header);
			}
			
			if(tab.hasBody()){
			
				String body = tab.getBody();
				//System.out.print(body); // debug
				Creater.drawBody(cb,body,document);
			}
			
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
