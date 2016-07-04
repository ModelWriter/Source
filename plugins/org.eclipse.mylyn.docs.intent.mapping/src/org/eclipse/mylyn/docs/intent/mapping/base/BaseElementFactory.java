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
package org.eclipse.mylyn.docs.intent.mapping.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates instances of {@link IBaseElement}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class BaseElementFactory {

	/**
	 * Describe creation of an {@link IBaseElement}.
	 * 
	 * @param <T>
	 *            the kind of {@link IBaseElement} described.
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public interface IFactoryDescriptor<T extends IBaseElement> {

		/**
		 * Creates a new instance of {@link IBaseElement}.
		 * 
		 * @return the created instance of {@link IBaseElement}
		 * @throws IllegalAccessException
		 *             if the class or its nullary constructor is not accessible
		 * @throws InstantiationException
		 *             if this Class represents an abstract class, an interface, an array class, a primitive
		 *             type, or void; or if the class has no nullary constructor; or if the instantiation
		 *             fails for some other reason
		 * @throws ClassNotFoundException
		 *             if the {@link Class} can't be found in the {@link ClassLoader}
		 */
		T createElement() throws InstantiationException, IllegalAccessException, ClassNotFoundException;

	}

	/**
	 * Describe creation of an {@link IBaseElement}.
	 * 
	 * @param <T>
	 *            the kind of {@link IBaseElement} described.
	 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
	 */
	public static final class FactoryDescriptor<T extends IBaseElement> implements IFactoryDescriptor<T> {

		/**
		 * The {@link Class}.
		 */
		private final Class<T> clazz;

		/**
		 * Constructor.
		 * 
		 * @param clazz
		 *            the {@link Class}
		 */
		public FactoryDescriptor(Class<T> clazz) {
			this.clazz = clazz;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.mylyn.docs.intent.mapping.base.BaseElementFactory.IFactoryDescriptor#createElement()
		 */
		public T createElement() throws InstantiationException, IllegalAccessException,
				ClassNotFoundException {
			return clazz.newInstance();
		}

	}

	/**
	 * Mapping from interfaces to instance classes.
	 */
	private final Map<Class<? extends IBaseElement>, IFactoryDescriptor<?>> descriptorMapping = new HashMap<Class<? extends IBaseElement>, IFactoryDescriptor<?>>();

	/**
	 * Gets the instance {@link IFactoryDescriptor} of the given interface {@link Class} from the
	 * {@link BaseElementFactory#descriptorMapping mapping}.
	 * 
	 * @param cls
	 *            the interface class
	 * @param <T>
	 *            the {@link IBaseElement} kind
	 * @return the instance {@link Class} of the given interface {@link Class} from the
	 *         {@link BaseElementFactory#descriptorMapping mapping}
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	@SuppressWarnings("unchecked")
	private <T extends IBaseElement> IFactoryDescriptor<T> getDescriptor(Class<T> cls)
			throws ClassNotFoundException {
		return (IFactoryDescriptor<T>)descriptorMapping.get(cls);
	}

	/**
	 * Creates an instance of {@link IBaseElement} for the given interface {@link Class}.
	 * 
	 * @param interfaceCls
	 *            the interface class
	 * @param <T>
	 *            the {@link IBaseElement} kind
	 * @return the created instance of {@link IBaseElement} for the given interface {@link Class} if an
	 *         instance {@link Class} has been {@link BaseElementFactory#addDescriptor(Class, Class) added}
	 *         for the interface {@link Class}, <code>null</code> otherwise
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason
	 * @throws ClassNotFoundException
	 *             if the {@link Class} can't be found
	 */
	public <T extends IBaseElement> T createElement(Class<T> interfaceCls) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		final T res;

		final IFactoryDescriptor<T> descriptor = getDescriptor(interfaceCls);
		if (descriptor != null) {
			res = descriptor.createElement();
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * Add an instance {@link Class} for the given interface {@link Class}.
	 * 
	 * @param interfaceCls
	 *            the interface {@link Class}
	 * @param descriptor
	 *            the {@link LazyFactoryDescriptor}
	 * @param <T>
	 *            the {@link IBaseElement} kind
	 */
	public <T extends IBaseElement> void addDescriptor(Class<T> interfaceCls,
			IFactoryDescriptor<? extends T> descriptor) {
		descriptorMapping.put(interfaceCls, descriptor);
	}

	/**
	 * Removes the instance {@link Class} of the given interface {@link Class}.
	 * 
	 * @param interfaceCls
	 *            the interface {@link Class}
	 */
	public void removeDescriptor(Class<?> interfaceCls) {
		descriptorMapping.remove(interfaceCls);
	}

}
