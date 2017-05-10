package synalp.commons.grammar;

import java.util.*;

import synalp.commons.input.Lemma;
import synalp.commons.lexicon.*;
import synalp.commons.semantics.Semantics;
import synalp.commons.unification.*;
import synalp.commons.utils.Utils;
import synalp.generation.configuration.GeneratorOption;


/**
 * A GrammarEntry is an entry in a grammar.
 * @author Alexandre Denis
 */
public class GrammarEntry implements UnifiableComponent
{
	private String name;
	private Tree tree;
	private Trace trace;
	private String family;
	private Semantics semantics;
	private InstantiationContext context;
	private FeatureStructure entryInterface;
	
	private float probability;


	/**
	 * Renames all variables of all given entries such that no entry share any variable.
	 * @param entries
	 */
	public static void renameVariables(Collection<GrammarEntry> entries)
	{
		for(GrammarEntry entry : entries)
			for(FeatureVariable var : entry.getAllVariables())
				entry.replaceVariable(var, new FeatureVariable(UUID.randomUUID().toString()));
	}


	/**
	 * Displays a String representation of the given entries names. If the entry has an anchor add
	 * it.
	 * @param entries
	 * @return a String
	 */
	public static String toString(Collection<GrammarEntry> entries)
	{
		StringBuilder ret = new StringBuilder();
		for(GrammarEntry entry : entries)
		{
			ret.append(entry.toMiniString());
			ret.append(" ");
		}
		return ret.toString().trim();
	}


	/**
	 * Creates a new empty tree with given name.
	 * @param name
	 */
	public GrammarEntry(String name)
	{
		this.name = name;
		this.context = new InstantiationContext();
	}


	/**
	 * Deep copies this Grammar entry.
	 * @param entry
	 */
	public GrammarEntry(GrammarEntry entry)
	{
		this.name = entry.getName();
		this.family = entry.getFamily();
		this.tree = new Tree(entry.getTree());
		this.trace = new Trace(entry.getTrace());
		this.semantics = new Semantics(entry.getSemantics());
		this.context = new InstantiationContext(entry.getContext());
		this.entryInterface = new FeatureStructure(entry.getInterface());
		
		this.probability = entry.getProbability();
	}


	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 * @return a family name
	 */
	public final String getFamily()
	{
		return family;
	}


	/**
	 * Sets the family name of the entry.
	 * @param family
	 */
	public void setFamily(String family)
	{
		this.family = family;
	}


	/**
	 * @return the semantics of the entry
	 */
	public Semantics getSemantics()
	{
		return semantics;
	}


	/**
	 * @param semantics
	 */
	public void setSemantics(Semantics semantics)
	{
		this.semantics = semantics;
	}


	/**
	 * Returns the instantiated semantics of the entry. It creates a new semantics from the entry
	 * semantics and instantiates it (resolve variables) in the entry current context.
	 * @return a new Semantics
	 */
	public Semantics getInstantiatedSemantics()
	{
		Semantics ret = new Semantics(semantics);
		semantics.instantiate(context);
		return ret;
	}


	/**
	 * @return the trace of the entry
	 */
	public final Trace getTrace()
	{
		return trace;
	}


	/**
	 * @param trace
	 */
	public void setTrace(Trace trace)
	{
		this.trace = trace;
	}


	/**
	 * @return the interface of the entry
	 */
	public final FeatureStructure getInterface()
	{
		return entryInterface;
	}


	/**
	 * @param fs
	 */
	public void setInterface(FeatureStructure fs)
	{
		entryInterface = fs;
	}


	/**
	 * @return a tree
	 */
	public Tree getTree()
	{
		return tree;
	}


	/**
	 * @param tree
	 */
	public void setTree(Tree tree)
	{
		this.tree = tree;
	}


	/**
	 * Sets the lemma of the main anchor of this entry's tree. If no main anchor node can be found,
	 * return false.
	 * @param lemma
	 * @return true if main anchor lemma has been set
	 */
	public boolean setMainAnchorLemma(Lemma lemma)
	{
		Node mainAnchor = getTree().getMainAnchor();
		if (mainAnchor == null)
			return false;
		else
		{
			mainAnchor.setAnchorLemma(lemma);
			return true;
		}
	}


	/**
	 * Returns the lemma of the main anchor if it exists.
	 * @return null if not found
	 */
	public Lemma getMainAnchorLemma()
	{
		Node mainAnchor = getTree().getMainAnchor();
		if (mainAnchor == null)
			return null;
		else return mainAnchor.getAnchorLemma();
	}


	/**
	 * Returns the surface form of the entry in terms of the anchored lemmas.
	 * @return the anchored lemmas of this entry's tree separated by space
	 */
	public String getLemmasSurface()
	{
		return Lemma.printLemmas(tree.getLemmas(), context, false);
	}


	/**
	 * Applies all given equations to the given entry. If one equation cannot be applied returns
	 * false without trying the other equations. Warning: if some equations can be applied they are
	 * still applied (to fix).
	 * @param equations
	 * @param overwrite if true, overwrites the features (see applyEquation(Equation, boolean)).
	 * @return true if all equations have been applied, false if one failed
	 */
	public boolean applyEquations(Equations equations, boolean overwrite)
	{
		for(Equation equation : equations)
			if (!applyEquation(equation, overwrite))
				return false;
		return true;
	}


	/**
	 * Applies the given equation to the given entry for a specified node. It also sets the lemma of
	 * the node if specified in the equation. Warning: each application if successful can modify the
	 * entry context.
	 * @param eq
	 * @param overwrite if overwrite is true, the unification is not tested and all the features of
	 *            the equations overwrite features of the node. It always returns true except if the
	 *            node cannot be found.
	 * @return false if the equation cannot be applied
	 */
	public boolean applyEquation(Equation eq, boolean overwrite)
	{
		// find node first
		String nodeId = eq.getNodeId();
		Node node = null;
		if (nodeId.equals("anchor"))
		{
			node = tree.getMainAnchor();
			if (node == null)
				return false;
		}
		else if (nodeId.equals("foot"))
		{
			node = tree.getFoot();
			if (node == null)
				return false;
		}
		else if (nodeId.equals("root"))
		{
			node = tree.getRoot();
			if (node == null)
				return false;
		}
		else
		{
			node = tree.getNodeById(nodeId);
			if (node == null)
			{
				if (!GeneratorOption.ALLOW_MISSING_COANCHORS)
					return false;
				else return true; // ignores the application but don't fail
			}
		}

		// we found it, now check the FS unification, return false if failed
		FeatureStructure eqFs = eq.getFeatureStructure();
		FeatureStructure nodeFs = node.getFs(eq.getType());

		if (overwrite)
		{
			for(Feature feat : eqFs.getFeatures())
			{
				String name = feat.getName();
				if (nodeFs.hasFeature(name))
				{
					Feature nodeFeat = nodeFs.getFeature(name);
					if (nodeFeat.getValue() instanceof FeatureConstant)
					{
						nodeFs.removeFeature(name);
						nodeFs.add(new Feature(feat));
					}
					else if (nodeFeat.getValue() instanceof FeatureVariable)
					{
						if (feat.getValue() instanceof FeatureConstant)
							context.put((FeatureVariable) nodeFeat.getValue(), feat.getValue().copy());
						else if (feat.getValue() instanceof FeatureVariable)
							context.put((FeatureVariable) nodeFeat.getValue(), context.getValue(feat.getValue()).copy());
					}
				}
				else nodeFs.add(new Feature(feat));
			}
			node.setFs(eq.getType(), nodeFs);
		}
		else
		{
			FeatureStructure result = Unifier.unify(nodeFs, eqFs, context);
			if (result == null)
				return false;
			else node.setFs(eq.getType(), result);

		}

		// eventually, if the equation specifies a lemma feature sets the lemma of the node
		setLemma(node, eqFs, context);

		return true;
	}


	/**
	 * Sets the lemma of the given Node considering a given value in the given context.
	 * @param node
	 * @param fs
	 * @param context
	 */
	private static void setLemma(Node node, FeatureStructure fs, InstantiationContext context)
	{
		Feature lemmaFeat = fs.getFeature("lemma");
		if (lemmaFeat != null)
		{
			FeatureValue val = context.getValue(lemmaFeat.getValue());
			node.setAnchorLemma(new Lemma(val.toString()));
		}
	}


	@Override
	public String toString()
	{
		return name + " " + tree.toString(context);
	}


	/**
	 * Returns a String representation of this entry with given NodeFormat.
	 * @param format
	 * @return a String
	 */
	public String toString(NodeFormat format)
	{
		return name + " " + tree.toString(format, context);
	}


	/**
	 * Returns a short one line String representation of this entry.
	 * @return a String
	 */
	public String toShortString()
	{
		return family + "." + tree.getId() + " " + tree.toString(context) + " " + semantics;
	}


	/**
	 * Returns a mini String representation of this entry.
	 * @return a String
	 */
	public String toMiniString()
	{
		return name + "." + Utils.print(tree.getLemmas(), " ", "[", "]");
	}


	/**
	 * Returns a full representation of this entry.
	 * @return a String
	 */
	public String toFullString()
	{
		return "id: " + name + "\nfamily: " + family + "\ntrace: " + trace + "\ninterface:" + entryInterface.toString(context) + "\ntree:" +
				tree.toString(NodeFormat.INDENTED, context) + "\nsemantics:" +
				semantics.toString(context) + "\ncontext:" + context;
	}


	/**
	 * @return the context
	 */
	public InstantiationContext getContext()
	{
		return context;
	}


	/**
	 * @param context
	 */
	public void setContext(InstantiationContext context)
	{
		this.context = context;
	}


	@Override
	public Set<FeatureVariable> getAllVariables()
	{
		Set<FeatureVariable> ret = new HashSet<FeatureVariable>();
		ret.addAll(tree.getAllVariables());
		ret.addAll(context.getAllVariables());
		ret.addAll(semantics.getAllVariables());
		ret.addAll(entryInterface.getAllVariables());
		return ret;
	}


	@Override
	public void replaceVariable(FeatureVariable variable, FeatureValue value)
	{
		tree.replaceVariable(variable, value);
		context.replaceVariable(variable, value);
		semantics.replaceVariable(variable, value);
		entryInterface.replaceVariable(variable, value);
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


	public float getProbability() {
		return probability;
	}


	public void setProbability(float probability) {
		this.probability = probability;
	}
}