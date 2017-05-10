package synalp.generation.jeni.semantics.rules;

import java.io.*;

import javax.xml.parsers.*;

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import synalp.commons.input.javacc.InputReader;
import synalp.commons.semantics.Semantics;
import synalp.commons.utils.Utils;
import synalp.generation.jeni.semantics.rules.operators.*;


/**
 * Reads a rule file.
 * @author Alexandre Denis
 */
public class RuleReader extends DefaultHandler
{
	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(RuleReader.class);

	private Rules rules;
	private MatchingRule matchRule;


	/**
	 * Shows how to read rules.
	 * @param args
	 * @throws IOException
	 * @throws SAXException
	 */
	public static void main(String[] args) throws SAXException, IOException
	{
		Rules rules = readRules(new File("resources/sem-xtag2/auto/rules.xml"));
		for(Rule rule : rules)
			System.out.println(rule);
	}


	/**
	 * Reads the given rules file in XML format. This method may produce NullPointerExceptions.
	 * @param file
	 * @return rules
	 */
	public static Rules readRulesNoException(File file)
	{
		RuleReader reader = new RuleReader();
		try
		{
			logger.info("Reading rules " + file);
			SAXParserFactory.newInstance().newSAXParser().parse(file, reader);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return reader.rules;
	}


	/**
	 * Reads the given rules file in XML format.
	 * @param file
	 * @return rules
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Rules readRules(File file) throws SAXException, IOException
	{
		RuleReader reader = new RuleReader();
		try
		{
			logger.info("Reading rules " + file);
			SAXParserFactory.newInstance().newSAXParser().parse(file, reader);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		return reader.rules;
	}


	@Override
	public void startDocument() throws SAXException
	{
		rules = new Rules();
	}


	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		try
		{
			if (qName.equals("rule"))
			{
				if (attributes.getValue("match") != null)
				{
					matchRule = createMatchingRule(attributes);
					if (matchRule != null)
						rules.add(matchRule);
				}
				else if (attributes.getValue("rewrite") != null)
				{
					Rule rewritingRule = createRewritingRule(attributes);
					if (rewritingRule != null)
						rules.add(rewritingRule);
				}
			}
			else if (qName.equals("add") || qName.equals("del") || qName.equals("replace-by") || qName.equals("replace"))
				addOperator(qName, attributes);
		}
		catch (Exception e)
		{
			throw new SAXException(e);
		}
	}


	/**
	 * Adds an operator to the current matching rule.
	 * @param name
	 * @param attributes
	 * @throws Exception 
	 */
	private void addOperator(String name, Attributes attributes) throws Exception
	{
		String newConst = attributes.getValue("new");
		String[] newConstValues = newConst==null ? new String[0] : Utils.splitAndTrim(newConst, ",");
		
		if (matchRule != null)
		{
			String semStr = attributes.getValue("sem");
			if (semStr==null)
			{
				String errorMsg = "Error: unable to parse rewriting rule, check the existence of feature 'sem'";
				logger.error(errorMsg);
				throw new Exception(errorMsg);
			}
				
			Semantics sem = InputReader.readSemantics(semStr);
			RuleOperator operator = null;
			if (name.equals("add"))
				operator = new AddOperator(sem);
			else if (name.equals("del"))
				operator = new DelOperator(sem);
			else if (name.equals("replace-by"))
				operator = new ReplaceOperator(sem, matchRule.getMatch());
			else if (name.equals("replace"))
			{
				Semantics by = InputReader.readSemantics(attributes.getValue("by"));
				operator = new ReplaceOperator(by, sem);
			}
			operator.addConstants(newConstValues);
			matchRule.addOperator(operator);
		}
	}
	
	
	/**
	 * Creates a RewritingRule.
	 * @param attributes
	 * @return a new RewritingRule
	 */
	private RewritingRule createRewritingRule(Attributes attributes)
	{
		String rewrite = attributes.getValue("rewrite");
		String by = attributes.getValue("by");
		String in = attributes.getValue("in");

		if (rewrite == null || by == null || in == null)
		{
			logger.error("Error: unable to parse rewriting rule, check the existence of features 'rewrite', 'by' and 'in'");
			return null;
		}
		else return new RewritingRule(rewrite, by, in);
	}


	/**
	 * Creates a MatchingRule.
	 * @param attributes
	 * @return a new MatchingRule
	 * @throws Exception if the semantics in features 'match' or 'without' cannot be parsed
	 */
	private MatchingRule createMatchingRule(Attributes attributes) throws Exception
	{
		String match = attributes.getValue("match");
		String without = attributes.getValue("without");

		if (match == null)
		{
			logger.error("Error: unable to parse matching rule, check the existence of feature 'match'");
			return null;
		}
		
		return new MatchingRule(InputReader.readSemantics(match), without==null ? new Semantics() : InputReader.readSemantics(without));
	}
}
