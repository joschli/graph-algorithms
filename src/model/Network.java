package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Network {

  private HashMap<Integer, Node> nodes;
  private List<EdgePair> edges;
  private List<Edge> realEdges;
  private List<List<Edge>> edgesForNode;
  private Node startNode;
  private Node endNode;
  private int highestIdx = 0;
  

  public Network() {
    nodes = new HashMap<>();
    edges = new ArrayList<>();
  }

  public Node getStartNode() {
    if(startNode == null){
      return getNodes().get(0);
    }
    return startNode;
  }

  public void setStartNode(Node startNode) {
    this.startNode = startNode;
  }

  public Node getEndNode() {
    if(endNode == null){
      return getNodes().get(getNodes().size()-1);
    }
    return endNode;
  }

  public void setEndNode(Node endNode) {
    this.endNode = endNode;
  }


  public void addNode(Node n) {
    if(n.getId() > highestIdx){
      highestIdx = n.getId();
    }
    nodes.put(n.getId(), n);
  }

  public List<Node> getNodes() {
    return nodes.values().stream().collect(Collectors.toList());
  }

  public void addEdgePair(EdgePair e) {
    edges.add(e);
  }

  public List<EdgePair> getEdgePairs() {
    return edges;
  }

  public void addEdgePair(Node n1, Node n2, int maxCapacity) {
    edges.add(new EdgePair(n1, n2, maxCapacity));
  }
  
  public List<Edge> getRealEdges(){
    return realEdges;
  }

  public List<Edge> getEdgesForNode(Node n) {
    return edgesForNode.get(n.getId());
  }

  private void calculateRealEdges(){
    realEdges = edges.stream().map(x -> x.getEdges()).flatMap(x -> x.stream()).collect(Collectors.toList());
  }

  public void calculateEdgesForNode() {
    calculateRealEdges();
    edgesForNode = new ArrayList<List<Edge>>(getHighestIndex());
    for(int i = 0; i < getHighestIndex()+1; i++){
      edgesForNode.add(new ArrayList<Edge>());
    }
    
    for (Node n : getNodes()) {
      List<Edge> edgesForN = edges
          .stream()
          .map(x -> x.getEdges())
          .flatMap(x -> x.stream())
          .filter(x -> x.getStart().getId() == n.getId())
          .collect(Collectors.toList());
      edgesForNode.add(n.getId(), edgesForN);
    }
  }
  
  public static void printFlow(List<EdgePair> flow){
    System.out.println("Flow:");
    flow.stream().forEach(x -> System.out.println(x.getCapacity()));
    System.out.println("---------------");
  }
  
  public int getHighestIndex(){
    return highestIdx;
  }
}
