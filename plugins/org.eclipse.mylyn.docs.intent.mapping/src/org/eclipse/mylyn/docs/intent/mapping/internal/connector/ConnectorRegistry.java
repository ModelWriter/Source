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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.connector.IConnector;
import org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry;

/**
 * The default implementation of {@link IConnectorRegistry}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ConnectorRegistry implements IConnectorRegistry {

	/**
	 * The {@link List} of {@link ConnectorRegistry#register(IConnector) registered} {@link IConnector}.
	 */
	private final List<IConnector> connectors = Collections.synchronizedList(new ArrayList<IConnector>());

	/**
	 * Mapping of {@link IConnector#getType() connector type} to {@link IConnector}.
	 */
	private final Map<Class<? extends ILocation>, IConnector> typeToConnector = new HashMap<Class<? extends ILocation>, IConnector>();

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#createLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer,
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
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#getLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer,
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
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#getElement(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public Object getElement(ILocation location) {
		for (IConnector connector : getConnectors()) {
			if (connector.canHandle(location)) {
				return connector.getElement(location);
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#getOrCreateLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer,
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
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#getLocationType(java.lang.Class,
	 *      java.lang.Object)
	 */
	public Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
			Object element) {
		for (IConnector connector : getConnectors()) {
			final Class<? extends ILocation> locationType = connector.getLocationType(containerType, element);
			if (locationType != null) {
				return locationType;
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#getName(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public String getName(ILocation location) {
		String res;
		if (!location.isMarkedAsDeleted()) {
			res = null;
			for (IConnector connector : getConnectors()) {
				if (connector.canHandle(location)) {
					res = connector.getName(location);
					break;
				}
			}
		} else {
			res = "(deleted)";
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#getLocationDescriptor(org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor,
	 *      java.lang.Object)
	 */
	public ILocationDescriptor getLocationDescriptor(ILocationDescriptor containerDescriptor,
			Object element) {
		for (IConnector connector : getConnectors()) {
			final ILocationDescriptor descriptor = connector.getLocationDescriptor(containerDescriptor,
					element);
			if (descriptor != null) {
				return descriptor;
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#updateLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocation,
	 *      java.lang.Object)
	 */
	public boolean updateLocation(ILocation location, Object element) {
		if (!location.isMarkedAsDeleted()) {
			for (IConnector connector : getConnectors()) {
				if (connector.canHandle(location)) {
					return connector.updateLocation(location, element);
				}
			}
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#register(org.eclipse.mylyn.docs.intent.mapping.connector.IConnector)
	 */
	public void register(IConnector connector) {
		if (connector != null) {
			synchronized(connectors) {
				int index = 0;
				boolean added = false;
				for (IConnector currentConnector : connectors) {
					if (currentConnector.getType().isAssignableFrom(connector.getType())) {
						connectors.add(index, connector);
						added = true;
						break;
					} else {
						index++;
					}
				}
				if (!added) {
					connectors.add(index, connector);
				}
				typeToConnector.put(connector.getType(), connector);
			}
		} else {
			throw new IllegalArgumentException("IConnector can't be null.");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#unregister(org.eclipse.mylyn.docs.intent.mapping.connector.IConnector)
	 */
	public void unregister(IConnector connector) {
		synchronized(connectors) {
			connectors.remove(connector);
			typeToConnector.remove(connector.getType());
		}
	}

	/**
	 * Gets the {@link List} of {@link IConnectorRegistry#register(IConnector) registered} {@link IConnector}.
	 * 
	 * @return the {@link List} of {@link IConnectorRegistry#register(IConnector) registered}
	 *         {@link IConnector}
	 */
	protected List<IConnector> getConnectors() {
		synchronized(connectors) {
			return Collections.unmodifiableList(new ArrayList<IConnector>(connectors));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#getConnector(java.lang.Class)
	 */
	public IConnector getConnector(Class<? extends ILocation> connectorType) {
		synchronized(connectors) {
			return typeToConnector.get(connectorType);
		}
	}

}
