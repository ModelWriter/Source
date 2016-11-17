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
package org.eclipse.mylyn.docs.intent.mapping.jena.ide;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.ResourceLocationListener;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class Activator extends Plugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.mylyn.docs.intent.mapping.jena.ide";

	/** This plug-in's shared instance. */
	private static Activator plugin;

	/**
	 * The {@link ResourceLocationListener}.
	 */
	private ResourceLocationListener resourceLocationListener;

	/**
	 * Gets the {@link ResourceLocationListener}.
	 * 
	 * @param resourceLocationListener
	 *            the {@link ResourceLocationListener}
	 */
	public void setResourceLocationListener(ResourceLocationListener resourceLocationListener) {
		if (this.resourceLocationListener != null) {
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(this.resourceLocationListener);
		}
		this.resourceLocationListener = resourceLocationListener;
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceLocationListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
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
