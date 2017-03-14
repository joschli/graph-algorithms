package algorithms;

import java.util.LinkedList;

import model.Edge;
import model.Network;
import model.Node;

public class BFSPreflowPush {
  
  private int[] distance;
  
  private Network graph;
  private LinkedList<Node> s;
  private boolean[] visited;
  private int[] currentArc;
  private Node start;
  
  public BFSPreflowPush(Network g, Node start){
    this.graph = g;
    this.start = start;
  }
  
  public int[] run(){
    init();
    iterate();
    return distance;
  }
  
  
  private void init(){
    s = new LinkedList<Node>();
    distance = new int[graph.getHighestIndex()+1];
    visited = new boolean[graph.getHighestIndex()+1];
    currentArc = new int[graph.getHighestIndex()+1];

    s.add(start);
    distance[start.getId()] = 0;
    visited[start.getId()] = true;
  }
  
  private void iterate(){
      while(!s.isEmpty()){
        Node v = s.peek();
        if(getCurrentArc(v) == null){
          s.pop();
        }else if(isVisited(getCurrentArc(v).getEnd())){
          increaseCurrentArc(v);
        }else{
          setVisited(getCurrentArc(v).getEnd());
          distance[getCurrentArc(v).getEnd().getId()] = distance[v.getId()]+1;
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
