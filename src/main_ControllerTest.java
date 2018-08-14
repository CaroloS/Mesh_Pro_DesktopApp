import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Timer;

import org.junit.Test;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

public class main_ControllerTest extends Thread {

	String command_Skin = "/Volumes/Maxtor/Caroline_documentes/workspace/Mesh_Pro_Mac/Blender/blender.app/Contents/MacOS/blender --background --python /Volumes/Maxtor/Caroline_documentes/workspace/Mesh_Pro_Mac/decimate.py -- /Volumes/Maxtor/Caroline_documentes/fetal_stls/ehSkin.stl /Volumes/Maxtor/Caroline_documentes/fetal_stls/ehSkin.glb skin glb";
	String command_Lung = "/Volumes/Maxtor/Caroline_documentes/workspace/Mesh_Pro_Mac/Blender/blender.app/Contents/MacOS/blender --background --python /Volumes/Maxtor/Caroline_documentes/workspace/Mesh_Pro_Mac/decimate.py -- /Volumes/Maxtor/Caroline_documentes/fetal_stls/left_lung.stl /Volumes/Maxtor/Caroline_documentes/fetal_stls/left_lung.glb heart glb";
	String command_Heart = "/Volumes/Maxtor/Caroline_documentes/workspace/Mesh_Pro_Mac/Blender/blender.app/Contents/MacOS/blender --background --python /Volumes/Maxtor/Caroline_documentes/workspace/Mesh_Pro_Mac/decimate.py -- /Volumes/Maxtor/Caroline_documentes/fetal_stls/heart.stl /Volumes/Maxtor/Caroline_documentes/fetal_stls/heart.glb heart glb";
	String command_Brain = "/Volumes/Maxtor/Caroline_documentes/workspace/Mesh_Pro_Mac/Blender/blender.app/Contents/MacOS/blender --background --python /Volumes/Maxtor/Caroline_documentes/workspace/Mesh_Pro_Mac/decimate.py -- /Volumes/Maxtor/Caroline_documentes/fetal_stls/right_lung.stl /Volumes/Maxtor/Caroline_documentes/fetal_stls/smallest_skin.glb brain glb";

	Command main = new Command();
	Main_Controller main2 = new Main_Controller();
	Boolean timerPlaying;
	String filePath = "users/caroline/file.stl";
	String[] splitPath = filePath.split("/");
	String newFile = filePath.replace(splitPath[splitPath.length - 1], "");

	@Test
	public void testProcess_Command() {
		String output = null;
		try {
			output = main.process_Command("sw_vers -productVersion");
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("10.13.5", output);

	}

	@Test
	public void test_FileType() {

		assertEquals(".stl", filePath.substring((filePath.length() - 4), filePath.length()));
	}

	@Test
	public void test_newPathLength() {
		int difference = filePath.split("/").length - newFile.split("/").length;
		assertEquals(difference, 1);
	}

	@Test
	public void test_EmptyCommand() {
		String output = null;
		try {
			output = main.process_Command(" ");
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("", output);
	}

	@Test
	public void test_execSkinTime() {

		long startTime = System.currentTimeMillis();

		try {
			main.process_Command(command_Skin);
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("skin process time:  " + elapsedTime);

	}

	@Test
	public void test_execLungTime() {

		long startTime = System.currentTimeMillis();

		try {
			main.process_Command(command_Lung);
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("lung process time:  " + elapsedTime);

	}

	@Test
	public void test_execHeartTime() {

		long startTime = System.currentTimeMillis();

		try {
			main.process_Command(command_Heart);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("heart process time:  " + elapsedTime);

	}

	@Test
	public void test_execBrainTime() {

		long startTime = System.currentTimeMillis();

		try {
			main.process_Command(command_Brain);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("brain process time:  " + elapsedTime);

	}

	@Test
	public void test_TimelinePlaying() {
		Timeline test_Delay = new Timeline(new KeyFrame(Duration.millis(1000), ae -> testDelay()));
		test_Delay.play();
		double x = test_Delay.getTotalDuration().toSeconds();
		int y = (int) Math.round(x);
		assertEquals(1, y);

	}

	@Test
	public void checkPathToBlender() {
		String blender_Path = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
		assertEquals("file:/Volumes/Maxtor/Caroline_documentes/workspace/Mesh_Pro_Mac/bin/", blender_Path);

	}

	@Test
	public void testStart_Background_Task() {
		BackgroundTask task = new BackgroundTask();
		new Thread(task).start();

		assertEquals(false, task.isCancelled());
	}

	@Test
	public void testBuild_Command_From_Selection1() {

		String command = build_Command_From_Selection("glb", "heart", "heart.stl");
		assertEquals("heart.glb", command);
	}

	@Test
	public void testFileisNull() {
		assertNull("File initially null", main2.file);
		assertNull("File initially null", main2.filePath);
	}

	@Test
	public void test_OpenGLTFViewer() {
		Boolean notOpen = main2.open_glbViewer();
		assertFalse(notOpen);
	}

	@Test
	public void testDelay() {
		timerPlaying = true;
	}

	@Test
	public void testProcess_Mesh() {
		assertNull("alert if filepath not set", main2.alert);
	}

	public String build_Command_From_Selection(String filetype, String organ, String new_fileName) {

		// file_Type = (String)
		// export_Type.getSelectionModel().getSelectedItem();
		// organ = (String)
		// organ_Selector.getSelectionModel().getSelectedItem();

		if (organ == null) {
			organ = "Lung";
		}
		if (filetype == null) {
			filetype = "glb";
		}

		// create new file name with .fbx or .glb extension
		new_fileName = new_fileName.substring(0, (new_fileName.length() - 3));
		if (filetype == "fbx") {
			new_fileName += "fbx";
		} else {
			new_fileName += "glb";
		}
		return new_fileName;

	}
	

}
