package synalp.parsing;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import synalp.commons.derivations.DerivationTree;
import synalp.commons.grammar.Grammar;
import synalp.commons.grammar.GrammarEntries;
import synalp.commons.grammar.GrammarEntry;
import synalp.commons.grammar.Node;
import synalp.commons.grammar.Tree;
import synalp.commons.input.Lemma;
import synalp.commons.lexicon.Equation;
import synalp.commons.lexicon.Equations;
import synalp.commons.lexicon.SyntacticLexicon;
import synalp.commons.lexicon.SyntacticLexiconEntry;
import synalp.commons.lexicon.lexformat.LexFormatEntry;
import synalp.commons.lexicon.lexformat.LexFormatLexicon;
import synalp.commons.lexicon.lexformat.javacc.LexFormatParser;
import synalp.commons.semantics.Semantics;
import synalp.commons.unification.Feature;
import synalp.commons.unification.FeatureConstant;
import synalp.commons.unification.FeatureStructure;
import synalp.commons.unification.FeatureValue;
import synalp.commons.unification.InstantiationContext;
import synalp.commons.unification.Subsumer;
import synalp.commons.unification.Unifier;
import synalp.generation.configuration.GeneratorOption;
import synalp.generation.jeni.JeniChartItem;
import synalp.generation.jeni.JeniChartItems;
import synalp.generation.jeni.JeniRealization;
import synalp.generation.jeni.TreeCombiner;
import synalp.parsing.Inputs.Sentence;
import synalp.parsing.Inputs.Word;
import synalp.parsing.treeCombinations.CKYChartCombinations;
import synalp.parsing.utils.CustomTextReplacement;
import synalp.parsing.utils.OrderedCombinations;
import synalp.parsing.utils.WordOrder;
import synalp.parsing.utils.nlptools.PoS;

/**
 * A. All derivations obtained by combining input Trees (two intermediate trees at a time in case of CKY based combination and all input trees in case of cartesian combinations)
 *  are subject to the following checks in order :
 * 		1. ruleOutIncorrectWordOrderTrees() -- To remove derivations that don't contain the ascending word order in their lexical nodes -- Method present in ParseTreesCombiner.java (Both CKYChartCombinations.java and FullCombinations.java call this class for combining the trees.)
 * 		2. ruleOutIncompleteDerivations() -- To remove derivations that didn't result from the combination of ALL the trees in its input -- Method present in CKYChartCombinations.java and FullCombinations.java
 * 
 * The identification of good and bad examples for Machine Learning Task begins from the final set of trees obtained after the completion of phase A described above.
 * 
 * B. Once the final set of trees are obtained (In case of CKY, the most-top row which is not empty in case the partial parse results are desired), the following checks are carried out in order : 
 * (All of these methods are present in this class itself)
 * 		1. ruleOutNonContiguousWordOrderTrees() -- To remove outputs that contain interior missing words. Doesn't check for exact equality because we also want partial results. So, this will retain results 
 * 				such as "John sleeps" in the input sentence "John Sleeps at night" but won't preserve output such as "John sleeps night". A limitation with this approach is that results such as "John sleeps"
 * 				are discarded when the input is "John always sleeps". Note : This has been disabled for now.
 *		2. ruleOutIncompleteSubstitutions() -- To remove derivations not fully lexicalised.
 *		3. ruleOutNonUnifyingTopBotTrees() -- To remove derivations whose top/down f.s. don't unify.
 *
 *
 * C. In case the final set of trees contain both partial and complete results, only (all of the) complete results are preserved. Else all of the partial results are preserved. 
 *
 * D. For sake of machine learning, all derived trees (and their recursive parent derived trees) belonging to category C described above fall under
 * bad examples. The rest of the derived trees (and their recursive parent derived trees) -- either complete or partial parse results -- will be
 *  considered as good examples.
 *
 * E. The idea behind Machine Learning is to ..
 * @author bikashg
 */


public class Parser {

	static Logger logger = Logger.getLogger(Parser.class);
	
	private Grammar grammar;
	private String macroFileDirectoryPath;
	
	private Set<String> newLexicalItemsProposed;
	private ArrayList<ParseResult> parseResults;
	
	/**
	 * For ML Purpose, the set of derivations that lead to bad and good word order output.
	 */
	private JeniChartItems MLGoodExamples;
	private JeniChartItems MLBadExamples;

	
	/**
	 * To store the criticial info representing this parse;
	 */
	private String statusReport;
	
	public Parser(Grammar grammar, String macroFileDirectoryPath) throws IOException {
		this.grammar = grammar;
		this.macroFileDirectoryPath = macroFileDirectoryPath;
		
		this.newLexicalItemsProposed = new HashSet<String>();
		this.parseResults = new ArrayList<ParseResult>();
		
		MLGoodExamples = new JeniChartItems();
		MLBadExamples = new JeniChartItems();
		
		this.statusReport = "";
	}


	
	
 	public void parse(Sentence inputSentence, SyntacticLexicon fullySpecifiedLexicon, SyntacticLexicon underSpecifiedLexicon, Map<String,Set<String>> morphs, boolean useProbability, String parseLogFileName) throws Exception {
 		// setting up a FileAppender dynamically... for this current processing
 		logger.removeAllAppenders();//To remove appenders associated to the static logger from the past run.
	    SimpleLayout layout = new SimpleLayout();    
	    FileAppender appender = new FileAppender(layout,parseLogFileName,false);    
	    logger.addAppender(appender);
	    logger.setAdditivity(false); // so as not to pass the log upwards to the root logger. 
 		
 		
 		logger.info("\n\n\n");
		logger.info("Input Sentence = \n\t"+inputSentence.getPlainSentence_AllWords()+"\n\t"+inputSentence+"\n\tLexical Items Selecting Words = "+inputSentence.getLexicalItemsSelectingWords()+"\n\n\n\n");

		
		
		Map<String,Set<GrammarEntry>> newLexicalEntryStrings = new HashMap<String,Set<GrammarEntry>>(); // new possible entries for current test suite.
		Set<String> multiWordAnchors_prevWord = new HashSet<String>(); 

		
		LinkedHashMap<Word,GrammarEntries> wordsToLexicalisedTrees = new LinkedHashMap<Word,GrammarEntries>(); // words to lexically instantiated trees. Insertion order important for cky combinations.
		ArrayList<Word> lexicalItemsSelectingWords = inputSentence.getLexicalItemsSelectingWords();
	
		

		// Each word of the inputSentence can itself be with the _+_ sign. E.g. : component_+_backslash_+_item_+_backslash_+_object
		// This will be used in checking exactness of multiword anchored lexical item with the words of the input sentence.
		List<String> singleWordsOfInputSentence = new ArrayList<String>();
		for (Word w:inputSentence.getAllWords()) {
			String value = w.getToken();
			if (value.contains("_+_")) {
				String[] values = value.split("\\_\\+\\_");
				for (String val:values)
					singleWordsOfInputSentence.add(val);
				}
			else {
				singleWordsOfInputSentence.add(value);
			}
		}
		
		
		
		for (int i=0; i<lexicalItemsSelectingWords.size(); i++) {
			// *********************************** Put if to check if the word needs to be parsed.
			Word lexicalItemSelectingWord = lexicalItemsSelectingWords.get(i);

			
			//logger.info("\nCurrent word = "+contentWord.getToken()+ ", prevAnchor = "+previousAnchor;
			// Skip if the current content word is present as multiword entry of the lexical entry selected for previous word.
			if (containedinPreviousMultiWordAnchor(multiWordAnchors_prevWord, "_+_"+lexicalItemSelectingWord.getToken().toLowerCase())) {
				continue;
			}
			
			// For previous content word
			if (i>0) {
				Word prevLexicalItemSelectingWord = lexicalItemsSelectingWords.get(i-1);
				// For those multiword anchors which contained the previous word:
				for (String prevAnchor:multiWordAnchors_prevWord) {
					if (prevAnchor.endsWith(prevLexicalItemSelectingWord.getToken()))
					{
						addGrammarEntries(inputSentence, fullySpecifiedLexicon, underSpecifiedLexicon, prevAnchor, prevLexicalItemSelectingWord, morphs, newLexicalEntryStrings, wordsToLexicalisedTrees);
					}
				}
				
				// For those words which were not a part of multiword expression:
				if (multiWordAnchors_prevWord.isEmpty()) {
					addGrammarEntries(inputSentence, fullySpecifiedLexicon, underSpecifiedLexicon, prevLexicalItemSelectingWord.getToken().toLowerCase(), prevLexicalItemSelectingWord, morphs, newLexicalEntryStrings, wordsToLexicalisedTrees);					
				}
			}	
			
			
			// For current content word
			// Empty the previousAnchors
			multiWordAnchors_prevWord = new HashSet<String>();
			
			
			for (SyntacticLexiconEntry lexEntry:fullySpecifiedLexicon) {
				String lexemeAnchor = lexEntry.getLemma().getValue();
				String checkAnchor = lexemeAnchor;
			
				if (checkAnchor.contains("_+_") && checkAnchor.startsWith(lexicalItemSelectingWord.getToken())) {
					//System.out.println("LexemeAnchor = "+checkAnchor+" Selecting word = "+lexicalItemSelectingWord);
					String[] splits = checkAnchor.split("\\_\\+\\_"); // split treats as regular expression.
					
					
					// We want to get the portion of the input word from the string selecting this lexical entry. 
					// And if before this string there were multiword anchors; the startIndex to look from in the
					// inputWords list must be adjusted accordingly.
					int startIndex = 0;
					for (int j=0;j<lexicalItemSelectingWord.getIndex();j++) {
						Word w = inputSentence.getWordAtIndex(j);
						String value = w.getToken();
						if (value.contains("_+_")) {
							String[] values = value.split("\\_\\+\\_");
							startIndex = startIndex + values.length;
						}
						else {
							startIndex ++;
						}
					}
					
					if (singleWordsOfInputSentence.size()>=startIndex+splits.length) {
						List<String> matchLookRange = singleWordsOfInputSentence.subList(startIndex, startIndex+splits.length);
						/*System.out.print("To check if ");
						for (String x:splits) {
							System.out.print(x+" ");
						}
						System.out.print("is equal to "+matchLookRange+"\n\n");*/
						if (isExactMatch(splits,matchLookRange)) {
							checkAnchor = splits[0];
						}
					}
				}
				
				if (checkAnchor.equalsIgnoreCase(lexicalItemSelectingWord.getToken())) {
					multiWordAnchors_prevWord.add(lexemeAnchor);
				}
			} 
			
		} // End of for loop -- finding multiwordAnchors for current word and grammar trees for previous word.

		
		// Create fully specified and underspecified entries for the last word.
		Word lastWord = lexicalItemsSelectingWords.get(lexicalItemsSelectingWords.size()-1);
		// For those multiword anchors which contained the previous word:
		for (String prevAnchor:multiWordAnchors_prevWord) {
			if (prevAnchor.endsWith(lastWord.getToken()))
			{
				addGrammarEntries(inputSentence, fullySpecifiedLexicon, underSpecifiedLexicon, prevAnchor, lastWord, morphs, newLexicalEntryStrings, wordsToLexicalisedTrees);
			}
		}
		// For those words which were not a part of multiword expression:
		if (multiWordAnchors_prevWord.isEmpty()) {
			addGrammarEntries(inputSentence, fullySpecifiedLexicon, underSpecifiedLexicon, lastWord.getToken().toLowerCase(), lastWord, morphs, newLexicalEntryStrings, wordsToLexicalisedTrees);
		}
		

		// ToDo : Improvement --> The part until above can be put in a separate method
		
		
		
		// At this point, we need to filter out the lexicalised trees (selected for successive word) whose lexical items were already
		// fully contained in the lexicalised trees (selected for the previous word).
		// This I have to do because of Prep trees that might be available as distinct adjoining tree or as coanchor node of a verb tree.
		// See Sentence.java file.
		filterRedundantTreesInSuccessiveWords(wordsToLexicalisedTrees, inputSentence);
		
		
		
		logAllTreesSelected(wordsToLexicalisedTrees, inputSentence);
		
		

		
		
		
		
		JeniChartItem.resetIdCount();
		JeniChartItems parseChartItems = new JeniChartItems();
		// After having the instantiated trees, need to combine them
		parseChartItems.addAll(doParse(wordsToLexicalisedTrees,useProbability));
		
		
		
		logger.info("\n\nAll results obtained from CKY/Cartesian combinations : \n"+parseChartItems);
		removeAllBadResults(parseChartItems);
		MLGoodExamples.addAll(parseChartItems);
		
		logger.info("\n\n\nTop Level ML Bad Examples = {");
		for (JeniChartItem badItem:MLBadExamples) {
			logger.info(badItem+"\t"+badItem.getDerivation().getDerivedTree());
			//logger.info(badItem+"\t"+badItem.getDerivation().getDerivedTree()+"\n\n\n\n"+badItem.getDerivation());
		}
		logger.info("}\n\n\n");
		logger.info("\n\n\nTop Level ML Good Examples = {");
		for (JeniChartItem goodItem:MLGoodExamples) {
			logger.info(goodItem+"\t"+goodItem.getDerivation().getDerivedTree());
			//logger.info(goodItem+"\t"+goodItem.getDerivation().getDerivedTree()+"\n\n\n\n"+goodItem.getDerivation());
		}
		logger.info("}\n\n\n");
		
		
		
		JeniChartItems recursiveParentDerivationsBadExamples = new JeniChartItems();
		for (JeniChartItem bad:MLBadExamples) {
			getRecursiveParentDerivations(bad.getParentItemSource(), bad.getParentItemTarget(), recursiveParentDerivationsBadExamples);
		}
		MLBadExamples.addAll(recursiveParentDerivationsBadExamples);
		JeniChartItems recursiveParentDerivationsGoodExamples = new JeniChartItems();
		for (JeniChartItem good:MLGoodExamples) {
			getRecursiveParentDerivations(good.getParentItemSource(), good.getParentItemTarget(), recursiveParentDerivationsGoodExamples);
		}
		MLGoodExamples.addAll(recursiveParentDerivationsGoodExamples);

		logger.info("\n\n\nAll (Recursive) ML Bad Examples = {");
		for (JeniChartItem badItem:MLBadExamples) {
			logger.info(badItem+"\t"+badItem.getDerivation().getDerivedTree());
		}
		logger.info("}\n\n\n");
		logger.info("\n\n\nAll (Recursive) ML Good Examples = {");
		for (JeniChartItem goodItem:MLGoodExamples) {
			logger.info(goodItem+"\t"+goodItem.getDerivation().getDerivedTree());
		}
		logger.info("}\n\n\n");
		

		
		
		logger.info("\n\n Final set of results (both full and/or partial) after removing bad derivations are :\n"+parseChartItems+"\n\n\n");
		
		
		// Compute ParseResults out of parse Trees.
		ArrayList<ParseResult> allParseResults = new ArrayList<ParseResult>();
		for (JeniChartItem item:parseChartItems) {
			Semantics treeSemantics = item.getSemantics();
			InstantiationContext variablesInstance = item.getContext();
			DerivationTree derivation = item.getDerivation();
			JeniRealization real = item.getRealization();
			ParseResult parseResult = new ParseResult(derivation, real, treeSemantics, variablesInstance, inputSentence.getAllWords(), inputSentence.getIndexOfRequiredInParseTreeWords());
			allParseResults.add(parseResult);
		}
		
		// First, we target results with complete semantic parse (i.e. check that they don't any words from the input sentence missing)
		ArrayList<ParseResult> selectedParseResults = new ArrayList<ParseResult>();
		for (ParseResult parseResult:allParseResults) {
			parseResult.computeResultTypeComplete(inputSentence);
			if (parseResult.isResultTypeComplete()) {
				selectedParseResults.add(parseResult);
			}
		}
		// However, if the complete parse results couldn't be obtained, we would like to resort to the 1-best partial parse results.
		if (selectedParseResults.isEmpty()&&(!allParseResults.isEmpty())) { // meaning that there are top level results but they were not complete results
			int highestLength = allParseResults.get(0).getSentenceofParseOutput().length();
			ParseResult selectedIncompleteParseResult = allParseResults.get(0);
			for (int i=1; i<allParseResults.size(); i++) {
				int length=allParseResults.get(i).getSentenceofParseOutput().length();
				if (length>highestLength) {
					highestLength = length;
					selectedIncompleteParseResult = allParseResults.get(i); 
				}
			}
			selectedParseResults.add(selectedIncompleteParseResult);
		}

		logger.info("\n\n\n\nFinal Results to Display for the current Input ("+inputSentence+") = \n" + "\n");
		for (int i=0; i<selectedParseResults.size(); i++){
			ParseResult currentParseResult = selectedParseResults.get(i); 
			String info = (i+1)+"--"+(currentParseResult.isResultTypeComplete()==true?"CompleteParse":"PartialParse")+"--"+selectedParseResults.get(i).getSentenceofParseOutput()+"\n";
			info = info + "\n" + info + currentParseResult.getparseString() + "\n";
			info = info + "\n\tDerivationTree = "+currentParseResult.getDerivationTree()+"\n";
			info = info + "\n\tDerivedTree = "+currentParseResult.getDerivationTree().getDerivedTree()+"\n";
			info = info + "\n\tVariables Instance = "+currentParseResult.getVariablesInstance()+"\n";
			info = info + "\n\n\nSemantics before top-bottom unification = "+currentParseResult.getSemanticsBeforeTopBottomUnification()+"\n";
			info = info + "\nSemantics after top-bottom unification = "+currentParseResult.getparseString()+"\n";
			logger.info(info);
		}
		logger.info("\n\n\n" + "\n");

		
	
		Set<String> newLexEntryStringsProposed = new HashSet<String>();
		// If the parse was successful, track those LexicalEntryStrings, use them to build new SyntacticLexiconEntries
		if (selectedParseResults.size()>0) { // Meaning that the parse was successful for this test suite. But it could have been the case that different lex entries were selected for the same word and only one of the lex entries selected lead to a successful parse.
			for (Map.Entry<String, Set<GrammarEntry>> newLexicalEntry: newLexicalEntryStrings.entrySet()) {
				String newLexEntryString = newLexicalEntry.getKey();
				Set<GrammarEntry> grammarEntriesSelected = newLexicalEntry.getValue();
				if (usedInDerivation(grammarEntriesSelected, selectedParseResults)) { 
					newLexEntryStringsProposed.add(newLexEntryString);
				}
			}
		}
		newLexicalItemsProposed.addAll(newLexEntryStringsProposed);
		
		this.parseResults = selectedParseResults;
	}
 	private boolean containedinPreviousMultiWordAnchor(Set<String> previousAnchors, String currentAnchor) {
		for (String previousAnchor:previousAnchors) {
			if (previousAnchor.toLowerCase().contains(currentAnchor)==true) {
				return true;
			}
		}
		return false;
	}
 	private boolean isExactMatch(String[]inputArray, List<String>inputList) {
		if (inputArray.length!=inputList.size()) {
			return false;
		}
		for (int i=0; i<inputArray.length; i++) {
			if (!inputArray[i].equalsIgnoreCase(inputList.get(i))) {
				return false;
			}
		}
		return true;
	}
	
 	
 	
 	private void addGrammarEntries(Sentence inputSentence, SyntacticLexicon fullySpecifiedLexicon, SyntacticLexicon underSpecifiedLexicon, String anchorString, Word lexicalItemSelectingWord, Map<String,Set<String>> morphs, Map<String,Set<GrammarEntry>> newLexicalEntryStrings, Map<Word,GrammarEntries> wordsToLexicalisedTrees) throws Exception {
		// Important for words such as "not" for which the underSpecifiedLexicon doesn't contain any entry.
		addGrammarEntriesFromFullySpecifiedLexicon(inputSentence, fullySpecifiedLexicon, anchorString, lexicalItemSelectingWord, morphs, newLexicalEntryStrings, wordsToLexicalisedTrees);
		// Important for creating adjoining trees (e.g. X or) using underSpecifiedLexicon out of NP words (e.g. "X") present in the fullySpecifiedLexicon.
		// Also, sometimes a wrong entry might be present in the fully specified lexicon due to error or elsewise. Eg: identify as a NP entry or eating as a NP entry
		addGrammarEntriesFromUnderSpecifiedLexicon(inputSentence, underSpecifiedLexicon, anchorString, lexicalItemSelectingWord, morphs, newLexicalEntryStrings, wordsToLexicalisedTrees);
	}
	private void addGrammarEntriesFromFullySpecifiedLexicon(Sentence inputSentence, SyntacticLexicon fullySpecifiedLexicon, String anchorString, Word lexicalItemSelectingWord, Map<String,Set<String>> morphs, Map<String,Set<GrammarEntry>> newLexicalEntryStrings, Map<Word,GrammarEntries> wordsToLexicalisedTrees) throws Exception {
		
		Set<GrammarEntry> selectedEntries = new HashSet<GrammarEntry>();
		
		
		for (SyntacticLexiconEntry lexEntry:fullySpecifiedLexicon) {
			String lexemeAnchor = lexEntry.getLemma().getValue();
			if (lexemeAnchor.equalsIgnoreCase(anchorString)) {
				LexFormatEntry originalLexFormat = lexEntry.getLexFormatEntry();
				int countCoAnchorsNeeded = originalLexFormat.getCoAnchorsCount();
				
				
				if (countCoAnchorsNeeded==0)
				{
					List<Word> coAnchorsPossible = new ArrayList<Word>();
					Set<GrammarEntry> grammarSelected = getMatchingGrammarEntries(lexEntry, lexemeAnchor, lexicalItemSelectingWord.getIndex(), coAnchorsPossible, inputSentence); // This returned grammar is a deep copy clone of the original grammar.
					selectedEntries.addAll(grammarSelected);
					// Update the lexicon used. // To keep track of the lexicalEntry String used for parse test.
					if (!newLexicalEntryStrings.containsKey(lexEntry.getLexFormatEntry().toString())) {
						newLexicalEntryStrings.put(lexEntry.getLexFormatEntry().toString(),grammarSelected);	
					}
					else {
						Set<GrammarEntry> existing = newLexicalEntryStrings.get(lexEntry.getLexFormatEntry().toString());
						existing.addAll(grammarSelected);
					}
				}
				else {
					List<String> originalCoAnchors = originalLexFormat.getAllCoAnchorsString();
					for (List<Word> coAnchorsPossible:inputSentence.getContiguousCoAnchors(countCoAnchorsNeeded)) {
						if (hasSameElements(coAnchorsPossible,originalCoAnchors)) {
							Set<GrammarEntry> grammarSelected = getMatchingGrammarEntries(lexEntry, lexemeAnchor, lexicalItemSelectingWord.getIndex(), coAnchorsPossible, inputSentence); // This returned grammar is a deep copy clone of the original grammar.
							selectedEntries.addAll(grammarSelected);
							// Update the lexicon used. // To keep track of the lexicalEntry String used for parse test.
							if (!newLexicalEntryStrings.containsKey(lexEntry.getLexFormatEntry().toString())) {
								newLexicalEntryStrings.put(lexEntry.getLexFormatEntry().toString(),grammarSelected);	
							}
							else {
								Set<GrammarEntry> existing = newLexicalEntryStrings.get(lexEntry.getLexFormatEntry().toString());
								existing.addAll(grammarSelected);
							}
						}
					}
				}
				
				
			}
		}
		
		
		
		// Update the grammarTrees to use
		if (!wordsToLexicalisedTrees.containsKey(lexicalItemSelectingWord)) {
			GrammarEntries prevWordGrammerTrees = new GrammarEntries();
			prevWordGrammerTrees.addAll(selectedEntries);
			wordsToLexicalisedTrees.put(lexicalItemSelectingWord,prevWordGrammerTrees);
		}
		else {
			GrammarEntries existingGrammar = wordsToLexicalisedTrees.get(lexicalItemSelectingWord);
			existingGrammar.addAll(selectedEntries);
		}
	}
	private boolean hasSameElements(List<Word> coAnchorsPossible, List<String> originalCoAnchors) {
		if (coAnchorsPossible.size()!=originalCoAnchors.size()) {
			return false;
		}
		for (int i=0;i<coAnchorsPossible.size();i++){
			Word coAnchor = coAnchorsPossible.get(i);
			if (!coAnchor.getToken().equalsIgnoreCase(originalCoAnchors.get(i))) {
				return false;
			}
		}
		return true;
	}
	private void addGrammarEntriesFromUnderSpecifiedLexicon(Sentence inputSentence, SyntacticLexicon underspecifiedLexicon, String anchorString, Word lexicalItemSelectingWord, Map<String,Set<String>> morphs, Map<String,Set<GrammarEntry>> newLexicalEntryStrings, Map<Word,GrammarEntries> wordsToLexicalisedTrees) throws Exception {
		Set<GrammarEntry> selectedEntries = new HashSet<GrammarEntry>();
					
		int countTotalCoAnchorsPossible = inputSentence.getCountTotalCoAnchorsPossible();
		for (SyntacticLexiconEntry lexEntry:underspecifiedLexicon) {
			LexFormatEntry originalLexFormat = lexEntry.getLexFormatEntry();
			int countCoAnchorsNeeded = originalLexFormat.getCoAnchorsCount();
			logger.info("\n\nTrying to create lexEntry for "+lexicalItemSelectingWord+" (countCoAnchorsPossible = "+countTotalCoAnchorsPossible+") using the unspecified lexEntry : (countCoAnchorsNeeded = "+countCoAnchorsNeeded+")\n"+originalLexFormat+"\n");
			if (countTotalCoAnchorsPossible<countCoAnchorsNeeded) { // the current underspecified lexical entry is not suitable for the current word.
				// can't use != check because countCoAnchorsPossible for "labels" in the sentence
				// "Pipes shall be identified by labels" turns out to be 1 and the NP trees in our lexicon has 0 coanchors.
				// On the other hand, this also leads to selection of NP trees for verbs, such as "identified" which leads to a higher number of combinations.
				logger.info("\nLexEntry \n"+originalLexFormat+"\n can't be used for "+lexicalItemSelectingWord+" because of mismatch; countCoAnchorsPossible = "+countTotalCoAnchorsPossible+", countCoAnchorsNeeded = "+countCoAnchorsNeeded);
				continue;
			}
			else {
							StringBuilder commonLexicalEntryString = new StringBuilder();
				
							// Create the anchor
							commonLexicalEntryString.append("*ENTRY: "+anchorString+"\n");
							// Create the category
							commonLexicalEntryString.append("*CAT: "+originalLexFormat.getCat()+"\n"); 
							// Create the Semantics left to do
							String lemma = getPossibleLemma(anchorString,morphs);
							String semantics = originalLexFormat.getSemText();
							
							commonLexicalEntryString.append("*SEM: "+semantics.replaceAll(originalLexFormat.getName().toLowerCase(), lemma)+"\n");
							
							// Create the Acc
							commonLexicalEntryString.append("*ACC: "+originalLexFormat.getAcc()+"\n");
							// Create the Families
							commonLexicalEntryString.append("*FAM: "+originalLexFormat.getFamily()+"\n");
							// Create the Filters
							commonLexicalEntryString.append("*FILTERS: "+ originalLexFormat.getFilterText()+"\n");
							// Create the Ex
							commonLexicalEntryString.append("*EX: "+ originalLexFormat.getEx()+"\n");
							// Create the EQUATIONS
							commonLexicalEntryString.append("*EQUATIONS:\n"+ originalLexFormat.getAnchorEquationsText()+"\n");
							// Create the coanchors -- once per each possibility
							
							
							if (countCoAnchorsNeeded==0)
							{
								String newLexicalEntryString = commonLexicalEntryString.toString() + "*COANCHORS:\n";
								LexFormatLexicon newLexItem =  LexFormatParser.readLexicon("include macros.mac\n"+newLexicalEntryString, macroFileDirectoryPath);
								SyntacticLexiconEntry copyEntry = newLexItem.convertLexicon().get(0); // We know that there will be only one syntacticlexiconentry for this single lexical item
								
								List<Word> coAnchorsPossible = new ArrayList<Word>();
								Set<GrammarEntry> grammarSelected = getMatchingGrammarEntries(copyEntry, anchorString, lexicalItemSelectingWord.getIndex(), coAnchorsPossible, inputSentence);
								
								for (GrammarEntry entry:grammarSelected) {
									selectedEntries.add(entry);
									// Update the lexicon used. // To keep track of the lexicalEntry String used for parse test.
									// The same lexical entry might have been used for grammar selection for different words; e.g. when the word is repeating in the sentence.
									if (!newLexicalEntryStrings.containsKey(newLexicalEntryString)) {
										Set<GrammarEntry> entrySet = new HashSet<GrammarEntry>();
										entrySet.add(entry);
										newLexicalEntryStrings.put(newLexicalEntryString,entrySet);	
									}
									else {
										Set<GrammarEntry> existing = newLexicalEntryStrings.get(newLexicalEntryString);
										existing.add(entry);
									}
								}
							}
							else {
								for (List<Word> coAnchorsPossible:inputSentence.getContiguousCoAnchors(countCoAnchorsNeeded)) {
									String newLexicalEntryString = commonLexicalEntryString.toString() + "*COANCHORS:\n"+ originalLexFormat.getCoAnchorEquationsTextSubstitute(coAnchorsPossible)+"\n";
									LexFormatLexicon newLexItem =  LexFormatParser.readLexicon("include macros.mac\n"+newLexicalEntryString, macroFileDirectoryPath);
									SyntacticLexiconEntry copyEntry = newLexItem.convertLexicon().get(0); // We know that there will be only one syntacticlexiconentry for this single lexical item
											
									// Select grammar Trees for the current contentWord using this newEntry
									Set<GrammarEntry> grammarSelected = getMatchingGrammarEntries(copyEntry, anchorString, lexicalItemSelectingWord.getIndex(), coAnchorsPossible, inputSentence);
									for (GrammarEntry entry:grammarSelected) {
											selectedEntries.add(entry);
											// Update the lexicon used. // To keep track of the lexicalEntry String used for parse test.
											// The same lexical entry might have been used for grammar selection for different words; e.g. when the word is repeating in the sentence.
											if (!newLexicalEntryStrings.containsKey(newLexicalEntryString)) {
												Set<GrammarEntry> entrySet = new HashSet<GrammarEntry>();
												entrySet.add(entry);
												newLexicalEntryStrings.put(newLexicalEntryString,entrySet);	
											}
											else {
												Set<GrammarEntry> existing = newLexicalEntryStrings.get(newLexicalEntryString);
												existing.add(entry);
											}
									}
								} // End of for -- instantiating with each coanchors assignment possibility for the current lexical entry for the current content word.
							} // End of else -- for trees having coanchors
							
							
			} // End of if -- checking if the current underspecified lexical entry could be used for the current content word.
		} // End of for loop -- instantiating each of the underspecified lexical entries for the current content word
					
		
		// Update the grammarTrees to use
		if (!wordsToLexicalisedTrees.containsKey(lexicalItemSelectingWord)) { // This is because some entries might have been put from the fully specified grammar
			GrammarEntries prevWordGrammerTrees = new GrammarEntries();
			prevWordGrammerTrees.addAll(selectedEntries);
			wordsToLexicalisedTrees.put(lexicalItemSelectingWord,prevWordGrammerTrees);
		}
		else {
			GrammarEntries existingGrammar = wordsToLexicalisedTrees.get(lexicalItemSelectingWord);
			existingGrammar.addAll(selectedEntries);
		}
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
	 * @param context
	 * @return a set of new grammar entries derived from the grammar entries but with proper
	 *         lemmatization, interface and context
	 */
	private Set<GrammarEntry> getMatchingGrammarEntries(SyntacticLexiconEntry lexEntry, String mainAnchorString, int mainAnchorIndex, List<Word> coAnchorWords,  Sentence inputSentence)
	{
		Set<GrammarEntry> ret = new HashSet<GrammarEntry>();
		Set<GrammarEntry> byFamily = grammar.getEntriesByFamilies(lexEntry.getFamilies());

		if (byFamily.isEmpty())
		{
			return ret;
		}
		for(GrammarEntry grammarEntry : byFamily)
		{
			FeatureStructure filter = new FeatureStructure();
			filter.add(new Feature("family", new FeatureConstant(grammarEntry.getTrace())));
			if (!new Subsumer().subsumes(lexEntry.getFilter().getFeatureStructure(), filter))
			{
				continue;
			}

			InstantiationContext newContext = new InstantiationContext(); // For parser, no semantics is required. So, empty instantiation context.


			

			GrammarEntry newEntry = new GrammarEntry(grammarEntry); // deep copy
			String newName = newEntry.getTree().getId();
			newEntry.getTree().setId(newName);
			newEntry.setName(newName);
			newEntry.setProbability(lexEntry.getProbability());
			FeatureStructure fs = Unifier.unify(lexEntry.getInterface(), grammarEntry.getInterface(), newContext);
			newEntry.setInterface(fs);
			
			

			/* 
			 * If equations apply, we need to make sure that the semantics of the grammar entry is well instantiated with
			 * regards of the input semantics, the subsumption handles that. In parser, subsumption is not needed, thus the
			 * instantiation context assigned to the resulting tree will be the context obtained after the unification code
			 * above (i.e. the value of the newContext variable).
			 */
			if (applyEquations(newEntry, lexEntry.getEquations(), newContext))
			{
				// we enabled family anchoring, hence the lemma should have been specified by equations
				Node mainAnchor = newEntry.getTree().getMainAnchor();
				if (mainAnchor == null)
					System.out.println("Error: tree of " + newEntry + " has no main anchor!");
				if (lexEntry.getLemma() != null)
					mainAnchor.setAnchorLemma(lexEntry.getLemma());

				// Cases to exclude a matching tree from selection
				if (mainAnchor.getAnchorLemma() == null) // still gets selected, anyway.
				{
					logger.warn("\nError: The grammar entry " + newEntry +" selected for the input word \""+mainAnchorString+"("+mainAnchorIndex+")\""+
							" is missing a lemma, either specify it in the lexicon, in the equations or co-anchor equations. Skipped" + "\n");
					continue;
				}
				// Some trees in my grammar bear hard coded lemma nodes such as 'all', 'only' , "or" etc. (that are not defined as co-anchors). So, if such trees get selected 
				// and the current input doesn't have such words, discard such trees
				if (bearsUnWantedNodes(newEntry.getTree(),inputSentence.getAllWords())) {
					logger.info("\nError: The grammar entry " + newEntry +" selected for the input word \""+mainAnchorString+"("+mainAnchorIndex+")\""+
							" bears extra words than those present in the selecting anchor. Skipped" + "\n");
					continue;
				}
				
				newEntry.setContext(newContext);

				
				// Assign word position from the input sentence to the anchor node. 
				mainAnchor.setNodeIndex(mainAnchorIndex);
				
				
				List<Node> coAnchors = new ArrayList<Node>();
				//Some trees in the grammar themselves bear hard coded lemma nodes such as 'all', 'only', "or", ",", "and" etc. (i.e. are are not defined as co-anchors in the lexical items).
				List<Node> hardCodedLemmas = new ArrayList<Node>();
				for (Node n:newEntry.getTree().getLemmasNodes()) {
					if (n!=mainAnchor) {
						if ((Sentence.skipWords.contains(n.getAnchorLemma().toString().toLowerCase())||PoS.prepositions.contains(n.getAnchorLemma().toString().toLowerCase()))) { 
							coAnchors.add(n);							
						}
						else { 
							hardCodedLemmas.add(n);
						}
					}
				}
				// Assign word position from the input sentence to the coanchor node.
				for (int i=0; i<coAnchors.size(); i++) {
					Node coAnchor = coAnchors.get(i);
					coAnchor.setNodeIndex(coAnchorWords.get(i).getIndex());
				}
				

				
				
				// Since the indices for hardCodedLemmas couldn't come from the lexiconSelection phase; we have to try alll possible assignments.
				List<List<Object>> hardCodedLemmasPossibleIndices = new ArrayList<List<Object>>();
				for (Node x:hardCodedLemmas) {
					hardCodedLemmasPossibleIndices.add(inputSentence.getAllPossibleIndicesforStringInSentence(x.getAnchorLemma().toString()));
				}
				List<List<Object>> possibilities = OrderedCombinations.getCombinations(hardCodedLemmasPossibleIndices);
				for (List<Object> possibility:possibilities){
					 for (int i=0;i<possibility.size();i++) {
						 hardCodedLemmas.get(i).setNodeIndex((int) possibility.get(i));
					 }
					if (WordOrder.isAscendingOrder(WordOrder.getLemmaNodesIndex(newEntry.getTree()))){
						GrammarEntry newEntry2 = new GrammarEntry(newEntry); // deep copy
						ret.add(newEntry2);
					}
				}
			}
			else {
				logger.info("Tree "+newEntry.getName()+" selected for the input word \""+mainAnchorString+"("+mainAnchorIndex+")\""+" discarded because of equations mismatch with lexical entry : \n"+lexEntry);
				logger.info("Tree Equations = ");
				logger.info(newEntry.getTree().getEquations());
				logger.info("Lexical Entry Equations = ");
				logger.info(lexEntry.getEquations());
				logger.info("\n");
				logger.info("\n");
			}
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
	private boolean applyEquations(GrammarEntry entry, Equations equations, InstantiationContext context)
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
	 * @return wether the equation have been appliegenerated
	 */
	private boolean applyEquation(GrammarEntry entry, Equation eq, InstantiationContext context)
	{
		// find node first
		String nodeId = eq.getNodeId();
		Node node = null;
		if (nodeId.equals("anchor"))
		{
			node = entry.getTree().getMainAnchor();
			if (node == null)
			{
				return false;
			}
		}
		else if (nodeId.equals("foot"))
		{
			node = entry.getTree().getFoot();
			if (node == null)
			{
				return false;
			}
		}
		else if (nodeId.equals("root"))
		{
			node = entry.getTree().getRoot();
			if (node == null)
			{
				// there is then something reeeally wrong
				return false;
			}
		}
		else
		{
			node = entry.getTree().getNodeById(nodeId);
			if (node == null)
			{
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
	private void setLemma(Node node, FeatureStructure fs, InstantiationContext context)
	{
		Feature lemmaFeat = fs.getFeature("lemma");
		if (lemmaFeat != null)
		{
			FeatureValue val = context.getValue(lemmaFeat.getValue());
			node.setAnchorLemma(new Lemma(val.toString()));
		}
	}
	private boolean bearsUnWantedNodes(Tree inputTree, ArrayList<Word> allWords) {
		for (Node node:inputTree.getLemmasNodes()) {
			// For multiwords coming from fully specified lexicon, there won't be a single word in the inputSentence which matches.
			// Skipping check on such words for now
			if (node.getAnchorLemma().getValue().contains("_+_")) {
				continue;
			}
			if (WordOrder.containsUnwantedWord(node.getAnchorLemma().getValue(), allWords)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	

	private void filterRedundantTreesInSuccessiveWords(LinkedHashMap<Word,GrammarEntries> wordsToLexicalisedTrees, Sentence inputSentence) {
		// LinkedHashMap maintains order but still doesn't allow to iterate by index; so need to convert it to ArrayList.
		// We need 2 parallel lists to map on index position of key and value.
		List<Word> words = new ArrayList<Word>(wordsToLexicalisedTrees.keySet());
		List<GrammarEntries> lexicalisedTrees = new ArrayList<GrammarEntries>(wordsToLexicalisedTrees.values());
		for (int index=0;index<lexicalisedTrees.size();index++) { // This index signifies the insertion order in wordsToLexicalisedTrees (content words).
			GrammarEntries currentWordTrees = lexicalisedTrees.get(index); 
			// Looking for possibilities to discard trees selected for successive words.
			// We want to check if the all the anchor+coanchors in the current grammar entry were already present in any of the
			// grammar entries selected for the previous word of the input sentence. This is done to avoid selecting the PP tree
			// for preposition if the VP tree has already used the preposition as its coanchor (for example).
			if (index>0) {
				GrammarEntries replacementTreesForCurrentWord = new GrammarEntries(); // The trees that we allow to keep after the filtering task 
				for (GrammarEntry currentWordTree:currentWordTrees) {
					boolean preserve = true;
					for (int j=index-1;j>=0;j--) { // We have to check in all past trees because this word(prep) might have been a long distance coanchor of the distant past word.
						GrammarEntries previousWordTrees = 	lexicalisedTrees.get(j);
						if (TreesContainAnyWordsInCurrentTree(previousWordTrees,currentWordTree)) {
							preserve = false;
							break;
						}
					}
					if (preserve) {
						replacementTreesForCurrentWord.add(currentWordTree);
					}
				}
				wordsToLexicalisedTrees.put(words.get(index), replacementTreesForCurrentWord); // Update the contents of wordsToLexicalisedTrees with the filtered trees.
			}
		}
		// ToDo : It would be nice to keep track of trees removed by this method and put that info in the log file so as to avoid any confusion
		// to the end user about why the tree was not selected for parsing purpose.
		// Kind of like implementing a hashmap of <tree,boolean> to say whether it is going to be used for building the chart agenda or not.
	}
	/**
	 * Returns true of any of the tree in the @prevs contains any word (having the same index) present in the @current tree.
	 * @param prevs
	 * @param current
	 * @return
	 */
	private boolean TreesContainAnyWordsInCurrentTree(GrammarEntries prevs, GrammarEntry current) {
		//System.out.print("\nTesting if prevs = "+prevs+" contain current = "+current);
		ArrayList<Integer> currentIndices = WordOrder.getLemmaNodesIndex(current.getTree());
		for (GrammarEntry prev:prevs) {
			ArrayList<Integer> prevIndices = WordOrder.getLemmaNodesIndex(prev.getTree());
			if (WordOrder.containsAny(prevIndices, currentIndices)) {
				//System.out.print(" and the result is true\n");
				return true;
			}
		}
		//System.out.print(" and the result is false\n");
		return false;
	}
	
	
	
	
	private void logAllTreesSelected(Map<Word,GrammarEntries> wordsToLexicalisedTrees, Sentence inputSentence) {
		StringBuilder text = new StringBuilder();
		text.append("\n\n\nDisplaying results of Lexical Selection Phase [Input Sentence = "+inputSentence+" \n\n");	
		for (Map.Entry<Word, GrammarEntries> x: wordsToLexicalisedTrees.entrySet()) {
			Word word = x.getKey();
			GrammarEntries grammarSelected = x.getValue();
			if (grammarSelected.isEmpty()) {
				statusReport = "No lexical entry for the word "+CustomTextReplacement.getDefaultReplacementString(word.getToken()) ;
			}
			text.append( "Trees Selected for " +word+ " [Count of Trees = "+grammarSelected.size()+"] : " + "\n");
			for (GrammarEntry y:grammarSelected) {
				text.append(y + "\n");
			}
			text.append("\n\n");
		}
		if (wordsToLexicalisedTrees.isEmpty()) {
			statusReport = "Supplied lexicon doesn't contain entry for any words of the input";
			text.append("No trees selected for any words in the input. The supplied lexicon doesn't contain any words in the sentences (as an anchor word). \n\n");
		}
		text.append("\n\n\n\n");
		logger.info(text.toString());
	}
	
	
	
	
	
	// ToDo :: 1. Need to implement ML filtering in CKY approach
	// 2. How to implement ML filtering for the cartesian combinations approach ???
	private ArrayList<JeniChartItem> doParse(Map<Word, GrammarEntries> wordsToLexicalisedTrees, boolean useProbability) {
		ArrayList<JeniChartItem> result;
		// 1. The CKY way -- dynamic programming approach
			logger.info("\n\n\n************************* Using the CKY Chart Parsing Combinations Approach *************************\n\n");
			ArrayList<ArrayList<JeniChartItem>> CKY_InitialAgenda = new ArrayList<ArrayList<JeniChartItem>>();
			for (Map.Entry<Word,GrammarEntries> entry:wordsToLexicalisedTrees.entrySet()) {
				GrammarEntries x = entry.getValue();
				ArrayList<JeniChartItem> aList = new ArrayList<JeniChartItem>();
				for (GrammarEntry y:x) {
					JeniChartItem z = new JeniChartItem(y.getTree(), y.getSemantics(), y.getContext(), y.getProbability());
					aList.add(z);
				}
				if (!aList.isEmpty()) { // if the input word didn't select any entries, then there will just be empty thing in the ajenda and that will hamper the production of partial parse productions.
					CKY_InitialAgenda.add(aList);
				}
			}
			CKYChartCombinations<JeniChartItem> cKYChartBuilder = new CKYChartCombinations<JeniChartItem>(CKY_InitialAgenda, logger);
			logger.info("\nChart Before Building = \n"+cKYChartBuilder);
			logger.info("\n\nTop Item ="+cKYChartBuilder.getTopItem()+"\n\n");
			logger.info("Displaying the results of Chart at each Row ...........\n");
			cKYChartBuilder.buildChart(new TreeCombiner(), null, useProbability); 
			logger.info("\n\n\n\nChart After Building = \n"+cKYChartBuilder);
			logger.info("\n\n\nTop Row Items ="+cKYChartBuilder.getTopItem());
			logger.info("\n\n\nResults Row Items (Flattened) ="+cKYChartBuilder.getNonEmptyRowResultsAsFlatList());
			logger.info("\n\n\n");
			result = cKYChartBuilder.getNonEmptyRowResultsAsFlatList();
		if (result.isEmpty()) {
			if (statusReport.isEmpty()) // if not already set (E.g. sometimes the parse proceeds even if the trees for not all of the words have been selected (to account for partial parse))
				statusReport = "Selected Trees couldn't combine (or the results they produced had bad/null word index order or incomplete derivations)";
		}
		return result;
	}
	
	
	
	
	
	
	/**
	 * Removes all the bad results (at the final stage of computation) that couldn't have been removed at intermediate levels of trees combinations 
	 * (such as in intermediate rows of CKY parse) otherwise. For the Cartesian combinations approach
	 * Since this action can only happen at the final phase of computation, this action will not, by itself, help in minimizing the 
	 * "intermediate trees combination explosion" problem but there are two main benefits. First, we filter out the unwanted results
	 * even if at the final phase. The second and most important is that we will use this information of Bad final results to "learn" about the 
	 * bad "intermediate derivations" and throw them (probably in some sort of "online learning" approach)  
	 * 
	 */
	private void removeAllBadResults(JeniChartItems results) {
		// Of all the parse trees derived, for them to be final results; we must rule out :
		
		// For now, not removing the nonContiguousWordOrderTrees becuase we want the parse of 'John sleeps' even if the sentence is "John always sleeps".
		// This will introduce the side effects as discussed on the class definition above.
		//ruleOutNonContiguousWordOrderTrees(results);
		
		ruleOutIncompleteSubstitutions(results);
		ruleOutNonUnifyingTopBotTrees(results);
		
		
		if (results.isEmpty()) { // i.e. if the results left after removing all the bad examples is empty
			if (statusReport.isEmpty()) // if not already set (E.g. sometimes the parse proceeds even if the trees for not all of the words have been selected (to account for partial parse)) 
				statusReport = "All results obtained were bad (Incomplete Substitutions and/or non unifying top bottom trees)";
		}
	}
	/**
	 * Removes results whose word order is non-contiguous. This will eliminate results such as "pressurization variation taken account"
	 * resulting from undesired combinations of trees selected (probably by combination of NXN adjoining trees selected for each word).
	 * Such results were preserved until now because they maintain the ascending word order but now, we are in the final phase of 
	 * computation and can therefore check for contiguous word order.
	 * Note that we still can't check for the exact word order because we would also like to keep partial results such as "Pipe shall be identified"
	 * when the input sentence was "Pipe shall be identified using labels"
	 */
	@SuppressWarnings("unused")
	private void ruleOutNonContiguousWordOrderTrees(JeniChartItems chart) {
		for(Iterator<JeniChartItem> it = chart.iterator(); it.hasNext();)
		{
			JeniChartItem item = it.next();
			if (!WordOrder.isContiguous(WordOrder.getLemmaNodesIndex(item.getTree()))) {
				MLBadExamples.add(item);
				it.remove();
			}
		}
	}
	/**
	 * Rules out items with non substituted nodes.
	 * @param chart
	 */
 	private void ruleOutIncompleteSubstitutions(JeniChartItems chart)
	{
		for(Iterator<JeniChartItem> it = chart.iterator(); it.hasNext();)
		{
			JeniChartItem item = it.next();
			Tree tree = item.getTree();
			if (!tree.getSubstitutions().isEmpty())
			{
				MLBadExamples.add(item);
				it.remove();
			}
		}
	}
	/**
	 * Remove items whose tree contain nodes that do not unify top and bot fs.
	 * @param chart
	 */
	private void ruleOutNonUnifyingTopBotTrees(JeniChartItems chart)
	{
		for(Iterator<JeniChartItem> it = chart.iterator(); it.hasNext();)
		{
			JeniChartItem item = it.next();
			Tree tree = item.getTree();
			Node failedNode = tree.getFailedTopBotNode(item.getContext());
			if (failedNode != null)
			{
				MLBadExamples.add(item);
				it.remove();
			}
		}
	}
	
	
	
	
	private void getRecursiveParentDerivations(JeniChartItem a, JeniChartItem b, JeniChartItems all) {
		if (a==null) {
			return;
		}
		else {
			all.add(a);
			getRecursiveParentDerivations(a.getParentItemSource(), a.getParentItemTarget(), all);
		}
		if (b==null) {
			return;
		}
		else {
			all.add(b);
			getRecursiveParentDerivations(b.getParentItemSource(), b.getParentItemTarget(), all);
		}
	}
	
	
	

	// ToDo : remove this method and use the one below. It is not necessary to form ResultSemantics object to check usedInDerivation
	/*
	 * Checks if any of the grammar entries was used in the derivation of any of the parse results 
	 */
	private boolean usedInDerivation(Set<GrammarEntry> grammarEntries, ArrayList<ParseResult> results) {
		for (ParseResult result:results) {
			DerivationTree derivationTree = result.getDerivationTree();
			for (GrammarEntry grammarEntry:grammarEntries) {
				if (derivationTree.getDerivationNodeByTree(derivationTree.getRoot(),grammarEntry.getTree())!=null) { // Meaning that this tree was present in the derivation tree.
					return true;
				}
			}
		}
		return false; // returns false when none of the trees were used in none of the results.
	}
	/**
	 * Checks if any of the grammar entries was used in the derivation of any of the parse results
	 * @param grammarEntries
	 * @param items
	 * @return
	 */
	private boolean usedInDerivation(Set<GrammarEntry> grammarEntries, JeniChartItems items) {
		for (JeniChartItem item:items) {
			for (GrammarEntry grammarEntry:grammarEntries) {
				if (usedInDerivation(grammarEntry, item)) {
					return true;
				}
			}
		}
		return false; // returns false when none of the trees were used in none of the results.
	}
	/**
	 * Checks if the given grammar entry was used in the derivation of the given parse result
	 * @param grammarEntries
	 * @param item
	 * @return
	 */
	private boolean usedInDerivation(GrammarEntry grammarEntry, JeniChartItem item) {
		DerivationTree derivationTree = item.getDerivation();
		if (derivationTree.getDerivationNodeByTree(derivationTree.getRoot(),grammarEntry.getTree())!=null) { // Meaning that this tree was present in the derivation tree.
			return true;
		}
		else 
			return false; // returns false when none of the trees were used in none of the results.
	}
	
	
	
	
	
	
	
 	private String getPossibleLemma(String variation,Map<String,Set<String>> morphs) {
		String ret = variation;
		for (Map.Entry<String, Set<String>>entry:morphs.entrySet()) {
			if (entry.getValue().contains(variation)) {
				return entry.getKey();
			}
		}
		return ret; // If not found a morph entry, return the original string
	}
	
	
	
 	public Set<String> getNewLexicalItemsProposed(){
		return newLexicalItemsProposed;
	}

	public ArrayList<ParseResult> getParseResults() {
		return parseResults;
	}
	

	
	public String getStatusReport() {
		return statusReport==""?"Unkown; not logged":statusReport;
	}
	
}
