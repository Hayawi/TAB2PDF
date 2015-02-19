package gui_system;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;


public class GUI extends JPanel{
	
	String inputPath;
	boolean InputEntered = false;
	
	public GUI(){

		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(GUI.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            	this.inputPath = file.getPath();
            	this.InputEntered = true;
        }
        
	}
	public boolean isInputEntered(){
		return this.InputEntered;
	}
	
	public String getInputFileName(){
		return this.inputPath;
	}
}
