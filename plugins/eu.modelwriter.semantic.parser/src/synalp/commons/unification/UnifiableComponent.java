package synalp.commons.unification;

import java.util.Set;

/**
 * An UnifiableComponent is an abstract Object that has variables.
 * @author adenis
 */
public interface UnifiableComponent
{
	/**
	 * Returns all variables of this UnifiableComponent.
	 * @return a set of variables
	 */
	public abstract Set<FeatureVariable> getAllVariables();


	/**
	 * Replaces all occurrences the variable by given value.
	 * @param variable
	 * @param value
	 */
	public abstract void replaceVariable(FeatureVariable variable, FeatureValue value);
	
	
	/**
	 * Instantiates this UnifiableComponent by replacing all variables whose value is defined
	 * in the given InstantiationContext by the corresponding value.
	 * @param context 
	 */
	public abstract void instantiate(InstantiationContext context);
}
