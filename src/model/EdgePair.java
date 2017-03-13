package model;

import java.util.Arrays;
import java.util.List;

public class EdgePair {

	public int capacity;
	public int maxCapacity;

	public Edge fwEdge;
	public Edge bwEdge;

	public EdgePair(Node start, Node end, int maxCapacity) {
		this.maxCapacity = maxCapacity;
		this.capacity = 0;

		this.fwEdge = new Edge(start, end, true, this);
		this.bwEdge = new Edge(end, start, false, this);
	}

	public void addCapacity(int capacity, boolean forward) {
		if (forward) {
			this.capacity += capacity;
		} else {
			this.capacity -= capacity;
		}
		;
	}

	public void setCapacity(int max) {
		this.maxCapacity = max;
	}

	public int getAvailableCapacity(boolean forward) {
		if (forward) {
			return maxCapacity - capacity;
		} else {
			return capacity;
		}
	}

	public void clearCapacity() {
		this.capacity = 0;
	}

	public List<Edge> getEdges() {
		return Arrays.asList(fwEdge, bwEdge);
	}
	
	public EdgePair clone(){
		EdgePair copy =  new EdgePair(fwEdge.getStart().clone(), fwEdge.getEnd().clone(), maxCapacity);
		copy.setActualCapacity(this.capacity);
		return copy;
	}
	
	public void setActualCapacity(int amount){
		this.capacity = amount;
	}

	public int getCapacity() {
		return capacity;
	}

	@Override
	public boolean equals(Object other) {
		EdgePair otherEdgePair = (EdgePair) other;
		return otherEdgePair.fwEdge.equals(this.fwEdge) && otherEdgePair.bwEdge.equals(this.bwEdge)
				|| otherEdgePair.fwEdge.equals(this.bwEdge) && otherEdgePair.bwEdge.equals(this.fwEdge);
	}

	public boolean contains(Edge edge) {
		return fwEdge.equals(edge) || bwEdge.equals(edge);
	}

	public boolean contains(Node n) {
		return fwEdge.getStart().equals(n) || fwEdge.getEnd().equals(n);
	}
}
