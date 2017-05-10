package synalp.commons.lexicon;

import java.util.Arrays;

import synalp.commons.input.*;
import synalp.commons.lexicon.lexformat.LexFormatEntry;
import synalp.commons.semantics.Semantics;
import synalp.commons.unification.FeatureStructure;


/**
 * @author Alexandre Denis
 */
public class SyntacticLexiconEntry
{
	private Lemma lemma;
	private String[] families;
	private Filter filter = new Filter();
	private Semantics semantics = new Semantics();
	private Equations equations = new Equations();
	private FeatureStructure entryInterface = new FeatureStructure();
	
	private float probability;

	private LexFormatEntry originalLexFormatText;
	
	public void setLexFormatEntry(LexFormatEntry entry) {
		this.originalLexFormatText = entry;
	}
	
	public LexFormatEntry getLexFormatEntry() {
		return originalLexFormatText;
	}
	
	public SyntacticLexiconEntry() {
		
	}
	/**
	 * Deep copy
	*/ 
	public SyntacticLexiconEntry(SyntacticLexiconEntry other) {
		this.lemma = new Lemma(other.getLemma());
		this.families = other.getFamilies(); // primitive type
		this.filter = new Filter(other.getFilter());
		this.semantics = new Semantics(other.getSemantics());
		this.equations = new Equations(other.getEquations());
		this.entryInterface = new FeatureStructure(other.entryInterface);
		this.probability = other.probability;
	}
	
	
	
	
	/**
	 * @return the filter
	 */
	public final Filter getFilter()
	{
		return filter;
	}


	/**
	 * @param filter
	 */
	public void setFilter(Filter filter)
	{
		this.filter = filter;
	}


	/**
	 * @return the semantics
	 */
	public final Semantics getSemantics()
	{
		return semantics;
	}


	/**
	 * @param semantics
	 */
	public void setSemantics(Semantics semantics)
	{
		this.semantics = semantics;
	}


	/**
	 * @return the equations
	 */
	public final Equations getEquations()
	{
		return equations;
	}


	/**
	 * @param equations
	 */
	public void setEquations(Equations equations)
	{
		this.equations = equations;
	}


	/**
	 * Returns the first found family if there are several.
	 * @return the families
	 */
	public final String getFirstFamily()
	{
		return families[0];
	}


	/**
	 * Returns all families of this entry.
	 * @return all the families this entry is said to belong
	 */
	public String[] getFamilies()
	{
		return families;
	}


	/**
	 * Sets the families, it should be meant as a disjunction.
	 * @param families
	 */
	public void setFamilies(String... families)
	{
		this.families = families;
	}


	/**
	 * @return the lemma
	 */
	public final Lemma getLemma()
	{
		return lemma;
	}


	/**
	 * @param lemma
	 */
	public void setLemma(Lemma lemma)
	{
		this.lemma = lemma;
	}


	/**
	 * Returns the feature structure for the interface.
	 * @return the interface
	 */
	public FeatureStructure getInterface()
	{
		return entryInterface;
	}


	/**
	 * Sets the feature structure corresponding to the interface.
	 * @param entryInterface
	 */
	public void setInterface(FeatureStructure entryInterface)
	{
		this.entryInterface = entryInterface;
	}


	@Override
	public String toString()
	{
		return lemma + " " + Arrays.asList(families) + "\nequations:" + equations + "\nfilter:" + filter + "\ninterface:" + entryInterface + "\nsemantics:" + semantics;
	}


	/**
	 * Returns a String representation of this entry in one line.
	 * @return a String
	 */
	public String toStringOneLine()
	{
		return lemma + " " + Arrays.asList(families) + " equations:" + equations + " filter:" + filter + " interface:" + entryInterface + " semantics:" + semantics;
	}
	
	
	/**
	 * Returns a short String representation of this entry, limited to lemma, families and semantics.
	 * @return a String
	 */
	public String toShortString()
	{
		return lemma + " " + Arrays.asList(families) + " semantics:" + semantics;
	}


	public float getProbability() {
		return probability;
	}


	public void setProbability(float probability) {
		this.probability = probability;
	}
}
