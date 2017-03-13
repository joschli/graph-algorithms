package test;

import java.util.ArrayList;
import java.util.List;

import algorithms.AbstractMaxFlowAlgorithm;
import generator.GraphGenerator;
import model.EdgePair;
import model.Network;

public class Test {

	List<Triplet> triplets;
	GraphGenerator generator;

	public Test(List<Triplet> triplets) {
		this.triplets = triplets;
		this.generator = new GraphGenerator(100000, 100000, false);
	}

	public void run() {
		List<InstanceGroup> instances = new ArrayList<>();

		for (Triplet t : triplets) {
			instances.add(generateInstances(t));
		}

		for (InstanceGroup group : instances) {
			for (Network instance : group.getInstances()) {
				List<AbstractMaxFlowAlgorithm> algorithms = AlgorithmFactory.createAlgorithm(instance);
				for (AbstractMaxFlowAlgorithm algorithm : algorithms) {
					List<EdgePair> result = algorithm.run();
					// getName in algorithm impls
					group.save(instance, new InstanceResult("EdmondsKarp", result));
					checkTestEnvironment(instance, algorithm);
				}
			}
		}
	}

	private void checkTestEnvironment(Network instance, AbstractMaxFlowAlgorithm algorithm) {
		// TODO: change edge capacities, check for expected behavior
	}

	private InstanceGroup generateInstances(Triplet t) {
		List<Network> instances = new ArrayList<>();
		for (int i = 0; i < t.instances; i++) {
			List<Network> networkProcess = generator.generateGraph(t.nodes, t.capacity);
			instances.add(networkProcess.get(networkProcess.size() - 1));
		}
		return new InstanceGroup(instances);
	}

}
