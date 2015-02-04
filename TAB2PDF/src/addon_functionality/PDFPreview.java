package addon_functionality;

import base_system.CreatePDF;
import base_system.ReadASCII;
import base_system.Tablature;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.*;

public class PDFPreview {
	public static void previewPDFDocumentInImage(Tablature tab)
			throws IOException {
		ByteBuffer buf = null;
		buf = ByteBuffer.wrap(CreatePDF.writePDFInMemory(tab).toByteArray());
		// use the PDFRenderer library on the buf which contains the in memory
		// PDF document
		PDFFile pdffile = new PDFFile(buf);
		PDFPage page = pdffile.getPage(1);

		// get the width and height for the doc at the default zoom
		Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
				(int) page.getBBox().getHeight());

		// generate the image
		Image img = page.getImage(rect.width, rect.height, // width &amp; height
				rect, // clip rect
				null, // null for the ImageObserver
				true, // fill background with white
				true) // block until drawing is done
		;

		// show the image in a frame
		JFrame frame = new JFrame("My incredible PDF document");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JLabel(new ImageIcon(img)));
		frame.pack();
		frame.setVisible(true);
	}
}