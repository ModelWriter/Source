package synalp.generation.jeni.selection.families;

import synalp.commons.grammar.*;
import synalp.commons.input.Lemma;
import synalp.commons.lexicon.*;
import synalp.commons.semantics.*;
import synalp.commons.unification.*;
import synalp.generation.jeni.selection.patterns.SelectionPattern;
import synalp.generation.jeni.selection.patterns.templates.CoselectionTemplate;

/**
 * A Coselection is a tree (the co-tree) that is selected together with another tree (the main
 * tree). The CoselectionTemplate provides information on how to select and anchor the co-tree,
 * while it is possible to specify additional equations to be applied on the main tree.
 * @author Alexandre Denis
 */
public class Coselection
{
	private GrammarEntry mainEntry;
	private CoselectionTemplate template;
	private InstantiationContext context;
	private Equations mainEquations; // the equations that this coselection imposes on the main tree
	


	/**
	 * Creates a new coselection that is not attached to a particular entry.
	 * @param template
	 * @param context
	 * @param mainEquations
	 */
	public Coselection(CoselectionTemplate template, InstantiationContext context, Equations mainEquations)
	{
		this.template = template;
		this.context = context;
		this.mainEquations = mainEquations;
	}
	
	
	/**
	 * Creates a new coselection that is attached to a particular entry.
	 * @param mainEntry 
	 * @param template
	 * @param context
	 * @param mainEquations
	 */
	public Coselection(GrammarEntry mainEntry, CoselectionTemplate template, InstantiationContext context, Equations mainEquations)
	{
		this.mainEntry = mainEntry;
		this.template = template;
		this.context = context;
		this.mainEquations = mainEquations;
	}


	/**
	 * Creates new Coselection grammar entries, one for each grammar entry selected by the templates
	 * trace. The other entries may be modified (main equations), and the semantics input as well
	 * (coselection literals). Warning: the given input will be modified when adding a new
	 * mandatory coselection.
	 * @param grammar
	 * @param input
	 * @param otherEntries
	 * @return new entries
	 */
	public GrammarEntries createCoselectionEntries(Grammar grammar, Semantics input, GrammarEntries otherEntries)
	{
		GrammarEntries ret = new GrammarEntries();
		Lemma lemma = template.getLemma(context);
		Equations equations = template.getEquations();
		FeatureConstant anchor = new FeatureConstant(context.getValue(SelectionPattern.ANCHOR_VAR).toString());
		for(GrammarEntry entry : grammar.getEntriesContainingTrace(template.getTrace()))
		{
			GrammarEntry newEntry = FamilyLexicalSelection.createEntry(entry, input, context, lemma, equations, false, true);
			if (newEntry == null)
				continue;

			// optional templates do not force the semantics
			if (!template.isOptional())
			{
				Semantics coselectSemantics = createCoselectSemantics(anchor, newEntry);
				newEntry.setSemantics(coselectSemantics);
				input.addAll(coselectSemantics);
			}

			ret.add(newEntry);
		}

		// then apply main equations to the other entries (shouldnt it be only with the stored main entry?)
		for(GrammarEntry mainEntry : getEntriesWithAnchor(otherEntries, anchor))
			for(Equation equation : mainEquations)
				mainEntry.applyEquation(equation, true);

		return ret;
	}


	/**
	 * Returns the main entry which can be null if the coselection is floating.
	 * @return the mainEntry
	 */
	public GrammarEntry getMainEntry()
	{
		return mainEntry;
	}


	/**
	 * Returns all the entries that have the given anchor constant for the value of ?A.
	 * @param entries
	 * @param anchor
	 * @return a subset of given entries
	 */
	private static GrammarEntries getEntriesWithAnchor(GrammarEntries entries, FeatureConstant anchor)
	{
		GrammarEntries ret = new GrammarEntries();
		for(GrammarEntry entry : entries)
			if (anchor.equals(entry.getContext().get(SelectionPattern.ANCHOR_VAR)))
				ret.add(entry);
		return ret;
	}


	/**
	 * Creates a semantics composed of one single literal "coselect(anchor, entry.name)". The anchor
	 * corresponds to the idx of the anchor of the item group (value of ?A if it exists).
	 * @param anchor
	 * @param entry
	 * @return new semantics with one literal
	 */
	private static Semantics createCoselectSemantics(FeatureConstant anchor, GrammarEntry entry)
	{
		DefaultLiteral literal = new DefaultLiteral();
		literal.setPredicate(new FeatureConstant("coselect"));
		literal.addArgument(anchor);
		literal.addArgument(new FeatureConstant(entry.getName() + "-" + entry.getMainAnchorLemma()));

		Semantics ret = new Semantics();
		ret.add(literal);
		return ret;
	}
}
