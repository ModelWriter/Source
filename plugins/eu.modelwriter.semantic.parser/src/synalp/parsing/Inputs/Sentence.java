package synalp.parsing.Inputs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import synalp.parsing.utils.CustomTextReplacement;
import synalp.parsing.utils.nlptools.PoS;


public class Sentence {
	
	private static final String splitMarker = " ";
	
	// The non-content words -- SkipWords, Quantifiers, Conjunctions and Prepositions.
	// Prepositions are ambiguous -- We let them be both content words as well as non-content words. This is to take advantage of the fact that the
	// grammar may contain VP trees with specifications of prep node as coanchor or the PP tree could be separately present. If both for a given
	// sentence such as (Pipe shall be identified by labels), we have both -- i) VP tree for "identified" with prep coanchor and ii) PP tree for 
	// "by" with adjunction node, both trees get selected and will lead to problem. This shall taken care at CKY chart initialisation level with
	// subsumption of word order. See doParse() in Parser.java
	// Everything else is a content word.
	// For each of the non-content words, we don't select a grammar tree.
	// In parse results, we want to check for the exact match of content words as well as skip words.
	public static final Set<String> skipWords = new HashSet<String>();
	static {
		/*
		skipWords.add("a");
		skipWords.add("the");
		*/
		skipWords.add("shall");
		skipWords.add("be");
		skipWords.add("should");
		skipWords.add("was");
		skipWords.add("were");
		skipWords.add("will");
		skipWords.add("may");
		skipWords.add("when");
	}
	
	
	public static final Set<String> quantifiers = new HashSet<String>();
	static {
		quantifiers.add("all");
		quantifiers.add("every");
		quantifiers.add("each");
		quantifiers.add("only");
	}
	
	
	
	// Need to replace these words because these words can't be parsed by Jeni.
	public static final Map<String,String> replaceWords = new HashMap<String,String>();
	// In lexicon file replace each key by _-_value_-_
	// Note : Put the value is all lowercase letters
	static {
		replaceWords.put("\"","_-_invcomma_-_");
		replaceWords.put("(", "_-_opensb_-_");
		replaceWords.put(")", "_-_closesb_-_");
		replaceWords.put("%", "_-_percent_-_");
		replaceWords.put("&", "_-_ampersand_-_");
		replaceWords.put("Â°","_-_degrees_-_");
		replaceWords.put("°", "_-_degrees_-_");
		replaceWords.put(";", "_-_semicolon_-_");
		replaceWords.put("±", "_-_plusminus_-_");
		replaceWords.put("–", "_-_dash_-_");
		replaceWords.put("…", "_-_ldots_-_");
		
		// Below this, the keys are also the reserved characters used in .lex format. Be careful before replacing them by their _+_value_+_
		replaceWords.put("/", "_-_backslash_-_"); 
		replaceWords.put(",", "_-_comma_-_");
		replaceWords.put("=", "_-_equalto_-_");
		replaceWords.put(">", "_-_greaterthan_-_");
		replaceWords.put("≥", "_-_morethan_-_");
		replaceWords.put("<", "_-_smallerthan_-_");
		replaceWords.put("≤", "_-_lessthan_-_");
		replaceWords.put(":", "_-_colon_-_");
		replaceWords.put("]", "_-_closebb_-_");
		
		
		
		
		/*
		Problem with regex \\.
		replaceWords.put("\", "_+_forwardSlash_+_");
		replaceWords.put("{", "_+_beginCurly_+_");
		replaceWords.put("}", "_+_endCurly_+_");
		*/
	}
	
	
	
	
	
	
	
	private ArrayList<Word> words = new ArrayList<>(); // All words in the given sentence
	private ArrayList<Word> lexicalItemsSelectingWords = new ArrayList<Word>(); // Those words of the given sentence for which a lexical entry
	// should be picked.
	private ArrayList<Word> requiredInParseTreeWords = new ArrayList<Word>(); // Those words of the given sentence whose presence must be 
	// checked in their parse trees.
	private ArrayList<Word> possibleCoAnchors = new ArrayList<Word>();

	
	
	/* Form Sentence Object from contiguous text */
	public Sentence (String text) {
		for (Map.Entry<String,String>replaceWord:replaceWords.entrySet()) {
			text = text.replace(replaceWord.getKey(), replaceWord.getValue());
		}
		text = text.replaceAll("\\s+", " "); // Replace multiple spaces by one.
		text = text.trim();
		
		
		// We will replace all multiword prepositions by "_+_" directly in the input itself.
		Matcher m = PoS.prepMatchingPattern.matcher(text);
		StringBuffer s = new StringBuffer();
	    while (m.find()) {
	        m.appendReplacement(s, m.group(1).replaceAll(" ", "_+_"));
	    }
	    m.appendTail(s);
	    text = s.toString();

	    
	    
		int counter=0;
		String [] divs = text.split(splitMarker); 
		for (int i=0;i<divs.length; i++) {
			String token = divs[i];
			token = token.trim().toLowerCase();
			if (token.equalsIgnoreCase(".")) { // the full stop at the end. Eg: He saw a cat .
				continue;
			}
			if (i==divs.length-1) {
				if (token.endsWith(".")) { // sometimes the full stop is not spearated by a space but is glued to the last word itself. Eg: He saw a cat.
					token = token.substring(0, token.length()-1); // onmit the fullstop character
				}
			}
			Word word= new Word(counter, token);
			requiredInParseTreeWords.add(word);
			words.add(word);
			if (!quantifiers.contains(token)&&!PoS.conjunctions.contains(token)&&!skipWords.contains(token)) {
				lexicalItemsSelectingWords.add(word);
			}
			counter ++;
		}
		computePossibleCoAnchors();
	}
	


	private void computePossibleCoAnchors() {
		// Note that in the Lexicon(i.e. the .lex file), the coanchors associated to each entry are only the skipwords and prepositions.
		// The quantifiers and conjunctions (if any) of the sentence are present in the trees selected by the lexical entry rather than 
		// associated as coanchors of the lex entry.
		for (Word w:words) {
			if (skipWords.contains(w.getToken().toLowerCase())||PoS.prepositions.contains(w.getToken().toLowerCase()))
				possibleCoAnchors.add(w);
		}
	}
	
	public int getCountTotalCoAnchorsPossible() {
		return possibleCoAnchors.size();
	}
	
	public List<List<Word>> getContiguousCoAnchors(int length) {
		List<List<Word>> ret = new ArrayList<List<Word>>();
		if (length==0) {
			ret.add(new ArrayList<Word>());
			return ret;
		}
		if (length>possibleCoAnchors.size()) {
			ret.add(new ArrayList<Word>());
			return ret; 
		}
		for (int i=0;(i+length)<=possibleCoAnchors.size();i++){
			List<Word> subList = possibleCoAnchors.subList(i, i+length);
			ret.add(subList);
		}
		return ret;
	}


	public ArrayList<Word> getLexicalItemsSelectingWords() {
		return lexicalItemsSelectingWords;
	}


	public ArrayList<Word> getRequiredInParseTreeWords() {
		return requiredInParseTreeWords;
	}

	public ArrayList<Integer> getIndexOfRequiredInParseTreeWords(){
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for (Word w:requiredInParseTreeWords) {
			ret.add(w.getIndex());
		}
		return ret;
	}
	
	public boolean isRequiredInParseTree(String s) {
		for (Word required : requiredInParseTreeWords) {
			if (required.getToken().equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}
	
	
	public ArrayList<Word> getAllWords() {
		return words;
	}
	
	public Word getWordAtIndex(int i) {
		return words.get(i);
	}
	
	public String toString() {
		return words.toString();
	}
	
	public String getPlainSentence_AllWords() {
		StringBuilder text = new StringBuilder("");
		for (Word word:getAllWords()) {
			text.append(word.getToken()+splitMarker);
		}
		String return_text= text.toString();
		return_text = return_text.substring(0,text.length()-1);
		return CustomTextReplacement.getDefaultReplacementString(return_text);
	}
	
	
	// Specific to the need of "or", "all", "only" etc. words that are hard coded into the grammar trees rather than as coanchors of lexical items.
	public ArrayList<Object> getAllPossibleIndicesforStringInSentence(String s) {
		ArrayList<Object> ret = new ArrayList<Object>();
		for (Word w:words) {
			if (w.getToken().equalsIgnoreCase(s)) {
				ret.add(w.getIndex());
			}
		}
		return ret;
	}
	
}
