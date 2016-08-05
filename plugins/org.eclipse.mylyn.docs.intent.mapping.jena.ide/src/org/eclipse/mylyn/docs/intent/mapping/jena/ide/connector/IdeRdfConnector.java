/*******************************************************************************
 * Copyright (c) 2016 Obeo.
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

import com.hp.hpl.jena.rdf.model.Resource;

import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.jena.connector.RdfConnector;

/**
 * Ide {@link RdfConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeRdfConnector extends RdfConnector {

	@Override
	protected Object adapt(Object element) {
		final Object res;

		final Resource adaptedElement = IdeMappingUtils.adapt(element, Resource.class);
		if (adaptedElement != null) {
			res = adaptedElement;
		} else {
			res = super.adapt(element);
		}

		return res;
	}

}
