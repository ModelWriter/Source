package synalp.generation;

/**
 * @author Celine Moro
 */
public enum GeneratorType
{
	/**
	 * Tree combining algorithm used in the Haskell version of Geni
	 */
	JENI_DEFAULT("jeni"),

	/**
	 * Tree combining algorithm used in RTGen (prolog)
	 */
	RTGEN("rtgen"),
	
	/**
	 * A probabilistic generator.
	 */
	PROBABILISTIC("probabilistic");
	

	private String value;


	private GeneratorType(String value)
	{
		this.value = value;
	}


	/**
	 * Return the value of a GeneratorType
	 * @return the value of a GeneratorType
	 */
	public String toString()
	{
		return value;
	}
}
