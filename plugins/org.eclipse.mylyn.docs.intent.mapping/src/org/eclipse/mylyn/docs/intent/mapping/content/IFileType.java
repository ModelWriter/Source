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

/**
 * File type.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IFileType {

	/**
	 * Gets the type ID.
	 * 
	 * @return the type ID
	 */
	String getID();

	/**
	 * Tells if this {@link IFileType} is a sub type of the given {@link IFileType#getID() type ID}.
	 * 
	 * @param typeID
	 *            the {@link IFileType#getID() type ID}
	 * @return <code>true</code> if this {@link IFileType} is a sub type of the given {@link IFileType#getID()
	 *         type ID}, <code>false</code> otherwise
	 */
	boolean isKindOf(String typeID);

}
