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
package org.eclipse.mylyn.docs.intent.mapping.jena.ide.connector;

import org.apache.jena.rdf.model.Resource;
import org.eclipse.core.resources.IFile;
import org.eclipse.mylyn.docs.intent.mapping.connector.IContainerProvider;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;

/**
 * {@link Resource} -> {@link IFile}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ResourceContainerProvider implements IContainerProvider {

	@Override
	public Object getContainer(Object element) {
		final Object res;

		if (element instanceof Resource) {
			res = IdeMappingUtils.adapt(element, IFile.class);
		} else {
			res = null;
		}

		return res;
	}

}
