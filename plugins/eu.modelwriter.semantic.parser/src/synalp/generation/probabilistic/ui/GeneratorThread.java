package synalp.generation.probabilistic.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import org.xml.sax.SAXException;

import synalp.commons.input.TestSuiteEntry;
import synalp.commons.output.MorphRealization;
import synalp.commons.output.SyntacticRealization;
import synalp.commons.utils.Utils;
import synalp.generation.configuration.GeneratorConfiguration;
import synalp.generation.configuration.GeneratorConfigurations;
import synalp.generation.jeni.JeniGenerator;
import synalp.generation.jeni.JeniRealization;
import synalp.generation.probabilistic.ProbabilisticGenerator;
import synalp.generation.probabilistic.Surface;
import synalp.generation.probabilistic.common.AppConfiguration;
import synalp.generation.configuration.GeneratorConfigurationReader;

public class GeneratorThread extends Thread
{
	private AppConfiguration appConfig;
	JTextArea genTextArea;
	JProgressBar progressBar;
	int entryNum = 0, totalEntries = 0;
	JLabel lblGenerationInProcess;


	public void setConfig(AppConfiguration appConfig)
	{
		this.appConfig = appConfig;
	}


	void setWidgetsToUpdate(JTextArea genTextArea, JProgressBar progressBar, JLabel lblGenerationInProcess)
	{
		this.genTextArea = genTextArea;
		this.progressBar = progressBar;
		this.lblGenerationInProcess = lblGenerationInProcess;
	}


	public void run()
	{

		startGeneration();
	}


	public static void main(String args[])
	{

	}


	void addToTextArea(String text)
	{
		genTextArea.append(text);

	}


	public void startGeneration()
	{

		if (this.appConfig == null)
		{
			System.err.println("Configuration is null");
		}
		else
		{
			DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
			df.setMaximumFractionDigits(3); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
			
			GeneratorConfiguration config = this.appConfig.getGenerationConfig();

			ProbabilisticGenerator generator = new ProbabilisticGenerator(config);
			String time = LocalDateTime.now().toString().split("T")[1];
			String date = LocalDateTime.now().toString().split("T")[0];

			if (appConfig.isVerboseOutput())
			{
				genTextArea.append("= Generation started at" + "\n");
			

				addToTextArea("*Time: " + time + "\n");
			

				addToTextArea("*Date: " + date + "\n\n");
		
			}
			else
			{
				addToTextArea("= Started at " + time + " " + date + "\n");
			}

			this.totalEntries = config.getTestSuite().size();
			this.progressBar.setMaximum(this.totalEntries);
			this.progressBar.setString(0 + "/" + this.totalEntries);
			this.progressBar.update(this.progressBar.getGraphics());
			this.lblGenerationInProcess.setText("Generation in process...");

			addToTextArea("== File resources info:" + "\n");
		

			addToTextArea("*Grammar: " + appConfig.getGrammarSource() + "\n");
		

			addToTextArea("*Lexicon: " + appConfig.getLexiconSource() + "\n");
			
		

			if (this.appConfig.getUserInputType() == 0)
			{
				addToTextArea("*Testsuite: " + appConfig.getTestsuiteSource() + "\n");
				
			}

			if (appConfig.isVerboseOutput())
			{
				addToTextArea("\n");
				

				addToTextArea("== PJeni Generator configuration info\n");
				

				addToTextArea("*Beam size: " + config.getOption("beam_size") + "\n\n");
				

				if (appConfig.getUserInputType() == 0)
				{
					addToTextArea("== Testsuite details:\n");
				
					addToTextArea("*#Tests: " + config.getTestSuite().size() + "\n\n");
				
				}
				else
				{
					addToTextArea("== Generating from specified input\n");
				}

			}
			else
			{
				addToTextArea("== Beam size: " + config.getOption("beam_size") + "\n");
				if (this.appConfig.getUserInputType() == 0)
					addToTextArea("== Number of tests in suite: " + config.getTestSuite().size() + "\n");
			}

			if (appConfig.isVerboseOutput())
			{
				addToTextArea("== Starting sentences generation\n\n");
				
			}

			for(TestSuiteEntry entry : config.getTestSuite())
			{
				//System.err.println(entry.getSemantics());
				this.progressBar.setValue(this.entryNum);
				this.progressBar.setString(this.entryNum + "/" + this.totalEntries);
				this.progressBar.update(this.progressBar.getGraphics());

				addToTextArea("=== Input #" + entryNum + "\n");
				

				addToTextArea("*Test item ID: " + entry.getId() + "\n");
				

				addToTextArea("*Semantics: " + entry.getSemantics().toString() + "\n");
				

				List<JeniRealization> results = generator.generate(entry.getSemantics());
				if (results.isEmpty())
				{
					addToTextArea("No sentence\n");
				
				}
				int sentenceCount = 1;

				List<Surface> resultsGrouped = Surface.groupSurfaces(results);
				
				
				if (results.size()>0 && resultsGrouped.size() == 0)
					addToTextArea("ERROR when getting surfaces\n");
				
				for(Surface surf : resultsGrouped)
				{
					
					
					
					
					if (appConfig.isVerboseOutput())
					{
						//addToTextArea("GENERATED SENTENCE #" + sentenceCount + ":\n" + getSurface(real,true)+ "\n\n");
						addToTextArea("==== Surface #" + sentenceCount + "\n");
				

						addToTextArea("*Sentence: " + surf.getSentence() + "\n");
				

				
						addToTextArea("*Times: " + surf.getTimes() + "\n");

						addToTextArea("*Probability: " + df.format(surf.getProbability()) + "\n\n");
				
					}
					else
					{
						addToTextArea("\"" + surf.getSentence() + "\"" + " (" + surf.getTimes()  + "," + df.format(surf.getProbability()) + ")\n");
					}

					++sentenceCount;

				}
				++this.entryNum;

			}

			this.progressBar.setValue(this.entryNum);
			this.progressBar.setString(this.entryNum + "/" + this.totalEntries);
			this.progressBar.update(this.progressBar.getGraphics());
			this.lblGenerationInProcess.setText("Done.");

			time = LocalDateTime.now().toString().split("T")[1];
			date = LocalDateTime.now().toString().split("T")[0];

			if (appConfig.isVerboseOutput())
			{
				addToTextArea("= Generation ended at" + "\n");
			//	System.out.println("= Generation ended at");

				addToTextArea("*Time: " + time + "\n");
				

				addToTextArea("*Date: " + date + "\n\n");
				
			}
			else
			{
				addToTextArea("= Ended at " + time + " " + date + "\n");
			}

		}

	}







}
