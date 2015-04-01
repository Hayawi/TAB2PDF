import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.JFileChooser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;

public class Controller {

	@FXML
	private TextField inputField;
	@FXML
	private TextField outputField;
	@FXML 
	private Button convertButton;
	@FXML
	private ImageView previewPage;
	@FXML
	private Slider spacingslider;
	@FXML
	private ColorPicker ColorChooser;
	@FXML
	private Button leftPage;
	@FXML
	private Button rightPage;
	@FXML
	private Label pageCounter;
	@FXML
	private Label maxPages;
	@FXML
	private Button BasicMode;
	@FXML
	private ImageView Cover;
	@FXML
	private Label BasicLabel;
	@FXML
	private Label PDFcomplete;
	@FXML
	private Button Back;
	@FXML
	private Button openPDF;
	@FXML
	private ImageView pdfLogo;
	@FXML
	private Separator separator;
	@FXML
	private Separator middle_separator;
	@FXML
	private Pagination previewPagination;
	@FXML
	private Button tablature;
	@FXML
	private Label instructions;
	@FXML
	private TextField destinationFolder;
	@FXML
	private ComboBox choosePDF;
	@FXML
	private CheckBox selected;
	@FXML
	private CheckBox converted;
	@FXML
	private Button openFolder;
	@FXML
	private ImageView checkConvert; 
	@FXML
	private ImageView checkSelect;
	@FXML
	private Button convertMultiple;

	public void openPDF() throws IOException{
				
		if (Desktop.isDesktopSupported()) {
		    File myFile = new File((String) choosePDF.getValue());
		    
		    //GUI.dir.indexOf(myFile);
		    Desktop.getDesktop().open(myFile);
		}
	}
	public void openFolder() throws IOException{
		
		if (Desktop.isDesktopSupported()) {
			
			String directoryname = (String) choosePDF.getValue();
		    File myFile = new File(directoryname.substring(0,directoryname.lastIndexOf('\\')+1));
		    Desktop.getDesktop().open(myFile);
		}
	}
	
	public void showBasic(){
				
		pdfLogo.setVisible(false);
		BasicMode.setVisible(false);
		BasicLabel.setVisible(false);
		
		tablature.setVisible(true);
		convertMultiple.setVisible(true);
		choosePDF.setVisible(true);
		openPDF.setVisible(true);
		openFolder.setVisible(true);

	}
	
	
	
	public void browse() throws IOException, DocumentException {
		
		// file chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Text File");
		
		File file = fileChooser.showOpenDialog(GUI.main);
		GUI.inputPath = file.getPath();
		GUI.outputName = GUI.inputPath.substring(GUI.inputPath.lastIndexOf('\\') + 1,GUI.inputPath.length() - 4);
		GUI.outputPath = GUI.inputPath.substring(0,GUI.inputPath.lastIndexOf('\\') + 1)	+ GUI.outputName + ".pdf"; 
		
		inputField.setText(GUI.inputPath);
		outputField.setText(GUI.inputPath.substring(GUI.inputPath.lastIndexOf('\\') + 1,GUI.inputPath.length() - 4));
		destinationFolder.setText(GUI.inputPath.substring(0,GUI.inputPath.lastIndexOf('\\') + 1));
	    
		previewPage.setImage(PDFPreview.previewPDFDocumentInImage(new Tablature(GUI.inputPath,GUI.outputPath)));
			
		//pageCounter.setText(Integer.toString(1));
		//	maxPages.setText(Integer.toString(PDFPreview.getMaxPage()));
			
		//leftPage.setVisible(true);
		//	leftPage.setDisable(true);
		//	rightPage.setVisible(true);
			
		//	if(PDFPreview.getCurrentPage() == PDFPreview.getMaxPage()){
		//		rightPage.setDisable(true);
			
	//	}
		
		// enable the user to press the convert button
		
	//	convertButton.setDisable(false);
	}	
	public void selectMultiple() throws IOException{
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Text File");
		List<File> dir = fileChooser.showOpenMultipleDialog(GUI.main);
		GUI.dir = dir;
		
		if (dir.size() > 0){
			checkSelect.setVisible(true);
			convertMultiple.setDisable(false);
		}
	}
	
	public void convertMultiple() throws IOException{
		
		ObservableList<String> choices = FXCollections.observableArrayList();
		
		for(File f:GUI.dir){
			String output = f.getPath().substring(0,f.getPath().length()-3) + "pdf";
			Tablature tab = new Tablature(f.getPath(), output);
			DrawPDF.writePDF(tab);
			
			choices.add(output);
		}
		
		choosePDF.setItems(choices);
		choosePDF.getSelectionModel().select(0);
	    choosePDF.setDisable(false);  
	    PDFcomplete.setDisable(false);
	    openPDF.setDisable(false);
	    openFolder.setDisable(false);
	    checkConvert.setVisible(true);
		
	}
	
	public void selectFolder(){
		
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File directory = directoryChooser.showDialog(GUI.main);
		
		destinationFolder.setText(directory.getPath());
		GUI.destinationFolder = directory.getPath();
		GUI.outputPath = GUI.destinationFolder + '\\'+GUI.outputName + ".pdf";
	}
	public void convert() throws IOException, DocumentException {
		
		GUI.outputName = outputField.getText();
		GUI.outputPath = GUI.destinationFolder + '\\'+  GUI.outputName + ".pdf";
		
		Tablature tab = new Tablature(GUI.inputPath, GUI.outputPath);
		tab.setSpacing((float)spacingslider.getValue());
		
		//BaseColor f = new BaseColor();
		
	//	tab.setFontColor(ColorChooser.getValue());
		DrawPDF.writePDF(tab);
		
	}

	public void showMainMenu() throws IOException {
	
		GUI.customizeSelected = false;
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main3.0.fxml"));
		Scene scene = new Scene(root, 535, 395);
	
		GUI.main.setScene(scene);
		GUI.main.centerOnScreen();
		GUI.main.show();
	}

	public void showAdvancedMode() throws IOException {
		
		GUI.customizeSelected = true;
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/BasicMode.fxml"));
		Scene scene = new Scene(root, 1046, 768);
	
		GUI.main.setScene(scene);
		GUI.main.centerOnScreen();
		GUI.main.show();
		
	}
	
	
	public void preview() throws IOException, DocumentException {
		
		Tablature tab = new Tablature(GUI.inputPath, GUI.outputPath);
		previewPage.setImage(PDFPreview.updatePreviewPDFDocument());
	}
/*
	public void turnRight() throws IOException, DocumentException {

		leftPage.setDisable(false);

		if (PDFPreview.getCurrentPage() + 1 == PDFPreview.getMaxPage()) {
			rightPage.setDisable(true);
		}

		pageCounter.setText(Integer.toString(Integer.parseInt(pageCounter
				.getText()) + 1));

		if (PDFPreview.getCurrentPage() < PDFPreview.getMaxPage()) {
			PDFPreview.rightPage();
		}
		
		preview();
	}

	public void turnLeft() throws IOException, DocumentException {

		rightPage.setDisable(false);

		if (PDFPreview.getCurrentPage() > 1) {
			PDFPreview.leftPage();
		}
		pageCounter.setText(Integer.toString(Integer.parseInt(pageCounter
				.getText()) - 1));
		
		preview();

	}
	*/
}
