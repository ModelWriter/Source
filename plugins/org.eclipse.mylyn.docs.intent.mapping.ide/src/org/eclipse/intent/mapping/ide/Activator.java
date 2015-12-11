package org.eclipse.intent.mapping.ide;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.intent.mapping.ide.connector.TextFileConnectorDelegate;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class Activator extends Plugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.intent.mapping.ide";

	/** This plug-in's shared instance. */
	private static Activator plugin;

	/** The registry listener that will be used to listen to extension changes. */
	private IdeMappingRegistryListener registryListener = new IdeMappingRegistryListener();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		IdeMappingUtils.getFileConectorDelegateRegistry().register(new TextFileConnectorDelegate());

		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addListener(registryListener, IdeMappingRegistryListener.LOCATION_EXTENSION_POINT);
		registry.addListener(registryListener,
				IdeMappingRegistryListener.FILE_CONNECTOR_DELEGATE_EXTENSION_POINT);
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