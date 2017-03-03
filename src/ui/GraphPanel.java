package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import model.Edge;
import model.EdgePair;
import model.Graph;
import model.Node;

public class GraphPanel extends JPanel {

	private Graph graph;

	public GraphPanel() {
		this.graph = new Graph();
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		Graphics2D g2d = (Graphics2D) g;

		for (Node n : graph.getNodes()) {
			drawCenteredCircle(n.getPoint(), 10, g2d);
		}

		for (EdgePair e : graph.getEdgePairs()) {
			Edge edge = e.getEdges().get(0);
			drawEdge(edge.getStart().getPoint(), edge.getEnd().getPoint(), g2d);
		}
	}

	public void drawCenteredCircle(Point p, int radius, Graphics2D g) {
		Ellipse2D.Double circle = new Ellipse2D.Double(p.getX() - radius / 2, p.getY() - radius / 2, radius, radius);
		g.fill(circle);
	}

	public void drawEdge(Point p1, Point p2, Graphics2D g) {
		Line2D.Double edge = new Line2D.Double(p1, p2);
		g.draw(edge);
	}
}
