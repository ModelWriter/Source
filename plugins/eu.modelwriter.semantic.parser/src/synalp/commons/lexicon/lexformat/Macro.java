package synalp.commons.lexicon.lexformat;

import synalp.commons.semantics.Semantics;
import synalp.commons.unification.FeatureStructure;

/**
 * A Macro is a pattern-based structure intended to convert a lexical entry in .lex format into a
 * proper SyntacticLexiconEntry.
 * @author adenis
 */
public class Macro
{
	private String name;
	private Semantics semantics;
	private FeatureStructure header;
	private FeatureStructure macroInterface;


	/**
	 * @param name
	 * @param header
	 * @param semantics
	 * @param macroInterface
	 */
	public Macro(String name, FeatureStructure header, Semantics semantics, FeatureStructure macroInterface)
	{
		this.name = name;
		this.header = header;
		this.semantics = semantics;
		this.macroInterface = macroInterface;
	}


	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * @return the semantics
	 */
	public Semantics getSemantics()
	{
		return semantics;
	}


	/**
	 * @return the header
	 */
	public FeatureStructure getHeader()
	{
		return header;
	}


	/**
	 * @return the macroInterface
	 */
	public FeatureStructure getMacroInterface()
	{
		return macroInterface;
	}


	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		ret.append(name).append(header).append("\n");
		ret.append("\t").append("semantics:[").append(semantics).append("]\n");
		ret.append("\t").append("interface:").append(macroInterface);
		return ret.toString();
	}
}
