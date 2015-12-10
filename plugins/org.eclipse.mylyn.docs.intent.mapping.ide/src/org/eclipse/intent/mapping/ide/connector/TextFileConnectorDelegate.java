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
package org.eclipse.intent.mapping.ide.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.intent.mapping.ide.Activator;
import org.eclipse.intent.mapping.ide.resource.IFileLocation;
import org.eclipse.intent.mapping.ide.resource.ITextFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.TextConnector;

/**
 * {@link ITextFileLocation} delegate.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextFileConnectorDelegate implements IFileConnectorDelegate {

	/**
	 * The default buffer size.
	 */
	private static final int BUFFER_SIZE = 4048;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.intent.mapping.ide.connector.IFileConnectorDelegate#getContentType()
	 */
	public IContentType getContentType() {
		final IContentTypeManager contentTypeManager = Platform.getContentTypeManager();

		return contentTypeManager.getContentType(IContentTypeManager.CT_TEXT);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.intent.mapping.ide.connector.IFileConnectorDelegate#getFileLocationType()
	 */
	public Class<? extends IFileLocation> getFileLocationType() {
		return ITextFileLocation.class;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.intent.mapping.ide.connector.IFileConnectorDelegate#initLocation(org.eclipse.intent.mapping.ide.resource.IFileLocation,
	 *      org.eclipse.core.resources.IFile)
	 */
	public void initLocation(IFileLocation location, IFile element) {
		try {
			final StringBuilder text = new StringBuilder(BUFFER_SIZE);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(element.getContents(),
					element.getCharset()));
			String line = reader.readLine();
			while (line != null) {
				text.append(line);
				line = reader.readLine();
			}
			final TextConnector connector = new TextConnector();
			connector.update((ITextFileLocation)location, text.toString());
		} catch (UnsupportedEncodingException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		} catch (IOException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}
}
