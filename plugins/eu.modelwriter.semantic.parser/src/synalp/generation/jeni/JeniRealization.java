package synalp.generation.jeni;

import java.util.*;

import synalp.commons.derivations.DerivationTree;
import synalp.commons.grammar.Tree;
import synalp.commons.input.*;
import synalp.commons.output.*;
import synalp.commons.unification.InstantiationContext;


/**
 * A JeniRealization is the implementation of Realization by Jeni.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class JeniRealization extends ArrayList<Lemma> implements SyntacticRealization
{
	private DerivationTree derivation;
	private InstantiationContext context;
	private Collection<MorphRealization> sentences;
	
	private float probability;


	/**
	 * Creates a new empty realization.
	 */
	public JeniRealization()
	{
		derivation = null;
	}


	/**
	 * Creates a new Realization with given derivation tree.
	 * @param derivation
	 * @param context 
	 */
	public JeniRealization(DerivationTree derivation, InstantiationContext context)
	{
		this.derivation = derivation;
		this.context = context;
		
	}


	/**
	 * @return the derivation
	 */
	public DerivationTree getDerivation()
	{
		return derivation;
	}


	@Override
	public List<Lemma> getLemmas()
	{
		return this;
	}


	@Override
	public void setLemmas(List<Lemma> lemmas)
	{
		clear();
		addAll(lemmas);
	}


	@Override
	public Collection<MorphRealization> getMorphRealizations()
	{
		return sentences;
	}


	@Override
	public void setMorphRealizations(Collection<MorphRealization> sentences)
	{
		this.sentences = sentences;
	}


	@Override
	public Tree getDerivedTree()
	{
		return derivation == null ? null : derivation.getDerivedTree();
	}


	@Override
	public InstantiationContext getContext()
	{
		return context;
	}


	@Override
	public void setContext(InstantiationContext context)
	{
		this.context = context;
	}


	public float getProbability()
	{
		return probability;
	}


	public void setProbability(float probability)
	{
		this.probability = probability;
	}

}
