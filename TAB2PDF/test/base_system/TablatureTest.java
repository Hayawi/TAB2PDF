package base_system;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TablatureTest {
	String s;
	@Before
	public void setUp() throws Exception {
		
		// sample header for testing.
		s = "TITLE=Moonlight Sonata\n"
				   + "SUBTITLE=Ludwig van Beethoven\n"
				   + "SPACING=5";
	}

	@Test
	public void testTablature() {
		Tablature t = new Tablature(s);
		assertTrue(t.getAscii().equals(s));
		
	}

	@Test
	public void testSetAscii() {
		Tablature tab = new Tablature(s);
		boolean value = tab.setAscii("some OTHER stuff");
		assertTrue(value);

	}

	@Test
	public void testGetAscii() {
		Tablature tab = new Tablature(s);
		String value = tab.getAscii();
		assertTrue(value.equals(s));
	}
	
	@Test
	public void testCheckHeaderExistsGood() throws IOException {
		Tablature tab = new Tablature(s);
		boolean headerExists = tab.checkHeaderExists();
		assertTrue("Title was not read correctly or text file has incomplete title.",headerExists);
	}
	
	// Note that this should print a error message to the terminal.
	@Test
	public void testCheckHeaderExistsBad() throws IOException {
		String s = "RUINEDTITLE=Moonlight Sonata\n"
				   + "SUBTITLE=Ludwig van Beethoven\n"
				   + "SPACING=5";
		Tablature tab = new Tablature(s);
		boolean headerExists = tab.checkHeaderExists();
		assertFalse("Header was read in correctly when it shouldn't have been.",headerExists);
	}
	
	@Test
	public void testParseHeaderGood() throws IOException {
		String s = "TITLE=Moonlight Sonata\n"
				   + "SUBTITLE=Ludwig van Beethoven\n"
				   + "SPACING=5";
		Tablature tab = new Tablature(s);
		String[] headerParsed = tab.parseHeader();
		assertTrue("header was not parsed properly",
				headerParsed[0].equals("Moonlight Sonata") &&
				headerParsed[1].equals("Ludwig van Beethoven") &&
				headerParsed[2].equals("5"));

	}
	
	@Test
	public void testParseHeaderBad() throws IOException {
		String s = "RUINEDTITLE=Moonlight Sonata\n"
				   + "SUBTITLE=Ludwig van Beethoven\n"
				   + "SPACING=5";
		Tablature tab = new Tablature(s);
		assertFalse("Says header exists when it does not, or is incorrect.",tab.hasHeader());

	}
	
	@Test 
	public void testGetHeader(){
		Tablature tab = new Tablature(s);
		String[] header = tab.getHeader();
		assertTrue(header[0].equals("Moonlight Sonata") &&
		header[1].equals("Ludwig van Beethoven") &&
		header[2].equals("5"));
	}

}
