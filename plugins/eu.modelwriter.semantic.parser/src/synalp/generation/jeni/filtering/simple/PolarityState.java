package synalp.generation.jeni.filtering.simple;

import java.util.*;

import synalp.commons.grammar.GrammarEntry;
import synalp.generation.jeni.filtering.*;


/**
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class PolarityState extends PolarityKeys
{
	private int index;
	private Set<GrammarEntry> visited = new HashSet<GrammarEntry>();


	/**
	 * Creates a new empty state.
	 */
	public PolarityState()
	{
		index = 0;
	}


	/**
	 * Creates a new empty state.
	 * @param index
	 */
	public PolarityState(int index)
	{
		this.index = index;
	}


	/**
	 * @param state
	 * @param index
	 */
	public PolarityState(PolarityState state, int index)
	{
		putAll(state);
		this.visited.addAll(state.visited);
		this.index = index;
	}


	@Override
	public Interval get(Object key)
	{
		if (!containsKey(key))
			return new Interval(0, 0);
		else return super.get(key);
	}


	/**
	 * Adds all the values of given map to this state.
	 * @param map
	 */
	public void addAll(PolarityKeys map)
	{
		for(PolarityKey key : map.keySet())
			put(key, get(key).add(map.get(key)));
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + index;
		return result;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PolarityState other = (PolarityState) obj;
		if (index != other.index)
			return false;
		return true;
	}


	/**
	 * @param context
	 * @return whether the state is final
	 */
	public boolean isFinal(PolarityContext context)
	{
		if (index != context.getNumberOfLiterals() - 1)
			return false;
		if (!isZero())
			return false;
		return true;
	}


	/**
	 * @return the index
	 */
	public int getIndex()
	{
		return index;
	}


	/**
	 * @param entry
	 * @return whether the entry has been visited
	 */
	public boolean isVisited(GrammarEntry entry)
	{
		return visited.contains(entry);
	}


	/**
	 * @param entry
	 */
	public void setVisited(GrammarEntry entry)
	{
		visited.add(entry);
	}


	/**
	 * Recursively build up a string representing the current polarity charge.
	 * @return a string representing polarity of this state and substates
	 */
	public String toPolarityString()
	{
		StringBuilder ret = new StringBuilder();
		for(PolarityKey key : keySet())
		{
			Interval nb = get(key);
			if (nb.start != 0 || nb.end != 0)
				ret.append(nb).append(key.getCat());
		}
		return ret.toString();
	}


	@Override
	public String toString()
	{
		String str = toPolarityString();
		if (str.equals(""))
			return "<" + index + ">";
		else return "<" + index + ", " + str + ">";
	}
}
