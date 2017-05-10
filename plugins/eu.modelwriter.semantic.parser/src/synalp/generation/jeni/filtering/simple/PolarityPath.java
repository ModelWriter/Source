package synalp.generation.jeni.filtering.simple;

import java.util.*;

import synalp.commons.grammar.GrammarEntry;


/**
 * A PolarityPath is a path in a PolarityAutomaton.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class PolarityPath extends ArrayList<PolarityTransition>
{
	/**
	 * Creates an empty path.
	 */
	public PolarityPath()
	{
	}
	
	
	/**
	 * Creates a path with a single transition.
	 * @param transition
	 */
	public PolarityPath(PolarityTransition transition)
	{
		add(transition);
	}


	/**
	 * Copies the given path and adds the given transition at the end.
	 * @param path
	 * @param transition
	 */
	public PolarityPath(PolarityPath path, PolarityTransition transition)
	{
		addAll(path);
		add(transition);
	}


	/**
	 * Returns a set of grammar entries that are on this PolarityPath.
	 * @return a set of grammar entries
	 */
	public Set<GrammarEntry> collectEntries()
	{
		Set<GrammarEntry> ret = new HashSet<GrammarEntry>();
		for(PolarityTransition transition : this)
			ret.addAll(transition.getEntries());
		return ret;
	}
}
