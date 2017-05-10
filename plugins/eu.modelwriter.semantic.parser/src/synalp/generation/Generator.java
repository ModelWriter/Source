package synalp.generation;

import java.util.List;

import synalp.commons.output.*;
import synalp.commons.semantics.Semantics;
import synalp.generation.configuration.*;
import synalp.generation.jeni.JeniGenerator;
import synalp.generation.probabilistic.ProbabilisticGenerator;

/**
 * General interface for all generators.
 * @author Céline Moro
 * @author Alexandre Denis
 */
public interface Generator
{
	/**
	 * Creates a Generator based on given configuration.
	 * @param config
	 * @return a Generator
	 * @throws a ConfigurationException if the config does not have the option "generator".
	 */
	public static Generator createGenerator(GeneratorConfiguration config)
	{
		switch (config.getOption("generator"))
		{
			case "jeni":
				return new JeniGenerator(config);

			case "probabilistic":
				return new ProbabilisticGenerator(config);

			default:
				throw new ConfigurationException("Error: unsupported generator '" + config.getOption("generator") + "'");
		}
	}


	/**
	 * Generates alternative realizations for the given semantic input.
	 * @param semantics
	 * @return a list of alternative realizations
	 */
	public List<? extends SyntacticRealization> generate(Semantics semantics);


	/**
	 * Return the chosen algorithm to use ({@link GeneratorType#RTGEN}, or
	 * {@link GeneratorType#JENI_DEFAULT}, ...)
	 * @return the chosen algorithm to use
	 */
	public GeneratorType getGeneratorType();
}
