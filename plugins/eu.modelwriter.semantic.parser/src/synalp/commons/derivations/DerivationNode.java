package synalp.commons.derivations;

import java.util.*;

import synalp.commons.grammar.Tree;
import synalp.commons.utils.Utils;


/**
 * A DerivationNode is a node in a DerivationTree which describes an operation between its tree and
 * its parent tree. The operation is fully described by its type, the parent tree and the address in
 * the parent tree where to perform the operation. For equality/hashcode, the parent is not used,
 * whereas only the tree id is used, not the whole tree.
 * @author Alexandre Denis
 */
public class DerivationNode
{
	private Tree tree; // the argument of the derivation node

	private GornAddress address;
	private DerivationNode parent;
	private DerivationNodeType type;

	private Collection<DerivationNode> children = new ArrayList<DerivationNode>();


	/**
	 * Creates a DerivationNode corresponding to the given Tree. It does not have children nor
	 * parent.
	 * @param tree
	 */
	public DerivationNode(Tree tree)
	{
		this.setTree(tree);
	}


	/**
	 * Creates a DerivationNode corresponding to the given Tree with the given type. It does not
	 * have children nor parent.
	 * @param type
	 * @param tree
	 */
	public DerivationNode(DerivationNodeType type, Tree tree)
	{
		this.setTree(tree);
		this.type = type;
	}


	/**
	 * Deep copies the given DerivationNode without considering its parent.
	 * @param node
	 */
	public DerivationNode(DerivationNode node)
	{
		this(node, null);
	}


	/**
	 * Deep copies the given DerivationNode but with given parent.
	 * @param node
	 * @param parent
	 */
	public DerivationNode(DerivationNode node, DerivationNode parent)
	{
		this.parent = parent;
		this.type = node.getType();
		this.tree = node.getTree();
		this.address = node.getAddress();
		for(DerivationNode child : node.getChildren())
			children.add(new DerivationNode(child, this));
	}


	/**
	 * Adds the given {@link DerivationNode} as a child. It makes sure that the child refers to this
	 * node as its parent.
	 * @param node
	 */
	public void addChild(DerivationNode node)
	{
		children.add(node);
		if (!node.hasParent() || !node.getParent().equals(this))
			node.setParent(this);
	}


	/**
	 * Tests if this {@link DerivationNode} has a defined parent.
	 * @return true if it has a non-null parent
	 */
	private boolean hasParent()
	{
		return parent != null;
	}


	/**
	 * @return the parent
	 */
	public DerivationNode getParent()
	{
		return parent;
	}


	/**
	 * @param parent the parent to set
	 */
	public void setParent(DerivationNode parent)
	{
		this.parent = parent;
	}


	/**
	 * @return the children
	 */
	public Collection<DerivationNode> getChildren()
	{
		return children;
	}


	/**
	 * @param children the children to set
	 */
	public void setChildren(List<DerivationNode> children)
	{
		this.children = children;
	}


	/**
	 * @return the address
	 */
	public GornAddress getAddress()
	{
		return address;
	}


	/**
	 * @param address the address to set
	 */
	public void setAddress(GornAddress address)
	{
		this.address = address;
	}


	/**
	 * @return the tree
	 */
	public Tree getTree()
	{
		return tree;
	}


	/**
	 * @param tree the tree to set
	 */
	public void setTree(Tree tree)
	{
		this.tree = tree;
	}


	/**
	 * @return the type
	 */
	public DerivationNodeType getType()
	{
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(DerivationNodeType type)
	{
		this.type = type;
	}


	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		ret.append("(").append(type.getSymbol()).append(" ").append(tree.getId()).append(".").append(Utils.print(tree.getLemmas(), "_"));
		if (address != null)
			ret.append(" ").append(address);
		if (children.isEmpty())
			ret.append(")");
		else
		{
			for(DerivationNode child : children)
				ret.append(" ").append(child);
			ret.append(")");
		}
		return ret.toString();
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((tree == null) ? 0 : tree.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DerivationNode other = (DerivationNode) obj;
		if (address == null)
		{
			if (other.address != null)
				return false;
		}
		else if (!address.equals(other.address))
			return false;
		if (children == null)
		{
			if (other.children != null)
				return false;
		}
		else if (!children.equals(other.children))
			return false;
		if (tree == null)
		{
			if (other.tree != null)
				return false;
		}
		else if (!tree.equals(other.tree))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	/**
	 * Returns true is this DerivationNode is the result of some substitution operation in the past.
	 * @return
	 */
	public boolean isObtainedAfterSubstitution() {
		if (type==DerivationNodeType.SUBSTITUTION)
			return true;
		else
			for(DerivationNode child : children)
				if (child.isObtainedAfterSubstitution())
					return true;
		return false;
	}
	
	/**
	 * Returns true is this DerivationNode is the result of some adjunction operation in the past.
	 * @return
	 */
	public boolean isObtainedAfterAdjunction() {
		if (type==DerivationNodeType.ADJUNCTION)
			return true;
		else
			for(DerivationNode child : children)
				if (child.isObtainedAfterAdjunction())
					return true;
		return false;
	}
}
