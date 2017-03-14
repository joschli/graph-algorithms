package ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JTextField;

import algorithms.Dinic;
import algorithms.EdmondsKarp;
import algorithms.FordFulkerson;
import algorithms.PreflowPush;
import algorithms.VisualizationData;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
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

	JTextField visLabel;

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

		packAndCenterFrame();
		frame.setVisible(true);
	}

	public void packAndCenterFrame() {
		frame.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
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
		graphPanel.getRenderContext().setEdgeShapeTransformer(new EdgeShape<Node, Edge>(graph).new Line());
		graphPanel.getRenderContext().setEdgeDrawPaintTransformer(edge -> {
			if (visActivated) {
				if (visData.hasSecondaryHighlights()) {
					for (Edge e : visData.getSecondaryHighlights().get(index)) {
						if ((e.getStart().getId() == edge.getStart().getId()
								&& e.getEnd().getId() == edge.getEnd().getId())
								|| (e.getEnd().getId() == edge.getStart().getId()
										&& e.getStart().getId() == edge.getEnd().getId())) {
							return Color.cyan;
						}
					}
				}

				for (Edge e : visData.getHighlights().get(index)) {
					if ((e.getStart().getId() == edge.getStart().getId()
							&& e.getEnd().getId() == edge.getEnd().getId())) {
						return Color.red;
					}
					if ((e.getEnd().getId() == edge.getStart().getId()
							&& e.getStart().getId() == edge.getEnd().getId())) {
						return Color.orange;
					}
				}
			}
			return Color.black;
		});

		graphPanel.getRenderContext().setVertexLabelTransformer(n -> {
			if (visActivated) {
				if (visData.isGoldbergTarjan()) {
					return "<html><font color='white'><b>"
							+ Integer.toString(visData.getNodeLabels().get(index)[n.getId()]) + "</b></font></html>";
				}
			}
			return "";
		});

		graphPanel.getRenderContext().setVertexStrokeTransformer((n) -> new BasicStroke(2));

		graphPanel.getRenderContext().setVertexDrawPaintTransformer(n -> {
			if (visActivated) {
				if (visData.isGoldbergTarjan()) {
					if (visData.getCuts().get(index).contains(n)) {
						return Color.getHSBColor(0, 0.9f, 0.78f);
					}
				}
			}
			return Color.BLACK;
		});

		graphPanel.getRenderContext().setVertexFillPaintTransformer(node -> {
			if (visActivated) {
				if (visData.isGoldbergTarjan()) {
					if (node.equals(visData.getNodeHighlights().get(index))) {
						return Color.blue;
					}

				}
			}
			if (network.getStartNode().equals(node)) {
				return Color.getHSBColor(0.4f, 0.9f, 0.41f);
			}
			if (network.getEndNode().equals(node)) {
				return Color.red;
			}
			return Color.gray;
		});
		graphPanel.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		graphPanel.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		visLabel = new JTextField("Generate a graph and use a specific algorithm to solve it!");
		visLabel.setEditable(false);
		visLabel.setBorder(null);
		if (!visData.getLabels().isEmpty()) {
			visLabel.setText(visData.getLabels().get(index));
		}
		graphPanel.add(visLabel);
		frame.getContentPane().add(graphPanel, BorderLayout.CENTER);
	}

	private void showNextNetwork() {
		if (index == networks.size() - 1) {
			return;
		}
		index++;
		menuPanel.setCurrentStep(index);
		showGraph();
	}

	private void showPreviousNetwork() {
		if (index == 0) {
			return;
		}
		index--;
		menuPanel.setCurrentStep(index);
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
			} else if (menuPanel.getAlgorithm().equals("EdmondsKarp")) {
				networks.get(index).calculateEdgesForNode();
				EdmondsKarp ek = new EdmondsKarp(networks.get(index));
				ek.setVisualization(true);
				ek.run();
				visData = ek.getVisData();
				networks = visData.getNetworks();
				index = 0;
				visActivated = true;
			} else if (menuPanel.getAlgorithm().equals("FordFulkerson")) {
				System.out.println("FORD FULKERSON");
				networks.get(index).calculateEdgesForNode();
				FordFulkerson ff = new FordFulkerson(networks.get(index));
				ff.setVisualization(true);
				ff.run();
				visData = ff.getVisData();
				networks = visData.getNetworks();
				index = 0;
				visActivated = true;
			} else if (menuPanel.getAlgorithm().equals("Dinic")) {
				System.out.println("DINIC");
				networks.get(index).calculateEdgesForNode();
				Dinic d = new Dinic(networks.get(index));
				d.setVisualization(true);
				List<EdgePair> flow = d.run();
				Network.printFlow(flow);
				visData = d.getVisData();
				networks = visData.getNetworks();
				index = 0;
				visActivated = true;
			} else if (menuPanel.getAlgorithm().equals("GoldbergTarjan")) {
				System.out.println("GOLDBERG TARJAN");
				networks.get(index).calculateEdgesForNode();
				PreflowPush pp = new PreflowPush(networks.get(index));
				pp.setVisualization(true);
				pp.run();
				visData = pp.getVisData();
				networks = visData.getNetworks();
				index = 0;
				visActivated = true;
			}
			menuPanel.setCurrentStep(index);
			menuPanel.setMaxSteps(networks.size() - 1);
			menuPanel.enableStart();
			showGraph();
			break;
		case "play":
			timer = new Timer(true);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					if (index == networks.size() - 1) {
						timer.cancel();
						menuPanel.disableNext();
						menuPanel.disablePause();
						return;
					}
					showNextNetwork();
					frame.pack();
				}
			}, 0, menuPanel.getDelay() * 1000);
			menuPanel.disableStart();
			menuPanel.enablePause();
			break;
		case "pause":
			timer.cancel();
			menuPanel.enableStart();
			menuPanel.disablePause();
			break;
		case "mode":
			String mode = modeSelection.getMode();
			if (mode == "run") {
				addMenu();
				addGraphPanel();
				index = networks.size() - 1;
				showGraph();
			} else if (mode == "test") {
				addTestPanel();
				this.frame.remove(modeSelection);
				packAndCenterFrame();
				return;
			}
			this.frame.remove(modeSelection);
			this.frame.pack();
			break;
		case "back":
			menuPanel.restart();
			index = networks.size() - 1;
			visActivated = false;
			showGraph();
			break;

		case "toEnd":
			index = networks.size() - 1;
			showGraph();
			menuPanel.setCurrentStep(index);
			break;
		case "toStart":
			index = 0;
			showGraph();
			menuPanel.setCurrentStep(index);
			break;
		default:
			break;
		}
		packAndCenterFrame();

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

	private void addTestPanel() {
		TestPanel panel = new TestPanel(this);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
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
