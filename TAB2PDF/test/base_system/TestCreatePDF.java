package base_system;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base_system.CreatePDF;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class TestCreatePDF {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testwritePDF() throws IOException {
		String s = "TITLE=Moonlight Sonata\n"
				+ "SUBTITLE=Ludwig van Beethoven\n"
				+ "SPACING=5";
		Tablature testtab = new Tablature(s);
		CreatePDF.writePDF("testWritePDF.pdf", testtab);
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
		boolean success = CreatePDF.drawHeader(cb,headerParsed);
		document.close(); // must close
		assertTrue("Header was not drawn successfully",success);
		
	}

	/*
	 * Tests drawing of 6 bar music staff.
	 */
	@Test
	public void testdrawMusicStaff() throws FileNotFoundException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream("drawStaffTest.pdf"));
		
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		boolean value = CreatePDF.drawStaff(cb,800);
		document.close(); // must close
		assertTrue("Drawing the staff did not complete successfully",value);
	}

	@After
	public void cleanUp(){
		//FileUtils.cleanDirectory(arg0);
	}
	
}
