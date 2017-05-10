package synalp.generation.probabilistic;

import synalp.generation.jeni.JeniChartItem;

/**
 * A general interface to define the probability of a chart item.
 * @author Alexandre Denis
 */
public interface ProbabilityStrategy
{
	/**
	 * A type for the strategy.
	 * @author Alexandre Denis
	 *
	 */
	public enum StrategyType
	{
		DEFAULT,
		SIMPLE
	}
	
	
	/**
	 * Returns the probability of given chart item according to this strategy.
	 * @param item
	 */
	public float getProbability(JeniChartItem item);
}
