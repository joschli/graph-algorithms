package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import generator.GraphGenerator;
import model.Edge;
import model.EdgePair;
import model.Network;
import model.Node;

public class MainFrame implements ActionListener {

	MenuPanel menuPanel;
	Layout<Node, Edge> layout;
	BasicVisualizationServer<Node, Edge> graphPanel;
	Graph<Node, Edge> graph;

	List<Network> networks;
	int index = 0;

	int width;
	int height;
	JFrame frame;

	public MainFrame(int width, int height) {
		this.width = width;
		this.height = height;
		setupFrame();

		addMenu();
		addGraphPanel();

		frame.pack();
		frame.setVisible(true);
	}

	private void setupFrame() {
		frame = new JFrame("TEST");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
	}

	private void addGraphPanel() {
		index = 0;
		GraphGenerator gen = new GraphGenerator(this.getWidth() - 10, this.getHeight() - 10);
		networks = gen.generateGraph(menuPanel.getNodeCount(), menuPanel.getCapacity());
		showGraph();
	}

	private void showGraph() {
		Network network = networks.get(index);
		graph = new SparseMultigraph<Node, Edge>();
		for (Node n : network.getNodes()) {
			graph.addVertex(n.clone());
		}
		for (EdgePair ep : network.getEdgePairs()) {
			graph.addEdge(ep.fwEdge, findNode(ep.fwEdge.getStart().getId()), findNode(ep.fwEdge.getEnd().getId()));
		}
		Layout<Node, Edge> layout = new StaticLayout<Node, Edge>(graph, input -> input.getPoint());
		layout.setSize(new Dimension(350, 350));
		graphPanel = new BasicVisualizationServer<Node, Edge>(layout);
		graphPanel.setPreferredSize(new Dimension(800, 600));
		graphPanel.getRenderContext().setEdgeShapeTransformer(new EdgeShape<Node, Edge>(graph).new Line());
		frame.getContentPane().add(graphPanel, BorderLayout.CENTER);
	}

	private void showNextNetwork() {
		if (index == networks.size() - 1) {
			return;
		}
		index++;
		showGraph();
	}

	private void showPreviousNetwork() {
		if (index == 0) {
			return;
		}
		index--;
		showGraph();
	}

	private Node findNode(int id) {
		for (Node n : graph.getVertices()) {
			if (n.getId() == id) {
				return n;
			}
		}
		return null;
	}

	private void addMenu() {
		menuPanel = new MenuPanel(this);
		frame.getContentPane().add(menuPanel, BorderLayout.PAGE_START);
	}

	public void actionPerformed(ActionEvent e) {
		frame.getContentPane().remove(graphPanel);

		switch (e.getActionCommand()) {
		case "generate":
			addGraphPanel();
			break;
		case "next":
			showNextNetwork();
			break;
		case "previous":
			showPreviousNetwork();
			break;
		default:
			break;
		}
		frame.pack();

		if (index > 0) {
			menuPanel.enablePrevious();
		}
		if (index < networks.size() - 1) {
			menuPanel.enableNext();
		}
		if (index == 0) {
			menuPanel.disablePrevious();
		}
		if (index >= networks.size() - 1) {
			menuPanel.disableNext();
		}

	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
