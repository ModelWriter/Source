package synalp.commons.unification;

import java.util.*;

/**
 * An Unifier unifies feature structures and returns a new feature structure if it succeeds, a null
 * value otherwise. The main method unify(fs,fs) iterates through each fs one after the other and
 * check if their features unify by picking the method according to features values types. When
 * finding a variable it builds the instantiation context which associates variables to their
 * values. If instantiations are known, they can be passed to the method unify(fs,fs,context).
 * @author Alexandre Denis
 */
public class Unifier
{
	/**
	 * Singleton unifier.
	 */
	private static final Unifier unifier = new Unifier();

	/**
	 * The first feature that failed of the last unification test. We should put that in an
	 * UnificationResult structure.
	 */
	public static Feature lastFailingFeature = null;


	/**
	 * Returns the unification of fs1 and fs2. If the unification fails, it returns null. This
	 * method creates a new instantiation context. If the context is important in output the other
	 * method unify(fs,fs,context) needs to be used.
	 * @param fs1 a feature structure
	 * @param fs2 a feature structure
	 * @return a new FeatureStructure if the unification succeeded or null if it failed.
	 */
	public static FeatureStructure unify(FeatureStructure fs1, FeatureStructure fs2)
	{
		return unifier.unifyFS(fs1, fs2);
	}


	/**
	 * Returns the unification of fs1 and fs2 in the given instantiation context. If the unification
	 * fails, it returns null. The given context may be altered by this method.
	 * @param fs1 a feature structure
	 * @param fs2 a feature structure
	 * @param context
	 * @return a new FeatureStructure if the unification succeeded or null if it failed.
	 */
	public static FeatureStructure unify(FeatureStructure fs1, FeatureStructure fs2, InstantiationContext context)
	{
		// the tmp context prevents modifying the context if the unification fails
		InstantiationContext tmpContext = new InstantiationContext(context);
		FeatureStructure ret = unifier.unifyFS(fs1, fs2, tmpContext);
		if (ret == null)
			return ret;
		else
		{
			context.clear();
			context.putAll(tmpContext);
			return ret;
		}
	}


	/**
	 * Returns the unification of v1 and v2 in the given instantiation context. If the unification
	 * fails, it returns null.
	 * @param v1
	 * @param v2
	 * @param context
	 * @return a feature value
	 */
	public static FeatureValue unify(FeatureValue v1, FeatureValue v2, InstantiationContext context)
	{
		// the tmp context prevents modifying the context if the unification fails
		InstantiationContext tmpContext = new InstantiationContext(context);
		FeatureValue ret = unifier.unifyVal(v1, v2, tmpContext);
		if (ret == null)
			return ret;
		else
		{
			context.clear();
			context.putAll(tmpContext);
			return ret;
		}
	}


	/**
	 * Returns the unification of v1 and v2 in an empty instantiation context. If the unification
	 * fails, it returns null. Note that this method does not allow to retrieve the context after
	 * unification.
	 * @param v1
	 * @param v2
	 * @return a feature value
	 */
	public static FeatureValue unify(FeatureValue v1, FeatureValue v2)
	{
		FeatureValue ret = unifier.unifyVal(v1, v2, new InstantiationContext());
		if (ret == null)
			return ret;
		else return ret;
	}


	/**
	 * Returns the unification of fs1 and fs2. If the unification fails, it returns null. This
	 * method creates a new instantiation context. If the context is important in output the other
	 * method unify(fs,fs,context) needs to be used.
	 * @param fs1 a feature structure
	 * @param fs2 a feature structure
	 * @return a new FeatureStructure if the unification succeeded or null if it failed.
	 */
	private FeatureStructure unifyFS(FeatureStructure fs1, FeatureStructure fs2)
	{
		return unifyFS(fs1, fs2, new InstantiationContext());
	}


	/**
	 * Returns the unification of fs1 and fs2 in the given instantiation context. If the unification
	 * fails, it returns null. The given context may be altered by this method.
	 * @param fs1 a feature structure
	 * @param fs2 a feature structure
	 * @param context
	 * @return a new FeatureStructure if the unification succeeded or null if it failed.
	 */
	private FeatureStructure unifyFS(FeatureStructure fs1, FeatureStructure fs2, InstantiationContext context)
	{
		FeatureStructure ret = new FeatureStructure();

		// iterate along fs1
		for(Feature feat : fs1.getFeatures())
		{
			FeatureValue val1 = feat.getValue();
			FeatureValue val2 = fs2.getValueOf(feat.getName());

			FeatureValue valUnified = unifyVal(val1, val2, context);
			if (valUnified == null)
			{
				lastFailingFeature = feat;
				return null;
			}
			else ret.add(new Feature(feat.getName(), valUnified));
		}

		// iterate along fs2
		for(Feature feat : fs2.getFeatures())
		{
			FeatureValue val1 = fs1.getValueOf(feat.getName());
			FeatureValue val2 = feat.getValue();

			FeatureValue valUnified = unifyVal(val1, val2, context);
			if (valUnified == null)
			{
				lastFailingFeature = feat;
				return null;
			}
			else ret.add(new Feature(feat.getName(), valUnified));
		}

		return ret;
	}


	/**
	 * Value - Value
	 * @param v1
	 * @param v2
	 * @param context
	 * @return a feature value
	 */
	private FeatureValue unifyVal(FeatureValue v1, FeatureValue v2, InstantiationContext context)
	{
		if (v1 == null)
		{
			if (v2 == null)
				return null;
			else if (v2 instanceof FeatureConstant)
				return unify((FeatureConstant) v2, v1, context);
			else if (v2 instanceof FeatureVariable)
				return unify((FeatureVariable) v2, v1, context);
			else if (v2 instanceof FeatureStructure)
				return unify((FeatureStructure) v2, v1, context);
		}
		else if (v1 instanceof FeatureConstant)
			return unify((FeatureConstant) v1, v2, context);
		else if (v1 instanceof FeatureVariable)
			return unify((FeatureVariable) v1, v2, context);
		else if (v1 instanceof FeatureStructure)
			return unify((FeatureStructure) v1, v2, context);
		return null;
	}


	/**
	 * Constant - Constant
	 * @param c1
	 * @param c2
	 * @param context
	 * @return a constant or null if the unification failed
	 */
	private FeatureConstant unify(FeatureConstant c1, FeatureConstant c2, InstantiationContext context)
	{
		Set<String> sharedValues = c1.getIntersection(c2);
		if (sharedValues.isEmpty())
			return null;
		else return new FeatureConstant(sharedValues);
	}


	/**
	 * Constant - Value. Unification of a constant and a null value returns the constant. We may
	 * restrict that to return null (but see failed tests).
	 * @param c1
	 * @param v2
	 * @param context
	 * @return a value or null if the unification failed
	 */
	private FeatureValue unify(FeatureConstant c1, FeatureValue v2, InstantiationContext context)
	{
		if (v2 == null)
			return c1;
		else if (v2 instanceof FeatureConstant)
			return unify(c1, (FeatureConstant) v2, context); // const, const
		else if (v2 instanceof FeatureVariable)
			return unify((FeatureVariable) v2, c1, context); // var, const
		else if (v2 instanceof FeatureStructure)
			return null; // const, fs
		return null;
	}


	/**
	 * Variable - Value
	 * @param v1
	 * @param v2
	 * @param context
	 * @return a value or null if the unification failed
	 */
	private FeatureValue unify(FeatureVariable v1, FeatureValue v2, InstantiationContext context)
	{
		if (v2 == null)
			return v1;
		else if (v2 instanceof FeatureConstant || v2 instanceof FeatureStructure)
			return unifyWithVar(v1, v2, context); // var, const
		else if (v2 instanceof FeatureVariable)
			return unify(v1, (FeatureVariable) v2, context); // var, var
		return null;
	}


	/**
	 * FS - Value
	 * @param fs1
	 * @param v2
	 * @param context
	 * @return a value or null if the unification failed
	 */
	private FeatureValue unify(FeatureStructure fs1, FeatureValue v2, InstantiationContext context)
	{
		if (v2 == null)
			return fs1;
		else if (v2 instanceof FeatureConstant)
			return null; // fs, const
		else if (v2 instanceof FeatureVariable)
			return unify((FeatureVariable) v2, fs1, context); // var, fs
		else if (v2 instanceof FeatureStructure)
			return unifyFS(fs1, (FeatureStructure) v2, context); // fs, fs
		return null;
	}


	/**
	 * Variable - Variable. They unify if their values are found in the context and unify. Warning:
	 * this method has been changed to return variables instead of values. But it is then not
	 * symmetric anymore when the two variables values are not null and unify, the first variable is
	 * returned.
	 * @param v1
	 * @param v2
	 * @param context
	 * @return a value or null if the unification failed, if both variables are instantiated in the context
	 * and their values unify, returns the first variable
	 */
	private FeatureValue unify(FeatureVariable v1, FeatureVariable v2, InstantiationContext context)
	{
		FeatureValue val1 = context.get(v1);
		FeatureValue val2 = context.get(v2);
		if (val1 == null)
		{
			if (val2 == null)
				return v1; // or null ?
			else
			{
				context.put(v1, val2);
				//return v1; 
				return val2;
			}
		}
		else
		{
			if (val2 == null)
			{
				context.put(v2, val1);
				//return v2; 
				return val1;
			}
			else
			{
				FeatureValue val = unifyVal(val1, val2, context);
				if (val == null)
					return null;
				else //return v1; 
					return val;
			}
		}
	}


	/**
	 * Variable - Constant
	 * @param v1
	 * @param c2
	 * @param context
	 * @return a value or null if the unification failed
	 */
	private FeatureValue unifyWithVar(FeatureVariable v1, FeatureValue c2, InstantiationContext context)
	{
		FeatureValue val1 = context.get(v1);
		if (val1 == null)
		{
			context.put(v1, c2);
			return v1;
		}
		else
		{
			FeatureValue val = unifyVal(c2, val1, context);
			if (val == null)
				return null;
			else
			{
				context.put(v1, val);
				return v1;
			}
		}
	}

}
