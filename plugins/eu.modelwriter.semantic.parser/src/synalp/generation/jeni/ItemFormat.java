package synalp.generation.jeni;

import synalp.commons.grammar.NodeFormat;

/**
 * The format of a ChartItem. We need to improve that.
 * @author Alexandre Denis
 */
public class ItemFormat
{
	/**
	 * A MINIMALIST format, one single line, only the id and lemmas.
	 */
	public static ItemFormat MINIMALIST = new ItemFormat(NodeFormat.MINIMALIST, true, true, false, false, false, false, false, false, false);

	/**
	 * A single line including id, lemmas and minimalist tree.
	 */
	public static ItemFormat SINGLE_LINE_TREE = new ItemFormat(NodeFormat.MINIMALIST, true, true, true, false, false, false, false, false, false);
	
	/**
	 * A single line including id, lemmas, minimalist tree, derivation and semantics.
	 */
	public static ItemFormat SINGLE_LINE_FULL = new ItemFormat(NodeFormat.MINIMALIST, true, true, true, true, true, false, false, false, false);
	
	/**
	 * A multi line with all fields but short tree representation.
	 */
	public static ItemFormat MULTI_LINE_SHORT = new ItemFormat(NodeFormat.MINIMALIST, true, true, true, true, true, true, false, false, false);
	
	/**
	 * A multi line with all fields, short tree representation and detailed lemmas.
	 */
	public static ItemFormat MULTI_LINE_FULL_LEMMAS = new ItemFormat(NodeFormat.MINIMALIST, true, true, true, true, true, true, false, true, false);
	
	/**
	 * A multi line with all fields, short tree representation and explicit field names.
	 */
	public static ItemFormat COMPLETE_EXPLICIT = new ItemFormat(NodeFormat.MINIMALIST, true, true, true, true, true, true, true, false, true);
	
	/**
	 * A multi line with all fields, fully indented tree, and explicit field names.
	 */
	public static ItemFormat COMPLETE_FULL_TREE = new ItemFormat(NodeFormat.INDENTED, true, true, true, true, true, true, true, true, true);
	
	
	private NodeFormat nodeFormat;

	private boolean showId;
	private boolean showLemmas;
	private boolean showTree;
	private boolean showDerivation;
	private boolean showSemantics;
	private boolean multiline;
	private boolean explicit;
	private boolean fullLemmas;
	private boolean showContext;


	/**
	 * @param nodeFormat which format of the nodes (only if showTree is true)
	 * @param showId whether to display id
	 * @param showLemmas whether to display lemmas
	 * @param showTree whether to display the derived tree
	 * @param showDerivation whether to display derivation tree
	 * @param showSemantics whether to display the semantics
	 * @param multiline whether to display the item in one line or indented multiline
	 * @param explicit whether to explicit each field
	 * @param fullLemmas whether to display lemmas and fs
	 * @param showContext wether to show the context
	 */
	public ItemFormat(NodeFormat nodeFormat, boolean showId, boolean showLemmas, boolean showTree, boolean showDerivation,
			boolean showSemantics, boolean multiline, boolean explicit, boolean fullLemmas, boolean showContext)
	{
		this.nodeFormat = nodeFormat;
		this.showId = showId;
		this.showLemmas = showLemmas;
		this.showTree = showTree;
		this.showDerivation = showDerivation;
		this.showSemantics = showSemantics;
		this.multiline = multiline;
		this.explicit = explicit;
		this.fullLemmas = fullLemmas;
		this.showContext = showContext;
	}


	/**
	 * @return the nodeFormat
	 */
	public NodeFormat getNodeFormat()
	{
		return nodeFormat;
	}


	/**
	 * @param nodeFormat the nodeFormat to set
	 */
	public void setNodeFormat(NodeFormat nodeFormat)
	{
		this.nodeFormat = nodeFormat;
	}


	/**
	 * @return the showId
	 */
	public boolean isShowId()
	{
		return showId;
	}


	/**
	 * @param showId the showId to set
	 */
	public void setShowId(boolean showId)
	{
		this.showId = showId;
	}


	/**
	 * @return the showLemmas
	 */
	public boolean isShowLemmas()
	{
		return showLemmas;
	}


	/**
	 * @param showLemmas the showLemmas to set
	 */
	public void setShowLemmas(boolean showLemmas)
	{
		this.showLemmas = showLemmas;
	}


	/**
	 * @return the showTree
	 */
	public boolean isShowTree()
	{
		return showTree;
	}


	/**
	 * @param showTree the showTree to set
	 */
	public void setShowTree(boolean showTree)
	{
		this.showTree = showTree;
	}


	/**
	 * @return the showDerivation
	 */
	public boolean isShowDerivation()
	{
		return showDerivation;
	}


	/**
	 * @param showDerivation the showDerivation to set
	 */
	public void setShowDerivation(boolean showDerivation)
	{
		this.showDerivation = showDerivation;
	}


	/**
	 * @return the showSemantics
	 */
	public boolean isShowSemantics()
	{
		return showSemantics;
	}


	/**
	 * @param showSemantics the showSemantics to set
	 */
	public void setShowSemantics(boolean showSemantics)
	{
		this.showSemantics = showSemantics;
	}


	/**
	 * @return the multiline
	 */
	public boolean isMultiline()
	{
		return multiline;
	}


	/**
	 * @param multiline the multiline to set
	 */
	public void setMultiline(boolean multiline)
	{
		this.multiline = multiline;
	}


	/**
	 * @return the explicit
	 */
	public boolean isExplicit()
	{
		return explicit;
	}


	/**
	 * @param explicit the explicit to set
	 */
	public void setExplicit(boolean explicit)
	{
		this.explicit = explicit;
	}


	/**
	 * @return the fullLemmas
	 */
	public boolean isFullLemmas()
	{
		return fullLemmas;
	}


	/**
	 * @param fullLemmas the fullLemmas to set
	 */
	public void setFullLemmas(boolean fullLemmas)
	{
		this.fullLemmas = fullLemmas;
	}


	/**
	 * @return the showContext
	 */
	public boolean isShowContext()
	{
		return showContext;
	}


	/**
	 * @param showContext the showContext to set
	 */
	public void setShowContext(boolean showContext)
	{
		this.showContext = showContext;
	}



}
