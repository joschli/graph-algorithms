package algorithms;

import java.util.List;

import model.Edge;
import model.EdgePair;
import model.Network;

public class FordFulkerson extends AbstractMaxFlowAlgorithm {

	private Network g;
	private int minCapacity;
	
	public FordFulkerson(Network g){
		this.g = g;
	}
	
	@Override
	public List<EdgePair> run() {
		init();
		List<Edge> path; 
		while((path = findNewFlowAugmentingPath()).size() > 0){
			increaseFlow(path);
		}
		return g.getEdgePairs();
	}
	
	private void init(){
		g.getEdgePairs().stream().forEach(e -> e.clearCapacity());
	}
	
	private List<Edge> findNewFlowAugmentingPath(){
		DFS search = new DFS(g);
		List<Edge> path = search.run();
		minCapacity = search.getAvailableCapacity();
		return path;
	}
	
	private void increaseFlow(List<Edge> path){
		path.stream().forEach(x -> x.addCapacity(minCapacity));
	}

}
