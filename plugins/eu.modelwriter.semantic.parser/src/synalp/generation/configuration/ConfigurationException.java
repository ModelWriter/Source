package synalp.generation.configuration;

/**
 * A ConfigurationException is an exception raised while loading or accessing configuration
 * resources or options.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class ConfigurationException extends RuntimeException
{

	/**
	 * Creates a new ConfigurationException with given message.
	 * @param message
	 */
	public ConfigurationException(String message)
	{
		super(message);
	}
}
