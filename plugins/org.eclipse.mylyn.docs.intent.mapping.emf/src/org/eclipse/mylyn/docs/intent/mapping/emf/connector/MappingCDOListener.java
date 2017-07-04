/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.emf.connector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.cdo.common.revision.CDOIDAndVersion;
import org.eclipse.emf.cdo.common.revision.CDORevisionKey;
import org.eclipse.emf.cdo.common.revision.delta.CDOFeatureDelta;
import org.eclipse.emf.cdo.common.revision.delta.CDORevisionDelta;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.eresource.CDOResourceFolder;
import org.eclipse.emf.cdo.eresource.CDOResourceNode;
import org.eclipse.emf.cdo.eresource.EresourcePackage;
import org.eclipse.emf.cdo.session.CDOSessionInvalidationEvent;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDORepositoryLocation;
import org.eclipse.net4j.util.event.IEvent;
import org.eclipse.net4j.util.event.IListener;

/**
 * Listen to changes and update locations accordingly.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MappingCDOListener implements IListener {

	/**
	 * The {@link CDOView} cache.
	 */
	private final Map<String, Map<Integer, CDOView>> viewCache = new HashMap<String, Map<Integer, CDOView>>();

	/**
	 * The {@link Set} of {@link IBase} to update.
	 */
	private final Set<IBase> bases = new LinkedHashSet<IBase>();

	/**
	 * Adds the given {@link CDOView} to the cache.
	 * 
	 * @param view
	 *            the {@link CDOView} to add
	 */
	public void addSessionToCache(CDOView view) {
		Map<Integer, CDOView> map = viewCache.get(view.getSession().getRepositoryInfo().getUUID());
		if (map == null) {
			map = new HashMap<Integer, CDOView>();
			viewCache.put(view.getSession().getRepositoryInfo().getUUID(), map);
		}
		map.put(view.getBranch().getID(), view);

		view.getSession().addListener(this);
	}

	/**
	 * Removes the given {@link CDOView} to the cache.
	 * 
	 * @param view
	 *            the {@link CDOView} to remove
	 */
	public void removeSessionFromCache(CDOView view) {
		view.getSession().removeListener(this);

		Map<Integer, CDOView> map = viewCache.get(view.getSession().getRepositoryInfo().getUUID());
		if (map != null) {
			if (map.remove(view.getBranch().getID()) != null && map.isEmpty()) {
				viewCache.remove(view.getSession().getRepositoryInfo().getUUID());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getElement(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public Object getElement(ILocation location) {
		final CDOView res;

		final Map<Integer, CDOView> map = viewCache.get(((ICDORepositoryLocation)location).getUUID());
		if (map != null) {
			res = map.get(((ICDORepositoryLocation)location).getBranchID());
		} else {
			res = null;
		}

		// TODO if not in cache open a session/view...

		return res;
	}

	/**
	 * Adds the given {@link IBase} to the {@link Set} of {@link IBase} to update.
	 * 
	 * @param base
	 *            the {@link IBase}
	 */
	public void addBaseToUpdate(IBase base) {
		bases.add(base);
	}

	/**
	 * Removes the given {@link IBase} from the {@link Set} of {@link IBase} to update.
	 * 
	 * @param base
	 *            the {@link IBase}
	 */
	public void removeBaseToUpdate(IBase base) {
		bases.add(base);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.net4j.util.event.IListener#notifyEvent(org.eclipse.net4j.util.event.IEvent)
	 */
	public void notifyEvent(IEvent event) {
		if (event instanceof CDOSessionInvalidationEvent) {
			final CDOSessionInvalidationEvent cdoSessionInvalidationEvent = (CDOSessionInvalidationEvent)event;

			checkDetachedObjects(cdoSessionInvalidationEvent);

			checkChangedObjects(cdoSessionInvalidationEvent);
		}
	}

	/**
	 * Checks for {@link CDOSessionInvalidationEvent#getDetachedObjects() detached objects}.
	 * 
	 * @param cdoSessionInvalidationEvent
	 *            the {@link CDOSessionInvalidationEvent}
	 */
	protected void checkDetachedObjects(final CDOSessionInvalidationEvent cdoSessionInvalidationEvent) {
		final CDOView view = cdoSessionInvalidationEvent.getSource().openView(cdoSessionInvalidationEvent
				.getBranch(), cdoSessionInvalidationEvent.getPreviousTimeStamp());

		for (CDOIDAndVersion cdoidAndVersion : cdoSessionInvalidationEvent.getDetachedObjects()) {
			final CDOObject object = view.getObject(cdoidAndVersion.getID());
			if (object instanceof CDOResourceNode) {
				final CDOResourceNode node = (CDOResourceNode)object;
				for (IBase base : bases) {
					final ILocation repositoryLocation = MappingUtils.getConnectorRegistry().getLocation(base,
							view);
					if (repositoryLocation != null) {
						deleteResourceNode(repositoryLocation, node);
					}
				}
			} else {
				// TODO other deletions/resource change
			}
		}

		view.close();
	}

	/**
	 * Deletes the given {@link CDOResourceNode} from the given {@link ILocationContainer}.
	 * 
	 * @param container
	 *            the {@link ILocationContainer}
	 * @param node
	 *            the {@link CDOResourceNode}
	 */
	protected void deleteResourceNode(final ILocationContainer container, final CDOResourceNode node) {
		final ILocation resourceNodeLocation = MappingUtils.getConnectorRegistry().getLocation(container,
				node);
		if (resourceNodeLocation != null) {
			try {
				MappingUtils.markAsDeletedOrDelete(resourceNodeLocation, node.getPath()
						+ " has been deleted.");
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks for {@link CDOSessionInvalidationEvent#getChangedObjects() changed objects}.
	 * 
	 * @param cdoSessionInvalidationEvent
	 *            the {@link CDOSessionInvalidationEvent}
	 */
	protected void checkChangedObjects(final CDOSessionInvalidationEvent cdoSessionInvalidationEvent) {
		final CDOView oldView = cdoSessionInvalidationEvent.getSource().openView(cdoSessionInvalidationEvent
				.getBranch(), cdoSessionInvalidationEvent.getPreviousTimeStamp());
		final CDOView newView = cdoSessionInvalidationEvent.getLocalTransaction();

		for (IBase base : bases) {
			final Map<CDOView, Set<Object>> changedElements = new HashMap<CDOView, Set<Object>>();
			for (CDORevisionKey cdoRevisionKey : cdoSessionInvalidationEvent.getChangedObjects()) {
				final ILocation repositoryLocation = MappingUtils.getConnectorRegistry().getLocation(base,
						oldView);
				if (repositoryLocation != null) {
					if (cdoRevisionKey instanceof CDORevisionDelta) {
						final CDORevisionDelta delta = (CDORevisionDelta)cdoRevisionKey;
						final CDOObject newObject = newView.getObject(delta.getID());
						Set<Object> set = changedElements.get(newView);
						if (set == null) {
							set = new HashSet<Object>();
							changedElements.put(newView, set);
						}
						set.add(newObject.eResource());
						final CDOFeatureDelta renameDelta = delta.getFeatureDelta(EresourcePackage.eINSTANCE
								.getCDOResourceNode_Name());
						final CDOFeatureDelta binaryContentDelta = delta.getFeatureDelta(
								EresourcePackage.eINSTANCE.getCDOBinaryResource_Contents());
						final CDOFeatureDelta textContentDelta = delta.getFeatureDelta(
								EresourcePackage.eINSTANCE.getCDOTextResource_Contents());
						if (renameDelta != null) {
							final CDOResourceNode object = (CDOResourceNode)oldView.getObject(delta.getID());
							final CDOResourceNode oldNode = (CDOResourceNode)object;
							renameResourceNode(newView, repositoryLocation, oldNode,
									(CDOResourceNode)newObject);
						} else if (binaryContentDelta != null || textContentDelta != null) {
							set.add(newObject);
						}
					}
				}
			}
			resourceContentChanged(base, changedElements);
		}
		oldView.close();

	}

	/**
	 * Renames the given {@link CDOResourceNode} and its contents if any.
	 * 
	 * @param newView
	 *            the {@link CDOView} before changes
	 * @param repositoryLocation
	 *            the repository location
	 * @param oldNode
	 *            the {@link CDOResourceNode} before changes
	 * @param newNode
	 *            the {@link CDOResourceNode} after changes
	 */
	private void renameResourceNode(CDOView newView, ILocation repositoryLocation, CDOResourceNode oldNode,
			CDOResourceNode newNode) {
		final ILocation location = MappingUtils.getConnectorRegistry().getLocation(repositoryLocation,
				oldNode);
		if (location != null) {
			MappingUtils.getConnectorRegistry().updateLocation(location, newNode);
		}
		if (oldNode instanceof CDOResourceFolder) {
			for (CDOResourceNode oldChild : ((CDOResourceFolder)oldNode).getNodes()) {
				final CDOResourceNode newChild = (CDOResourceNode)newView.getObject(oldChild.cdoID());
				if (newChild != null) {
					renameResourceNode(newView, repositoryLocation, oldChild, newChild);
				}
			}
		}
	}

	/**
	 * Updates changed {@link CDOResource}.
	 * 
	 * @param base
	 *            the {@link IBase}
	 * @param changedElements
	 *            the {@link Set} of changed elements per {@link CDOView}
	 */
	private void resourceContentChanged(IBase base, Map<CDOView, Set<Object>> changedElements) {
		for (Entry<CDOView, Set<Object>> entry : changedElements.entrySet()) {
			final ILocation repositoryLocation = MappingUtils.getConnectorRegistry().getLocation(base, entry
					.getKey());
			if (repositoryLocation != null) {
				for (Object element : entry.getValue()) {
					final ILocation location = MappingUtils.getConnectorRegistry().getLocation(
							repositoryLocation, element);
					if (location != null) {
						MappingUtils.getConnectorRegistry().updateLocation(location, element);
					}
				}
			}
		}
	}

}
