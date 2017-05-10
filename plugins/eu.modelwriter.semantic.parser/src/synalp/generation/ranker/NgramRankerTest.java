package synalp.generation.ranker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import synalp.commons.input.*;
import synalp.commons.tests.GeneratorTest;
import synalp.commons.utils.*;
import synalp.commons.utils.exceptions.*;
import synalp.commons.utils.loggers.LoggerConfiguration;
import synalp.generation.configuration.*;
import synalp.generation.jeni.*;

/**
 * NgramRankerTest tests the NgramRanker with JeniGenerator.
 * @author apoorvi
 */
@SuppressWarnings("javadoc")
public class NgramRankerTest extends GeneratorTest
{
	int beamsize;
	int timeout;
	String ngramtype;
	int testID ;

	public int countPASS1 ;
	public int countFAIL1 ;
	public int countPASS2 ;
	public int countFAIL2 ;
	public int countTO1;
	public int countTO2;
	public long avgGenerationTime1;
	public long avgGenerationTime2;
	/**
	 * @param beamsize
	 * @param timeout
	 * @param ngramtype
	 */
	public NgramRankerTest(final int beamsize, final int timeout, final String ngramtype){
		this.beamsize=beamsize;
		this.timeout=timeout;
		this.ngramtype=ngramtype;
	}
	
	static
	{
		LoggerConfiguration.init();
		System.out.println("Console logging off - see NgramRankerTest class");
	}

	
	public void testRanker()
	{
		GeneratorOption.TIMEOUT= timeout;
		if (ngramtype.equals("JNIBased-withfilter")){
			GeneratorOption.USE_FILTERING=true;
			GeneratorOption.USE_BIT_SEMANTICS=true;
		}
		else{
			GeneratorOption.USE_FILTERING=false;
			GeneratorOption.USE_BIT_SEMANTICS=false;
		}
		
		GeneratorConfiguration config = GeneratorConfigurations.getConfig("kbgen");
		
		
		//ResourceBundle bundle = loadBundle(ResourcesBundleType.KBGEN_BUNDLE.getBundle());
		
		System.out.println("Grammar: "+config.getGrammarFile()+" "+config.getGrammar().values().size()+" trees");
		System.out.println("Lexicon: "+config.getSyntacticLexiconFile()+" "+config.getSyntacticLexicon().size()+" entries");
		
		JeniGenerator generator1 = new JeniGenerator(config);
		JeniGenerator generator2 = new JeniGenerator(config);
		generator2.setRanker(new NgramRanker("resources/ranking/lm-genia-lemma.bin", beamsize, ngramtype));
		TestSuite testsuite = config.getTestSuite();
		try
		{
			BufferedWriter summary = new BufferedWriter(new FileWriter("resources/ranking/summary_"+beamsize+".txt"));
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			
			summary.write(dateFormat.format(cal.getTime()));
			summary.newLine();
			summary.newLine();
			
			summary.write("Grammar: "+config.getGrammarFile()+"  "+config.getGrammar().values().size()+" trees");
			summary.newLine();
			summary.newLine();
			
			summary.write("Lexicon: "+config.getSyntacticLexiconFile()+"  "+config.getSyntacticLexicon().size()+" entries");
			summary.newLine();
			summary.newLine();
			
			summary.write("Ranker Summary : ");
			summary.newLine();
			summary.newLine();
			
			long totalTime1 = 0;
			long totalTime2 = 0;
			for(TestSuiteEntry entry : testsuite)
			{
				testID++;
				summary.write("Test : " + testID);
				summary.newLine();
				System.out.println("Test : " + testID);

				String defaultresult = "Timeout";
				Perf.logStart();
				try
				{
					boolean result1 = test(generator1, entry);
					defaultresult = getTestResult(result1);
				}
				catch (TimeoutException e)
				{
					System.out.println("Timeout");
				}
				long time1 = Perf.logEnd();
				//totalTime1 = totalTime1 + time1;
				if (defaultresult.equals("PASS")){
					countPASS1++;
					totalTime1 = totalTime1 + time1;
				}
				else if (defaultresult.equals("FAIL")){
					countFAIL1++;
					totalTime1 = totalTime1 + time1;
				}
				summary.write("DefaultRanker( " + Perf.formatTime(time1) + " ) -->	 " + defaultresult);
				summary.newLine();

				String ngramresult = "Timeout";
				Perf.logStart();
				try
				{
					boolean result2 = test(generator2, entry);
					ngramresult = getTestResult(result2);
				}
				catch (TimeoutException e)
				{
					System.out.println("Timeout");
				}

				long time2 = Perf.logEnd();
				if (ngramresult.equals("PASS")){
					countPASS2++;
					totalTime2 = totalTime2 + time2;
				}
				else if (ngramresult.equals("FAIL")){
					countFAIL2++;
					totalTime2 = totalTime2 + time2;
				}
				summary.write("NgramRanker( " + Perf.formatTime(time2) + " ) -->	" + ngramresult);
				summary.newLine();
				summary.newLine();

				//System.out.println("ranker mean: "+Perf.formatTime(Perf.logMean("ranker")));
				//System.out.println(NgramRanker.stepsNb+" steps");
			}
			countTO1 = testID - countPASS1 - countFAIL1;
			countTO2 = testID - countPASS2 - countFAIL2;
			avgGenerationTime1 = (long) ((double)totalTime1 / (double)(countPASS1+countFAIL1));
			avgGenerationTime2 = (long) ((double)totalTime2 / (double)(countPASS2+countFAIL2));
			summary.write("No.of tests : "+ testID);
			summary.newLine();
			summary.newLine();
			summary.write("DefaultRanker( " + Perf.formatTime(avgGenerationTime1) + " ) -->	" + countPASS1 + " PASS		" + countFAIL1 + " FAIL		" +
							countTO1 + " TIMEOUT");
			summary.newLine();
			summary.write("NgramRanker( " + Perf.formatTime(avgGenerationTime2) + " ) -->	" + countPASS2 + " PASS		" + countFAIL2 + " FAIL		" +
					countTO2 + " TIMEOUT");
			summary.newLine();
			summary.newLine();
			summary.write("DefaultRanker( " + Perf.formatTime(avgGenerationTime1) + " ) -->	" + ((double)countPASS1/(double)testID)*100 + " % PASS		" + ((double)countFAIL1/(double)testID)*100 + " % FAIL		" +
					((double)countTO1/(double)testID)*100 + " % TIMEOUT");
			summary.newLine();
			summary.write("NgramRanker( " + Perf.formatTime(avgGenerationTime2) + " ) -->	" + ((double)countPASS2/(double)testID)*100 + " % PASS		" + ((double)countFAIL2/(double)testID)*100 + " % FAIL		" +
					((double)countTO2/(double)testID)*100 + " % TIMEOUT");
			summary.newLine();
			summary.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * @param val
	 * @return whether a test is PASS or FAIL
	 */
	public String getTestResult(boolean val)
	{
		if (val)
			return "PASS";
		else return "FAIL";
	}

}
