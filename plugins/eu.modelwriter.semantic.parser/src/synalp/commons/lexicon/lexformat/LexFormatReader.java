package synalp.commons.lexicon.lexformat;

import java.io.*;

import synalp.commons.lexicon.*;
import synalp.commons.lexicon.lexformat.javacc.LexFormatParser;


/**
 * A LexFormatReader reads both a LexFormatLexicon (.lex) and a macros file (.mac).
 * @author Alexandre Denis
 */
public class LexFormatReader
{
	/**
	 * Demonstrates how to call the LexFormatReader.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		testLexiconRead();
	}


	private static void testLexiconRead() throws Exception
	{
		LexFormatLexicon lexicon = readLexicon(new File("doc/examples/input/ex_lexicon.lex"));
		for(LexFormatEntry entry : lexicon)
			System.out.println(entry+"\n");
		System.out.println("Read " + lexicon.size() + " entries");
		
		SyntacticLexicon converted = lexicon.convertLexicon();
		for(SyntacticLexiconEntry entry : converted)
			System.out.println(entry+"\n");
		SyntacticLexiconWriter.write(converted, new File("doc/examples/input/ex_lexicon.xml"));
	}


	@SuppressWarnings("unused")
	private static void testMacrosRead() throws Exception
	{
		Macros tests = readMacros(new File("doc/examples/input/transsem/macros.mac"));
		for(Macro test : tests.values())
			System.out.println(test + "\n");
		System.out.println("Read " + tests.size() + " macros");
	}


	/**
	 * Reads the given File as a lexicon in the LexFormatLexicon.
	 * @param file
	 * @return a lexicon
	 * @throws Exception
	 */
	public static LexFormatLexicon readLexicon(File file) throws Exception
	{
		return LexFormatParser.readLexicon(file);
	}


	/**
	 * Reads the given File in the GenI macro format.
	 * @param file
	 * @return TestSuite
	 * @throws Exception
	 */
	public static Macros readMacros(File file) throws Exception
	{
		try(RandomAccessFile f = new RandomAccessFile(file, "r"))
		{
			byte[] content = new byte[(int) f.length()];
			f.readFully(content);
			return LexFormatParser.readMacros(new String(content));
		}
	}
}
