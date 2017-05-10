package synalp.commons.tests;

import java.io.*;

import org.junit.Test;

import synalp.commons.lexicon.*;
import synalp.generation.configuration.GeneratorConfiguration;
import static org.junit.Assert.fail;

/**
 * Reads a lexicon, writes it to a file, then re-read it. We should test if the lexicon is still the same.
 * @author Alexandre Denis
 *
 */
public class SyntacticLexiconWriterTest
{
	
	@Test
	@SuppressWarnings("javadoc")
	public void testReadWrite()
	{
		try
		{
			SyntacticLexicon lexicon = GeneratorConfiguration.getSyntacticLexicon("french");
			File tmp = File.createTempFile("test", ".xml");
			System.out.println("Writing on "+tmp);
			//tmp.deleteOnExit();
			SyntacticLexiconWriter.write(lexicon, tmp);
			SyntacticLexiconReader.readLexicon(tmp);
		}
		catch (Exception e)
		{
			fail(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
}
