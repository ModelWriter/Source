package synalp.parsing.configuration;

import java.io.File;

import synalp.generation.configuration.GeneratorConfiguration;
import synalp.generation.configuration.GeneratorOption;

public class DesktopParserConfiguration extends SkeletalParserConfiguration {

	private File sentencesFile;
	private File parseResultFile;
	
	
	public DesktopParserConfiguration(GeneratorConfiguration config) {
		super(config);
		sentencesFile = config.getResource("sentences");
		parseResultFile = config.getResource("output");
	}

	public File getSentencesFile() {
		return sentencesFile;
	}
	
	public File getParseResultFile() {
		return parseResultFile;
	}
	
	public String getParseConfigInfo() {
		StringBuilder text = new StringBuilder("\n\n\n\n\nParser Configuration Info : "+this.getClass().getName()+"\n");
		text.append("\nInput Grammar FileName = " + grammarFile.getAbsolutePath());
		text.append("\n\nInput Under Specified Lexicon FileName = " + uLexiconFile.getAbsolutePath());
		text.append("\n\nInput Fully Specified Lexicon FileName = " + fLexiconFile.getAbsolutePath());
		text.append("\n\nInput Sentences FileName = " + sentencesFile.getAbsolutePath());
		text.append("\n\nOutput File = " + parseResultFile.getAbsolutePath());
		text.append("\n\nOther Options = \n"+GeneratorOption.getStatus());
		text.append("\nUse Probability = "+useProbability);
		text.append("\n\n\n\n");
		text.append("\n\n\n");
		return text.toString();
	}
}
