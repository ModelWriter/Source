package synalp.generation.configuration;

import java.lang.reflect.*;
import java.util.*;

import synalp.commons.utils.Utils;
import synalp.generation.GeneratorType;
import synalp.generation.jeni.LexicalSelectionType;
import synalp.generation.morphology.*;
import synalp.generation.probabilistic.*;
import synalp.generation.ranker.RankerType;

/**
 * All options along with some comments. All the fields are considered options, this class contains
 * also methods to setup the fields using a GeneratorConfiguration.
 * @author Alexandre Denis
 */
@SuppressWarnings("javadoc")
public class GeneratorOption
{
	@OptionMessage("If true, rewrites the lex node by merging them to their parent, setting their parent type to COANCHOR and adding a lemma feature.")
	public static boolean REWRITE_LEX_NODES = true;

	@OptionMessage("If true, rewrites the \"lemanchor\" feature names as \"lemma\". It also makes sure that the node type is a COANCHOR type (and thus blocks potential substitution.")
	public static boolean REWRITE_LEMANCHOR = true;

	@OptionMessage("If true, automatically assign the node identifiers following GenI convention.")
	public static boolean ASSIGN_NODE_IDS = true;

	@OptionMessage("Rewrites \"lex\" feature names as \"lemma\" to better match the internal naming scheme.")
	public static boolean REWRITE_LEX_AS_LEMMA = true;

	@OptionMessage(" Allows lexicon entries with empty lemma. The lemma must come from the semantics through equations.")
	public static boolean ALLOW_EMPTY_LEMMAS = true;

	@OptionMessage("If true, labels are discarded when testing unification of literals.")
	public static boolean IGNORE_LABELS = false;

	@OptionMessage("If true, consider the case while matching, if false do not consider the case.")
	public static boolean CASE_DEPENDENT = false;

	@OptionMessage("If false, does not allow ChartItems that have the same derivation trees.")
	public static boolean ALLOW_DUPLICATES = false;

	@OptionMessage("The timeout for the generator in milliseconds. Set to 0 to disable.")
	public static long TIMEOUT = 0;

	@OptionMessage("If true, uses filtering to group entries together before performing generation.")
	public static boolean USE_FILTERING = true;

	@OptionMessage("If USE_FILTERING is true, lists categories to use as top categories (we should have instead ROOT_FEAT)")
	public static String FILTERING_CATEGORIES = "s";

	@OptionMessage("If true, rename all variables of selected entries before returning them. It prevents having variable naming conflicts that would force renaming on the fly.")
	public static boolean RENAME_VARIABLES = true;

	@OptionMessage("If true, when a coanchor equation cannot be applied because of a missing named node, only display a warning and enable entry selection. If false, when a coanchor equation cannot be applied for this reason, do not select the entry and display an error.")
	public static boolean ALLOW_MISSING_COANCHORS = false;

	@OptionMessage("If true, when doing an operation, consider idx features unification. It is a kind of early top/bot unification failure. For the adjunction it is relatively easy, but for substitution this early failure only works if there is no auxiliary tree that would alter the idx later on.")
	public static boolean EARLY_SEMANTIC_FAILURE = true;

	@OptionMessage("Defines what to do when there is a missing lexem.")
	public static MissingLexemPolicy MISSING_LEXEM_POLICY = MissingLexemPolicy.OUTPUT_MISSING;

	@OptionMessage("Defines whether the morph lexicon contains first the lexem then the lemma or the opposite.")
	public static MorphLexiconOrder MORPH_LEXICON_ORDER = MorphLexiconOrder.LEMMA_FIRST;

	@OptionMessage("If true, the semantics that is used is a BitSemantics, a Semantics representation that only considers coverage instead of literals.")
	public static boolean USE_BIT_SEMANTICS = true;

	@OptionMessage("Defines the type of lexical selection to use")
	public static LexicalSelectionType SELECTION = LexicalSelectionType.CLASSICAL_SELECTION;

	@OptionMessage("Defines the type of ranker to use")
	public static RankerType RANKER = RankerType.DEFAULT_RANKER;

	@OptionMessage("Defines the size of the beam when using ranking")
	public static int BEAM_SIZE = 2;

	@OptionMessage("Defines the type of generator")
	public static GeneratorType GENERATOR = GeneratorType.JENI_DEFAULT;
	
	@OptionMessage("A type of strategy for computing the probability of new items based on their operation")
	public static ProbabilityStrategy.StrategyType PROBA_STRATEGY_TYPE = ProbabilityStrategy.StrategyType.DEFAULT;
	
	
	/**
	 * Setup options given a GeneratorConfiguration. May throw ConfigurationException.
	 */
	public static void setupOptions(GeneratorConfiguration config)
	{
		Field[] fields = GeneratorOption.class.getFields();
		for(String optionName : config.getOptions().keySet())
		{
			Field field = getField(optionName, fields);
			if (field == null)
				throwMissingOptionException(optionName, fields);
			else setupField(field, optionName, config.getValue(optionName));
		}
	}


	/**
	 * Returns a status representation of all options.
	 * @return
	 */
	public static String getStatus()
	{
		try
		{
			StringBuilder ret = new StringBuilder();
			for(Field field : GeneratorOption.class.getFields())
				ret.append(field.getName().toLowerCase()).append(" = ").append(field.get(null)).append("\n");
			return ret.toString().trim();
		}
		catch(Exception e)
		{
			return "Error: unable to read status of GeneratorOption: "+e.getMessage();
		}
	}
	
	
	/**
	 * Setup the given field. May throw a ConfigurationException.
	 * @param field
	 * @param name
	 * @param value
	 */
	public static void setupField(Field field, String name, String value)
	{
		Type fieldType = field.getType();
		try
		{
			if (fieldType.equals(boolean.class))
				field.setBoolean(GeneratorOption.class, Boolean.parseBoolean(value));
			else if (fieldType.equals(long.class))
				field.setLong(GeneratorOption.class, Long.parseLong(value));
			else if (fieldType.equals(int.class))
				field.setInt(GeneratorOption.class, Integer.parseInt(value));
			else if (fieldType.equals(String.class))
				field.set(GeneratorOption.class, value);
			else if (((Class<?>) field.getGenericType()).isEnum())
				setupEnumField(field, name, value);
		}
		catch (ConfigurationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throwInvalidValueException(field, name, value);
		}
	}


	/**
	 * Setups the given enum field. May throw ConfigurationException.
	 * @param field
	 * @param name
	 * @param value
	 * @throws Exception
	 */
	private static void setupEnumField(Field field, String name, String value) throws Exception
	{
		List<String> expectedValues = new ArrayList<String>();

		for(Object obj : ((Class<?>) field.getGenericType()).getEnumConstants())
		{
			expectedValues.add(obj.toString().toLowerCase());
			if (value.equalsIgnoreCase(obj.toString()))
			{
				field.set(GeneratorOption.class, obj);
				return;
			}
		}
		throw new ConfigurationException("Error: invalid value for option named '" + name + "' (expected one of: " +
											Utils.print(expectedValues, ", ") +
											" but input was: '" + value + "')");
	}


	/**
	 * Throws a configuration exception.
	 * @param option
	 * @param fields
	 */
	private static void throwMissingOptionException(String option, Field[] fields)
	{
		throw new ConfigurationException("Error: unable to configure the option named '" + option + "', there is no such option (defined: " +
											fieldsToString(fields) + ")");
	}


	/**
	 * Throws a configuration exception.
	 * @param field
	 * @param name
	 * @param value
	 */
	private static void throwInvalidValueException(Field field, String name, String value)
	{
		throw new ConfigurationException("Error: invalid value for option named '" + name + "' (expected: " + field.getType() + " but input was: '" + value +
											"')");
	}


	/**
	 * Returns a String representation of given fields names.
	 * @param fields
	 * @return a String
	 */
	private static String fieldsToString(Field[] fields)
	{
		StringBuilder ret = new StringBuilder();
		boolean first = true;
		for(Field field : fields)
		{
			if (!first)
				ret.append(", ");
			ret.append(field.getName().toLowerCase());
			first = false;
		}
		return ret.toString().trim();
	}


	/**
	 * Returns a field among the given field that has the same name modulo the case.
	 * @param name
	 * @param fields
	 * @return null if not found
	 */
	public static Field getField(String name, Field[] fields)
	{
		for(Field field : fields)
			if (field.getName().equalsIgnoreCase(name))
				return field;
		return null;
	}
}
