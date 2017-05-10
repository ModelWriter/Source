package synalp.commons.unification;

import java.util.*;

/**
 * A Subsumer performs subsumption tests (fs1 subsumes fs2), that is all the information of fs1 is
 * contained in fs2. The basic algo does: iterate through fs1 and check that each feature of fs1
 * exists in fs2 and that their values unify. It also check for re-entry that is all the paths to a
 * given variable in fs1 must also be present in fs2. Whenever it finds a variable in fs1, it
 * remembers its path and check if there is also a variable in fs2 that has the same path.
 * @author Alexandre Denis
 */
public class Subsumer
{
	private Paths paths;


	/**
	 * Creates a new Subsumer.
	 */
	public Subsumer()
	{
		// check cost of these
		paths = new Paths();
	}


	/**
	 * Tests if fs1 subsumes fs2. That is, if all the information contained in fs1 is also contained
	 * in fs2.
	 * @param fs1
	 * @param fs2
	 * @return whether fs1 subsumes fs2
	 */
	public boolean subsumes(FeatureStructure fs1, FeatureStructure fs2)
	{
		paths = new Paths();
		return subsumes(fs1, fs2, new InstantiationContext());
	}


	/**
	 * Tests if fs1 subsumes fs2. That is, if all the information contained in fs1 is also contained
	 * in fs2.
	 * @param fs1
	 * @param fs2
	 * @return whether fs1 subsumes fs2
	 */
	public boolean subsumes(FeatureValue fs1, FeatureValue fs2)
	{
		paths = new Paths();
		return subsumes(fs1, fs2, new InstantiationContext());
	}


	/**
	 * @param fs1
	 * @param fs2
	 * @param context
	 * @return whether fs1 subsumes fs2
	 */
	public boolean subsumes(FeatureStructure fs1, FeatureStructure fs2, InstantiationContext context)
	{
		return subsumes(fs1, fs2, new Path(), context);
	}


	/**
	 * @param fs1
	 * @param fs2
	 * @param context
	 * @return whether fs1 subsumes fs2
	 */
	public boolean subsumes(FeatureValue fs1, FeatureValue fs2, InstantiationContext context)
	{
		return subsumes(fs1, fs2, new Path(), context);
	}


	// FS - FS
	/**
	 * @param fs1
	 * @param fs2
	 * @param currentPath
	 * @param context
	 * @return whether fs1 subsumes fs2
	 */
	public boolean subsumes(FeatureStructure fs1, FeatureStructure fs2, Path currentPath, InstantiationContext context)
	{
		// iterate along fs1
		for(Feature feat : fs1.getFeatures())
		{
			FeatureValue val1 = feat.getValue();
			FeatureValue val2 = fs2.getValueOf(feat.getName());

			if (val2 == null)
				return false;
			else
			{
				currentPath.add(feat.getName());
				if (!subsumes(val1, val2, currentPath, context))
					return false;
				currentPath.remove(currentPath.size() - 1);
			}
		}

		return true;
	}


	/**
	 * @param v1
	 * @param v2
	 * @param currentPath TODO
	 * @param context
	 * @return a feature value
	 */
	private boolean subsumes(FeatureValue v1, FeatureValue v2, Path currentPath, InstantiationContext context)
	{
		if (v1 == null)
		{
			if (v2 == null)
				return true;
			else return false;
		}
		else if (v1 instanceof FeatureConstant)
			return subsumes((FeatureConstant) v1, v2, currentPath, context);
		else if (v1 instanceof FeatureVariable)
			return subsumes((FeatureVariable) v1, v2, currentPath, context);
		else if (v1 instanceof FeatureStructure)
			return subsumes((FeatureStructure) v1, v2, currentPath, context);
		return false;
	}


	// Const - Value
	private boolean subsumes(FeatureConstant c1, FeatureValue v2, Path currentPath, InstantiationContext context)
	{
		if (v2 instanceof FeatureConstant)
			return subsumes(c1, (FeatureConstant) v2, currentPath, context); // const, const
		else if (v2 instanceof FeatureVariable)
		{
			FeatureValue val2 = context.get(v2);
			if (val2 == null)
				return false;
			else return subsumes(c1, val2, currentPath, context);
		}
		else return false;
	}


	// Var - Value 
	private boolean subsumes(FeatureVariable v1, FeatureValue v2, Path currentPath, InstantiationContext context)
	{
		// check re-entry condition : any var already found in the subsumer must match another var with the same paths in
		// the subsumee, we store the vars when iterating through the fs
		if (paths.isNewVariable(v1))
		{
			paths.addToPaths(v1, new Path(currentPath));
			if (v2 instanceof FeatureVariable)
			{
				paths.addToPaths((FeatureVariable) v2, new Path(currentPath));
				return subsumes(v1, (FeatureVariable) v2, currentPath, context);
			}
			else if (v2 instanceof FeatureConstant)
				return subsumes(v1, (FeatureConstant) v2, currentPath, context);
			else if (v2 instanceof FeatureStructure)
				return subsumes(v1, (FeatureStructure) v2, currentPath, context);
			else return false;
		}
		else
		{
			paths.addToPaths(v1, new Path(currentPath));
			if (v2 instanceof FeatureVariable)
			{
				paths.addToPaths((FeatureVariable) v2, new Path(currentPath));
				if (paths.haveSamePaths(v1, (FeatureVariable) v2))
					return subsumes(v1, (FeatureVariable) v2, currentPath, context);
				else return false;
			}
			else return false;
		}
	}


	// FS - Value
	private boolean subsumes(FeatureStructure fs1, FeatureValue v2, Path currentPath, InstantiationContext context)
	{
		if (v2 == null)
			return false;
		else if (v2 instanceof FeatureConstant)
			return false; // fs, const
		else if (v2 instanceof FeatureVariable)
			return subsumes(fs1, (FeatureVariable) v2, currentPath, context); // var, fs
		else if (v2 instanceof FeatureStructure)
			return subsumes(fs1, (FeatureStructure) v2, currentPath, context); // fs, fs
		return false;
	}


	// FS - Var
	private boolean subsumes(FeatureStructure fs1, FeatureVariable v2, Path currentPath, InstantiationContext context)
	{
		FeatureValue val2 = context.get(v2);
		if (val2 == null)
			return false;
		else return subsumes(fs1, val2, currentPath, context);
	}


	// Const - Const
	private boolean subsumes(FeatureConstant c1, FeatureConstant c2, Path currentPath, InstantiationContext context)
	{
		return c2.getValues().containsAll(c1.getValues());
	}


	// Var - Const
	private boolean subsumes(FeatureVariable v1, FeatureConstant c2, Path currentPath, InstantiationContext context)
	{
		FeatureValue val1 = context.get(v1);
		if (val1 == null)
			return true;
		else return subsumes(val1, c2, currentPath, context);
	}


	// Var - Var
	private boolean subsumes(FeatureVariable v1, FeatureVariable v2, Path currentPath, InstantiationContext context)
	{
		FeatureValue val1 = context.get(v1);
		FeatureValue val2 = context.get(v2);
		if (val1 == null)
		{
			if (val2 == null)
				return true;
			else
			{
				context.put(v1, val2); // not sure
				return true;
			}
		}
		else
		{
			if (val2 == null)
				return false;
			else return subsumes(val1, val2, currentPath, context);
		}
	}


	// Var - FS
	private boolean subsumes(FeatureVariable v1, FeatureStructure fs2, Path currentPath, InstantiationContext context)
	{
		FeatureValue val1 = context.get(v1);
		if (val1 == null)
		{
			//context.put(v1, fs2);
			return true;
		}
		else return subsumes(val1, fs2, currentPath, context);
	}


	/**
	 * A Path is a sequence of feature names
	 * @author Alexandre Denis
	 */
	@SuppressWarnings("serial")
	private class Path extends ArrayList<String>
	{
		public Path()
		{

		}


		public Path(Path path)
		{
			addAll(path);
		}
	}


	/**
	 * Associates variables to set of known paths
	 * @author Alexandre Denis
	 */
	@SuppressWarnings("serial")
	private class Paths extends HashMap<FeatureVariable, Set<Path>>
	{
		/**
		 * Tests if the two given variables have the same stored paths.
		 * @param var1
		 * @param var2
		 * @return whether the two variables have the same paths
		 */
		public boolean haveSamePaths(FeatureVariable var1, FeatureVariable var2)
		{
			return containsKey(var1) &&
					containsKey(var2) &&
					get(var2).equals(get(var1));
		}


		/**
		 * Adds the given path to the given variable.
		 * @param var
		 * @param path
		 */
		public void addToPaths(FeatureVariable var, Path path)
		{
			if (!containsKey(var))
				put(var, new HashSet<Path>());
			get(var).add(path);
		}


		/**
		 * Tests if the given variable has been seen previously and is thus associated to a path
		 * already.
		 * @param var
		 * @return whether the given variable is associated to a path.
		 */
		public boolean isNewVariable(FeatureVariable var)
		{
			return !containsKey(var);
		}
	}
}
