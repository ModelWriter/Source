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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider;

/**
 * Creates instances of {@link IContainerProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ContainerProviderFactory {

	/**
	 * Describe creation of an {@link IBaseElement}.
	 * 
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public interface IFactoryDescriptor {

		/**
		 * Gets the {@link IContainerProvider} {@link Class#getCanonicalName() class name}.
		 * 
		 * @return the {@link IContainerProvider} {@link Class#getCanonicalName() class name}
		 */
		String getClassName();

		/**
		 * Creates a new instance of {@link IContainerProvider}.
		 * 
		 * @return the created instance of {@link IContainerProvider}
		 * @throws IllegalAccessException
		 *             if the class or its nullary constructor is not accessible
		 * @throws InstantiationException
		 *             if this Class represents an abstract class, an interface, an array class, a primitive
		 *             type, or void; or if the class has no nullary constructor; or if the instantiation
		 *             fails for some other reason
		 * @throws ClassNotFoundException
		 *             if the {@link Class} can't be found in the {@link ClassLoader}
		 */
		IContainerProvider createElement() throws InstantiationException, IllegalAccessException,
				ClassNotFoundException;
	}

	/**
	 * Describe creation of an {@link IContainerProvider}.
	 * 
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static final class FactoryDescriptor implements IFactoryDescriptor {

		/**
		 * The {@link Class}.
		 */
		private final Class<? extends IContainerProvider> clazz;

		/**
		 * Constructor.
		 * 
		 * @param clazz
		 *            the {@link Class}
		 */
		public FactoryDescriptor(Class<? extends IContainerProvider> clazz) {
			this.clazz = clazz;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ContainerProviderFactory.IFactoryDescriptor#getClassName()
		 */
		public String getClassName() {
			return clazz.getCanonicalName();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.ContainerProviderFactory.IFactoryDescriptor#createElement()
		 */
		public IContainerProvider createElement() throws InstantiationException, IllegalAccessException,
				ClassNotFoundException {
			return clazz.newInstance();
		}

	}

	/**
	 * Mapping from {@link IFactoryDescriptor#getClassName() class name} to {@link IFactoryDescriptor}.
	 */
	private final Map<String, IFactoryDescriptor> descriptorMapping = new LinkedHashMap<String, IFactoryDescriptor>();

	/**
	 * Gets the instance {@link IFactoryDescriptor} for the given {@link Class#getCanonicalName() class name}
	 * from the {@link ContainerProviderFactory#descriptorMapping mapping}.
	 * 
	 * @param className
	 *            the {@link Class#getCanonicalName() class name}
	 * @return the instance {@link Class} of the given interface {@link Class} from the
	 *         {@link ContainerProviderFactory#descriptorMapping mapping}
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	private IFactoryDescriptor getDescriptor(String className) throws ClassNotFoundException {
		return descriptorMapping.get(className);
	}

	/**
	 * Creates an instance of {@link IContainerProvider} for the given interface
	 * {@link Class#getCanonicalName() class name}.
	 * 
	 * @param className
	 *            the {@link Class#getCanonicalName() class name}
	 * @return the created instance of {@link IContainerProvider} if the {@link Class#getCanonicalName() class
	 *         name} has been {@link ContainerProviderFactory#addDescriptor(IFactoryDescriptor) added} for the
	 *         {@link Class#getCanonicalName() class name}, <code>null</code> otherwise
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	public IContainerProvider createProvider(String className) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		final IContainerProvider res;

		final IFactoryDescriptor descriptor = getDescriptor(className);
		if (descriptor != null) {
			res = descriptor.createElement();
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * Add the given {@link IFactoryDescriptor}.
	 * 
	 * @param descriptor
	 *            the {@link IFactoryDescriptor}
	 */
	public void addDescriptor(IFactoryDescriptor descriptor) {
		descriptorMapping.put(descriptor.getClassName(), descriptor);
	}

	/**
	 * Removes the {@link IFactoryDescriptor} for the given {@link Class#getCanonicalName() class name}.
	 * 
	 * @param className
	 *            the {@link Class#getCanonicalName() class name}
	 */
	public void removeDescriptor(String className) {
		descriptorMapping.remove(className);
	}

	public List<String> getProviderNames() {
		return new ArrayList<String>(descriptorMapping.keySet());
	}

}
