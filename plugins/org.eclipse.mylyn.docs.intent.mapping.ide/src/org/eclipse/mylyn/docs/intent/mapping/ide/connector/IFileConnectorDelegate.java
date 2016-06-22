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
package org.eclipse.mylyn.docs.intent.mapping.ide.connector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;

/**
 * {@link org.eclipse.core.resources.IFile IFile} connector delegate used by {@link ResourceConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IFileConnectorDelegate {

	/**
	 * Gets the handled {@link IContentType}.
	 * 
	 * @return the handled {@link IContentType}
	 */
	IContentType getContentType();

	/**
	 * Gets the {@link IFileLocation} type.
	 * 
	 * @return the {@link IFileLocation} type
	 */
	Class<? extends IFileLocation> getFileLocationType();

	/**
	 * Initializes the given {@link IFileLocation} with the given {@link IFile}.
	 * 
	 * @param location
	 *            the {@link IFileLocation}
	 * @param element
	 *            the {@link IFile}
	 */
	void initLocation(IFileLocation location, IFile element);

}
