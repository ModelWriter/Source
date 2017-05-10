package synalp.generation.jeni.selection.patterns.templates;

import java.util.*;

import synalp.commons.semantics.*;
import synalp.commons.unification.*;


/**
 * An AssumptionTemplate is a template that assumes literals if they are not present in the input.
 * @author Alexandre Denis
 */
public class AssumptionTemplate extends SelectionTemplate
{
	private Semantics assumption = new Semantics();


	/**
	 * @param assumption the assumption to set
	 */
	public void setAssumption(Semantics assumption)
	{
		this.assumption = assumption;
	}


	/**
	 * Checks if all literals of the assumption are found in the input semantics. A literal is found
	 * if there exists a literal in the given semantics that unifies with it in the given context.
	 * This method modifies the input semantics by adding those literals instantiated in the
	 * context.
	 * @param input
	 * @param context
	 */
	public void applyAssumption(Semantics input, InstantiationContext context)
	{
		for(DefaultLiteral literal : assumption)
		{
			DefaultLiteral absLiteral = abstractLiteral(literal, context);
			if (!input.contains(absLiteral, context))
			{
				DefaultLiteral add = new DefaultLiteral(literal);
				add.instantiate(context);
				input.add(add);
			}
		}
	}


	/**
	 * Transforms all constants of given literal by variables.
	 * @param literal
	 * @param context
	 * @return a new literal built from given literal without constants
	 */
	private static DefaultLiteral abstractLiteral(DefaultLiteral literal, InstantiationContext context)
	{
		List<FeatureValue> values = new ArrayList<FeatureValue>();
		for(FeatureValue arg : literal.getArguments())
			if (arg instanceof FeatureConstant)
				values.add(context.getNewVariable());
			else values.add(arg);

		DefaultLiteral ret = new DefaultLiteral(literal);
		ret.setArguments(values);
		return ret;
	}


	@Override
	public String toString()
	{
		return "assume " + assumption;
	}
}
