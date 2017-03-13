package generator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import model.Network;
import model.Node;

public class PathExists {

	private Network network;
	private Node start, goal;

	private List<Pair<Node, Integer>> distances;
	private List<Node> nodes;

	public PathExists(Network n, Node start, Node goal) {
		this.network = n;
		this.start = start;
		this.goal = goal;
		this.distances = new ArrayList<>();
		this.nodes = new ArrayList<>();
	}

	public boolean run() {
		init();
		while (!nodes.isEmpty()) {
			Pair<Node, Integer> u = getNextPair();
			if (u.getLeft().equals(goal)) {
				return u.getRight() < Integer.MAX_VALUE;
			}
			nodes.remove(u.getLeft());
			for (Node v : network.getNeighbors(u.getLeft())) {
				if (nodes.contains(v)) {
					updateDistances(u, v);
				}
			}
		}
		return false;
	}

	private Pair<Node, Integer> getNextPair() {
		for (Pair<Node, Integer> pair : distances) {
			if (nodes.contains(pair.getLeft())) {
				return pair;
			}
		}
		return null;
	}

	private void updateDistances(Pair<Node, Integer> u, Node v) {
		int dist = distances.get(0).getRight() + 1;
		int idx = getPair(v);
		if (distances.get(idx).getRight() > dist) {
			distances.remove(idx);
			distances.add(Pair.of(v, dist));
			sortDistances();
		}
	}

	private int getPair(Node v) {
		for (int i = 0; i < distances.size(); i++) {
			if (distances.get(i).getLeft().equals(v)) {
				return i;
			}
		}
		return -1;
	}

	public void init() {
		for (Node n : network.getNodes()) {
			nodes.add(n);
			if (n.equals(start)) {
				continue;
			}
			distances.add(Pair.of(n, Integer.MAX_VALUE));
		}
		distances.add(Pair.of(start, 0));
		sortDistances();
	}

	public void sortDistances() {
		distances.sort((left, right) -> {
			return left.getRight() - right.getRight();
		});
	}
}
