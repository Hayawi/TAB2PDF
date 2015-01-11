package output;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReadASCIITest {

	@Before
	public void setUp() throws Exception {
		PrintWriter writer = new PrintWriter(
				"tabs.txt","UTF-8");
		writer.println("-");
		writer.close();
	}

	@Test
	public void testOpenFileBad() throws IOException {
		File f = ReadASCII.openFile("tabs.txt");
		FileReader fr = new FileReader(f);
		assertNotEquals("Read p as a hyphen.",fr.read(),(int)'p');
		fr.close();
	}

	@Test
	public void testOpenFileGood() throws IOException {
		File f = ReadASCII.openFile("tabs.txt");
		FileReader fr = new FileReader(f);
		assertEquals("Hyphen not read from tab file.",fr.read(),(int)'-');
		fr.close();
	}
	@After
	public void closeDown() throws Exception{
		File f = new File("tabs.txt");
		f.delete();
	}

}
