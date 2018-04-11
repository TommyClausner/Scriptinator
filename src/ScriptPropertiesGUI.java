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

public class ScriptPropertiesGUI extends StyleSheet {
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

	public ScriptPropertiesGUI(Script ScriptIn) {
		c.gridx = 0;
		LocalScript = ScriptIn;
		LocalScriptOld=ScriptIn;

		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String>[] tmp = new LinkedHashMap[] { LocalScript.input_map, LocalScript.output_map,
				LocalScript.misc_map, LocalScript.internal_map, LocalScript.qsub_map, LocalScript.code_map };
		int i = 0;
		for (LinkedHashMap<String, String> currmap : tmp) {
			HashMapsToAdd[i] = (LinkedHashMap<String, String>) currmap;
			i++;
		}
	}

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

	public void MakeMainFrame() {
		PropertiesDialog = GUImethods.makeFrame();
		PropertiesDialog.setModal(true);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - PropertiesDialog.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - PropertiesDialog.getHeight()) / 2);
		PropertiesDialog.setLocation(x, y);
	}

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

	public void MakeButtonPanel() {
		MakeGridBagConstraints();
		mainbuttonpanel.setFont(DefaultGUIFont);
		mainbuttonpanel.setLayout(new GridBagLayout());
		c.gridx = GUIscriptPropertiesPanelMaxNumCol;
		c.gridy = 0;

		mainbuttonpanel.add(new JLabel("Script Settings"), c);

		c.gridy = 2;

		JButton loadbtn = GUImethods.makeButton("Load Script");
		loadbtn.putClientProperty("JComponent.sizeVariant", "regular");
		loadbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					LocalScript.load();
				} catch (IOException | ScriptinatorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				MakeGridBagConstraints();
				UpdateMainPanel();
				UpdateMainFrame();
			}
		});
		mainbuttonpanel.add(loadbtn, c);

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

		c.gridy++;
		JButton runbtn = GUImethods.makeButton("Run Script");
		runbtn.putClientProperty("JComponent.sizeVariant", "regular");
		runbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					collectPanelInfo();
					System.out.println(LocalScript.runScript());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		mainbuttonpanel.add(runbtn, c);

		c.gridy++;
		JButton cancelbtn = GUImethods.makeButton("Cancel");
		cancelbtn.putClientProperty("JComponent.sizeVariant", "regular");
		cancelbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalScript=LocalScriptOld;
				PropertiesDialog.dispatchEvent(new WindowEvent(PropertiesDialog, WindowEvent.WINDOW_CLOSING));
			}
		});
		mainbuttonpanel.add(cancelbtn, c);

		// Input Mod Buttons
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

		// Output Mod Buttons
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

		// Misc Mod Buttons
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

	public void MakeGridBagConstraints() {
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 0;
		c.weightx = 0.1;
		c.gridwidth = 1;
		c.weighty = 0.1;
		c.gridx = 0;
	}

	public void updateWindow() {
		MakeGridBagConstraints();
		MakeMainFrame();
		PropertiesDialog.setTitle("Properties of " + LocalScript.internal_map.get(InternalVarNameLabel));
		MakeButtonPanel();
		UpdateMainPanel();
		UpdateMainFrame();
	}

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