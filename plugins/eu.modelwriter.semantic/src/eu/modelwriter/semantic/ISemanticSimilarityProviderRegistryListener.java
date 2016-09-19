/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *******************************************************************************/
package eu.modelwriter.semantic;

/**
 * Listen to {@link ISemanticSimilarityProviderRegistry} changes.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ISemanticSimilarityProviderRegistryListener {

	/**
	 * Stub implementation.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class Stub implements ISemanticSimilarityProviderRegistryListener {

		/**
		 * {@inheritDoc}
		 *
		 * @see eu.modelwriter.semantic.ISemanticSimilarityProviderRegistryListener#providerRegistred(eu.modelwriter.semantic.ISemanticSimilarityProvider)
		 */
		public void providerRegistred(ISemanticSimilarityProvider provider) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see eu.modelwriter.semantic.ISemanticSimilarityProviderRegistryListener#providerUnregistred(eu.modelwriter.semantic.ISemanticSimilarityProvider)
		 */
		public void providerUnregistred(ISemanticSimilarityProvider provider) {
			// nothing to do here
		}

	}

	/**
	 * Notification when an {@link ISemanticSimilarityProvider} is
	 * {@link ISemanticSimilarityProviderRegistry#register(ISemanticSimilarityProvider) registered}.
	 * 
	 * @param provider
	 *            the registered {@link ISemanticSimilarityProvider}
	 */
	void providerRegistred(ISemanticSimilarityProvider provider);

	/**
	 * Notification when an {@link ISemanticSimilarityProvider} is
	 * {@link ISemanticSimilarityProviderRegistry#unregister(ISemanticSimilarityProvider) unregistered}.
	 * 
	 * @param provider
	 *            the unregistered {@link ISemanticSimilarityProvider}
	 */
	void providerUnregistred(ISemanticSimilarityProvider provider);

}
