package synalp.generation.jeni.filtering;

import java.util.*;

import synalp.commons.grammar.*;
import synalp.commons.unification.*;


/**
 * Simple cat-based polarity key.
 * @author Alexandre Denis
 */
public class PolarityKey
{
	private FeatureConstant cat;


	/**
	 * Retrieves all the possible polarity keys from the given entries. It looks at substitution
	 * node and root node of initial trees.
	 * @param entries
	 * @return a set of keys
	 */
	public static Set<PolarityKey> retrieveKeys(Collection<GrammarEntry> entries)
	{
		Set<PolarityKey> ret = new HashSet<PolarityKey>();
		for(GrammarEntry entry : entries)
		{
			Tree tree = entry.getTree();
			if (!tree.isAuxiliary())
				ret.add(new PolarityKey(tree.getRoot().getCategory()));
			for(Node subst : tree.getSubstitutions())
				for(FeatureConstant constant : subst.getCategory().flattenDisjunctions())
					ret.add(new PolarityKey(constant));
		}
		return ret;
	}


	/**
	 * Creates a new PolarityKey with given category.
	 * @param cat
	 */
	public PolarityKey(FeatureConstant cat)
	{
		this.cat = cat;
	}


	/**
	 * @return the cat
	 */
	public FeatureConstant getCat()
	{
		return cat;
	}

	
	/**
	 * Tests if this PolarityKey is more specific than given PolarityKey.
	 * That is, all the values of this PolarityKey are contained in the given PolarityKey values.
	 * @param key 
	 * @return  true if all values of this key categories are contained in the given key categories
	 */
	public boolean isMoreSpecific(PolarityKey key)
	{
		return key.getCat().getValues().containsAll(cat.getValues());
	}
	

	/**
	 * Tests if this PolarityKey matches the given Node.
	 * @param node
	 * @return true if the category matches the node category.
	 */
	public boolean matchesNode(Node node)
	{
		return node.hasCategory(cat);
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cat == null) ? 0 : cat.hashCode());
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
		PolarityKey other = (PolarityKey) obj;
		if (cat == null)
		{
			if (other.cat != null)
				return false;
		}
		else if (!cat.equals(other.cat))
			return false;
		return true;
	}


	@Override
	public String toString()
	{
		return "cat:" + cat;
	}
}
