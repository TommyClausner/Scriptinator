import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.io.Serializable;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ScriptIcon extends StyleSheet implements Serializable {

	protected ArrayList<JLabel> connections = new ArrayList<JLabel>();

	protected Boolean dragging = false;
	protected JLabel QsubIndicator;

	protected Boolean isselected = false;

	protected Script LocalScript;
	protected JPanel ScriptIconPanel = new JPanel();
	protected GUImethods GUIHelper = new GUImethods();
	protected LayoutManager overlay = new OverlayLayout(ScriptIconPanel);

	public void MakeQsubIndicatorRing(Boolean visible) {
		if (visible) {
			QsubIndicator = new JLabel(GUIHelper.new RoundIcon(GUIscriptIconSize, 1, GUIscriptIconQsubRingColor));
			ScriptIconPanel.add(QsubIndicator, BorderLayout.CENTER);
			ScriptIconPanel.add(
					new JLabel(GUIHelper.new RoundIcon(GUIscriptIconSize, 0, GUIscriptIconQsubRingFrameColor)),
					BorderLayout.CENTER);
		} else {
			QsubIndicator = new JLabel(GUIHelper.new RoundIcon(GUIscriptIconSize, 1, GUIbackGroundColor));
			ScriptIconPanel.add(QsubIndicator, BorderLayout.CENTER);
			ScriptIconPanel.add(
					new JLabel(GUIHelper.new RoundIcon(GUIscriptIconSize, 0, GUIscriptIconQsubRingFrameColor)),
					BorderLayout.CENTER);
		}
	}

	public void setSize(int newSize) {
		GUIscriptIconSize = newSize;
	}

	public int getSize() {
		return GUIscriptIconSize;
	}

	public void setCol(Color newColor) {
		LocalScript.ScriptObjectColor = newColor;
	}

	public Color getCol() {
		return LocalScript.ScriptObjectColor;
	}

	public void updateAppearance() {

		ScriptIconPanel.removeAll();
		makeGUIobject();
		ScriptIconPanel.setLocation(LocalScript.Location);
		ScriptIconPanel.revalidate();
		ScriptIconPanel.repaint();
		ScriptIconPanel.setVisible(true);
	}

	public ScriptIcon(Script ScriptIn) {
		LocalScript = ScriptIn;
		LocalScript.hasqsub = Boolean.parseBoolean(LocalScript.internal_map.get(InternalVarNameQsub));
		updateAppearance();
	}

	public void makeGUIobject() {

		ScriptIconPanel.setLayout(overlay);
		JLabel Circle = new JLabel(
				GUIHelper.new RoundIcon(GUIscriptIconSize, GUIscriptIconQsubRingSize, LocalScript.ScriptObjectColor));
		ScriptIconPanel.setBounds(0, 0, GUIscriptIconSize, GUIscriptIconSize);
		JLabel ThreeLetterDisp=new JLabel("\t\t"+LocalScript.internal_map.get(InternalVarNameShortDescription), SwingConstants.CENTER);
		ThreeLetterDisp.setOpaque(false);
		ThreeLetterDisp.setForeground(Color.WHITE);
		ThreeLetterDisp.setFont(new Font("Helvetica", Font.BOLD, 16));
		ScriptIconPanel.add(ThreeLetterDisp, BorderLayout.CENTER);
		ScriptIconPanel.add(Circle, BorderLayout.CENTER);
		MakeQsubIndicatorRing(LocalScript.hasqsub);

		ScriptIconPanel.addMouseListener(new ScriptObjectMouseListener());
		ScriptIconPanel.addMouseMotionListener(new ScriptObjectMouseMotionListener());

		ScriptIconPanel.setBackground(new Color(0, 0, 0, 0));
		ScriptIconPanel.revalidate();
		ScriptIconPanel.repaint();
	}

	protected class ScriptObjectMouseMotionListener implements MouseMotionListener {

		public void mouseDragged(MouseEvent event) {
			LocalScript.setPos(Playground.ConnectorsPanel.getMousePosition());
			Playground.reDrawWindow();
			dragging = true;
			Playground.selectedScripts.clear();
			event.consume();
		}

		@Override
		public void mouseMoved(MouseEvent event) {
		}
	}

	protected class ScriptObjectMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent event) {

			if (event.getButton() == MouseEvent.BUTTON1) {
			}

			if (event.getButton() == MouseEvent.BUTTON2) {
			}

			if (event.getButton() == MouseEvent.BUTTON3) {
			}

			if (event.getClickCount() == 2 && !event.isConsumed()) {

				ScriptPropertiesGUI Properties = new ScriptPropertiesGUI(LocalScript);
				Properties.updateWindow();

				LocalScript = Properties.LocalScript;
				LocalScript.hasqsub = Boolean.parseBoolean(LocalScript.internal_map.get(InternalVarNameQsub));
				updateAppearance();

				isselected = false;
				Playground.selectedScripts.clear();
				event.consume();

			} else if (event.getClickCount() == 1 && !event.isConsumed() & !dragging) {
				isselected = !isselected;
				if (isselected) {
					isselected = false;
					if (Playground.selectedScripts.size() >= 2) {
						Playground.selectedScripts.clear();
					}

					if (Playground.selectedScripts.size() == 1) {
						if (Playground.selectedScripts.get(0) != LocalScript) {
							Playground.selectedScripts.add(LocalScript);
							Playground.updateConnections();
						}
					} else {
						Playground.selectedScripts.add(LocalScript);
					}

				}
				event.consume();
			}
		}

		public void mouseEntered(MouseEvent event) {
		}

		public void mouseExited(MouseEvent event) {
		}

		public void mousePressed(MouseEvent event) {
		}

		public void mouseReleased(MouseEvent event) {

			if (!event.isConsumed()) {
				dragging = false;
				event.consume();
			}
		}

	}
}