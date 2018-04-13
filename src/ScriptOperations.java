
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

/*
 * class used to perform methods on language specific scripts and script objects
 */
public class ScriptOperations extends StyleSheet {

	// find header indicator, read header information and rest of the file as plain
	// text
	public static String[] ReadScriptFromFileAndSplitHeaderAndCode(String ScriptPath) throws FileNotFoundException {
		Scanner scanner = null;
		String[] out_array = new String[2];
		String FullHeaderString = "";
		String FullFileWithoutHeaderString = "";

		scanner = new Scanner(new File(ScriptPath));

		scanner.useDelimiter(eolDelimiter);

		FullHeaderString = "";
		FullFileWithoutHeaderString = "";
		String currline = "";
		Boolean readline = false;
		while (scanner.hasNext()) {
			currline = scanner.nextLine();

			// find start and end of header section
			if (currline.contains(IndStringEndHeader)) {
				readline = false;
				currline = scanner.nextLine();
			} else if (currline.contains(IndStringBeginHeader)) {
				readline = true;
				currline = scanner.nextLine();
			}

			if (readline) {
				FullHeaderString += (currline + eol);
			} else {
				// obtain raw code
				currline = currline.trim();
				if (!currline.contains(LanguageEnvironment) & !currline.trim().matches("")) {
					FullFileWithoutHeaderString += (currline + eol);
				}
			}
		}

		// check if file has header, else create one
		if (!HelperMethods.hasHeader(ScriptPath)) {
			Script tmp = new Script();
			FullHeaderString = MakeHeaderString(tmp);
		}
		out_array[0] = FullHeaderString;

		out_array[1] = FullFileWithoutHeaderString;
		scanner.close();

		return out_array;

	}

	// translate plain text header information into script object
	public static Script HeaderString2Script(String FullHeaderString) {
		Script newScript = new Script();
		newScript.file_header = FullHeaderString;
		ArrayList<String> HeaderStringSeparated = new ArrayList<String>();
		LinkedHashMap<String, Integer> headerInfoIndex = new LinkedHashMap<String, Integer>();
		int i = 1;
		Boolean isFieldName = true;

		// find indices for subsections
		for (String currenLine : FullHeaderString.split(eolDelimiter)) {

			// avoiding switch case statement due to its inability to deal with variable
			// values
			if (currenLine.matches(
					LanguageCommentPrefix + ".*" + IndStringInput + ".*" + IndStringEndOfHeaderSection + ".*")) {
				headerInfoIndex.put(IndStringInput, i);
				isFieldName = true;
				i++;

			} else if (currenLine.matches(
					LanguageCommentPrefix + ".*" + IndStringOutput + ".*" + IndStringEndOfHeaderSection + ".*")) {
				headerInfoIndex.put(IndStringOutput, i);
				isFieldName = true;
				i++;

			} else if (currenLine.matches(
					LanguageCommentPrefix + ".*" + IndStringFileName + ".*" + IndStringEndOfHeaderSection + ".*")) {
				headerInfoIndex.put(IndStringFileName, i);
				isFieldName = true;
				i++;

			} else if (currenLine.matches(
					LanguageCommentPrefix + ".*" + IndStringQsub + ".*" + IndStringEndOfHeaderSection + ".*")) {
				headerInfoIndex.put(IndStringQsub, i);
				isFieldName = true;
				i++;

			} else if (currenLine.matches(
					LanguageCommentPrefix + ".*" + IndStringMisc + ".*" + IndStringEndOfHeaderSection + ".*")) {
				headerInfoIndex.put(IndStringMisc, i);
				isFieldName = true;
				i++;
			} else if (currenLine.matches(
					LanguageCommentPrefix + ".*" + IndStringCode + ".*" + IndStringEndOfHeaderSection + ".*")) {
				headerInfoIndex.put(IndStringCode, i);
				isFieldName = true;
				i++;
			} else {
				isFieldName = false;
			}

			if (!isFieldName) {
				String currenLine_tmp = currenLine.startsWith(LanguageCommentPrefix) ? currenLine.substring(1).trim()
						: currenLine.trim();

				if (HeaderStringSeparated.size() <= (i - 1)) {
					HeaderStringSeparated.add(currenLine_tmp + eol);
				} else {
					HeaderStringSeparated.set(i - 1, HeaderStringSeparated.get(i - 1) + currenLine_tmp + eol);
				}
			}
		}

		// translate separated string sections into hashmaps and add to new script
		String tmp = HeaderStringSeparated.get(headerInfoIndex.get(IndStringInput));
		newScript.input_map = HelperMethods.StringValuePairs2HashMap(tmp, false);

		tmp = HeaderStringSeparated.get(headerInfoIndex.get(IndStringOutput));
		newScript.output_map = HelperMethods.StringValuePairs2HashMap(tmp, false);

		tmp = HeaderStringSeparated.get(headerInfoIndex.get(IndStringQsub));
		newScript.qsub_map = HelperMethods.StringValuePairs2HashMap(tmp, false);

		tmp = HeaderStringSeparated.get(headerInfoIndex.get(IndStringMisc));
		newScript.misc_map = HelperMethods.StringValuePairs2HashMap(tmp, false);

		tmp = HeaderStringSeparated.get(headerInfoIndex.get(IndStringFileName));
		newScript.internal_map = HelperMethods.StringValuePairs2HashMap(tmp, false);
		return newScript;
	}

	// create a header string as used in the language specific scripts from internal
	// script object header information
	public static String MakeHeaderString(Script ScriptIn) {

		String constructedHeader = Multiprefix + " " + IndStringBeginHeader + " " + Multiprefix + eol + eol
				+ Multiprefix + " " + IndStringInternalHeader + " " + Multiprefix + eol + eol;
		constructedHeader += (LanguageCommentPrefix + " " + IndStringFileName + AddToIndStringFileName
				+ IndStringEndOfHeaderSection + eol);
		constructedHeader += HelperMethods.HashMap2StringValuePairs(ScriptIn.internal_map, LanguageCommentPrefix + " ");

		constructedHeader += (eol + Multiprefix + " " + IndStringExternalHeader + " " + Multiprefix + eol);

		constructedHeader += (eol + LanguageCommentPrefix + " " + IndStringInput + AddToIndStringInput
				+ IndStringEndOfHeaderSection + eol);
		constructedHeader += HelperMethods.HashMap2StringValuePairs(ScriptIn.input_map, "");

		constructedHeader += (eol + LanguageCommentPrefix + " " + IndStringOutput + AddToIndStringOutput
				+ IndStringEndOfHeaderSection + eol);
		constructedHeader += HelperMethods.HashMap2StringValuePairs(ScriptIn.output_map, "");

		constructedHeader += (eol + LanguageCommentPrefix + " " + IndStringQsub + AddToIndStringQsub
				+ IndStringEndOfHeaderSection + eol);
		constructedHeader += HelperMethods.HashMap2StringValuePairs(ScriptIn.qsub_map, "");

		constructedHeader += (eol + LanguageCommentPrefix + " " + IndStringMisc + AddToIndStringMisc
				+ IndStringEndOfHeaderSection + eol);
		constructedHeader += HelperMethods.HashMap2StringValuePairs(ScriptIn.misc_map, "");

		constructedHeader += (eol + Multiprefix + " " + IndStringEndHeader + " " + Multiprefix + eol);
		return constructedHeader;
	}

}