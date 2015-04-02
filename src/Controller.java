import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	@FXML
	private ColorPicker titleColor;
	@FXML
	private ColorPicker subtitleColor;
	@FXML
	private TextField titleField;
	@FXML
	private TextField subtitleField;
	
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
	public void openWebsite() throws IOException{
		
		if (Desktop.isDesktopSupported()) {
		    String htmlFile = "https://docs.google.com/document/d/1c9ieJbA7ZBDRBE2fL4T_ZOh1aMUGF1Iuo86CKayG7Kg/edit";
		    
		    //GUI.dir.indexOf(myFile);
		    Desktop.getDesktop().browse(URI.create(htmlFile));
		}
		
	}
	
	public void advancedPDF() throws IOException{
		
		if (Desktop.isDesktopSupported()) {
		    File myFile = new File(GUI.outputPath);
		    
		    //GUI.dir.indexOf(myFile);
		    Desktop.getDesktop().open(myFile);
		}
		
	}
	
	public void advancedFolder() throws IOException{
		
		if (Desktop.isDesktopSupported()) {
			
			String directoryname = GUI.outputPath;
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
		
		inputField.setText(file.getPath());
		outputField.setText(file.getPath().substring(file.getPath().lastIndexOf('\\') + 1,file.getPath().length() - 4));
		destinationFolder.setText(file.getPath().substring(0,file.getPath().lastIndexOf('\\') + 1));
	    
		String outputPath = destinationFolder.getText() + outputField.getText() + ".pdf";
		
		previewPage.setImage(PDFPreview.previewPDFDocumentInImage(new Tablature(file.getPath(),outputPath)));
		ColorChooser.setValue(javafx.scene.paint.Color.BLACK);
		titleColor.setValue(javafx.scene.paint.Color.BLACK);
		subtitleColor.setValue(javafx.scene.paint.Color.BLACK);
		
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
		
		try {

		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Text File");
		List<File> dir = fileChooser.showOpenMultipleDialog(GUI.main);
		GUI.dir = dir;
		
		if (dir.size() > 0){
			checkSelect.setVisible(true);
			convertMultiple.setDisable(false);
		}
		
		checkConvert.setVisible(false);
		} catch (NullPointerException name) {
			
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
		
	}
	public void convert() throws IOException, DocumentException {
		
		openFolder.setDisable(false);
		openPDF.setDisable(false);
		
		String inputPath = inputField.getText();
		String outputPath = destinationFolder.getText() + outputField.getText() + ".pdf";
		
	
		GUI.outputPath = outputPath;
		BaseColor f = new BaseColor((float)ColorChooser.getValue().getRed(), (float)ColorChooser.getValue().getGreen(),(float)ColorChooser.getValue().getBlue());
		
		Tablature tab = new Tablature(inputPath, outputPath);
		
		tab.setSpacing((float)spacingslider.getValue());
		tab.setFontColor(f);
		
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
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/BasicMode2.0.fxml"));
		Scene scene = new Scene(root, 1046, 768);
	
		GUI.main.setScene(scene);
		GUI.main.centerOnScreen();
		GUI.main.show();
		
	}
	
	
	public void preview() throws IOException, DocumentException {
		
			
		
		String inputPath = inputField.getText();
		String outputPath = destinationFolder.getText() + outputField.getText() + ".pdf";
		Tablature tab = new Tablature(inputPath, outputPath);
		BaseColor f = new BaseColor((float)ColorChooser.getValue().getRed(), (float)ColorChooser.getValue().getGreen(),(float)ColorChooser.getValue().getBlue());
		tab.setFontColor(f);
		tab.setSpacing((float)spacingslider.getValue());
		previewPage.setImage(PDFPreview.previewPDFDocumentInImage(tab));
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
