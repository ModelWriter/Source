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
package org.eclipse.mylyn.docs.intent.mapping.connector;

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;

/**
 * An abstract implementation of {@link IConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractConnector implements IConnector {

	/**
	 * {@link ILocationDescriptor} based on a {@link Object} and a location type.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	protected class ObjectLocationDescriptor implements ILocationDescriptor {

		/**
		 * The located {@link Object element}.
		 */
		private Object element;

		/**
		 * The containing {@link IBase}.
		 */
		private final IBase base;

		/**
		 * The {@link ILocationDescriptor#getContainerDescriptor() container descriptor}.
		 */
		private final ILocationDescriptor containerDescriptor;

		/**
		 * The human readable name.
		 */
		private final String name;

		/**
		 * Constructor.
		 * 
		 * @param base
		 *            the containing {@link IBase}
		 * @param element
		 *            the {@link Object}
		 * @param name
		 *            the human readable name
		 */
		public ObjectLocationDescriptor(IBase base, Object element, String name) {
			this.base = base;
			final ILocationDescriptor containerDesc;
			final Object container = base.getContainerProviderRegistry().getContainer(element);
			if (container != null) {
				containerDesc = MappingUtils.getConnectorRegistry().getLocationDescriptor(base, container);
				if (containerDesc == null) {
					throw new IllegalStateException(
							"the ILocationDescriptor container should not be null at this point.");
				}
			} else {
				containerDesc = null;
			}
			this.containerDescriptor = containerDesc;
			this.element = element;
			this.name = name;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getContainerDescriptor()
		 */
		public ILocationDescriptor getContainerDescriptor() {
			return containerDescriptor;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#exists()
		 */
		public boolean exists() {
			return getLocation() != null;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getLocation()
		 */
		public ILocation getLocation() {
			final ILocation res;

			if (getContainerDescriptor() == null) {
				res = AbstractConnector.this.getLocation(base, element);
			} else if (getContainerDescriptor().exists()) {
				res = AbstractConnector.this.getLocation(getContainerDescriptor().getLocation(), element);
			} else {
				res = null;
			}

			return res;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getOrCreate()
		 */
		public ILocation getOrCreate() throws InstantiationException, IllegalAccessException,
				ClassNotFoundException {
			final ILocation res;

			final ILocationContainer container;
			if (getContainerDescriptor() == null) {
				container = base;
			} else {
				container = getContainerDescriptor().getOrCreate();
			}

			final ILocation existingLocation = AbstractConnector.this.getLocation(container, element);
			if (existingLocation != null) {
				res = existingLocation;
			} else {
				res = AbstractConnector.this.createLocation(container, element);
			}

			return res;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#update(java.lang.Object)
		 */
		public boolean update(Object e) {
			return AbstractConnector.this.updateLocationDescriptor(this, e);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getElement()
		 */
		public Object getElement() {
			return element;
		}

		/**
		 * Sets the given element.
		 * 
		 * @param element
		 *            the element
		 */
		public void setElement(Object element) {
			this.element = element;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#getName()
		 */
		public String getName() {
			return name;
		}

		@Override
		public int hashCode() {
			return element.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof ObjectLocationDescriptor && element.equals(
					((ObjectLocationDescriptor)obj).element);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor#dispose()
		 */
		public void dispose() {
			AbstractConnector.this.dispose(this);
		}

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#canHandle(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
	 */
	public boolean canHandle(ILocation location) {
		return getType().isAssignableFrom(location.getClass());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#createLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer,
	 *      java.lang.Object)
	 */
	public ILocation createLocation(ILocationContainer container, Object element)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final ILocation res;

		final Object adaptedElement = adapt(element);
		final Class<? extends ILocation> locationType = getLocationType(getContainerType(container),
				adaptedElement);
		if (locationType != null) {
			final ILocation location = MappingUtils.getBase(container).getFactory().createElement(
					locationType);
			if (location == null) {
				throw new IllegalArgumentException("The base can't create " + locationType.getSimpleName());
			} else {
				initLocation(container, location, adaptedElement);
				location.setContainer(container);
				res = location;
			}
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#updateLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocation,
	 *      java.lang.Object)
	 */
	public boolean updateLocation(ILocation location, Object element) {
		final boolean res = canUpdate(element);

		if (res) {
			update(location, element);
		}

		return res;
	}

	/**
	 * Updates the given {@link ObjectLocationDescriptor} with the given element.
	 * 
	 * @param locationDescriptor
	 *            the {@link ObjectLocationDescriptor} to update
	 * @param element
	 *            the element
	 * @return <code>true</code> if any changes has been done, <code>false</code> otherwise
	 */
	public boolean updateLocationDescriptor(ObjectLocationDescriptor locationDescriptor, Object element) {
		locationDescriptor.setElement(element);

		return true;
	}

	/**
	 * Tells if we can update with the given located element.
	 * 
	 * @param element
	 *            the located element
	 * @return <code>true</code> if we can update with the given located element, <code>false</code> otherwise
	 */
	protected abstract boolean canUpdate(Object element);

	/**
	 * Update the given {@link ILocation} by {@link AbstractConnector#initLocation(ILocation, Object)
	 * initializing} it with the given element.
	 * 
	 * @param location
	 *            the {@link ILocation} to update
	 * @param element
	 *            the located element
	 */
	protected void update(ILocation location, Object element) {
		initLocation(location.getContainer(), location, element);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#getLocation(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer,
	 *      java.lang.Object)
	 */
	public ILocation getLocation(ILocationContainer container, Object element) {
		final Object adaptedElement = adapt(element);
		final Class<? extends ILocationContainer> locationType = getLocationType(getContainerType(container),
				adaptedElement);
		if (locationType != null) {
			for (ILocation location : container.getContents()) {
				if (match(location, element)) {
					return location;
				}
			}
		}

		return null;
	}

	/**
	 * Tells if the given location match the given {@link Object element}.
	 * 
	 * @param location
	 *            the {@link ILocation}
	 * @param element
	 *            the {@link Object element}
	 * @return <code>true</code> if the given location match the given {@link Object element};
	 *         <code>false</code> otherwise
	 */
	protected abstract boolean match(ILocation location, Object element);

	/**
	 * Gets the container type of the given {@link ILocation container}.
	 * 
	 * @param container
	 *            the containing {@link ILocationContainer}
	 * @return the container type of the given {@link ILocation container}
	 */
	private Class<? extends ILocationContainer> getContainerType(ILocationContainer container) {
		return container.getClass();
	}

	/**
	 * Adapts the given {@link Object element} in order to create a location.
	 * 
	 * @param element
	 *            the {@link Object element}
	 * @return the {@link Object adapted element}
	 */
	protected Object adapt(Object element) {
		return element;
	}

	/**
	 * Initializes the given {@link ILocation}.
	 * 
	 * @param container
	 *            the {@link ILocationContainer}
	 * @param location
	 *            the {@link ILocation} to initialize
	 * @param element
	 *            the element to locate
	 */
	protected abstract void initLocation(ILocationContainer container, ILocation location, Object element);

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnector#dispose(org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor)
	 */
	public void dispose(ILocationDescriptor locationDescriptor) {
		// nothing to do here
	}

}
