package synalp.commons.unification;

import java.util.*;

/**
 * A variable value in a feature. The name of the variable is final because the variable can be a
 * key in a map. If the name changes, the key will change also and many problems can arise.
 * @author Alexandre Denis
 */
public final class FeatureVariable extends FeatureValue
{
	private final String name;

	/**
	 * Creates a new variable that does not appear in the given collection of variables and modifies the
	 * given set of variables to add it. This method alters the given collection of variables.
	 * @param vars
	 * @return a new variable
	 */
	public static FeatureVariable createNewVariable(Collection<FeatureVariable> vars)
	{
		return createNewVariable(vars, 0);
	}
	

	/**
	 * Creates a new variable that does not appear in the given collection of variables and modifies the
	 * given set of variables to add it. This method alters the given collection of variables.
	 * @param vars
	 * @param i a starting index to rename the variable
	 * @return a new variable
	 */
	public static FeatureVariable createNewVariable(Collection<FeatureVariable> vars, int i)
	{
		FeatureVariable ret = new FeatureVariable("@V" + i);
		while(vars.contains(ret))
			ret = new FeatureVariable("@V" + (i++));
		vars.add(ret);
		return ret;
	}


	/**
	 * @param name
	 */
	public FeatureVariable(String name)
	{
		this.name = name;
	}


	/**
	 * Deep copies the given variable.
	 * @param variable
	 */
	public FeatureVariable(FeatureVariable variable)
	{
		this.name = variable.getName();
	}


	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		FeatureVariable other = (FeatureVariable) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}


	@Override
	public String toString()
	{
		return name;
	}


	/**
	 * Returns a String representation of this variable in the given context.
	 * @param context
	 * @return a String
	 */
	public String toString(InstantiationContext context)
	{
		if (context.containsKey(this))
			return name + "." + context.get(this).toString(context);
		else return name;
	}


	@Override
	public FeatureValueType getType()
	{
		return FeatureValueType.VARIABLE;
	}


	@Override
	public FeatureVariable copy()
	{
		return new FeatureVariable(this);
	}


	@Override
	public Set<FeatureVariable> getAllVariables()
	{
		return Collections.singleton(this);
	}


	@Override
	public void replaceVariable(FeatureVariable variable, FeatureValue value)
	{
		// does nothing, the replacement must happen at an upper level
	}
}
