package synalp.generation.jeni.filtering;

import java.util.*;

import synalp.commons.grammar.*;
import synalp.commons.semantics.DefaultLiteral;


/**
 * A PolarityContext is a data structure that gathers the literals to produce, the grammar entries
 * and the polarity cache. It also contains few helper methods.
 * @author Alexandre Denis
 */
public class PolarityContext
{
	private final PolarityKey rootKey;
	private final Set<PolarityKey> allKeys;
	private final PolarityCache polarityCache;
	private final List<DefaultLiteral> literals;
	private final Map<String, GrammarEntry> entries; // <entry name, entry>


	/**
	 * @param rootKey
	 * @param literals
	 * @param entries
	 */
	public PolarityContext(PolarityKey rootKey, List<DefaultLiteral> literals, Set<GrammarEntry> entries)
	{
		this.rootKey = rootKey;
		this.entries = createEntryMap(entries);
		this.literals = literals;
		this.allKeys = PolarityKey.retrieveKeys(entries);
		this.polarityCache = new PolarityCache(entries, allKeys);
	}


	/**
	 * Creates a map of entries by name.
	 * @param entries
	 * @return a map of entries by name.
	 */
	private Map<String, GrammarEntry> createEntryMap(Set<GrammarEntry> entries)
	{
		Map<String, GrammarEntry> ret = new HashMap<String, GrammarEntry>();
		for(GrammarEntry entry : entries)
			ret.put(entry.getName(), entry);
		return ret;
	}

	
	
	/**
	 * Tests if the given collection of entries has a zero overall polarity with respect to the root
	 * key. The given entries must be the instances that were provided in the constructor.
	 * @param entries
	 * @return true if the given collection of entries sums to zero polarity.
	 */
	public boolean isZeroPolarity(Collection<GrammarEntry> entries)
	{
		if (rootKey.getCat().isDisjunction())
		{
			// if the root is a disjunction, we need to find a match
			PolarityKeys keys = new PolarityKeys();
			for(GrammarEntry entry : entries)
				keys.incrementAll(polarityCache.get(entry.getTree()));
			return keys.matches(rootKey, new Interval(-1,-1));
		}
		else
		{
			// if it is not a disjunction we can do it faster by counting
			PolarityKeys keys = new PolarityKeys();
			keys.put(rootKey, new Interval(-1,-1));
			for(GrammarEntry entry : entries)
				keys.incrementAll(polarityCache.get(entry.getTree()));
			return keys.isZero();
		}
	}


	/**
	 * Returns the expected number of literals.
	 * @return the number of literals
	 */
	public int getNumberOfLiterals()
	{
		return literals.size();
	}


	/**
	 * Creates all the literals indexes greater than given literal index that are covered by the
	 * given entry semantics. These are literals that need to be covered in the next states.
	 * @param literalIndex
	 * @param entry
	 * @return a set of indexes
	 */
	public Set<Integer> createExtraLiterals(int literalIndex, GrammarEntry entry)
	{
		Set<Integer> ret = new HashSet<Integer>();
		for(int i = literalIndex + 1; i < literals.size(); i++)
		{
			DefaultLiteral candidate = literals.get(i);
			if (entry.getSemantics().contains(candidate, entry.getContext()))
				ret.add(i);
		}
		return ret;
	}


	/**
	 * Returns the polarity of the entry tree with given key. It makes use of the cache.
	 * @param entry
	 * @param key
	 * @return a polarity
	 */
	public Interval getPolarity(GrammarEntry entry, PolarityKey key)
	{
		return polarityCache.getPolarity(entry.getTree(), key);
	}
	
	
	/**
	 * Returns all the keys for given entry.
	 * @param entry 
	 * @return keys
	 */
	public PolarityKeys getPolarity(GrammarEntry entry)
	{
		return polarityCache.get(entry.getTree());
	}


	/**
	 * Tests if the given key is the root key.
	 * @param key
	 * @return whether the key is the root key
	 */
	public boolean isRootKey(PolarityKey key)
	{
		return key.equals(rootKey);
	}


	/**
	 * @return the allKeys
	 */
	public Set<PolarityKey> getAllKeys()
	{
		return allKeys;
	}


	/**
	 * @return the rootKey
	 */
	public PolarityKey getRootKey()
	{
		return rootKey;
	}


	/**
	 * @return the literals
	 */
	public List<DefaultLiteral> getLiterals()
	{
		return literals;
	}


	/**
	 * @param i
	 * @return a literal
	 */
	public DefaultLiteral getLiteral(int i)
	{
		return literals.get(i);
	}


	/**
	 * @return the entries
	 */
	public Collection<GrammarEntry> getEntries()
	{
		return entries.values();
	}


	/**
	 * Returns the entry by its name.
	 * @param name
	 * @return null if not found.
	 */
	public GrammarEntry getEntryByName(String name)
	{
		return entries.get(name);
	}
	

	/**
	 * Returns the entries by their names.
	 * @param names
	 * @return a list of entries
	 */
	public List<GrammarEntry> getEntriesByNames(Collection<String> names)
	{
		List<GrammarEntry> ret = new ArrayList<GrammarEntry>();
		for(String name : names)
			ret.add(getEntryByName(name));
		return ret;
	}


	/**
	 * @return the polarityCache
	 */
	public PolarityCache getPolarityCache()
	{
		return polarityCache;
	}


	/**
	 * Returns the entries that are subsumed by given literal.
	 * @param index
	 * @return entries
	 */
	public Set<GrammarEntry> getEntriesByLiteral(int index)
	{
		Set<GrammarEntry> ret = new HashSet<GrammarEntry>();
		DefaultLiteral literal = getLiteral(index);
		for(GrammarEntry entry : entries.values())
			if (entry.getSemantics().contains(literal, entry.getContext()))
				ret.add(entry);
		return ret;
	}


	/**
	 * Returns the entries that have no semantics.
	 * @return set of entries with empty semantics
	 */
	public Set<GrammarEntry> getEmptySemanticsEntries()
	{
		return Grammar.getEmptySemanticsEntries(entries.values());
	}
}
