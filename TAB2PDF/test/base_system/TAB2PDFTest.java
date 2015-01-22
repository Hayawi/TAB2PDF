package base_system;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base_system.TAB2PDF;

public class TAB2PDFTest {
	
	String[] strings = {"tabs.txt","tabs.pdf"};
	
	@Before
	public void setup() throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(
				"tabs.txt","UTF-8");
		writer.print("TITLE=Moonlight Sonata\n"
				   + "SUBTITLE=Ludwig van Beethoven\n"
				   + "SPACING=5");
		writer.close();
	}

	@Test
	public void testMainArgs() throws IOException {
		
		TAB2PDF.main(strings);
		File f = new File(strings[0]);
		File i = new File(strings[1]);
		assertTrue("Files not created.",f.isFile() && i.isFile());
	}
	
	@After
	public void close(){
		File pdf = new File("tabs.pdf");
		File txt = new File("tabs.txt");
		txt.delete();
		pdf.delete();
		
	}
	

}
