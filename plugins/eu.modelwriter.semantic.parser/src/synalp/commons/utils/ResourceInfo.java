package synalp.commons.utils;

import java.util.*;

import synalp.commons.grammar.*;
import synalp.commons.input.TestSuite;
import synalp.commons.lexicon.SyntacticLexicon;
import synalp.commons.unification.FeatureConstant;
import synalp.generation.configuration.GeneratorConfiguration;

/**
 * Shows some requests on the Resources.
 * @author Alexandre Denis
 */
public class ResourceInfo
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		//printInfo(TRANSSEM_BUNDLE);
		//printNbOfTrees(loadGrammar(FRENCH_GRAMMAR));
		//printNbOfEntries(loadLexicon(FRENCH_LEXICON));
		//printAllClasses(loadGrammar(SEMXTAG2_GRAMMAR));
		//printGrammarByAdjunction(loadGrammar(SEMXTAG2_FULL_GRAMMAR), "n", FootType.RIGHT);
		printGrammarByAnchors(GeneratorConfiguration.getGrammar("semxtag2"));
		
		//printGrammarByTrace(loadGrammar(SEMXTAG2_GRAMMAR), "Reln0V");
	}


	/**
	 * Prints general info about the given config.
	 * @param bundle
	 */
	public static void printInfo(GeneratorConfiguration config)
	{
		config.load();
		printNbOfTrees(config.getGrammar());
		printNbOfEntries(config.getSyntacticLexicon());
		printNbOfEntries(config.getTestSuite());
	}


	/**
	 * Prints the number of entries of given TestSuite.
	 * @param suite
	 */
	public static void printNbOfEntries(TestSuite suite)
	{
		System.out.println("suite has " + suite.size() + " entries");
	}


	/**
	 * Prints the number of entries of given lexicon.
	 * @param lexicon
	 */
	public static void printNbOfEntries(SyntacticLexicon lexicon)
	{
		System.out.println("lexicon has " + lexicon.size() + " entries");
	}


	/**
	 * Prints the number of trees of grammar.
	 * @param grammar
	 */
	public static void printNbOfTrees(Grammar grammar)
	{
		System.out.println("grammar has " + grammar.size() + " trees");
	}


	/**
	 * Print all classes of the grammar.
	 * @param grammar
	 */
	public static void printAllClasses(Grammar grammar)
	{
		Set<String> traces = new HashSet<String>();
		for(GrammarEntry entry : grammar.values())
			traces.addAll(entry.getTrace());
		List<String> orderedTraces = new ArrayList<String>(traces);
		Collections.sort(orderedTraces);
		for(String trace : orderedTraces)
			System.out.println(trace);
	}


	/**
	 * Print all entries of given grammar gathered by type of anchors, sorted by tree size.
	 * @param grammar
	 */
	public static void printGrammarByAnchors(Grammar grammar)
	{
		for(String cat : grammar.getAllCategories())
		{
			System.out.println("Cat: " + cat);
			List<GrammarEntry> entries = new ArrayList<GrammarEntry>(grammar.getEntriesByAnchorCategory(new FeatureConstant(cat)));
			Grammar.sortByTreeSize(entries);
			Grammar.printShort(entries);
			System.out.println("\n");
		}
	}
	
	
	/**
	 * Prints all entries of given grammar that match given trace.
	 * @param grammar 
	 * @param traceElements 
	 */
	public static void printGrammarByTrace(Grammar grammar, String...traceElements)
	{
		Trace trace = new Trace(traceElements);
		for(GrammarEntry entry : grammar.getEntriesContainingTrace(trace))
			System.out.println(entry+"\n");
	}
	
	
	/**
	 * Prints all entries of given grammar that adjunct on given category.
	 * @param grammar 
	 * @param cat 
	 * @param type 
	 */
	public static void printGrammarByAdjunction(Grammar grammar, String cat, FootType type)
	{
		FeatureConstant catConst = new FeatureConstant(cat);
		for(GrammarEntry entry : grammar.getEntriesByFoot(catConst, type))
			System.out.println(entry+"\n");
	}
}
