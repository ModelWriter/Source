package synalp.commons.output;

import java.util.*;

import synalp.commons.input.Lexem;
import synalp.commons.unification.InstantiationContext;
import synalp.commons.utils.Utils;


/**
 * A MorphRealization is a morphologically realized syntactic realization, represented as a list of
 * Lexems. It is associated to an instantiation context for convenience.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class MorphRealization extends ArrayList<Lexem>
{
	private InstantiationContext context = new InstantiationContext();


	/**
	 * Creates an empty MorphRealization.
	 */
	public MorphRealization()
	{

	}


	/**
	 * Creates an empty MorphRealization from the given String sentence. The String input is assumed
	 * to contain lexemes separated by the space character. If there is any punctuation, it is
	 * considered as part of a lexeme or as an independent lexeme.
	 * @param sentence
	 */
	public MorphRealization(String sentence)
	{
		for(String lexem : sentence.trim().split(" "))
			add(new Lexem(lexem));
	}


	/**
	 * Creates a new MorphRealization with given Lexems.
	 * @param context
	 * @param lexems
	 */
	public MorphRealization(InstantiationContext context, Lexem... lexems)
	{
		addAll(Arrays.asList(lexems));
		this.context = context;
	}


	/**
	 * Creates a shallow copy of the given MorphRealization with given context and with additional
	 * lexem.
	 * @param real a realization to copy
	 * @param context a context
	 * @param finalLexem a lexem to add at the end
	 */
	public MorphRealization(MorphRealization real, InstantiationContext context, Lexem finalLexem)
	{
		addAll(real);
		add(finalLexem);
		this.context = context;
	}


	/**
	 * Returns the MorphRealization as a String in which lexems are separated by a single space
	 * character.
	 * @return a surface form of this MorphRealization
	 */
	public String asString()
	{
		return Utils.print(asStringList(), " ");
	}


	/**
	 * Returns this MorphRealization as a list of the lexems surface words.
	 * @return a list of string of the same size than this MorphRealization
	 */
	public List<String> asStringList()
	{
		List<String> ret = new ArrayList<String>();
		for(Lexem lexem : this)
			ret.add(lexem.getValue());
		return ret;
	}


	/**
	 * @return the context
	 */
	public InstantiationContext getContext()
	{
		return context;
	}


	/**
	 * @param context the context to set
	 */
	public void setContext(InstantiationContext context)
	{
		this.context = context;
	}
	
	
	/**
	 * Tests if this MorphRealization contains a Lexem that has the same surface form as given lexem.
	 * @param lexem
	 * @return true if such Lexem exists, false otherwise
	 */
	public boolean containsLexem(Lexem lexem)
	{
		for(Lexem thisLexem : this)
			if (thisLexem.getValue().equals(lexem.getValue()))
				return true;
		return false;
	}
}
