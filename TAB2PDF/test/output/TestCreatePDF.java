package output;

import static org.junit.Assert.*;

import java.io.File;
import org.junit.Before;
import org.junit.Test;
import output.CreatePDF;

public class TestCreatePDF {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testwritePDF() {
		CreatePDF.writePDF("test.pdf", "some stuff");
		File f = new File("test.pdf");
		assertTrue("PDF file not created properly",f.exists() && !f.isDirectory());
		f.delete();
		
	}

}
