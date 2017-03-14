package test;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;

public class TimedExecution<T, V> {

	Function<T, V> func;

	public TimedExecution(Function<T, V> func) {
		this.func = func;
	}

	public long time() {
		long startTime = System.nanoTime();
		func.apply(null);
		long endTime = System.nanoTime();

		long duration = (endTime - startTime) / 1000000;

		return duration;
	}

	public double run(int iterations) {
		List<Long> runTimes = new ArrayList<>();
		for (int i = 0; i < 1; i++) {
			runTimes.add(time());
		}
		double sum = runTimes.stream().reduce((long) 0, (a, b) -> a + b);
		double avg = sum / runTimes.size();
		return avg;
	}
}
