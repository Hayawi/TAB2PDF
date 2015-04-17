import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;

public class Tablature extends Object {
	
	private String s;
	private String title = "";
	private String subtitle = "";
	private String filepath;
	private float spacing;
	private int fontSize;
	private ArrayList<Measure> measures;
	private String body;
	private BaseColor fontColor;
	private BaseColor titleColor;
	private BaseColor subtitleColor;
	private boolean numberedMeasures;

	public void draw() throws IOException, DocumentException {
		DrawPDF.writePDF(this);
	}
	
	public void setOutputPath(String filepath) {
		this.filepath = filepath;
	}
	
	public String getOutputPath () {
		return this.filepath;
	}
	
	public boolean numberMeasures() {
		return numberedMeasures;
	}
	
	public void setNumberMeasures(boolean numberMeasures) {
		this.numberedMeasures = numberMeasures;
	}
	
	public Tablature(String inputPath, String outputPath) throws IOException {
		this.fontSize = 8;
		String file = ParseFile.openFile(inputPath);
		this.filepath = outputPath;
		this.processFile(file);
		this.fontColor = BaseColor.BLACK;
		this.numberedMeasures = false;
		if(measures.size() == 0) {
			throw new EmptyTablatureException("No measures were detected during the conversion of the tablature.");
		}
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
	
	public BaseColor getTitleColor() {
		return this.titleColor;
	}
	
	public void setTitleColor(BaseColor color) {
		this.titleColor = color;
	}
	
	public BaseColor getSubtitleColor() {
		return this.subtitleColor;
	}
	
	public void setSubtitleColor(BaseColor color) {
		this.subtitleColor = color;
	}
	// fix later 
	private void processFile(String file) {
		char newLine = '\n';
		int indexOfTitle = file.indexOf("TITLE="); 
		int indexOfSubtitle = file.indexOf("SUBTITLE=");
		int indexOfSpacing= file.indexOf("SPACING=");
		
		if (indexOfTitle >= 0) {
			this.title = file.substring(indexOfTitle + "TITLE=".length() , file.indexOf(newLine, indexOfTitle)).trim();
		}
		if (indexOfSubtitle >= 0) {
			this.subtitle = file.substring(indexOfSubtitle + "SUBTITLE=".length() , file.indexOf(newLine, indexOfSubtitle)).trim();
		}
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
