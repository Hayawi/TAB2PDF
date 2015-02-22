package base_system;

import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class CreaterTest {
	String simpleBody, goodBody, complexBody, moreComplexBody;

	/**
	 * The setup provides four different levels of complexity for test driven development.  
	 * For simple methods which are really dependent on the complexity of the input, the "simpleBody"
	 * and "goodBody"  string are used as input. More tests where complex symbols such as hammer or embedded
	 *  repeat numbers are tested "complexBody" and "moreComplexBody" are used as test input.  This helps
	 *  narrow down problems more quickly after they occur.  After the problems are fixed, the complexity of 
	 *  the input is gradually increased.  A method is assumed to have correct operation when all levels 
	 *  of complexity are tested on it and the method is returning correct output at these levels. 
	 *  
	 *   
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		simpleBody = "|-----|-4--||\n"
				   + "|-----|----||\n"
				   + "|-----|-7--||\n"
				   + "|-----|----||\n" 
				   + "|-----|----||\n"
				   + "|-----|----||";

		goodBody = "|-------------------------|-------------------------|\n"
				+ "|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n"
				+ "|---2-----2-----2-----2---|---2-----2-----2-----2---|\n"
				+ "|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n"
				+ "|-0-----------------------|-------------------------|\n"
				+ "|-------------------------|-3-----------------------|\n\n"

				+ "|-------------------------|-------------------------|\n"
				+ "|-----1-----1-----3-----3-|-----3-----1-----0-----0-|\n"
				+ "|---2-----2-----3-----3---|---1-----2-----2-----1---|\n"
				+ "|-3-----3-----3-----3-----|-2-----2-----2-----0-----|\n"
				+ "|-------------5-----------|-------------------------|\n"
				+ "|-1-----------------------|-0-----------0-----------|";

		complexBody = "||------------------------------<12>----------||\n"
				+ "||-<12>------------------------------<12>-----||\n"
				+ "||*-----<5>-----------<7>--------------------*||\n"
				+ "||*---------------<7>------------------------*||\n"
				+ "||--------------------------------------------||\n"
				+ "||--------------------------------------------||\n\n"

				+ "||---------3----------10-----0-------0---------------7-|\n"
				+ "||-----0---------10--------------0-------0-------5s7---|\n"
				+ "||*------2----------0----------2-------2-------0-------|\n"
				+ "||*--------------------------------2-------------------|\n"
				+ "||---2---------------------3--------3h0----------------|\n"
				+ "||-0-----------7-----------------------------0---------|\n\n";

		moreComplexBody =

				  "||---12----<12>--12----12-|----12----12----12----12-|---10---10---10---10---10---10-|4\n"
				+ "||------------------------|-------------------------|------7---------7---------7----||\n"
				+ "||*-----12----11----12----|-------<12>--11----12----|---------------------0--------*||\n"
				+ "||*-----------------------|-------------------------|-----------7------------------*||\n"
				+ "||----------------------0-|p3-----------------------|-------------------------------||\n"
				+ "||-0----------------------|-12----------------------|-7-----------------------------||\n\n"

				+ "||---------7-------|---------5-------|---------7-------|---------5---------|||\n"
				+ "||-----5s7---7-----|-----3s5---5-----|-----5s7---7-----|-----3s5---5-------|||\n"
				+ "||*--0---------0---|-------------0---|---0---------0---|-------------0---0-|||\n"
				+ "||*--------------2-|---2-----------2-|---------------2-|---2-----------2---|||\n"
				+ "||-----------------|-2---------------|-----3h0---------|-2-----------------|||\n"
				+ "||-0---------------|-----------------|-0---------------|-------------------|||";
	}

	/**
	 * Tests the separation of staves by calling the method with "goodBody" as 
	 * an argument. An oracle which is a string of the first stave of "goodBody" 
	 * is used to check that the first string in the array of strings returned by 
	 * seperateDatves is indeed equal to the first stave in "goodBody"
	 */
	@Test
	public void testSeperateStaves() {
		String[] staves = Creater.seperateStaves(goodBody);
		String oracle = "|-------------------------|-------------------------|\n"
				+ "|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n"
				+ "|---2-----2-----2-----2---|---2-----2-----2-----2---|\n"
				+ "|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n"
				+ "|-0-----------------------|-------------------------|\n"
				+ "|-------------------------|-3-----------------------|";
		// System.out.println(staves[1]);
		assertTrue(staves[0].equals(oracle));
	}

	/**
	 * Tests that the first vertical bar(s) is removed from all staves other than the first one.
	 * An oracle is used to check against the return of the seperateStave(String) method.
	 */
	@Test
	public void testRemoveConsecutiveBars() {
		String[] staves = Creater.seperateStaves(goodBody);

		String[] newStaves = Creater.removeConsecutiveBars(staves);
		String oracle1 = "|-------------------------|-------------------------|\n"
				+ "|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n"
				+ "|---2-----2-----2-----2---|---2-----2-----2-----2---|\n"
				+ "|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n"
				+ "|-0-----------------------|-------------------------|\n"
				+ "|-------------------------|-3-----------------------|";
		String oracle2 = "-------------------------|-------------------------|\n"
				+ "-----1-----1-----3-----3-|-----3-----1-----0-----0-|\n"
				+ "---2-----2-----3-----3---|---1-----2-----2-----1---|\n"
				+ "-3-----3-----3-----3-----|-2-----2-----2-----0-----|\n"
				+ "-------------5-----------|-------------------------|\n"
				+ "-1-----------------------|-0-----------0-----------|\n";

		assertTrue(oracle1.equals(newStaves[0]));
		assertTrue(oracle2.equals(newStaves[1]));
	}

	/**
	 * Tests that concatenating an array of staves produces a single stave.
	 * The test case is embedded in the code and the test case has a simple body with no
	 * special characters other digits, hyphens and single vertical bars. An oracle is used to check the result.
	 * This tests depends on methods "seperateStaves() and removeConsecutiveBars() working
	 * properly. 
	 */
	@Test
	public void testConcateStaves() {

		String[] staves = Creater.seperateStaves(goodBody);
		String[] newStaves = Creater.removeConsecutiveBars(staves);
		String oracle = "|-------------------------|-------------------------|-------------------------|-------------------------|";
		// |-------------------------|-------------------------|-------------------------|-------------------------|

		String[] bigStave = Creater.concatStaves(newStaves);
		// System.out.print(bigStave[0]);
		// System.out.print(oracle);
		assertTrue(bigStave[0].equals(oracle));

	}

	/**
	 * Tests that concatenating an array of staves producing a single stave.
	 * The test case is embedded in the code and the test case has a "complex body" containing special
	 * characters, digits, hyphens and double vertical bars with asterisks (symbolizing repeats).
	 *  An oracle is used to check the result.
	 *  This tests depends on methods "seperateStaves() and removeConsecutiveBars() working
	 *  properly. 
	 */
	@Test
	public void testConcateStavesComplex() {

		String[] staves = Creater.seperateStaves(complexBody);
		String[] newStaves = Creater.removeConsecutiveBars(staves);
		String oracle = "||------------------------------<12>----------||---------3----------10-----0-------0---------------7-|";
		String[] bigStave = Creater.concatStaves(newStaves);
		assertTrue(bigStave[0].equals(oracle));

	}

	/**
	 This tests the makeBigStave() method and checks that staves are successfully 
	 concatenated into one large stave. The test input is embedded in the testing setup 
	 and the oracle is embedded within this testing method.   
	 The input is "goodBody" which has simple structure, no double 
	 bars and no special symbols other than digits, vertical bars and hyphens. 
	 Note: only the first and last lines of the output are checked to save space.
	*/
	@Test
	public void testMakeBigStave() {
		Creater.makeBigStave(goodBody);
		String[] bigStave = Creater.getBigStave();
		// System.out.print(bigStave[5]);
		String oracleFirstLine = "|-------------------------|-------------------------|-------------------------|-------------------------|";
		String oracleLastLine = "|-------------------------|-3-----------------------|-1-----------------------|-0-----------0-----------|";
		assertTrue(oracleFirstLine.equals(bigStave[0]));
		assertTrue(oracleLastLine.equals(bigStave[5]));

	}

	/**
	 This tests the makeBigStave() method and checks that staves are successfully 
	 concatenated into one large stave. The test input is embedded in the testing setup 
	 and the oracle is embedded within this testing method.   
	 The input is "moreComplexBody" which has  more complex structure, double bars with asterisks,
	 embedded repeat numbers within double bars, special notes such as lagato slides,
	 volume swells and pull offs. Also a triple vertical bar to at the end.
	 Note: Only the fist and last lines are checked against the oracle to save space.
	 */
	@Test
	public void testMakeBigStaveMoreComplexCase() {
		Creater.makeBigStave(moreComplexBody);
		String[] bigStave = Creater.getBigStave();
		 System.out.print(bigStave[0]);
		String oracle = "||---12----<12>--12----12-|----12----12----12----12-|---10---10" + 
		 "---10---10---10---10-|4---------7-------|---------5-------|---------7-------|---------5---------|||";
		assertTrue(oracle.equals(bigStave[0]));

	}

	/**
	 * This tests that the countEndBars(String) works properly, the input and 
	 * the oracle are included in the test method. The complexity the input is minimal
	 * because the function is simple.  It just counts the number of vertical bars that are
	 * at the end of the last line of the stave. 
	 */
	@Test
	public void testCountEndBars() {
		String stave = 
				  "||---3--------2--------0-----0-0-----|3"
				+ "||---------------------------------0-||"
				+ "||*----2-0------2-0------2-0-----2--*||"
				+ "||*---------0-----------------------*||"
				+ "||-3-----------------2---------------||"
				+ "||-----------------------------------||";
		int count = Creater.countEndBars(stave);
		// System.out.print(count);
		assertTrue(count == 2);
	}

	/**
	 * Test that the countBeginBars(String) is working properly.
	 * the input and the oracle are included in this method.  The input is 
	 * not complex as the countBeginBars simply counts the number of bars 
	 * at the beginning of the first line of the string.
	 */
	@Test
	public void testCountBeginBars() {
		String line = "||------------------------------<12>----------||\n";
		int count = Creater.countBeginBars(line);
		// System.out.print(count);
		assertTrue(count == 2);
	}

	/**
	 * Test that a the "repeat number" embedded within double vertical bars 
	 * is successfully detected and returned by the "getRepeatNum(String,String)" method
	 * The testing input and the oracle are embedded within this testing method.
	 */
	@Test
	public void testGetRepeatNum() {
		String line1 = "|---10---10---10---10---10---10-|4";
		String line2 = "|------7---------7---------7----||";
		int num = Creater.getRepeatNum(line1, line2);
		assertTrue(num == 4);

	}

	/**
	 * This tests that the "getRepeatNumber" method does not return a number when it 
	 * should not return any number.  The input contains no embedded repeat number, is included
	 * within this test method.  The test asserts that the "getRepeatNum" returns -1 
	 * when no number is embedded within vertical bars.
	 */
	@Test
	public void testGetRepeatNumNoRepeat() {
		String line1 = "|---10---10---10---10---10---10-||";
		String line2 = "|------7---------7---------7----||";
		int num = Creater.getRepeatNum(line1, line2);
		assertTrue(num == -1);

	}

	/**
	 * This tests that the cutStave(index, index2) method works properly,
	 * It's success is dependant on the "makeBigStave" method working properly. 
	 * The oracle is included in this testing method and the input is the "moreComplexBody"
	 * although this complexity of the input was not needed since the cutStave method simple 
	 * returns a new stave which is the partition copied from the bigStave between the 
	 * two indices provided to the cutStave method.
	 */
	@Test
	public void testCutStave() {
		Creater.makeBigStave(moreComplexBody);
		Creater.cutStave(0, 10);
		String[] s = Creater.staveBuffer;
		// System.out.println(s[0]);
		assertTrue(s[0].equals("||---12---"));

	}

	/**
	 * This is more of a system test in that the output is checked by eye rather than 
	 * an oracle.  This test prints to the console the ascii representation of the 
	 * staves after they have been reordered so that they fit within the specified 
	 * bounds of the PDF page.  changing the pageWidth instance variable of the Creator
	 * class should change the distribution of the measures within staves so that they
	 * fit with the pageWidth.  The input used is the "moreComplexBody" which contains 
	 * the most complex cases contained within the acceptance test cases.
	 */
	@Test
	public void testOrganizeStave() {
		Creater.makeBigStave(moreComplexBody);
		Creater.reorganizeStaves();
		for (String[] stave : Creater.getOrganizedStaves()) {
			for (int i = 0; i < stave.length; i++) {
				for (int j = 0; j < stave[i].length(); j++) {
					System.out.print(stave[i].charAt(j));
				}
				System.out.println();
			}
			System.out.println("\n");
		}
	}

	/**
	 * Test that "indexOfLastMeasure(index1,index2)" correctly returns the index
	 * of the last vertical bar of the preceding measure relative to the 
	 * current measure.
	 * The oracle is included within the test methods body and asserts what the correct 
	 * returned index should be based on the "moreComplexBody" input.
	 */
	@Test
	public void testIndexOfLastMeasure() {
		Creater.makeBigStave(moreComplexBody);
		int i = Creater.indexOfLastMeasure(65, 100);
		// System.out.println(i);
		assertTrue(i == 85);
	}

	/**
	 * This is system level testing where the output is judged by eye to match portions of the acceptance 
	 * tests.
	 * 
	 * The testing input is still taken from within this testing class, but
	 * the that input is taken directly from the acceptance tests so it still makes sense to 
	 * compare against the desired acceptance output. 
	 * 
	 * Note that the testing input is only snippits of the acceptance output,  not the whole thing.
	 * @throws IOException
	 * @throws DocumentException
	 */
	@Test
	// system testing.
	public void testCreateTab() throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream("test/base_system/system_tests_output/system_test_body.pdf"));

		document.open();
		PdfContentByte cb = writer.getDirectContent();
		Creater.createTab(document, cb, moreComplexBody);
		document.close();
		assertTrue(true);
		
	}

	/**
	 * Tests that the draw header method correctly draws a head for a PDF file.
	 * This is system test, and must be checked by eye that the output is correct.
	 * @throws DocumentException
	 * @throws IOException
	 */
	@Test
	public void testDrawHeader() throws DocumentException, IOException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream("test/base_system/system_tests_output/system_test_header.pdf"));

		String[] header = {"This is the song name", "This is the composer name"};
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		Creater.drawHeader(cb, header);
		document.close();
		assertTrue(true);
		
	}

}
