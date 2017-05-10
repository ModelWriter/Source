package synalp.generation.selection;

import synalp.commons.semantics.Semantics;

/**
 * A LexicalSelection is any Object that is able to select and anchor trees of a grammar given a
 * semantic input.
 * @author Alexandre Denis
 */
public interface LexicalSelection
{
	/**
	 * Selects grammar entries from a given semantic input and returns a LexicalSelectionResult.
	 * @param input
	 * @return a LexicalSelectionResult
	 */
	public LexicalSelectionResult selectEntries(Semantics input);
}
