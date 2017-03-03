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

	public GraphGenerator() {
	}

	public Graph generateGraph(int nodeCount, int graphCapacity) {
		Graph graph = new Graph();
		List<Node> nodes = generateNodes(nodeCount);
		List<EdgePair> edges = generateEdges(nodes);
		sortEdgesByDistance(edges);
		for (EdgePair edge : edges) {
			if (noIntersectionWithGraph(graph, edge)) {
				graph.addEdgePair(edge);
			}
		}
		distributeCapacities(graph);
		return graph;
	}

	private void distributeCapacities(Graph graph) {
		// TODO set start/end and distribute capacity
	}

	private boolean noIntersectionWithGraph(Graph graph, EdgePair edge) {
		Line2D edgeLine = createLineFromEdge(edge);
		for (EdgePair graphEdge : graph.getEdgePairs()) {
			Line2D graphEdgeLine = createLineFromEdge(graphEdge);
			if (edgeLine.intersectsLine(graphEdgeLine)) {
				return false;
			}
		}
		return true;
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
				return dist1 > dist2 ? -1 : (dist1 < dist2) ? 1 : 0;
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
			nodes.add(new Node(getRandomPointInRectangle(10, 20)));
		}
		return nodes;
	}

	private Point getRandomPointInRectangle(int width, int height) {
		return new Point(getRandomNumber(width), getRandomNumber(height));
	}

	private int getRandomNumber(int max) {
		return (int) (Math.random() * max) + 1;
	}

	private double getDistance(EdgePair edge) {
		return edge.getEdges().get(0).getStart().getPoint().distance(edge.getEdges().get(0).getEnd().getPoint());
	}

}
