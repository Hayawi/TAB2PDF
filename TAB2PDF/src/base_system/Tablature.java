package base_system;


/* This class is the document which is being built to be writing as a PDF file.
 * It contains the original ascii as a single string, individial parts 
 * of the file.   For example it hold the header as seperate data from the
 * music tabs. 
 */
public class Tablature {
	
	private String s;
	private String[] header;
	private boolean headerExists;
	private String body;
	private boolean bodyExists;
	
	public Tablature(String s){
		this.setAscii(s);
		this.header = this.parseHeader();
		this.body = this.parseBody();
	}
	
	public boolean setAscii(String s){
		this.s = s;
		return true;
	}
	
	public String getAscii(){
		return this.s;
	}
	
	public String[] getHeader(){
		return this.header;
	}
	
	/*
	 * A tester that tells if the input ASCII file has 
	 * a header which includes a title, subtitle and
	 * spacing.
	 */
	public boolean checkHeaderExists() {
		String[] parts = this.s.split("=|\n");
		return parts[0].compareTo("TITLE") == 0 &&
				parts[2].compareTo("SUBTITLE") == 0 &&
				parts[4].compareTo("SPACING") == 0;
	}
	
	/*
	 * Parses the title, subtitle and spacing of the ascii file.
	 */
	public String[] parseHeader() {
		if(checkHeaderExists()){
			this.headerExists = true;
		String[] parts = this.s.split("=|\n");
		String[] titSubSpace = new String[3];
		titSubSpace[0] = parts[1];
		titSubSpace[1] = parts[3];
		titSubSpace[2] = parts[5];
		return titSubSpace;
		}
		else{
			// insert exception here.
			this.headerExists = false;
			return null;
		}
				
	}

	public boolean hasHeader(){
		return this.headerExists;
	}
	
	public String parseBody() {
		int startOfBody = this.s.indexOf('|');
		if(startOfBody == -1){
			bodyExists = false;
			return null;
		}
		else{
			bodyExists = true;
			return s.substring(startOfBody);
		}	
	}
	
	public boolean hasBody(){
		return this.bodyExists;
	}
	
	public String getBody(){
		return this.body;
	}

}
