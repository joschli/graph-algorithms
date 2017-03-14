package algorithms;

import java.util.ArrayList;
import java.util.List;

import model.Edge;
import model.Network;

public class VisualizationData {

	private List<Network> graphs;
	private List<List<Edge>> highlights;
	private List<List<Edge>> secondaryHighlights;
	
	private boolean hasSecondaryHighlights = false;
	
	public VisualizationData(){
		graphs = new ArrayList<Network>();
		highlights = new ArrayList<List<Edge>>();
		secondaryHighlights = new ArrayList<List<Edge>>();
	}
	
	public void addNetwork(Network n){
		graphs.add(n);
	}
	
	public void addPath(List<Edge> p){
		highlights.add(p);
	}
	
	public void addSecondaryHighlight(List<Edge> p){
		secondaryHighlights.add(p);
	}
	
	public void setSecondaryHighlights(boolean b){
		this.hasSecondaryHighlights = b;
	}
	
	public List<Network> getNetworks(){
		return graphs;
	}
	
	public List<List<Edge>> getHighlights(){
		return highlights;
	}
	
	public List<List<Edge>> getSecondaryHighlights(){
		return secondaryHighlights;
	}
	
	public boolean hasSecondaryHighlights(){
		return hasSecondaryHighlights;
	}
	
}
