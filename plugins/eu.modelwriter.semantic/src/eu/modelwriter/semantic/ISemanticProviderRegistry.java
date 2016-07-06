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
 * The {@link ISemanticProvider} registry maintains a set of active {@link ISemanticProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ISemanticProviderRegistry {

	/**
	 * Registers an {@link ISemanticProvider}.
	 * 
	 * @param base
	 *            the {@link ISemanticProvider} to register
	 */
	void register(ISemanticProvider base);

	/**
	 * Unregisters an {@link ISemanticProvider}.
	 * 
	 * @param base
	 *            the {@link ISemanticProvider} to unregister
	 */
	void unregister(ISemanticProvider base);

	/**
	 * Gets the {@link Set} of registered {@link ISemanticProvider}.
	 * 
	 * @return the {@link Set} of registered {@link ISemanticProvider}
	 */
	Set<ISemanticProvider> getProviders();

	/**
	 * Adds an {@link ISemanticProviderRegistryListener}.
	 * 
	 * @param listener
	 *            the {@link ISemanticProviderRegistryListener} to add
	 */
	void addListener(ISemanticProviderRegistryListener listener);

	/**
	 * Removes an {@link ISemanticProviderRegistryListener}.
	 * 
	 * @param listener
	 *            the {@link ISemanticProviderRegistryListener} to remove
	 */
	void removeListener(ISemanticProviderRegistryListener listener);
}
