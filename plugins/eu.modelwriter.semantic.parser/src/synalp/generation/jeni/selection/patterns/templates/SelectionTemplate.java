package synalp.generation.jeni.selection.patterns.templates;

import synalp.commons.grammar.*;
import synalp.commons.input.Lemma;
import synalp.commons.lexicon.Equations;
import synalp.commons.unification.*;
import synalp.generation.jeni.selection.patterns.*;

/**
 * A SelectionTemplate is a template for selecting entries.
 * @author Alexandre Denis
 */
public class SelectionTemplate
{
	private Trace trace = new Trace(); // the trace
	private Equations equations = new Equations(); // equations
	private FeatureValue lemmaValue = null; // the value of the lemma, either a constant or a variable
	private SelectionConstraint varConstraint = null; // a constraint on a variable


	/**
	 * Returns the lemma of this SelectionTemplate. If the lemmaValue is a constant, returns a Lemma
	 * from it. If the lemmaValue is a variable, retrieves it from the context. If there is no
	 * lemmaValue, retrieve the value of the LEMMA_VAR from the context.
	 * @param context
	 * @return null if no lemma is found
	 */
	public Lemma getLemma(InstantiationContext context)
	{
		if (lemmaValue == null)
		{
			if (context.containsKey(SelectionPattern.LEMMA_VAR))
				return new Lemma(context.get(SelectionPattern.LEMMA_VAR).toString());
			else return null;
		}
		else return new Lemma(context.getValue(lemmaValue).toString());
	}


	/**
	 * @return the equations
	 */
	public Equations getEquations()
	{
		return equations;
	}


	/**
	 * @return the trace
	 */
	public Trace getTrace()
	{
		return trace;
	}


	/**
	 * Tests if this SelectionTemplate has a non empty trace.
	 * @return true if this SelectionTemplate has a non empty trace
	 */
	public boolean hasTrace()
	{
		return !trace.isEmpty();
	}


	/**
	 * @return the coselection
	 */
	public boolean isCoselection()
	{
		return this instanceof CoselectionTemplate;
	}


	/**
	 * Tests if this SelectionTemplate is satisfied in the given context.
	 * @param context
	 * @return false if there exists an unsatisfied variable constraint, true otherwise.
	 */
	public boolean isSatisfied(InstantiationContext context)
	{
		if (varConstraint == null)
			return true;
		else return varConstraint.isSatisfied(context);
	}


	/**
	 * @param trace the trace to set
	 */
	public void setTrace(Trace trace)
	{
		this.trace = trace;
	}


	/**
	 * @param equations the equations to set
	 */
	public void setEquations(Equations equations)
	{
		this.equations = equations;
	}


	/**
	 * @param lemmaValue the lemmaValue to set
	 */
	public void setLemmaValue(FeatureValue lemmaValue)
	{
		this.lemmaValue = lemmaValue;
	}


	/**
	 * @param varConstraint the varConstraint to set
	 */
	public void setVarConstraint(SelectionConstraint varConstraint)
	{
		this.varConstraint = varConstraint;
	}


	/**
	 * Returns the entries selected by this template's trace.
	 * @param grammar
	 * @return entries
	 */
	public GrammarEntries getEntries(Grammar grammar)
	{
		if (!hasTrace())
			return new GrammarEntries();
		else return grammar.getEntriesContainingTrace(trace);
	}


	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		if (isCoselection())
			ret.append("coselect ");
		else ret.append("select ");
		if (!trace.isEmpty())
			ret.append("trace ").append(trace);
		if (!equations.isEmpty())
			ret.append(" eq ").append(equations);
		if (varConstraint != null)
			ret.append(" when ").append(varConstraint);
		if (lemmaValue != null)
			ret.append(" lemma ").append(lemmaValue);
		return ret.toString();
	}
}
