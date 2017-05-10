package synalp.generation.jeni.filtering.simple;

import java.util.*;

import org.apache.log4j.Logger;

import synalp.commons.grammar.*;
import synalp.commons.semantics.DefaultLiteral;
import synalp.generation.jeni.filtering.*;


/**
 * @author Alexandre Denis
 */
@SuppressWarnings("javadoc")
public class PolarityAutomaton
{
	public static Logger logger = Logger.getLogger(PolarityAutomaton.class);

	private PolarityKey key; // for debugging purposes

	private Map<PolarityState, Set<PolarityTransition>> outgoing; // <state, transitions going out this state>
	private Map<PolarityState, Set<PolarityTransition>> incoming; // <state, transitions going into this state>


	public PolarityAutomaton()
	{
		this(null);
	}


	public PolarityAutomaton(PolarityKey key)
	{
		this.key = key;
		outgoing = new HashMap<PolarityState, Set<PolarityTransition>>();
		incoming = new HashMap<PolarityState, Set<PolarityTransition>>();
	}


	/**
	 * Creates this automaton recursively.
	 * @param state
	 * @param literals
	 * @param entries
	 */
	public void createAutomaton(PolarityContext context)
	{
		logStartCreatingAutomaton(context);
		PolarityState initialState = new PolarityState(-1);
		initialState.put(context.getRootKey(), new Interval(-1,-1));

		Set<PolarityState> prevStates = new HashSet<PolarityState>();
		prevStates.add(initialState);
		prevStates.addAll(createEmptySemanticsTransitions(initialState, context));

		//Set<GrammarEntry> visited = new HashSet<GrammarEntry>();
		for(int i = 0; i < context.getNumberOfLiterals(); i++)
		{
			logLoopStart(i, prevStates, context.getLiteral(i));
			Set<GrammarEntry> entries = context.getEntriesByLiteral(i);
			Set<PolarityState> newStates = new HashSet<PolarityState>();
			for(PolarityState state : prevStates)
				for(GrammarEntry entry : entries)
					if (!state.isVisited(entry))
					{
						state.setVisited(entry);
						PolarityState newState = new PolarityState(state, i);
						newState.addAll(context.getPolarityCache().get(entry.getTree()));
						newStates.add(newState);
						addTransition(new PolarityTransition(state, newState, entry));

						logNotVisited(entry, state, newState);
					}
					else
					{
						PolarityState newState = new PolarityState(state, i);
						newStates.add(newState);
						addTransition(new PolarityTransition(state, newState, entry));

						logVisited(entry, state, newState);
					}

			prevStates = newStates;
		}
		//System.out.println(this);
	}

	/**
	 * Creates new states for empty semantics transitions.
	 * @param initialState
	 * @param context
	 * @return
	 */
	private Set<PolarityState> createEmptySemanticsTransitions(PolarityState initialState, PolarityContext context)
	{
		Set<PolarityState> ret = new HashSet<PolarityState>();
		Set<GrammarEntry> emptySemanticsEntries = context.getEmptySemanticsEntries();
		if (emptySemanticsEntries.isEmpty())
		{
			ret.add(initialState);
			return ret;
		}

		for(GrammarEntry entry : emptySemanticsEntries)
		{
			PolarityState newState = new PolarityState(initialState, -1);
			newState.addAll(context.getPolarityCache().get(entry.getTree()));
			addTransition(new PolarityTransition(initialState, newState, entry));
			ret.add(newState);
		}
		return ret;
	}


	public Set<PolarityPath> getPathsToFinalStates(PolarityContext context)
	{
		Set<PolarityPath> ret = new HashSet<PolarityPath>();
		for(PolarityState state : incoming.keySet())
			if (state.isFinal(context))
				for(PolarityTransition transition : incoming.get(state))
				{
					Set<PolarityPath> paths = getPathsTo(transition);
					for(PolarityPath path : paths)
						path.add(transition);
					ret.addAll(paths);
				}
		return ret;
	}


	/**
	 * @param source
	 * @return
	 */
	private Set<PolarityPath> getPathsTo(PolarityTransition transition)
	{
		Set<PolarityPath> ret = new HashSet<PolarityPath>();
		PolarityState source = transition.getSource();
		if (!incoming.containsKey(source))
		{
			ret.add(new PolarityPath(transition));
			return ret;
		}
		else
		{
			for(PolarityTransition tr : incoming.get(source))
			{
				Set<PolarityPath> paths = getPathsTo(tr);
				for(PolarityPath path : paths)
					ret.add(new PolarityPath(path, transition));
			}
			return ret;
		}
	}


	/**
	 * Adds the given transition to this automaton. If there exists already a transition between the
	 * source and the target, it adds all the entries of the given transition to the existing
	 * transition instead of adding a new one.
	 * @param transition
	 */
	private void addTransition(PolarityTransition transition)
	{
		addOutgoing(transition);
		addIncoming(transition);
	}


	private void addOutgoing(PolarityTransition transition)
	{
		PolarityState source = transition.getSource();
		if (!outgoing.containsKey(source))
			outgoing.put(source, new HashSet<PolarityTransition>());

		for(PolarityTransition existing : outgoing.get(source))
			if (existing.getTarget().equals(transition.getTarget()))
			{
				existing.addEntries(transition.getEntries());
				return;
			}

		outgoing.get(source).add(transition);
	}


	private void addIncoming(PolarityTransition transition)
	{
		PolarityState target = transition.getTarget();
		if (!incoming.containsKey(target))
			incoming.put(target, new HashSet<PolarityTransition>());

		for(PolarityTransition existing : incoming.get(target))
			if (existing.getSource().equals(transition.getSource()))
			{
				existing.addEntries(transition.getEntries());
				return;
			}

		incoming.get(target).add(transition);
	}


	public Set<PolarityState> getAllStates()
	{
		Set<PolarityState> states = new HashSet<PolarityState>(incoming.keySet());
		states.addAll(outgoing.keySet());
		return states;
	}


	/**
	 * @return
	 */
	public int size()
	{
		return getAllStates().size();
	}


	/**
	 * @return the key
	 */
	public PolarityKey getKey()
	{
		return key;
	}


	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		List<PolarityState> states = new ArrayList<PolarityState>(getAllStates());
		Collections.sort(states, new Comparator<PolarityState>()
		{
			public int compare(PolarityState arg0, PolarityState arg1)
			{
				return arg0.getIndex() - arg1.getIndex();
			}
		});
		for(PolarityState state : states)
		{
			ret.append(state);
			if (outgoing.containsKey(state))
				for(PolarityTransition tr : outgoing.get(state))
					ret.append("\n\t-> ").append(tr.getTarget()).append(" ").append(GrammarEntry.toString(tr.getEntries()));
			ret.append("\n");
		}
		return ret.toString().trim();
	}


//// logging



	private void logStartCreatingAutomaton(PolarityContext context)
	{
		if (logger.isDebugEnabled())
			logger.debug("Creating automaton with "+context.getLiterals());
	}

	
	private static void logLoopStart(int i, Set<PolarityState> prevStates, DefaultLiteral literal)
	{
		if (logger.isDebugEnabled())
			logger.debug(i + " " + prevStates+" "+literal);
	}


	private static void logVisited(GrammarEntry entry, PolarityState state, PolarityState newState)
	{
		if (logger.isTraceEnabled())
			logger.trace("\t" + state + " known entry " + entry.toMiniString() + " -> " + newState);
	}


	private static void logNotVisited(GrammarEntry entry, PolarityState state, PolarityState newState)
	{
		if (logger.isTraceEnabled())
			logger.trace("\t" + state + " new entry " + entry.toMiniString() + " -> " + newState);
	}
}
