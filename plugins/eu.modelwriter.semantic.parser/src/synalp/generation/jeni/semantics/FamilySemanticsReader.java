package synalp.generation.jeni.semantics;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import synalp.commons.grammar.Trace;
import synalp.commons.input.Lemma;
import synalp.commons.lexicon.Equations;
import synalp.commons.semantics.*;
import synalp.commons.unification.*;
import synalp.commons.utils.*;


/**
 * A FamilySemanticsReader in XML format.
 * @author Alexandre Denis
 */
public class FamilySemanticsReader extends DefaultHandler
{
	private static Logger logger = Logger.getLogger(FamilySemanticsReader.class);

	private String curTag;

	private Lemma curLemma;
	private InstantiationContext curContext;
	private FamilySemantics curFamilySemantics;
	private FamiliesSemantics familiesSemantics;


	/**
	 * Demonstrates how to read family semantics.
	 * @param args
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void main(String[] args) throws SAXException, IOException
	{
		/*List<FamilySemantics> families = FamilySemanticsReader.readFamiliesSemantics(ResourcesBundleFile.SEMXTAG2_AUTO_SEM.getFile());
		for(FamilySemantics family : families)
			System.out.println(family + "\n");*/
	}


	/**
	 * Reads the given family semantics without exceptions. May produce NullPointer exceptions.
	 * @param file
	 * @return a syntactic lexicon
	 */
	public static FamiliesSemantics readFamiliesSemanticsNoException(File file)
	{
		FamilySemanticsReader reader = new FamilySemanticsReader();
		try
		{
			logger.info("Reading family semantics " + file);
			SAXParserFactory.newInstance().newSAXParser().parse(file, reader);
		}
		catch (Exception e)
		{
			System.err.println("Error while reading family semantics after or in "+reader.curFamilySemantics+" in file "+file+" : "+e.getMessage());
		}
		return reader.familiesSemantics;
	}


	/**
	 * Reads the given family semantics.
	 * @param file
	 * @return a syntactic lexicon
	 * @throws SAXException
	 * @throws IOException
	 */
	public static FamiliesSemantics readFamiliesSemantics(File file) throws SAXException, IOException
	{
		FamilySemanticsReader reader = new FamilySemanticsReader();
		try
		{
			logger.info("Reading family semantics " + file);
			SAXParserFactory.newInstance().newSAXParser().parse(file, reader);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		return reader.familiesSemantics;
	}


	@Override
	public void startDocument() throws SAXException
	{
		familiesSemantics = new FamiliesSemantics();
	}


	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		curTag = qName;
		if (qName.equals("family"))
		{
			curFamilySemantics = new FamilySemantics();
			familiesSemantics.add(curFamilySemantics);
			String trace = attributes.getValue("trace");
			if (trace == null)
				logger.error("Error: unable to find trace attribute for a family semantics");
			else curFamilySemantics.setTrace(new Trace(Utils.splitAndTrim(trace)));

			String pattern = attributes.getValue("pattern");
			if (pattern != null && pattern.equals("true"))
				curFamilySemantics.setPattern(true);

			String eqStr = attributes.getValue("eq");
			if (eqStr != null)
				curFamilySemantics.setFamilyEquations(Equations.readEquations(eqStr));
		}
		else if (qName.equals("lemmas"))
		{
			// accepts both "value" and "values"
			String lemmas = attributes.getValue("value");
			if (lemmas==null)
				lemmas = attributes.getValue("values");
			if (lemmas != null)
				curFamilySemantics.addAll(createLemmas(Utils.splitAndTrim(lemmas)));
		}
		else if (qName.equals("lemma"))
		{
			String value = attributes.getValue("value");
			if (value == null)
				logger.error("Error: unable to find lemma value attribute for family semantics " + curFamilySemantics);
			else
			{
				curLemma = new Lemma(value.trim());
				curContext = new InstantiationContext();

				Equations equations = new Equations();
				String eq = attributes.getValue("eq");
				if (eq != null)
					equations = Equations.readEquations(eq);

				String coanchorsVal = attributes.getValue("coanchors");
				if (coanchorsVal != null)
					curFamilySemantics.add(curLemma, curContext, Arrays.asList(createLemmas(Utils.splitAndTrim(coanchorsVal, ","))), equations);
				else curFamilySemantics.add(curLemma, curContext, equations);
			}
		}
		else if (qName.equals("var"))
		{
			String name = attributes.getValue("name");
			String value = attributes.getValue("value");
			if (name == null)
				logger.error("Error: unable to find name attribute for a variable in family semantics " + curFamilySemantics);
			if (value == null)
				logger.error("Error: unable to find value attribute for a variable in family semantics " + curFamilySemantics);
			curContext.put(new FeatureVariable(name), new FeatureConstant(value));
		}
	}


	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		curTag = "";
	}


	/**
	 * Creates Lemma instances from strings.
	 * @param lemmas
	 * @return an array of Lemmas
	 */
	private static Lemma[] createLemmas(String[] lemmas)
	{
		Lemma[] ret = new Lemma[lemmas.length];
		for(int i = 0; i < lemmas.length; i++)
			ret[i] = new Lemma(lemmas[i]);
		return ret;
	}


	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if (curTag.equals("semantics"))
			curFamilySemantics.setSemantics(Semantics.readSemantics(toString(ch, start, length)));
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
