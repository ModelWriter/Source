package synalp.generation.configuration;

import java.io.File;
import java.util.*;

import synalp.commons.grammar.*;
import synalp.commons.input.*;
import synalp.commons.lexicon.*;
import synalp.generation.morphology.*;

/**
 * This class is meant to represent all options and resources of a Generator. It also contains
 * static methods and repositories for the main types of resources, namely grammar, syntactic
 * lexicon, morphological lexicon and test suite.
 * @author Alexandre Denis
 */
public class GeneratorConfiguration
{
	private static final String GRAMMAR_KEY = "grammar";
	private static final String SYN_LEXICON_KEY = "lexicon";
	private static final String MORPH_LEXICON_KEY = "morph";
	private static final String TEST_SUITE_KEY = "testsuite";

	private final String name;
	private Map<String, String> options;
	private Map<String, File> resources;

	private static Map<File, Grammar> loadedGrammars = new HashMap<File, Grammar>();
	private static Map<File, TestSuite> loadedTestsuites = new HashMap<File, TestSuite>();
	private static Map<File, MorphLexicon> loadedMorphLexicons = new HashMap<File, MorphLexicon>();
	private static Map<File, SyntacticLexicon> loadedSynLexicons = new HashMap<File, SyntacticLexicon>();


	/**
	 * Loads the given lexicon file in XML or LEX format. If the given File is not a lexicon or is
	 * not found an exception is caught and print to stderr.
	 * @param file a lexicon file in XML or LEX format, the extension distinguishing the type.
	 * @param forceReload if true, the lexicon will be reloaded, if false, the loaded lexicon will
	 *            be returned or loaded for the first time.
	 * @return a SyntacticLexicon or null if there has been an exception while reading the file
	 */
	public static SyntacticLexicon loadSynLexicon(File file, boolean forceReload)
	{
		if (loadedSynLexicons.containsKey(file) && !forceReload)
			return loadedSynLexicons.get(file);

		try
		{
			SyntacticLexicon ret = SyntacticLexiconReader.readLexicon(file);
			loadedSynLexicons.put(file, ret);
			return ret;
		}
		catch (Exception e)
		{
			System.err.println("Error: exception while reading lexicon " + file + " : " + e.getMessage());
		}

		return null;
	}


	/**
	 * Returns the syntactic lexicon found for given configuration name. If it has not been loaded
	 * before, loads it.
	 * @param configuration
	 * @return
	 */
	public static SyntacticLexicon getSyntacticLexicon(String configuration)
	{
		return GeneratorConfigurations.getConfig(configuration).getSyntacticLexicon();
	}


	/**
	 * Loads the given grammar file in XML. If the given File is not a grammar or is not found an
	 * exception is caught and print to stderr.
	 * @param file a grammar file in XML format.
	 * @param forceReload if true, the grammar will be reloaded, if false, the loaded grammar will
	 *            be returned or loaded for the first time.
	 * @return a Grammar or null if there has been an exception while reading the file
	 */
	public static Grammar loadGrammar(File file, boolean forceReload)
	{
		if (loadedGrammars.containsKey(file) && !forceReload)
			return loadedGrammars.get(file);

		try
		{
			Grammar ret = GrammarReader.readGrammar(file);
			loadedGrammars.put(file, ret);
			return ret;
		}
		catch (Exception e)
		{
			System.err.println("Error: exception while reading grammar " + file + " : " + e.getMessage());
		}

		return null;
	}


	/**
	 * Returns the grammar found for given configuration name. If it has not been loaded before,
	 * loads it.
	 * @param configuration
	 * @return
	 */
	public static Grammar getGrammar(String configuration)
	{
		return GeneratorConfigurations.getConfig(configuration).getGrammar();
	}


	/**
	 * Returns the grammar file found for given configuration name.
	 * @param configuration
	 * @return
	 */
	public static File getGrammarFile(String configuration)
	{
		return GeneratorConfigurations.getConfig(configuration).getGrammarFile();
	}


	/**
	 * Loads the given test suite file in Jeni format. If the given File is not in Jeni format or is
	 * not found an exception is caught and print to stderr.
	 * @param file a lexicon file in XML format.
	 * @param forceReload
	 * @return a TestSuite or null if there has been an exception while reading the file
	 */
	public static TestSuite loadTestSuite(File file, boolean forceReload)
	{
		if (loadedTestsuites.containsKey(file) && !forceReload)
			return loadedTestsuites.get(file);

		try
		{
			TestSuite ret = TestSuiteReader.readTestSuite(file);
			loadedTestsuites.put(file, ret);
			return ret;
		}
		catch (Exception e)
		{
			System.err.println("Error: exception while reading test suite " + file + " : " + e.getMessage());
		}

		return null;
	}


	/**
	 * Loads the given morph lexicon in XML or MPH format.
	 * @param file
	 * @param forceReload
	 * @return a morph lexicon
	 */
	public static MorphLexicon loadMorphLexicon(File file, boolean forceReload)
	{
		if (loadedMorphLexicons.containsKey(file) && !forceReload)
			return loadedMorphLexicons.get(file);

		if (file == null)
			return new MorphLexicon();

		try
		{
			MorphLexicon ret = MorphLexiconReader.readLexicon(file);
			loadedMorphLexicons.put(file, ret);
			return ret;
		}
		catch (Exception e)
		{
			System.err.println("Error: exception while reading morph lexicon " + file + " : " + e.getMessage());
		}
		return null;
	}


	/**
	 * @return the loadedGrammars
	 */
	public static Map<File, Grammar> getLoadedGrammars()
	{
		return loadedGrammars;
	}


	/**
	 * @param loadedGrammars the loadedGrammars to set
	 */
	public static void setLoadedGrammars(Map<File, Grammar> loadedGrammars)
	{
		GeneratorConfiguration.loadedGrammars = loadedGrammars;
	}


	/**
	 * @return the loadedTestsuites
	 */
	public static Map<File, TestSuite> getLoadedTestsuites()
	{
		return loadedTestsuites;
	}


	/**
	 * @param loadedTestsuites the loadedTestsuites to set
	 */
	public static void setLoadedTestsuites(Map<File, TestSuite> loadedTestsuites)
	{
		GeneratorConfiguration.loadedTestsuites = loadedTestsuites;
	}


	/**
	 * @return the loadedMorphLexicons
	 */
	public static Map<File, MorphLexicon> getLoadedMorphLexicons()
	{
		return loadedMorphLexicons;
	}


	/**
	 * @param loadedMorphLexicons the loadedMorphLexicons to set
	 */
	public static void setLoadedMorphLexicons(Map<File, MorphLexicon> loadedMorphLexicons)
	{
		GeneratorConfiguration.loadedMorphLexicons = loadedMorphLexicons;
	}


	/**
	 * @return the loadedSynLexicons
	 */
	public static Map<File, SyntacticLexicon> getLoadedSynLexicons()
	{
		return loadedSynLexicons;
	}


	/**
	 * @param loadedSynLexicons the loadedSynLexicons to set
	 */
	public static void setLoadedSynLexicons(Map<File, SyntacticLexicon> loadedSynLexicons)
	{
		GeneratorConfiguration.loadedSynLexicons = loadedSynLexicons;
	}


	/**
	 * @param name
	 */
	public GeneratorConfiguration(String name)
	{
		this.name = name;
		this.options = new HashMap<String, String>();
		this.resources = new HashMap<String, File>();
	}


	/**
	 * Loads this configuration. Both setup the static options values in GeneratorOption and loads
	 * the grammar, testsuite, morph and syntactic lexicons if possible. Other resources such as
	 * module dependent resources need to be loaded by their respective module.
	 */
	public void load()
	{
		GeneratorOption.setupOptions(this);
		getGrammarNoException();
		getTestSuiteNoException();
		getMorphLexiconNoException();
		getSyntacticLexiconNoException();
	}

	/**
	 * Added by Bikash.
	 */
	public void reloadLexicon() {
		loadSynLexicon(resources.get(SYN_LEXICON_KEY), true);
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * Returns an option given its name.
	 * @param name
	 * @return the value of the option
	 * @throw ConfigurationException if not found
	 */
	public String getOption(String name)
	{
		if (options.containsKey(name))
			return options.get(name);
		else throw new ConfigurationException("Error: option '"+name+"' not found; defined options are: "+options.keySet());
	}
	
	
	/**
	 * Returns an option given its name cast as a float
	 * @param name
	 * @return the value of the option
	 * @throw ConfigurationException if not found or if not a float
	 */
	public float getFloatOption(String name)
	{
		if (options.containsKey(name))
		{
			try
			{
				return Float.parseFloat(options.get(name));
			}
			catch(NumberFormatException e)
			{
				throw new ConfigurationException("Error: wrong type of option '"+name+"' with value '"+options.get(name)+"' (expecting float)");
			}
		}
		else throw new ConfigurationException("Error: option '"+name+"' not found; defined options are: "+options.keySet());
	}
	
	
	/**
	 * Returns an option given its name cast as an integer.
	 * @param name
	 * @return the value of the option
	 * @throw ConfigurationException if not found or if not an integer
	 */
	public int getIntOption(String name)
	{
		if (options.containsKey(name))
		{
			try
			{
				return Integer.parseInt(options.get(name));
			}
			catch(NumberFormatException e)
			{
				throw new ConfigurationException("Error: wrong type of option '"+name+"' with value '"+options.get(name)+"' (expecting integer)");
			}
		}
		else throw new ConfigurationException("Error: option '"+name+"' not found; defined options are: "+options.keySet());
	}
	
	
	/**
	 * Returns an option given its name cast as a boolean.
	 * If the option has a value different from "true", this method returns false.
	 * @param name
	 * @return the value of the option
	 * @throw ConfigurationException if not found
	 */
	public boolean getBooleanOption(String name)
	{
		if (options.containsKey(name))
			return Boolean.parseBoolean(options.get(name));
		else throw new ConfigurationException("Error: option '"+name+"' not found; defined options are: "+options.keySet());
	}
	
	
	/**
	 * @return the options
	 */
	public Map<String, String> getOptions()
	{
		return options;
	}


	/**
	 * Sets the given option.
	 * @param name
	 * @param value
	 */
	public void setOption(String name, String value)
	{
		options.put(name, value);
	}


	/**
	 * @param options the options to set
	 */
	public void setOptions(Map<String, String> options)
	{
		this.options = options;
	}


	/**
	 * Tests if this config has the given option set.
	 * @param name
	 * @return wether this config has the given option set
	 */
	public boolean hasOption(String name)
	{
		return options.containsKey(name);
	}


	/**
	 * @return the resources
	 */
	public Map<String, File> getResources()
	{
		return resources;
	}


	/**
	 * Tests if the grammar of this configuration has been defined.
	 * @return true if this configuration defines a grammar
	 */
	public boolean hasGrammar()
	{
		return resources.containsKey(GRAMMAR_KEY);
	}


	/**
	 * Tests if the syntactic lexicon of this configuration has been defined.
	 * @return true if this configuration defines a syntactic lexicon
	 */
	public boolean hasSynLexicon()
	{
		return resources.containsKey(SYN_LEXICON_KEY);
	}


	/**
	 * Tests if the morphological lexicon of this configuration has been defined.
	 * @return true if this configuration defines a morphological lexicon
	 */
	public boolean hasMorphLexicon()
	{
		return resources.containsKey(MORPH_LEXICON_KEY);
	}


	/**
	 * Tests if the test suite of this configuration has been defined.
	 * @return true if this configuration defines a test suite
	 */
	public boolean hasTestSuite()
	{
		return resources.containsKey(TEST_SUITE_KEY);
	}


	/**
	 * Returns the resource with given name. Throws a ConfigurationException if not found.
	 * @param name
	 * @return a File
	 */
	public File getResource(String name)
	{
		if (!resources.containsKey(name))
			throw new ConfigurationException("Error: unable to find resource named '" + name + "' in configuration '" + this.name + "'");
		else return resources.get(name);
	}


	/**
	 * Sets the grammar file of this configuration.
	 * @param file
	 */
	public void setGrammarFile(File file)
	{
		setResource(GRAMMAR_KEY, file);
	}


	/**
	 * Sets the syntactic lexicon file of this configuration.
	 * @param file
	 */
	public void setSyntacticLexiconFile(File file)
	{
		setResource(SYN_LEXICON_KEY, file);
	}


	/**
	 * Sets the morphological lexicon file of this configuration.
	 * @param file
	 */
	public void setMorphLexiconFile(File file)
	{
		setResource(MORPH_LEXICON_KEY, file);
	}


	/**
	 * Sets the test suite file of this configuration.
	 * @param file
	 */
	public void setTestSuiteFile(File file)
	{
		setResource(TEST_SUITE_KEY, file);
	}


	/**
	 * Sets the given resource.
	 * @param name
	 * @param file
	 */
	public void setResource(String name, File file)
	{
		resources.put(name, file);
	}


	/**
	 * @param resources the resources to set
	 */
	public void setResources(Map<String, File> resources)
	{
		this.resources = resources;
	}


	/**
	 * Tests if this config has the given resource set.
	 * @param name
	 * @return wether this config has the given resource set
	 */
	public boolean hasResource(String name)
	{
		return resources.containsKey(name);
	}


	/**
	 * Returns the morph lexicon of this configuration. Throws a ConfigurationException if not
	 * found.
	 * @return the morph lexicon of this configuration if it exists
	 */
	public MorphLexicon getMorphLexicon()
	{
		if (!resources.containsKey(MORPH_LEXICON_KEY))
			throwMissingResource(MORPH_LEXICON_KEY);
		return loadMorphLexicon(resources.get(MORPH_LEXICON_KEY), false);
	}


	/**
	 * Returns the syntactic lexicon of this configuration. Throws a ConfigurationException if not
	 * found.
	 * @return the syntactic lexicon of this configuration if it exists
	 */
	public SyntacticLexicon getSyntacticLexicon()
	{
		return loadSynLexicon(getSyntacticLexiconFile(), false);
	}


	/**
	 * Returns the syntactic lexicon file of this configuration. Throws a ConfigurationException if
	 * not found.
	 * @return the syntactic lexicon file of this configuration if it exists
	 */
	public File getSyntacticLexiconFile()
	{
		if (!resources.containsKey(SYN_LEXICON_KEY))
			throwMissingResource(SYN_LEXICON_KEY);
		return resources.get(SYN_LEXICON_KEY);
	}


	/**
	 * Returns the testsuite of this configuration. Throws a ConfigurationException if not found.
	 * @return the testsuite of this configuration if it exists
	 */
	public TestSuite getTestSuite()
	{
		if (!resources.containsKey(TEST_SUITE_KEY))
			throwMissingResource(TEST_SUITE_KEY);
		return loadTestSuite(resources.get(TEST_SUITE_KEY), false);
	}


	/**
	 * Returns the grammar file of this configuration. Throws a ConfigurationException if not found.
	 * @return the grammar file of this configuration if it exists
	 */
	public File getGrammarFile()
	{
		if (!resources.containsKey(GRAMMAR_KEY))
			throwMissingResource(GRAMMAR_KEY);
		return resources.get(GRAMMAR_KEY);
	}


	/**
	 * Returns the grammar of this configuration. Throws a ConfigurationException if not found.
	 * @return the grammar of this configuration if it exists
	 */
	public Grammar getGrammar()
	{
		return loadGrammar(getGrammarFile(), false);
	}


	/**
	 * Returns the morph lexicon of this configuration.
	 * @return the morph lexicon of this configuration if it exists
	 */
	public MorphLexicon getMorphLexiconNoException()
	{
		if (!resources.containsKey(MORPH_LEXICON_KEY))
			return null;
		else return loadMorphLexicon(resources.get(MORPH_LEXICON_KEY), false);
	}


	/**
	 * Returns the syntactic lexicon of this configuration.
	 * @return the syntactic lexicon of this configuration if it exists
	 */
	public SyntacticLexicon getSyntacticLexiconNoException()
	{
		if (!resources.containsKey(SYN_LEXICON_KEY))
			return null;
		else return loadSynLexicon(resources.get(SYN_LEXICON_KEY), false);
	}


	/**
	 * Returns the testsuite of this configuration.
	 * @return the testsuite of this configuration if it exists
	 */
	public TestSuite getTestSuiteNoException()
	{
		if (!resources.containsKey(TEST_SUITE_KEY))
			return null;
		else return loadTestSuite(resources.get(TEST_SUITE_KEY), false);
	}


	/**
	 * Returns the grammar of this configuration.
	 * @return the grammar of this configuration if it exists
	 */
	public Grammar getGrammarNoException()
	{
		if (!resources.containsKey(GRAMMAR_KEY))
			return null;
		else return loadGrammar(resources.get(GRAMMAR_KEY), false);
	}


	/**
	 * Returns a multiline textual representation of this configuration.
	 * @return a String
	 */
	public String printConfiguration()
	{
		StringBuilder ret = new StringBuilder();
		ret.append("config:\n\t").append(name).append("\n");
		ret.append("options:\n");
		for(String option : options.keySet())
			ret.append("\t").append(option).append("=").append(options.get(option)).append("\n");
		ret.append("resources:\n");
		for(String resource : resources.keySet())
			ret.append("\t").append(resource).append("=").append(resources.get(resource)).append("\n");
		return ret.toString().trim();
	}


	/**
	 * Throws a missing resource configuration exception.
	 * @param resource the name of the missing resource
	 */
	private void throwMissingResource(String resource)
	{
		throw new ConfigurationException("Error: unable to find resource named '" + resource + "' in configuration '" + name + "'");
	}


	@Override
	public String toString()
	{
		return name;
	}


	/**
	 * Returns the stored value of the given option name or null if not found.
	 * @param option
	 * @return null if not found
	 */
	public String getValue(String option)
	{
		return options.get(option);
	}
	
	
	public File getTestsuiteFile() {
		return getResource(GeneratorConfiguration.TEST_SUITE_KEY);
	}
}
