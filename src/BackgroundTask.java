import java.io.BufferedReader;
import java.io.InputStreamReader;

import javafx.concurrent.Task;

public class BackgroundTask extends Task<Integer> {

	@Override
	protected Integer call() {

		try {
			
			Command.process_Command(Command.command.toString());
			
		} catch (InterruptedException e) {

			e.printStackTrace();
			if (isCancelled()) {
				updateMessage("Cancelled");
				return null;
			}
		}

		System.out.println("done!");
		return null;

	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		updateMessage("Cancelled!");
		System.out.println("cancelled");
		return super.cancel(mayInterruptIfRunning);
	}

	protected void updateProgress(double workDone) {
		updateMessage("progress!" + workDone);
		//super.updateProgress(workDone);
	}

}
