package algorithms;

import java.util.ArrayList;
import java.util.List;

import model.Edge;
import model.Network;
import model.Node;

public class VisualizationData {
/*
 * Usage: Ford Fulkerson und EdmondsKarps = highlights, labels, graphs für hervorgehobene Pfade(List<Edge>), Beschreibungsstrings und Graphen nach jedem Schritt
 * Dinic: Testen mit hatSecondaryHighlights ob Dinic, zusätzlich zu den obigen noch secondaryHighlights als Zweite List<Edge> am besten in anderer Farbe
 * Preflow-Push: Mit goldberg testen ob ist, benutzt highlights, labels, graphs, nodelabels (für Beschriftung der Nodes nach Höhe), nodeHighlights zum Highlighten einzelner Nodes
 * 						=> NodeHighlight ist entweder null oder eine Node die gehighlightet werden soll, highlihgts ist entweder leere Liste oder eine einzelne Edge
 * 				=> Außerdem cuts => Gruppe von Nodes die zur einen Hälfte des Cuts gehören, iwie highlighten bei jedem Schritt
 * 
 * 
 */
	
	private List<Network> graphs;
	private List<List<Edge>> highlights;
	private List<List<Edge>> secondaryHighlights;
	private List<Node> nodeHighlight;
	private List<int[]> nodeLabels;
	private List<String> labels;
	private List<List<Node>> cuts;
	
	private boolean goldberg = false;
	private boolean hasSecondaryHighlights = false;
	
	public VisualizationData(){
		graphs = new ArrayList<Network>();
		highlights = new ArrayList<List<Edge>>();
		secondaryHighlights = new ArrayList<List<Edge>>();
		nodeHighlight = new ArrayList<Node>();
		nodeLabels = new ArrayList<int[]>();
		cuts = new ArrayList<List<Node>>();
		labels = new ArrayList<String>();
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
	
	public void addNodeHighlight(Node n){
		nodeHighlight.add(n);
	}
	
	public void addNobeLabels(int[] labels){
		nodeLabels.add(labels);
	}
	
	public void addLabel(String label){
		labels.add(label);
	}
	
	public List<String> getLabels(){
		return labels;
	}
	
	public List<int[]> getNodeLabels(){
		return nodeLabels;
	}
	
	public void setGoldbergTarjan(boolean b){
		goldberg = b;
	}
	
	public boolean isGoldbergTarjan(){
		return goldberg;
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
	
	public List<Node> getNodeHighlights(){
		return nodeHighlight;
	}
	public void addCut(List<Node> cut){
		cuts.add(cut);
	}
	
	public List<List<Node>> getCuts(){
		return cuts;
	}
	
}
