package model;

import java.util.ArrayList;
import java.util.List;

public class Graph {

	private List<Node> nodes;
	private List<Edge> edges;

	public Graph() {
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
	}

	public void addNode(Node n) {
		nodes.add(n);
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void addEdge(Edge e) {
		edges.add(e);
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void addEdge(Node n1, Node n2, int maxCapacity) {
		edges.add(new Edge(n1, n2, maxCapacity));
	}

}
