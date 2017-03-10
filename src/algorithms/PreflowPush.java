package algorithms;

import java.util.List;

import model.EdgePair;
import model.Network;

public class PreflowPush extends AbstractMaxFlowAlgorithm {

	private Network g;

	public PreflowPush(Network g) {
		this.g = g;
	}

	@Override
	public List<EdgePair> run() {
		init();
		while (true) {
			break;
		}
		return null;
	}

	private void init() {
		g.getEdgePairs().stream().forEach(e -> e.clearCapacity());
	}

}
