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

import eu.modelwriter.semantic.ISemanticSimilarityProvider;
import eu.modelwriter.semantic.ISemanticSimilarityProviderRegistryListener;
import eu.modelwriter.semantic.IdentitySimilarityProvider;
import eu.modelwriter.semantic.internal.SemanticSimilarityProviderRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests {@link SemanticSimilarityProviderRegistry}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticSimilarityProviderRegistryTests {

	/**
	 * Test implementation of {@link ISemanticSimilarityProviderRegistryListener}.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class TestSemanticSimilarityProviderRegistryListener implements ISemanticSimilarityProviderRegistryListener {

		/**
		 * Number of times
		 * {@link ISemanticSimilarityProviderRegistryListener#providerRegistred(ISemanticSimilarityProvider)}
		 * has been called.
		 */
		private int providerRegistred;

		/**
		 * Number of times
		 * {@link ISemanticSimilarityProviderRegistryListener#providerUnregistred(ISemanticSimilarityProvider)}
		 * has been called.
		 */
		private int providerUnregistred;

		/**
		 * {@inheritDoc}
		 *
		 * @see eu.modelwriter.semantic.ISemanticSimilarityProviderRegistryListener#providerRegistred(eu.modelwriter.semantic.ISemanticSimilarityProvider)
		 */
		public void providerRegistred(ISemanticSimilarityProvider provider) {
			providerRegistred++;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see eu.modelwriter.semantic.ISemanticSimilarityProviderRegistryListener#providerUnregistred(eu.modelwriter.semantic.ISemanticSimilarityProvider)
		 */
		public void providerUnregistred(ISemanticSimilarityProvider provider) {
			providerUnregistred++;
		}
	}

	@Test
	public void registerNull() {
		final SemanticSimilarityProviderRegistry registry = new SemanticSimilarityProviderRegistry();
		final TestSemanticSimilarityProviderRegistryListener listener = new TestSemanticSimilarityProviderRegistryListener();
		registry.addListener(listener);

		registry.register(null);

		assertEquals(1, registry.getProviders().size());
		assertNull(registry.getProviders().iterator().next());
		assertSemanticSimilarityProviderRegistryListener(listener, 1, 0);
	}

	@Test
	public void register() {
		final SemanticSimilarityProviderRegistry registry = new SemanticSimilarityProviderRegistry();
		final TestSemanticSimilarityProviderRegistryListener listener = new TestSemanticSimilarityProviderRegistryListener();
		registry.addListener(listener);

		final ISemanticSimilarityProvider provider = new IdentitySimilarityProvider();

		registry.register(provider);

		assertEquals(1, registry.getProviders().size());
		assertEquals(provider, registry.getProviders().iterator().next());
		assertSemanticSimilarityProviderRegistryListener(listener, 1, 0);
	}

	@Test
	public void unregisterNull() {
		final SemanticSimilarityProviderRegistry registry = new SemanticSimilarityProviderRegistry();
		final TestSemanticSimilarityProviderRegistryListener listener = new TestSemanticSimilarityProviderRegistryListener();

		registry.register(null);

		registry.addListener(listener);

		registry.unregister(null);

		assertEquals(0, registry.getProviders().size());
		assertSemanticSimilarityProviderRegistryListener(listener, 0, 1);
	}

	@Test
	public void unregister() {
		final SemanticSimilarityProviderRegistry registry = new SemanticSimilarityProviderRegistry();
		final TestSemanticSimilarityProviderRegistryListener listener = new TestSemanticSimilarityProviderRegistryListener();

		final ISemanticSimilarityProvider provider = new IdentitySimilarityProvider();

		registry.register(provider);

		registry.addListener(listener);

		registry.unregister(provider);

		assertEquals(0, registry.getProviders().size());
		assertSemanticSimilarityProviderRegistryListener(listener, 0, 1);
	}

	public static void assertSemanticSimilarityProviderRegistryListener(
			TestSemanticSimilarityProviderRegistryListener listener, int providerRegistred,
			int providerUnregistred) {
		assertEquals(providerRegistred, listener.providerRegistred);
		assertEquals(providerUnregistred, listener.providerUnregistred);
	}

}
