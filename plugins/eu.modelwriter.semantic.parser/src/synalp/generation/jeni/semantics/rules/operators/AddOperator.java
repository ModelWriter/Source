package synalp.generation.jeni.semantics.rules.operators;

import synalp.commons.semantics.Semantics;
import synalp.commons.unification.InstantiationContext;

/**
 * @author Alexandre Denis
 */
public class AddOperator extends RuleOperator
{
	private Semantics add;


	/**
	 * @param add
	 */
	public AddOperator(Semantics add)
	{
		this.add = add;
	}


	/**
	 * @param semantics
	 * @param context
	 */
	public void apply(Semantics semantics, InstantiationContext context)
	{
		createConstants(semantics, context);
		Semantics newSem = new Semantics(add);
		newSem.instantiate(context);
		semantics.addAll(newSem);
	}


	@Override
	public String toString()
	{
		return "add " + add;
	}
}
