package synalp.commons.grammar;

/**
 * A NodeFormat contains options to display a node.
 * @author Alexandre Denis
 */
public class NodeFormat
{
	/**
	 * Format: only the node without children and including FS
	 */
	public static NodeFormat SINGLE_NODE = new NodeFormat(false, false, false, true, false);
	
	/**
	 * Format: node and children on one line with the idx and no FS
	 */
	public static NodeFormat MINIMALIST = new NodeFormat(false, true, true, false, false);
	
	/**
	 * Format: node and children on one line with the idx with FS but only instantiated features.
	 */
	public static NodeFormat MINIMALIST_INSTFS = new NodeFormat(false, true, true, true, true);
	
	/**
	 * Format: node and children on several lines with indent, idx and FS
	 */
	public static NodeFormat INDENTED = new NodeFormat(true, true, true, true, false);

	private boolean showFS;
	private boolean showIdx;
	private boolean indented;
	private boolean recursive;
	private boolean instFeatFS;
	

	/**
	 * @param indented if true prints a Node across several lines with indent
	 * @param showIdx if true when Nodes have idx features print them
	 * @param recursive if true prints a Node and its children, if false only prints the Node
	 * @param showFS if true prints the top and bot fs
	 * @param instFeatFS if true the FS shows only the instantiated features
	 */
	public NodeFormat(boolean indented, boolean showIdx, boolean recursive, boolean showFS, boolean instFeatFS)
	{
		this.indented = indented;
		this.showIdx = showIdx;
		this.recursive = recursive;
		this.showFS = showFS;
		this.instFeatFS = instFeatFS;
	}


	/**
	 * @return the showIdx
	 */
	public boolean isShowIdx()
	{
		return showIdx;
	}


	/**
	 * @return the indented
	 */
	public boolean isIndented()
	{
		return indented;
	}


	/**
	 * @return the recursive
	 */
	public boolean isRecursive()
	{
		return recursive;
	}


	/**
	 * @return whether to show fs
	 */
	public boolean isShowFS()
	{
		return showFS;
	}


	/**
	 * @return the instFeatFS
	 */
	public boolean isInstFeatFS()
	{
		return instFeatFS;
	}

}
