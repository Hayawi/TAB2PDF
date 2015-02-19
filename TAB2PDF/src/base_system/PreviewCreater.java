package base_system;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class PreviewCreater extends Creater{
	
	public ByteArrayOutputStream writePDFInMemory(Tablature tab) throws IOException {
		Document document = new Document();
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
try {		PdfWriter writer = PdfWriter.getInstance(document,oStream);

			document.open();
			PdfContentByte cb = writer.getDirectContent();
	
			if(tab.hasHeader()){
				String[] header = tab.getHeader();
				Creater.drawHeader(cb, header);
			}
			
			if(tab.hasBody()){
				String body = tab.getBody();
				//System.out.print(body); // debug
				Creater.createTab(document,cb,body);
			}
			
			document.close(); // no need to close PDFWriter?
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
		return oStream;
	}

}
