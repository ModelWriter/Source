package synalp.generation.jeni.selection.families;

import java.io.File;
import java.util.*;

import org.apache.log4j.*;

import synalp.commons.grammar.*;
import synalp.commons.semantics.*;
import synalp.commons.unification.FeatureVariable;
import synalp.generation.configuration.*;
import synalp.generation.jeni.JeniLexicalSelection;
import synalp.generation.jeni.selection.patterns.*;
import synalp.generation.jeni.selection.patterns.templates.*;
import synalp.generation.jeni.semantics.SemanticsBuilder;

import com.google.common.collect.*;

/**
 * A FamilySemanticsBuilder associates semantics to entries of a grammar thanks to patterns that
 * associate semantics to traces. An entry semantics is defined by the patterns of the most specific
 * traces that matches the entry trace.
 * @author Alexandre Denis
 */
public class FamilySemanticsBuilder
{
	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(FamilySemanticsBuilder.class);

	private SelectionPatterns patterns;


	/**
	 * Sets the semantics of the given grammar and writes it back if overwrite is true.
	 * @param grammarFile
	 * @param patternsFile
	 * @param overwrite if true overwrites the grammarFile, if false do not write
	 * @return the grammar after it has been written to file
	 */
	public static Grammar setGrammarSemantics(File grammarFile, File patternsFile, boolean overwrite)
	{
		// disables automatic node ids assignments otherwise it will overwrite the existing ids
		GeneratorOption.ASSIGN_NODE_IDS = false;

		Grammar grammar = GeneratorConfiguration.loadGrammar(grammarFile, false);
		SelectionPatterns patterns = SelectionPatterns.load(patternsFile);
		FamilySemanticsBuilder builder = new FamilySemanticsBuilder(patterns);
		builder.setSemantics(grammar);
		if (overwrite)
			GrammarWriter.write(grammar, grammarFile);
		return grammar;
	}


	/**
	 * Sets the semantics of the given grammar
	 * @param grammar
	 * @param patterns
	 * @return the grammar after its semantics has been set
	 */
	public static Grammar setGrammarSemantics(Grammar grammar, SelectionPatterns patterns)
	{
		new FamilySemanticsBuilder(patterns).setSemantics(grammar);
		return grammar;
	}


	/**
	 * Creates a new FamilySemanticsBuilder based on given patterns.
	 * @param patterns
	 */
	public FamilySemanticsBuilder(SelectionPatterns patterns)
	{
		this.patterns = patterns;
	}


	/**
	 * Sets the semantics of given grammar.
	 * @param grammar
	 */
	public void setSemantics(Grammar grammar)
	{
		List<GrammarEntry> sorted = new ArrayList<GrammarEntry>(grammar.values());
		Grammar.sortByNames(sorted);
		for(GrammarEntry entry : sorted)
			setSemantics(entry);
	}


	/**
	 * This post process rewrites the nodes that have lemmas. It enables to apply the "lemanchor"
	 * mechanism that overwrites a node type to set it as a coanchor.
	 * @param entry
	 */
	private void postProcessEntry(GrammarEntry entry)
	{
		for(Node node : entry.getTree().getNodes())
			GrammarReader.rewriteLemanchor(node);
	}


	/**
	 * Sets the semantics of given entry. It first clears the existing semantics and sets arguments
	 * identifiers. It then retrieves all patterns whose trace matches the entry trace and filters
	 * them to keep the most specific. Finally, it joins all the semantics of matched patterns, and
	 * applies co-anchoring equations.
	 * @param entry
	 */
	private void setSemantics(GrammarEntry entry)
	{
		SemanticsBuilder.clearSemantics(entry);
		SemanticsBuilder.setIdentifiers(entry);
		logStartEntry(entry);

		Multimap<Trace, SelectionPattern> patterns = getMatchingPatterns(entry);
		List<SelectionPattern> mostSpecific = keepsMostSpecific(patterns);

		logMostSpecificPatterns(mostSpecific);

		Semantics semantics = new Semantics();
		for(SelectionPattern pattern : mostSpecific)
		{
			semantics.addNoDoublonsNames(pattern.getMatch());
			applyCoanchoring(entry, pattern);
		}
		//semantics.removeSelectionalLiterals();

		logSemantics(semantics);
		entry.setSemantics(semantics);

		postProcessEntry(entry);

		if (!semantics.isEmpty())
			checkValidity(entry);

		logEndEntry(entry);
	}


	/**
	 * Applies coanchoring equations as found in the given pattern.
	 * @param entry
	 * @param pattern
	 */
	private void applyCoanchoring(GrammarEntry entry, SelectionPattern pattern)
	{
		for(SelectionTemplate template : pattern.getTemplates())
			if (template instanceof CoanchorTemplate)
				JeniLexicalSelection.applyEquations(entry, template.getEquations(), entry.getContext());
	}


	/**
	 * Checks if the entry is valid with regards to its semantics. It tests if all arguments
	 * variables of the tree (foot and subst) are found in the semantics, and vice versa if all
	 * arguments variables of the semantics are found in the tree.
	 * @param entry
	 */
	private void checkValidity(GrammarEntry entry)
	{
		Set<FeatureVariable> entryArgs = getArgumentsVariables(entry.getTree().getAllVariables());
		Set<FeatureVariable> semArgs = getArgumentsVariables(entry.getSemantics().getAllVariables());

		Set<FeatureVariable> missingFromSem = new HashSet<FeatureVariable>(entryArgs);
		missingFromSem.removeAll(semArgs);
		if (!missingFromSem.isEmpty())
		{
			logMissing(missingFromSem, entry);
			//System.exit(-1);
		}

		Set<FeatureVariable> exceedingInSem = new HashSet<FeatureVariable>(semArgs);
		exceedingInSem.removeAll(entryArgs);
		if (!exceedingInSem.isEmpty())
		{
			logSurplus(exceedingInSem, entry);
			//System.exit(-1);
		}
	}


	/**
	 * Returns the subset of variables that are arguments.
	 * @param vars
	 * @return a subset of variables
	 */
	private static Set<FeatureVariable> getArgumentsVariables(Collection<FeatureVariable> vars)
	{
		Set<FeatureVariable> ret = new HashSet<FeatureVariable>();
		for(FeatureVariable var : vars)
			if (SelectionPattern.isArgumentVariable(var))
				ret.add(var);
		return ret;
	}


	/**
	 * Returns the most specific patterns that match the entry.
	 * @param entry
	 * @return a list of most specific patterns
	 */
	public List<SelectionPattern> getMostSpecificMatchingPatterns(GrammarEntry entry)
	{
		Multimap<Trace, SelectionPattern> patterns = getMatchingPatterns(entry);
		List<SelectionPattern> mostSpecific = keepsMostSpecific(patterns);
		return mostSpecific;
	}


	/**
	 * Returns a list of the patterns whose trace is the most specific. It skips from the given
	 * multimap all traces that are subtraces of other traces.
	 * @param traces
	 * @return a list of most specific patterns
	 */
	private List<SelectionPattern> keepsMostSpecific(Multimap<Trace, SelectionPattern> traces)
	{
		List<SelectionPattern> ret = new ArrayList<SelectionPattern>();
		for(Trace trace1 : traces.keySet())
		{
			boolean found = false;
			for(Trace trace2 : traces.keySet())
				if (trace1 != trace2 && trace1.isSubTrace(trace2))
				{
					found = true;
					break;
				}
			if (!found)
				ret.addAll(traces.get(trace1));
		}
		return ret;
	}


	/**
	 * Returns all traces whose pattern match the given entry. It only considers non-selectional
	 * patterns (patterns that contains at least one non-selectional literal).
	 * @param entry
	 * @return all traces and their patterns
	 */
	private Multimap<Trace, SelectionPattern> getMatchingPatterns(GrammarEntry entry)
	{
		Multimap<Trace, SelectionPattern> ret = HashMultimap.create();
		for(SelectionPattern pattern : patterns)
		{
			//if (!containsNoSelectionalLiterals(pattern.getMatch()))
			//	continue;

			for(SelectionTemplate template : pattern.getTemplates())
			{
				if (template.isCoselection())
					continue;

				Trace trace = template.getTrace();
				if (trace.isEmpty() || !trace.isSubTrace(entry.getTrace()))
					continue;

				ret.put(trace, pattern);
			}
		}
		return ret;
	}


	/**
	 * Tests whether the given semantics contains no selectional literal.
	 * @param semantics
	 * @return true if the given semantics contains no selectional literal.
	 */
	@SuppressWarnings("unused")
	private boolean containsNoSelectionalLiterals(Semantics semantics)
	{
		for(DefaultLiteral literal : semantics)
			if (literal.isSelectional())
				return false;
		return true;
	}


////// logging

	private static void logStartEntry(GrammarEntry entry)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Starting entry " + entry.toShortString());
			logger.debug("\ttrace: " + entry.getTrace());
		}
	}


	private static void logEndEntry(GrammarEntry entry)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending entry " + entry.toShortString());
			logger.debug("");
		}
	}


	private static void logMostSpecificPatterns(List<SelectionPattern> mostSpecific)
	{
		if (logger.isDebugEnabled())
			logger.debug("\tpatterns: " + mostSpecific);
	}


	private static void logSemantics(Semantics semantics)
	{
		if (logger.isDebugEnabled())
			logger.debug("\tsemantics: " + semantics);
	}


	private static void logSurplus(Set<FeatureVariable> vars, GrammarEntry entry)
	{
		logger.warn("Warning: surplus " + vars + " for entry " + entry + " (check if it is normal that it is not realized)");
	}


	private static void logMissing(Set<FeatureVariable> vars, GrammarEntry entry)
	{
		logger.warn("Warning: missing " + vars + " for entry " + entry + " (check if there are other constraints on that node)");
	}

}
