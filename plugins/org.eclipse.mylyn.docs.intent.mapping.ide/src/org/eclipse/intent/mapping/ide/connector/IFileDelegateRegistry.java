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
package org.eclipse.intent.mapping.ide.connector;

import java.util.List;

/**
 * The {@link IFileConnectorDelegate} registry maintains a {@link List} of active
 * {@link IFileConnectorDelegate}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IFileDelegateRegistry {

	/**
	 * Registers the given {@link IFileConnectorDelegate}.
	 * 
	 * @param delegate
	 *            the {@link IFileConnectorDelegate} to register
	 */
	void register(IFileConnectorDelegate delegate);

	/**
	 * Unregisters the given {@link IFileConnectorDelegate}.
	 * 
	 * @param delegate
	 *            the {@link IFileConnectorDelegate} to register
	 */
	void unregister(IFileConnectorDelegate delegate);

	/**
	 * Gets the {@link List} of {@link IFileDelegateRegistry#register(IFileConnectorDelegate) registered}
	 * {@link IFileConnectorDelegate}.
	 * 
	 * @return the {@link List} of {@link IFileDelegateRegistry#register(IFileConnectorDelegate) registered}
	 *         {@link IFileConnectorDelegate}
	 */
	List<IFileConnectorDelegate> getConnectorDelegates();

}
