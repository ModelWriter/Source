package synalp.generation.ranker;

import java.util.Comparator;
import java.util.Map;

import synalp.generation.ChartItem;


/**
 * @author apoorvi
 *
 */

public class DoubleValueComparator implements Comparator<Object>{

    Map<ChartItem, Double> base;

    /**
     * @param base
     */
    public DoubleValueComparator(final Map<ChartItem, Double> base) {
        this.base = base;
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object a, Object b) {

        if ((Double) base.get(a) > (Double) base.get(b)) {
            return -1;
        } else if ((Double) base.get(a) == (Double) base.get(b)) {
            return 0;
        } else {
            return 1;
        }
    }
}
