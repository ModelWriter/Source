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

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.ITextFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.TextConnector;

/**
 * {@link ITextFileLocation} delegate.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextFileConnectorDelegate extends AbstractFileConnectorDelegate {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getContentType()
	 */
	public IContentType getContentType() {
		final IContentTypeManager contentTypeManager = Platform.getContentTypeManager();

		return contentTypeManager.getContentType(IContentTypeManager.CT_TEXT);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getFileLocationType()
	 */
	public Class<? extends IFileLocation> getFileLocationType() {
		return ITextFileLocation.class;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#initLocation(org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation,
	 *      org.eclipse.core.resources.IFile)
	 */
	public void initLocation(IFileLocation location, IFile element) {
		try {
			final String text = MappingUtils.getContent(element.getLocation().toFile(), element.getCharset());
			TextConnector.updateTextContainer((ITextFileLocation)location, text);
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		} catch (IOException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}

}
