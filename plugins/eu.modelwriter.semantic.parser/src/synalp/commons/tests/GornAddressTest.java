package synalp.commons.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import synalp.commons.derivations.GornAddress;
import synalp.commons.grammar.*;


/**
 * Write a proper test suite and maybe move this class in junit packages.
 * @author Alexandre Denis
 */
public class GornAddressTest
{

	/**
	 * Test basic addressing.
	 */
	@Test
	public void testAddress()
	{
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node root = new Node();
		Tree tree = new Tree("", root);
		root.addChild(n1);
		n1.addChild(n2);
		n1.addChild(n3);

		// 0:root ( 0.0:n1 ( 0.0.0:n2 0.0.1:n3 ) )

		assertNull(tree.getNode(new GornAddress(0, 1)));
		assertNotNull(tree.getNode(new GornAddress(0, 0, 0)));
	}


	/**
	 * Test address construction.
	 */
	@Test
	public void testAddressCreation()
	{
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node root = new Node();
		Tree tree = new Tree("", root);
		root.addChild(n1);
		n1.addChild(n2);
		n1.addChild(n3);

		// 0:root ( 0.0:n1 ( 0.0.0:n2 0.0.1:n3 ) )

		assertEquals(GornAddress.getAddress(n1, tree), new GornAddress(0, 0));
		assertEquals(GornAddress.getAddress(n2, tree), new GornAddress(0, 0, 0));
		assertEquals(GornAddress.getAddress(n3, tree), new GornAddress(0, 0, 1));
	}


	/**
	 * Test address leftOf method.
	 */
	@Test
	public void testLeftOf()
	{
		assertTrue(new GornAddress(0, 0, 1).isLeftOf(new GornAddress(0, 0, 2)));
	}
}
