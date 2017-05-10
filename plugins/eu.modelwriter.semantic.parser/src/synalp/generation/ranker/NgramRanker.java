package synalp.generation.ranker;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.TreeMap;

import synalp.commons.input.Lemma;
import synalp.commons.utils.*;
import synalp.generation.ChartItem;

/**
 * @author apoorvi
 */
public class NgramRanker implements Ranker
{
	/**
	 * temporary file used to write sentences; used by n-gram
	 */
	private static final String TEST_FILENAME = "resources/ranking/test.txt";

	float ppl;
	HashMap<String, Double> map2;
	String lmfile;
	private int beamsize;
	String ngramtype;



	/**
	 * Constructor:constructs by taking the language-model file
	 * @param lmfile
	 * @param beamsize
	 * @param ngramtype
	 */
	public NgramRanker(final String lmfile, int beamsize, String ngramtype)
	{
		this.lmfile = lmfile;
		this.beamsize = beamsize;
		this.ngramtype = ngramtype;

	}


	/**
	 * @return a map of Sentences to their Perplexities
	 */
	public HashMap<String, Double> getPerplexity()
	{
		try
		{
			Process p = Runtime.getRuntime().exec(
													"ngram -ppl " + TEST_FILENAME
															+ " -use-server 2000 -debug 1");
			BufferedReader b = new BufferedReader(new InputStreamReader(
																		p.getInputStream()));
			map2 = new HashMap<String, Double>();
			String sentence;
			while((sentence = b.readLine()) != null)
			{
				b.readLine();
				String temp1 = b.readLine();
				b.readLine();
				if (temp1 != null)
				{
					int start = temp1.indexOf("ppl= ") + 5;
					int end = temp1.indexOf(" ", start);
					map2.put(sentence,
								Double.parseDouble(temp1.substring(start, end)));
				}
			}
			b.close();
			if (map2.isEmpty())
				System.err.println("Error: no ngram results, did you launch the server (example: ngram -order 3 -lm lm-genia-lemma.bin -server-port 2000)?");
			return map2;
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return null;
	}


	/**
	 * @param chartitems : List of lemmas
	 * @return map of ChartItems to their respective Sentences
	 * @throws IOException
	 */
	public HashMap<ChartItem, String> writeTofile(final List<? extends ChartItem> chartitems) throws IOException
	{
		HashMap<ChartItem, String> ret = new HashMap<ChartItem, String>();
		BufferedWriter testfile = new BufferedWriter(new FileWriter(
																	TEST_FILENAME));
		int chartitemsize = chartitems.size();
		for(int i = 0; i < chartitemsize; i++)
		{
			ChartItem temp = chartitems.get(i);
			String sentence = Utils.print(temp.getLemmas(), " ");
			testfile.write(sentence);
			testfile.newLine();
			ret.put(temp, sentence);
		}

		testfile.close();
		return ret;
	}


	/**
	 * @param map1 : Map of CI -> Sentence
	 * @param map2 : Map of Sentence -> Ppl
	 * @return map : Map of CI -> Ppl
	 */
	public Map<ChartItem, Double> assignPerplexity(final HashMap<ChartItem, String> map1, final HashMap<String, Double> map2)
	{
		Map<ChartItem, Double> ret = new HashMap<ChartItem, Double>();
		for(ChartItem item : map1.keySet())
		{
			String str = map1.get(item).trim();
			if (map2.containsKey(str))
				ret.put(item, map2.get(str));

		}
		return ret;
	}


	/**
	 * Returns the log probabilities of given chart items.
	 * @param chartitems
	 * @return a map ChartItem -> Log prob
	 */
	private Map<ChartItem, Double> getLogProbabilities(List<? extends ChartItem> chartitems)
	{
		Map<ChartItem, Double> ret = new HashMap<ChartItem, Double>();
		for(ChartItem item : chartitems)
		{
			List<String> words = new ArrayList<String>();
			for(Lemma lemma : item.getLemmas())
				words.add(lemma.toString());
			//ret.put(item, sriLM.getSentenceLogProb(words));
			ret.put(item, 1.00); // Edited here so as to avoid the SRILM library -- EPL issues (wanted to get rid of GPL libraries as much as possible and for ModelWriter project, we are not using a probabilistic generation anyway).
		}
		return ret;
	}


	/**
	 * @param chartitems
	 * @return sorted list of given chartitems in the order of increasing perplexities.
	 */
	public List<? extends ChartItem> rank(final List<? extends ChartItem> chartitems)
	{
		if (chartitems.isEmpty())
			return chartitems;

		Map<ChartItem, Double> ciToppl;
		if (ngramtype.equals("serverBased"))
		{
			HashMap<ChartItem, String> ciTosentence = null;
			try
			{
				ciTosentence = writeTofile(chartitems);
				HashMap<String, Double> sentenceToppl = getPerplexity();
				ciToppl = assignPerplexity(ciTosentence, sentenceToppl);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			HashMap<String, Double> sentenceToppl = getPerplexity();
			ciToppl = assignPerplexity(ciTosentence, sentenceToppl);
		}
		else ciToppl = getLogProbabilities(chartitems);

		DoubleValueComparator dvc = new DoubleValueComparator(ciToppl);
		TreeMap<ChartItem, Double> sorted = new TreeMap<ChartItem, Double>(dvc);
		sorted.putAll(ciToppl);
		List<ChartItem> ret = new ArrayList<ChartItem>();
		Set<ChartItem> keys = sorted.keySet();
		int n = 0;
		for(Iterator<ChartItem> i = keys.iterator(); i.hasNext();)
		{
			ChartItem sortedCI = (ChartItem) i.next();
			if (n < beamsize)
			{
				n++;
				ret.add(sortedCI);
			}
			else break;
		}
		return ret;
	}
}
