package base_system;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class CreatePDF {
	/*
	 * Writes the actual pdf file.
	 */
	public static boolean writePDF(String filename, Tablature tab) throws IOException{
		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(filename));

			document.open();
			PdfContentByte cb = writer.getDirectContent();

			if(tab.hasHeader()){
				String[] header = tab.getHeader();
				CreatePDF.drawHeader(cb, header);
			}
			
			if(tab.hasBody()){
			
				String body = tab.getBody();
				//System.out.print(body); // debug
				CreatePDF.drawBody(cb,body,document);
			}
			
			document.close(); // no need to close PDFWriter?
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	// use the set spacing and frame indent to calculate how many frames can be drawn on a line.
	
	/*
	 * This has been totally hacked together.  will not work on anything but the first input.
	 */
	public static void drawBody(PdfContentByte cb, String body,Document doc) throws DocumentException, IOException{
	
		int staffCount = 0;
		int yStart = 750;
		int staffSpacing = 70;
		int numOfStaffs = 0;
		int barLineSpacing = 7;
		int xSpacing = 5;
		int frameIndent = 40;
		float yDecrement;
		String[] lines = body.split("\n");
		float xPosition;
		boolean drawNext = false;
		cb.setLineWidth(0.3f);
		
		
		for(int i = 0; i < lines.length; i++){		
			
			xPosition = frameIndent; // where to start drawing stuff.
			yDecrement = i*barLineSpacing;
			
			// if a empty line is found, that means the next 6 lines
			// should be placed adjacent to the last 6 lines drawn, for input1
			if(lines[i].length() == 0 && drawNext == false)
			{
				drawNext = true;
			}
			else if(lines[i].length() == 0 && drawNext == true) // next staff after 2 frames.
			{
				drawNext = false;
				numOfStaffs++;
				staffCount++;
				yStart = yStart - staffSpacing;
				
				if(staffCount > 9){  // makes a new page after certain num of staffs.
					doc.newPage();
					cb.setLineWidth(0.3f);
					staffCount = 0;
					yStart = 790;
				}
			}
			
			//draw left indent only for every other couple of frames, starting with the first one.
			if(drawNext == false && lines[i].length() != 0 ){
				cb.moveTo(0,(yStart +  98 * numOfStaffs ) - yDecrement);
				cb.lineTo(frameIndent,(yStart +  98 * numOfStaffs) - yDecrement);	
				cb.stroke();
				
				cb.moveTo(600,(yStart +  98 * numOfStaffs ) - yDecrement);
				cb.lineTo(600-frameIndent-20,(yStart +  98 * numOfStaffs) - yDecrement);	
				cb.stroke();
			}
		
			// shift the two frames to right side
			if(drawNext){
				xPosition += 125 *2;		
			}
			
			for(int j = 0; j < lines[i].length(); j++){
				
				
				// move up but one staff
				if(drawNext)  
					yDecrement =(i-7)*barLineSpacing;
				
				char c = lines[i].charAt(j);
				if(c == '-'){
					cb.moveTo(xPosition,(yStart +  98 * numOfStaffs ) - yDecrement);
					xPosition += xSpacing;
					cb.lineTo(xPosition+1,(yStart +  98 * numOfStaffs) - yDecrement);	
					cb.stroke();
				}
				else if(Character.isDigit(c)){
					cb.beginText();
					BaseFont bf = BaseFont.createFont();
			        cb.setFontAndSize(bf, 7); 
			        cb.showTextAligned(PdfContentByte.ALIGN_CENTER,String.valueOf(c), xPosition +3, (yStart + 98 * numOfStaffs) - yDecrement-3, 0);
			        cb.endText();
			        xPosition += xSpacing;
				}
				else if(c == '|' && (i % 6) == 0){ 
					// only draws verticle line when detecting '|' on the first line of staff.
			
					cb.moveTo(xPosition, yStart);
					cb.lineTo(xPosition, yStart - 5*barLineSpacing);
					cb.stroke();
					
					//System.out.println(numOfFrames);	
				}
				else if(c == 's'){  // note similar else if will be added to account for other wierd symbols which are denoted by letters.
					
					// draw the normal line for a hyphen
					cb.moveTo(xPosition,(yStart +  98 * numOfStaffs ) - yDecrement);
					xPosition += xSpacing;
					cb.lineTo(xPosition+1,(yStart +  98 * numOfStaffs) - yDecrement);
					cb.stroke();
					
					// go back and draw the line crossing
					xPosition -= xSpacing;
					cb.moveTo(xPosition+2,(yStart +  98 * numOfStaffs ) - yDecrement-2);
					cb.lineTo(xPosition+5,(yStart +  98 * numOfStaffs) - yDecrement+2);	
					cb.stroke();
					
			        xPosition += xSpacing;
			        
				}
				else if(c == 'h'){  // note similar else if will be added to account for other wierd symbols which are denoted by letters.
					
					// draw the normal line for a hyphen
					cb.moveTo(xPosition,(yStart +  98 * numOfStaffs ) - yDecrement);
					xPosition += xSpacing;
					cb.lineTo(xPosition+1,(yStart +  98 * numOfStaffs) - yDecrement);
					cb.stroke();
					
					// go back and draw the line crossing
					xPosition -= xSpacing;
					cb.moveTo(xPosition+1,(yStart +  98 * numOfStaffs ) - yDecrement+2);
					cb.lineTo(xPosition+5,(yStart +  98 * numOfStaffs) - yDecrement+2);	
					cb.stroke();
					
			        xPosition += xSpacing;
			        
				}
				else if(c == 'p'){  // note similar else if will be added to account for other wierd symbols which are denoted by letters.
					
					// draw the normal line for a hyphen
					cb.moveTo(xPosition,(yStart +  98 * numOfStaffs ) - yDecrement);
					xPosition += xSpacing;
					cb.lineTo(xPosition+1,(yStart +  98 * numOfStaffs) - yDecrement);
					cb.stroke();
					
					// go back and draw the line crossing
					xPosition -= xSpacing;
					cb.moveTo(xPosition-xSpacing,(yStart +  98 * numOfStaffs ) - yDecrement+3);
					cb.lineTo(xPosition+xSpacing,(yStart +  98 * numOfStaffs) - yDecrement+3);	
					cb.stroke();	
			        xPosition += xSpacing;
			        
				}	
			}
		}
	}
	
	/*
	 * Draws a stylish header.
	 */
	public static boolean drawHeader(PdfContentByte cb, String[] header) throws DocumentException, IOException {
		cb.beginText();
		BaseFont bf = BaseFont.createFont();
        cb.setFontAndSize(bf, 32);   
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, header[0], 275, 800, 0);
        cb.setFontAndSize(bf, 14); 
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, header[1], 275, 780, 0);
        cb.endText();
	return true;	
	}
	
}
