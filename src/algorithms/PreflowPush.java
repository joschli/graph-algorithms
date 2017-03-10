package algorithms;

import java.util.List;

import model.EdgePair;
import model.Graph;

public class PreflowPush extends AbstractMaxFlowAlgorithm {

	private Graph g;
	
	public PreflowPush(Graph g){
		this.g = g; 
	}
	
	@Override
	public List<EdgePair> run() {
		init();
		while(true){
			break;
		}
		return null;
	}
	
	private void init(){
		g.getEdgePairs().stream().forEach(e -> e.clearCapacity());
	}

}
