package synalp.commons.utils;

import java.util.Date;
import synalp.commons.utils.exceptions.TimeoutException;
import synalp.generation.configuration.GeneratorOption;


/**
 * A TimeoutManager helps to handle timeout across different classes. You need first to call start
 * and check regularly for timeout.
 * @author Alexandre Denis
 */
public class TimeoutManager
{
	private static long startTime;


	/**
	 * Starts the TimeoutManager.
	 */
	public static void start()
	{
		startTime = new Date().getTime();
	}


	/**
	 * Checks the timeout and throws a TimeoutException if there is a timeout. This method only
	 * works if the TIMEOUT set in GeneratorOptions is strictly greater than 0.
	 * @param message the message of the TimeoutException
	 */
	public static void checkTimeout(String message)
	{
		if (GeneratorOption.TIMEOUT > 0 && getTimeSinceStart() > GeneratorOption.TIMEOUT)
			throw new TimeoutException(message);
	}


	/**
	 * Returns the time since start.
	 * @return the since since start or the current time if it has not started yet
	 */
	public static long getTimeSinceStart()
	{
		return new Date().getTime() - startTime;
	}
}
