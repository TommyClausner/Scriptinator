
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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.OverlayLayout;

/*
 * Main part of the program: provides main GUI panel and creates pipeline
 */
public class Playground extends StyleSheet {

	// predefine all operating variables
	protected static JDialog ConnectorsDialog = GUImethods.makeFrame();
	protected static GUImethods GUIHelper = new GUImethods();
	protected static JPanel ConnectorsPanel = new JPanel();
	protected static JPanel ControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
	protected static JPanel mainPanel;
	protected static GridBagConstraints c = new GridBagConstraints();

	protected static ArrayList<Script> selectedScripts = new ArrayList<Script>();

	protected static ArrayList<JPanel> selectedScriptIcons = new ArrayList<JPanel>();
	protected static LinkedHashMap<Script, Integer> ScriptHierarchy = new LinkedHashMap<Script, Integer>();
	protected static LinkedHashMap<Integer, Script> ScriptsAdded = new LinkedHashMap<Integer, Script>();
	protected static LinkedHashMap<Script, Integer> IndicesScriptsAdded = new LinkedHashMap<Script, Integer>();

	protected static String SavePath = "";

	// constructor: initiates main frame and panel
	public Playground() {
		mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		MakeMainDialog();
		reDrawWindow();
	}

	// update all graphics
	public static void reDrawWindow() {
		UpdateMainPanel();
		UpdateMainDialog();
		ConnectorsDialog.setVisible(true);
	}

	// creates main Dialog adding a listener to properly exit the program on window
	// disposal
	public void MakeMainDialog() {
		ConnectorsDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});

		ConnectorsDialog.setTitle("Tommy's Scriptinator 3000 TM");
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		ConnectorsDialog.setSize((int) (dimension.getWidth() * MainWindowOccupiesThisMuchOfMyScreen),
				(int) (dimension.getHeight() * MainWindowOccupiesThisMuchOfMyScreen));
		ConnectorsDialog = GUImethods.centerFrame(ConnectorsDialog);
	}

	// reinitializes main dialog
	public static void UpdateMainDialog() {
		ConnectorsDialog.getContentPane().removeAll();
		ConnectorsDialog.getContentPane().add(BorderLayout.CENTER, mainPanel);
		ConnectorsDialog.revalidate();
		ConnectorsDialog.repaint();

	}

	// prepare components for being redrawn
	public static void UpdateMainPanel() {
		ConnectorsPanel.removeAll();
		ConnectorsPanel.setLayout(null);

		// set up size corresponding to predefinitions
		Rectangle ControlPanelDimension = ConnectorsDialog.getBounds();
		ControlPanelDimension.setBounds(0, 0, ControlPanelDimension.width,
				ControlPanelDimension.height - MainWindowButtonSize);

		ConnectorsPanel.setBounds(ControlPanelDimension);
		ConnectorsPanel.setPreferredSize(new Dimension(ControlPanelDimension.width, ControlPanelDimension.height));
		ConnectorsPanel.setBackground(StyleSheet.GUIbackGroundColor);
		ConnectorsPanel.addMouseListener(new ConnectorsPanelMouseListener());
		Script thisScript;

		// make new script icons
		for (int n = 0; n < ScriptsAdded.size(); n++) {
			thisScript = ScriptsAdded.get(n);
			selectedScriptIcons.get(n).setLocation(thisScript.Location);
			ConnectorsPanel.add(selectedScriptIcons.get(n));
		}

		// make new connection lines
		for (int n = 0; n < ScriptsAdded.size(); n++) {
			thisScript = ScriptsAdded.get(n);
			if (thisScript.ConnectionFrom != null) {
				for (int connection : thisScript.ConnectionFrom) {

					ArrayList<Point> points = updateConnectionLine(thisScript.getPos(),
							ScriptsAdded.get(connection).getPos(), GUIConnectionIndicatorSize);
					int i = 0;
					double mod = 1.5;
					for (Point pointcoords : points) {
						ConnectorsPanel.add(makeConnectionIndicatorDot(pointcoords,
								(int) (mod * GUIConnectionIndicatorSize * i / points.size())));
						i++;
					}
				}
			}
		}

		// playground panel
		ConnectorsPanel.revalidate();
		ConnectorsPanel.repaint();
		ConnectorsPanel.setLocation(0, MainWindowButtonSize);

		mainPanel.removeAll();
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		// create main buttons and add them
		ControlPanel = mainControlGUI();
		mainPanel.add(ControlPanel);
		mainPanel.add(ConnectorsPanel);

		// refresh main panel
		mainPanel.revalidate();
		mainPanel.repaint();
		mainPanel.setVisible(true);

	}

	// create Mouse Listener for main Panel
	protected static class ConnectorsPanelMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent event) {

			if (event.getButton() == MouseEvent.BUTTON1) {
			}

			if (event.getButton() == MouseEvent.BUTTON3) {

			}

			// double click
			if (event.getClickCount() == 2 && !event.isConsumed()) {
				Point currLocation = event.getPoint();
				Script newScript = new Script();
				newScript.setPos(currLocation);
				ScriptsAdded.put(ScriptsAdded.size(), newScript);
				IndicesScriptsAdded.put(newScript, ScriptsAdded.size() - 1);
				selectedScriptIcons.add(new ScriptIcon(newScript).ScriptIconPanel);
				reDrawWindow();
				event.consume();
			} else {
			}
		}

		public void mouseEntered(MouseEvent event) {
		}

		public void mouseExited(MouseEvent event) {
		}

		public void mousePressed(MouseEvent event) {
		}

		public void mouseReleased(MouseEvent event) {
		}
	}

	// button panel at top of main frame
	public static JPanel mainControlGUI() {
		ControlPanel.removeAll();
		ControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, MainWindowButtonSize / 3, 0));
		ControlPanel.setFont(DefaultGUIFont);

		JButton clearbtn = new RoundButton("/");
		clearbtn.setFont(GUIScriptIconFont);
		clearbtn.setForeground(Color.white);
		clearbtn.setPreferredSize(new Dimension(MainWindowButtonSize, MainWindowButtonSize));
		clearbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (GUImethods.BooleanDialog("Do you really want to clear the pipeline?", "Clear Pipeline")) {
					clearPipeline();
					reDrawWindow();
				}
			}
		});
		ControlPanel.add(clearbtn);

		JButton savebtn = new RoundButton("S");
		savebtn.setFont(GUIScriptIconFont);
		savebtn.setForeground(Color.white);
		savebtn.setPreferredSize(new Dimension(MainWindowButtonSize, MainWindowButtonSize));
		savebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SavePath = GUImethods.FileSelectionDialog("Save Pipeline...", "Pipeline", InternalPipeExtension,
							false, true);
					save();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		ControlPanel.add(savebtn);

		JButton loadbtn = new RoundButton("L");
		loadbtn.setFont(GUIScriptIconFont);
		loadbtn.setForeground(Color.white);
		loadbtn.setPreferredSize(new Dimension(MainWindowButtonSize, MainWindowButtonSize));
		loadbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					clearPipeline();
					load();
					reDrawWindow();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		ControlPanel.add(loadbtn);

		JButton pipebtn = new RoundButton("P");
		pipebtn.setFont(GUIScriptIconFont);
		pipebtn.setForeground(Color.white);
		pipebtn.setPreferredSize(new Dimension(MainWindowButtonSize, MainWindowButtonSize));
		pipebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getHierarchy();
				try {
					makePipeline();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		ControlPanel.add(pipebtn);

		JButton aboutbtn = new RoundButton("!");
		aboutbtn.setFont(GUIScriptIconFont);
		aboutbtn.setForeground(Color.white);
		aboutbtn.setPreferredSize(new Dimension(MainWindowButtonSize, MainWindowButtonSize));
		aboutbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAboutInformation();
			}
		});
		ControlPanel.add(aboutbtn);

		JButton helpbtn = new RoundButton("?");
		helpbtn.setFont(GUIScriptIconFont);
		helpbtn.setForeground(Color.white);
		helpbtn.setPreferredSize(new Dimension(MainWindowButtonSize, MainWindowButtonSize));
		helpbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(new File("README.pdf"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		ControlPanel.add(helpbtn);

		JButton exitbtn = new RoundButton("X");
		exitbtn.setFont(GUIScriptIconFont);
		exitbtn.setForeground(Color.white);
		exitbtn.setPreferredSize(new Dimension(MainWindowButtonSize, MainWindowButtonSize));
		exitbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		ControlPanel.add(exitbtn);
		ControlPanel.setBackground(GUIbackGroundColor);
		ControlPanel.setPreferredSize(new Dimension(ConnectorsPanel.getWidth(), MainWindowButtonSize));
		ControlPanel.revalidate();
		ControlPanel.repaint();

		return ControlPanel;
	}

	// spawn "About" information window
	public static void showAboutInformation() {

		File file = null;

		file = new File(System.getProperty("user.dir") + filesep + "About.txt");

		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			String HelpInfoText = new String(data, "UTF-8");

			JTextArea AboutInfo = new JTextArea(HelpInfoText);
			AboutInfo.setHighlighter(null);
			AboutInfo.setEditable(false);
			JDialog AboutFrame = GUImethods.makeFrame();

			AboutFrame.add(AboutInfo);
			AboutFrame.pack();
			AboutFrame.revalidate();
			AboutFrame.repaint();
			AboutFrame = GUImethods.centerFrame(AboutFrame);
			AboutFrame.setVisible(true);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// make dot for connection line
	public static JPanel makeConnectionIndicatorDot(Point p, int size) {
		JPanel Connection = new JPanel();
		LayoutManager overlayConnections = new OverlayLayout(Connection);
		Connection.setLayout(overlayConnections);

		JLabel innerCircle = new JLabel(GUIHelper.new RoundIcon(size, 1, Color.WHITE));
		JLabel outerCircle = new JLabel(GUIHelper.new RoundIcon(size, 0, Color.BLACK));

		Connection.setBounds(0, 0, size, size);
		Connection.add(innerCircle);
		Connection.add(outerCircle, BorderLayout.CENTER);
		Connection.setBackground(new Color(0, 0, 0, 0));
		Connection.setLocation(p);
		Connection.revalidate();
		Connection.repaint();
		return Connection;
	}

	// refresh connection line points (e.g. when dragging an icon)
	public static ArrayList<Point> updateConnectionLine(Point p1, Point p2, int space) {

		ArrayList<Point> pointlist = new ArrayList<Point>();

		float XDiff = Math.abs(p1.x - p2.x);
		float YDiff = Math.abs(p1.y - p2.y);

		// infer number of points such that points fill the line without overlap at
		// their maximum size
		int NumXPoints = (int) (Math.sqrt(Math.pow(XDiff, 2) + Math.pow(YDiff, 2)) / (float) space);
		int NumYPoints = (int) (Math.sqrt(Math.pow(XDiff, 2) + Math.pow(YDiff, 2)) / (float) space);

		if (NumYPoints < 1) {
			NumYPoints = NumXPoints;
		} else if (NumXPoints < 1) {
			NumXPoints = NumYPoints;
		} else if (NumYPoints < 1 & NumXPoints < 1) {
			NumXPoints = 1;
			NumYPoints = 1;
		}

		float[] YPoints = HelperMethods.linspace(p1.getY(), p2.getY(), NumYPoints);
		float[] XPoints = HelperMethods.linspace(p1.getX(), p2.getX(), NumXPoints);

		for (int i = 0; i < YPoints.length; i++) {
			Point pointcoords = new Point((int) XPoints[i], (int) YPoints[i]);
			pointlist.add(pointcoords);
		}
		return pointlist;
	}

	// refresh connection line (e.g. when dragging an icon)
	public static void updateConnections() {
		if (selectedScripts.size() == 2) {

			int IndScript0 = IndicesScriptsAdded.get(selectedScripts.get(0));
			int IndScript1 = IndicesScriptsAdded.get(selectedScripts.get(1));

			Script tmp1 = selectedScripts.get(1);
			Boolean add = true;
			int i = 0;
			for (int tmpConn : tmp1.ConnectionFrom) {
				if (tmpConn == IndScript0) {
					add = false;
					break;
				}
				i++;
			}
			if (add) {
				tmp1.ConnectionFrom.add(IndScript0);
			} else {
				tmp1.ConnectionFrom.remove(i);
			}

			ScriptsAdded.put(IndScript1, tmp1);
			reDrawWindow();
		}
	}

	// de-serialize pipeline
	public static void load() throws ClassNotFoundException, IOException {
		SavePath = GUImethods.FileSelectionDialog("Select Pipeline Folder", "Pipeline", InternalPipeExtension, true,
				true);

		FileInputStream fileIn = new FileInputStream(SavePath);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		Script[] Oin = (Script[]) in.readObject();

		selectedScriptIcons.clear();
		ScriptsAdded.clear();
		IndicesScriptsAdded.clear();
		in.close();
		fileIn.close();

		for (Script thisScript : Oin) {
			ScriptsAdded.put(ScriptsAdded.size(), thisScript);
			IndicesScriptsAdded.put(thisScript, ScriptsAdded.size() - 1);
			selectedScriptIcons.add(new ScriptIcon(thisScript).ScriptIconPanel);
		}

	}

	// serialize pipeline
	protected static void save() throws IOException {
		Boolean writeindeed = false;
		File f = new File(SavePath);

		// overwrite check
		if (f.exists()) {
			if (GUImethods.BooleanDialog("Overwrite existing file?", "File exists")) {
				new File(SavePath).delete();
				writeindeed = true;
			}
		} else {
			writeindeed = true;
		}

		if (writeindeed) {
			Script[] Oout = ScriptsAdded.values().toArray(new Script[ScriptsAdded.size()]);

			FileOutputStream FileOutputStream = new FileOutputStream(SavePath);
			ObjectOutputStream ScriptsPipelineFileOutputStream = new ObjectOutputStream(FileOutputStream);
			ScriptsPipelineFileOutputStream.writeObject(Oout);
			ScriptsPipelineFileOutputStream.close();
			FileOutputStream.close();
		}
	}

	/*
	 * infer hierarchy from graph
	 * 
	 * the algorithm works as follows: - find all caps (scripts that are not
	 * "connected from" any other script) - go backwards and raise the value of a
	 * script that is connected from the cap by the maximum within the hierarchy + 1
	 * - define those scripts as new caps and repeat until a script has empty
	 * "connections from" (end point)
	 * 
	 * This way the branch levels of the leaves are found relative to the lowest
	 * level.
	 * 
	 * Going backwards from here allows us to sort the scripts in the "correct"
	 * order
	 */
	public static void getHierarchy() {
		ScriptHierarchy.clear();

		// get all scripts in question
		Script[] allScripts = ScriptsAdded.values().toArray(new Script[ScriptsAdded.size()]);

		// find script that are not "connected from" any other script
		ArrayList<Integer> validCapsIndices = findCaps();

		// if cap put 1 , else put zero into the hierarchy
		for (Script tmp : allScripts) {

			if (tmp.ConnectionFrom.size() < 1) {
				ScriptHierarchy.put(tmp, 1);
			} else {
				ScriptHierarchy.put(tmp, 0);
			}
		}

		ArrayList<Integer> currentIndices = validCapsIndices;
		ArrayList<Integer> newIndices = new ArrayList<Integer>();
		ArrayList<Integer> uniquenewIndices = new ArrayList<Integer>();
		Set<Integer> set;

		while (!currentIndices.isEmpty()) {
			newIndices.clear();
			uniquenewIndices.clear();

			for (Integer currCap : currentIndices) {
				newIndices.addAll(ScriptsAdded.get(currCap).ConnectionFrom);
			}
			set = new HashSet<Integer>(newIndices);
			uniquenewIndices = new ArrayList<Integer>(set);

			Integer prevVal = Collections.max(ScriptHierarchy.values());

			for (Integer uniquenewInd : uniquenewIndices) {
				ScriptHierarchy.put(ScriptsAdded.get(uniquenewInd), prevVal + 1);
			}
			currentIndices.clear();
			currentIndices.addAll(uniquenewIndices);

		}
		Integer maxVal;
		Integer compVal;
		for (Integer currCap : findCaps()) {
			maxVal = 0;

			for (Integer currInd : ScriptsAdded.get(currCap).ConnectionFrom) {
				compVal = ScriptHierarchy.get(ScriptsAdded.get(currInd));
				if (maxVal < compVal) {
					maxVal = compVal;
				}
			}
			ScriptHierarchy.put(ScriptsAdded.get(currCap), maxVal - 1);
		}
	}

	// find all scripts that are not "connected from" any other script
	public static ArrayList<Integer> findCaps() {

		Iterator<Entry<Integer, Script>> it = ScriptsAdded.entrySet().iterator();

		LinkedHashMap<Integer, Integer> allNotCappingIndices = new LinkedHashMap<Integer, Integer>();
		LinkedHashMap<Integer, Integer> allCappingIndices = new LinkedHashMap<Integer, Integer>();

		while (it.hasNext()) {
			Map.Entry<Integer, Script> pair = (Map.Entry<Integer, Script>) it.next();

			allCappingIndices.put(allCappingIndices.size(), pair.getKey());

			for (Integer value : pair.getValue().ConnectionFrom) {
				allNotCappingIndices.put(allNotCappingIndices.size(), value);
			}
		}
		int invalidCapping;
		for (int m = 0; m < allNotCappingIndices.size(); m++) {
			invalidCapping = allNotCappingIndices.get(m);
			allCappingIndices.values().removeAll(Collections.singleton(invalidCapping));
		}
		ArrayList<Integer> validCapsIndices = new ArrayList<Integer>(allCappingIndices.values());
		return validCapsIndices;
	}

	// wrap a script object to be used together with Qsub (causes a change in the
	// pipeline due to the use of a qsub job submission wrapper script)
	public static Script newQsubScript(Script ScriptIn) throws FileNotFoundException {
		Script Qsub = new Script();

		Qsub.internal_map.put(InternalVarNameFile, QsubTemplateLocation);

		try {
			Qsub.File2ScriptInfo();
		} catch (NullPointerException e) {
		}
		Qsub.output_map = ScriptIn.output_map;
		Qsub.qsub_map = ScriptIn.qsub_map;
		Qsub.input_map.put(QsubRunScriptVarName, ScriptIn.internal_map.get(InternalVarNameFile));
		Qsub.internal_map.put(InternalVarNameLabel, ScriptIn.internal_map.get(InternalVarNameLabel) + "Qsub");
		return Qsub;
	}

	// creates runnable pipeline in form of a language specific script file, keeping
	// the graphically defined dependencies
	public static void makePipeline() throws IOException {
		String path = GUImethods.FileSelectionDialog("Select Pipeline folder", "New Pipeline",
				InternalPipeExtension + "Collection", false, false);

		String PipelineCode = LanguageEnvironment + eol;

		for (Script thisScript : sortScriptsByHierarchy()) {

			thisScript.internal_map.put(InternalVarNameFile,
					path + filesep + thisScript.internal_map.get(InternalVarNameLabel) + "." + LanguageFileExtension);
			thisScript.UpdateHeaderString();

			String newFile = LanguageEnvironment + eol + eol + thisScript.file_header + eol
					+ thisScript.code_map.get("") + eol;
			BufferedWriter out = new BufferedWriter(new FileWriter(thisScript.internal_map.get(InternalVarNameFile)));
			out.write(newFile);
			out.close();

			if (thisScript.hasqsub) {
				thisScript = newQsubScript(thisScript);
				thisScript.internal_map.put(InternalVarNameLabel, thisScript.internal_map.get(InternalVarNameLabel));

				thisScript.internal_map.put(InternalVarNameFile, path + filesep
						+ thisScript.internal_map.get(InternalVarNameLabel) + "." + LanguageFileExtension);
				thisScript.UpdateHeaderString();

				newFile = LanguageEnvironment + eol + eol + thisScript.file_header + eol + thisScript.code_map.get("")
						+ eol;
				out = new BufferedWriter(new FileWriter(thisScript.internal_map.get(InternalVarNameFile)));
				out.write(newFile);
				out.close();
			}

			PipelineCode += (LanguageRuncommand + " " + thisScript.internal_map.get(InternalVarNameFile) + eol);

		}
		PipelineCode += (LanguageExitCommand + eol);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String filename_ = dateFormat.format(date).toString().replaceAll("/|:|\\s+", "-");
		BufferedWriter out = new BufferedWriter(
				new FileWriter(path + filesep + filename_ + "." + LanguageFileExtension));
		out.write(PipelineCode);
		out.close();

		SavePath = path + filesep + filename_ + "." + InternalPipeExtension;
		save();
	}

	// sorts scripts that were loaded according to a defined hierarchy
	public static ArrayList<Script> sortScriptsByHierarchy() {
		ArrayList<Script> sortedScrips = new ArrayList<Script>();

		Integer highestlevel = Collections.max(ScriptHierarchy.values());

		while (sortedScrips.size() < ScriptsAdded.size()) {

			Iterator<Entry<Integer, Script>> it = ScriptsAdded.entrySet().iterator();

			while (it.hasNext()) {
				Map.Entry<Integer, Script> pair = (Map.Entry<Integer, Script>) it.next();

				if (ScriptHierarchy.get(pair.getValue()) == highestlevel) {
					sortedScrips.add(pair.getValue());
				}

			}
			highestlevel--;
		}

		return sortedScrips;
	}

	// reset internal values
	public static void clearPipeline() {
		ScriptsAdded.clear();
		ScriptHierarchy.clear();
		IndicesScriptsAdded.clear();
		selectedScriptIcons.clear();
		selectedScripts.clear();
	}

}