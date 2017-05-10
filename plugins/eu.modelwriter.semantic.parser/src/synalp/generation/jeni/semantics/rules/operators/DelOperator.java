package synalp.generation.jeni.semantics.rules.operators;

import synalp.commons.semantics.Semantics;
import synalp.commons.unification.InstantiationContext;

/**
 * @author Alexandre Denis
 */
public class DelOperator extends RuleOperator
{
	private Semantics toDelete;


	/**
	 * @param toDelete
	 */
	public DelOperator(Semantics toDelete)
	{
		this.toDelete = toDelete;
	}


	/**
	 * @param semantics
	 * @param context
	 */
	public void apply(Semantics semantics, InstantiationContext context)
	{
		Semantics sem = new Semantics(toDelete);
		sem.instantiate(context);
		semantics.removeAll(sem);
	}


	/**
	 * Returns the semantics.
	 * @return the semantics
	 */
	public Semantics getSemantics()
	{
		return toDelete;
	}


	@Override
	public String toString()
	{
		return "del " + toDelete;
	}
}
