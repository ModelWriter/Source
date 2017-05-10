package synalp.commons.unification;

import java.util.*;

/**
 * An InstantiationContext keeps the instantiations of variables while performing a feature
 * structure unification.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class InstantiationContext extends HashMap<FeatureVariable, FeatureValue> implements UnifiableComponent
{

	/**
	 * Creates an empty InstantiationContext.
	 */
	public InstantiationContext()
	{

	}
	
	
	/**
	 * Returns a new variable which does not exist in this context.
	 * @return a new variable not appearing in this context
	 */
	public FeatureVariable getNewVariable()
	{
		int id=0;
		while(containsKey(new FeatureVariable("?VVV"+id)))
			id++;
		return new FeatureVariable("?VVV"+id);
	}


	/**
	 * Creates an InstantiationContext by aggregating the given contexts. The order of the arguments
	 * matter, a variable instantiation in a latter context can override a variable instantiation in
	 * a former context. This method only aggregates the contexts together, it does not perform a
	 * copy of variables or values.
	 * @param contexts
	 */
	public InstantiationContext(InstantiationContext... contexts)
	{
		for(InstantiationContext context : contexts)
			putAll(context);
	}


	/**
	 * Tests if this context is compatible with the given context. That is, there is no variable
	 * that has a different value in the two contexts. Note: it does not try to perform unification
	 * of the values but only equality.
	 * @param context
	 * @return false if there exists a variable in this context whose value is not equal to the
	 *         value of this variable in the given context (and vice versa).
	 */
	public boolean isCompatible(InstantiationContext context)
	{
		for(FeatureVariable var : keySet())
		{
			FeatureValue val1 = get(var);
			FeatureValue val2 = context.get(var);
			if (val1 != null && val2 != null && !val1.equals(val2))
				return false;
		}

		for(FeatureVariable var : context.keySet())
		{
			FeatureValue val1 = context.get(var);
			FeatureValue val2 = get(var);
			if (val1 != null && val2 != null && !val1.equals(val2))
				return false;
		}

		return true;
	}
	
	
	/**
	 * Tests if this context is compatible with the given context and variables. That is, there is no variable
	 * that has a different value in the two contexts. Note: it does not try to perform unification
	 * of the values but only equality.
	 * @param context
	 * @param vars a set of variables of this context
	 * @return false if there exists a variable in this context whose value is not equal to the
	 *         value of this variable in the given context (and vice versa).
	 */
	public boolean isCompatible(InstantiationContext context, Collection<FeatureVariable> vars)
	{
		for(FeatureVariable var : vars)
		{
			FeatureValue val1 = get(var);
			FeatureValue val2 = context.get(var);
			if (val1 != null && val2 != null && !val1.equals(val2))
				return false;
		}

		for(FeatureVariable var : vars)
		{
			FeatureValue val1 = context.get(var);
			FeatureValue val2 = get(var);
			if (val1 != null && val2 != null && !val1.equals(val2))
				return false;
		}

		return true;
	}


	/**
	 * Convenience method to put an instantiation.
	 * @param varName
	 * @param value
	 * @return this InstantiationContext for chaining
	 */
	public InstantiationContext set(String varName, FeatureValue value)
	{
		put(new FeatureVariable(varName), value);
		return this;
	}


	/**
	 * Returns the value of the given FeatureValue in this context. If the FeatureValue is a
	 * constant or a feature structure, returns it. If the FeatureValue is a variable, returns its
	 * instantiation if it exists, or returns the variable if it is not found.
	 * @param value
	 * @return a FeatureValue
	 */
	public FeatureValue getValue(FeatureValue value)
	{
		if (value.getType() == FeatureValueType.VARIABLE && containsKey(value))
			return get(value);
		else return value;
	}


	/**
	 * Returns a deep copy of this InstantiationContext. It copies all variables and all values.
	 * Note that variable copying is probably not necessary since its only field is final.
	 * @return a deep copy of this InstantiationContext
	 */
	public InstantiationContext copy()
	{
		InstantiationContext ret = new InstantiationContext();
		for(FeatureVariable var : keySet())
			ret.put(var.copy(), get(var).copy());
		return ret;
	}


	/**
	 * Removes all variables of this InstantiationContext that do not appear in the given unifiable
	 * components.
	 * @param components
	 */
	public void clean(UnifiableComponent... components)
	{
		List<Set<FeatureVariable>> allVars = new ArrayList<Set<FeatureVariable>>();
		for(UnifiableComponent component : components)
			allVars.add(component.getAllVariables());

		for(FeatureVariable var : new ArrayList<FeatureVariable>(this.keySet()))
		{
			boolean found = false;
			for(Set<FeatureVariable> vars : allVars)
			{
				if (vars.contains(var))
				{
					found = true;
					break;
				}
			}

			if (!found)
				remove(var);
		}
	}


	/**
	 * Replaces the old variable by the given value. It not only replaces the keys but also the
	 * variables that appear in values.
	 * @param variable
	 * @param value
	 */
	@Override
	public void replaceVariable(FeatureVariable variable, FeatureValue value)
	{
		// replace the variable in the feature structures
		for(FeatureValue val : values())
			if (val.getType() == FeatureValueType.FEATURE_STRUCTURE)
				((FeatureStructure) val).replaceVariable(variable, value);

		// replace the variable in the keys
		if (value instanceof FeatureVariable)
		{
			FeatureVariable var = (FeatureVariable) value;
			if (containsKey(variable))
			{
				put(var, get(variable));
				remove(variable);
			}
		}
	}


	/**
	 * Returns all the variables in the instantiation context. It not only returns the keys but also
	 * all the variables that appear in values.
	 * @return a set of variables
	 */
	@Override
	public Set<FeatureVariable> getAllVariables()
	{
		Set<FeatureVariable> ret = new HashSet<FeatureVariable>();
		ret.addAll(keySet());
		for(FeatureValue value : values())
			ret.addAll(value.getAllVariables());
		return ret;
	}


	/**
	 * This method has been added for coherence reason. I do not think it will be of practical use.
	 */
	@Override
	public void instantiate(InstantiationContext context)
	{
		for(FeatureVariable var : getAllVariables())
			if (context.containsKey(var))
				replaceVariable(var, context.get(var));
	}


	/**
	 * Returns a string representation of this context sorted by variable.
	 * @return a string
	 */
	public String toStringSorted()
	{
		List<FeatureVariable> sorted = new ArrayList<FeatureVariable>(keySet());
		Collections.sort(sorted, new Comparator<FeatureVariable>(){
			public int compare(FeatureVariable arg0, FeatureVariable arg1)
			{
				return arg0.getName().compareTo(arg1.getName());
			}});
		StringBuilder ret = new StringBuilder("{");
		if (!sorted.isEmpty())
		{
			ret.append(sorted.get(0)).append("=").append(get(sorted.get(0)));
			for(int i=1; i<sorted.size(); i++)
				ret.append(", ").append(sorted.get(i)).append("=").append(get(sorted.get(i)));
		}
		ret.append("}");
		return ret.toString();
	}
}
