package synalp.generation.jeni;

import java.io.File;
import java.util.*;

import org.apache.log4j.*;

import synalp.commons.grammar.*;
import synalp.commons.lexicon.SyntacticLexicon;
import synalp.commons.semantics.*;
import synalp.commons.utils.*;
import synalp.commons.utils.exceptions.TimeoutException;
import synalp.generation.*;
import synalp.generation.configuration.*;
import synalp.generation.jeni.selection.families.FamilyLexicalSelection;
import synalp.generation.jeni.semantics.rules.Rules;
import synalp.generation.morphology.*;
import synalp.generation.ranker.*;
import synalp.generation.selection.*;
import static synalp.generation.configuration.GeneratorOption.*;

/**
 * JeniGenerator is the Java port of the GenI generator.
 * @author Alexandre Denis
 */
public class JeniGenerator implements Generator
{
	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(JeniGenerator.class);

	private Grammar grammar;
	private JeniChartItems chart;
	private TreeCombiner combiner;
	private SyntacticLexicon lexicon;
	private Rules rules = new Rules();
	private Ranker ranker = new DefaultRanker();
	private MorphRealizer morphRealizer = new DefaultMorphRealizer();
	private LexicalSelection lexicalSelection;

	private GenerationReport generationReport; // the current report, may be null


	/**
	 * Creates a new Generator based on given grammar and lexicon. It uses a DefaultRanker and a DefaultMorphRealizer.
	 * @param grammar
	 * @param lexicon
	 */
	public JeniGenerator(Grammar grammar, SyntacticLexicon lexicon)
	{
		init(grammar, lexicon, new JeniLexicalSelection(grammar, lexicon), new DefaultMorphRealizer(), new DefaultRanker(), new TreeCombiner());
	}


	/**
	 * Creates a new JeniGenerator based on given configuration.
	 * @param config
	 */
	public JeniGenerator(GeneratorConfiguration config)
	{
		config.load();

		switch (SELECTION)
		{
			case CLASSICAL_SELECTION:
				grammar = config.getGrammar();
				lexicon = config.getSyntacticLexicon();
				lexicalSelection = new JeniLexicalSelection(grammar, lexicon);
				break;

			case PATTERN_SELECTION:
				grammar = config.getGrammar();
				File lexicalPatterns = config.getResource("lexical_semantics");
				File grammarPatterns = config.getResource("grammar_semantics");
				lexicalSelection = FamilyLexicalSelection.newInstance(lexicalPatterns, grammarPatterns, grammar);
				break;
		}

		switch (RANKER)
		{
			case DEFAULT_RANKER:
				ranker = new DefaultRanker();
				break;

			case NGRAM_RANKER:
				File lmFile = config.getResource("lmfile");
				ranker = new NgramRanker(lmFile.getPath(), BEAM_SIZE, "");
				break;
		}
		
		
		switch(PROBA_STRATEGY_TYPE)
		{
			case DEFAULT:
				combiner = new TreeCombiner(p -> 1);
				break;
				
			case SIMPLE:
				combiner = new TreeCombiner(p -> p.getParentItemSource().getProbability() * p.getParentItemTarget().getProbability());
				break;
		}
		
		
		if (config.hasMorphLexicon())
			morphRealizer = new DefaultMorphRealizer(config.getMorphLexicon());
		else morphRealizer = new DefaultMorphRealizer();

	}


	/**
	 * Initializes the fields of this JeniGenerator.
	 * @param grammar
	 * @param lexicon
	 * @param selection
	 * @param morphRealizer
	 * @param ranker
	 */
	private void init(Grammar grammar, SyntacticLexicon lexicon, LexicalSelection selection, MorphRealizer morphRealizer, Ranker ranker, TreeCombiner combiner)
	{
		this.grammar = grammar;
		this.lexicon = lexicon;
		this.lexicalSelection = selection;
		this.morphRealizer = morphRealizer;
		this.ranker = ranker;
		this.combiner = combiner;
	}


	@Override
	public List<JeniRealization> generate(Semantics semantics)
	{
		TimeoutManager.start();
		generationReport = new GenerationReport();
		generationReport.setStartTime(new Date());
		generationReport.setAllItems(new JeniChartItems());
		generationReport.setOriginalInput(new Semantics(semantics));

		List<JeniRealization> ret = new ArrayList<JeniRealization>();
		try
		{
			
			ret = generateItems(semantics).getRealizations();
			for(JeniRealization real : ret)
				morphRealizer.setMorphRealizations(real);
			
				
			
		}
		catch (TimeoutException e)
		{
			logger.warn(e.getMessage());
			generationReport.setErrorMessage("Timeout: " + e.getMessage());
			generationReport.setTotalTime(TimeoutManager.getTimeSinceStart());
			throw e;
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			generationReport.setErrorMessage("Exception: " + e.getMessage());
		}

		generationReport.setTotalTime(TimeoutManager.getTimeSinceStart());
		generationReport.setSyntacticRealizations(ret);

		return ret;
	}


	/**
	 * Returns items realizing the given semantics.
	 * @param semantics
	 * @return a list of complete chart items
	 */
	private JeniChartItems generateItems(Semantics semantics)
	{
		logRealizationStart(semantics);
		rules.apply(semantics);
		logAppliedRules(semantics);

		JeniChartItem.resetIdCount();
		JeniChartItems ret = new JeniChartItems();

		LexicalSelectionResult result = lexicalSelection.selectEntries(semantics);

		generationReport.setLexicalSelection(result);

		for(Semantics sem : result.getInputs())
			for(JeniChartItems selectedGroup : createItemsGroups(result.getResults(sem)))
			{
				Semantics workingSem = GeneratorOption.USE_BIT_SEMANTICS ? new BitSemantics(sem, true) : sem;
				logGenerationStart(workingSem);
				ret.addAll(generate(workingSem, selectedGroup));
			}

		generationReport.setResultingItems(ret);

		return ret;
	}


	/**
	 * Creates groups of items, one for each GrammarEntries object.
	 * @param entriesGroups
	 * @return a set in which each element is itself a set of chart items
	 */
	private Set<JeniChartItems> createItemsGroups(Set<GrammarEntries> entriesGroups)
	{
		Set<JeniChartItems> ret = new HashSet<JeniChartItems>();
		for(GrammarEntries entries : entriesGroups)
		{
			JeniChartItems items = new JeniChartItems();
			for(GrammarEntry entry : entries)
			{
				TimeoutManager.checkTimeout("Timeout in preparing agenda");
				items.addDoublons(new JeniChartItem(entry.getTree(), entry.getSemantics(), entry.getContext(), entry.getProbability()));
			}
			ret.add(items);
		}
		return ret;
	}


	/**
	 * Generates the given semantics with a priori list of selected items.
	 * @param semantics
	 * @param agenda
	 * @return chart items, each one being a surface realization
	 */
	//MAIN LOOP TO MODIFY
	protected JeniChartItems generate(Semantics semantics, JeniChartItems agenda)
	{
		
		chart = new JeniChartItems();
		JeniChartItems auxiliaryAgenda = new JeniChartItems();

		logInitialAgenda(agenda);

		logger.info("*** Substitution phase");

		while(!agenda.isEmpty())
			performSubstitutionStep(agenda, chart, auxiliaryAgenda);

		ruleOutIncompleteSubstitutions(chart);

		// now adjunctions
		agenda = new JeniChartItems(chart);
		chart = new JeniChartItems(auxiliaryAgenda);

		print("chart", chart);
		print("agenda", agenda);

		logger.info("*** Adjunction phase (chart: " + chart.size() + ", agenda: " + agenda.size() + ")");

		JeniChartItems ret = new JeniChartItems();
		while(!agenda.isEmpty())
			ret.addAll(performAdjunctionStep(semantics, agenda, chart));

		print("results before filtering", ret);
		ruleOutNonUnifyingTopBotTrees(ret);
		setupLemmaFeatures(ret);

		logResults(ret);

		return ret;
	}

	
	/**
	 * Prepares the lemmas of all trees for the morphology.
	 * It unifies the top and bot fs for all lemmas.
	 * @param chart
	 */
	protected static void setupLemmaFeatures(JeniChartItems chart)
	{
		for(JeniChartItem item : chart)
			item.getTree().setupLemmaFeatures(item.getContext());
	}
	

	/**
	 * Remove items whose tree contain nodes that do not unify top and bot fs.
	 * @param chart
	 */
	protected static void ruleOutNonUnifyingTopBotTrees(JeniChartItems chart)
	{
		for(Iterator<JeniChartItem> it = chart.iterator(); it.hasNext();)
		{
			JeniChartItem item = it.next();
			Tree tree = item.getTree();
			Node failedNode = tree.getFailedTopBotNode(item.getContext());
			if (failedNode != null)
			{
				it.remove();
				logRulingOutNonUnifyingTopBot(item, failedNode);
			}
		}
	}


	/**
	 * Performs one step of substitutions. One item from the agenda is removed, if it is an
	 * auxiliary tree put it in auxiliaryAgenda, else try to combine it with items in chart and add
	 * result to agenda. Eventually add the item to the chart.
	 * @param agenda
	 * @param chart
	 * @param auxiliaryAgenda
	 */
	private void performSubstitutionStep(JeniChartItems agenda, JeniChartItems chart, JeniChartItems auxiliaryAgenda)
	{
		TimeoutManager.checkTimeout("Timeout in substitution step");

		logger.debug("*** substitution step, agenda size: " + agenda.size());

		//print("agenda", agenda);
		//print("chart", chart);

		JeniChartItem item = agenda.removeFirst();
		if (item.getTree().getFoot() != null && item.getTree().getSubstitutions().isEmpty())
			auxiliaryAgenda.add(item);
		else
		{
			logTestSubstitution(item);

			JeniChartItems pending = new JeniChartItems(agenda, chart, auxiliaryAgenda);

			JeniChartItems newItems = new JeniChartItems();
			for(JeniChartItem other : chart)
				newItems.addAll(combiner.getSubstitutionCombinations(item, other, pending));
			logSubstitutionResult(item, newItems);

			JeniChartItems filtered = new JeniChartItems(ranker.rank(newItems));
			logFilteredResult(newItems, filtered);

			chart.add(item);
			agenda.addAll(filtered);
		}
	}


	/**
	 * Performs one step of adjunctions. One item from the agenda is removed and if its semantics
	 * match the input semantics it shall be returned, then try to combine it with items in chart
	 * and add result to agenda.
	 * @param input the original semantics we want to generate
	 * @param agenda
	 * @param chart
	 * @return a list of newly created chart items
	 */
	private JeniChartItems performAdjunctionStep(Semantics input, JeniChartItems agenda, JeniChartItems chart)
	{
		TimeoutManager.checkTimeout("Timeout in adjunction step");

		logger.debug("*** adjunction step, agenda size: " + agenda.size());

		print("agenda", agenda);
		print("chart", chart);

		JeniChartItems results = new JeniChartItems();
		JeniChartItem item = agenda.removeFirst();

		// this eats a lot of memory!
		//generationReport.getAllItems().add(item);

		/* 
		 * adding the result and stopping has the problematic effect that empty semantics adjunctions
		 * are not considered, typically the auxiliaries.
		 */
		//System.out.println("Doing "+item);
		if (item.getSemantics().equals(input, item.getContext()))
		{
			logSemanticsCoverageSuccess(item, input);
			results.add(item);
		}
		else logSemanticsCoverageFailed(item, input);

		logTestingAdjunction(item);
		JeniChartItems newItems = new JeniChartItems();
		for(JeniChartItem other : chart)
			newItems.addAll(combiner.getAdjunctionCombinations(item, other));
		logAdjunctionResult(item, newItems);

		JeniChartItems filtered = new JeniChartItems(ranker.rank(newItems));
		logFilteredResult(newItems, filtered);

		agenda.addAll(filtered);

		return results;
	}


	/**
	 * Rules out items with non substitued nodes.
	 * @param chart
	 */
	private static void ruleOutIncompleteSubstitutions(JeniChartItems chart)
	{
		for(Iterator<JeniChartItem> it = chart.iterator(); it.hasNext();)
		{
			JeniChartItem item = it.next();
			Tree tree = item.getTree();
			if (!tree.getSubstitutions().isEmpty())
			{
				it.remove();
				logRulingOutIncompleteSubstitution(item);
			}
		}
	}


	/**
	 * Returns the chart.
	 * @return the chart
	 */
	public JeniChartItems getChart()
	{
		return chart;
	}


	/**
	 * @return the rules
	 */
	public Rules getRules()
	{
		return rules;
	}


	/**
	 * @param rules the rules to set
	 */
	public void setRules(Rules rules)
	{
		this.rules = rules;
	}


	/**
	 * @return the grammar
	 */
	public Grammar getGrammar()
	{
		return grammar;
	}


	/**
	 * @param grammar the grammar to set
	 */
	public void setGrammar(Grammar grammar)
	{
		this.grammar = grammar;
	}


	/**
	 * @return the lexicon
	 */
	public SyntacticLexicon getLexicon()
	{
		return lexicon;
	}


	/**
	 * @param lexicon the lexicon to set
	 */
	public void setLexicon(SyntacticLexicon lexicon)
	{
		this.lexicon = lexicon;
	}


	/**
	 * @return the ranker
	 */
	public Ranker getRanker()
	{
		return ranker;
	}


	/**
	 * @param ranker the ranker to set
	 */
	public void setRanker(Ranker ranker)
	{
		this.ranker = ranker;
	}


	/**
	 * @return the morphRealizer
	 */
	public MorphRealizer getMorphRealizer()
	{
		return morphRealizer;
	}


	/**
	 * @param morphRealizer the morphRealizer to set
	 */
	public void setMorphRealizer(MorphRealizer morphRealizer)
	{
		this.morphRealizer = morphRealizer;
	}


	/**
	 * @return the combiner
	 */
	public TreeCombiner getCombiner()
	{
		return combiner;
	}


	/**
	 * @param combiner the combiner to set
	 */
	public void setCombiner(TreeCombiner combiner)
	{
		this.combiner = combiner;
	}


	/**
	 * @return the lexicalSelection
	 */
	public LexicalSelection getLexicalSelection()
	{
		return lexicalSelection;
	}


	/**
	 * @param lexicalSelection the lexicalSelection to set
	 */
	public void setLexicalSelection(LexicalSelection lexicalSelection)
	{
		this.lexicalSelection = lexicalSelection;
	}


	/**
	 * @return the generationReport
	 */
	public GenerationReport getGenerationReport()
	{
		return generationReport;
	}


///// log messages

	protected static void logResults(JeniChartItems items)
	{
		if (logger.isInfoEnabled())
			printInfo("results", items);
	}


	protected static void logInitialAgenda(JeniChartItems agenda)
	{
		if (logger.isInfoEnabled())
			printInfo("initial agenda", agenda);
	}


	protected static void printInfo(String prefix, JeniChartItems items)
	{
		if (logger.isInfoEnabled())
			logger.info("--- " + prefix + " : " + items.size() + " items");
		else logger.info("--- " + prefix);

		if (logger.isTraceEnabled())
			for(JeniChartItem item : items) {
				logger.trace("\n" + item.toString(ItemFormat.COMPLETE_FULL_TREE));
				
			}
		
		else if (logger.isDebugEnabled()) {
			for(JeniChartItem item : items) {
				logger.debug("\n" + item.toString(ItemFormat.MULTI_LINE_SHORT));
				logger.debug("\n probability: " + item.getProbability());
			}
			
		}
	}


	private static void logRulingOutIncompleteSubstitution(JeniChartItem item)
	{
		if (logger.isDebugEnabled())
			logger.debug("Ruling out " + item);
	}


	private static void logSubstitutionResult(JeniChartItem item, JeniChartItems newItems)
	{
		if (logger.isTraceEnabled())
			logger.trace("Substitutions with " + item + " give: " + newItems);
	}


	private static void logFilteredResult(JeniChartItems items, JeniChartItems filtered)
	{
		if (logger.isTraceEnabled())
		{
			logger.trace("Previous items: " + items.toStringLemmas());
			logger.trace("After filtering: " + filtered.toStringLemmas());
		}
	}


	private static void logAdjunctionResult(JeniChartItem item, JeniChartItems newItems)
	{
		if (logger.isTraceEnabled())
			logger.trace("Adjunctions with " + item + " give: " + newItems);
	}


	private static void logTestSubstitution(JeniChartItem item)
	{
		if (logger.isTraceEnabled())
			logger.trace("Testing substitutions with " + item);
	}


	private static void logTestingAdjunction(JeniChartItem item)
	{
		if (logger.isTraceEnabled())
			logger.trace("Testing adjunctions with " + item);
	}


	private static void logSemanticsCoverageFailed(JeniChartItem item, Semantics input)
	{
		if (logger.isDebugEnabled())
		{
			List<DefaultLiteral> missing = input.difference(item.getSemantics(), item.getContext());
			List<DefaultLiteral> surplus = item.getSemantics().difference(input, item.getContext());
			logger.debug("Coverage failure for item " + item.getId() + " " + Utils.print(item.getLemmas(), " ", "[", "]") +
							(missing.isEmpty() ? "" : " missing:" + new Semantics(missing).toString(item.getContext())) +
							(surplus.isEmpty() ? "" : " surplus:" + new Semantics(surplus).toString(item.getContext())) +
							"\nitem:[" + item.getSemantics().toStringSorted(item.getContext()) + "]\ninput:[" + input.toStringSorted(item.getContext()) + "]");
		}
	}


	private static void logSemanticsCoverageSuccess(JeniChartItem item, Semantics input)
	{
		if (logger.isDebugEnabled())
			logger.debug("Coverage success for item " + item.getId() + " " + Utils.print(item.getLemmas(), " ", "[", "]"));
	}


	private static void logRealizationStart(Semantics semantics)
	{
		if (logger.isInfoEnabled())
			logger.info("************* Performing generation of " + semantics + " *************");
	}


	private static void logGenerationStart(Semantics semantics)
	{
		if (logger.isInfoEnabled())
			logger.info("Starting generation of input: " + semantics);
	}


	private static void logAppliedRules(Semantics semantics)
	{
		if (logger.isDebugEnabled())
			logger.debug("After rewriting by rules: " + semantics);
	}


	private static void logRulingOutNonUnifyingTopBot(JeniChartItem item, Node failedNode)
	{
		if (logger.isTraceEnabled())
			logger.trace("Ruling out non unifying top-bot " + item.getId() + ": " + Utils.print(item.getLemmas(), " ", "[", "]") + " " +
							item.getTree().getId() +
							" Failed node: " + failedNode.toString(NodeFormat.SINGLE_NODE, item.getContext()));
		else if (logger.isDebugEnabled())
			logger.debug("Ruling out non unifying top-bot " + item.getId() + ": " + Utils.print(item.getLemmas(), " ", "[", "]") + " " + item.getTree());
	}


	/**
	 * Print the given chart items using the given debug prefix.
	 * @param prefix
	 * @param items
	 */
	private static void print(String prefix, JeniChartItems items)
	{
		if (logger.isDebugEnabled())
		{
			if (logger.isTraceEnabled())
			{
				logger.trace("--- " + prefix);
				for(JeniChartItem item : items)
				{
					logger.trace(item.toString(ItemFormat.SINGLE_LINE_FULL));
					logger.trace("");
				}
			}
			else
			{
				logger.debug("--- " + prefix);
				for(JeniChartItem item : items)
					logger.debug(item.toString());
			}
		}
	}


	@Override
	public GeneratorType getGeneratorType()
	{
		return GeneratorType.JENI_DEFAULT;
	}

}
