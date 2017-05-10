package synalp.commons.grammar;

/**
 * The type of a Node in a LTAG grammar.
 * @author Alexandre Denis
 */
public enum NodeType
{
	/**
	 * An anchor node
	 */
	ANCHOR("anchor"),
	/**
	 * A foot node
	 */
	FOOT("foot"),
	/**
	 * A standard node
	 */
	STD("std"),
	/**
	 * A substitution node
	 */
	SUBST("subst"),
	/**
	 * A lexical node, this type is used only when reading the grammar to be backward compatible. It
	 * is rewritten as a COANCHOR node.
	 */
	LEX("lex"),
	/**
	 * A co-anchor node.
	 */
	COANCHOR("coanchor"),
	/**
	 * A non-adjunction node, this type is used only when reading the grammar. It is rewritten as a
	 * STD node with the property of forbidding adjunctions.
	 */
	NADJ("nadj");

	private String value;


	private NodeType(String value)
	{
		this.value = value;
	}

	
	/**
	 * Returns the type String value.
	 * @return a String value of this type
	 */
	public String getValue()
	{
		return value;
	}
	

	/**
	 * @param str
	 * @return a node type
	 */
	public static NodeType parse(String str)
	{
		for(NodeType type : values())
			if (type.getValue().equalsIgnoreCase(str))
				return type;
		return null;
	}
}
