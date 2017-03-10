package generator;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.EdgePair;
import model.Graph;
import model.Node;

public class GraphGenerator {

	private int width;
	private int height;

	public GraphGenerator(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public List<Graph> generateGraph(int nodeCount, int graphCapacity) {
		List<Graph> graphs = new ArrayList<>();
		graphs.add(new Graph());

		List<Node> nodes = generateNodes(nodeCount);
		for (Node n : nodes) {
			lastGraph(graphs).addNode(n);
		}

		List<EdgePair> edges = generateEdges(nodes);
		sortEdgesByDistance(edges);

		EdgePair e = addEdge(lastGraph(graphs), edges);
		while (e != null) {
			Graph g = cloneGraph(lastGraph(graphs));
			g.addEdgePair(e);
			graphs.add(g);
			e = addEdge(lastGraph(graphs), edges);
		}

		distributeCapacities(lastGraph(graphs));
		return graphs;
	}

	private Graph lastGraph(List<Graph> graphs) {
		return graphs.get(graphs.size() - 1);
	}

	private EdgePair addEdge(Graph graph, List<EdgePair> edges) {
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

	private Graph cloneGraph(Graph graph) {
		Graph clone = new Graph();
		for (Node n : graph.getNodes()) {
			clone.addNode(n);
		}
		for (EdgePair e : graph.getEdgePairs()) {
			clone.addEdgePair(e);
		}
		return clone;
	}

	private void distributeCapacities(Graph graph) {
		// TODO set start/end and distribute capacity
	}

	private boolean noIntersectionWithGraph(Graph graph, EdgePair edge) {
		Line2D edgeLine = createLineFromEdge(edge);
		for (EdgePair graphEdge : graph.getEdgePairs()) {
			Line2D graphEdgeLine = createLineFromEdge(graphEdge);
			if (getStartPoint(edge) != getStartPoint(graphEdge) && getStartPoint(edge) != getEndPoint(graphEdge)
					&& getEndPoint(edge) != getStartPoint(graphEdge) && getEndPoint(edge) != getEndPoint(graphEdge)
					&& edgeLine.intersectsLine(graphEdgeLine)) {
				return false;
			}
		}
		return true;
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
				if (j != i) {
					edges.add(new EdgePair(nodes.get(i), nodes.get(j), 0));
				}
			}
		}
		return edges;
	}

	private List<Node> generateNodes(int nodeCount) {
		List<Node> nodes = new ArrayList<>();
		for (int i = 0; i < nodeCount; i++) {
			nodes.add(new Node(getRandomPointInRectangle()));
		}
		return nodes;
	}

	private Point getRandomPointInRectangle() {
		return new Point(getRandomNumber(width), getRandomNumber(height));
	}

	private int getRandomNumber(int max) {
		return (int) (Math.random() * max) + 10;
	}

	private double getDistance(EdgePair edge) {
		return edge.getEdges().get(0).getStart().getPoint().distance(edge.getEdges().get(0).getEnd().getPoint());
	}

}
