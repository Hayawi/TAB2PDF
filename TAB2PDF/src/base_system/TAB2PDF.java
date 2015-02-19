package base_system;

import gui_system.GUI;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import addon_functionality.PDFPreview;
 
/*
 * The main class of the program. This runs the base system, which is run from 
 * the command line.  Other packages may be added to give additional functionality
 * such as a GUI or new features such as convert to file types other than PDF.
 */
public class TAB2PDF {
	
    // chris
	public static void main(String[] args) throws IOException, InterruptedException, DocumentException {
		
		// no arguments supplied, defaults to opening gui for file selection.
		if(args.length == 0){
			GUI gui = new GUI();
			String input;
			while(true){ // busy waiting on input from file selector.
				Thread.sleep(500);
				if(gui.isInputEntered()){
					 input = gui.getInputFileName();
					 break;
				}
			} // end while
			String s = InputUtil.openFile(input);
			Tablature tab = new Tablature(s);
			String output =input.substring(0,5);
			output = input.concat(".pdf");
			new PDFCreater().writePDF(output, tab);
			//PDFPreview.previewPDFDocumentInImage(tab);
			
		}// two arguments should be specified on the commmand line, in CLI system.
		else{ 

		// insert checks for checking proper input formatting.  possibly create methods to do so. 
			
		//read the ascii, to get a string
		String s = InputUtil.openFile(args[0]);
		
		//create a new tablature, with ascii
		Tablature tab = new Tablature(s);
		
		// use methods of tablature to stylize
		// nothing right now, will use default style. (style of sample output1).
		
		//write the pdf
		PDFCreater pdfCreate = new PDFCreater();
		pdfCreate.writePDF(args[1],tab);
		}
	}

	
}
