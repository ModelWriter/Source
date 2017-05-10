package synalp.parsing.utils;

import java.util.Map;

import synalp.parsing.Inputs.Sentence;

public class CustomTextReplacement {

	public static String getDefaultReplacementString (String input) {
		String formattedSentence = input;
		for (Map.Entry<String, String> replacement:Sentence.replaceWords.entrySet()) {
			formattedSentence = formattedSentence.replace(replacement.getValue(),replacement.getKey()); // This also replaces all matching occurrences like replaceAll except that this time the replacer can be a
			// string and not be considered as a regex pattern (as in the case of replaceAll)
		}
		formattedSentence = formattedSentence.replace("_+_"," "); // This one is for the anchors of fully specified entries bearing multiple words.
		return formattedSentence;
	}

}
