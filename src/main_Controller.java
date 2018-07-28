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

public class main_Controller {

	public static String filePath, outPath, new_fileName, saved_File;
	public static String organ, file_Type;
	public static StringBuffer command;
	public static StringBuffer output;

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

//	executeMeshCommand task;
	public static executeMeshCommand task = new executeMeshCommand();
	public static Process p;

	/// OPENING FILE CHOOSER TO SELECT MESH /////////////////
	public void select_Mesh() {

		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(Main.thestage);

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

	////// START PROCESS MESH /////////////////
	public void process_Mesh() {

		if (filePath != null) {

			build_Command_From_Selection();

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
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("A file has not been selected");
			alert.setContentText("Please upload a file for processing");
			alert.showAndWait();
		}

	}

	///// COMMAND LINE FUNCTION /////////////
	public void start_Background_Task() {

		//task = new executeMeshCommand();
	//	System.out.println(task);

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

	}

	public static void process_Command() {
		
		try {
			p = Runtime.getRuntime().exec(command.toString());
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			
		}
		System.out.println(output.toString());
		p.destroy();
		

	}

	public void updateLabels_Success() {
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

	public void updateLabels_Failed() {
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

	public void build_Command_From_Selection() {

		file_Type = (String) export_Type.getSelectionModel().getSelectedItem();
		organ = (String) organ_Selector.getSelectionModel().getSelectedItem();

		if (organ == null) {
			organ = "Lung";
		}
		if (file_Type == null) {
			file_Type = "glb";
		}

		// create new file name with .fbx or .glb extension
		new_fileName = new_fileName.substring(0, (new_fileName.length() - 3));
		if (file_Type == "fbx") {
			new_fileName += "fbx";
		} else {
			new_fileName += "glb";
		}
		System.out.println(new_fileName);

		// CREATE THE STRING COMMAND USING THE UPLOADED FILE
		output = new StringBuffer();

		command = new StringBuffer();
		command.append(Main.blender_Path);
		command.append("Blender/blender.app/Contents/MacOS/blender --background --python decimate.py -- ");

		command.append(filePath);
		command.append(" ");
		command.append(outPath);
		command.append(new_fileName);
		command.append(" ");
		command.append(organ);
		command.append(" ");
		command.append(file_Type);

	}

	public void show_File() {
		final FileChooser fileChooser2 = new FileChooser();
		fileChooser2.setInitialDirectory(new File(outPath));
		File file2 = fileChooser2.showOpenDialog(Main.thestage);

	}

	public void open_glbViewer() {
		try {
			Desktop.getDesktop().browse(new URI("https://gltf-viewer.donmccurdy.com/"));
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

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
		task = new executeMeshCommand();
		
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
