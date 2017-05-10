package synalp.generation.jeni.selection.families;

import java.io.File;
import java.util.*;

import synalp.commons.grammar.*;
import synalp.commons.utils.loggers.LoggerConfiguration;
import synalp.generation.configuration.GeneratorConfiguration;

@SuppressWarnings("javadoc")
public class TestSemanticsBuilder
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		LoggerConfiguration.init();
		File grammarFile = GeneratorConfiguration.getGrammarFile("semxtag");
		File patternFile = new File("resources/sem-xtag2/semantics.sem");
		Grammar grammar = FamilySemanticsBuilder.setGrammarSemantics(grammarFile, patternFile, false);
		List<GrammarEntry> entries = new ArrayList<GrammarEntry>(grammar.getEntries());
		Grammar.sortByNames(entries);
		Grammar.printShort(entries);
	}

}
