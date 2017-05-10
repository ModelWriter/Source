package synalp.commons.semantics;

import java.util.*;

import org.apache.log4j.Logger;

import synalp.commons.unification.InstantiationContext;


/**
 * The semantic subsumption is a bit more complex than feature structure subsumption since a literal
 * can be unified with several possible literals, leading to several possible instantiation
 * contexts, while in a feature structure, the subsumption only considers one and only one
 * instantiation context.
 * @author Alexandre Denis
 */
public class SemanticsSubsumer
{
	@SuppressWarnings("javadoc")
	public static Logger logger = Logger.getLogger(SemanticsSubsumer.class);


	/**
	 * Tests if sem1 subsumes sem2, that is all the information of sem1 is found in sem2 as well.
	 * This is the informational subsumption and not the logical subsumption. By default, this
	 * method ignores selectional literals.
	 * @param sem1
	 * @param sem2
	 * @return a set of alternative instantiation contexts
	 */
	public static Set<InstantiationContext> subsumes(Semantics sem1, Semantics sem2)
	{
		return subsumes(sem1, sem2, new InstantiationContext(), true);
	}


	/**
	 * Tests if sem1 subsumes sem2, that is all the information of sem1 is found in sem2 as well.
	 * This is the informational subsumption and not the logical subsumption.
	 * @param sem1
	 * @param sem2
	 * @param ignoreSelectionalLiterals if true selectional literals are ignored when testing
	 *            subsumption
	 * @return a set of alternative instantiation contexts
	 */
	public static Set<InstantiationContext> subsumes(Semantics sem1, Semantics sem2, boolean ignoreSelectionalLiterals)
	{
		return subsumes(sem1, sem2, new InstantiationContext(), ignoreSelectionalLiterals);
	}


	/**
	 * Tests if sem1 subsumes sem2, that is all the information of sem1 is found in sem2 as well in
	 * the given a priori context of instantiation. This is the informational subsumption and not
	 * the logical subsumption. By default this method ignores selectional literals.
	 * @param sem1
	 * @param sem2
	 * @param context
	 * @return a set of alternative instantiation contexts
	 */
	public static Set<InstantiationContext> subsumes(Semantics sem1, Semantics sem2, InstantiationContext context)
	{
		return subsumes(sem1, sem2, context, true);
	}


	/**
	 * Tests if sem1 subsumes sem2, that is all the information of sem1 is found in sem2 as well in
	 * the given a priori context of instantiation. This is the informational subsumption and not
	 * the logical subsumption.
	 * @param sem1
	 * @param sem2
	 * @param context
	 * @param ignoreSelectionalLiterals if true selectional literals are ignored when testing
	 *            subsumption
	 * @return a set of alternative instantiation contexts
	 */
	public static Set<InstantiationContext> subsumes(Semantics sem1, Semantics sem2, InstantiationContext context, boolean ignoreSelectionalLiterals)
	{
		Set<InstantiationContext> ret = new HashSet<InstantiationContext>();
		ret.add(context);

		for(DefaultLiteral literal : sem1)
		{
			if (ignoreSelectionalLiterals && literal.isSelectional())
			{
				logIgnoreLiteral(literal);
				continue;
			}

			logStartLiteral(literal, ret);
			Set<InstantiationContext> tmpContexts = new HashSet<InstantiationContext>();
			for(InstantiationContext c : ret)
			{
				Collection<InstantiationContext> newContexts = findLiteral(literal, sem2, c, ignoreSelectionalLiterals);
				if (!newContexts.isEmpty())
					tmpContexts.addAll(newContexts);
			}

			if (tmpContexts.isEmpty())
			{
				logLiteralNotFound(literal, ret, sem2);
				return new HashSet<InstantiationContext>();
			}
			else
			{
				logContextsAfterPass(tmpContexts);
				ret = tmpContexts;
			}
		}

		return ret;
	}


	/**
	 * Tests if sem1 subsumes sem2, that is all the information of sem1 is found in sem2 as well in
	 * the given a priori context of instantiation. This is the informational subsumption and not
	 * the logical subsumption.
	 * @param sem1
	 * @param sem2
	 * @param context
	 * @param ignoreSelectionalLiterals if true selectional literals are ignored when testing
	 *            subsumption
	 * @return a set of alternative instantiation contexts
	 */
	public static SemanticsSubsumptionResult subsumesMapping(Semantics sem1, Semantics sem2, InstantiationContext context, boolean ignoreSelectionalLiterals)
	{
		SemanticsSubsumptionResult ret = new SemanticsSubsumptionResult();

		for(DefaultLiteral literal : sem1)
		{
			if (ignoreSelectionalLiterals && literal.isSelectional())
			{
				logIgnoreLiteral(literal);
				continue;
			}

			logStartLiteral(literal, ret.getContexts());
			SemanticsSubsumptionResult tmpContexts = new SemanticsSubsumptionResult();
			if (ret.isEmpty())
			{
				SemanticsSubsumptionResult newContexts = findLiteralMapping(literal, sem2, context, new ArrayList<DefaultLiteral>(), ignoreSelectionalLiterals);
				if (!newContexts.isEmpty())
					tmpContexts.addAll(newContexts);
			}
			else
			{
				for(InstantiationContext c : ret.getContexts())
				{
					SemanticsSubsumptionResult newContexts = findLiteralMapping(literal, sem2, c, ret.getCoveredLiterals(c), ignoreSelectionalLiterals);
					if (!newContexts.isEmpty())
						tmpContexts.addAll(newContexts);
				}
			}

			if (tmpContexts.isEmpty())
			{
				logLiteralNotFound(literal, ret.getContexts(), sem2);
				return new SemanticsSubsumptionResult();
			}
			else
			{
				logContextsAfterPass(tmpContexts.getContexts());
				ret = tmpContexts;
			}
		}

		return ret;
	}


	/**
	 * Returns a string representation of the literal in each instantiation context. Useful for
	 * debugging.
	 * @param literal
	 * @param contexts
	 * @return a string
	 */
	private static String debugLiteral(DefaultLiteral literal, Set<InstantiationContext> contexts)
	{
		if (contexts.isEmpty())
			return literal.toString();

		StringBuilder ret = new StringBuilder();
		for(InstantiationContext context : contexts)
			ret.append(literal.toString(context)).append(" ");

		return ret.toString().trim();
	}


	/**
	 * Finds all possible ways to instantiate the literal variables in the given semantics and given
	 * a priori context. This method does not modify the input context.
	 * @param literal
	 * @param semantics
	 * @param context
	 * @param ignoreSelectionalLiterals if true selectional literals are ignored when finding
	 *            literal
	 * @return a new set of contexts
	 */
	private static Set<InstantiationContext> findLiteral(DefaultLiteral literal, Semantics semantics, InstantiationContext context,
			boolean ignoreSelectionalLiterals)
	{
		Set<InstantiationContext> ret = new HashSet<InstantiationContext>();
		for(DefaultLiteral otherLiteral : semantics)
		{
			if (ignoreSelectionalLiterals && otherLiteral.isSelectional() && !literal.isSelectional())
			{
				logIgnoreLiteral(otherLiteral);
				continue;
			}

			InstantiationContext newContext = new InstantiationContext(context);
			if (otherLiteral.unifies(literal, newContext))
				ret.add(newContext);
		}
		return ret;
	}


	/**
	 * Finds all possible ways to instantiate the literal variables in the given semantics and given
	 * a priori context. This method does not modify the input context.
	 * @param literal
	 * @param semantics
	 * @param context
	 * @param previousLiterals
	 * @param ignoreSelectionalLiterals if true selectional literals are ignored when finding
	 *            literal
	 * @return a new set of contexts
	 */
	private static SemanticsSubsumptionResult findLiteralMapping(DefaultLiteral literal, Semantics semantics, InstantiationContext context,
			Collection<DefaultLiteral> previousLiterals,
			boolean ignoreSelectionalLiterals)
	{
		SemanticsSubsumptionResult ret = new SemanticsSubsumptionResult();
		for(DefaultLiteral otherLiteral : semantics)
		{
			if (ignoreSelectionalLiterals && otherLiteral.isSelectional() && !literal.isSelectional())
			{
				logIgnoreLiteral(otherLiteral);
				continue;
			}

			InstantiationContext newContext = new InstantiationContext(context);
			if (otherLiteral.unifies(literal, newContext))
			{
				if (!literal.isSelectional())
					ret.addLiteral(newContext, otherLiteral);
				else ret.addContext(newContext);
				ret.addLiterals(newContext, previousLiterals);
			}
		}
		return ret;
	}


////// logging

	private static void logStartLiteral(DefaultLiteral literal, Set<InstantiationContext> contexts)
	{
		if (logger.isTraceEnabled())
			logger.trace("Instantiating " + literal + "/" + contexts);
	}


	private static void logLiteralNotFound(DefaultLiteral literal, Set<InstantiationContext> contexts, Semantics input)
	{
		if (logger.isTraceEnabled())
			logger.trace("Not found: literal " + debugLiteral(literal, contexts) + " in input semantics: " + input + " in all explored contexts: " + contexts);
	}


	private static void logContextsAfterPass(Set<InstantiationContext> contexts)
	{
		if (logger.isTraceEnabled())
			logger.trace("All contexts: " + contexts);
	}


	private static void logIgnoreLiteral(DefaultLiteral literal)
	{
		if (logger.isTraceEnabled())
			logger.trace("Ignoring literal " + literal + " since it is selectional");
	}

}
