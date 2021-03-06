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

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class Activator extends Plugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "eu.modelwriter.semantic.ide";

	/** This plug-in's shared instance. */
	private static Activator plugin;

	/** The registry listener that will be used to listen to extension changes. */
	private IdeSemanticRegistryListener registryListener = new IdeSemanticRegistryListener();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addListener(registryListener, IdeSemanticRegistryListener.BASE_PROVIDER_EXTENSION_POINT);
		registry.addListener(registryListener, IdeSemanticRegistryListener.SEMANTIC_PROVIDER_EXTENSION_POINT);
		registry.addListener(registryListener,
				IdeSemanticRegistryListener.SEMANTIC_SIMILARITY_PROVIDER_EXTENSION_POINT);
		registryListener.parseInitialContributions();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;

		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.removeListener(registryListener);
		// TODO clear registry ?
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
