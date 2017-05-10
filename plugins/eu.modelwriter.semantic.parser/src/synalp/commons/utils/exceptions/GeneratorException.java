package synalp.commons.utils.exceptions;

/**
 * Exception object for Jeni
 * @author Celine Moro
 */
@SuppressWarnings("serial")
public class GeneratorException extends Exception
{

	/**
	 * Create a new instance of GeneratorException
	 */
	public GeneratorException()
	{
	}


	/**
	 * Create a new instance of GeneratorException
	 * @param message give details of the exception
	 */
	public GeneratorException(String message)
	{
		super(message);
	}


	/**
	 * Create a new instance of GeneratorException
	 * @param cause Exception responsible of this exception
	 */
	public GeneratorException(Throwable cause)
	{
		super(cause);
	}


	/**
	 * Create a new instance of GeneratorException
	 * @param message give details of the exception
	 * @param cause Exception responsible of this exception
	 */
	public GeneratorException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
