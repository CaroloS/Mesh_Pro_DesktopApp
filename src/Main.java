import java.io.BufferedReader;
import java.io.InputStreamReader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static Stage thestage;
	public static String blender_Path;

	@Override
	public void start(Stage primaryStage) {
		try {

			thestage = primaryStage;

			// System.out.println(new File("").getAbsolutePath());
			System.out.println(this.getClass().getProtectionDomain().getCodeSource().getLocation());
			
			blender_Path = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
			blender_Path = blender_Path.substring(5, (blender_Path.length() - 4));
			System.out.println(blender_Path);


			Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
			Scene scene = new Scene(root, 1108, 783);
			thestage.setScene(scene);
			thestage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	

}

//String osName = System.getProperty("os.name").toLowerCase();
//System.out.println(osName);