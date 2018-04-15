
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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import java.io.IOException;

import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

// properties window for individual scripts
public class ScriptPropertiesGUI extends StyleSheet {

	// predefine used variables
	protected JPanel[] PropertiesSubPanels;
	protected JPanel PropertiesPanel = new JPanel();
	protected JDialog PropertiesDialog;
	protected Script LocalScript;
	protected Script LocalScriptOld;
	protected GridBagConstraints c = new GridBagConstraints();
	protected String[] PanelsToAdd = new String[] { IndStringInput, IndStringOutput, IndStringMisc, IndStringFileName,
			IndStringQsub, IndStringCode };
	protected Object[] HashMapsToAdd = new Object[PanelsToAdd.length];
	protected JPanel mainbuttonpanel = new JPanel();

	// constructor to open the GUI using the respective information obtained from
	// the input script
	public ScriptPropertiesGUI(Script ScriptIn) {
		c.gridx = 0;
		LocalScript = ScriptIn;
		LocalScriptOld = ScriptIn;

		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String>[] tmp = new LinkedHashMap[] { LocalScript.input_map, LocalScript.output_map,
				LocalScript.misc_map, LocalScript.internal_map, LocalScript.qsub_map, LocalScript.code_map };
		int i = 0;
		for (LinkedHashMap<String, String> currmap : tmp) {
			HashMapsToAdd[i] = (LinkedHashMap<String, String>) currmap;
			i++;
		}
	}

	// collect information from editable text boxes and update the respective script
	// object
	public void collectPanelInfo() {
		int i = 0;
		for (JPanel currpanel : PropertiesSubPanels) {

			switch (i) {
			case 0:
				LocalScript.input_map = GUImethods.Panel2HashMap(currpanel);
			case 1:
				LocalScript.output_map = GUImethods.Panel2HashMap(currpanel);
			case 2:
				LocalScript.misc_map = GUImethods.Panel2HashMap(currpanel);
			case 3:
				LocalScript.internal_map = GUImethods.Panel2HashMap(currpanel);
			case 4:
				LocalScript.qsub_map = GUImethods.Panel2HashMap(currpanel);
			case 5:
				LocalScript.code_map = GUImethods.Panel2HashMap(currpanel);
			}

			i++;
		}
		LocalScript.UpdateHeaderString();
	}

	// create the main dialog
	public void MakeMainFrame() {
		PropertiesDialog = GUImethods.makeFrame();
		PropertiesDialog.setModal(true);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - PropertiesDialog.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - PropertiesDialog.getHeight()) / 2);
		PropertiesDialog.setLocation(x, y);
	}

	// update the main dialog (i.e. redraw)
	public void UpdateMainFrame() {
		JScrollPane scroller = new JScrollPane(PropertiesPanel);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		PropertiesDialog.getContentPane().removeAll();
		PropertiesDialog.getContentPane().add(BorderLayout.CENTER, scroller);
		PropertiesDialog.pack();
		PropertiesDialog.revalidate();
		PropertiesDialog.toFront();
		PropertiesDialog.repaint();
		PropertiesDialog = GUImethods.centerFrame(PropertiesDialog);
		PropertiesDialog.setVisible(true);

	}

	// redefine values of editable text boxes as present in the script object
	@SuppressWarnings("unchecked")
	public void UpdateMainPanel() {
		PropertiesPanel = new JPanel();
		PropertiesPanel.setFont(DefaultGUIFont);
		PropertiesPanel.setLayout(new GridBagLayout());
		HashMapsToAdd = new LinkedHashMap[] { LocalScript.input_map, LocalScript.output_map, LocalScript.misc_map,
				LocalScript.internal_map, LocalScript.qsub_map, LocalScript.code_map };
		PropertiesSubPanels = GUImethods.makeMultiPanelForProperties(PanelsToAdd,
				(LinkedHashMap<String, String>[]) HashMapsToAdd);
		updateJPanelArray();
		PropertiesPanel.revalidate();
		PropertiesPanel.repaint();
	}

	// create all buttons on a panel
	public void MakeButtonPanel() {
		MakeGridBagConstraints();
		mainbuttonpanel.setFont(DefaultGUIFont);
		mainbuttonpanel.setLayout(new GridBagLayout());
		c.gridx = GUIscriptPropertiesPanelMaxNumCol;
		c.gridy = 0;

		mainbuttonpanel.add(new JLabel("Script Settings"), c);

		// will spawn a dialog to select a script in the defined language. If the script
		// already has a valid header, then this information will update the existing
		// panel information.
		c.gridy = 2;
		JButton loadbtn = GUImethods.makeButton("Load Script");
		loadbtn.putClientProperty("JComponent.sizeVariant", "regular");
		loadbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					LocalScript.load();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				MakeGridBagConstraints();
				UpdateMainPanel();
				UpdateMainFrame();
			}
		});
		mainbuttonpanel.add(loadbtn, c);

		// closes the Properties window and sets the parameters in the defined way.
		c.gridy++;
		JButton applybtn = GUImethods.makeButton("Confirm and Close");
		applybtn.putClientProperty("JComponent.sizeVariant", "regular");
		applybtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				collectPanelInfo();
				PropertiesDialog.dispatchEvent(new WindowEvent(PropertiesDialog, WindowEvent.WINDOW_CLOSING));
			}
		});
		mainbuttonpanel.add(applybtn, c);

		// creates a runnable script in the respective language, including the header
		// information wrapped in language specific comments.
		c.gridy++;
		JButton overwritebtn = GUImethods.makeButton("Write to file");
		overwritebtn.putClientProperty("JComponent.sizeVariant", "regular");
		overwritebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				collectPanelInfo();
				try {
					LocalScript.save();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		mainbuttonpanel.add(overwritebtn, c);

		// closes the Properties window and reverts the state to it’s original state
		c.gridy++;
		JButton cancelbtn = GUImethods.makeButton("Cancel");
		cancelbtn.putClientProperty("JComponent.sizeVariant", "regular");
		cancelbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalScript = LocalScriptOld;
				PropertiesDialog.dispatchEvent(new WindowEvent(PropertiesDialog, WindowEvent.WINDOW_CLOSING));
			}
		});
		mainbuttonpanel.add(cancelbtn, c);

		// closes the Properties window and deletes this Script from pipeline
		c.gridy++;
		JButton deletebtn = GUImethods.makeButton("Delete Script");
		deletebtn.putClientProperty("JComponent.sizeVariant", "regular");
		deletebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (GUImethods.BooleanDialog("Do you really want to delete this script?", "Delete Script",
						skipDeleteScriptDialog)) {
					LocalScript = LocalScriptOld;
					LocalScript.isdeleted = true;
					PropertiesDialog.dispatchEvent(new WindowEvent(PropertiesDialog, WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		mainbuttonpanel.add(deletebtn, c);

		// Buttons for modifying number of variables for "Input", "Output" and "Misc"
		c.gridy = 1;
		c.gridx = 0;
		mainbuttonpanel.add(new JLabel("Input"), c);
		c.gridy++;
		JButton addInbtn = GUImethods.makeButton("+");
		addInbtn.putClientProperty("JComponent.sizeVariant", "regular");
		addInbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalScript.makeInputVar();
				MakeGridBagConstraints();
				UpdateMainPanel();
				UpdateMainFrame();
			}
		});
		mainbuttonpanel.add(addInbtn, c);

		c.gridy++;
		JButton subInbtn = GUImethods.makeButton("-");
		subInbtn.putClientProperty("JComponent.sizeVariant", "regular");
		subInbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalScript.removeInputVar();
				MakeGridBagConstraints();
				UpdateMainPanel();
				UpdateMainFrame();
			}
		});
		mainbuttonpanel.add(subInbtn, c);

		c.gridx = 0;
		c.gridy++;
		mainbuttonpanel.add(new JLabel("Output"), c);
		c.gridy++;
		JButton addOutbtn = GUImethods.makeButton("+");
		addOutbtn.putClientProperty("JComponent.sizeVariant", "regular");
		addOutbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalScript.makeOutputVar();
				MakeGridBagConstraints();
				UpdateMainPanel();
				UpdateMainFrame();
			}
		});
		mainbuttonpanel.add(addOutbtn, c);

		c.gridy++;
		JButton subOutbtn = GUImethods.makeButton("-");
		subOutbtn.putClientProperty("JComponent.sizeVariant", "regular");
		subOutbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalScript.removeOutputVar();
				MakeGridBagConstraints();
				UpdateMainPanel();
				UpdateMainFrame();
			}
		});
		mainbuttonpanel.add(subOutbtn, c);

		c.gridx = 0;
		c.gridy++;
		mainbuttonpanel.add(new JLabel("Misc"), c);
		c.gridy++;
		JButton addMiscbtn = GUImethods.makeButton("+");
		addMiscbtn.putClientProperty("JComponent.sizeVariant", "regular");
		addMiscbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalScript.makeMiscVar();
				MakeGridBagConstraints();
				UpdateMainPanel();
				UpdateMainFrame();
			}
		});
		mainbuttonpanel.add(addMiscbtn, c);

		c.gridy++;
		JButton subMiscbtn = GUImethods.makeButton("-");
		subMiscbtn.putClientProperty("JComponent.sizeVariant", "regular");
		subMiscbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalScript.removeMiscVar();
				MakeGridBagConstraints();
				UpdateMainPanel();
				UpdateMainFrame();
			}
		});
		mainbuttonpanel.add(subMiscbtn, c);
	}

	// reset GridBagConstraints
	public void MakeGridBagConstraints() {
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 0;
		c.weightx = 0.1;
		c.gridwidth = 1;
		c.weighty = 0.1;
		c.gridx = 0;
	}

	// update all graphical components
	public void updateWindow() {
		MakeGridBagConstraints();
		MakeMainFrame();
		PropertiesDialog.setTitle("Properties of " + LocalScript.internal_map.get(InternalVarNameLabel));
		MakeButtonPanel();
		UpdateMainPanel();
		UpdateMainFrame();
	}

	// update section arrays
	public void updateJPanelArray() {
		int iscode;
		int n = 0;
		MakeGridBagConstraints();

		for (JPanel subpanel : PropertiesSubPanels) {
			if (n > 0) {
				iscode = ((PanelsToAdd[n].matches(IndStringCode)) ? 1 : 0);
				c.gridy = (int) Math.floor(n / GUIscriptPropertiesPanelMaxNumCol) + iscode;
				c.gridwidth = (GUIscriptPropertiesPanelMaxNumCol - 2) * iscode + 1;

				iscode = ((PanelsToAdd[n].matches(IndStringCode)) ? 0 : 1);
				c.gridx = (n - GUIscriptPropertiesPanelMaxNumCol * c.gridy) * iscode;

			}
			PropertiesPanel.add(subpanel, c);
			n++;
		}

		c.gridx = GUIscriptPropertiesPanelMaxNumCol - 1;
		c.gridy = 2;
		PropertiesPanel.add(mainbuttonpanel, c);
	}

}