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

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.conector.IConnector;
import org.eclipse.mylyn.docs.intent.mapping.conector.IConnectorRegistry;

/**
 * The default implementation of {@link IConnectorRegistry}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ConnectorRegistry implements IConnectorRegistry {

	/**
	 * The {@link Set} of {@link ConnectorRegistry#register(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
	 * registered} {@link IConnector}.
	 */
	private final Set<IConnector> connectors = Collections.synchronizedSet(new LinkedHashSet<IConnector>());

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnectorRegistry#createLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer,
	 *      java.lang.Object)
	 */
	public ILocation createLocation(ILocationContainer container, Object element)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		for (IConnector connector : getConnectors()) {
			final ILocation location = connector.createLocation(container, element);
			if (location != null) {
				return location;
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnectorRegistry#getLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer,
	 *      java.lang.Object)
	 */
	public ILocation getLocation(ILocationContainer container, Object element) {
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
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnectorRegistry#getOrCreateLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer,
	 *      java.lang.Object)
	 */
	public ILocation getOrCreateLocation(ILocationContainer container, Object element)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final ILocation res;

		final ILocation foundLocation = getLocation(container, element);
		if (foundLocation != null) {
			res = foundLocation;
		} else {
			res = createLocation(container, element);
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnectorRegistry#getName(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public String getName(ILocation location) {
		for (IConnector connector : getConnectors()) {
			final String name = connector.getName(location);
			if (name != null) {
				return name;
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
