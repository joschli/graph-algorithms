package algorithms;

import java.util.List;

import model.Edge;
import model.EdgePair;
import model.Graph;

public class Dinic extends AbstractMaxFlowAlgorithm {

	private Graph g;
	
	public Dinic(Graph g){
		this.g = g; 
	}
	@Override
	public List<EdgePair> run() {
		init();
		List<Edge> subgraph;
		while((subgraph = findSubgraph()).size() != 0){
			createAndAddBlockingFlow(subgraph);
		}
		return g.getEdgePairs();
	}
	
	private void init(){
		g.getEdgePairs().stream().forEach(e -> e.clearCapacity());
	}
	
	private List<Edge> findSubgraph(){
		BFS bfs = new BFS(g);
		return bfs.runDinic();
	}
	
	private void createAndAddBlockingFlow(List<Edge> subGraph){
		SimpleGraph sg = new SimpleGraph(subGraph, g.getStartNode(), g.getEndNode());
		while(true){
			DFSForDinic dfs = new DFSForDinic(sg);
			List<Edge> path = dfs.runDinic();
			if(path.size() == 0){
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
