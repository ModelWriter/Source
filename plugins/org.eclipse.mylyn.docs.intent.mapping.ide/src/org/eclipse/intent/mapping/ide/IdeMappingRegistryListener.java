package org.eclipse.intent.mapping.ide;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.intent.mapping.ide.connector.IFileConnectorDelegate;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.IFactoryDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.base.IBase;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocation;
import org.osgi.framework.Bundle;

/**
 * This listener will allow us to be aware of contribution changes against the {@link ILocation} and
 * {@link IFileConnectorDelegate} extension points.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeMappingRegistryListener implements IRegistryEventListener {

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
	public static final String BASE_ATTRIBUTE_CLASS = "class";

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
	public static final String FILE_CONNECTOR_DELEGATE_ATTRIBUTE_CLASS = "class";

	/**
	 * An {@link IFactoryDescriptor} for an extension point.
	 * 
	 * @param <T>
	 *            the kind of {@link ILocation}
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static class ExtensionFactoryDescriptor<T extends ILocation> implements IFactoryDescriptor<T> {

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
				Activator.getDefault().getLog().log(
						new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
			}
			return null;
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
						Class<? extends IBase> baseClass = (Class<? extends IBase>)contributor
								.loadClass(baseClassName);
						for (IConfigurationElement locationElem : elem.getChildren(LOCATION_TAG_EXTENSION)) {
							final String interfaceClassName = locationElem
									.getAttribute(LOCATION_ATTRIBUTE_INTERFACE);
							Class<ILocation> interfaceClass = (Class<ILocation>)contributor
									.loadClass(interfaceClassName);
							MappingUtils.unregisterLocationImplementation(baseClass, interfaceClass);
						}
					} catch (ClassNotFoundException e) {
						Activator.getDefault().getLog().log(
								new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
					}
				} else if (FILE_CONNECTOR_DELEGATE_TAG_EXTENSION.equals(elem.getName())) {
					final String delegateClassName = elem
							.getAttribute(FILE_CONNECTOR_DELEGATE_ATTRIBUTE_CLASS);
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
	 *            Parses the given extension and adds its contribution to the registry.
	 */
	@SuppressWarnings("unchecked")
	private void parseLocationExtension(IExtension extension) {
		final IConfigurationElement[] configElements = extension.getConfigurationElements();
		for (IConfigurationElement elem : configElements) {
			if (BASE_TAG_EXTENSION.equals(elem.getName())) {
				final Bundle contributor = Platform.getBundle(elem.getContributor().getName());
				final String baseClassName = elem.getAttribute(BASE_ATTRIBUTE_CLASS);
				try {
					Class<? extends IBase> baseClass = (Class<? extends IBase>)contributor
							.loadClass(baseClassName);
					for (IConfigurationElement locationElem : elem.getChildren(LOCATION_TAG_EXTENSION)) {
						final String interfaceClassName = locationElem
								.getAttribute(LOCATION_ATTRIBUTE_INTERFACE);
						Class<ILocation> interfaceClass = (Class<ILocation>)contributor
								.loadClass(interfaceClassName);
						MappingUtils.registerLocationImplementation(baseClass, interfaceClass,
								new ExtensionFactoryDescriptor<ILocation>(locationElem));
					}
				} catch (ClassNotFoundException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				}
			}
		}
	}

	/**
	 * Parses a single {@link IFileConnectorDelegate} extension contribution.
	 * 
	 * @param extension
	 *            Parses the given extension and adds its contribution to the registry.
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
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				}
			}
		}
	}

}
