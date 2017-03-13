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

		while ((displayable && !checkEdges(lastGraph(graphs)))
				|| !distributeCapacities(lastGraph(graphs), graphCapacity)) {
			graphs = generateGraph(nodeCount);
		}

		return graphs;
	}

	private boolean checkEdges(Network lastGraph) {
		for (Node n : lastGraph.getNodes()) {
			for (EdgePair pair : lastGraph.getEdgePairs()) {
				double dist = createLineFromEdge(pair).ptLineDist(n.getPoint());
				if (dist > 0.0 && dist < 30) {
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

	private boolean distributeCapacities(Network graph, int maxCapacity) {
		// TODO set start/end and distribute capacity
		Node min = null;
		Node max = null;
		for (Node n : graph.getNodes()) {
			if (min == null) {
				min = n;
			}
			if (n.getPoint().getX() < min.getPoint().getX() && n.getPoint().getY() > min.getPoint().getY()) {
				min = n;
			}
		}
		for (Node n : graph.getNodes()) {
			if (max == null && !n.equals(min)) {
				max = n;
			}
			if (max != null && n.getPoint().getX() > max.getPoint().getX()
					&& n.getPoint().getY() < max.getPoint().getY()) {
				max = n;
			}
		}

		assert (!min.equals(max));
		graph.setStartNode(min);
		graph.setEndNode(max);

		if (!checkIfConnected(graph)) {
			return false;
		}

		List<Integer> capacities = new ArrayList<>();
		for (int i = 0; i < graph.getEdgePairs().size(); i++) {
			int cap = getRandomCap((int) (maxCapacity / graph.getEdgePairs().size()));
			capacities.add(getRandomCap(maxCapacity));
		}

		for (int i = 0; i < graph.getEdgePairs().size(); i++) {
			graph.getEdgePairs().get(i).setCapacity(capacities.get(i));
		}

		if (!checkIndicentEdges(graph)) {
			return false;
		}

		return true;
	}

	private boolean checkIndicentEdges(Network graph) {
		FordFulkerson fordFulkerson = new FordFulkerson(graph);
		return false;
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
					if (n.getPoint().distance(newNode.getPoint()) < 30) {
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

	private double getDistance(EdgePair edge) {
		return edge.getEdges().get(0).getStart().getPoint().distance(edge.getEdges().get(0).getEnd().getPoint());
	}

}
