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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.content.IFileType;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.EObjectConnector.EObjectContainerHelper;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.resource.IEObjectFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.AbstractFileConnectorDelegate;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;

/**
 * {@link IEObjectFileLocation} delegate.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectFileConnectorDelegate extends AbstractFileConnectorDelegate {

	/**
	 * The {@link IFileType}.
	 */
	private static final IFileType FILE_TYPE = MappingUtils.getFileTypeRegistry().getFileType(
			"org.eclipse.emf.ecore.xmi");

	/**
	 * The {@link EObjectContainerHelper}.
	 */
	private final EObjectContainerHelper eObjectContainerHelper = new EObjectContainerHelper();

	/**
	 * Mapping of {@link IEObjectFileLocation#getFullPath() full path} to {@link Resource}.
	 */
	private final Map<String, Resource> knownResources = new HashMap<String, Resource>();

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getFileType()
	 */
	public IFileType getFileType() {
		return FILE_TYPE;
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
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#initLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer,
	 *      org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation,
	 *      org.eclipse.core.resources.IFile)
	 */
	public void initLocation(ILocationContainer container, IFileLocation location, IFile element) {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource resource = rs.getResource(URI.createPlatformResourceURI(element.getFullPath()
				.toPortableString(), true), true);
		knownResources.put(location.getFullPath(), resource);
		try {
			eObjectContainerHelper.updateEObjectContainer(container, (IEObjectContainer)location, resource);
			// CHECKSTYLE:OFF
		} catch (Exception e) {
			// CHECKSTYLE:ON
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(),
					e));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate#getElement(org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation)
	 */
	public Object getElement(IFileLocation location) {
		if (!knownResources.containsKey(location.getFullPath())) {
			initLocation(location.getContainer(), location, (IFile)super.getElement(location));
		}

		return knownResources.get(location.getFullPath());
	}

}
