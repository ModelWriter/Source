package synalp.generation.jeni.filtering.dlx;

import java.util.*;

import synalp.commons.grammar.*;
import synalp.commons.semantics.Semantics;
import synalp.generation.jeni.filtering.*;

import com.google.common.collect.*;


/**
 * An EntryPack is a GrammarEntries object with an identifier and a representative element.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class EntryPack extends GrammarEntries
{
	private String id;
	private GrammarEntry representative;

	
	/**
	 * Unpacks the given list of groups. It does the cartesian product of each group.
	 * @param packs
	 * @return a set of GrammarEntries
	 */
	public static Set<GrammarEntries> unpack(List<EntryPack> packs)
	{
		Set<GrammarEntries> ret = new HashSet<GrammarEntries>();
		for(List<GrammarEntry> entries : Sets.cartesianProduct(packs))
			ret.add(new GrammarEntries(entries));
		return ret;
	}
	

	/**
	 * Creates entry packs gathering entries with the same semantis and polarity keys. Warning: it
	 * only works with BitSemantics because of the hashcode method of the semantics.
	 * @param context
	 * @return a map of packs with their ids
	 */
	public static Map<String, EntryPack> createPacks(PolarityContext context)
	{
		return createPacksSemOnly(context);
	}
	
	
	/**
	 * Creates packs based on semantics only.
	 * @param context
	 * @return packs with their id
	 */
	public static Map<String, EntryPack> createPacksSemOnly(PolarityContext context)
	{
		Map<Semantics, GrammarEntries> table = new HashMap<Semantics, GrammarEntries>();
		for(GrammarEntry entry : context.getEntries())
		{
			Semantics sem = entry.getSemantics();
			if (!table.containsKey(sem))
				table.put(sem, new GrammarEntries());
			table.get(sem).add(entry);
		}

		int currentId = 0;
		Map<String, EntryPack> ret = new HashMap<String, EntryPack>();
		for(GrammarEntries entries : table.values())
		{
			String id = "G" + (currentId++);
			ret.put(id, new EntryPack(id, entries));
		}

		return ret;
	}
	
	
	/**
	 * Creates packs based on semantics and keys.
	 * @param context
	 * @return packs with their id
	 */
	public static Map<String, EntryPack> createPacksSemKey(PolarityContext context)
	{
		Table<Semantics, PolarityKeys, GrammarEntries> table = HashBasedTable.create();
		for(GrammarEntry entry : context.getEntries())
		{
			Semantics sem = entry.getSemantics();
			PolarityKeys keys = context.getPolarity(entry);
			if (!table.contains(sem, keys))
				table.put(sem, keys, new GrammarEntries());
			table.get(sem, keys).add(entry);
		}

		int currentId = 0;
		Map<String, EntryPack> ret = new HashMap<String, EntryPack>();
		for(GrammarEntries entries : table.values())
		{
			String id = "G" + (currentId++);
			ret.put(id, new EntryPack(id, entries));
		}

		return ret;
	}


	/**
	 * Creates a new EntryGroup with given id and entries. The given entries must not be empty.
	 * @param id
	 * @param entries
	 */
	public EntryPack(String id, GrammarEntries entries)
	{
		super(entries);
		this.id = id;
		this.representative = entries.iterator().next();
	}


	/**
	 * Returns the representative element.
	 * @return the representative
	 */
	public GrammarEntry getRepresentative()
	{
		return representative;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}
	
	
	/**
	 * @param context
	 * @return entries
	 */
	public Collection<GrammarEntries> getSubEntriesPerKey(PolarityContext context)
	{
		Map<PolarityKeys, GrammarEntries> table = new HashMap<PolarityKeys, GrammarEntries>();
		for(GrammarEntry entry : this)
		{
			PolarityKeys keys = context.getPolarity(entry);
			if (!table.containsKey(keys))
				table.put(keys, new GrammarEntries());
			table.get(keys).add(entry);
		}
		return table.values(); 
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		EntryPack other = (EntryPack) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}
}
