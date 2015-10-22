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
package org.eclipse.mylyn.docs.intent.mapping.conector;

import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;

/**
 * An abstract implementation of {@link IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractConnector implements IConnector {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnector#createLocation(org.eclipse.mylyn.docs.intent.mapping.base.IBase,
	 *      org.eclipse.mylyn.docs.intent.mapping.base.ILocation, java.lang.Object)
	 */
	public ILocation createLocation(IBase base, ILocation container, Object element)
			throws InstantiationException, IllegalAccessException {
		final ILocation res;

		final Class<? extends ILocation> locationType = getLocationType(getContainerType(container), element);
		if (locationType != null) {
			final ILocation location = base.getFactory().createElement(locationType);
			if (location == null) {
				throw new IllegalArgumentException("The base can't create " + locationType.getSimpleName());
			} else {
				initLocation(location, element);
				location.setContainer(location);
				res = location;
			}
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * Gets the container type of the given {@link ILocation container}.
	 * 
	 * @param container
	 *            the containing {@link ILocation} can be <code>null</code>
	 * @return the container type of the given {@link ILocation container}
	 */
	private Class<? extends ILocation> getContainerType(ILocation container) {
		final Class<? extends ILocation> res;

		if (container != null) {
			res = container.getClass();
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * Gets the {@link ILocation} type according to the given container type and an element to locate.
	 * 
	 * @param containerType
	 *            the type of the containing {@link ILocation} can be <code>null</code> if not contained
	 * @param element
	 *            the Element object to locate
	 * @return the {@link ILocation} type according to the given container type and an element to locate if
	 *         any is handled by this {@link IConnector}, <code>null</code> otherwise
	 */
	protected abstract Class<? extends ILocation> getLocationType(Class<? extends ILocation> containerType,
			Object element);

	/**
	 * Initializes the given {@link ILocation}.
	 * 
	 * @param location
	 *            the {@link ILocation} to initialize
	 * @param element
	 *            the element to locate
	 */
	protected abstract void initLocation(ILocation location, Object element);

}
