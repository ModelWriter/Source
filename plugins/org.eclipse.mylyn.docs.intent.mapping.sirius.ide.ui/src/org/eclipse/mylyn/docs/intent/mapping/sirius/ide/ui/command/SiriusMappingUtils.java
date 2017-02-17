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
package org.eclipse.mylyn.docs.intent.mapping.sirius.ide.ui.command;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;

/**
 * Utility class for Sirius mapping.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public final class SiriusMappingUtils {

	/**
	 * Constructor.
	 */
	private SiriusMappingUtils() {
		// nothing to do here
	}

	/**
	 * Gets the {@link DSemanticDecorator#getTarget() semantic element} from the given selected {@link Object}
	 * .
	 * 
	 * @param selected
	 *            the selected {@link Object}
	 * @return the {@link DSemanticDecorator#getTarget() semantic element} from the given selected
	 *         {@link Object} if any, <code>null</code> otherwise
	 */
	public static Object getSemanticElementFromSelectedObject(Object selected) {
		final Object res;

		final EObject eObject = IdeMappingUtils.adapt(selected, EObject.class);
		if (eObject instanceof DSemanticDecorator) {
			res = ((DSemanticDecorator)eObject).getTarget();
		} else {
			res = null;
		}
		return res;
	}

}
