package synalp.commons.grammar;

import java.util.*;

/**
 * A Trace is a set of strings.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class Trace extends HashSet<String>
{
	/**
	 * Creates a new empty Trace.
	 */
	public Trace()
	{

	}


	/**
	 * Creates a Trace from given array of strings.
	 * @param traces
	 */
	public Trace(String... traces)
	{
		addAll(Arrays.asList(traces));
	}
	
	
	/**
	 * Creates a Trace from given collection of strings.
	 * @param traces
	 */
	public Trace(Collection<String> traces)
	{
		addAll(traces);
	}


	/**
	 * Deep copies the given Trace.
	 * @param trace
	 */
	public Trace(Trace trace)
	{
		if (trace != null)
			addAll(trace);
	}


	/**
	 * Tests whether this Trace is a subTrace of given Trace.
	 * @param trace
	 * @return trace.containsAll(this)
	 */
	public boolean isSubTrace(Trace trace)
	{
		return trace.containsAll(this);
	}
}
