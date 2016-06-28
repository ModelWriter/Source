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
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.conector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.ide.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IResourceLocation;

/**
 * An {@link IResource} {@link org.eclipse.mylyn.docs.intent.mapping.conector.IConnector IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ResourceConnector extends AbstractConnector {

	@Override
	protected Class<? extends ILocation> getLocationType(Class<? extends ILocation> containerType,
			Object element) {
		final Class<? extends ILocation> res;

		if (element instanceof IFile) {
			res = getIFileLocationType((IFile)element);
		} else if (element instanceof IResource) {
			res = IResourceLocation.class;
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * Gets the {@link IFileLocation} for the given {@link IFile}.
	 * 
	 * @param file
	 *            the {@link IFile}
	 * @return the {@link IFileLocation} for the given {@link IFile} if any, <code>null</code> otherwise
	 */
	private Class<? extends IFileLocation> getIFileLocationType(IFile file) {
		Class<? extends IFileLocation> res = null;

		final IFileConnectorDelegate delegate = getDelegate(file);
		if (delegate != null) {
			res = delegate.getFileLocationType();
		}

		return res;
	}

	/**
	 * Gets the {@link IFileConnectorDelegate} for the given {@link IFile}.
	 * 
	 * @param file
	 *            the {@link IFile}
	 * @return the {@link IFileConnectorDelegate} for the given {@link IFile} if any, <code>null</code>
	 *         otherwise
	 */
	private IFileConnectorDelegate getDelegate(IFile file) {
		IFileConnectorDelegate res = null;

		try {
			InputStream contents = file.getContents();
			final IContentType contentType = Platform.getContentTypeManager().findContentTypeFor(contents,
					file.getName());
			contents.close();

			if (contentType != null) {
				for (IFileConnectorDelegate delegate : IdeMappingUtils.getFileConectorDelegateRegistry()
						.getConnectorDelegates()) {
					if (contentType.isKindOf(delegate.getContentType())) {
						res = delegate;
						break;
					}
				}
			}
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		} catch (IOException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}

		return res;
	}

	@Override
	protected void initLocation(ILocation location, Object element) {
		final IResourceLocation toInit = (IResourceLocation)location;

		toInit.setFullPath(((IResource)element).getFullPath().toPortableString());
		if (element instanceof IFile) {
			final IFileConnectorDelegate delegate = getDelegate((IFile)element);
			if (delegate != null) {
				delegate.initLocation((IFileLocation)location, (IFile)element);
			} else {
				throw new IllegalArgumentException(
						"A delegate created a IFileLocation but we can't find it anymore...");
			}
		}
	}

	@Override
	protected boolean match(ILocation location, Object element) {
		final IResourceLocation resourceLocation = (IResourceLocation)location;
		final IResource resource = (IResource)element;

		return resourceLocation.getFullPath().equals(resource.getFullPath().toString());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnector#getName(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public String getName(ILocation location) {
		final String res;

		if (location instanceof IResourceLocation) {
			res = ((IResourceLocation)location).getFullPath();
		} else {
			res = null;
		}

		return res;
	}

}
