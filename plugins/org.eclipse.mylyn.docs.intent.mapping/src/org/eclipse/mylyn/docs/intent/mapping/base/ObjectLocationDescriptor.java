/*******************************************************************************
 * Copyright (c) 2016 Obeo.
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

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.conector.IConnector;

/**
 * {@link ILocationDescriptor} based on a {@link Object} and a location type.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ObjectLocationDescriptor implements ILocationDescriptor {

	/**
	 * The {@link Object}.
	 */
	private final Object element;

	/**
	 * The {@link org.eclipse.mylyn.docs.intent.mapping.conector.IConnector#getType() connector type}.
	 */
	private final Class<? extends ILocation> type;

	/**
	 * The {@link ILocationDescriptor#getContainerDescriptor() container descriptor}.
	 */
	private final ILocationDescriptor containerDescriptor;

	/**
	 * The human readable name.
	 */
	private final String name;

	/**
	 * Constructor.
	 * 
	 * @param containerDescriptor
	 *            the {@link ILocationDescriptor#getContainerDescriptor() container descriptor}
	 * @param element
	 *            the {@link Object}
	 * @param name
	 *            the human readable name
	 * @param type
	 *            the {@link org.eclipse.mylyn.docs.intent.mapping.conector.IConnector#getType() connector
	 *            type}
	 */
	public ObjectLocationDescriptor(ILocationDescriptor containerDescriptor, Object element, String name,
			Class<? extends ILocation> type) {
		this.containerDescriptor = containerDescriptor;
		this.element = element;
		this.name = name;
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getContainerDescriptor()
	 */
	public ILocationDescriptor getContainerDescriptor() {
		return containerDescriptor;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getConnectorType()
	 */
	public Class<? extends ILocation> getConnectorType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#exists(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
	 */
	public boolean exists(IBase base) {
		return getLocation(base) != null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getLocation(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
	 */
	public ILocation getLocation(IBase base) {
		final ILocation res;

		if (getContainerDescriptor() == null) {
			final IConnector connector = MappingUtils.getConnectorRegistry().getConnector(getConnectorType());
			res = connector.getLocation(base, element);
		} else if (getContainerDescriptor().exists(base)) {
			final IConnector connector = MappingUtils.getConnectorRegistry().getConnector(getConnectorType());
			res = connector.getLocation(getContainerDescriptor().getLocation(base), element);
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getOrCreate(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
	 */
	public ILocation getOrCreate(IBase base) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		final ILocation res;

		final ILocationContainer container;
		if (getContainerDescriptor() == null) {
			container = base;
		} else {
			container = getContainerDescriptor().getOrCreate(base);
		}

		final IConnector connector = MappingUtils.getConnectorRegistry().getConnector(getConnectorType());
		final ILocation existingLocation = connector.getLocation(container, element);
		if (existingLocation != null) {
			res = existingLocation;
		} else {
			res = connector.createLocation(container, element);
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getElement()
	 */
	public Object getElement() {
		return element;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getName()
	 */
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return element.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ObjectLocationDescriptor
				&& element.equals(((ObjectLocationDescriptor)obj).element);
	}

}
