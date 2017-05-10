package synalp.commons.lexicon;

import synalp.commons.unification.FeatureStructure;

/**
 * Filter is the class that represents the feature structure ({@link FeatureStructure}) used to
 * filter trees during the lexical selection phase.
 * <p>
 * It is defined in the syntactic lexicon input file (.glex, .lex or .xml).
 * </p>
 * @author Alexandre Denis
 * @author Céline Moro
 * @version %I%, %G%
 * @since 1.0
 */
public class Filter
{
	/**
	 * The {@link FeatureStructure} instance representing a particular feature to filter.
	 */
	private FeatureStructure featureStructure;


	/**
	 * Creates a new empty Filter.
	 */
	public Filter()
	{
		featureStructure = new FeatureStructure();
	}

	/**
	 * Deep Copy
	 */
	public Filter (Filter other) {
		this.featureStructure = new FeatureStructure(other.featureStructure);
	}
	

	/**
	 * Creates a new Filter based on given FeatureStructure.
	 * @param featureStructure
	 */
	public Filter(FeatureStructure featureStructure)
	{
		this.featureStructure = featureStructure;
	}


	/**
	 * Gets the {@link FeatureStructure} instance representing a particular feature to filter.
	 * @return the featureStructure
	 */
	public FeatureStructure getFeatureStructure()
	{
		return featureStructure;
	}


	/**
	 * Sets the {@link FeatureStructure} instance representing a particular feature to filter.
	 * @param featureStructure the featureStructure to set
	 */
	public void setFeatureStructure(FeatureStructure featureStructure)
	{
		this.featureStructure = featureStructure;
	}


	@Override
	public String toString()
	{
		return featureStructure.toString();
	}
}
