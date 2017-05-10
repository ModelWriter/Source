package synalp.generation.jeni.selection.patterns.templates;

import synalp.commons.semantics.Semantics;
import synalp.commons.unification.InstantiationContext;
import synalp.generation.jeni.semantics.rules.operators.ReplaceOperator;

/**
 * A RewritingTemplate rewrites parts of the input semantics.
 * @author Alexandre Denis
 *
 */
public class RewritingTemplate extends SelectionTemplate
{
	private ReplaceOperator operator;
	
	/**
	 * 
	 * @param oldSem
	 * @param newSem
	 */
	public RewritingTemplate(Semantics oldSem, Semantics newSem)
	{
		this.operator = new ReplaceOperator(newSem, oldSem);
	}
	
	/**
	 * 
	 * @param semantics
	 * @param context
	 */
	public void applyRewriting(Semantics semantics, InstantiationContext context)
	{
		if (operator.getOld().doesSubsume(semantics, context))
			operator.apply(semantics, context);
	}
	
}
