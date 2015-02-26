

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFCreater extends Creater{

	public boolean writePDF(String filename, Tablature tab) throws IOException, DocumentException {
			Document doc = new Document();
			
			File f = new File(filename);
			f.delete();
			
			PdfWriter writer = PdfWriter.getInstance(doc,new FileOutputStream(filename));
			
			doc.open();
			PdfContentByte cb = writer.getDirectContent();
	
			if(tab.hasHeader()){
				String[] header = tab.getHeader();
				Creater.drawHeader(cb, header);
			}
			
			if(tab.hasBody()){		
				String body = tab.getBody();
				//System.out.print(body); // debug
				//Creater.drawBody(cb,body,document);
				Creater.createTab(doc,cb,body);
			}
			
			doc.close(); // no need to close PDFWriter?

		return true;
	}

}
