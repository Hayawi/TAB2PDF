package output;

import java.io.IOException;

public class TAB2PDF {

	public static void main(String[] args) throws IOException {
		String s = ReadASCII.openFile(args[0]);
		// insert methods of style here.
		CreatePDF.writePDF(args[1],s);
		
	}

}
