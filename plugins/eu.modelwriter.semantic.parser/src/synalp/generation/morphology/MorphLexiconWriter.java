package synalp.generation.morphology;

import java.io.*;
import java.util.*;

import org.xml.sax.SAXException;

import synalp.commons.input.*;
import synalp.generation.configuration.GeneratorOption;


/**
 * Writes a MorphLexicon in MPH format. The order in which the lemma and lexems are written depends
 * on the GeneratorOption MORPH_LEXEM_FIRST.
 * @author Alexandre Denis
 */
public class MorphLexiconWriter
{
	/**
	 * Demonstrates reading/writing with different MORPH_LEXEM_FIRST options.
	 * @param args
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void main(String[] args) throws SAXException, IOException
	{
		File lexFile = new File("resources/sem-xtag2/morph.mph");

		GeneratorOption.MORPH_LEXICON_ORDER = MorphLexiconOrder.LEXEM_FIRST;
		MorphLexicon lexicon = MorphLexiconReader.readLexicon(lexFile);

		GeneratorOption.MORPH_LEXICON_ORDER = MorphLexiconOrder.LEMMA_FIRST;
		MorphLexiconWriter.writeLexicon(lexicon, new File("resources/sem-xtag2/morph2.mph"));
	}


	/**
	 * Writes the given lexicon to the given file.
	 * @param lexicon
	 * @param file
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void writeLexicon(MorphLexicon lexicon, File file) throws SAXException, IOException
	{
		file.delete();
		RandomAccessFile output = new RandomAccessFile(file, "rw");
		List<String> lemmas = new ArrayList<String>(lexicon.keySet());
		Collections.sort(lemmas);
		for(String lemma : lemmas)
		{
			writeEntry(lexicon.get(lemma), output);
			output.writeBytes("\n");
		}
		output.close();
	}


	/**
	 * Writes the given entry to the given RandomAccessFile.
	 * @param morphLexiconEntry
	 * @param output
	 */
	private static void writeEntry(MorphLexiconEntry morphLexiconEntry, RandomAccessFile output)
	{
		Lemma lemma = morphLexiconEntry.getLemma();
		List<Lexem> lexems = new ArrayList<Lexem>(morphLexiconEntry.getLexems());
		Collections.sort(lexems, new Comparator<Lexem>()
		{
			public int compare(Lexem arg0, Lexem arg1)
			{
				return arg0.getValue().compareTo(arg1.getValue());
			}
		});

		for(Lexem lexem : lexems)
			writeToFile(lemma, lexem, output);
	}


	/**
	 * Writes the given lemma and lexem to given file.
	 * @param lemma
	 * @param lexem
	 * @param output
	 */
	private static void writeToFile(Lemma lemma, Lexem lexem, RandomAccessFile output)
	{
		try
		{
			if (GeneratorOption.MORPH_LEXICON_ORDER == MorphLexiconOrder.LEXEM_FIRST)
			{
				output.writeBytes(lexem.getValue());
				output.writeBytes("\t");
				output.writeBytes(lemma.getValue());
				output.writeBytes("\t");
			}
			else
			{
				output.writeBytes(lemma.getValue());
				output.writeBytes("\t");
				output.writeBytes(lexem.getValue());
				output.writeBytes("\t");
			}
			output.writeBytes(lexem.getFs().toString());
			output.writeBytes("\n");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
