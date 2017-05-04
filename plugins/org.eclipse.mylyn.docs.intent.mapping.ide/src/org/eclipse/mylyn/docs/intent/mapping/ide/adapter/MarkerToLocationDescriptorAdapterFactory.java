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
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.Activator;

/**
 * Adapts {@link IMarker} to {@link ILocationDescriptor}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
@SuppressWarnings("restriction")
public class MarkerToLocationDescriptorAdapterFactory implements IAdapterFactory {

	/**
	 * Mapping from {@link IMarkerToLocationDescriptor} to marker type.
	 */
	private static final Map<IMarkerToLocationDescriptor, String> ADAPTER_TYPE = new HashMap<IMarkerToLocationDescriptor, String>();

	/**
	 * The {@link List} of {@link IMarkerToLocationDescriptor} from the more specific marker type to the less
	 * specific.
	 */
	private static final List<IMarkerToLocationDescriptor> ADAPTERS = new ArrayList<IMarkerToLocationDescriptor>();

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		ILocationDescriptor res = null;

		if (adaptableObject instanceof IMarker) {
			final IMarker marker = (IMarker)adaptableObject;
			final MarkerManager manager = ((Workspace)ResourcesPlugin.getWorkspace()).getMarkerManager();
			try {
				final String markerType = marker.getType();
				for (IMarkerToLocationDescriptor adapter : ADAPTERS) {
					if (manager.isSubtype(markerType, ADAPTER_TYPE.get(adapter))) {
						res = adapter.getAdapter(marker);
						if (res != null) {
							break;
						}
					}
				}
			} catch (CoreException e) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e
						.getMessage(), e));
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
		return new Class<?>[] {ILocationDescriptor.class };
	}

	/**
	 * registers the given {@link IMarkerToLocationDescriptor} for the given marker type.
	 * 
	 * @param adapter
	 *            the {@link IMarkerToLocationDescriptor}
	 * @param markerType
	 *            the marker type
	 */
	public static void register(IMarkerToLocationDescriptor adapter, String markerType) {
		ADAPTER_TYPE.put(adapter, markerType);

		final MarkerManager manager = ((Workspace)ResourcesPlugin.getWorkspace()).getMarkerManager();
		int i = 0;
		for (IMarkerToLocationDescriptor registeredAdapter : ADAPTERS) {
			if (manager.isSubtype(markerType, ADAPTER_TYPE.get(registeredAdapter))) {
				break;
			}
			i++;
		}

		ADAPTERS.add(i, adapter);
	}
}
