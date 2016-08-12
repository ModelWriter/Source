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
package org.eclipse.mylyn.docs.intent.mapping.jena.connector;

import com.hp.hpl.jena.rdf.model.Resource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.conector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.jena.IRdfContainer;
import org.eclipse.mylyn.docs.intent.mapping.jena.IRdfLocation;

/**
 * {@link Resource} {@link org.eclipse.mylyn.docs.intent.mapping.conector.IConnector IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class RdfConnector extends AbstractConnector {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnector#getName(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public String getName(ILocation location) {
		final String res;

		if (location instanceof IRdfLocation) {
			res = ((IRdfLocation)location).getURI();
		} else {
			res = null;
		}

		return res;
	}

	@Override
	protected boolean match(ILocation location, Object element) {
		final IRdfLocation rdfLocation = (IRdfLocation)location;
		final Resource resource = (Resource)element;

		return rdfLocation.getURI().equals(resource.getURI());
	}

	@Override
	protected Class<? extends ILocation> getLocationType(Class<? extends ILocationContainer> containerType,
			Object element) {
		final Class<? extends ILocation> res;

		if (IRdfContainer.class.isAssignableFrom(containerType) && element instanceof Resource) {
			res = getLocationType();
		} else {
			res = null;
		}

		return res;
	}

	@Override
	protected void initLocation(ILocation location, Object element) {
		final IRdfLocation rdfLocation = (IRdfLocation)location;
		final Resource resource = (Resource)element;

		rdfLocation.setURI(resource.getURI());
	}

	/**
	 * updates the given {@link IRdfContainer} with the given {@link List} of {@link Resource}.
	 * 
	 * @param container
	 *            the {@link IRdfContainer}
	 * @param resources
	 *            the {@link List} of {@link Resource}
	 */
	public void update(IRdfContainer container, List<Resource> resources) {
		final Set<String> newURIs = new HashSet<String>();
		for (Resource resource : resources) {
			newURIs.add(resource.getURI());
		}

		container.setResources(resources);

		for (ILocation child : container.getContents()) {
			if (child instanceof IRdfLocation) {
				if (!newURIs.contains(((IRdfLocation)child).getURI())) {
					// TODO mark child as deleted
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.conector.IConnector#getLocationType()
	 */
	public Class<? extends ILocation> getLocationType() {
		return IRdfLocation.class;
	}

}
