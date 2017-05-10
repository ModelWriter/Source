package synalp.commons.semantics;

import java.util.*;

import synalp.commons.input.javacc.InputReader;
import synalp.commons.unification.*;

import com.google.common.collect.Sets;


/**
 * Semantics is a set of Literals. Operations are: <li>
 * <ul>
 * intersection: at least one literal of this Semantics unifies with one literal of the other
 * semantics
 * </ul>
 * <ul>
 * subsumption: all the information of this Semantics is contained in the other Semantics, that is,
 * all Literals of this Semantics unify with at least one literal of the other Semantics. It is not
 * the logical subsumption which would rather go the other way, that is this semantics would subsume
 * the other semantics if all the intended extension of this semantics would contain the intended
 * extension of the other semantics. We use unification, but we could use subsumption of FS
 * directly.
 * </ul>
 * <ul>
 * equivalence: two semantics are equivalent if they subsume each other.
 * </ul>
 * <ul>
 * equality: two semantics are equal if they are equivalent and have the same size. The equality is
 * stronger than equivalence. Compare for instance "foo(a|b)" and "foo(a) foo(b)", we consider that
 * they are equivalent but not equal.
 * </ul>
 * </li>
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class Semantics extends HashSet<DefaultLiteral> implements UnifiableComponent
{
	/**
	 * Reads the given Semantics from given String. Literals are separated with spaces. If the input
	 * String is not a Semantics, display an exception on stdout and return null. Example:
	 * "A:proper_q(C CR CS) qeq(CR F) F:named(C fiona_n) F:indiv(C f sg)".
	 * @param semantics
	 * @return a Semantics or null if it failed reading a proper semantics
	 */
	public static Semantics readSemantics(String semantics)
	{
		try
		{
			return InputReader.readSemantics(semantics);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Creates a new empty Semantics.
	 */
	public Semantics()
	{

	}


	/**
	 * Deep copies the given Semantics. Adds all literals if there are more than one semantics.
	 * @param semantics
	 */
	public Semantics(Semantics... semantics)
	{
		for(Semantics sem : semantics)
			for(DefaultLiteral literal : sem)
				add(new DefaultLiteral(literal));
	}


	/**
	 * Deep copies the given literals.
	 * @param literals
	 */
	public Semantics(Collection<DefaultLiteral> literals)
	{
		//addAll(literals);
		for(DefaultLiteral literal : literals)
			add(new DefaultLiteral(literal));
	}


	/**
	 * Returns an ordered list view of this Semantics.
	 * @return a list of literals
	 */
	public List<DefaultLiteral> asList()
	{
		return new ArrayList<DefaultLiteral>(this);
	}


	/**
	 * Flattens all disjunctions from this Semantics.
	 * @return the set of Semantics without disjunction
	 */
	public Set<Semantics> flattenDisjunctions()
	{
		List<Set<DefaultLiteral>> flattened = new ArrayList<Set<DefaultLiteral>>();
		for(DefaultLiteral literal : this)
			flattened.add(literal.flattenDisjunctions());
		Set<Semantics> ret = new HashSet<Semantics>();
		for(List<DefaultLiteral> element : Sets.cartesianProduct(flattened))
			ret.add(new Semantics(element));
		return ret;
	}


	/**
	 * Adds all the given semantics to this semantics. This method modifies this Semantics.
	 * @param semantics
	 * @return this Semantics for chaining
	 */
	public Semantics join(Collection<Semantics> semantics)
	{
		for(Semantics sem : semantics)
			addAll(sem);
		return this;
	}


	/**
	 * Reduces all the disjunctions of this Semantics to the values of given Semantics. It takes for
	 * each literal of this Semantics, the constants of the first unifying literal of the given
	 * Semantics.
	 * @param semantics
	 */
	public void reduceDisjunctions(Semantics semantics)
	{
		for(DefaultLiteral literal : this)
		{
			for(DefaultLiteral otherLiteral : semantics)
				if (literal.unifies(otherLiteral))
				{
					List<FeatureValue> newArgs = new ArrayList<FeatureValue>();
					for(FeatureValue arg : otherLiteral.getArguments())
						newArgs.add(arg.copy());
					literal.setArguments(newArgs);
					break;
				}
		}
	}


	/**
	 * Instantiates this semantics. It replaces all the variables if they have a value defined in
	 * the given InstantiationContext. This method modifies this Semantics.
	 * @param context
	 */
	@Override
	public void instantiate(InstantiationContext context)
	{
		for(DefaultLiteral literal : this)
			literal.instantiate(context);
	}


	@Override
	public void replaceVariable(FeatureVariable variable, FeatureValue value)
	{
		for(DefaultLiteral literal : this)
			literal.replaceVariable(variable, value);
	}


	@Override
	public Set<FeatureVariable> getAllVariables()
	{
		Set<FeatureVariable> ret = new HashSet<FeatureVariable>();
		for(DefaultLiteral literal : this)
			ret.addAll(literal.getAllVariables());
		return ret;
	}


	/**
	 * Tests if this semantics intersects the given semantics.
	 * @param semantics
	 * @param context
	 * @return true if there is at least one literal that belongs to both semantics, false otherwise
	 */
	public boolean semanticsIntersects(Semantics semantics, InstantiationContext context)
	{
		for(DefaultLiteral literal1 : this)
			for(DefaultLiteral literal2 : semantics)
				if (literal1.unifies(literal2, context))
					return true;
		return false;
	}


	/**
	 * Returns the intersection wrt the given context. It returns the set of literals (as a
	 * Semantics) of this Semantics that unify with at least one literal of the given Semantics.
	 * @param semantics
	 * @param context
	 * @return true if there is at least one literal that belongs to both semantics, false otherwise
	 */
	public Semantics intersection(Semantics semantics, InstantiationContext context)
	{
		Set<DefaultLiteral> literals = new HashSet<DefaultLiteral>();
		for(DefaultLiteral literal1 : this)
			for(DefaultLiteral literal2 : semantics)
				if (literal1.unifies(literal2, context))
					literals.add(literal1);
		return new Semantics(literals);
	}


	/**
	 * Tests if this semantics contains the given Literal modulo the context. Warning: it is not the
	 * same than the method contains() defined in the super class which only looks for Literals
	 * whose values are equal without considering context.
	 * @param literal
	 * @param context
	 * @return true if this semantics contains a literal that unifies with given literal in given
	 *         context
	 */
	public boolean contains(DefaultLiteral literal, InstantiationContext context)
	{
		for(DefaultLiteral literal1 : this)
			if (literal1.unifies(literal, context))
				return true;
		return false;
	}


	/**
	 * Returns all literals of this Semantics that cannot be unified with any literal of the given
	 * Semantics.
	 * @param semantics
	 * @return a list of literals of this Semantics that cannot be unified with any literal of the
	 *         given Semantics.
	 */
	public List<DefaultLiteral> difference(Semantics semantics)
	{
		return difference(semantics, new InstantiationContext());
	}


	/**
	 * Returns all literals of this Semantics that cannot be unified with any literal of the given
	 * Semantics in the given context.
	 * @param semantics
	 * @param context
	 * @return a list of literals of this Semantics that cannot be unified with any literal of the
	 *         given Semantics.
	 */
	public List<DefaultLiteral> difference(Semantics semantics, InstantiationContext context)
	{
		List<DefaultLiteral> ret = new ArrayList<DefaultLiteral>();
		for(DefaultLiteral literal : this)
			if (!semantics.contains(literal, context))
				ret.add(literal);
		return ret;
	}


	/**
	 * Tests if this Semantics subsumes the given Semantics. Instead of returning contexts, it
	 * returns true if there exists a context, and false otherwise. By default this method ignores
	 * selectional literals.
	 * @param semantics
	 * @return true if there exists a context in which this Semantics subsumes the given one
	 */
	public boolean doesSubsume(Semantics semantics)
	{
		return !subsumes(semantics, new InstantiationContext(), true).isEmpty();
	}


	/**
	 * Tests if this Semantics subsumes the given Semantics. Instead of returning contexts, it
	 * returns true if there exists a context, and false otherwise. By default this method ignores
	 * selectional literals.
	 * @param semantics
	 * @param context
	 * @return true if there exists a context in which this Semantics subsumes the given one
	 */
	public boolean doesSubsume(Semantics semantics, InstantiationContext context)
	{
		return !subsumes(semantics, context, true).isEmpty();
	}


	/**
	 * Tests if this Semantics subsumes the given Semantics. It returns the set of contexts in which
	 * this semantics subsumes the given one. By default this method ignores selectional literals.
	 * @param semantics
	 * @return a set of contexts or an empty set if this Semantics does not subsume the given one
	 */
	public Set<InstantiationContext> subsumes(Semantics semantics)
	{
		return subsumes(semantics, new InstantiationContext(), true);
	}


	/**
	 * Tests if this Semantics subsumes the given Semantics. It returns the set of contexts in which
	 * this semantics subsumes the given one.
	 * @param semantics
	 * @param ignoreSelectionalLiterals if true selectional literals are ignored when testing
	 *            subsumption
	 * @return a set of contexts or an empty set if this Semantics does not subsume the given one
	 */
	public Set<InstantiationContext> subsumes(Semantics semantics, boolean ignoreSelectionalLiterals)
	{
		return subsumes(semantics, new InstantiationContext(), ignoreSelectionalLiterals);
	}


	/**
	 * Tests if this Semantics subsumes the given Semantics. It returns the set of contexts in which
	 * this semantics subsumes the given one. By default this method ignores selectional literals.
	 * @param semantics
	 * @param context
	 * @return a set of contexts or an empty set if this Semantics does not subsume the given one
	 */
	public Set<InstantiationContext> subsumes(Semantics semantics, InstantiationContext context)
	{
		return subsumes(semantics, context, true);
	}


	/**
	 * Tests if this Semantics subsumes the given Semantics. It returns the set of contexts in which
	 * this semantics subsumes the given one.
	 * @param semantics
	 * @param context
	 * @param ignoreSelectionalLiterals if true selectional literals are ignored when testing
	 *            subsumption
	 * @return a set of contexts or an empty set if this Semantics does not subsume the given one
	 */
	public Set<InstantiationContext> subsumes(Semantics semantics, InstantiationContext context, boolean ignoreSelectionalLiterals)
	{
		return SemanticsSubsumer.subsumes(this, semantics, context, ignoreSelectionalLiterals);
	}


	/**
	 * Tests if this Semantics equals the given semantics considering the given instantiation
	 * context. Two semantics are equal if they have same size and they are equivalent.
	 * @param semantics
	 * @param context
	 * @return true if both Semantics contains Literals that unify
	 */
	public boolean equals(Semantics semantics, InstantiationContext context)
	{
		if (semantics.size() != this.size())
			return false;
		else return isEquivalentTo(semantics, context);
	}


	/**
	 * Tests if this Semantics is equivalent to the given semantics considering the given
	 * instantiation context. Two semantics are equivalent if they subsume each other.
	 * @param semantics
	 * @param context
	 * @return true if both Semantics contains Literals that unify
	 */
	public boolean isEquivalentTo(Semantics semantics, InstantiationContext context)
	{
		if (subsumes(semantics, context).isEmpty())
			return false;
		if (semantics.subsumes(this, context).isEmpty())
			return false;
		return true;
	}


	/**
	 * Removes all the literals from this Semantics that unify with a literal of given Semantics.
	 * @param semantics
	 */
	public void removeAll(Semantics semantics)
	{
		removeAll(semantics, new InstantiationContext());
	}


	/**
	 * Removes all the literals from this Semantics that unify with a literal of given Semantics in
	 * given context.
	 * @param semantics
	 * @param context
	 */
	public void removeAll(Semantics semantics, InstantiationContext context)
	{
		for(Iterator<DefaultLiteral> it = iterator(); it.hasNext();)
			if (semantics.contains(it.next(), context))
				it.remove();
	}


	/**
	 * Removes selectional literals from this Semantics. This method alters this Semantics.
	 */
	public void removeSelectionalLiterals()
	{
		for(Iterator<DefaultLiteral> it = iterator(); it.hasNext();)
			if (it.next().isSelectional())
				it.remove();
	}


	/**
	 * Adds all the literals of the given Semantics to this Semantics without doublons. The doublons
	 * are tested with literal unification in an empty context.
	 * @param semantics
	 */
	public void addNoDoublons(Semantics semantics)
	{
		InstantiationContext context = new InstantiationContext();
		for(DefaultLiteral literal : semantics)
			if (!contains(literal, context))
				add(literal);
	}


	/**
	 * Adds all the literals of the given Semantics to this Semantics without doublons. The doublons
	 * are tested with the DefaultLiteral equalNames method.
	 * @param semantics
	 */
	public void addNoDoublonsNames(Semantics semantics)
	{
		for(DefaultLiteral literal : semantics)
		{
			boolean found = false;
			for(DefaultLiteral thisLit : this)
				if (literal.equalNames(thisLit))
				{
					found=true;
					break;
				}
			if (!found)
				add(literal);
		}
	}


	@Override
	public String toString()
	{
		if (isEmpty())
			return "";
		else
		{
			StringBuilder ret = new StringBuilder();
			for(DefaultLiteral literal : this)
				ret.append(literal).append(" ");
			return ret.toString().trim();
		}
	}


	/**
	 * Returns a String representation of this Semantics in given context.
	 * @param context
	 * @return a String
	 */
	public String toString(InstantiationContext context)
	{
		if (isEmpty())
			return "";
		else
		{
			StringBuilder ret = new StringBuilder();
			for(DefaultLiteral literal : this)
				ret.append(literal.toString(context)).append(" ");
			return ret.toString().trim();
		}
	}


	/**
	 * Returns a String representation of this Semantics in given context.
	 * @param context
	 * @return a String
	 */
	public String toStringSorted(InstantiationContext context)
	{
		if (isEmpty())
			return "";
		else
		{
			final Map<DefaultLiteral, String> strings = new HashMap<DefaultLiteral, String>();
			for(DefaultLiteral literal : this)
				strings.put(literal, literal.toString(context));

			List<DefaultLiteral> literals = new ArrayList<DefaultLiteral>(this);
			Collections.sort(literals, new Comparator<DefaultLiteral>()
			{
				public int compare(DefaultLiteral arg0, DefaultLiteral arg1)
				{
					return strings.get(arg0).compareTo(strings.get(arg1));
				}
			});

			StringBuilder ret = new StringBuilder();
			for(DefaultLiteral literal : literals)
				ret.append(strings.get(literal)).append(" ");
			return ret.toString().trim();
		}
	}
}
