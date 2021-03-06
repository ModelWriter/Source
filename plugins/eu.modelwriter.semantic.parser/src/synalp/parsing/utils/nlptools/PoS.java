package synalp.parsing.utils.nlptools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class PoS {

	private static final String[] CONJ_VALUES = new String[] {
			"and", "comma", "or"
	};
	public static final Set<String> conjunctions = new HashSet<String>(Arrays.asList(CONJ_VALUES));
	
	
	
	private static final String[] PREP_VALUES = new String[] {
				"by", "with", "from", "above", "on", "in", "inside", "into", "for", "at", "to", "outside", 
				"onto", "below", "between", "before", "as", "without", "near", "per", "aboard", "about",
				"across", "after", "against", "along", "amid", "among", "anti", "around", "behind", 
				"beneath", "beside", "besides", "beyond", "but", "concerning", "considering", "despite",
				"down", "during", "except", "excepting", "excluding", "following", "like", "minus", 
				"off", "opposite", "over", "past", "plus", "regarding", "round", "save", "since", "than",
				"through", "toward", "towards", "underneath", "unlike", "until", "upon", "versus", "within",
				"without", "using", "of",
				// Other complex prepositions are
				"taking in account", "taking into account", "at the level of", "in case of", "in between",
				"directly on", "in the area of", "in front of", "instead of", "passing through", "in front of",
				"directly to", "together with", "at the end of", "directly by", "along with", "on account of",
				"by means of", "according to", "ahead of", "along with", "apart from", "as for", "as well as",
				"aside from", "away from", "because of", "but for", "by means of", "by virtue of", "by way of", 
				"close to", "contrary to", "due to", "except for", "far from", "for lack of", "in accordance with", 
				"in addition to", "in back of", "in between", "in the case of", "in charge of", "in exchange for",
				"in front of", "in light of", "in line with", "in place of", "in the process of", "in process of",
				"in regard to", "inside of", "in spite of", "instead of", "in view of", "near to", "next to",
				"on account of", "on behalf of", "on top of", "out of", "outside of", "owing to", "prior to",
				"subsequent to", "such as", "thanks to", "together with", "up against", "up to", "up until",
				"with respect to", "directly onto",
				"by taking into account", "by taking in account"
			};
	
	// This regex matches the multiword prepositions in the list above with greedy matching format.
	// Should be updated each time some entry is deleted/added to the PREP_VALUES list above
	// The python script at resources/airbus/excel_sheet_lexicon/extract_lexicon.py produces the needed regex
	// Using this site : http://www.regexplanet.com/advanced/java/index.html (after clicking on the test button), 
	// I converted the true regex generated by the python script above to get the Java compiler compatible regex string representation
	// The regex is the same but the java compiler needs to escape \ by \\, [ by \[ and so on.
	private static String prepMatchingRegexString = "\\b(a(bo(ard\\b|ut\\b|ve\\b)|c(cording to\\b|ross\\b)|fter\\b|gainst\\b|head of\\b|long( with\\b|\\b)|m(id\\b|ong\\b)|nti\\b|part from\\b|round\\b|s( (for\\b|well as\\b)|\\b|ide from\\b)|t( the (end of\\b|level of\\b)|\\b)|way from\\b)|b(e(cause of\\b|fore\\b|hind\\b|low\\b|neath\\b|side(\\b|s\\b)|tween\\b|yond\\b)|ut( for\\b|\\b)|y( (means of\\b|taking in( account\\b|to account\\b)|virtue of\\b|way of\\b)|\\b))|c(lose to\\b|on(cerning\\b|sidering\\b|trary to\\b))|d(espite\\b|irectly (by\\b|on(\\b|to\\b)|to\\b)|own\\b|u(e to\\b|ring\\b))|exc(ept( for\\b|\\b|ing\\b)|luding\\b)|f(ar from\\b|o(llowing\\b|r( lack of\\b|\\b))|rom\\b)|in( (a(ccordance with\\b|ddition to\\b)|b(ack of\\b|etween\\b)|c(ase of\\b|harge of\\b)|exchange for\\b|front of\\b|li(ght of\\b|ne with\\b)|p(lace of\\b|rocess of\\b)|regard to\\b|spite of\\b|the (area of\\b|case of\\b|process of\\b)|view of\\b)|\\b|s(ide( of\\b|\\b)|tead of\\b)|to\\b)|like\\b|minus\\b|ne(ar( to\\b|\\b)|xt to\\b)|o(f(\\b|f\\b)|n( (account of\\b|behalf of\\b|top of\\b)|\\b|to\\b)|pposite\\b|ut( of\\b|side( of\\b|\\b))|ver\\b|wing to\\b)|p(as(sing through\\b|t\\b)|er\\b|lus\\b|rior to\\b)|r(egarding\\b|ound\\b)|s(ave\\b|ince\\b|u(bsequent to\\b|ch as\\b))|t(aking in( account\\b|to account\\b)|h(an(\\b|ks to\\b)|rough\\b)|o(\\b|gether with\\b|ward(\\b|s\\b)))|u(n(derneath\\b|like\\b|til\\b)|p( (against\\b|to\\b|until\\b)|on\\b)|sing\\b)|versus\\b|with( respect to\\b|\\b|in\\b|out\\b))";
	public static Pattern prepMatchingPattern = Pattern.compile(prepMatchingRegexString);
	
	
	//public static final Set<String> prepositions = new HashSet<String>(Arrays.asList(PREP_VALUES));
	public static final Preps prepositions = new Preps(Arrays.asList(PREP_VALUES));
	
}

