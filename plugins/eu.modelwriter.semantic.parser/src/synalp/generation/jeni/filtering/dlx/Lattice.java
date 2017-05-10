package synalp.generation.jeni.filtering.dlx;

import java.util.*;

import com.google.common.collect.*;

/**
 * While reasonably fast, it is still too slow to be useful.
 * @author Alexandre Denis
 *
 */
public class Lattice
{
	private List<String> elements;
	private Set<Set<String>> originalObjects;
	private Multimap<BitSet, BitSet> paths = LinkedHashMultimap.create();
	private Multimap<BitSet, BitSet> relation = HashMultimap.create();


	/**
	 * @param objects
	 */
	public Lattice(Set<Set<String>> objects)
	{
		this.originalObjects = objects;
		this.elements = gatherElements(objects);
	}

	
	/**
	 * 
	 */
	public void expandLayers()
	{
		addLayers(createAllBitSets(originalObjects));
	}
	

	/**
	 * Recursively add layers.
	 * @param objects at the n-th layer
	 */
	private void addLayers(Set<BitSet> objects)
	{
		System.out.println("Layer size "+objects.size());
		if (objects.isEmpty())
			return;

		int size = objects.size();
		Set<BitSet> results = new HashSet<BitSet>();
		List<BitSet> ordered = new ArrayList<BitSet>(objects);
		for(int i = 0; i < size; i++)
			for(int j = i + 1; j < size; j++)
			{
				BitSet bi = ordered.get(i);
				BitSet bj = ordered.get(j);

				BitSet inter = (BitSet) bi.clone();
				inter.and(bj);
				if (!inter.isEmpty() && !inter.equals(bi) && !inter.equals(bj))
				{
					relation.put(inter, bi);
					relation.put(inter, bj);
					paths.put(bi, inter);
					paths.put(bj, inter);
					results.add(inter);
				}
			}
		
		
		addLayers(results);
	}


	private Set<BitSet> createAllBitSets(Set<Set<String>> objects)
	{
		Set<BitSet> ret = new HashSet<BitSet>();
		for(Set<String> object : objects)
			ret.add(createBitSet(object));
		return ret;
	}


	private BitSet createBitSet(Set<String> object)
	{
		BitSet ret = new BitSet(elements.size());
		for(int i = 0; i < elements.size(); i++)
			if (object.contains(elements.get(i)))
				ret.set(i);
		return ret;
	}


	private Set<String> getElement(BitSet bitset)
	{
		Set<String> ret = new HashSet<String>();
		for(int i = 0; i < elements.size(); i++)
			if (bitset.get(i))
				ret.add(elements.get(i));
		return ret;
	}


	private static List<String> gatherElements(Set<Set<String>> objects)
	{
		Set<String> ret = new HashSet<String>();
		for(Set<String> set : objects)
			ret.addAll(set);
		return new ArrayList<String>(ret);
	}


	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();

		List<BitSet> ordered = new ArrayList<BitSet>(relation.keySet());
		Collections.sort(ordered, new Comparator<BitSet>()
		{
			public int compare(BitSet o1, BitSet o2)
			{
				return o1.cardinality() - o2.cardinality();
			}
		});

		for(BitSet key : ordered)
		{
			Set<String> keyStr = getElement(key);
			ret.append(keyStr).append(" -> ");
			for(BitSet value : relation.get(key))
			{
				Set<String> valStr = getElement(value);
				valStr.removeAll(keyStr);
				ret.append("+").append(valStr).append(" ");
			}
			ret.append("\n");
		}
		
		ret.append("\nPATHS\n");
		for(BitSet key : paths.keySet())
		{
			Set<String> keyStr = getElement(key);
			ret.append(keyStr).append(" -> ");
			for(BitSet value : paths.get(key))
			{
				Set<String> valStr = getElement(value);
				//valStr.removeAll(keyStr);
				ret.append("+").append(valStr).append(" ");
			}
			ret.append("\n");
		}
		
		return ret.toString().trim();
	}
}
