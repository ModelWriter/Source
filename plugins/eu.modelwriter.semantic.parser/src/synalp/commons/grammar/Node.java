package synalp.commons.grammar;

import java.util.*;

import synalp.commons.input.Lemma;
import synalp.commons.lexicon.Equation;
import synalp.commons.lexicon.Equations;
import synalp.commons.unification.*;
import synalp.commons.utils.Utils;

/**
 * Node of a tree object
 * @author Alexandre Denis
 */
public class Node
{
	/**
	 * Whether to display the hashcode of the node, it eases the debug but makes output hard to read
	 */
	public static boolean DISPLAY_HASHCODE = false;

	private String id = ""; // the id of this Node, it is also called name, empty string by default.
	private Node parent;
	private NodeType type;
	private Lemma anchor; // the anchor lemma, only valid for anchor nodes (and coanchors)
	private boolean anchoredInGrammar; // true when the lemma of this node has been defined in the grammar, false otherwise 
	private boolean noAdjunction; // if true, there is no adjunction possible on that node
	private boolean phonE; // if true the node is marked has having a phon=e feature (fake lexical node)
	private List<Node> children = new ArrayList<Node>();

	private FeatureConstant category;
	private FeatureStructure fsTop = new FeatureStructure();
	private FeatureStructure fsBot = new FeatureStructure();
	
	private Equations equations = new Equations(); // Equations comprise the top and bottom feature structures.
	
	private Integer nodeIndex = null; // Added by Bikash (specific to the need in parsing task only)


	/**
	 * Creates an empty standard Node.
	 */
	public Node()
	{
		this.type = NodeType.STD;
	}


	/**
	 * Creates an empty Node with given type.
	 * @param type
	 */
	public Node(NodeType type)
	{
		this.type = type;
	}


	/**
	 * Deep copies the given Node but with given parent.
	 * @param node
	 * @param parent
	 */
	public Node(Node node, Node parent)
	{
		this.id = node.getId();
		this.parent = parent;
		this.type = node.getType();
		this.category = new FeatureConstant(node.getCategory());
		this.anchoredInGrammar = node.isAnchoredInGrammar();
		this.noAdjunction = node.isNoAdjunction();
		this.phonE = node.isPhonE();
		this.fsTop = new FeatureStructure(node.getFsTop());
		this.fsBot = new FeatureStructure(node.getFsBot());
		if (node.getAnchorLemma() != null)
			this.anchor = new Lemma(node.getAnchorLemma());
		for(Node child : node.getChildren())
			children.add(new Node(child, this));
		
		if (!this.fsTop.isEmpty()) {
			Equation topEq = new Equation(this.getId(),FeatureStructureType.TOP,this.fsTop,this.type==NodeType.COANCHOR?true:false);
			equations.add(topEq);
		}
		if (!this.fsBot.isEmpty()) {
			Equation botEq = new Equation(this.getId(),FeatureStructureType.BOTTOM,this.fsBot,this.type==NodeType.COANCHOR?true:false);
			equations.add(botEq);
		}
		this.nodeIndex = node.getNodeIndex();
	}


	/**
	 * Tests whether this Node has the given category.
	 * @param cat
	 * @return true if this Node's category unifies with given category
	 */
	public boolean hasCategory(FeatureConstant cat)
	{
		return Unifier.unify(category, cat) != null;
	}


	/**
	 * Replaces node1 by node2 in the children nodes of this Node. This method also detaches node1
	 * from this node's tree.
	 * @param node1
	 * @param node2
	 * @return false if node1 does not belong to the children nodes of this Node, true if it does
	 *         and the replacement was successful.
	 */
	public boolean replaceChild(Node node1, Node node2)
	{
		int index = children.indexOf(node1);
		if (index == -1)
			return false;
		else
		{
			children.set(index, node2);
			node2.setParent(this);
			node1.setParent(null);
			return true;
		}
	}


	/**
	 * Returns a live List of the node's children. We should return a List view instead of live
	 * list, it would be more careful.
	 * @return a live List of children
	 */
	public final List<Node> getChildren()
	{
		return children;
	}


	/**
	 * Sets the children. This must be handle with care or the parenthood relationship may be
	 * broken.
	 * @param children
	 */
	public void setChildren(List<Node> children)
	{
		this.children = children;
	}


	/**
	 * Returns the parent of this Node if it exists.
	 * @return the parent or null
	 */
	public final Node getParent()
	{
		return parent;
	}


	/**
	 * Sets the parent of this node. It makes sure that the parent node's children contain this
	 * node.
	 * @param node a node that is the new parent of this Node or null to erase its parent
	 */
	public void setParent(Node node)
	{
		parent = node;
		if (parent != null && !parent.getChildren().contains(this))
			parent.addChild(this);
	}


	/**
	 * Adds the given Node as a child. It makes sure that the child refers to this node as its
	 * parent.
	 * @param node
	 */
	public void addChild(Node node)
	{
		children.add(node);
		if (!node.hasParent() || !node.getParent().equals(this))
			node.setParent(this);
	}


	/**
	 * Removes the given Node as a child of this Node. It makes sure that the child does not refer
	 * anymore to this Node as its parent.
	 * @param node
	 */
	public void removeChild(Node node)
	{
		if (children.remove(node))
			node.setParent(null);
	}


	/**
	 * Tests if this node has a defined parent.
	 * @return true if it has a non-null parent
	 */
	private boolean hasParent()
	{
		return parent != null;
	}


	/**
	 * Tests if this node is a root node.
	 * @return true if it has a null parent
	 */
	public boolean isRoot()
	{
		return parent == null;
	}


	/**
	 * Tests if this node is a foot node.
	 * @return true if this node is a foot node
	 */
	public boolean isFoot()
	{
		return type == NodeType.FOOT;
	}


	/**
	 * Tests if this node is an anchor node (not a co-anchor).
	 * @return true if this node is an anchor node
	 */
	public boolean isAnchor()
	{
		return type == NodeType.ANCHOR;
	}


	/**
	 * Tests if this node is a substitution node
	 * @return true if this node is a substitution node
	 */
	public boolean isSubst()
	{
		return type == NodeType.SUBST;
	}


	/**
	 * @return the category of this node
	 */
	public final FeatureConstant getCategory()
	{
		return category;
	}


	/**
	 * @param category
	 */
	public void setCategory(FeatureConstant category)
	{
		this.category = category;
	}


	/**
	 * @return the type of this node
	 */
	public final NodeType getType()
	{
		return type;
	}


	/**
	 * @param type
	 */
	public void setType(NodeType type)
	{
		this.type = type;
	}


	/**
	 * @return the TOP feature structure
	 */
	public final FeatureStructure getFsTop()
	{
		return fsTop;
	}


	/**
	 * @param fs
	 * @return this Node for chaining
	 */
	public Node setFsTop(FeatureStructure fs)
	{
		fsTop = fs;
		return this;
	}


	/**
	 * @return the BOT feature structure
	 */
	public final FeatureStructure getFsBot()
	{
		return fsBot;
	}


	/**
	 * @param fs
	 * @return this Node for chaining
	 */
	public Node setFsBot(FeatureStructure fs)
	{
		fsBot = fs;
		return this;
	}


	/**
	 * Returns fsTop or fsBottom depending on the given type.
	 * @param type
	 * @return a feature structure of this node
	 */
	public FeatureStructure getFs(FeatureStructureType type)
	{
		switch (type)
		{
			case TOP:
				return fsTop;
			case BOTTOM:
				return fsBot;
		}
		return null;
	}


	/**
	 * Sets the fsTop or fsBottom given the type.
	 * @param type
	 * @param fs
	 * @return this Node for chaining
	 */
	public Node setFs(FeatureStructureType type, FeatureStructure fs)
	{
		switch (type)
		{
			case TOP:
				fsTop = fs;
				break;

			case BOTTOM:
				fsBot = fs;
				break;
		}
		return this;
	}


	/**
	 * Sets the feature structures of this Node. The given feature structure is assumed to contain
	 * the category 'cat', the top feature structure 'top' and the bottom feature structure 'bot'.
	 * @param fs a big feature structure containing cat, top and bot features.
	 */
	public void setTopBotCat(FeatureStructure fs)
	{
		Feature catFeature = fs.getFeature("cat");
		if (catFeature != null)
			category = ((FeatureConstant) catFeature.getValue());
		else category = new FeatureConstant();

		Feature topFeature = fs.getFeature("top");
		if (topFeature != null)
			fsTop = (FeatureStructure) topFeature.getValue();

		Feature botFeature = fs.getFeature("bot");
		if (botFeature != null)
			fsBot = (FeatureStructure) botFeature.getValue();

		Feature phonEFeature = fs.getFeature("phon");
		if (phonEFeature != null && phonEFeature.getValue().toString().equals("e"))
			phonE = true;
	}


	/**
	 * Returns a pretty representation of this node.
	 * @return a String
	 */
	@Override
	public String toString()
	{
		return toString(NodeFormat.MINIMALIST, new InstantiationContext());
	}


	/**
	 * Returns a pretty representation of this node without initial indent.
	 * @param format
	 * @param context
	 * @return a String
	 */
	public String toString(NodeFormat format, InstantiationContext context)
	{
		return toString(format, context, "");
	}


	/**
	 * Returns a pretty representation of this node.
	 * @param format
	 * @param context
	 * @param indent the current indent
	 * @return a String
	 */
	public String toString(NodeFormat format, InstantiationContext context, String indent)
	{
		StringBuilder ret = new StringBuilder();

		if (format.isIndented())
			ret.append(indent);

		StringBuilder header = new StringBuilder();

		// cross for no adjunction
		if (isNoAdjunction())
			header.append("\u2717");

		// cat
		header.append(category);

		// node type symbol
		if (type == NodeType.ANCHOR)
			header.append("\u2666");
		if (type == NodeType.COANCHOR)
			header.append("\u2662");
		else if (type == NodeType.SUBST)
			header.append("\u2193");
		else if (type == NodeType.FOOT)
			header.append("\u272D");

		// anchor
		if (anchor != null)
			header.append(anchor);

		// idx
		if (format.isShowIdx())
		{
			FeatureValue topIdx = fsTop.getValueOf("idx");
			FeatureValue botIdx = fsBot.getValueOf("idx");

			if (topIdx != null && botIdx != null)
			{
				FeatureValue topVal = context.getValue(topIdx);
				FeatureValue botVal = context.getValue(botIdx);
				if (topVal.equals(botVal))
					header.append(formatIdx(topIdx, context));
				else
				{
					header.append(formatIdx(topIdx, context));
					header.append("/");
					header.append(formatIdx(botIdx, context));
				}
			}
			else if (topIdx != null)
				header.append(formatIdx(topIdx, context));
			else if (botIdx != null)
				header.append(formatIdx(botIdx, context));
		}

		ret.append(header);

		// fs
		if (format.isShowFS())
		{
			if (format.isInstFeatFS())
				ret.append(fsTop.toStringInstFS(context)).append("|").append(fsBot.toStringInstFS(context));
			else
			{
				ret.append(" top=").append(fsTop.toString(context));
				if (!fsTop.isEmpty() && !fsBot.isEmpty() && format!=NodeFormat.SINGLE_NODE)
					ret.append("\n").append(indent).append(Utils.repeat(' ', header.length()));
				ret.append(" bot=").append(fsBot.toString(context));
			}
		}

		if (!children.isEmpty() && format!=NodeFormat.SINGLE_NODE)
		{
			if (format.isIndented())
			{
				for(Node child : children)
					ret.append("\n").append(child.toString(format, context, indent + "\t"));
			}
			else
			{
				StringBuilder str = new StringBuilder();
				for(Node child : children)
					str.append(child.toString(format, context, indent)).append(" ");
				ret.append("(").append(str.toString().trim()).append(")");
			}
		}
		return ret.toString();
	}


	/**
	 * Returns a formatted String for the given idx value.
	 * @param idx
	 * @param context 
	 * @return a String
	 */
	private static String formatIdx(FeatureValue idx, InstantiationContext context)
	{
		if (idx instanceof FeatureConstant)
			return "?"+idx;
		else return idx.toString(context);
	}


	/**
	 * Returns the lemma that anchors this Node if it is an anchor node.
	 * @return the anchor or null if none is defined.
	 */
	public Lemma getAnchorLemma()
	{
		return anchor;
	}


	/**
	 * Sets the lemma that anchors this Node.
	 * @param anchor the anchor to set
	 */
	public void setAnchorLemma(Lemma anchor)
	{
		this.anchor = anchor;
	}


	/**
	 * Sets the lemma that anchors this Node. This method also enables to set the anchoredInGrammar
	 * flag.
	 * @param anchor the anchor to set
	 * @param anchoredInGrammar the anchoredInGrammar flag
	 */
	public void setAnchorLemma(Lemma anchor, boolean anchoredInGrammar)
	{
		this.anchor = anchor;
		this.anchoredInGrammar = anchoredInGrammar;
	}


	/**
	 * Tests whether this Node's anchor has been defined in the grammar. It is also called a "lex"
	 * node in the previous format.
	 * @return whether this Node's anchor has been defined in the grammar
	 */
	public boolean isAnchoredInGrammar()
	{
		return anchoredInGrammar;
	}


	/**
	 * Gets the id of this Node.
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}


	/**
	 * Sets the id of this Node.
	 * @param id
	 */
	public void setId(String id)
	{
		this.id = id;
	}


	/**
	 * Tests if this Node forbids adjunctions.
	 * @return true if this Node forbids adjunctions, false if it allows adjunctions.
	 */
	public boolean isNoAdjunction()
	{
		return noAdjunction;
	}


	/**
	 * Sets whether this Node forbids adjunctions.
	 * @param noAdjunction
	 */
	public void setNoAdjunction(boolean noAdjunction)
	{
		this.noAdjunction = noAdjunction;
	}


	/**
	 * Makes sure that the feature structure of the lemma is well defined. This is in general
	 * assumed to be called at the end of the generation process, before returning realizations.
	 * @param context the context in which the top and bot fs are unified before setting the lemma
	 *            fs
	 */
	public void setupLemmaFeatures(InstantiationContext context)
	{
		if (anchor != null)
		{
			FeatureStructure fs = Unifier.unify(fsTop, fsBot, context);
			if (fs != null) // we should maybe warn if it is null
			{
				anchor.setFs(fs);
				anchor.setCategory(category);
			}
		}
	}


	/**
	 * @return the phonE
	 */
	public boolean isPhonE()
	{
		return phonE;
	}


	/**
	 * @param phonE the phonE to set
	 */
	public void setPhonE(boolean phonE)
	{
		this.phonE = phonE;
	}

	public Equations getEquations() {
		return equations;
	}
	
	public void setNodeIndex(int index) {
		this.nodeIndex = index;
	}
	public Integer getNodeIndex() {
		return nodeIndex;
	}
}
