package synalp.commons.grammar;

import java.util.*;

import synalp.commons.derivations.GornAddress;
import synalp.commons.input.Lemma;
import synalp.commons.lexicon.Equations;
import synalp.commons.unification.*;


/**
 * A Tree.
 */
public class Tree implements UnifiableComponent
{
	private String id;
	private Node root;


	/**
	 * Creates a new empty tree with given id.
	 * @param id
	 */
	public Tree(String id)
	{
		this.id = id;
	}


	/**
	 * Creates a new tree with given id and root.
	 * @param id
	 * @param root
	 */
	public Tree(String id, Node root)
	{
		this.id = id;
		this.root = root;
	}


	/**
	 * Deep copies the given Tree.
	 * @param tree
	 */
	public Tree(Tree tree)
	{
		this.id = tree.getId();
		this.root = new Node(tree.getRoot(), null);
	}


	/**
	 * Makes sure that all the leaves of this Tree have a lemma anchor. If there is no lemma, create
	 * one whose surface form is the node category (first one if many).
	 */
	public void lemmatizeLeaves()
	{
		for(Node node : getYield())
			if (node.getAnchorLemma() == null)
			{
				Lemma anchor = new Lemma(node.getCategory().getFirstValue());
				FeatureStructure fs = new FeatureStructure();
				anchor.setFs(fs);
				anchor.setCategory(node.getCategory());
				node.setAnchorLemma(anchor);
			}
	}


	/**
	 * Makes sure that all the lemmas of this Tree have a correct feature structure.
	 * @param context the context in which to unify the top/bot fs before setting the lemma fs
	 */
	public void setupLemmaFeatures(InstantiationContext context)
	{
		for(Node node : getLemmasNodes())
			node.setupLemmaFeatures(context);
	}


	/**
	 * Tests if all nodes of this Tree have top and bot fs that unify in the given context.
	 * @param context
	 * @return true if it is the case, false otherwise
	 */
	public boolean topBotUnifies(InstantiationContext context)
	{
		return getFailedTopBotNode(context) == null;
	}


	/**
	 * Returns the first found Node of this Tree for which the structures top and bot do not unify
	 * in the given context.
	 * @param context
	 * @return a Node whose top bot unification failed or null if none can be found
	 */
	public Node getFailedTopBotNode(InstantiationContext context)
	{
		for(Node node : getNodes())
			if (Unifier.unify(node.getFsTop(), node.getFsBot(), context) == null)
				return node;
		return null;
	}


	/**
	 * Replaces the given variable by the given value everywhere it appears in this Tree.
	 * @param variable
	 * @param value
	 */
	@Override
	public void replaceVariable(FeatureVariable variable, FeatureValue value)
	{
		for(Node node : getNodes())
		{
			node.getFsTop().replaceVariable(variable, value);
			node.getFsBot().replaceVariable(variable, value);
		}
	}


	/**
	 * Returns all variables that are defined in all feature structures of this Tree.
	 * @return a set of variables
	 */
	@Override
	public Set<FeatureVariable> getAllVariables()
	{
		Set<FeatureVariable> ret = new HashSet<FeatureVariable>();
		for(Node node : getNodes())
		{
			ret.addAll(node.getFsTop().getAllVariables());
			ret.addAll(node.getFsBot().getAllVariables());
		}
		return ret;
	}


	/**
	 * This method has been added for coherence reason. I do not think it will be of practical use.
	 */
	@Override
	public void instantiate(InstantiationContext context)
	{
		for(FeatureVariable var : getAllVariables())
			if (context.containsKey(var))
				replaceVariable(var, context.get(var));
	}


	/**
	 * Returns the list of lemmas of anchor nodes. Note that this has been changed since we now
	 * allow NADJ nodes.
	 * @return a list of lemmas
	 */
	public List<Lemma> getLemmas()
	{
		List<Lemma> ret = new ArrayList<Lemma>();
		for(Node anchor : getLemmasNodes())
			ret.add(anchor.getAnchorLemma());
		return ret;
	}


	/**
	 * Returns the first found main anchor node of this Tree.
	 * @return the anchor node
	 */
	public Node getMainAnchor()
	{
		return getNodeByType(root, NodeType.ANCHOR);
	}


	/**
	 * Returns all anchor nodes of this Tree, including co-anchors.
	 * @return the list of anchors and co-anchors in prefix order.
	 */
	public List<Node> getAnchors()
	{
		return getNodesByType(root, NodeType.ANCHOR, NodeType.COANCHOR);
	}
	
	
	/**
	 * Returns all coanchor nodes of this Tree.
	 * @return the list of co-anchors in prefix order.
	 */
	public List<Node> getCoAnchors()
	{
		return getNodesByType(root, NodeType.COANCHOR);
	}


	/**
	 * Returns all the nodes in prefix order, whatever their type, that contain a non-null lemma.
	 * @return the list of nodes whose lemma is not null in prefix order.
	 */
	public List<Node> getLemmasNodes()
	{
		List<Node> ret = new ArrayList<Node>();
		for(Node node : getNodes())
			if (node.getAnchorLemma() != null)
				ret.add(node);
		return ret;
	}


	/**
	 * Returns all co-anchor nodes of this Tree whose lemma is not yet defined.
	 * @return a list of nodes
	 */
	public List<Node> getUninstantiatedCoanchors()
	{
		List<Node> ret = new ArrayList<Node>();
		for(Node node : getNodes())
			if (node.getType() == NodeType.COANCHOR && node.getAnchorLemma() == null)
				ret.add(node);
		return ret;
	}


	/**
	 * Returns the foot node of this Tree if there is one.
	 * @return the foot node or null if there is none
	 */
	public Node getFoot()
	{
		return getNodeByType(root, NodeType.FOOT);
	}


	/**
	 * Returns all the substitution nodes of this Tree.
	 * @return the foot node
	 */
	public List<Node> getSubstitutions()
	{
		return getNodesByType(root, NodeType.SUBST);
	}


	/**
	 * Returns all the nodes of this Tree in prefix order.
	 * @return all the nodes
	 */
	public List<Node> getNodes()
	{
		return getNodes(root);
	}


	/**
	 * Returns the first found Node with given id. This typically could be cached.
	 * @param id
	 * @return a Node of this Tree or null if none is found
	 */
	public Node getNodeById(String id)
	{
		for(Node node : getNodes())
			if (node.getId().equals(id))
				return node;
		return null;
	}


	/**
	 * Returns all the nodes below the given node including the given node.
	 * @param node
	 * @return a list of nodes
	 */
	private List<Node> getNodes(Node node)
	{
		List<Node> ret = new ArrayList<Node>();
		ret.add(node);
		for(Node child : node.getChildren())
			ret.addAll(getNodes(child));
		return ret;
	}


	/**
	 * Returns the first found node of this Tree, including root, which has one of the given types.
	 * We may consider caching this.
	 * @param types
	 * @return a Node of this Tree or null if none is found.
	 */
	public Node getNodeByType(NodeType... types)
	{
		return getNodeByType(root, types);
	}


	/**
	 * Returns all nodes of this Tree in the prefix order, including root, which have one the given
	 * types. We may consider caching this.
	 * @param types
	 * @return a list of Nodes of this Tree or empty list if none is found.
	 */
	public List<Node> getNodesByType(NodeType... types)
	{
		return getNodesByType(root, types);
	}


	/**
	 * Returns the first found descendant node of the given node, including given node, which has
	 * one of the given types. We may consider caching this.
	 * @param node
	 * @param types
	 * @return a Node of this Tree or null if none is found.
	 */
	private Node getNodeByType(Node node, NodeType... types)
	{
		for(NodeType type : types)
			if (node.getType() == type)
				return node;

		for(Node child : node.getChildren())
		{
			Node result = getNodeByType(child, types);
			if (result != null)
				return result;
		}

		return null;
	}


	/**
	 * Returns all the descendant nodes of the given node in the prefix order, including given node,
	 * which have one the given types. We may consider caching this.
	 * @param node
	 * @param types
	 * @return a list of Nodes of this Tree or empty list if none is found.
	 */
	private List<Node> getNodesByType(Node node, NodeType... types)
	{
		List<Node> ret = new ArrayList<Node>();
		for(NodeType type : types)
			if (node.getType() == type)
				ret.add(node);
		for(Node child : node.getChildren())
			ret.addAll(getNodesByType(child, types));
		return ret;
	}


	/**
	 * Returns the yield of this Tree that is the list of leaves.
	 * @return the list of leaves of this Tree
	 */
	public List<Node> getYield()
	{
		return getYield(root);
	}


	/**
	 * Returns the yield of a Node, that is the list of leaves.
	 * @param node
	 * @return the list of leaves accessible from the given Node
	 */
	private List<Node> getYield(Node node)
	{
		List<Node> ret = new ArrayList<Node>();
		if (node.getChildren().isEmpty())
			ret.add(node);
		else for(Node child : node.getChildren())
			ret.addAll(getYield(child));
		return ret;
	}


	/**
	 * Returns the Node of this Tree referred to by given address.
	 * @param address
	 * @return a Node of this Tree or null if the address is out of bounds
	 */
	public Node getNode(GornAddress address)
	{
		return address.getNode(this);
	}


	/**
	 * @return the name
	 */
	public String getId()
	{
		return id;
	}


	/**
	 * @param id the name to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}


	/**
	 * @return the root of the tree
	 */
	public final Node getRoot()
	{
		return root;
	}


	/**
	 * @param root
	 */
	public void setRoot(Node root)
	{
		this.root = root;
	}


	/**
	 * Tests if this Tree is auxiliary. This typically could be cached.
	 * @return whether the Tree contains a foot node
	 */
	public boolean isAuxiliary()
	{
		return getFoot() != null;
	}


	/**
	 * Returns a pretty representation of the tree. Note that it uses unicode characters to
	 * represent the node types.
	 * @return a String
	 */
	@Override
	public String toString()
	{
		return toString(new InstantiationContext());
	}


	/**
	 * Returns a pretty representation of the tree. Note that it uses unicode characters to
	 * represent the node types.
	 * @param context
	 * @return a String
	 */
	public String toString(InstantiationContext context)
	{
		return toString(NodeFormat.MINIMALIST, context);
	}


	/**
	 * Returns a pretty representation of the tree. Note that it uses unicode characters to
	 * represent the node types.
	 * @param format
	 * @param context
	 * @return a String
	 */
	public String toString(NodeFormat format, InstantiationContext context)
	{
		return root.toString(format, context);
	}


	/**
	 * Only uses the id.
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	/**
	 * Only uses the id.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tree other = (Tree) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	public Equations getEquations() {
		Equations equations = new Equations();
		// My understanding is that the equations should comprise of the top and bottom feature structures on the (main anchor and the co-anchors) nodes only.
		for (Node x:getAnchors()) {
			Equations xEqs = x.getEquations(); 
			if (!xEqs.isEmpty()) {
				equations.addAll(xEqs);
			}
		}
		
		/*
		Node main = getMainAnchor();
		FeatureStructure topFS = main.getFsTop();
		if (!topFS.isEmpty()) {
			Equation topEq = new Equation(main.getId(),FeatureStructureType.TOP,topFS,false);
			equations.add(topEq);
		}
		FeatureStructure bottomFS = main.getFsBot();
		if (!bottomFS.isEmpty()) {
			Equation bottomEq = new Equation(main.getId(),FeatureStructureType.BOTTOM,bottomFS,false);
			equations.add(bottomEq);
		}
		
		
		for (Node x:getCoAnchors()) {
			FeatureStructure top = x.getFsTop();
			if (!top.isEmpty()) {
				Equation topx = new Equation(x.getId(), FeatureStructureType.TOP, top, true);
				equations.add(topx);
			}
			FeatureStructure bottom = x.getFsBot();
			if (!bottom.isEmpty()) {
				Equation bottomx = new Equation(x.getId(), FeatureStructureType.BOTTOM, bottom, true);
				equations.add(bottomx);
			}
		}
		*/
		
		return equations;
	}


	public List<Integer> getLemmaNodesIndex() {
		List<Integer> ret = new ArrayList<Integer>();
		for (Node n:getLemmasNodes()) {
			ret.add(n.getNodeIndex());
		}
		return ret;
	}
}
