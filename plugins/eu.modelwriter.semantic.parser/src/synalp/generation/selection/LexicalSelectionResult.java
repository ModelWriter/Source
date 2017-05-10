package synalp.generation.selection;

import java.util.*;

import synalp.commons.grammar.GrammarEntries;
import synalp.commons.semantics.Semantics;

import com.google.common.collect.*;


/**
 * A LexicalSelectionResult is a data structure returned by the lexical selection phase. It
 * associates a Semantics to a collection of GrammarEntries (implemented as a Multimap). Each
 * element of a given collection of GrammarEntries is itself a set of GrammarEntry objects that are
 * coherent with each other. It has been done this way to model the output of polarity filtering.
 * When polarity filtering is disabled, each collection of GrammarEntries contains only one element.
 * The Semantics keys are meant to represent variants of the input Semantics if the input Semantics
 * has to be modified.
 * @author Alexandre Denis
 */
public class LexicalSelectionResult
{
	private Multimap<Semantics, GrammarEntries> results = HashMultimap.create();


	/**
	 * Creates a new empty LexicalSelectionResult.
	 */
	public LexicalSelectionResult()
	{

	}


	/**
	 * Creates a LexicalSelectionResult with given Semantics and GrammarEntries.
	 * @param semantics
	 * @param entries
	 */
	public LexicalSelectionResult(Semantics semantics, GrammarEntries entries)
	{
		results.put(semantics, entries);
	}


	/**
	 * Creates a LexicalSelectionResult with given Semantics and collection of GrammarEntries.
	 * @param semantics
	 * @param entries
	 */
	public LexicalSelectionResult(Semantics semantics, Collection<GrammarEntries> entries)
	{
		results.putAll(semantics, entries);
	}


	/**
	 * Associates one GrammarEntries object to the given Semantics.
	 * @param semantics
	 * @param entries
	 */
	public void addResult(Semantics semantics, GrammarEntries entries)
	{
		results.put(semantics, entries);
	}

	
	/**
	 * Returns the collection of Semantics inputs that are considered in this LexicalSelectionResult.
	 * @return all the semantics variants
	 */
	public Collection<Semantics> getInputs()
	{
		return results.keySet();
	}
	

	/**
	 * Returns the set of GrammarEntries associated to the given Semantics.
	 * @param semantics
	 * @return the set of GrammarEntries associated to the given Semantics.
	 */
	public Set<GrammarEntries> getResults(Semantics semantics)
	{
		return new HashSet<GrammarEntries>(results.get(semantics));
	}


	/**
	 * Returns the set of all GrammarEntries associated to all Semantics. This method has been added
	 * to keep compatibility with the previous LexicalSelection interface.
	 * @return a set of GrammarEntries
	 */
	public Set<GrammarEntries> getResults()
	{
		Set<GrammarEntries> ret = new HashSet<GrammarEntries>();
		for(Semantics sem : results.keySet())
			ret.addAll(results.get(sem));
		return ret;
	}
}
