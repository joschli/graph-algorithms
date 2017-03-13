package test;

import java.util.ArrayList;
import java.util.List;

import algorithms.AbstractMaxFlowAlgorithm;
import algorithms.Dinic;
import algorithms.EdmondsKarp;
import algorithms.FordFulkerson;
import algorithms.PreflowPush;
import model.Network;

public class AlgorithmFactory {

	public static List<AbstractMaxFlowAlgorithm> createAlgorithm(Network instance) {
		// TODO: init all algorithms
		List<AbstractMaxFlowAlgorithm> algorithms = new ArrayList<>();
		algorithms.add(new EdmondsKarp(instance));
		algorithms.add(new FordFulkerson(instance));
		algorithms.add(new PreflowPush(instance));
		algorithms.add(new Dinic(instance));
		return algorithms;
	}
}
