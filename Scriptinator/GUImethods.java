
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import java.io.File;
import java.io.IOException;

import java.util.LinkedHashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * Methods that are used by different GUI compartments - those can be considered "general GUI methods"
 */
public class GUImethods extends StyleSheet {

	// simple dialog to ask whether the respective choice was correct
	public static Boolean BooleanDialog(String decisionText, String TitleText, Boolean skip) {
		if (skip) { // defined in StyleSheet as default behavior whether to skip and confirm the
					// dialog. This is meant for convenience according to user habits.
			return true;
		} else {
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null, decisionText, TitleText, dialogButton,
					JOptionPane.QUESTION_MESSAGE, new ImageIcon());
			return dialogResult == JOptionPane.YES_OPTION;
		}
	}

	// centers a frame to the screen
	public static JDialog centerFrame(JDialog frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		return frame;

	}

	// spawns file / folder selection dialog
	public static String FileSelectionDialog(String Title, String filename, String extention, Boolean OpenTrueSaveFalse,
			Boolean FileTrueFolderFalse) {
		FileNameExtensionFilter filter = null;

		// construct file extension filter if FileTrueFolderFalse=true
		if (FileTrueFolderFalse) {
			filter = new FileNameExtensionFilter(filename, extention);
		}
		// create and set up file chooser
		final JFileChooser fc = new JFileChooser();

		// set default directory when prompting file selection dialog to from where it
		// was called
		fc.setDialogTitle(Title);

		// differentiate between files and folders
		if (FileTrueFolderFalse) {
			fc.setSelectedFile(new File(System.getProperty("user.dir") + filesep + filename + "." + extention));
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setFileFilter(filter);
		} else {
			fc.setAcceptAllFileFilterUsed(false);
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		int returnVal;

		// differentiate between open and save chooser
		if (OpenTrueSaveFalse) {
			returnVal = fc.showOpenDialog(fc);
		} else {
			returnVal = fc.showSaveDialog(fc);
		}
		File file_ = null;

		// check if a file was selected
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file_ = fc.getSelectedFile();
		}
		return file_.getAbsolutePath();
	}

	// spawns file / folder selection dialog and returns a Script object from the
	// sourced file
	public static Script FileSelectionDialog2Script() {
		Script newScript = new Script();

		try {
			newScript.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newScript;
	}

	// converts a LinkedHashMap<String, String> to a JPanel containing the
	// respective information in form of editable text boxes
	public static JPanel HashMap2Panel(LinkedHashMap<String, String> HMIn, String title, int NumRows, int NumCols,
			Boolean split) {

		// define layout options
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.insets = new Insets(5, 2, 0, 2); // top padding

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setFont(StyleSheet.DefaultGUIFont);
		panel.setLayout(gridbag);

		// create values that are displayed. I.e. name and value separated by the
		// language specific variable declaration string
		String values = HelperMethods.HashMap2StringValuePairs(HMIn, "");
		String[] value_split = new String[] { values };

		// if the output string should be treated line wise
		if (split) {
			value_split = value_split[0].split(StyleSheet.eolDelimiter);
		} else {
			StringBuilder notsplit = new StringBuilder(value_split[0]);
			notsplit.deleteCharAt(0);
			values = notsplit.toString();
			value_split = new String[] { notsplit.toString() };
		}

		c.gridx = 1;
		panel.add(new JLabel(title), c);
		c.gridx = 0;
		for (int n = 0; n < HMIn.size(); n++) {
			c.gridy = n + 2;
			panel.add(makeEditableTextField(value_split[n], NumRows, NumCols, true, !split), c);
		}

		return panel;
	}

	// returns a new JButton actually its just slightly more convenient than
	// creating a JButton regularly.
	public static JButton makeButton(String ButtonString) {
		JButton button = new JButton(ButtonString);
		return button;
	}

	// creates an editable text field according to the given specifications
	public static JScrollPane makeEditableTextField(String textIn, int NumRows, int NumCols, Boolean HorScroller,
			Boolean VerScroller) {
		JTextArea text;

		// size of the text area
		text = new JTextArea(NumRows, NumCols);
		text.setFont(StyleSheet.DefaultCodeFont);
		text.append(textIn);

		// scrollers to eventually be added
		JScrollPane scroller = new JScrollPane(text);
		if (HorScroller) {
			scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		} else {
			scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		}
		if (VerScroller) {
			scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		} else {
			scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		}

		return scroller;
	}

	// returns a new JDialog with a DefaultCloseOperation defined
	public static JDialog makeFrame() {
		JDialog frame = new JDialog(new JFrame());
		frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		return frame;
	}

	// returns a JPanel[] from a set of HashMaps
	public static JPanel[] makeMultiPanelForProperties(String[] PanelsToAdd,
			LinkedHashMap<String, String>[] HashMapsToAdd) {
		int iscode;
		
		// how many mono spaced characters are displayed on the panel
		int NumRows = 15;
		int NumCols = 30;
		
		int NumRowsDyn = 1;
		JPanel[] PropertiesPanels = new JPanel[PanelsToAdd.length];
		for (int n = 0; n < PanelsToAdd.length; n++) {

			if (n > 0) {
				iscode = ((PanelsToAdd[n].matches(StyleSheet.IndStringCode)) ? 1 : 0);
				NumRowsDyn = 1 + (NumRows - 1) * iscode;
			}

			PropertiesPanels[n] = HashMap2Panel(HashMapsToAdd[n], PanelsToAdd[n], NumRowsDyn, NumCols,
					!PanelsToAdd[n].matches(StyleSheet.IndStringCode));
		}
		return PropertiesPanels;
	}

	// converts JPanel containing the respective information in form of editable
	// text boxes into a LinkedHashMap<String, String>
	public static LinkedHashMap<String, String> Panel2HashMap(JPanel panel) {
		Component[] components = panel.getComponents();
		String text_obtained = "";
		Boolean iscode = false;
		for (Component component : components) {

			// header of the section
			if (component.getClass().equals(JLabel.class)) {
				JLabel LabelPanel = (JLabel) component;
				if (LabelPanel.getText().matches(StyleSheet.IndStringCode)) {
					iscode = true;
				}
			}

			// content of a section component (e.g. one input variable from the section ")
			if (component.getClass().equals(JScrollPane.class)) {
				JScrollPane scroller = (JScrollPane) component;
				JViewport viewport = scroller.getViewport();
				Component[] componentsScroller = viewport.getComponents();
				for (Component componentScroller : componentsScroller) {

					// retrieve String information
					if (componentScroller.getClass().equals(JTextArea.class)) {
						JTextArea text_from_JTextArea = (JTextArea) componentScroller;
						text_obtained += text_from_JTextArea.getText() + StyleSheet.eol;
					}
				}
			}

		}
		return HelperMethods.StringValuePairs2HashMap(text_obtained, iscode);
	}

	// make round icon for use in a ScriptIcon
	public class RoundIcon implements Icon {

		private int size;
		private Paint color;
		private int red_size_by;

		public RoundIcon(int size, int red_size_byin, Paint color) {
			this.size = size;
			this.color = color;
			this.red_size_by = red_size_byin;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D) g;
			Paint op = g2d.getPaint();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setPaint(color);
			g2d.fillOval(x + red_size_by, y + red_size_by, size - 2 * red_size_by, size - 2 * red_size_by);
			g2d.setPaint(op);
		}

		@Override
		public int getIconWidth() {
			return size;
		}

		@Override
		public int getIconHeight() {
			return size;
		}

	}

}