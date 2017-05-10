package synalp.commons.tests;

import org.junit.Test;

import synalp.commons.semantics.Semantics;
import synalp.commons.unification.InstantiationContext;
import synalp.commons.utils.loggers.LoggerConfiguration;

import static org.junit.Assert.*;

/**
 * @author Alexandre Denis
 */
public class SemanticsTest
{
	static
	{
		LoggerConfiguration.init();
	}


	/**
	 * Note that the subsumption fails because since literals are constants, they do not unify
	 * properly.
	 */
	@Test
	public void testSubsumption1()
	{
		String str1 = "A0_1:indiv(?B0_1 ?C0_1 ?D0_1) F0_1:qeq(?G0_1 ?A0_1) A0_1:named(?B0_1 tammy_n) H0_1:proper_q(?B0_1 ?G0_1 ?I0_1)";
		String str2 = "L6:event(S pres indet ind) qeq(CR L5) L4:indiv(C f pl 3) L2:proper_q(B BR BS) L3:indiv(B f sg) L4:udef_q(C CR CS) L0:proper_q(A AR AS) L6:intelligent_adj(S C) L3:named(B fiona_n) qeq(BR L3) L5:and(C A B) L1:named(A tammy_n) L1:indiv(A f sg) qeq(AR L1)";
		Semantics sem1 = Semantics.readSemantics(str1);
		Semantics sem2 = Semantics.readSemantics(str2);
		assertTrue(sem1.subsumes(sem2).isEmpty());
	}


	/**
	 * A modified version of the test in which literals are variables. The subsumption passes.
	 */
	@Test
	public void testSubsumption2()
	{
		String str1 = "?A0_1:indiv(?B0_1 ?C0_1 ?D0_1) ?F0_1:qeq(?G0_1 ?A0_1) ?A0_1:named(?B0_1 tammy_n) ?H0_1:proper_q(?B0_1 ?G0_1 ?I0_1)";
		String str2 = "L6:event(S pres indet ind) qeq(CR L5) L4:indiv(C f pl 3) L2:proper_q(B BR BS) L3:indiv(B f sg) L4:udef_q(C CR CS) L0:proper_q(A AR AS) L6:intelligent_adj(S C) L3:named(B fiona_n) qeq(BR L3) L5:and(C A B) L1:named(A tammy_n) L1:indiv(A f sg) qeq(AR L1)";
		Semantics sem1 = Semantics.readSemantics(str1);
		Semantics sem2 = Semantics.readSemantics(str2);
		assertFalse(sem1.subsumes(sem2).isEmpty());
	}
	
	
	@Test
	@SuppressWarnings("javadoc")
	public void testFlatten()
	{
		Semantics sem1 = Semantics.readSemantics("foo(a|b c|d)");
		Semantics sem2 = Semantics.readSemantics("foo(a c) foo(b c) foo(a d) foo(b d)");
		assertTrue(new Semantics().join(sem1.flattenDisjunctions()).equals(sem2, new InstantiationContext()));
		
		sem1 = Semantics.readSemantics("foo(a|b c|d) bar(e f|g)");
		assertTrue(sem1.flattenDisjunctions().size() == 8);
	}
}
