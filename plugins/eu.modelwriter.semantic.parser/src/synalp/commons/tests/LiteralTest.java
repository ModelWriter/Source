package synalp.commons.tests;

import org.junit.Test;

import synalp.commons.semantics.DefaultLiteral;
import synalp.commons.unification.*;

import static org.junit.Assert.*;

/**
 * @author Alexandre Denis
 */
public class LiteralTest
{

	@Test
	@SuppressWarnings("javadoc")
	public void testUnification()
	{
		DefaultLiteral lit1 = DefaultLiteral.readLiteral("F0_1:qeq(?G0_1 ?A0_1)");
		DefaultLiteral lit2 = DefaultLiteral.readLiteral("qeq(BR L3)");

		InstantiationContext context = new InstantiationContext();
		assertTrue(lit1.unifies(lit2, context));
		assertTrue(context.containsKey(new FeatureVariable("?G0_1")));
		assertEquals(new FeatureConstant("BR"), context.get(new FeatureVariable("?G0_1")));
	}


	@Test
	@SuppressWarnings("javadoc")
	public void testUnificationIgnoreLabels()
	{
		DefaultLiteral lit1 = DefaultLiteral.readLiteral("F0_1:qeq(?G0_1 ?A0_1)");
		DefaultLiteral lit2 = DefaultLiteral.readLiteral("L1:qeq(BR L3)");

		DefaultLiteral.IGNORE_LABELS = false;
		assertFalse(lit1.unifies(lit2));

		DefaultLiteral.IGNORE_LABELS = true;
		assertTrue(lit1.unifies(lit2));
	}
}
