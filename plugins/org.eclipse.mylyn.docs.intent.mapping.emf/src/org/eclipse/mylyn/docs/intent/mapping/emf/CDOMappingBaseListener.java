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
package org.eclipse.mylyn.docs.intent.mapping.emf;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.revision.CDOIDAndVersion;
import org.eclipse.emf.cdo.session.CDOSessionInvalidationEvent;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.IBaseRegistry;
import org.eclipse.mylyn.docs.intent.mapping.impl.BaseImpl;
import org.eclipse.net4j.util.event.IEvent;
import org.eclipse.net4j.util.event.IListener;

/**
 * A CDO listener that track changes on {@link IBase}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class CDOMappingBaseListener implements IListener {

	/**
	 * Mapping to keep track of {@link IBaseRegistry#register(IBase) registered} bases.
	 */
	Map<CDOID, Map<String, Map<Integer, BaseImpl>>> bases = new HashMap<CDOID, Map<String, Map<Integer, BaseImpl>>>();

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.net4j.util.event.IListener#notifyEvent(org.eclipse.net4j.util.event.IEvent)
	 */
	public void notifyEvent(IEvent event) {
		if (event instanceof CDOSessionInvalidationEvent) {
			final CDOSessionInvalidationEvent cdoSessionInvalidationEvent = (CDOSessionInvalidationEvent)event;

			checkAttachedObjects(cdoSessionInvalidationEvent);

			checkDetachedObjects(cdoSessionInvalidationEvent);
		}
	}

	/**
	 * Checks for {@link CDOSessionInvalidationEvent#getNewObjects() attached objects}.
	 * 
	 * @param cdoSessionInvalidationEvent
	 *            the {@link CDOSessionInvalidationEvent}
	 */
	protected void checkAttachedObjects(CDOSessionInvalidationEvent cdoSessionInvalidationEvent) {
		final CDOView view = cdoSessionInvalidationEvent.getLocalTransaction();

		for (CDOIDAndVersion cdoidAndVersion : cdoSessionInvalidationEvent.getNewObjects()) {
			final CDOObject object = view.getObject(cdoidAndVersion.getID());
			if (object instanceof BaseImpl) {
				registerBase(view, (BaseImpl)object);
			}
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
			if (object instanceof BaseImpl) {
				unregisterBase(view, (BaseImpl)object);
			}
		}

		view.close();
	}

	/**
	 * {@link IBaseRegistry#register(IBase) Registers} the given {@link BaseImpl}.
	 * 
	 * @param view
	 *            the {@link CDOView}
	 * @param base
	 *            the {@link BaseImpl}
	 */
	protected void registerBase(CDOView view, BaseImpl base) {
		MappingUtils.getMappingRegistry().register(base);
		final CDOID cdoID = base.cdoID();
		Map<String, Map<Integer, BaseImpl>> repoMap = bases.get(cdoID);
		if (repoMap == null) {
			repoMap = new HashMap<String, Map<Integer, BaseImpl>>();
			bases.put(cdoID, repoMap);
		}
		final String uuid = view.getSession().getRepositoryInfo().getUUID();
		Map<Integer, BaseImpl> branchMap = repoMap.get(uuid);
		if (branchMap == null) {
			branchMap = new HashMap<Integer, BaseImpl>();
			repoMap.put(uuid, branchMap);
		}
		branchMap.put(view.getBranch().getID(), base);
		MappingUtils.getMappingRegistry().register(base);
	}

	/**
	 * {@link IBaseRegistry#unregister(IBase) Unregisters} the given {@link BaseImpl}.
	 * 
	 * @param view
	 *            the {@link CDOView}
	 * @param base
	 *            the {@link BaseImpl}
	 */
	protected void unregisterBase(CDOView view, BaseImpl base) {
		final CDOID cdoID = base.cdoID();
		final Map<String, Map<Integer, BaseImpl>> repoMap = bases.get(cdoID);
		if (repoMap != null) {
			final String uuid = view.getSession().getRepositoryInfo().getUUID();
			final Map<Integer, BaseImpl> branchMap = repoMap.get(uuid);
			if (branchMap != null) {
				final BaseImpl toRemove = branchMap.remove(view.getBranch().getID());
				if (toRemove != null) {
					MappingUtils.getMappingRegistry().unregister(toRemove);
					removeFromMap(cdoID, repoMap, uuid, branchMap);
				}
			}
		}
	}

	/**
	 * Removes empty maps.
	 * 
	 * @param cdoID
	 *            the {@link CDOID}
	 * @param repoMap
	 *            the repository map
	 * @param uuid
	 *            the UUID of the repository
	 * @param branchMap
	 *            the branch map
	 */
	protected void removeFromMap(CDOID cdoID, Map<String, Map<Integer, BaseImpl>> repoMap, String uuid,
			Map<Integer, BaseImpl> branchMap) {
		if (branchMap.isEmpty()) {
			repoMap.remove(uuid);
			if (repoMap.isEmpty()) {
				bases.remove(cdoID);
			}
		}
	}

}
