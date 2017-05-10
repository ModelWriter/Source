package synalp.parsing.treeCombinations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import synalp.generation.jeni.JeniChartItem;
import synalp.generation.jeni.JeniChartItems;
import synalp.generation.jeni.TreeCombiner;
import synalp.generation.ranker.Ranker;
import synalp.parsing.ParseTreesCombiner;

/**
 * @author bikash
 * An sample demo program.
 * @param <T>
 */

public class CKYChartCombinations<T> {
	
	private ArrayList<ArrayList<ArrayList<T>>> fullchart;
	private ArrayList<ArrayList<T>> initial_agenda;
	private Logger logger;
	
	public CKYChartCombinations(ArrayList<ArrayList<T>> initial_agenda, Logger logger) {
		fullchart = new ArrayList<ArrayList<ArrayList<T>>>();
		this.initial_agenda = initial_agenda;
		fullchart.add(initial_agenda);
		this.logger = logger;
	}
	
	// By default, allows addition of duplicate items in arraylist of each row. For JeniChartItem, this has been prohibited by using instance of JeniChartItems class. 
	@SuppressWarnings("unchecked")
	public void buildChart(TreeCombiner combiner, Ranker ranker, boolean probabilistic) {
		for (int i=1; i<initial_agenda.size();i++) {
			logger.info("\n\n############################# Building ChartItems for Row "+i+" #############################\n");
			ArrayList<ArrayList<T>> ithRow = new ArrayList<ArrayList<T>>(); // initialise for ith row; beginning from index = 1
			for (int j=0;j<initial_agenda.size()-i;j++) { // for each possible column in the ith row
				
				
				ArrayList<ArrayList<ArrayList<T>>> chartBelow = getSubList(fullchart,0,i); // Excludes the ith index.
				ArrayList<ArrayList<T>> downEntries = getDownEntries(chartBelow,j);
				ArrayList<ArrayList<T>> diagonalEntries = getDiagonalEntries(chartBelow,i+j);

				
				ArrayList<T> combinations = new ArrayList<T>();
				if (combiner!=null)
					combinations = (ArrayList<T>) new JeniChartItems(); // JeniChartItems also removes duplicates
				
				for (int p=0;p<downEntries.size();p++) {
					ArrayList<T> current_downEntries = downEntries.get(p);
					ArrayList<T> current_diagonalEntries = diagonalEntries.get(diagonalEntries.size()-1-p); // both the arrays are going to be the same size.
					
					for (T downEntry:current_downEntries) {
						for (T diagEntry:current_diagonalEntries) {
							combinations.addAll(combine(downEntry,diagEntry,combiner,probabilistic));
						}
					}
					
				}
				
				// In case of JchartItmes; we only want to store n-many items at each cell -- prune using ranking.
				/*if (ranker!=null) {
					List<JeniChartItem> rankedItems = (List<JeniChartItem>) ranker.rank((List<? extends ChartItem>) combinations);
					ithRow.add((ArrayList<T>) rankedItems);
				}
				else */{
					ithRow.add(combinations);
				}
				
				
			}
			fullchart.add(ithRow);
		}
	}
		
	private ArrayList<ArrayList<ArrayList<T>>> getSubList(ArrayList<ArrayList<ArrayList<T>>> input, int startIndex, int endIndex) { //endIndex is exclusive
		ArrayList<ArrayList<ArrayList<T>>> ret = new ArrayList<ArrayList<ArrayList<T>>>();
		for (int i=startIndex;i<endIndex;i++) {
			ret.add(input.get(i));
		}
		return ret;
	}
		
	private ArrayList<ArrayList<T>> getDownEntries(List<ArrayList<ArrayList<T>>> input, int columnIndex) {
		ArrayList<ArrayList<T>> downEntries = new ArrayList<ArrayList<T>>();
		for (ArrayList<ArrayList<T>> entry:input) {
			downEntries.add(entry.get(columnIndex));
		}
		return downEntries;
	}
	
	private ArrayList<ArrayList<T>> getDiagonalEntries(List<ArrayList<ArrayList<T>>> input, int diagIndex) {
		ArrayList<ArrayList<T>> diagonalEntries = new ArrayList<ArrayList<T>>();
		for (ArrayList<ArrayList<T>> entry:input) {
			diagonalEntries.add(entry.get(diagIndex));
			diagIndex = diagIndex - 1; // The diagonal entries lie at -1 diagIndex at each row above.
		}
		return diagonalEntries;
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<T> combine(T first, T second, TreeCombiner combiner, boolean probabilistic) { // two things can combine and give more than 1 result. Eg: 'a' and 'b' can combine to 'ab' or 'ba', depending upon what rules we allow in the grammar.
		ArrayList<T> ret = new ArrayList<T>();
		// Simple Grammar -- the output is simply the combination of input strings, in the order they were presented.
		if (first.getClass() == String.class && second.getClass() == String.class) {
			ret.add((T) ((String) first).concat((String) second));
		}
		if (first.getClass() == Integer.class && second.getClass() == Integer.class) {
			ret.add((T) (Integer) ((Integer) first + (Integer) second)); // this was just a test to play with the generic type T; doesn't make sense to add integers.
		}
		// More complex grammar -- the output is the combination of the input (first and second) TAG trees in any order possible between them. 
		if (first.getClass() == JeniChartItem.class && second.getClass() == JeniChartItem.class) {
			ParseTreesCombiner comb = new ParseTreesCombiner(probabilistic, logger); 
			JeniChartItems inputs = new JeniChartItems();
			inputs.add((JeniChartItem) first);
			inputs.add((JeniChartItem) second);
			JeniChartItems results = comb.combine(inputs); // This call updates the parameter inputs itself and sets it to [] before it returns
			
			
			// So, creating another ArrayList to represent the collection of first and second
			JeniChartItems inputsx = new JeniChartItems();
			inputsx.add((JeniChartItem) first);
			inputsx.add((JeniChartItem) second);
			ruleOutIncompleteDerivations(results,inputsx);  	
			ret.addAll((Collection<? extends T>) results);
		}
		return ret; 
	}
	
	/**
	 * The @results represents the JeniChartItems which have been built using only 2 immediate input JeniChartItems.
	 * Further the inputs may or may not be intermediate chart results. In any case, since the @param results is obtained from
	 * combinations of IMMEDIATE input JeniChartItems (and not after a long run as in case of FullCombinations), 
	 * we can filter out those derivations that have been built using only some of the given trees. In other words, 
	 * all the trees in @param trees_used must combine
	 * @param results
	 * @param trees_used
	 */

	private void ruleOutIncompleteDerivations(JeniChartItems results, JeniChartItems inputs) {
		logger.info("\n\nResults obtained with the inputs Trees = ");
		logger.info("{");
		for (JeniChartItem item:inputs) {
			logger.info(item.toString()+item.getDerivation()+", ");
		}
		logger.info("} are : \n");
		int countResults = 1;
		for(Iterator<JeniChartItem> it = results.iterator(); it.hasNext();) {
			
			
			JeniChartItem result = it.next();
			
			
			logger.info(countResults+". "+result.toString()+result.getDerivation()+"\n");
			
			boolean complete = isCompleteResult(result,inputs);
			
			if (complete) {
				logger.info("\n\tRemarks = OK. All input trees combined. " +"\n\n");
			}
			else {
				logger.info("\n\tRemarks = Not all trees combined. Going to be discarded." +"\n\n");
				it.remove();
			}
			countResults++;
		}
		logger.info("\n");
	}
	
	/**
	 * returns true if @result was obtained from the combinations of all items in the @inputs
	 * @param result
	 * @param inputs
	 * @return
	 */
	private boolean isCompleteResult(JeniChartItem result, JeniChartItems inputs) {
		for (JeniChartItem input:inputs) {
			if (!((input==result.getParentItemSource())||(input==result.getParentItemTarget()))) { // if input is not equal to one of the 2 immediate parents
				logger.info("\n\t\tNo, Input "+input+" is not a constituent of "+result);
				return false;
			}
			else {
				logger.info("\n\t\tYes, Input "+input+" is used in the making up of "+result);				
			}
		}
		return true;
	}
	
	
	public String toString() {
		String ret = "FullChart = \n";
		for (int i=0; i<fullchart.size(); i++) {
			ret = ret +"Row "+i+" = "+fullchart.get(i)+"\n";
		}
		return ret;
	}
	
	public ArrayList<ArrayList<ArrayList<T>>> getFullChart() {
		return fullchart;
	}
	
	public ArrayList<ArrayList<T>> getTopItem() {
		return fullchart.get(fullchart.size()-1);
	}
	
	public ArrayList<T> getTopItemAsFlatList() { // In the top level, there should be only one ArrayList (which can contain one or more elements)
		//Hence could be flattened as well.
		// A sample top row looks like this : 
		//[[Item-6 [routings shall be defined]]]
		ArrayList<T> flattened = new ArrayList<T>();
		ArrayList<ArrayList<T>> topRow = getTopItem();
		if (topRow.size()==1) {
			flattened = topRow.get(0);
		}
		else { 
			if (topRow.isEmpty()) { // The top row may not even have 1 arraylist as its content if the initial agenda was completely empty.
				flattened = new ArrayList<T>(); 
			}
			else { // Better error handling needed than this.
				System.out.println("Bug in implementing the chartBuilder. The Top row should have only one arraylist as its content");
				System.exit(1);
			}
		}		
		return flattened;
	}
	
	public ArrayList<ArrayList<T>> getInitialAgenda(){
		return initial_agenda;
	}
	
	
	/*
	 * The count of rows in the chart is equal to the count of inputs words making up the agenda. When the top row is non empty, it means that
	 * the grammar could parse the total sentences. But sometimes, this may not be the case and therefore, we wish to get partially parsed results.  
	 */
	public ArrayList<T> getNonEmptyRowResultsAsFlatList() {
		// First find the best row to extract the results from
		int maxrowCount = fullchart.size()-1; // the indexing begins from 0;
		for (int y=maxrowCount; y>0; y--) { // we want to look for partial results until the row 1. At row 0, the items are the inputs themselves
			if (rowContainsSomeResult(fullchart.get(y))) {
				maxrowCount = y;
				break;
			}
		}
		
		ArrayList<T> flattened = new ArrayList<T>();
		// Then flatten out all the results in that row (each row = ArrayList of Arraylist of results). A sample row looks like this : 
		// [[], [Item-6 [routings shall be defined]], [Item-15 [routings shall be defined]]]
		for (ArrayList<T> x:fullchart.get(maxrowCount)) {
			flattened.addAll(x);
		}
		return flattened;
	}
	
	
	private boolean rowContainsSomeResult(ArrayList<ArrayList<T>> input) {
		for (ArrayList<T> x:input) {
			if (!x.isEmpty())
				return true;
		}
		return false;
	}

}
