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
package org.eclipse.mylyn.docs.intent.mapping.ide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileDelegateRegistry;
import org.eclipse.mylyn.docs.intent.mapping.ide.internal.connector.FileDelegateRegistry;
import org.eclipse.mylyn.docs.intent.mapping.ide.resource.IFileLocation;

/**
 * Ide mapping utilities.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public final class IdeMappingUtils {

	/**
	 * Listener for the {@link IdeMappingUtils#getLocationsPool() locations pool}.
	 * 
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public interface ILocationsPoolListener {

		/**
		 * Stub implementation.
		 *
		 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
		 */
		public static class Stub implements ILocationsPoolListener {

			/**
			 * {@inheritDoc}
			 *
			 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils.ILocationsPoolListener#locationActivated(org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor)
			 */
			public void locationActivated(ILocationDescriptor locationDescriptor) {
				// nothing to do here
			}

			/**
			 * {@inheritDoc}
			 *
			 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils.ILocationsPoolListener#locationDeactivated(org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor)
			 */
			public void locationDeactivated(ILocationDescriptor locationDescriptor) {
				// nothing to do here
			}

			/**
			 * {@inheritDoc}
			 *
			 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils.ILocationsPoolListener#contentsAdded(org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor)
			 */
			public void contentsAdded(ILocationDescriptor locationDescriptor) {
				// nothing to do here
			}

			/**
			 * {@inheritDoc}
			 *
			 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils.ILocationsPoolListener#contentsRemoved(org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor)
			 */
			public void contentsRemoved(ILocationDescriptor locationDescriptor) {
				// nothing to do here
			}

		}

		/**
		 * Notifies that the given {@link ILocationDescriptor} has been activated.
		 * 
		 * @param locationDescriptor
		 *            the activated {@link ILocationDescriptor}
		 */
		void locationActivated(ILocationDescriptor locationDescriptor);

		/**
		 * Notifies that the given {@link ILocationDescriptor} has been deactivated.
		 * 
		 * @param locationDescriptor
		 *            the deactivated {@link ILocationDescriptor}
		 */
		void locationDeactivated(ILocationDescriptor locationDescriptor);

		/**
		 * Notifies that a {@link ILocationDescriptor} has been added.
		 * 
		 * @param locationDescriptor
		 *            the added {@link ILocationDescriptor}
		 */
		void contentsAdded(ILocationDescriptor locationDescriptor);

		/**
		 * Notifies that a {@link ILocationDescriptor} has been removed.
		 * 
		 * @param locationDescriptor
		 *            the removed {@link ILocationDescriptor}
		 */
		void contentsRemoved(ILocationDescriptor locationDescriptor);

	}

	/**
	 * The {@link IFileDelegateRegistry} instance.
	 */
	private static final IFileDelegateRegistry REGISTRY = new FileDelegateRegistry();

	/**
	 * Mapping from an {@link ILocation} and it's {@link IMarker}.
	 */
	private static final Map<ILocation, IMarker> LOCATION_TO_MARKER = new HashMap<ILocation, IMarker>();

	/**
	 * The pool of {@link ILocationDescriptor} to link with.
	 */
	private static final Map<ILocationDescriptor, Boolean> LOCATIONS_POOL = new LinkedHashMap<ILocationDescriptor, Boolean>();

	/**
	 * The {@link List} of {@link ILocationsPoolListener}.
	 */
	private static final List<ILocationsPoolListener> LOCATIONS_POOL_LISTENERS = new ArrayList<ILocationsPoolListener>();

	/**
	 * The current {@link IBase}.
	 */
	private static IBase currentBase;

	/**
	 * Constructor.
	 */
	private IdeMappingUtils() {
		// nothing to do here
	}

	/**
	 * Gets the {@link IFileDelegateRegistry}.
	 * 
	 * @return the {@link IFileDelegateRegistry}
	 */
	public static IFileDelegateRegistry getFileConectorDelegateRegistry() {
		return REGISTRY;
	}

	/**
	 * Adapts the given {@link Object element} into an {@link Object} of the given {@link Class}.
	 * 
	 * @param element
	 *            the {@link Object element}
	 * @param cls
	 *            the {@link Class}
	 * @return the {@link Object} of the given {@link Class} if any, <code>null</code> otherwise
	 * @param <T>
	 *            the kind of result
	 */
	@SuppressWarnings("unchecked")
	public static <T> T adapt(Object element, Class<T> cls) {
		final T res;

		if (cls.isInstance(element)) {
			res = (T)element;
		} else if (element instanceof IAdaptable) {
			final T adaptedElement = (T)((IAdaptable)element).getAdapter(cls);
			if (adaptedElement != null) {
				res = adaptedElement;
			} else {
				res = (T)Platform.getAdapterManager().getAdapter(element, cls);
			}
		} else {
			res = (T)Platform.getAdapterManager().getAdapter(element, cls);
		}

		return res;
	}

	/**
	 * Sets the current {@link IBase} to the given {@link IBase}.
	 * 
	 * @param base
	 *            the {@link IBase}
	 */
	public static void setCurrentBase(IBase base) {
		currentBase = base;
	}

	/**
	 * Gets the current {@link IBase}.
	 * 
	 * @return the current {@link IBase}
	 */
	public static IBase getCurentBase() {
		return currentBase;
	}

	/**
	 * Gets the {@link ILocation#getContainer() containing} {@link IFileLocation} of the given
	 * {@link ILocation} or itself if it's a {@link IFileLocation}.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 * @return the {@link ILocation#getContainer() containing} {@link IFileLocation} of the given
	 *         {@link ILocation} or itself if it's a {@link IFileLocation}, <code>null</code> if none is found
	 */
	public static IFileLocation getContainingFileLocation(ILocation location) {
		final IFileLocation res;

		if (location instanceof IFileLocation) {
			res = (IFileLocation)location;
		} else if (location.getContainer() instanceof ILocation) {
			res = getContainingFileLocation((ILocation)location.getContainer());
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * Gets or creates the {@link IMarker} for the given {@link ILocation}.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 * @return the {@link IMarker} for the given {@link ILocation} if any exists or can be created,
	 *         <code>null</code> otherwise
	 */
	public static IMarker getOrCreateMarker(ILocation location) {
		final IMarker res;

		final IMarker existingMarker = getMarker(location);
		if (existingMarker == null) {
			res = IdeMappingUtils.adapt(location, IMarker.class);
			if (res != null) {
				LOCATION_TO_MARKER.put(location, res);
			}
		} else {
			res = existingMarker;
		}

		return res;
	}

	/**
	 * Gets the cached marker for the given {@link ILocation}.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 * @return the cached marker for the given {@link ILocation} if any, <code>null</code> otherwise
	 */
	public static IMarker getMarker(ILocation location) {
		return LOCATION_TO_MARKER.get(location);
	}

	/**
	 * {@link IMarker#delete() Deletes} the {@link IMarker} for the given {@link ILocation}.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 */
	public static void deleteMarker(ILocation location) {
		final IMarker marker = LOCATION_TO_MARKER.remove(location);
		if (marker != null) {
			try {
				marker.delete();
			} catch (CoreException e) {
				Activator.getDefault().getLog().log(
						new Status(IStatus.ERROR, Activator.PLUGIN_ID,
								"unable to delete a location markers for "
										+ marker.getResource().getFullPath().toString(), e));
			}
		}
	}

	/**
	 * Removes the {@link IMarker} for the given {@link ILocation}.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 * @see #deleteMarker(ILocation) for deletion
	 */
	public static void removeMarker(ILocation location) {
		LOCATION_TO_MARKER.remove(location);
	}

	/**
	 * Gets the pool of {@link ILocationDescriptor} to link with.
	 * 
	 * @return the pool of {@link ILocationDescriptor} to link with
	 */
	public static Set<ILocationDescriptor> getLocationsPool() {
		return Collections.unmodifiableSet(LOCATIONS_POOL.keySet());
	}

	/**
	 * Gets the {@link List} of {@link ILocationsPoolListener} in a thread safe way.
	 * 
	 * @return the {@link List} of {@link ILocationsPoolListener} in a thread safe way
	 */
	private static List<ILocationsPoolListener> getLocationPoolListeners() {
		synchronized(LOCATIONS_POOL_LISTENERS) {
			return new ArrayList<ILocationsPoolListener>(LOCATIONS_POOL_LISTENERS);
		}
	}

	/**
	 * Adds the given {@link ILocationDescriptor} to the {@link #getLocationsPool() pool of location
	 * descriptor}.
	 * 
	 * @param locationDesciptor
	 *            the {@link ILocationDescriptor} to add
	 */
	public static void addLocationToPool(ILocationDescriptor locationDesciptor) {
		final Boolean added;
		synchronized(LOCATIONS_POOL) {
			added = LOCATIONS_POOL.put(locationDesciptor, false);
		}
		if (added == null || added) {
			for (ILocationsPoolListener listener : getLocationPoolListeners()) {
				listener.contentsAdded(locationDesciptor);
			}
		}
	}

	/**
	 * Removes the given {@link ILocationDescriptor} from the {@link #getLocationsPool() pool of location
	 * decriptor}.
	 * 
	 * @param locationDescriptor
	 *            the {@link ILocation} to remove
	 */
	public static void removeLocationFromPool(ILocationDescriptor locationDescriptor) {
		final Boolean removed;
		synchronized(LOCATIONS_POOL) {
			removed = LOCATIONS_POOL.remove(locationDescriptor);
		}
		if (removed != null) {
			for (ILocationsPoolListener listener : getLocationPoolListeners()) {
				listener.contentsRemoved(locationDescriptor);
			}
		}
	}

	/**
	 * Adds a {@link ILocationsPoolListener} to the {@link #getLocationsPool() pool of location}.
	 * 
	 * @param listener
	 *            the {@link ILocationsPoolListener} to add
	 */
	public static void addLocationToPoolListener(ILocationsPoolListener listener) {
		synchronized(LOCATIONS_POOL_LISTENERS) {
			LOCATIONS_POOL_LISTENERS.add(listener);
		}
	}

	/**
	 * removes a {@link ILocationsPoolListener} from the {@link #getLocationsPool() pool of location}.
	 * 
	 * @param listener
	 *            the {@link ILocationsPoolListener} to add
	 */
	public static void removeLocationFromPoolListener(ILocationsPoolListener listener) {
		synchronized(LOCATIONS_POOL_LISTENERS) {
			LOCATIONS_POOL_LISTENERS.remove(listener);
		}
	}

	/**
	 * Activates the given {@link ILocationDescriptor} form the {@link #getLocationsPool() pool of location
	 * descriptor}.
	 * 
	 * @param locationDescriptor
	 *            the {@link ILocationDescriptor} to activate
	 */
	public static void activateLocation(ILocationDescriptor locationDescriptor) {
		final boolean changed;

		synchronized(LOCATIONS_POOL) {
			assert LOCATIONS_POOL.containsKey(locationDescriptor);
			final Boolean lastValue = LOCATIONS_POOL.put(locationDescriptor, Boolean.TRUE);
			changed = lastValue == null || !lastValue;
		}

		if (changed) {
			for (ILocationsPoolListener listener : getLocationPoolListeners()) {
				listener.locationActivated(locationDescriptor);
			}
		}
	}

	/**
	 * Deactivates the given {@link ILocationDescriptor} form the {@link #getLocationsPool() pool of location
	 * descriptor}.
	 * 
	 * @param locationDescriptor
	 *            the {@link ILocationDescriptor} to deactivate
	 */
	public static void deactivateLocation(ILocationDescriptor locationDescriptor) {
		final boolean changed;

		synchronized(LOCATIONS_POOL) {
			assert LOCATIONS_POOL.containsKey(locationDescriptor);
			final Boolean lastValue = LOCATIONS_POOL.put(locationDescriptor, Boolean.FALSE);
			changed = lastValue == null || lastValue;
		}

		if (changed) {
			for (ILocationsPoolListener listener : getLocationPoolListeners()) {
				listener.locationDeactivated(locationDescriptor);
			}
		}
	}

	/**
	 * Tells if the given {@link ILocationDescriptor} from the {@link IdeMappingUtils#getLocationsPool()
	 * location descriptors pool} is active.
	 * 
	 * @param locationDescriptor
	 *            the {@link ILocationDescriptor}
	 * @return <code>true</code> if the given {@link ILocationDescriptor} from the
	 *         {@link IdeMappingUtils#getLocationsPool() location descriptors pool} is active,
	 *         <code>false</code> otherwise
	 */
	public static boolean isActive(ILocationDescriptor locationDescriptor) {
		final boolean res;

		synchronized(LOCATIONS_POOL) {
			final Boolean isActive = LOCATIONS_POOL.get(locationDescriptor);
			res = isActive != null && isActive;
		}

		return res;
	}

	/**
	 * Tells if there is at least one {@link IdeMappingUtils#isActive(ILocationDescriptor) active}
	 * {@link ILocationDescriptor} in the {@link IdeMappingUtils#getLocationsPool() location descriptors pool}
	 * .
	 * 
	 * @return <code>true</code> if there is at least one
	 *         {@link IdeMappingUtils#isActive(ILocationDescriptor) active} {@link ILocationDescriptor} in the
	 *         {@link IdeMappingUtils#getLocationsPool() location descriptors pool}, <code>false</code>
	 *         otherwise
	 */
	public static boolean asActiveLocationDescriptor() {
		synchronized(LOCATIONS_POOL) {
			return LOCATIONS_POOL.values().contains(Boolean.TRUE);
		}
	}

}
