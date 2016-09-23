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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.text.ITextLocation;
import org.eclipse.mylyn.docs.intent.mapping.text.TextRegion;

/**
 * Marker to {@link ITextLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MarkerToTextLocation implements IMarkerToLocation {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.adapter.IMarkerToLocation#getAdapter(org.eclipse.core.resources.IMarker)
	 */
	public ITextLocation getAdapter(IMarker marker) {
		ITextLocation res = null;

		final IBase currentBase = IdeMappingUtils.getCurentBase();
		if (currentBase != null) {
			try {
				if (marker.isSubtypeOf(IMarker.TEXT)) {
					// TODO we implicitly decide to have a flat structure of location here... we probably
					// don't want to do that
					final ILocation container = MappingUtils.getConnectorRegistry().getOrCreateLocation(
							currentBase, marker.getResource());
					final int start = (Integer)marker.getAttribute(IMarker.CHAR_START);
					final Integer end = (Integer)marker.getAttribute(IMarker.CHAR_END);
					final TextRegion region = new TextRegion(start, end);
					res = (ITextLocation)MappingUtils.getConnectorRegistry().getOrCreateLocation(container,
							region);
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

		return res;
	}

}
