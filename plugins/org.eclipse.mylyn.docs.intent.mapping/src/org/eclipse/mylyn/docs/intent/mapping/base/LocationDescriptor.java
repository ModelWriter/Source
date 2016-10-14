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

/**
 * {@link ILocationDescriptor} for an existing {@link ILocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class LocationDescriptor implements ILocationDescriptor {

	/**
	 * The {@link ILocation}.
	 */
	private final ILocation location;

	/**
	 * Constructor.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 */
	public LocationDescriptor(ILocation location) {
		this.location = location;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getContainerDescriptor()
	 */
	public ILocationDescriptor getContainerDescriptor() {
		final ILocationDescriptor res;

		if (location.getContainer() instanceof ILocation) {
			res = new LocationDescriptor((ILocation)location.getContainer());
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#exists(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
	 */
	public boolean exists(IBase base) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getLocation(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
	 */
	public ILocation getLocation(IBase base) {
		return location;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getOrCreate(org.eclipse.mylyn.docs.intent.mapping.base.IBase)
	 */
	public ILocation getOrCreate(IBase base) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return location;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#update(java.lang.Object)
	 */
	public boolean update(Object element) {
		return MappingUtils.getConnectorRegistry().updateLocation(location, element);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getElement()
	 */
	public ILocation getElement() {
		return location;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getName()
	 */
	public String getName() {
		return MappingUtils.getConnectorRegistry().getName(location);
	}

	@Override
	public int hashCode() {
		return location.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof LocationDescriptor && location.equals(((LocationDescriptor)obj).location);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#dispose()
	 */
	public void dispose() {
		// nothing to do here
	}

}
