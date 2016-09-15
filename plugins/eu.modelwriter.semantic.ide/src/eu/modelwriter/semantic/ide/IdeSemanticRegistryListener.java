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

import eu.modelwriter.semantic.ISemanticProvider;
import eu.modelwriter.semantic.ISemanticSimilarityProvider;
import eu.modelwriter.semantic.SemanticUtils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
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
	 * Plugin providing {@link ISemanticProvider} extension point to parse for extensions.
	 */
	public static final String SEMANTIC_PROVIDER_EXTENSION_POINT = "eu.modelwriter.semantic.ide.semanticProvider";

	/**
	 * {@link ISemanticProvider} tag.
	 */
	public static final String SEMANTIC_PROVIDER_TAG_EXTENSION = "provider";

	/**
	 * The {@link ISemanticProvider} extension point base attribute.
	 */
	public static final String SEMANTIC_PROVIDER_ATTRIBUTE_CLASS = "class";

	/**
	 * Plugin providing {@link ISemanticProvider} extension point to parse for extensions.
	 */
	public static final String SEMANTIC_SIMILARITY_PROVIDER_EXTENSION_POINT = "eu.modelwriter.semantic.ide.semanticSimilarityProvider";

	/**
	 * {@link ISemanticSimilarityProvider} tag.
	 */
	public static final String SEMANTIC_SIMILARITY_PROVIDER_TAG_EXTENSION = "provider";

	/**
	 * The {@link ISemanticSimilarityProvider} extension point base attribute.
	 */
	public static final String SEMANTIC_SIMILARITY_PROVIDER_ATTRIBUTE_CLASS = "class";

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
			if (SEMANTIC_PROVIDER_EXTENSION_POINT.equals(extension.getUniqueIdentifier())) {
				parseSemanticProviderExtension(extension);
			}
			if (SEMANTIC_SIMILARITY_PROVIDER_EXTENSION_POINT.equals(extension.getUniqueIdentifier())) {
				parseSemanticSimilarityProviderExtension(extension);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IRegistryEventListener#removed(org.eclipse.core.runtime.IExtension[])
	 */
	public void removed(IExtension[] extensions) {
		for (IExtension extension : extensions) {
			final IConfigurationElement[] configElements = extension.getConfigurationElements();
			for (IConfigurationElement elem : configElements) {
				if (SEMANTIC_PROVIDER_TAG_EXTENSION.equals(elem.getName())) {
					final String delegateClassName = elem.getAttribute(SEMANTIC_PROVIDER_ATTRIBUTE_CLASS);
					for (ISemanticProvider provider : SemanticUtils.getSemanticProviderRegistry()
							.getProviders()) {
						if (delegateClassName.equals(provider.getClass().getName())) {
							SemanticUtils.getSemanticProviderRegistry().unregister(provider);
							break;
						}
					}
				} else if (SEMANTIC_SIMILARITY_PROVIDER_TAG_EXTENSION.equals(elem.getName())) {
					final String delegateClassName = elem
							.getAttribute(SEMANTIC_SIMILARITY_PROVIDER_ATTRIBUTE_CLASS);
					for (ISemanticSimilarityProvider provider : SemanticUtils
							.getSemanticSimilarityProviderRegistry().getProviders()) {
						if (delegateClassName.equals(provider.getClass().getName())) {
							SemanticUtils.getSemanticSimilarityProviderRegistry().unregister(provider);
							break;
						}
					}
				}
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
		for (IExtension extension : registry.getExtensionPoint(SEMANTIC_PROVIDER_EXTENSION_POINT)
				.getExtensions()) {
			parseSemanticProviderExtension(extension);
		}
		for (IExtension extension : registry.getExtensionPoint(SEMANTIC_SIMILARITY_PROVIDER_EXTENSION_POINT)
				.getExtensions()) {
			parseSemanticSimilarityProviderExtension(extension);
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
			if (bundle.getState() != Bundle.ACTIVE) {
				bundle.start();
			}
		} catch (BundleException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}

	/**
	 * Parses a single {@link ISemanticProvider} extension contribution.
	 * 
	 * @param extension
	 *            Parses the given extension and adds its contribution to the registry.
	 */
	private void parseSemanticProviderExtension(IExtension extension) {
		final IConfigurationElement[] configElements = extension.getConfigurationElements();
		for (IConfigurationElement elem : configElements) {
			if (SEMANTIC_PROVIDER_TAG_EXTENSION.equals(elem.getName())) {
				try {
					final ISemanticProvider provider = (ISemanticProvider)elem
							.createExecutableExtension(SEMANTIC_PROVIDER_ATTRIBUTE_CLASS);
					SemanticUtils.getSemanticProviderRegistry().register(provider);
				} catch (CoreException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				}
			}
		}
	}

	/**
	 * Parses a single {@link ISemanticSimilarityProvider} extension contribution.
	 * 
	 * @param extension
	 *            Parses the given extension and adds its contribution to the registry.
	 */
	private void parseSemanticSimilarityProviderExtension(IExtension extension) {
		final IConfigurationElement[] configElements = extension.getConfigurationElements();
		for (IConfigurationElement elem : configElements) {
			if (SEMANTIC_SIMILARITY_PROVIDER_TAG_EXTENSION.equals(elem.getName())) {
				try {
					final ISemanticSimilarityProvider provider = (ISemanticSimilarityProvider)elem
							.createExecutableExtension(SEMANTIC_SIMILARITY_PROVIDER_ATTRIBUTE_CLASS);
					SemanticUtils.getSemanticSimilarityProviderRegistry().register(provider);
				} catch (CoreException e) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				}
			}
		}
	}

}
