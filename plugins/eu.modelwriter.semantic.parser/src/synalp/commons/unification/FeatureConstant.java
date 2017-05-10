package synalp.commons.unification;

import java.util.*;

/**
 * A constant value in a feature. The case dependency is controlled by a public flag, if true, all
 * the values are set to lower case.
 * @author Alexandre Denis
 */
public class FeatureConstant extends FeatureValue
{
	/**
	 * If true, displays single values with brackets, if false, displays single values without
	 * brackets
	 */
	public static boolean DISPLAY_ATOM_BRACKETS = false;

	/**
	 * If true, consider the case while matching, if false do not consider the case.
	 */
	public static boolean CASE_DEPENDENT = false;

	// warning, while the order provided with LinkedHashSet is nice for the display it may be problematic for equality
	private Set<String> values = new HashSet<String>();


	/**
	 * Creates a new FeatureConstant based on the given disjunction of values.
	 * @param values
	 */
	public FeatureConstant(String... values)
	{
		if (CASE_DEPENDENT)
			this.values.addAll(Arrays.asList(values));
		else this.values.addAll(toLower(new HashSet<String>(Arrays.asList(values))));
	}


	/**
	 * @param values
	 */
	public FeatureConstant(Collection<String> values)
	{
		if (CASE_DEPENDENT)
			this.values = new HashSet<String>(values);
		else this.values = toLower(new HashSet<String>(values));
	}


	/**
	 * Deep copies the given constant.
	 * @param constant
	 */
	public FeatureConstant(FeatureConstant constant)
	{
		this.values = new HashSet<String>(constant.getValues());
	}


	/**
	 * Returns the list of values that intersect with given constant.
	 * @param c
	 * @return a list of values
	 */
	public Set<String> getIntersection(FeatureConstant c)
	{
		Set<String> ret = new HashSet<String>(values);
		ret.retainAll(c.getValues());
		return ret;
	}


	/**
	 * Returns a set view of the values of this constant. The returned set is not backed to the
	 * actual set of values to prevent conflicts with the CASE_DEPENDENT setting.
	 * @return a set of values
	 */
	public Set<String> getValues()
	{
		return new HashSet<String>(values);
	}


	/**
	 * Returns the first value of this constant.
	 * @return the value or null if this constant has no values
	 */
	public String getFirstValue()
	{
		if (values.isEmpty())
			return null;
		else return values.iterator().next();
	}


	/**
	 * Tests if this FeatureConstant is a disjunction of values.
	 * @return whether it contains more than one value
	 */
	public boolean isDisjunction()
	{
		return values.size() > 1;
	}


	/**
	 * Adds the given value to this constant.
	 * @param value
	 */
	public void addValue(String value)
	{
		if (CASE_DEPENDENT)
			values.add(value);
		else values.add(value.toLowerCase());
	}


	@Override
	public String toString()
	{
		if (this.values.isEmpty())
			return "{}";
		else if (this.values.size() == 1)
		{
			if (DISPLAY_ATOM_BRACKETS)
				return "{" + values.iterator().next() + "}";
			else return values.iterator().next();
		}
		else
		{
			List<String> values = new ArrayList<String>(this.values);
			StringBuilder ret = new StringBuilder();
			ret.append(values.get(0));
			for(int i = 1; i < values.size(); i++)
				ret.append(",").append(values.get(i));
			return "{" + ret + "}";
		}
	}


	@Override
	public String toString(InstantiationContext context)
	{
		return toString();
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
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
		FeatureConstant other = (FeatureConstant) obj;
		if (values == null)
		{
			if (other.values != null)
				return false;
		}
		else
		{
			if (!values.equals(other.values))
				return false;
		}

		return true;
	}


	@Override
	public FeatureValueType getType()
	{
		return FeatureValueType.CONSTANT;
	}


	@Override
	public FeatureConstant copy()
	{
		return new FeatureConstant(this);
	}


	@Override
	public Set<FeatureVariable> getAllVariables()
	{
		return new HashSet<FeatureVariable>();
	}


	/**
	 * Lowers the given set of strings.
	 * @param values
	 * @return a set of lower case strings
	 */
	private static Set<String> toLower(Set<String> values)
	{
		Set<String> ret = new HashSet<String>();
		for(String value : values)
			ret.add(value.toLowerCase());
		return ret;
	}


	/**
	 * Creates a new FeatureConstant for each value of this constant.
	 * @return a set of constants containing a constant for each value
	 */
	public Set<FeatureConstant> flattenDisjunctions()
	{
		if (values.size() == 1)
			return Collections.singleton(this);

		Set<FeatureConstant> ret = new HashSet<FeatureConstant>();
		for(String value : values)
			ret.add(new FeatureConstant(value));
		return ret;
	}


	@Override
	public void replaceVariable(FeatureVariable variable, FeatureValue value)
	{
		// does nothing
	}
}
