/*******************************************************************************
 * Copyright (c) 2016 Obeo.
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

import eu.modelwriter.semantic.ISemanticProvider;
import eu.modelwriter.semantic.ISemanticProviderRegistryListener;
import eu.modelwriter.semantic.StringSemanticProvider;
import eu.modelwriter.semantic.internal.SemanticProviderRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests {@link SemanticProviderRegistry}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticProviderRegistryTests {

	/**
	 * Test implementation of {@link ISemanticProviderRegistryListener}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestSemanticProviderRegistryListener implements ISemanticProviderRegistryListener {

		/**
		 * Number of times {@link ISemanticProviderRegistryListener#providerRegistred(ISemanticProvider)} has
		 * been called.
		 */
		private int providerRegistred;

		/**
		 * Number of times {@link ISemanticProviderRegistryListener#providerUnregistred(ISemanticProvider)}
		 * has been called.
		 */
		private int providerUnregistred;

		/**
		 * {@inheritDoc}
		 *
		 * @see eu.modelwriter.semantic.ISemanticProviderRegistryListener#providerRegistred(eu.modelwriter.semantic.ISemanticProvider)
		 */
		public void providerRegistred(ISemanticProvider provider) {
			providerRegistred++;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see eu.modelwriter.semantic.ISemanticProviderRegistryListener#providerUnregistred(eu.modelwriter.semantic.ISemanticProvider)
		 */
		public void providerUnregistred(ISemanticProvider provider) {
			providerUnregistred++;
		}
	}

	@Test
	public void registerNull() {
		final SemanticProviderRegistry registry = new SemanticProviderRegistry();
		final TestSemanticProviderRegistryListener listener = new TestSemanticProviderRegistryListener();
		registry.addListener(listener);

		registry.register(null);

		assertEquals(1, registry.getProviders().size());
		assertNull(registry.getProviders().iterator().next());
		assertSemanticProviderRegistryListener(listener, 1, 0);
	}

	@Test
	public void register() {
		final SemanticProviderRegistry registry = new SemanticProviderRegistry();
		final TestSemanticProviderRegistryListener listener = new TestSemanticProviderRegistryListener();
		registry.addListener(listener);

		final ISemanticProvider provider = new StringSemanticProvider();

		registry.register(provider);

		assertEquals(1, registry.getProviders().size());
		assertEquals(provider, registry.getProviders().iterator().next());
		assertSemanticProviderRegistryListener(listener, 1, 0);
	}

	@Test
	public void unregisterNull() {
		final SemanticProviderRegistry registry = new SemanticProviderRegistry();
		final TestSemanticProviderRegistryListener listener = new TestSemanticProviderRegistryListener();

		registry.register(null);

		registry.addListener(listener);

		registry.unregister(null);

		assertEquals(0, registry.getProviders().size());
		assertSemanticProviderRegistryListener(listener, 0, 1);
	}

	@Test
	public void unregister() {
		final SemanticProviderRegistry registry = new SemanticProviderRegistry();
		final TestSemanticProviderRegistryListener listener = new TestSemanticProviderRegistryListener();

		final ISemanticProvider provider = new StringSemanticProvider();

		registry.register(provider);

		registry.addListener(listener);

		registry.unregister(provider);

		assertEquals(0, registry.getProviders().size());
		assertSemanticProviderRegistryListener(listener, 0, 1);
	}

	public static void assertSemanticProviderRegistryListener(TestSemanticProviderRegistryListener listener,
			int providerRegistred, int providerUnregistred) {
		assertEquals(providerRegistred, listener.providerRegistred);
		assertEquals(providerUnregistred, listener.providerUnregistred);
	}

}
