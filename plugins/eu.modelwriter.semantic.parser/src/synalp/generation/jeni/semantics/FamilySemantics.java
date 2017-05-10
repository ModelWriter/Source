package synalp.generation.jeni.semantics;

import java.util.*;

import synalp.commons.grammar.Trace;
import synalp.commons.input.Lemma;
import synalp.commons.lexicon.*;
import synalp.commons.semantics.Semantics;
import synalp.commons.unification.*;


/**
 * A FamilySemantics is the structure that associates a trace to a semantics and lemmas. Lemmas are
 * associated to an instantiation context in case we want to reify part of the semantics for the
 * lemmas. Semantics conventions are: ?A is the anchor, ?F the foot, ?R the root, ?S0..?Sn the
 * substitutions, and ?L the lemma. A FamilySemantics can be a pattern, that is, it does not specify
 * the semantics of the trace but only warns that the semantics need to be applied to the lemma.
 * @author Alexandre Denis
 */
public class FamilySemantics
{
	private Trace trace;
	private boolean isPattern;
	private Semantics semantics = new Semantics();
	private Equations familyEquations = new Equations();
	private Map<Lemma, InstantiationContext> lemmas = new HashMap<Lemma, InstantiationContext>();
	private Map<Lemma, List<Lemma>> coanchors = new HashMap<Lemma, List<Lemma>>();
	private Map<Lemma, Equations> equations = new HashMap<Lemma, Equations>();


	/**
	 * Creates an empty FamilySemantics.
	 */
	public FamilySemantics()
	{

	}


	/**
	 * @param trace
	 * @param semantics
	 * @param lemmas
	 */
	public FamilySemantics(Trace trace, Semantics semantics, Map<Lemma, InstantiationContext> lemmas)
	{
		this.trace = trace;
		this.semantics = semantics;
		this.lemmas = lemmas;
	}


	/**
	 * Adds the given lemmas with a default context in which ?L is assigned to the lemma value.
	 * @param lemmas
	 */
	public void addAll(Lemma... lemmas)
	{
		for(Lemma lemma : lemmas)
			add(lemma, new InstantiationContext());
	}


	/**
	 * Adds the given lemma with the given context.
	 * @param lemma
	 * @param context
	 */
	public void add(Lemma lemma, InstantiationContext context)
	{
		this.lemmas.put(lemma, context);
		this.equations.put(lemma, new Equations());
		if (context.isEmpty())
			context.put(new FeatureVariable("?L"), new FeatureConstant(lemma.getValue()));
	}


	/**
	 * Adds the given lemma with the given context.
	 * @param lemma
	 * @param context
	 * @param equations
	 */
	public void add(Lemma lemma, InstantiationContext context, Equations equations)
	{
		this.lemmas.put(lemma, context);
		this.equations.put(lemma, equations);
		if (context.isEmpty())
			context.put(new FeatureVariable("?L"), new FeatureConstant(lemma.getValue()));
	}


	/**
	 * Adds the given lemma with the given context and coanchors.
	 * @param lemma
	 * @param context
	 * @param coanchors
	 * @param equations
	 */
	public void add(Lemma lemma, InstantiationContext context, List<Lemma> coanchors, Equations equations)
	{
		this.lemmas.put(lemma, context);
		this.equations.put(lemma, equations);
		this.coanchors.put(lemma, coanchors);
		if (context.isEmpty())
			context.put(new FeatureVariable("?L"), new FeatureConstant(lemma.getValue()));
	}


	/**
	 * @return the trace
	 */
	public Trace getTrace()
	{
		return trace;
	}


	/**
	 * @param trace the trace to set
	 */
	public void setTrace(Trace trace)
	{
		this.trace = trace;
	}


	/**
	 * @return the semantics
	 */
	public Semantics getSemantics()
	{
		return semantics;
	}


	/**
	 * @param semantics the semantics to set
	 */
	public void setSemantics(Semantics semantics)
	{
		this.semantics = semantics;
	}


	/**
	 * @return the lemmas
	 */
	public Map<Lemma, InstantiationContext> getLemmas()
	{
		return lemmas;
	}


	/**
	 * @param lemmas the lemmas to set
	 */
	public void setLemmas(Map<Lemma, InstantiationContext> lemmas)
	{
		this.lemmas = lemmas;
	}


	/**
	 * @return the isPattern
	 */
	public boolean isPattern()
	{
		return isPattern;
	}


	/**
	 * @param isPattern the isPattern to set
	 */
	public void setPattern(boolean isPattern)
	{
		this.isPattern = isPattern;
	}


	/**
	 * @return the coanchors
	 */
	public Map<Lemma, List<Lemma>> getCoanchors()
	{
		return coanchors;
	}


	/**
	 * Returns coanchors for given Lemma.
	 * @param lemma
	 * @return the coanchors
	 */
	public List<Lemma> getCoanchors(Lemma lemma)
	{
		return coanchors.containsKey(lemma) ? coanchors.get(lemma) : new ArrayList<Lemma>();
	}


	/**
	 * @param coanchors the coanchors to set
	 */
	public void setCoanchors(Map<Lemma, List<Lemma>> coanchors)
	{
		this.coanchors = coanchors;
	}


	@Override
	public String toString()
	{
		return "trace:" + trace + "\nsemantics:[" + semantics + "]" + (lemmas.isEmpty() ? "" : "\nlemmas:" + lemmas);
	}


	/**
	 * Returns Equations for given lemma.
	 * @param lemma
	 * @return equations
	 */
	public Equations getEquations(Lemma lemma)
	{
		return equations.get(lemma);
	}


	/**
	 * Returns the family equations.
	 * @return the family equations
	 */
	public Equations getFamilyEquations()
	{
		return familyEquations;
	}


	/**
	 * Sets the family equations.
	 * @param familyEquations
	 */
	public void setFamilyEquations(Equations familyEquations)
	{
		this.familyEquations = familyEquations;
	}
}
