package test;

import com.google.common.base.Function;

public class TimedExecution<T, V> {

	Function<T, V> func;

	public TimedExecution(Function<T, V> func) {
		this.func = func;
	}

	public long run(T input) {
		long startTime = System.nanoTime();
		func.apply(input);
		long endTime = System.nanoTime();

		long duration = (endTime - startTime) / 1000000;

		return duration;
	}
}
