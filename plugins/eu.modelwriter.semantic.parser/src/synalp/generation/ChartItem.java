package synalp.generation;

import java.util.List;

import synalp.commons.input.Lemma;


/**
 * A ChartItem is a temporary object created during the generation process that monitors the
 * realization of a sentence fragment. It only contains so far the lemmas of the currently built
 * sentence fragment.
 * @author Alexandre Denis
 */
public interface ChartItem
{
	
	/**
	 * Returns the lemmas that this ChartItem covers.
	 * @return an ordered list of lemmas.
	 */
	public List<Lemma> getLemmas();
	
	/**
	 * @return the probability of the ChartItem
	 */
	public float getProbability();
}
