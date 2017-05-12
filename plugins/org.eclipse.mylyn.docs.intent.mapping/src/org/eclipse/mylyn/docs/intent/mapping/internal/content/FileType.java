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
package org.eclipse.mylyn.docs.intent.mapping.internal.content;

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.content.IFileType;

/**
 * Implementation of IFileType.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class FileType implements IFileType {

	/**
	 * The {@link IFileType#getID() type ID}.
	 */
	private final String id;

	/**
	 * Constructor.
	 * 
	 * @param typeID
	 *            the {@link IFileType#getID() type ID}
	 */
	public FileType(String typeID) {
		this.id = typeID;
	}

	public String getID() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.content.IFileType#isKindOf(java.lang.String)
	 */
	public boolean isKindOf(String typeID) {
		return MappingUtils.getFileTypeRegistry().isKindOf(id, typeID);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof IFileType && getID().equals(((IFileType)obj).getID());
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
