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

import java.util.Set;

/**
 * The {@link ISemanticSimilarityProvider} registry maintains a set of active
 * {@link ISemanticSimilarityProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ISemanticSimilarityProviderRegistry {

	/**
	 * Registers an {@link ISemanticSimilarityProvider}.
	 * 
	 * @param base
	 *            the {@link ISemanticSimilarityProvider} to register
	 */
	void register(ISemanticSimilarityProvider base);

	/**
	 * Unregisters an {@link ISemanticSimilarityProvider}.
	 * 
	 * @param base
	 *            the {@link ISemanticSimilarityProvider} to unregister
	 */
	void unregister(ISemanticSimilarityProvider base);

	/**
	 * Gets the {@link Set} of registered {@link ISemanticSimilarityProvider}.
	 * 
	 * @return the {@link Set} of registered {@link ISemanticSimilarityProvider}
	 */
	Set<ISemanticSimilarityProvider> getProviders();

	/**
	 * Adds an {@link ISemanticSimilarityProviderRegistryListener}.
	 * 
	 * @param listener
	 *            the {@link ISemanticSimilarityProviderRegistryListener} to add
	 */
	void addListener(ISemanticSimilarityProviderRegistryListener listener);

	/**
	 * Removes an {@link ISemanticSimilarityProviderRegistryListener}.
	 * 
	 * @param listener
	 *            the {@link ISemanticSimilarityProviderRegistryListener} to remove
	 */
	void removeListener(ISemanticSimilarityProviderRegistryListener listener);
}
