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
package org.eclipse.mylyn.docs.intent.mapping.ide.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.Activator;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;

/**
 * An {@link IResourceChangeListener} that update
 * {@link org.eclipse.mylyn.docs.intent.mapping.ide.resource.IResourceLocation IResourceLocation}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ResourceLocationListener implements IResourceChangeListener {

	/**
	 * The "Unable to load mapping base from " message.
	 */
	private static final String UNABLE_LISTEN_TO_RESOURCE_LOCATION = "Unable listen to resource location: ";

	/**
	 * The {@link List} of known {@link ILocationDescriptor}.
	 */
	private final List<ILocationDescriptor> knownDescriptors = new ArrayList<ILocationDescriptor>();

	/**
	 * The
	 * {@link org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#register(org.eclipse.mylyn.docs.intent.mapping.connector.IConnector)
	 * registered} {@link ResourceConnector}.
	 */
	private final ResourceConnector resourceConnector;

	/**
	 * Constructor.
	 * 
	 * @param scan
	 *            tells if we should scan the workspace
	 * @param resourceConnector
	 *            the
	 *            {@link org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#register(org.eclipse.mylyn.docs.intent.mapping.connector.IConnector)
	 *            registered} {@link ResourceConnector}
	 */
	public ResourceLocationListener(boolean scan, ResourceConnector resourceConnector) {
		if (scan) {
			scan(ResourcesPlugin.getWorkspace().getRoot());
		}
		this.resourceConnector = resourceConnector;
	}

	/**
	 * Scans the given {@link IContainer} for bases.
	 * 
	 * @param container
	 *            the {@link IContainer}
	 */
	private void scan(IContainer container) {
		IResource[] members;
		final IBase base = IdeMappingUtils.getCurentBase();
		if (base != null) {
			try {
				members = container.members();
				for (IResource member : members) {
					updateLocation(base, member);
				}
			} catch (CoreException e) {
				Activator.getDefault().getLog().log(
						new Status(IStatus.WARNING, Activator.PLUGIN_ID, UNABLE_LISTEN_TO_RESOURCE_LOCATION
								+ e.getMessage(), e));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event) {
		final IBase base = IdeMappingUtils.getCurentBase();
		if (event.getDelta() != null && base != null) {
			walkDelta(event.getDelta(), new HashMap<IPath, IResource>(), base);
		}
	}

	/**
	 * Walks the {@link IResourceDelta} tree.
	 * 
	 * @param delta
	 *            the root {@link IResourceDelta}
	 * @param movedResources
	 *            mapping of moved {@link IResource}
	 * @param base
	 *            the current {@link IBase}
	 */
	private void walkDelta(IResourceDelta delta, HashMap<IPath, IResource> movedResources, IBase base) {
		processDelta(delta, movedResources, base);
		for (IResourceDelta child : delta.getAffectedChildren()) {
			walkDelta(child, movedResources, base);
		}
	}

	/**
	 * Processes the given {@link IResourceDelta} by
	 * {@link WorkspaceListener#fireChange(fr.obeo.dsl.workspace.listener.change.IChange) firing}
	 * {@link fr.obeo.dsl.workspace.listener.change.IChange IChange}.
	 * 
	 * @param delta
	 *            the {@link IResourceDelta} to process
	 * @param movedResources
	 *            mapping of moved {@link IResource}
	 * @param base
	 *            the current {@link IBase}
	 */
	private void processDelta(IResourceDelta delta, HashMap<IPath, IResource> movedResources, IBase base) {
		switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				processAddedDelta(delta, movedResources, base);
				break;

			case IResourceDelta.REMOVED:
				processRemovedDelta(delta, movedResources, base);
				break;

			case IResourceDelta.CHANGED:
				processChangedDelta(delta, base);
				break;

			default:
				break;
		}
	}

	/**
	 * Process {@link IResourceDelta} with {@link IResourceDelta#CHANGED changed}
	 * {@link IResourceDelta#getKind() kind}.
	 * 
	 * @param delta
	 *            the {@link IResourceDelta} with {@link IResourceDelta#CHANGED changed}
	 *            {@link IResourceDelta#getKind() kind}
	 * @param base
	 *            the current {@link IBase}
	 */
	private void processChangedDelta(IResourceDelta delta, IBase base) {
		if ((delta.getFlags() & IResourceDelta.OPEN) != 0) {
			if (delta.getResource().isAccessible()) {
				updateLocation(base, delta.getResource());
			} else {
				// we can't track changes but the location is still valid for the moment
			}
		} else if ((delta.getFlags() & IResourceDelta.DESCRIPTION) != 0) {
			// nothing to do here
		} else if ((delta.getFlags() & IResourceDelta.CONTENT) != 0) {
			updateLocation(base, delta.getResource());
		}
	}

	/**
	 * Process {@link IResourceDelta} with {@link IResourceDelta#REMOVED removed}
	 * {@link IResourceDelta#getKind() kind}.
	 * 
	 * @param delta
	 *            the {@link IResourceDelta} with {@link IResourceDelta#REMOVED removed}
	 *            {@link IResourceDelta#getKind() kind}
	 * @param movedResources
	 *            mapping of moved {@link IResource}
	 * @param base
	 *            the current {@link IBase}
	 */
	private void processRemovedDelta(IResourceDelta delta, HashMap<IPath, IResource> movedResources,
			IBase base) {
		if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0) {
			final IResource target = movedResources.get(delta.getMovedToPath());
			if (target != null) {
				moveResource(base, delta.getResource(), target);
			} else {
				movedResources.put(delta.getResource().getFullPath(), delta.getResource());
			}
		} else {
			// TODO we implicitly decide to have a flat structure of location here... we probably don't want
			// to do
			// that
			final ILocation location = resourceConnector.getLocation(base, delta.getResource());
			if (location != null) {
				try {
					MappingUtils.markAsDeletedOrDelete(location, "the resource "
							+ delta.getResource().getFullPath().toString() + " has been deleted.");
				} catch (InstantiationException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.getMessage(), e));
				} catch (IllegalAccessException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.getMessage(), e));
				} catch (ClassNotFoundException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.getMessage(), e));
				}
			}
		}
	}

	/**
	 * Process {@link IResourceDelta} with {@link IResourceDelta#ADDED added} {@link IResourceDelta#getKind()
	 * kind}.
	 * 
	 * @param delta
	 *            the {@link IResourceDelta} with {@link IResourceDelta#ADDED added}
	 *            {@link IResourceDelta#getKind() kind}
	 * @param movedResources
	 *            mapping of moved {@link IResource}
	 * @param base
	 *            the current {@link IBase}
	 */
	private void processAddedDelta(IResourceDelta delta, HashMap<IPath, IResource> movedResources, IBase base) {
		if ((delta.getFlags() & IResourceDelta.MOVED_FROM) != 0) {
			final IResource source = movedResources.get(delta.getMovedFromPath());
			if (source != null) {
				moveResource(base, source, delta.getResource());
			} else {
				movedResources.put(delta.getResource().getFullPath(), delta.getResource());
			}
		} else {
			updateLocation(base, delta.getResource());
		}
	}

	/**
	 * Updates the given {@link IResource}.
	 * 
	 * @param base
	 *            the current {@link IBase}
	 * @param resource
	 *            the changed {@link IResource}
	 */
	private void updateLocation(IBase base, IResource resource) {
		// TODO we implicitly decide to have a flat structure of location here... we probably don't want to do
		// that
		final ILocation location = resourceConnector.getLocation(base, resource);
		if (location != null && !location.isMarkedAsDeleted()) {
			resourceConnector.updateLocation(location, resource);
			try {
				MappingUtils.markAsChanged(location, "Content changed.");
			} catch (InstantiationException e) {
				Activator.getDefault().getLog().log(
						new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.getMessage(), e));
			} catch (IllegalAccessException e) {
				Activator.getDefault().getLog().log(
						new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.getMessage(), e));
			} catch (ClassNotFoundException e) {
				Activator.getDefault().getLog().log(
						new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.getMessage(), e));
			}
		}
		updateKnownDescriptors(resource);
	}

	/**
	 * Handles a {@link IResource} move.
	 * 
	 * @param base
	 *            the current {@link IBase}
	 * @param source
	 *            the source {@link IResource}
	 * @param target
	 *            the target {@link IResource}
	 */
	private void moveResource(IBase base, IResource source, IResource target) {
		// TODO we implicitly decide to have a flat structure of location here... we probably don't want to do
		// that
		final ILocation location = resourceConnector.getLocation(base, source);
		if (location != null && !location.isMarkedAsDeleted()) {
			resourceConnector.updateLocation(location, target);
			try {
				MappingUtils.markAsChanged(location, String.format("%s moved to %s", source.getFullPath()
						.toPortableString(), target.getFullPath().toPortableString()));
			} catch (InstantiationException e) {
				Activator.getDefault().getLog().log(
						new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.getMessage(), e));
			} catch (IllegalAccessException e) {
				Activator.getDefault().getLog().log(
						new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.getMessage(), e));
			} catch (ClassNotFoundException e) {
				Activator.getDefault().getLog().log(
						new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.getMessage(), e));
			}
		}
		moveKnownDescriptor(source, target);
	}

	/**
	 * Updates known {@link ILocationDescriptor} for the given {@link IResource}.
	 * 
	 * @param resource
	 *            the {@link IResource}
	 */
	private void updateKnownDescriptors(IResource resource) {
		synchronized(knownDescriptors) {
			for (ILocationDescriptor descriptor : knownDescriptors) {
				if (resource.equals(descriptor.getElement())) {
					descriptor.update(resource);
				}
			}
		}
	}

	/**
	 * Move known {@link ILocationDescriptor} from the given source to the given target.
	 * 
	 * @param source
	 *            the source
	 * @param target
	 *            the target
	 */
	private void moveKnownDescriptor(IResource source, IResource target) {
		synchronized(knownDescriptors) {
			for (ILocationDescriptor descriptor : knownDescriptors) {
				if (source.equals(descriptor.getElement())) {
					descriptor.update(target);
				}
			}
		}
	}

	/**
	 * Adds the given {@link ILocationDescriptor}.
	 * 
	 * @param descriptor
	 *            the {@link ILocationDescriptor} to add
	 */
	public void addKnownDescriptor(ILocationDescriptor descriptor) {
		synchronized(knownDescriptors) {
			knownDescriptors.add(descriptor);
		}
	}

	/**
	 * Removes the given {@link ILocationDescriptor}.
	 * 
	 * @param descriptor
	 *            the {@link ILocationDescriptor} to remove
	 */
	public void removeKnownDescriptor(ILocationDescriptor descriptor) {
		synchronized(knownDescriptors) {
			knownDescriptors.remove(descriptor);
		}
	}

}
