package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Network {

	private HashMap<Integer, Node> nodes;
	private List<EdgePair> edges;
	private List<List<Edge>> edgesForNode;
	private Node startNode;
	private Node endNode;
	private int highestIdx = 0;

	public Network() {
		nodes = new HashMap<>();
		edges = new ArrayList<>();
	}

	public Node getStartNode() {
		if (startNode == null) {
			return getNodes().get(0);
		}
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		if (endNode == null) {
			return getNodes().get(getNodes().size() - 1);
		}
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public void addNode(Node n) {
		if (n.getId() > highestIdx) {
			highestIdx = n.getId();
		}
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

	public void getNodesFromEdgePairs() {

	}

	public void calculateEdgesForNode() {
		edgesForNode = new ArrayList<List<Edge>>(getHighestIndex());
		for (int i = 0; i < getHighestIndex() + 1; i++) {
			edgesForNode.add(new ArrayList<Edge>());
		}

		for (Node n : getNodes()) {
			List<Edge> edgesForN = edges.stream().map(x -> x.getEdges()).flatMap(x -> x.stream())
					.filter(x -> x.getStart().getId() == n.getId()).collect(Collectors.toList());
			edgesForNode.add(n.getId(), edgesForN);
		}
	}

	public static void printFlow(List<EdgePair> flow) {
		System.out.println("Flow:");
		flow.stream().forEach(x -> System.out.println(x.getCapacity()));
		System.out.println("---------------");
	}

	public int getHighestIndex() {
		return highestIdx;
	}

	public Network copy() {
		Network copy = new Network();
		getEdgePairs().stream().forEach(e -> copy.addEdgePair(e.clone()));
		copy.getEdgePairs().stream().map(x -> Arrays.asList(x.fwEdge.getStart(), x.fwEdge.getEnd()))
				.flatMap(x -> x.stream()).distinct().forEach(x -> copy.addNode(x));
		copy.calculateEdgesForNode();
		return copy;
	}

	public List<Node> getNeighbors(Node n) {
		List<Node> neighbors = new ArrayList<>();
		for (EdgePair e : edges) {
			if (!e.contains(n)) {
				continue;
			}
			if (e.fwEdge.getStart().equals(n)) {
				neighbors.add(e.fwEdge.getEnd());
			}
		}
		return neighbors;
	}

	public void clearCapacities() {
		getEdgePairs().stream().forEach(e -> e.clearCapacity());
	}

}
