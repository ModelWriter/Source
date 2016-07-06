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
package eu.modelwriter.semantic.internal;

import eu.modelwriter.semantic.ISemanticSimilarityProvider;
import eu.modelwriter.semantic.ISemanticSimilarityProviderRegistry;
import eu.modelwriter.semantic.ISemanticSimilarityProviderRegistryListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The default implementation of {@link ISemanticSimilarityProvider}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticSimilarityProviderRegistry implements ISemanticSimilarityProviderRegistry {

	/**
	 * The {@link Set} of {@link SemanticSimilarityProviderRegistry#register(ISemanticSimilarityProvider)
	 * registered} {@link ISemanticSimilarityProvider}.
	 */
	private final Set<ISemanticSimilarityProvider> bases = new LinkedHashSet<ISemanticSimilarityProvider>();

	/**
	 * The {@link List} of {@link ISemanticSimilarityProviderRegistryListener}.
	 */
	private final List<ISemanticSimilarityProviderRegistryListener> listeners = new ArrayList<ISemanticSimilarityProviderRegistryListener>();

	/**
	 * Gets the {@link List} of {@link ISemanticSimilarityProviderRegistryListener} in a thread save way.
	 * 
	 * @return the {@link List} of {@link ISemanticSimilarityProviderRegistryListener} in a thread save way
	 */
	private List<ISemanticSimilarityProviderRegistryListener> getListeners() {
		synchronized(listeners) {
			return new ArrayList<ISemanticSimilarityProviderRegistryListener>(listeners);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ISemanticSimilarityProviderRegistry#register(org.eclipse.mylyn.docs.intent.mapping.base.ISemanticSimilarityProvider)
	 */
	public void register(ISemanticSimilarityProvider base) {
		final boolean added;
		synchronized(this) {
			added = bases.add(base);
		}
		if (added) {
			for (ISemanticSimilarityProviderRegistryListener listener : getListeners()) {
				listener.baseRegistred(base);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ISemanticSimilarityProviderRegistry#unregister(org.eclipse.mylyn.docs.intent.mapping.base.ISemanticSimilarityProvider)
	 */
	public void unregister(ISemanticSimilarityProvider base) {
		final boolean removed;
		synchronized(this) {
			removed = bases.remove(base);
		}
		if (removed) {
			for (ISemanticSimilarityProviderRegistryListener listener : getListeners()) {
				listener.baseUnregistred(base);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ISemanticSimilarityProviderRegistry#getProviders()
	 */
	public Set<ISemanticSimilarityProvider> getProviders() {
		synchronized(this) {
			return Collections.unmodifiableSet(bases);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ISemanticSimilarityProviderRegistry#addListener(org.eclipse.mylyn.docs.intent.mapping.base.ISemanticSimilarityProviderRegistryListener)
	 */
	public void addListener(ISemanticSimilarityProviderRegistryListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ISemanticSimilarityProviderRegistry#removeListener(org.eclipse.mylyn.docs.intent.mapping.base.ISemanticSimilarityProviderRegistryListener)
	 */
	public void removeListener(ISemanticSimilarityProviderRegistryListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}

}
