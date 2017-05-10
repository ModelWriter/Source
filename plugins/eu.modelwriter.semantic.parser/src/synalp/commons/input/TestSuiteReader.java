package synalp.commons.input;

import java.io.*;

import synalp.commons.input.javacc.InputReader;


/**
 * A LexFormatReader reads a TestSuite in GenI format.
 * @author Alexandre Denis
 */
public class TestSuiteReader
{
	/**
	 * Demonstrates how to call the LexFormatReader.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		TestSuite tests = readTestSuite(new File("doc/examples/input/french/test.geni"));
		for(TestSuiteEntry test : tests)
			System.out.println(test+"\n");
		System.out.println("Read "+tests.size()+" entries");
	}


	/**
	 * Reads the given File in the GenI test suite format.
	 * @param file
	 * @return TestSuite
	 * @throws Exception
	 */
	public static TestSuite readTestSuite(File file) throws Exception
	{
		try(RandomAccessFile f = new RandomAccessFile(file, "r"))
		{
			byte[] content = new byte[(int) f.length()];
			f.readFully(content);
			return InputReader.readTestSuite(new String(content));
		}
	}
}
