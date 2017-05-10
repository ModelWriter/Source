package synalp.parsing.configuration;

import java.io.File;
import java.lang.reflect.Field;


import synalp.generation.configuration.GeneratorConfiguration;
import synalp.generation.configuration.GeneratorOption;

/**
 * Parser Config except for the input sentences file. This was done so that the web app would be able to independently specify input sentences at runtime.
 * @author bikash
 *
 */
public abstract class SkeletalParserConfiguration {
	
	protected boolean useProbability;
	protected File grammarFile;
	protected File uLexiconFile;
	protected File fLexiconFile;
	
	protected File parseMorphFile;
	protected File inputOntologyFile; // the input airbus ontology to use for enrichment
	
	protected boolean reasonerFlag;
	

	public SkeletalParserConfiguration(GeneratorConfiguration config) {
		
		Field[] genFields = GeneratorOption.class.getFields();
		for(String optionName : config.getOptions().keySet())
		{
			Field field = GeneratorOption.getField(optionName, genFields);
			if (field == null) {
				// Do nothing. This is not one of the standard options originally defined for generator. 
			}
			else 
				GeneratorOption.setupField(field, optionName, config.getValue(optionName));
		}
		
		useProbability = config.getOption("use_probability").equalsIgnoreCase("true")?true:false;
		
		
		grammarFile = config.getResource("grammar"); 
		uLexiconFile = config.getResource("lexicon");
		fLexiconFile = config.getResource("fslexicon");
		
		parseMorphFile = config.getResource("morph");
		inputOntologyFile = config.getResource("ontology");
		
		reasonerFlag = config.getOption("useRealReasoner").equalsIgnoreCase("true")?true:false;
	}
	
	
	
	
	public boolean isUseProbability() {
		return useProbability;
	}


	public File getGrammarFile() {
		return grammarFile;
	}




	public File getuLexiconFile() {
		return uLexiconFile;
	}




	public File getfLexiconFile() {
		return fLexiconFile;
	}



	public File getParseMorphFile() {
		return parseMorphFile;
	}


	public File getInputOntologyFile() {
		return inputOntologyFile;
	}

	
	public boolean getReasonerFlag() {
		return reasonerFlag;
	}
	

	public abstract String getParseConfigInfo();
	
}
