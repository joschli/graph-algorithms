package model;

import java.awt.Point;

public class Node {

	private static int ID = 0;

	private int id;
	private Point pos;

	public Node() {
		this.id = ID++;
	}

	public Node(Point pos) {
		this.id = ID++;
		this.pos = pos;
	}

	public int getId() {
		return id;
	}

	public Point getPoint() {
		return pos;
	}

}
