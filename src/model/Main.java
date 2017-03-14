package model;

import java.util.Arrays;
import java.util.List;

import algorithms.Dinic;
import algorithms.EdmondsKarp;
import algorithms.SimpleGraph;
import test.TestFactory;
import algorithms.FordFulkerson;
import algorithms.PreflowPush;
import ui.MainFrame;

public class Main {
	

	
	public static void main(String[] args) {
	  /*
		Node n0 = new Node();
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node n4 = new Node();
		Node n5 = new Node();

		EdgePair e1 = new EdgePair(n0, n1, 3);
		EdgePair e2 = new EdgePair(n0, n2, 3);
		EdgePair e3 = new EdgePair(n2, n1, 2);
		EdgePair e4 = new EdgePair(n1, n3, 2);
		EdgePair e5 = new EdgePair(n2, n4, 3);
		EdgePair e6 = new EdgePair(n4, n3, 4);
		EdgePair e7 = new EdgePair(n3, n5, 3);
		EdgePair e8 = new EdgePair(n4, n5, 2);

		Network g = new Network();
		Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8).stream().forEach(e -> g.addEdgePair(e));
		Arrays.asList(n0, n1, n2, n3, n4, n5).stream().forEach(n -> g.addNode(n));
		g.calculateEdgesForNode();


		Node n6 = new Node();
		Node n7 = new Node();
		Node n8 = new Node();
		Node n9 = new Node();

		EdgePair e9 = new EdgePair(n6, n7, 1);
		EdgePair e10 = new EdgePair(n6, n8, 1);
		EdgePair e11 = new EdgePair(n7, n8, 1);
		EdgePair e12 = new EdgePair(n7, n9, 1);
		EdgePair e13 = new EdgePair(n8, n9, 1);

		Network g2 = new Network();
		Arrays.asList(e9, e10, e11, e12, e13).stream().forEach(edge -> g2.addEdgePair(edge));
		Arrays.asList(n6, n7, n8, n9).stream().forEach(n -> g2.addNode(n));
		g2.calculateEdgesForNode();
*/
		
		/*FordFulkerson f = new FordFulkerson(g);
		List<EdgePair> flow = f.run();
		Graph.printFlow(flow);
		
		EdmondsKarp e = new EdmondsKarp(g);
		List<EdgePair> flow = e.run();
		g.printFlow(flow);
		*/
		/*Dinic d = new Dinic(g);
		List<EdgePair> flow = d.run();
		Network.printFlow(flow);
		*/
		/*PreflowPush p = new PreflowPush(g);
		List<EdgePair> flow = p.run();
		Graph.printFlow(flow);*/

		TestFactory.createMediumTestCase().run("TEST_Medium_2.txt");
		}
	
	
	
}
