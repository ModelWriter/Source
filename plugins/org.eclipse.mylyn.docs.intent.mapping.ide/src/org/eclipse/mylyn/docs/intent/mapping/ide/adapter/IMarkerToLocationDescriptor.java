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
package org.eclipse.mylyn.docs.intent.mapping.ide.adapter;

import org.eclipse.core.resources.IMarker;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;

/**
 * Adapts an {@link IMarker} to an {@link ILocationDescriptor}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IMarkerToLocationDescriptor {

	/**
	 * Gets the {@link ILocationDescriptor} for the given {@link IMarker}.
	 * 
	 * @param marker
	 *            the {@link IMarker} to adapt
	 * @return the {@link ILocationDescriptor} for the given {@link IMarker} if any, <code>null</code>
	 *         otherwise
	 */
	ILocationDescriptor getAdapter(IMarker marker);

}
