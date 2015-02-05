package base_system;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import base_system.Creater;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFCreaterTest {
	String goodHeaderBody;

	@Before
	public void setUp() throws Exception {
		goodHeaderBody = "TITLE=Moonlight Sonata\n"
				+ "SUBTITLE=Ludwig van Beethoven\n"
				+ "SPACING=5\n\n"

				+"|-------------------------|-------------------------|\n"
				+"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n"
				+"|---2-----2-----2-----2---|---2-----2-----2-----2---|\n"
				+"|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n"
				+"|-0-----------------------|-------------------------|\n"
				+"|-------------------------|-3-----------------------|\n\n"

				+"|-------------------------|-------------------------|\n"
				+"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n"
				+"|---2-----2-----2-----2---|---2-----2-----2-----2---|\n"
				+"|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n"
				+"|-0-----------------------|-------------------------|\n"
				+"|-------------------------|-3-----------------------|\n\n"
				
				+"|-------------------------|-------------------------|\n"
				+"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n"
				+"|---2-----2-----2-----2---|---2-----2-----2-----2---|\n"
				+"|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n"
				+"|-0-----------------------|-------------------------|\n"
				+"|-------------------------|-3-----------------------|\n\n"
				
				+"|-------------------------|-------------------------|\n"
				+"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n"
				+"|---2-----2-----2-----2---|---2-----2-----2-----2---|\n"
				+"|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n"
				+"|-0-----------------------|-------------------------|\n"
				+"|-------------------------|-3-----------------------|\n\n"
				
				+"|-------------------------|-------------------------|\n"
				+"|-----1-----1-----3-----3-|-----3-----1-----0-----0-|\n"
				+"|---2-----2-----3-----3---|---1-----2-----2-----1---|\n"
				+"|-3-----3-----3-----3-----|-2-----2-----2-----0-----|\n"
				+"|-------------5-----------|-------------------------|\n"
				+"|-1-----------------------|-0-----------0-----------|";
				
	}

	@Test
	public void testwritePDFGoodHeaderBody() throws IOException {
		Tablature testtab = new Tablature(goodHeaderBody);
		new PDFCreater().writePDF("testWritePDF.pdf", testtab);
		File f = new File("testWritePDF.pdf");
		assertTrue("PDF file not created properly",f.exists() && !f.isDirectory());
		//f.delete();
		
	}
	
	/*
	 Tests drawing of a stylish header
	 */
	@Test
	public void testdrawHeaderGood() throws DocumentException, IOException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream("drawHeaderTest.pdf"));
		String s = "TITLE=Moonlight Sonata\n"
				   + "SUBTITLE=Ludwig van Beethoven\n"
				   + "SPACING=5";
		Tablature tab = new Tablature(s);
		String[] headerParsed = tab.parseHeader();
		
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		boolean success = Creater.drawHeader(cb,headerParsed);
		document.close(); // must close
		assertTrue("Header was not drawn successfully",success);
		
		// cleanup
		File f = new File("drawHeaderTest.pdf");
		f.delete();
	}
	
}
