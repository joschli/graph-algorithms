package algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Edge;
import model.EdgePair;
import model.Network;

public class Dinic extends AbstractMaxFlowAlgorithm {

	private Network g;
	private VisualizationData data;
	private boolean visualization = false;
	
	public Dinic(Network g) {
		this.g = g;
	}
	

	public void setVisualization(boolean b){
		visualization = b;
	}

	@Override 
	public String getName(){
		return "Dinic";
	}
	@Override
	public List<EdgePair> run() {
		init();
		List<Edge> subgraph;
		while ((subgraph = findSubgraph()).size() != 0) {
			createAndAddBlockingFlow(subgraph);
			if(visualization){
				data.addNetwork(g.copy());
				data.addPath(subgraph);
			}
		}
		return g.getEdgePairs();
	}

	private void init() {
		g.getEdgePairs().stream().forEach(e -> e.clearCapacity());
		if(visualization){
			data = new VisualizationData();
			data.addNetwork(g.copy());
			data.addPath(new ArrayList<Edge>());
			data.addSecondaryHighlight(new ArrayList<Edge>());
			data.setSecondaryHighlights(true);
			data.addLabel("After Initialization");
		}
	}

	private List<Edge> findSubgraph() {
		BFS bfs = new BFS(g);
		return bfs.runDinic();
	}
	// O(m*n) => m iterationen (Anzahl Edges, dajedes mal edges weniger werden) Dfs O(n) mit Nodebedingung
	private void createAndAddBlockingFlow(List<Edge> subGraph){
		LinkedList<Edge> secondaryHighlight = new LinkedList<Edge>();
		SimpleGraph sg = new SimpleGraph(subGraph, g.getStartNode(), g.getEndNode());
		while (true) {
			DFSForDinic dfs = new DFSForDinic(sg);
			List<Edge> path = dfs.runDinic();
			if (path.size() == 0) {
				break;
			}
			int minCapacity = dfs.getAvailableCapacity();
			path.stream().forEach(x -> {
				secondaryHighlight.addFirst(x);
				x.addCapacity(minCapacity);
				sg.removeEdge(x);
			});
		}
		if(visualization){
			data.addLabel("Shortest Path Subgraph with Blocking Flow");
			data.addSecondaryHighlight(secondaryHighlight);
		}

	}

}
