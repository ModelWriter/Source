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
package org.eclipse.mylyn.docs.intent.mapping.emf.connector;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider;

/**
 * {@link EObject} -> {@link CDOResource} -> {@link CDOView}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class CDOContainerProvider implements IContainerProvider {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider#getContainer(java.lang.Object)
	 */
	public Object getContainer(Object element) {
		final Object res;

		if (element instanceof CDOResource) {
			res = ((CDOResource)element).cdoView();
		} else if (element instanceof EObject) {
			final Resource resource = ((EObject)element).eResource();
			if (resource instanceof CDOResource) {
				res = resource;
			} else {
				res = null;
			}
		} else {
			res = null;
		}

		return res;
	}

}
