package synalp.generation.probabilistic.ui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import synalp.commons.utils.loggers.LoggerConfiguration;
import synalp.generation.probabilistic.common.AppConfiguration;
import synalp.generation.probabilistic.common.TestsuiteRunner;

public class PJeniCli
{
	static
	{
		LoggerConfiguration.init();
	}
	public static void main(String args[]) throws IOException
	{
		if (args.length >= 4)
		{
			String outName = null;
			if (args.length == 5)
			{
				outName = args[4];
			}
			String grammarFile = args[0];
			String lexiconFile = args[1];
			String testsuiteFile = args[2];
			
			AppConfiguration appConfig = new AppConfiguration();
			//app configuration
			appConfig.setConfiguration(grammarFile, lexiconFile, testsuiteFile);
			appConfig.setBeamSize(args[3]);
			
			TestsuiteRunner runner = new TestsuiteRunner();
			String output = runner.runJeniConfiguration(appConfig);
			
			// To show output on console. 
			System.out.println(output);
			
			// To print output to file also, if specified.
			if (outName != null)
			{
				OutputStream outputStream = new FileOutputStream(outName);
				OutputStreamWriter output2 = new OutputStreamWriter(outputStream);
				output2.write(output);
				output2.close();
				System.out.println("\n\nOutput also written to "+outName);
			}
			
		}
		else {
			System.out.println("Usage: java -Xmx3g -jar [this .jar] grammar_file lexicon_file testsuite_file beam_size [output_file]");
		}
	}

}
