import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import java.util.Map.Entry;

public class Script extends StyleSheet implements Serializable {

	private static final long serialVersionUID = -3943336977167464590L;

	protected String file_header = "";

	// initialize content features
	protected LinkedHashMap<String, String> input_map = HelperMethods
			.StringValuePairs2HashMap("InputVarName" + LanguageDeclareVarUsing + LanguageDefaultVariableValue, false);
	protected LinkedHashMap<String, String> output_map = HelperMethods
			.StringValuePairs2HashMap("OutputVarName" + LanguageDeclareVarUsing + LanguageDefaultVariableValue, false);
	protected LinkedHashMap<String, String> internal_map = HelperMethods
			.StringValuePairs2HashMap(InternalVarNameLabel + LanguageDeclareVarUsing + InternalDefaultLabel + eol
					+ InternalVarNameFile + LanguageDeclareVarUsing + System.getProperty("user.home") + filesep
					+ InternalVarNameFile + ".sh" + eol + InternalVarNameQsub + LanguageDeclareVarUsing + "false" + eol
					+ InternalVarNameThreeLetter + LanguageDeclareVarUsing + InternalDefaultThreeLetter, false);

	protected LinkedHashMap<String, String> misc_map = HelperMethods
			.StringValuePairs2HashMap("MiscVarName" + LanguageDeclareVarUsing + LanguageDefaultVariableValue, false);

	protected LinkedHashMap<String, String> code_map = HelperMethods.StringValuePairs2HashMap(
			"" + LanguageDeclareVarUsing + LanguageCommentPrefix + " This could be your code", true);

	protected LinkedHashMap<String, String> qsub_map = HelperMethods.StringValuePairs2HashMap(
			"jobtype" + eol + "batch" + eol + "walltime" + eol + "\"24:00:00\"" + eol + "memory" + eol + "32gb", false);

	protected ArrayList<Integer> ConnectionFrom = new ArrayList<Integer>();

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
		code_map.put("", Code);

		input_map = tmp.input_map;
		output_map = tmp.output_map;
		file_header = tmp.file_header;
		internal_map = tmp.internal_map;
		internal_map.put(InternalVarNameFile, path);
		qsub_map = tmp.qsub_map;
		misc_map = tmp.misc_map;
		UpdateHeaderString();
	}

	protected void load() throws FileNotFoundException, IOException, ScriptinatorException {
		String path = GUImethods.FileSelectionDialog("select " + LanguageScriptTypeName, LanguageScriptTypeName,
				LanguageFileExtension, true,true);

		internal_map.put(InternalVarNameFile, path);
		try {
			File2ScriptInfo();
		} catch (NullPointerException e) {
			throw new ScriptinatorException("Invalid Header");
		}
	}

	protected void save() throws IOException {
		Boolean writeindeed = false;
		String newFile = LanguageEnvironment + eol + eol + file_header + eol + code_map.get("") + eol;
		File f = new File(internal_map.get(InternalVarNameFile));
		if (f.exists()) {
			if (GUImethods.BooleanDialog("Overwrite existing file?", "File exists")) {
				writeindeed = true;
			}
		} else {
			internal_map.put(InternalVarNameFile, GUImethods.FileSelectionDialog("select " + LanguageScriptTypeName,
					LanguageScriptTypeName, LanguageFileExtension, false,true));
			writeindeed = true;
		}

		if (writeindeed) {

			BufferedWriter out = new BufferedWriter(new FileWriter(internal_map.get(InternalVarNameFile)));
			out.write(newFile);
			out.close();
		}
	}

	protected String runScript() throws IOException {

		File tmpFile = File.createTempFile("tmp", "." + LanguageFileExtension,
				new File(System.getProperty("user.dir")));

		BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile.getAbsoluteFile()));
		out.write(LanguageEnvironment + eol + HelperMethods.stripCommentedLines(file_header + eol + code_map.get("")));
		out.close();

		Process proc = Runtime.getRuntime().exec(LanguageRuncommand + " " + tmpFile.getAbsoluteFile());
		BufferedReader read = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String outstring = "";
		while (read.ready()) {
			outstring += read.readLine();
		}
		proc.destroy();
		tmpFile.delete();
		return outstring;
	}

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

	public void setPos(Point p) {
		Location = new Point(p.x - GUIscriptIconSize / 2, p.y - GUIscriptIconSize / 2);
	}

	public Point getPos() {
		return new Point(Location.x + GUIscriptIconSize / 2, Location.y + GUIscriptIconSize / 2);
	}

}