package synalp.generation.jeni.tests;

import org.junit.*;
import org.junit.runners.MethodSorters;

import synalp.commons.semantics.*;
import synalp.commons.tests.GeneratorTest;
import synalp.generation.Generator;
import synalp.generation.configuration.*;
import synalp.generation.jeni.*;


/**
 * Tests JeniGenerator.
 * @author Alexandre Denis
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JeniGeneratorTest extends GeneratorTest
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


	/**
	 * This test is kept as an example.
	 */
	@Test
	public void test1_Bill_sleep()
	{
		System.out.println("\n**** Bill sleep");
		test(new JeniGenerator(GeneratorConfigurations.getConfig("minimal")),
				Semantics.readSemantics("A0_1:sleep(e0 a0) A0_1:bill(a0)"), "Bill sleep");
	}


	@Test
	public void test1_probabilistic()
	{
		System.out.println("\n**** Test Probabilistic");
		GeneratorConfiguration config = GeneratorConfigurations.getConfig("probabilistic");
		for(int i=0; i<20; i++)
		{
			System.out.println("\n==== Beam size: "+i+" ====");
			config.setOption("beam_size", String.valueOf(i));
			testJeni(config);	
		}
	}


	@Test
	@SuppressWarnings("javadoc")
	public void test2_SuiteTranssem()
	{
		testJeni(GeneratorConfigurations.getConfig("transsem"));
	}


	@Test
	@SuppressWarnings("javadoc")
	public void test3_SuiteFrench()
	{
		testJeni(GeneratorConfigurations.getConfig("french"));
	}


	@Test
	@SuppressWarnings("javadoc")
	public void test4_SuiteSemXTAG2()
	{
		testJeni(GeneratorConfigurations.getConfig("semxtag"));
	}
	

	@Test
	@SuppressWarnings("javadoc")
	public void test6_KBGen()
	{
		testJeni(GeneratorConfigurations.getConfig("kbgen"), true);
	}
}
