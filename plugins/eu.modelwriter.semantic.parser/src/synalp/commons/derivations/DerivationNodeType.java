package synalp.commons.derivations;


/**
 * The type of derivation node.
 * @author Alexandre Denis
 */
public enum DerivationNodeType
{
	/**
	 * The substitution type.
	 */
	SUBSTITUTION,
	/**
	 * The adjunction type.
	 */
	ADJUNCTION,
	/**
	 * The anchoring type.
	 */
	ANCHORING;

	/**
	 * Returns a symbol representing the node type.
	 * @return a string
	 */
	public String getSymbol()
	{
		switch (this)
		{
			case SUBSTITUTION:
				return "\u2193";
			case ADJUNCTION:
				return "\u272D";
			case ANCHORING:
				return "\u2662";
			default:
				return toString();
		}
	}

}
