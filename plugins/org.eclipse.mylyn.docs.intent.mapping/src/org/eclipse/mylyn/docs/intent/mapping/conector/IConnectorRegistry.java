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

import java.util.Set;

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;

/**
 * The {@link IConnector} registry maintains a set of active {@link IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IConnectorRegistry {

	/**
	 * Creates the {@link ILocation} according to the given container and an element to locate.
	 * 
	 * @param container
	 *            the {@link ILocationContainer}, it must be contained into an
	 *            {@link org.eclipse.mylyn.docs.intent.mapping.base.IBase IBase}
	 * @param element
	 *            the Element object to locate
	 * @return the created {@link ILocation} according to the given container and an element to locate if any
	 *         is handled by a {@link IConnectorRegistry#register(IConnector) registered} {@link IConnector},
	 *         <code>null</code> otherwise
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
	 *         handled by a {@link IConnectorRegistry#register(IConnector) registered} {@link IConnector},
	 *         <code>null</code> otherwise
	 */
	ILocation getLocation(ILocationContainer container, Object element);

	/**
	 * Gets or creates an {@link ILocation} according to the given element.
	 * 
	 * @param container
	 *            the {@link ILocationContainer}, it must be contained into an
	 *            {@link org.eclipse.mylyn.docs.intent.mapping.base.IBase IBase}
	 * @param element
	 *            the Element object to locate
	 * @return the {@link ILocation}
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason.
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	ILocation getOrCreateLocation(ILocationContainer container, Object element)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException;

	/**
	 * Gets a human readable name for the given {@link ILocation}.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 * @return a human readable name for the given {@link ILocation} if any is handled by a
	 *         {@link IConnectorRegistry#register(IConnector) registered} {@link IConnector},
	 *         <code>null</code> otherwise
	 */
	String getName(ILocation location);

	/**
	 * Registers the given {@link IConnector}.
	 * 
	 * @param connector
	 *            the {@link IConnector} to register
	 */
	void register(IConnector connector);

	/**
	 * Unregisters the given {@link IConnector}.
	 * 
	 * @param connector
	 *            the {@link IConnector} to register
	 */
	void unregister(IConnector connector);

	/**
	 * Gets the {@link Set} of {@link IConnectorRegistry#register(IConnector) registered} {@link IConnector}.
	 * 
	 * @return the {@link Set} of {@link IConnectorRegistry#register(IConnector) registered}
	 *         {@link IConnector}
	 */
	Set<IConnector> getConnectors();

}
