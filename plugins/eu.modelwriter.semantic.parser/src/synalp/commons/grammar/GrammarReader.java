package synalp.commons.grammar;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import synalp.commons.input.Lemma;
import synalp.commons.semantics.*;
import synalp.commons.unification.*;
import synalp.generation.configuration.GeneratorOption;


/**
 * A GrammarReader reads a grammar in XML format.
 * @author Alexandre Denis
 */
public class GrammarReader extends DefaultHandler
{
	private static Logger logger = Logger.getLogger(GrammarReader.class);

	private static boolean warnedAboutLemanchor;

	/**
	 * Objects corresponding to node in the tree
	 */
	private Tree tree;
	private Trace trace;
	private String curTag;
	private Grammar grammar;
	private DefaultLiteral literal;
	private GrammarEntry entry;
	private Semantics semantics;
	private FeatureConstant disjunction;

	/**
	 * Stacks containing the objects parsed
	 */
	private Stack<Node> nodesStack;
	private Stack<Feature> featStack;
	private Stack<FeatureStructure> structStack;

	/**
	 * Fields sets to true when the current node is the one given in the name of the attribute
	 */
	private boolean inArg;
	private boolean inLabel;
	private boolean inLiteral;
	private boolean inPredicate;
	private boolean inInterface;
	private boolean inDisjunction;


	/**
	 * Demonstrates how to read a grammar.
	 * @param args
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void main(String[] args) throws SAXException, IOException
	{
		Grammar grammar = GrammarReader.readGrammar(new File("/home/laura/gitprojects/quelonli/icgen/source/icgen/data/model/../dist/build/grammar/valuations.xml"));
		for(GrammarEntry entry : grammar.values())
			System.out.println(entry.toFullString() + "\n");
		for(String family : grammar.getFamilies().keySet())
			System.out.println("family:'"+family+"' : "+grammar.getFamilies().get(family));
		System.out.println("Read " + grammar.size() + " entries");
	}


	/**
	 * Reads the given grammar.
	 * @param file
	 * @return a syntactic lexicon
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Grammar readGrammar(File file) throws SAXException, IOException
	{
		GrammarReader reader = new GrammarReader();
		try
		{
			logger.info("Reading grammar " + file);
			SAXParserFactory.newInstance().newSAXParser().parse(file, reader);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		return reader.grammar;
	}


	@Override
	public void endDocument() throws SAXException
	{
		postProcess(grammar);
		grammar.computeFamiliesCache();
	}


	/**
	 * Resolves the given entity. It prevents the retrieval of the grammar dtd.
	 */
	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException
	{
		return new InputSource(new StringReader(""));
	}


	@Override
	public void startDocument() throws SAXException
	{
		grammar = new Grammar();
		nodesStack = new Stack<Node>();
		featStack = new Stack<Feature>();
		structStack = new Stack<FeatureStructure>();
	}


	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		curTag = qName;
		if (qName.equals("entry"))
		{
			entry = createEntry(attributes);
			grammar.add(entry);
		}
		else if (qName.equals("trace"))
		{
			trace = new Trace();
			entry.setTrace(trace);
		}
		else if (qName.equals("tree"))
		{
			tree = createTree(attributes);
			entry.setTree(tree);
		}
		else if (qName.equals("semantics"))
		{
			semantics = new Semantics();
			entry.setSemantics(semantics);
		}
		else if (qName.equals("literal"))
		{
			inLiteral = true;
			literal = new DefaultLiteral();
			semantics.add(literal);
		}
		else if (qName.equals("vAlt"))
		{
			inDisjunction = true;
			disjunction = new FeatureConstant();
		}
		else if (qName.equals("sym"))
		{
			FeatureValue value = createFeatureValue(attributes);
			if (inDisjunction)
			{
				// we could be more cautious
				disjunction.addValue(value.toString());
			}
			else
			{
				if (!inLiteral)
					featStack.peek().setValue(value);
				else if (inLabel)
					literal.setLabel(value);
				else if (inArg)
					literal.addArgument(value);
				else if (inPredicate)
					literal.setPredicate(value);
			}
		}
		else if (qName.equals("node"))
			nodesStack.push(createNode(attributes));
		else if (qName.equals("f"))
			featStack.push(createFeature(attributes));
		else if (qName.equals("fs"))
			structStack.push(new FeatureStructure()); // do coref
		else if (qName.equals("interface"))
			inInterface = true;
		else if (qName.equals("label"))
			inLabel = true;
		else if (qName.equals("predicate"))
			inPredicate = true;
		else if (qName.equals("arg"))
			inArg = true;
	}


	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if (qName.equals("f"))
			structStack.peek().add(featStack.pop());
		else if (qName.equals("fs"))
		{
			// if there is no fs in the stack anymore, attach the fs either to the interface or to the current node, else attach it to the top feature
			FeatureStructure fs = structStack.pop();
			if (structStack.isEmpty())
			{
				if (inInterface)
					entry.setInterface(fs);
				else nodesStack.peek().setTopBotCat(fs);
			}
			else featStack.peek().setValue(fs);
		}
		else if (qName.equals("node"))
		{
			// when popping node, create parenthood or set tree root
			Node node = nodesStack.pop();
			if (node == null)
				throw new SAXException("Error: every <node> must match a closing </node>");
			if (!nodesStack.isEmpty())
				node.setParent(nodesStack.peek());
			else tree.setRoot(node);
		}
		else if (qName.equals("interface"))
			inInterface = false;
		else if (qName.equals("literal"))
			inLiteral = false;
		else if (qName.equals("label"))
			inLabel = false;
		else if (qName.equals("predicate"))
			inPredicate = false;
		else if (qName.equals("arg"))
			inArg = false;
		else if (qName.equals("vAlt"))
		{
			if (!inLiteral)
				featStack.peek().setValue(disjunction);
			else if (inLabel)
				literal.setLabel(disjunction);
			else if (inArg)
				literal.addArgument(disjunction);
			else if (inPredicate)
				literal.setPredicate(disjunction);
			inDisjunction = false;
			disjunction = null;
		}

		curTag = "";
	}


	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if (curTag.equals("family"))
			entry.setFamily(toString(ch, start, length));
		else if (curTag.equals("class"))
			trace.add(toString(ch, start, length));
	}


	/**
	 * Creates a FeatureValue.
	 * @param attributes
	 * @return a FeatureValue which can either be a FeatureConstant or a FeatureVariable
	 * @throws SAXException
	 */
	private FeatureValue createFeatureValue(Attributes attributes) throws SAXException
	{
		String value = attributes.getValue("value");
		if (value == null)
		{
			String varname = attributes.getValue("varname");
			if (varname == null)
				throw new SAXException("Error: a symbol in a fs must have either a 'value' or a 'varname' attribute");
			else return new FeatureVariable(varname);
		}
		else return new FeatureConstant(value);
	}


	/**
	 * Post process the grammar. It both rewrites lex nodes and lemanchor features.
	 * @param grammar
	 */
	private static void postProcess(Grammar grammar)
	{
		for(GrammarEntry entry : grammar.values())
		{
			for(Node node : entry.getTree().getNodes())
			{
				if (GeneratorOption.REWRITE_LEX_NODES && node.getType() == NodeType.LEX)
					rewriteLexNode(node, entry);

				if (GeneratorOption.REWRITE_LEMANCHOR)
					rewriteLemanchor(node);
			}

			if (GeneratorOption.ASSIGN_NODE_IDS)
			{
				List<Integer> a = new ArrayList<Integer>();
				a.add(0);
				assignNodeID(entry.getTree().getRoot(), a);
			}
		}
	}


	/**
	 * Assigns a node identifier to each node of a TAG tree. The node identifier is build as n.i
	 * where i is an integer incremented for each visited node. The tree is traversed in depth-first
	 * and the children are visited in the ordered they are entered in the grammar (if [a b c ] is
	 * the list of children then a is visited first, then b and c). The root node is i=0
	 * @param tree root node of a tree ({@link Node})
	 * @param a should be initialized with the single element 0 TODO: better way of doing this?
	 */
	private static void assignNodeID(Node tree, List<Integer> a)
	{
		if (tree.getId().isEmpty()) //is never null; empty by default!
			tree.setId("n" + a.get(0));
		for(Node n : tree.getChildren())
		{
			a.add(0, a.get(0) + 1);
			assignNodeID(n, a);
		}
	}


	/**
	 * Rewrites lex nodes as coanchors nodes. It does not do any merging with the parent node and
	 * simply rewrites a lex node as a COANCHOR. It forbids though any adjunction on the coanchor
	 * itself, adjunctions need to be performed at the parent level if needed.
	 * @param node
	 * @param entry
	 */
	private static void rewriteLexNode(Node node, GrammarEntry entry)
	{
		if (node.isPhonE())
			node.setType(NodeType.STD);
		else node.setType(NodeType.COANCHOR);
		node.setNoAdjunction(true);

		// the anchor lemma of the parent is the category of the child node, we take here the first value of the category if it exists
		if (node.getCategory() != null && !node.getCategory().getValues().isEmpty())
		{
			node.setAnchorLemma(new Lemma(node.getCategory().getFirstValue()), true);
			node.getFsTop().addConstantFeature("lemma", node.getCategory());
			node.getFsBot().addConstantFeature("lemma", node.getCategory());
		}

		if (node.getParent() != null)
			node.setCategory(node.getParent().getCategory());

		logger.warn("Warning: rewriting lex node for tree " + entry.getName() + " (lex nodes are now deprecated)");
	}


	/**
	 * Rewrites lex nodes as coanchors nodes by merging them to their parent. Note that it appears
	 * that parent lex nodes are not always terminal, hence this merging causes to have coanchors
	 * inside the tree. We may remove this method in the future.
	 * @param node
	 * @param entry
	 * @throws SAXException
	 */
	@SuppressWarnings("unused")
	private static void rewriteLexNodeParentMerge(Node node, GrammarEntry entry) throws SAXException
	{
		Node parent = node.getParent();

		if (node.isPhonE())
			parent.setType(NodeType.STD);
		else parent.setType(NodeType.COANCHOR);
		FeatureStructure newBot = Unifier.unify(node.getFsBot(), parent.getFsBot());
		FeatureStructure newTop = Unifier.unify(node.getFsTop(), parent.getFsTop());
		if (newBot == null)
			throw new SAXException("Error: unable to rewrite lex node as a coanchor node since its bot fs and its parent bot fs do not unify (tree " +
									entry.getName() +
									")");
		if (newTop == null)
			throw new SAXException("Error: unable to rewrite lex node as a coanchor node since its top fs and its parent top fs do not unify (tree " +
									entry.getName() +
									")");

		parent.setFsBot(newBot);
		parent.setFsTop(newTop);

		// the anchor lemma of the parent is the category of the child node, we take here the first value of the category if it exists
		if (node.getCategory() != null && !node.getCategory().getValues().isEmpty())
		{
			parent.setAnchorLemma(new Lemma(node.getCategory().getFirstValue()), true);
			parent.getFsTop().addConstantFeature("lemma", node.getCategory());
			parent.getFsBot().addConstantFeature("lemma", node.getCategory());
		}

		parent.removeChild(node);

		logger.warn("Warning: rewriting lex node for tree " + entry.getName() + " (lex nodes are now deprecated)");
	}


	/**
	 * Rewrites lemanchor features as "lemma", sets the node type to coanchor and also sets the
	 * actual lemma.
	 * @param node
	 */
	public static void rewriteLemanchor(Node node)
	{
		List<Feature> features = new ArrayList<Feature>();
		features.addAll(node.getFsTop().getAllFeaturesRecursively());
		features.addAll(node.getFsBot().getAllFeaturesRecursively());

		for(Feature feat : features)
		{
			if (feat.getName().equals("lemanchor"))
			{
				feat.setName("lemma");
				if (!warnedAboutLemanchor)
				{
					logger.warn("Warning: rewriting 'lemanchor' as 'lemma' ('lemanchor' is now deprecated)");
					warnedAboutLemanchor = true;
				}
			}

			if (feat.getName().equals("lemma"))
			{
				if (node.getType() != NodeType.COANCHOR && node.getType() != NodeType.ANCHOR)
					node.setType(NodeType.COANCHOR);
				node.setAnchorLemma(new Lemma(feat.getValue().toString()));
			}
		}
	}


	/**
	 * Creates a Node.
	 * @param attributes
	 * @return a new Node
	 * @throws SAXException
	 */
	private Node createNode(Attributes attributes) throws SAXException
	{
		String typeStr = attributes.getValue("type");
		if (typeStr == null)
			throw new SAXException("Error: a node is missing a 'type' attribute");

		NodeType type = NodeType.parse(typeStr);
		if (type == null)
			throw new SAXException("Error: a node has type '" + typeStr + "' which is not a valid type");

		Node ret = new Node(type);

		if (type == NodeType.NADJ)
		{
			ret.setType(NodeType.STD);
			ret.setNoAdjunction(true);
		}

		String id = attributes.getValue("name"); // the feature is called name, but id seems more appropriate
		if (id != null)
			ret.setId(id);
		return ret;
	}


	/**
	 * Creates a Tree.
	 * @param attributes
	 * @return a new Tree
	 * @throws SAXException
	 */
	private static Tree createTree(Attributes attributes) throws SAXException
	{
		String id = attributes.getValue("id");
		if (id == null)
			throw new SAXException("Error: a tree is missing a 'id' attribute");
		else return new Tree(id);
	}


	/**
	 * Creates a GrammarEntry.
	 * @param attributes
	 * @return a new GrammarEntry
	 * @throws SAXException
	 */
	private static GrammarEntry createEntry(Attributes attributes) throws SAXException
	{
		String name = attributes.getValue("name");
		if (name == null)
			throw new SAXException("Error: a grammar entry is missing a 'name' attribute");
		else return new GrammarEntry(name);
	}


	/**
	 * Creates a Feature.
	 * @param attributes
	 * @return a feature without specified value.
	 * @throws SAXException
	 */
	private Feature createFeature(Attributes attributes) throws SAXException
	{
		String name = attributes.getValue("name");
		if (name == null)
			throw new SAXException("Error: a feature is missing a 'name' attribute");
		return new Feature(name);
	}


	/**
	 * Returns a String from the given character range.
	 * @param ch
	 * @param start
	 * @param length
	 * @return a String
	 */
	private static String toString(char[] ch, int start, int length)
	{
		return new String(Arrays.copyOfRange(ch, start, start + length));
	}

}
