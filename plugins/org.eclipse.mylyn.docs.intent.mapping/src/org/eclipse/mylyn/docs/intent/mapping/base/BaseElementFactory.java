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
	 * Mapping from interfaces to instance classes.
	 */
	private final Map<Class<? extends IBaseElement>, Class<? extends IBaseElement>> classMapping = new HashMap<Class<? extends IBaseElement>, Class<? extends IBaseElement>>();

	/**
	 * Gets the instance {@link Class} of the given interface {@link Class} from the
	 * {@link BaseElementFactory#classMapping mapping}.
	 * 
	 * @param cls
	 *            the interface class
	 * @param <L>
	 *            the {@link IBaseElement} kind
	 * @return the instance {@link Class} of the given interface {@link Class} from the
	 *         {@link BaseElementFactory#classMapping mapping}
	 */
	@SuppressWarnings("unchecked")
	private <L extends IBaseElement> Class<L> getInstanceClass(Class<L> cls) {
		return (Class<L>)classMapping.get(cls);
	}

	/**
	 * Creates an instance of {@link IBaseElement} for the given interface {@link Class}.
	 * 
	 * @param interfaceCls
	 *            the interface class
	 * @param <L>
	 *            the {@link IBaseElement} kind
	 * @return the created instance of {@link IBaseElement} for the given interface {@link Class} if an
	 *         instance {@link Class} has been {@link BaseElementFactory#addClassInstance(Class, Class) added}
	 *         for the interface {@link Class}, <code>null</code> otherwise
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an array class, a primitive type,
	 *             or void; or if the class has no nullary constructor; or if the instantiation fails for some
	 *             other reason.
	 */
	public <L extends IBaseElement> L createElement(Class<L> interfaceCls) throws InstantiationException,
			IllegalAccessException {
		final L res;

		Class<L> instanceClass = getInstanceClass(interfaceCls);
		if (instanceClass != null) {
			res = instanceClass.newInstance();
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
	 * @param instanceCls
	 *            the instance {@link Class}
	 * @param <L>
	 *            the {@link IBaseElement} kind
	 */
	public <L extends IBaseElement> void addClassInstance(Class<L> interfaceCls,
			Class<? extends L> instanceCls) {
		classMapping.put(interfaceCls, instanceCls);
	}

	/**
	 * Removes the instance {@link Class} of the given interface {@link Class}.
	 * 
	 * @param interfaceCls
	 *            the interface {@link Class}
	 */
	public void removeClassInstance(Class<?> interfaceCls) {
		classMapping.remove(interfaceCls);
	}

}
