import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.itextpdf.text.DocumentException;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class PDFPreview {

	private static int maxPage;
	private static int currentPage = 1;
	private static ByteBuffer buf;
	
	 public static javafx.scene.image.Image createImage(java.awt.Image image) throws IOException {
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    ImageIO.write((RenderedImage) image, "png", out);
		    out.flush();
		    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		    return new javafx.scene.image.Image(in);
		  }

	public static javafx.scene.image.Image previewPDFDocumentInImage(
			Tablature tab) throws IOException, DocumentException {
		// ByteBuffer buf = null;
		
		buf = ByteBuffer.wrap(DrawPDF.writePDFInMemory(tab).toByteArray());

		// use the PDFRenderer library on the buf which contains the in memory
		// PDF document
		PDFFile pdffile = new PDFFile(buf);
		PDFPage page = pdffile.getPage(currentPage);
		
		maxPage = pdffile.getNumPages();
		
		// get the width and height for the doc at the default zoom
		Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
				(int) page.getBBox().getHeight());

		// generate the image
		Image img = page.getImage(rect.width, rect.height, rect, null, true, true);

		return PDFPreview.createImage(img);
	}
	
	public static int getMaxPage(){
		return maxPage;
	}
	
	public static int getCurrentPage(){
		return currentPage;
	}
	
	public static boolean setCurrentPage(int page) throws Exception{
		if (page >= 1 && page <= maxPage){
			currentPage = page;
			return true;
		}
		else{
			return false;
		}
	}
	
	public static void leftPage(){
		if (currentPage != 1){
			currentPage--;
		}
	}
	
	public static void rightPage(){
		if (currentPage != maxPage){
			currentPage++;
		}
	}
	
	public static javafx.scene.image.Image updatePreviewPDFDocument() throws IOException{
		PDFFile pdffile = new PDFFile(buf);
		PDFPage page = pdffile.getPage(currentPage);
		
		maxPage = pdffile.getNumPages();
		
		// get the width and height for the doc at the default zoom
		Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
				(int) page.getBBox().getHeight());

		// generate the image
		Image img = page.getImage(rect.width, rect.height, rect, null, true, true);

		return PDFPreview.createImage(img);
	}
	
	
}