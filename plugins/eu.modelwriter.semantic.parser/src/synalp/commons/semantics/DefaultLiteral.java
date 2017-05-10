package synalp.commons.semantics;

import java.util.*;

import synalp.commons.input.javacc.InputReader;
import synalp.commons.unification.*;

import com.google.common.collect.Sets;

import static synalp.commons.unification.FeatureValueType.*;

/**
 * Implements literals in a flat semantic representation used to specify the meaning representation
 * to be verbalised. Since the elements of a Literal can be used in unification as constants or
 * variables, its label, predicate and arguments are represented as FeatureValue. Note that
 * FeatureValue can also be a FeatureStructure, we may create a super class gathering
 * FeatureVariable and FeatureConstant to prevent that. A DefaultLiteral can be "selectional" which
 * means that the literal is not truly part of a semantics but is used to select trees.
 * 
 * <pre>
 * example of a literal : 
 * {@code A:bake(B C D)}
 * </pre>
 * @author Céline Moro
 * @author Alexandre Denis
 * @version %I%, %G%
 * @since 1.0
 */
public class DefaultLiteral implements Literal
{
	/**
	 * If true, labels are discarded when testing unification of literals.
	 */
	public static boolean IGNORE_LABELS = false;

	private boolean selectional;
	private FeatureValue label = new FeatureConstant("");
	private FeatureValue predicate = new FeatureConstant("");
	private List<FeatureValue> arguments = new ArrayList<FeatureValue>();


	/**
	 * Reads the given Literal from given String. If the input String is not a Literal, display an
	 * exception on stdout and return null. Example: "A:proper_q(?C CR CS)".
	 * @param literal
	 * @return a Semantics or null if it failed reading a proper semantics
	 */
	public static DefaultLiteral readLiteral(String literal)
	{
		try
		{
			return InputReader.readLiteral(literal);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Creates a literal from given list of values. The first element is considered as the label,
	 * the second as the predicate and the rest as arguments. It has been designed as a static
	 * method to avoid confusion.
	 * @param values
	 * @param selectional whether the new literals is selectional
	 * @return a new DefaultLiteral or null if the input list does not contain enough values.
	 */
	@SuppressWarnings("unchecked")
	private static DefaultLiteral createLiteral(List<? extends FeatureValue> values, boolean selectional)
	{
		if (values.size() < 2)
			return null;
		else return new DefaultLiteral(values.get(0), values.get(1), (List<FeatureValue>) values.subList(2, values.size()), selectional);
	}


	/**
	 * Zero constructor.
	 */
	public DefaultLiteral()
	{

	}


	/**
	 * Creates a 0-ary predicate without label.
	 * @param predicate
	 */
	public DefaultLiteral(FeatureValue predicate)
	{
		this.predicate = predicate;
	}


	/**
	 * Creates a new Literal given a label, predicate and arguments.
	 * @param label
	 * @param predicate
	 * @param arguments
	 */
	public DefaultLiteral(FeatureValue label, FeatureValue predicate, FeatureValue... arguments)
	{
		this.label = label;
		this.predicate = predicate;
		this.arguments = Arrays.asList(arguments);
	}


	/**
	 * Creates a new Literal given a label, predicate and arguments.
	 * @param label
	 * @param predicate
	 * @param arguments
	 * @param selectional
	 */
	public DefaultLiteral(FeatureValue label, FeatureValue predicate, List<FeatureValue> arguments, boolean selectional)
	{
		this.label = label;
		this.predicate = predicate;
		this.arguments = arguments;
		this.selectional = selectional;
	}


	/**
	 * Deep copies the given Literal.
	 * @param literal
	 */
	public DefaultLiteral(Literal literal)
	{
		this.selectional = literal.isSelectional();
		this.label = literal.getLabel().copy();
		this.predicate = literal.getPredicate().copy();
		for(FeatureValue arg : literal.getArguments())
			addArgument(arg.copy());
	}


	/**
	 * Tests if this DefaultLiteral equals the given DefaultLiteral. This is an equals method based
	 * on the FeatureValue equals, it considers the variable names and not their value.
	 * @param other 
	 * @return true if this DefaultLiteral equals the given DefaultLiteral 
	 */
	public boolean equalNames(DefaultLiteral other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (arguments == null)
		{
			if (other.arguments != null)
				return false;
		}
		else if (!arguments.equals(other.arguments))
			return false;
		if (label == null)
		{
			if (other.label != null)
				return false;
		}
		else if (!label.equals(other.label))
			return false;
		if (predicate == null)
		{
			if (other.predicate != null)
				return false;
		}
		else if (!predicate.equals(other.predicate))
			return false;
		if (selectional != other.selectional)
			return false;
		return true;
	}


	/**
	 * Flattens all disjunctions.
	 * @return a set of literals in which there is no disjunctions.
	 */
	public Set<DefaultLiteral> flattenDisjunctions()
	{
		Set<DefaultLiteral> ret = new HashSet<DefaultLiteral>();
		List<Set<? extends FeatureValue>> flattened = unrollDisjunctions();
		for(List<? extends FeatureValue> element : Sets.cartesianProduct(flattened))
			ret.add(createLiteral(element, selectional));
		return ret;
	}


	/**
	 * Creates a list of feature value sets. Each element of the list corresponds to alternative
	 * values for: label, predicate and arguments. The returned list has at least two elements.
	 * @return a list of set of alternative values for each element of this literal.
	 */
	private List<Set<? extends FeatureValue>> unrollDisjunctions()
	{
		List<Set<? extends FeatureValue>> ret = new ArrayList<Set<? extends FeatureValue>>();

		if (label instanceof FeatureConstant)
			ret.add(((FeatureConstant) label).flattenDisjunctions());
		else ret.add(Collections.singleton(label));

		if (predicate instanceof FeatureConstant)
			ret.add(((FeatureConstant) predicate).flattenDisjunctions());
		else ret.add(Collections.singleton(predicate));

		for(FeatureValue arg : arguments)
			if (arg instanceof FeatureConstant)
				ret.add(((FeatureConstant) arg).flattenDisjunctions());
			else ret.add(Collections.singleton(arg));

		return ret;
	}


	@Override
	public void instantiate(InstantiationContext context)
	{
		for(FeatureVariable var : getAllVariables())
			if (context.containsKey(var))
				replaceVariable(var, context.get(var));
	}


	@Override
	public void replaceVariable(FeatureVariable variable, FeatureValue value)
	{
		if (label.equals(variable))
			label = value;
		if (predicate.equals(variable))
			predicate = value;
		List<FeatureValue> newArgs = new ArrayList<FeatureValue>();
		for(FeatureValue arg : arguments)
			if (arg.equals(variable))
				newArgs.add(value);
			else newArgs.add(arg);
		this.arguments = newArgs;
	}


	@Override
	public Set<FeatureVariable> getAllVariables()
	{
		Set<FeatureVariable> ret = new HashSet<FeatureVariable>();
		if (label != null && label.getType() == VARIABLE)
			ret.add((FeatureVariable) label);
		if (predicate.getType() == VARIABLE)
			ret.add((FeatureVariable) predicate);
		for(FeatureValue arg : arguments)
			if (arg.getType() == VARIABLE)
				ret.add((FeatureVariable) arg);
		return ret;
	}


	@Override
	public boolean unifies(Literal other)
	{
		return unifies(other, new InstantiationContext());
	}


	@Override
	public boolean unifies(Literal other, InstantiationContext context)
	{
		if (this.getArity() != other.getArity())
			return false;

		if (!IGNORE_LABELS)
			if (Unifier.unify(this.getLabel(), other.getLabel(), context) == null)
				return false;

		if (Unifier.unify(this.getPredicate(), other.getPredicate(), context) == null)
			return false;

		for(int i = 0; i < this.getArguments().size(); i++)
			if (Unifier.unify(this.getArgument(i), other.getArgument(i), context) == null)
				return false;

		return true;
	}


	@Override
	public FeatureValue getLabel()
	{
		return label;
	}


	@Override
	public void setLabel(FeatureValue label)
	{
		this.label = label;
	}


	@Override
	public FeatureValue getPredicate()
	{
		return predicate;
	}


	@Override
	public void setPredicate(FeatureValue predicate)
	{
		this.predicate = predicate;
	}


	@Override
	public List<FeatureValue> getArguments()
	{
		return arguments;
	}


	@Override
	public void setArguments(List<FeatureValue> arguments)
	{
		this.arguments = arguments;
	}


	@Override
	public FeatureValue getArgument(int index)
	{
		return arguments.get(index);
	}


	@Override
	public void addArgument(FeatureValue arg)
	{
		arguments.add(arg);
	}


	@Override
	public int getArity()
	{
		return arguments.size();
	}


	@Override
	public String toString()
	{
		StringBuilder args = new StringBuilder();
		for(FeatureValue arg : arguments)
			args.append(arg).append(" ");

		return (isSelectional() ? "~" : "") + (label.toString().isEmpty() ? "" : label + ":") + predicate + "(" + args.toString().trim() + ")";
	}


	/**
	 * Returns a String representation of this Literal in given context.
	 * @param context
	 * @return a String
	 */
	public String toString(InstantiationContext context)
	{
		StringBuilder args = new StringBuilder();
		for(FeatureValue arg : arguments)
			args.append(arg.toString(context)).append(" ");

		return (isSelectional() ? "~" : "") + (label.toString().isEmpty() ? "" : label.toString(context) + ":") + predicate.toString(context) + "(" +
				args.toString().trim() + ")";
	}


	@Override
	public boolean isSelectional()
	{
		return selectional;
	}


	@Override
	public void setSelectional(boolean selectional)
	{
		this.selectional = selectional;
	}
}
