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
package org.eclipse.mylyn.docs.intent.mapping.content;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provide {@link IFileTypeProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IFileTypeProvider {

	/**
	 * Gets the {@link IFileType} for the given {@link InputStream} and resource name.
	 * 
	 * @param contents
	 *            the {@link InputStream} to the content
	 * @param name
	 *            the resource name
	 * @return the {@link IFileType} for the given {@link InputStream} and resource name if nay,
	 *         <code>null</code> otherwise
	 * @throws IOException
	 *             if the {@link InputStream} can't be read
	 */
	IFileType getFileTypeFor(InputStream contents, String name) throws IOException;

}
