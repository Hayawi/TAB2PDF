
import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;




public class GUI extends Application {

	public static Stage main;
	public static String inputPath;
	public static String outputName;
	public static String outputPath;
	public static String destinationFolder;
	public static List<File> dir;
	public static boolean InputEntered;
	public static boolean customizeSelected = false;
	
	public static void main(String[] args) {
		launch(args);
	}

	public static boolean isInputEntered() {
		return InputEntered;
	}

	public static String getInputFileName() {
		return inputPath;
	}

	public void start(Stage primaryStage) throws IOException {

		Parent root = FXMLLoader
				.load(getClass().getResource("/fxml/MainMenu5.0.fxml"));

		Scene scene = new Scene(root, 1038, 758);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Tab2PDF");
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.show();

		main = primaryStage;
		
	}
}
