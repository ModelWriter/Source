package synalp.generation.jeni;

/**
 * The type of lexical selection to use.
 * @author Alexandre Denis
 *
 */
public enum LexicalSelectionType
{
	/**
	 * The classical selection: grammar with semantics + lexicon.
	 */
	CLASSICAL_SELECTION,
	
	/**
	 * The pattern selection: grammar without semantics + semantics patterns
	 */
	PATTERN_SELECTION;
}
