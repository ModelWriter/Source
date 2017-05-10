package synalp.parsing.treeCombinations;

import java.util.ArrayList;

import org.apache.log4j.Logger;


public class CKYChartCombinationsTester {
	
	static Logger logger = Logger.getLogger(CKYChartCombinationsTester.class);
	

	public static void main(String[] args) {
		
		ArrayList<ArrayList<String>> initial_agenda = new ArrayList<ArrayList<String>>();
		ArrayList<String> first = new ArrayList<String>();
		first.add("a");
		first.add("x");
		ArrayList<String> second = new ArrayList<String>();
		second.add("b");
		ArrayList<String> third = new ArrayList<String>();
		third.add("c");
		ArrayList<String> forth = new ArrayList<String>();
		forth.add("d");
		ArrayList<String> fifth = new ArrayList<String>();
		fifth.add("e");

		/*
		ArrayList<ArrayList<Integer>> initial_agenda = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> first = new ArrayList<Integer>();
		first.add(1);
		ArrayList<Integer> second = new ArrayList<Integer>();
		second.add(2);
		ArrayList<Integer> third = new ArrayList<Integer>();
		third.add(3);
		ArrayList<Integer> forth = new ArrayList<Integer>();
		forth.add(4);
		ArrayList<Integer> fifth = new ArrayList<Integer>();
		fifth.add(5);
		*/

		
		
		initial_agenda.add(first);
		initial_agenda.add(second);
		initial_agenda.add(third);
		initial_agenda.add(forth);
		initial_agenda.add(fifth);
		
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		CKYChartCombinations cKYChartCombinations = new CKYChartCombinations(initial_agenda, logger); // The first parameter is only for compatibility reasons; doesn't make sense in this code.
		System.out.println("\nChart Before Building = \n"+cKYChartCombinations);
		System.out.println("\nTop Item ="+cKYChartCombinations.getTopItem());
		cKYChartCombinations.buildChart(null, null, false); // The parameters are for compatibility reasons; not going to be used for String and Integers.
		System.out.println("\n\n\n\nChart After Building = \n"+cKYChartCombinations);
		System.out.println("\nTop Item ="+cKYChartCombinations.getTopItem());
		System.out.println("\nTop Item (Flattened) ="+cKYChartCombinations.getTopItemAsFlatList());
	}
	
}
