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
	private boolean visualization = false;
	
	public FordFulkerson(Network g){
		this.g = g;
	}
	
	public void setVisualization(boolean b){
		visualization = b;
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
			increaseFlow(path);
			if(visualization){
				data.addPath(path);
				data.addNetwork(g.copy());
				data.addLabel("Augmenting Path got Augmented with" + minCapacity + " Capacity");
			}
		}
		return g.getEdgePairs();
	}
	
	private void init(){
		g.getEdgePairs().stream().forEach(e -> e.clearCapacity());

		if(visualization){
			data = new VisualizationData();
			data.addNetwork(g.copy());
			data.addPath(new ArrayList<Edge>());
			data.addLabel("After Initialization");
		}
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

	public VisualizationData getVisData() {
		return data;
	}

}
