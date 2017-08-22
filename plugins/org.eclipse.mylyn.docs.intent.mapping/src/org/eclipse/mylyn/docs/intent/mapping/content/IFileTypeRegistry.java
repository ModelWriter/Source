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
import java.util.List;
import java.util.Set;

/**
 * The {@link IFileType} registry.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IFileTypeRegistry {

	/**
	 * Gets the {@link List} of {@link IFileTypeProvider}.
	 * 
	 * @return the {@link List} of {@link IFileTypeProvider}
	 */
	List<IFileTypeProvider> getFileTypeProviders();

	/**
	 * Registers the given {@link IFileType#getID() type ID} with the given {@link Set} of its super
	 * {@link IFileType#getID() type IDs}.
	 * 
	 * @param typeID
	 *            the {@link IFileType#getID() type ID}
	 * @param suprTypeIDs
	 *            the {@link Set} of its super {@link IFileType#getID() type IDs}
	 */
	void registerFileType(String typeID, Set<String> suprTypeIDs);

	/**
	 * Unresiters the given {@link IFileType#getID() type ID}.
	 * 
	 * @param typeID
	 *            the {@link IFileType#getID() type ID}
	 */
	void unregisterFileType(String typeID);

	/**
	 * Gets the {@link IFileType} for the given {@link IFileType#getID() type ID}.
	 * 
	 * @param typeID
	 *            the {@link IFileType#getID() type ID}
	 * @return the {@link IFileType} for the given {@link IFileType#getID() type ID} if any, <code>null</code>
	 *         otherwise
	 */
	IFileType getFileType(String typeID);

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

	/**
	 * Tells if the given {@link IFileType#getID() type ID} is a sub type of the given
	 * {@link IFileType#getID() super type ID}.
	 * 
	 * @param typeID
	 *            the {@link IFileType#getID() type ID}
	 * @param superTypeID
	 *            the {@link IFileType#getID() super type ID}
	 * @return <code>true</code> if the given {@link IFileType#getID() type ID} is a sub type of the given
	 *         {@link IFileType#getID() super type ID}, <code>false</code> otherwise
	 */
	boolean isKindOf(String typeID, String superTypeID);

}
