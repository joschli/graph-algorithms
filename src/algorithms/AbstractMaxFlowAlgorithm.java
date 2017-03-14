package algorithms;

import java.util.List;

import model.EdgePair;

public abstract class AbstractMaxFlowAlgorithm {
	
	public abstract List<EdgePair> run();

	public abstract String getName();
	
}
