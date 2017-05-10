package synalp.generation.configuration;

import java.io.*;
import java.util.*;

/**
 * Stores all possible configurations indexed by their name.
 * @author Alexandre Denis
 */
@SuppressWarnings("serial")
public class GeneratorConfigurations extends HashMap<String, GeneratorConfiguration> implements Iterable<GeneratorConfiguration>
{
	/**
	 * The name of the system properties referring to the jeni configuration file.
	 */
	private static final String CONFIGURATION_PROPERTY = "jeni.configuration";

	/**
	 * The name of the default configuration file.
	 */
	private static final File DEFAULT_CONFIGURATION_FILE = new File("configuration.xml");

	private static GeneratorConfigurations singleton;
	
	static
	{
		singleton = createConfigurations();
	}
	

	/**
	 * Creates configurations. It first looks at the file referred to by the system property
	 * "jeni.configuration", and if unable to load it, uses the default configuration file. May
	 * throw a ConfigurationException. Note: no configuration is loaded, it is the responsability of
	 * the generator to load the desired configuration.
	 * @return configurations
	 */
	public static GeneratorConfigurations createConfigurations()
	{
		File configFile = DEFAULT_CONFIGURATION_FILE;
		String configFilename = System.getProperty(CONFIGURATION_PROPERTY);
		if (configFilename == null)
			System.err.println("Warning: system property '" + CONFIGURATION_PROPERTY + "' has not been defined, using default configuration file (" +
								DEFAULT_CONFIGURATION_FILE + ")");
		else configFile = new File(configFilename);

		try
		{
			return GeneratorConfigurationReader.readConfigurations(configFile);
		}
		catch (Exception e)
		{
			throw new ConfigurationException("Error: unable to load configuration file '" + configFile + "': " + e.getMessage());
		}
	}
	
	
	/**
	 * Tests if the given configuration exists.
	 * @param name
	 * @return
	 */
	public static boolean hasConfig(String name)
	{
		return singleton.containsKey(name);
	}
	
	
	/**
	 * Returns the configuration with given name.
	 * @param name
	 * @return a configuration or null if not found
	 * @throws a ConfigurationException if the configuration is not found
	 */
	public static GeneratorConfiguration getConfig(String name)
	{
		if (hasConfig(name))
			return singleton.get(name);
		else throw new ConfigurationException("Error: unable to find configuration '"+name+"', known configurations: "+singleton.keySet());
	}


	/**
	 * Adds the given configuration. If there exists a configuration with the same name it is
	 * overwritten.
	 * @param curConfig
	 */
	public void add(GeneratorConfiguration curConfig)
	{
		put(curConfig.getName(), curConfig);
	}


	@Override
	public Iterator<GeneratorConfiguration> iterator()
	{
		return values().iterator();
	}

}
