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

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;

/**
 * The connector is in charge of {@link ILocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IConnector {

	/**
	 * Creates the {@link ILocation} according to the given container and an element to locate.
	 * 
	 * @param container
	 *            the {@link ILocationContainer}, it must be contained into an
	 *            {@link org.eclipse.mylyn.docs.intent.mapping.base.IBase IBase}
	 * @param element
	 *            the Element object to locate
	 * @return the created {@link ILocation} according to the given container and an element to locate if any
	 *         is handled by this {@link IConnector}, <code>null</code> otherwise
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason.
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	ILocation createLocation(ILocationContainer container, Object element) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException;

	/**
	 * Gets the {@link ILocation} according to the given container and an element to locate.
	 * 
	 * @param container
	 *            the type of the containing {@link ILocationContainer}
	 * @param element
	 *            the Element object to locate
	 * @return the {@link ILocation} according to the given container and an element to locate if any is
	 *         handled by this {@link IConnector} and found, <code>null</code> otherwise
	 */
	ILocation getLocation(ILocationContainer container, Object element);

	/**
	 * Gets the {@link ILocationDescriptor} for the given element.
	 * 
	 * @param containerDescriptor
	 *            the container {@link ILocationDescriptor} can be <code>null</code>
	 * @param element
	 *            the element
	 * @return the {@link ILocationDescriptor} for the given element if handled by this {@link IConnector},
	 *         <code>null</code> otherwise
	 */
	ILocationDescriptor getLocationDescriptor(ILocationDescriptor containerDescriptor, Object element);

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
	Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
			Object element);

	/**
	 * Gets a human readable name for the given {@link ILocation}.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 * @return a human readable name for the given {@link ILocation} if any, <code>null</code> otherwise
	 */
	String getName(ILocation location);

	/**
	 * Gets the {@link ILocation} {@link Class} managed by this connector.
	 * 
	 * @return the {@link ILocation} {@link Class} managed by this connector
	 */
	Class<? extends ILocation> getType();

}
