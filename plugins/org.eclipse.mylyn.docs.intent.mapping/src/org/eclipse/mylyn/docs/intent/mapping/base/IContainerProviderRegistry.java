/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.base;

import org.eclipse.mylyn.docs.intent.mapping.connector.IConnector;
import org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider;

/**
 * A registry for {@link IContainerProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IContainerProviderRegistry {

	/**
	 * Registers the given {@link IContainerProvider}.
	 * 
	 * @param provider
	 *            the {@link IConnector} to register
	 */
	void register(IContainerProvider provider);

	/**
	 * Unregisters the given {@link IContainerProvider}.
	 * 
	 * @param provider
	 *            the {@link IContainerProvider} to register
	 */
	void unregister(IContainerProvider provider);

	/**
	 * Gets the container of the given element.
	 * 
	 * @param element
	 *            the element
	 * @return the container of the given element if any, <code>null</code> otherwise
	 */
	Object getContainer(Object element);

}
