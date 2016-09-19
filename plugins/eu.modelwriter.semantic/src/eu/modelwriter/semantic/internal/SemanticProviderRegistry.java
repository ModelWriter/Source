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

import eu.modelwriter.semantic.ISemanticProvider;
import eu.modelwriter.semantic.ISemanticProviderRegistry;
import eu.modelwriter.semantic.ISemanticProviderRegistryListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The default implementation of {@link ISemanticProviderRegistry}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticProviderRegistry implements ISemanticProviderRegistry {

	/**
	 * The {@link Set} of {@link SemanticProviderRegistry#register(ISemanticProvider) registered}
	 * {@link ISemanticProvider}.
	 */
	private final Set<ISemanticProvider> providers = new LinkedHashSet<ISemanticProvider>();

	/**
	 * The {@link List} of {@link ISemanticProviderRegistryListener}.
	 */
	private final List<ISemanticProviderRegistryListener> listeners = new ArrayList<ISemanticProviderRegistryListener>();

	/**
	 * Gets the {@link List} of {@link ISemanticProviderRegistryListener} in a thread save way.
	 * 
	 * @return the {@link List} of {@link ISemanticProviderRegistryListener} in a thread save way
	 */
	private List<ISemanticProviderRegistryListener> getListeners() {
		synchronized(listeners) {
			return new ArrayList<ISemanticProviderRegistryListener>(listeners);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ISemanticProviderRegistry#register(org.eclipse.mylyn.docs.intent.mapping.base.ISemanticProvider)
	 */
	public void register(ISemanticProvider provider) {
		final boolean added;
		synchronized(this) {
			added = providers.add(provider);
		}
		if (added) {
			for (ISemanticProviderRegistryListener listener : getListeners()) {
				listener.providerRegistred(provider);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ISemanticProviderRegistry#unregister(org.eclipse.mylyn.docs.intent.mapping.base.ISemanticProvider)
	 */
	public void unregister(ISemanticProvider provider) {
		final boolean removed;
		synchronized(this) {
			removed = providers.remove(provider);
		}
		if (removed) {
			for (ISemanticProviderRegistryListener listener : getListeners()) {
				listener.providerUnregistred(provider);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ISemanticProviderRegistry#getProviders()
	 */
	public Set<ISemanticProvider> getProviders() {
		synchronized(this) {
			return Collections.unmodifiableSet(providers);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ISemanticProviderRegistry#addListener(org.eclipse.mylyn.docs.intent.mapping.base.ISemanticProviderRegistryListener)
	 */
	public void addListener(ISemanticProviderRegistryListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ISemanticProviderRegistry#removeListener(org.eclipse.mylyn.docs.intent.mapping.base.ISemanticProviderRegistryListener)
	 */
	public void removeListener(ISemanticProviderRegistryListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}

}
