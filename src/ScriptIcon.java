
/*  _______  _______  __   __  __   __  __   __  __   _______                                              
 * |       ||       ||  |_|  ||  |_|  ||  | |  ||  | |       |                                             
 * |_     _||   _   ||       ||       ||  |_|  ||__| |  _____|                                             
 *   |   |  |  | |  ||       ||       ||       |     | |_____                                              
 *   |   |  |  |_|  ||       ||       ||_     _|     |_____  |                                             
 *   |   |  |       || ||_|| || ||_|| |  |   |        _____| |                                             
 *   |___|  |_______||_|   |_||_|   |_|  |___|       |_______|                                             
 *  _______  _______  ______    ___   _______  _______  ___   __    _  _______  _______  _______  ______   
 * |       ||       ||    _ |  |   | |       ||       ||   | |  |  | ||   _   ||       ||       ||    _ |  
 * |  _____||       ||   | ||  |   | |    _  ||_     _||   | |   |_| ||  |_|  ||   _   ||_     _||   | ||  
 * | |_____ |       ||   |_||_ |   | |   |_| |  |   |  |   | |       ||       ||  | |  |  |   |  |   |_||_ 
 * |_____  ||      _||    __  ||   | |    ___|  |   |  |   | |  _    ||       ||  |_|  |  |   |  |    __  |
 *  _____| ||     |_ |   |  | ||   | |   |      |   |  |   | | | |   ||   _   ||       |  |   |  |   |  | |
 * |_______||_______||___|  |_||___| |___|      |___|  |___| |_|  |__||__| |__||_______|  |___|  |___|  |_|
 *  _______  _______  _______  _______    _______  __   __                                                 
 * |       ||  _    ||  _    ||  _    |  |       ||  |_|  |                                                
 * |___    || | |   || | |   || | |   |  |_     _||       |                                                
 *  ___|   || | |   || | |   || | |   |    |   |  |       |                                                
 * |___    || |_|   || |_|   || |_|   |    |   |  |       |                                                
 *  ___|   ||       ||       ||       |    |   |  | ||_|| |                                                
 * |_______||_______||_______||_______|    |___|  |_|   |_|                                                
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;

/*
 * graphical counterpart to a script object. Will be represented as a filled circle on the screen 
 */
public class ScriptIcon extends StyleSheet {

	// initialize variables
	protected ArrayList<JLabel> connections = new ArrayList<JLabel>();

	protected Boolean dragging = false;
	protected JLabel QsubIndicator;

	protected Boolean isselected = false;

	protected Script LocalScript;
	protected JPanel ScriptIconPanel = new JPanel();
	protected GUImethods GUIHelper = new GUImethods();
	protected LayoutManager overlay = new OverlayLayout(ScriptIconPanel);

	// make script icon from script object
	public ScriptIcon(Script ScriptIn) {
		LocalScript = ScriptIn;
		LocalScript.hasqsub = Boolean.parseBoolean(LocalScript.internal_map.get(InternalVarNameQsub));
		updateAppearance();
	}

	// change appearance of outer ring according to whether useqsub is true or false
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

	// update appearance of icon (i.e. redraw)
	public void updateAppearance() {
		ScriptIconPanel.removeAll();
		makeGUIobject();
		ScriptIconPanel.setLocation(LocalScript.Location);
		ScriptIconPanel.revalidate();
		ScriptIconPanel.repaint();
		ScriptIconPanel.setVisible(true);
	}

	// initialize panel
	public void makeGUIobject() {

		ScriptIconPanel.setLayout(overlay);
		JLabel Circle = new JLabel(
				GUIHelper.new RoundIcon(GUIscriptIconSize, GUIscriptIconQsubRingSize, LocalScript.ScriptObjectColor));
		ScriptIconPanel.setBounds(0, 0, GUIscriptIconSize, GUIscriptIconSize);
		JLabel ThreeLetterDisp = new JLabel("\t\t" + LocalScript.internal_map.get(InternalVarNameShortDescription),
				SwingConstants.CENTER);
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

	// make mouse motion listener for script object
	protected class ScriptObjectMouseMotionListener implements MouseMotionListener {

		// if dragging, update icon location
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

	// make mouse listener for script object
	protected class ScriptObjectMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent event) {

			if (event.getButton() == MouseEvent.BUTTON1) {
			}

			if (event.getButton() == MouseEvent.BUTTON2) {
			}

			if (event.getButton() == MouseEvent.BUTTON3) {
			}

			// if double click spawn properties window
			if (event.getClickCount() == 2 && !event.isConsumed()) {

				ScriptPropertiesGUI Properties = new ScriptPropertiesGUI(LocalScript);
				Properties.updateWindow();

				LocalScript = Properties.LocalScript;
				LocalScript.hasqsub = Boolean.parseBoolean(LocalScript.internal_map.get(InternalVarNameQsub));
				updateAppearance();

				isselected = false;
				Playground.selectedScripts.clear();
				event.consume();
				// if single click, select item for being connected
			} else if (event.getClickCount() == 1 && !event.isConsumed() & !dragging) {

				isselected = !isselected;
				if (isselected) {
					isselected = false;
					
					// just to make sure not more than 2 scripts can be selected
					if (Playground.selectedScripts.size() >= 2) {
						Playground.selectedScripts.clear();
					}

					// if one script was already selected, draw / remove line
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

		// set dragging to false if mouse is not clicked
		public void mouseReleased(MouseEvent event) {
			if (!event.isConsumed()) {
				dragging = false;
				event.consume();
			}
		}

	}
}