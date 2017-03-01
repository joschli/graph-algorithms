package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Graph {

	private HashMap<Integer, Node> nodes;
	private List<Edge> edges;
	private HashMap<Integer, List<Edge>> edgesForNode;

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

	public void addEdge(Edge e) {
		edges.add(e);
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void addEdge(Node n1, Node n2, int maxCapacity) {
		edges.add(new Edge(n1,n2, maxCapacity));
	}
	
	public List<Edge> getEdgesForNode(Node n){
		return edgesForNode.get(n.getId());
	}
	
	public void calculateEdgesForNode(){
		for(Node n : getNodes()){
			List<Edge> edgesForN = edges
										.stream()
										.filter(x -> x.getStart().getId() == n.getId())
										.collect(Collectors.toList());
			edgesForNode.put(n.getId(),edgesForN) ;
		}
	}

}
