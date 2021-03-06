/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package eu.modelwriter.semantic.tests.internal;

import eu.modelwriter.semantic.IBase;
import eu.modelwriter.semantic.IBaseRegistryListener;
import eu.modelwriter.semantic.internal.BaseRegistry;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link BaseRegistry}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class BaseRegistryTests {

	/**
	 * A test implementation of {@link IBaseRegistryListener}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	private final class TestBaseRegitryListener implements IBaseRegistryListener {

		/**
		 * Number of time {@link TestBaseRegitryListener#baseRegistred(IBase)} has been called.
		 */
		private int baseUnregistred;

		/**
		 * Number of time {@link TestBaseRegitryListener#baseUnregistred(IBase)} has been called.
		 */
		private int baseRegistred;

		public void baseRegistred(IBase base) {
			baseRegistred++;
		}

		public void baseUnregistred(IBase base) {
			baseUnregistred++;
		}

	}

	/**
	 * A test {@link IBase} implementation.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestBase implements IBase {

		public String getName() {
			// nothing to do here
			return null;
		}

		public Set<?> getConcepts() {
			// nothing to do here
			return null;
		}

	}

	/**
	 * The {@link BaseRegistry} to test.
	 */
	private BaseRegistry baseRegistery;

	/**
	 * The {@link TestBaseRegitryListener} that will be
	 * {@link org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistry#register(IBase) registered}.
	 */
	private TestBaseRegitryListener listener;

	/**
	 * The {@link TestBaseRegitryListener} that will be
	 * {@link org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistry#register(IBase) registered} then
	 * {@link org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistry#unregister(IBase) unregistered}.
	 */
	private TestBaseRegitryListener removedListener;

	protected void assertTestBaseRegitryListener(TestBaseRegitryListener l, int baseRegistred,
			int baseUnregistred) {
		assertEquals(baseRegistred, l.baseRegistred);
		assertEquals(baseUnregistred, l.baseUnregistred);
	}

	@Before
	public void before() {
		baseRegistery = new BaseRegistry();
		listener = new TestBaseRegitryListener();
		removedListener = new TestBaseRegitryListener();
		baseRegistery.addListener(listener);
		baseRegistery.addListener(removedListener);
		baseRegistery.removeListener(removedListener);

	}

	@After
	public void after() {
		baseRegistery.removeListener(listener);
	}

	@Test
	public void register() {
		final IBase base = new TestBase();
		baseRegistery.register(base);

		assertEquals(1, baseRegistery.getBases().size());

		assertTestBaseRegitryListener(listener, 1, 0);
		assertTestBaseRegitryListener(removedListener, 0, 0);
	}

	@Test
	public void unregister() {
		final IBase base = new TestBase();
		baseRegistery.register(base);

		assertEquals(1, baseRegistery.getBases().size());

		baseRegistery.unregister(base);

		assertEquals(0, baseRegistery.getBases().size());

		assertTestBaseRegitryListener(listener, 1, 1);
		assertTestBaseRegitryListener(removedListener, 0, 0);
	}

	@Test
	public void getBases() {
		final IBase base0 = new TestBase();
		final IBase base1 = new TestBase();
		final IBase base2 = new TestBase();
		baseRegistery.register(base0);
		baseRegistery.register(base1);
		baseRegistery.register(base2);

		final Set<IBase> bases = baseRegistery.getBases();
		assertEquals(3, bases.size());

		assertTrue(bases.contains(base0));
		assertTrue(bases.contains(base1));
		assertTrue(bases.contains(base2));

		assertTestBaseRegitryListener(listener, 3, 0);
		assertTestBaseRegitryListener(removedListener, 0, 0);
	}

}
