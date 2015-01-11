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
		writer.print("--");
		writer.close();
	}

	@Test
	public void testOpenFileBad() throws IOException {
		String s = ReadASCII.openFile("tabs.txt");
		assertFalse("Read p as a hyphen.",s.equals("p"));
	}
	
	@Test
	public void testOpenFileGood() throws IOException {
		String s = ReadASCII.openFile("tabs.txt");
		assertTrue("Read p as a hyphen.",s.equals("--"));
	}


	@After
	public void closeDown() throws Exception{
		File f = new File("tabs.txt");
		f.delete();
	}

}
