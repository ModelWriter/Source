package synalp.generation.ranker;

import java.util.List;

import synalp.generation.ChartItem;


/**
 * @author apoorvi
 */
public class DefaultRanker implements Ranker
{

	@Override
	public List<? extends ChartItem> rank(List<? extends ChartItem> items)
	{

		return items;
	}

}
