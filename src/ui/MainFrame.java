package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import algorithms.EdmondsKarp;
import algorithms.VisualizationData;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import generator.GraphGenerator;
import model.Edge;
import model.EdgePair;
import model.Network;
import model.Node;

public class MainFrame implements ActionListener {

	MenuPanel menuPanel;
	ModeSelection modeSelection;
	Layout<Node, Edge> layout;
	BasicVisualizationServer<Node, Edge> graphPanel;
	Graph<Node, Edge> graph;

	List<Network> networks;
	VisualizationData visData;
	boolean visActivated = false;
	int index = 0;

	Timer timer;

	int width;
	int height;
	int viewPortWidth;
	int viewPortHeight;
	JFrame frame;

	public MainFrame(int width, int height) {
		this.width = width;
		this.height = height;
		this.viewPortWidth = (int) this.width / 2;
		this.viewPortHeight = (int) this.height / 2;
		visData = new VisualizationData();
		setupFrame();
		modeSelection = new ModeSelection(this);
		this.frame.add(modeSelection);

		frame.pack();
		frame.setVisible(true);
	}

	private void setupFrame() {
		frame = new JFrame("Graph Algorithms");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
	}

	private void addGraphPanel() {
		index = 0;
		GraphGenerator gen = new GraphGenerator(this.width * 2, this.height * 2, true);
		networks = gen.generateGraph(menuPanel.getNodeCount(), menuPanel.getCapacity());
		showGraph();
	}

	private void showGraph() {
		if (index > networks.size() - 1) {
			index = networks.size();
		}
		Network network = networks.get(index);
		graph = new DirectedSparseMultigraph<Node, Edge>();
		Polygon poly = new Polygon();
		for (Node n : network.getNodes()) {
			graph.addVertex(n.clone());
			poly.addPoint((int) n.getPoint().getX(), (int) n.getPoint().getY());
		}
		Rectangle r = poly.getBounds();
		double xsc = r.getCenterX() / (this.width * 2);
		double ysc = r.getCenterY() / (this.height * 2);
		Point center = new Point((int) (this.viewPortWidth * xsc), (int) (this.viewPortHeight * ysc));
		Point viewPortCenter = new Point((int) (this.viewPortWidth * 0.5), (int) (this.viewPortHeight * 0.5));
		Point newCenter = new Point((int) (center.getX() - viewPortCenter.getX()),
				(int) (center.getY() - viewPortCenter.getY()));
		for (EdgePair ep : network.getEdgePairs()) {
			graph.addEdge(ep.fwEdge, findNode(ep.fwEdge.getStart().getId()), findNode(ep.fwEdge.getEnd().getId()));
			graph.addEdge(ep.bwEdge, findNode(ep.bwEdge.getStart().getId()), findNode(ep.bwEdge.getEnd().getId()));
		}
		Layout<Node, Edge> layout = new StaticLayout<Node, Edge>(graph);
		layout.setInitializer(input -> {
			Point2D t = input.getPoint();
			double xs = t.getX() / (this.width * 2);
			double ys = t.getY() / (this.height * 2);
			return new Point2D.Double((this.viewPortWidth * xs) - newCenter.getX(),
					(viewPortHeight * ys) - newCenter.getY());
		});
		graphPanel = new BasicVisualizationServer<Node, Edge>(layout);
		graphPanel.setPreferredSize(new Dimension(this.viewPortWidth, this.viewPortHeight));
		graphPanel.getRenderContext().setEdgeShapeTransformer(edge -> {
			GeneralPath path = new GeneralPath();
			path.reset();
			path.moveTo(0.0f, 0.0f);
			path.lineTo(0.0f, 3.f);
			path.lineTo(1.0f, 3.f);
			path.lineTo(1.0f, 1.0f);
			return path;
		});
		graphPanel.getRenderContext().setEdgeDrawPaintTransformer(edge -> {
			if(visActivated){
				for(Edge e: visData.getHighlights().get(index)){
					if(e.getStart().getId() == edge.getStart().getId() && e.getEnd().getId() == edge.getEnd().getId()){
						return Color.red;
					}
				}
			}
			return Color.black;
		});
		graphPanel.getRenderContext().setVertexLabelTransformer(n -> {
			if (network.getStartNode().getId() == n.getId()) {
				return "s";
			} else if (network.getEndNode().getId() == n.getId()) {
				return "t";
			}
			return new ToStringLabeller().apply(n);
		});
		graphPanel.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		graphPanel.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
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

		switch (e.getActionCommand()) {
		case "generate":
			frame.getContentPane().remove(graphPanel);
			addGraphPanel();
			index = networks.size() - 1;
			showGraph();
			break;
		case "next":
			frame.getContentPane().remove(graphPanel);
			showNextNetwork();
			if (timer != null) {
				timer.cancel();
			}

			menuPanel.enableStart();
			break;
		case "previous":
			frame.getContentPane().remove(graphPanel);
			showPreviousNetwork();
			if (timer != null) {
				timer.cancel();
			}
			menuPanel.enableStart();
			break;
		case "start":
			menuPanel.start();
			if (menuPanel.getAlgorithm() == "zg") {
				index = 0;
			} else {
				networks.get(index).calculateEdgesForNode();
				EdmondsKarp ek= new EdmondsKarp(networks.get(index));
				ek.run();
				visData = ek.getVisData();
				networks = visData.getNetworks();
				index = 0;
				visActivated = true;
			}
			showGraph();
			break;
		case "play":
			timer = new Timer(true);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					index++;
					showGraph();
					frame.pack();
					if (index >= networks.size() - 1) {
						timer.cancel();
					}
				}
			}, 0, menuPanel.getDelay() * 1000);
			menuPanel.disableStart();
			menuPanel.enablePause();
			break;
		case "pause":
			timer.cancel();
			menuPanel.enableStart();
			menuPanel.disablePause();
		case "mode":
			String mode = modeSelection.getMode();
			if (mode == "run") {
				addMenu();
				addGraphPanel();
				index = networks.size() - 1;
				showGraph();
			} else if (mode == "test") {
				System.exit(0);
			}
			this.frame.remove(modeSelection);
			this.frame.pack();
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
			menuPanel.disableStart();
			menuPanel.disablePause();
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
