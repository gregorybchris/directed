package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

public class GC {
	public static final int SCREEN_WIDTH = 1000;
	public static final int SCREEN_HEIGHT = 650;
	
	public static final Color COLOR_BACKGROUND = new Color(30, 30, 30);
	public static final Color COLOR_VERTEX = new Color(60, 60, 210);
	public static final Color COLOR_EDGE = new Color(100, 100, 230);
	public static final Color COLOR_VERTEX_TEXT = new Color(250, 250, 250);
	
	public static final Color COLOR_EDIT_BACKGROUND = new Color(.98f, .98f, .98f, .55f);
	public static final Color COLOR_EDIT_TEXT = new Color(0, 0, 0);
	
	public static final int VERTEX_RADIUS = 8;
	public static final int EDGE_WIDTH = 4;
	
	public static final BasicStroke STROKE_MAIN = new BasicStroke(GC.EDGE_WIDTH);
	
	public static final Font FONT_VERTEX = new Font("Tahoma", Font.BOLD, 9);
	public static final Font FONT_EDIT = new Font("Tahoma", Font.BOLD, 13);
}
