package synalp.parsing.utils;

import java.util.ArrayList;

import synalp.commons.grammar.Node;
import synalp.commons.grammar.Tree;
import synalp.parsing.Inputs.Word;

public class WordOrder {

	public static boolean containsUnwantedWord(String input, ArrayList<Word> allWords) {
		for (Word w:allWords) {
			if (w.getToken().equalsIgnoreCase(input))
				return false;
		}
		return true;
	}
	
	
	public static boolean isContiguous(ArrayList<Integer> wordOrder) {
		if (wordOrder.isEmpty()) {
			return false;
		}
		int beginValue = wordOrder.get(0);
		for (int i=1;i<wordOrder.size();i++) {
			beginValue = beginValue + 1;
			if (wordOrder.get(i)!=beginValue) {
				return false;
			}
		}
		return true;
	}
	
	
 	public static boolean isAscendingOrder(ArrayList<Integer> wordOrder) {
		if (wordOrder.isEmpty()) {
			return false;
		}
		if (wordOrder.contains(null)) {
			return false;
		}
		int prevWordIndex = wordOrder.get(0);
		for(int i=1; i<wordOrder.size(); i++) {
		    if(prevWordIndex>=wordOrder.get(i)) {
		       return false;
		    }
		    else {
		    	prevWordIndex = wordOrder.get(i);
		    }
		}
		return true;
	}
	
	public static ArrayList<Integer> getLemmaNodesIndex(Tree t) {
		ArrayList<Integer> order = new ArrayList<Integer>();
		for (Node anchor:t.getLemmasNodes()){ 
			order.add(anchor.getNodeIndex());
		}
		return order;
	}
	
	
	
	/**
	 * Returns true if a contains any elements of b
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean containsAny(ArrayList<Integer> a, ArrayList<Integer> b) {
		for (Integer y:b) {
			if (a.contains(y)) {
				return true;
			}
		}
		return false;
	}
}
