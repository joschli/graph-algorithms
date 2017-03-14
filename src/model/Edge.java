package model;

public class Edge {
	private Node n1;
	private Node n2;
	private boolean forward;
	private EdgePair pair;

	public Edge(Node start, Node end, boolean forward, EdgePair pair) {
		this.n1 = start;
		this.n2 = end;
		this.forward = forward;
		this.pair = pair;
	}

	public void addCapacity(int capacity) {
		pair.addCapacity(capacity, forward);
	}

	public int getAvailableCapacity() {
		return pair.getAvailableCapacity(forward);
	}

	public Node getStart() {
		return this.n1;
	}

	public Node getEnd() {
		return this.n2;
	}

	public boolean getDirection() {
		return forward;
	}

	@Override
	public boolean equals(Object other) {
		Edge otherEdge = (Edge) other;
		return otherEdge.getStart().equals(this.getStart()) && otherEdge.getEnd().equals(this.getEnd());
	}

	@Override
	public String toString() {
		return "<html><b>" + pair.getCapacity() + "|" + (pair.getAvailableCapacity(true) + pair.getCapacity()) + "</b> </html>";
	}

}
