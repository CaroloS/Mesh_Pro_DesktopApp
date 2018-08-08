import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class Main_Controller {

	public static String filePath, outPath, new_fileName, saved_File;
	public static String organ, file_Type;
	
	File file;
	Alert alert;

	@FXML
	Label file_Path_Label, status, updateMessage, small_Update, comment2;
	@FXML
	AnchorPane done_Anchor;
	@FXML
	ImageView gif_ImageView;
	@FXML
	ComboBox export_Type, organ_Selector;
	@FXML
	VBox web_ViewBox2, web_ViewBox1;
	@FXML
	Hyperlink hyperLink1, hyperLink2;

//	BackgroundTask task;
	public static BackgroundTask task = new BackgroundTask();
	

	/**
	 * 
	 * 
	 */
	private void select_Mesh() {
		
		refresh_Page();

		final FileChooser fileChooser = new FileChooser();
		file = fileChooser.showOpenDialog(Main.thestage);

		if (file != null) {
			System.out.println(file.toString());
			filePath = file.toString();

			// Alert if file type is incorrect - must be .stl file
			if (!(filePath.substring((filePath.length() - 4), filePath.length()).equals(".stl"))) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText("Sorry that file type is not accepted");
				alert.setContentText("Please upload a .stl file for processing");
				alert.showAndWait();
				filePath = null;
			}

			else {
				filePath = file.toString();
				String[] outPath_Array = filePath.split("/");

				// get directory path (without file name)
				outPath = filePath.replace(outPath_Array[outPath_Array.length - 1], "");
				System.out.println(outPath);
				new_fileName = outPath_Array[outPath_Array.length - 1];

				file_Path_Label.setText(filePath);
			}
		}

	}

	/**
	 * 
	 * 
	 */
	private void process_Mesh() {

		if (filePath != null) {

			file_Type = (String) export_Type.getSelectionModel().getSelectedItem();
			organ = (String) organ_Selector.getSelectionModel().getSelectedItem();
			
			Command.build_Command_From_Selection(file_Type, organ);

			updateMessage.setText("Thanks! Your mesh is being processed...");
			small_Update.setText("(This may take some time. Files over 100MB may take over 5minutes)");
			gif_ImageView.setVisible(true);
			gif_ImageView.setImage(new Image(this.getClass().getResource("stopwtach.gif").toExternalForm()));

			// Call start_Background_Task() with slight delay - to allow above
			// labels to be set
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> start_Background_Task()));
			timeline.play();

		} else {

			// ALERT IF NO FILE SELECTED
			alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("A file has not been selected");
			alert.setContentText("Please upload a file for processing");
			alert.showAndWait();
		}

	}

	/**
	 * 
	 * 
	 */
	private void start_Background_Task() {

		// progress_Bar.progressProperty().bind(task.progressProperty());
		// progress_Ind.progressProperty().bind(task.progressProperty());
		// status.textProperty().bind(task.messageProperty());

		new Thread(task).start();
		
		
		task.setOnSucceeded(e -> {
			updateLabels_Success();
		});

		task.setOnFailed(e -> {
			updateLabels_Failed();
		});
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(420000), ae -> check_Finished()));
		timeline.play();

	}
	
	private void check_Finished() {
		if (task.isRunning()) {
			gif_ImageView.setVisible(false);
			
		}
	}


	private void updateLabels_Success() {
		
		updateMessage.setText("Success!");
		done_Anchor.setVisible(true);

		String message = "Your processed mesh has been saved in: ";
		saved_File = outPath + new_fileName;
		small_Update.setText(message);
		gif_ImageView.setVisible(false);
		hyperLink1.setText(saved_File);
		comment2.setText("View glb files");
		hyperLink2.setText("here");

	}

	private void updateLabels_Failed() {
		
		updateMessage.setText("Success!  Mesh Processing Complete");
		done_Anchor.setVisible(true);

		String message = "Your processed mesh has been saved in: ";
		saved_File = outPath + new_fileName;
		small_Update.setText(message);
		gif_ImageView.setVisible(false);
		hyperLink1.setText(saved_File);
		comment2.setText("View glb files");
		hyperLink2.setText("here");
	}
	

	
	private void show_File() {
		
		final FileChooser fileChooser2 = new FileChooser();
		fileChooser2.setInitialDirectory(new File(outPath));
		File file2 = fileChooser2.showOpenDialog(Main.thestage);

	}

	
	private Boolean open_glbViewer() {
		Boolean notOpen = false;
		
		try {
			Desktop.getDesktop().browse(new URI("https://gltf-viewer.donmccurdy.com/"));
		} catch (IOException e1) {
			notOpen = true;
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
			notOpen = true;
		}
		return notOpen;

	}
	
	
	public void refresh_Page() {
		
		updateMessage.setText("");
		small_Update.setText("");
		file_Path_Label.setText("");
		done_Anchor.setVisible(false);
		filePath = null;
		outPath = null;
		new_fileName = null;
		hyperLink1.setText("");
		hyperLink2.setText("");
		comment2.setText("");
		new_fileName = null;
		saved_File = null;
		gif_ImageView.setVisible(false);
		
		// cancel thread
		
		System.out.println(task);
		task.cancel(true);
		task = new BackgroundTask();
		
	}

	@FXML
	public void initialize() {
		
		done_Anchor.setVisible(false);

		export_Type.getItems().addAll("fbx", "glb");
		organ_Selector.getItems().addAll("Skin", "Brain", "Lung", "Heart", "Bone");

		
		// Lays out the webview engine on third tab with hololens web app
		WebView web_View2 = new WebView();
		web_View2.setPrefHeight(783);
		web_View2.setPrefWidth(1108);
		WebEngine engine = web_View2.getEngine();
		engine.load("https://goshmhif.azurewebsites.net/Hololens-Webapp/#/add");
		engine.setJavaScriptEnabled(true);
		

		web_ViewBox2.getChildren().addAll(web_View2);

		// lays out webview wngine on second tab with gltf viewer app
		WebView web_View1 = new WebView();
		web_View1.setPrefHeight(783);
		web_View1.setPrefWidth(1108);
		WebEngine engine1 = web_View1.getEngine();
		engine1.load("https://gltf-viewer.donmccurdy.com/");
		engine1.setJavaScriptEnabled(true);

		web_ViewBox1.getChildren().addAll(web_View1);

	}

}
