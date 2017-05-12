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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.mylyn.docs.intent.mapping.content.IFileType;
import org.eclipse.mylyn.docs.intent.mapping.content.IFileTypeRegistry;

/**
 * Implementation of {@link IFileTypeRegistry}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class FileTypeRegistry implements IFileTypeRegistry {

	/**
	 * The mapping of {@link IFileType#isKindOf(String) super types}.
	 */
	private final Map<String, Set<String>> superTypes = new HashMap<String, Set<String>>();

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.content.IFileTypeRegistry#registerFileType(java.lang.String,
	 *      java.util.Set)
	 */
	public void registerFileType(String typeID, Set<String> suprTypeIDs) {
		superTypes.put(typeID, suprTypeIDs);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.content.IFileTypeRegistry#unregisterFileType(java.lang.String)
	 */
	public void unregisterFileType(String typeID) {
		superTypes.remove(typeID);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.content.IFileTypeRegistry#getFileType(java.lang.String)
	 */
	public IFileType getFileType(String typeID) {
		final IFileType res;

		if (superTypes.containsKey(typeID)) {
			res = new FileType(typeID);
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.content.IFileTypeRegistry#isKindOf(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean isKindOf(String typeID, String superTypeID) {
		final Set<String> superTypeIDs = superTypes.get(typeID);
		return isKindOf(typeID, superTypeID, superTypeIDs);
	}

	/**
	 * Tells if the given {@link IFileType#getID() type ID} is a sub type of the given
	 * {@link IFileType#getID() super type ID}.
	 * 
	 * @param typeID
	 *            the {@link IFileType#getID() type ID}
	 * @param superTypeID
	 *            the {@link IFileType#getID() super type ID}
	 * @param superTypeIDs
	 *            the {@link Set} of {@link IFileType#getID() super type IDs} for the given
	 *            {@link IFileType#getID() type ID}
	 * @return <code>true</code> if the given {@link IFileType#getID() type ID} is a sub type of the given
	 *         {@link IFileType#getID() super type ID}, <code>false</code> otherwise
	 */
	protected boolean isKindOf(String typeID, String superTypeID, Set<String> superTypeIDs) {
		final boolean res;

		if (superTypeIDs == null || superTypeIDs.isEmpty()) {
			res = false;
		} else if (superTypeIDs.contains(superTypeID)) {
			res = true;
		} else {
			boolean isIndirectSuperType = false;
			for (String superType : superTypeIDs) {
				isIndirectSuperType = isKindOf(superType, superTypeID, superTypes.get(superType));
				if (isIndirectSuperType) {
					break;
				}
			}
			res = isIndirectSuperType;
		}

		return res;
	}

}
