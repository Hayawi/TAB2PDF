import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ParseFileTest {
	
	static String missingHeader;
	static String ascii,goodInput,badMeasure,filler,shortMeasure;
	private static String trailingSpaceMeasure;
	private static File file ;
	
	

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
		"|------------------------------||------h--------||-3-----------------------|||\n\n");
		
		trailingSpaceMeasure =  
				"|-------------------------|-------------------------| \n" +
				"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n" +
				"|---2-----2-----2-----2---|---2-----2-----2-----2---|\n" +
				"|-0-----------------------|-------------------------|\n" +  
				"|-------------------------|-3-----------------------|\n\n";
		
		shortMeasure =  
				"|-------------------------|---------------------- ---|\n" +
				"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n" +
				"|---2-----2-----2-----2---|---2-----2-----2-----2---|\n" +
				"|-0-----------------------|-------------------------|\n" +  // uneven length
				"|-------------------------|-3-----------------------|\n\n";
		
		badMeasure =  
				"|-------------------------|---------------------- ---|\n" +
				"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n" +
				"|---2-----2-----2-----2---|---2-----2-----2-----2---|\n" +
				"|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n" +
				"|-0-----------------------|-------------------------|\n" +  // uneven length
				"|-------------------------|-3-----------------------|\n\n";
				
		filler = 
		"|-------------------------|-------------------------|\n" +
		"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n" +
		"|---2-----2-----2-----2---|---2-----2-----2-----2---|\n" +
		"|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n" +
		"|-0-----------------------|-------------------------|\n" +
		"|-------------------------|-3-----------------------|\n\n";
		
		goodInput = 
				"TITLE=Moonlight Sonata\n" +
				"SUBTITLE=Ludwig van Beethoven\n" +
				"SPACING=12\n\n" +

				"|2----s-------------------||-----  -------^------------|5----|||\n" +
				"|-----<10>--1--h--1-----1-||-----  1-----1-----1-----1-||----|||\n" +
				"|---2-----2---p-2-----2---||*--2-  ----2-----2-----2--*||----|||\n" +
				"|-2-----<2>---2-----2-----||*2---  --2-----2-----2----*||----|||\n" +
				"|-0-----------------------||-----  --------------------||----|||\n" +
				"|-------------------------||-3---  --------------------||----|||\n\n" + 
				"WEFEWFFEWFEWF";
		
		ascii = goodInput;
		for(int i = 0; i < 30; i++){
			ascii += filler;
			
			f.close(); //  note a file can't be read until its closed.
		}
	
	}

	@AfterClass
	public static void afterClass(){
		file.delete();
	}
	
	@Test
	public void testOpenFile() throws IOException {
		try{
		ParseFile.openFile("");  // supposed to fail.
		}
		catch(FileNotFoundException e){
			assertTrue(true);  // should throw exception
		}
		
		// should succeed
		String in = ParseFile.openFile("testin.txt");
		assertTrue(in.length() > 0);
	}

	@Test
	public void testSortMeasure() { // should fail if header is included
		ParseFile.sortMeasure(filler);
	}

	@Test
	public void tesSortMeasureUnevenBlock() { // should fail if header is included
		ParseFile.sortMeasure(badMeasure);
	}
	
	@Test
	public void testSortMeasureGarBage() { // should fail if header is included
		ParseFile.sortMeasure(shortMeasure);
	}
	
	@Test
	public void testSortMeasureTrailingSpaces() { // should fail if header is included
		ParseFile.sortMeasure(trailingSpaceMeasure);
	}

	@Test
	public void testParse() {
		ParseFile.parse(ascii);
	}

	@Test
	public void testConvertToMeasures() {
	
	ArrayList<String> p = new ArrayList<String>();
	p.add("|-------------------------|-------------------------|\n"); // test uneven length.
	p.add("|-------------------------|-------------------------|\n");
	p.add("|-------------------------|-------------------------|\n");
	p.add("|-------------------------|-------------------------|\n");
	p.add("|-------------------------|-------------------------|\n");
	p.add("|-------------------------|-------------------------|\n");
		ParseFile.convertToMeasures(p);
	}

	@Test
	public void testUnevenBlockLengthCheck() {
		ArrayList<String> p = new ArrayList<String>();
		p.add("|-------------------------|------------------------|\n   |"); // test uneven length.
		p.add("|-------------------------|-------------------------|\n");
		p.add("|-------------------------|-------------------------|\n");
		p.add("|-------------------------|-------------------------|\n");
		p.add("|-------------------------|-------------------------|\n");
		p.add("|-------------------------|-------------------------|\n");
		
		assertTrue(ParseFile.unevenBlockLengthCheck(p));
	}
	
	@Test
	public void testUnevenBlockLengthCheckFalse() {
		ArrayList<String> p = new ArrayList<String>();
		p.add("|-------------------------|-------------------------|\n"); // test uneven length.
		p.add("|-------------------------|-------------------------|\n");
		p.add("|-------------------------|-------------------------|\n");
		p.add("|-------------------------|-------------------------|\n");
		p.add("|-------------------------|-------------------------|\n");
		p.add("|-------------------------|-------------------------|\n");
		
		assertFalse(ParseFile.unevenBlockLengthCheck(p));
	}
	
	@Test // test no need here,  but later on might be.
	public void testParseFileConstructor(){
		ParseFile p = new ParseFile();
		assertTrue(p != null);
	}

}
