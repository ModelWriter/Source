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
package eu.modelwriter.semantic.ide;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * This listener will allow us to be aware of contribution changes against the
 * {@link eu.modelwriter.semantic.IBase IBase} extension points.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeSemanticRegistryListener implements IRegistryEventListener {

	/**
	 * Plugin providing {@link eu.modelwriter.semantic.IBase IBase} extension point to parse for extensions.
	 */
	public static final String BASE_PROVIDER_EXTENSION_POINT = "eu.modelwriter.semantic.ide.baseProvider";

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IRegistryEventListener#added(org.eclipse.core.runtime.IExtension[])
	 */
	public void added(IExtension[] extensions) {
		for (IExtension extension : extensions) {
			if (BASE_PROVIDER_EXTENSION_POINT.equals(extension.getUniqueIdentifier())) {
				parseBaseProviderExtension(extension);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IRegistryEventListener#removed(org.eclipse.core.runtime.IExtension[])
	 */
	public void removed(IExtension[] extensions) {
		// nothing to do here
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IRegistryEventListener#added(org.eclipse.core.runtime.IExtensionPoint[])
	 */
	public void added(IExtensionPoint[] extensionPoints) {
		// no need to listen to this event
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IRegistryEventListener#removed(org.eclipse.core.runtime.IExtensionPoint[])
	 */
	public void removed(IExtensionPoint[] extensionPoints) {
		// no need to listen to this event
	}

	/**
	 * Though this listener reacts to the extension point changes, there could have been contributions before
	 * it's been registered. This will parse these initial contributions.
	 */
	public void parseInitialContributions() {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		for (IExtension extension : registry.getExtensionPoint(BASE_PROVIDER_EXTENSION_POINT).getExtensions()) {
			parseBaseProviderExtension(extension);
		}
	}

	/**
	 * Parses a single {@link eu.modelwriter.semantic.IBase IBase} extension contribution.
	 * 
	 * @param extension
	 *            Parses the given extension and adds its contribution to the registry.
	 */
	private void parseBaseProviderExtension(IExtension extension) {
		final String bundleName = extension.getContributor().getName();
		final Bundle bundle = Platform.getBundle(bundleName);
		try {
			if (bundle.getState() != Bundle.STARTING && bundle.getState() != Bundle.ACTIVE) {
				bundle.start();
			}
		} catch (BundleException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}

}
