package synalp.generation.probabilistic;

import java.util.*;

import synalp.generation.ChartItem;
import synalp.generation.configuration.GeneratorOption;
import synalp.generation.ranker.Ranker;

/**
 * @aluna
 */
public class ProbabilisticRanker implements Ranker
{

	/**
	 * @param items: a list of elements that are an implementation of ChartItem
	 * Ranks the element of the list using probabilities 
	 * @return a list of CharItem elements with the higher probability 
	 */
	@Override
	public List<? extends ChartItem> rank(List<? extends ChartItem> items)
	{
		// order JeniChartItem list in ascending order
		/*Collections.sort(items, (item1, item2) -> (int) (item2.getProbability() - item1.getProbability()));
		int maxItems = (beamWidth > items.size()) ? items.size() - 1
				: beamWidth - 1;
		return items.subList(0, maxItems);*/
		
		Collections.sort(items, Comparator.comparing(ChartItem::getProbability).reversed());
	
		int index=0;
		float proba=Float.MAX_VALUE;
		int nbOfDiffBeamSizes=0;
		for(index=0; index<items.size(); index++)
		{
			if (items.get(index).getProbability()<proba)
				nbOfDiffBeamSizes++;
			if (nbOfDiffBeamSizes > GeneratorOption.BEAM_SIZE)
				return items.subList(0, index);
			proba = items.get(index).getProbability();
		}
		
		//return items;
		int maxItems = (GeneratorOption.BEAM_SIZE > items.size()) ? items.size()
				: GeneratorOption.BEAM_SIZE;
		return getSubList(items,0,maxItems);
		//return items.subList(0, maxItems);
	}
	
	private ArrayList<ChartItem> getSubList(List<? extends ChartItem> items, int startIndex, int endIndex) { //endIndex is exclusive
		ArrayList<ChartItem> ret = new ArrayList<ChartItem>(); 
		for (int i=startIndex;i<endIndex;i++) {
			ret.add(items.get(i));
		}
		return ret;
	}
}
