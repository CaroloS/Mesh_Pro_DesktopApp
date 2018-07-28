import java.io.BufferedReader;
import java.io.InputStreamReader;

import javafx.concurrent.Task;

public class executeMeshCommand extends Task<Integer> {

	@Override
	protected Integer call() throws Exception {
		
		main_Controller.process_Command();

		System.out.println("done!");
		return 10;
		
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		updateMessage("Cancelled!");
		System.out.println("cancelled");
		return super.cancel(mayInterruptIfRunning);
	}
	
	
	@Override
	protected void updateProgress(double workDone, double max) {
		updateMessage("progress!" + workDone);
		super.updateProgress(workDone, max);
	}
	
	
}
