import java.io.BufferedReader;
import java.io.InputStreamReader;

import javafx.concurrent.Task;

public class executeMeshCommandTest extends Task<Integer> {

	public String inProcess;
	public Boolean done;
	public Boolean cancelled = false;
	
	@Override
	protected Integer call() throws Exception {
		
		inProcess = "task in process ";
		return 10;
		
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		cancelled = true;
		return super.cancel(mayInterruptIfRunning);
	}
	
	
	@Override
	protected void updateProgress(double workDone, double max) {
		done = true;
		
	}
	
	
}
