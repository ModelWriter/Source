package synalp.commons.input;

import java.util.*;

import synalp.commons.output.*;
import synalp.commons.semantics.Semantics;
import synalp.commons.utils.Utils;
import synalp.generation.Generator;
import synalp.generation.jeni.*;


/**
 * A TestSuiteEntry is an entry in a test suite. A strict entry is an entry that says that the
 * semantics must exactly produce the set of sentences, a non-strict entry (or loose entry) is an
 * entry that says that the semantics must at least produce the set of sentences, additional ones
 * may be produced. Moreover an entry can be morph-based, that is it tests the actual
 * morphologically realized sentence, or lemma-based (by default) that is it only tests the lemmas.
 * This class contains both the data structure and methods to evaluate a Generator by returning a
 * TestResult.
 * @author Alexandre Denis
 */
public class TestSuiteEntry
{
	private String id;
	private boolean morph;
	private boolean strict;
	private Semantics semantics;
	private List<String> sentences;


	/**
	 * Creates a new entry which is not strict.
	 * @param id
	 * @param semantics
	 * @param sentences
	 */
	public TestSuiteEntry(String id, Semantics semantics, List<String> sentences)
	{
		this.id = id;
		this.semantics = semantics;
		this.sentences = sentences;
	}


	/**
	 * Copy constructor.
	 * @param entry
	 */
	public TestSuiteEntry(TestSuiteEntry entry)
	{
		this.id = entry.getId();
		this.strict = entry.isStrict();
		this.semantics = new Semantics(entry.getSemantics());
		this.sentences = new ArrayList<String>(entry.getSentences());
	}


	/**
	 * Performs a test with this TestSuiteEntry and given generator.
	 * @param generator
	 * @return a new TestResult
	 */
	public TestResult performTest(Generator generator)
	{
		TestResult ret = new TestResult();
		ret.setTest(this);

		List<? extends SyntacticRealization> results = generator.generate(semantics);
		List<String> resultsSurface = getSurface(results, morph);

		// temporary, discuss with Laura about moving GenerationReport to Generator interface
		if (generator instanceof JeniGenerator)
			ret.setReport(((JeniGenerator) generator).getGenerationReport());
		
		// short test if there is no expected sentences
		if (sentences.isEmpty())
		{
			if (resultsSurface.isEmpty())
			{
				ret.setMessage("No realizations");
				ret.setType(TestResultType.FAIL);
			}
			else ret.setType(TestResultType.PASS);
			return ret;
		}

		// otherwise compare the expected sentences to the results, compare them at the lemmatized or inflected level
		List<? extends SyntacticRealization> expected = asRealizations(sentences);
		List<String> expectedSurface = getSurface(expected, morph);

		for(String expectedReal : expectedSurface)
			if (!resultsSurface.contains(expectedReal))
			{
				List<String> toDisplay = new ArrayList<String>(new HashSet<String>(resultsSurface));
				Collections.sort(toDisplay);
				String msg = id + " output \"" + expectedReal + "\" is not produced (produced: " + toDisplay + ")";
				ret.setMessage(msg);
				ret.setType(TestResultType.FAIL);
				return ret;
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
				ret.setMessage(msg);
				ret.setType(TestResultType.FAIL);
				return ret;
			}

		}

		ret.setType(TestResultType.PASS);
		return ret;
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


	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}


	/**
	 * @return the semantics
	 */
	public Semantics getSemantics()
	{
		return semantics;
	}


	/**
	 * @param semantics the semantics to set
	 */
	public void setSemantic(Semantics semantics)
	{
		this.semantics = semantics;
	}


	/**
	 * @return the sentences
	 */
	public List<String> getSentences()
	{
		return sentences;
	}


	/**
	 * @param sentences the sentences to set
	 */
	public void setSentences(List<String> sentences)
	{
		this.sentences = sentences;
	}


	/**
	 * Returns a String representation of this TestSuiteEntry.
	 */
	@Override
	public String toString()
	{
		return (isStrict() ? "@strict " : "") + id + "\nsemantics:" + semantics + "\nsentence:" + sentences;
	}


	/**
	 * @return the strict
	 */
	public boolean isStrict()
	{
		return strict;
	}


	/**
	 * @param strict the strict to set
	 */
	public void setStrict(boolean strict)
	{
		this.strict = strict;
	}


	/**
	 * @return the morph
	 */
	public boolean isMorph()
	{
		return morph;
	}


	/**
	 * @param morph the morph to set
	 */
	public void setMorph(boolean morph)
	{
		this.morph = morph;
	}
}
