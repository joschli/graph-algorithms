package algorithms;

import java.util.LinkedList;
import java.util.List;

import model.Edge;
import model.Node;

public class DFSForDinic {

	private SimpleGraph graph;

	private LinkedList<Node> s;
	private boolean[] visited;
	private int[] currentArc;
	private LinkedList<Edge> path;
	private LinkedList<Integer> availableCapacity; 
	
	public DFSForDinic(SimpleGraph g){
		this.graph = g;
	}
	
	public List<Edge> runDinic(){
		init();
		iterateDinic();
		return path;
	}
	
	private void init(){
		s = new LinkedList<Node>();
		availableCapacity = new LinkedList<Integer>();
		path = new LinkedList<Edge>();
		visited = new boolean[graph.getHighestIndex()+1];
		currentArc = new int[graph.getHighestIndex()+1];
		
		s.addFirst(graph.getStartNode());
		availableCapacity.addFirst(Integer.MAX_VALUE);
		graph.getNodes().stream().forEach(x -> {visited[x.getId()] = false; currentArc[x.getId()] = 0;});
		visited[graph.getStartNode().getId()] = true;
	}
	
	private void iterateDinic(){
		while(!isVisited(graph.getEndNode()) && !s.isEmpty()){
			Node v = s.peek();
			if(getCurrentArc(v) == null){
				s.pop();
				if(v.getId() != graph.getStartNode().getId()){
					path.pop();
					availableCapacity.pop();
					graph.removeNode(v);
				}else{
					return;
				}
			}else if(isVisited(getCurrentArc(v).getEnd()) || getCurrentArc(v).getAvailableCapacity() == 0 || !graph.isAllowed(getCurrentArc(v))){
				increaseCurrentArc(v);
			}else{
			  path.addFirst(getCurrentArc(v));
			  availableCapacity.addFirst(Math.min(availableCapacity.peek(), getCurrentArc(v).getAvailableCapacity()));
			  visited[getCurrentArc(v).getEnd().getId()] = true;
			  s.addFirst(getCurrentArc(v).getEnd());
			}
		}
	}
	
	private boolean isVisited(Node n){
		return visited[n.getId()];
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
  
	public int getAvailableCapacity(){
		return availableCapacity.peek();
	}
}
