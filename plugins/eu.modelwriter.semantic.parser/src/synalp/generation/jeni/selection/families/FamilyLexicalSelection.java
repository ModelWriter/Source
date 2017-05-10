package synalp.generation.jeni.selection.families;

import java.io.File;
import java.util.*;

import org.apache.log4j.Logger;

import synalp.commons.grammar.*;
import synalp.commons.input.Lemma;
import synalp.commons.lexicon.*;
import synalp.commons.semantics.*;
import synalp.commons.unification.*;
import synalp.generation.jeni.JeniLexicalSelection;
import synalp.generation.jeni.filtering.dlx.DLXGrouping;
import synalp.generation.jeni.selection.patterns.*;
import synalp.generation.jeni.selection.patterns.javacc.PatternReader;
import synalp.generation.jeni.selection.patterns.templates.*;
import synalp.generation.selection.*;


/**
 * FamilyLexicalSelection first associates semantics to grammar entries before doing lexical
 * selection. There are few tricks though. The most important one is the handling of coselections.
 * Coselections are all the trees that are selected parallel to a main selection. For instance in
 * "bill is sleeping", the "sleep" event will select main trees, but will also coselect the
 * auxiliary. The semantics of the auxiliary is generated on the fly such that it is guaranteed to
 * be part of the output. Other kinds of coselections are function words such as
 * "the man who sleeps", here the selection of a particular tree involves the selection of "who".
 * However, when selecting trees it is important to correctly disjoin the groups of trees to make
 * sure that "who" is not coselected in the same group of entries than "the man sleeps" (normal
 * intransitive construction). Other coselections are optional auxiliaries, like
 * "the cat is chased by the dog", we want both that "the cat is chased by the dog" and "the cat
 * chased by the dog" could be produced, and as such the coselected auxiliary does not have to be
 * present but is allowed to be, hence its semantics needs to be empty.
 * @author Alexandre Denis
 */
@SuppressWarnings("javadoc")
public class FamilyLexicalSelection implements LexicalSelection
{
	public static Logger logger = Logger.getLogger(FamilyLexicalSelection.class);

	private FeatureConstant rootCat = new FeatureConstant("s", "np");
	private FamilySemanticsBuilder semanticsBuilder;
	private SelectionPatterns selectionPatterns = new SelectionPatterns();
	private SelectionPatterns preProcessingPatterns = new SelectionPatterns();

	// this is used to have unique trees after selection that would facilitate the removal of duplicate items
	private static int currentId;
	private Grammar grammar;


	/**
	 * Creates a new FamilyLexicalSelection based on given pattern file, captures exceptions. This methods alters the
	 * given Grammar such that the trees are associated to semantics.
	 * @param lexicalPatterns
	 * @return a FamilyLexicalSelection or null if it was not possible to create it.
	 */
	public static FamilyLexicalSelection newInstance(File patternFile, File grammarPatternsFile, Grammar grammar)
	{
		try
		{
			SelectionPatterns grammarPatterns = PatternReader.readPatterns(grammarPatternsFile);
			
			FamilySemanticsBuilder.setGrammarSemantics(grammar, grammarPatterns);
			
			SelectionPatterns lexicalPatterns = PatternReader.readPatterns(patternFile);
			return new FamilyLexicalSelection(lexicalPatterns, grammarPatterns, grammar);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("Error while creating FamilyLexicalSelection based on patterns file " + patternFile + " : " + e.getMessage());
			return null;
		}
	}


	/**
	 * Creates a new FamilyLexicalSelection with given patterns.
	 * @param lexicalPatterns
	 */
	public FamilyLexicalSelection(SelectionPatterns lexicalPatterns, SelectionPatterns grammarPatterns, Grammar grammar)
	{
		this.grammar = grammar;
		this.semanticsBuilder = new FamilySemanticsBuilder(grammarPatterns);
		for(SelectionPattern pattern : lexicalPatterns)
			if (pattern.hasAssumption() || pattern.hasRewriting())
				preProcessingPatterns.add(pattern);
			else selectionPatterns.add(pattern);
	}


	/**
	 * Selects entries from the grammar thanks to the given input and patterns. First the input is
	 * preprocessed using assumption or rewriting patterns. Disjunctions are flattened and for each
	 * semantics without disjunction, it selects the entries.
	 * @param grammar the grammar needs to have semantics using following variable conventions: ?A
	 *            anchor, ?Sn substitution n (prefix order), ?F foot
	 */
	@Override
	public LexicalSelectionResult selectEntries(Semantics input)
	{
		currentId = 0;

		applyPreprocessing(input);
		logAfterAssumptions(input);

		LexicalSelectionResult result = new LexicalSelectionResult();
		for(Semantics flattened : input.flattenDisjunctions())
			appendEntriesNoDisjunctions(flattened, result);

		return result;
	}


	/**
	 * Selects (builds actually) entries from the given input without disjunctions. It tries to
	 * match each pattern wrt the input and for each matching context, and each satisfied template
	 * (non coselectional) of the matched pattern, it creates a new entry if possible. In the end,
	 * all templates that were coselections creates new entries. Coselections can be created in two
	 * ways, thanks to lexical patterns (typically complex tenses), and grammar patterns
	 * (coselections with other trees).
	 * @param input
	 * @return
	 */
	private void appendEntriesNoDisjunctions(Semantics input, LexicalSelectionResult currentResult)
	{
		logSelectingEntries(input);

		GrammarEntries selected = new GrammarEntries();

		// iterate through the patterns, build regular entries, remember coselections to create them in the end
		Coselections lexicalCoselections = new Coselections();
		Coselections grammaticalCoselections = new Coselections();
		for(SelectionPattern pattern : selectionPatterns)
		{
			SemanticsSubsumptionResult results = pattern.matchMapping(input);

			logPatternMatchResult(pattern, results);
			GrammarEntries selectedByPattern = new GrammarEntries();
			for(InstantiationContext context : results.getContexts())
			{
				if (JeniLexicalSelection.hasEqualNumeralVariables(context))
					continue;

				Semantics matched = new Semantics(results.getCoveredLiterals(context));

				// copy pattern
				SelectionPattern patternCopy = new SelectionPattern(pattern);
				patternCopy.removeUnsatisfiedTemplates(context);

				for(SelectionTemplate template : patternCopy.getTemplates())
					if (template.isCoselection())
						lexicalCoselections.add(new Coselection((CoselectionTemplate) template, context, pattern.getEquations())); // this is a lexical coselection
					else for(GrammarEntry entry : template.getEntries(grammar))
					{
						GrammarEntry newEntry = createEntry(entry, input, matched, context, patternCopy);
						if (newEntry != null)
						{
							selectedByPattern.add(newEntry);
							grammaticalCoselections.addAll(getGrammarCoselections(newEntry)); // this is a grammatical coselection
						}
					}
			}
			selected.addAll(selectedByPattern);
			logPatternSelectedEntries(selectedByPattern, results);
		}

		logSelectedEntries(selected);

		checkCoveredSemantics(input, selected);

		// then once we selected the entries, let's regroup them by input covering
		Set<GrammarEntries> selectedGroups = DLXGrouping.group(selected, input);

		logNumberOfGroups(selectedGroups);

		// and for each group, adds the corresponding grammatical coselections, but also potentially alters the semantics
		for(GrammarEntries selectedGroup : selectedGroups)
		{
			logFindingCoselectionsForGroup(selectedGroup);

			// adds first all grammatical coselections
			Semantics selectedGroupSemantics = new Semantics(input);
			GrammarEntries newGrammaticalCoselections = new GrammarEntries();
			for(GrammarEntry entry : selectedGroup)
				for(Coselection coselection : grammaticalCoselections)
					if (coselection.getMainEntry() == entry)
						newGrammaticalCoselections.addAll(coselection.createCoselectionEntries(grammar, selectedGroupSemantics, selectedGroup));
			
			// then adds all lexical coselections
			GrammarEntries newLexicalCoselections = new GrammarEntries();
			for(Coselection coselection : lexicalCoselections)
				newLexicalCoselections.addAll(coselection.createCoselectionEntries(grammar, selectedGroupSemantics, selectedGroup));
			
			selectedGroup.addAll(newGrammaticalCoselections);
			selectedGroup.addAll(newLexicalCoselections);

			// eventually add the group as a result, here we could:
			// - apply polarity filtering now since the group is meant to be complete
			// - also completely remove group from results if it does not cover the whole semantics (normally that should not happen though)
			
			if (Polarities.satisfies(selectedGroup, rootCat))
			{
				checkCoveredSemantics(selectedGroupSemantics, selectedGroup);
				currentResult.addResult(selectedGroupSemantics, selectedGroup);

				logSelectedAndCoselectedEntries(selectedGroup);
			}
			else logFailedPolarity(selectedGroup);
		}
	}


	/**
	 * Retrieves the coselections from given grammar entry thanks to grammar patterns.
	 * @param entries
	 * @return
	 */
	private List<Coselection> getGrammarCoselections(GrammarEntry entry)
	{
		List<Coselection> ret = new ArrayList<Coselection>();
		for(SelectionPattern pattern : semanticsBuilder.getMostSpecificMatchingPatterns(entry))
			for(SelectionTemplate template : pattern.getCoselectionTemplates())
				if (template.isSatisfied(entry.getContext()))
					ret.add(new Coselection(entry, (CoselectionTemplate) template, entry.getContext(), pattern.getEquations()));
		return ret;
	}


	/**
	 * Checks the semantics covered by the items of this table wrt to the given semantics.
	 * @param input
	 */
	private void checkCoveredSemantics(Semantics input, GrammarEntries entries)
	{
		Semantics covered = new Semantics();
		for(GrammarEntry entry : entries)
		{
			Semantics entrySem = new Semantics(entry.getSemantics());
			entrySem.instantiate(entry.getContext());
			covered.addNoDoublons(entrySem);
		}

		List<DefaultLiteral> difference = input.difference(covered);
		if (!difference.isEmpty())
			logUncoveredLiterals(difference, input);
	}


	/**
	 * Creates an entry from given selection pattern.
	 * @param entry
	 * @param input
	 * @param matched
	 * @param context
	 * @param pattern
	 * @return
	 */
	private GrammarEntry createEntry(GrammarEntry entry, Semantics input, Semantics matched, InstantiationContext context, SelectionPattern pattern)
	{
		return createEntry(entry, input, context, pattern.getFirstFoundLemma(context), pattern.getEquations(), true, false);
	}


	/**
	 * Creates a copy of the given entry if it has a semantics that subsumes the input and the
	 * equations can be applied.
	 * @param entry
	 * @param input
	 * @param context
	 * @param lemma
	 * @param equations
	 * @param semChecking if true, do not allow empty semantics entries or entries whose semantics
	 *            does not subsumes the input
	 * @param overwriteEquations if true the equations are meant to overwrite features, if false
	 *            normal unification is tested
	 * @return the newly created entry or null if it was not possible
	 */
	static GrammarEntry createEntry(GrammarEntry entry, Semantics input, InstantiationContext context, Lemma lemma, Equations equations, boolean semChecking,
			boolean overwriteEquations)
	{
		logCreatingCandidate(entry);

		InstantiationContext newContext = new InstantiationContext(context);
		GrammarEntry ret = new GrammarEntry(entry);

		if (semChecking)
		{
			if (ret.getSemantics().isEmpty())
			{
				logNoSemantics(entry);
				return null;
			}
			SemanticsSubsumptionResult results = SemanticsSubsumer.subsumesMapping(ret.getSemantics(), input, newContext, false);
			if (results.isEmpty())
			{
				logSemanticsMismatch(entry, newContext);
				return null;
			}
			else newContext = results.getContexts().iterator().next(); // this could be problematic
		}

		ret.setMainAnchorLemma(lemma);
		ret.setContext(newContext);

		if (!ret.applyEquations(equations, overwriteEquations))
		{
			logger.warn("Equations: " + equations.toString(newContext) + " cannot be applied to " + ret + " (skipping it)");
			return null;
		}

		if (!ret.getTree().getUninstantiatedCoanchors().isEmpty())
		{
			logger.warn("Entry " + ret.getName() + " as uninstantiated coanchors (skipping it)");
			return null;
		}

		String newName = ret.getTree().getId() + "-" + (currentId++);
		ret.setName(newName);
		ret.getTree().setId(newName);
		ret.getSemantics().removeSelectionalLiterals();

		return ret;
	}


	/**
	 * Applies all assumptions first.
	 * @param input
	 */
	private void applyPreprocessing(Semantics input)
	{
		for(SelectionPattern preProcessing : preProcessingPatterns)
			for(InstantiationContext context : preProcessing.match(input))
				for(SelectionTemplate template : preProcessing.getTemplates())
					if (template instanceof AssumptionTemplate)
						((AssumptionTemplate) template).applyAssumption(input, context);
					else if (template instanceof RewritingTemplate)
						((RewritingTemplate) template).applyRewriting(input, context);
	}


//// logging

	private static void logNumberOfGroups(Set<GrammarEntries> selectedGroups)
	{
		if (logger.isDebugEnabled())
			logger.debug("After grouping: " + selectedGroups.size() + " groups of entries");
	}


	private static void logAfterAssumptions(Semantics input)
	{
		if (logger.isDebugEnabled())
			logger.debug("After all assumptions: " + input);
	}


	private static void logUncoveredLiterals(List<DefaultLiteral> difference, Semantics input)
	{
		String msg = "Miscovered literals by lexical selection: " + difference;
		logger.error(msg);
		System.err.println(msg);
	}


	private static void logPatternMatchResult(SelectionPattern pattern, SemanticsSubsumptionResult results)
	{
		if (logger.isTraceEnabled())
			logger.trace("Pattern " + pattern.getId() + " match: " + results);
	}


	private static void logSelectedAndCoselectedEntries(GrammarEntries ret)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("All (co)selected entries:");
			for(GrammarEntry entry : ret)
				logger.debug(entry.toString(NodeFormat.MINIMALIST_INSTFS) + " " + entry.getSemantics().toString(entry.getContext()));
		}
	}


	private static void logFindingCoselectionsForGroup(GrammarEntries selectedGroup)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("");
			logger.debug("Looking for coselections for group: " + selectedGroup);
		}

	}


	private static void logSelectedEntries(GrammarEntries ret)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Selected entries (no coselections):");
			for(GrammarEntry entry : ret)
				logger.debug(entry + " " + entry.getSemantics().toString(entry.getContext()));
		}
	}


	private static void logSelectingEntries(Semantics input)
	{
		if (logger.isDebugEnabled())
			logger.debug("Selecting entries from input: " + input);
	}


	private static void logPatternSelectedEntries(GrammarEntries selectedByPattern, SemanticsSubsumptionResult results)
	{
		if (!selectedByPattern.isEmpty() && logger.isTraceEnabled())
			logger.trace("\tSelected: " + selectedByPattern);
		if (selectedByPattern.isEmpty() && !results.isEmpty())
			logger.warn("\tWarning: pattern matched but no selected entries (issue?)");
	}


	private static void logSemanticsMismatch(GrammarEntry entry, InstantiationContext context)
	{
		if (logger.isTraceEnabled())
			logger.trace("Entry " + entry + " semantics mismatch (check grammar semantics): " + entry.getSemantics().toString(context));
	}


	private static void logNoSemantics(GrammarEntry entry)
	{
		if (logger.isTraceEnabled())
			logger.trace("Entry " + entry + " has no semantics");
	}


	private static void logCreatingCandidate(GrammarEntry entry)
	{
		if (logger.isTraceEnabled())
			logger.trace("Checking candidate " + entry + " " + entry.getTrace());
	}



	private static void logFailedPolarity(GrammarEntries selectedGroup)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Failed polarity for given group: "+new Polarities(selectedGroup));
			for(GrammarEntry entry : selectedGroup)
				logger.debug(entry);
		}
		
	}

}
