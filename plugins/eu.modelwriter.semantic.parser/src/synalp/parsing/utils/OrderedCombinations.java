package synalp.parsing.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;


/**
 * This class returns ordered combinations (ordered combinations = Permutation) of elements of arrays. The different arrays are treated as independent; hence even if the elements of 2 different happen to be 
 * equal, they are still not considered to be repetitions. Thus it will generate n*m*k*l*.....*z groups of items.
 * 
 * @author bikashg
 *
 */
public class OrderedCombinations {
	
	@Test
	public void test() {
		final List<Object> l1 = new ArrayList<Object>();
	    l1.add(1);
	    l1.add(2);
	    final List<Object> l2 = new ArrayList<Object>();
	    l2.add("3");
	    l2.add("4");
	    l2.add("5");
	    final List<Object> l3 = new ArrayList<Object>();
	    l3.add(true);
	    l3.add(false);

	    final List<List<Object>> c = new ArrayList<>(3);
	    c.add(l1);
	    c.add(l2);
	    c.add(l3);

	    // Get combinations
	    List<List<Object>> m = OrderedCombinations.getCombinations(c);
	    System.out.println("M before = "+m);
	    // Each list within the list has the elements in reverse combination order.
	    for (List<Object> each:m) {
	    	Collections.reverse(each);
	    }
	    System.out.println("M after = "+m); 
	}
	
	
	public static List<List<Object>> getCombinations(List<List<Object>> input) {
		return combination(0, input);
	}
	

	private static List<List<Object>> combination(int index, List<List<Object>> input) {
		List<List<Object>> ret = new ArrayList<List<Object>>();
	    if (index == input.size()) {
	        ret.add(new ArrayList<Object>());
	    } 
	     else {
	        for (Object obj : input.get(index)) {
	            for (List<Object> result : combination(index+1, input)) {
	            	//System.out.println("result is "+result);
	                result.add(obj);
	                //System.out.println("result became "+result);
	                ret.add(result);
	                //System.out.println("ret became "+ret);
	            }
	        }
	    }
	    return ret;
	}
}
