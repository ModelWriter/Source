package synalp.generation.morphology;

/**
 * Defines what to do when there is a missing lexem.
 * @author Alexandre Denis
 */
public enum MissingLexemPolicy
{
	/**
	 * Ignore completely the missing lexem. The lemma is not matched, hence the returned morph
	 * realization has not the same length than the input syntactic realization.
	 */
	IGNORE,

	/**
	 * Outputs a fake lexem whose surface form is the lemma.
	 */
	OUTPUT_LEMMA,

	/**
	 * Outputs a fake lexem "<missing:lemma>" that explicitely warns about the missing lemma
	 */
	OUTPUT_MISSING,
	
	/**
	 * Outputs a fake detailed lexem "<missing:lemma+fs>" that explicitely warns about the missing lemma
	 */
	OUTPUT_MISSING_DETAILED;
	
	
	/**
	 * Parses the given policy case independent.
	 * @param str
	 * @return null if not found
	 */
	public static MissingLexemPolicy parse(String str)
	{
		for(MissingLexemPolicy policy : values())
			if (str.equalsIgnoreCase(policy.toString()))
				return policy;
		return null;
	}
}
