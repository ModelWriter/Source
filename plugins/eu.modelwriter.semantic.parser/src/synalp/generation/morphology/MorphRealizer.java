package synalp.generation.morphology;

import java.util.Collection;

import synalp.commons.output.*;


/**
 * General interface for morphological realization.
 * @author Alexandre Denis
 *
 */
public interface MorphRealizer
{
	/**
	 * Sets the MorphRealizations of the given SyntacticRealization and returns them as well.
	 * @param synRealization
	 * @return the newly created MorphRealizations attached to the given SyntacticRealization
	 */
	public Collection<MorphRealization> setMorphRealizations(SyntacticRealization synRealization);
}
