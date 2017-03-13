package test;

import java.util.List;

import model.EdgePair;

public class InstanceResult {

	List<EdgePair> result;
	String algorithm;

	public InstanceResult(String algorithm, List<EdgePair> result) {
		this.algorithm = algorithm;
		this.result = result;
	}
}
