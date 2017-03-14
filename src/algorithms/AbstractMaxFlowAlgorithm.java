package algorithms;

import java.util.List;

import model.EdgePair;
import model.Network;

public abstract class AbstractMaxFlowAlgorithm {
	
	public abstract List<EdgePair> run();
	
	public abstract Network getGraph();

	public abstract String getName();
	
}
