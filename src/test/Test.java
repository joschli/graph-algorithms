package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import algorithms.AbstractMaxFlowAlgorithm;
import algorithms.BFSAll;
import generator.GraphGenerator;
import model.EdgePair;
import model.Network;
import model.Node;

public class Test {

	List<Triplet> triplets;
	GraphGenerator generator;

	public Test(List<Triplet> triplets) {
		this.triplets = triplets;
		this.generator = new GraphGenerator(100000, 100000, false);
	}

	public void run(String fileName) {
		List<InstanceGroup> instances = new ArrayList<>();
		Path file = Paths.get(fileName);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.toFile(), true));
		
			for (Triplet t : triplets) {
				instances.add(generateInstances(t));
			}
	
			int groupId = 0;
			int instanceId = 0;
			for (InstanceGroup group : instances) {
				instanceId = 0;
				for (Network instance : group.getInstances()) {
					List<AbstractMaxFlowAlgorithm> algorithms = AlgorithmFactory.createAlgorithm(instance);
					List<InstanceResult> algoResults = new ArrayList<>();
					for (AbstractMaxFlowAlgorithm algorithm : algorithms) {
						List<EdgePair> result = algorithm.run();
						// getName in algorithm impls
						InstanceResult res = new InstanceResult(algorithm.getName(), result, algorithm.getGraph());
						checkResultForValidity(res);
						checkTestEnvironment(instance, algorithm);
						algoResults.add(res);
					}
					checkResultEquality(algoResults);
					group.save(instance,algoResults);
					log(bw, groupId, instanceId, algoResults);
					instanceId++;
				}
				groupId++;
			}
		} catch (IOException e) {
		}
		
	}
	
	private void log(BufferedWriter bw, int groupId, int instanceId, List<InstanceResult> algoResults) throws IOException{
		bw.newLine();
		bw.write("-------------------------------------------------------------------------------------");
		bw.newLine();
		bw.write(new Date().toString());
		bw.newLine();
		bw.write("GROUP: " + groupId);
		bw.newLine();
		bw.write("INSTANCE: " + instanceId);
		bw.newLine();
		for(InstanceResult res : algoResults){
			bw.write("ALGORITHM: " + res.algorithm);
			bw.newLine();
			if(res.isValid()){
				bw.write("NO ERRORS - VALID");
				bw.newLine();
			}else{
				bw.write("ERRORS:" + res.errors.size() + " - NOT VALID");
				bw.newLine();
				for(FlowError e : res.errors){
					bw.write(e.name);
					bw.newLine();
				}
			}
		}
		bw.flush();
	}
	
	private void checkResultEquality(List<InstanceResult> res){
		if(res.stream().map(x -> getFlow(x.instance)).distinct().collect(Collectors.toList()).size() != 1){
			res.stream().forEach(x -> x.addError(FlowError.createDifferentResultError()));
		}
	}
	
	private int getFlow(Network n){
		int outflowStart = n.getEdgePairs().stream().filter(x ->  (x.fwEdge.getStart().getId() == n.getStartNode().getId())).map(x -> x.getCapacity()).reduce(0, Integer::sum);
		return outflowStart;
	}

	private void checkResultForValidity(InstanceResult result){
		checkFlowConstraints(result);
		checkCapacityConstraints(result);
		checkSaturatedCut(result);
	}
	
	private void checkSaturatedCut(InstanceResult result) {
		BFSAll bfs = new BFSAll(result.instance);
		if(bfs.run().contains(result.instance.getEndNode())){
			result.addError(FlowError.createSaturatedCutError());
		}
	}

	private void checkCapacityConstraints(InstanceResult result) {
		List<EdgePair> edges = result.result;
		if(edges.stream().anyMatch(x -> (x.getCapacity() > x.getMaxCapacity() || x.getCapacity() < 0))){
			result.addError(FlowError.createCapacityError());
		}
	}

	private void checkFlowConstraints(InstanceResult result) {		
		List<EdgePair> edges = result.result;

		int outflowStart = edges.stream().filter(x ->  (x.fwEdge.getStart().getId() == result.instance.getStartNode().getId())).map(x -> x.getCapacity()).reduce(0, Integer::sum);
		int inflowEnd = edges.stream().filter(x ->  (x.fwEdge.getEnd().getId() == result.instance.getEndNode().getId())).map(x -> x.getCapacity()).reduce(0, Integer::sum);
		if(outflowStart != inflowEnd){
			result.addError(FlowError.createFlowConstraintStartError());
			return;
		}
		
		for(Node n : result.instance.getNodes()){
			if(n.getId() != result.instance.getStartNode().getId() && n.getId() != result.instance.getEndNode().getId()){
				//Einfluss = Ausfluss
				int inflow = edges.stream().filter(x ->  (x.fwEdge.getEnd().getId() == n.getId())).map(x -> x.getCapacity()).reduce(0, Integer::sum);
				int outflow = edges.stream().filter(x ->  (x.fwEdge.getStart().getId() == n.getId())).map(x -> x.getCapacity()).reduce(0, Integer::sum);
				if(inflow != outflow){
					result.addError(FlowError.createFlowConstraintError());
					return;
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
