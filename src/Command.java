import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Command {

	public static StringBuffer command;
	public static StringBuffer output;
	public static Process p;

	public static void build_Command_From_Selection(String filetype, String organ) {

		if (organ == null) {
			organ = "Lung";
		}
		if (filetype == null) {
			filetype = "glb";
		}

		// create new file name with .fbx or .glb extension
		Main_Controller.new_fileName = Main_Controller.new_fileName.substring(0,
				(Main_Controller.new_fileName.length() - 3));
		if (filetype == "fbx") {
			Main_Controller.new_fileName += "fbx";
		} else {
			Main_Controller.new_fileName += "glb";
		}
		System.out.println(Main_Controller.new_fileName);

		// CREATE THE STRING COMMAND USING THE UPLOADED FILE
		output = new StringBuffer();

		command = new StringBuffer();
		command.append(Main.blender_Path);
		command.append("Blender/blender.app/Contents/MacOS/blender --background --python decimate.py -- ");

		command.append(Main_Controller.filePath);
		command.append(" ");
		command.append(Main_Controller.outPath);
		command.append(Main_Controller.new_fileName);
		command.append(" ");
		command.append(organ);
		command.append(" ");
		command.append(filetype);

	}

	public static String process_Command(String command2) throws InterruptedException {

		StringBuffer s = new StringBuffer();

		try {
			p = Runtime.getRuntime().exec(command2);
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				s.append(line);

			}
			// System.out.println(s.toString());
			System.out.println("\n");
			p.destroy();

		} catch (InterruptedException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//return s.toString();
		return "\n";

	}

}
