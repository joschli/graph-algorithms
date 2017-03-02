package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Graph {

	private HashMap<Integer, Node> nodes;
	private List<EdgePair> edges;
	private HashMap<Integer, List<Edge>> edgesForNode;
	private Node startNode;
	private Node endNode;

	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public Graph() {
		nodes = new HashMap<>();
		edges = new ArrayList<>();
		edgesForNode = new HashMap<>();
	}

	public void addNode(Node n) {
		nodes.put(n.getId(), n);
	}

	public List<Node> getNodes() {
		return nodes.values().stream().collect(Collectors.toList());
	}

	public void addEdgePair(EdgePair e) {
		edges.add(e);
	}

	public List<EdgePair> getEdgePairs() {
		return edges;
	}

	public void addEdgePair(Node n1, Node n2, int maxCapacity) {
		edges.add(new EdgePair(n1, n2, maxCapacity));
	}

	public List<Edge> getEdgesForNode(Node n) {
		return edgesForNode.get(n.getId());
	}


	public void calculateEdgesForNode() {
		for (Node n : getNodes()) {
			List<Edge> edgesForN = edges
					.stream()
					.map(x -> x.getEdges())
					.flatMap(x -> x.stream())
					.filter(x -> x.getStart().getId() == n.getId())
					.collect(Collectors.toList());
			edgesForNode.put(n.getId(), edgesForN);
		}
	}

}
