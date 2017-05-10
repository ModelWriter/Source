package synalp.generation.morphology;

import java.util.*;

import synalp.commons.input.*;


/**
 * A MorphLexiconEntry is an entry is a MorphLexicon. It associates a lemma to a set of lexems
 * (inflected forms) of the lemma.
 * @author Alexandre Denis
 */
public class MorphLexiconEntry
{
	private Lemma lemma;
	private Set<Lexem> lexems = new HashSet<Lexem>();


	/**
	 * Creates a new MorphLexiconEntry with given Lemma and Lexems.
	 * @param lemma
	 * @param lexems
	 */
	public MorphLexiconEntry(Lemma lemma, Lexem... lexems)
	{
		this.lemma = lemma;
		this.lexems.addAll(Arrays.asList(lexems));
	}


	/**
	 * @return the lemma
	 */
	public Lemma getLemma()
	{
		return lemma;
	}


	/**
	 * @param lemma the lemma to set
	 */
	public void setLemma(Lemma lemma)
	{
		this.lemma = lemma;
	}


	/**
	 * @return the lexems
	 */
	public Set<Lexem> getLexems()
	{
		return lexems;
	}


	/**
	 * @param lexems the lexems to set
	 */
	public void setLexems(Set<Lexem> lexems)
	{
		this.lexems = lexems;
	}

	
	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		for(Lexem lexem : lexems)
			ret.append("\"").append(lemma).append("\" \"").append(lexem).append("\" ").append(lexem.getFs()).append("\n");
		return ret.toString().trim();
	}
}
