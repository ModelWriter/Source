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
package org.eclipse.mylyn.docs.intent.mapping.base;

/**
 * Listen to {@link ILocation} changes.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ILocationListener extends ILocationContainerListener {

	/**
	 * Stub implementation.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class Stub extends ILocationContainerListener.Stub implements ILocationListener {

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener#sourceLinkAdded(org.eclipse.mylyn.docs.intent.mapping.base.ILink)
		 */
		public void sourceLinkAdded(ILink link) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener#sourceLinkRemoved(org.eclipse.mylyn.docs.intent.mapping.base.ILink)
		 */
		public void sourceLinkRemoved(ILink link) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener#targetLinkAdded(org.eclipse.mylyn.docs.intent.mapping.base.ILink)
		 */
		public void targetLinkAdded(ILink link) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener#targetLinkRemoved(org.eclipse.mylyn.docs.intent.mapping.base.ILink)
		 */
		public void targetLinkRemoved(ILink link) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener#containerChanged(org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer,
		 *      org.eclipse.mylyn.docs.intent.mapping.base.ILocationContainer)
		 */
		public void containerChanged(ILocationContainer oldContainer, ILocationContainer newContainer) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILocationListener#markedAsDeletedChanged(boolean)
		 */
		public void markedAsDeletedChanged(boolean newValue) {
			// nothing to do here
		}

	}

	/**
	 * Notifies when the {@link ILocation#getSourceLinks() source} {@link ILink} is added.
	 * 
	 * @param link
	 *            the added {@link ILocation#getSourceLinks() source} {@link ILink}
	 */
	void sourceLinkAdded(ILink link);

	/**
	 * Notifies when the {@link ILocation#getSourceLinks() source} {@link ILink} is removed.
	 * 
	 * @param link
	 *            the removed {@link ILocation#getSourceLinks() source} {@link ILink}
	 */
	void sourceLinkRemoved(ILink link);

	/**
	 * Notifies when the {@link ILocation#getTargetLinks() target} {@link ILink} is added.
	 * 
	 * @param link
	 *            the added {@link ILocation#getTargetLinks() target} {@link ILink}
	 */
	void targetLinkAdded(ILink link);

	/**
	 * Notifies when the {@link ILocation#getTargetLinks() target} {@link ILink} is removed.
	 * 
	 * @param link
	 *            the removed {@link ILocation#getTargetLinks() target} {@link ILink}
	 */
	void targetLinkRemoved(ILink link);

	/**
	 * Notifies when the {@link ILocation#getContainer() container} {@link ILocationContainer} has changed.
	 * 
	 * @param oldContainer
	 *            the old {@link ILocationContainer}
	 * @param newContainer
	 *            the new {@link ILocationContainer}
	 */
	void containerChanged(ILocationContainer oldContainer, ILocationContainer newContainer);

	/**
	 * Notifies when the {@link ILocation#isMarkedAsDeleted() marked as deleted} has changed.
	 * 
	 * @param newValue
	 *            the new value
	 */
	void markedAsDeletedChanged(boolean newValue);

}
