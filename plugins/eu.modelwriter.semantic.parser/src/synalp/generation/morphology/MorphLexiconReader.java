package synalp.generation.morphology;

import java.io.*;

import javax.xml.parsers.*;

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import synalp.commons.input.*;
import synalp.commons.unification.FeatureStructure;
import synalp.generation.morphology.javacc.MphFormatParser;


/**
 * Reads a MorphLexicon in XML or MPH format. The XML format contains lines such as <m
 * l="disapprove" f="disapproved" fs="cat=v; pers=3; mode=ppart"/>, where l is the lemma, f the
 * inflected form and fs the feature structure.
 * @author Alexandre Denis
 */
public class MorphLexiconReader extends DefaultHandler
{
	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(MorphLexiconReader.class);

	private MorphLexicon lexicon;


	/**
	 * Shows how to read a lexicon.
	 * @param args
	 * @throws IOException
	 * @throws SAXException
	 */
	public static void main(String[] args) throws SAXException, IOException
	{
		MorphLexicon lexicon = readLexicon(new File("resources/sem-xtag2/auto/morph.mph"));
		for(MorphLexiconEntry entry : lexicon.values())
			System.out.println(entry);
	}


	/**
	 * Reads the given morph lexicon. This method determines the proper way to read the lexicon
	 * thanks to the file extension. If the extension is ".xml", it uses the XML format, if the
	 * extension is ".mph" it uses the MPH format. Otherwise it throws an exception.
	 * @param file
	 * @return a morph lexicon
	 * @throws SAXException
	 * @throws IOException
	 */
	public static MorphLexicon readLexicon(File file) throws SAXException, IOException
	{
		if (file.getName().endsWith(".xml"))
			return readLexiconXMLFormat(file);
		else if (file.getName().endsWith(".mph"))
			return readLexiconMphFormat(file);
		else throw new IOException("Error: unable to determine lexicon format, the file must end either with '.xml' or '.mph'");
	}


	/**
	 * Reads the given morph lexicon in MPH format.
	 * @param file
	 * @return a morph lexicon
	 * @throws IOException
	 */
	public static MorphLexicon readLexiconMphFormat(File file) throws IOException
	{
		try
		{
			return MphFormatParser.readLexicon(file);
		}
		catch (Exception e)
		{
			throw new IOException("Error: unable to read " + file + " : " + e.getMessage());
		}
	}


	/**
	 * Reads the given lexicon in XML format.
	 * @param file
	 * @return a morphological lexicon
	 * @throws SAXException
	 * @throws IOException
	 */
	public static MorphLexicon readLexiconXMLFormat(File file) throws SAXException, IOException
	{
		MorphLexiconReader reader = new MorphLexiconReader();
		try
		{
			logger.info("Reading morph lexicon " + file);
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
		lexicon = new MorphLexicon();
	}


	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		if (qName.equals("m"))
		{
			String lemmaStr = attributes.getValue("l");
			if (lemmaStr == null)
			{
				logger.error("Missing lemma feature \"l\" for a morphological entry, skipping");
				return;
			}

			String morphStr = attributes.getValue("f");
			if (morphStr == null)
			{
				logger.error("Missing inflected form feature \"f\" for a morphological entry, skipping");
				return;
			}

			String fsStr = attributes.getValue("fs");
			FeatureStructure fs = new FeatureStructure();
			if (fsStr != null)
				try
				{
					fs = MphFormatParser.readFeatureStructure(fsStr);
				}
				catch (Exception e)
				{
					logger.error("Unable to parse feature structure '" + fsStr + "' : " + e.getMessage() + ", skipping fs");

				}

			lexicon.add(new MorphLexiconEntry(new Lemma(lemmaStr), new Lexem(morphStr, fs)));
		}
	}
}
