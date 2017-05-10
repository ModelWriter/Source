package synalp.commons.lexicon;

import java.util.ArrayList;

import synalp.commons.lexicon.Equation;
import synalp.commons.lexicon.javacc.EquationReader;
import synalp.commons.unification.*;


/**
 * Equations is the class that represent a list of objects {@link Equation} in the syntactic lexicon
 * input file (.glex, .lex or .xml).
 * <p>
 * It extends {@link java.util.ArrayList}.
 * </p>
 * <p>
 * It is a list of doublets where each doublet corresponds to a constraint on the tree.
 * </p>
 * @author Alexandre Denis
 * @author Céline Moro
 * @version %I%, %G%
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Equations extends ArrayList<Equation>
{
	/**
	 * Read equations from given String. The format for each equation is <node name> "." <fs type
	 * (bot or top)> "=" "[" <feat value pairs> "]". Where <feat value pairs> are <feat> "="
	 * <value>. Equations are separated by space.
	 * @param str
	 * @return a list of equations
	 */
	public static Equations readEquations(String str)
	{
		try
		{
			return EquationReader.readEquations(str);
		}
		catch (Exception e)
		{
			System.err.println("Error while reading equation: '" + str + "' : " + e.getLocalizedMessage().replaceAll("\\s+", " "));
			return null;
		}
	}


	/**
	 * Creates empty equations.
	 */
	public Equations()
	{

	}


	/**
	 * Deep copies the given equations.
	 * @param equations
	 */
	public Equations(Equations equations)
	{
		for(Equation equation : equations)
			add(new Equation(equation));
	}


	/**
	 * Aggregates all the equations which are defined for the same pair (nodeId,fs type). This
	 * method modifies this set of equations and typically returns a smaller set of equations.
	 * @return this set of Equations for chaining
	 * @throws Exception
	 */
	public Equations aggregate() throws Exception
	{
		Equations ret = new Equations();
		for(Equation eq : this)
		{
			boolean found = false;
			for(Equation added : ret)
				if (eq.getType() == added.getType() && eq.getNodeId().equals(added.getNodeId()))
				{
					FeatureStructure unif = Unifier.unify(added.getFeatureStructure(), eq.getFeatureStructure());
					if (unif == null)
						throw new Exception("Error: a set of equations " + this + " is not consistent with unification");
					else added.setFeatureStructure(unif);
					found = true;
				}
			if (!found)
				ret.add(eq);
		}

		clear();
		addAll(ret);
		return this;
	}


	/**
	 * Returns a String representation of these equations in context.
	 * @param context
	 * @return a String
	 */
	public String toString(InstantiationContext context)
	{
		StringBuilder ret = new StringBuilder();
		if (isEmpty())
		{
			ret.append("[").append("]");
			return ret.toString();
		}
		else
		{
			ret.append("[");
			boolean first = true;
			for(Equation element : this)
				if (first)
				{
					ret.append(element.toString(context));
					first = false;
				}
				else ret.append(" ").append(element);
			ret.append("]");
			return ret.toString();
		}
	}
}
