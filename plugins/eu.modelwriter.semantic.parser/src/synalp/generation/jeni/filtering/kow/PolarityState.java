package synalp.generation.jeni.filtering.kow;

import java.util.Set;

import synalp.generation.jeni.filtering.PolarityContext;


/**
 * @author Alexandre Denis
 */
public class PolarityState
{
	private final int polarity;
	private final int literalIndex;
	private final Set<Integer> extraLiterals; // literals that have been seen in a tree semantics but not yet found in input

	private final PolarityState substate; // the state in a lower order automaton
	private final PolarityAutomaton automaton; // the automaton this state belongs to


	/**
	 * Creates a new PolarityState
	 * @param literalIndex
	 * @param polarity
	 * @param substate
	 * @param extraLiterals
	 * @param automaton
	 */
	public PolarityState(int literalIndex, int polarity, PolarityState substate, Set<Integer> extraLiterals, PolarityAutomaton automaton)
	{
		this.literalIndex = literalIndex;
		this.polarity = polarity;
		this.substate = substate;
		this.extraLiterals = extraLiterals;
		this.automaton = automaton;
	}


	/**
	 * Tests if this PolarityState is final given the context.
	 * @param context
	 * @return whether this state is final
	 */
	public boolean isFinal(PolarityContext context)
	{
		if (literalIndex < context.getNumberOfLiterals())
			return false;
		if (polarity != 0)
			return false;
		if (!extraLiterals.isEmpty())
			return false;

		if (substate == null)
			return true;
		else return substate.isFinal(context);
	}


	/**
	 * @return the polarity
	 */
	public int getPolarity()
	{
		return polarity;
	}


	/**
	 * @return the literalIndex
	 */
	public int getLiteralIndex()
	{
		return literalIndex;
	}


	/**
	 * @return the extraLiterals
	 */
	public Set<Integer> getExtraLiterals()
	{
		return extraLiterals;
	}


	/**
	 * @return the substate
	 */
	public PolarityState getSubState()
	{
		return substate;
	}


	/**
	 * @return the automaton
	 */
	public PolarityAutomaton getAutomaton()
	{
		return automaton;
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((automaton == null) ? 0 : automaton.hashCode());
		result = prime * result + ((extraLiterals == null) ? 0 : extraLiterals.hashCode());
		result = prime * result + literalIndex;
		result = prime * result + polarity;

		return result;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PolarityState other = (PolarityState) obj;
		if (automaton == null)
		{
			if (other.automaton != null)
				return false;
		}
		else if (!automaton.equals(other.automaton))
			return false;
		if (extraLiterals == null)
		{
			if (other.extraLiterals != null)
				return false;
		}
		else if (!extraLiterals.equals(other.extraLiterals))
			return false;
		if (literalIndex != other.literalIndex)
			return false;
		if (polarity != other.polarity)
			return false;

		return true;
	}


	/**
	 * Recursively build up a string representing the current polarity charge.
	 * @return a string representing polarity of this state and substates
	 */
	public String toPolarityString()
	{
		StringBuilder ret = new StringBuilder();
		if (automaton.getKey() != null)
		{
			ret.append(polarity > 0 ? "+" : "").append(polarity);
			ret.append(automaton.getKey().getCat());
		}
		if (substate != null)
			ret.append(substate.toPolarityString());
		return ret.toString();
	}


	@Override
	public String toString()
	{
		if (extraLiterals.isEmpty())
			return "<" + literalIndex + ", " + toPolarityString() + ">";
		else return "<" + literalIndex + ", " + toPolarityString() + ", " + extraLiterals + ">";
	}

}
