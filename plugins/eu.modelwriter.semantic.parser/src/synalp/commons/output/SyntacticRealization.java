package synalp.commons.output;

import java.util.*;

import synalp.commons.grammar.Tree;
import synalp.commons.input.Lemma;
import synalp.commons.unification.InstantiationContext;


/**
 * A Realization is the basic output of the generator. It should contain a list of the lemmas that
 * were generated, but may also contain the sentences that are morphologically realized from these
 * lemmas. Since it is possible to have several morphological realizations (sentences), they are
 * returned as a list.
 * @author Alexandre Denis
 */
public interface SyntacticRealization
{
	/**
	 * Returns a live list of the lemmas of this Realization. All operations performed on the
	 * returned list must be reflected in the Realization.
	 * @return a live list of lemmas
	 */
	public List<Lemma> getLemmas();


	/**
	 * Sets the lemmas of this Realization.
	 * @param lemmas
	 */
	public void setLemmas(List<Lemma> lemmas);


	/**
	 * Returns a live list of the morphological sentences associated to this Realization. All
	 * operations performed on the returned list must be reflected in the Realization.
	 * @return a live list of sentences
	 */
	public Collection<MorphRealization> getMorphRealizations();


	/**
	 * Sets the sentences of this Realization.
	 * @param sentences
	 */
	public void setMorphRealizations(Collection<MorphRealization> sentences);


	/**
	 * Returns the derived tree associated to this Realization.
	 * @return the derived tree
	 */
	public Tree getDerivedTree();
	
	
	/**
	 * Returns the context in which to interpret the variables of lemmas feature structures.
	 * @return a context
	 */
	public InstantiationContext getContext();
	
	
	/**
	 * Sets the context in which to interpret the variables of the lemmas feature structures.
	 * @param context 
	 */
	public void setContext(InstantiationContext context);
}
