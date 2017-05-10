package synalp.generation.jeni.selection.patterns;

import java.util.*;
import java.util.regex.*;

import synalp.commons.grammar.*;
import synalp.commons.input.Lemma;
import synalp.commons.lexicon.Equations;
import synalp.commons.semantics.*;
import synalp.commons.unification.*;
import synalp.generation.jeni.selection.patterns.templates.*;


/**
 * A SelectionPattern represents the association of set of selection templates to a semantic match.
 * @author Alexandre Denis
 */
public class SelectionPattern
{
	/**
	 * The variable denoting the anchor in the match.
	 */
	public static final FeatureVariable ANCHOR_VAR = new FeatureVariable("?A");

	/**
	 * The variable denoting the foot in the match.
	 */
	public static final FeatureVariable FOOT_VAR = new FeatureVariable("?F");

	/**
	 * The variable denoting the lemma in the match.
	 */
	public static final FeatureVariable LEMMA_VAR = new FeatureVariable("?L");

	private String id = "";
	private Semantics match;
	private boolean shallow;
	private String exclude;
	private List<SelectionTemplate> templates;


	/**
	 * Creates a non-shallow pattern.
	 * @param match
	 * @param templates
	 */
	public SelectionPattern(Semantics match, List<SelectionTemplate> templates)
	{
		this(match, templates, false);
	}


	/**
	 * @param match
	 * @param templates
	 * @param shallow
	 */
	public SelectionPattern(Semantics match, List<SelectionTemplate> templates, boolean shallow)
	{
		this.match = match;
		this.shallow = shallow;
		this.templates = templates;
	}


	/**
	 * @param id
	 * @param match
	 * @param templates
	 * @param shallow
	 */
	public SelectionPattern(String id, Semantics match, boolean shallow, SelectionTemplate... templates)
	{
		this.id = id;
		this.match = match;
		this.shallow = shallow;
		this.templates = Arrays.asList(templates);
	}


	/**
	 * Creates a *shallow* copy of this SelectionPattern.
	 * @param pattern
	 */
	public SelectionPattern(SelectionPattern pattern)
	{
		this.id = pattern.getId();
		this.shallow = pattern.isShallow();
		this.exclude = pattern.getExclude();
		this.match = new Semantics(pattern.getMatch());
		this.templates = new ArrayList<SelectionTemplate>(pattern.templates);
	}


	/**
	 * Returns the first found lemma.
	 * @param context
	 * @return a new lemma or null if not found.
	 */
	public Lemma getFirstFoundLemma(InstantiationContext context)
	{
		for(SelectionTemplate template : templates)
		{
			Lemma lemma = template.getLemma(context);
			if (lemma != null)
				return lemma;
		}

		return null;
	}


	/**
	 * Returns all equations of this pattern without coselection equations.
	 * @return equations
	 */
	public Equations getEquations()
	{
		Equations ret = new Equations();
		for(SelectionTemplate template : templates)
			if (!template.isCoselection())
				ret.addAll(template.getEquations());
		return ret;
	}


	/**
	 * Returns the contexts in which this pattern's semantics subsumes the given input.
	 * @param input
	 * @return a set of contexts that may be empty when no match
	 */
	public Set<InstantiationContext> match(Semantics input)
	{
		return match.subsumes(input, false);
	}


	/**
	 * Returns the contexts in which this pattern's semantics subsumes the given input.
	 * @param input
	 * @return a set of contexts that may be empty when no match
	 */
	public SemanticsSubsumptionResult matchMapping(Semantics input)
	{
		return SemanticsSubsumer.subsumesMapping(match, input, new InstantiationContext(), false);
	}


	/**
	 * @return the pattern
	 */
	public Semantics getMatch()
	{
		return match;
	}


	/**
	 * Returns a live list of all templates.
	 * @return the templates
	 */
	public List<SelectionTemplate> getTemplates()
	{
		return templates;
	}


	/**
	 * Returns the templates that have a trace and are not coselections.
	 * @return the templates
	 */
	public List<SelectionTemplate> getNormalSelectionTemplates()
	{
		List<SelectionTemplate> ret = new ArrayList<SelectionTemplate>();
		for(SelectionTemplate template : templates)
			if (!template.getTrace().isEmpty() && !template.isCoselection())
				ret.add(template);
		return ret;
	}


	/**
	 * Returns all templates that have a trace and are coselections.
	 * @return a new list of templates
	 */
	public List<CoselectionTemplate> getCoselectionTemplates()
	{
		List<CoselectionTemplate> ret = new ArrayList<CoselectionTemplate>();
		for(SelectionTemplate template : templates)
			if (!template.getTrace().isEmpty() && template.isCoselection())
				ret.add((CoselectionTemplate) template);
		return ret;
	}


	/**
	 * Returns true if this pattern contains a SelectionTemplate which has an assumption
	 * @return true if this pattern contains a SelectionTemplate which has an assumption
	 */
	public boolean hasAssumption()
	{
		for(SelectionTemplate template : templates)
			if (template instanceof AssumptionTemplate)
				return true;
		return false;
	}


	/**
	 * Returns true if this pattern contains a SelectionTemplate which is a RewritingTemplate.
	 * @return true if this pattern contains a SelectionTemplate which is a RewritingTemplate.
	 */
	public boolean hasRewriting()
	{
		for(SelectionTemplate template : templates)
			if (template instanceof RewritingTemplate)
				return true;
		return false;
	}


	/**
	 * Removes all the templates which are not satisfied in the given context. This method modifies
	 * this pattern.
	 * @param context
	 */
	public void removeUnsatisfiedTemplates(InstantiationContext context)
	{
		for(Iterator<SelectionTemplate> it = templates.iterator(); it.hasNext();)
			if (!it.next().isSatisfied(context))
				it.remove();
	}


	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}


	/**
	 * Returns the entries of the grammar that are covered by all the selection templates of this
	 * pattern which are satisfied in the given context (without coselection).
	 * @param grammar
	 * @param context
	 * @return a set of entries
	 */
	public GrammarEntries getEntries(Grammar grammar, InstantiationContext context)
	{
		GrammarEntries ret = new GrammarEntries();
		for(SelectionTemplate template : templates)
			if (template.isSatisfied(context) && !template.isCoselection())
				ret.addAll(grammar.getEntriesContainingTrace(template.getTrace()));
		return ret;
	}


	@Override
	public String toString()
	{
		return id;
	}


	/**
	 * Tests if the given entry has valid arguments (foot and subst) with regards to the pattern
	 * match semantics.
	 * @param entry
	 * @return true if the entry has valid arguments (subst and foot nodes)
	 */
	public boolean isSemanticsCompatible(GrammarEntry entry)
	{
		for(FeatureVariable var : match.getAllVariables())
			if (!satisfyArgument(var, entry))
				return false;
		return true;
	}


	/**
	 * Tests if the given variable can be satisfied by the given entry. If the variable is the foot
	 * variable check if the tree is auxiliary, if the variable is a subst variable, check if the
	 * tree contains such subst node. If the variable is neither a foot or a subst variable, return
	 * true.
	 * @param var
	 * @param entry
	 * @return true if the entry satisfies the variable requirement
	 */
	private static boolean satisfyArgument(FeatureVariable var, GrammarEntry entry)
	{
		String name = var.getName();
		if (name.equals(FOOT_VAR))
			return entry.getTree().isAuxiliary();
		else
		{
			Pattern p = Pattern.compile("\\?S([0-9]+)");
			Matcher m = p.matcher(name);
			if (m.find())
			{
				int substIndex = Integer.parseInt(m.group(1));
				return entry.getTree().getSubstitutions().size() > substIndex;
			}
			else return true;
		}
	}


	/**
	 * Tests if the given variable is an argument variable (foot or subst).
	 * @param var
	 * @return true if it is an argument variable
	 */
	public static boolean isArgumentVariable(FeatureVariable var)
	{
		return var.equals(FOOT_VAR) || var.getName().matches("\\?S[0-9]+");
	}


	/**
	 * Tests if this pattern is shallow.
	 * @return whether this pattern is shallow
	 */
	public boolean isShallow()
	{
		return shallow;
	}


	/**
	 * @return the exclude
	 */
	public String getExclude()
	{
		return exclude;
	}


	/**
	 * @param exclude the exclude to set
	 */
	public void setExclude(String exclude)
	{
		this.exclude = exclude;
	}

}
