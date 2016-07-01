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
 * Listen to {@link IReport} changes.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IReportListener {

	/**
	 * Stub implementation.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class Stub implements IReportListener {

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IReportListener#descriptionChanged(java.lang.String,
		 *      java.lang.String)
		 */
		public void descriptionChanged(String oldDescription, String newDescription) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IReportListener#linkChanged(org.eclipse.mylyn.docs.intent.mapping.base.ILink,
		 *      org.eclipse.mylyn.docs.intent.mapping.base.ILink)
		 */
		public void linkChanged(ILink oldLink, ILink newLink) {
			// nothing to do here
		}
	}

	/**
	 * Notifies when the {@link IReport#getDescription() description} is changed.
	 * 
	 * @param oldDescription
	 *            the old {@link IReport#getDescription() description}
	 * @param newDescription
	 *            the new {@link IReport#getDescription() description}
	 */
	void descriptionChanged(String oldDescription, String newDescription);

	/**
	 * Notifies when the {@link IReport#getLink() link} is changed.
	 * 
	 * @param oldLink
	 *            the old {@link IReport#getLink() link}
	 * @param newLink
	 *            the new {@link IReport#getLink() link}
	 */
	void linkChanged(ILink oldLink, ILink newLink);

}
