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
package org.eclipse.intent.mapping.ide.resource;

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;

/**
 * An {@link org.eclipse.core.resources.IResource IResource} {@link ILocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IResourceLocation extends ILocation {

	/**
	 * Get the {@link org.eclipse.core.resources.IResource#getFullPath() full path}
	 * {@link org.eclipse.core.runtime.IPath#toPortableString() portable string} of the located
	 * {@link org.eclipse.core.resources.IResource IResource}.
	 * 
	 * @return the {@link org.eclipse.core.resources.IResource#getFullPath() full path}
	 *         {@link org.eclipse.core.runtime.IPath#toPortableString() portable string} of the located
	 *         {@link org.eclipse.core.resources.IResource IResource}
	 */
	String getFullPath();

	/**
	 * Set the {@link org.eclipse.core.resources.IResource#getFullPath() full path}
	 * {@link org.eclipse.core.runtime.IPath#toPortableString() portable string} of the located
	 * {@link org.eclipse.core.resources.IResource IResource}.
	 * 
	 * @param path
	 *            the {@link org.eclipse.core.resources.IResource#getFullPath() full path}
	 *            {@link org.eclipse.core.runtime.IPath#toPortableString() portable string} of the
	 *            {@link org.eclipse.core.resources.IResource IResource} to locate
	 */
	void setFullPath(String path);

}
