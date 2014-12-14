package physics;

import geom.Graph;
import geom.Point;
import graphics.GC;

public class Engine {
	private Graph graph;

	private boolean clusterForceOn = true;
	private boolean distanceForceOn = true;
	private boolean proximityForceOn = true;

	public Engine(Graph graph) {
		this.graph = graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public void toggleClusterForce() {
		clusterForceOn = !clusterForceOn;
	}

	public void toggleDistanceForce() {
		distanceForceOn = !distanceForceOn;
	}

	public void toggleProximityForce() {
		proximityForceOn = !proximityForceOn;
	}

	// ALGORITHM:
	// Repel more for the sum of the degrees of the vertices
	// Repel more for larger graph distances (BFS)
	// Attract for smaller graph distances (BFS)
	// Repel for closer geometric distances (Distance Formula)
	// Attract to center of screen
	public void update() {
		for(int i = 0; i < graph.getSize(); i++) {
			if(graph.exists(i)) {
				for(int j = i + 1; j < graph.getSize(); j++)
					if(i != j && graph.exists(j))
						interract(i, j);
				floatCenter(i);
			}
		}
	}

	private double getClusterForce(int degSum) {
		double multiplier = 20;
		return degSum / multiplier;
	}

	private double getDistanceForce(int dist, int length) {
		double multiplier = 300;
		double adjacentForce = 17;
		double disconnectedForce = .4;
		double minLength = 0;
		if(dist == -1)
			return -disconnectedForce;
		if(dist == 1)
			return -adjacentForce;
		else
			return dist / (int)(length + minLength) * multiplier;
	}

	private double getProximityForce(int length) {
		double multiplier = 8.5;
		double optimumLength = 75;
		double minLength = 10;
		double standardForce = 10;
		if(length < minLength)
			return standardForce;
		else
			return multiplier / Math.pow(length / optimumLength, 2);
	}

	private double getGravityForce(int length, int deg) {
		double multiplier = .8;
		double minLength = 10;
		if(length > minLength)
			return (deg + 1) / multiplier;
		else
			return 0;
	}

	private void interract(int v1, int v2) {
		Point p1 = graph.getPoint(v1);
		Point p2 = graph.getPoint(v2);
		int degree1 = graph.getDegree(v1);
		int degree2 = graph.getDegree(v2);
		int distance = graph.distance(v1, v2);
		int length = p1.distance(p2);

		double force = 0;
		if(clusterForceOn)
			force += getClusterForce(degree1 + degree2);
		if(distanceForceOn)
			force += getDistanceForce(distance, length);
		if(proximityForceOn)
			force += getProximityForce(length);
		double angle1 = Math.atan2(p1.y - p2.y, p1.x - p2.x);
		double angle2 = Math.atan2(p2.y - p1.y, p2.x - p1.x);
		int dxCoord1 = (int)(force * Math.cos(angle1));
		int dyCoord1 = (int)(force * Math.sin(angle1));
		int dxCoord2 = (int)(force * Math.cos(angle2));
		int dyCoord2 = (int)(force * Math.sin(angle2));
		p1.x += dxCoord1;
		p1.y += dyCoord1;
		p2.x += dxCoord2;
		p2.y += dyCoord2;
	}

	private void floatCenter(int v) {
		Point p = graph.getPoint(v);
		Point c = new Point(GC.SCREEN_WIDTH / 2, GC.SCREEN_HEIGHT / 2);
		int length = p.distance(c);
		int degree = graph.getDegree(v);

		double force = getGravityForce(length, degree);
		double angle = Math.atan2(c.y - p.y, c.x - p.x);
		int dxCoord = (int)(force * Math.cos(angle));
		int dyCoord = (int)(force * Math.sin(angle));
		p.x += dxCoord;
		p.y += dyCoord;
	}
}
