package synalp.commons.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import synalp.commons.unification.*;


/**
 * @author Alexandre Denis
 */
public class UnifierTest
{
	private static boolean ADDITIONAL_SUBSUMPTIONS = true;

	private FeatureStructure fs1, fs2, fs3;
	private InstantiationContext context;


	/**
	 * Tests unification without variables.
	 */
	@Test
	public void testSimpleUnifications()
	{
		System.out.println("\n*** Simple unifications");

		//
		fs1 = struct();
		fs2 = struct();
		unify(fs1, fs2, fs1);

		//
		fs1 = struct().addConstantFeature("a", "b");
		fs2 = struct();
		unify(fs1, fs2, fs1);
		unify(fs2, fs1, fs1);

		//
		fs1 = struct().addConstantFeature("a", "b");
		fs2 = struct().addConstantFeature("c", "d");
		fs3 = struct().addConstantFeature("a", "b").addConstantFeature("c", "d");
		unify(fs1, fs2, fs3);
		unify(fs2, fs1, fs3);

		//
		fs1 = struct().addConstantFeature("a", "b");
		fs2 = struct().addConstantFeature("a", "d");
		unify(fs1, fs2, null);
		unify(fs2, fs1, null);
	}


	/**
	 * Tests disjunction.
	 */
	@Test
	public void testDisjunctions()
	{
		System.out.println("\n*** Disjunctions");
		FeatureStructure fs1;
		FeatureStructure fs2;

		//
		fs1 = struct().addConstantFeature("a", "b", "c");
		fs2 = struct();
		unify(fs1, fs2, fs1);
		unify(fs2, fs1, fs1);

		//
		fs1 = struct().addConstantFeature("a", "b", "c");
		fs2 = struct().addConstantFeature("a", "b");
		unify(fs1, fs2, fs2);
		unify(fs2, fs1, fs2);

		//
		fs1 = struct().addConstantFeature("a", "b", "c");
		fs2 = struct().addConstantFeature("a", "b", "d");
		fs3 = struct().addConstantFeature("a", "b");
		unify(fs1, fs2, fs3);
		unify(fs2, fs1, fs3);
	}


	/**
	 * Tests unification with fs with 2 levels.
	 */
	@Test
	public void testDeepFS()
	{
		System.out.println("\n*** Deeper unifications");

		//
		fs1 = struct().addComplexFeature("a", struct().addConstantFeature("b", "c"));
		fs2 = struct().addComplexFeature("a", struct().addConstantFeature("b", "c"));
		unify(fs1, fs2, fs1);

		//
		fs1 = struct().addComplexFeature("a", struct().addConstantFeature("b", "c"));
		fs2 = struct().addComplexFeature("a", struct().addConstantFeature("b", "d"));
		unify(fs1, fs2, null);
	}


	/**
	 * 
	 */
	@Test
	public void testVars()
	{
		System.out.println("\n*** Variable unifications");

		//
		fs1 = struct().addVariableFeature("a", "@1");
		fs2 = struct().addConstantFeature("a", "d");
		unify(fs1, fs2, fs1, new InstantiationContext().set("@1", new FeatureConstant("d")));

		//
		fs1 = struct().addVariableFeature("a", "@1").addVariableFeature("b", "@1");
		fs2 = struct().addConstantFeature("a", "d");
		unify(fs1, fs2, fs1, new InstantiationContext().set("@1", new FeatureConstant("d")));

		//
		fs1 = struct().addVariableFeature("a", "@1").addVariableFeature("b", "@1");
		fs2 = struct().addComplexFeature("a", struct().addConstantFeature("c", "d"))
						.addComplexFeature("b", struct().addConstantFeature("e", "f"));
		unify(fs1, fs2, fs1, new InstantiationContext().set("@1", struct().addConstantFeature("c", "d")
																		  .addConstantFeature("e", "f")));
	}


	/**
	 * 
	 */
	@Test
	public void testUnifyFS()
	{
		System.out.println("\n*** Other unifications");

		//
		fs1 = struct().addVariableFeature("a", "@1").addVariableFeature("b", "@1");
		fs2 = struct().addConstantFeature("a", "d");
		context = new InstantiationContext().set("@1", new FeatureConstant("d"));
		unify(fs1, fs2, context, fs1);

		//
		fs1 = struct().addVariableFeature("a", "@1").addVariableFeature("b", "@1");
		fs2 = struct().addConstantFeature("a", "d");
		context = new InstantiationContext().set("@1", new FeatureConstant("e"));
		unify(fs1, fs2, context, null);

		//
		fs1 = struct().addVariableFeature("a", "@1").addVariableFeature("b", "@1");
		fs2 = struct().addConstantFeature("a", "e");
		context = new InstantiationContext().set("@1", new FeatureConstant("e", "f"));
		unify(fs1, fs2, context, fs1, new InstantiationContext().set("@1", new FeatureConstant("e")));

	}


	@Test
	@SuppressWarnings("javadoc")
	public void testUnifyValues()
	{
		unify(new FeatureVariable("@A"), struct().addVariableFeature("x", "@B"), new FeatureVariable("@A"));
	}


	private void unify(FeatureValue fs1, FeatureValue fs2, FeatureValue expected)
	{
		unify(fs1, fs2, new InstantiationContext(), expected);
	}


	private void unify(FeatureValue fs1, FeatureValue fs2, InstantiationContext context, FeatureValue expected)
	{
		String fs1Str = fs1.toString(context);
		String fs2Str = fs2.toString(context);

		FeatureValue res = Unifier.unify(fs1, fs2, context);
		System.out.println(fs1Str + " U " + fs2Str + " = " + (res == null ? null : res.toString(context)));
		assertEquals(expected, res);
	}


	private void unify(FeatureValue fs1, FeatureValue fs2, FeatureValue expected, InstantiationContext expectedContext)
	{
		unify(fs1, fs2, new InstantiationContext(), expected, expectedContext);
	}


	private void unify(FeatureValue fs1, FeatureValue fs2, InstantiationContext context, FeatureValue expected, InstantiationContext expectedContext)
	{
		String fs1Str = fs1.toString(context);
		String fs2Str = fs2.toString(context);

		FeatureValue res = Unifier.unify(fs1, fs2, context);
		System.out.println(fs1Str + " U " + fs2Str + " = " + res.toString(context));
		assertEquals(expected, res);
		assertEquals(expectedContext, context);

		if (ADDITIONAL_SUBSUMPTIONS)
		{
			SubsumerTest.subsumes(fs1, res, context, true);
			SubsumerTest.subsumes(fs2, res, context, true);
		}
	}


	/**
	 * Shorcut to create a new FeatureStructure.
	 * @return a new feature structure
	 */
	private FeatureStructure struct()
	{
		return new FeatureStructure();
	}

}
