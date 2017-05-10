package synalp.commons.unification;

/**
 * A simple type of feature structure, top or bottom.
 * @author Alexandre Denis
 */
public enum FeatureStructureType
{
	/**
	 * The TOP type.
	 */
	TOP("top"),
	/**
	 * The BOTTOM type.
	 */
	BOTTOM("bot");

	private String value;


	private FeatureStructureType(String value)
	{
		this.value = value;
	}


	/**
	 * Returns a String value.
	 * @return a String "top" or "bot".
	 */
	public String getValue()
	{
		return value;
	}


	/**
	 * Returns a FeatureStructureType from given string.
	 * @param type
	 * @return null if the input string is neither 'top' nor 'bot'
	 */
	public static FeatureStructureType parse(String type)
	{
		if (type.equalsIgnoreCase("top"))
			return TOP;
		else if (type.equalsIgnoreCase("bot"))
			return BOTTOM;
		else return null;
	}
}
