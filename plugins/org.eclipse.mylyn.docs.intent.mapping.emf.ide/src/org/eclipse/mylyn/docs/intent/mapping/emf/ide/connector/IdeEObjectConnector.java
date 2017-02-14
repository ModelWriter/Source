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
package org.eclipse.mylyn.docs.intent.mapping.emf.ide.connector;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.mylyn.docs.intent.mapping.emf.connector.EObjectConnector;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;

/**
 * Ide implementation of {@link EObjectConnector}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class IdeEObjectConnector extends EObjectConnector {

	@Override
	protected Object adapt(Object element) {
		final Object res;

		final EObject eObject = IdeMappingUtils.adapt(element, EObject.class);
		if (eObject != null) {
			res = eObject;
		} else {
			final Setting setting = IdeMappingUtils.adapt(element, Setting.class);
			if (setting != null) {
				res = setting;
			} else {
				res = super.adapt(element);
			}
		}

		return res;
	}

}
