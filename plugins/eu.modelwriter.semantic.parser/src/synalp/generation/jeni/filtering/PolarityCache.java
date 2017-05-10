package synalp.generation.jeni.filtering;

import java.util.*;

import synalp.commons.grammar.*;


/**
 * A PolarityCache caches the polarity charge for each tree and key.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class PolarityCache extends HashMap<Tree, PolarityKeys>
{
	/**
	 * Creates a new polarity cache from given entries and keys.
	 * @param entries
	 * @param keys
	 */
	public PolarityCache(Collection<GrammarEntry> entries, Collection<PolarityKey> keys)
	{
		for(GrammarEntry entry : entries)
			for(PolarityKey key : keys)
				cachePolarity(entry.getTree(), key);
	}

	
	/**
	 * @param tree
	 * @param key
	 */
	private void cachePolarity(Tree tree, PolarityKey key)
	{
		cachePolarity(tree, key, getPolarity(tree, key));
	}
	
	
	/**
	 * @param tree
	 * @param key
	 * @param polarity 
	 */
	private void cachePolarity(Tree tree, PolarityKey key, Interval polarity)
	{
		if (!containsKey(tree))
			put(tree, new PolarityKeys());
		get(tree).put(key, polarity);
	}
	
	
	/**
	 * Returns the polarity of given Tree with given key.
	 * If it is not in the cache yet, add it to the cache.
	 * @param tree
	 * @param key
	 * @return the polarity of given Tree
	 */
	public Interval getPolarity(Tree tree, PolarityKey key)
	{
		if (containsKey(tree) && get(tree).containsKey(key))
			return get(tree).get(key);
		
		Interval ret = new Interval();
		for(Node node : tree.getNodes())
			ret.addLocal(getPolarity(node, tree, key));
		
		cachePolarity(tree, key, ret);
		
		return ret;
	}


	/**
	 * Returns the polarity of given Node in given Tree with given key.
	 * @param node
	 * @param tree
	 * @param key
	 * @return the polarity of given node
	 */
	private static Interval getPolarity(Node node, Tree tree, PolarityKey key)
	{
		if (key.matchesNode(node))
		{
			if (node.getType() == NodeType.SUBST)
			{
				if (node.getCategory().getValues().equals(key.getCat().getValues()))
					return new Interval(-1,-1);
				else return new Interval(-1,0);
			}
			else if (node.isRoot() && !tree.isAuxiliary())
			{
				if (node.getCategory().getValues().equals(key.getCat().getValues()))
					return new Interval(1,1);
				else return new Interval(0,1);
			}
		}

		return new Interval(0,0);
	}
}
