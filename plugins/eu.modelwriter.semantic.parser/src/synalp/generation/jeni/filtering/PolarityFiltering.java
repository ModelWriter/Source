package synalp.generation.jeni.filtering;

import java.util.Set;

import synalp.commons.grammar.*;
import synalp.commons.semantics.Semantics;


/**
 * General interface to do filtering.
 * @author Alexandre Denis
 */
public interface PolarityFiltering
{
	/**
	 * Returns sets of grammar entries that are coherent together.
	 * @param rootKey the key to determine the root category
	 * @param input the input semantics
	 * @param entries grammar entries that need to be filtered
	 * @return sets of grammar entries that are coherent together
	 */
	public Set<GrammarEntries> filter(PolarityKey rootKey, Semantics input, Set<GrammarEntry> entries);
}
