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

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.mylyn.docs.intent.mapping.MappingUtils;
import org.eclipse.mylyn.docs.intent.mapping.base.ILocationDescriptor;
import org.eclipse.mylyn.docs.intent.mapping.ide.IdeMappingUtils;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;

/**
 * Tests if the {@link DSemanticDecorator}'s {@link DSemanticDecorator#getTarget() semantic element} can be
 * located.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SemanticElementIsLocationPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		final boolean res;

		if (receiver instanceof DSemanticDecorator) {
			final EObject element = ((DSemanticDecorator)receiver).getTarget();
			if (IdeMappingUtils.getCurrentBase() != null) {
				final ILocationDescriptor locationDescriptor = MappingUtils.getConnectorRegistry()
						.getLocationDescriptor(null, element);
				if (locationDescriptor != null) {
					locationDescriptor.dispose();
					res = true;
				} else {
					res = false;
				}
			} else {
				res = false;
			}
		} else {
			res = false;
		}

		return res;
	}
}
