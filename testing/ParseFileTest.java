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


public class ParseFileTest {
	
	static Tablature t;
	static File file;
	static PrintWriter writer;
	static String missingHeader;
	static String ascii;
	
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		String filler = 
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

				"|------s------------------||-------------------------||  |\n" +
				"|-----<10>--1--h--1-----1-||-----1-----1-----1-----1-||\n" +
				"|---2-----2---p-2-----2---||*--2-----2-----2-----2--*||\n" +
				"|-2-----<2>---2-----2-----||*2-----2-----2-----2----*||\n" +
				"|-0-----------------------||-------------------------||\n" +
				"|-------------------------||-3-----------------------||\n\n";
		
		ascii = goodInput;
		for(int i = 0; i < 30; i++){
			ascii += filler;
		}
		
		file  = new File("testin.txt");
		writer = new PrintWriter(file);
		writer.write(ascii);
				writer.close();	
				t = new Tablature("testin.txt","testout.txt");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOpenFile() throws IOException {
		try{
		ParseFile.openFile("");  // supposed to fail.
		}
		catch(FileNotFoundException e){
			assertTrue(true);  // throws exception when it should.
		}
		
		String in = ParseFile.openFile("testin.txt");
		assertTrue(in.length() > 0);
	}

	@Test
	public void testSortMeasure() {
		ParseFile.sortMeasure(ascii);
	}

	@Test
	public void testParse() {
		fail("Not yet implemented");
	}

	@Test
	public void testConvertToMeasures() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnevenBlockLengthCheck() {
		fail("Not yet implemented");
	}

}
