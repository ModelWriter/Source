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
package org.eclipse.mylyn.docs.intent.mapping.ide.adapter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.docs.intent.mapping.ide.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.ILocationMarker;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;

/**
 * {@link ITextLocation} to Marker {@link IAdapterFactory}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextLocationToMarkerAdapterFactory implements IAdapterFactory {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		IMarker res;

		if (adaptableObject instanceof ITextLocation) {
			final ITextLocation textLocation = (ITextLocation)adaptableObject;

			final IFileLocation fileLocation = IdeMappingUtils.getContainingFileLocation(textLocation);
			final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
					new Path(fileLocation.getFullPath()));
			try {
				// TODO move the mapping Location -> marker from MapperView to utility class and use it as
				// cache here
				res = file.createMarker(ILocationMarker.TEXT_LOCATION_ID);
				res.setAttribute(IMarker.CHAR_START, textLocation.getStartOffset());
				res.setAttribute(IMarker.CHAR_END, textLocation.getEndOffset());
				res.setAttribute(ILocationMarker.LOCATION_ATTRIBUTE, textLocation);
			} catch (CoreException e) {
				res = null;
				Activator.getDefault().getLog().log(
						new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
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
