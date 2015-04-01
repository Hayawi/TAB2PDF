import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


import org.junit.Test;

import com.itextpdf.text.DocumentException;


public class PDFPreviewTest {

	@Test
	public void testCreateImage() throws IOException {
		
		 File file = new File("test.jpg");
		 Image image = ImageIO.read(file);
		javafx.scene.image.Image javafxImage = PDFPreview.createImage(image);
		assertTrue(javafxImage != null);
		
		RenderedImage image2 = (RenderedImage) image;
		javafx.scene.image.Image javafxImage2 = PDFPreview.createImage((Image) image2);
		assertTrue(javafxImage2 != null);
	}

	@Test
	public void testPreviewPDFDocumentInImage() throws IOException, DocumentException {;
		Tablature t = new Tablature("testin.txt","testout.pdf");
		javafx.scene.image.Image i = PDFPreview.previewPDFDocumentInImage(t);
		assertTrue(i != null);
	}

	@Test
	public void testUpdatePreviewPDFDocument() throws IOException, DocumentException {
		Tablature t = new Tablature("testin.txt","testout.pdf");
		PDFPreview.previewPDFDocumentInImage(t);
		javafx.scene.image.Image j = PDFPreview.updatePreviewPDFDocument();
		assertTrue(j != null);	
	}
}
