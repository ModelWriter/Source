package synalp.generation.jeni.semantics.rules;

import synalp.commons.semantics.Semantics;

/**
 * A Rule is a general purpose object that works on the input Semantics.
 * @author Alexandre Denis
 *
 */
public interface Rule
{
	/**
	 * Applies this Rule on the given Semantics. The given Semantics is altered.
	 * @param input
	 */
	public void apply(Semantics input);
}
