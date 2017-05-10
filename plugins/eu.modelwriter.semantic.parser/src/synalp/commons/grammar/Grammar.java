package synalp.commons.grammar;

import java.util.*;

import synalp.commons.derivations.GornAddress;
import synalp.commons.unification.FeatureConstant;


/**
 * A LTAG grammar indexed by entry name. It also provides a way to access to entries by their family
 * name once the families cache has been computed.
 */
@SuppressWarnings("serial")
public class Grammar extends HashMap<String, GrammarEntry>
{
	private Map<String, Set<GrammarEntry>> families; // <family name, entries in family>


	/**
	 * Prints on stdout the given grammar.
	 * @param grammar
	 */
	public static void print(Grammar grammar)
	{
		print(grammar.values());
	}


	/**
	 * Prints on stdout the given grammar.
	 * @param grammar
	 */
	public static void printShort(Grammar grammar)
	{
		printShort(grammar.values());
	}


	/**
	 * Prints on stdout the given entries.
	 * @param entries
	 */
	public static void print(Collection<GrammarEntry> entries)
	{
		List<GrammarEntry> sorted = new ArrayList<GrammarEntry>(entries);
		sortByFamilies(sorted);
		for(GrammarEntry entry : sorted)
			System.out.println(entry + "\n");
	}


	/**
	 * Prints on stdout a short representation of the given entries.
	 * @param entries
	 */
	public static void printShort(Collection<GrammarEntry> entries)
	{
		List<GrammarEntry> sorted = new ArrayList<GrammarEntry>(entries);
		sortByFamilies(sorted);
		for(GrammarEntry entry : sorted)
			System.out.println(entry.toShortString());
	}


	/**
	 * Prints on stdout a short representation of the given entries.
	 * @param entries
	 * @return the names of entries separated by spaces
	 */
	public static String printNames(Collection<GrammarEntry> entries)
	{
		List<GrammarEntry> sorted = new ArrayList<GrammarEntry>(entries);
		sortByFamilies(sorted);
		StringBuilder ret = new StringBuilder();
		for(GrammarEntry entry : sorted)
			ret.append(entry.getName()).append(" ");
		return ret.toString().trim();
	}


	/**
	 * Sorts the given list of entries by the number of nodes their tree contains.
	 * @param entries
	 */
	public static void sortByNames(List<GrammarEntry> entries)
	{
		Collections.sort(entries, new Comparator<GrammarEntry>()
		{
			@Override
			public int compare(GrammarEntry arg0, GrammarEntry arg1)
			{
				return arg0.getName().compareTo(arg1.getName());
			}
		});
	}


	/**
	 * Sorts the given list of entries by the number of nodes their tree contains.
	 * @param entries
	 */
	public static void sortByTreeSize(List<GrammarEntry> entries)
	{
		final Map<GrammarEntry, Integer> treeSizes = new HashMap<GrammarEntry, Integer>();
		for(GrammarEntry entry : entries)
			treeSizes.put(entry, entry.getTree().getNodes().size());

		Collections.sort(entries, new Comparator<GrammarEntry>()
		{
			@Override
			public int compare(GrammarEntry arg0, GrammarEntry arg1)
			{
				return treeSizes.get(arg0).compareTo(treeSizes.get(arg1));
			}
		});
	}


	/**
	 * Sorts the given list of entries by their family names.
	 * @param entries
	 */
	public static void sortByFamilies(List<GrammarEntry> entries)
	{
		Collections.sort(entries, new Comparator<GrammarEntry>()
		{
			@Override
			public int compare(GrammarEntry arg0, GrammarEntry arg1)
			{
				return arg0.getFamily().compareTo(arg1.getFamily());
			}
		});
	}


	/**
	 * Sorts the given list of entries by their family names and tree size.
	 * @param entries
	 */
	public static void sortByFamiliesAndTreeSize(List<GrammarEntry> entries)
	{
		final Map<GrammarEntry, Integer> treeSizes = new HashMap<GrammarEntry, Integer>();
		for(GrammarEntry entry : entries)
			treeSizes.put(entry, entry.getTree().getNodes().size());

		Collections.sort(entries, new Comparator<GrammarEntry>()
		{
			@Override
			public int compare(GrammarEntry arg0, GrammarEntry arg1)
			{
				int fam = arg0.getFamily().compareTo(arg1.getFamily());
				if (fam == 0)
					return treeSizes.get(arg0).compareTo(treeSizes.get(arg1));
				else return fam;

			}
		});
	}


	/**
	 * Returns the set of unique families given a collection of entries.
	 * @param entries
	 * @return a set of families names
	 */
	public static Set<String> getFamilies(Collection<GrammarEntry> entries)
	{
		Set<String> ret = new HashSet<String>();
		for(GrammarEntry entry : entries)
			ret.add(entry.getFamily());
		return ret;
	}


	/**
	 * Returns the set of entries among given entries that do not have semantics.
	 * @param entries
	 * @return a set of entries with an empty semantics
	 */
	public static GrammarEntries getEmptySemanticsEntries(Collection<GrammarEntry> entries)
	{
		GrammarEntries ret = new GrammarEntries();
		for(GrammarEntry entry : entries)
			if (entry.getSemantics().isEmpty())
				ret.add(entry);
		return ret;
	}


	/**
	 * Creates a new empty Grammar.
	 */
	public Grammar()
	{
		this.families = new HashMap<String, Set<GrammarEntry>>();
	}


	/**
	 * Adds the given entry to this Grammar. If the grammar already contains an entry with the same
	 * name, it is overriden.
	 * @param entry
	 */
	public void add(GrammarEntry entry)
	{
		put(entry.getName(), entry);
	}


	/**
	 * Returns all the entries of this Grammar. Warning: this method creates a new GrammarEntries
	 * instance, prefer this.values().
	 * @return entries of this Grammar
	 */
	public GrammarEntries getEntries()
	{
		return new GrammarEntries(values());
	}


	/**
	 * Returns the entries that belong to the given families.
	 * @param families
	 * @return a set of entries, empty set if no entries are found in the given family.
	 */
	public GrammarEntries getEntriesByFamilies(String... families)
	{
		GrammarEntries ret = new GrammarEntries();
		for(String family : families)
			if (this.families.containsKey(family))
				ret.addAll(this.families.get(family));
		return ret;
	}


	/**
	 * Returns the entries that are anchored by given category.
	 * @param category
	 * @return a set of entries
	 */
	public GrammarEntries getEntriesByAnchorCategory(FeatureConstant category)
	{
		return getEntriesByAnchorCategory(values(), category);
	}


	/**
	 * Returns the entries from the given entries that are anchored by given category.
	 * @param entries
	 * @param category
	 * @return a set of entries
	 */
	public static GrammarEntries getEntriesByAnchorCategory(Collection<GrammarEntry> entries, FeatureConstant category)
	{
		GrammarEntries ret = new GrammarEntries();
		for(GrammarEntry entry : entries)
			if (entry.getTree().getMainAnchor().hasCategory(category))
				ret.add(entry);
		return ret;
	}


	/**
	 * Returns the entries of this Grammar that adjunct on the given category.
	 * @param category
	 * @return a set of entries
	 */
	public GrammarEntries getEntriesByFootCategory(FeatureConstant category)
	{
		return getEntriesByFootCategory(values(), category);
	}


	/**
	 * Returns the entries from the given entries that adjunct on the given category.
	 * @param entries
	 * @param category
	 * @return a set of entries
	 */
	public static GrammarEntries getEntriesByFootCategory(Collection<GrammarEntry> entries, FeatureConstant category)
	{
		GrammarEntries ret = new GrammarEntries();
		for(GrammarEntry entry : entries)
		{
			Node foot = entry.getTree().getFoot();
			if (foot != null && foot.hasCategory(category))
				ret.add(entry);
		}
		return ret;
	}


	/**
	 * Returns the auxiliaries entries from this Grammar given the foot category and the position of
	 * foot with regards to anchor.
	 * @param category
	 * @param footType
	 * @return a set of entries
	 */
	public GrammarEntries getEntriesByFoot(FeatureConstant category, FootType footType)
	{
		return getEntriesByFoot(values(), category, footType);
	}


	/**
	 * Returns the auxiliaries entries from the given entries given the foot category and the
	 * position of foot with regards to anchor.
	 * @param entries
	 * @param category
	 * @param footType
	 * @return a set of entries
	 */
	public static GrammarEntries getEntriesByFoot(Collection<GrammarEntry> entries, FeatureConstant category, FootType footType)
	{
		GrammarEntries ret = new GrammarEntries();
		for(GrammarEntry entry : entries)
		{
			Node foot = entry.getTree().getFoot();
			if (foot != null && foot.hasCategory(category))
			{
				Node anchor = entry.getTree().getMainAnchor();
				GornAddress footAddress = GornAddress.getAddress(foot, entry.getTree());
				GornAddress anchorAddress = GornAddress.getAddress(anchor, entry.getTree());
				boolean leftOf = footAddress.isLeftOf(anchorAddress);
				if (footType == FootType.LEFT && leftOf)
					ret.add(entry);
				if (footType == FootType.RIGHT && !leftOf)
					ret.add(entry);
			}
		}
		return ret;
	}


	/**
	 * Returns the entries whose trace contains entirely the given trace.
	 * @param trace
	 * @return a set of entries
	 */
	public GrammarEntries getEntriesContainingTrace(Trace trace)
	{
		return getEntriesContainingTrace(values(), trace);
	}


	/**
	 * Returns the entries from the given entries whose trace contains entirely the given trace.
	 * @param entries
	 * @param trace
	 * @return a set of entries
	 */
	public static GrammarEntries getEntriesContainingTrace(Collection<GrammarEntry> entries, Trace trace)
	{
		GrammarEntries ret = new GrammarEntries();
		for(GrammarEntry entry : entries)
			if (entry.getTrace().containsAll(trace))
				ret.add(entry);
		return ret;
	}


	/**
	 * Returns the whole set of categories used by this Grammar. Disjunctive categories are exploded
	 * and returned as strings.
	 * @return a set of strings
	 */
	public Set<String> getAllCategories()
	{
		Set<String> ret = new HashSet<String>();
		for(GrammarEntry entry : values())
			for(Node node : entry.getTree().getNodes())
				ret.addAll(node.getCategory().getValues());
		return ret;
	}
	
	public Map<String, Set<GrammarEntry>> getFamilies()
	{
		return families;
	}


	/**
	 * Computes the families cache.
	 */
	public void computeFamiliesCache()
	{
		for(GrammarEntry entry : values())
		{
			if (!families.containsKey(entry.getFamily()))
				families.put(entry.getFamily(), new HashSet<GrammarEntry>());
			families.get(entry.getFamily()).add(entry);
		}
	}
}
