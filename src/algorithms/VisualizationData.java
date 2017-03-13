package algorithms;

import java.util.ArrayList;
import java.util.List;

import model.Edge;
import model.Network;

public class VisualizationData {

	private List<Network> graphs;
	private List<List<Edge>> highlights;
	
	public VisualizationData(){
		graphs = new ArrayList<Network>();
		highlights = new ArrayList<List<Edge>>();
	}
	
	public void addNetwork(Network n){
		graphs.add(n);
	}
	
	public void addPath(List<Edge> p){
		highlights.add(p);
	}
	
	public List<Network> getNetworks(){
		return graphs;
	}
	
	public List<List<Edge>> getHighlights(){
		return highlights;
	}
	
}
