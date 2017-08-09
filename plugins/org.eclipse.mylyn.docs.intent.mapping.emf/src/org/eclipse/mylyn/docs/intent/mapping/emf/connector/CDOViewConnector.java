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

import java.util.Set;

import org.eclipse.emf.cdo.internal.net4j.CDONet4jSessionImpl;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.ObjectLocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.connector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.emf.ICDORepositoryLocation;

/**
 * {@link CDOView} connector.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class CDOViewConnector extends AbstractConnector {

	/**
	 * The {@link MappingCDOListener}.
	 */
	private final MappingCDOListener listener = new MappingCDOListener();

	/**
	 * Adds the given {@link CDOView} to the cache.
	 * 
	 * @param view
	 *            the {@link CDOView} to add
	 */
	public void addSessionToCache(CDOView view) {
		listener.addSessionToCache(view);
	}

	/**
	 * Removes the given {@link CDOView} to the cache.
	 * 
	 * @param view
	 *            the {@link CDOView} to remove
	 */
	public void removeSessionFromCache(CDOView view) {
		listener.removeSessionFromCache(view);
	}

	/**
	 * Adds the given {@link IBase} to the {@link Set} of {@link IBase} to update.
	 * 
	 * @param base
	 *            the {@link IBase}
	 */
	public void addBaseToUpdate(IBase base) {
		listener.addBaseToUpdate(base);
	}

	/**
	 * Removes the given {@link IBase} from the {@link Set} of {@link IBase} to update.
	 * 
	 * @param base
	 *            the {@link IBase}
	 */
	public void removeBaseToUpdate(IBase base) {
		listener.removeBaseToUpdate(base);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getLocationDescriptor(org.eclipse.mylyn.docs.intent.mapping.base.IBase,
	 *      java.lang.Object)
	 */
	public ILocationDescriptor getLocationDescriptor(IBase base, Object element) {
		final ILocationDescriptor res;

		final Object adapted = adapt(element);
		if (adapted instanceof CDOView) {
			final CDOView cdoView = (CDOView)adapted;
			res = new ObjectLocationDescriptor(this, base, adapted, cdoView.getSession().getRepositoryInfo()
					.getName() + " (" + getURL(cdoView) + ")");
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getLocationType(java.lang.Class,
	 *      java.lang.Object)
	 */
	public Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
			Object element) {
		final Class<? extends ILocation> res;

		if (element instanceof CDOView) {
			res = getType();
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getElement(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public Object getElement(ILocation location) {
		return listener.getElement(location);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getName(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public String getName(ILocation location) {
		final ICDORepositoryLocation repositoryLocation = (ICDORepositoryLocation)location;
		return repositoryLocation.getName() + " (" + repositoryLocation.getURL() + ")";
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getType()
	 */
	public Class<? extends ILocation> getType() {
		return ICDORepositoryLocation.class;
	}

	@Override
	protected boolean canUpdate(Object element) {
		return element instanceof CDOView;
	}

	@Override
	protected boolean match(ILocation location, Object element) {
		return ((ICDORepositoryLocation)location).getUUID().equals(((CDOView)element).getSession()
				.getRepositoryInfo().getUUID()) && ((ICDORepositoryLocation)location)
						.getBranchID() == ((CDOView)element).getBranch().getID();
	}

	@Override
	protected void initLocation(ILocationContainer container, ILocation location, Object element) {
		final ICDORepositoryLocation toInit = (ICDORepositoryLocation)location;
		final CDOView cdoView = (CDOView)element;

		toInit.setURL(getURL(cdoView));
		toInit.setUUID(cdoView.getSession().getRepositoryInfo().getUUID());
		toInit.setName(cdoView.getSession().getRepositoryInfo().getName());
		toInit.setBranchID(cdoView.getBranch().getID());
	}

	/**
	 * Gets the URL for the given {@link CDOView}.
	 * 
	 * @param cdoView
	 *            the {@link CDOView}
	 * @return the URL for the given {@link CDOView}
	 */
	protected String getURL(CDOView cdoView) {
		final String res;

		if (cdoView.getSession() instanceof CDONet4jSessionImpl) {
			res = ((CDONet4jSessionImpl)cdoView.getSession()).getConnector().getURL();
		} else {
			res = null;
		}

		return res;
	}

}
