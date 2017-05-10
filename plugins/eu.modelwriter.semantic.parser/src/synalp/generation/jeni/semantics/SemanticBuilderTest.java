package synalp.generation.jeni.semantics;


import java.io.IOException;

import org.junit.Test;


/**
 * Tests the semantic building.
 * @author Alexandre Denis
 *
 */
public class SemanticBuilderTest
{
	@Test
	@SuppressWarnings("javadoc")
	public void testLoad() throws IOException
	{
		/*Grammar grammar = loadGrammar(ResourcesBundleFile.SEMXTAG2_GRAMMAR.getFile());
		FamiliesSemantics familySemantics = null;
		try
		{
			familySemantics = FamilySemanticsReader.readFamiliesSemantics(ResourcesBundleFile.SEMXTAG2_AUTO_SEM.getFile());
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		
		SemanticsBuilder builder = new SemanticsBuilder(familySemantics);
		builder.setSemantics(grammar);
		GrammarWriter.write(grammar, ResourcesBundleFile.SEMXTAG2_AUTO_GRAMMAR.getFile());
		
		SyntacticLexicon lexicon = builder.createLexicon(grammar);
		SyntacticLexiconWriter.write(lexicon, ResourcesBundleFile.SEMXTAG2_AUTO_LEXICON.getFile());
		
		for(SyntacticLexiconEntry entry : lexicon)
			System.out.println(entry+"\n");

		System.out.println();
		
		Grammar.printShort(grammar.values());*/
	}
}
