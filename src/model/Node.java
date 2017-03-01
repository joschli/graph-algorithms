package model;

public class Node {

	private static int ID = 0;

	private int id;

	public Node() {
		this.id = ID++;
	}

	public int getId() {
		return id;
	}
	

}
