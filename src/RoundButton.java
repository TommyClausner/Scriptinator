import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;

/*
 * Thanks to: https://www.javacodex.com/More-Examples/2/14
 */
public class RoundButton extends JButton {

	private static final long serialVersionUID = 7500430102759570147L;

	public RoundButton(String label) {
		super(label);

		setBackground(Color.lightGray);
		setFocusable(false);

		/*
		 * These statements enlarge the button so that it becomes a circle rather than
		 * an oval.
		 */
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width, size.height);
		setPreferredSize(size);

		/*
		 * This call causes the JButton not to paint the background. This allows us to
		 * paint a round background.
		 */
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusPainted(false);
		setMargin(new Insets(0, 0, 0, 0));
		setBorder(null);
	}

	protected void paintComponent(Graphics g) {
		if (getModel().isArmed()) {
			g.setColor(Color.white);
		} else {
			g.setColor(getBackground());
		}
		g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

		super.paintComponent(g);
	}

	protected void paintBorder(Graphics g) {
		g.setColor(Color.white);
		g.drawOval(0, 0, getSize().width-1, getSize().height - 1);
	}

	// Hit detection.
	Shape shape;

	public boolean contains(int x, int y) {
		// If the button has changed size, make a new shape object.
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		return shape.contains(x, y);
	}
}