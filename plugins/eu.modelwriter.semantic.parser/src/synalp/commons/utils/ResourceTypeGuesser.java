package synalp.commons.utils;

import java.io.*;
import java.util.*;

/**
 * Opens files in a directory and tries to guess from the first symbols in the file's content the
 * type of resource: syntactic lexicon, grammar, test suite or morph lexicon. To do better: use
 * n-grams.
 * @author Alexandre Denis
 */
public class ResourceTypeGuesser
{
	/**
	 * The type of resource.
	 * @author Alexandre Denis
	 */
	@SuppressWarnings("javadoc")
	public enum ResourceType
	{
		UNKNOWN,
		GRAMMAR("grammar", "entry", "family", "tree", "node"),
		TEST_SUITE("semantics", "sentence"),
		SYNTACTIC_LEXICON("lexicon", "lemma", "semantics", "equations"),
		MORPHOLOGICAL_LEXICON(".+\\s+.+\\s+\\[.*\\]");

		private String[] tags;


		private ResourceType(String... tags)
		{
			this.tags = tags;
		}
	}


	/**
	 * Tests the resource type guesser.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		System.out.println(new ResourceTypeGuesser().getMostLikelyFilesType(new File("resources/sem-xtag2/auto")));
		System.out.println(new ResourceTypeGuesser().getMostLikelyFilesType(new File("resources/sem-xtag2/sources")));
	}


	/**
	 * Returns a map of the files contained in the given directory and their assumed type.
	 * @param directory
	 * @return a map of files and their type, UNKNOWN if it cannot be guessed
	 * @throws IOException
	 */
	public Map<File, ResourceType> getMostLikelyFilesType(File directory) throws IOException
	{
		Map<File, ResourceType> ret = new HashMap<File, ResourceType>();
		for(File file : directory.listFiles())
			if (!file.isDirectory())
				ret.put(file, getMostLikelyType(file));
		return ret;
	}


	/**
	 * Returns the most likely file type.
	 * @param file
	 * @return a type of resource
	 * @throws IOException
	 */
	public ResourceType getMostLikelyType(File file) throws IOException
	{
		int max = 0;
		ResourceType ret = null;
		Map<ResourceType, Integer> counts = getSymbolsCount(file);
		for(ResourceType type : counts.keySet())
		{
			int count = counts.get(type);
			if (count >= max)
			{
				max = count;
				ret = type;
			}
		}
		return ret;
	}


	/**
	 * Returns counts for all files, the higher the most probable. It looks wether the resource type
	 * tags matches the file name and start of file (30 lines), discard low counts, and maps UNKNOWN
	 * to 1 if no tag is found.
	 * @param file
	 * @return a count for each type, the higher the most probable
	 * @throws IOException
	 */
	public Map<ResourceType, Integer> getSymbolsCount(File file) throws IOException
	{
		Map<ResourceType, Integer> ret = new HashMap<ResourceType, Integer>();
		
		String fileName=file.getName();
		for(ResourceType type : ResourceType.values())
			for(String tag : type.tags)
				if (fileName.matches(tag) || fileName.indexOf(tag) != -1)
					Utils.addN(3, type, ret);
		
		Perf.logTime();
		try(RandomAccessFile input = new RandomAccessFile(file, "r");)
		{
			String line;
			int i = 0;
			while(i < 5 && ((line = input.readLine()) != null))
			{
				line = line.trim();
				if (!line.isEmpty())
					for(ResourceType type : ResourceType.values())
						for(String tag : type.tags)
							if (line.matches(tag) || line.indexOf(tag) != -1)
								Utils.addOne(type, ret);
			}
			System.out.println(file+" "+Perf.logTime());
			for(ResourceType type : ret.keySet().toArray(new ResourceType[ret.size()]))
				if (ret.get(type) < 3)
					ret.remove(type);
			if (ret.isEmpty())
				ret.put(ResourceType.UNKNOWN, 1);
			return ret;
		}
	}
}
