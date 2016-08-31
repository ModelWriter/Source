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
package org.eclipse.mylyn.docs.intent.mapping.base;

/**
 * Listen to {@link ILocationContainer} changes.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ILocationContainerListener {

	/**
	 * Stub implementation.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class Stub implements ILocationContainerListener {

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainerListener#contentsAdded(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
		 */
		public void contentsAdded(ILocation location) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainerListener#contentsRemoved(org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
		 */
		public void contentsRemoved(ILocation location) {
			// nothing to do here
		}

	}

	/**
	 * Notifies when the {@link ILocation#getContents() content} {@link ILocation} is added.
	 * 
	 * @param location
	 *            the added {@link ILocation#getContents() content} {@link ILocation}
	 */
	void contentsAdded(ILocation location);

	/**
	 * Notifies when the {@link ILocation#getContents() content} {@link ILocation} is removed.
	 * 
	 * @param location
	 *            the removed {@link ILocation#getContents() content} {@link ILocation}
	 */
	void contentsRemoved(ILocation location);

}
