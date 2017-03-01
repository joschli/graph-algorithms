package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import model.Edge;
import model.Graph;
import model.Node;

public class DFS {
	private Graph graph;
	private Stack<Node> s;
	private HashMap<Integer, Boolean> visited;
	private HashMap<Integer, Integer> currentArc;
	private List<Edge> path;
	private List<Integer> availableCapacity; 
	
	public DFS(Graph graph){
		this.graph = graph;
	}
	
	public List<Edge> run(){
		init();
		iterate();
		return path;
	}
	
	private void init(){
		s = new Stack<Node>();
		availableCapacity = new ArrayList<Integer>();
		path = new ArrayList<Edge>();
		visited = new HashMap<>();
		currentArc = new HashMap<>();
		
		s.add(graph.getNodes().get(0));
		availableCapacity.add(Integer.MAX_VALUE);
		graph.getNodes().stream().forEach(x -> {visited.put(x.getId(), false); currentArc.put(x.getId(), 0);});
		visited.put(graph.getNodes().get(0).getId(), true);
	}
	
	private void iterate(){	
		while(!isVisited(graph.getNodes().get(graph.getNodes().size()-1)) && !s.isEmpty()){
			Node v = s.peek();
			if(getCurrentArc(v) == null){
				s.pop();
				if(v.getId() != graph.getNodes().get(0).getId()){
					path.remove(path.size()-1);
					availableCapacity.remove(availableCapacity.size()-1);
				}
			}else if(isVisited(getCurrentArc(v).getEnd()) || getCurrentArc(v).getAvailableCapacity() == 0){
				currentArc.put(v.getId(), currentArc.get(v.getId())+1);
			}else{
				path.add(getCurrentArc(v));
				availableCapacity.add(Math.min(availableCapacity.get(availableCapacity.size()-1), getCurrentArc(v).getAvailableCapacity()));
				visited.put(getCurrentArc(v).getEnd().getId(), true);
				s.push(getCurrentArc(v).getEnd());
			}
		}
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
		return availableCapacity.get(availableCapacity.size()-1);
	}
}
