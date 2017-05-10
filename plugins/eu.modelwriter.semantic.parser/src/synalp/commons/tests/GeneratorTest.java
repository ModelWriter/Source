package synalp.commons.tests;

import static org.junit.Assert.fail;

import java.util.*;

import org.apache.log4j.Logger;

import synalp.commons.input.*;
import synalp.commons.output.*;
import synalp.commons.semantics.Semantics;
import synalp.commons.utils.*;
import synalp.commons.utils.exceptions.TimeoutException;
import synalp.commons.utils.loggers.LoggerConfiguration;
import synalp.generation.Generator;
import synalp.generation.jeni.JeniRealization;


/**
 * A GeneratorTests contains useful methods to test a Generator. It can be extended by a Junit test
 * or a custom test class. The GeneratorTest can test in strict mode (the input must exactly produce
 * the output) or in loose mode (the input must at least produce the output - more can be produced).
 * @author Alexandre Denis
 */
public abstract class GeneratorTest
{
	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(GeneratorTest.class);

	static
	{
		LoggerConfiguration.init();
	}
	
	
	/**
	 * Tests all entries of the given test suite. This method may throw TimeoutExceptions.
	 * @param generator
	 * @param suite
	 */
	public void test(Generator generator, TestSuite suite)
	{
		test(generator, suite, false);
	}


	/**
	 * Tests all entries of the given test suite.
	 * @param generator
	 * @param suite
	 * @param catchTimeout if catchTimeout is true, timeout exceptions do not interrupt the test ; if
	 * catchTimeout is false, the first timeout exception invalidates the test.
	 */
	public void test(Generator generator, TestSuite suite, boolean catchTimeout)
	{
		for(TestSuiteEntry entry : suite)
			if (catchTimeout)
			{
				try
				{
					test(generator, entry);
				}
				catch(TimeoutException e)
				{
					logger.warn("Timeout");
					System.out.println("Timeout "+entry.getId());
				}
			}
			else test(generator, entry);
	}


	/**
	 * Tests the given entry.
	 * @param generator
	 * @param entry
	 * @return true if the test passes, false otherwise
	 */
	public boolean test(Generator generator, TestSuiteEntry entry)
	{
		return test(generator, entry.getId(), entry.isStrict(), entry.isMorph(), entry.getSemantics(), entry.getSentences());
	}


	/**
	 * Tests the generator on given input in loose mode by default. The input needs to produce at
	 * least the given sentences, more are allowed.
	 * @param generator
	 * @param input
	 * @param expectedSentences an array of sentences in which lemmas are separated with the space
	 *            character
	 * @return true if the test passes, false otherwise
	 */
	public boolean test(Generator generator, Semantics input, String... expectedSentences)
	{
		return test(generator, "", false, false, input, Arrays.asList(expectedSentences));
	}


	/**
	 * Tests the generator on given input in loose mode by default. The input needs to produce at
	 * least the given sentences, more are allowed.
	 * @param generator
	 * @param input
	 * @param expectedSentences a list of sentences in which lemmas are separated with the space
	 *            character
	 * @return true if the test passes, false otherwise
	 */
	public boolean test(Generator generator, Semantics input, List<String> expectedSentences)
	{
		return test(generator, "", false, false, input, expectedSentences);
	}


	/**
	 * Tests the generator on given input. The input needs to produce the given sentences.
	 * @param generator
	 * @param strict if true the input needs to produce exactly the given list of sentences, if
	 *            false the input needs to produce at least the given list of sentences
	 * @param input
	 * @param expectedSentences an array of sentences in which lemmas are separated with the space
	 *            character
	 * @return true if the test passes, false otherwise
	 */
	public boolean test(Generator generator, boolean strict, Semantics input, String... expectedSentences)
	{
		return test(generator, "", strict, false, input, Arrays.asList(expectedSentences));
	}


	/**
	 * Tests the generator on given input. The input needs to produce the given sentences. However,
	 * if the given sentences list is empty the test is slightly different and is rather boolean: if
	 * there is an output, it returns true, and if there is not returns false.
	 * @param generator
	 * @param id
	 * @param strict if true the input needs to produce exactly the given list of sentences, if
	 *            false the input needs to produce at least the given list of sentences
	 * @param morph if true tests the actual morph realization, if false tests the lemma realization
	 * @param input
	 * @param expectedSentences a list of sentences in which lemmas are separated with the space
	 *            character
	 * @return true if the test passes, false otherwise
	 */
	public boolean test(Generator generator, String id, boolean strict, boolean morph, Semantics input, List<String> expectedSentences)
	{
		logger.info("Testing " + input + " -> " + expectedSentences);
		Perf.logTime();
		List<? extends SyntacticRealization> results = generator.generate(input);
		List<? extends SyntacticRealization> expected = asRealizations(expectedSentences);
		print(id, strict, morph, results);

		List<String> resultsSurface = getSurface(results, morph);

		// short test if there is no expected sentences
		if (expectedSentences.isEmpty())
			return !resultsSurface.isEmpty();

		List<String> expectedSurface = getSurface(expected, morph);

		for(String expectedReal : expectedSurface)
			if (!resultsSurface.contains(expectedReal))
			{
				List<String> toDisplay = new ArrayList<String>(new HashSet<String>(resultsSurface));
				Collections.sort(toDisplay);
				String msg = id + " output \"" + expectedReal + "\" is not produced (produced: " + toDisplay + ")";
				logger.error(msg);
				fail(msg);
				System.out.println(msg);
				return false;
			}

		if (strict)
		{
			Set<String> notExpected = new HashSet<String>();
			for(String result : resultsSurface)
				if (!expectedSurface.contains(result))
					notExpected.add(result);

			if (notExpected.size() > 0)
			{
				List<String> toDisplay = new ArrayList<String>(notExpected);
				Collections.sort(toDisplay);
				String msg = id + " output " + toDisplay + " " + (toDisplay.size() == 1 ? "is" : "are") + " produced but not expected (strict mode)";
				logger.error(msg);
				fail(msg);
				System.out.println(msg);
				return false;
			}

		}

		return true;
	}


	/**
	 * Returns the surface form of given realizations. If morph is true, returns the morphological
	 * realizations, if false returns the lemmas separated by space.
	 * @param realizations
	 * @param morph
	 * @return a list of surface forms
	 */
	private static List<String> getSurface(List<? extends SyntacticRealization> realizations, boolean morph)
	{
		List<String> ret = new ArrayList<String>();
		for(SyntacticRealization real : realizations)
			ret.addAll(getSurface(real, morph));
		return ret;
	}


	/**
	 * Returns the surface form of given realization. If morph is true, returns the morphological
	 * realizations, if false returns the lemmas separated by space.
	 * @param real
	 * @param morph
	 * @return a list of surface forms
	 */
	private static List<String> getSurface(SyntacticRealization real, boolean morph)
	{
		List<String> ret = new ArrayList<String>();
		if (morph)
		{
			for(MorphRealization morphReal : real.getMorphRealizations())
				ret.add(morphReal.asString());
		}
		else ret.add(Utils.print(real.getLemmas(), " "));
		return ret;
	}


	/**
	 * Prints the results on stdout.
	 * @param id
	 * @param strict
	 * @param morph if true prints the actual morph realization
	 * @param results
	 */
	private static void print(String id, boolean strict, boolean morph, List<? extends SyntacticRealization> results)
	{
		/*if (results.isEmpty())
			return;*/

		System.out.print("(" + Perf.logTime() + ") : ");

		Map<String, Integer> counts = countRealizations(morph, results);
		boolean first = true;
		for(String result : counts.keySet())
		{
			if (!first)
				System.out.print(" ; ");
			System.out.print(result + (counts.get(result) > 1 ? " (x" + counts.get(result) + ")" : ""));
			first = false;
		}
		System.out.println();
	}


	/**
	 * Counts for each results surface form how many times it appears. The surface form is built by
	 * separating each lemma with the space character.
	 * @param morph if true returns the actual morph realizations
	 * @param results
	 * @return the surface forms of each realization associated with the number of times it appears
	 */
	private static Map<String, Integer> countRealizations(boolean morph, List<? extends SyntacticRealization> results)
	{
		Map<String, Integer> ret = new HashMap<String, Integer>();
		if (morph)
		{
			for(SyntacticRealization result : results)
				for(MorphRealization morphReal : result.getMorphRealizations())
				{
					String surface = morphReal.asString();
					if (!ret.containsKey(surface))
						ret.put(surface, 0);
					ret.put(surface, ret.get(surface) + 1);
				}
		}
		else
		{
			for(SyntacticRealization result : results)
			{
				String surface = Utils.print(result.getLemmas(), " ");
				if (!ret.containsKey(surface))
					ret.put(surface, 0);
				ret.put(surface, ret.get(surface) + 1);
			}
		}
		return ret;
	}


	/**
	 * Returns the sentences as realizations. Probably we could directly have realizations in the
	 * TestSuiteEntry class instead of a List<String> that would avoid having this method.
	 * @param sentences
	 * @return the input sentences split around space characters and represented as realizations
	 */
	private static List<? extends SyntacticRealization> asRealizations(List<String> sentences)
	{
		List<JeniRealization> ret = new ArrayList<JeniRealization>();
		for(String sentence : sentences)
		{
			JeniRealization synReal = new JeniRealization();
			MorphRealization morphReal = new MorphRealization();
			synReal.setMorphRealizations(Collections.singleton(morphReal));
			for(String lemma : sentence.split(" "))
			{
				synReal.add(new Lemma(lemma));
				morphReal.add(new Lexem(lemma));
			}
			ret.add(synReal);
		}
		return ret;
	}
}
