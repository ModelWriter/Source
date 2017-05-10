package synalp.commons.unification;

/**
 * A Feature is the association of a name and a FeatureValue.
 * @author Alexandre Denis
 */
public class Feature
{
	private String name;
	private FeatureValue value;


	/**
	 * Creates a Feature with given name without specifying its value.
	 * @param name
	 */
	public Feature(String name)
	{
		this.name = name;
		this.value = null;
	}


	/**
	 * Creates a Feature with given name and value.
	 * @param name
	 * @param value
	 */
	public Feature(String name, FeatureValue value)
	{
		this.name = name;
		this.value = value;
	}


	/**
	 * Deep copies the given Feature.
	 * @param feat
	 */
	public Feature(Feature feat)
	{
		this.name = feat.getName();
		this.value = feat.getValue().copy();
	}


	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 * @return the value
	 */
	public FeatureValue getValue()
	{
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(FeatureValue value)
	{
		this.value = value;
	}


	@Override
	public String toString()
	{
		return name + ":" + value;
	}


	/**
	 * Returns a String representation of this Feature in the given context.
	 * @param context
	 * @return a String
	 */
	public String toString(InstantiationContext context)
	{
		return name + ":" + value.toString(context);
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Feature other = (Feature) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
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

}
