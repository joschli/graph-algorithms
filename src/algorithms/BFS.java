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
	private LinkedList<Edge>[] pathToNode;
	
	//DINIC SPEZIELL
	private int[] level;
	//Jede Node gibt an welche Edges sie hat => edgesForNode[3][4][5] == true => Node 3 hat Edge von Node 4 zu Node 5
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
	 //								Edmons-Karps 									 //
	//////////////////////////////////////////////////////////////////////////////////
	
	public List<Edge> run(){
		init();
		iterate();
		return pathToNode[graph.getEndNode().getId()];
	}
	
	private void init(){
		s = new LinkedList<Node>();
		availableCapacity = new int[graph.getHighestIndex()+1];
		pathToNode = new LinkedList[graph.getHighestIndex()+1];
		visited = new boolean[graph.getHighestIndex()+1];
		currentArc = new int[graph.getHighestIndex()+1];
		
		edgesForNode = new LinkedList[graph.getHighestIndex()];
		level = new int[graph.getHighestIndex()+1];
		

		s.addLast(graph.getStartNode());
		availableCapacity[graph.getStartNode().getId()] = Integer.MAX_VALUE;
		graph.getNodes()
				.stream()
				.forEach(x -> {
					visited[x.getId()] = false;
					currentArc[x.getId()] = 0;
					level[x.getId()] = Integer.MAX_VALUE;
					pathToNode[x.getId()] = new LinkedList<Edge>();});
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
				pathToNode[getCurrentArc(v).getEnd().getId()] =  appendPath(v);
				setVisited(getCurrentArc(v).getEnd());
				s.addLast(getCurrentArc(v).getEnd());
			}
		}
	}
	

	private int appendCapacity(Node n){
		return Math.min(availableCapacity[n.getId()], getCurrentArc(n).getAvailableCapacity());
	}
	
	private LinkedList<Edge> appendPath(Node n){
		LinkedList<Edge> path = pathToNode[n.getId()];
		path.addLast(getCurrentArc(n));
		return path;	
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
		return edgesForNode[graph.getEndNode().getId()];
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
	  //edgesForNode.compute(n.getId(), (k, v) -> {v.addAll(edgesForNode.get(prev.getId())); v.add(getCurrentArc(prev)); return v;});
	}

	
	private boolean isTargetHigherLevel(Node v, Node target){
		return level[target.getId()] > level[v.getId()]; 
	}
	
}
