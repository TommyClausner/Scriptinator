
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
import java.io.File;
import java.io.FileNotFoundException;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

/*
 * Methods that are used to support the workflow in general
 */
public class HelperMethods extends StyleSheet {

	// check if file has header
	public static Boolean hasHeader(String ScriptPath) throws FileNotFoundException {
		File file = new File(ScriptPath);
		Boolean hasHeader = false;
		Scanner scanner = null;
		scanner = new Scanner(file);

		// scan until you find a header and if so set hasHeader to true
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.contains(IndStringBeginHeader)) {
				hasHeader = true;
				break;
			}
		}
		scanner.close();
		return hasHeader;
	}

	// convert a LinkedHashMap<String, String> to a strings of the form
	// "Name"+string_used_for_value_declaration+"Value"+eol
	// this methods converts between internal and human readable representation
	public static String HashMap2StringValuePairs(LinkedHashMap<String, String> inmap, String prefix) {
		String constructedHeadertmp = "";
		Iterator<Entry<String, String>> tmp_map = inmap.entrySet().iterator();
		while (tmp_map.hasNext()) {
			Map.Entry<String, String> entry = (Entry<String, String>) tmp_map.next();
			String key = entry.getKey();
			String value = entry.getValue();
			if (key.matches("")) {
				constructedHeadertmp += (value + eol);
			} else {
				constructedHeadertmp += (prefix + key + LanguageDeclareVarUsing + value + eol);
			}
		}
		return constructedHeadertmp;

	}

	// create linear spaced values
	public static float[] linspace(double start, double end, int steps) {
		float[] linspace = new float[steps];
		linspace[0] = (float) start;
		float spacing = (float) ((end - start) / (double) steps);
		if (steps > 1) {
			for (int i = 1; i < steps; i++) {
				linspace[i] = linspace[i - 1] + spacing;
			}
		}
		return linspace;
	}

	// convert strings of the form
	// "Name"+string_used_for_value_declaration+"Value"+eol to a
	// LinkedHashMap<String, String>
	// this methods converts between internal and human readable representation
	public static LinkedHashMap<String, String> StringValuePairs2HashMap(String StringValuePairs, Boolean iscode) {
		LinkedHashMap<String, String> out_map = new LinkedHashMap<String, String>();
		String[] string_to_process;

		// code gets a special treatment, because it's stored in the same way, but
		// retrieved differently (it has no Name and declaration String)
		if (iscode) {

			StringBuilder sb = new StringBuilder(LanguageDeclareVarUsing + StringValuePairs);
			if (StringValuePairs.indexOf(LanguageDeclareVarUsing) == 0) {
				sb.deleteCharAt(StringValuePairs.indexOf(LanguageDeclareVarUsing));// find Name - Value separator
			}

			String[] string_to_processtmp = { "", sb.toString() };
			string_to_process = string_to_processtmp;
		} else {
			String[] string_to_processtmp_alt = StringValuePairs.split(LanguageDeclareVarUsing + "|" + eol);
			string_to_process = string_to_processtmp_alt;
		}
		String[] pair = new String[2];
		int i = 0;
		for (String currentLine : string_to_process) {

			pair[i] = currentLine;
			i++;

			if (i == 2) {
				out_map.put(pair[0], pair[1]);
				i = 0;
			}
		}

		return out_map;
	}

	// Strip Commented Lines - removes all commented lines
	protected static String stripCommentedLines(String StringToStrip) {
		String stripedFile = "";
		for (String currenLine : StringToStrip.split(eolDelimiter)) {
			if (!currenLine.matches(LanguageCommentPrefix + ".*")) {
				stripedFile += (currenLine + eol);
			}
		}
		return stripedFile;
	}

}