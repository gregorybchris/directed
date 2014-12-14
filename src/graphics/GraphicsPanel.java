package graphics;

import geom.Graph;
import geom.Point;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JPanel;

import physics.Engine;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;

	private static boolean CREATE_RANDOM_GRAPH = false;
	
	private Engine engine;
	private Graph graph;

	private HashSet<Integer> keys;
	private boolean editMode = false;
	private String editCommand = "";

	public GraphicsPanel() {
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setFocusable(true);

		graph = new Graph();
		engine = new Engine(graph);

		keys = new HashSet<Integer>();
		
		if(CREATE_RANDOM_GRAPH)
			setRandomGraph();
	}

	public void refresh() {
		engine.update();
		repaint();
	}

	private void setRandomGraph() {
		Random r = new Random();
		int numVertices = 25;
		int numEdges = 15;
		
		for(int i = 0; i < numVertices; i++)
			graph.add(r.nextInt(1000), r.nextInt(500));
		for(int i = 0; i < numEdges; i++)
			graph.addEdge(r.nextInt(numVertices), r.nextInt(numVertices));
		for(int i = 0; i < numVertices; i++)
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

		for(int i = 0; i < graph.getSize(); i++) {
			if(graph.exists(i)) {
				Point p1 = graph.getPoint(i);
				for(int j = i + 1; j < graph.getSize(); j++) {
					if(graph.exists(j) && graph.isAdjacent(i, j)) {
						Point p2 = graph.getPoint(j);
						g2.setColor(GC.COLOR_EDGE);
						Line2D lineToDraw = new Line2D.Double(
								p1.x + GC.VERTEX_RADIUS, p1.y + GC.VERTEX_RADIUS, 
								p2.x + GC.VERTEX_RADIUS, p2.y + GC.VERTEX_RADIUS);
						g2.draw(lineToDraw);
					}
				}

				g2.setColor(GC.COLOR_VERTEX);
				Ellipse2D.Double vertexToDraw = new Ellipse2D.Double(
						p1.x, p1.y, GC.VERTEX_RADIUS * 2, GC.VERTEX_RADIUS * 2);
				g2.fill(vertexToDraw);
				g2.setColor(GC.COLOR_VERTEX.darker());
				g2.draw(vertexToDraw);
				g2.setColor(GC.COLOR_VERTEX_TEXT);
				g2.setFont(GC.FONT_VERTEX);
				int IDLength = (i + "").length();
				int dx = GC.VERTEX_RADIUS * 2 - 13;
				if(IDLength == 1)
					dx += 2;
				int dy = GC.VERTEX_RADIUS * 2 - 5;
				g2.drawString(i + "", p1.x + dx, p1.y + dy);			
			}
		}

		if(editMode) {
			g2.setColor(GC.COLOR_EDIT_BACKGROUND);
			g2.fillRect(440, 240, 120, 70);
			g2.setColor(GC.COLOR_EDIT_TEXT);
			g2.setFont(GC.FONT_EDIT);
			g2.drawString(editCommand, 460, 275);
		}
	}

	@Override
	public void mousePressed(MouseEvent me) {
		graph.add(me.getX(), me.getY());
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		keys.add(code);

		if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_Z)
			editMode = true;
		else if(code == KeyEvent.VK_X)
			graph.add(GC.SCREEN_WIDTH / 2, GC.SCREEN_HEIGHT / 2);
		else if(code == KeyEvent.VK_C) {
			editMode = false;
			editCommand = "";
		}
		else if(code == KeyEvent.VK_EQUALS) {
			graph = new Graph();
			engine.setGraph(graph);
			setRandomGraph();
		}
		else if(code == KeyEvent.VK_F)
			engine.toggleDistanceForce();

		if(keys.contains(KeyEvent.VK_ENTER) || keys.contains(KeyEvent.VK_Z)) {
			if(code == KeyEvent.VK_SHIFT)
				if(editCommand.length() > 0)
					editCommand = editCommand.substring(0, editCommand.length() - 1);
			if((code >= KeyEvent.VK_0 && code <= KeyEvent.VK_9) || code == KeyEvent.VK_SPACE) {
				if(editCommand.length() < 10)
					editCommand += ke.getKeyChar();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		int code = ke.getKeyCode();
		keys.remove(ke.getKeyCode());

		if(code == KeyEvent.VK_ENTER) {
			editMode = false;
			runCommand(true);
			editCommand = "";
		}
		else if(code == KeyEvent.VK_Z) {
			editMode = false;
			runCommand(false);
			editCommand = "";
		}
	}

	private boolean runCommand(boolean edge) {
		if(editCommand.length() == 0)
			return false;
		int spaceCount = 0;
		for(char c : editCommand.toCharArray())
			if(c == ' ')
				spaceCount++;

		if(spaceCount == 0 && edge == false) {
			int index = Integer.parseInt(editCommand);
			if(graph.exists(index)) {
				graph.remove(index);
				return true;
			}
		}
		else if(spaceCount == 1 && edge == true) {
			String[] parts = editCommand.split(" ");
			int v1 = Integer.parseInt(parts[0]);
			int v2 = Integer.parseInt(parts[1]);
			if(graph.exists(v1) && graph.exists(v2)) {
				if(graph.isAdjacent(v1, v2))
					graph.removeEdge(v1, v2);
				else
					graph.addEdge(v1, v2);
				return true;
			}
		}
		return false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
}