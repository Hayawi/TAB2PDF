package base_system;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
/*
 * Utility class for reading Ascii files.
 */
public class ReadASCII {
	
	/*
	 * Opens the specified file and reads in all the text into one string
	 * which it then returns. 
	 */
	public static String openFile(String filename) throws IOException{
		  FileInputStream inputStream = new FileInputStream(filename);
		  String everything;
		    try {
		        everything = IOUtils.toString(inputStream);
		    } finally {
		        inputStream.close();
		    }
		    return everything;
	}

}
