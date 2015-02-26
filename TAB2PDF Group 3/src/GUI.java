
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class GUI extends Application {

	public static Stage main;
	public static String inputPath;
	public static String outputName;
	public static String outputPath;
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
				.load(getClass().getResource("/fxml/Main.fxml"));

		Scene scene = new Scene(root, 580, 340);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Tab-2-PDF");

		primaryStage.setResizable(false);
		primaryStage.show();

		main = primaryStage;
	}
}
