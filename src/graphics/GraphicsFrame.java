package graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;


public class GraphicsFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private GraphicsPanel panel;
	private final Timer t;
	private static final int DELAY = 40;

	public GraphicsFrame(String title) {
		super(title);
		panel = new GraphicsPanel();
		this.getContentPane().add(panel);
		
		t = new Timer(DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.refresh();
			}
		});
		t.start();
	}
}