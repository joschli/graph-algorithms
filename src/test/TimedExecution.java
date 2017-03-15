package test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimedExecution {

	TimedFunc func;

	public TimedExecution(TimedFunc func) {
		this.func = func;
	}

	public long time() {
		long startTime = System.nanoTime();
		func.apply();
		long endTime = System.nanoTime();

		long duration = (endTime - startTime) / 1000000;

		return duration;
	}

	public double run(int iterations) {
		List<Long> runTimes = new ArrayList<>();
		for (int i = 0; i < iterations; i++) {
			runTimes.add(time());
		}
		double sum = runTimes.stream().reduce((long) 0, (a, b) -> a + b);
		double avg = sum / runTimes.size();
		return avg;
	}

	public static void run(TimedFunc func) {
		log("Starting Execution");
		long startTime = System.nanoTime();
		func.apply();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
		log("Finished Execution [" + duration + " ms]");
	}

	public static void log(String msg) {
		System.out.println("[" + dateString() + "] - " + msg);
	}

	private static String dateString() {
		DateFormat df = new SimpleDateFormat("ddMMyyyy HH:mm");
		return df.format(new Date(System.currentTimeMillis()));
	}

}
