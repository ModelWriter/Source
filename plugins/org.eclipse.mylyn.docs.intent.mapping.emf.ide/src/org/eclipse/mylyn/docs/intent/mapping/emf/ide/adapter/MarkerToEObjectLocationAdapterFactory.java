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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.IEObjectLocation;
import org.eclipse.mylyn.docs.intent.mapping.emf.ide.marker.IEObjectLocationMaker;
import org.eclipse.mylyn.docs.intent.mapping.ide.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;

/**
 * {@link IEObjectLocation} to marker {@link IAdapterFactory}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MarkerToEObjectLocationAdapterFactory implements IAdapterFactory {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	public IEObjectLocation getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		IEObjectLocation res = null;

		final IBase currentBase = IdeMappingUtils.getCurentBase();
		if (currentBase != null) {
			if (adaptableObject instanceof IMarker) {
				final IMarker marker = (IMarker)adaptableObject;
				try {
					if (marker.isSubtypeOf(IEObjectLocationMaker.EOBJECT_LOCATION_ID)) {
						// TODO we implicitly decide to have a flat structure of location here... we probably
						// don't want to do that
						final ILocation container = MappingUtils.getConnectorRegistry().getOrCreateLocation(
								currentBase, marker.getResource());
						final String uri = (String)marker.getAttribute(IEObjectLocationMaker.URI_ATTRIBUTE);
						// TODO we should change this to use a global ResourceSet...
						final ResourceSet rs = new ResourceSetImpl();
						final EObject eObject = rs.getEObject(URI.createURI(uri), true);
						res = (IEObjectLocation)MappingUtils.getConnectorRegistry().getOrCreateLocation(
								container, eObject);
					}
				} catch (CoreException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				} catch (InstantiationException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				} catch (IllegalAccessException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				} catch (ClassNotFoundException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				}
			}
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class<?>[] getAdapterList() {
		return new Class[] {ILocation.class };
	}

}
