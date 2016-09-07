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
package org.eclipse.mylyn.docs.intent.mapping.internal.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistry;
import org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistryListener;

/**
 * The default implementation of {@link IBaseRegistry}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class BaseRegistry implements IBaseRegistry {

	/**
	 * The {@link Set} of {@link BaseRegistry#register(IBase) registered} {@link IBase}.
	 */
	private final Set<IBase> bases = new LinkedHashSet<IBase>();

	/**
	 * The {@link List} of {@link IBaseRegistryListener}.
	 */
	private final List<IBaseRegistryListener> listeners = new ArrayList<IBaseRegistryListener>();

	/**
	 * Gets the {@link List} of {@link IBaseRegistryListener} in a thread safe way.
	 * 
	 * @return the {@link List} of {@link IBaseRegistryListener} in a thread safe way
	 */
	private List<IBaseRegistryListener> getListeners() {
		synchronized(listeners) {
			return new ArrayList<IBaseRegistryListener>(listeners);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistry#register(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
	 */
	public void register(IBase base) {
		final boolean added;
		synchronized(this) {
			added = bases.add(base);
		}
		if (added) {
			for (IBaseRegistryListener listener : getListeners()) {
				listener.baseRegistred(base);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistry#unregister(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
	 */
	public void unregister(IBase base) {
		final boolean removed;
		synchronized(this) {
			removed = bases.remove(base);
		}
		if (removed) {
			for (IBaseRegistryListener listener : getListeners()) {
				listener.baseUnregistred(base);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistry#getBases()
	 */
	public Set<IBase> getBases() {
		synchronized(this) {
			return Collections.unmodifiableSet(bases);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistry#addListener(org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistryListener)
	 */
	public void addListener(IBaseRegistryListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistry#removeListener(org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistryListener)
	 */
	public void removeListener(IBaseRegistryListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}

}
