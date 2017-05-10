package synalp.commons.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import synalp.commons.unification.*;


/**
 * @author Alexandre Denis
 */
public class SubsumerTest
{
	private InstantiationContext context;
	private FeatureStructure fs1, fs2;


	/**
	 * Tests simple subsumptions.
	 */
	@Test
	public void testSimpleSubsumptions()
	{
		System.out.println("\n*** Simple subsumptions");

		//
		fs1 = struct();
		fs2 = struct();
		subsumes(fs1, fs2, true);

		//
		fs1 = struct().addConstantFeature("a", "b");
		fs2 = struct();
		subsumes(fs1, fs2, false);
		subsumes(fs2, fs1, true);

		//
		fs1 = struct().addConstantFeature("a", "b");
		fs2 = struct().addConstantFeature("a", "c");
		subsumes(fs1, fs2, false);

		//
		fs1 = struct().addConstantFeature("a", "b");
		fs2 = struct().addConstantFeature("a", "b").addConstantFeature("c", "d");
		subsumes(fs1, fs2, true);
		subsumes(fs2, fs1, false);

		//
		fs1 = struct().addConstantFeature("a", "b");
		fs2 = struct().addConstantFeature("a", "d");
		subsumes(fs1, fs2, false);
	}


	/**
	 * Tests subsumptions with disjunctions.
	 */
	@Test
	public void testDisjunctionSubsumptions()
	{
		System.out.println("\n*** Disjunction subsumptions");

		//
		fs1 = struct().addConstantFeature("a", "b", "c");
		fs2 = struct().addConstantFeature("a", "b");
		subsumes(fs1, fs2, false);
		subsumes(fs2, fs1, true);

		//
		fs1 = struct().addConstantFeature("a", "e").addConstantFeature("b", "e", "f");
		fs2 = struct().addVariableFeature("a", "@1").addVariableFeature("b", "@1");
		context = ctxt().set("@1", new FeatureConstant("e", "f"));
		subsumes(fs1, fs2, context, true);
		subsumes(fs2, fs1, context, false);
		
		//
		fs1 = struct().addVariableFeature("a", "@1").addVariableFeature("b", "@1");
		fs2 = struct().addConstantFeature("a", "e");
		subsumes(fs1, fs2, ctxt().set("@1", new FeatureConstant("e", "f")), false);
		subsumes(fs2, fs1, false);
	}


	/**
	 * Tests subsumptions with variables.
	 */
	@Test
	public void testVariables()
	{
		System.out.println("\n*** Variables subsumptions");

		// 
		fs1 = struct().addVariableFeature("a", "@1");
		fs2 = struct().addConstantFeature("a", "d");
		subsumes(fs1, fs2, true); // [a:@1] subsumes [a:{d}] -> true
		subsumes(fs2, fs1, false); // [a:{d}] subsumes [a:@1] -> false

		//
		fs1 = struct().addVariableFeature("a", "@1");
		fs2 = struct().addConstantFeature("a", "d");
		subsumes(fs1, fs2, ctxt().set("@1", new FeatureConstant("d")), true); // [a:@1{d}] subsumes [a:{d}] -> true
		subsumes(fs2, fs1, ctxt().set("@1", new FeatureConstant("d")), true); // [a:{d}] subsumes [a:@1{d}] -> true

		//
		fs1 = struct().addVariableFeature("a", "@1").addVariableFeature("b", "@1");
		fs2 = struct().addConstantFeature("a", "d").addConstantFeature("b", "d");
		subsumes(fs1, fs2, false); // [a:@1 b:@1] subsumes [a:{d} b:{d}] -> false
		subsumes(fs2, fs1, false); // [a:{d} b:{d}] subsumes [a:@1 b:@1] -> false

		//
		fs1 = struct().addVariableFeature("a", "@1").addVariableFeature("b", "@1");
		fs2 = struct().addConstantFeature("a", "d").addConstantFeature("b", "d");
		subsumes(fs1, fs2, ctxt().set("@1", new FeatureConstant("d")), false); // [b:@1{d} a:@1] subsumes [b:{d} a:{d}] -> false
		subsumes(fs2, fs1, ctxt().set("@1", new FeatureConstant("d")), true); // [b:{d} a:{d}] subsumes [b:@1{d} a:@1] -> true

		//
		fs1 = struct().addVariableFeature("a", "@1").addVariableFeature("b", "@1");
		fs2 = struct().addVariableFeature("a", "@2").addVariableFeature("b", "@2");
		subsumes(fs1, fs2, true);
		subsumes(fs2, fs1, true);
	}


	/**
	 * @param fs1
	 * @param fs2
	 * @param success
	 */
	public static void subsumes(FeatureValue fs1, FeatureValue fs2, boolean success)
	{
		subsumes(fs1, fs2, new InstantiationContext(), success);
	}


	/**
	 * @param fs1
	 * @param fs2
	 * @param context
	 * @param success
	 */
	public static void subsumes(FeatureValue fs1, FeatureValue fs2, InstantiationContext context, boolean success)
	{
		Subsumer subsumer = new Subsumer();
		String fs1Str = fs1.toString(context);
		String fs2Str = fs2.toString(context);

		boolean res = subsumer.subsumes(fs1, fs2, context);
		System.out.println(fs1Str + " subsumes " + fs2Str + " -> " + res);
		if (success)
			assertTrue(res);
		else assertFalse(res);
	}

	
	private FeatureStructure struct()
	{
		return new FeatureStructure();
	}


	private InstantiationContext ctxt()
	{
		return new InstantiationContext();
	}
}
