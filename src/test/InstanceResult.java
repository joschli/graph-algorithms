package test;

import java.util.ArrayList;
import java.util.List;

import model.EdgePair;
import model.Network;

public class InstanceResult {

	List<EdgePair> result;
	String algorithm;
	Network instance;
	List<FlowError> errors;

	public InstanceResult(String algorithm, List<EdgePair> result, Network graphInstance) {
		this.algorithm = algorithm;
		this.result = result;
		this.instance = graphInstance;
		errors = new ArrayList<>();
	}
	
	public void addError(FlowError e){
		errors.add(e);
	}
	
	public boolean isValid(){
		return errors.size() == 0;
	}

}
