package output;
import java.io.FileInputStream;
import java.io.IOException;


import org.apache.commons.io.IOUtils;

public class ReadASCII {

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
