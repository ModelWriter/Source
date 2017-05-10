package synalp.generation.jeni.semantics.rules.operators;

import synalp.commons.semantics.Semantics;
import synalp.commons.unification.InstantiationContext;

/**
 * Does an add and del.
 * @author Alexandre Denis
 */
public class ReplaceOperator extends RuleOperator
{
	private AddOperator add;
	private DelOperator del;


	/**
	 * @param replacement
	 * @param deleted
	 */
	public ReplaceOperator(Semantics replacement, Semantics deleted)
	{
		this.del = new DelOperator(deleted);
		this.add = new AddOperator(replacement);
	}


	/**
	 * @param semantics
	 * @param context
	 */
	public void apply(Semantics semantics, InstantiationContext context)
	{
		del.apply(semantics, context);
		add.apply(semantics, context);
	}


	/**
	 * Sets the old semantics, the one that has to be deleted.
	 * @param oldSem
	 */
	public void setOld(Semantics oldSem)
	{
		del = new DelOperator(oldSem);
	}


	/**
	 * Sets the new semantics, the one that has to be added.
	 * @param newSem
	 */
	public void setNew(Semantics newSem)
	{
		add = new AddOperator(newSem);
	}


	/**
	 * Returns the old semantics.
	 * @return the old semantics.
	 */
	public Semantics getOld()
	{
		return del.getSemantics();
	}


	@Override
	public String toString()
	{
		return del + " then " + add;
	}
}
