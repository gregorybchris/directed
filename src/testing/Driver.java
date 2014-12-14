package testing;

import geom.Graph;

public class Driver {
	public static void main(String[] args) {
		Graph graph = new Graph();
		for(int i = 0; i < 10; i++)
			graph.add();
		
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);
		graph.addEdge(3, 4);
		graph.addEdge(4, 5);
		graph.addEdge(5, 6);
		graph.addEdge(3, 6);
		graph.printAdjacencyMatrix();
		graph.printDistanceMatrix();
		System.out.println(graph.distance(1, 5));
		System.out.println(graph.distance(1, 1));
	}
}
