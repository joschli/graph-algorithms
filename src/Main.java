
import generator.GraphGenerator;
import test.TimedExecution;
import ui.MainFrame;

public class Main {
	public static void main(String[] args) {
		GraphGenerator gen = new GraphGenerator(10000, 10000, false);
		TimedExecution exec = new TimedExecution((e) -> gen.generateGraph(20, 5));
		// System.out.println(exec.run(5));
		new MainFrame(3800, 2000);
	}
}
