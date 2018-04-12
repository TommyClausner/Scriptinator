
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

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Miscellaneous methods to support the workflow
public class HelperMethods extends StyleSheet {

	// get index of first character of StringToBeFound within StringToSearcheThrough
	public static int strfind(String StringToSearcheThrough, String StringToBeFound) {
		Pattern pattern = Pattern.compile(StringToBeFound);
		Matcher matcher = pattern.matcher(StringToSearcheThrough);
		int value = 0;
		while (matcher.find()) {
			value = matcher.start();
		}
		return value;
	}

	public static LinkedHashMap<String, String> StringValuePairs2HashMap(String StringValuePairs, Boolean iscode) {

		LinkedHashMap<String, String> out_map = new LinkedHashMap<String, String>();
		String[] string_to_process;
		if (iscode) {

			StringBuilder sb = new StringBuilder(StringValuePairs);
			if (StringValuePairs.indexOf(LanguageDeclareVarUsing) >= 0) {
				sb.deleteCharAt(StringValuePairs.indexOf(LanguageDeclareVarUsing));
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

	// check if file has header
	public static Boolean hasHeader(String ScriptPath) throws FileNotFoundException {
		File file = new File(ScriptPath);
		Boolean hasHeader = false;
		Scanner scanner = null;
		scanner = new Scanner(file);
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

	// Strip Commented Lines
	protected static String stripCommentedLines(String StringToStrip) {
		String stripedFile = "";
		for (String currenLine : StringToStrip.split(eolDelimiter)) {
			if (!currenLine.matches(LanguageCommentPrefix + ".*")) {
				stripedFile += (currenLine + eol);
			}
		}
		return stripedFile;
	}

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
}