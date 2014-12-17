package graphics;

import geom.Graph;
import geom.Point;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JPanel;

import physics.Engine;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	private static final int RANDOM_NUM_VERTICES = 25;
	private static final int RANDOM_NUM_EDGES = 15;

	private Random r;
	private Engine engine;
	private Graph graph;

	private HashSet<Integer> keys;
	private Point mouseLocation;
	private boolean mouseDown = false;
	private int storeID = -1;

	public GraphicsPanel() {
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setFocusable(true);
		this.setCursor(GC.CURSOR_EDIT);

		r = new Random();
		graph = new Graph();
		engine = new Engine(graph);

		keys = new HashSet<Integer>();
		mouseLocation = new Point(-1, -1);
	}

	public void refresh() {
		engine.update();
		repaint();
	}

	private void resetGraph() {
		graph = new Graph();
		engine.setGraph(graph);
	}

	private void setRandomGraph() {
		resetGraph();

		for(int i = 0; i < RANDOM_NUM_VERTICES; i++)
			graph.add(r.nextInt(1000), r.nextInt(500));
		for(int i = 0; i < RANDOM_NUM_EDGES; i++)
			graph.addEdge(r.nextInt(RANDOM_NUM_VERTICES), r.nextInt(RANDOM_NUM_VERTICES));
		for(int i = 0; i < RANDOM_NUM_VERTICES; i++)
			if(graph.getDegree(i) == 0)
				graph.remove(i);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(GC.COLOR_BACKGROUND);
		g2.fillRect(0, 0, GC.SCREEN_WIDTH, GC.SCREEN_HEIGHT);
		g2.setStroke(GC.STROKE_MAIN);

		int finalID = -1;
		for(int i = 0; i < graph.getSize(); i++) {
			if(graph.exists(i)) {
				Point p = graph.getPoint(i);
				int distance = p.distance(mouseLocation.x, mouseLocation.y);
				if(distance < GC.VERTEX_RADIUS_CLICK)
					finalID = i;
			}
		}
		
		if(finalID != storeID && mouseDown && storeID != -1 && !keys.contains(KeyEvent.VK_D)) {
			Color edgeColor;
			if(finalID == -1)
				edgeColor = GC.COLOR_EDGE_TRY;
			else if(graph.isAdjacent(storeID, finalID))
				edgeColor = GC.COLOR_EDGE_REMOVE;
			else
				edgeColor = GC.COLOR_EDGE_ADD;
			g2.setColor(edgeColor);
			Point p = graph.getPoint(storeID);
			Line2D edgeToDraw = new Line2D.Double(p.x, p.y, mouseLocation.x, mouseLocation.y);
			g2.draw(edgeToDraw);
		}
		
		for(int i = 0; i < graph.getSize(); i++) {
			if(graph.exists(i)) {
				Point p1 = graph.getPoint(i);
				for(int j = i + 1; j < graph.getSize(); j++) {
					if(graph.exists(j) && graph.isAdjacent(i, j)) {
						Point p2 = graph.getPoint(j);
						g2.setColor(GC.COLOR_EDGE);
						Line2D edgeToDraw = new Line2D.Double(p1.x, p1.y, p2.x , p2.y);
						g2.draw(edgeToDraw);
					}
				}

				g2.setColor(GC.COLOR_VERTEX);
				Ellipse2D.Double vertexToDraw = new Ellipse2D.Double(
						p1.x - GC.VERTEX_RADIUS, p1.y - GC.VERTEX_RADIUS, 
						GC.VERTEX_RADIUS * 2, GC.VERTEX_RADIUS * 2);
				g2.fill(vertexToDraw);
				g2.setColor(GC.COLOR_VERTEX.darker());
				g2.draw(vertexToDraw);
//				g2.setColor(GC.COLOR_VERTEX_TEXT);
//				g2.setFont(GC.FONT_VERTEX);
//				int IDLength = (i + "").length();
//				int dx = GC.VERTEX_RADIUS * 2 - 13 - GC.VERTEX_RADIUS;
//				if(IDLength == 1)
//					dx += 2;
//				int dy = GC.VERTEX_RADIUS * 2 - 5 - GC.VERTEX_RADIUS;
//				g2.drawString(i + "", p1.x + dx, p1.y + dy);


			}
		}
	}

	/*
	 * F - Toggle forces
	 * G - Random graph
	 * R - Reset Screen
	 * 
	 * D - Delete node
	 */

	@Override
	public void mousePressed(MouseEvent me) {
		int x = me.getX();
		int y = me.getY();
		mouseDown = true;

		for(int i = 0; i < graph.getSize(); i++) {
			if(graph.exists(i)) {
				Point p = graph.getPoint(i);
				int distance = p.distance(x, y);
				if(distance < GC.VERTEX_RADIUS_CLICK)
					storeID = i;
			}
		}

		if(keys.contains(KeyEvent.VK_D) && storeID != -1) {
			graph.remove(storeID);
			storeID = -1;
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		int x = me.getX();
		int y = me.getY();
		mouseDown = false;

		if(storeID == -1 && !keys.contains(KeyEvent.VK_D))
			graph.add(me.getX(), me.getY());
		else {
			int finalID = -1;
			for(int i = 0; i < graph.getSize(); i++) {
				if(graph.exists(i)) {
					Point p = graph.getPoint(i);
					int distance = p.distance(x, y);
					if(distance < GC.VERTEX_RADIUS_CLICK)
						finalID = i;
				}
			}

			if(finalID != -1 && storeID != finalID) {
				if(graph.isAdjacent(storeID, finalID))
					graph.removeEdge(storeID, finalID);
				else
					graph.addEdge(storeID, finalID);
			}
		}

		storeID = -1;
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		mouseLocation.set(me.getX(), me.getY());
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		mouseLocation.set(me.getX(), me.getY());
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		keys.add(code);

		if(code == KeyEvent.VK_F)
			engine.toggleDistanceForce();
		else if(code == KeyEvent.VK_G)
			setRandomGraph();
		else if(code == KeyEvent.VK_R)
			resetGraph();
		else if(code == KeyEvent.VK_D)
			this.setCursor(GC.CURSOR_DELETE);
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		int code = ke.getKeyCode();
		keys.remove(code);

		if(code == KeyEvent.VK_D)
			this.setCursor(GC.CURSOR_EDIT);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
}