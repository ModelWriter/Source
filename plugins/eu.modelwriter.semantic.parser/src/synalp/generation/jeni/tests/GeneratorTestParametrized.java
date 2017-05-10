package synalp.generation.jeni.tests;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;

import synalp.commons.input.*;
import synalp.commons.output.SyntacticRealization;
import synalp.commons.semantics.*;
import synalp.commons.utils.loggers.LoggerConfiguration;
import synalp.generation.Generator;
import synalp.generation.configuration.*;
import synalp.generation.jeni.*;


/**
 * General purpose test for Generators. It tests independently each entry of a testsuite with a
 * given generator (use Eclipse to launch). Ran with junit 4.11 because of the display.
 * This class needs to be fixed because it relies on Lemma for which equals is too constrained, to discuss.
 * @author Alexandre Denis
 */
@RunWith(Parameterized.class)
@SuppressWarnings("javadoc")
public class GeneratorTestParametrized
{
	private Semantics input;
	private List<? extends SyntacticRealization> expected;
	public static GeneratorConfiguration config = GeneratorConfigurations.getConfig("transsem");
	public static Generator generator = new JeniGenerator(config);

	static
	{
		LoggerConfiguration.init();
	}


	@Test
	public void test()
	{
		List<? extends SyntacticRealization> results = generator.generate(input);

		for(SyntacticRealization result : results)
			if (!contains(expected, result))
				fail(result + " is produced but not expected");

		for(SyntacticRealization expectedReal : expected)
			if (!contains(results, expectedReal))
				fail(expected + " is not produced (produced: " + results + ")");
	}


	/**
	 * Creates for each entry of the suite a pair (semantics, expected realization). The third
	 * parameter is the entry id and used only for display purpose.
	 * @return
	 */
	@Parameters(name = "{index}: {2}")
	public static Collection<Object[]> data()
	{
		List<Object[]> ret = new ArrayList<Object[]>();
		for(TestSuiteEntry entry : config.getTestSuite())
		{
			Object[] array = new Object[3];
			array[0] = entry.getSemantics();
			array[1] = asRealizations(entry.getSentences());
			array[2] = entry.getId();
			ret.add(array);
		}

		return ret;
	}
	
	
	/**
	 * Tests if the given list of realizations contains the given realizations regarding lemma surface form only.
	 * @param realizations
	 * @param realization
	 * @return
	 */
	private static boolean contains(List<? extends SyntacticRealization> realizations, SyntacticRealization realization)
	{
		for(SyntacticRealization otherReal : realizations)
		{
			List<Lemma> otherLemmas = otherReal.getLemmas();
			List<Lemma> lemmas = realization.getLemmas();
			if (otherLemmas.size()!=lemmas.size())
				continue;
			
			boolean allFound=true;
			for (int i=0; i<lemmas.size(); i++)
				if (!otherLemmas.get(i).getValue().equals(lemmas.get(i).getValue()))
				{
					allFound=false;
					break;
				}
			if (allFound)
				return true;
		}
		return false;
	}


	/**
	 * Returns the sentences as realizations. (should be done when parsing TestSuiteEntry?)
	 * @param sentences
	 * @return
	 */
	private static List<? extends SyntacticRealization> asRealizations(List<String> sentences)
	{
		List<JeniRealization> ret = new ArrayList<JeniRealization>();
		for(String sentence : sentences)
		{
			JeniRealization real = new JeniRealization();
			for(String lemma : sentence.split(" "))
				real.add(new Lemma(lemma));
			ret.add(real);
		}
		return ret;
	}


	public GeneratorTestParametrized(Semantics input, List<? extends SyntacticRealization> expected, String str)
	{
		this.input = input;
		this.expected = expected;
	}

}
