package synalp.commons.unification;


/**
 * Super class for all feature values.
 * @author Alexandre Denis
 */
public abstract class FeatureValue implements UnifiableComponent
{
	/**
	 * Returns the type of this FeatureValue.
	 * @return a FeatureValueType
	 */
	public abstract FeatureValueType getType();


	/**
	 * Returns a deep copy of this FeatureValue.
	 * @return a deep copy of this FeatureValue.
	 */
	public abstract FeatureValue copy();


	/**
	 * Returns a String representation of this FeatureValue in the given context.
	 * @param context 
	 * @return a String
	 */
	public abstract String toString(InstantiationContext context);


	
	@Override
	public void instantiate(InstantiationContext context)
	{
		for(FeatureVariable var : getAllVariables())
			if (context.containsKey(var))
				replaceVariable(var, context.get(var));
	}
}
