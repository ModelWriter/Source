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
package org.eclipse.mylyn.docs.intent.mapping.ide;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.ContainerProviderFactory;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.eclipse.mylyn.docs.intent.mapping.connector.IConnector;
import org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider;
import org.eclipse.mylyn.docs.intent.mapping.ide.adapter.IMarkerToLocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.adapter.MarkerToLocationDescriptorAdapterFactory;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileConnectorDelegate;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * This listener will allow us to be aware of contribution changes against the {@link ILocation} and
 * {@link IFileConnectorDelegate} extension points.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeMappingRegistryListener implements IRegistryEventListener {

	/**
	 * The class constant.
	 */
	public static final String CLASS = "class";

	/**
	 * {@link ILocation} extension point to parse for extensions.
	 */
	public static final String LOCATION_EXTENSION_POINT = "org.eclipse.mylyn.docs.intent.mapping.ide.location";

	/**
	 * {@link ILocation} tag.
	 */
	public static final String BASE_TAG_EXTENSION = "base";

	/**
	 * The {@link ILocation} extension point base attribute.
	 */
	public static final String BASE_ATTRIBUTE_CLASS = CLASS;

	/**
	 * {@link ILocation} tag.
	 */
	public static final String LOCATION_TAG_EXTENSION = "location";

	/**
	 * {@link ILocation} tag.
	 */
	public static final String LOCATION_ATTRIBUTE_INTERFACE = "interface";

	/**
	 * {@link ILocation} tag.
	 */
	public static final String LOCATION_ATTRIBUTE_IMPLEMENTATION = "implementation";

	/**
	 * {@link IFileConnectorDelegate} extension point to parse for extensions.
	 */
	public static final String FILE_CONNECTOR_DELEGATE_EXTENSION_POINT = "org.eclipse.mylyn.docs.intent.mapping.ide.fileConnectorDelegate";

	/**
	 * {@link IFileConnectorDelegate} tag.
	 */
	public static final String FILE_CONNECTOR_DELEGATE_TAG_EXTENSION = "fileConnectorDelegate";

	/**
	 * The {@link ILocation} extension point base attribute.
	 */
	public static final String FILE_CONNECTOR_DELEGATE_ATTRIBUTE_CLASS = CLASS;

	/**
	 * Plugin providing {@link IBase} extension point to parse for extensions.
	 */
	public static final String BASE_PROVIDER_EXTENSION_POINT = "org.eclipse.mylyn.docs.intent.mapping.ide.baseProvider";

	/**
	 * Plugin providing {@link IConnector} extension point to parse for extensions.
	 */
	public static final String CONNECTOR_EXTENSION_POINT = "org.eclipse.mylyn.docs.intent.mapping.ide.connector";

	/**
	 * {@link IConnector} tag.
	 */
	public static final String CONNECTOR_TAG_EXTENSION = "connector";

	/**
	 * The {@link ILocation} extension point connector attribute.
	 */
	public static final String CONNECTOR_ATTRIBUTE_CLASS = CLASS;

	/**
	 * Plugin providing {@link IMarkerToLocationDescriptor} extension point to parse for extensions.
	 */
	public static final String MARKER_TO_LOCATION_EXTENSION_POINT = "org.eclipse.mylyn.docs.intent.mapping.ide.markerToLocation";

	/**
	 * {@link IMarkerToLocationDescriptor} tag.
	 */
	public static final String MARKER_TO_LOCATION_TAG_EXTENSION = "markerToLocation";

	/**
	 * The {@link IMarkerToLocationDescriptor} extension point adapter attribute.
	 */
	public static final String MARKER_TO_LOCATION_ATTRIBUTE_CLASS = CLASS;

	/**
	 * The {@link IMarkerToLocationDescriptor} extension point adapter attribute.
	 */
	public static final String MARKER_TO_LOCATION_ATTRIBUTE_MARKER_TYPE = "markerType";

	/**
	 * {@link IContainerProvider} extension point to parse for extensions.
	 */
	public static final String CONTAINER_PROVIDER_EXTENSION_POINT = "org.eclipse.mylyn.docs.intent.mapping.ide.containerProvider";

	/**
	 * {@link IContainerProvider} tag.
	 */
	public static final String CONTAINER_PROVIDER_TAG_EXTENSION = "provider";

	/**
	 * The {@link IContainerProvider} extension point base attribute.
	 */
	public static final String CONTAINER_PROVIDER_ATTRIBUTE_CLASS = CLASS;

	/**
	 * An {@link BaseElementFactory.FactoryDescriptor} for an extension point.
	 * 
	 * @param <T>
	 *            the kind of {@link ILocation}
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class ExtensionFactoryDescriptor<T extends ILocation> implements BaseElementFactory.IFactoryDescriptor<T> {

		/**
		 * The {@link IConfigurationElement}.
		 */
		private final IConfigurationElement element;

		/**
		 * Constructor.
		 * 
		 * @param element
		 *            the {@link IConfigurationElement}
		 */
		public ExtensionFactoryDescriptor(IConfigurationElement element) {
			this.element = element;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.IFactoryDescriptor#createElement()
		 */
		@SuppressWarnings("unchecked")
		public T createElement() throws InstantiationException, IllegalAccessException,
				ClassNotFoundException {
			try {
				return (T)element.createExecutableExtension(LOCATION_ATTRIBUTE_IMPLEMENTATION);
			} catch (CoreException e) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e
						.getMessage(), e));
			}
			return null;
		}

	}

	/**
	 * An {@link ContainerProviderFactory.FactoryDescriptor} for an extension point.
	 * 
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class ExtensionContainerProviderFactoryDescriptor implements ContainerProviderFactory.IFactoryDescriptor {

		/**
		 * The {@link IConfigurationElement}.
		 */
		private final IConfigurationElement element;

		/**
		 * Constructor.
		 * 
		 * @param element
		 *            the {@link IConfigurationElement}
		 */
		public ExtensionContainerProviderFactoryDescriptor(IConfigurationElement element) {
			this.element = element;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.IFactoryDescriptor#createElement()
		 */
		public IContainerProvider createElement() throws InstantiationException, IllegalAccessException,
				ClassNotFoundException {
			try {
				return (IContainerProvider)element.createExecutableExtension(
						CONTAINER_PROVIDER_ATTRIBUTE_CLASS);
			} catch (CoreException e) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e
						.getMessage(), e));
			}
			return null;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ContainerProviderFactory.IFactoryDescriptor#getClassName()
		 */
		public String getClassName() {
			return element.getAttribute(CONTAINER_PROVIDER_ATTRIBUTE_CLASS);
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IRegistryEventListener#added(org.eclipse.core.runtime.IExtension[])
	 */
	public void added(IExtension[] extensions) {
		for (IExtension extension : extensions) {
			if (LOCATION_EXTENSION_POINT.equals(extension.getUniqueIdentifier())) {
				parseLocationExtension(extension);
			} else if (FILE_CONNECTOR_DELEGATE_EXTENSION_POINT.equals(extension.getUniqueIdentifier())) {
				parseFileConnectorDelegateExtension(extension);
			} else if (BASE_PROVIDER_EXTENSION_POINT.equals(extension.getUniqueIdentifier())) {
				parseBaseProviderExtension(extension);
			} else if (CONNECTOR_EXTENSION_POINT.equals(extension.getUniqueIdentifier())) {
				parseConnectorProviderExtension(extension);
			} else if (MARKER_TO_LOCATION_EXTENSION_POINT.equals(extension.getUniqueIdentifier())) {
				parseMarkerToLocationExtension(extension);
			} else if (CONTAINER_PROVIDER_EXTENSION_POINT.equals(extension.getUniqueIdentifier())) {
				parseContainerProviderExtension(extension);
			}
		}
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
	 * Though this listener reacts to the extension point changes, there could have been contributions before
	 * it's been registered. This will parse these initial contributions.
	 */
	public void parseInitialContributions() {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		for (IExtension extension : registry.getExtensionPoint(LOCATION_EXTENSION_POINT).getExtensions()) {
			parseLocationExtension(extension);
		}
		for (IExtension extension : registry.getExtensionPoint(FILE_CONNECTOR_DELEGATE_EXTENSION_POINT)
				.getExtensions()) {
			parseFileConnectorDelegateExtension(extension);
		}
		for (IExtension extension : registry.getExtensionPoint(BASE_PROVIDER_EXTENSION_POINT)
				.getExtensions()) {
			parseBaseProviderExtension(extension);
		}
		for (IExtension extension : registry.getExtensionPoint(CONNECTOR_EXTENSION_POINT).getExtensions()) {
			parseConnectorProviderExtension(extension);
		}
		for (IExtension extension : registry.getExtensionPoint(MARKER_TO_LOCATION_EXTENSION_POINT)
				.getExtensions()) {
			parseMarkerToLocationExtension(extension);
		}
		for (IExtension extension : registry.getExtensionPoint(CONTAINER_PROVIDER_EXTENSION_POINT)
				.getExtensions()) {
			parseContainerProviderExtension(extension);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IRegistryEventListener#removed(org.eclipse.core.runtime.IExtension[])
	 */
	@SuppressWarnings("unchecked")
	public void removed(IExtension[] extensions) {
		for (IExtension extension : extensions) {
			final IConfigurationElement[] configElements = extension.getConfigurationElements();
			for (IConfigurationElement elem : configElements) {
				if (BASE_TAG_EXTENSION.equals(elem.getName())) {
					final Bundle contributor = Platform.getBundle(elem.getContributor().getName());
					final String baseClassName = elem.getAttribute(BASE_ATTRIBUTE_CLASS);
					try {
						Class<? extends IBase> baseClass = (Class<? extends IBase>)contributor.loadClass(
								baseClassName);
						for (IConfigurationElement locationElem : elem.getChildren(LOCATION_TAG_EXTENSION)) {
							final String interfaceClassName = locationElem.getAttribute(
									LOCATION_ATTRIBUTE_INTERFACE);
							Class<ILocation> interfaceClass = (Class<ILocation>)contributor.loadClass(
									interfaceClassName);
							MappingUtils.unregisterLocationImplementation(baseClass, interfaceClass);
						}
					} catch (ClassNotFoundException e) {
						Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e
								.getMessage(), e));
					}
				} else if (FILE_CONNECTOR_DELEGATE_TAG_EXTENSION.equals(elem.getName())) {
					final String delegateClassName = elem.getAttribute(
							FILE_CONNECTOR_DELEGATE_ATTRIBUTE_CLASS);
					IFileConnectorDelegate delegateToRemove = null;
					for (IFileConnectorDelegate delegate : IdeMappingUtils.getFileConectorDelegateRegistry()
							.getConnectorDelegates()) {
						if (delegateClassName.equals(delegate.getClass().getName())) {
							delegateToRemove = delegate;
							break;
						}
					}
					if (delegateToRemove != null) {
						IdeMappingUtils.getFileConectorDelegateRegistry().unregister(delegateToRemove);
					}
				} else if (CONTAINER_PROVIDER_TAG_EXTENSION.equals(elem.getName())) {
					MappingUtils.getContainerProviderFactory().removeDescriptor(elem.getAttribute(
							CONTAINER_PROVIDER_ATTRIBUTE_CLASS));
				}
			}
		}
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
	 * Parses a single {@link ILocation} extension contribution.
	 * 
	 * @param extension
	 *            Parses the given extension and adds its contribution to the registry
	 */
	@SuppressWarnings("unchecked")
	private void parseLocationExtension(IExtension extension) {
		final IConfigurationElement[] configElements = extension.getConfigurationElements();
		for (IConfigurationElement elem : configElements) {
			if (BASE_TAG_EXTENSION.equals(elem.getName())) {
				final Bundle contributor = Platform.getBundle(elem.getContributor().getName());
				final String baseClassName = elem.getAttribute(BASE_ATTRIBUTE_CLASS);
				try {
					Class<? extends IBase> baseClass = (Class<? extends IBase>)contributor.loadClass(
							baseClassName);
					for (IConfigurationElement locationElem : elem.getChildren(LOCATION_TAG_EXTENSION)) {
						final String interfaceClassName = locationElem.getAttribute(
								LOCATION_ATTRIBUTE_INTERFACE);
						Class<ILocation> interfaceClass = (Class<ILocation>)contributor.loadClass(
								interfaceClassName);
						MappingUtils.registerLocationImplementation(baseClass, interfaceClass,
								new ExtensionFactoryDescriptor<ILocation>(locationElem));
					}
				} catch (ClassNotFoundException e) {
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e
							.getMessage(), e));
				}
			}
		}
	}

	/**
	 * Parses a single {@link IFileConnectorDelegate} extension contribution.
	 * 
	 * @param extension
	 *            Parses the given extension and adds its contribution to the registry
	 */
	private void parseFileConnectorDelegateExtension(IExtension extension) {
		final IConfigurationElement[] configElements = extension.getConfigurationElements();
		for (IConfigurationElement elem : configElements) {
			if (FILE_CONNECTOR_DELEGATE_TAG_EXTENSION.equals(elem.getName())) {
				try {
					final IFileConnectorDelegate delegate = (IFileConnectorDelegate)elem
							.createExecutableExtension(FILE_CONNECTOR_DELEGATE_ATTRIBUTE_CLASS);
					IdeMappingUtils.getFileConectorDelegateRegistry().register(delegate);
				} catch (CoreException e) {
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e
							.getMessage(), e));
				}
			}
		}
	}

	/**
	 * Parses a single {@link IBase} extension contribution.
	 * 
	 * @param extension
	 *            Parses the given extension and adds its contribution to the registry.
	 */
	private void parseBaseProviderExtension(IExtension extension) {
		final String bundleName = extension.getContributor().getName();
		final Bundle bundle = Platform.getBundle(bundleName);
		try {
			if (bundle.getState() != Bundle.ACTIVE) {
				bundle.start();
			}
		} catch (BundleException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(),
					e));
		}
	}

	/**
	 * Parses a single {@link IConnector} extension contribution.
	 * 
	 * @param extension
	 *            Parses the given extension and adds its contribution to the registry
	 */
	private void parseConnectorProviderExtension(IExtension extension) {
		final IConfigurationElement[] configElements = extension.getConfigurationElements();
		for (IConfigurationElement elem : configElements) {
			if (CONNECTOR_TAG_EXTENSION.equals(elem.getName())) {
				try {
					final IConnector connector = (IConnector)elem.createExecutableExtension(
							CONNECTOR_ATTRIBUTE_CLASS);
					MappingUtils.getConnectorRegistry().register(connector);
				} catch (CoreException e) {
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e
							.getMessage(), e));
				}
			}
		}
	}

	/**
	 * Parses a single {@link IMarkerToLocationDescriptor} extension contribution.
	 * 
	 * @param extension
	 *            Parses the given extension and adds its contribution to the registry
	 */
	private void parseMarkerToLocationExtension(IExtension extension) {
		final IConfigurationElement[] configElements = extension.getConfigurationElements();
		for (IConfigurationElement elem : configElements) {
			if (MARKER_TO_LOCATION_TAG_EXTENSION.equals(elem.getName())) {
				try {
					final String marterType = elem.getAttribute(MARKER_TO_LOCATION_ATTRIBUTE_MARKER_TYPE);
					final IMarkerToLocationDescriptor adapter = (IMarkerToLocationDescriptor)elem
							.createExecutableExtension(MARKER_TO_LOCATION_ATTRIBUTE_CLASS);
					MarkerToLocationDescriptorAdapterFactory.register(adapter, marterType);
				} catch (CoreException e) {
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e
							.getMessage(), e));
				}
			}
		}
	}

	/**
	 * Parses a single {@link IContainerProvider} extension contribution.
	 * 
	 * @param extension
	 *            Parses the given extension and adds its contribution to the registry
	 */
	private void parseContainerProviderExtension(IExtension extension) {
		final IConfigurationElement[] configElements = extension.getConfigurationElements();
		for (IConfigurationElement elem : configElements) {
			if (CONTAINER_PROVIDER_TAG_EXTENSION.equals(elem.getName())) {
				MappingUtils.getContainerProviderFactory().addDescriptor(
						new ExtensionContainerProviderFactoryDescriptor(elem));
			}
		}
	}

}
