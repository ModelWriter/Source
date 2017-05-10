package synalp.generation.ranker;

import java.util.List;

import synalp.generation.*;

/**
 * A Ranker ranks chart items.
 * @author apoorvi
 */
public interface Ranker
{
	/**
	 * Returns the given chart items in another order. Note: this method may return
	 * the whole list of items reordered, but also a subset of items.
	 * @param items
	 * @return ranked items
	 */
	public List<? extends ChartItem> rank(List<? extends ChartItem> items);
}
