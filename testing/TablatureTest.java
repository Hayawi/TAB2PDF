import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;


public class TablatureTest {
	static Tablature t;
	static File file;
	static PrintWriter writer;
	static String missingHeader ;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		missingHeader = 
						"|-------------------------|-------------------------|\n" +
						"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n" +
						"|---2-----2-----2-----2---|---2-----2-----2-----2---|\n" +
						"|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n" +
						"|-0-----------------------|-------------------------|\n" +
						"|-------------------------|-3-----------------------|\n\n";
		
		String goodInput = 
				"TITLE=Moonlight Sonata\n" +
				"SUBTITLE=Ludwig van Beethoven\n" +
				"SPACING=12\n\n" +

				"|-------------------------|-------------------------|\n" +
				"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n" +
				"|---2-----2-----2-----2---|---2-----2-----2-----2---|\n" +
				"|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n" +
				"|-0-----------------------|-------------------------|\n" +
				"|-------------------------|-3-----------------------|\n\n";
		
		file  = new File("testin.txt");
		writer = new PrintWriter(file);
		writer.write(goodInput);
				writer.close();	
				t = new Tablature("testin.txt","testout.txt");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		file.delete();
	}

	@Test
	public void testDraw() throws IOException, DocumentException {
		assertTrue(true);
		assertTrue(t.draw());
	}

	@Test
	public void testSetOutputPath() {
		t.setOutputPath("testOutPath");
		assertTrue(t.getOutputPath().equals("testOutPath"));
		assertFalse(t.getOutputPath().equals("badOutPath"));
	}

	@Test 
	public void testGetOutputPath() throws IOException {
		t = new Tablature("testin.txt","testout.pdf");
		t.setOutputPath("testOutPath");
		assertTrue(t.getOutputPath().equals("testOutPath"));
		assertFalse(t.getOutputPath().equals("badOutPath"));
	}

	@Test 
	public void testTablature() throws IOException {
		try{
		t = new Tablature("","");  // supposed to fail.
		}
		catch(FileNotFoundException e){
			assertTrue(true);   
		}
		
		t = new Tablature("testin.txt","testout.txt");
		assertTrue(t != null);
		
	}

	@Test
	public void testSetAscii() throws IOException {
		t.setAscii("asciitext.txt");
		assertTrue(t.getAscii().equals("asciitext.txt"));
		assertFalse(t.setAscii(""));
	}

	@Test
	public void testGetAscii() {
		t.setAscii("asciitext.txt");
		assertTrue(t.getAscii().equals("asciitext.txt"));
		assertFalse(t.getAscii().equals("asctext.txt"));	
	}

	@Test
	public void testGetTitle() {
		t.setTitle("title");
		assertTrue(t.getTitle().equals("title"));
		assertFalse(t.getTitle().equals("fail"));
		
	}

	@Test
	public void testGetSubtitle() {
		t.setSubtitle("subtitle");
		assertTrue(t.getSubtitle().equals("subtitle"));
		assertFalse(t.getSubtitle().equals("badtitle"));
	}

	@Test
	public void testGetSpacing() {
		t.setSubtitle("subtitle");
		assertTrue(t.getSubtitle().equals("subtitle"));
		assertFalse(t.getSubtitle().equals("badtitle"));
	}

	@Test
	public void testGetFontSize() {
		assertTrue(t.getFontSize() == 8);
	}

	@Test
	public void testGetMeasures() {
		t.setSubtitle("subtitle");
		assertTrue(t.getSubtitle().equals("subtitle"));
		assertFalse(t.getSubtitle().equals("badtitle"));
	}

	@Test
	public void testSetTitle() {
		t.setSubtitle("subtitle");
		assertTrue(t.getSubtitle().equals("subtitle"));
		assertFalse(t.getSubtitle().equals("badtitle"));
	}

	@Test
	public void testSetSubtitle() {
		t.setSubtitle("subtitle");
		assertTrue(t.getSubtitle().equals("subtitle"));
		assertFalse(t.getSubtitle().equals("badtitle"));
	}

	@Test
	public void testSetSpacing() {
		assertFalse(t.setSpacing(-1));
		assertTrue(t.setSpacing(1));
	}

	@Test
	public void testSetFontSize() {
		assertFalse(t.setFontSize(-1));
		assertTrue(t.setFontSize(1));
		
	}

	@Test
	public void testGetFontColor() {
		assertTrue(t.getFontColor().equals(BaseColor.BLACK));
		assertFalse(t.getFontColor().equals(BaseColor.GREEN));
	}

	@Test
	public void testSetFontColor() {
		assertTrue(t.setFontColor(BaseColor.DARK_GRAY));
		
	}

	@Test
	public void testGetTitleColor() {
		t.setTitleColor(BaseColor.GREEN);
		assertTrue(t.getTitleColor().equals(BaseColor.GREEN));
		assertFalse(t.getTitleColor().equals(BaseColor.BLACK));
	}

	@Test // same as above really.
	public void testSetTitleColor() {
		t.setTitleColor(BaseColor.BLUE);
		assertTrue(t.getTitleColor().equals(BaseColor.BLUE));
		assertFalse(t.getTitleColor().equals(BaseColor.BLACK));
	}

	@Test
	public void testGetSubtitleColor() {
		t.setSubtitleColor(BaseColor.BLUE);
		assertTrue(t.getSubtitleColor().equals(BaseColor.BLUE));
		assertFalse(t.getSubtitleColor().equals(BaseColor.BLACK));
	}

	@Test
	public void testSetSubtitleColor() {
		t.setSubtitleColor(BaseColor.BLUE);
		assertTrue(t.getSubtitleColor().equals(BaseColor.BLUE));
		assertFalse(t.getSubtitleColor().equals(BaseColor.BLACK));
	}

	@Test
	public void testMissingSpacing() throws IOException {
		file = new File("testNoheader");
		writer = new PrintWriter(file);
		writer.write(missingHeader);
		t = new Tablature("testNoheader","output");
	}
}
