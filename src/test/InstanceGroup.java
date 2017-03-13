package test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Network;

public class InstanceGroup {

	private List<Network> instances;
	private Map<Network, InstanceResult> instanceToResult;

	public InstanceGroup(List<Network> instances) {
		this.instances = instances;
		this.instanceToResult = new HashMap<>();
	}

	public List<Network> getInstances() {
		return instances;
	}

	public void save(Network instance, InstanceResult result) {
		instanceToResult.put(instance, result);
	}

	// TODO save info about run on these instances

}
