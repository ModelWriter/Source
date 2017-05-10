package synalp.commons.utils.loggers;

import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author Alexandre Denis
 */
public class LoggerConfiguration
{
	/**
	 * Configure loggers for synalp.generation.jeni package.
	 */
	public static void init()
	{
		// normally it should be done by starting with -Dlog4j.configuration=file:log4j.xml
		DOMConfigurator.configure("log4j.xml");
	}
}
