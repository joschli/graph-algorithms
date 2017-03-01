package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import model.Edge;
import model.Graph;
import model.Node;

public class BFS {
	private Graph graph;
	private LinkedList<Node> s;
	private HashMap<Integer, Boolean> visited;
	private HashMap<Integer, Integer> currentArc;
	private HashMap<Integer,Integer> availableCapacity; 
	private HashMap<Integer,List<Edge>> pathToNode;
	
	
	public BFS(Graph graph){
		this.graph = graph;
	}
	
	public List<Edge> run(){
		init();
		iterate();
		return pathToNode.getOrDefault(graph.getNodes().get(graph.getNodes().size()-1).getId(), new ArrayList<>());
	}
	
	private void init(){
		s = new LinkedList<Node>();
		availableCapacity = new HashMap<>();
		pathToNode = new HashMap<>();
		visited = new HashMap<>();
		currentArc = new HashMap<>();

		s.add(graph.getNodes().get(0));
		availableCapacity.put(graph.getNodes().get(0).getId(), Integer.MAX_VALUE);
		graph.getNodes().stream().forEach(x -> {visited.put(x.getId(), false); currentArc.put(x.getId(), 0);});
		visited.put(graph.getNodes().get(0).getId(), true);
	}
	
	private void iterate(){	
		while(!isVisited(graph.getNodes().get(graph.getNodes().size()-1)) && !s.isEmpty()){
			Node v = s.peek();
			if(getCurrentArc(v) == null){
				s.pop();
			}else if(isVisited(getCurrentArc(v).getEnd()) || getCurrentArc(v).getAvailableCapacity() == 0){
				currentArc.put(v.getId(), currentArc.get(v.getId())+1);
			}else{
				availableCapacity.put(getCurrentArc(v).getEnd().getId(), appendCapacity(v));
				pathToNode.put(getCurrentArc(v).getEnd().getId(), appendPath(v));
				visited.put(getCurrentArc(v).getEnd().getId(), true);
				s.add(getCurrentArc(v).getEnd());
			}
		}
	}
	
	private int appendCapacity(Node n){
		return Math.min(availableCapacity.get(n.getId()), getCurrentArc(n).getAvailableCapacity());
	}
	
	private List<Edge> appendPath(Node n){
		List<Edge> path = new ArrayList<>();
		path.addAll(pathToNode.getOrDefault(n.getId(), new ArrayList<>()));
		path.add(getCurrentArc(n));
		return path;	
	}
	
	private boolean isVisited(Node n){
		return visited.get(n.getId());
	}
	
	private Edge getCurrentArc(Node n){
		if(graph.getEdgesForNode(n).size() > currentArc.get(n.getId())){
			return graph.getEdgesForNode(n).get(currentArc.get(n.getId()));
		}else{
			return null;
		}
		
	}
	
	public int getAvailableCapacity(){
		return availableCapacity.getOrDefault(graph.getNodes().get(graph.getNodes().size()-1).getId(),0);
	}
}
