package synalp.generation.jeni;

import java.util.*;

import synalp.commons.derivations.*;
import synalp.commons.grammar.*;
import synalp.commons.input.Lemma;
import synalp.commons.semantics.*;
import synalp.commons.unification.*;
import synalp.commons.utils.Utils;
import synalp.generation.ChartItem;


/**
 * A JeniChartItem is an item in a chart. It contains the tree that is currently built, the
 * semantics that is currently covered, the derivation tree that represents the operations that lead
 * to this ChartItem, and an instantiation context for the variables that are shared across the
 * tree.
 * @author Alexandre Denis
 */
public final class JeniChartItem implements ChartItem, UnifiableComponent
{
	private static int currentId = 0;

	private final String id;
	private final Tree tree;
	private final Semantics semantics;
	private final DerivationTree derivation;
	private final InstantiationContext context;
	private Set<FeatureVariable> variablesCache;

	private JeniChartItem parentItemSource;
	private JeniChartItem parentItemTarget;
	private DerivationNodeType parentOperationType;
	
	private float probability;

	private ArrayList<String> usedItemsID = new ArrayList<String>(); // to keep track of all items ID used in building this chart item.
	
	
	/**
	 * Creates a new id, for internal purpose.
	 * @return a new id
	 */
	private static String createNewId()
	{
		return "Item-" + (currentId++);
	}


	/**
	 * Resets the id count.
	 */
	public static void resetIdCount()
	{
		currentId = 0;
	}


	/**
	 * Creates a new ChartItem based on given tree, semantics and context. This method creates a new
	 * id for the ChartItem and initializes the derivation tree to be an ANCHORING node.
	 * @param tree
	 * @param semantics
	 * @param context
	 * @param probability
	 */
	public JeniChartItem(Tree tree, Semantics semantics, InstantiationContext context, float probability)
	{
		this.id = createNewId();
		this.tree = tree;
		this.semantics = semantics;
		this.context = context;
		this.context.clean(semantics, tree);
		this.parentOperationType = DerivationNodeType.ANCHORING;
		this.derivation = new DerivationTree(tree, new DerivationNode(DerivationNodeType.ANCHORING, tree));
		
		this.probability = probability;
	}
	
	
	/**
	 * Creates a new ChartItem based on given tree, semantics, context and derivation tree. This
	 * method creates a new id for the ChartItem.
	 * @param tree
	 * @param semantics
	 * @param context
	 * @param derivationTree
	 * @param type the type of operation that lead to that item
	 * @param itemSource the source item of the operation
	 * @param itemTarget the target item of the operation
	 * @param probability the probability of this item
	 */
	public JeniChartItem(Tree tree, Semantics semantics, InstantiationContext context, DerivationTree derivationTree, DerivationNodeType type,
			JeniChartItem itemSource, JeniChartItem itemTarget)
	{
		this.id = createNewId();
		this.tree = tree;
		this.semantics = semantics;
		this.context = context;
		this.context.clean(semantics, tree);
		this.derivation = derivationTree;
		this.parentOperationType = type;
		this.parentItemSource = itemSource;
		this.parentItemTarget = itemTarget;	
	}


	/**
	 * Combines the instantiation contexts of the two chart items such that there is no variable
	 * that appears in both items. This method both updates the context of each item to prevent name
	 * conflicts and returns an aggregated context.
	 * @param item1 an item whose context may be modified
	 * @param item2 an item whose context may be modified
	 * @return a new instantiation context that contains all the variables of item1 and all the
	 *         variables of item2 after renaming.
	 */
	public static InstantiationContext combineContexts(JeniChartItem item1, JeniChartItem item2)
	{
		Set<FeatureVariable> vars1 = item1.getAllVariables();
		Set<FeatureVariable> vars2 = item2.getAllVariables();

		Set<FeatureVariable> union = new HashSet<FeatureVariable>(vars1);
		union.addAll(vars2);

		Set<FeatureVariable> intersects = new HashSet<FeatureVariable>(vars1);
		intersects.retainAll(vars2);

		if (intersects.isEmpty())
			return new InstantiationContext(item1.getContext(), item2.getContext());
		else
		{
			for(FeatureVariable sharedVar : intersects)
				item1.replaceVariable(sharedVar, FeatureVariable.createNewVariable(union));
			return new InstantiationContext(item1.getContext(), item2.getContext());
		}
	}


	/**
	 * Tests whether this ChartItem alters the idx for the given category.
	 * @param cat
	 * @return true if this ChartItem alters the idx
	 */
	public boolean isIdxAltering(FeatureConstant cat)
	{
		Node root = tree.getRoot();
		if (!root.hasCategory(cat))
			return false;

		Node foot = tree.getFoot();
		if (foot != null)
		{
			FeatureValue rootTopIdx = root.getFsTop().getValueOf("idx");
			FeatureValue rootBotIdx = root.getFsBot().getValueOf("idx");
			FeatureValue footTopIdx = foot.getFsTop().getValueOf("idx");
			FeatureValue footBotIdx = foot.getFsBot().getValueOf("idx");

			if (idxMismatch(rootTopIdx, footTopIdx))
				return true;
			if (idxMismatch(rootTopIdx, footBotIdx))
				return true;
			if (idxMismatch(rootBotIdx, footTopIdx))
				return true;
			if (idxMismatch(rootBotIdx, footBotIdx))
				return true;
		}

		return false;
	}


	/**
	 * Tests whether the two given values mismatch. It is used to rule out the null case.
	 * @param idx1
	 * @param idx2
	 * @return true if the two values mismatch
	 */
	private boolean idxMismatch(FeatureValue idx1, FeatureValue idx2)
	{
		if (idx1 == null || idx2 == null)
			return false;
		else if (idx1.getType() == FeatureValueType.VARIABLE && idx2.getType() == FeatureValueType.VARIABLE)
		{
			if (((FeatureVariable) idx1).getName().equals(((FeatureVariable) idx2).getName()))
				return false;
		}

		return Unifier.unify(idx1, idx2, context) != null; // I think it's buggy
	}


	/**
	 * Returns the id of this ChartItem.
	 * @return an id
	 */
	public String getId()
	{
		return id;
	}


	/**
	 * Replaces the given variable by the new one in the tree, the semantics, and the context of
	 * this ChartItem.
	 * @param oldVar
	 * @param newVar
	 */
	public void replaceVariable(FeatureVariable oldVar, FeatureValue newVar)
	{
		tree.replaceVariable(oldVar, newVar);
		context.replaceVariable(oldVar, newVar);
		semantics.replaceVariable(oldVar, newVar);

		if (variablesCache == null)
			computeVariableCache();
		else
		{
			variablesCache.remove(oldVar);
			if (newVar.getType() == FeatureValueType.VARIABLE)
				variablesCache.add((FeatureVariable) newVar);
		}
	}


	/**
	 * Returns all variables that are associated to this ChartItem. This includes the variables in
	 * the tree feature structures, the variables in the semantics, and the variables in context.
	 * There could be variables in the context that do not appear directly in the structures or
	 * semantics, it is possible to have variables that are instantiated to structures that contain
	 * themselves variables. There could also be variables in the structures that do not appear in
	 * the context since they may be still not instantiated. This method may be costly and we may
	 * want to cache that somehow.
	 * @return a set of variables
	 */
	public Set<FeatureVariable> getAllVariables()
	{
		if (variablesCache == null)
			computeVariableCache();

		return variablesCache;
	}


	/**
	 * Creates the variable cache.
	 */
	private void computeVariableCache()
	{
		variablesCache = new HashSet<FeatureVariable>();
		variablesCache.addAll(tree.getAllVariables());
		variablesCache.addAll(context.getAllVariables());
		variablesCache.addAll(semantics.getAllVariables());
	}


	/**
	 * @return the tree
	 */
	public Tree getTree()
	{
		return tree;
	}


	/**
	 * @return the semantics
	 */
	public Semantics getSemantics()
	{
		return semantics;
	}


	/**
	 * @return the derivation
	 */
	public DerivationTree getDerivation()
	{
		return derivation;
	}


	/**
	 * @return the context
	 */
	public InstantiationContext getContext()
	{
		return context;
	}


	@Override
	public String toString()
	{
		return toString(ItemFormat.MINIMALIST);
	}


	/**
	 * Returns a string representation of this item with given format.
	 * @param format
	 * @return a String
	 */
	public String toString(ItemFormat format)
	{
		return toString(format, context);
	}


	/**
	 * Returns a string representation of this item in the given context.
	 * @param context
	 * @return a String
	 */
	public String toString(InstantiationContext context)
	{
		return toString(ItemFormat.MINIMALIST, context);
	}


	/**
	 * Returns a string representation of this item with given format in given context.
	 * @param format
	 * @param context
	 * @return a String
	 */
	public String toString(ItemFormat format, InstantiationContext context)
	{
		StringBuilder ret = new StringBuilder();

		if (format.isShowId())
			ret.append(format.isExplicit() ? "id: " : "").append(id).append(" ");
		if (format.isShowLemmas())
		{
			ret.append(format.isExplicit() ? "lemmas: " : "").append(Utils.print(tree.getLemmas(), " ", "[", "]"));
			;
			if (format.isFullLemmas())
				ret.append("\n\t").append(Lemma.printLemmas(tree.getLemmas(), context, "\n\t", true));
			ret.append(format.isMultiline() ? "\n\t" : " ");
		}
		if (format.isShowTree())
		{
			ret.append(format.isExplicit() ? "tree: " : "");
			if (format.getNodeFormat().isIndented())
				ret.append("\n");
			ret.append(tree.toString(format.getNodeFormat(), context));
			ret.append(format.isMultiline() ? "\n\t" : " ");
		}
		if (format.isShowDerivation())
			ret.append(format.isExplicit() ? "derivation: " : "").append(derivation).append(format.isMultiline() ? "\n\t" : " ");
		if (format.isShowSemantics())
			ret.append(format.isExplicit() ? "semantics: " : "").append(semantics.toString(context)).append(format.isMultiline() ? "\n\t" : " ");
		if (format.isShowContext())
			ret.append(format.isExplicit() ? "context: " : "").append(context);
		return ret.toString().trim();
	}


	@Override
	public void instantiate(InstantiationContext context)
	{
		// does nothing
	}


	/**
	 * Returns a String built with the lemmas of this item.
	 * @return a String in which lemmas are separated by space.
	 */
	public String getLemmaString()
	{
		return Utils.print(getLemmas(), " ", "[", "]");
	}


	/**
	 * @return the parentItemSource
	 */
	public JeniChartItem getParentItemSource()
	{
		return parentItemSource;
	}


	/**
	 * @return the parentItemTarget
	 */
	public JeniChartItem getParentItemTarget()
	{
		return parentItemTarget;
	}


	/**
	 * @return the parentOperationType
	 */
	public DerivationNodeType getParentOperationType()
	{
		return parentOperationType;
	}


	@Override
	public List<Lemma> getLemmas()
	{
		return tree.getLemmas();
	}


	public float getProbability() {
		return probability;
	}


	public void setProbability(float probability) {
		this.probability = probability;
	}

	public JeniRealization getRealization() {
		JeniRealization real = new JeniRealization(getDerivation(), getContext());
		real.setProbability(getProbability());
		real.addAll(getTree().getLemmas());
		return real;
	}

	public void addUsedItemsID(ArrayList<String> usedItemsID) {
		this.usedItemsID.addAll(usedItemsID);
	}
	
	public void addUsedItemsID(String usedItemsID) {
		this.usedItemsID.add(usedItemsID);
	}
	
	public ArrayList<String> getUsedItemsID() {
		return usedItemsID;
	}
}
