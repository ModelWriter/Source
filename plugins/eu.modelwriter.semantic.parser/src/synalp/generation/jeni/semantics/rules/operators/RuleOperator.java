package synalp.generation.jeni.semantics.rules.operators;

import java.util.*;

import synalp.commons.semantics.*;
import synalp.commons.unification.*;


/**
 * A RuleOperator is the super class for the operators Add, Del, Replace.
 * @author Alexandre Denis
 */
public abstract class RuleOperator
{
	// these are "constant-templates" hence variables to be instantiated as new constants
	// they are used to create new constants in the Add operator, imagine we have "literal1(?A ?New) literal2(?New)" where
	// ?A is an existing variable, and ?New has to be added but not as a variable but as a new constant
	protected Set<FeatureVariable> newConstants = new HashSet<FeatureVariable>();


	/**
	 * @param input
	 * @param context
	 */
	public abstract void apply(Semantics input, InstantiationContext context);


	/**
	 * @param names
	 */
	public void addConstants(String... names)
	{
		for(String name : names)
			newConstants.add(new FeatureVariable(name));
	}


	/**
	 * Creates a new constant for each variable that is not used anywhere in the given Semantics. It
	 * uses the variable raw name to derive the new constant name. This method alters the given context.
	 * @param semantics
	 * @param context 
	 */
	protected void createConstants(Semantics semantics, InstantiationContext context)
	{
		Set<String> allConstants = getAllConstantsValues(semantics);
		for(FeatureVariable var : newConstants)
		{
			FeatureConstant newConstant = createNewConstant(var.getName(), allConstants);
			context.put(var, newConstant);
		}
	}


	/**
	 * Creates a new FeatureConstant whose name is not found in the existing names.
	 * @param baseName
	 * @param existingNames
	 * @return a FeatureConstant
	 */
	private FeatureConstant createNewConstant(String baseName, Set<String> existingNames)
	{
		if (baseName.startsWith("?"))
			baseName = baseName.substring(1).toLowerCase();

		if (!existingNames.contains(baseName))
			return new FeatureConstant(baseName);
		else
		{
			int i = 0;
			while(existingNames.contains(baseName + i))
				i++;
			return new FeatureConstant(baseName + i);
		}
	}


	/**
	 * Returns all constants values of the given Semantics literals' arguments.
	 * @param semantics
	 * @return a set of constant values
	 */
	static Set<String> getAllConstantsValues(Semantics semantics)
	{
		Set<String> ret = new HashSet<String>();
		for(DefaultLiteral literal : semantics)
			for(FeatureValue value : literal.getArguments())
				if (value instanceof FeatureConstant)
					for(String constValue : ((FeatureConstant) value).getValues())
						ret.add(constValue.toLowerCase());
		return ret;
	}
}
