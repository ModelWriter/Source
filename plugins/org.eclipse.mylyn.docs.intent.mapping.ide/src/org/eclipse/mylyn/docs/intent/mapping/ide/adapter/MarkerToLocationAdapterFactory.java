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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.internal.resources.MarkerManager;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.ide.Activator;

/**
 * Adapts {@link IMarker} to {@link ILocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MarkerToLocationAdapterFactory implements IAdapterFactory {

	/**
	 * Mapping from {@link IMarkerToLocation} to marker type.
	 */
	private static final Map<IMarkerToLocation, String> ADAPTER_TYPE = new HashMap<IMarkerToLocation, String>();

	/**
	 * The {@link List} of {@link IMarkerToLocation} from the more specific marker type to the less specific.
	 */
	private static final List<IMarkerToLocation> ADAPTERS = new ArrayList<IMarkerToLocation>();

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("restriction")
	public ILocation getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		ILocation res = null;

		if (adaptableObject instanceof IMarker) {
			final IMarker marker = (IMarker)adaptableObject;
			final MarkerManager manager = ((Workspace)ResourcesPlugin.getWorkspace()).getMarkerManager();
			try {
				final String markerType = marker.getType();
				for (IMarkerToLocation adapter : ADAPTERS) {
					if (manager.isSubtype(markerType, ADAPTER_TYPE.get(adapter))) {
						res = adapter.getAdapter(marker);
						if (res != null) {
							break;
						}
					}
				}
			} catch (CoreException e) {
				Activator.getDefault().getLog().log(
						new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
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
		return new Class<?>[] {ILocation.class };
	}

	/**
	 * registers the given {@link IMarkerToLocation} for the given marker type.
	 * 
	 * @param adapter
	 *            the {@link IMarkerToLocation}
	 * @param markerType
	 *            the marker type
	 */
	@SuppressWarnings("restriction")
	public static void register(IMarkerToLocation adapter, String markerType) {
		ADAPTER_TYPE.put(adapter, markerType);

		final MarkerManager manager = ((Workspace)ResourcesPlugin.getWorkspace()).getMarkerManager();
		int i = 0;
		for (IMarkerToLocation registeredAdapter : ADAPTERS) {
			if (manager.isSubtype(markerType, ADAPTER_TYPE.get(registeredAdapter))) {
				break;
			}
			i++;
		}

		ADAPTERS.add(i, adapter);
	}
}
