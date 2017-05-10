package synalp.generation.probabilistic.common;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import synalp.commons.input.TestSuiteEntry;
import synalp.generation.configuration.GeneratorConfiguration;
import synalp.generation.jeni.JeniRealization;
import synalp.generation.probabilistic.ProbabilisticGenerator;
import synalp.generation.probabilistic.Surface;

public class TestsuiteRunner
{
	public String runJeniConfiguration(AppConfiguration appConfig) throws IOException
	{
		int entryNum=1;
		
		StringBuilder out = new StringBuilder();

		if (appConfig == null)
		{
			System.err.println("Configuration is null");
		}
		else
		{
			DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
			df.setMaximumFractionDigits(3); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS

			//System.out.println(df.format(myValue))
			GeneratorConfiguration config = appConfig.getGenerationConfig();

			ProbabilisticGenerator generator = new ProbabilisticGenerator(config);
			String time = LocalDateTime.now().toString().split("T")[1];
			String date = LocalDateTime.now().toString().split("T")[0];

			if (appConfig.isVerboseOutput())
			{
				out.append("= Generation started at" + "\n");
			

				out.append("*Time: " + time + "\n");
			

				out.append("*Date: " + date + "\n\n");
		
			}
			else
			{
				out.append("= Started at " + time + " " + date + "\n");
			}

			

			out.append("== File resources info:" + "\n");
		

			out.append("*Grammar: " + appConfig.getGrammarSource() + "\n");
		

			out.append("*Lexicon: " + appConfig.getLexiconSource() + "\n");
			
		

			if (appConfig.getUserInputType() == 0)
			{
				out.append("*Testsuite: " + appConfig.getTestsuiteSource() + "\n");
				
			}

			if (appConfig.isVerboseOutput())
			{
				out.append("\n");
				

				out.append("== PJeni Generator configuration info\n");
				

				out.append("*Beam size: " + config.getOption("beam_size") + "\n\n");
				

				if (appConfig.getUserInputType() == 0)
				{
					out.append("== Testsuite details:\n");
				
					out.append("*#Tests: " + config.getTestSuite().size() + "\n\n");
				
				}
				else
				{
					out.append("== Generating from specified input\n");
				}

			}
			else
			{
				out.append("== Beam size: " + config.getOption("beam_size") + "\n");
				if (appConfig.getUserInputType() == 0)
					out.append("== Number of tests in suite: " + config.getTestSuite().size() + "\n");
			}

			if (appConfig.isVerboseOutput())
			{
				out.append("== Starting sentences generation\n\n");
				
			}
			
			for(TestSuiteEntry entry : config.getTestSuite())
			{
				

				out.append("=== Input #" + entryNum + "\n");
				

				out.append("*Test item ID: " + entry.getId() + "\n");
				

				out.append("*Semantics: " + entry.getSemantics().toString() + "\n");
				

				List<JeniRealization> results = generator.generate(entry.getSemantics());
				if (results.isEmpty())
				{
					out.append("No sentence\n");
				
				}
				int sentenceCount = 1;

				List<Surface> resultsGrouped = Surface.groupSurfaces(results);
				
				
				if (results.size()>0 && resultsGrouped.size() == 0)
					out.append("ERROR when getting surfaces\n");
				
				for(Surface surf : resultsGrouped)
				{
					
					
					
					
					if (appConfig.isVerboseOutput())
					{
						//out.write("GENERATED SENTENCE #" + sentenceCount + ":\n" + getSurface(real,true)+ "\n\n");
						out.append("==== Surface #" + sentenceCount + "\n");
				

						out.append("*Sentence: " + surf.getSentence() + "\n");
				

				
						out.append("*Times: " + surf.getTimes() + "\n");

						out.append("*Probability: " + df.format(surf.getProbability()) + "\n\n");
				
					}
					else
					{
						out.append("\"" + surf.getSentence() + "\"" + " (" + surf.getTimes()  + "," + df.format(surf.getProbability()) + ")\n");
					}

					++sentenceCount;

				}
				++entryNum;

			}
			

			

			time = LocalDateTime.now().toString().split("T")[1];
			date = LocalDateTime.now().toString().split("T")[0];

			if (appConfig.isVerboseOutput())
			{
				out.append("= Generation ended at" + "\n");
			

				out.append("*Time: " + time + "\n");
				

				out.append("*Date: " + date + "\n\n");
				
			}
			else
			{
				out.append("= Ended at " + time + " " + date + "\n");
			}

		}
		
		return out.toString();
	}

}
