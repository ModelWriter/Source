package synalp.generation.jeni.selection.families;

import java.util.*;

import synalp.commons.grammar.*;
import synalp.commons.unification.FeatureConstant;
import synalp.commons.utils.Utils;

/**
 * Another simpler implementation of polarities. The keys are categories names, the values their
 * polarity.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class Polarities extends HashMap<String, Integer>
{
	/**
	 * Tests whether the given entries satisfy the given polarity constraint.
	 * @param entries
	 * @param constraint a constant whose values are the expected root categories
	 * @return true if the entries satisfy the constraint
	 */
	public static boolean satisfies(GrammarEntries entries, FeatureConstant constraint)
	{
		return new Polarities(entries).matches(constraint.getValues());
	}


	/**
	 * Creates and computes the polarities for given entries.
	 * @param entries
	 */
	public Polarities(GrammarEntries entries)
	{
		for(GrammarEntry entry : entries)
			computePolarities(entry);
	}


	/**
	 * Tests wether these polarities match the given alternative categories. This method returns
	 * true if there exists exactly one non-zero polarity whose value is 1, and if the corresponding
	 * category is found in the given alternatives.
	 * @param alternatives
	 * @return whether these polarities match the alternative categories
	 */
	public boolean matches(Set<String> alternatives)
	{
		boolean found = false;
		for(String cat : keySet())
		{
			int value = get(cat);
			if (value != 0)
			{
				if (value != 1)
					return false;
				else if (found)
					return false;
				else if (!alternatives.contains(cat))
					return false;
				else found = true;
			}
		}

		return true;
	}


	/**
	 * Computes the polarities for given entry.
	 * @param entry
	 */
	private void computePolarities(GrammarEntry entry)
	{
		boolean isAuxiliary = entry.getTree().isAuxiliary();
		Map<String, Integer> polarities = new HashMap<String, Integer>();
		for(Node node : entry.getTree().getNodes())
			if (node.isRoot() && !isAuxiliary)
				Utils.addN(1, node.getCategory().toString(), polarities);
			else if (node.isSubst())
				Utils.addN(-1, node.getCategory().toString(), polarities);

		for(String cat : polarities.keySet())
			Utils.addN(polarities.get(cat), cat, this);
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder("{");
		for(String cat : keySet())
		{
			int value = get(cat);
			if (value!=0)
				ret.append(value).append(cat).append(" ");
		}
		return ret.toString().trim()+"}";
	}
}
