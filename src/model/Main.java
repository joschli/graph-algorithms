package model;

import java.util.Arrays;
import java.util.List;

import algorithms.EdmondsKarp;
import algorithms.FordFulkerson;

public class Main {
	public static void main(String[] args) {
		Node n0 = new Node();
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node n4 = new Node();
		Node n5 = new Node();
		
		Edge e1 = new Edge(n0, n1, 2);
		Edge e2 = new Edge(n0, n2, 3);
		Edge e3 = new Edge(n2, n1, 2);
		Edge e4 = new Edge(n1, n3, 2);
		Edge e5 = new Edge(n2, n4, 3);
		Edge e6 = new Edge(n4, n3, 4);
		Edge e7 = new Edge(n3, n5, 3);
		Edge e8 = new Edge(n4, n5, 2);
		
		
		Graph g = new Graph();
		Arrays.asList(e1,e2,e3,e4,e5,e6,e7,e8).stream().forEach(e -> g.addEdge(e));
		Arrays.asList(n0,n1,n2,n3,n4,n5).stream().forEach(n -> g.addNode(n));
		g.calculateEdgesForNode();
		Graph g1 = g;
		
		FordFulkerson f = new FordFulkerson(g);
		List<Edge> flow = f.run();
		f.printFlow(flow);
		
		EdmondsKarp e = new EdmondsKarp(g1);
		List<Edge> flow2 = e.run();
		e.printFlow(flow2);
		
		
	}
}
