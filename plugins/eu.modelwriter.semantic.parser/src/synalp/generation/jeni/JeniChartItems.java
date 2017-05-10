package synalp.generation.jeni;

import java.util.*;

import synalp.commons.grammar.*;
import synalp.commons.semantics.Semantics;
import synalp.commons.unification.*;
import synalp.commons.utils.Utils;
import synalp.generation.*;
import synalp.generation.configuration.GeneratorOption;

/**
 * A list of chart items, it mainly checks for duplicates. The methods used for comparison are
 * stored in this class since they can be quite specific and prone to optimizations. We could have
 * put them in their respective classes but we do not want to spread the notion of equality where it
 * is not relevant (and could lead to unexpected behaviours).
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class JeniChartItems extends ArrayList<JeniChartItem>
{
	/**
	 * Empty constructor.
	 */
	public JeniChartItems()
	{

	}


	/**
	 * Shallow copy the given items.
	 * @param itemsSet
	 */
	public JeniChartItems(JeniChartItems... itemsSet)
	{
		for(JeniChartItems items : itemsSet)
			addAll(items);
	}


	/**
	 * Shallow copy the given items, assuming they are JeniChartItems.
	 * @param items
	 */
	public JeniChartItems(List<? extends ChartItem> items)
	{
		for(ChartItem item : items)
			add((JeniChartItem) item);
	}


	/**
	 * Removes the first element of this List. We could of course do remove(0), but this method
	 * could be used in case ChartItems is a set.
	 * @return a ChartItem or null if the list is empty
	 */
	public JeniChartItem removeFirst()
	{
		if (isEmpty())
			return null;
		Iterator<JeniChartItem> it = iterator();
		JeniChartItem ret = it.next();
		it.remove();
		return ret;
	}


	@Override
	public boolean addAll(Collection<? extends JeniChartItem> items)
	{
		for(JeniChartItem item : items)
			add(item);
		return true;
	}


	/**
	 * Adds the item without considering wether there exists a doublon.
	 * @param item
	 * @return true as specified by super.add
	 */
	public boolean addDoublons(JeniChartItem item)
	{
		return super.add(item);
	}


	@Override
	public boolean add(JeniChartItem item)
	{
		//System.out.println("GeneratorOption.ALLOW_DUPLICATES is "+GeneratorOption.ALLOW_DUPLICATES);
		
		if (GeneratorOption.ALLOW_DUPLICATES)
			return super.add(item);
		else
		{
			boolean found = false;
			for(JeniChartItem thisItem : this)
				if (equals(thisItem, item))
				{
					found = true;
					break;
				}
			/*if (thisItem.getDerivation().equals(item.getDerivation()))
			{
				found = true;
				break;
			}*/
			if (found)
				return false;
			else return super.add(item);
		}
	}


	/**
	 * Returns the first found ChartItem whose tree would alter the idx. It is used in early
	 * semantic failure.
	 * @param cat
	 * @return a ChartItem of this list or null if none is found
	 */
	public JeniChartItem getIdxAlteringItem(FeatureConstant cat)
	{
		for(JeniChartItem item : this)
			if (item.isIdxAltering(cat))
				return item;
		return null;
	}


	/**
	 * Returns JeniRealizations from these items.
	 * @return realizations
	 */
	public List<JeniRealization> getRealizations()
	{
		List<JeniRealization> ret = new ArrayList<JeniRealization>();
		for(JeniChartItem item : this)
		{
			/*JeniRealization real = new JeniRealization(item.getDerivation(), item.getContext());
			real.setProbability(item.getProbability());
			real.addAll(item.getTree().getLemmas());
			ret.add(real);*/
			ret.add(item.getRealization());
		}
		return ret;
	}


	/**
	 * Returns a String representation of the lemmas of these chart items.
	 * @return a String
	 */
	public String toStringLemmas()
	{
		StringBuilder ret = new StringBuilder();
		for(ChartItem item : this)
			ret.append(Utils.print(item.getLemmas(), " ")).append("; ");
		return ret.toString().trim();
	}


	/**
	 * Tests chart item equality.
	 * @param item1
	 * @param item2
	 * @return true if the items are equal
	 */
	public static boolean equals(JeniChartItem item1, JeniChartItem item2)
	{
		return equals(item1.getTree(), item2.getTree(), item1.getContext(), item2.getContext());
	}


	/**
	 * Tests tree equality.
	 * @param tree1
	 * @param tree2
	 * @param context1
	 * @param context2
	 * @return true if the trees are equal
	 */
	public static boolean equals(Tree tree1, Tree tree2, InstantiationContext context1, InstantiationContext context2)
	{
		return equals(tree1.getRoot(), tree2.getRoot(), context1, context2);
	}


	/**
	 * Tests node equality.
	 * @param node1
	 * @param node2
	 * @param context1
	 * @param context2
	 * @return true if the nodes are equal
	 */
	public static boolean equals(Node node1, Node node2, InstantiationContext context1, InstantiationContext context2)
	{
		if (node1.getType() != node2.getType())
			return false;
		if (node1.getAnchorLemma() == null && node2.getAnchorLemma() != null)
			return false;
		if (node1.getAnchorLemma() != null && node2.getAnchorLemma() == null)
			return false;
		if (node1.getAnchorLemma() != null && node2.getAnchorLemma() != null && !node1.getAnchorLemma().equals(node2.getAnchorLemma()))
			return false;
		if (node1.isAnchoredInGrammar() != node2.isAnchoredInGrammar())
			return false;
		if (node1.isNoAdjunction() != node2.isNoAdjunction())
			return false;
		if (!node1.getCategory().equals(node2.getCategory()))
			return false;
		if (!equals(node1.getFsTop(), node2.getFsTop(), context1, context2))
			return false;
		if (!equals(node1.getFsBot(), node2.getFsBot(), context1, context2))
			return false;

		List<Node> children1 = node1.getChildren();
		List<Node> children2 = node2.getChildren();
		if (children1.size() != children2.size())
			return false;
		for(int i = 0; i < children1.size(); i++)
			if (!equals(children1.get(i), children2.get(i), context1, context2))
				return false;

		return true;
	}


	/**
	 * Tests fs equality.
	 * @param fs1
	 * @param fs2
	 * @param context1
	 * @param context2
	 * @return true if the fs are equal
	 */
	public static boolean equals(FeatureStructure fs1, FeatureStructure fs2, InstantiationContext context1, InstantiationContext context2)
	{
		if (!equalsDirected(fs1, fs2, context1, context2))
			return false;
		if (!equalsDirected(fs2, fs1, context2, context1))
			return false;
		return true;
	}


	/**
	 * Tests equality of fs1 and fs2 from fs1 point of view. It does not test for feature variable
	 * mismatch, only instantiations.
	 * @param fs1
	 * @param fs2
	 * @param context1
	 * @param context2
	 * @return false if fs1 is not equal to fs2
	 */
	private static boolean equalsDirected(FeatureStructure fs1, FeatureStructure fs2, InstantiationContext context1, InstantiationContext context2)
	{
		for(Feature feat1 : fs1.getFeatures())
		{
			if (!fs2.hasFeature(feat1.getName()))
				return false;
			else
			{
				FeatureValue val1 = context1.getValue(feat1.getValue());
				FeatureValue val2 = context2.getValue(fs2.getValueOf(feat1.getName()));

				if (val1 instanceof FeatureVariable && !(val2 instanceof FeatureVariable))
					return false;
				if (!(val1 instanceof FeatureVariable) && val2 instanceof FeatureVariable)
					return false;
				if (!(val1 instanceof FeatureVariable) && !(val2 instanceof FeatureVariable))
					if (!val1.equals(val2))
						return false;
			}
		}

		return true;
	}

	public Semantics getSemantics() {
		Semantics ret = new Semantics(); 
		for (JeniChartItem item:this) {
			ret.addAll(item.getSemantics());
		}
		return ret;
	}
}
