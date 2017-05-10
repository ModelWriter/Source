package synalp.commons.derivations;

import java.util.*;

import synalp.commons.grammar.*;


/**
 * A GornAddress is a way to refer to a node in a tree.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class GornAddress extends ArrayList<Integer>
{
	/**
	 * Creates a new GornAddress that refer to given Node in given Tree.
	 * @param node
	 * @param tree
	 * @return a new gorn address or null if the Node does not belong to the Tree
	 */
	public static GornAddress getAddress(Node node, Tree tree)
	{
		return getAddress(node, tree.getRoot(), new GornAddress(0));
	}


	/**
	 * Returns the address of given target from given node and current address.
	 * @param target
	 * @param node
	 * @param currentAddress
	 * @return a new GornAddress or null if the target is not found
	 */
	private static GornAddress getAddress(Node target, Node node, GornAddress currentAddress)
	{
		if (target.equals(node))
			return currentAddress;
		else for(int i = 0; i < node.getChildren().size(); i++)
		{
			GornAddress result = getAddress(target, node.getChildren().get(i), new GornAddress(currentAddress, i));
			if (result != null)
				return result;
		}
		return null;
	}


	/**
	 * Creates a new GornAddress.
	 * @param indexes
	 */
	public GornAddress(Integer... indexes)
	{
		addAll(Arrays.asList(indexes));
	}


	/**
	 * Creates a new GornAddress.
	 * @param indexes
	 */
	public GornAddress(List<Integer> indexes)
	{
		addAll(indexes);
	}


	/**
	 * Copies the given address but adds the given indexes in the end.
	 * @param address
	 * @param indexes
	 */
	public GornAddress(GornAddress address, Integer... indexes)
	{
		addAll(address);
		addAll(Arrays.asList(indexes));
	}


	/**
	 * Returns the Node referred to by this address in the given Tree.
	 * @param tree
	 * @return a Node or null if this address does not refer correctly in the given Tree
	 */
	public Node getNode(Tree tree)
	{
		return getNode(tree.getRoot(), 1);
	}


	/**
	 * Tests whether this address is left of the given address.
	 * @param address 
	 * @return whether this address is left of the given address
	 */
	public boolean isLeftOf(GornAddress address)
	{
		if (get(0) == address.get(0))
			return subAddress().isLeftOf(address.subAddress());
		else return get(0) < address.get(0);
	}
	
	
	/**
	 * Returns this GornAddress but truncated after its first index. This method returns a new address and does not modify this one.
	 * @return a new GornAddress truncated after the first index
	 */
	private GornAddress subAddress()
	{
		return new GornAddress(subList(1, size()));
	}


	/**
	 * Returns the Node referred at the given address index.
	 * @param node
	 * @param indexInAddress
	 * @return a Node or null if not found
	 */
	private Node getNode(Node node, int indexInAddress)
	{
		if (indexInAddress >= size())
			return node;
		else
		{
			int index = get(indexInAddress);
			if (index >= node.getChildren().size() || index < 0)
				return null;
			else return getNode(node.getChildren().get(index), indexInAddress + 1);
		}
	}


	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		if (isEmpty())
			return "";
		else
		{
			ret.append(get(0));
			for(int i = 1; i < size(); i++)
				ret.append(".").append(get(i));
			return ret.toString();
		}
	}
}
