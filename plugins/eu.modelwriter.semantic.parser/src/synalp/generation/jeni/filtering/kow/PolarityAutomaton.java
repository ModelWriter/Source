package synalp.generation.jeni.filtering.kow;

import java.util.*;

import org.apache.log4j.Logger;

import synalp.commons.grammar.GrammarEntry;
import synalp.generation.jeni.filtering.*;


/**
 * @author Alexandre Denis
 */
@SuppressWarnings("javadoc")
public class PolarityAutomaton
{
	public static Logger logger = Logger.getLogger(PolarityAutomaton.class);

	private PolarityKey key; // for debugging purposes
	private PolarityState initialState;

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
		logger.debug("Creating 0-key automaton");

		initialState = new PolarityState(0, 0, null, new HashSet<Integer>(), this);
		createAutomaton(initialState, context, new HashSet<PolarityState>());

		if (logger.isTraceEnabled())
			logger.trace("0-key automaton complete:\n" + this);

		minimize(context);

		if (logger.isTraceEnabled())
			logger.trace("0-key automaton minimized:\n" + this);
	}


	/**
	 * Creates recursively all transitions going out the given state.
	 * @param state
	 * @param literals
	 * @param entries
	 */
	private void createAutomaton(PolarityState state, PolarityContext context, Set<PolarityState> visited)
	{
		logger.trace("Visiting " + state);
		if (visited.contains(state))
			return;
		else visited.add(state);

		for(GrammarEntry entry : context.getEntries())
		{
			Set<Integer> extraLiterals = context.createExtraLiterals(state.getLiteralIndex(), entry);
			PolarityState newState = new PolarityState(state.getLiteralIndex() + 1, 0, null, extraLiterals, this);
			PolarityTransition transition = new PolarityTransition(state, newState, entry);
			addTransition(transition);

			logger.trace("Creating new transition " + transition);

			if (newState.getLiteralIndex() < context.getNumberOfLiterals())
				createAutomaton(newState, context, visited);

		}
	}


	/**
	 * Creates this automaton by extending the lower automaton.
	 */
	public void extendAutomaton(PolarityAutomaton lowerAutomaton, PolarityContext context)
	{
		logger.debug("Creating " + key + " automaton");

		int z = context.isRootKey(key) ? -1 : 0;
		initialState = new PolarityState(0, z, lowerAutomaton.initialState, lowerAutomaton.initialState.getExtraLiterals(), this);
		extendAutomaton(initialState, context, new HashSet<PolarityState>());

		if (logger.isTraceEnabled())
			logger.trace(key + " automaton complete:\n" + this);

		minimize(context);

		if (logger.isTraceEnabled())
			logger.trace(key + " automaton minimized:\n" + this);
	}


	/**
	 * Extends the automaton.
	 * @param state
	 * @param context
	 * @param hashSet
	 */
	private void extendAutomaton(PolarityState state, PolarityContext context, Set<PolarityState> visited)
	{
		logger.trace("Visiting " + state);
		if (visited.contains(state))
			return;
		else visited.add(state);

		PolarityState subState = state.getSubState();
		for(GrammarEntry entry : context.getEntries())
		{
			PolarityState nextSubState = subState.getAutomaton().getNext(subState, entry);
			if (nextSubState == null)
				continue;

			// obviously this does not work, we need to reintegrate the disjunctive polarites.
			int newPolarity = 0; //state.getPolarity()  + context.getPolarity(entry, key);

			PolarityState newState = new PolarityState(state.getLiteralIndex() + 1, newPolarity, nextSubState, nextSubState.getExtraLiterals(), this);
			PolarityTransition transition = new PolarityTransition(state, newState, entry);

			addTransition(transition);

			logger.trace("Creating new transition " + transition);

			if (newState.getLiteralIndex() < context.getNumberOfLiterals())
				extendAutomaton(newState, context, visited);

		}
	}


	/**
	 * Returns the next state of the given state that gets through the given entry.
	 * @param substate
	 * @param entry
	 * @return null if there is no such next state
	 */
	private PolarityState getNext(PolarityState state, GrammarEntry entry)
	{
		if (outgoing.containsKey(state))
			for(PolarityTransition transition : outgoing.get(state))
				if (transition.getEntries().contains(entry))
					return transition.getTarget();
		return null;
	}


	/**
	 * Minimizes this automaton by removing all states that do not lead to final states.
	 * @param context
	 */
	private void minimize(PolarityContext context)
	{
		for(PolarityState state : new ArrayList<PolarityState>(incoming.keySet()))
			if (state.getLiteralIndex() == context.getNumberOfLiterals() && !state.isFinal(context))
				removeBackward(state);
	}


	/**
	 * Removes the given state and removes all the states that exclusively transition to that state.
	 * @param state
	 */
	private void removeBackward(PolarityState state)
	{
		outgoing.remove(state);
		if (incoming.containsKey(state))
			for(PolarityTransition transition : incoming.remove(state))
			{
				PolarityState ancestor = transition.getSource();
				if (outgoing.containsKey(ancestor) && outgoing.get(ancestor).size() == 1)
					removeBackward(ancestor);
			}
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
				return arg0.getLiteralIndex() - arg1.getLiteralIndex();
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
}
