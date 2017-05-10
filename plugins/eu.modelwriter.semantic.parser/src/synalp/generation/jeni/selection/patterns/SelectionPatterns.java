package synalp.generation.jeni.selection.patterns;

import java.io.File;
import java.util.ArrayList;

import synalp.generation.jeni.selection.patterns.javacc.PatternReader;


/**
 * SelectionPatterns is a list of SelectionPattern.
 * @author Alexandre Denis
 *
 */
@SuppressWarnings("serial")
public class SelectionPatterns extends ArrayList<SelectionPattern>
{
	/**
	 * Loads the given file as SelectionPatterns. It catched the exception.
	 * @param patternFile
	 * @return new SelectionPatterns or null if there was a problem
	 */
	public static SelectionPatterns load(File patternFile)
	{
		try
		{
			return PatternReader.readPatterns(patternFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
