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
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.connector;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.EObjectConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.resource.IEObjectFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.AbstractFileConnectorDelegate;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;

/**
 * {@link IEObjectFileLocation} delegate.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectFileConnectorDelegate extends AbstractFileConnectorDelegate {

	/**
	 * Mapping of {@link IEObjectFileLocation#getFullPath() full path} to {@link Resource}.
	 */
	private final Map<String, Resource> knownResrouces = new HashMap<String, Resource>();

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getContentType()
	 */
	public IContentType getContentType() {
		final IContentTypeManager contentTypeManager = Platform.getContentTypeManager();

		return contentTypeManager.getContentType("org.eclipse.emf.ecore.xmi");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getFileLocationType()
	 */
	public Class<? extends IFileLocation> getFileLocationType() {
		return IEObjectFileLocation.class;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#initLocation(org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation,
	 *      org.eclipse.core.resources.IFile)
	 */
	public void initLocation(IFileLocation location, IFile element) {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource resource = rs.getResource(URI.createPlatformResourceURI(element.getFullPath()
				.toPortableString(), true), true);
		knownResrouces.put(location.getFullPath(), resource);
		EObjectConnector.updateEObjectContainer((IEObjectContainer)location, resource);

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getElement(org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation)
	 */
	public Object getElement(IFileLocation location) {
		if (!knownResrouces.containsKey(location.getFullPath())) {
			initLocation(location, (IFile)super.getElement(location));
		}

		return knownResrouces.get(location.getFullPath());
	}

}
