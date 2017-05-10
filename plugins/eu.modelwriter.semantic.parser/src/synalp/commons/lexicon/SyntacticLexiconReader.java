package synalp.commons.lexicon;

import java.io.*;

import javax.xml.parsers.*;

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import synalp.commons.input.Lemma;
import synalp.commons.lexicon.lexformat.*;
import synalp.commons.semantics.*;
import synalp.commons.unification.*;
import synalp.generation.configuration.GeneratorOption;


/**
 * A SyntacticLexiconReader parses a syntactic lexicon in XML format. Note that the lexicon allows
 * to have duplicates, we should filter those.
 * @author Alexandre Denis
 */
public class SyntacticLexiconReader extends DefaultHandler
{
	/**
	 * The prefix of the symbols that denote variables.
	 */
	public static String VARIABLE_PREFIX = "?";

	/**
	 * Forces that the labels are considered systematically as variables. This is done because if
	 * labels are considered constants they will fail to unify with the input constant labels. If
	 * true, all labels are variables and a variable prefix is added to them if they don't have one,
	 * if false they only are variables if they are correctly prefixed.
	 */
	public static boolean LABEL_ARE_ALWAYS_VARIABLES = true;

	private static Logger logger = Logger.getLogger(SyntacticLexiconReader.class);

	private SyntacticLexicon lexicon;
	private SyntacticLexiconEntry entry;

	private Feature feature;
	private FeatureValue value;
	private FeatureStructure featureStructure;

	private DefaultLiteral literal;
	private Equation equation;
	private Equations equations;
	private Semantics semantics;

	private boolean inLiteral;


	/**
	 * Demonstrates how to read a lexicon.
	 * @param args
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void main(String[] args) throws SAXException, IOException
	{
		SyntacticLexicon lexicon = readLexicon(new File("doc/examples/input/ex_lexicon.xml"));
		for(SyntacticLexiconEntry entry : lexicon)
			System.out.println(entry + "\n");
		System.out.println("Read " + lexicon.size() + " entries");
	}


	/**
	 * Reads the given lexicon. This method determines the proper way to read the lexicon thanks to
	 * the file extension. If the extension is ".xml", it uses the xml format, if the extension is
	 * ".lex" it uses the lex format. Otherwise it throws an exception.
	 * @param file
	 * @return a syntactic lexicon
	 * @throws SAXException
	 * @throws IOException
	 */
	public static SyntacticLexicon readLexicon(File file) throws SAXException, IOException
	{
		if (file.getName().endsWith(".xml"))
			return readLexiconXMLFormat(file);
		else if (file.getName().endsWith(".lex"))
			return readLexiconLexFormat(file);
		else throw new IOException("Error: unable to determine lexicon format, the file must end either with '.xml' or '.lex'");
	}


	/**
	 * Reads the given lexicon assuming it is in LEX format. It is assumed that the macros file
	 * needed to convert the LEX entries is defined in the file with an "include" operator.
	 * @param file
	 * @return a syntactic lexicon
	 * @throws SAXException
	 * @throws IOException
	 */
	private static SyntacticLexicon readLexiconLexFormat(File file) throws SAXException, IOException
	{
		LexFormatLexicon lexicon;
		try
		{
			lexicon = LexFormatReader.readLexicon(file);
		}
		catch (Exception e)
		{
			throw new SAXException("Error: unable to read " + file + " : " + e.getMessage());
		}
		return lexicon.convertLexicon();
	}


	/**
	 * Reads the given lexicon assuming it is in XML format.
	 * @param file
	 * @return a syntactic lexicon
	 * @throws SAXException
	 * @throws IOException
	 */
	public static SyntacticLexicon readLexiconXMLFormat(File file) throws SAXException, IOException
	{
		SyntacticLexiconReader reader = new SyntacticLexiconReader();
		try
		{
			SAXParserFactory.newInstance().newSAXParser().parse(file, reader);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		return reader.lexicon;
	}


	@Override
	public void startDocument() throws SAXException
	{
		lexicon = new SyntacticLexicon();
	}


	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		if (qName.equals("lemma"))
		{
			entry = createEntry(attributes);
			equations = new Equations();
		}
		else if (qName.equals("fs"))
			featureStructure = new FeatureStructure();
		else if (qName.equals("f"))
			feature = createFeature(attributes);
		else if (qName.equals("sym"))
			value = createValue(attributes);
		else if (qName.equals("equation"))
			equation = createEquation(attributes, false);
		else if (qName.equals("coanchor"))
			equation = createEquation(attributes, true);
		else if (qName.equals("literal"))
		{
			literal = createLiteral(attributes);
			inLiteral = true;
		}
		else if (qName.equals("semantics"))
			semantics = new Semantics();
	}


	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if (qName.equals("f"))
			featureStructure.add(feature);
		else if (qName.equals("literal"))
		{
			inLiteral = false;
			semantics.add(literal);
		}
		else if (qName.equals("sym"))
		{
			// <sym> may be used in literal or feature structure
			if (inLiteral)
				literal.addArgument(value);
			else feature.setValue(value);
		}
		else if (qName.equals("equation") || qName.equals("coanchor"))
		{
			equation.setFeatureStructure(featureStructure);
			equations.add(equation);
		}
		else if (qName.equals("interface"))
			entry.setInterface(featureStructure);
		else if (qName.equals("filter"))
			entry.setFilter(new Filter(featureStructure));
		else if (qName.equals("lemma"))
		{
			try
			{
				entry.setEquations(equations.aggregate());
			}
			catch (Exception e)
			{
				logger.error(e.getMessage());
			}
			lexicon.add(entry);
		}
		else if (qName.equals("semantics"))
			entry.setSemantics(semantics);
	}


	/**
	 * Creates a Literal.
	 * @param attributes
	 * @return a literal
	 * @throws SAXException
	 */
	private DefaultLiteral createLiteral(Attributes attributes) throws SAXException
	{
		// predicate
		String predicate = attributes.getValue("predicate");
		if (predicate == null)
			throw new SAXException("Error: a literal is missing a 'predicate' attribute");
		FeatureValue predValue;
		if (predicate.startsWith(VARIABLE_PREFIX))
			predValue = new FeatureVariable(predicate);
		else predValue = new FeatureConstant(predicate);

		// label
		String label = attributes.getValue("label");
		if (label == null)
			throw new SAXException("Error: a literal with predicate '" + predicate + "' is missing a 'label' attribute");
		FeatureValue labelValue = null;
		if (LABEL_ARE_ALWAYS_VARIABLES)
		{
			if (label.startsWith(VARIABLE_PREFIX))
				labelValue = new FeatureVariable(label);
			else labelValue = new FeatureVariable(VARIABLE_PREFIX + label);
		}
		else
		{
			if (label.startsWith(VARIABLE_PREFIX))
				labelValue = new FeatureVariable(label);
			else labelValue = new FeatureConstant(label);
		}

		// create literal eventually
		DefaultLiteral ret = new DefaultLiteral(predValue);
		ret.setLabel(labelValue);
		return ret;
	}


	/**
	 * Creates an Equation.
	 * @param attributes
	 * @param isCoanchorEquation
	 * @return an equation
	 * @throws SAXException
	 */
	private Equation createEquation(Attributes attributes, boolean isCoanchorEquation) throws SAXException
	{
		String type = attributes.getValue("type");
		if (type == null)
			throw new SAXException("Error: an equation is missing a 'type' attribute");

		FeatureStructureType fsType = FeatureStructureType.parse(type);
		if (fsType == null)
			throw new SAXException("Error: an equation type '" + type + "' is invalid, it should be 'bot' or 'top'");

		String nodeId = attributes.getValue("node_id");
		if (nodeId == null)
			throw new SAXException("Error: an equation is missing a 'node_id' attribute");

		return new Equation(nodeId, fsType, isCoanchorEquation);
	}


	/**
	 * Creates a FeatureValue. If the value starts with the VARIABLE_PREFIX, a new variable is
	 * returned, else a constant is returned.
	 * @param attributes
	 * @return a constant
	 * @throws SAXException
	 */
	private FeatureValue createValue(Attributes attributes) throws SAXException
	{
		String value = attributes.getValue("value");
		if (value == null)
			throw new SAXException("Error: a constant is missing a 'value' attribute");
		if (value.startsWith(VARIABLE_PREFIX))
			return new FeatureVariable(value);
		else return new FeatureConstant(value);
	}


	/**
	 * Creates a Feature.
	 * @param attributes
	 * @return a feature without specified value.
	 * @throws SAXException
	 */
	private static Feature createFeature(Attributes attributes) throws SAXException
	{
		String name = attributes.getValue("name");
		if (name == null)
			throw new SAXException("Error: a feature is missing a 'name' attribute");

		if (GeneratorOption.REWRITE_LEX_AS_LEMMA && name.equals("lex"))
			name = "lemma";

		return new Feature(name);
	}


	/**
	 * Creates a syntactic lexicon entry.
	 * @param attributes
	 * @return a syntactic lexicon entry.
	 * @throws SAXException
	 */
	private static SyntacticLexiconEntry createEntry(Attributes attributes) throws SAXException
	{
		SyntacticLexiconEntry ret = new SyntacticLexiconEntry();
		String name = attributes.getValue("name");
		if (name == null)
		{
			if (!GeneratorOption.ALLOW_EMPTY_LEMMAS)
				throw new SAXException("Error: a lemma is missing a 'name' attribute, family '" + attributes.getValue("family") + "'");
		}
		else ret.setLemma(new Lemma(name));
		String family = attributes.getValue("family");
		if (family == null)
			throw new SAXException("Error: lemma '" + name + "' is missing a 'family' attribute");

		if (family.indexOf(',') != -1)
		{
			String[] familyparts = family.split(",");
			String[] families = new String[familyparts.length];
			for(int i = 0; i < familyparts.length; i++)
				families[i] = familyparts[i].trim();
			ret.setFamilies(families);
		}
		else ret.setFamilies(family);
		return ret;
	}
}
