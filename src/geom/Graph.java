package geom;

import java.util.ArrayDeque;
import java.util.Queue;

public class Graph {
	private static final int INITIAL_CAPACITY = 10;
	private static final char EDGE_NONE = '_';
	private static final char EDGE_FALSE = '0';
	private static final char EDGE_TRUE = '1';

	private Point[] points;
	private char[][] adjacencyMatrix;
	private int[][] distances;
	private int capacity;
	private int size;
	private int order;

	public Graph() {
		capacity = INITIAL_CAPACITY;
		points = new Point[capacity];
		adjacencyMatrix = new char[capacity][capacity];
		distances = new int[capacity][capacity];
		for(int y = 0; y < capacity; y++) {
			for(int x = 0; x < capacity; x++) {
				adjacencyMatrix[x][y] = EDGE_NONE;
				distances[x][y] = -1;
			}
		}
		size = 0;
		order = 0;
	}

	public boolean isAdjacent(int v1, int v2) {
		if(!exists(v1) || !exists(v2))
			return false;
		return adjacencyMatrix[v1][v2] == EDGE_TRUE;
	}

	public Point getPoint(int v) {
		return points[v];
	}

	public int getDegree(int v) {
		if(!exists(v))
			throw new GraphException("Graph vertex does not exist: " + v);

		int toReturn = 0;
		for(int i = 0; i < size; i++) {
			if(i != v && adjacencyMatrix[v][i] == EDGE_TRUE)
				toReturn++;
		}
		return toReturn;
	}

	public boolean exists(int v) {
		return v < size && adjacencyMatrix[v][v] != EDGE_NONE;
	}

	public int add() {
		return add(0, 0);
	}

	public int add(int x, int y) {
		for(int i = 0; i < size + 1; i++) {
			adjacencyMatrix[size][i] = EDGE_FALSE;
			adjacencyMatrix[i][size] = EDGE_FALSE;
		}
		points[size] = new Point(x, y);

		order++;
		size++;

		if(size == capacity)
			expand();
		recalculateDistances();
		return size - 1;
	}

	public void addEdge(int v1, int v2) {
		if(!exists(v1))
			throw new GraphException("Graph vertex does not exist: " + v1);
		if(!exists(v2))
			throw new GraphException("Graph vertex does not exist: " + v2);

		adjacencyMatrix[v1][v2] = EDGE_TRUE;
		adjacencyMatrix[v2][v1] = EDGE_TRUE;
		recalculateDistances();
	}

	public void remove(int v) {
		if(!exists(v))
			throw new GraphException("Graph vertex does not exist: " + v);

		for(int i = 0; i < size; i++) {
			adjacencyMatrix[v][i] = EDGE_NONE;
			adjacencyMatrix[i][v] = EDGE_NONE;
		}
		order--;
		recalculateDistances();
	}

	public void removeEdge(int v1, int v2) {
		if(!exists(v1))
			throw new GraphException("Graph vertex does not exist: " + v1);
		if(!exists(v2))
			throw new GraphException("Graph vertex does not exist: " + v2);

		adjacencyMatrix[v1][v2] = EDGE_FALSE;
		adjacencyMatrix[v2][v1] = EDGE_FALSE;
		recalculateDistances();
	}

	public int getOrder() {
		return order;
	}

	public int getSize() {
		return size;
	}

	public int distance(int v1, int v2) {
		return distances[v1][v2];
	}

	private void recalculateDistances() {
		for(int v = 0; v < size; v++) {
			if(exists(v)) {
				int[] dv = new int[size];
				int[] pv = new int[size];
				for(int i = 0; i < size; i++) {
					dv[i] = -1;
					pv[i] = -1;
				}

				Queue<Integer> queue = new ArrayDeque<Integer>();
				queue.add(v);
				dv[v] = 0;
				while(!queue.isEmpty()) {
					int w = queue.poll();
					for(int i = 0; i < size; i++) {
						if(exists(i) && isAdjacent(w, i) && dv[i] == -1) {
							dv[i] = dv[w] + 1;
							pv[i] = w;
							queue.add(i);
						}
					}
				}
				for(int i = 0; i < size; i++) {
					if(exists(i)) {
						distances[v][i] = dv[i];
						distances[i][v] = dv[i];
					}
				}
			}
		}
	}

	private void expand() {
		Point[] newPoints = new Point[capacity * 2];
		char[][] newMatrix = new char[capacity * 2][capacity * 2];
		int[][] newDistances = new int[capacity * 2][capacity * 2];
		for(int y = 0; y < capacity * 2; y++) {
			for(int x = 0; x < capacity * 2; x++) {
				if(x < capacity && y < capacity) {
					newPoints[y] = points[y];
					newMatrix[x][y] = adjacencyMatrix[x][y];
					newDistances[x][y] = distances[x][y];
				}
				else {
					newMatrix[x][y] = EDGE_NONE;
					newDistances[x][y] = -1;
				}
			}
		}
		capacity *= 2;
		points = newPoints;
		adjacencyMatrix = newMatrix;
		distances = newDistances;
	}

	public void printAdjacencyMatrix() {
		for(int y = 0; y < capacity; y++) {
			for(int x = 0; x < capacity; x++)
				System.out.print(adjacencyMatrix[x][y] + " ");
			System.out.println();
		}
		System.out.println();
	}
	
	public void printDistanceMatrix() {
		for(int y = 0; y < capacity; y++) {
			for(int x = 0; x < capacity; x++) {
				if(distances[x][y] == -1)
					System.out.print("_ ");
				else
					System.out.print(distances[x][y] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}