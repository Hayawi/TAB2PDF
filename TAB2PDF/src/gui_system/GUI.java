package gui_system;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
