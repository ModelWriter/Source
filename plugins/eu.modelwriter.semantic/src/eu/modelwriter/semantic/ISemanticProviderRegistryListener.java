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
 * Listen to {@link ISemanticProviderRegistry} changes.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ISemanticProviderRegistryListener {

	/**
	 * Stub implementation.
	 *
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	class Stub implements ISemanticProviderRegistryListener {

		/**
		 * {@inheritDoc}
		 *
		 * @see eu.modelwriter.semantic.ISemanticProviderRegistryListener#providerRegistred(eu.modelwriter.semantic.ISemanticProvider)
		 */
		public void providerRegistred(ISemanticProvider base) {
			// nothing to do here
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see eu.modelwriter.semantic.ISemanticProviderRegistryListener#providerUnregistred(eu.modelwriter.semantic.ISemanticProvider)
		 */
		public void providerUnregistred(ISemanticProvider base) {
			// nothing to do here
		}

	}

	/**
	 * Notification when an {@link ISemanticProvider} is
	 * {@link ISemanticProviderRegistry#register(ISemanticProvider) registered}.
	 * 
	 * @param base
	 *            the registered {@link ISemanticProvider}
	 */
	void providerRegistred(ISemanticProvider base);

	/**
	 * Notification when an {@link ISemanticProvider} is
	 * {@link ISemanticProviderRegistry#unregister(ISemanticProvider) unregistered}.
	 * 
	 * @param base
	 *            the unregistered {@link ISemanticProvider}
	 */
	void providerUnregistred(ISemanticProvider base);

}
