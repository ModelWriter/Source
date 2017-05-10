package synalp.commons.semantics;

import java.util.*;

import synalp.commons.unification.InstantiationContext;


/**
 * A SemanticsSubsumptionResult is a data structure meant to gather the results of a semantics
 * subsumption test. It associates to each possible instantiation context literals of the second
 * argument that are matched. It thus enables to remember what were the covered literals.
 * @author Alexandre Denis
 */
public class SemanticsSubsumptionResult
{
	private Map<InstantiationContext, List<DefaultLiteral>> results = new HashMap<InstantiationContext, List<DefaultLiteral>>();


	/**
	 * Tests if there are results.
	 * @return false if there is no result.
	 */
	public boolean isEmpty()
	{
		return results.isEmpty();
	}


	/**
	 * Tests if the subsumption is true. It is equivalent to say that there exists results.
	 * @return true if there are results
	 */
	public boolean isTrue()
	{
		return !isEmpty();
	}


	/**
	 * Returns the contexts in which the subsumption would be true.
	 * @return a collection of contexts, or an empty set if it cannot be true.
	 */
	public Set<InstantiationContext> getContexts()
	{
		return results.keySet();
	}


	/**
	 * Returned the covered literals in the given context.
	 * @param context
	 * @return a set of literals that may be empty
	 */
	public List<DefaultLiteral> getCoveredLiterals(InstantiationContext context)
	{
		if (results.containsKey(context))
			return results.get(context);
		else return new ArrayList<DefaultLiteral>();
	}


	/**
	 * Adds the given literal to the given context.
	 * @param context
	 * @param literal
	 */
	public void addLiteral(InstantiationContext context, DefaultLiteral literal)
	{
		if (!results.containsKey(context))
			results.put(context, new ArrayList<DefaultLiteral>());
		results.get(context).add(literal);
	}


	/**
	 * Adds the given literals to the given context.
	 * @param context
	 * @param literals
	 */
	public void addLiterals(InstantiationContext context, Collection<DefaultLiteral> literals)
	{
		if (!results.containsKey(context))
			results.put(context, new ArrayList<DefaultLiteral>());
		results.get(context).addAll(literals);
	}


	/**
	 * Adds the given context wihout literals. If the context already exists, do nothing.
	 * @param context
	 */
	public void addContext(InstantiationContext context)
	{
		if (!results.containsKey(context))
			results.put(context, new ArrayList<DefaultLiteral>());
	}


	/**
	 * Adds all the results of the given SemanticsSubsumptionResult to this instance.
	 * @param result
	 */
	public void addAll(SemanticsSubsumptionResult result)
	{
		for(InstantiationContext context : result.getContexts())
			addLiterals(context, result.getCoveredLiterals(context));
	}


	@Override
	public String toString()
	{
		return results.toString();
	}
}
