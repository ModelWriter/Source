/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.base;

/**
 * Listen to {@link IBase} changes.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IBaseListener extends ILocationContainerListener {

	/**
	 * Stub implementation.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class Stub extends ILocationContainerListener.Stub implements IBaseListener {

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener#nameChanged(java.lang.String,
		 *      java.lang.String)
		 */
		public void nameChanged(String oldName, String newName) {
			// nothing to do here

		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener#reportAdded(org.eclipse.mylyn.docs.intent.mapping.base.IReport)
		 */
		public void reportAdded(IReport report) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.IBaseListener#reportRemoved(org.eclipse.mylyn.docs.intent.mapping.base.IReport)
		 */
		public void reportRemoved(IReport report) {
			// nothing to do here
		}

	}

	/**
	 * Notifies when the {@link IBase#getName() name} has been changed.
	 * 
	 * @param oldName
	 *            the old {@link IBase#getName() name}
	 * @param newName
	 *            the new {@link IBase#getName() name}
	 */
	void nameChanged(String oldName, String newName);

	/**
	 * Notifies when the {@link IBase#getReports() reports} {@link IReport} is added.
	 * 
	 * @param report
	 *            the added {@link IBase#getReports() reports} {@link IReport}
	 */
	void reportAdded(IReport report);

	/**
	 * Notifies when the {@link IBase#getReports() reports} {@link IReport} is removed.
	 * 
	 * @param report
	 *            the removed {@link IBase#getReports() reports} {@link IReport}
	 */
	void reportRemoved(IReport report);

}
