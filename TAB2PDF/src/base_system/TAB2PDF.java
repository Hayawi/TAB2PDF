package base_system;

import gui_system.GUI;

import java.io.IOException;

import addon_functionality.PDFPreview;
 
/*
 * The main class of the program. This runs the base system, which is run from 
 * the command line.  Other packages may be added to give additional functionality
 * such as a GUI or new features such as convert to file types other than PDF.
 */
public class TAB2PDF {

	public static void main(String[] args) throws IOException, InterruptedException {
		
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
			String s = ReadASCII.openFile(input);
			Tablature tab = new Tablature(s);
			int extensionPos = input.indexOf(".txt");
			String output =input.substring(0,5);
			output = input.concat(".pdf");
			PDFPreview.previewPDFDocumentInImage(tab);
			CreatePDF.writePDF(output,tab);
		}
		else{ // two arguments should be specified on the commmand line, in CLI system.

		// insert checks for checking proper input formatting.  possibly create methodds to do so. 
			
		//read the ascii, to get a string
		String s = ReadASCII.openFile(args[0]);
		
		//create a new tablature, with ascii
		Tablature tab = new Tablature(s);
		
		// use methods of tablature to stylize
		// nothing right now, will use default style. (style of sample output1).
		
		//write the pdf
		CreatePDF.writePDF(args[1],tab);
		}
	}

	
}
