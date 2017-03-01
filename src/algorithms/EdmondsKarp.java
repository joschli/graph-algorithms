package algorithms;

import java.util.List;

import model.Edge;
import model.Graph;

public class EdmondsKarp extends AbstractMaxFlowAlgorithm {
	private Graph g;
	private int minCapacity;
	
	public EdmondsKarp(Graph g){
		this.g = g;
	}
	
	@Override
	public List<Edge> run() {
		init();
		List<Edge> path; 
		while((path = findNewFlowAugmentingPath()).size() > 0){
			increaseFlow(path);
		}
		
		assert(g.getEdges().stream().allMatch(x -> x.getCapacity() > 0));
		return g.getEdges();
	}
	
	private void init(){
		g.getEdges().stream().forEach(e -> e.clearCapacity());
	}
	
	private List<Edge> findNewFlowAugmentingPath(){
		BFS search = new BFS(g);
		List<Edge> path = search.run();
		minCapacity = search.getAvailableCapacity();
		return path;
	}
	
	private void increaseFlow(List<Edge> path){
		path.stream().forEach(x -> x.addCapacity(minCapacity));
	}
	
	public void printFlow(List<Edge> flow){
		System.out.println("Flow:");
		flow.stream().forEach(x -> System.out.println(x.getCapacity()));
		System.out.println("---------------");
	}
}
