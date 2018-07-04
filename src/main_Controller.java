import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class main_Controller {

	public static String filePath;
	public static String outPath;
	public static String new_fileName;
	StringBuffer command;
	StringBuffer output;

	@FXML
	Label file_Path_Label, updateMessage, small_Update;
	@FXML
	AnchorPane done_Anchor;
	@FXML
	ImageView gif_ImageView;

	////// PROCESS MESH /////////////////
	public void process_Mesh() {

		if (filePath != null) {

			// CREATE THE STRING COMMAND USING THE UPLOADED FILE
			output = new StringBuffer();

			command = new StringBuffer();
			command.append(Main.blender_Path);
			command.append(
					"Blender/blender.app/Contents/MacOS/blender --background --python /Volumes/Maxtor/Caroline_documentes/decimator/decimate.py -- ");

			command.append(filePath);
			command.append(" ");
			command.append(outPath);
			command.append(new_fileName);

			updateMessage.setText("Thanks! Your mesh is being processed...");
			small_Update.setText("(This may take some time. Files over 100MB may take over 5minutes)");
			gif_ImageView.setImage(new Image(this.getClass().getResource("kidney.gif").toExternalForm()));

			//CALL EXECUTE COMMAND FUNCTION
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> executeCommand()));
			timeline.play();
			
		} else {
			
			//ALERT IF NO FILE SELECTED
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("A file has not been selected");
			alert.setContentText("Please upload a file for processing");
			alert.showAndWait();
		}

	}

	///// COMMAND LINE FUNCTION /////////////
	public void executeCommand() {

		Process p;

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

		System.out.println(output.toString());

		updateMessage.setText("Success!  Mesh Processing Complete");
		done_Anchor.setVisible(true);

		String saved_File = "Your processed mesh has been saved in: ";
		saved_File += outPath;
		saved_File += new_fileName;
		small_Update.setText(saved_File);

	}

	/// OPENING FILE CHOOSER TO SELECT MESH /////////////////
	public void select_Mesh() {

		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(Main.thestage);
		System.out.println(file.toString());
		filePath = file.toString();

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

			outPath = filePath.replace(outPath_Array[outPath_Array.length - 1], "");
			System.out.println(outPath);

			new_fileName = outPath_Array[outPath_Array.length - 1];
			new_fileName = new_fileName.substring(0, (new_fileName.length() - 3));
			new_fileName += "fbx";
			System.out.println(new_fileName);

			file_Path_Label.setText(filePath);
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

	}

	@FXML
	public void initialize() {
		done_Anchor.setVisible(false);

	}

}

// file:/E:/Caroline_documentes/workspace/Tester32/bin/

// "/E:/Caroline_documentes/workspace/Mesh_Pro/blender-2.79b-windows64/blender
// --background --python E:/Caroline_documentes/decimator/decimate.py --
// E:/Caroline_documentes/fetal_stls/right_lung.stl
// E:/Caroline_documentes/fetal_stls/javanew12.fbx"
