/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.connector;

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;

/**
 * Provides container element for {@link ILocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IContainerProvider {

	/**
	 * Gets the container of the given element.
	 * 
	 * @param element
	 *            the element
	 * @return the container of the given element if any, <code>null</code> otherwise
	 */
	Object getContainer(Object element);

	/**
	 * Gets the {@link ILocation} {@link Class} managed by this provider.
	 * 
	 * @return the {@link ILocation} {@link Class} managed by this provider
	 */
	Class<? extends ILocation> getType();

}
