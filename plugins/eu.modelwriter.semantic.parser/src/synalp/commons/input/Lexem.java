package synalp.commons.input;

import synalp.commons.unification.FeatureStructure;

/**
 * A Lexem is an inflected form of a Lemma. Note: it should be renamed Lexeme! Moreover it does not
 * represent exactly a lexeme since given the wikipedia definition, the lexeme is the abstract set
 * of inflected forms.
 * @author Alexandre Denis
 */
public class Lexem
{
	private String value;
	private FeatureStructure fs = new FeatureStructure();


	/**
	 * @param value
	 */
	public Lexem(String value)
	{
		this.value = value;
	}


	/**
	 * @param value
	 * @param fs
	 */
	public Lexem(String value, FeatureStructure fs)
	{
		this.value = value;
		this.fs = fs;
	}


	/**
	 * @return the feature structure
	 */
	public final FeatureStructure getFs()
	{
		return fs;
	}



	/**
	 * @param value
	 */
	public void setFs(FeatureStructure value)
	{
		fs = value;
	}


	/**
	 * @return the value
	 */
	public final String getValue()
	{
		return value;
	}


	/**
	 * @param new_value
	 */
	public void setValue(String new_value)
	{
		value = new_value;
	}


	@Override
	public String toString()
	{
		return value;
	}
}
