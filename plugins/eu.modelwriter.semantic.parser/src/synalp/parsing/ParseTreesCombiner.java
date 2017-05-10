package synalp.parsing;

import java.util.Iterator;

import org.apache.log4j.Logger;

import synalp.generation.jeni.JeniChartItem;
import synalp.generation.jeni.JeniChartItems;
import synalp.generation.jeni.TreeCombiner;
import synalp.parsing.utils.WordOrder;

public class ParseTreesCombiner {
	private JeniChartItems chart;
	private TreeCombiner combiner;
	private Logger logger;
	
	
	public ParseTreesCombiner(boolean probabilistic) {
		chart = new JeniChartItems();
		if (!probabilistic) {
			combiner = new TreeCombiner(p -> 1);	
		}
		else {
			combiner = new TreeCombiner(p -> p.getParentItemSource().getProbability() * p.getParentItemTarget().getProbability());
		}
	}
	
	
	public ParseTreesCombiner(boolean probabilistic, Logger logger) {
		this(probabilistic);
		this.logger = logger;
	}
	
	/*
	 * Given two or several items in agenda, it combines all of them and returns the (zero or several) resulting trees.
	 */
	public JeniChartItems combine(JeniChartItems agenda)
	{
		chart = new JeniChartItems();
		JeniChartItems auxiliaryAgenda = new JeniChartItems();

		while(!agenda.isEmpty())
			performSubstitutionStep(agenda, chart, auxiliaryAgenda);

		// now adjunctions
		agenda = new JeniChartItems(chart);
		chart = new JeniChartItems(auxiliaryAgenda);

		JeniChartItems ret = new JeniChartItems();
		while(!agenda.isEmpty())
			ret.addAll(performAdjunctionStep(agenda, chart));


		ruleOutIncorrectWordOrderTrees(ret);

		return ret;
	}
	
	
	/**
	 * Performs one step of substitutions. One item from the agenda is removed, if it is an
	 * auxiliary tree put it in auxiliaryAgenda, else try to combine it with items in chart and add
	 * result to agenda. Eventually add the item to the chart.
	 * @param agenda
	 * @param chart
	 * @param auxiliaryAgenda
	 */
	private void performSubstitutionStep(JeniChartItems agenda, JeniChartItems chart, JeniChartItems auxiliaryAgenda)
	{
		JeniChartItem item = agenda.removeFirst();
		if (item.getTree().getFoot() != null && item.getTree().getSubstitutions().isEmpty())
			auxiliaryAgenda.add(item);
		else
		{
			//JeniChartItems pending = new JeniChartItems(agenda, chart, auxiliaryAgenda);
			JeniChartItems newItems = new JeniChartItems();
			for(JeniChartItem other : chart) {
				//newItems.addAll(combiner.getSubstitutionCombinations(item, other, pending)); // Pending is needed when the Early_Semantic_Failure option is set to be true. (In case of parsing, I think that doesn't make sense because the trees that are going to be combined are based on their word indices and not on semantics intersection tests)
				newItems.addAll(combiner.getSubstitutionCombinationsforParsing(item, other));
			}
			chart.add(item);
			agenda.addAll(newItems);
		}
	}
	
	/**
	 * Performs one step of adjunctions. One item from the agenda is removed and if its semantics
	 * match the input semantics it shall be returned, then try to combine it with items in chart
	 * and add result to agenda.
	 * @param input the original semantics we want to generate
	 * @param agenda
	 * @param chart
	 * @return a list of newly created chart items
	 */
 	private JeniChartItems performAdjunctionStep(JeniChartItems agenda, JeniChartItems chart)
	{
		JeniChartItems results = new JeniChartItems();
		JeniChartItem item = agenda.removeFirst();

		results.add(item);

		JeniChartItems newItems = new JeniChartItems();
		for(JeniChartItem other : chart)
			newItems.addAll(combiner.getAdjunctionCombinationsforParsing(item, other));

		agenda.addAll(newItems);
		return results;
	}
	
	/**
	 * To exclude the parse trees which contain extra words/out-of-order words in the output sentence.
	 * Here, the check is whether the nodes are in ascending order or not. Can't use the exact order check
	 * because the intermediate results (when using CKY) can have non instantiated Foot/Substitution Nodes.
	 * @param results
	 */
	private void ruleOutIncorrectWordOrderTrees(JeniChartItems results) {
		
		for(Iterator<JeniChartItem> it = results.iterator(); it.hasNext();) {
			JeniChartItem result = it.next();
			if (!WordOrder.isAscendingOrder(WordOrder.getLemmaNodesIndex(result.getTree()))) {
				logger.info("\n\n The output "+result+" was discarded because of its incorrect(null) word order "+WordOrder.getLemmaNodesIndex(result.getTree()));
				it.remove();
			}
		}
	}
}
