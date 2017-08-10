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
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.connector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider;

/**
 * {@link EObject} -> {@link IFile}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class EObjectContainerProvider implements IContainerProvider {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider#getContainer(java.lang.Object)
	 */
	public Object getContainer(Object element) {
		final Object res;

		if (element instanceof EObject && ((EObject)element).eResource() != null && ((EObject)element)
				.eResource().getURI() != null) {
			final URI uri = ((EObject)element).eResource().getURI();
			if (uri.isPlatformResource()) {
				final String path = ((EObject)element).eResource().getURI().toString().substring(
						"platform:/resource".length());
				res = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
			} else {
				res = null;
			}
		} else {
			res = null;
		}

		return res;
	}

}
