package base_system;

import java.io.IOException;
 
/*
 * The main class of the program. This runs the base system, which is run from 
 * the command line.  Other packages may be added to give additional functionality
 * such as a GUI or new features such as convert to file types other than PDF.
 */
public class TAB2PDF {

	public static void main(String[] args) throws IOException {
		
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
