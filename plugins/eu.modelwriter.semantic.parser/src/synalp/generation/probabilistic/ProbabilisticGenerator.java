package synalp.generation.probabilistic;

import java.util.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import org.apache.log4j.Logger;

import synalp.commons.derivations.DerivationTree;
import synalp.commons.output.MorphRealization;
import synalp.commons.output.SyntacticRealization;
import synalp.commons.semantics.Semantics;
import synalp.commons.unification.InstantiationContext;
import synalp.commons.utils.Utils;
import synalp.generation.ChartItem;
import synalp.generation.configuration.*;
import synalp.generation.jeni.*;

/**
 * A ProbabilisticGenerator performs generation iteratively by constructing derivation trees with
 * increasing size.
 * @author Alexandre Denis
 */
public class ProbabilisticGenerator extends JeniGenerator
{
	private static Logger logger = Logger.getLogger(ProbabilisticGenerator.class);


	/**
	 * Creates a new ProbabilisticConfigurator based on given configuration.
	 * @param config
	 */
	public ProbabilisticGenerator(GeneratorConfiguration config)
	{
		super(config);
		setRanker(new ProbabilisticRanker());
	}
	
	
	private List<JeniChartItem> getCombinationResults(JeniChartItem it1, JeniChartItem it2) {
		Set<String> usedItems = new HashSet<String>();
		usedItems.addAll(it1.getUsedItemsID());
		usedItems.addAll(it2.getUsedItemsID());
		ArrayList<String> usedItemsID = new ArrayList<String>(usedItems);
		
		JeniChartItems possibleresults = new JeniChartItems();
		
		List<JeniChartItem> newAdjChartItems = getCombiner().getAdjunctionCombinations(it1, it2);
		if (!newAdjChartItems.isEmpty()) {
			for (JeniChartItem newAdjChartItem:newAdjChartItems) {
				newAdjChartItem.addUsedItemsID(usedItemsID);
				possibleresults.add(newAdjChartItem);
			}
		}
		
		// Note that for substition : if item2(or item1), for example, has more than 1 substitution nodes into which item1(or item2) can be substitued,
		// only the first site available site will be chosen. See the 
		// private List<JeniChartItem> getSubstitutionCombinations(JeniChartItem item1, JeniChartItem item2, InstantiationContext context, boolean useEarlyFailure,	JeniChartItems allItems)
		// method in the TreeCombiner class.
		List<JeniChartItem> newSubstChartItems = getCombiner().getSubstitutionCombinations(it1, it2);
		if (!newSubstChartItems.isEmpty()) {
			for (JeniChartItem newSubstChartItem:newSubstChartItems) {
				newSubstChartItem.addUsedItemsID(usedItemsID);
				possibleresults.add(newSubstChartItem); 
			}
		}
		
		return possibleresults;
	}
	
	
	private boolean isSubset(JeniChartItem it1, JeniChartItem it2) {
		boolean ret = false; //default value
		// True if it1 is subset of it2. X is subset of Y when X was derived using one or more constituent of Y.
		ArrayList<String> usedItems_it1 = it1.getUsedItemsID();
		ArrayList<String> usedItems_it2 = it2.getUsedItemsID();
		for (String item:usedItems_it2) {
			if (usedItems_it1.contains(item)) {
				return true;
			}
		}
		return ret;
	}

	/**
	 * Generates the given semantics with a priori list of selected items.
	 * @param semantics
	 * @param agenda
	 * @return chart items, each one being a surface realization
	 */
	@Override
	protected JeniChartItems generate(Semantics semantics, JeniChartItems agenda)
	{
		logger.info("Semantics is "+semantics+"\n\n");
		
		logger.info("Starting agenda is "+agenda);
		
		Map<Integer, JeniChartItems> itemsPerSize = new HashMap<>();
		itemsPerSize.put(1, agenda);
		logger.info("Starting Agenda size: "+agenda.size()+"\n");
		
		// Initialise agenda items to say that they use themsleves.
		for (JeniChartItem item:agenda) {
			item.addUsedItemsID(item.getId());
		}
		
		for(int n = 2; n <= semantics.size(); n++)
		{
			logger.info("Running for size "+n);
			System.out.println("Running for size "+n);
			JeniChartItems tmpItems = new JeniChartItems();
			
			for(int i = 1; i <= Math.floor(n / 2); i++) {
				JeniChartItems ith_elements = itemsPerSize.get(i);
				JeniChartItems jth_elements = itemsPerSize.get(n-i);
				logger.info("Trying to combine row "+i+" with row "+(n-i));
				//System.out.println("Trying to combine row "+i+" with row "+(n-i));

				for(int r=0; r<ith_elements.size();r++) {
					for (int s=0;s<jth_elements.size();s++) {
						if (i==(n-i)) { // happens in case of 1*1, 2*2, 3*3 etc... 
							if (s<=r) { // do not allow to combine self and/or with lower index : (1,3) is the same as (3,1) for Jeni because given (1,3) Jeni looks out for all combinations possible for both (1,3) and (3,1) 
								continue;
							}
							if (isSubset(ith_elements.get(s),ith_elements.get(r))) { // don't try to combine the result obtained for (1,2) with the result obtained for (2,1) or with the results obtained for (1,4)
								continue;
							}
							List<JeniChartItem> results = getCombinationResults(ith_elements.get(s),ith_elements.get(r));
							//logger.info("Tried Combination of "+ith_elements.get(s).getId()+"("+ith_elements.get(s)+")"+"="+ith_elements.get(s).getUsedItemsID()+" with "+ith_elements.get(r).getId()+"("+ith_elements.get(r)+")"+"="+ith_elements.get(r).getUsedItemsID()+" #### Results : "+results);
							tmpItems.addAll(results);
						}
						else {
							if (isSubset(jth_elements.get(s),ith_elements.get(r))) { // don't try to combine the results obtained for (1,2) with 1 or 2.
								continue;
							}
							List<JeniChartItem> results = getCombinationResults(jth_elements.get(s),ith_elements.get(r));
							//logger.info("Tried Combinination of "+jth_elements.get(s).getId()+"("+jth_elements.get(s)+")"+"="+jth_elements.get(s).getUsedItemsID()+" with "+ith_elements.get(r).getId()+"("+ith_elements.get(r)+")"+"="+ith_elements.get(r).getUsedItemsID()+" #### Results : "+results);
							tmpItems.addAll(results);
						}
					} // End of Combination of all elements of ith row with all elements of jth row.
				} // End of looping on elements on ith row.
			}// Looping for the choice of ith row
			
			logger.info("Output Before ranking = "+ tmpItems);
			//System.out.println("Output Before ranking = "+ tmpItems);
			List<JeniChartItem> rankedItems = (List<JeniChartItem>) getRanker().rank(tmpItems);
			logger.info("Output After ranking = "+ rankedItems);
			//System.out.println("Output After ranking = "+ rankedItems);
			
			itemsPerSize.put(n, new JeniChartItems(rankedItems));
			logger.info("Reduced from "+tmpItems.size()+" to "+itemsPerSize.get(n).size() + " by use of beam_size("+GeneratorOption.BEAM_SIZE+") ranker.\n\n");
			System.out.println("Reduced from "+tmpItems.size()+" to "+itemsPerSize.get(n).size() + " by use of beam_size("+GeneratorOption.BEAM_SIZE+") ranker.\n\n");
		} // Looping for the results at combination of n literals from the input
		
		JeniChartItems ret = itemsPerSize.get(semantics.size());
		
		
		
		ruleOutNonUnifyingTopBotTrees(ret);
		setupLemmaFeatures(ret);
		logResults(ret);
		logger.info("*******************Beam size="+GeneratorOption.BEAM_SIZE);
		return ret;
	}
	
	 /** Returns the surface form of given realization. If morph is true, returns the morphological
	 * realizations, if false returns the lemmas separated by space.
	 * @param real
	 * @param morph
	 * @return a list of surface forms
	 */
	private static List<String> getSurface(SyntacticRealization real, boolean morph)
	{
		List<String> ret = new ArrayList<String>();
		if (morph)
		{
			for(MorphRealization morphReal : real.getMorphRealizations())
				ret.add(morphReal.asString());
		}
		else ret.add(Utils.print(real.getLemmas(), " "));
		return ret;
	}



	public static Map<String, Integer> groupRealizations(List<JeniRealization> results)
	{
		Map<String, Integer> ret = new HashMap<String, Integer>();

		for(JeniRealization result : results)
			for(MorphRealization morphReal : result.getMorphRealizations())
			{
				String surface = morphReal.asString() + ":" + result.getProbability();
				if (!ret.containsKey(surface))
					ret.put(surface, 0);
				ret.put(surface, ret.get(surface) + 1);
			}

		return ret;
	}

}
