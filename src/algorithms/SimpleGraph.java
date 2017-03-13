package algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Edge;
import model.Network;
import model.Node;

public class SimpleGraph extends Network {
	private List<Edge> edges;
	private Set<Node> nodes;
	private List<List<Edge>> edgesForNode;
	private List<List<Edge>> edgesToNode;
	private boolean[][] edgeRemoved;
	private boolean[] nodeRemoved;
	private int highIdx = 0;
	
	public SimpleGraph (List<Edge> e, Node start, Node end){
		this.edges = new ArrayList<Edge>(e);
		this.setStartNode(start);
		this.setEndNode(end);
		nodes = new HashSet<Node>();
		calculateEdgesForNodes();
	}

	private void calculateEdgesForNodes() {
		edges.stream().forEach((x) -> {
			nodes.add(x.getStart());
			nodes.add(x.getEnd());
			if(x.getStart().getId() > highIdx){
			  highIdx = x.getStart().getId();
			}
			if(x.getEnd().getId() > highIdx){
			  highIdx = x.getEnd().getId();
			}
		});
		
		edgesForNode = new ArrayList<List<Edge>>(getHighestIndex()+1);
    edgesToNode = new ArrayList<List<Edge>>(getHighestIndex()+1);

    for(int i = 0; i < getHighestIndex()+1; i++){
      edgesForNode.add(new ArrayList<Edge>());
      edgesToNode.add(new ArrayList<Edge>());
    }
    
		edges.stream().forEach((x) -> {
		  List<Edge> e = edgesForNode.get(x.getStart().getId());
      e.add(x);
      edgesForNode.add(x.getStart().getId(), e);
      List<Edge> e2 = edgesToNode.get(x.getEnd().getId());
      e2.add(x);
      edgesToNode.add(x.getEnd().getId(), e2);
		});
    edgeRemoved = new boolean[getHighestIndex()+1][getHighestIndex()+1];
    nodeRemoved = new boolean[getHighestIndex()+1];
	}

	public void removeEdge(Edge e) {
		edgeRemoved[e.getStart().getId()][e.getEnd().getId()] = true;
	}

	public void removeNode(Node n) {
		nodeRemoved[n.getId()] = true;
	}

	@Override
	public int getHighestIndex() {return highIdx;};
	
	
	@Override
	public List<Node> getNodes(){
		return new ArrayList<Node>(nodes);
	}

	@Override
	public List<Edge> getEdgesForNode(Node n) {
		return edgesForNode.get(n.getId());
	}

  public boolean isAllowed(Edge edge) {
    return !edgeRemoved[edge.getStart().getId()][edge.getEnd().getId()] && !nodeRemoved[edge.getStart().getId()] && !nodeRemoved[edge.getEnd().getId()];
  }

}