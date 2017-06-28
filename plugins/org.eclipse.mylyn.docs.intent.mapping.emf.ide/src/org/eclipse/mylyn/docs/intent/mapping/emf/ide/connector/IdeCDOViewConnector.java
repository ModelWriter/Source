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

import org.eclipse.emf.cdo.eresource.CDOResourceNode;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.CDOViewConnector;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;

/**
 * Ide {@link CDOViewConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeCDOViewConnector extends CDOViewConnector {

	@Override
	protected Object adapt(Object element) {
		final Object res;

		final CDOResourceNode adaptedElement = IdeMappingUtils.adapt(element, CDOResourceNode.class);
		if (adaptedElement != null) {
			res = adaptedElement;
		} else {
			res = super.adapt(element);
		}

		return res;
	}

}
