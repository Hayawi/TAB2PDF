package base_system;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TablatureTest {
	String goodHeader,badHeader,goodBody,badBody,goodHeaderBody,badHeaderBody;
	
	// note this could be Before
	@Before
	public void setUp() throws Exception {
		
		// sample header for testing.
		goodHeader = "TITLE=Moonlight Sonata\n"
				   + "SUBTITLE=Ludwig van Beethoven\n"
				   + "SPACING=5";
		badHeader = "RUINEDTITLE=Moonlight Sonata\n"
				   + "SUBTITLE=Ludwig van Beethoven\n"
				   + "SPACING=5";
		
		goodBody =
		 "|-------------------------|-------------------------|\n"
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
				+"|-----1-----1-----3-----3-|-----3-----1-----0-----0-|\n"
				+"|---2-----2-----3-----3---|---1-----2-----2-----1---|\n"
				+"|-3-----3-----3-----3-----|-2-----2-----2-----0-----|\n"
				+"|-------------5-----------|-------------------------|\n"
				+"|-1-----------------------|-0-----------0-----------|";
		
		badHeaderBody = "TITLE=Moonlight Sonata\n"
				+ "SUBTITLE=Ludwig van Beethoven\n"
				+ "SPACING=5\n"

				+"|-------------------------|-------------------------|"
				+"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|"
				+"|---2-----2-----2-----2---|---2-----2-----2-----2---|"
				+"|-2-----2-----2-----2-----|-2-----2-----2-----2-----|"
				+"|-0-----------------------|-------------------------|"
				+"|-------------------------|-3-----------------------|"

				+"|-------------------------|-------------------------|"
				+"|-----1-----1-----3-----3-|-----3-----1-----0-----0-|"
				+"|---2-----2-----3-----3---|---1-----2-----2-----1---|"
				+"|-3-----3-----3-----3-----|-2-----2-----2-----0-----|"
				+"|-------------5-----------|-------------------------|"
				+"|-1-----------------------|-0-----------0-----------|";
	}

	@Test
	public void testTablature() {
		Tablature t = new Tablature(goodHeader);
		assertTrue(t.getAscii().equals(goodHeader));
		
	}

	@Test
	public void testSetAscii() {
		Tablature tab = new Tablature(goodHeader);
		boolean value = tab.setAscii("some OTHER stuff");
		assertTrue(value);

	}

	@Test
	public void testGetAscii() {
		Tablature tab = new Tablature(goodHeader);
		String value = tab.getAscii();
		assertTrue(value.equals(goodHeader));
	}
	
	@Test
	public void testCheckHeaderExistsGood() throws IOException {
		Tablature tab = new Tablature(goodHeader);
		boolean headerExists = tab.checkHeaderExists();
		assertTrue("Title was not read correctly or text file has incomplete title.",headerExists);
	}
	
	// Note that this should print a error message to the terminal.
	@Test
	public void testCheckHeaderExistsBad() throws IOException {
		Tablature tab = new Tablature(badHeader);
		boolean headerExists = tab.checkHeaderExists();
		assertFalse("Header was read in correctly when it shouldn't have been.",headerExists);
	}
	
	@Test
	public void testParseHeaderGood() throws IOException {
		Tablature tab = new Tablature(goodHeader);
		String[] headerParsed = tab.parseHeader();
		assertTrue("header was not parsed properly",
				headerParsed[0].equals("Moonlight Sonata") &&
				headerParsed[1].equals("Ludwig van Beethoven") &&
				headerParsed[2].equals("5"));

	}
	
	@Test
	public void testParseHeaderBad() throws IOException {
		Tablature tab = new Tablature(badHeader);
		assertFalse("Says header exists when it does not, or is incorrect.",tab.hasHeader());

	}
	
	@Test 
	public void testGetHeader(){
		Tablature tab = new Tablature(goodHeader);
		String[] header = tab.getHeader();
		assertTrue(header[0].equals("Moonlight Sonata") &&
		header[1].equals("Ludwig van Beethoven") &&
		header[2].equals("5"));
	}
	
	@Test //sees if it can seperate the header from the body.
	public void testParseBodyGood(){
		Tablature tab = new Tablature(goodHeaderBody);
		String body = tab.parseBody();
		//System.out.print(body);
		assertTrue(body.equals(goodBody));
				
	}
	
	@Test //sees if it can parse the body when there is no header.
	public void testParseBodyGoodNoHeader(){
		Tablature tab = new Tablature(goodBody);
		String body = tab.parseBody();
		//System.out.print(body);
		assertTrue(body.equals(goodBody));
				
	}

	@Test //sees if it returns null when no body exists.
	public void testParseBodyNoBody(){
		Tablature tab = new Tablature(goodHeader);
		String body = tab.parseBody();
		assertTrue(body == null);
				
	}
	
	@Test //sees if it returns null when no body exists and it's a bad header.
	public void testParseBodyNoBodyBadHeader(){
		Tablature tab = new Tablature(badHeader);
		String body = tab.parseBody();
		assertTrue(body == null);
	}
	
	@Test
	public void testHasBodyTrue(){
		Tablature tab = new Tablature(goodHeaderBody);
		assertTrue(tab.hasBody());
	}
	
	@Test
	public void testHasBodyFalse(){
		Tablature tab = new Tablature(goodHeader);
		assertFalse(tab.hasBody());
	}
	
	@Test
	public void testGetBodyTrue(){
		Tablature tab = new Tablature(goodHeaderBody);
		assertTrue(tab.getBody().equals(goodBody));
	}
	
}
