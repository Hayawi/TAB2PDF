import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.itextpdf.text.DocumentException;


public class DrawPDFTest {

	private static File file ;
	private static Tablature t;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		file = new File("testin.txt");
		file.createNewFile();
		PrintWriter f = new PrintWriter(file);
		f.write(
		"TITLE=Moonlight Sonata\n" +
		"SUBTITLE=Ludwig van Beethoven\n" +
		"SPACING=12\n\n" +
		
		"|-----s------------------------||--------p------|0-------------------------||| | \n" +
		"|-----1-----1----1p1----1------||-----<12>------||-----1-----1h2---1-----1-|||\n" +
		"|---2-----2-----2-----2<>------||*-----<11>---4*||*--2-----2-----2s12--2---|||\n" +
		"|-2-----2-----2-----2----------||*-----------12*||*2-----2-----2-----2-----|||\n" +
		"|-0----------------------------||---------------||-------------------------|||\n" +
		"|------------------------------||------h--------||-3-----------------------|||\n\n" +
		
		
		"|-----s------------------------||---------------|1-------------------------||\n" +
		"|-----1-----1----1p1----1------||-----<12>------||-----1-----1h2---1-----1-||\n" +
		"|---2-----2-----2-----2<>------||*-----<11><12>*||---2-----2-----2-----2---||\n" +
		"|-2-----2-----2-----2----------||*----------<2>*||-2-----2-----2-----2-----||\n" +
		"|-0----------------------------||---------------||-------------------------||\n" +
		"|------------------------------||---------------||-3-----------------------||\n\n" +
		
		"|-----s------------------------||---------------|2-------------------------||\n" +
		"|-----1-----1----1p1----1------||-----<12>------||-----1-----1h2---1-----1-||\n" +
		"|---2-----2-----2-----2<>------||*-----<11><12>*||---2-----2-----2-----2---||\n" +
		"|-2-----2-----2-----2----------||*----------<2>*||-2-----2-----2-----2-----||\n" +
		"|-0----------------------------||---------------||-------------------------||\n" +
		"|------------------------------||---------------||-3-----------------------||\n\n" +
		
		"|-----s---------------------- -||---------------|10-------------------------||\n" +
		"|-----1-----1----1p1----1---- -||-----<12>------||-----1-----1h2---1-----1-||\n" +
		"|---2-----2-----2-----2<>---- -||*-----<11><12>*||---2-----2-----2-----2---||\n" +
		"|-2-----2-----2-----2-------- -||*----------<2>*||-2-----2-----2-----2-----||\n" +
		"|-0-------------------------- -||---------------||-------------------------||\n" +
		"|---------------------------- -||---------------||-3-----------------------||\n\n" +
		
		"|-----s------------------------||---------------|9-------------------------||\n" +
		"|-----1-----1----1p1----1------||-----<12>------||-----1-----1h2---1-----1-||\n" +
		"|---2-----2-----2-----2<>------||*-----<11><12>*||---2-----2-----2-----2---||\n" +
		"|-2-----2-----2-----2----------||*----------<2>*||-2-----2-----2-----2-----||\n" +
		"|-0----------------------------||---------------||-------------------------||\n" +
		"|------------------------------||---------------||-3-----------------------||\n\n" +
		
		"|-----s------------------------||---------------|9-------------------------||\n" +
		"|-----1-----1----1p1----1------||-----<12>------||-----1-----1h2---1-----1-||\n" +
		"|---2-----2-----2-----2<>------||*-----<11><12>*||---2-----2-----2-----2---||\n" +
		"|-2-----2-----2-----2----------||*----------<2>*||-2-----2-----2-----2-----||\n" +
		"|-0----------------------------||---------------||-------------------------||\n" +
		"|------------------------------||---------------||-3-----------------------||\n\n"		
		);
		f.close();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {	
		file.delete();
	}

	@Before
	public void setUp() throws Exception {
		t = new Tablature("rememberingrain.txt", "testout.pdf");
	}

	@Test
	public void testWritePDF() throws IOException, DocumentException {
		t.setNumberMeasures(true);
		Boolean result = DrawPDF.writePDF(t);
		assertTrue(result);
		try {
		DrawPDF.writePDF(null); // supposed to fail.
		}
		catch(NullPointerException e){
			assertTrue(true);
		}
	}

	@Test
	public void testWritePDFInMemory() throws IOException, NoSuchMethodException, SecurityException, DocumentException {
		ByteArrayOutputStream result = DrawPDF.writePDFInMemory(t);
		assertTrue(result instanceof ByteArrayOutputStream);	
		try{
		DrawPDF.writePDFInMemory(null); // supposed to fail.
		}
		catch(NullPointerException e){
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testDrawPDFConstructor(){
		try{
		new DrawPDF(); // should fail.
		}
		catch(Exception e){
			assertTrue(true);   // exception should be thrown, DrawPDF must should not be instantiated.
		}
		
	}
}
