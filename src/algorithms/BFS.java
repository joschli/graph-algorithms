package algorithms;

import java.util.LinkedList;
import java.util.List;

import model.Edge;
import model.Network;
import model.Node;

public class BFS {
	private Network graph;
	private LinkedList<Node> s;
	private boolean[] visited;
	private int[] currentArc;
	private int[] availableCapacity; 
	private Edge[] pathToNode;
	
	//DINIC SPEZIELL
	private int[] level;
	private LinkedList<Edge>[] edgesForNode;
	
	
	public BFS(Network graph){
		this.graph = graph;
	}
	
	private boolean isVisited(Node n){
		return visited[n.getId()];
	}
	
	private void setVisited(Node n){
		visited[n.getId()] = true;
	}
	
	private Edge getCurrentArc(Node n){
		if(graph.getEdgesForNode(n).size() > currentArc[n.getId()]){
			return graph.getEdgesForNode(n).get(currentArc[n.getId()]);
		}else{
			return null;
		}	
	}

 private void increaseCurrentArc(Node n){
	    currentArc[n.getId()] = currentArc[n.getId()] +1;
 }
	
	  //////////////////////////////////////////////////////////////////////////////////
	 //								Edmonds-Karps 									 //
	//////////////////////////////////////////////////////////////////////////////////
	
	public List<Edge> run(){
		init();
		iterate();
		return getPath();
	}
	
	private List<Edge> getPath(){
		LinkedList<Edge> path = new LinkedList<Edge>();
		Node n = graph.getEndNode();
		for(Edge e = pathToNode[n.getId()]; e != null; e = pathToNode[e.getStart().getId()]){
			path.addFirst(e);
		}
		
		return path;
	}
	
	private void init(){
		s = new LinkedList<Node>();
		availableCapacity = new int[graph.getHighestIndex()+1];
		pathToNode = new Edge[graph.getHighestIndex()+1];
		visited = new boolean[graph.getHighestIndex()+1];
		currentArc = new int[graph.getHighestIndex()+1];
		
		edgesForNode = new LinkedList[graph.getHighestIndex()+1];
		level = new int[graph.getHighestIndex()+1];
		

		s.addLast(graph.getStartNode());
		availableCapacity[graph.getStartNode().getId()] = Integer.MAX_VALUE;
		graph.getNodes()
				.stream()
				.forEach(x -> {
					level[x.getId()] = Integer.MAX_VALUE;
					edgesForNode[x.getId()] = new LinkedList<Edge>();});
		visited[graph.getStartNode().getId()] = true;
		level[graph.getStartNode().getId()] = 0;
	}
	
	private void iterate(){	
		while(!isVisited(graph.getEndNode()) && !s.isEmpty()){
		  Node v = s.peek();
			if(getCurrentArc(v) == null){
				s.pop();
			}else if(isVisited(getCurrentArc(v).getEnd()) || getCurrentArc(v).getAvailableCapacity() == 0){
				increaseCurrentArc(v);
			}else{
				availableCapacity[getCurrentArc(v).getEnd().getId()] = appendCapacity(v);
				pathToNode[getCurrentArc(v).getEnd().getId()] =  getCurrentArc(v);
				setVisited(getCurrentArc(v).getEnd());
				s.addLast(getCurrentArc(v).getEnd());
			}
		}
	}
	

	private int appendCapacity(Node n){
		return Math.min(availableCapacity[n.getId()], getCurrentArc(n).getAvailableCapacity());
	}
	
	

	public int getAvailableCapacity(){
		return availableCapacity[graph.getEndNode().getId()];
	}
	
	
	  ///////////////////////////////////////////////////////////////////////////////////////////////
	 //									DINIC													  //
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<Edge> runDinic(){
		init();
		iterateDinic();
		return getEdges();
	}
	
	private List<Edge> getEdges(){
	  LinkedList<Edge> s = new LinkedList<Edge>(edgesForNode[graph.getEndNode().getId()]);
	  LinkedList<Edge> withoutDuplicates = new LinkedList<Edge>();
	  boolean[][] exists = new boolean[graph.getHighestIndex()+1][graph.getHighestIndex()+1];
	  
	  while(!s.isEmpty()){
	    Edge e = s.pop();
	    exists[e.getStart().getId()][e.getEnd().getId()] = true;
	    withoutDuplicates.addFirst(e);
	    while(edgesForNode[e.getStart().getId()].size() > 0){
	      Edge e_nxt = edgesForNode[e.getStart().getId()].pop();
	      if(!exists[e_nxt.getStart().getId()][e_nxt.getEnd().getId()]){
	        s.addFirst(e_nxt);
	      }
	    }
	    
	  }
	  return withoutDuplicates;
	}
	
	private void iterateDinic(){
		int currLevel = 0;
		Node v = s.peek();
		currLevel = level[v.getId()];
		//Break as soon as the target level is finished
		while(currLevel <= level[graph.getEndNode().getId()] && !s.isEmpty()){
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
				if(level[getCurrentArc(v).getEnd().getId()] == Integer.MAX_VALUE){
				  level[getCurrentArc(v).getEnd().getId()] = currLevel+1;
				}
				if(!isVisited(getCurrentArc(v).getEnd())){
					setVisited(getCurrentArc(v).getEnd());
					s.addLast(getCurrentArc(v).getEnd());
				}
				increaseCurrentArc(v);
			}
			v = s.peek();
			currLevel = level[v.getId()];
		}
	}
	
	
	private void mergeEdgesForNode(Node n, Node prev){
	  edgesForNode[n.getId()].addFirst(getCurrentArc(prev));
	}

	
	private boolean isTargetHigherLevel(Node v, Node target){
		return level[target.getId()] > level[v.getId()]; 
	}
	
}
