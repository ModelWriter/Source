package synalp.generation.jeni.tests;

import synalp.commons.input.*;
import synalp.commons.utils.*;
import synalp.commons.utils.exceptions.TimeoutException;
import synalp.commons.utils.loggers.LoggerConfiguration;
import synalp.generation.configuration.*;
import synalp.generation.jeni.JeniLexicalSelection;

/**
 * Simple filtering test.
 * @author adenis
 *
 */
public class TestFiltering
{
	public static void main(String[] args)
	{
		GeneratorConfiguration config = GeneratorConfigurations.getConfig("kbgen");
		
		LoggerConfiguration.init();
		JeniLexicalSelection selection = new JeniLexicalSelection(config.getGrammar(), config.getSyntacticLexicon());
		for(TestSuiteEntry entry : config.getTestSuite())
		{
			System.out.println(entry.getId());
			try
			{
				TimeoutManager.start();
				selection.selectEntries(entry.getSemantics());
			}
			catch(TimeoutException e)
			{
				System.err.println(e.getMessage());
			}
		}
	}
}
