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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.connector.AbstractConnector;
import org.eclipse.mylyn.docs.intent.mapping.jena.IRdfContainer;
import org.eclipse.mylyn.docs.intent.mapping.jena.IRdfLocation;

/**
 * {@link Resource} {@link org.eclipse.mylyn.docs.intent.mapping.connector.IConnector IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class RdfConnector extends AbstractConnector {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getName(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public String getName(ILocation location) {
		final String res;

		res = ((IRdfLocation)location).getURI();

		return res;
	}

	@Override
	protected boolean match(ILocation location, Object element) {
		final IRdfLocation rdfLocation = (IRdfLocation)location;
		final Resource resource = (Resource)element;

		return rdfLocation.getURI().equals(resource.getURI());
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

		if (IRdfContainer.class.isAssignableFrom(containerType) && element instanceof Resource) {
			res = getType();
		} else {
			res = null;
		}

		return res;
	}

	@Override
	protected void initLocation(ILocationContainer container, ILocation location, Object element) {
		final IRdfLocation rdfLocation = (IRdfLocation)location;
		final Resource resource = (Resource)element;

		rdfLocation.setURI(resource.getURI());
	}

	@Override
	protected boolean canUpdate(Object element) {
		return element instanceof Resource;
	}

	/**
	 * updates the given {@link IRdfContainer} with the given {@link List} of {@link Resource}.
	 * 
	 * @param container
	 *            the {@link IRdfContainer}
	 * @param resources
	 *            the {@link List} of {@link Resource}
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason.
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	public static void updateRdfContainer(IRdfContainer container, List<Resource> resources)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final Set<String> newURIs = new HashSet<String>();
		for (Resource resource : resources) {
			newURIs.add(resource.getURI());
		}

		for (ILocation child : container.getContents()) {
			if (child instanceof IRdfLocation) {
				if (!newURIs.contains(((IRdfLocation)child).getURI())) {
					MappingUtils.markAsDeletedOrDelete(child, ((IRdfLocation)child).getURI().toString()
							+ " doesn't longer.");
				}
				// TODO detect and handle change
			}
		}
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
		if (adapted instanceof Resource) {
			res = new ObjectLocationDescriptor(base, adapted, ((Resource)adapted).getURI());
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
		final Resource res;

		final Model model = (Model)MappingUtils.getConnectorRegistry().getElement((ILocation)location
				.getContainer());
		res = model.getResource(((IRdfLocation)location).getURI());

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getType()
	 */
	public Class<? extends ILocation> getType() {
		return IRdfLocation.class;
	}

}
