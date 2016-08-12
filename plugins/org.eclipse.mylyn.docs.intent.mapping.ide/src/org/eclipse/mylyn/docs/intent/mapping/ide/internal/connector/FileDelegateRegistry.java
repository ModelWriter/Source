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
package org.eclipse.mylyn.docs.intent.mapping.ide.internal.connector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileDelegateRegistry;

/**
 * The default implementation of {@link IFileDelegateRegistry}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class FileDelegateRegistry implements IFileDelegateRegistry {

	/**
	 * The {@link List} of {@link FileDelegateRegistry#register(IFileConnectorDelegate) registered}
	 * {@link IFileConnectorDelegate}.
	 */
	private final List<IFileConnectorDelegate> connectorDelegates = Collections
			.synchronizedList(new ArrayList<IFileConnectorDelegate>());

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileDelegateRegistry#register(org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate)
	 */
	public void register(IFileConnectorDelegate delegate) {
		if (delegate != null) {
			synchronized(connectorDelegates) {
				int index = 0;
				boolean added = false;
				for (IFileConnectorDelegate connectorDelegate : connectorDelegates) {
					if (delegate.getContentType().isKindOf(connectorDelegate.getContentType())) {
						connectorDelegates.add(index, delegate);
						added = true;
						break;
					} else {
						index++;
					}
				}
				if (!added) {
					connectorDelegates.add(index, delegate);
				}
			}
		} else {
			throw new IllegalArgumentException("IFileConnectorDelegate can't be null.");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileDelegateRegistry#unregister(org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate)
	 */
	public void unregister(IFileConnectorDelegate delegate) {
		connectorDelegates.remove(delegate);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileDelegateRegistry#getConnectorDelegates()
	 */
	public List<IFileConnectorDelegate> getConnectorDelegates() {
		synchronized(connectorDelegates) {
			return Collections.unmodifiableList(connectorDelegates);
		}
	}

}
