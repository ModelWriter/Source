package synalp.generation.jeni;

import java.util.*;

import org.apache.log4j.Logger;

import synalp.commons.derivations.*;
import synalp.commons.grammar.*;
import synalp.commons.semantics.*;
import synalp.commons.unification.*;
import synalp.generation.configuration.GeneratorOption;
import synalp.generation.probabilistic.ProbabilityStrategy;
import synalp.parsing.utils.WordOrder;

/**
 * A TreeCombiner combines trees according to substitution and adjunction.
 * @author Alexandre Denis
 */
public class TreeCombiner
{
	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(TreeCombiner.class);


	/**
	 * A simple data structure to retrieve both the newly tree, the operation and the gorn address
	 * and ease the creation of derivation nodes.
	 * @author Alexandre Denis
	 */
	private class TreeOperation
	{
		public Tree derivedTree;
		public GornAddress address;
		public DerivationNodeType type;


		/**
		 * @param type
		 * @param derivedTree
		 * @param address
		 */
		public TreeOperation(DerivationNodeType type, Tree derivedTree, GornAddress address)
		{
			this.type = type;
			this.address = address;
			this.derivedTree = derivedTree;
		}
	}

	private ProbabilityStrategy probabilityStrategy;


	/**
	 * Creates a default TreeCombiner. By default, all items that are created will have
	 * probability=1.
	 */
	public TreeCombiner()
	{
		probabilityStrategy = p -> 1;
	}


	/**
	 * Creates a TreeCombiner that will use the given probability strategy to compute the
	 * probability of new items.
	 * @param probabilityStrategy
	 */
	public TreeCombiner(ProbabilityStrategy probabilityStrategy)
	{
		this.probabilityStrategy = probabilityStrategy;
	}


///////// SUBSTITUTIONS	/////////

	/**
	 * Returns all combinations by substitutions of item1 and item2. This method does not test early
	 * semantic failure at all.
	 * @param item1
	 * @param item2
	 * @return items resulting from combinations
	 */
	public List<JeniChartItem> getSubstitutionCombinations(JeniChartItem item1, JeniChartItem item2)
	{
		return getSubstitutionCombinations(item1, item2, false, null);
	}


	/**
	 * Returns all combinations by substitutions of item1 and item2.
	 * @param item1
	 * @param item2
	 * @param allItems all items of agenda, chart and auxiliary tree, required to test the early
	 *            semantic failure if the option GeneratorOption.EARLY_SEMANTIC_FAILURE is set to
	 *            true.
	 * @return items resulting from combinations
	 */
	public List<JeniChartItem> getSubstitutionCombinations(JeniChartItem item1, JeniChartItem item2, JeniChartItems allItems)
	{
		return getSubstitutionCombinations(item1, item2, GeneratorOption.EARLY_SEMANTIC_FAILURE, allItems);
	}


	/**
	 * Returns all combinations by substitutions of item1 and item2.
	 * @param item1
	 * @param item2
	 * @param useEarlyFailure, if true tests early semantic failure
	 * @param allItems all items of agenda, chart and auxiliary tree, required to test the early
	 *            semantic failure
	 * @return items resulting from combinations
	 */
	private List<JeniChartItem> getSubstitutionCombinations(JeniChartItem item1, JeniChartItem item2, boolean useEarlyFailure, JeniChartItems allItems)
	{
		List<JeniChartItem> ret = new ArrayList<JeniChartItem>();
		boolean subItem1InItem2 = couldSubstitute(item1, item2);
		boolean subItem2InItem1 = couldSubstitute(item2, item1);

		if (!subItem1InItem2 && !subItem2InItem1)
		{
			logSubstitutionImpossible(item1, item2);
			logSubstitutionImpossible(item2, item1);
			return ret;
		}

		InstantiationContext context = JeniChartItem.combineContexts(item1, item2);
		if (item1.getSemantics().semanticsIntersects(item2.getSemantics(), context))
		{
			logSemanticOverlap(item1, item2, context);
			return ret;
		}
		else
		{
			if (subItem1InItem2)
				ret.addAll(getSubstitutionCombinations(item1, item2, context, useEarlyFailure, allItems));
			if (subItem2InItem1)
				ret.addAll(getSubstitutionCombinations(item2, item1, context, useEarlyFailure, allItems));
			return ret;
		}
	}

	
	/**
	 * Returns all combinations by substitutions of item1 and item2 in context of parsing 
	 * (i.e. performs wordrdercheck instead of semantics intersection while combining trees). Therefore, 
	 * this method does not test early semantic failure at all.
	 * @param item1
	 * @param item2
	 * @return items resulting from combinations
	 */
	public List<JeniChartItem> getSubstitutionCombinationsforParsing(JeniChartItem item1, JeniChartItem item2)
	{
		List<JeniChartItem> ret = new ArrayList<JeniChartItem>();
		boolean subItem1InItem2 = couldSubstitute(item1, item2);
		boolean subItem2InItem1 = couldSubstitute(item2, item1);

		if (!subItem1InItem2 && !subItem2InItem1)
		{
			logSubstitutionImpossible(item1, item2);
			logSubstitutionImpossible(item2, item1);
			return ret;
		}

		InstantiationContext context = JeniChartItem.combineContexts(item1, item2);
		if (subItem1InItem2)
			if (WordOrder.containsAny(WordOrder.getLemmaNodesIndex(item2.getTree()), WordOrder.getLemmaNodesIndex(item1.getTree()))) {
				return ret;
			}
			else
				ret.addAll(getSubstitutionCombinations(item1, item2, context, false, null));
		if (subItem2InItem1)
			if (WordOrder.containsAny(WordOrder.getLemmaNodesIndex(item1.getTree()), WordOrder.getLemmaNodesIndex(item2.getTree()))) {
				return ret;
			}
			else
				ret.addAll(getSubstitutionCombinations(item2, item1, context, false, null));
		return ret;
	}

	/**
	 * Tests whether it is possible to substitute item1 into item2.
	 * @param item1
	 * @param item2
	 * @return true if it is maybe possible to substitute item1 into item2, false if it is certain
	 *         that it is not possible
	 */
	private boolean couldSubstitute(JeniChartItem item1, JeniChartItem item2)
	{
		Tree tree1 = item1.getTree();
		if (tree1.isAuxiliary())
			return false;

		FeatureConstant cat = tree1.getRoot().getCategory();
		for(Node node : item2.getTree().getSubstitutions())
			if (node.hasCategory(cat))
				return true;

		return false;
	}


	/**
	 * Tries to substitute tree1 in one of the substitution node of tree2. Note that this method is
	 * particularly not optimized since we iterate through the tree two times, once for getting the
	 * substitutions nodes, and once to build the address of these nodes. We could do both at the
	 * same time in the future.
	 * @param item1
	 * @param item2
	 * @param context
	 * @param allItems all items of agenda, chart and auxiliary tree, required to test the early
	 *            semantic failure
	 * @return all the new trees that may be built by such substitution
	 */
	private List<JeniChartItem> getSubstitutionCombinations(JeniChartItem item1, JeniChartItem item2, InstantiationContext context, boolean useEarlyFailure,
			JeniChartItems allItems)
	{
		logSubstitutionTest(item1, item2, context);

		Tree tree1 = item1.getTree();
		Tree tree2 = item2.getTree();

		Semantics joinedSemantics;
		if (GeneratorOption.USE_BIT_SEMANTICS)
			joinedSemantics = new BitSemantics((BitSemantics) item1.getSemantics()).join((BitSemantics) item2.getSemantics());
		else joinedSemantics = new Semantics(item1.getSemantics(), item2.getSemantics());

		List<JeniChartItem> ret = new ArrayList<JeniChartItem>();
		FeatureConstant cat = tree1.getRoot().getCategory();

		// the whole substitution algorithm can be simplified to prevent doing all possible combinations
		// by imposing a strict order on the operations: it is necessary to fill the substitutions in
		// the order they appear in the tree, hence we can only test the first found substitution node
		// this was our previous EARLY_SUCCESS global option, which is now acknowledged to be mandatory

		List<Node> substNodes = tree2.getSubstitutions();
		if (substNodes.isEmpty())
			return ret;

		Node substNode = substNodes.get(0);
		if (!substNode.hasCategory(cat))
			return ret;

		InstantiationContext newContext = new InstantiationContext(context);
		FeatureStructure top = Unifier.unify(tree1.getRoot().getFsTop(), substNode.getFsTop(), newContext);
		if (top == null)
		{
			logCandidateNodeUnificationFailure("top", tree1.getRoot().getFsTop(), substNode.getFsTop(), newContext);
			return ret;
		}

		// note that this unification is not standard, but needs to be done in practice
		FeatureStructure bot = Unifier.unify(tree1.getRoot().getFsBot(), substNode.getFsBot(), newContext);
		if (bot == null)
		{
			logCandidateNodeUnificationFailure("bot", tree1.getRoot().getFsBot(), substNode.getFsBot(), newContext);
			return ret;
		}

		// the early semantic failure for substitution only works if we can guarantee that there is no auxiliary tree that is able to alter the idx features
		if (useEarlyFailure)
			if (!unifyIdx(top, bot, newContext))
			{
				JeniChartItem alteringItem = allItems.getIdxAlteringItem(cat);

				if (alteringItem == null)
				{
					logUnifyIdxFailureSubst(item1, item2, top, bot, newContext);
					return ret;
				}
				else logUnifyIdxFailureImpossible(item1, item2, alteringItem, newContext);
			}
			else logUnifyIdxSuccess(item1, item2, newContext);

		TreeOperation treeOperation = createSubstitution(substNode, item2, item1, top, bot);
		DerivationTree derivationTree = createDerivationTree(treeOperation, item1, item2);
		JeniChartItem newItem = new JeniChartItem(treeOperation.derivedTree, joinedSemantics, newContext, derivationTree, DerivationNodeType.SUBSTITUTION,
													item1, item2);

		renameCoIndexedVariable(newItem, top, substNode.getFsTop());
		renameCoIndexedVariable(newItem, bot, substNode.getFsBot());

		newItem.setProbability(probabilityStrategy.getProbability(newItem));
		
		ret.add(newItem);
		logSubstitutionSuccess(item1, item2, newItem, newContext);

		return ret;
	}


	/**
	 * Creates a new tree by replacing the target node in the target tree by the source tree root.
	 * @param targetNode a substitution node in the target tree
	 * @param targetItem the item of the target tree
	 * @param sourceItem the item whose tree whose root will be inserted as a substitution
	 * @param top the unified feature structure of the new node
	 * @param bot the unified feature structure of the new node
	 * @return a new TreeOperation gathering the result of the substitution
	 */
	private TreeOperation createSubstitution(Node targetNode, JeniChartItem targetItem, JeniChartItem sourceItem, FeatureStructure top, FeatureStructure bot)
	{
		Tree targetTree = targetItem.getTree();
		Tree sourceTree = sourceItem.getTree();

		Tree ret = new Tree(targetTree);
		ret.setId(sourceItem.getId() + "+" + targetItem.getId());

		Node root = new Tree(sourceTree).getRoot().setFsTop(top).setFsBot(bot);
		GornAddress targetAddress = GornAddress.getAddress(targetNode, targetTree);
		Node nodeToReplace = ret.getNode(targetAddress);
		nodeToReplace.getParent().replaceChild(nodeToReplace, root);

		return new TreeOperation(DerivationNodeType.SUBSTITUTION, ret, targetAddress);
	}


	/**
	 * Renames all occurrences of target variables that appear to be co-indexed in source.
	 * @param targetItem
	 * @param fsSource
	 * @param fsTarget
	 */
	private static void renameCoIndexedVariable(JeniChartItem targetItem, FeatureStructure fsSource, FeatureStructure fsTarget)
	{
		for(Feature feat : fsSource.getFeatures())
		{
			if (feat.getValue() instanceof FeatureVariable)
			{
				FeatureValue otherValue = fsTarget.getValueOf(feat.getName());
				if (otherValue != null && !otherValue.equals(feat.getValue()) && otherValue instanceof FeatureVariable)
					targetItem.replaceVariable((FeatureVariable) otherValue, new FeatureVariable(((FeatureVariable) feat.getValue()).getName()));
			}
		}
	}


///////// ADJUNCTIONS /////////

	/**
	 * Returns all combinations by adjunctions of item1 and item2.
	 * @param item1
	 * @param item2
	 * @return items resulting from combinations
	 */
	public List<JeniChartItem> getAdjunctionCombinations(JeniChartItem item1, JeniChartItem item2)
	{
		List<JeniChartItem> ret = new ArrayList<JeniChartItem>();
		boolean adjItem1OnItem2 = couldAdjunct(item1, item2);
		boolean adjItem2OnItem1 = couldAdjunct(item2, item1);

		if (!adjItem1OnItem2 && !adjItem2OnItem1)
		{
			logAdjunctionImpossible(item1, item2);
			logAdjunctionImpossible(item2, item1);
			return ret;
		}

		InstantiationContext context = JeniChartItem.combineContexts(item1, item2);
		if (item1.getSemantics().semanticsIntersects(item2.getSemantics(), context))
		{
			logSemanticOverlap(item1, item2, context);
			return ret;
		}
		else
		{
			if (adjItem1OnItem2)
				ret.addAll(getAdjunctionCombinations(item1, item2, context));
			if (adjItem2OnItem1)
				ret.addAll(getAdjunctionCombinations(item2, item1, context));
			return ret;
		}
	}


	/**
	 * Returns all combinations by adjunctions of item1 and item2 in context of parsing. The tree combination task is based on word order check and not semantics intersection.  
	 * @param item1
	 * @param item2
	 * @return items resulting from combinations
	 */
	public List<JeniChartItem> getAdjunctionCombinationsforParsing(JeniChartItem item1, JeniChartItem item2)
	{
		List<JeniChartItem> ret = new ArrayList<JeniChartItem>();
		boolean adjItem1OnItem2 = couldAdjunct(item1, item2);
		boolean adjItem2OnItem1 = couldAdjunct(item2, item1);

		if (!adjItem1OnItem2 && !adjItem2OnItem1)
		{
			logAdjunctionImpossible(item1, item2);
			logAdjunctionImpossible(item2, item1);
			ret.add(item2); //item2 is the item in the chart.
			return ret;
		}

		InstantiationContext context = JeniChartItem.combineContexts(item1, item2);
		if (adjItem1OnItem2)
			if (WordOrder.containsAny(WordOrder.getLemmaNodesIndex(item2.getTree()), WordOrder.getLemmaNodesIndex(item1.getTree()))) {
				return ret;
			}
			else
				ret.addAll(getAdjunctionCombinations(item1, item2, context));
		if (adjItem2OnItem1)
			if (WordOrder.containsAny(WordOrder.getLemmaNodesIndex(item1.getTree()), WordOrder.getLemmaNodesIndex(item2.getTree()))) {
				return ret;
			}
			else
				ret.addAll(getAdjunctionCombinations(item2, item1, context));
		return ret;
	}
	
	/**
	 * Tests whether it is possible to adjunct item1 onto a node of item2.
	 * @param item1 the auxiliary item
	 * @param item2
	 * @return true if it is maybe possible to adjunct item1 onto item2, false if it is certain that
	 *         it is not possible
	 */
	private boolean couldAdjunct(JeniChartItem item1, JeniChartItem item2)
	{
		Tree tree1 = item1.getTree();
		if (!tree1.isAuxiliary())
			return false;

		FeatureConstant cat = tree1.getRoot().getCategory();
		for(Node node : item2.getTree().getNodes())
			if (!node.isNoAdjunction() && node.hasCategory(cat))
				return true;
		return false;
	}


	/**
	 * Tries to adjunct the tree of item1 onto a node of the tree of item2.
	 * @param item1 the auxiliary item
	 * @param item2
	 * @param context
	 * @return a list of new trees
	 */
	private List<JeniChartItem> getAdjunctionCombinations(JeniChartItem item1, JeniChartItem item2, InstantiationContext context)
	{
		logAdjunctionTest(item1, item2, context);

		Tree tree1 = item1.getTree();
		Tree tree2 = item2.getTree();
		Semantics joinedSemantics;

		if (GeneratorOption.USE_BIT_SEMANTICS)
			joinedSemantics = new BitSemantics((BitSemantics) item1.getSemantics()).join((BitSemantics) item2.getSemantics());
		else joinedSemantics = new Semantics(item1.getSemantics(), item2.getSemantics());

		List<JeniChartItem> ret = new ArrayList<JeniChartItem>();

		Node foot = tree1.getFoot();
		if (foot == null)
			return ret;

		FeatureConstant cat = foot.getCategory();
		Node auxRoot = tree1.getRoot();
		for(Node node : tree2.getNodes())
			if (!node.isNoAdjunction() && node.hasCategory(cat))
			{
				InstantiationContext newContext = new InstantiationContext(context);

				FeatureStructure top = Unifier.unify(node.getFsTop(), auxRoot.getFsTop(), newContext);
				if (top == null)
				{
					logAdjunctionFailureTop(item2, item1, node, newContext);
					continue;
				}

				FeatureStructure bot = Unifier.unify(node.getFsBot(), foot.getFsBot(), newContext);
				if (bot == null)
				{
					logAdjunctionFailureBot(item2, item1, node, newContext);
					continue;
				}

				// this is an additional unification that is meant to prevent several adjunctions on the previous foot
				bot = Unifier.unify(auxRoot.getFsTop(), bot, newContext);
				if (bot == null)
				{
					logAdjunctionFailureBot(item2, item1, node, newContext);
					continue;
				}

				if (GeneratorOption.EARLY_SEMANTIC_FAILURE)
					if (!unifyIdx(top, auxRoot.getFsBot(), newContext) || !unifyIdx(foot.getFsTop(), bot, newContext))
					{
						logUnifyIdxFailureAdj(item2, item1, node, top, bot, newContext);
						continue;
					}

				TreeOperation treeOperation = createAdjunction(foot, item1, node, item2, top, bot);
				DerivationTree derivationTree = createDerivationTree(treeOperation, item1, item2);

				JeniChartItem newItem = new JeniChartItem(treeOperation.derivedTree, joinedSemantics, newContext, derivationTree,
															DerivationNodeType.ADJUNCTION, item1, item2);
				ret.add(newItem);

				renameCoIndexedVariable(newItem, top, auxRoot.getFsTop());
				renameCoIndexedVariable(newItem, bot, foot.getFsBot());
				
				newItem.setProbability(probabilityStrategy.getProbability(newItem));

				logAdjunctionSuccess(item2, item1, newItem, newContext);

				//if (EARLY_SUCCESS)
				//	return ret;
			}

		return ret;
	}


	/**
	 * Creates a new tree by replacing the target node in the target tree by the whole auxiliary
	 * tree.
	 * @param foot the foot node of the auxiliary tree
	 * @param auxItem an item with an auxiliary tree (with the foot node)
	 * @param targetNode the node in the target tree onto which the adjunction is performed
	 * @param targetItem the item of the tree containing the target node
	 * @param top the new top fs
	 * @param bot the new bot fs
	 * @return a new TreeOperation gathering the result of the adjunction
	 */
	private TreeOperation createAdjunction(Node foot, JeniChartItem auxItem, Node targetNode, JeniChartItem targetItem, FeatureStructure top,
			FeatureStructure bot)
	{
		Tree targetTree = targetItem.getTree();
		Tree auxTree = auxItem.getTree();

		Tree newAux = new Tree(auxTree);
		Node newRoot = newAux.getRoot().setFsTop(top);
		Node newFoot = newAux.getNode(GornAddress.getAddress(foot, auxTree));

		newFoot.setFsBot(bot);
		newFoot.setFsTop(bot); // it is not an error since we unified top/bot of the foot node
		newFoot.setType(targetNode.getType());
		newFoot.setNoAdjunction(true);
		newFoot.setAnchorLemma(targetNode.getAnchorLemma());
		// Added by Bikash for piggybacking the node index (in parsing)
		if (targetNode.getNodeIndex()!=null) { // sometimes the targetNode may not be a lemma node ??
			newFoot.setNodeIndex(targetNode.getNodeIndex());
		}

		Tree ret = new Tree(targetTree);
		ret.setId(auxItem.getId() + "+" + targetItem.getId());

		GornAddress targetAddress = GornAddress.getAddress(targetNode, targetTree);
		Node nodeToReplace = ret.getNode(targetAddress);

		if (nodeToReplace.getParent() == null)
			ret.setRoot(newRoot);
		else nodeToReplace.getParent().replaceChild(nodeToReplace, newRoot);

		for(Node child : nodeToReplace.getChildren())
			newFoot.addChild(child);

		return new TreeOperation(DerivationNodeType.ADJUNCTION, ret, targetAddress);
	}


///////// UTILS	/////////

	/**
	 * Returns true if the idx feature of fs1 unifies with the idx feature of fs2 in the given
	 * context.
	 * @param fs1
	 * @param fs2
	 * @param context
	 * @return false if the idx features exist and do not unify
	 */
	private boolean unifyIdx(FeatureStructure fs1, FeatureStructure fs2, InstantiationContext context)
	{
		FeatureValue v1 = fs1.getValueOf("idx");
		FeatureValue v2 = fs2.getValueOf("idx");
		if (v1 == null && v2 == null)
			return true;
		else return Unifier.unify(v1, v2, context) != null;
		//return Unifier.unify(fs1.getValueOf("idx"), fs2.getValueOf("idx"), context) != null;
	}


	/**
	 * Creates a new DerivationTree given a TreeOperation, an argument and a target.
	 * @param operation
	 * @param argument
	 * @param target
	 * @return a new DerivationTree
	 */
	private DerivationTree createDerivationTree(TreeOperation operation, JeniChartItem argument, JeniChartItem target)
	{
		DerivationNode argNode = new DerivationNode(argument.getDerivation().getRoot());
		argNode.setType(operation.type);
		argNode.setAddress(operation.address);
		DerivationNode targetNode = new DerivationNode(target.getDerivation().getRoot());
		targetNode.addChild(argNode);

		return new DerivationTree(operation.derivedTree, targetNode);
	}


//// debug messages, they are put there to improve code readability

	private static void logSubstitutionImpossible(JeniChartItem item1, JeniChartItem item2)
	{
		if (logger.isTraceEnabled())
			logger.trace("Pre-substitution failure: unable to substitute " + item1 + " in " + item2);
	}


	private void logCandidateNodeUnificationFailure(String fs, FeatureStructure fs1, FeatureStructure fs2, InstantiationContext context)
	{
		if (logger.isTraceEnabled())
			logger.trace("Unification " + fs + " failure: " + fs1.toString(context) + " and " + fs2.toString(context));

	}


	private static void logAdjunctionImpossible(JeniChartItem item1, JeniChartItem item2)
	{
		if (logger.isTraceEnabled())
			logger.trace("Pre-adjunction failure: unable to adjunct " + item1 + " onto " + item2);
	}


	private static void logUnifyIdxFailureImpossible(JeniChartItem item1, JeniChartItem item2, JeniChartItem alteringItem, InstantiationContext context)
	{
		if (logger.isDebugEnabled())
			logger.debug("Despite idx clash, the substitution of " + item1.getId() + " into " + item2.getId() +
							" is possible because there exists an idx altering item: " +
							alteringItem.toString() + " context: " + context);
	}


	private static void logAdjunctionTest(JeniChartItem item1, JeniChartItem item2, InstantiationContext context)
	{
		if (logger.isDebugEnabled())
			logger.debug("Testing adjunction of " + item1 + " onto " + item2);
	}


	private static void logSubstitutionTest(JeniChartItem item1, JeniChartItem item2, InstantiationContext context)
	{
		if (logger.isDebugEnabled())
			logger.debug("Testing substitution of " + item1 + " into " + item2);
	}


	private static void logUnifyIdxFailureSubst(JeniChartItem item1, JeniChartItem item2, FeatureStructure top, FeatureStructure bot,
			InstantiationContext context)
	{
		if (logger.isDebugEnabled())
			logger.debug("Substitution of\n" + item1.getId() + ":\n" + item1.getTree().toString(context) +
							"\nin " + item2.getId() + ":\n" + item2.getTree().toString(context) + "\nfails because of EARLY SEMANTIC FAILURE:\ntop=" +
							top.toString(context) +
							" bot=" + bot.toString(context));
	}


	private static void logUnifyIdxFailureAdj(JeniChartItem item1, JeniChartItem item2, Node node, FeatureStructure top, FeatureStructure bot,
			InstantiationContext context)
	{
		if (logger.isDebugEnabled())
			logger.debug("Adjunction of\n" + item2.getId() + ":\n" + item2.getTree().toString(context) +
							"\nwith " + item1.getId() + ":\n" + item1.getTree().toString(context) + "\nnode " + node +
							"\nfails because of EARLY SEMANTIC FAILURE:\ntop=" + top.toString(context) +
							" bot=" + bot.toString(context));
	}


	private static void logUnifyIdxSuccess(JeniChartItem item1, JeniChartItem item2, InstantiationContext newContext)
	{
		if (logger.isDebugEnabled())
			logger.debug("The idx of " + item1.getId() + " and " + item2.getId() + " are perfectly compatible (EARLY SEMANTIC FAILURE)");
	}


	private static void logAdjunctionFailureTop(JeniChartItem item1, JeniChartItem item2, Node node, InstantiationContext context)
	{
		if (logger.isTraceEnabled())
			logger.trace("Adjunction of\n" + item2.getId() + ":\n" + item2.getTree().toString(context) +
							"\non " + item1.getId() + ":\n" + item1.getTree().toString(context) + "\nnode " + node +
							"\nfails because top fs do not unify: " + node.getFsTop().toString(context) +
							" and " + item2.getTree().getRoot().getFsTop().toString(context));
	}


	private static void logAdjunctionFailureBot(JeniChartItem item1, JeniChartItem item2, Node node, InstantiationContext context)
	{
		if (logger.isTraceEnabled())
			logger.trace("Adjunction of\n" + item2.toString(ItemFormat.COMPLETE_FULL_TREE, context) +
							"\nonto\n" + item1.toString(ItemFormat.COMPLETE_FULL_TREE, context) + ":\nfails because fs bot failure on node " + node);
		else if (logger.isDebugEnabled())
			logger.debug("Adjunction of\n" + item2.toString(context) +
							"\nonto (fs in merged context)\n" + item1.toString(context) + ":\nfails because fs bot failure on node " + node);
	}


	private static void logSemanticOverlap(JeniChartItem item1, JeniChartItem item2, InstantiationContext context)
	{
		if (logger.isTraceEnabled())
			logger.trace(item1.getId() + " " + item1.getLemmaString() + " does not combine with " + item2.getId() + " " + item2.getLemmaString() +
							" -> semantics overlap");
	}


	private static void logSubstitutionSuccess(JeniChartItem item1, JeniChartItem item2, JeniChartItem newItem, InstantiationContext context)
	{
		if (logger.isTraceEnabled())
			logger.trace("Substitution of\n" + item1.toString(ItemFormat.COMPLETE_FULL_TREE, context) +
							"\ninto\n" + item2.toString(ItemFormat.COMPLETE_FULL_TREE, context) + ":\nleads to new item:\n" +
							newItem.toString(ItemFormat.COMPLETE_FULL_TREE, context));
		else if (logger.isDebugEnabled())
			logger.debug("Substitution of\n" + item1.toString(context) +
							"\ninto\n" + item2.toString(context) + ":\nleads to new item:\n" + newItem.toString(context));
	}


	private static void logAdjunctionSuccess(JeniChartItem item1, JeniChartItem item2, JeniChartItem newItem, InstantiationContext context)
	{
		if (logger.isTraceEnabled())
			logger.trace("Adjunction of\n" + item2.toString(ItemFormat.COMPLETE_FULL_TREE, context) +
							"\nonto\n" + item1.toString(ItemFormat.COMPLETE_FULL_TREE, context) + ":\nleads to new item:\n" +
							newItem.toString(ItemFormat.COMPLETE_FULL_TREE, context));
		else if (logger.isDebugEnabled())
			logger.debug("Adjunction of\n" + item2.toString(context) +
							"\nonto\n" + item1.toString(context) + ":\nleads to new item:\n" + newItem.toString(context));
	}
}
