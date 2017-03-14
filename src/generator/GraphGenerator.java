package generator;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import algorithms.FordFulkerson;
import model.EdgePair;
import model.Network;
import model.Node;

public class GraphGenerator {

	public static final int NODEDISTANCE = 50;
	public static final int EDGEDISTANCE = 50;

	private int width;
	private int height;
	private boolean displayable;

	public GraphGenerator(int width, int height, boolean displayable) {
		this.width = width;
		this.height = height;
		this.displayable = displayable;
	}

	public List<Network> generateGraph(int nodeCount, int graphCapacity) {
		List<Network> graphs = generateGraph(nodeCount);
		while ((displayable && !checkEdges(lastGraph(graphs)) || !finalizeGraph(lastGraph(graphs), graphCapacity))) {
			graphs = generateGraph(nodeCount);
		}

		return graphs;
	}

	private boolean checkEdges(Network lastGraph) {
		for (Node n : lastGraph.getNodes()) {
			for (EdgePair pair : lastGraph.getEdgePairs()) {
				double dist = createLineFromEdge(pair).ptLineDist(n.getPoint());
				if (dist > 0.0 && dist < EDGEDISTANCE) {
					return false;
				}
			}
		}
		return true;
	}

	private List<Network> generateGraph(int nodeCount) {
		List<Network> graphs = new ArrayList<>();
		graphs.add(new Network());

		List<Node> nodes = generateNodes(nodeCount);
		for (Node n : nodes) {
			lastGraph(graphs).addNode(n);
		}

		List<EdgePair> edges = generateEdges(nodes);
		sortEdgesByDistance(edges);

		EdgePair e = addEdge(lastGraph(graphs), edges);
		while (e != null) {
			Network g = cloneGraph(lastGraph(graphs));
			g.addEdgePair(e);
			graphs.add(g);
			e = addEdge(lastGraph(graphs), edges);
		}
		return graphs;
	}

	private Network lastGraph(List<Network> graphs) {
		return graphs.get(graphs.size() - 1);
	}

	private EdgePair addEdge(Network graph, List<EdgePair> edges) {
		EdgePair e = null;
		for (EdgePair edge : edges) {
			if (noIntersectionWithGraph(graph, edge)) {
				e = edge;
				break;
			}
		}
		if (e != null) {
			edges.remove(e);
		}
		return e;
	}

	private Network cloneGraph(Network graph) {
		Network clone = new Network();
		for (Node n : graph.getNodes()) {
			clone.addNode(n);
		}
		for (EdgePair e : graph.getEdgePairs()) {
			clone.addEdgePair(e);
		}
		return clone;
	}

	private boolean finalizeGraph(Network graph, int maxCapacity) {
		setStartNode(graph);
		setEndNode(graph);
		if (!setEdgeDirections(graph)) {
			return false;
		}

		int tryCount = 0;
		while (!validCapacityDistribution(graph)) {
			if (tryCount == 5) {
				return false;
			}

			distributeCapacities(graph, maxCapacity);
			tryCount++;
		}

		return true;
	}

	private boolean setEdgeDirections(Network graph) {

		for (EdgePair pair : graph.getEdgePairsForNode(graph.getStartNode())) {
			if (!graph.getStartNode().equals(pair.fwEdge.getStart())) {
				pair.invert();
			}
		}

		for (EdgePair pair : graph.getEdgePairsForNode(graph.getEndNode())) {
			if (!graph.getEndNode().equals(pair.fwEdge.getEnd())) {
				pair.invert();
			}
		}

		changeEdgeDirections(graph);
		return validNodes(graph);
	}

	private boolean validNodes(Network graph) {
		boolean valid = true;
		for (Node n : graph.getNodes()) {
			if (n.equals(graph.getStartNode()) || n.equals(graph.getEndNode())) {
				continue;
			}

			List<EdgePair> edges = graph.getEdgePairsForNode(n);

			valid = validNodeEdges(n, edges) && valid;
		}
		return valid;
	}

	private void changeEdgeDirections(Network graph) {
		for (Node n : graph.getNodes()) {
			if (n.equals(graph.getStartNode()) || n.equals(graph.getEndNode())) {
				continue;
			}

			List<EdgePair> edges = graph.getEdgePairsForNode(n);

			if (validNodeEdges(n, edges)) {
				continue;
			}

			for (EdgePair pair : edges) {
				if (!pair.contains(graph.getEndNode()) && !pair.contains(graph.getStartNode())) {
					pair.invert();
					if (checkOtherNodeStillValid(n, pair, graph)) {
						break;
					}
					pair.invert();
					continue;
				}
			}
		}
	}

	private boolean checkOtherNodeStillValid(Node n, EdgePair pair, Network graph) {
		Node otherNode = pair.getOtherNode(n);
		return validNodeEdges(otherNode, graph.getEdgePairsForNode(otherNode));
	}

	private boolean validNodeEdges(Node n, List<EdgePair> edges) {
		return edges.stream().anyMatch((e) -> e.fwEdge.getStart().equals(n))
				&& edges.stream().anyMatch((e) -> e.fwEdge.getEnd().equals(n));
	}

	private void distributeCapacities(Network graph, int maxCapacity) {
		List<Integer> capacities = new ArrayList<>();
		for (int i = 0; i < graph.getEdgePairs().size(); i++) {
			capacities.add(getRandomCap(maxCapacity));
		}

		for (int i = 0; i < graph.getEdgePairs().size(); i++) {
			graph.getEdgePairs().get(i).setCapacity(capacities.get(i));
		}
	}

	private boolean validCapacityDistribution(Network graph) {
		graph.calculateEdgesForNode();
		boolean valid = checkIndicentEdges(graph);
		graph.clearCapacities();
		return valid;
	}

	private boolean validNodePlacement(Network graph) {
		if (graph.getStartNode().equals(graph.getEndNode())) {
			return false;
		}
		if (graph.getNeighbors(graph.getStartNode()).contains(graph.getEndNode())) {
			return false;
		}
		return checkIfConnected(graph);
	}

	private void setEndNode(Network graph) {
		Node max = null;
		for (Node n : graph.getNodes()) {
			if (max == null && !n.equals(graph.getStartNode())) {
				max = n;
			}
			if (max != null && n.getPoint().getX() > max.getPoint().getX()
					&& n.getPoint().getY() < max.getPoint().getY()) {
				max = n;
			}
		}
		graph.setEndNode(max);
	}

	private void setStartNode(Network graph) {
		Node min = null;
		for (Node n : graph.getNodes()) {
			if (min == null) {
				min = n;
			}
			if (n.getPoint().getX() < min.getPoint().getX() && n.getPoint().getY() > min.getPoint().getY()) {
				min = n;
			}
		}
		graph.setStartNode(min);
	}

	private boolean checkIndicentEdges(Network graph) {
		FordFulkerson fordFulkerson = new FordFulkerson(graph);
		List<EdgePair> result = fordFulkerson.run();
		for (EdgePair edgePair : result) {
			if ((edgePair.contains(graph.getStartNode()) || edgePair.contains(graph.getEndNode()))
					&& ((edgePair.getAvailableCapacity(true) == 0))) {
				return false;
			}
		}
		return true;
	}

	private boolean checkIfConnected(Network graph) {
		return new PathExists(graph, graph.getStartNode(), graph.getEndNode()).run();
	}

	private int getRandomCap(int maxCapacity) {
		return (int) (Math.random() * (maxCapacity)) + 1;
	}

	private boolean noIntersectionWithGraph(Network graph, EdgePair edge) {
		Line2D edgeLine = createLineFromEdge(edge);
		boolean intersect = true;
		for (EdgePair graphEdge : graph.getEdgePairs()) {
			Line2D graphEdgeLine = createLineFromEdge(graphEdge);
			if ((getStartPoint(edge) != getStartPoint(graphEdge) && getStartPoint(edge) != getEndPoint(graphEdge)
					&& getEndPoint(edge) != getStartPoint(graphEdge) && getEndPoint(edge) != getEndPoint(graphEdge)
					&& edgeLine.intersectsLine(graphEdgeLine))) {
				intersect = false;
				break;
			}
		}

		for (Node n : graph.getNodes()) {
			if (n == edge.bwEdge.getStart() || n == edge.bwEdge.getEnd()) {
				continue;
			}
			if (edgeLine.ptLineDist(n.getPoint()) == 0.0) {
				intersect = false;
				break;
			}
		}
		return intersect;
	}

	private Point getStartPoint(EdgePair edge) {
		return edge.getEdges().get(0).getStart().getPoint();
	}

	private Point getEndPoint(EdgePair edge) {
		return edge.getEdges().get(0).getEnd().getPoint();
	}

	private Line2D createLineFromEdge(EdgePair edge) {
		return new Line2D.Double(edge.getEdges().get(0).getStart().getPoint(),
				edge.getEdges().get(0).getEnd().getPoint());
	}

	private void sortEdgesByDistance(List<EdgePair> edges) {
		edges.sort(new Comparator<EdgePair>() {

			@Override
			public int compare(EdgePair arg0, EdgePair arg1) {
				double dist1 = getDistance(arg0);
				double dist2 = getDistance(arg1);
				return dist1 < dist2 ? -1 : (dist1 > dist2) ? 1 : 0;
			}

		});
	}

	private List<EdgePair> generateEdges(List<Node> nodes) {
		List<EdgePair> edges = new ArrayList<>();
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.size(); j++) {
				if (j == i) {
					continue;
				}
				EdgePair newEdge = new EdgePair(nodes.get(i), nodes.get(j), 0);
				if (edges.contains(newEdge)) {
					continue;
				}
				edges.add(newEdge);
			}
		}
		return edges;
	}

	private List<Node> generateNodes(int nodeCount) {
		List<Node> nodes = new ArrayList<>();
		Node.ID = 0;
		for (int i = 0; i < nodeCount; i++) {
			Node newNode = new Node(getRandomPointInRectangle());
			if (!displayable) {
				nodes.add(newNode);
				continue;
			}
			boolean goodPosition = false;
			while (!goodPosition) {
				for (Node n : nodes) {
					if (n.getPoint().distance(newNode.getPoint()) < NODEDISTANCE) {
						goodPosition = false;
						break;
					}
					goodPosition = true;
				}
				if (goodPosition || nodes.isEmpty()) {
					nodes.add(newNode);
					break;
				} else {
					newNode = new Node(getRandomPointInRectangle());
				}
			}
		}
		return nodes;
	}

	private Point getRandomPointInRectangle() {
		return new Point(getRandomNumber(width), getRandomNumber(height));
	}

	private int getRandomNumber(int max) {
		return (int) (Math.random() * (max - 100)) + 100;
	}

	private int getRandomIndex(int size) {
		return (int) (Math.random() * (size)) + 0;
	}

	private double getDistance(EdgePair edge) {
		return edge.getEdges().get(0).getStart().getPoint().distance(edge.getEdges().get(0).getEnd().getPoint());
	}

}
