package algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Edge;
import model.Network;
import model.Node;

public class BFSAll {

	  private Network graph;
	  private LinkedList<Node> s;
	  private boolean[] visited;
	  private int[] currentArc;
	  private List<Node> nodes;
	  
	  
	  public BFSAll(Network g){
	    this.graph = g;
	  }
	  
	  public List<Node> run(){
	    init();
	    iterate();
	    return nodes;
	  }	  
	  
	  private void init(){
	    s = new LinkedList<Node>();
	    visited = new boolean[graph.getHighestIndex()+1];
	    currentArc = new int[graph.getHighestIndex()+1];
	    nodes = new ArrayList<Node>();
	    nodes.add(graph.getStartNode());
	    s.add(graph.getStartNode());
	    visited[graph.getStartNode().getId()] = true;
	  }
	  
	  private void iterate(){
	      while(!s.isEmpty()){
	        Node v = s.peek();
	        if(getCurrentArc(v) == null){
	          s.pop();
	        }else if(isVisited(getCurrentArc(v).getEnd()) || getCurrentArc(v).getAvailableCapacity() == 0){
	          increaseCurrentArc(v);
	        }else{
	          setVisited(getCurrentArc(v).getEnd());
	          nodes.add(getCurrentArc(v).getEnd());
	          s.add(getCurrentArc(v).getEnd());
	        }
	      }
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
}
