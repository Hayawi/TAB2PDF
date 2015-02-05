package addon_functionality;

import base_system.Creater;
import base_system.PreviewCreater;
import base_system.Tablature;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.*;
import java.nio.ByteBuffer;

import javax.swing.*;

public class PDFPreview {
	
	public static void previewPDFDocumentInImage(Tablature tab)
			throws IOException {
		
		ByteBuffer buf = null;
		buf = ByteBuffer.wrap(new PreviewCreater().writePDFInMemory(tab).toByteArray());
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
		// Mina change if you want
		JFrame frame = new JFrame("PDF Preview");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JLabel(new ImageIcon(img)));
		frame.pack();
		frame.setVisible(true);
	}
}