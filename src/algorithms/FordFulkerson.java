package algorithms;

import java.util.ArrayList;
import java.util.List;

import model.Edge;
import model.EdgePair;
import model.Network;

public class FordFulkerson extends AbstractMaxFlowAlgorithm {

	private Network g;
	private int minCapacity;
	private VisualizationData data;
	
	public FordFulkerson(Network g){
		this.g = g;
	}
	
	@Override 
	public String getName(){
		return "Ford-Fulkerson";
	}
	
	@Override
	public List<EdgePair> run() {
		init();
		List<Edge> path; 
		while((path = findNewFlowAugmentingPath()).size() > 0){
			data.addPath(path);
			increaseFlow(path);
			data.addNetwork(g.copy());
		}
		return g.getEdgePairs();
	}
	
	private void init(){
		data = new VisualizationData();
		g.getEdgePairs().stream().forEach(e -> e.clearCapacity());

		data.addNetwork(g.copy());
		data.addPath(new ArrayList<Edge>());
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
