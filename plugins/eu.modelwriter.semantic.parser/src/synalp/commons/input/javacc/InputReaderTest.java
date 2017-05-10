package synalp.commons.input.javacc;

import org.junit.Test;

import synalp.commons.semantics.DefaultLiteral;
import synalp.commons.unification.*;

import static org.junit.Assert.*;

/**
 * @author Alexandre Denis
 */
public class InputReaderTest
{
	@Test
	@SuppressWarnings("javadoc")
	public void testLiteralRead1()
	{
		DefaultLiteral literal = DefaultLiteral.readLiteral("A:proper_q(?C CR CS)");
		assertNotNull(literal);
		assertEquals(literal.getLabel(), new FeatureConstant("A"));
		assertEquals(literal.getPredicate(), new FeatureConstant("proper_q"));
		assertEquals(literal.getArguments().size(), 3);
		assertEquals(literal.getArgument(0), new FeatureVariable("?C"));
		assertEquals(literal.getArgument(1), new FeatureConstant("CR"));
	}


	@Test
	@SuppressWarnings("javadoc")
	public void testLiteralRead2()
	{
		DefaultLiteral literal = DefaultLiteral.readLiteral("qeq(BR L3)");
		assertNotNull(literal);
		assertNull(literal.getLabel());
		assertEquals(literal.getPredicate(), new FeatureConstant("qeq"));
		assertEquals(literal.getArguments().size(), 2);
	}
}
