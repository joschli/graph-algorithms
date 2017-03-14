package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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

		try {
			Files.createDirectories(Paths.get("./logs/"));
		} catch (IOException e1) {
		}
		Path file = Paths.get("./logs/" + fileName);
		Path testFile = Paths.get("./logs/test_test_" + fileName);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.toFile(), true));
			BufferedWriter testW = new BufferedWriter(new FileWriter(testFile.toFile(), true));
		
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
						checkTestEnvironment(res, testW, groupId, instanceId);
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
		List<EdgePair> edges = result.instance.getEdgePairs();
		if(edges.stream().anyMatch(x -> (x.getCapacity() > x.getMaxCapacity() || x.getCapacity() < 0))){
			result.addError(FlowError.createCapacityError());
		}
	}

	private void checkFlowConstraints(InstanceResult result) {		
		List<EdgePair> edges = result.instance.getEdgePairs();

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

	private void checkTestEnvironment(InstanceResult res, BufferedWriter testW, int groupId, int instanceId) throws IOException{
		String s = "";
		if(checkCapacityTest(res)){
			s += "CAPACITY TEST: SUCCESS |";
		}else{
			s += "CAPACITY TEST: FAILURE |";
		}
		if(checkFlowTest(res)){
			s += "FLOW TEST: SUCCESS |";
		}else{
			s += "FLOW TEST: FAILURE |";
		}
		if(checkSatCutTest(res)){
			s += "SAT CUT TEST: SUCCESS |";
		}else{
			s += "SAT CUT TEST: FAILURE";
		}
		
		testW.newLine();
		testW.write("-------------------------------------------------");
		testW.newLine();
		testW.newLine();
		testW.write(new Date().toString());
		testW.newLine();
		testW.write("GROUP: " + groupId);
		testW.newLine();
		testW.write("INSTANCE: " + instanceId);
		testW.newLine();
		testW.write(s);
		testW.newLine();
		testW.flush();
	}

	private boolean checkSatCutTest(InstanceResult res) {
		InstanceResult testInstance = new InstanceResult(res.algorithm, res.result, res.instance.copy());
		testInstance.instance.getEdgePairs().stream().forEach(x -> x.setActualCapacity(0));
		checkSaturatedCut(testInstance);
		if(!testInstance.isValid()){
			return true;
		}
		return false;
	}

	private boolean checkFlowTest(InstanceResult res) {
		InstanceResult testInstance = new InstanceResult(res.algorithm, res.result, res.instance.copy());
		int randomIndex = ((int) (Math.random()*(testInstance.instance.getEdgePairs().size()-3)))+1;
		int cap = testInstance.instance.getEdgePairs().get(randomIndex).getCapacity();
		testInstance.instance.getEdgePairs().get(randomIndex).setActualCapacity(cap+1);;
		
		checkFlowConstraints(testInstance);
		if(!testInstance.isValid()){
			return true;
		}
		return false;
	}

	private boolean checkCapacityTest(InstanceResult res ) {
		InstanceResult testInstance = new InstanceResult(res.algorithm, res.result, res.instance.copy());
		EdgePair capError = testInstance.instance.getEdgePairs().get((int) (Math.random()*(testInstance.instance.getEdgePairs().size()-1)));
		capError.setActualCapacity(capError.getMaxCapacity()+1);
		checkCapacityConstraints(testInstance);
		if(!testInstance.isValid()){
			return true;
		}
		return false;
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
