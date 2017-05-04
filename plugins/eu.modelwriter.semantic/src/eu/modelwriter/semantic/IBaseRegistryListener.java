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
package eu.modelwriter.semantic;

/**
 * Listen to {@link IBaseRegistry} changes.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IBaseRegistryListener {

	/**
	 * Stub implementation.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	class Stub implements IBaseRegistryListener {

		/**
		 * {@inheritDoc}
		 *
		 * @see eu.modelwriter.semantic.IBaseRegistryListener#baseRegistred(eu.modelwriter.semantic.IBase)
		 */
		public void baseRegistred(IBase base) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see eu.modelwriter.semantic.IBaseRegistryListener#baseUnregistred(eu.modelwriter.semantic.IBase)
		 */
		public void baseUnregistred(IBase base) {
			// nothing to do here
		}

	}

	/**
	 * Notification when an {@link IBase} is {@link IBaseRegistry#register(IBase) registered}.
	 * 
	 * @param base
	 *            the registered {@link IBase}
	 */
	void baseRegistred(IBase base);

	/**
	 * Notification when an {@link IBase} is {@link IBaseRegistry#unregister(IBase) unregistered}.
	 * 
	 * @param base
	 *            the unregistered {@link IBase}
	 */
	void baseUnregistred(IBase base);

}
