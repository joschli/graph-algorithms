package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Edge;
import model.Network;
import model.Node;

public class SimpleGraph extends Network {
	public List<Edge> edges;
	public Set<Node> nodes;
	public HashMap<Integer, List<Edge>> edgesForNode;
	public HashMap<Integer, List<Edge>> edgesToNode;

	public SimpleGraph(List<Edge> e, Node start, Node end) {
		this.edges = new ArrayList<Edge>(e);
		this.setStartNode(start);
		this.setEndNode(end);
		nodes = new HashSet<Node>();
		edgesForNode = new HashMap<Integer, List<Edge>>();
		edgesToNode = new HashMap<Integer, List<Edge>>();
		calculateEdgesForNodes();
	}

	private void calculateEdgesForNodes() {
		edges.stream().forEach((x) -> {
			nodes.add(x.getStart());
			nodes.add(x.getEnd());
			List<Edge> e = edgesForNode.getOrDefault(x.getStart().getId(), new ArrayList<Edge>());
			e.add(x);
			edgesForNode.put(x.getStart().getId(), e);
			List<Edge> e2 = edgesToNode.getOrDefault(x.getEnd().getId(), new ArrayList<Edge>());
			e2.add(x);
			edgesToNode.put(x.getEnd().getId(), e2);
		});
	}

	public void removeEdge(Edge e) {
		edges.remove(e);
		edgesForNode.get(e.getStart().getId()).remove(e);
		edgesToNode.get(e.getEnd().getId()).remove(e);
	}

	public void removeNode(Node n) {
		System.out.println("REMOVING NODE: n" + n.getId());
		nodes.remove(n);
		edgesForNode.get(n.getId()).stream().forEach(x -> removeEdge(x));
		edgesToNode.get(n.getId()).stream().forEach(x -> removeEdge(x));
	}

	@Override
	public List<Node> getNodes() {
		return new ArrayList<Node>(nodes);
	}

	@Override
	public List<Edge> getEdgesForNode(Node n) {
		return edgesForNode.get(n.getId());
	}

}