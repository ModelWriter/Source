package synalp.commons.utils.exceptions;

/**
 * Thrown when loading an option failed.
 * @author Alexandre Denis
 *
 */
@SuppressWarnings("serial")
public class OptionLoadException extends GeneratorException
{
	/**
	 * 
	 * @param message
	 */
	public OptionLoadException(String message)
	{
		super(message);
	}
}
