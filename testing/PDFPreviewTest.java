import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.BeforeClass;
import org.junit.Test;

import com.itextpdf.text.DocumentException;


public class PDFPreviewTest {
	
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
						"SPACING=5\n\n" +

						"|------s------------------||-------------------------||\n" +
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
				writer.write(goodInput);
				writer.close();	
					t = new Tablature("moonlightsonata.txt","output.txt");
					
	}
	
	@Test // checks that create image converts between javafx image and awt.image successfully.
	public void testCreateImage() throws IOException {
		
		BufferedImage off_Image =
					new BufferedImage(100, 50,
                    BufferedImage.TYPE_INT_ARGB);
		assertTrue(PDFPreview.createImage(off_Image) instanceof javafx.scene.image.Image);

	}

	@Test
	public void testPreviewPDFDocumentInImage() throws IOException, DocumentException {;
		javafx.scene.image.Image i = PDFPreview.previewPDFDocumentInImage(t);
		assertTrue(i != null);
	}

	@Test
	public void testUpdatePreviewPDFDocument() throws IOException, DocumentException {
		PDFPreview.previewPDFDocumentInImage(t);
		javafx.scene.image.Image j = PDFPreview.updatePreviewPDFDocument();
		assertTrue(j != null);	
	}
	
	@Test
	public void testPDFPreviewConstructor(){
		PDFPreview p = new PDFPreview();
		assertTrue(p != null);
		
	}
	
	@Test
	public void testGetMaxPage(){
		assertTrue(PDFPreview.getMaxPage() == 8);
		assertFalse(PDFPreview.getMaxPage() == 1);
	}
	
	@Test
	public void testGetCurrentPage() throws Exception{
		PDFPreview.setCurrentPage(1); 
		assertTrue(PDFPreview.getCurrentPage() == 1);
	}
	
	// test set current page,  4 cases because there is only a true return of a exception.
	@Test 
	public void testSetCurrentPageExceptionTrue() throws Exception{
		
			PDFPreview.setCurrentPage(8);    // supposed to fail.
			assertFalse(PDFPreview.getCurrentPage() == 2);
	}
	
	@Test 
	public void testSetCurrentPageExceptionFalse() throws Exception{
			PDFPreview.setCurrentPage(1);   
			assertTrue(PDFPreview.getCurrentPage() == 1);
	}
	
	@Test
	public void testSetCurrentPageInputGood() throws Exception{
		// TODO:  figure out why maxpage is equal to zero here.
		assertTrue(PDFPreview.setCurrentPage(1) == true);
		
	}
	
	@Test
	public void testSetCurrentPageInputBad() throws Exception{
		// TODO:  figure out why maxpage is equal to zero here.
		assertTrue(PDFPreview.setCurrentPage(-1) == false);
		
	}
	
	@Test
	public void testLeftPage() throws Exception{
		PDFPreview.previewPDFDocumentInImage(t);
		PDFPreview.setCurrentPage(4);
		PDFPreview.leftPage();
		assertTrue(PDFPreview.getCurrentPage() == 3);
		assertFalse(PDFPreview.getCurrentPage() == 2);
	}
	
	@Test
	public void testRightPage() throws Exception{
		PDFPreview.previewPDFDocumentInImage(t);
		System.out.println(PDFPreview.getCurrentPage() == 1);
		PDFPreview.rightPage();
		assertTrue(PDFPreview.getCurrentPage() == 2);
	}

}
