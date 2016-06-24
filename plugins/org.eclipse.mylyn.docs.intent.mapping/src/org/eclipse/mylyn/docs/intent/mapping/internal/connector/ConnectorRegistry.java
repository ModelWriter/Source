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
package org.eclipse.mylyn.docs.intent.mapping.internal.connector;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.conector.IConnector;
import org.eclipse.mylyn.docs.intent.mapping.conector.IConnectorRegistry;

/**
 * The default implementation of {@link IConnectorRegistry}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ConnectorRegistry implements IConnectorRegistry {

	/**
	 * The {@link Set} of {@link ConnectorRegistry#register(IBase) registered} {@link IConnector}.
	 */
	private final Set<IConnector> connectors = Collections.synchronizedSet(new LinkedHashSet<IConnector>());

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnectorRegistry#createLocation(org.eclipse.mylyn.docs.intent.mapping.base.IBase,
	 *      org.eclipse.mylyn.docs.intent.mapping.base.ILocation, java.lang.Object)
	 */
	public ILocation createLocation(IBase base, ILocation container, Object element)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		for (IConnector connector : getConnectors()) {
			final ILocation location = connector.createLocation(base, container, element);
			if (location != null) {
				return location;
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnectorRegistry#getLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocation,
	 *      java.lang.Object)
	 */
	public ILocation getLocation(ILocation container, Object element) {
		for (IConnector connector : getConnectors()) {
			final ILocation location = connector.getLocation(container, element);
			if (location != null) {
				return location;
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnectorRegistry#register(org.eclipse.mylyn.docs.intent.mapping.conector.IConnector)
	 */
	public void register(IConnector connector) {
		connectors.add(connector);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnectorRegistry#unregister(org.eclipse.mylyn.docs.intent.mapping.conector.IConnector)
	 */
	public void unregister(IConnector connector) {
		connectors.remove(connector);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnectorRegistry#getConnectors()
	 */
	public Set<IConnector> getConnectors() {
		synchronized(connectors) {
			return Collections.unmodifiableSet(connectors);
		}
	}

}
