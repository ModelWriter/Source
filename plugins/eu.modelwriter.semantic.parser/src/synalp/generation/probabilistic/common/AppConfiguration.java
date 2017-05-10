package synalp.generation.probabilistic.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import synalp.generation.configuration.GeneratorConfiguration;
import synalp.generation.configuration.GeneratorConfigurations;

public class AppConfiguration
{

	String builtinTestsuiteName;
	boolean isBuiltinTest;

	private GeneratorConfiguration generationConfig;
	Map<String, String> resourcesFiles;
	Map<String, Boolean> infoToOutput;
	private int userInputType = 0;//userInput or testsuite
	String userInput;
	static int run = 0;
	private boolean verboseOutput;


	public AppConfiguration()
	{
		this.resourcesFiles = new HashMap();
		//load bundled sources for having basic configuration, then replace input files with the desired ones
		this.setGenerationConfig(GeneratorConfigurations.getConfig("probabilistic_demosuite"));
		this.resourcesFiles.put("grammar", getGenerationConfig().getGrammarFile().getAbsolutePath());
		this.resourcesFiles.put("lexicon", getGenerationConfig().getSyntacticLexiconFile().getAbsolutePath());
		this.resourcesFiles.put("testsuite", getGenerationConfig().getTestsuiteFile().getAbsolutePath());

		this.infoToOutput = new HashMap();
		this.isBuiltinTest = false;
		this.setVerboseOutput(true);

	}


	public void setBeamSize(String beam)
	{
		getGenerationConfig().setOption("beam_size", beam);
	}


	void createConfigFromTestsuite(String configName)
	{

		this.resourcesFiles.put("grammar", getGenerationConfig().getGrammarFile().getName());
		this.resourcesFiles.put("lexicon", getGenerationConfig().getSyntacticLexiconFile().getName());
		this.resourcesFiles.put("testsuite", getGenerationConfig().getTestsuiteFile().getName());
		this.isBuiltinTest = true;
		this.builtinTestsuiteName = configName;

	}


	public void setConfiguration(String grammarFilename, String lexiconFilename)
	{

		getGenerationConfig().setGrammarFile(new File(grammarFilename));
		this.resourcesFiles.put("grammar", grammarFilename);

		getGenerationConfig().setSyntacticLexiconFile(new File(lexiconFilename));
		this.resourcesFiles.put("lexicon", lexiconFilename);
	}


	public void setConfiguration(String grammarFilename, String lexiconFilename, String testsuiteFilename)
	{
		File grammarFile = new File(grammarFilename);
		getGenerationConfig().setGrammarFile(grammarFile);
		this.resourcesFiles.put("grammar", grammarFilename);

		getGenerationConfig().setSyntacticLexiconFile(new File(lexiconFilename));
		this.resourcesFiles.put("lexicon", lexiconFilename);

		getGenerationConfig().setTestSuiteFile(new File(testsuiteFilename));
		this.resourcesFiles.put("testsuite", testsuiteFilename);
	}


	public void setUserInput(int type, String theInput) throws IOException
	{

		this.setUserInputType(type);
		this.userInput = theInput;
		createAndConfigureInputFileForSemantics(theInput);
	}


	public void setUserInputForPredefinedSample(int type, String name, String theInput) throws IOException
	{
		if (type == 1)
		{
			this.setLexiconSource("resources/probabilistic/koda.lexicon.28072015.lex");
			System.out.println("Predefined input chosen -> using default lexicon source: " + "resources/probabilistic/koda.lexicon.28072015.lex");
		}

		this.setUserInputType(type);
		this.userInput = theInput;
		createAndConfigureInputFileForSemantics(name, theInput);
	}


	public void createAndConfigureInputFileForSemantics(String inputSemantics) throws IOException
	{
		++run;
		BufferedWriter buffOutput = new BufferedWriter(new FileWriter(new File("TmpFile" + run + ".geni")));
		buffOutput.write("test-user-input\n");
		buffOutput.write("semantics:[" + inputSemantics + "]\n");
		buffOutput.close();
		this.setTestsuiteSource("TmpFile" + run + ".geni");

	}


	public void createAndConfigureInputFileForSemantics(String sampleName, String inputSemantics) throws IOException
	{
		++run;
		BufferedWriter buffOutput = new BufferedWriter(new FileWriter(new File("TmpFile" + run + ".geni")));

		buffOutput.write(sampleName + "\n");
		buffOutput.write("semantics:[" + inputSemantics + "]\n");
		buffOutput.close();
		this.setTestsuiteSource("TmpFile" + run + ".geni");

	}


	public String getGrammarSource()
	{

		return this.resourcesFiles.get("grammar");

	}


	public String getLexiconSource()
	{

		return this.resourcesFiles.get("lexicon");

	}


	void setTestsuiteSource(String filename)
	{
		this.resourcesFiles.put("testsuite", filename);
		getGenerationConfig().setTestSuiteFile(new File(filename));
	}


	public String getTestsuiteSource()
	{

		return this.resourcesFiles.get("testsuite");
	}


	void setLexiconSource(String filename)
	{
		this.resourcesFiles.put("lexicon", filename);
	}


	void setGrammarSource(String filename)
	{
		this.resourcesFiles.put("grammar", filename);
	}


	/**
	 * @return the verboseOutput
	 */
	public boolean isVerboseOutput()
	{
		return verboseOutput;
	}


	/**
	 * @param verboseOutput the verboseOutput to set
	 */
	public void setVerboseOutput(boolean verboseOutput)
	{
		this.verboseOutput = verboseOutput;
	}


	/**
	 * @return the userInputType
	 */
	public int getUserInputType()
	{
		return userInputType;
	}


	/**
	 * @param userInputType the userInputType to set
	 */
	public void setUserInputType(int userInputType)
	{
		this.userInputType = userInputType;
	}


	public GeneratorConfiguration getGenerationConfig()
	{
		return generationConfig;
	}


	void setGenerationConfig(GeneratorConfiguration generationConfig)
	{
		this.generationConfig = generationConfig;
	}
}