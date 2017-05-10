package synalp.commons.lexicon.lexformat;

import java.util.ArrayList;

import synalp.commons.lexicon.SyntacticLexicon;


/**
 * A LexFormatLexicon is a list of LexFormatEntries.
 * 
 * @lhp Is macros compulsory? then should force to exist
 * before using methods which rely on it.
 * 
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class LexFormatLexicon extends ArrayList<LexFormatEntry>
{
	private Macros macros;


	/**
	 * @return the macros
	 */
	public Macros getMacros()
	{
		return macros;
	}


	/**
	 * @param macros the macros to set
	 */
	public void setMacros(Macros macros)
	{
		this.macros = macros;
	}
	
	
	/**
	 * Converts this LexFormatLexicon to a proper SyntacticLexicon;
	 * @return a SyntacticLexicon
	 */
	public SyntacticLexicon convertLexicon()
	{
		SyntacticLexicon ret = new SyntacticLexicon();
		for(LexFormatEntry entry : this)
			ret.add(entry.convertEntry(macros));
		return ret;
	}
}
