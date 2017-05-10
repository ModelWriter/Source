package synalp.commons.input;

import java.util.*;

import synalp.commons.utils.Utils;


/**
 * Lemmatization covers all the lemmas in an anchor and co-anchors. As a String representation, all
 * lemmas are separated by "_" character. The convention is that the first element is the main anchor,
 * and the others are co-anchors in the prefix order they appear in the tree.
 * @author Alexandre Denis
 */
public class Lemmatization
{
	private Lemma mainAnchor;
	private List<Lemma> coAnchors = new ArrayList<Lemma>();


	/**
	 * Creates a Lemmatization from given str. The first one being the mainAnchor and the others
	 * coAnchors.
	 * @param lemmas
	 */
	public Lemmatization(String... lemmas)
	{
		mainAnchor = new Lemma(lemmas[0]);
		for(int i = 1; i < lemmas.length; i++)
			coAnchors.add(new Lemma(lemmas[i]));
	}


	/**
	 * @return the mainAnchor
	 */
	public Lemma getMainAnchor()
	{
		return mainAnchor;
	}


	/**
	 * @return the coAnchors
	 */
	public List<Lemma> getCoAnchors()
	{
		return coAnchors;
	}


	@Override
	public String toString()
	{
		List<Lemma> all = new ArrayList<Lemma>();
		all.add(mainAnchor);
		all.addAll(coAnchors);
		return Utils.print(all, "_");
	}



	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coAnchors == null) ? 0 : coAnchors.hashCode());
		result = prime * result + ((mainAnchor == null) ? 0 : mainAnchor.getValue().hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lemmatization other = (Lemmatization) obj;
		if (coAnchors == null)
		{
			if (other.coAnchors != null)
				return false;
		}
		else if (!coAnchors.equals(other.coAnchors))
			return false;
		if (mainAnchor == null)
		{
			if (other.mainAnchor != null)
				return false;
		}
		else if (!mainAnchor.getValue().equals(other.mainAnchor.getValue()))
			return false;
		return true;
	}
	
	
}
