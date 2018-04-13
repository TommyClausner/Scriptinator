
/*   _______                              _             
 *  |__   __|                            ( )            
 *     | | ___  _ __ ___  _ __ ___  _   _|/ ___         
 *     | |/ _ \| '_ ` _ \| '_ ` _ \| | | | / __|        
 *     | | (_) | | | | | | | | | | | |_| | \__ \        
 *     |_|\___/|_| |_| |_|_| |_| |_|\__, | |___/        
 *   ____            _     _         __/ |              
 *  |  _ \          | |   (_)       |___/ |             
 *  | |_) | __ _ ___| |__  _ _ __   __ _| |_ ___  _ __  
 *  |  _ < / _` / __| '_ \| | '_ \ / _` | __/ _ \| '__| 
 *  | |_) | (_| \__ \ | | | | | | | (_| | || (_) | |    
 *  |____/ \__,_|___/_| |_|_|_| |_|\__,_|\__\___/|_|    
 *   ____   ___   ___   ___    _______ __  __           
 *  |___ \ / _ \ / _ \ / _ \  |__   __|  \/  |          
 *    __) | | | | | | | | | |    | |  | \  / |          
 *   |__ <| | | | | | | | | |    | |  | |\/| |          
 *   ___) | |_| | |_| | |_| |    | |  | |  | |          
 *  |____/ \___/ \___/ \___/     |_|  |_|  |_|                  
 */
import java.awt.Color;
import java.awt.Font;

// initialize style features
// Those can be chosen freely. Definitions should apply to all (ba)sh files.
public class StyleSheet {

	// Script dependent file and line separators
	protected static String eol = System.getProperty("line.separator"); // End-Of-Line indication
	protected static String eolDelimiter = "\\s*[" + eol + "]\\s*"; // Delimiter for newline
	protected static String filesep = System.getProperty("file.separator"); // do not use system rather than runcommand
	protected static Font DefaultGUIFont = new Font("Helvetica", Font.PLAIN, 12);
	protected static Font DefaultCodeFont = new Font("Futura", Font.PLAIN, 12);
	protected static String logfile = System.getProperty("user.dir") + System.getProperty("file.separator")
			+ "TestCoverage.log";

	// specific
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
	protected static String LanguageDefaultVariableValue = "none";
	protected static String LanguageExitCommand="exit 0";
	// section

	// Name of Variables for the currently sourced file and the qsub-indication
	protected static String InternalVarNameFile = "file"; // use this to indicate internal file name
	protected static String InternalVarNameQsub = "useqsub"; // use this to indicate internal variable name for qsub use
	protected static String InternalVarNameLabel = "label";
	protected static String InternalDefaultLabel = "Unknown file";
	protected static String InternalVarNameShortDescription = "shortLabel";
	protected static String InternalDefaultShortDescription = "uKn";
	protected static String InternalPipeExtension = "pipe";

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
	protected static String IndStringCode = "Code"; // String that must be contained to indicate misc section

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
	protected static int GUIConnectionIndicatorSize = 10;

	// Size of node connection icon in px
	protected static int GUIscriptIconSize = 50;
	// Size of Qsub Ring for node connection icon in px
	protected static int GUIscriptIconQsubRingSize = 4;

	// Maximum Number of Information columns in the properties panel
	protected static int GUIscriptPropertiesPanelMaxNumCol = 4;

	// Colors for Qsub indication (color and frame color)
	protected static Color GUIscriptIconQsubRingFrameColor = Color.BLACK;
	protected static Color GUIscriptIconQsubRingColor = Color.WHITE;

	protected static double MainWindowOccupiesThisMuchOfMyScreen = 0.8;

	protected static int MainWindowButtonSize = 40;
	
	protected static String QsubTemplateLocation=System.getProperty("user.dir")+filesep+"templates"+filesep+"RunOnQsubTemplate.sh";
	
	protected static String QsubRunScriptVarName="ScriptToRun";
}