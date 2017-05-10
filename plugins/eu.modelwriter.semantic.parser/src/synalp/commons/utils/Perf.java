package synalp.commons.utils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Enables to monitor time.
 * @author Alexandre Denis
 */
public class Perf
{
	/**
	 * The timestamps for each perf monitor id.
	 */
	private static Map<String, Long> timestamps = new HashMap<String, Long>();

	/**
	 * The number of times we called each perf monitor logEnd() method.
	 */
	private static Map<String, Integer> logOccurrences = new HashMap<String, Integer>();

	/**
	 * The accumulated time for each perf monitor id.
	 */
	private static Map<String, Long> accumulatedTime = new HashMap<String, Long>();

	private static long millis;


	/**
	 * Asserts that we should monitor the time from the moment of the call for the default monitor.
	 */
	public static void logStart()
	{
		logStart("default");
	}


	/**
	 * Asserts that we should monitor the time from the moment of the call given a monitor id. The
	 * same id has to be used to retrieve the time with logEnd.
	 * @param id an id for the monitor
	 */
	public static void logStart(String id)
	{
		timestamps.put(id, new Date().getTime());
	}


	/**
	 * Returns the time in ms since last call of logStart() method for the default monitor.
	 * @return the time in ms since last call of logStart() method.
	 */
	public static long logEnd()
	{
		return logEnd("default");
	}


	/**
	 * Returns the mean rounded time for the default perf monitor across all calls of logEnd method.
	 * @return the mean rounded time
	 */
	public static long logMean()
	{
		return logMean("default");
	}


	/**
	 * Returns the accumulated time for the default perf monitor across all calls of logEnd method.
	 * @return the mean rounded time
	 */
	public static long logAccumulated()
	{
		return logAccumulated("default");
	}


	/**
	 * Returns the time in ms since last call of logStart() method for a given monitor id.
	 * @param id an id for the monitor
	 * @return the time in ms since last call of logStart() method.
	 */
	public static long logEnd(String id)
	{
		if (!timestamps.containsKey(id))
			return 0;
		else
		{
			long time = new Date().getTime() - timestamps.get(id);
			Utils.addOne(id, logOccurrences);
			Utils.addNLong(time, id, accumulatedTime);
			return time;
		}
	}


	/**
	 * Returns the formatted time in ms since last call of logStart() method for a given monitor id.
	 * @param id an id for the monitor
	 * @return the formatted time in ms since last call of logStart() method.
	 */
	public static String logEndFormat(String id)
	{
		return formatTime(logEnd(id));
	}


	/**
	 * Returns the mean rounded time for a perf monitor across all calls of logEnd method.
	 * @param id an id for the monitor
	 * @return the mean rounded time
	 */
	public static long logMean(String id)
	{
		if (!logOccurrences.containsKey(id))
			return 0;

		int occurrences = logOccurrences.get(id);
		if (occurrences == 0)
			return 0;

		return accumulatedTime.get(id) / occurrences;
	}


	/**
	 * Returns the accumulated time for a perf monitor across all calls of logEnd method.
	 * @param id an id for the monitor
	 * @return the mean rounded time
	 */
	public static long logAccumulated(String id)
	{
		if (!accumulatedTime.containsKey(id))
			return 0;
		else return accumulatedTime.get(id);
	}


	/**
	 * Formats the given time in ms as a String.
	 * @param millis
	 * @return a formatted representation of the time
	 */
	public static String formatTime(long millis)
	{
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
		long ms = millis - TimeUnit.SECONDS.toMillis(seconds) - TimeUnit.MINUTES.toMillis(minutes);

		StringBuilder ret = new StringBuilder();
		if (minutes > 0)
			ret.append(minutes).append("mins ");
		if (seconds > 0)
			ret.append(seconds).append("s ");
		if (ms > 0)
			ret.append(ms).append("ms");
		else ret.append("0ms");

		return ret.toString().trim();
	}


	/**
	 * Returns a String representation of the time since last call of this method.
	 * @return a String
	 */
	public static String logTime()
	{
		if (millis == 0)
			millis = new Date().getTime();
		else millis = new Date().getTime() - millis;

		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
		long ms = millis - TimeUnit.SECONDS.toMillis(seconds) - TimeUnit.MINUTES.toMillis(minutes);

		millis = new Date().getTime();

		if (minutes > 0)
			return String.format("%d min, %d sec, %d ms", minutes, seconds, ms);
		else if (seconds > 0)
			return String.format("%ds %dms", seconds, ms);
		else return String.format("%d ms", ms);
	}
}
