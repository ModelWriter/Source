package synalp.generation.jeni.filtering.kow;

import java.util.*;

import org.apache.log4j.Logger;

import synalp.commons.grammar.*;
import synalp.commons.semantics.*;
import synalp.generation.jeni.filtering.*;


/**
 * This is from E.Kow phd, but I don't think I got it right.
 * @author Alexandre Denis
 */
public class PolarityFilteringKow implements PolarityFiltering
{
	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(PolarityFilteringKow.class);


	@Override
	public Set<GrammarEntries> filter(PolarityKey rootKey, Semantics input, Set<GrammarEntry> entries)
	{
		return filter(new PolarityContext(rootKey, input.asList(), entries));
	}


	/**
	 * Performs a filter of the entries.
	 * @param context 
	 * @return a set whose each element is a set of entries that are coherent alltogether
	 */
	public Set<GrammarEntries> filter(PolarityContext context)
	{
		PolarityAutomaton automaton = new PolarityAutomaton();
		automaton.createAutomaton(context);
		//logger.fine("0-key automaton: " + automaton.size()+"\n"+automaton);
		//logger.fine("0-key automaton: " + automaton.size());

		for(PolarityKey key : context.getAllKeys())
		{
			PolarityAutomaton newAutomaton = new PolarityAutomaton(key);
			newAutomaton.extendAutomaton(automaton, context);
			automaton = newAutomaton;
			//logger.fine(key+" automaton: " + automaton.size()+"\n"+automaton);
			//logger.fine(key + " automaton: " + automaton.size());
		}

		//System.out.println(automaton);

		Set<GrammarEntries> ret = new HashSet<GrammarEntries>();
		for(PolarityPath path : automaton.getPathsToFinalStates(context))
		{
			//System.out.println("Path: " + path);
			ret.add(new GrammarEntries(path.collectEntries()));
		}

		return ret;
	}

}
