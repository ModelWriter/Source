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
package org.eclipse.mylyn.docs.intent.mapping.ide.connector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;

/**
 * Abstract {@link IFileConnectorDelegate}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractFileConnectorDelegate implements IFileConnectorDelegate {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#update(org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation,
	 *      org.eclipse.core.resources.IFile)
	 */
	public void update(IFileLocation location, IFile element) {
		initLocation(location, element);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getElement(org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation)
	 */
	public Object getElement(IFileLocation location) {
		final IFile res = ResourcesPlugin.getWorkspace().getRoot().getFile(
				Path.fromPortableString(location.getFullPath()));
		return res;
	}

}
