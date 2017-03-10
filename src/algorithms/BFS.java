package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	
	//DINIC SPEZIELL
	private HashMap<Integer, Integer> level;
	private HashMap<Integer, Set<Edge>> edgesForNode;
	
	
	public BFS(Graph graph){
		this.graph = graph;
	}
	
	private boolean isVisited(Node n){
		return visited.get(n.getId());
	}
	
	private void setVisited(Node n){
		visited.put(n.getId(), true);
	}
	
	private Edge getCurrentArc(Node n){
		if(graph.getEdgesForNode(n).size() > currentArc.get(n.getId())){
			return graph.getEdgesForNode(n).get(currentArc.get(n.getId()));
		}else{
			return null;
		}	
	}

	private void increaseCurrentArc(Node n){
		currentArc.put(n.getId(), currentArc.get(n.getId())+1);
	}
	
	  //////////////////////////////////////////////////////////////////////////////////
	 //								Edmons-Karps 									 //
	//////////////////////////////////////////////////////////////////////////////////
	
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
		
		edgesForNode = new HashMap<>();
		level = new HashMap<>();
		level.put(graph.getStartNode().getId(), 0);
		

		s.add(graph.getStartNode());
		availableCapacity.put(graph.getStartNode().getId(), Integer.MAX_VALUE);
		graph.getNodes()
				.stream()
				.forEach(x -> {
					visited.put(x.getId(), false);
					currentArc.put(x.getId(), 0);
					edgesForNode.put(x.getId(), new HashSet<Edge>());});
		visited.put(graph.getStartNode().getId(), true);
	}
	
	private void iterate(){	
		while(!isVisited(graph.getEndNode()) && !s.isEmpty()){
			Node v = s.peek();
			if(getCurrentArc(v) == null){
				s.pop();
			}else if(isVisited(getCurrentArc(v).getEnd()) || getCurrentArc(v).getAvailableCapacity() == 0){
				increaseCurrentArc(v);
			}else{
				availableCapacity.put(getCurrentArc(v).getEnd().getId(), appendCapacity(v));
				pathToNode.put(getCurrentArc(v).getEnd().getId(), appendPath(v));
				setVisited(getCurrentArc(v).getEnd());
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
	

	public int getAvailableCapacity(){
		return availableCapacity.getOrDefault(graph.getEndNode().getId(),0);
	}
	
	
	  ///////////////////////////////////////////////////////////////////////////////////////////////
	 //									DINIC													  //
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<Edge> runDinic(){
		init();
		iterateDinic();
		return new ArrayList<Edge>(edgesForNode.get(graph.getEndNode().getId()));		
	}
	
	private void iterateDinic(){
		int currLevel = 0;
		Node v = s.peek();
		currLevel = level.get(v.getId());
		//Break as soon as the target level is finished
		while(currLevel <= level.getOrDefault(graph.getEndNode().getId(), Integer.MAX_VALUE) && !s.isEmpty()){
			if(getCurrentArc(v) == null){
				//If Node is finished pop Node from Q
				s.pop();
				if(s.isEmpty()){
					return;
				}
			}else if(!isTargetHigherLevel(v, getCurrentArc(v).getEnd()) ||
					getCurrentArc(v).getAvailableCapacity() == 0){
				//If Arc leads to higher Level node or is nonexistent, go to next arc
				increaseCurrentArc(v);
			}else{
				//Else updateNodes for End of Current Arc and if seen for first time set Level and visited and add to q
				mergeEdgesForNode(getCurrentArc(v).getEnd(), v);
				level.putIfAbsent(getCurrentArc(v).getEnd().getId(), currLevel+1);
				if(!isVisited(getCurrentArc(v).getEnd())){
					setVisited(getCurrentArc(v).getEnd());
					s.add(getCurrentArc(v).getEnd());
				}
				increaseCurrentArc(v);
			}
			v = s.peek();
			currLevel = level.getOrDefault(v.getId(), Integer.MAX_VALUE);
		}
	}
	
	
	private void mergeEdgesForNode(Node n, Node prev){
		edgesForNode.compute(n.getId(), (k, v) -> {v.addAll(edgesForNode.get(prev.getId())); v.add(getCurrentArc(prev)); return v;});
	}

	
	private boolean isTargetHigherLevel(Node v, Node target){
		assert(level.get(v.getId()) != null);
		return level.getOrDefault(target.getId(), Integer.MAX_VALUE) > level.get(v.getId()); 
	}
	
}
