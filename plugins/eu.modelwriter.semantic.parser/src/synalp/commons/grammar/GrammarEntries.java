package synalp.commons.grammar;

import java.util.*;

/**
 * GrammarEntries is simply a set of GrammarEntry. It has been added to model the output of the
 * lexical selection.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class GrammarEntries extends HashSet<GrammarEntry>
{
	/**
	 * Flattens the given collection of entries.
	 * @param coll
	 * @return entries
	 */
	public static GrammarEntries flatten(Collection<GrammarEntries> coll)
	{
		GrammarEntries ret = new GrammarEntries();
		for(GrammarEntries entries : coll)
			ret.addAll(entries);
		return ret;
	}


	/**
	 * Empty constructor.
	 */
	public GrammarEntries()
	{

	}


	/**
	 * Shallow copy constructor.
	 * @param entries
	 */
	public GrammarEntries(Collection<GrammarEntry> entries)
	{
		addAll(entries);
	}


	/**
	 * Shallow copy constructor.
	 * @param entries
	 */
	@SafeVarargs
	public GrammarEntries(Collection<GrammarEntry>... entries)
	{
		for(Collection<GrammarEntry> coll : entries)
			addAll(coll);
	}


	/**
	 * Returns the intersection. This method does not modify these entries nor the given entries.
	 * @param entries
	 * @return the intersection
	 */
	public GrammarEntries intersection(GrammarEntries entries)
	{
		GrammarEntries ret = new GrammarEntries(this);
		ret.retainAll(entries);
		return ret;
	}


	/**
	 * Returns a map of entries indexed by their names.
	 * @return entries indexed by names
	 */
	public Map<String, GrammarEntry> getEntriesByNames()
	{
		Map<String, GrammarEntry> ret = new HashMap<String, GrammarEntry>();
		for(GrammarEntry entry : this)
			ret.put(entry.getName(), entry);
		return ret;
	}


	/**
	 * Returns the entries sorted by the index of their surface form in the given lemmas list.
	 * @param lemmas
	 * @return a list of entries
	 */
	public List<GrammarEntry> getEntriesSortedByIndex(List<String> lemmas)
	{
		List<GrammarEntry> ret = new ArrayList<GrammarEntry>();
		final Map<GrammarEntry, Integer> indexes = new HashMap<GrammarEntry, Integer>();
		for(GrammarEntry entry : this)
		{
			int index = lemmas.indexOf(entry.getLemmasSurface());
			indexes.put(entry, index);
			ret.add(entry);
		}
		Collections.sort(ret, new Comparator<GrammarEntry>()
		{
			@Override
			public int compare(GrammarEntry entry0, GrammarEntry entry1)
			{
				return indexes.get(entry0).compareTo(indexes.get(entry1));
			}
		});
		return ret;
	}

}
