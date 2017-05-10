package synalp.commons.lexicon.lexformat;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import synalp.commons.input.Lemma;
import synalp.commons.lexicon.*;
import synalp.commons.semantics.Semantics;
import synalp.commons.unification.*;
import synalp.parsing.Inputs.Word;


/**
 * A LexFormatEntry is an entry in a lexicon formatted in the .lex format. It is used as a 
 * data structure to build the SyntacticLexiconEntry.
 * @author adenis
 */
public class LexFormatEntry
{
	private static Logger logger = Logger.getLogger(LexFormatEntry.class);
	
	/**
	 * feature use to represent the lemma part of a coanchor equation anc -> of/p as
	 * anc -> lemma = of 
	 */
	public static String COANCHOR_LEMMA_FEAT = "lemma";
	/**
	 * feature use to represent the cat part of a coanchor equation anc -> of/p as
	 * anc -> cat = p 
	 */
	public static String COANCHOR_CAT_FEAT = "cat";

	private String name;
	private String ex; // not sure of its use
	private float acc; // not sure of its use
	private String cat; // cat does not seem used, is it ?
	private String family;
	private Filter filters;
	private Equations equations;
	private String macroName;
	private FeatureStructure macroHeader;

	/**
	 * @param name
	 * @param cat
	 * @param macroName
	 * @param macroHeader
	 * @param acc
	 * @param family
	 * @param filters
	 * @param ex
	 * @param equations
	 */
	public LexFormatEntry(String name, String cat, String macroName, FeatureStructure macroHeader, float acc, String family, Filter filters, String ex,
			Equations equations)
	{
		this.name = name;
		this.cat = cat;
		this.macroName = macroName;
		this.macroHeader = macroHeader;
		this.acc = acc;
		this.family = family;
		this.filters = filters;
		this.ex = ex;
		this.equations = equations;
	}


	/**
	 * Converts this LexFormatEntry into a SyntacticLexiconEntry by applying the corresponding
	 * Macro.
	 * @param macros all existing macros
	 * @return a SyntacticLexiconEntry
	 */
	public SyntacticLexiconEntry convertEntry(Macros macros)
	{
		SyntacticLexiconEntry ret = new SyntacticLexiconEntry();
		ret.setLemma(new Lemma(name));
		ret.setFamilies(family);
		try
		{
			ret.setEquations(equations.aggregate());
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}

		FeatureStructure newFilters = (filters!= null) ? 
				new FeatureStructure(filters.getFeatureStructure()) :
					new FeatureStructure();
		newFilters.addConstantFeature("family", family);
		ret.setFilter(new Filter(newFilters));

		Macro macro = macros.get(macroName);
		if (macro == null)
		{
			logger.error("Warning: entry '" + name + "' has no related macro, hence an empty semantics");
			ret.setSemantics(new Semantics());
			ret.setInterface(new FeatureStructure());
		}
		else
		{
			InstantiationContext context = new InstantiationContext();
			FeatureStructure newHeader = Unifier.unify(macroHeader, macro.getHeader(), context);
			if (newHeader == null)
				logger.error("Warning: header of '" + name + "' does not unify with macro header '" + macro.getName() + "' : " + macro.getHeader());

			FeatureStructure entryInterface = new FeatureStructure(macro.getMacroInterface());
			entryInterface.instantiate(context);
			ret.setInterface(entryInterface);

			Semantics semantics = new Semantics(macro.getSemantics());
			semantics.instantiate(context);
			ret.setSemantics(semantics);
			
			ret.setProbability(acc);
		}

		// Finally, store the original info about the lexformat input used.
		ret.setLexFormatEntry(this);
		return ret;
	}

	/**
	 * @return the lemma
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the ACC field
	 */
	public float getAcc() {
		return acc;
	}

	/**
	 * @return the syntactic category of the lexical entry
	 */
	public String getCat() {
		return cat;
	}

	/**
	 * @return the family of the lexical entry
	 */
	public String getFamily() {
		return family;
	}

	/**
	 * @return {@link Equations} which is a collection of {@link Equation} objects 
	 */
	public Equations getEquations() {
		return equations;
	}

	/**
	 * @return the name of the semantic macro associated to the lexical entry
	 */
	public String getMacroName() {
		return macroName;
	}

	/**
	 * @return the macro configuration arguments/parameters
	 */
	public FeatureStructure getMacroHeader() {
		return macroHeader;
	}

	public String getSemText() {
		String text = macroHeader.toString();
		text = text.replaceAll(" ", ",");
		text = text.replaceAll(":", "=");
		return macroName+text;
	}
	
	public String getFilterText() {
		String text = filters.toString();
		text = text.replaceAll(" ", ",");
		text = text.replaceAll(":", "=");
		return text;
	}
	
	public String getEx() {
		return "{}";
	}
	
	/*
	 * The text inside the *EQUATIONS: are in lexical entry. However, (as per my
	 * understanding)the Equation dataType here holds the equations for the anchor (in the *EQUATIONS: attribute of the lexicon) 
	 * as well as the equations for the *COANCHORS: field. Therefore, I 
	 * check if the equation is not of type co-anchor and if so, obtain string to represent all (one or more) of them.
	 */
	public String getAnchorEquationsText() {
		StringBuilder text = new StringBuilder("");
		if (equations.size()>0) { // Some lexical entries don't even have the equations for the anchor. Eg: for NP entries.
			for (Equation eq:equations) {
				if (!eq.isCoanchorEquation()) {
					String featureString = eq.getFeatureStructure().toString().replaceAll(":", "=");
					featureString = featureString.replace("[", "");
					featureString = featureString.replace("]", "");
					text.append(eq.getNodeId()+" -> "+ featureString+"\n");
				}
			}
		}
		return text.toString().trim();
	}
	
	// And likewise for the coanchor text except that this time we check that the equation is of type coanchor
	public String getCoAnchorEquationsText() {
		StringBuilder text = new StringBuilder("");
		if (equations.size()>0) { // Some lexical entries don't even have the equations for the anchor. Eg: for NP entries.
			for (Equation eq:equations) {
				if (eq.isCoanchorEquation()) {
					String featureString = eq.getFeatureStructure().toString().replaceAll(":", "=");
					featureString = featureString.replace("[", "");
					featureString = featureString.replace("]", "");
					String[] lemma_cat = featureString.trim().split(" ");
					text.append(eq.getNodeId()+" -> "+ lemma_cat[0].split("=")[1]+"/"+lemma_cat[1].split("=")[1]+"\n");
				}
			}
		}
		return text.toString().trim();
	}
	
	
	public String getCoAnchorEquationsTextSubstitute(List<Word> newCoAnchors) {
		StringBuilder text = new StringBuilder();
		int countCoAnchorsSeen = 0;
		for (int i=0; i<equations.size(); i++) {
			if (equations.get(i).isCoanchorEquation()) {
				String replacer = newCoAnchors.get(countCoAnchorsSeen).getToken().toLowerCase();
	
				Equation currentEq = equations.get(i);
				String featureString = currentEq.getFeatureStructure().toString().replaceAll(":", "=");
				
				//System.out.println("Current Equation = "+currentEq+" and featureString = "+featureString);
				
				featureString = featureString.replace("=", "");
				featureString = featureString.replace("[", "");
				featureString = featureString.replace("]", "");
				featureString = featureString.replaceAll("lemma", "");
				featureString = featureString.replaceAll("cat", "");
				
				String[] lemma_cat = featureString.trim().split(" ");
				text.append(currentEq.getNodeId()+" -> "+replacer+"/"+lemma_cat[1]+"\n");
				countCoAnchorsSeen++;
			}
		}
		return text.toString();
	}
	
	public List<String> getAllCoAnchorsString() {
		List<String> out = new ArrayList<String>();
		for (int i=0; i<equations.size(); i++) {
			Equation currentEquation = equations.get(i); 
			if (currentEquation.isCoanchorEquation()) {
				FeatureStructure fs = currentEquation.getFeatureStructure();
				String featureString = fs.toString(); // this comes out in the form of [lemma:shall cat:aux]
				String lemma = featureString.trim().split(" ")[0].split(":")[1];
				out.add(lemma);
			}
		}
		return out;
	}
	
	
	public int getCoAnchorsCount() {
		int count = 0;
		for (Equation eq:equations) {
			if (eq.isCoanchorEquation())
				count++;
		}
		return count;
	}

	/**
	 * 
	 * @return the string representation of this lexical item in the .lex file format
	 */
	public String getLexFormatString() {
		String ret = "";
		return ret + "*COANCHORS:\n"+ getCoAnchorEquationsText()+"\n";
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		ret.append("*ENTRY: ").append(name).append("\n");
		ret.append("*CAT: ").append(cat).append("\n");
		ret.append("*SEM: ").append(getSemText()).append("\n");
		ret.append("*ACC: ").append(acc).append("\n");
		ret.append("*FAM: ").append(family).append("\n");
		ret.append("*FILTERS: ").append(filters).append("\n");
		ret.append("*EX: ").append(ex).append("\n");
		ret.append("*EQUATIONS: \n").append(getAnchorEquationsText()).append("\n");
		ret.append("*COANCHORS: \n").append(getCoAnchorEquationsText()).append("\n");
		return ret.toString();
	}
}
