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
package org.eclipse.mylyn.docs.intent.mapping.ide.internal.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.mylyn.docs.intent.mapping.content.IFileType;
import org.eclipse.mylyn.docs.intent.mapping.content.IFileTypeRegistry;
import org.eclipse.mylyn.docs.intent.mapping.internal.content.FileType;
import org.eclipse.mylyn.docs.intent.mapping.internal.content.FileTypeRegistry;

/**
 * Ide implementation of {@link FileTypeRegistry}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeFileTypeRegistry implements IFileTypeRegistry {

	/**
	 * Delegating {@link IFileTypeRegistry}.
	 */
	private final IFileTypeRegistry registry;

	/**
	 * Constructor.
	 * 
	 * @param registry
	 *            the delegating {@link IFileTypeRegistry}
	 */
	public IdeFileTypeRegistry(IFileTypeRegistry registry) {
		this.registry = registry;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.content.IFileTypeRegistry#registerFileType(java.lang.String,
	 *      java.util.Set)
	 */
	public void registerFileType(String typeID, Set<String> suprTypeIDs) {
		registry.registerFileType(typeID, suprTypeIDs);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.content.IFileTypeRegistry#unregisterFileType(java.lang.String)
	 */
	public void unregisterFileType(String typeID) {
		registry.unregisterFileType(typeID);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.content.IFileTypeRegistry#getFileType(java.lang.String)
	 */
	public IFileType getFileType(String typeID) {
		final IFileType res;

		final IFileType delegateFileType = registry.getFileType(typeID);
		if (delegateFileType == null) {
			final IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
			final IContentType contentType = contentTypeManager.getContentType(typeID);
			if (contentType != null) {
				res = new FileType(contentType.getId());
			} else {
				res = null;
			}
		} else {
			res = delegateFileType;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IOException
	 * @see org.eclipse.mylyn.docs.intent.mapping.content.IFileTypeRegistry#getFileTypeFor(java.io.InputStream,
	 *      java.lang.String)
	 */
	public IFileType getFileTypeFor(InputStream contents, String name) throws IOException {
		final IFileType res;

		final IFileType delegateFileType = registry.getFileTypeFor(contents, name);
		if (delegateFileType == null) {
			final IContentType contentType = Platform.getContentTypeManager().findContentTypeFor(contents,
					name);
			if (contentType != null) {
				res = new FileType(contentType.getId());
			} else {
				res = null;
			}
		} else {
			res = delegateFileType;
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
		final boolean res;

		if (registry.isKindOf(typeID, superTypeID)) {
			res = true;
		} else {
			final IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
			final IContentType contentType = contentTypeManager.getContentType(typeID);
			final IContentType superContentType = contentTypeManager.getContentType(superTypeID);
			res = contentType.isKindOf(superContentType);
		}

		return res;
	}

	/**
	 * Gets the delegating {@link IFileTypeRegistry}.
	 * 
	 * @return the delegating {@link IFileTypeRegistry}
	 */
	public IFileTypeRegistry getDelegate() {
		return registry;
	}

}
