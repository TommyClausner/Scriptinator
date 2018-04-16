
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
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;

// initialize style features
// Those can be chosen freely. Definitions should apply to all language specific script files.
public class StyleSheet {

	// Script dependent file and line separators
	protected static String eol = System.getProperty("line.separator"); // End-Of-Line indication
	protected static String eolDelimiter = "\\s*[" + eol + "]\\s*"; // Delimiter for newline
	protected static String filesep = System.getProperty("file.separator"); // default system specific file separator
	protected static Font DefaultGUIFont = new Font("Helvetica", Font.PLAIN, 12); // default font for GUI elements
	protected static Font DefaultCodeFont = new Font("Futura", Font.PLAIN, 12); // default font for displayed code

	// Definition of basic Language aspects used for header readout
	protected static String LanguageEnvironment = "#!" + filesep + "bin" + filesep + "bash"; // first line of every
																								// executable

	protected static String LanguageRuncommand = "sh"; // command used for execution (e.g. sh myscript.sh)
	protected static String LanguageDeclareVarUsing = "="; // command used to assign variables (e.g. var=1)
	protected static String LanguageEndOfInstruction = ";"; // String used to separate commands (e.g. var=1;)
	protected static String LanguageFileExtension = "sh";
	protected static String LanguageScriptTypeName = "BashScript";
	protected static String LanguageCommentPrefix = "#"; // String that is used to comment a script (e.g. # for bash
															// scripts)
	protected static String LanguageDefaultVariableValue = "none"; // value of a new unused variable
	protected static String LanguageExitCommand = "exit 0"; // exit command used by the language to exit the programm
	// section

	// Name of Variables for the currently sourced file and the qsub-indication
	protected static String InternalVarNameFile = "file"; // use this to indicate absolute path
	protected static String InternalVarNameQsub = "useqsub"; // use this to indicate internal variable name for qsub use
	protected static String InternalVarNameLabel = "label"; // used to indicate internal label
	protected static String InternalDefaultLabel = "Unknown file"; // default internal label
	protected static String InternalVarNameShortDescription = "shortLabel"; // used to indicate internal short
																			// description displayed on script icons
	protected static String InternalDefaultShortDescription = "uKn"; // default short description
	protected static String InternalPipeExtension = "pipe"; // extension used to (de-) serialize pipelines

	// Strings to identify start, end and sections of the header
	protected static String IndStringBeginHeader = "BEGIN HEADER"; // use this to indicate the start of the header
	protected static String IndStringEndHeader = "END HEADER"; // use this to indicate the end of the header
	protected static String IndStringInternalHeader = "Scriptinator"; // use this to indicate internal variables
	protected static String IndStringExternalHeader = "Script"; // use this to indicate script variables

	// default String to mark section (added to the end of a section header, making
	// it identifiable)
	protected static String IndStringEndOfHeaderSection = " " + LanguageCommentPrefix; // String to indicate the end of
																						// a section header
	protected static int NumRepetitionsMultiprefix = 3; // how often is "LanguageCommentPrefix"
	protected static String Multiprefix = new String(new char[NumRepetitionsMultiprefix]).replace("\0",
			Character.toString(LanguageCommentPrefix.charAt(0))); // multi-LanguageCommentPrefix for special annotation
																	// (e.g. ###)

	// initialize key strings for header
	/*
	 * Those can be chosen freely. The respective string defined here must be
	 * present in the in the header file to indicate the section. Thereby the
	 * section must be called "#*String*#", where String is one of the below to be
	 * chosen and "*" a place holder for any text. The first "#" represents the
	 * comment prefix defined and the second "#" the section identifier
	 */
	protected static String IndStringFileName = "General"; // String that must be contained to indicate File section
	protected static String IndStringInput = "Input"; // String that must be contained to indicate Input section
	protected static String IndStringOutput = "Output"; // String that must be contained to indicate Output section
	protected static String IndStringQsub = "Qsub"; // String that must be contained to indicate qsub section
	protected static String IndStringMisc = "Misc"; // String that must be contained to indicate misc section
	protected static String IndStringCode = "Code"; // String that must be contained to indicate code section

	// added to Key Strings for header to provide additional information
	protected static String AddToIndStringFileName = " information"; // use this as additional header string for the
																		// File section
	protected static String AddToIndStringInput = " Variables and Paths"; // use this as additional header string for
																			// the Input
	protected static String AddToIndStringOutput = " Variables and Paths"; // use this as additional header string for
																			// the Output
	// section
	protected static String AddToIndStringQsub = " information"; // use this as additional header string for the qsub
																	// section
	protected static String AddToIndStringMisc = " Variables"; // use this as additional header string for the misc
																// section

	protected static String AddToIndStringCode = " actual "; // use this as additional header string for the File

	/*
	 * GUI SETTINGS
	 */

	// GUI Script Icon font
	protected static Font GUIScriptIconFont = new Font("Futura", Font.BOLD, 16);

	// Background color of main window
	protected static Color GUIbackGroundColor = Color.LIGHT_GRAY;

	// Colors for node Icons - will be chosen randomly
	protected static Color[] GUIscriptIconColors = new Color[] { new Color(92, 194, 252), new Color(28, 164, 252),
			new Color(17, 119, 184), new Color(8, 78, 126),

			new Color(121, 252, 233), new Color(47, 230, 207), new Color(25, 168, 156), new Color(17, 123, 118),

			new Color(140, 248, 90), new Color(101, 214, 67), new Color(41, 175, 29), new Color(13, 112, 15),

			new Color(254, 250, 55), new Color(250, 224, 71), new Color(247, 185, 43), new Color(253, 147, 38),

			new Color(253, 151, 143), new Color(252, 101, 84), new Color(235, 38, 31), new Color(179, 26, 16),

			new Color(253, 143, 197), new Color(237, 98, 167), new Color(201, 47, 123), new Color(151, 30, 94) };

	// Thickness of node connection lines in px
	protected static int GUIConnectionIndicatorSize = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()
			/ 80;

	// Size of node connection icon in px
	protected static int GUIscriptIconSize = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 17.5);
	// Size of Qsub Ring for node connection icon in px
	protected static int GUIscriptIconQsubRingSize = 4;

	// Maximum Number of Information columns in the properties panel
	protected static int GUIscriptPropertiesPanelMaxNumCol = 4;

	// Colors for Qsub indication (color and frame color)
	protected static Color GUIscriptIconQsubRingFrameColor = Color.BLACK;
	protected static Color GUIscriptIconQsubRingColor = Color.WHITE;

	// ratio of how much is occupied by the window
	protected static double MainWindowOccupiesThisMuchOfMyScreen = 0.8;

	// size of main buttons in pixel
	protected static int MainWindowButtonSize = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 20;

	// location of qsub submission template file
	protected static String QsubTemplateLocation = "RunOnQsubTemplate.sh";

	// variable name indicating the path of the script to be wrapped
	protected static String QsubRunScriptVarName = "ScriptToRun";

	// skip boolean dialogs
	protected static Boolean skipClearPipelineDialog = false;
	protected static Boolean skipDeleteScriptDialog = true;
	protected static Boolean skipOverwriteSaveScriptDialog = false;
	protected static Boolean skipOverwriteSavePipelineDialog = false;
	protected static Boolean skipCloseWindowDialog = false;
}