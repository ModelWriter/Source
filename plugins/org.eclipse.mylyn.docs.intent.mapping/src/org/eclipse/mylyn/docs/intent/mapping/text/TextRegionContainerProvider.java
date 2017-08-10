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
package org.eclipse.mylyn.docs.intent.mapping.text;

import org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider;

/**
 * {@link TextRegion} -> {@link TextRegion#getContainer() text region container}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TextRegionContainerProvider implements IContainerProvider {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider#getContainer(java.lang.Object)
	 */
	public Object getContainer(Object element) {
		final Object res;

		if (element instanceof TextRegion) {
			res = ((TextRegion)element).getContainer();
		} else {
			res = null;
		}

		return res;
	}

}
