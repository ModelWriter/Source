package synalp.generation.jeni.semantics.rules;

import java.util.ArrayList;

import synalp.commons.semantics.Semantics;


/**
 * A list of rules. The order matters.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class Rules extends ArrayList<Rule>
{
	/**
	 * Applies these rules to the given semantic input, one after the other. This method alters the
	 * given Semantics.
	 * @param input
	 */
	public void apply(Semantics input)
	{
		for(Rule rule : this)
			rule.apply(input);
	}
}
