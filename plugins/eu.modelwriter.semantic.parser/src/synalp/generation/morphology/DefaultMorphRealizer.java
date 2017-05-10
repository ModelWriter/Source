package synalp.generation.morphology;

import java.util.*;

import org.apache.log4j.Logger;

import synalp.commons.input.*;
import synalp.commons.output.*;
import synalp.commons.unification.*;
import synalp.generation.configuration.GeneratorOption;


/**
 * A DefaultMorphRealizer sets all the morphological realizations of a given syntactic realization.
 * @author Alexandre Denis
 */
public class DefaultMorphRealizer implements MorphRealizer
{
	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(DefaultMorphRealizer.class);

	private MorphLexicon lexicon;


	/**
	 * Creates a new DefaultMorphRealizer based on an empty lexicon.
	 */
	public DefaultMorphRealizer()
	{
		this.lexicon = new MorphLexicon();
	}


	/**
	 * Creates a new DefaultMorphRealizer based on given lexicon.
	 * @param lexicon
	 */
	public DefaultMorphRealizer(MorphLexicon lexicon)
	{
		this.lexicon = lexicon;
	}


	@Override
	public Collection<MorphRealization> setMorphRealizations(SyntacticRealization synRealization)
	{
		logMorphRealizationStart(synRealization);
		Collection<MorphRealization> ret = new ArrayList<MorphRealization>();
		for(Lemma lemma : synRealization.getLemmas())
		{
			if (ret.isEmpty())
			{
				// add an initial morph real for each lexem of the lemma
				Map<Lexem, InstantiationContext> lexems = getLexems(lemma, synRealization.getContext());
				for(Lexem lexem : lexems.keySet())
					ret.add(new MorphRealization(lexems.get(lexem), lexem));
			}
			else
			{
				// for each existing morph real, and for each lexem tries to append the latter to the former
				Collection<MorphRealization> newReal = new ArrayList<MorphRealization>();
				for(MorphRealization real : ret)
				{
					Map<Lexem, InstantiationContext> lexems = getLexems(lemma, real.getContext());
					for(Lexem lexem : lexems.keySet())
						newReal.add(new MorphRealization(real, lexems.get(lexem), lexem));
				}
				ret = newReal;
			}
			logCurrentRealizations(ret);
		}
		logMorphRealizationEnd(ret);
		synRealization.setMorphRealizations(ret);
		return ret;
	}


	/**
	 * Retrieves the Lexems (inflected forms) for given Lemma. Each lexem is associated to an
	 * instantiation context.
	 * @param lemma
	 * @param context
	 * @return a set of lexems
	 */
	protected Map<Lexem, InstantiationContext> getLexems(Lemma lemma, InstantiationContext context)
	{
		Map<Lexem, InstantiationContext> ret = new HashMap<Lexem, InstantiationContext>();
		if (lexicon.containsKey(lemma.getValue()))
			for(Lexem lexem : lexicon.get(lemma.getValue()).getLexems())
			{
				InstantiationContext tmpContext = new InstantiationContext(context);
				FeatureStructure fs = Unifier.unify(lemma.getFs(), lexem.getFs(), tmpContext);
				if (fs != null)
					ret.put(new Lexem(lexem.getValue(), fs), tmpContext);
			}

		if (ret.isEmpty())
		{
			if (GeneratorOption.MISSING_LEXEM_POLICY == MissingLexemPolicy.OUTPUT_LEMMA)
				ret.put(new Lexem(lemma.getValue(), lemma.getFs()), new InstantiationContext(context));
			else if (GeneratorOption.MISSING_LEXEM_POLICY == MissingLexemPolicy.OUTPUT_MISSING)
				ret.put(new Lexem("<missing:" + lemma.getValue() + ">"), new InstantiationContext(context));
			else if (GeneratorOption.MISSING_LEXEM_POLICY == MissingLexemPolicy.OUTPUT_MISSING_DETAILED)
				ret.put(new Lexem("<missing:" + lemma.getValue() + lemma.getFs().toString(context) + ">"), new InstantiationContext(context));
		}

		logLexemsFound(lemma, context, ret.keySet());
		return ret;
	}


/////// logging

	private static void logMorphRealizationStart(SyntacticRealization synRealization)
	{
		if (logger.isInfoEnabled())
			logger.info("Starting morph realization of " + synRealization);
		if (logger.isDebugEnabled())
			for(Lemma lemma : synRealization.getLemmas())
				logger.debug("\t"+lemma.toString(synRealization.getContext()));
	}


	private static void logLexemsFound(Lemma lemma, InstantiationContext context, Collection<Lexem> lexems)
	{
		if (logger.isDebugEnabled())
			logger.debug("Lexems for " + lemma + " are " + lexems);
	}


	private static void logCurrentRealizations(Collection<MorphRealization> ret)
	{
		if (logger.isDebugEnabled())
			logger.debug("Current realization are: " + ret);
	}


	private static void logMorphRealizationEnd(Collection<MorphRealization> ret)
	{
		if (logger.isInfoEnabled())
			logger.info("End morph realization: " + ret);
	}

}
