package synalp.commons.input;

import synalp.generation.GenerationReport;

/**
 * Represents the result of a test.
 * @author Alexandre Denis
 *
 */
public class TestResult
{
	private String message;
	private TestSuiteEntry test;
	private TestResultType type;
	private GenerationReport report;


	/**
	 * @return the test
	 */
	public TestSuiteEntry getTest()
	{
		return test;
	}


	/**
	 * @param test the test to set
	 */
	public void setTest(TestSuiteEntry test)
	{
		this.test = test;
	}


	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}


	/**
	 * @return the type
	 */
	public TestResultType getType()
	{
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(TestResultType type)
	{
		this.type = type;
	}


	/**
	 * @return the report
	 */
	public GenerationReport getReport()
	{
		return report;
	}


	/**
	 * @param report the report to set
	 */
	public void setReport(GenerationReport report)
	{
		this.report = report;
	}

}
