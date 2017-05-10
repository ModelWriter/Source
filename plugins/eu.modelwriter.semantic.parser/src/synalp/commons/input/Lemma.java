package synalp.commons.input;

import java.util.List;

import synalp.commons.unification.*;


/**
 * Represents a lemma, the lemma string realization is the "value" field. The category is found both
 * in the feature structure and as a feature value.
 * @author Alexandre Denis
 */
public class Lemma
{
	private Lexem lexems; // this is not used yet
	private String value;
	private FeatureValue category;
	private FeatureStructure fs = new FeatureStructure();


	/**
	 * Returns a String representation of the given list of lemmas associated with their feature
	 * structure.
	 * @param lemmas
	 * @param context
	 * @param printFS if true shows also the fs
	 * @return a String
	 */
	public static String printLemmas(List<Lemma> lemmas, InstantiationContext context, boolean printFS)
	{
		return printLemmas(lemmas, context, " ", printFS);
	}


	/**
	 * Returns a String representation of the given list of lemmas associated with their feature
	 * structure.
	 * @param lemmas
	 * @param context
	 * @param separator a separator string added after each lemma
	 * @param printFS if true shows also the fs
	 * @return a String
	 */
	public static String printLemmas(List<Lemma> lemmas, InstantiationContext context, String separator, boolean printFS)
	{
		StringBuilder ret = new StringBuilder();
		for(Lemma lemma : lemmas)
		{
			ret.append(lemma.getValue());
			if (printFS)
				ret.append(lemma.getFs() == null ? "" : " " + lemma.getFs().toString(context));
			ret.append(separator);
		}
		return ret.toString().trim();
	}


	/**
	 * Creates a new Lemma with given surface form value.
	 * @param value
	 */
	public Lemma(String value)
	{
		this.value = value;
	}


	/**
	 * Creates a new Lemma with given surface form value and feature structure.
	 * @param value
	 * @param fs
	 */
	public Lemma(String value, FeatureStructure fs)
	{
		this.value = value;
		this.fs = fs;
	}


	/**
	 * Deep copy the given Lemma. This constructor does not copy the lexems or fs yet.
	 * @param lemma
	 */
	public Lemma(Lemma lemma)
	{
		this.value = lemma.getValue();
		this.lexems = null;
		this.fs = null;
	}


	/**
	 * @return the lexems
	 */
	public final Lexem getLexems()
	{
		return lexems;
	}


	/**
	 * @param value
	 */
	public void setLexems(Lexem value)
	{
		lexems = value;
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


	/**
	 * Returns a String representation of this lemma including its fs in the given context.
	 * @param context
	 * @return a String
	 */
	public String toString(InstantiationContext context)
	{
		StringBuilder ret = new StringBuilder();
		ret.append(value);
		if (fs != null)
			ret.append(" ").append(fs.toString(context));
		return ret.toString();
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fs == null) ? 0 : fs.hashCode());
		result = prime * result + ((lexems == null) ? 0 : lexems.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Lemma other = (Lemma) obj;
		if (fs == null)
		{
			if (other.fs != null)
				return false;
		}
		else if (!fs.equals(other.fs))
			return false;
		if (lexems == null)
		{
			if (other.lexems != null)
				return false;
		}
		else if (!lexems.equals(other.lexems))
			return false;
		if (value == null)
		{
			if (other.value != null)
				return false;
		}
		else if (!value.equals(other.value))
			return false;
		return true;
	}


	/**
	 * @return the category
	 */
	public FeatureValue getCategory()
	{
		return category;
	}


	/**
	 * @param category the category to set
	 */
	public void setCategory(FeatureValue category)
	{
		this.category = category;
		this.fs.add(new Feature("cat", category));
	}

}
