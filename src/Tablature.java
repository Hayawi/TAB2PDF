import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.itextpdf.text.BaseColor;

public class Tablature extends Object {
	
	private String s;
	private String title = "";
	private String subtitle = "";
	private String filename;
	private String filepath;
	private float spacing;
	private int fontSize;
	private ArrayList<Measure> measures;
	private String body;
	private BaseColor fontColor;

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
		// System.out.println(inputPath + " " + outputPath);  //debug Chris
		this.fontSize = 8;
		String file = ParseFile.openFile(inputPath);
		this.filepath = outputPath;
		this.processFile(file);
		this.fontColor = BaseColor.BLACK;
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
	
	public ArrayList<Measure> getMeasures() {
		return this.measures;
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

	public BaseColor getFontColor() {
		return this.fontColor;
	}
	
	public void setFontColor(BaseColor color) {
		this.fontColor = color;
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
		this.measures = ParseFile.sortMeasure(file);
	}
	
	

}
