package synalp.commons.unification;

import java.util.*;

/**
 * A FeatureStructure is an unordered store for features.
 */
public class FeatureStructure extends FeatureValue
{
	private Map<String, Feature> features = new LinkedHashMap<String, Feature>();


	/**
	 * Creates an empty FeatureStructure.
	 */
	public FeatureStructure()
	{

	}


	/**
	 * Creates a FeatureStructure with given features.
	 * @param features
	 */
	public FeatureStructure(Feature... features)
	{
		for(Feature feat : features)
			add(feat);
	}


	/**
	 * Deep copies the given FeatureStructure.
	 * @param fs
	 */
	public FeatureStructure(FeatureStructure fs)
	{
		for(Feature feat : fs.getFeatures())
			add(new Feature(feat));
	}


	/**
	 * Replaces all occurrences of the old variable by the new variable.
	 * @param variable
	 * @param value
	 */
	public void replaceVariable(FeatureVariable variable, FeatureValue value)
	{
		for(Feature feat : features.values())
			switch (feat.getValue().getType())
			{
				case FEATURE_STRUCTURE:
					((FeatureStructure) feat.getValue()).replaceVariable(variable, value);
					break;

				case VARIABLE:
					if (feat.getValue().equals(variable))
						feat.setValue(value);
					break;
					
				case CONSTANT:
					break;
			}
	}


	/**
	 * Returns all variables that are defined in this FeatureStructure or one of its descendant
	 * structure.
	 */
	@Override
	public Set<FeatureVariable> getAllVariables()
	{
		Set<FeatureVariable> ret = new HashSet<FeatureVariable>();
		for(Feature feat : features.values())
			if (feat.getValue() != null)
				ret.addAll(feat.getValue().getAllVariables());
		return ret;
	}


	/**
	 * Adds the given Feature to this FeatureStructure. If there already exists a feature with the
	 * same name, it is overridden.
	 * @param feature
	 * @return this FeatureStructure for chaining
	 */
	public FeatureStructure add(Feature feature)
	{
		features.put(feature.getName(), feature);
		return this;
	}


	/**
	 * Convenience method to add a constant feature. If there already exists a feature with the same
	 * name, it is overriden.
	 * @param name
	 * @param values
	 * @return this FeatureStructure for chaining
	 */
	public FeatureStructure addConstantFeature(String name, String... values)
	{
		return add(new Feature(name, new FeatureConstant(values)));
	}


	/**
	 * Convenience method to add a constant feature. If there already exists a feature with the same
	 * name, it is overriden.
	 * @param name
	 * @param constant
	 * @return this FeatureStructure for chaining
	 */
	public FeatureStructure addConstantFeature(String name, FeatureConstant constant)
	{
		return add(new Feature(name, constant));
	}


	/**
	 * Convenience method to add a variable feature. If there already exists a feature with the same
	 * name, it is overriden.
	 * @param name the name of the new feature
	 * @param varName the name of the variable
	 * @return this FeatureStructure for chaining
	 */
	public FeatureStructure addVariableFeature(String name, String varName)
	{
		return add(new Feature(name, new FeatureVariable(varName)));
	}


	/**
	 * Convenience method to add a variable feature. If there already exists a feature with the same
	 * name, it is overriden.
	 * @param name the name of the new feature
	 * @param variable
	 * @return this FeatureStructure for chaining
	 */
	public FeatureStructure addVariableFeature(String name, FeatureVariable variable)
	{
		return add(new Feature(name, variable));
	}


	/**
	 * Convenience method to add a feature whose value is a FeatureStructure. If there already
	 * exists a feature with the same name, it is overriden.
	 * @param name the name of the new feature
	 * @param fs a feature structure
	 * @return this FeatureStructure for chaining
	 */
	public FeatureStructure addComplexFeature(String name, FeatureStructure fs)
	{
		return add(new Feature(name, fs));
	}


	/**
	 * Removes the feature that has the given name.
	 * @param name
	 * @return this FeatureStructure for chaining
	 */
	public FeatureStructure removeFeature(String name)
	{
		features.remove(name);
		return this;
	}


	/**
	 * Returns a list view of features of this FeatureStructure.
	 * @return a list view of features
	 */
	public List<Feature> getFeatures()
	{
		return new ArrayList<Feature>(features.values());
	}


	/**
	 * Tests if this FeatureStructure is empty.
	 * @return true if this FeatureStructure does not contain any Feature.
	 */
	public boolean isEmpty()
	{
		return features.isEmpty();
	}


	/**
	 * Returns a list view of all features of this FeatureStructure and all features that may be
	 * embedded more deeply.
	 * @return a list view of features
	 */
	public List<Feature> getAllFeaturesRecursively()
	{
		List<Feature> ret = new ArrayList<Feature>();
		for(Feature feat : features.values())
		{
			ret.add(feat);
			if (feat.getValue().getType() == FeatureValueType.FEATURE_STRUCTURE)
				ret.addAll(((FeatureStructure) feat.getValue()).getAllFeaturesRecursively());
		}
		return ret;
	}


	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		for(String feat : features.keySet())
			ret.append(features.get(feat)).append(" ");
		return "[" + ret.toString().trim() + "]";
	}


	@Override
	public String toString(InstantiationContext context)
	{
		StringBuilder ret = new StringBuilder();
		for(String feat : features.keySet())
			ret.append(features.get(feat).toString(context)).append(" ");
		return "[" + ret.toString().trim() + "]";
	}


	/**
	 * Returns a String representation of this FS, showing only the features that have a constant
	 * value or an instantiated value in the given context.
	 * @param context
	 * @return a String, or the empty string if there is no instantiated/constant feature
	 */
	public String toStringInstFS(InstantiationContext context)
	{
		StringBuilder ret = new StringBuilder();
		for(Feature feat : features.values())
		{
			FeatureValue val = context.getValue(feat.getValue());
			if (val instanceof FeatureConstant)
				ret.append(feat.toString(context)).append(" ");
		}

		if (ret.length() == 0)
			return "";
		else return "[" + ret.toString().trim() + "]";
	}


	/**
	 * Returns the value of the given feature name.
	 * @param feat
	 * @return null if not found
	 */
	public FeatureValue getValueOf(String feat)
	{
		if (features.containsKey(feat))
			return features.get(feat).getValue();
		else return null;
	}


	/**
	 * Returns the given feature by name.
	 * @param name
	 * @return null if not found
	 */
	public Feature getFeature(String name)
	{
		return features.get(name);
	}


	/**
	 * Tests whether this FeatureStructure has a Feature with given name.
	 * @param name
	 * @return true if this FeatureStructure has a Feature with given name
	 */
	public boolean hasFeature(String name)
	{
		return features.containsKey(name);
	}


	/**
	 * Tests whether this FeatureStructure has a Feature with given name and value considered as a
	 * constant.
	 * @param name
	 * @param value
	 * @return true if this FeatureStructure has a Feature with given name
	 */
	public boolean hasConstantFeature(String name, String value)
	{
		Feature feat = getFeature(name);
		if (feat == null)
			return false;
		FeatureValue val = feat.getValue();
		if (val == null || val.getType() != FeatureValueType.CONSTANT)
			return false;
		else return ((FeatureConstant) val).getFirstValue().equals(value);
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((features == null) ? 0 : features.hashCode());
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
		FeatureStructure other = (FeatureStructure) obj;
		if (features == null)
		{
			if (other.features != null)
				return false;
		}
		else if (!features.equals(other.features))
			return false;
		return true;
	}


	@Override
	public FeatureValueType getType()
	{
		return FeatureValueType.FEATURE_STRUCTURE;
	}


	@Override
	public FeatureStructure copy()
	{
		return new FeatureStructure(this);
	}

}
