package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import model.Edge;
import model.EdgePair;
import model.Node;
import model.Network;

public class PreflowPush extends AbstractMaxFlowAlgorithm {

  private int[] currentArc;
  private int[] d;
  private int[] excess;
  
  private VisualizationData visData;

  private LinkedList<Node> s;
  
	private Network graph;
	private boolean visualization = false;

	public PreflowPush(Network g) {
		this.graph = g;
	}
	
	public void setVisualization(boolean b){
		visualization = b;
	}
	
	@Override
	public Network getGraph(){
		return graph;
	}
	
	@Override 
	public String getName(){
		return "Preflow-Push";
	}

	@Override
	public List<EdgePair> run() {
		init();
		iterate();
		return graph.getEdgePairs();
	}
	
	private void init(){
		currentArc = new int[graph.getHighestIndex()+1];
		d = new int[graph.getHighestIndex()+1];
		s = new LinkedList<Node>();
		excess = new int[graph.getHighestIndex()+1];
	  
		//Compute Valid Distance Labeling with BFS
		d = computeValidDistanceLabeling();
		d[graph.getStartNode().getId()] = graph.getNodes().size();
		d[graph.getEndNode().getId()] = 0;
	
		graph.getEdgePairs().stream().forEach(e -> e.clearCapacity());
		graph.getEdgesForNode(graph.getStartNode())
	    .stream()
	    .forEach(edge ->{
	              increaseExcess(edge.getEnd().getId(), edge.getAvailableCapacity());
	              edge.addCapacity(edge.getAvailableCapacity());
	              s.addFirst(edge.getEnd());
	            });
		
		
		if(visualization){
			visData = new VisualizationData();
			visData.addNetwork(graph.copy());
			visData.addNodeHighlight(null);
			visData.addNobeLabels(d.clone());
			visData.addPath(graph.getEdgesForNode(graph.getStartNode()));
			visData.addLabel("After Initialization");
			visData.setGoldbergTarjan(true);
			visData.addCut(Arrays.asList(graph.getStartNode()));
		}
	}
	
	
	
	private int[] computeValidDistanceLabeling(){
		BFSPreflowPush bfs = new BFSPreflowPush(graph, graph.getEndNode());
		return bfs.run();
	}
	
	private void iterate(){
	    while(!s.isEmpty()){
	      Node v = s.peek();
	      if(getCurrentArc(v) != null && (!isAdmissible(getCurrentArc(v)) || getCurrentArc(v).getAvailableCapacity() == 0 )){
	    	  	increaseCurrentArc(v);
	      }else if(getCurrentArc(v) != null && getCurrentArc(v).getAvailableCapacity() != 0 && isAdmissible(getCurrentArc(v))){
		        int w_id = getCurrentArc(v).getEnd().getId();
		        if(w_id != graph.getStartNode().getId() && w_id != graph.getEndNode().getId() && getExcess(w_id) == 0){
		  	       s.addLast(getCurrentArc(v).getEnd());
	  	        }
		        
	  	        //Increase Flow over (v,w) by
	  	        int min = Math.min(getExcess(v.getId()), getCurrentArc(v).getAvailableCapacity());
	  	        getCurrentArc(v).addCapacity(min);
	  	        //Increase Excess of w by min and decrease excess of f by that value
	  	        increaseExcess(w_id, min);
	  	        decreaseExcess(v.getId(), min);
	  	        
	  	        if(getExcess(v.getId()) == 0){
	  	        	s.pop();
	  	        }
	  	        if(visualization){
		  	        visData.addLabel("Pushed " + min + " capacity on Edge");
		  	        visData.addNobeLabels(d.clone());
		  	        visData.addNetwork(graph.copy());
		  	        visData.addNodeHighlight(null);
		  	        visData.addPath(Arrays.asList(getCurrentArc(v)));
		  	        visData.addCut(findCut());
	  	        }
	      }else{
		        int dmin = getMinD(v);
		        if(visualization){
			        visData.addLabel("Relabeled Node from " + d[v.getId()] + " to " + (dmin+1));
		        }
		        d[v.getId()] = dmin +1;
		        resetCurrentArc(v); 
		        
		        if(visualization){
		        	visData.addNetwork(graph.copy());
			        visData.addNobeLabels(d.clone());
			        visData.addNodeHighlight(v);
			        visData.addPath(new ArrayList<Edge>());
			        visData.addCut(findCut());
		        }
	      }
	    }
	}
	
	private List<Node> findCut(){
		BFSAll bfs = new BFSAll(graph.copy());
		return bfs.run();
	}
	
	private int getMinD(Node v){
		return graph.getEdgesForNode(v).stream().map(x -> {return x.getAvailableCapacity()!=0 ? d[x.getEnd().getId()]: Integer.MAX_VALUE;}).min(Integer::compare).get();
	}
	
	private boolean isAdmissible(Edge e){
		return d[e.getStart().getId()] == d[e.getEnd().getId()] +1;
	}
	
	private void increaseExcess(int id, int amount){
		excess[id] = excess[id] +amount;
	}
	
	private void decreaseExcess(int id, int amount){
		excess[id] = excess[id] - amount;
	}
	
	private int getExcess(int id){
		return excess[id];
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
  
	 private void resetCurrentArc(Node n){
	  currentArc[n.getId()] = 0;
	 }

	public VisualizationData getVisData() {
		return visData;
	}

}
