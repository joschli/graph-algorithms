package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;

import generator.GraphGenerator;
import model.Graph;

public class MainFrame extends JFrame implements ActionListener {

	GraphPanel graph;

	MenuPanel menuPanel;

	int width;
	int height;

	public MainFrame(int width, int height) {
		this.width = width;
		this.height = height;

		setupFrame();
		addGraphPanel();
		addMenu();

		this.pack();
		this.setSize(width, height);
	}

	private void setupFrame() {
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void addGraphPanel() {
		graph = new GraphPanel();
		graph.setMinimumSize(new Dimension((int) (0.8 * width), height));
		this.getContentPane().add(graph, BorderLayout.CENTER);
	}

	private void addMenu() {
		menuPanel = new MenuPanel(this);
		this.getContentPane().add(menuPanel, BorderLayout.PAGE_START);
	}

	public void showGraph(Graph graph) {
		this.graph.setGraph(graph);
		this.graph.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		if ("generate".equals(e.getActionCommand())) {
			GraphGenerator gen = new GraphGenerator(this.graph.getWidth() - 10, this.graph.getHeight() - 10);
			List<Graph> graphs = gen.generateGraph(menuPanel.getNodeCount(), menuPanel.getCapacity());
			showGraph(graphs.get(graphs.size() - 1));
		}
	}

}
