package synalp.generation;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.*;

import synalp.commons.input.TestSuiteEntry;
import synalp.commons.output.*;
import synalp.commons.utils.*;
import synalp.commons.utils.exceptions.TimeoutException;
import synalp.commons.utils.loggers.LoggerConfiguration;
import synalp.generation.configuration.*;
import synalp.generation.jeni.JeniGenerator;
import synalp.generation.morphology.MissingLexemPolicy;
import synalp.generation.ranker.NgramRanker;
import static org.kohsuke.args4j.ExampleMode.ALL;

/**
 * Main command line generator. This class enables to run the Jeni generator by writing the results
 * of each test in an output directory. It only works for the Jeni generator for now, and we may
 * change this class to use the GeneratorApplication which is meant to be more generic.
 * @author Alexandre Denis
 */
public class GeneratorMain
{
	private static Logger logger = Logger.getLogger(GeneratorMain.class);

	@Option(name = "-g", usage = "grammar file", metaVar = "<xml file>", required = false)
	private File grammarFile = null;

	@Option(name = "-l", usage = "lexicon file", metaVar = "<xml|lex file>", required = false)
	private File synLexiconFile = null;

	@Option(name = "-s", usage = "testsuite file in jeni format", metaVar = "<jeni file>", required = false)
	private File testSuiteFile = null;

	@Option(name = "-m", usage = "morph file", metaVar = "<xml|mph file>")
	private File morphLexiconFile = null;

	@Option(name = "-o", usage = "output directory", metaVar = "<output dir>", required = false)
	private File outputDirectory = null;

	@Option(name = "-lm", usage = "language model", metaVar = "<lm file>", required = false)
	private File lmFile = null;

	@Option(name = "-bs", usage = "beam size (default 5)", metaVar = "<integer>", required = false)
	private int beamSize = 5;

	@Option(name = "-c", usage = "configuration name, if set overrides all other parameters", metaVar = "<string>", required = false)
	private String configName = null;

	@Argument(usage = "other options, see format in default.options.properties", metaVar = "option=value")
	private List<String> options = new ArrayList<String>();

	static
	{
		LoggerConfiguration.init();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			new GeneratorMain().run(args);
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}


	/**
	 * Runs the generator.
	 * @param args
	 * @throws Exception
	 */
	private void run(String[] args) throws Exception
	{
		// sets the stderr to stdout to avoid interleaved outputs in console mode
		// we may disable it in the future
		System.setErr(System.out);

		CmdLineParser parser = new CmdLineParser(this);
		parser.setUsageWidth(80);

		try
		{
			parser.parseArgument(args);
			if (configName == null)
				runNoConfig(parser);
			else runWithConfig();
		}
		catch (CmdLineException e)
		{
			displayUsage(parser, e);
			return;
		}

	}


	/**
	 * Runs the generator with a configuration.
	 * @throws IOException
	 */
	private void runWithConfig() throws IOException
	{
		GeneratorConfiguration config = GeneratorConfigurations.getConfig(configName);
		System.out.println(config.printConfiguration());
		config.load();
		
		try{ // if the morph file is not specified, assume that the surface expression is the lemma itself. 
			config.getMorphLexicon();
		}
		catch (Exception ConfigurationException) {
			GeneratorOption.MISSING_LEXEM_POLICY = MissingLexemPolicy.OUTPUT_LEMMA;
		}
		
		Generator generator = Generator.createGenerator(config);
	
		if (logger.isInfoEnabled())
			logger.info(GeneratorOption.getStatus());

		int index = 1;
		for(TestSuiteEntry entry : config.getTestSuite())
		{
			System.out.println("\n\nProcessing " + (index++) + "/" + config.getTestSuite().size()+ " : \n\t"+entry.getSemantics());
			String resultsString = run(generator, entry);
			//System.out.println("\nResults = \n"+resultsString);
			System.out.println(resultsString);
		}
	}


	/**
	 * Runs the generator without a configuration.
	 * @param parser
	 * @throws Exception
	 */
	private void runNoConfig(CmdLineParser parser) throws Exception
	{
		if (synLexiconFile == null || grammarFile == null || testSuiteFile == null)
			throw new CmdLineException(parser, "Error: missing grammar or lexicon or testsuite");

		if (outputDirectory!=null && outputDirectory.isFile() && !outputDirectory.isDirectory())
			throw new Exception("Unable to write to output directory " + outputDirectory + ", it is not a directory");

		System.out.println("grammar: " + grammarFile);
		System.out.println("test suite: " + testSuiteFile);
		System.out.println("syn lexicon: " + synLexiconFile);
		System.out.println("morph lexicon: " + morphLexiconFile);
		System.out.println("lm file: " + lmFile);
		System.out.println("output dir: " + outputDirectory);
		System.out.println("options: " + options);
		System.out.println("lib: " + System.getProperty("java.library.path"));

		GeneratorConfiguration config = new GeneratorConfiguration("custom");
		config.setGrammarFile(grammarFile);
		config.setSyntacticLexiconFile(synLexiconFile);
		config.setTestSuiteFile(testSuiteFile);
		config.setMorphLexiconFile(morphLexiconFile);
		config.setOptions(getOptionsMap());
		config.load();
		
		if (morphLexiconFile==null) { // if the morph file is not specified, assume that the surface expression is the lemma itself. 
			GeneratorOption.MISSING_LEXEM_POLICY = MissingLexemPolicy.OUTPUT_LEMMA;
		}

		if (outputDirectory!=null) {
			Utils.delete(outputDirectory);
			outputDirectory.mkdirs();
		}

		JeniGenerator generator = new JeniGenerator(config);
		
		if (lmFile != null)
			generator.setRanker(new NgramRanker(lmFile.getAbsolutePath(), beamSize, "jni"));
		
		int index = 1;
		for(TestSuiteEntry entry : config.getTestSuite())
		{
			System.out.println("\n\nProcessing " + (index++) + "/" + config.getTestSuite().size()+ " : \n\t"+entry.getSemantics());
			String resultsString = run(generator, entry);
			//System.out.println("\nResults = \n"+resultsString);
			System.out.println(resultsString);
		}

	}


	/**
	 * Runs the generator on the given entry and writes the result to a file in the output
	 * directory.
	 * @param generator the generator
	 * @param entry
	 * @return String containing the results.
	 * @throws IOException
	 */
	private String run(Generator generator, TestSuiteEntry entry) throws IOException
	{
		String name = entry.getId();
		if (name.length() > 255)
			name = name.substring(0, 255);

		File outputDir = null;
		RandomAccessFile output = null;
		if (outputDirectory!=null) {
			outputDir = new File(outputDirectory + File.separator + name);
			Utils.delete(outputDir);
			outputDir.mkdirs();
			output = new RandomAccessFile(outputDir + File.separator + "responses", "rw");
		}
		
		
		List<? extends SyntacticRealization> results;

		StringBuilder resultsString = new StringBuilder();
		
		Perf.logStart("generation");
		try
		{
			results = generator.generate(entry.getSemantics());
			resultsString.append("\n" + results.size() + " Results (" + Perf.formatTime(Perf.logEnd("generation")) + "):\n");
		}
		catch (TimeoutException e)
		{
			results = new ArrayList<SyntacticRealization>();
			resultsString.append("\n\tTimeout (" + Perf.formatTime(Perf.logEnd("generation")) + ")");
		}

		for(SyntacticRealization result : results) {
			for(MorphRealization sentence : result.getMorphRealizations()) {
				String formattedSentence = sentence.asString();
				if (output!=null) {
					output.writeBytes(formattedSentence + "\n");
				}
				resultsString.append("\t"+formattedSentence + "\n");
			}
		}
		if (output!=null) {
			resultsString.append("\nResults also written to file " +outputDir + File.separator + "responses"+ "\n\n");
			output.close();
		}
		return resultsString.toString();
	}


	/**
	 * Returns free options as a map.
	 * @return
	 */
	private Map<String, String> getOptionsMap()
	{
		StringBuilder str = new StringBuilder();
		for(String option : options)
			str.append(option).append("\n");

		try (ByteArrayInputStream stream = new ByteArrayInputStream(str.toString().getBytes()))
		{
			Properties props = new Properties();
			props.load(stream);
			Map<String, String> ret = new HashMap<>();
			for(String key : props.stringPropertyNames())
				ret.put(key, props.getProperty(key));
			return ret;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return new HashMap<>();
		}
	}


	/**
	 * Shows usage.
	 * @param parser
	 * @param e
	 */
	private void displayUsage(CmdLineParser parser, CmdLineException e)
	{
		System.err.println(e.getMessage());
		System.err.println("./run_generator.sh [options...] arguments...");
		parser.printUsage(System.err);
		System.err.println();
		System.err.println("  Example: java GeneratorMain" + parser.printExample(ALL) + " TIMEOUT=1000 CASE_DEPENDENT=true");
	}
}
