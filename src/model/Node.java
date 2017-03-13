package model;

import java.awt.Point;

public class Node {

	public static int ID = 0;

	private int id;
	private Point pos;

	public Node() {
		this.id = ID++;
	}

	public Node(Point pos) {
		this.id = ID++;
		this.pos = pos;
	}

	private Node(int id, Point pos) {
		this.id = id;
		this.pos = pos;
	}

	public Node clone() {
		return new Node(this.id, this.pos);
	}

	public int getId() {
		return id;
	}

	public Point getPoint() {
		return pos;
	}

	public boolean equals(Object other) {
		if (other == null)
			return false;
		Node otherNode = (Node) other;
		return otherNode.getId() == this.getId() && otherNode.getPoint() != null && this.getPoint() != null
				&& otherNode.getPoint().equals(this.getPoint());
	}

	public String toString() {
		return "(" + this.id + ")";
	}

}
