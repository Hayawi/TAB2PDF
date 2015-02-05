package base_system;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base_system.InputUtil;

public class InputUtilTest {

	@Before
	public void setUp() throws Exception {
		PrintWriter writer = new PrintWriter(
				"hyphen.txt","UTF-8");
		writer.print("--");
		writer.close();
	}

	@Test
	public void testOpenFileBad() throws IOException {
		String s = InputUtil.openFile("hyphen.txt");
		assertFalse("Read p as a hyphen.",s.equals("p"));
	}
	
	@Test
	public void testOpenFileGood() throws IOException {
		String s = InputUtil.openFile("hyphen.txt");
		assertTrue("Read p as a hyphen.",s.equals("--"));
	}
	

	@After
	public void closeDown() throws Exception{
		File f = new File("hyphen.txt");
		f.delete();
	}

}
