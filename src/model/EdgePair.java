package model;

import java.util.Arrays;
import java.util.List;

public class EdgePair {
	
	public int capacity;
	public int maxCapacity;
	
	public Edge fwEdge;
	public Edge bwEdge;
	
	public EdgePair(Node start, Node end, int maxCapacity){
		this.maxCapacity = maxCapacity;
		this.capacity = 0;
		
		this.fwEdge = new Edge(start, end, true, this);
		this.bwEdge = new Edge(end, start,false, this);
	}
	
	public void addCapacity(int capacity, boolean forward) {
		if(forward){
			this.capacity += capacity;
		}else{
			this.capacity -= capacity;
		};
	}
	
	public int getAvailableCapacity(boolean forward){
		if(forward){
			return maxCapacity-capacity;
		}else{
			return capacity;
		}
	}
	
	public void clearCapacity(){
		this.capacity = 0;
	}
	
	public List<Edge> getEdges(){
		return Arrays.asList(fwEdge, bwEdge);
	}
	
	
	public int getCapacity(){
		return capacity;
	}

}
