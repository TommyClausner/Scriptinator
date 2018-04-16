
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
import java.awt.Point;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import java.util.Map.Entry;

/*
 * main script object. All relevant information for scripts and their headers are stored here
 */
public class Script extends StyleSheet implements Serializable {

	private static final long serialVersionUID = -3943336977167464590L;
	protected String file_header = "";

	// initialize content features
	protected LinkedHashMap<String, String> input_map = HelperMethods
			.StringValuePairs2HashMap("InputVarName" + LanguageDeclareVarUsing + LanguageDefaultVariableValue, false);
	protected LinkedHashMap<String, String> output_map = HelperMethods
			.StringValuePairs2HashMap("OutputVarName" + LanguageDeclareVarUsing + LanguageDefaultVariableValue, false);
	protected LinkedHashMap<String, String> internal_map = HelperMethods.StringValuePairs2HashMap(
			InternalVarNameLabel + LanguageDeclareVarUsing + InternalDefaultLabel + eol + InternalVarNameFile
					+ LanguageDeclareVarUsing + System.getProperty("user.home") + filesep + InternalVarNameFile + ".sh"
					+ eol + InternalVarNameQsub + LanguageDeclareVarUsing + "false" + eol
					+ InternalVarNameShortDescription + LanguageDeclareVarUsing + InternalDefaultShortDescription,
			false);

	protected LinkedHashMap<String, String> misc_map = HelperMethods
			.StringValuePairs2HashMap("MiscVarName" + LanguageDeclareVarUsing + LanguageDefaultVariableValue, false);

	protected LinkedHashMap<String, String> code_map = HelperMethods.StringValuePairs2HashMap(
			"" + LanguageDeclareVarUsing + LanguageCommentPrefix + " This could be your code", true);

	protected LinkedHashMap<String, String> qsub_map = HelperMethods.StringValuePairs2HashMap(
			"jobtype" + eol + "batch" + eol + "walltime" + eol + "\"24:00:00\"" + eol + "memory" + eol + "32gb", false);

	protected ArrayList<Integer> ConnectionFrom = new ArrayList<Integer>();

	protected Boolean isdeleted = false;

	// GUI related properties
	protected Random rn = new Random();
	protected int colorInd = rn.nextInt(GUIscriptIconColors.length);
	protected Color ScriptObjectColor = GUIscriptIconColors[colorInd];
	protected Point Location;
	protected Boolean hasqsub;

	// wrapper function to update header information
	protected void UpdateHeaderString() {
		file_header = ScriptOperations.MakeHeaderString(this);
	}

	// wrapper function to update header information
	protected void UpdateHeaderInfo(Script ScriptIn) {
		input_map = ScriptIn.input_map;
		output_map = ScriptIn.output_map;
		internal_map = ScriptIn.internal_map;
		qsub_map = ScriptIn.qsub_map;
		misc_map = ScriptIn.misc_map;
		code_map = ScriptIn.code_map;
		file_header = ScriptOperations.MakeHeaderString(this);
	}

	// update script information from file
	protected void File2ScriptInfo() throws FileNotFoundException {
		Script tmp = new Script();

		// if there is no header create default header
		String path = internal_map.get(InternalVarNameFile);
		if (!HelperMethods.hasHeader(path)) {
			tmp = ScriptOperations.HeaderString2Script(ScriptOperations.MakeHeaderString(this));
			file_header = tmp.file_header;
		}
		// create dummy object to update actual object
		tmp = ScriptOperations.HeaderString2Script(
				ScriptOperations.ReadScriptFromFileAndSplitHeaderAndCode(internal_map.get(InternalVarNameFile))[0]);
		String Code = ScriptOperations
				.ReadScriptFromFileAndSplitHeaderAndCode(internal_map.get(InternalVarNameFile))[1];

		code_map = new LinkedHashMap<String, String>();
		code_map.put("", LanguageDeclareVarUsing + Code);

		input_map = tmp.input_map;
		output_map = tmp.output_map;
		file_header = tmp.file_header;
		internal_map = tmp.internal_map;
		internal_map.put(InternalVarNameFile, path);
		qsub_map = tmp.qsub_map;
		misc_map = tmp.misc_map;
		UpdateHeaderString();
	}

	// source a language specific script
	protected void load() throws FileNotFoundException, IOException {
		String path = GUImethods.FileSelectionDialog("select " + LanguageScriptTypeName, LanguageScriptTypeName,
				LanguageFileExtension, true, true);

		internal_map.put(InternalVarNameFile, path);
		try {
			File2ScriptInfo();
		} catch (NullPointerException e) {
		}
	}

	// write a language specific script according to the stored information
	protected void save() throws IOException {
		Boolean writeindeed = false;

		StringBuilder code = new StringBuilder(code_map.get(""));
		code.deleteCharAt(0);

		String newFile = LanguageEnvironment + eol + eol + file_header + eol + code.toString() + eol;
		File f = new File(internal_map.get(InternalVarNameFile));
		if (f.exists()) {
			if (GUImethods.BooleanDialog("Overwrite existing file?", "File exists", skipOverwriteSaveScriptDialog)) {
				writeindeed = true;
			} else {
				internal_map.put(InternalVarNameFile, GUImethods.FileSelectionDialog("select " + LanguageScriptTypeName,
						LanguageScriptTypeName, LanguageFileExtension, false, true));
				writeindeed = true;
			}
		} else {
			internal_map.put(InternalVarNameFile, GUImethods.FileSelectionDialog("select " + LanguageScriptTypeName,
					LanguageScriptTypeName, LanguageFileExtension, false, true));
			writeindeed = true;
		}

		if (writeindeed) {

			BufferedWriter out = new BufferedWriter(new FileWriter(internal_map.get(InternalVarNameFile)));
			out.write(newFile);
			out.close();
		}
	}

	// the following methods are used by the properties GUI to modify the number of
	// variables in a given section
	public void makeInputVar() {
		input_map.put("NewInputVar" + input_map.size(), "none");
	}

	public void removeInputVar() {
		if (input_map.size() > 0) {
			Entry<String, String> last = null;
			for (Entry<String, String> e : input_map.entrySet())
				last = e;
			input_map.remove(last.getKey());
		}
	}

	public void makeOutputVar() {
		output_map.put("NewOutputVar" + output_map.size(), "none");
	}

	public void removeOutputVar() {
		if (output_map.size() > 0) {
			Entry<String, String> last = null;
			for (Entry<String, String> e : output_map.entrySet())
				last = e;
			output_map.remove((last.getKey()));
		}
	}

	public void makeMiscVar() {
		misc_map.put("NewMiscVar" + misc_map.size(), "none");
	}

	public void removeMiscVar() {
		if (misc_map.size() > 0) {
			Entry<String, String> last = null;
			for (Entry<String, String> e : misc_map.entrySet())
				last = e;
			misc_map.remove((last.getKey()));
		}
	}

	// set location to center of (graphical) object
	public void setPos(Point p) {
		Location = new Point(p.x - GUIscriptIconSize / 2, p.y - GUIscriptIconSize / 2);
	}

	// get original position
	public Point getPos() {
		return new Point(Location.x + GUIscriptIconSize / 2, Location.y + GUIscriptIconSize / 2);
	}

}