package synalp.generation.jeni;

import java.util.*;

import org.apache.log4j.*;

import synalp.commons.grammar.*;
import synalp.commons.input.Lemma;
import synalp.commons.lexicon.*;
import synalp.commons.semantics.*;
import synalp.commons.unification.*;
import synalp.commons.utils.*;
import synalp.generation.configuration.GeneratorOption;
import synalp.generation.jeni.filtering.*;
import synalp.generation.jeni.filtering.dlx.DLXFiltering;
import synalp.generation.selection.*;

/**
 * LexicalSelection takes a SyntacticLexicon, a Grammar and an input Semantics and returns the
 * grammar entries that match.
 * @author Alexandre Denis
 */
public class JeniLexicalSelection implements LexicalSelection
{
	/**
	 * If true, the variables that are named ?1, ?2 .. ?n are forced to be instantiated to different
	 * constants. This is actually required to express the existence of two relations when having
	 * syntactic lexicon entries for functional words like "who" that should be selected when two
	 * different relations are found. We may consider to have more powerful semantics matching that
	 * would embed this kind of constraint.
	 */
	public static boolean NUMERAL_VARIABLES_CLASH = true;

	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(JeniLexicalSelection.class);

	// this is used to have unique trees after selection that would facilitate the removal of duplicate items
	private static int currentId;

	private Grammar grammar;
	private SyntacticLexicon lexicon;


	/**
	 * Creates a new JeniLexicalSelection based on given grammar and lexicon.
	 * @param grammar
	 * @param lexicon
	 */
	public JeniLexicalSelection(Grammar grammar, SyntacticLexicon lexicon)
	{
		this.grammar = grammar;
		this.lexicon = lexicon;
	}


	@Override
	public LexicalSelectionResult selectEntries(Semantics input)
	{
		logStart(input);

		currentId = 0;
		GrammarEntries ret = new GrammarEntries();
		for(SyntacticLexiconEntry lexEntry : lexicon)
		{
			logTestingLexicalEntry(lexEntry);

			if (lexEntry.getSemantics().isEmpty())
			{
				logSemanticsIgnored(lexEntry);
				continue;
			}

			Set<InstantiationContext> contexts = lexEntry.getSemantics().subsumes(input);

			if (contexts.isEmpty())
			{
				logNoSubsumption(lexEntry.getSemantics(), input);
				continue;
			}

			for(InstantiationContext context : contexts)
			{
				logSemanticsSubsumed(context);

				if (NUMERAL_VARIABLES_CLASH && hasEqualNumeralVariables(context))
				{
					logNumeralVariablesClash(context);
					continue;
				}

				for(GrammarEntry newEntry : getMatchingGrammarEntries(grammar, lexEntry, input, context))
					if (!found(newEntry, ret))
						ret.add(newEntry);
			}
		}

		// remove selectional literals since they already have done their job
		input.removeSelectionalLiterals();

		if (GeneratorOption.USE_FILTERING)
		{
			PolarityFiltering filter = new DLXFiltering();
			//PolarityFiltering filter = new PolarityFilteringSimple();
			//PolarityFiltering filter = new PolarityFilteringKow();
			Set<GrammarEntries> filtered = filter.filter(new PolarityKey(new FeatureConstant(Utils.splitAndTrim(GeneratorOption.FILTERING_CATEGORIES))),
															input, ret);
			logFilteredResults(filtered);
			return new LexicalSelectionResult(input, filtered);
		}
		else
		{
			if (GeneratorOption.RENAME_VARIABLES)
				GrammarEntry.renameVariables(ret);

			logEnd(ret);
			return new LexicalSelectionResult(input, ret);
		}
	}


	/**
	 * Tests whether the given context contains two numeral variables whose instantiated values are
	 * the same. A numeral variable has the form "?n" with n an integer.
	 * @param context
	 * @return true if the context contains this kind of instantiation, false otherwise
	 */
	public static boolean hasEqualNumeralVariables(InstantiationContext context)
	{
		Map<FeatureValue, FeatureVariable> rev = new HashMap<FeatureValue, FeatureVariable>();
		for(FeatureVariable var : context.keySet())
			if (var.getName().matches("\\?[0-9]+"))
				if (rev.containsKey(context.get(var)))
					return true;
				else rev.put(context.get(var), var);
		return false;
	}


	/**
	 * Tests if the given entry is found in the given set of entries regarding name, lemmas and
	 * context.
	 * @param entry
	 * @param entries
	 * @return true if there exists another entry with the same name, same lemmas and same context.
	 */
	private boolean found(GrammarEntry entry, Set<GrammarEntry> entries)
	{
		for(GrammarEntry existing : entries)
			if (entry.getName().equals(existing.getName()) && entry.getTree().getLemmas().equals(existing.getTree().getLemmas()) &&
				entry.getContext().equals(existing.getContext()))
			{
				logDiscardSinceExisting(entry);
				return true;
			}
		return false;
	}




	/**
	 * Returns the set of grammar entries that match. An entry of the given grammar matches if it
	 * has the same family than the lexicon entry, if their interfaces unify and if all equations of
	 * the lexicon entry can be applied satisfactorily. Moreover the entry must not have an empty
	 * semantics, this is a safe test that prevents selecting dummy entries. If one really wants to
	 * select entries with empty semantics, it is required that the interface of the entry carries a
	 * "sem=no" feature.
	 * @param grammar
	 * @param lexEntry
	 * @param input
	 * @param context
	 * @return a set of new grammar entries derived from the grammar entries but with proper
	 *         lemmatization, interface and context
	 */
	private Set<GrammarEntry> getMatchingGrammarEntries(Grammar grammar, SyntacticLexiconEntry lexEntry, Semantics input, InstantiationContext context)
	{
		Set<GrammarEntry> ret = new HashSet<GrammarEntry>();
		Set<GrammarEntry> byFamily = grammar.getEntriesByFamilies(lexEntry.getFamilies());

		if (byFamily.isEmpty())
		{
			logNoFamily(lexEntry.getFamilies());
			return ret;
		}

		for(GrammarEntry grammarEntry : byFamily)
		{
			FeatureStructure filter = new FeatureStructure();
			filter.add(new Feature("family", new FeatureConstant(grammarEntry.getTrace())));
			if (!new Subsumer().subsumes(lexEntry.getFilter().getFeatureStructure(), filter))
			{
				logTraceFailure(grammarEntry, lexEntry.getFilter());
				continue;
			}

			InstantiationContext newContext = new InstantiationContext(context);
			logTestingGrammarEntry(grammarEntry, newContext);

			/* 
			 * Check semantics emptiness. If a grammar entry has no semantics, ignore it unless
			 * the interface of the entry is marked with a feature sem=no, in which case keep it.
			 */
			if (grammarEntry.getSemantics().isEmpty())
			{
				if (grammarEntry.getInterface().hasConstantFeature("sem", "no"))
					logMaintainedEmptySemanticsGrammarEntry(grammarEntry);
				else
				{
					logIgnoredEmptySemanticsGrammarEntry(grammarEntry);
					continue;
				}
			}

			FeatureStructure fs = Unifier.unify(lexEntry.getInterface(), grammarEntry.getInterface(), newContext);
			if (fs == null)
			{
				logInterfaceUnificationFailure(grammarEntry, lexEntry, newContext);
				continue;
			}

			GrammarEntry newEntry = new GrammarEntry(grammarEntry); // this may be overkill
			String newName = newEntry.getTree().getId() + "-" + (currentId++);
			newEntry.getTree().setId(newName);
			newEntry.setName(newName);
			newEntry.setInterface(fs);
			
			newEntry.setProbability(lexEntry.getProbability());

			/* 
			 * If equations apply, we need to make sure that the semantics of the grammar entry is well instantiated with
			 * regards of the input semantics, the subsumption handles that. However we need to do the subsumption on the
			 * lex entry since doing it on the input semantics may raise multiple instantiations which should have been
			 * taken care of at the caller level, hence we can do the subsumption and take the first found context.
			 */
			if (applyEquations(newEntry, lexEntry.getEquations(), newContext))
			{
				InstantiationContext tmpContext = new InstantiationContext(newContext);
				Set<InstantiationContext> newContexts = newEntry.getSemantics().subsumes(lexEntry.getSemantics(), newContext);

				if (newContexts.isEmpty())
				{
					logContextUpdateFailure(newEntry, lexEntry, input, tmpContext);
					continue;
				}

				// we enabled family anchoring, hence the lemma should have been specified by equations
				Node mainAnchor = newEntry.getTree().getMainAnchor();
				if (mainAnchor == null)
					logger.error("Error: tree of " + newEntry + " has no main anchor!");
				if (lexEntry.getLemma() != null)
					mainAnchor.setAnchorLemma(lexEntry.getLemma());

				if (mainAnchor.getAnchorLemma() == null)
				{
					logger.error("Error: a grammar entry " + newEntry +
									" is missing a lemma, either specify it in the lexicon, in the equations or co-anchor equations (skipped)");
					continue;
				}

				newEntry.setContext(newContexts.iterator().next());

				if (GeneratorOption.USE_BIT_SEMANTICS)
					newEntry.setSemantics(new BitSemantics(newEntry.getSemantics(), input, newEntry.getContext()));

				ret.add(newEntry);
				logAddingEntry(newEntry);
			}
			else logFailingEquations(lexEntry.getEquations(), newContext);
		}
		return ret;
	}


	/**
	 * Applies the anchoring equations to the given grammar entry tree. It takes care of all the
	 * features assignment of the given entry, including the lemma value of the anchor if specified.
	 * Warning: some equations may be applied before it returns false! Since the entry is in general
	 * discarded it is not a problem, but be careful.
	 * @param entry
	 * @param equations
	 * @param context
	 * @return true if the equations have been applied or false if it was not possible
	 */
	public static boolean applyEquations(GrammarEntry entry, Equations equations, InstantiationContext context)
	{
		for(Equation eq : equations)
			if (!applyEquation(entry, eq, context))
				return false;
		return true;
	}


	/**
	 * Applies the given equation to the given entry.
	 * @param entry
	 * @param eq
	 * @param context
	 * @return wether the equation have been applied
	 */
	public static boolean applyEquation(GrammarEntry entry, Equation eq, InstantiationContext context)
	{
		// find node first
		String nodeId = eq.getNodeId();
		Node node = null;
		if (nodeId.equals("anchor"))
		{
			node = entry.getTree().getMainAnchor();
			if (node == null)
			{
				logTypedNodeNotFound("anchor", entry);
				return false;
			}
		}
		else if (nodeId.equals("foot"))
		{
			node = entry.getTree().getFoot();
			if (node == null)
			{
				logTypedNodeNotFound("foot", entry);
				return false;
			}
		}
		else if (nodeId.equals("root"))
		{
			node = entry.getTree().getRoot();
			if (node == null)
			{
				// there is then something reeeally wrong
				logTypedNodeNotFound("root", entry);
				return false;
			}
		}
		else
		{
			node = entry.getTree().getNodeById(nodeId);
			if (node == null)
			{
				logNamedNodeNotFound(entry, nodeId);
				if (!GeneratorOption.ALLOW_MISSING_COANCHORS)
					return false;
				else return true;
			}
		}

		// we found it, now check the FS unification, return false if failed
		FeatureStructure eqFs = eq.getFeatureStructure();
		FeatureStructure anchorFs = node.getFs(eq.getType());
		FeatureStructure result = Unifier.unify(anchorFs, eqFs, context);
		if (result == null)
			return false;
		else node.setFs(eq.getType(), result);

		// eventually, if the equation specifies a lemma feature sets the lemma of the node
		setLemma(node, eqFs, context);
		return true;
	}


	/**
	 * Sets the lemma of the given Node considering a given value in the given context.
	 * @param node
	 * @param fs
	 * @param context
	 */
	private static void setLemma(Node node, FeatureStructure fs, InstantiationContext context)
	{
		Feature lemmaFeat = fs.getFeature("lemma");
		if (lemmaFeat != null)
		{
			FeatureValue val = context.getValue(lemmaFeat.getValue());
			node.setAnchorLemma(new Lemma(val.toString()));
		}
	}


///// log messages

	private static void logStart(Semantics input)
	{
		if (logger.isDebugEnabled())
			logger.debug("Doing lexical selection with input: " + input);
	}


	private static void logEnd(Set<GrammarEntry> ret)
	{
		if (logger.isInfoEnabled())
			logger.info("Selected " + ret.size() + " grammar entries");
	}


	private void logNoSubsumption(Semantics entrySem, Semantics input)
	{
		if (logger.isTraceEnabled())
		{
			List<DefaultLiteral> missing = entrySem.difference(input);
			logger.trace("Sumbsumption failure, missing from input: " + missing);
		}
	}


	private static void logSemanticsIgnored(SyntacticLexiconEntry lexEntry)
	{
		if (logger.isTraceEnabled())
			logger.trace("Semantics of " + lexEntry.toStringOneLine() + " not selected because it is empty");
	}


	private static void logIgnoredEmptySemanticsGrammarEntry(GrammarEntry grammarEntry)
	{
		if (logger.isTraceEnabled())
			logger.trace("Entry " + grammarEntry.getName() + " is ignored since it has no semantics");
	}


	private static void logMaintainedEmptySemanticsGrammarEntry(GrammarEntry grammarEntry)
	{
		if (logger.isTraceEnabled())
			logger.trace("Entry " + grammarEntry.getName() + " has no semantics but it is kept since it carries a sem=no feature");
	}


	private static void logNoFamily(String[] families)
	{
		if (logger.isTraceEnabled())
			logger.trace("No grammar entries have been selected by families " + Utils.print(families));
	}


	private static void logTraceFailure(GrammarEntry entry, Filter filter)
	{
		if (logger.isTraceEnabled())
			logger.trace("Entry " + entry.getName() + " mismatch filter " + filter);
	}


	private static void logFailingEquations(Equations equations, InstantiationContext context)
	{
		if (logger.isTraceEnabled())
			logger.trace("Failing equations, check " + equations.toString(context));
	}


	private static void logFilteredResults(Set<GrammarEntries> filtered)
	{
		if (logger.isDebugEnabled())
			for(Set<GrammarEntry> e : filtered)
				logger.debug(GrammarEntry.toString(e));
	}


	private static void logTestingLexicalEntry(SyntacticLexiconEntry lexEntry)
	{
		if (logger.isTraceEnabled())
		{
			logger.trace("");
			logger.trace("Testing lexicon entry " + lexEntry.toShortString());
		}
	}


	@SuppressWarnings("unused")
	private static void logSemanticsNotSubsumed(SyntacticLexiconEntry lexEntry, Semantics input)
	{
		if (logger.isTraceEnabled())
			logger.trace("Semantics fail " + lexEntry.toStringOneLine() + " does not subsume " + input);
	}


	private static void logSemanticsSubsumed(InstantiationContext context)
	{
		if (logger.isDebugEnabled())
			logger.debug("Entry subsumes input in context " + context);
	}


	private static void logTestingGrammarEntry(GrammarEntry grammarEntry, InstantiationContext context)
	{
		if (logger.isTraceEnabled())
			logger.trace("Testing grammar entry " + grammarEntry.getName() + " in context " + context);
	}


	private static void logInterfaceUnificationFailure(GrammarEntry grammarEntry, SyntacticLexiconEntry lexEntry, InstantiationContext context)
	{
		if (logger.isTraceEnabled())
			logger.trace("Interface fail for " + grammarEntry.getName() + " " + lexEntry.getInterface() + " " +
							grammarEntry.getInterface() + " in context " + context);
	}


	private static void logContextUpdateFailure(GrammarEntry newEntry, SyntacticLexiconEntry lexEntry, Semantics input, InstantiationContext context)
	{
		if (logger.isTraceEnabled())
			logger.trace("Subsumption fail for " + newEntry.getName() +
							", likely caused by a mismatch in resources, the semantics of the grammar entry does not match the lexical entry: " +
							newEntry.getSemantics().toString(context) + " does not subsume " + lexEntry.getSemantics().toString(context));
	}


	private static void logTypedNodeNotFound(String type, GrammarEntry entry)
	{
		logger.error("Error: unable to find " + type + " node of " + entry.getName() + ", please check this tree");
	}


	private static void logNamedNodeNotFound(GrammarEntry entry, String nodeId)
	{
		if (GeneratorOption.ALLOW_MISSING_COANCHORS)
		{
			logger.warn("Warning: unable to find node of " + entry.getName() + " named '" + nodeId +
								"' please check the co-anchor equations (equation ignored because of ALLOW_MISSING_COANCHORS)");
		}
		else
		{
			logger.error("Error: unable to find node of " + entry.getName() + " named '" + nodeId + "' please check the co-anchor equations");
		}
	}


	private static void logNumeralVariablesClash(InstantiationContext context)
	{
		if (logger.isDebugEnabled())
			logger.debug("The following context is discarded since it contains equal numeral variables and NUMERAL_VARIABLES_CLASH has been set: " + context);
	}
	

	private static void logDiscardSinceExisting(GrammarEntry entry)
	{
		if (logger.isTraceEnabled())
			logger.trace("Discarding "+entry.getName()+" since there already exists a selected entry with that name");
		
	}



	private void logAddingEntry(GrammarEntry newEntry)
	{
		if (logger.isTraceEnabled())
			logger.trace("Selecting entry "+newEntry.getName());
	}

}
