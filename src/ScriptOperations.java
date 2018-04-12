import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class ScriptOperations extends StyleSheet {

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
				currline = currline.trim();
				if (!currline.contains(LanguageEnvironment) & !currline.trim().matches("")) {
					FullFileWithoutHeaderString += (currline + eol);
				}
			}
		}
		if (!HelperMethods.hasHeader(ScriptPath)) {
			Script tmp = new Script();
			FullHeaderString = MakeHeaderString(tmp);
		}
		out_array[0] = FullHeaderString;

		out_array[1] = FullFileWithoutHeaderString;
		scanner.close();

		return out_array;

	}

	public static Script HeaderString2Script(String FullHeaderString) {
		Script newScript = new Script();
		newScript.file_header = FullHeaderString;
		ArrayList<String> HeaderStringSeparated = new ArrayList<String>();
		LinkedHashMap<String, Integer> headerInfoIndex = new LinkedHashMap<String, Integer>();
		int i = 1;
		Boolean isFieldName = true;
		for (String currenLine : FullHeaderString.split(eolDelimiter)) {

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