import java.io.BufferedReader;
import java.io.InputStreamReader;

import javafx.concurrent.Task;

public class executeMeshCommand extends Task<Integer> {

	@Override
	protected Integer call() throws Exception {
		
		main_Controller controller = new main_Controller();
		
		controller.process_Command();
		
		/*
		for (int i= 0; i < 10; i++) {
			System.out.println(i + 1);
			updateProgress(i + 1, 10);
			Thread.sleep(500);
			
			if (isCancelled()) {
				return i;
			}
		}
		return 10;
		*/
		
		System.out.println("done!");
	
		
		return 10;
		
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		updateMessage("Cancelled!");
		return super.cancel(mayInterruptIfRunning);
	}
	
	
	@Override
	protected void updateProgress(double workDone, double max) {
		updateMessage("progress!" + workDone);
		super.updateProgress(workDone, max);
	}
}
