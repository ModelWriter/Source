package synalp.generation.jeni.semantics;

import java.util.*;

import synalp.commons.grammar.*;
import synalp.commons.input.Lemma;
import synalp.commons.lexicon.*;
import synalp.commons.semantics.Semantics;
import synalp.commons.unification.*;


/**
 * Creates automatically the semantics of a grammar according to trace semantics, which are a way to
 * associate a semantics to a given trace. The association is based on a naming convention for
 * variables, ?F is the foot, ?R is the root, ?A is the anchor, ?S0..?Sn are substitutions.
 * @author Alexandre Denis
 */
public class SemanticsBuilder
{
	private FamiliesSemantics familiesSemantics;


	/**
	 * Creates a new SemanticsBuilder based on given FamilySemantics.
	 * @param familiesSemantics
	 */
	public SemanticsBuilder(FamiliesSemantics familiesSemantics)
	{
		this.familiesSemantics = familiesSemantics;
	}


	/**
	 * Sets the semantics of all the entries of the given grammar.
	 * @param grammar
	 */
	public void setSemantics(Grammar grammar)
	{
		for(GrammarEntry entry : grammar.values())
			setSemantics(entry);
	}


	/**
	 * Creates a SyntacticLexicon from families semantics and given Grammar.
	 * It iterates through families, and for each pair lemma/tree, creates a lexicon entry.
	 * @param grammar
	 * @return a new SyntacticLexicon
	 */
	public SyntacticLexicon createLexicon(Grammar grammar)
	{
		SyntacticLexicon ret = new SyntacticLexicon();
		for(FamilySemantics famSem : familiesSemantics)
		{
			if (famSem.getLemmas().isEmpty())
				continue;

			for(GrammarEntry entry : grammar.getEntriesContainingTrace(famSem.getTrace()))
			{
				// this is necessary to retrieve family-wide anchoring equations on families without explicit lemmas
				Equations familyWideEquations = new Equations();
				for(FamilySemantics otherSem : familiesSemantics)
					if (otherSem.getTrace().isSubTrace(entry.getTrace()))
						familyWideEquations.addAll(otherSem.getFamilyEquations());
				
				for(Lemma lemma : famSem.getLemmas().keySet())
				{
					// filter out entries with mismatching coanchors
					if (entry.getTree().getUninstantiatedCoanchors().size() != famSem.getCoanchors(lemma).size())
						continue;

					SyntacticLexiconEntry lexEntry = new SyntacticLexiconEntry();
					lexEntry.setFamilies(entry.getFamily());
					lexEntry.setLemma(lemma);

					// set filter
					FeatureStructure filter = new FeatureStructure();
					filter.addConstantFeature("family", new FeatureConstant(entry.getTrace()));
					lexEntry.setFilter(new Filter(filter));

					/* 
					 * set instantiated semantics, if the fam sem is a pattern use this semantics
					 * else use normally the semantics of the entry 
					 */
					Semantics lexSemantics = new Semantics(famSem.isPattern() ? famSem.getSemantics() : entry.getSemantics());
					lexSemantics.instantiate(famSem.getLemmas().get(lemma));
					lexEntry.setSemantics(lexSemantics);

					/*
					 * set all equations: the family-wide equations, the equations for the particular lemma and the co-anchor equations. 
					 * Note that the family equations are only set for famSem that have lemmas!
					 */
					lexEntry.getEquations().addAll(familyWideEquations);
					lexEntry.getEquations().addAll(famSem.getEquations(lemma));
					setCoanchorEquations(lexEntry, entry, famSem, lemma);

					ret.add(lexEntry);
				}
			}
		}

		return ret;
	}


	/**
	 * Adds co-anchor equations to the given lexicon entry. It retrieves the node id according to
	 * the order in which they appear. Maybe here we have to take the canonical tree to assign the
	 * coanchors. Or it is possible specify that in the input format.
	 * @param lexEntry
	 * @param entry
	 * @param famSem
	 * @param lemma
	 */
	private void setCoanchorEquations(SyntacticLexiconEntry lexEntry, GrammarEntry entry, FamilySemantics famSem, Lemma lemma)
	{
		List<Lemma> coanchors = famSem.getCoanchors(lemma);
		if (!coanchors.isEmpty())
		{
			List<Node> coanchorNodes = entry.getTree().getNodesByType(NodeType.COANCHOR);
			for(int i = 0; i < coanchors.size(); i++)
			{
				Lemma coanchor = coanchors.get(i);
				Node coanchorNode = coanchorNodes.get(i);
				FeatureStructure fs = new FeatureStructure().addConstantFeature("lemma", coanchor.getValue());
				lexEntry.getEquations().add(new Equation(coanchorNode.getId(), FeatureStructureType.TOP, fs, true));
			}
		}
	}


	/**
	 * Sets the semantics of the given entry automatically. This method clears the semantics of the
	 * entry, sets its identifiers and defines its semantics by the most specific families semantics
	 * whose trace match the entry trace.
	 * @param entry
	 */
	private void setSemantics(GrammarEntry entry)
	{
		clearSemantics(entry);
		setIdentifiers(entry);

		// first get all fam sem that are contained in the entry trace except for patterns
		List<FamilySemantics> matched = new ArrayList<FamilySemantics>();
		for(FamilySemantics famSem : familiesSemantics)
			if (!famSem.isPattern() && famSem.getTrace().isSubTrace(entry.getTrace()))
				matched.add(famSem);

		// removed fam sems that are subtraces of each other (keep the most specific)
		List<FamilySemantics> kept = new ArrayList<FamilySemantics>();
		for(FamilySemantics famSem1 : matched)
		{
			boolean found = false;
			for(FamilySemantics famSem2 : matched)
				if (famSem1 != famSem2 && famSem1.getTrace().isSubTrace(famSem2.getTrace()))
				{
					found = true;
					break;
				}
			if (!found)
				kept.add(famSem1);
		}

		// now gathers all kept trace sem in the semantics
		Semantics semantics = new Semantics();
		for(FamilySemantics famSem : kept)
			semantics.addAll(famSem.getSemantics());

		entry.setSemantics(semantics);
	}


	/**
	 * Sets the identifiers of the given entry.
	 * @param entry
	 */
	public static void setIdentifiers(GrammarEntry entry)
	{
		int n = 0;
		boolean isAuxiliary = entry.getTree().isAuxiliary();
		for(Node node : entry.getTree().getNodes())
			if (node.isRoot())
			{
				if (isAuxiliary)
					setIdentifier(node, "?F"); // by default the root idx is the foot idx
				else setIdentifier(node, "?A"); // by default the root idx is the anchor idx
			}
			else if (node.isAnchor())
			{
				setIdentifier(node, "?A");
				setLemma(node);
			}
			else if (node.isFoot())
				setIdentifier(node, "?F");
			else if (node.isSubst())
				setIdentifier(node, "?S" + (n++));
	}


	/**
	 * Sets a feature lemma=?L
	 * @param node
	 */
	private static void setLemma(Node node)
	{
		node.getFsTop().addVariableFeature("lemma", "?L");
		node.getFsBot().addVariableFeature("lemma", "?L");
	}


	private static void setIdentifier(Node node, String var)
	{
		node.getFsTop().addVariableFeature("idx", var);
		node.getFsBot().addVariableFeature("idx", var);
	}


	/**
	 * Clears the semantics field of the given entry and all features in all nodes whose name is
	 * "idx".
	 * @param entry
	 */
	public static void clearSemantics(GrammarEntry entry)
	{
		entry.setSemantics(new Semantics());
		for(Node node : entry.getTree().getNodes())
		{
			node.getFsTop().removeFeature("idx");
			node.getFsBot().removeFeature("idx");
		}
	}
}
