package model;

public class Edge {
	private Node start;
	private Node end;
	private int maxCapacity;
	private int capacity;

	public Edge(Node start, Node end, int maxCapacity) {
		this.start = start;
		this.end = end;
		this.maxCapacity = maxCapacity;
		this.capacity = 0;
	}

	public void addCapacity(int capacity) {
		this.capacity += capacity;
	}
	
	public void clearCapacity(){
		this.capacity = 0;
	}
	
	public int getAvailableCapacity(){
		return maxCapacity - capacity;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public int getMaxCapacity() {
		return this.maxCapacity;
	}

	public Node getStart() {
		return this.start;
	}

	public Node getEnd() {
		return this.end;
	}
	
	

}
