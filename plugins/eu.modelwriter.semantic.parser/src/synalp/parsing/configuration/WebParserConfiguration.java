package synalp.parsing.configuration;


import synalp.generation.configuration.GeneratorConfiguration;
import synalp.generation.configuration.GeneratorOption;

public class WebParserConfiguration extends SkeletalParserConfiguration{

	public WebParserConfiguration(GeneratorConfiguration config) {
		super(config);
	}

	@Override
	public String getParseConfigInfo() {
		StringBuilder text = new StringBuilder("\n\n\n\n\nParser Configuration Info : "+this.getClass().getName()+"\n");
		text.append("\nInput Grammar FileName = " + grammarFile.getAbsolutePath());
		text.append("\n\nInput Under Specified Lexicon FileName = " + uLexiconFile.getAbsolutePath());
		text.append("\n\nInput Fully Specified Lexicon FileName = " + fLexiconFile.getAbsolutePath());
		text.append("\n\nOther Options = \n"+GeneratorOption.getStatus());
		text.append("\nUse Probability = "+useProbability);
		text.append("\n\n\n\n");
		text.append("\n\n\n");
		return text.toString();
	}


}
