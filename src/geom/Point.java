package geom;

public class Point {
	public int x;
	public int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point() {
		this.x = 0;
		this.y = 0;
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return this.x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int distance(Point other) {
		int dx = this.x - other.x;
		int dy = this.y - other.y;
		return (int)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}
	
	public int distance(int x, int y) {
		int dx = this.x - x;
		int dy = this.y - y;
		return (int)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
