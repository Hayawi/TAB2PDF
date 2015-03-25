import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.itextpdf.text.DocumentException;
/* This class is the document which is being built to be writing as a PDF file.
 * It contains the original ascii as a single string, individial parts 
 * of the file.   For example it hold the header as seperate data from the
 * music tabs. 
 */
public class Tablature {
	
	private String s;
	private String title = "";
	private String subtitle = "";
	private String filename;
	private String filepath;
	private float spacing;
	private int fontSize;
	private ArrayList<Measure> staves;

	private String body;

	public void draw() throws IOException {
		DrawPDF.writePDF(this);
	}
	
	public void setOutputPath(String filepath) {
		this.filepath = filepath;
	}
	
	public String getOutputPath () {
		return this.filepath;
	}
	
	public Tablature(String inputPath, String outputPath) throws IOException {
		this.fontSize = 8;
		String file = ParseFile.openFile(inputPath);
		this.filepath = outputPath;
		this.processFile(file);
	}
	
	public boolean setAscii(String s){
		this.s = s;
		return true;
	}
	
	public String getAscii(){
		return this.s;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getSubtitle() {
		return this.subtitle;
	}
	
	public float getSpacing() {
		return this.spacing;
	}
	
	public int getFontSize() {
		return this.fontSize;
	}
	
	public ArrayList getMeasures() {
		return this.staves;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	// fix later 
	private void processFile(String file) {
		char newLine = '\n';
		int indexOfTitle = file.indexOf("TITLE="); 
		
		if (indexOfTitle >= 0) {
			this.title = file.substring(indexOfTitle + "TITLE=".length() , file.indexOf(newLine, indexOfTitle) - 1).trim();
		}
		

		int indexOfSubtitle = file.indexOf("SUBTITLE=");
		
		if (indexOfSubtitle >= 0) {
			this.subtitle = file.substring(indexOfSubtitle + "SUBTITLE=".length() , file.indexOf(newLine, indexOfSubtitle) - 1).trim();
		}
		

		int indexOfSpacing= file.indexOf("SPACING=");
		
		if (indexOfSpacing >= 0) {
			this.spacing = Float.parseFloat(file.substring(indexOfSpacing + "SPACING=".length() , file.indexOf(newLine, indexOfSpacing)).trim()); 
		}
		else
			this.spacing = 5.0f;
		
		Pattern pattern = Pattern.compile("\\|?(\\||^[0-9])-");
		Matcher matcher = pattern.matcher(file);
		
		if (matcher.find())
			file = file.substring(matcher.start());
		else 
			file = "";
		this.body = file;
		this.staves = ParseFile.sortMeasure(file);
	}
	
	

}
