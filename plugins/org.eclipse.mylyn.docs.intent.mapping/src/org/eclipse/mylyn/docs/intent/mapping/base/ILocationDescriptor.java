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

/**
 * Describe an {@link ILocation} in order to access it or create it.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ILocationDescriptor {

	/**
	 * Gets the {@link ILocation#getContainer() container} {@link ILocationDescriptor}.
	 * 
	 * @return the {@link ILocation#getContainer() container} {@link ILocationDescriptor} if any,
	 *         <code>null</code> otherwise
	 */
	ILocationDescriptor getContainerDescriptor();

	/**
	 * Tells if the described {@link ILocation} exists.
	 * 
	 * @return <code>true</code> if the described {@link ILocation} exists, <code>false</code> otherwise
	 */
	boolean exists();

	/**
	 * Gets the {@link ILocation} if it {@link ILocationDescriptor#exists() exists}.
	 * 
	 * @return the {@link ILocation} if it {@link ILocationDescriptor#exists() exists}, <code>null</code>
	 *         otherwise
	 */
	ILocation getLocation();

	/**
	 * Gets or creates the described {@link ILocation}. It will {@link ILocationDescriptor#getOrCreate()
	 * create} {@link ILocationDescriptor#getContainerDescriptor() described containers}.
	 * 
	 * @return the described {@link ILocation}
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason.
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	ILocation getOrCreate() throws InstantiationException, IllegalAccessException, ClassNotFoundException;

	/**
	 * Updates with the given Element.
	 * 
	 * @param element
	 *            the element
	 * @return <code>true</code> if updated, <code>false</code> otherwise
	 */
	boolean update(Object element);

	/**
	 * Gets the located element.
	 * 
	 * @return the located element
	 */
	Object getElement();

	/**
	 * Gets the human readable name.
	 * 
	 * @return the human readable name
	 */
	String getName();

	/**
	 * Disposes the descriptor when not needed anymore.
	 */
	void dispose();

}
