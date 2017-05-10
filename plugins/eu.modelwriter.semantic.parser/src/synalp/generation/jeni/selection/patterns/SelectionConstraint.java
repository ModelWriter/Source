package synalp.generation.jeni.selection.patterns;

import synalp.commons.unification.*;

/**
 * A constraint on variable instantiation.
 * @author Alexandre Denis
 */
public class SelectionConstraint
{
	private FeatureValue value;
	private FeatureVariable variable;


	/**
	 * @param variable
	 * @param value
	 */
	public SelectionConstraint(FeatureVariable variable, FeatureValue value)
	{
		this.variable = variable;
		this.value = value;
	}


	/**
	 * Tests if this constraint is satisfied in the given context.
	 * @param context
	 * @return true if this variable exists in the given context and its value unifies with this value.
	 */
	public boolean isSatisfied(InstantiationContext context)
	{
		if (!context.containsKey(variable))
			return false;
		else return Unifier.unify(variable, value, new InstantiationContext(context)) != null;
	}


	@Override
	public String toString()
	{
		return variable + ":" + value;
	}
}
