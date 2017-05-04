/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Oveo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.base;

/**
 * Listen to {@link ILink} changes.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ILinkListener {

	/**
	 * Stub implementation.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	class Stub implements ILinkListener {

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener#descriptionChanged(java.lang.String,
		 *      java.lang.String)
		 */
		public void descriptionChanged(String oldDescription, String newDescription) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener#sourceChanged(org.eclipse.mylyn.docs.intent.mapping.base.ILocation,
		 *      org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
		 */
		public void sourceChanged(ILocation oldSource, ILocation newSource) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener#targetChanged(org.eclipse.mylyn.docs.intent.mapping.base.ILocation,
		 *      org.eclipse.mylyn.docs.intent.mapping.base.ILocation)
		 */
		public void targetChanged(ILocation oldTarget, ILocation newTarget) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener#reportAdded(org.eclipse.mylyn.docs.intent.mapping.base.IReport)
		 */
		public void reportAdded(IReport report) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ILinkListener#reportRemoved(org.eclipse.mylyn.docs.intent.mapping.base.IReport)
		 */
		public void reportRemoved(IReport report) {
			// nothing to do here
		}
	}

	/**
	 * Notifies when the {@link ILink#getDescription() description} is changed.
	 * 
	 * @param oldDescription
	 *            the old {@link ILink#getDescription() description}
	 * @param newDescription
	 *            the new {@link ILink#getDescription() description}
	 */
	void descriptionChanged(String oldDescription, String newDescription);

	/**
	 * Notifies when the {@link ILink#getSource() source} is changed.
	 * 
	 * @param oldSource
	 *            the old {@link ILink#getSource() source}
	 * @param newSource
	 *            the new {@link ILink#getSource() source}
	 */
	void sourceChanged(ILocation oldSource, ILocation newSource);

	/**
	 * Notifies when the {@link ILink#getTarget() target} is changed.
	 * 
	 * @param oldTarget
	 *            the old {@link ILink#getTarget() target}
	 * @param newTarget
	 *            the new {@link ILink#getTarget() target}
	 */
	void targetChanged(ILocation oldTarget, ILocation newTarget);

	/**
	 * Notifies when the {@link ILink#getReports() reports} {@link IReport} is added.
	 * 
	 * @param report
	 *            the added {@link ILink#getReports() reports} {@link IReport}
	 */
	void reportAdded(IReport report);

	/**
	 * Notifies when the {@link ILink#getReports() reports} {@link IReport} is removed.
	 * 
	 * @param report
	 *            the removed {@link ILink#getReports() reports} {@link IReport}
	 */
	void reportRemoved(IReport report);

}
