package synalp.commons.utils.exceptions;

import synalp.commons.utils.Perf;
import synalp.generation.configuration.GeneratorOption;


/**
 * Thrown when the generation has timed out. For now it is a RuntimeException (and thus not a GeneratorException),
 * in the future we may change that.
 * @author Alexandre Denis
 *
 */
@SuppressWarnings("serial")
public class TimeoutException extends RuntimeException
{
	/**
	 * 
	 * @param message
	 */
	public TimeoutException(String message)
	{
		super(message+" (timeout is "+Perf.formatTime(GeneratorOption.TIMEOUT)+")");
	}
}
