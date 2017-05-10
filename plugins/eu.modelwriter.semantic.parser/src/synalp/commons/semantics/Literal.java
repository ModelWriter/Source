package synalp.commons.semantics;

import java.util.List;

import synalp.commons.unification.*;


/**
 * @author Laura Perez
 */
public interface Literal extends UnifiableComponent
{

	/**
	 * Tests if this literal unifies with the given literal without a priori context.
	 * @param other
	 * @return whether the two literals unify
	 */
	public boolean unifies(Literal other);


	/**
	 * Tests if this literal unifies with the given literal with given context. Warning: the context
	 * may be modified by this method, we should copy it before testing and update it upon success.
	 * @param other
	 * @param context
	 * @return whether the two literals unify
	 */
	public boolean unifies(Literal other, InstantiationContext context);


	/**
	 * Gets the {@link String} instance representing the label of the predicate.
	 * @return the label
	 */
	public FeatureValue getLabel();


	/**
	 * Sets the {@link String} instance representing the label of the predicate.
	 * @param label the label to set
	 */
	public void setLabel(FeatureValue label);


	/**
	 * Gets the {@link String} instance representing the predicate of a literal.
	 * @return the predicate
	 */
	public FeatureValue getPredicate();


	/**
	 * Sets the {@link String} instance representing the predicate of a literal.
	 * @param predicate the predicate to set
	 */
	public void setPredicate(FeatureValue predicate);


	/**
	 * Gets the {@link FeatureValue} instance representing the whole set of arguments of the
	 * predicate.
	 * @return the arguments
	 */
	public List<FeatureValue> getArguments();


	/**
	 * Sets the {@link FeatureValue} instance representing the whole set of arguments of the
	 * predicate.
	 * @param arguments the arguments to set
	 */
	public void setArguments(List<FeatureValue> arguments);


	/**
	 * Returns the i-th argument of this Literal.
	 * @param index Index of the argument to get
	 * @return the argument
	 */
	public FeatureValue getArgument(int index);


	/**
	 * Adds an argument to this literal.
	 * @param arg
	 */
	public void addArgument(FeatureValue arg);


	/**
	 * Returns the arity of this Literal, that is its number of arguments.
	 * @return the arity of this Literal
	 */
	public int getArity();


	/**
	 * Tests if this Literal is selectional.
	 * @return whether this Literal is selectional
	 */
	public boolean isSelectional();


	/**
	 * Sets whether this Literal is selectional.
	 * @param selectional the selectional to set
	 */
	public void setSelectional(boolean selectional);

}
