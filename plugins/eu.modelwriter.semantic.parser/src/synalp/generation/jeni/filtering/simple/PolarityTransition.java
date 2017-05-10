package synalp.generation.jeni.filtering.simple;

import java.util.*;

import synalp.commons.grammar.*;


/**
 * PolarityTransition between two states.
 * @author Alexandre Denis
 */
public class PolarityTransition
{
	private PolarityState source;
	private PolarityState target;
	private Set<GrammarEntry> entries;


	/**
	 * @param source 
	 * @param target
	 */
	public PolarityTransition(PolarityState source, PolarityState target)
	{
		this.setSource(source);
		this.target = target;
		this.entries = new HashSet<GrammarEntry>();
	}
	
	
	/**
	 * @param source 
	 * @param target
	 * @param entry 
	 */
	public PolarityTransition(PolarityState source, PolarityState target, GrammarEntry entry)
	{
		this.setSource(source);
		this.target = target;
		this.entries = new HashSet<GrammarEntry>();
		this.entries.add(entry);
	}


	/**
	 * Adds the given entry to the set of entries.
	 * @param entry
	 */
	public void addEntry(GrammarEntry entry)
	{
		entries.add(entry);
	}


	/**
	 * @return the target
	 */
	public PolarityState getTarget()
	{
		return target;
	}


	/**
	 * @param target the target to set
	 */
	public void setTarget(PolarityState target)
	{
		this.target = target;
	}


	/**
	 * @return the entries
	 */
	public Set<GrammarEntry> getEntries()
	{
		return entries;
	}


	/**
	 * @param entries the entries to set
	 */
	public void setEntries(Set<GrammarEntry> entries)
	{
		this.entries = entries;
	}



	/**
	 * @return the source
	 */
	public PolarityState getSource()
	{
		return source;
	}


	/**
	 * @param source the source to set
	 */
	public void setSource(PolarityState source)
	{
		this.source = source;
	}


	/**
	 * @param entries
	 */
	public void addEntries(Set<GrammarEntry> entries)
	{
		this.entries.addAll(entries);
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		ret.append(source).append(" -> ").append(target).append(" ").append(GrammarEntry.toString(entries));
		return ret.toString().trim();
	}
}
