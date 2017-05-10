package synalp.commons.tests;

import java.io.*;

import org.junit.Test;

import synalp.commons.grammar.*;
import synalp.generation.configuration.GeneratorConfiguration;
import static org.junit.Assert.fail;

/**
 * Reads a grammar, writes it to a file, then re-read it. We should test if the grammar is still the same.
 * @author Alexandre Denis
 *
 */
public class GrammarWriterTest
{
	
	@Test
	@SuppressWarnings("javadoc")
	public void testReadWrite()
	{
		try
		{
			// it uses directly the GrammarReader instead of Resources.loadGrammar to enable the exception
			Grammar grammar = GeneratorConfiguration.getGrammar("semxtag");
			File tmp = File.createTempFile("test", ".xml");
			//tmp.deleteOnExit();
			GrammarWriter.write(grammar, tmp);
			GrammarReader.readGrammar(tmp);
		}
		catch (Exception e)
		{
			fail(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
}
