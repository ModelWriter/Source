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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.mylyn.docs.intent.mapping.ide.connector.IFileDelegateRegistry;
import org.eclipse.mylyn.docs.intent.mapping.ide.internal.connector.FileDelegateRegistry;

/**
 * Ide mapping utilities.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public final class IdeMappingUtils {

	/**
	 * The {@link IFileDelegateRegistry} instance.
	 */
	private static final IFileDelegateRegistry REGISTRY = new FileDelegateRegistry();

	/**
	 * Constructor.
	 */
	private IdeMappingUtils() {
		// nothing to do here
	}

	/**
	 * Gets the {@link IFileDelegateRegistry}.
	 * 
	 * @return the {@link IFileDelegateRegistry}
	 */
	public static IFileDelegateRegistry getFileConectorDelegateRegistry() {
		return REGISTRY;
	}

	/**
	 * Adapts the given {@link Object element} into an {@link Object} of the given {@link Class}.
	 * 
	 * @param element
	 *            the {@link Object element}
	 * @param cls
	 *            the {@link Class}
	 * @return the {@link Object} of the given {@link Class} if any, <code>null</code> otherwise
	 */
	public static Object adapt(Object element, Class<?> cls) {
		final Object res;

		if (cls.isInstance(element)) {
			res = element;
		} else if (element instanceof IAdaptable) {
			final Object adaptedElement = ((IAdaptable)element).getAdapter(cls);
			if (adaptedElement != null) {
				res = adaptedElement;
			} else {
				res = Platform.getAdapterManager().getAdapter(element, cls);
			}
		} else {
			res = Platform.getAdapterManager().getAdapter(element, cls);
		}

		return res;
	}

}
