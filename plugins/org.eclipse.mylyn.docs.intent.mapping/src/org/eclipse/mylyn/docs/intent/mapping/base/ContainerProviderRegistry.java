/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.mapping.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry;
import org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider;
import org.eclipse.mylyn.docs.intent.mapping.internal.connector.ConnectorRegistry;
import org.eclipse.mylyn.docs.intent.mapping.text.TextRegionContainerProvider;

/**
 * The default implementation of {@link IContainerProviderRegistry}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ContainerProviderRegistry implements IContainerProviderRegistry {

	/**
	 * The {@link List} of {@link ConnectorRegistry#register(IContainerProvider) registered}
	 * {@link IContainerProvider}.
	 */
	private final List<IContainerProvider> providers = Collections.synchronizedList(
			new ArrayList<IContainerProvider>());

	/**
	 * Mapping of provider name to instance.
	 */
	private final Map<String, IContainerProvider> nameToProvider = new HashMap<String, IContainerProvider>();

	/**
	 * The {@link IBase} we are attached to.
	 */
	private final IBase base;

	/**
	 * Constructor.
	 * 
	 * @param base
	 *            the {@link IBase} to attach to
	 */
	public ContainerProviderRegistry(IBase base) {
		this.base = base;
		synchronized(providers) {
			providers.add(new TextRegionContainerProvider());
		}
		for (String providerName : base.getContainerProviders()) {
			try {
				register(MappingUtils.getContainerProviderFactory().createProvider(providerName));
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		base.addListener(new IBaseListener.Stub() {
			@Override
			public void containerProviderAdded(String provider) {
				try {
					register(MappingUtils.getContainerProviderFactory().createProvider(provider));
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void containerProviderRemoved(String provider) {
				final IContainerProvider providerInstance = nameToProvider.get(provider);
				if (providerInstance != null) {
					unregister(providerInstance);
				}
			}
		});
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#register(org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider)
	 */
	public void register(IContainerProvider provider) {
		if (provider != null) {
			synchronized(providers) {
				if (!nameToProvider.containsKey(provider.getClass().getCanonicalName())) {
					providers.add(provider);
					nameToProvider.put(provider.getClass().getCanonicalName(), provider);
					base.getContainerProviders().add(provider.getClass().getCanonicalName());
				}
			}
		} else {
			throw new IllegalArgumentException("IContainerProvider can't be null.");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#unregister(org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider)
	 */
	public void unregister(IContainerProvider provider) {
		synchronized(providers) {
			final IContainerProvider providerInstance = nameToProvider.remove(provider.getClass()
					.getCanonicalName());
			if (providerInstance != null) {
				providers.remove(provider);
				base.getContainerProviders().remove(provider.getClass().getCanonicalName());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IConnectorRegistry#getContainer(java.lang.Object)
	 */
	public Object getContainer(Object element) {
		for (IContainerProvider provider : getContainerProviders()) {
			final Object container = provider.getContainer(element);
			if (container != null) {
				return container;
			}
		}

		return null;
	}

	/**
	 * Gets the {@link List} of {@link IConnectorRegistry#register(IContainerProvider) registered}
	 * {@link IContainerProvider}.
	 * 
	 * @return the {@link List} of {@link IConnectorRegistry#register(IContainerProvider) registered}
	 *         {@link IContainerProvider}
	 */
	protected List<IContainerProvider> getContainerProviders() {
		synchronized(providers) {
			return Collections.unmodifiableList(new ArrayList<IContainerProvider>(providers));
		}
	}

}
