package synalp.commons.lexicon.lexformat;

import java.util.*;

/**
 * Macros contains macros indexed by their name.
 * @author Alexandre Denis
 *
 */
@SuppressWarnings("serial")
public class Macros extends HashMap<String, Macro>
{
	/**
	 * Adds the given Macro. If there is an existing Macro with same name it is overriden.
	 * @param macro 
	 */
	public void add(Macro macro)
	{
		put(macro.getName(), macro);
	}
}
