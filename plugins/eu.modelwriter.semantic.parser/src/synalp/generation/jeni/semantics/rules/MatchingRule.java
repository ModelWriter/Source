package synalp.generation.jeni.semantics.rules;

import java.util.*;

import synalp.commons.semantics.*;
import synalp.commons.unification.InstantiationContext;
import synalp.generation.jeni.semantics.rules.operators.RuleOperator;


/**
 * A MatchingRule is meant to match the input and perform some add operations.
 * @author Alexandre Denis
 */
public class MatchingRule implements Rule
{
	private Semantics match;
	private Semantics without;
	private Set<RuleOperator> operators = new HashSet<RuleOperator>();


	/**
	 * @param match
	 * @param without
	 */
	public MatchingRule(Semantics match, Semantics without)
	{
		this.match = match;
		this.without = without;
	}


	/**
	 * Applies this MatchingRule to the given Semantics.
	 * @param input
	 */
	@Override
	public void apply(Semantics input)
	{
		for(InstantiationContext context : SemanticsSubsumer.subsumes(match, input))
			if (!context.isEmpty())
				if (without.isEmpty() || (!without.isEmpty() && SemanticsSubsumer.subsumes(without, input, context).isEmpty()))
					for(RuleOperator operator : operators)
						operator.apply(input, context);
	}


	/**
	 * @param operator
	 */
	public void addOperator(RuleOperator operator)
	{
		operators.add(operator);
	}


	@Override
	public String toString()
	{
		return "when found " + match + (without.isEmpty() ? "" : " without " + without) + " then " + operators;
	}


	/**
	 * @return the match
	 */
	public Semantics getMatch()
	{
		return match;
	}
}
