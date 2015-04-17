import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

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
	private Slider zoomSlider;
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
	@FXML
	private Button browsePDF;
	@FXML
	private Button basicButton;
	@FXML
	private Button basicButtonhover;
	@FXML
	private Button advancedButtonhover;
	@FXML
	private Button helpButton;
	@FXML
	private Button basicopenPDF;
	@FXML
	private Button basicopenFolder;
	@FXML
	private Button basicConvert;
	@FXML
	private Label selectFile;
	@FXML
	private Button backButton;
	@FXML
	private Button folder;
	@FXML
	private Button apply;
	@FXML
	private Button advancedPDF;
	@FXML
	private Button advancedFolder;
	@FXML
	private Button advancedConvert;
	@FXML
	private ScrollPane scroll;
	@FXML
	private Label spacingLabel;
	@FXML
	private Label errorMessage;
	@FXML
	private Label error;
	@FXML
	private Button okay;
	
	public void basicHover(){
	
		basicButtonhover.setStyle("-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;");
		basicButtonhover.setEffect(new DropShadow());
		
		
	}
	public void basicUnhover(){
		basicButtonhover.setStyle("-fx-background-color: #30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;");
		basicButtonhover.setEffect(null);
	}
	
	public void advancedHover(){
		advancedButtonhover.setStyle("-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;");
		advancedButtonhover.setEffect(new DropShadow());
	}
	public void advancedUnhover(){
		advancedButtonhover.setStyle("-fx-background-color: #30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;");
		advancedButtonhover.setEffect(null);
	}
	
	public void helpHover(){
		helpButton.setStyle("-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;");
		helpButton.setEffect(new DropShadow());
	}
	public void helpUnhover(){
		helpButton.setStyle("-fx-background-color: #30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;");
		helpButton.setEffect(null);
	}
	
	
	public void basicopenPDFHover(){
		basicopenPDF.setStyle("-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
		basicopenPDF.setEffect(new DropShadow());
	}
	public void basicopenPDFUnhover(){
		basicopenPDF.setStyle("-fx-background-color: #30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
		basicopenPDF.setEffect(null);
	}
	public void basicopenFolderHover(){
		basicopenFolder.setStyle("-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
		basicopenFolder.setEffect(new DropShadow());
	}
	public void basicopenFolderUnhover(){
		basicopenFolder.setStyle("-fx-background-color: #30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
		basicopenFolder.setEffect(null);
	}
	public void basicConvertHover(){
		basicConvert.setStyle("-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
		basicConvert.setEffect(new DropShadow());
	}
	public void basicConvertUnhover(){
		basicConvert.setStyle("-fx-background-color: #30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
		basicConvert.setEffect(null);
	}
	public void choosePDFHover(){
		choosePDF.setStyle("-fx-font: 22px \"Roboto Light\"; -fx-font-fill: white;-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
		choosePDF.setEffect(new DropShadow());
	}
	public void choosePDFUnhover(){
		
		choosePDF.setStyle("-fx-font: 22px \"Roboto Light\"; -fx-font-fill: white;-fx-background-color:#30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
		choosePDF.setEffect(null);
	}
	
	public void tablatureHover(){
		tablature.setStyle("-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
		tablature.setEffect(new DropShadow());
	}
	public void tablatureUnhover(){
		tablature.setStyle("-fx-background-color: #30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
		tablature.setEffect(null);
	}
	
	public void backHover(){
		backButton.setStyle("-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;");
		backButton.setEffect(new DropShadow());
	}
	public void backUnhover(){
		backButton.setStyle("-fx-background-color: #30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;");
		backButton.setEffect(null);
	}
	
	public void titleColorHover(){
		titleColor.setStyle("-fx-background-color:#0072bc;");
		titleColor.setEffect(new DropShadow());
	}
	public void titleColorUnhover(){
		titleColor.setStyle("-fx-background-color:#636363;");
		titleColor.setEffect(null);
	}
	public void subtitleColorHover(){
		subtitleColor.setStyle("-fx-background-color:#0072bc;");
		subtitleColor.setEffect(new DropShadow());
	}
	public void subtitleColorUnhover(){
		subtitleColor.setStyle("-fx-background-color:#636363;");
		subtitleColor.setEffect(null);
	}
	public void colorHover(){
		ColorChooser.setStyle("-fx-background-color:#0072bc;");
		ColorChooser.setEffect(new DropShadow());
	}
	public void colorUnhover(){
		ColorChooser.setStyle("-fx-background-color:#636363;");
		ColorChooser.setEffect(null);
	}
	
	public void browseHover(){
		browsePDF.setStyle("-fx-background-color:#0072bc; -fx-border-radius:5; -fx-pref-height:37;");
		browsePDF.setEffect(new DropShadow());
	}
	public void browseUnhover(){
		browsePDF.setStyle("-fx-background-color:#30302f; -fx-border-radius:5; -fx-pref-height:37;");
		browsePDF.setEffect(null);
	}
	public void folderHover(){
		folder.setStyle("-fx-background-color:#0072bc; -fx-border-radius:5; -fx-pref-height:37;");
		folder.setEffect(new DropShadow());
	}
	public void folderUnhover(){
		folder.setStyle("-fx-background-color:#30302f; -fx-border-radius:5; -fx-pref-height:37;");
		folder.setEffect(null);
	}
	
	public void applyHover(){
		apply.setStyle("-fx-background-color:#0072bc; -fx-border-radius:5; -fx-pref-height:33;");
		apply.setEffect(new DropShadow());
	}
	public void applyUnhover(){
		apply.setStyle("-fx-background-color:#30302f; -fx-border-radius:5; -fx-pref-height:33;");
		apply.setEffect(null);
	}
	public void advancedPDFHover(){
		advancedPDF.setStyle("-fx-background-color:#0072bc; -fx-border-radius:5;");
		advancedPDF.setEffect(new DropShadow());
	}
	public void advancedPDFUnhover(){
		advancedPDF.setStyle("-fx-background-color:#30302f; -fx-border-radius:5;");
		advancedPDF.setEffect(null);
	}
	public void advancedFolderHover(){
		advancedFolder.setStyle("-fx-background-color:#0072bc; -fx-border-radius:5;");
		advancedFolder.setEffect(new DropShadow());
	}
	public void advancedFolderUnhover(){
		advancedFolder.setStyle("-fx-background-color:#30302f; -fx-border-radius:5;");
		advancedFolder.setEffect(null);
	}
	public void advancedConvertHover(){
		
		advancedConvert.setEffect(new DropShadow());
	}
	public void advancedConvertUnhover(){
		
		advancedConvert.setEffect(null);
	}
		
	public void leftHover(){
		leftPage.setStyle("-fx-background-color:#0072bc; -fx-border-radius:5; ");
		leftPage.setEffect(new DropShadow());
	}
	public void leftUnhover(){
		leftPage.setStyle("-fx-background-color:#30302f; -fx-border-radius:5; ");
		leftPage.setEffect(null);
	}
	public void rightHover(){
		rightPage.setStyle("-fx-background-color:#0072bc; -fx-border-radius:5; ");
		rightPage.setEffect(new DropShadow());
	}
	public void rightUnhover(){
		rightPage.setStyle("-fx-background-color:#30302f; -fx-border-radius:5; ");
		rightPage.setEffect(null);
	}
	
	public void openPDF() throws IOException{
		try{		
		if (Desktop.isDesktopSupported()) {
		    File myFile = new File((String) choosePDF.getValue());
		    
		    //GUI.dir.indexOf(myFile);
		    Desktop.getDesktop().open(myFile);
		}
	} catch (NullPointerException name) {}
	}
	public void openFolder() throws IOException{
		try{
		if (Desktop.isDesktopSupported()) {
			
			String directoryname = (String) choosePDF.getValue();
		    File myFile = new File(directoryname.substring(0,directoryname.lastIndexOf('\\')+1));
		    Desktop.getDesktop().open(myFile);
		}
	} catch (NullPointerException name) {}
	}
	public void openWebsite() throws IOException{
		
		if (Desktop.isDesktopSupported()) {
		    String htmlFile = "https://docs.google.com/document/d/1c9ieJbA7ZBDRBE2fL4T_ZOh1aMUGF1Iuo86CKayG7Kg/edit";
		    
		    //GUI.dir.indexOf(myFile);
		    Desktop.getDesktop().browse(URI.create(htmlFile));
		}
		
	}
	
	public void advancedPDF() throws IOException{
		try{
		if (Desktop.isDesktopSupported()) {
		    File myFile = new File(GUI.outputPath);
		    
		    //GUI.dir.indexOf(myFile);
		    Desktop.getDesktop().open(myFile);
		}
	} catch (IllegalArgumentException name) {
		
	}catch(NullPointerException e){}
	}
	
	public void advancedFolder() throws IOException{
		try{
		if (Desktop.isDesktopSupported()) {
			
			String directoryname = GUI.outputPath;
		    File myFile = new File(directoryname.substring(0,directoryname.lastIndexOf('\\')+1));
		    Desktop.getDesktop().open(myFile);
		}
		} catch (IllegalArgumentException name) {}catch(NullPointerException e){}
		}
	
	
	public void showBasic() throws IOException{
		
		GUI.customizeSelected = false;
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/BasicMode5.0.fxml"));

		Scene scene = new Scene(root, 1046, 768);
	
		GUI.main.setScene(scene);
		GUI.main.centerOnScreen();
		GUI.main.show();

	}
		
	public void browse() throws IOException, DocumentException {
		try{
			
		// file chooser
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Open Text File");
		
		File file = fileChooser.showOpenDialog(GUI.main);
		
		if(!file.getAbsolutePath().endsWith(".txt")){
			showError("Please only select text files (.txt)");
			return;
		}
		
		inputField.setText(file.getPath());
		outputField.setText(file.getPath().substring(file.getPath().lastIndexOf('\\') + 1,file.getPath().length() - 4));
		destinationFolder.setText(file.getPath().substring(0,file.getPath().lastIndexOf('\\') + 1));
	    
		String outputPath = destinationFolder.getText() + outputField.getText() + ".pdf";
		
		Tablature tab = new Tablature(file.getPath(), outputPath);
		
		
		spacingslider.setValue(tab.getSpacing());
		spacingLabel.setText(Float.toString(tab.getSpacing()));
		previewPage.setImage(PDFPreview.previewPDFDocumentInImage(tab));
		ColorChooser.setValue(javafx.scene.paint.Color.BLACK);
		titleColor.setValue(javafx.scene.paint.Color.BLACK);
		subtitleColor.setValue(javafx.scene.paint.Color.BLACK);
		
		advancedConvert.setDisable(false);
		
		advancedPDF.setDisable(true);
		advancedFolder.setDisable(true);
		
		advancedPDF.setStyle("-fx-background-color:#30302f; -fx-border-radius:5;");
		advancedFolder.setStyle("-fx-background-color:#30302f; -fx-border-radius:5;");
		
		maxPages.setText(Integer.toString(PDFPreview.getMaxPage()));
		pageCounter.setText(Integer.toString(1));
		
		leftPage.setDisable(true);
		
		if(PDFPreview.getMaxPage() > 1){
			rightPage.setDisable(false);
		}
		
	    
	    previewPage.setFitWidth(zoomSlider.getValue()*5.5);
	    previewPage.setFitHeight(zoomSlider.getValue()*5.5);
	    
//		if(PDFPreview.getCurrentPage() < PDFPreview.getMaxPage()){
//			rightPage.setDisable(false);	
//		}
//		
		}catch(NullPointerException e){	}
		
		catch(InvalidMeasureException e){
			showError(e.getMessage());
			return;
		}catch(EmptyTablatureException e){
			showError(e.getMessage());
			return;
		}
		
		
		// enable the user to press the convert button
		
	//	convertButton.setDisable(false);
	}	
	public void selectMultiple() throws IOException{
		
		try {

		
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Open Text File");
		List<File> dir = fileChooser.showOpenMultipleDialog(GUI.main);
		GUI.dir = dir;
		for(File f :dir){
			if(!f.getAbsolutePath().endsWith(".txt")){
				showError("Please only select text files (.txt)");
				return;
			}
		}
		if (dir.size() > 0){
			
			basicConvert.setDisable(false);
			tablature.setStyle("-fx-background-color: #30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65;"); 
			basicConvert.setStyle("-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
			
			basicopenPDF.setDisable(true);
			choosePDF.setDisable(true);  
		    basicopenFolder.setDisable(true);
		    
		    selectFile.setVisible(true);
		    basicopenPDF.setStyle("-fx-background-color: #30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
		    basicopenFolder.setStyle("-fx-background-color: #30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
			choosePDF.setStyle("-fx-font: 22px \"Roboto Light\"; -fx-font-fill: white;-fx-background-color:#30302f;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");

		    
		}
		
		
		
		} catch (NullPointerException name) {}
		
	}
	
	public void convertMultiple() throws IOException{
		
		
		ObservableList<String> choices = FXCollections.observableArrayList();
		
		for(File f:GUI.dir){
					
			String output = f.getPath().substring(0,f.getPath().length()-3) + "pdf";
			
			try{
			Tablature tab = new Tablature(f.getPath(), output);
			DrawPDF.writePDF(tab);
			}catch(FileNotFoundException e){
					if (e.toString().contains("The process cannot access the file because it is being used by another process")){
				   // JOptionPane.showMessageDialog(null, "Please close the file before converting.", "Error",JOptionPane.ERROR_MESSAGE);
						showError( "Please close the file before converting.");
					}
					else if (e.toString().contains("Access is denied")) {
					    
						showError( "Cannot output file to this directory, please select another directory.");
					}
					else if (e.toString().contains("The system cannot find the path specified")) {
	
						showError( "The output directory does not exist.");

					}
					else {
						showError( "Error");
					
					}
					return;
			}catch(DocumentException e){
				showError( "Document Exception");
				return;

			}catch(InvalidMeasureException e){
				showError(e.getMessage());
				return;
			}catch(EmptyTablatureException e){
				showError(e.getMessage());
				return;
			}
			choices.add(output);
		}
		
        choosePDF.setItems(choices);
		choosePDF.getSelectionModel().select(0);
		choosePDF.setStyle("-fx-font: 16px \"Roboto Light\"; -fx-font-fill: white;-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
		
		selectFile.setVisible(false);
	    choosePDF.setDisable(false);  
	    basicopenFolder.setDisable(false);
	    basicopenPDF.setDisable(false);
	    
	    basicopenFolder.setStyle("-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
	    basicopenPDF.setStyle("-fx-background-color:#0072bc;-fx-border-width:0.4;-fx-border-color:white;-fx-border-style:solid;-fx-border-radius:5;-fx-pref-height:65");
	    

		
	}
	
	public void selectFolder(){
		try{
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File directory = directoryChooser.showDialog(GUI.main);
		
		destinationFolder.setText(directory.getPath());
	}catch(NullPointerException e){}
		
	}
	public void convert() throws IOException {
		
	//	openFolder.setDisable(false);
//		openPDF.setDisable(false);
		try{
			
		if(destinationFolder.getText().charAt(destinationFolder.getText().length()-1) != '\\')	{
			destinationFolder.setText(destinationFolder.getText()+ "\\");
		}
		String inputPath = inputField.getText();
		String outputPath = destinationFolder.getText() + outputField.getText() + ".pdf";
		
		GUI.outputPath = outputPath;
		BaseColor f = new BaseColor((float)ColorChooser.getValue().getRed(), (float)ColorChooser.getValue().getGreen(),(float)ColorChooser.getValue().getBlue());
		BaseColor t = new BaseColor((float)titleColor.getValue().getRed(), (float)titleColor.getValue().getGreen(),(float)titleColor.getValue().getBlue());
		BaseColor s = new BaseColor((float)subtitleColor.getValue().getRed(), (float)subtitleColor.getValue().getGreen(),(float)subtitleColor.getValue().getBlue());
		try{
		Tablature tab = new Tablature(inputPath, outputPath);
		
		tab.setSpacing((float)spacingslider.getValue());
		tab.setFontColor(f);
		tab.setTitleColor(t);
		tab.setSubtitleColor(s);
		
		if(titleField.getText().length() >0){
			tab.setTitle(titleField.getText());
		}
		
		if(subtitleField.getText().length() >0){
			tab.setSubtitle(subtitleField.getText());
		}
		
		
			DrawPDF.writePDF(tab);
		}catch(FileNotFoundException e){
			if (e.toString().contains("The process cannot access the file because it is being used by another process")){
		   // JOptionPane.showMessageDialog(null, "Please close the file before converting.", "Error",JOptionPane.ERROR_MESSAGE);
				showError( "Please close the file before converting.");
			}
			else if (e.toString().contains("Access is denied")) {
			    
				showError( "Cannot output file to this directory, please select another directory.");
			}
			else if (e.toString().contains("The system cannot find the path specified")) {

				showError( "The output directory does not exist.");

			}
			else {
				showError( "Error");
			
			}
			return;
		}catch(DocumentException e){
			showError( "Document Exception");
			return;
		}catch(InvalidMeasureException e){
			showError(e.getMessage());
			return;
		}catch(EmptyTablatureException e){
			showError(e.getMessage());
			return;
		}
		
		advancedPDF.setDisable(false);
		advancedFolder.setDisable(false);
		
		advancedPDF.setStyle("-fx-background-color:#0072bc; -fx-border-radius:5;");
		advancedFolder.setStyle("-fx-background-color:#0072bc; -fx-border-radius:5;");
		
		}catch(FileNotFoundException e){}catch(NullPointerException e){}
			
	}

	public void showMainMenu() throws IOException {
	
		GUI.customizeSelected = false;
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/BasicMode5.0.fxml"));

		Scene scene = new Scene(root, 1046, 768);
	
		GUI.main.setScene(scene);
		GUI.main.centerOnScreen();
		GUI.main.show();
	}

	public void showAdvancedMode() throws IOException {
		
		GUI.customizeSelected = true;
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdvancedMode5.0.fxml"));
		Scene scene = new Scene(root, 1046, 768);

		GUI.main.setScene(scene);
		GUI.main.centerOnScreen();
		GUI.main.show();
		

	}
	
	
	public void preview() throws IOException, DocumentException {
		
			
		try{
		String inputPath = inputField.getText();
		String outputPath = destinationFolder.getText() + outputField.getText() + ".pdf";
		Tablature tab = new Tablature(inputPath, outputPath);
		BaseColor f = new BaseColor((float)ColorChooser.getValue().getRed(), (float)ColorChooser.getValue().getGreen(),(float)ColorChooser.getValue().getBlue());
		BaseColor t = new BaseColor((float)titleColor.getValue().getRed(), (float)titleColor.getValue().getGreen(),(float)titleColor.getValue().getBlue());
		BaseColor s = new BaseColor((float)subtitleColor.getValue().getRed(), (float)subtitleColor.getValue().getGreen(),(float)subtitleColor.getValue().getBlue());
		
		tab.setFontColor(f);
		tab.setTitleColor(t);
		tab.setSubtitleColor(s);
		tab.setSpacing((float)spacingslider.getValue());
		
		if(titleField.getText().length() >0){
			tab.setTitle(titleField.getText());	
		}
		
		if(subtitleField.getText().length() >0){
			tab.setSubtitle(subtitleField.getText());
		}
		PDFPreview.setCurrentPage(1);
		previewPage.setImage(PDFPreview.previewPDFDocumentInImage(tab));
		
		
		if(PDFPreview.getMaxPage() == 1){
			rightPage.setDisable(true);
		}
		if(PDFPreview.getMaxPage() > 1){
			rightPage.setDisable(false);
		}
		
		leftPage.setDisable(true);
		pageCounter.setText(Integer.toString(1));
		maxPages.setText(Integer.toString(PDFPreview.getMaxPage()));
		
	} catch (FileNotFoundException name) {}
	}

	public void zoom(){
		
		//previewPage.setScaleX((-25 +zoomSlider.getValue()) /100);
	   // previewPage.setScaleY((-25+zoomSlider.getValue()) / 100);
	    
		previewPage.setFitWidth(zoomSlider.getValue()*5.5);
	    previewPage.setFitHeight(zoomSlider.getValue()*5.5);
	}
	
	
	public void turnRight() throws IOException, DocumentException {

		leftPage.setDisable(false);

		if (PDFPreview.getCurrentPage() + 1 == PDFPreview.getMaxPage()) {
			rightPage.setDisable(true);
		}

		pageCounter.setText(Integer.toString(Integer.parseInt(pageCounter.getText()) + 1));

		if (PDFPreview.getCurrentPage() < PDFPreview.getMaxPage()) {
			PDFPreview.rightPage();
		}
		
		previewPage.setImage(PDFPreview.updatePreviewPDFDocument());
	}

	public void turnLeft() throws IOException, DocumentException {

		rightPage.setDisable(false);

		if (PDFPreview.getCurrentPage() > 1) {
			PDFPreview.leftPage();
		}
		pageCounter.setText(Integer.toString(Integer.parseInt(pageCounter.getText()) - 1));
		
		if (PDFPreview.getCurrentPage() == 1) {
			leftPage.setDisable(true);
		}

		previewPage.setImage(PDFPreview.updatePreviewPDFDocument());

	}
	public void spacingLabel(){
		
		DecimalFormat oneDigit = new DecimalFormat("#,##0.0");
		String rounded = oneDigit.format(spacingslider.getValue());
		spacingLabel.setText(rounded);
	}
	
	public void showError(String message) throws IOException{
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText("Error");
		alert.setContentText(message);

		alert.showAndWait();
		
	}

}
