package synalp.generation.configuration;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Reads a configuration file, first parse xml and eventually setup the configurations using inheritance.
 * @author Alexandre Denis
 */
public class GeneratorConfigurationReader extends DefaultHandler
{
	private static Logger logger = Logger.getLogger(GeneratorConfigurationReader.class);

	private GeneratorConfiguration curConfig;
	private GeneratorConfigurations configurations;
	private Map<GeneratorConfiguration, String> baseDir;
	private Map<GeneratorConfiguration, String> inheritance;

	
	/**
	 * Tests reading a configuration file.
	 * @param args 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static void main(String[] args) throws SAXException, IOException
	{
		GeneratorConfigurations configs = readConfigurations(new File("configuration.xml"));
		for(GeneratorConfiguration config : configs)
		{
			if (!config.getName().equals("default"))
				config.load();
			System.out.println(config.printConfiguration()+"\n");
		}
	}
	

	/**
	 * Reads the given grammar.
	 * @param file
	 * @return a syntactic lexicon
	 * @throws SAXException
	 * @throws IOException
	 */
	public static GeneratorConfigurations readConfigurations(File file) throws SAXException, IOException
	{
		GeneratorConfigurationReader reader = new GeneratorConfigurationReader();
		try
		{
			logger.info("Reading config file " + file);
			SAXParserFactory.newInstance().newSAXParser().parse(file, reader);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		return reader.configurations;
	}


	@Override
	public void startDocument() throws SAXException
	{
		configurations = new GeneratorConfigurations();
		baseDir = new HashMap<GeneratorConfiguration, String>();
		inheritance = new HashMap<GeneratorConfiguration, String>();
	}


	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		if (qName.equals("config"))
		{
			curConfig = createConfig(attributes);
			configurations.add(curConfig);
		}
		else if (qName.equals("option"))
			setOption(attributes);
		else if (qName.equals("resource"))
			setResource(attributes);
	}


	@Override
	public void endDocument() throws SAXException
	{
		setupInheritance();
	}


	/**
	 * Propagates the inherited options and resources. It could be more optimized by explicitely
	 * representing the tree structure before processing (some config are tested twice).
	 */
	private void setupInheritance()
	{
		try
		{
			for(GeneratorConfiguration config : configurations)
				setupInheritance(config);
		}
		catch(StackOverflowError e)
		{
			System.err.println("Error: unable to setup inheritance for configuration file, check if you do not have inheritance loops (a->b->a)");
		}
	}


	/**
	 * Setups the inheritance for given config.
	 * @param config
	 */
	private void setupInheritance(GeneratorConfiguration config)
	{
		if (!inheritance.containsKey(config))
			return;
		String parentName = inheritance.get(config);
		if (!configurations.containsKey(parentName))
			throw new ConfigurationException("Error: unable to find inherited configuration '" + parentName + "' by configuration '" + config + "'");
		GeneratorConfiguration parentConfig = configurations.get(parentName);
		setupInheritance(parentConfig);

		// first inherit options
		Map<String, String> parentOptions = parentConfig.getOptions();
		for(String option : parentOptions.keySet())
			if (!config.hasOption(option))
				config.setOption(option, parentOptions.get(option));

		// then inherit resources
		Map<String, File> parentResources = parentConfig.getResources();
		for(String resource : parentResources.keySet())
			if (!config.hasResource(resource))
				config.setResource(resource, new File(parentResources.get(resource).getAbsolutePath()));
	}


	/**
	 * Sets a resource to the current config.
	 * @param attributes
	 * @throws SAXException
	 */
	private void setResource(Attributes attributes) throws SAXException
	{
		String name = attributes.getValue("name");
		if (name == null)
			throw new SAXException("Error: a resource of configuration '" + curConfig + "' is missing a 'name' attribute");
		String file = attributes.getValue("file");
		if (file == null)
			throw new SAXException("Error: a resource of configuration '" + curConfig + "' is missing a 'file' attribute");
		if (baseDir.containsKey(curConfig))
			curConfig.setResource(name, new File(baseDir.get(curConfig) + (baseDir.get(curConfig).endsWith(File.separator) ? "" : File.separator) + file));
		else curConfig.setResource(name, new File(file));
	}


	/**
	 * Sets an option to the current config.
	 * @param attributes
	 * @throws SAXException
	 */
	private void setOption(Attributes attributes) throws SAXException
	{
		String name = attributes.getValue("name");
		if (name == null)
			throw new SAXException("Error: an option of configuration '" + curConfig.getName() + "' is missing a 'name' attribute");
		String value = attributes.getValue("value");
		if (value == null)
			throw new SAXException("Error: an option of configuration '" + curConfig.getName() + "' is missing a 'value' attribute");
		curConfig.setOption(name, value);
	}


	/**
	 * Creates a new configuration.
	 * @param attributes
	 * @return a new configuration
	 * @throws SAXException
	 */
	private GeneratorConfiguration createConfig(Attributes attributes) throws SAXException
	{
		String name = attributes.getValue("name");
		if (name == null)
			throw new SAXException("Error: a configuration is missing a 'name' attribute");
		String inherit = attributes.getValue("inherit");
		String baseDir = attributes.getValue("basedir");
		GeneratorConfiguration ret = new GeneratorConfiguration(name);
		if (inherit != null)
			inheritance.put(ret, inherit);
		if (baseDir != null)
			this.baseDir.put(ret, baseDir);
		return ret;
	}

}
