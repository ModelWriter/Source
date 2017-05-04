/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.adapter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.marker.IEObjectLocationMaker;
import org.eclipse.mylyn.docs.intent.mapping.ide.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;

/**
 * {@link IEObjectLocation} to Marker {@link IAdapterFactory}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectLocationToMarkerAdapterFactory implements IAdapterFactory {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public IMarker getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		IMarker res;

		if (adaptableObject instanceof IEObjectLocation) {
			final IEObjectLocation eObjLocation = (IEObjectLocation)adaptableObject;
			final IMarker existingMarker = IdeMappingUtils.getMarker(eObjLocation);
			if (existingMarker != null) {
				res = existingMarker;
			} else {
				final IFileLocation fileLocation = IdeMappingUtils.getContainingFileLocation(eObjLocation);
				final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(Path.fromPortableString(
						fileLocation.getFullPath()));
				try {
					res = file.createMarker(IEObjectLocationMaker.EOBJECT_LOCATION_ID);
					final EObject eObject = (EObject)MappingUtils.getConnectorRegistry().getElement(
							eObjLocation);
					res.setAttribute(IEObjectLocationMaker.URI_ATTRIBUTE, EcoreUtil.getURI(eObject)
							.toString());
				} catch (CoreException e) {
					res = null;
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e
							.getMessage(), e));
				}
			}
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class<?>[] getAdapterList() {
		return new Class[] {IMarker.class };
	}

}
