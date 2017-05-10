package synalp.generation.probabilistic.tests;

import org.junit.*;
import org.junit.runners.MethodSorters;

import synalp.commons.semantics.*;
import synalp.commons.tests.GeneratorTest;
import synalp.generation.Generator;
import synalp.generation.configuration.*;
import synalp.generation.jeni.*;

/**
 * Tests Probabilistic JeniGenerator.
 * @author Andrés Luna
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PJeniTests extends GeneratorTest
{
	/**
	 * Tests Jeni on the given configuration (includes grammar, lexicon and test suite).
	 * @param bundle
	 */
	public void testJeni(GeneratorConfiguration config)
	{
		testJeni(config, false);
	}


	/**
	 * Tests Jeni on the given configuration (includes grammar, lexicon and test suite).
	 * @param bundle
	 * @param catchTimeout
	 */
	private void testJeni(GeneratorConfiguration config, boolean catchTimeout)
	{
		config.load();
		test(Generator.createGenerator(config), config.getTestSuite(), catchTimeout);
	}

	@Test
	public void test1()
	{
		System.out.println("\n**** PJeni Test 1");
		GeneratorConfiguration config = GeneratorConfigurations.getConfig("probabilistic");
		for(int i = 0; i < 20; i++)
		{
			System.out.println("\n==== Beam size: " + i + " ====");
			config.setOption("beam_size", String.valueOf(i));
			testJeni(config);
		}
	}


	@Test
	public void test2_demo_suite()
	{
		System.out.println("\n**** PJeni Test 2: Demo suite");
		GeneratorConfiguration config = GeneratorConfigurations.getConfig("probabilistic_demosuite");
		testJeni(config);

	}

}
