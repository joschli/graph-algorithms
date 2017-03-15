import test.TestFactory;
import test.TimedExecution;

public class Main {
	public static void main(String[] args) {
		TimedExecution.run(() -> {
			TestFactory.createSmallTestCase().run("verysmall.txt");
		});
	}
}
