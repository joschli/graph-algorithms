package algorithms;

import java.util.ArrayList;
import java.util.List;

import model.Edge;
import model.EdgePair;
import model.Network;

public class Dinic extends AbstractMaxFlowAlgorithm {

	private Network g;
	private VisualizationData data;
	
	public Dinic(Network g) {
		this.g = g;
	}

	@Override
	public List<EdgePair> run() {
		init();
		List<Edge> subgraph;
		while ((subgraph = findSubgraph()).size() != 0) {
			data.addPath(subgraph);
			createAndAddBlockingFlow(subgraph);
			data.addNetwork(g.copy());
		}
		return g.getEdgePairs();
	}

	private void init() {
		data = new VisualizationData();
		data.addNetwork(g.copy());
		data.addPath(new ArrayList<Edge>());
		data.addSecondaryHighlight(new ArrayList<Edge>());
		data.setSecondaryHighlights(true);
		g.getEdgePairs().stream().forEach(e -> e.clearCapacity());
	}

	private List<Edge> findSubgraph() {
		BFS bfs = new BFS(g);
		return bfs.runDinic();
	}
	// O(m*n) => m iterationen (Anzahl Edges, dajedes mal edges weniger werden) Dfs O(n) mit Nodebedingung
	private void createAndAddBlockingFlow(List<Edge> subGraph){
		SimpleGraph sg = new SimpleGraph(subGraph, g.getStartNode(), g.getEndNode());
		while (true) {
			DFSForDinic dfs = new DFSForDinic(sg);
			List<Edge> path = dfs.runDinic();
			if (path.size() == 0) {
				break;
			}
			int minCapacity = dfs.getAvailableCapacity();
			path.stream().forEach(x -> {
				x.addCapacity(minCapacity);
				sg.removeEdge(x);
			});
		}

	}

}
