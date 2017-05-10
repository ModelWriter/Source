package synalp.generation.jeni.filtering.simple;

import java.util.*;

import org.apache.log4j.Logger;

import synalp.commons.grammar.*;
import synalp.commons.semantics.*;
import synalp.generation.jeni.filtering.*;


/**
 * Another polarity filtering algo, it is simpler than E.Kow's one but may be incorrect.
 * @author Alexandre Denis
 */
public class PolarityFilteringSimple implements PolarityFiltering
{
	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(PolarityFilteringSimple.class);


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
	private Set<GrammarEntries> filter(PolarityContext context)
	{
		if (logger.isDebugEnabled())
			logger.debug("Polarity keys: "+context.getAllKeys());
		
		PolarityAutomaton automaton = new PolarityAutomaton();
		automaton.createAutomaton(context);

		if (logger.isDebugEnabled())
			logger.debug("Automaton:\n" + automaton.toString());

		Set<GrammarEntries> ret = new HashSet<GrammarEntries>();
		for(PolarityPath path : automaton.getPathsToFinalStates(context))
		{
			System.out.println("Path: " + path);
			ret.add(new GrammarEntries(path.collectEntries()));
		}

		return ret;
	}

}
