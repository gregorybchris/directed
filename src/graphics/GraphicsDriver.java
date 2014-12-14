package graphics;

import javax.swing.JFrame;

public class GraphicsDriver {
	public static void main(String[] args) {
		GraphicsFrame f = new GraphicsFrame("Force Directed Graphs - Chris Gregory");
		f.setBounds(0, 0, GC.SCREEN_WIDTH, GC.SCREEN_HEIGHT);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
